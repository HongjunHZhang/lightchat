package com.zhj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.entity.FriendMsg;
import com.zhj.entity.GroupMsg;
import com.zhj.entity.ReportInfo;
import com.zhj.entity.UserCount;
import com.zhj.entity.dic.DefaultSrc;
import com.zhj.entity.ret.RetReportInfo;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;
import com.zhj.mapper.FriendMsgMapper;
import com.zhj.mapper.GroupMsgMapper;
import com.zhj.mapper.ReportInfoMapper;
import com.zhj.mapper.UserCountMapper;
import com.zhj.service.IReportService;
import com.zhj.util.TimeUtil;
import com.zhj.util.cache.CacheRoom;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * IReportServiceImpl
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/12/22 11:57
 */
@Service
public class IReportServiceImpl extends ServiceImpl<ReportInfoMapper, ReportInfo> implements IReportService {
    @Resource
    ReportInfoMapper reportInfoMapper;
    @Resource
    FriendMsgMapper friendMsgMapper;
    @Resource
    GroupMsgMapper groupMsgMapper;
    @Resource
    UserCountMapper userCountMapper;



    @Override
    public String reportUser(ReportInfo reportInfo) {
        fillReportInfo(reportInfo);
        reportInfoMapper.insert(reportInfo);
        return "举报成功，管理员正在对该消息进行验证，发现用户违规将进行封禁处理";
    }

    @Override
    public List<RetReportInfo> getReportUser() {
        if (UserContext.checkIsNotAdmin()){
            throw CustomException.createCustomException(ExceptionCodeEnum.ADMIN_ROLE_ERROR);
        }
        List<ReportInfo> processedList = reportInfoMapper.selectList(new QueryWrapper<ReportInfo>().eq("processed", "1"));
        List<RetReportInfo> retReportInfoList = new ArrayList<>();
        processedList.forEach(t->{
            RetReportInfo retReportInfo = new RetReportInfo();
            retReportInfo.setReportId(t.getId());
            retReportInfo.setReportReason(t.getReportReason());
            if ("0".equals(t.getMsgType())){
                GroupMsg groupMsg = groupMsgMapper.selectOne(new QueryWrapper<GroupMsg>().eq("id", t.getMsgId()));
                retReportInfo.setMsg(groupMsg.getMsg());
                retReportInfo.setType(groupMsg.getType());
            }else {
                FriendMsg friendMsg = friendMsgMapper.selectOne(new QueryWrapper<FriendMsg>().eq("id", t.getMsgId()));
                retReportInfo.setMsg(friendMsg.getMsg());
                retReportInfo.setType(friendMsg.getType());
            }
            if (t.getUserId() == null){
                retReportInfo.setUserId(null);
                retReportInfo.setUserNickName("匿名用户");
                retReportInfo.setUserPhoto(DefaultSrc.getRootUrlPath()+"/admin/userphoto/userphoto6.jpg");
            }else {
                UserCount userCountSelect = CacheRoom.getUserCountOfCount(t.getUserId());
                retReportInfo.setUserId(userCountSelect.getUserCount());
                retReportInfo.setUserNickName(userCountSelect.getNickName());
                retReportInfo.setUserPhoto(userCountSelect.getPhoto());
            }
            UserCount userCountSecond = CacheRoom.getUserCountOfCount(t.getBeReportId());
            retReportInfo.setBeReportId(userCountSecond.getUserCount());
            retReportInfo.setBeReportPhoto(userCountSecond.getPhoto());
            retReportInfo.setBeReportNickName(userCountSecond.getNickName());
            retReportInfoList.add(retReportInfo);
        });
        return retReportInfoList;
    }


    @Override
    public String banUser(String json) {
        if (UserContext.checkIsNotAdmin()){
            throw CustomException.createCustomException(ExceptionCodeEnum.ADMIN_ROLE_ERROR);
        }
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String tempUser = map.get("userCount");
        if (tempUser != null){
            UserCount userCount = CacheRoom.getUserCountOfCount(tempUser);
            userCount.setSafeLevel("-1");
            userCountMapper.update(userCount,new QueryWrapper<UserCount>().eq("id",userCount.getId()));
        }

        reportInfoMapper.changeIsValid(Integer.parseInt(map.get("reportId")));
        return tempUser==null?"忽略申请成功":"封禁帐号成功";
    }

    @Override
    public List<UserCount> getBanUser() {
        if (UserContext.checkIsNotAdmin()){
            throw CustomException.createCustomException(ExceptionCodeEnum.ADMIN_ROLE_ERROR);
        }
        return userCountMapper.selectList(new QueryWrapper<UserCount>().eq("safe_level", "-1"));
    }


    @Override
    public String unBanUser(Integer id) {
        if (UserContext.checkIsNotAdmin()){
            throw CustomException.createCustomException(ExceptionCodeEnum.ADMIN_ROLE_ERROR);
        }
        UserCount userCount = userCountMapper.selectOne(new QueryWrapper<UserCount>().eq("id",id));
        userCount.setSafeLevel("1");
        userCountMapper.update(userCount,new QueryWrapper<UserCount>().eq("id",userCount.getId()));
        return "解禁用户成功";
    }

    public void fillReportInfo(ReportInfo reportInfo){
        reportInfo.setCreateTime(TimeUtil.getSimpleTime());
        reportInfo.setProcessed("1");
        reportInfo.setIsValid("1");
    }

}
