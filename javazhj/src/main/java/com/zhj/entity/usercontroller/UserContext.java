package com.zhj.entity.usercontroller;

import com.zhj.entity.login.TokenBody;
import com.zhj.exception.CustomException;
import com.zhj.exception.ExceptionCodeEnum;

/**
 * @author 789
 */
public class UserContext {
    private final static ThreadLocal<TokenBody> THREAD_LOCAL = new ThreadLocal<>();
    private final static ThreadLocal<String> AUTHORIZATION = new ThreadLocal<>();

    public static TokenBody get(){
        return THREAD_LOCAL.get();
    }

    public static void set(TokenBody tokenBody){
        THREAD_LOCAL.set(tokenBody);
    }

    public static void remove(){
        THREAD_LOCAL.remove();
    }

    public static void removeAuthorization(){
        AUTHORIZATION.remove();
    }

    public static void setAuthorization(String authorization){
        AUTHORIZATION.set(authorization);
    }

    public static String getUserCount(){
        if (THREAD_LOCAL.get() == null){
            throw CustomException.createCustomException(ExceptionCodeEnum.USER_VALID);
        }
        return THREAD_LOCAL.get().getName();
    }

    public static String getUserCountOrDefault(String defaultValue){
        if (THREAD_LOCAL.get() == null){
            return defaultValue;
        }
        return THREAD_LOCAL.get().getName();
    }

    public static void checkIsTourist(){
        if ("0".equals(THREAD_LOCAL.get().getRootLevel())){
            throw CustomException.createCustomException(ExceptionCodeEnum.TOURISTS_ROLE_ERROR);
        }
    }

    public static boolean checkHasRole(String level){
        return THREAD_LOCAL.get().getRootLevel().compareTo(level) > 0;
    }

    public static boolean checkIsNotAdmin(){
        return THREAD_LOCAL.get() == null || THREAD_LOCAL.get().getRootLevel().compareTo("3") < 0;
    }

    public static String getAuthorization(){
        return AUTHORIZATION.get();
    }

}
