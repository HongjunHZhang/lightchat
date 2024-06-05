package com.zhj.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.entity.*;
import com.zhj.entity.dic.DefaultSrc;
import com.zhj.entity.dic.RelationShipCode;
import com.zhj.entity.login.TokenBody;
import com.zhj.entity.ret.RetAddGroup;
import com.zhj.entity.ret.RetAddUser;
import com.zhj.entity.ret.RetAddUserMsg;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;
import com.zhj.mapper.*;
import com.zhj.service.AddUserService;
import com.zhj.util.FileUtil;
import com.zhj.util.entry.JwtUtils;
import com.zhj.util.TimeUtil;
import com.zhj.util.cache.CacheRoom;
import com.zhj.websocket.WebSocketRoom;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author 789
 */
@Service
public class AddUserServiceImpl extends ServiceImpl<AdduserMapper, AddUser> implements AddUserService {
    @Resource
    AdduserMapper adduserMapper;
    @Resource
    UserCountMapper userCountMapper;
    @Resource
    GroupCountMapper groupCountMapper;
    @Resource
    GroupMemberMapper groupMemberMapper;
    @Resource
    AddGroupMapper addGroupMapper;
    @Resource
    FriendMapper friendMapper;
    @Resource
    AddShipMapper addShipMapper;
    @Resource
    GroupMsgMapper groupMsgMapper;

    @Override
    public List<RetAddUser> getDefaultUser() {
        String userCount = UserContext.getUserCount();
        return adduserMapper.getDefaultUserInfo(userCount);
    }

    @Override
    public List<RetAddUser> searchUser(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String userCount = JwtUtils.getUserCount(map);
        List<RetAddUser> defaultUserInfo = adduserMapper.getInfoByNickNameOrCount(map.get("value"));
        couldAdd(defaultUserInfo,userCount);
        return defaultUserInfo;
    }

    @Override
    public List<RetAddGroup> getDefaultGroup() {
        String userCount = UserContext.getUserCount();
        List<RetAddGroup> defaultGroupInfo = adduserMapper.getDefaultGroupInfo(userCount);
        for (RetAddGroup retAddGroup : defaultGroupInfo) {
            retAddGroup.setCouldAdd(1);
        }
        return defaultGroupInfo;
    }

    @Override
    public List<RetAddGroup> searchGroup(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        String userCount = JwtUtils.getUserCount(map);
        List<RetAddGroup> defaultGroupInfo = adduserMapper.getGroupByNickNameOrCount(map.get("value"));
        couldAddGroup(defaultGroupInfo,userCount);
        return defaultGroupInfo;
    }

    @Override
    public void registerGroup(MultipartFile file, String defaultInfo) {
        GroupCount groupCount = JSONObject.parseObject(defaultInfo, GroupCount.class);
        QueryWrapper<GroupCount> queryWrapper = new QueryWrapper<GroupCount>().eq("group_count", groupCount.getGroupCount());
        if (groupCountMapper.selectCount(queryWrapper)>0){
            throw CustomException.createCustomException(ExceptionCodeEnum.GROUP_HAS_EXIST);
        }
        String userCount = UserContext.get().getName();
        fillGroupCount(groupCount,userCount);
        if (file != null){
            try {
                String fileName = groupCount.getGroupCount() + "groupPhoto.jpg";
                groupCount.setPhoto(DefaultSrc.getGroupPhotoSuffix() + "/" + groupCount.getGroupCount() + "/" + groupCount.getGroupCount() + "groupPhoto.jpg");
                FileUtil.saveFileAndCompress(file,fileName, DefaultSrc.getRootGroupFile()+"/"+groupCount.getGroupCount());
            }catch (Exception e){
                throw CustomException.createCustomException(ExceptionCodeEnum.SYSTEM_FILE_READ_EXCEPTION);
            }
        }else {
            groupCount.setPhoto(DefaultSrc.getRootUrlPath()+"/admin/userphoto/userphoto10.jpg");
        }
        groupCountMapper.insert(groupCount);
        GroupMember groupMember = new GroupMember();
        fillGroupMember(groupMember,groupCount,CacheRoom.getUserCountOfCount(userCount),"0");
        groupMemberMapper.insert(groupMember);
        WebSocketRoom.addRoom(groupCount.getGroupCount(),new HashMap<>());
    }

