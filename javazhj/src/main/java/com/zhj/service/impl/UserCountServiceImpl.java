package com.zhj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.zhj.entity.*;
import com.zhj.entity.dic.*;
import com.zhj.entity.login.TokenBody;
import com.zhj.entity.ret.*;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;
import com.zhj.mapper.*;
import com.zhj.service.IUserCountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.util.*;
import com.zhj.util.cache.CacheRoom;
import com.zhj.util.entry.JwtUtils;
import com.zhj.util.entry.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhj
 * @since 2021-11-17
 */
@Service
public class UserCountServiceImpl extends ServiceImpl<UserCountMapper, UserCount> implements IUserCountService {
    @Resource
    UserCountMapper userCountMapper;
    @Resource
    FriendMsgMapper friendMsgMapper;
    @Resource
    GroupMsgMapper groupMsgMapper;
    @Resource
    FriendMsgTempMapper friendMsgTempMapper;
    @Resource
    GroupMemberMapper groupMemberMapper;
    @Resource
    GroupMsgTempMapper groupMsgTempMapper;
    @Resource
    LoadMsgRecordMapper loadMsgRecordMapper;
    @Resource
    UserIdMapper userIdMapper;
    @Resource
    FriendMapper friendMapper;
    @Resource
    SpaceCountMapper spaceCountMapper;
    @Resource
    GroupCountMapper groupCountMapper;
    @Resource
    FriendMsgServiceImpl friendMsgService;
    @Resource
    GroupMsgTempServiceImpl groupMsgTempService;
    @Resource
    RedisUtil redisUtil;
    @Resource
    EmailUtil emailUtil;

    Pattern digitPattern = Pattern.compile("[^0-9]");
    private final Logger logger = LoggerFactory.getLogger(IUserCountService.class);

    @Override
    public void fillDefaultUser(UserCount userCount) {
       userCount.setIsValid("1");
       userCount.setSafeLevel("1");
       userCount.setRootLevel("1");
       userCount.setStatus("1");
       userCount.setOpen("1");
       userCount.setCreateTime(TimeUtil.getSimpleTime());
    }

    @Override
    public UserCount login(UserCount userCount, HttpServletResponse httpResponse) {
        System.out.println("进入登陆页面了哦");
        userCount.setPassword(Md5Util.createMd5ByPassword(userCount.getPassword()));
        Optional<UserCount> loginInfo = Optional.ofNullable(userCountMapper.getLoginInfo(userCount));
        if (!loginInfo.isPresent()){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_LOGIN_ERROR);
        }else {
            if ("-1".equals(loginInfo.get().getSafeLevel())){
                throw CustomException.createCustomException(ExceptionCodeEnum.USER_HAS_BAN);
            }

            if ("0".equals(loginInfo.get().getRootLevel())){
                throw CustomException.createCustomException(ExceptionCodeEnum.USER_HAS_NOT_LOGIN_ROLE);
            }

            if (!UserStatusDic.checkStatus(loginInfo.get().getStatus())){
                throw CustomException.createCustomException(UserStatusDic.getCodeReason(loginInfo.get().getStatus()));
            }

        }

