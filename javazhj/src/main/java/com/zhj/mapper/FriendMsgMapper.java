package com.zhj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhj.entity.FriendMsg;
import com.zhj.entity.ret.FriendMsgCurrent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FriendMsgMapper extends BaseMapper<FriendMsg> {
   /**
    * 获取好友消息
    * @param countIdentify 帐户标识符
    * @return 返回消息列表
    */
   List<FriendMsg> getFriendMsg(@Param("countIdentify") String countIdentify);

   /**
    * 标记消息已读
    * @param userCount 用户帐户
    * @param friendCount 好友账户
    * @param lastReadTime 上次标记时间
    * @return 返回操作成功行数
    */
   int signRead(@Param("userCount") String userCount,@Param("friendCount") String friendCount,@Param("lastReadTime") String lastReadTime);

   /**
    * 获取帐户历史所有好友信息
    * @param userCount 好友账户
    * @return 信息集合
    */
   List<FriendMsgCurrent> getMainMsg(@Param("userCount") String userCount);

   /**
    * 获取帐户历史所有群聊信息
    * @param userCount 好友账户
    * @return 信息集合
    */
   List<FriendMsgCurrent> getMainGroupMsg(@Param("userCount") String userCount);

   /**
    * 获取未读消息条数
    * @param userCount 用户帐户
    * @param friendCount 好友账户
    * @return 未读条数
    */
   Integer getUnReadFriendMsg(@Param("userCount") String userCount,@Param("friendCount") String friendCount);

}
