package com.zhj.entity.dic;

/**
 * @author 789
 */

public enum RelationShipCode {
    /**
     * 请求新建关系类型
     */
    FRIEND("1","请求关系变为朋友"),
    JI_YOU("2","请求关系变为闺蜜"),
    GUI_MI("3","请求关系变为基友"),
    LIAN_REN("4","请求关系变为恋人"),
    ;
    private String code;
    private String msg;

    RelationShipCode(String code, String msg) {
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
        for (RelationShipCode value : RelationShipCode.values()) {
            if (code.equals(value.getCode())){
                return value.getMsg();
            }
        }
        return "未知关系";

    }

}
