package com.zhj.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhj.entity.*;
import com.zhj.entity.dic.DefaultSrc;
import com.zhj.entity.result.DataResult;
import com.zhj.entity.ret.PageMine;
import com.zhj.entity.ret.RetFriendMsg;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;
import com.zhj.mapper.*;
import com.zhj.service.IFriendMsgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.util.EmojiUtils;
import com.zhj.util.FileUtil;
import com.zhj.util.SimpleUtil;
import com.zhj.util.TimeUtil;
import com.zhj.util.cache.CacheRoom;
import com.zhj.websocket.WebSocket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhj
 * @since 2022-09-28
 */
@Service
public class FriendMsgServiceImpl extends ServiceImpl<FriendMsgMapper, FriendMsg> implements IFriendMsgService {

    @Resource
    UserCountMapper userCountMapper;
    @Resource
    RefuseCallMapper refuseCallMapper;
    @Resource
    FriendMapper friendMapper;
    @Resource
    GroupMemberMapper groupMemberMapper;
    @Resource
    FriendMsgMapper friendMsgMapper;
    @Resource
    FriendMsgTempMapper friendMsgTempMapper;

    @Override
    public DataResult getDefaultMsg(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String friendCount = map.get("friendCount");
        if (friendCount == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.REQUEST_PARAM_ERROR);
        }
        String userCount = UserContext.getUserCount();
        RetFriendMsg retFriendMsg = new RetFriendMsg();
        UserCount userCountRet = CacheRoom.getUserCountOfCount(userCount);
        UserCount friendCountRet = CacheRoom.getUserCountOfCount(friendCount);
        PageHelper.startPage(Integer.parseInt(map.get("pageNum")),Integer.parseInt(map.get("pageSize")));
        List<FriendMsg> friendMsgList = friendMsgMapper.getFriendMsg(SimpleUtil.combineCountOfFriend(userCount, friendCount));
        friendMsgList.forEach(t->{
            UserCount sendUser = CacheRoom.getUserCountOfCount(t.getSendId());
            t.setSendNickName(sendUser.getNickName());
            t.setCountPhoto(sendUser.getPhoto());
            UserCount receiveUser = CacheRoom.getUserCountOfCount(t.getReceiveId());
            t.setReceiveNickName(receiveUser.getNickName());
            t.setReceiveCountPhoto(receiveUser.getPhoto());
        });
        friendMsgMapper.signRead(userCount,friendCount,TimeUtil.getSimpleTime());
        PageInfo<FriendMsg> friendMsgPageInfo = new PageInfo<>(friendMsgList);
        friendMsgList.sort(Comparator.comparing(FriendMsg::getCreateTime));
        String simpleTime = TimeUtil.getZeroSimpleTime();
        long lastTime = 0L;
        for (FriendMsg friendMsg : friendMsgList) {
            friendMsg.setMsg(EmojiUtils.emojiRecovery2(friendMsg.getMsg()));
            String tempMsg = friendMsg.getCreateTime();
            friendMsg.setCreateTime(TimeUtil.getDayTime(friendMsg.getCreateTime(),simpleTime,lastTime));
            lastTime = Long.parseLong(tempMsg);
        }
        retFriendMsg.setUserCount(userCountRet);
        retFriendMsg.setFriendCount(friendCountRet);
        retFriendMsg.setFriendMsg(friendMsgList);
        retFriendMsg.setPageMine(new PageMine(friendMsgPageInfo.getPages(),friendMsgPageInfo.getPageSize()));
        return DataResult.successOfData(retFriendMsg);
    }

    @Override
    public boolean sendMsg(MultipartFile file, String defaultInfo, String viaGroup, String bsId) throws IOException {
            if (file != null){
                UserContext.checkIsTourist();
            }
        String userCount = UserContext.getUserCount();
        FriendMsg friendMsg = JSONObject.parseObject(defaultInfo, FriendMsg.class);
        if (viaGroup == null){
            List<Friend> friendList = friendMapper.selectList(new QueryWrapper<Friend>().eq("user_id", friendMsg.getReceiveId()).eq("friend_id", friendMsg.getSendId()));
            if (friendList.size() == 0){
                throw CustomException.createCustomException(ExceptionCodeEnum.USER_IS_NOT_FRIEND);
            }
        }else {
            GroupMember memberSend = groupMemberMapper.selectOne(new QueryWrapper<GroupMember>().eq("group_count",viaGroup).eq("user_count",friendMsg.getSendId()));
            GroupMember memberReceived = groupMemberMapper.selectOne(new QueryWrapper<GroupMember>().eq("group_count",viaGroup).eq("user_count",friendMsg.getReceiveId()));
            if (memberSend == null ){
                throw CustomException.createCustomException("你不是群("+viaGroup+")的成员，不能通过该群向目标发送消息");
            }
            if( memberReceived == null){
                throw CustomException.createCustomException("账号"+friendMsg.getReceiveId()+"不是群("+viaGroup+")的成员，不能通过该群向目标发送消息");
            }
        }

        FriendMsg friendMsgRet = fillFriendMsg(friendMsg);
        if ("1".equals(friendMsgRet.getType())){
            String fileName = userCount+"-"+friendMsgRet.getCreateTime()+".mp3";
            String path = DefaultSrc.getRootUserFile()+"/"+userCount;
            String actualName = FileUtil.saveFile(file,fileName,path);
            friendMsgRet.setMsg(DefaultSrc.getUserPhotoSuffix()+"/"+userCount+"/"+actualName);
        }else if(!"0".equals(friendMsgRet.getType())) {
            String path = DefaultSrc.getRootUserFile()+"/"+userCount;
            if (file == null){
                throw CustomException.createCustomException(ExceptionCodeEnum.SYSTEM_FILE_READ_EXCEPTION);
            }
            String fileName = file.getOriginalFilename();
            String actualName = FileUtil.saveFile(file,fileName,path);
            friendMsgRet.setMsg(DefaultSrc.getUserPhotoSuffix()+"/"+userCount+"/"+actualName);
            friendMsgRet.setFileName(actualName);
        }
            sendMsg(friendMsgRet,bsId);
        return true;
    }

    @Override
    public void refuseCall() {
        String userCount = UserContext.getUserCount();
        RefuseCall userSelect = refuseCallMapper.selectOne(new QueryWrapper<RefuseCall>().eq("refuse_user_id", userCount));
        if (userSelect == null){
            RefuseCall refuseCall = new RefuseCall();
            refuseCall.setRefuseUserId(userCount);
            refuseCallMapper.insert(refuseCall);
        }else {
            refuseCallMapper.delete(new QueryWrapper<RefuseCall>().eq("id", userSelect.getId()));
        }
    }

    public void putFriendMsgToTemp(FriendMsg friendMsg){
        Integer ret = friendMsgTempMapper.searchRecordByTwoCount(friendMsg);
        FriendMsgTemp friendMsgTemp = new FriendMsgTemp(friendMsg);
        if (ret == null || ret == 0){
            friendMsgTemp.setUnRead(1);
            friendMsgTempMapper.insert(friendMsgTemp);
            return;
        }
        saveInfoAfterSendByTemp(friendMsgTemp);
    }

    public void saveInfoAfterSendByTemp(FriendMsgTemp friendMsgTemp){
        friendMsgTempMapper.changeRecord(friendMsgTemp);
        friendMsgTempMapper.signReadOfFriend(friendMsgTemp);
    }

    public FriendMsg fillFriendMsg(FriendMsg friendMsg)  {
        friendMsg.setCreateTime(TimeUtil.getSimpleTime());
        friendMsg.setCountType("0");
        friendMsg.setIsValid("1");
        friendMsg.setCountIdentify(SimpleUtil.combineCountOfFriend(friendMsg.getSendId(),friendMsg.getReceiveId()));
        return friendMsg;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
   public void modifyCount(){
        List<FriendMsg> friendMsgList = friendMsgMapper.selectList(new QueryWrapper<>());
        for (FriendMsg friendMsg : friendMsgList) {
            friendMsg.setCountIdentify(SimpleUtil.combineCountOfFriend(friendMsg.getSendId(),friendMsg.getReceiveId()));
            friendMsgMapper.update(friendMsg,new QueryWrapper<FriendMsg>().lambda().eq(FriendMsg::getId,friendMsg.getId()));
        }
    }

    public void sendMsg(FriendMsg friendMsg,String bsId){
        friendMsg.setMsg(EmojiUtils.emojiConvert(friendMsg.getMsg()));
        friendMsgMapper.insert(friendMsg);
//        friendMsgMapper.signRead(userCount,friendMsg.getReceiveId(), TimeUtil.getSimpleTime());
        WebSocket.htmlReceiveMsg(friendMsg,bsId);
        putFriendMsgToTemp(friendMsg);
    }

}
