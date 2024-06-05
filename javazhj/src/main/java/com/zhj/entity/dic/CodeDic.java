package com.zhj.entity.dic;

/**
 * @author 789
 */

public enum CodeDic {
    SUCCESS("200","成功"),
    AUTHORIZATION_WILL_EXPIRED("201","签证即将过期"),
    ERROR("400","失败"),
    AUTHORIZATION_EXPIRED("401","签证过期"),
    ;
    private String code;
    private String msg;

    CodeDic(String code, String msg) {
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
