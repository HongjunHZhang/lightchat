package com.zhj.mapper;

import com.zhj.entity.SpaceValue;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhj
 * @since 2022-03-09
 */
public interface SpaceValueMapper extends BaseMapper<SpaceValue> {
   /**
    * 获取点赞信息
    * @param associatedId 空间帐号
    * @return 点赞记录
    */
   List<Map<String,String>> getThumbInfo(@Param("associatedId") Integer associatedId);

   /**
    * 获取留言信息
    * @param associatedId 空间帐号
    * @return 留言信息列表
    */
   List<SpaceValue> getPrivateTalk(@Param("associatedId") Integer associatedId);

   /**
    * 获取说说的图片
    * @param associatedId 空间帐号
    * @return 照片墙信息列表
    */
   List<String> getPhotoInfo(@Param("associatedId") Integer associatedId);

}