    @Override
    public List<RetAddUserMsg>  getNewUser() {
        String userCount = UserContext.getUserCount();
        List<AddUser> retAddUser = adduserMapper.selectList(new QueryWrapper<AddUser>().eq("friend_Id", userCount).eq("add_status",1));
        List<RetAddUserMsg>  retAddUserMsgList = new ArrayList<>();
        long longTime = Long.parseLong(TimeUtil.getSimpleTime());
        for (AddUser addUser : retAddUser) {
            RetAddUserMsg retAddUserMsg = new RetAddUserMsg();
            retAddUserMsg.setMsg(addUser.getMsg());
            retAddUserMsg.setUserCount(addUser.getUserId());
            retAddUserMsg.setCreateTime(TimeUtil.getDay(addUser.getCreateTime(),longTime));
            retAddUserMsg.setNickName(addUser.getUserNickName());
            retAddUserMsg.setPhoto(addUser.getPhoto());
            retAddUserMsg.setType(1);
            retAddUserMsgList.add(retAddUserMsg);
        }
        List<Object> groupMembers = groupMemberMapper.selectObjs(new QueryWrapper<GroupMember>().ne("user_level", "2").eq("user_count", userCount)
                .select("group_count")
        );

        for (Object obj : groupMembers) {
            List<AddGroup> addGroupList = addGroupMapper.selectList(new QueryWrapper<AddGroup>().eq("group_count", obj.toString()).eq("add_status",1));
            if (addGroupList.size() == 0){
                continue;
            }
            GroupCount groupCount = groupCountMapper.selectOne(new QueryWrapper<GroupCount>().eq("group_count", obj.toString()));
            for (AddGroup addGroup : addGroupList) {
                RetAddUserMsg retAddUserMsg = new RetAddUserMsg();
                retAddUserMsg.setMsg(addGroup.getMsg());
                retAddUserMsg.setUserCount(addGroup.getUserCount());
                retAddUserMsg.setCreateTime(TimeUtil.getDay(addGroup.getCreateTime(),longTime));
                retAddUserMsg.setNickName(addGroup.getUserNickName());
                retAddUserMsg.setPhoto(addGroup.getUserPhoto());
                retAddUserMsg.setType(2);
                retAddUserMsg.setGroupName(groupCount.getGroupName());
                retAddUserMsg.setGroupCount(groupCount.getGroupCount());
                retAddUserMsgList.add(retAddUserMsg);
            }
        }
        List<AddShip> shipRequest = addShipMapper.getShipRequest(userCount);
        for (AddShip addShip : shipRequest) {
            RetAddUserMsg retAddUserMsg = new RetAddUserMsg();
            retAddUserMsg.setUserCount(addShip.getSendId());
            retAddUserMsg.setPhoto(addShip.getSendPhoto());
            retAddUserMsg.setType(3);
            retAddUserMsg.setNickName(addShip.getSendNickName());
            retAddUserMsg.setCreateTime(TimeUtil.getDay(addShip.getCreateTime(),longTime));
            retAddUserMsg.setGroupCount(addShip.getRelationShip());
            retAddUserMsg.setMsg(addShip.getMsg());
            retAddUserMsgList.add(retAddUserMsg);
        }
        retAddUserMsgList.sort((t1,t2)->t2.getCreateTime().compareTo(t1.getCreateTime()));
        return retAddUserMsgList;
    }

