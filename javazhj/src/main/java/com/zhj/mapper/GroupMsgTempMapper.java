package com.zhj.mapper;

import com.zhj.entity.FriendMsgTemp;
import com.zhj.entity.GroupMsgTemp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhj
 * @since 2022-10-09
 */
public interface GroupMsgTempMapper extends BaseMapper<GroupMsgTemp> {
     /**
      * 获取发送人员的所有消息，后面主要用于将每条消息人签上已读
      * @param groupList 群列表
      * @param formatDate 日期
      * @return 消息列表
      */
     List<FriendMsgTemp> getNewMsg(@Param("groupList") List<String> groupList,@Param("createTime") String formatDate);

}
