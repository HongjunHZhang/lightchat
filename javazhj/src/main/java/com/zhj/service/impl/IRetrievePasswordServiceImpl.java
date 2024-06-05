package com.zhj.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhj.entity.UserCount;
import com.zhj.entity.dic.RedisSuffix;
import com.zhj.entity.usercontroller.UserContext;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;
import com.zhj.mapper.UserCountMapper;
import com.zhj.service.IRetrievePasswordService;
import com.zhj.util.AES;
import com.zhj.util.EmailUtil;
import com.zhj.util.RedisUtil;
import com.zhj.util.ThreadPoolUtil;
import com.zhj.util.cache.CacheRoom;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * IRetrievePasswordServiceImpl
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/12/22 12:05
 */
@Service
public class IRetrievePasswordServiceImpl extends ServiceImpl<UserCountMapper, UserCount> implements IRetrievePasswordService {
    @Resource
    UserCountMapper userCountMapper;
    @Resource
    EmailUtil emailUtil;
    @Resource
    RedisUtil redisUtil;


    @Override
    public String getVerifyCode(String userCount)  {
        UserCount userCountSelect = CacheRoom.getUserCountOfCount(userCount);
        if (userCountSelect == null || userCountSelect.getEmail() == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_NOT_EMAIL);
        }
        if (redisUtil.getString(RedisSuffix.RETRIEVE.getMsg() + userCountSelect.getEmail()) != null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_EMAIL_EXIST_VERIFICATION);
        }
        ThreadPoolUtil.execute(()->{
            try {
                emailUtil.sendCodeEmail(new String[]{userCountSelect.getEmail()});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return "请在邮箱中查看您的验证消息";
    }

    @Override
    public String changePassword(String json) {
        Map<String,String> map = JSON.parseObject(json,new TypeReference<Map<String,String>>(){});
        String userCount = map.get("userCount");
        if (userCount == null || "".equals(userCount)){
            throw CustomException.createCustomException("帐号错误，请核实验帐号是否输入正确");
        }
        UserCount userCountSelect = CacheRoom.getUserCountOfCount(userCount);
        String email = userCountSelect.getEmail();

        if(!redisUtil.compareValue(RedisSuffix.RETRIEVE.getMsg()+email,map.get("verifyCode"))){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_EMAIL_VERIFICATION_ERROR);
        }

        String password = map.get("password");
        if (password == null || password.length() < 6){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_PASSWORD_TO_SIMPLE);
        }

        userCountMapper.updatePasswordByUserCount(userCount, AES.encrypt(password));
        return "新密码修改成功,现在您可以使用新密码登录了";
    }

    @Override
    public String emailVerifyCode(String email) throws Exception {
        if (redisUtil.getString(RedisSuffix.CHANGE.getMsg() + email) != null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_EMAIL_EXIST_VERIFICATION);
        }
        ThreadPoolUtil.execute(()->{
            try {
                emailUtil.sendChangeEmail(new String[]{email});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return "请在您的邮箱："+email+"中查看您的验证消息";
    }

    @Override
    public String changeEmail(String json) {
        Map<String,String> map = JSON.parseObject(json,new TypeReference<Map<String,String>>(){});
        String userCount = UserContext.getUserCount();
        UserCount userCountSelect = userCountMapper.selectOne(new QueryWrapper<UserCount>().eq("user_count", userCount)
                .eq("password",AES.encrypt(map.get("oldPassword"))));
        if (userCountSelect == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_PASSWORD_ERROR);
        }

        String email = map.get("email");
        if(!redisUtil.compareValue(RedisSuffix.CHANGE.getMsg()+email,map.get("verifyCode"))){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_EMAIL_VERIFICATION_ERROR);
        }

        userCountMapper.updateEmailByUserCount(userCount,email);
        return "新邮箱绑定成功";
    }
}
