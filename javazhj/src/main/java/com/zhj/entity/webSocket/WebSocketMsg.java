package com.zhj.entity.webSocket;

/**
 * WebSocketMsg
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2023/3/5 20:19
 */
public class WebSocketMsg {
    private int id;
    private String msg;
    private String sendId;
    private String receiveId;
    private String msgType;
    private String countPhoto;
    private String sendNickName;
    private String bsId;
    private String fileName;
    private String groupId;
    private String groupPhoto;
    private String groupNickName;
    private String type;

    public WebSocketMsg(int id, String msg, String sendId, String receiveId, String msgType, String countPhoto, String sendNickName, String bsId, String fileName, String groupId, String groupPhoto, String groupNickName, String type) {
        this.id = id;
        this.msg = msg;
        this.sendId = sendId;
        this.receiveId = receiveId;
        this.msgType = msgType;
        this.countPhoto = countPhoto;
        this.sendNickName = sendNickName;
        this.bsId = bsId;
        this.fileName = fileName;
        this.groupId = groupId;
        this.groupPhoto = groupPhoto;
        this.groupNickName = groupNickName;
        this.type = type;
    }

    public WebSocketMsg() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getCountPhoto() {
        return countPhoto;
    }

    public void setCountPhoto(String countPhoto) {
        this.countPhoto = countPhoto;
    }

    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
    }

    public String getBsId() {
        return bsId;
    }

    public void setBsId(String bsId) {
        this.bsId = bsId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupPhoto() {
        return groupPhoto;
    }

    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }

    public String getGroupNickName() {
        return groupNickName;
    }

    public void setGroupNickName(String groupNickName) {
        this.groupNickName = groupNickName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
