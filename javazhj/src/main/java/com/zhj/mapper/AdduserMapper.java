package com.zhj.mapper;

import com.zhj.entity.AddUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhj.entity.Friend;
import com.zhj.entity.ret.RetAddGroup;
import com.zhj.entity.ret.RetAddUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhj
 * @since 2022-02-22
 */
public interface AdduserMapper extends BaseMapper<AddUser> {
     /**
      * 获取默认的用户，去除用户已加朋友
      * @param userId 用户帐号（是得到结果排除自己）
      * @return 人员列表
      */
     List<RetAddUser> getDefaultUserInfo(@Param("userId") String userId);

     /**
      * 通过昵称或帐户查找用户
      * @param param 传入参数
      * @return 查找得到的用户结果集
      */
     List<RetAddUser> getInfoByNickNameOrCount(@Param("param") String param);

     /**
      * 获取默认的群聊，去除自己在的群聊，因为默认群聊类似于推荐群聊，不推荐已加入群聊
      * @param userId 用户帐户
      * @return 群聊列表
      */
     List<RetAddGroup> getDefaultGroupInfo(@Param("userId") String userId);

     /**
      * 通过昵称或帐户查找群聊
      * @param param 传入参数
      * @return 查找得到的群聊结果集
      */
     List<RetAddGroup> getGroupByNickNameOrCount(@Param("param") String param);

     /**
      * 判断两个人是否是好友
      * @param userId 用户帐户
      * @param friendId 好友帐户
      * @return 返回结果集
      */
     List<Friend> getFriend(@Param("userId") String userId,@Param("friendId") String friendId);

     /**
      * 判断是否可以进入群聊，返回结果若大于0则证明用户已存在于群聊
      * @param userId 用户帐户
      * @param groupId 群聊帐户
      * @return 大于0则证明用户已存在于群聊，小于0则证明不存在，可以发起进入请求
      */
     Long enterGroupInfo(@Param("userId") String userId,@Param("groupId") String groupId);

}