    @Override
    public String confirmAddUser(RetAddUserMsg retAddUserMsg) {
        String userCount = UserContext.getUserCount();
        if (retAddUserMsg.getChoose() == 1) {
            int delete = adduserMapper.delete(new QueryWrapper<AddUser>().eq("user_id", retAddUserMsg.getUserCount()).eq("friend_id", userCount));
            if (delete == 0) {
                throw CustomException.createCustomException(ExceptionCodeEnum.USER_INFO_VALID);
            }
            Friend friend = new Friend();
            Friend friendUser = new Friend();
            fillFriend(friend, userCount, retAddUserMsg.getUserCount());
            fillFriend(friendUser,retAddUserMsg.getUserCount(),userCount);
            friendMapper.insert(friend);
            friendMapper.insert(friendUser);
            return "添加好友成功";
        }else if (retAddUserMsg.getChoose() == 2){
            int delete = adduserMapper.delete(new QueryWrapper<AddUser>().eq("user_id", retAddUserMsg.getUserCount()).eq("friend_id", userCount));
            if (delete == 0) {
                throw CustomException.createCustomException(ExceptionCodeEnum.USER_INFO_VALID);
            }else {
                return "拒绝好友申请成功";
            }
        }else {
            AddUser addUser = adduserMapper.selectOne(new QueryWrapper<AddUser>().eq("user_id", retAddUserMsg.getUserCount()).eq("friend_id", userCount));
            addUser.setAddStatus("-1");
            int id = adduserMapper.update(addUser, new QueryWrapper<AddUser>().eq("id", addUser.getId()));
            if (id == 1){
                return "忽略好友成功，你将不会再收到该用户的请求";
            }else {
                throw CustomException.createCustomException(ExceptionCodeEnum.REQUEST_ERROR);
            }

        }
    }

    @Override
    public String confirmAddGroup(RetAddUserMsg retAddUserMsg) {
        String userCount = UserContext.getUserCount();
        if (retAddUserMsg.getChoose() == 1) {
            Long aLong = groupMemberMapper.selectCount(new QueryWrapper<GroupMember>().eq("group_count", retAddUserMsg.getGroupCount()).eq("user_count",userCount).ne("user_level", "2"));
            if ( aLong == null || aLong == 0) {
                throw CustomException.createCustomException(ExceptionCodeEnum.USER_VALID);
            }
            int delete = addGroupMapper.delete(new QueryWrapper<AddGroup>().eq("user_count", retAddUserMsg.getUserCount()).eq("group_count", retAddUserMsg.getGroupCount()));
            if (delete == 0){
                throw CustomException.createCustomException(ExceptionCodeEnum.REQUEST_ERROR);
            }
            GroupMember groupMember = new GroupMember();
            UserCount userCountRet = CacheRoom.getUserCountOfCount(retAddUserMsg.getUserCount());
            fillGroupMember(groupMember,userCountRet,retAddUserMsg.getGroupCount());
            groupMemberMapper.insert(groupMember);
            groupCountMapper.addPeople(retAddUserMsg.getGroupCount());
            WebSocketRoom.refreshRoom(groupMember.getGroupCount());
            return "同意进群成功";
        }else if (retAddUserMsg.getChoose() == 2){
            int delete = addGroupMapper.delete(new QueryWrapper<AddGroup>().eq("user_count", retAddUserMsg.getUserCount()).eq("group_count", retAddUserMsg.getGroupCount()));
            if (delete == 0) {
                throw CustomException.createCustomException(ExceptionCodeEnum.REQUEST_ERROR);
            }else {
                return "拒绝入群成功";
            }
        }else {
            AddGroup addGroup = addGroupMapper.selectOne(new QueryWrapper<AddGroup>().eq("user_Count", retAddUserMsg.getUserCount()).eq("group_count", retAddUserMsg.getGroupCount()));
            addGroup.setAddStatus("-1");
            int id = addGroupMapper.update(addGroup, new QueryWrapper<AddGroup>().eq("id", addGroup.getId()));
            if (id == 1){
                return "忽略用户成功，你将不会再收到该用户的请求";
            }else {
                throw CustomException.createCustomException(ExceptionCodeEnum.REQUEST_ERROR);
            }

        }
    }


