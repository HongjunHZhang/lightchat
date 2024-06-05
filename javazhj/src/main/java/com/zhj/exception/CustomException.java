package com.zhj.exception;

/**
 * CustomException
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/12/29 21:59
 */
public class CustomException extends RuntimeException {
    private String message;
    private int code;

    public CustomException(String message) {
        super(message);
        this.message = message;
    }
    public CustomException(String message,int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public CustomException() {

    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static CustomException createCustomException(String msg){
        return new CustomException(msg);
    }

    public static CustomException createCustomException(ExceptionCodeEnum exceptionCodeEnum){
        return new CustomException(exceptionCodeEnum.getMsg(), exceptionCodeEnum.getCode());
    }

}
