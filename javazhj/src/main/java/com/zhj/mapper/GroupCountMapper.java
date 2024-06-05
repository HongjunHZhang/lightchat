package com.zhj.mapper;

import com.zhj.entity.GroupCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhj
 * @since 2022-02-21
 */
public interface GroupCountMapper extends BaseMapper<GroupCount> {
   /**
    * 标记群聊人数加一
    * @param groupCount 群帐号
    * @return 操作成功条数
    */
   int  addPeople(@RequestParam("groupCount") String groupCount);

}