    @Override
    public String addRelationShip(AddShip addShip) {
        String userCount = UserContext.getUserCount();
        if ("1".equals(addShip.getRelationShip())){
            friendMapper.setRelationShip(addShip.getSendId(),addShip.getReceiveId(),"1");
            return "你和用户"+addShip.getReceiveNickName()+"成功变为朋友";
        }

        AddShip addShipSelect = addShipMapper.selectOne(new QueryWrapper<AddShip>().eq("send_id", userCount).eq("receive_id", addShip.getReceiveId()));
        if (addShipSelect != null){
            addShipSelect.setRelationShip(addShip.getRelationShip());
            addShipSelect.setMsg(RelationShipCode.parseCode(addShip.getRelationShip()));
            addShipMapper.update(addShipSelect, new UpdateWrapper<AddShip>().eq("id", addShipSelect.getId()));
            return "发起请求成功";
        }

        fillAddShip(addShip);
        addShipMapper.insert(addShip);
        return "发起请求成功";
    }

    @Override
    public String confirmRelationShip(RetAddUserMsg retAddUserMsg) {
        String userCount = UserContext.getUserCount();
        if (retAddUserMsg.getChoose() == 1){
            addShipMapper.delete(new QueryWrapper<AddShip>().eq("send_ID", retAddUserMsg.getUserCount()).eq("receive_ID", userCount));
            //前端把relationship放进groupCount，我不想写实体类了
            friendMapper.setRelationShip(retAddUserMsg.getUserCount(),userCount,retAddUserMsg.getGroupCount());
            return retAddUserMsg.getMsg()+"成功";
        }
        if (retAddUserMsg.getChoose() == 2){
            addShipMapper.delete(new QueryWrapper<AddShip>().eq("send_ID", retAddUserMsg.getUserCount()).eq("receive_ID", userCount));
            return "拒绝关系申请成功";
        }
        if (retAddUserMsg.getChoose() == 3){
            AddShip addShip = addShipMapper.selectOne(new QueryWrapper<AddShip>().eq("send_ID", retAddUserMsg.getUserCount()).eq("receive_ID", userCount));
            addShip.setAddStatus("-1");
            addShipMapper.update(addShip, new UpdateWrapper<AddShip>().eq("id", addShip.getId()));
            return "忽略好友申请成功，你将不会再收到该用户的关系申请";
        }
        throw CustomException.createCustomException(ExceptionCodeEnum.REQUEST_ERROR);
    }

