package com.zhj.entity.ret;

import java.util.List;

/**
 * @author 789
 */
public class TalkBox {
    private Integer id;
    private String sendNickName;
    private String sendPhoto;
    private String sendId;
    private String msgInfo;
    private Integer thumbNum;
    private Integer spaceCount;
    private List<Thumb> thumb;
    private List<RetRemark> remark;
    private List<String> photoSrc;
    private String createTime;
    private boolean hasThumb;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getMsgInfo() {
        return msgInfo;
    }

    public void setMsgInfo(String msgInfo) {
        this.msgInfo = msgInfo;
    }

    public Integer getThumbNum() {
        return thumbNum;
    }

    public void setThumbNum(Integer thumbNum) {
        this.thumbNum = thumbNum;
    }

    public List<Thumb> getThumb() {
        return thumb;
    }

    public void setThumb(List<Thumb> thumb) {
        this.thumb = thumb;
    }

    public List<RetRemark> getRemark() {
        return remark;
    }

    public void setRemark(List<RetRemark> remark) {
        this.remark = remark;
    }
    public boolean isHasThumb() {
        return hasThumb;
    }

    public void setHasThumb(boolean hasThumb) {
        this.hasThumb = hasThumb;
    }

    public String getSendPhoto() {
        return sendPhoto;
    }

    public void setSendPhoto(String sendPhoto) {
        this.sendPhoto = sendPhoto;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<String> getPhotoSrc() {
        return photoSrc;
    }

    public void setPhotoSrc(List<String> photoSrc) {
        this.photoSrc = photoSrc;
    }

    public Integer getSpaceCount() {
        return spaceCount;
    }

    public void setSpaceCount(Integer spaceCount) {
        this.spaceCount = spaceCount;
    }
}
