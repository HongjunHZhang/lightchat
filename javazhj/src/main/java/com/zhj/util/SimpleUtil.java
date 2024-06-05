package com.zhj.util;


import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;

import java.util.Random;

/**
 * @author 789
 */
public class SimpleUtil {

    private static final int VERIFICATION_LENGTH = 4;

    public static String getVerificationCode() {
        StringBuilder s =new StringBuilder();
        Random random = new Random();
        for (int i=0;i< VERIFICATION_LENGTH;i++){
           s.append(random.nextInt(10));
        }
        return s.toString();
    }

    /**
     * 组合两个帐号生成对话唯一标识符
     * @param userCount 用户帐户
     * @param friendCount 好友账户
     * @return 组合生成字符
     */
    public static String combineCountOfFriend(String userCount,String friendCount){
        if (userCount == null || friendCount == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_NOT_EXIST);
        }
        return userCount.compareTo(friendCount) > 0 ? friendCount + "-" + userCount : userCount + "-" + friendCount;
    }


}
