package com.zhj.entity.dic;

/**
 * @author 789
 */

public enum CodeGroup {
    /**
     * 群权限信息
     */
    MIN_CREATE("0","我创建的群"),
    MIN_ADMIN("1","我管理的群"),
    MIN_MEMBER("2","我加入的群")
    ;
    private String code;
    private String msg;

    CodeGroup(String code, String msg) {
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

    public static String parseCode(String code){
        for (CodeGroup value : CodeGroup.values()) {
           if (code.equals(value.getCode())){
               return value.getMsg();
           }
        }
        return "未知分组";

    }
}

