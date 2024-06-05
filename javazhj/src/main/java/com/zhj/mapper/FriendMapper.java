package com.zhj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhj.entity.Friend;
import com.zhj.entity.SpaceCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendMapper extends BaseMapper<Friend> {
   /**
    * 设置用户间的关系值
    * @param userCount 用户帐户
    * @param friendCount 好友帐户
    * @param status 设置为的状态
    * @return 返回操作成功行数
    */
   int  setRelationShip(@Param("userCount") String userCount,@Param("friendCount") String friendCount,@Param("status") String status);

   /**
    * 删除好友
    * @param userCount 用户帐户
    * @param friendCount 好友帐户
    * @param shipType 好友所在组-例如（我的朋友）
    * @return 返回操作成功行数
    */
   int deleteFriend(@Param("userCount") String userCount,@Param("friendCount") String friendCount,@Param("shipType") String shipType);

   /**
    * 获取所有好友的空间
    * @param userCount 用户账户
    * @return 好友空间列表
    */
   List<String> getFriendSpaceCount(@Param("userCount") String userCount);

   /**
    * 获取user_id与friend_id不同的列表
    * @return 集合列表
    */
   List<Friend> getFriendOtherMine();

   /**
    * 获取传入帐号空间的详细信息
    * @param userCount 帐号
    * @return 返回空间信息
    */
   List<SpaceCount> getFriendSpaceCountInfo(@Param("userCount") String userCount);
}
