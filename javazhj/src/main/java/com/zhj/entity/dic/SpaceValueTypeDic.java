package com.zhj.entity.dic;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 789
 */

public enum SpaceValueTypeDic {
    /**
     * spaceValue的类型区别
     */
    PHOTO_PATH("0","图片路径"),
    THUMB_INFO("1","点赞信息"),
    LEAVE_MESSAGE("2","留言信息"),
    ;
    private String code;
    private String msg;

    public static List<String> TALK_INFO;

    static {
        TALK_INFO = new ArrayList<>();
        TALK_INFO.add("0");
        TALK_INFO.add("1");
    }
    SpaceValueTypeDic(String code, String msg) {
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
