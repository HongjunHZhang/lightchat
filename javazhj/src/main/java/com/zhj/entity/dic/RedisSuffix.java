package com.zhj.entity.dic;

/**
 * @author 789
 */

public enum RedisSuffix {
    /**
     * redis配置前缀-注册、找回密码、修改密码
     */
    REGISTER("0","login:register:"),
    RETRIEVE("1","login:retrieve:"),
    CHANGE("2","login:change:"),
    ;
    private String code;
    private String msg;

    RedisSuffix(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
