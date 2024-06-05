package com.zhj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhj.entity.GroupMsg;
import com.zhj.entity.ret.RetGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhj
 * @since 2022-02-21
 */
public interface GroupMsgMapper extends BaseMapper<GroupMsg> {
    /**
     * 获取群聊消息
     * @param groupCount 群账号
     * @return 返回群帐户对应群消息消息
     */
     List<RetGroup> getGroupMsg(@Param("groupCount") String groupCount);

    /**
     * 标记帐户群消息已读至的时间
     * @param userCount 用户账号
     * @param groupCount 群账号
     * @param lastReadTime 上一次阅读时间
     * @return 返回操作成功行数
     */
     int signRead(@Param("userCount") String userCount,@Param("groupCount") String groupCount,@Param("lastReadTime") String lastReadTime);

    /**
     * 按日期获取群消息，主要用于活跃度统计
     * @param groupCount 群账号
     * @param createTime 时间
     * @return 群消息
     */
     List<RetGroup> getGroupMsgByDay(@Param("groupCount") String groupCount,@Param("createTime") String createTime);

    /**
     * 获取群中所有图片集合，按发送用户id分类
     * @param groupCount 群账号
     * @return 群图片list
     */
     List<RetGroup> getGroupPhoto(@Param("groupCount") String groupCount);

    /**
     * 获取群聊中未读消息条数
     * @param userCount 用户账户
     * @param groupCount 群账号
     * @return 消息条数
     */
     Integer getUnReadFriendMsg(@Param("userCount") String userCount,@Param("groupCount") String groupCount);
}
