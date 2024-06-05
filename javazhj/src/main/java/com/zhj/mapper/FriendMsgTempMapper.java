package com.zhj.mapper;

import com.zhj.entity.FriendMsg;
import com.zhj.entity.FriendMsgTemp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhj
 * @since 2022-09-28
 */
public interface FriendMsgTempMapper extends BaseMapper<FriendMsgTemp> {
    /**
     * 查询新消息记录
     * @param receiveId 用户帐户
     * @return 所有新消息集合
     */
    List<FriendMsgTemp> getNewRecord(@Param("receiveId") String receiveId);

    /**
     * 查询自己发送的记录
     * @param createTime 时间
     * @param sendId 发送者帐户
     * @return 发送消息集合
     */
    List<FriendMsgTemp> getMineSendRecord(@Param("createTime") String createTime, @Param("sendId") String sendId);
    /**
     * 查询是否有记录
     * @param friendMsg 消息实体
     * @return 临时表是否已有数据
     */
    Integer searchRecordByTwoCount(FriendMsg friendMsg);

    /**
     * 存取信息
     * @param friendMsgTemp 临时消息实体
     *
     */

    void saveMsg(FriendMsgTemp friendMsgTemp);

    /**
     * 接收到新消息时增加记录
     * @param friendMsgTemp 临时消息实体
     */
    void changeRecord(FriendMsgTemp friendMsgTemp);

    /**
     * 去除朋友之前发送的消息记录，因为消息只保持最新的消息，双方都只保持一条
     * @param friendMsgTemp 临时消息实体
     */
    void signReadOfFriend(FriendMsgTemp friendMsgTemp);


    /**
     * 按id标记以读
     * @param idList id列表
     */
    void signRead(List<Integer> idList);

    /**
     * 标记账户已读
     * @param userCount 账户
     */
    void signReadByUser(@Param("userCount") String userCount);

    /**
     * 标记账户已读,自己发送的那一条
     * @param userCount 账户
     */
    void signReadByUserOfMine(@Param("userCount") String userCount);

}
