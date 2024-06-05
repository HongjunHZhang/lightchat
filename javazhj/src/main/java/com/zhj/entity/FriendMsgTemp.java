package com.zhj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 *
 * </p>
 *
 * @author zhj
 * @since 2022-09-28
 */
@TableName("friend_msg_temp")
@ApiModel(value = "FriendMsgTemp对象", description = "好友消息历史表，用于接收最新消息")
public class FriendMsgTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("发送方Id")
    private String sendId;

    @ApiModelProperty("接收方id")
    private String receiveId;

    @ApiModelProperty("发送方昵称")
    private String sendNickName;

    @ApiModelProperty("接收方昵称")
    private String receiveNickName;

    @ApiModelProperty("发送时间")
    private String createTime;

    @ApiModelProperty("发送消息")
    private String msg;

    @ApiModelProperty("1为可被接收,2为不可接收")
    private String countType;

    @ApiModelProperty("1为合法,2为不合法")
    private String isValid;

    @ApiModelProperty("帐户头像")
    private String countPhoto;

    @ApiModelProperty("0代表普通消息，1代表语音，2代表图片，3代表文件")
    private String type;

    @ApiModelProperty("文件名字")
    private String fileName;

    @ApiModelProperty("未读消息")
    private Integer unRead;

    private String groupName;

    private String receiveCountPhoto;

    @TableField(select = false)
    private String countIdentify;

    private String mineUnRead;

    public FriendMsgTemp() {
    }

    public FriendMsgTemp(FriendMsg friendMsg){
        this.sendId = friendMsg.getSendId();
        this.receiveId = friendMsg.getReceiveId();
        this.sendNickName = friendMsg.getSendNickName();
        this.receiveNickName = friendMsg.getReceiveNickName();
        this.createTime = friendMsg.getCreateTime();
        this.msg = friendMsg.getMsg();
        this.countType = "0";
        this.isValid = friendMsg.getIsValid();
        this.countPhoto = friendMsg.getCountPhoto();
        this.type = friendMsg.getType();
        this.fileName = friendMsg.getFileName();
        this.receiveCountPhoto = friendMsg.getReceiveCountPhoto();
        this.countIdentify = friendMsg.getCountIdentify();
        this.mineUnRead = "1";
    }

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
    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }
    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
    }
    public String getReceiveNickName() {
        return receiveNickName;
    }

    public void setReceiveNickName(String receiveNickName) {
        this.receiveNickName = receiveNickName;
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
    public String getCountType() {
        return countType;
    }

    public void setCountType(String countType) {
        this.countType = countType;
    }
    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
    public String getCountPhoto() {
        return countPhoto;
    }

    public void setCountPhoto(String countPhoto) {
        this.countPhoto = countPhoto;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public Integer getUnRead() {
        return unRead;
    }

    public void setUnRead(Integer unRead) {
        this.unRead = unRead;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getReceiveCountPhoto() {
        return receiveCountPhoto;
    }

    public void setReceiveCountPhoto(String receiveCountPhoto) {
        this.receiveCountPhoto = receiveCountPhoto;
    }

    public String getCountIdentify() {
        return countIdentify;
    }

    public void setCountIdentify(String countIdentify) {
        this.countIdentify = countIdentify;
    }

    public String getmineUnRead() {
        return mineUnRead;
    }

    public void setmineUnRead(String mineUnRead) {
        this.mineUnRead = mineUnRead;
    }

    @Override
    public String toString() {
        return "FriendMsgTemp{" +
                "id=" + id +
                ", sendId='" + sendId + '\'' +
                ", receiveId='" + receiveId + '\'' +
                ", sendNickName='" + sendNickName + '\'' +
                ", receiveNickName='" + receiveNickName + '\'' +
                ", createTime='" + createTime + '\'' +
                ", msg='" + msg + '\'' +
                ", countType='" + countType + '\'' +
                ", isValid='" + isValid + '\'' +
                ", countPhoto='" + countPhoto + '\'' +
                ", type='" + type + '\'' +
                ", fileName='" + fileName + '\'' +
                ", unRead=" + unRead +
                ", groupName='" + groupName + '\'' +
                ", receiveCountPhoto='" + receiveCountPhoto + '\'' +
                ", countIdentify='" + countIdentify + '\'' +
                ", mineUnRead='" + mineUnRead + '\'' +
                '}';
    }
}