    @Override
    public String deleteUser(String json) {
        UserContext.checkIsTourist();
        String userCount = UserContext.getUserCount();
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        if (!map.getOrDefault("userId","").equals(userCount)){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_INFO_VALID);
        }
        friendMapper.deleteFriend(userCount,map.get("friendId"),map.get("shipType"));
        return "删除好友成功";
    }

    @Override
    public String exitGroup(String json) {
        String userCount = UserContext.getUserCount();
        String groupCount = getGroupCount(JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {}),userCount);
        GroupMember groupMember = groupMemberMapper.selectOne(new QueryWrapper<GroupMember>().eq("group_count", groupCount).eq("user_count", userCount));
        if ("0".equals(groupMember.getUserLevel())){
            throw CustomException.createCustomException(ExceptionCodeEnum.GROUP_MASTER_EXIT);
        }
        groupMemberMapper.delete(new QueryWrapper<GroupMember>().eq("group_count", groupCount).eq("user_count", userCount));
        return "退出该群聊成功";
    }

    @Override
    public String dissolveGroup(String json) {
        String userCount = UserContext.getUserCount();
        String groupCount = getGroupCount(JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {}),userCount);
        GroupMember groupMember = groupMemberMapper.selectOne(new QueryWrapper<GroupMember>().eq("group_count", groupCount).eq("user_count", userCount));
        if (!"0".equals(groupMember.getUserLevel())){
            throw CustomException.createCustomException(ExceptionCodeEnum.GROUP_DISSOLUTION_ROLE);
        }
        groupMemberMapper.delete(new QueryWrapper<GroupMember>().eq("group_count", groupCount));
        groupMsgMapper.delete(new QueryWrapper<GroupMsg>().eq("group_id",groupCount));
        groupCountMapper.delete(new QueryWrapper<GroupCount>().eq("group_count", groupCount));
        addGroupMapper.delete(new QueryWrapper<AddGroup>().eq("group_count", groupCount));
        return "解散该群聊成功";
    }

    @Override
    public boolean adduser(String json) {
        Map<String, String> map = JSONObject.parseObject(json, new TypeReference<Map<String, String>>() {});
        TokenBody tokenBody = UserContext.get();
        String userCount = tokenBody.getName();
        UserCount retCount = CacheRoom.getUserCountOfCount(userCount);
        return addUser(userCount, map.get("friendId"),map.get("msg"),retCount.getPhoto());
    }

    @Override
    public boolean addUser(String userCount, String friendId,String msg,String photo) {
        List<AddUser> addUserList = adduserMapper.selectList(new QueryWrapper<AddUser>().eq("user_id", userCount).eq("friend_id", friendId));
        if (addUserList != null && addUserList.size()>0){
            AddUser addUser = addUserList.get(0);
            if (addUser.getTime() > 2){
                throw CustomException.createCustomException(ExceptionCodeEnum.ADD_USER_EXIST);
            }
            addUser.setTime(addUser.getTime()+1);
            addUser.setMsg(msg);
            addUser.setPhoto(photo);
            adduserMapper.update(addUser, new UpdateWrapper<AddUser>().eq("id", addUser.getId()));
            return true;
        }

        AddUser addUser = fillAddUserInfo(CacheRoom.getUserCountOfCount(userCount), friendId,msg,photo);
        adduserMapper.insert(addUser);
        return true;
    }





    @Override
    public int addGroup(String userCount, String groupCount, String msg) {
        UserCount retCount = CacheRoom.getUserCountOfCount(userCount);
        GroupCount retLong = groupCountMapper.selectOne(new QueryWrapper<GroupCount>().eq("group_count", groupCount));
        if (retLong != null && "-1".equals(retLong.getEnterRole())){
            GroupMember groupMember = fillGroupMember(retCount, groupCount);
            this.groupMemberMapper.insert(groupMember);
            retLong.setPeopleNum(retLong.getPeopleNum()+1);
            groupCountMapper.update(retLong,new UpdateWrapper<GroupCount>().eq("id",retLong.getId()));
            return 1;
        }
        List<AddGroup> addGroupList = addGroupMapper.selectList(new QueryWrapper<AddGroup>().eq("user_Count", userCount).eq("group_count", groupCount));
        if (addGroupList != null && addGroupList.size()>0){
            AddGroup addGroupTemp = addGroupList.get(0);
            if (addGroupTemp.getTime() > 2){
                throw CustomException.createCustomException(ExceptionCodeEnum.ADD_GROUP_EXIST);
            }
            addGroupTemp.setTime(addGroupTemp.getTime()+1);
            addGroupTemp.setMsg(msg);
            addGroupTemp.setUserPhoto(retCount.getPhoto());
             addGroupMapper.update(addGroupTemp, new UpdateWrapper<AddGroup>().eq("id", addGroupTemp.getId()));
            return 0;
        }
        AddGroup addGroup = fillAddGroupInfo(retCount, groupCount, msg, retCount.getPhoto());
        addGroupMapper.insert(addGroup);
        return 0;
    }


    AddUser fillAddUserInfo(UserCount userCount, String friendId,String msg,String photo){
        AddUser addUser = new AddUser();
        addUser.setUserId(userCount.getUserCount());
        addUser.setUserNickName(userCount.getNickName());
        addUser.setFriendId(friendId);
        addUser.setCreateTime(TimeUtil.getSimpleTime());
        addUser.setTime(1);
        addUser.setMsg(msg);
        addUser.setPhoto(photo);
        addUser.setAddStatus("1");
        return addUser;
    }

    AddGroup fillAddGroupInfo(UserCount userCount, String groupCount,String msg,String photo){
        AddGroup addGroup = new AddGroup();
        addGroup.setUserCount(userCount.getUserCount());
        addGroup.setUserNickName(userCount.getNickName());
        addGroup.setGroupCount(groupCount);
        addGroup.setCreateTime(TimeUtil.getSimpleTime());
        addGroup.setTime(1);
        addGroup.setMsg(msg);
        addGroup.setUserPhoto(photo);
        addGroup.setAddStatus("1");
        return addGroup;
    }
    GroupMember fillGroupMember(UserCount userCount, String groupCount){
        GroupMember groupMember = new GroupMember();
        groupMember.setUserLevel("2");
        groupMember.setGroupCount(groupCount);
        groupMember.setPhotoSrc(userCount.getPhoto());
        groupMember.setUserCount(userCount.getUserCount());
        groupMember.setCreateTime(TimeUtil.getSimpleTime());
        groupMember.setNickName(userCount.getNickName());
         return groupMember;
    }

    public void couldAdd(List<RetAddUser> retAddUserList,String userCount){
        for (RetAddUser retAddUser : retAddUserList) {
            List<Friend> friendList = Optional.ofNullable(adduserMapper.getFriend(retAddUser.getUserCount(), userCount)).orElse(new ArrayList<>());
            if (friendList.size() > 0){
                retAddUser.setCouldAdd(-1);
            }else {
                retAddUser.setCouldAdd(1);
            }


        }
    }

    public void couldAddGroup(List<RetAddGroup> retAddGroupList, String userCount){
        for (RetAddGroup retAddGroup : retAddGroupList) {
            Long num = Optional.ofNullable(adduserMapper.enterGroupInfo(userCount, retAddGroup.getGroupCount())).orElse(0L);
            if (num>0){
                retAddGroup.setCouldAdd(-1);
            }else {
                retAddGroup.setCouldAdd(1);
            }

        }

    }

    void fillGroupCount(GroupCount groupCount,String userCount){
        groupCount.setCreateTime(TimeUtil.getSimpleTime());
        groupCount.setSimpleInfo("该群还没有编写简介");
        groupCount.setPeopleNum(1);
        groupCount.setCreatorCount(userCount);
    }

    void fillGroupMember(GroupMember groupMember,GroupCount groupCount,UserCount userCount,String level){
        groupMember.setGroupCount(groupCount.getGroupCount());
        groupMember.setCreateTime(TimeUtil.getSimpleTime());
        groupMember.setNickName(userCount.getNickName());
        groupMember.setPhotoSrc(userCount.getPhoto());
        groupMember.setUserCount(userCount.getUserCount());
        groupMember.setUserLevel(level);
    }

    void fillFriend(Friend friend,String userCount,String friendCount){
        friend.setUserId(userCount);
        friend.setFriendId(friendCount);
        friend.setCreateTime(TimeUtil.getSimpleTime());
        friend.setShip("1");
        friend.setShipType("我的好友");
        friend.setLastReadTime(TimeUtil.getSimpleTime());
    }

    void fillGroupMember(GroupMember groupMember,UserCount userCount,String groupCount){
        groupMember.setGroupCount(groupCount);
        groupMember.setUserCount(userCount.getUserCount());
        groupMember.setUserLevel("2");
        groupMember.setNickName(userCount.getNickName());
        groupMember.setCreateTime(TimeUtil.getSimpleTime());
        groupMember.setPhotoSrc(userCount.getPhoto());
        groupMember.setLastReadTime("0");
    }

    void fillAddShip(AddShip addShip){
        addShip.setMsg(RelationShipCode.parseCode(addShip.getRelationShip()));
        addShip.setCreateTime(TimeUtil.getSimpleTime());
        addShip.setAddStatus("1");
    }

   static String getGroupCount(Map<String, String> map,String userCount){
        String groupCount = map.get("groupCount");
        if (!map.getOrDefault("userCount","").equals(userCount)){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_INFO_VALID);
        }
        return groupCount;
    }

}
