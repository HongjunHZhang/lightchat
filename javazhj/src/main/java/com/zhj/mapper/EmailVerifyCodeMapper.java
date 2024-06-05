package com.zhj.mapper;

import com.zhj.entity.EmailVerifyCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhj
 * @since 2022-06-24
 */
public interface EmailVerifyCodeMapper extends BaseMapper<EmailVerifyCode> {
   /**
    * 这是一个失效的方法暂时不用，原意是把邮箱信息放mysql数据库，后面放redis了
    * @param email 邮箱地址
    * @param overdueTime 现在时间
    * @param type 类型
    * @return 返回结果集
    */
   List<EmailVerifyCode> getValidUserByEmail(@Param("email") String email,@Param("overdueTime") String overdueTime,@Param("type") String type);

   /**
    * 这是一个失效的方法暂时不用，原意是把邮箱信息放mysql数据库，后面放redis了
    * @param email 邮箱地址
    * @param overdueTime 现在时间
    * @param verifyCode 验证码
    * @param type 类型
    * @return 返回结果集
    */
   List<EmailVerifyCode> checkEmail(@Param("email") String email,@Param("overdueTime") String overdueTime,@Param("verifyCode") String verifyCode,@Param("type") String type);

}
