package com.zhj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2022-10-09
 */
@TableName("group_msg_temp")
@ApiModel(value = "GroupMsgTemp对象", description = "群消息临时表，用于记录新消息")
public class GroupMsgTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("发送方帐户")
    private String sendId;

    @ApiModelProperty("接收方帐户(@某人)")
    private String receiveId;

    @ApiModelProperty("消息内容")
    private String msg;

    @ApiModelProperty("发送时间")
    private String createTime;

    @ApiModelProperty("消息类型")
    private String countType;

    @ApiModelProperty("是否合法，1为合法")
    private String isValid;

    @ApiModelProperty("群标识符")
    private String groupId;

    @ApiModelProperty("用户昵称")
    private String sendNickName;

    @ApiModelProperty("消息类型")
    private String type;

    @ApiModelProperty("文件名")
    private String fileName;

    private String countPhoto;

    private String unRead;

    private String groupName;

    private String groupPhoto;

    public GroupMsgTemp(GroupMsg groupMsg) {
        this.id = groupMsg.getId();
        this.sendId = groupMsg.getSendId();
        this.receiveId = groupMsg.getReceiveId();
        this.msg = groupMsg.getMsg();
        this.createTime = groupMsg.getCreateTime();
        this.countType = "1";
        this.isValid = groupMsg.getIsValid();
        this.groupId = groupMsg.getGroupId();
        this.sendNickName = groupMsg.getSendNickName();
        this.type = groupMsg.getType();
        this.fileName = groupMsg.getFileName();
        this.countPhoto = groupMsg.getCountPhoto();
        this.groupPhoto = groupMsg.getGroupPhoto();
        this.groupName = groupMsg.getGroupName();
    }

    public GroupMsgTemp() {
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
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
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

    public String getCountPhoto() {
        return countPhoto;
    }

    public void setCountPhoto(String countPhoto) {
        this.countPhoto = countPhoto;
    }

    public String getUnRead() {
        return unRead;
    }

    public void setUnRead(String unRead) {
        this.unRead = unRead;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupPhoto() {
        return groupPhoto;
    }

    public void setGroupPhoto(String groupPhoto) {
        this.groupPhoto = groupPhoto;
    }

    @Override
    public String toString() {
        return "GroupMsgTemp{" +
            "id=" + id +
            ", sendId=" + sendId +
            ", receiveId=" + receiveId +
            ", msg=" + msg +
            ", createTime=" + createTime +
            ", countType=" + countType +
            ", isValid=" + isValid +
            ", groupId=" + groupId +
            ", sendNickName=" + sendNickName +
            ", type=" + type +
            ", fileName=" + fileName +
        "}";
    }
}
