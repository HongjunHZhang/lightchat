package com.zhj.mapper;

import com.zhj.entity.GroupMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhj.entity.ret.RetGroupMember;
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
public interface GroupMemberMapper extends BaseMapper<GroupMember> {
    /**
     * 获取群聊的成员按权限等级以及在线情况排序
     * @param groupCount 群账号
     * @return 群成员列表
     */
    List<RetGroupMember> getMember(@Param("groupCount") String groupCount);
}