        userCountMapper.online(userCount.getUserCount());
        CacheRoom.updateUserCountOfCount(userCount.getUserCount());
        httpResponse.setHeader("Authorization", JwtUtils.getJwtAuthorization(TokenBody.userCountToTokenBody(loginInfo.get())));
        httpResponse.setHeader("Access-Control-Expose-Headers","Authorization");
        return loginInfo.get();
    }

    @Override
    public UserCount touristLogin(HttpServletResponse httpResponse) {
        UserCount userCount = new UserCount();
        userCount.setUserCount("66666666");
        userCount.setPassword("12345678");
        return login(userCount,httpResponse);
    }

    @Override
    public boolean register(UserCountVerify userCountVerify) {
        UserCount userCount = UserCountVerify.parseToUserCount(userCountVerify);
        if (userCount.getPassword() == null || userCount.getPassword().length() <6){
            throw CustomException.createCustomException("密码不符合规范");
        }
        if (userCount.getUserCount() == null || userCount.getUserCount().length()<6){
            throw CustomException.createCustomException("账号不允许少于6位");
        }

        if (!checkDigit(userCount.getUserCount())){
            throw CustomException.createCustomException("账号中不能有数字以外的字符");
        }

        if (userCountVerify.getEmail() != null &&(userCountVerify.getVerifyCode() == null || !userCountVerify.getVerifyCode().equals(redisUtil.getString(RedisSuffix.REGISTER.getMsg() +userCountVerify.getEmail())) )  ){
            throw CustomException.createCustomException("验证码错误");
        }

        List<UserCount> userCountList = userCountMapper.selectList(new QueryWrapper<UserCount>().eq("user_count", userCount.getUserCount()));
        userCountList = Optional.ofNullable(userCountList).orElse(new ArrayList<>());
        if (userCountList.size()>0){
            throw CustomException.createCustomException("帐号已经被注册了,请使用一个新的帐号注册");
        }

        UserId userId = new UserId(userCount.getUserCount(),userCount.getPassword());
        userIdMapper.insert(userId);
        String encrypt = AES.encrypt(userCount.getPassword());
        userCount.setPassword(encrypt);
        userCount.setPhoto(DefaultSrc.getPhotoSuffix()+userCount.getPhoto()+".jpg");
        userCount.setOnline("0");
        fillDefaultUser(userCount);
        userCountMapper.insert(userCount);
        addDefaultFriend(userCount.getUserCount());
        createSpaceCount(userCount.getUserCount(),userCount.getId());
        return true;
    }

    @Override
    public String emailVerifyCode(String json) throws Exception {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String email = map.get("email");
        if (redisUtil.getString(RedisSuffix.REGISTER.getMsg() + email) != null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_EMAIL_EXIST_VERIFICATION);
        }
        ThreadPoolUtil.execute(() -> {
            try {
                emailUtil.sendRegisterEmail(new String[]{email});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return "请在邮箱中查看您的验证消息";
    }

    @Override
    public void outLine() {
        userCountMapper.outline(UserContext.getUserCount());
        CacheRoom.updateUserCountOfCount(UserContext.getUserCount());
    }

    @Override
    public UserCount getUserInfoByUserCount(String userCount) {
        QueryWrapper<UserCount> queryWrapper = new QueryWrapper<UserCount>().eq("user_count", userCount)
                .eq("is_valid", "1")
                .eq("status", "1")
                .eq("safe_level", "1");
        List<UserCount> userCountList = userCountMapper.selectList(queryWrapper);
        return userCountList.get(0);
    }

    @Override
    public List<FriendMsgCurrent> loadHistoryMsg() {
        String userCount = UserContext.getUserCount();
        List<FriendMsgCurrent> mainMsg = friendMsgMapper.getMainMsg(userCount);
        mainMsg.forEach(t ->{
            String suffix = userCount.equals(t.getSendId()) ? "" : CacheRoom.getUserCountOfCount(t.getSendId()).getNickName() + " : ";
            if ("0".equals(t.getType())) {
                t.setMsg(EmojiUtils.emojiRecovery2(suffix+t.getMsg()));
            } else {
                t.setMsg(suffix + CodeFileType.parseCode(t.getType()));
            }
            Integer unRead = friendMsgMapper.getUnReadFriendMsg(userCount,t.getSendId());
            t.setCountType("0");
            t.setUnRead(unRead > 99?99:unRead);
        });
        List<FriendMsgCurrent> mainGroupMsg = friendMsgMapper.getMainGroupMsg(userCount);
        mainGroupMsg.forEach(t->{
            String nickName = CacheRoom.getUserCountOfCount(t.getSendId()).getNickName();
            t.setSendId(t.getGroupId());
            if ("0".equals(t.getType())) {
                t.setMsg(EmojiUtils.emojiRecovery2(nickName + " : " + t.getMsg()));
            }else {
                t.setMsg(nickName + " : "+CodeFileType.parseCode(t.getType()));
            }
            Integer unRead = groupMsgMapper.getUnReadFriendMsg(userCount,t.getSendId());
            t.setCountType("1");
            t.setUnRead(unRead > 99?99:unRead);
        });                //主要用于区分群消息和好友消息
        mainMsg.addAll(mainGroupMsg);
        mainMsg.sort((t1,t2)->t2.getCreateTime().compareTo(t1.getCreateTime()));
        return mainMsg;
    }

    @Override
    public RetFriendAndGroup getFriendAndGroup() {
        TokenBody tokenBody = UserContext.get();
        String userCount = tokenBody.getName();
        List<UserType> friendList = userCountMapper.getFriend(userCount);
        RetFriendAndGroup retFriendAndGroup = new RetFriendAndGroup();
        Map<String,String> retMap = new HashMap<>();
        friendList.forEach(t->retMap.put(t.getUserCount(),t.getShip()));
        Map<String, List<UserType>> collect = friendList.stream().collect(Collectors.groupingBy(UserType::getShipType));
        List<UserCountType> retUserCountType = new ArrayList<>();
        for (Map.Entry<String, List<UserType>> stringListEntry : collect.entrySet()) {
            UserCountType userCountType = new UserCountType();
            List<UserCount> userCountList = userCountMapper.selectList(new QueryWrapper<UserCount>().in("user_count", stringListEntry.getValue().stream().
                    map(UserType::getUserCount).collect(Collectors.toList())));
            userCountList.sort((t1,t2)->t2.getOnline().compareTo(t1.getOnline()));
            long count = userCountList.stream().filter(t -> "1".equals(t.getOnline())).count();
            List<RetUserCount> userCountRet = new ArrayList<>();
            for (UserCount userCountTemp : userCountList) {
                RetUserCount retUserCount = new RetUserCount();
                retUserCount.setUserCount(userCountTemp);
                retUserCount.setShip(retMap.get(userCountTemp.getUserCount()));
                userCountRet.add(retUserCount);
            }
            userCountType.setUserCount(userCountRet);
            userCountType.setShipType(stringListEntry.getKey());
            userCountType.setOnline(count);
            retUserCountType.add(userCountType);
        }
        retFriendAndGroup.setUserCountList(retUserCountType);

        List<RetGroupType> retGroupCountType = new ArrayList<>();
        List<UserType> groupList = userCountMapper.getGroup(userCount);
        Map<String, List<UserType>> retGroupType = groupList.stream().collect(Collectors.groupingBy(UserType::getShipType));

        for (Map.Entry<String, List<UserType>> stringListEntry : retGroupType.entrySet()) {
            RetGroupType retGroupTypeList = new RetGroupType();
            List<GroupCount> groupCountList = groupCountMapper.selectList(new QueryWrapper<GroupCount>().in("group_count", stringListEntry.getValue().stream().
                    map(UserType::getUserCount).collect(Collectors.toList())));
            retGroupTypeList.setShipType(CodeGroup.parseCode(stringListEntry.getKey()));
            retGroupTypeList.setGroupCount(groupCountList);
            retGroupCountType.add(retGroupTypeList);
        }
        retFriendAndGroup.setGroupCountList(retGroupCountType);

        return retFriendAndGroup;
    }


    @Override
    public UserCount getFriendInfo(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String friendCount = map.get("friendCount");
        UserCount userCount = CacheRoom.getUserCountOfCount(friendCount);
        if (userCount == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_NOT_EXIST);
        }
        return userCount;
    }

    @Override
    public String updateUserInfo(MultipartFile photo, String json) {
        UserContext.checkIsTourist();
        String userCount = UserContext.getUserCount();
        JSONObject jsonObject = JSONObject.parseObject(json);
        UserCount userInfo = JSONObject.toJavaObject(jsonObject, UserCount.class);
        if (!userInfo.getUserCount().equals(userCount)){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_INFO_VALID);
        }
        UserCount userCountSelect = CacheRoom.getUserCountOfCount(userCount);

        if (photo != null){
            userCountSelect.setPhoto(DefaultSrc.getUserPhotoSuffix()+"/"+userCount+"/"+userCount+".jpg");
            try {
                String actualName = FileUtil.saveFileAndCompress(photo,userCount+".jpg",DefaultSrc.getRootUserFile()+"/"+userCount);
                userCountSelect.setPhoto(actualName);
            }catch (Exception e){
                logger.error("用户"+userCount+"上传头像失败");
            }

        }
        fillUserCount(userCountSelect,userInfo);
        userCountMapper.update(userCountSelect,new QueryWrapper<UserCount>().eq("id",userCountSelect.getId()));
        CacheRoom.updateUserCountOfCount(userCountSelect.getUserCount());
        return "更新个人信息成功";
    }

    @Override
    public String updatePassword(String json) {
        UserContext.checkIsTourist();
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String userCount = UserContext.getUserCount();
        if (!userCount.equals(map.get("userCount"))){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_INFO_VALID);
        }
        UserCount userCountSelect = userCountMapper.selectOne(new QueryWrapper<UserCount>().eq("user_count", userCount)
                .eq("password",Md5Util.createMd5ByPassword(map.get("oldPassword"))));
        if (userCountSelect==null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_PASSWORD_ERROR);
        }
        if (map.get("newPassword").length()<6){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_PASSWORD_TO_SIMPLE);
        }
        if (map.get("newPassword").equals(map.get("oldPassword"))){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_PASSWORD_SAME_OF_OLD);
        }
        userCountSelect.setPassword(Md5Util.createMd5ByPassword(map.get("newPassword")));
        userCountMapper.update(userCountSelect,new QueryWrapper<UserCount>().eq("id",userCountSelect.getId()));
        return "修改密码成功";
    }

    @Override
    public int resetAllUserPassword(String password) {

        if (  UserContext.checkIsNotAdmin() ){
            throw CustomException.createCustomException(ExceptionCodeEnum.ADMIN_ROLE_ERROR);
        }

       return userCountMapper.resetAllPassword(Md5Util.createMd5ByPassword(password));
    }


    @Override
    public String submitNewShip(String shipType) {
        String userCount = UserContext.getUserCount();
        if (shipType == null || "".equals(shipType)){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_GROUP_NEED_NAME);
        }
        long count = friendMapper.selectCount(new QueryWrapper<Friend>().eq("user_id", userCount).eq("ship_type", shipType));
        if (count > 0){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_GROUP_EXIST);
        }
        String simpleTime = TimeUtil.getSimpleTime();
        Friend friend = new Friend();
        friend.setFriendId(userCount);
        friend.setUserId(userCount);
        friend.setCreateTime(simpleTime);
        friend.setShip("0");
        friend.setShipType(shipType);
        friend.setLastReadTime(simpleTime);
        friendMapper.insert(friend);
        return "新建分组成功";
    }

    @Override
    public String updateShipType(String json) {
        String userCount = UserContext.getUserCount();
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String shipType = map.get("shipType");
        Friend friendRet = friendMapper.selectOne(new QueryWrapper<Friend>().eq("user_id", userCount).eq("friend_id", map.get("friendCount")).eq("ship_type",shipType));
        if (friendRet != null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_GROUP_EXIST_USER_COUNT);
        }
        Friend friend = friendMapper.selectOne(new QueryWrapper<Friend>().eq("user_id", userCount).eq("friend_id", map.get("friendCount")).eq("ship_type",map.get("oldShipType")));
        friend.setShipType(shipType);
        friendMapper.update(friend,new QueryWrapper<Friend>().eq("id",friend.getId()));
        return "移动好友分组成功";
    }

    @Override
    public List<FriendMsgTemp> getNewMsg() {
        String userCount = UserContext.getUserCount();
        List<FriendMsgTemp> messageList = friendMsgTempMapper.getNewRecord(userCount);
//        List<Integer> idList = messageList.stream().filter(t->!userCount.equals(t.getSendId())).map(FriendMsgTemp::getId).collect(Collectors.toList());
//        List<List<Integer>> partitionList = Lists.partition(idList, 500);
//         // 将已查询消息标记为0
//        for (List<Integer> list : partitionList) {
//            if (list.size() > 0){
//                friendMsgTempMapper.signRead(list);
//            }
//        }
          friendMsgTempMapper.signReadByUser(userCount);
          friendMsgTempMapper.signReadByUserOfMine(userCount);


        if (messageList.size() > 0){
            messageList.forEach(t->{
                String showCount = userCount.equals(t.getSendId()) ? t.getReceiveId() : t.getSendId();
                // 如果自己是发送方，则应该标记unRead为0
                if (userCount.equals(t.getSendId())){
                    t.setUnRead(0);
                }
                UserCount userInfo = CacheRoom.getUserCountOfCount(showCount);
                t.setSendNickName(userInfo.getNickName());
                t.setCountPhoto(userInfo.getPhoto());
                parseMessage(t,userCount);
                t.setSendId(showCount);
            });
        }

        List<String> groupCountList = groupMemberMapper.selectList(new QueryWrapper<GroupMember>().eq("user_count", userCount)).stream().map(GroupMember::getGroupCount).collect(Collectors.toList());
        List<List<String>> groupPartition = Lists.partition(groupCountList, 500);
        List<LoadMsgRecord> recordList = loadMsgRecordMapper.selectList(new QueryWrapper<LoadMsgRecord>().lambda().eq(LoadMsgRecord::getUserCount, userCount));
        String formatDate = "0";
        String readTime = TimeUtil.getSimpleTime();
        if (recordList.size() > 0){
            formatDate = recordList.get(0).getLastReadTime();
            recordList.get(0).setLastReadTime(readTime);
            loadMsgRecordMapper.update(recordList.get(0),new QueryWrapper<LoadMsgRecord>().eq("id",recordList.get(0).getId()));
        }else {
            LoadMsgRecord loadMsgRecord = new LoadMsgRecord();
            loadMsgRecord.setLastReadTime(readTime);
            loadMsgRecord.setUserCount(userCount);
            loadMsgRecordMapper.insert(loadMsgRecord);
        }

//        List<FriendMsgTemp> iSendRecordList = friendMsgTempMapper.getMineSendRecord(formatDate, userCount);
//        iSendRecordList.forEach(t->{
//            UserCount userInfo = CacheRoom.getUserCountOfCount(userCount);
//            t.setSendNickName(userInfo.getNickName());
//            t.setCountPhoto(userInfo.getPhoto());
//        });
//        messageList.addAll(iSendRecordList);

        for (List<String> list : groupPartition) {
            if (list.size() > 0){
                List<FriendMsgTemp> newMsgList = groupMsgTempMapper.getNewMsg(list, formatDate);
                newMsgList.forEach(t->{
                    Integer unRead = groupMsgMapper.getUnReadFriendMsg(userCount,t.getSendId());
//                    UserCount userInfo = CacheRoom.getUserCountOfCount(t.getSendId());
//                    t.setSendNickName(userInfo.getNickName());
                    t.setCountPhoto(groupCountMapper.selectOne(new QueryWrapper<GroupCount>().lambda().eq(GroupCount::getGroupCount,t.getSendId())).getPhoto());
                    t.setUnRead(unRead > 99?99:unRead);
                    parseMessage(t,userCount);
                });
                messageList.addAll(newMsgList);
            }
        }

        messageList.sort((t1,t2)->t2.getCreateTime().compareTo(t1.getCreateTime()));
        return messageList;
    }

    @Override
    public List<CurrentTalkCount> getCurrentTalkCount() {
        String userCount = UserContext.getUserCount();
        List<FriendMsgCurrent> mainMsg = friendMsgMapper.getMainMsg(userCount);
        List<FriendMsgCurrent> mainGroupMsg = friendMsgMapper.getMainGroupMsg(userCount);
        List<CurrentTalkCount> ret = new ArrayList<>();
        mainMsg.forEach(t->{
            if (t != null){
                ret.add(new CurrentTalkCount(t.getSendId(),"0",t.getCountPhoto(),t.getSendNickName()));
            }
        });
        mainGroupMsg.forEach(t->{
            if (t != null) {
                ret.add(new CurrentTalkCount(t.getSendId(), "1", t.getCountPhoto(), t.getSendNickName()));
            }
        });
//        ret.sort(Comparator.comparing(CurrentTalkCount::getCount));
        return ret;
    }


    @Override
    public List<CurrentTalkCount> getSearchCountList() {
        String userCount = UserContext.getUserCount();
        List<UserType> friendList = userCountMapper.getFriend(userCount);
        List<UserType> groupList = userCountMapper.getGroup(userCount);
        List<String> userList = friendList.stream().map(UserType::getUserCount).collect(Collectors.toList());
        List<CurrentTalkCount> ret = new ArrayList<>();
        Map<String, UserCount> map = CacheRoom.getUserCountOfCountList(userList);
        userList.forEach(t->{
            UserCount tempUser = map.get(t);
            if (tempUser != null && !userCount.equals(t)){
                ret.add(new CurrentTalkCount(tempUser.getUserCount(),"0",tempUser.getPhoto(),tempUser.getNickName()));
            }
        });

        List<String> groupCountList = groupList.stream().map(UserType::getUserCount).collect(Collectors.toList());
        List<GroupCount> groupSearch = groupCountMapper.selectList(new QueryWrapper<GroupCount>().in("group_count", groupCountList));
        groupSearch.forEach(t->{
            ret.add(new CurrentTalkCount(t.getGroupCount(),"1",t.getPhoto(),t.getGroupName()));
        });
        return ret;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean forwardMsg(String forwardPeople, String msgValue) {
        List<CurrentTalkCount> currentTalkCountList = JSONObject.parseArray(forwardPeople, CurrentTalkCount.class);
        MsgValue msgValueCov = JSONObject.parseObject(msgValue, MsgValue.class);
        UserCount userCount = CacheRoom.getUserCountOfCount(UserContext.getUserCount());
        FriendMsg friendMsg = null;
        GroupMsg groupMsg = null;
        String simpleTime = TimeUtil.getSimpleTime();
        if ("0".equals(msgValueCov.getMsgType())){
            friendMsg = friendMsgMapper.selectOne(new QueryWrapper<FriendMsg>().lambda().eq(FriendMsg::getId, msgValueCov.getId()));
            if (friendMsg == null){
                throw CustomException.createCustomException(ExceptionCodeEnum.MSG_NOT_FOUND);
            }
            friendMsg.setSendId(userCount.getUserCount());
            friendMsg.setSendNickName(userCount.getNickName());
            friendMsg.setCountPhoto(userCount.getPhoto());
        }else {
            groupMsg = groupMsgMapper.selectOne(new QueryWrapper<GroupMsg>().lambda().eq(GroupMsg::getId, msgValueCov.getId()));
            if (groupMsg == null){
                throw CustomException.createCustomException(ExceptionCodeEnum.MSG_NOT_FOUND);
            }
            groupMsg.setSendId(userCount.getUserCount());
            groupMsg.setSendId(userCount.getUserCount());
            groupMsg.setSendNickName(userCount.getNickName());
            groupMsg.setCountPhoto(userCount.getPhoto());
        }
        sendMsgOfForward(currentTalkCountList,friendMsg,groupMsg,userCount.getUserCount());


        return true;
    }

   public void sendMsgOfForward(List<CurrentTalkCount> forwardPeople,FriendMsg friendMsg, GroupMsg groupMsg,String userCount){
        if (friendMsg == null && groupMsg == null){
            throw CustomException.createCustomException("参数错误，好友消息和群消息不能同时为空");
        }
       UserCount userCountInfo = CacheRoom.getUserCountOfCount(userCount);
       Map<String, List<CurrentTalkCount>> map = forwardPeople.stream().collect(Collectors.groupingBy(CurrentTalkCount::getCountType));
        //用户帐户
        List<CurrentTalkCount> privateCount = map.get("0");
        //群聊账号
        List<CurrentTalkCount> groupCount = map.get("1");

        if (privateCount != null && privateCount.size() > 0){
            if (friendMsg == null){
                friendMsg = new FriendMsg();
                friendMsg.setMsg(groupMsg.getMsg());
                friendMsg.setType(groupMsg.getType());
                friendMsg.setFileName(groupMsg.getFileName());
                friendMsg.setIsValid(groupMsg.getIsValid());
                friendMsg.setCreateTime(groupMsg.getCreateTime());
                friendMsg.setSendId(userCount);
                friendMsg.setSendNickName(userCountInfo.getNickName());
                friendMsg.setCountPhoto(userCountInfo.getPhoto());
                friendMsg.setCountType("0");
            }

            for (CurrentTalkCount currentTalkCount : privateCount) {
                friendMsg.setId(null);
                friendMsg.setReceiveId(currentTalkCount.getCount());
                friendMsg.setCountIdentify(SimpleUtil.combineCountOfFriend(friendMsg.getSendId(),friendMsg.getReceiveId()));
                friendMsg.setCreateTime(TimeUtil.getSimpleTime());
                friendMsgService.sendMsg(friendMsg,null);
            }

        }

        if (groupCount != null && groupCount.size() > 0){
            if (groupMsg == null){
                groupMsg = new GroupMsg();
                groupMsg.setMsg(friendMsg.getMsg());
                groupMsg.setType(friendMsg.getType());
                groupMsg.setFileName(friendMsg.getFileName());
                groupMsg.setIsValid(friendMsg.getIsValid());
                groupMsg.setCreateTime(friendMsg.getCreateTime());
                groupMsg.setSendId(userCount);
                groupMsg.setSendNickName(userCountInfo.getNickName());
                groupMsg.setCountPhoto(userCountInfo.getPhoto());
                groupMsg.setCountType("1");
            }

            for (CurrentTalkCount currentTalkCount : groupCount) {
                groupMsg.setId(null);
                groupMsg.setReceiveId(currentTalkCount.getCount());
                groupMsg.setGroupId(currentTalkCount.getCount());
                groupMsg.setGroupName(currentTalkCount.getNickName());
                groupMsg.setGroupPhoto(currentTalkCount.getPhoto());
                groupMsg.setCreateTime(TimeUtil.getSimpleTime());
                groupMsgTempService.sendMsg(groupMsg,null);
            }
        }

    }

    boolean checkDigit(String str){
        Matcher matcher = digitPattern.matcher(str);
        String s = matcher.replaceAll("");
        return str.length() == s.length();
    }


    void addDefaultFriend(String userCount){
        Friend friend = new Friend();
        friend.setFriendId(userCount);
        friend.setUserId(userCount);
        friend.setCreateTime(TimeUtil.getSimpleTime());
        friend.setShip("0");
        friend.setShipType("我的好友");
        friendMapper.insert(friend);

    }
    void createSpaceCount(String userCount,Integer id){
        SpaceCount spaceCount = new SpaceCount();
        spaceCount.setCreateTime(TimeUtil.getSimpleTime());
        spaceCount.setUserCount(userCount);
        spaceCount.setAnimal("0");
        spaceCount.setScore(0);
        spaceCountMapper.insert(spaceCount);
        userCountMapper.updateSpaceById(id,spaceCount.getId());
    }

    public void fillUserCount(UserCount userCountSelect,UserCount userCount){
        userCountSelect.setSimpleInfo(userCount.getSimpleInfo());
        userCountSelect.setNickName(userCount.getNickName());
        userCountSelect.setAge(userCount.getAge());
        userCountSelect.setHabit(userCount.getHabit());
        userCountSelect.setSex(userCount.getSex());
        userCountSelect.setPhone(userCount.getPhone());
        userCountSelect.setBirthday(userCount.getBirthday());
        userCountSelect.setConstellation(userCount.getConstellation());
        userCountSelect.setAddress(userCount.getAddress());
        userCountSelect.setSchool(userCount.getSchool());
        userCountSelect.setCompany(userCount.getCompany());
    }

    public void  parseMessage(FriendMsgTemp friendMsgTemp,String userCount){
        String suffix = userCount.equals(friendMsgTemp.getSendId()) ? "": friendMsgTemp.getSendNickName() + " : ";
        if ("0".equals(friendMsgTemp.getType())) {
            friendMsgTemp.setMsg(EmojiUtils.emojiRecovery2(suffix+friendMsgTemp.getMsg()));
        } else {
            friendMsgTemp.setMsg(suffix + CodeFileType.parseCode(friendMsgTemp.getType()));
        }
        if (friendMsgTemp.getGroupName() != null && !"".equals(friendMsgTemp.getGroupName())){
            friendMsgTemp.setSendNickName(friendMsgTemp.getGroupName());
        }

    }




}
