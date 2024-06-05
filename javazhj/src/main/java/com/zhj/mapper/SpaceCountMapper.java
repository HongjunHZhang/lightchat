package com.zhj.mapper;

import com.zhj.entity.SpaceCount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhj
 * @since 2022-03-09
 */
public interface SpaceCountMapper extends BaseMapper<SpaceCount> {
   /**
    * 无效接口
    * @param spaceCount 空间帐户
    * @return 用户帐户
    */
   String getUserNickName(@Param("spaceCount") Integer spaceCount);

}
