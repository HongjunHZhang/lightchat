package com.zhj.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhj.entity.*;
import com.zhj.entity.dic.DefaultSrc;
import com.zhj.entity.ret.PageMine;
import com.zhj.entity.ret.RetGroup;
import com.zhj.entity.ret.RetGroupMember;
import com.zhj.entity.ret.RetGroupMsg;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;
import com.zhj.mapper.*;
import com.zhj.service.IGroupMsgService;
import com.zhj.util.EmojiUtils;
import com.zhj.util.FileUtil;
import com.zhj.util.TimeUtil;
import com.zhj.util.cache.CacheRoom;
import com.zhj.websocket.WebSocket;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhj
 * @since 2022-10-09
 */
@Service
public class GroupMsgTempServiceImpl extends ServiceImpl<GroupMsgTempMapper, GroupMsgTemp> implements IGroupMsgService {
    @Resource
    UserCountMapper userCountMapper;
    @Resource
    GroupCountMapper groupCountMapper;
     @Resource
     GroupMemberMapper groupMemberMapper;
     @Resource
    GroupMsgMapper groupMsgMapper;
     @Resource
     GroupMsgTempMapper groupMsgTempMapper;


    @Override
    public RetGroupMsg getDefaultMsg(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String groupCount = map.get("groupCount");
        if (groupCount == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.GROUP_NOT_EXIST);
        }
        String userCount = UserContext.getUserCount();
        RetGroupMsg retGroupMsg = new RetGroupMsg();
        UserCount userCountRet = CacheRoom.getUserCountOfCount(userCount);
        GroupCount groupCountRet = groupCountMapper.selectOne(new QueryWrapper<GroupCount>().eq("group_count", groupCount));
        if (groupCountRet == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.GROUP_NOT_EXIST);
        }
        PageHelper.startPage(Integer.parseInt(map.get("pageNum")),Integer.parseInt(map.get("pageSize")));
        List<RetGroup> groupMsgList = groupMsgMapper.getGroupMsg(groupCount);
        groupMsgMapper.signRead(userCount,groupCount,TimeUtil.getSimpleTime());
        PageInfo<RetGroup> retGroupPageInfo = new PageInfo<>(groupMsgList);
        String simpleTime = TimeUtil.getZeroSimpleTime();
        long lastTime = 0L;
        groupMsgList.sort(Comparator.comparing(RetGroup::getCreateTime));
        for (RetGroup groupMsg : groupMsgList) {
            UserCount userInfo = CacheRoom.getUserCountOfCount(groupMsg.getSendId());
            groupMsg.setCountPhoto(userInfo.getPhoto());
            groupMsg.setSendNickName(userInfo.getNickName());
            groupMsg.setMsg(EmojiUtils.emojiRecovery2(groupMsg.getMsg()));
            String tempMsg = groupMsg.getCreateTime();
            groupMsg.setCreateTime(TimeUtil.getDayTime(groupMsg.getCreateTime(),simpleTime,lastTime));
            lastTime = Long.parseLong(tempMsg);
        }
        List<RetGroupMember> groupMemberRet = groupMemberMapper.getMember(groupCount);
        List<String> countList = groupMemberRet.stream().map(RetGroupMember::getUserCount).collect(Collectors.toList());
        long onlineNum = groupMemberRet.stream().filter(t -> ("1").equals(t.getOnline())).count();

        retGroupMsg.setRoleList(groupMemberRet);
        retGroupMsg.setGroupMsg(groupMsgList);
        retGroupMsg.setGroupCount(groupCountRet);
        retGroupMsg.setUserCount(userCountRet);
        retGroupMsg.setCountList(countList);
        retGroupMsg.setOnlineNum(Integer.parseInt(String.valueOf(onlineNum)));
        retGroupMsg.setAllNum(groupMemberRet.size());
        retGroupMsg.setPageMine(new PageMine(retGroupPageInfo.getPages(),10));
        return retGroupMsg;
    }

    @Override
    public RetGroupMsg getNextMsg(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String groupCount = map.get("groupCount");
        if (groupCount == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.GROUP_NOT_EXIST);
        }
        UserContext.getUserCount();
        RetGroupMsg retGroupMsg = new RetGroupMsg();
        PageHelper.startPage(Integer.parseInt(map.get("pageNum")),Integer.parseInt(map.get("pageSize")));
        List<RetGroup> groupMsgList = groupMsgMapper.getGroupMsg(groupCount);
        String simpleTime = TimeUtil.getZeroSimpleTime();
        long lastTime = 0L;
        groupMsgList.sort(Comparator.comparing(RetGroup::getCreateTime));
        for (RetGroup groupMsg : groupMsgList) {
            UserCount userInfo = CacheRoom.getUserCountOfCount(groupMsg.getSendId());
            groupMsg.setSendNickName(userInfo.getNickName());
            groupMsg.setCountPhoto(userInfo.getPhoto());
            groupMsg.setMsg(EmojiUtils.emojiRecovery2(groupMsg.getMsg()));
            String tempMsg = groupMsg.getCreateTime();
            groupMsg.setCreateTime(TimeUtil.getDayTime(groupMsg.getCreateTime(),simpleTime,lastTime));
            lastTime = Long.parseLong(tempMsg);
        }
        retGroupMsg.setGroupMsg(groupMsgList);
        return retGroupMsg;
    }

    @Override
      public boolean saveMsg(MultipartFile file,String defaultInfo,String bsId) throws IOException {
            if (file != null){
                UserContext.checkIsTourist();
            }
          String userCount = UserContext.getUserCount();
          GroupMsg groupMsg = JSONObject.parseObject(defaultInfo, GroupMsg.class);
          GroupMsg groupMsgRet = fillGroupMsg(groupMsg);
          GroupMember groupMember = groupMemberMapper.selectOne(new QueryWrapper<GroupMember>().eq("group_count", groupMsgRet.getGroupId()).eq("user_count", groupMsgRet.getSendId()));
          if (groupMember == null){
              throw CustomException.createCustomException(ExceptionCodeEnum.Group_IS_NOT_MEMBER);
          }
          if ("1".equals(groupMsgRet.getType())){
              String fileName = userCount+"-"+groupMsgRet.getCreateTime()+".mp3";
              String path = DefaultSrc.getRootGroupFile()+"/"+groupMsgRet.getGroupId()+"/"+userCount;
              String actualName = FileUtil.saveFile(file,fileName, path);
              groupMsgRet.setMsg(DefaultSrc.getGroupPhotoSuffix()+"/"+groupMsgRet.getGroupId()+"/"+userCount+"/"+actualName);
          }else if (!"0".equals(groupMsgRet.getType())){
              if (file == null){
                  throw CustomException.createCustomException(ExceptionCodeEnum.SYSTEM_FILE_READ_EXCEPTION);
              }
              String fileName = file.getOriginalFilename();
              String actualName = FileUtil.saveFile(file,fileName,DefaultSrc.getRootGroupFile()+"/"+groupMsgRet.getGroupId()+"/"+userCount);
              groupMsgRet.setMsg(DefaultSrc.getGroupPhotoSuffix()+"/"+groupMsgRet.getGroupId()+"/"+userCount+"/"+actualName);
              groupMsgRet.setFileName(actualName);
          }
          sendMsg(groupMsgRet,bsId);
          return true;
      }

      @Override
      public String withdrawGroupMsg(GroupMsg groupMsg){
          String userCount = UserContext.getUserCount();
          GroupMsg groupMsgSelect = groupMsgMapper.selectOne(new QueryWrapper<GroupMsg>().lambda().eq(GroupMsg::getId, groupMsg.getId()));
          GroupMember groupMember = groupMemberMapper.selectOne(new QueryWrapper<GroupMember>().lambda().eq(GroupMember::getGroupCount, groupMsgSelect.getGroupId()).eq(GroupMember::getUserCount, userCount).eq(GroupMember::getUserLevel, "0"));
          if (groupMember == null){
              throw CustomException.createCustomException(ExceptionCodeEnum.GROUP_HAS_NOT_ROLE_WITHDRAW);
          }

          groupMsgSelect.setIsValid("0");
          groupMsgMapper.update(groupMsgSelect,new QueryWrapper<GroupMsg>().lambda().eq(GroupMsg::getId,groupMsgSelect.getId()));
          GroupMsgTemp groupMsgTemp = new GroupMsgTemp(groupMsg);
          groupMsgTemp.setIsValid("1");
          groupMsgTemp.setMsg("该消息已被撤回");
          groupMsgTempMapper.update(groupMsgTemp,new QueryWrapper<GroupMsgTemp>().lambda().eq(GroupMsgTemp::getId,groupMsgTemp.getId()));
          return "成功";
      }

      public void putMsgToTemp(GroupMsg groupMsg){
          GroupMsgTemp groupMsgTemp = new GroupMsgTemp(groupMsg);
          groupMsgTempMapper.delete(new QueryWrapper<GroupMsgTemp>().lambda().eq(GroupMsgTemp::getGroupId,groupMsgTemp.getGroupId()));
          groupMsgTempMapper.insert(groupMsgTemp);
//          List<GroupMsgTemp> groupMsgTempList = groupMsgTempMapper.selectList(new QueryWrapper<GroupMsgTemp>().lambda().eq(GroupMsgTemp::getGroupId, groupMsg.getGroupId()));
//          if (groupMsgTempList.size() == 0){

//              return;
//          }
//          GroupMsgTemp groupMsgTempTemp = groupMsgTempList.get(0);
//          groupMsgTempTemp.setId(groupMsgTemp.getId());
//          groupMsgTempMapper.update(groupMsgTemp,new QueryWrapper<GroupMsgTemp>().lambda().eq(GroupMsgTemp::getGroupId,groupMsgTemp.getGroupId()));
      }


    public GroupMsg fillGroupMsg(GroupMsg groupMsg){
        groupMsg.setCreateTime(TimeUtil.getSimpleTime());
        groupMsg.setCountType("1");
        groupMsg.setIsValid("1");
        return groupMsg;
    }

    public void sendMsg(GroupMsg groupMsg, String bsId){
        groupMsg.setMsg(EmojiUtils.emojiConvert(groupMsg.getMsg()));
        groupMsgMapper.insert(groupMsg);
        WebSocket.htmlReceiveMsg(groupMsg,bsId);
        groupMsgMapper.signRead(UserContext.getUserCount(),groupMsg.getGroupId(), TimeUtil.getSimpleTime());
        putMsgToTemp(groupMsg);
    }

}
