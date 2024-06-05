package com.zhj.mapper;

import com.zhj.entity.AddShip;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhj
 * @since 2022-03-02
 */
public interface AddShipMapper extends BaseMapper<AddShip> {
     /**
      * 获取关系请求信息
      * @param userCount 用户账户
      * @return 请求列表
      */
     List<AddShip> getShipRequest(@Param("userCount") String userCount);
}
