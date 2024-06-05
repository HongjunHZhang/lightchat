package com.zhj.exception;

/**
 * @author 789
 */

public enum ExceptionCodeEnum {
    /**
     * 异常情况
     */
    PARAM_IS_NULL(0,"参数为null"),
    ID_IS_NULL(1,"ID为空"),
    PARAM_TYPE_ERROR(2,"参数类型错误"),
    DATA_BASE_CONNECTION_ERROR(3,"数据库连接错误"),
    USER_VALID(300,"用户信息有误,请尝试重新登录"),
    USER_NOT_EXIST(301,"用户不存在或已注销"),
    USER_INFO_VALID(302,"身份信息校验失败,请尝试重新登录"),
    USER_IS_NOT_FRIEND(303,"你不是对方的好友，不允许向对方发送消息"),
    USER_HAS_BAN(304,"账号异常或已被封禁，请尝试联系管理员解封"),
    USER_HAS_NOT_LOGIN_ROLE(305,"用户无登录权限"),
    USER_SPACE_NOT_EXIST(306,"用户空间不存在"),
    USER_IDENTIFY_NOT_MARCH(307,"用户身份不匹配"),
    USER_NOT_EMAIL(311,"帐号不存在或该账号未绑定邮箱，无法使用该功能"),
    USER_EMAIL_VERIFICATION_ERROR(312,"验证码错误，请核实验证码是否输入正确"),
    USER_EMAIL_EXIST_VERIFICATION(313,"您的邮箱中已有验证消息，若未收到请耐心等待三分钟后再试"),
    USER_PASSWORD_TO_SIMPLE(320,"密码过于简单"),
    USER_PASSWORD_ERROR(321,"密码验证失败，请重试"),
    USER_PASSWORD_NOT_SAME(322,"两次密码不一致"),
    USER_PASSWORD_SAME_OF_OLD(323,"新密码不能和原密码一致"),
    USER_HAS_NOT_FRIEND(330,"你还没有好友，请先去添加好友"),
    USER_LOGIN_ERROR(340,"帐户不存在或密码有误"),
    USER_GROUP_NEED_NAME(350,"分组必须有一个名字"),
    USER_GROUP_EXIST(351,"已有该分组,操作失败"),
    USER_GROUP_EXIST_USER_COUNT(352,"该分组已有该好友,操作失败"),
    MSG_NOT_FOUND(353,"该消息记录未找到"),
    GROUP_NOT_EXIST(400,"群不存在或已注销"),
    GROUP_HAS_EXIST(401,"群账号已被注册"),
    ADD_USER_EXIST(402,"请耐心等待对方同意，勿重复添加"),
    GROUP_MASTER_EXIT(403,"你是群主,无法请求退出群，请选择解散群聊选项"),
    GROUP_DISSOLUTION_ROLE(404,"你不是群主,无法选择解散群聊选项"),
    GROUP_HAS_NOT_ROLE(405,"你无权限执行该操作"),
    GROUP_HAS_NOT_ROLE_WITHDRAW(406,"你不是群主,无权限执行该操作"),
    GROUP_CHANGE_SELF_ROLE(407,"你无法改变自己的身份"),
    Group_IS_NOT_MEMBER(408,"你不是群成员，不能发送群消息"),
    ADD_GROUP_EXIST(422,"请耐心等待群主同意，勿重复添加"),
    REQUEST_ERROR(500,"请求失败，请尝试重试"),
    REQUEST_PARAM_ERROR(501,"请求参数错误，请尝试重试"),
    ADMIN_ROLE_ERROR(520,"你没有管理员权限"),
    NO_MARCH_RECORD(530,"未找到匹配的记录"),
    SYSTEM_FILE_READ_EXCEPTION(600,"文件读取错误"),
    TOURISTS_ROLE_ERROR(700,"游客没有权限执行该操作"),
    UNKNOWN_ERROR(999,"未知错误"),
    ;
    //状态码
    private int code;
    //描述信息
    private String msg;

    ExceptionCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static String parseCode(int code){
        for (ExceptionCodeEnum value : ExceptionCodeEnum.values()) {
           if (value.getCode() == code){
               return value.getMsg();
           }
        }
        return "[未知]";

    }
}

