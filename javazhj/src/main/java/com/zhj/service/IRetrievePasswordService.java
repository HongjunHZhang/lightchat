package com.zhj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhj.entity.UserCount;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * IRetrievePasswordService
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/12/22 12:04
 */
public interface IRetrievePasswordService extends IService<UserCount> {
    /**
     * 获取找回密码邮箱验证码
     * @param userCount 用户帐户
     * @return 申请状态
     */
    String getVerifyCode(@RequestParam("userCount") String userCount);

    /**
     * 修改密码
     * @param json 帐户、验证码等的json形式
     * @return 操作状态
     */
    String changePassword(String json);

    /**
     * 获取修改邮箱验证码
     * @param email  邮箱地址
     * @return 操作状态
     * @throws Exception 发送邮件异常
     */
    String emailVerifyCode(String email) throws Exception;

    /**
     * 修改邮箱
     * @param json 核对信息，包括旧密码、验证信息等
     * @return 操作状态
     */
    String changeEmail(String json);
}
