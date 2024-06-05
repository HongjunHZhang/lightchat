package com.zhj.entity.dic;

/**
 * @author 789
 */

public enum CodeFileType {
    /**
     * 文件类型
     */
    COMMON_MSG("0","[聊天消息]"),
    VOICE_MSG("1","[语音消息]"),
    PHOTO_MSG("2","[图片]"),
    RADIO_MSG("3","[视频文件]"),
    AUDIO_MSG("4","[音频文件]"),
    FILE_MSG("5","[文件]"),
    ;

    private String code;
    private String msg;

    CodeFileType(String code, String msg) {
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
        for (CodeFileType value : CodeFileType.values()) {
           if (value.getCode().equals(code)){
               return value.getMsg();
           }
        }
        return "[未知]";

    }
}

