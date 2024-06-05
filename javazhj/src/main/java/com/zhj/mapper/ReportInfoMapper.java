package com.zhj.mapper;

import com.zhj.entity.ReportInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhj
 * @since 2022-04-12
 */
public interface ReportInfoMapper extends BaseMapper<ReportInfo> {

   /**
    * 改变状态，标记消息是否已处理
    * @param reportId 被举报者id
    * @return 返回操作成功条数
    */
   int changeIsValid(@Param("reportId") int reportId);
}
