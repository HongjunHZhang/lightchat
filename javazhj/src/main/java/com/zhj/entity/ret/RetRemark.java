package com.zhj.entity.ret;

import com.zhj.entity.SpaceRemark;

import java.util.List;

/**
 * @author 789
 */
public class RetRemark {
    private Integer id;
    private String sendId;
    private String sendNickName;
    private String sendPhoto;
    private String createTime;
    private String msg;
    private List<SpaceRemark> replay;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<SpaceRemark> getReplay() {
        return replay;
    }

    public void setReplay(List<SpaceRemark> replay) {
        this.replay = replay;
    }

    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
    }

    public String getSendPhoto() {
        return sendPhoto;
    }

    public void setSendPhoto(String sendPhoto) {
        this.sendPhoto = sendPhoto;
    }
}
