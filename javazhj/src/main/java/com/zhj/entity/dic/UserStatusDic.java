package com.zhj.entity.dic;

/**
 * @author 789
 */

public enum UserStatusDic {
    /**
     * 账号异常类型
     */
    NORMAL("200","账号正常"),
    FROZEN("401","账号已被冻结"),
    ILLEGAL("402","账号非法"),
    Ban("403","账号已被禁用")
   ;
   private String code;
   private String reason;

    UserStatusDic(String code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    UserStatusDic() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public static boolean checkStatus(String code){
        if ("1".equals(code)){
            return true;
        }else {
            return false;
        }
    }

    public static String getCodeReason(String code){

        for (UserStatusDic value : UserStatusDic.values()) {
            if (value.code.equals(code)){
                return value.reason;
            }
        }
        return "未知状态";
    }

}
