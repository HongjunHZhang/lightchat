package com.zhj.mapper;

import com.zhj.entity.UserCount;
import com.zhj.entity.Visitors;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhj
 * @since 2022-03-17
 */
public interface VisitorsMapper extends BaseMapper<Visitors> {
   /**
    * 获取最近到访人员
    * @param userCount 用户帐户
    * @return 到访人员帐户集合
    */
   List<UserCount> getCurrentVisitors(@Param("userCount") String userCount);

}
