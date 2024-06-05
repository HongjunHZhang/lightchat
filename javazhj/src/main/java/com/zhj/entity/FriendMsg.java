package com.zhj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.ibatis.annotations.Select;

/**
 * @author 789
 */
@ApiModel(value = "好友聊天对象", description = "好友消息")
@TableName("friend_msg")
public class FriendMsg {
    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("唯一标识符id")
    private Integer id;

    @TableField("send_id")
    @ApiModelProperty("发送方帐户")
    private String sendId;

    @TableField("receive_id")
    @ApiModelProperty("接收方帐户")
    private String receiveId;

    @TableField("send_nick_name")
    @ApiModelProperty("发送方昵称")
    private String sendNickName;

    @TableField("receive_nick_name")
    @ApiModelProperty("接收方昵称")
    private String receiveNickName;

    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private String createTime;

    @TableField("msg")
    @ApiModelProperty("消息")
    private String msg;

    @TableField("count_type")
    @ApiModelProperty("可以被接收")
    private String countType;

    @TableField("is_valid")
    @ApiModelProperty("是否合法")
    private String isValid;

    @TableField("count_photo")
    @ApiModelProperty("发送方头像")
    private String countPhoto;

    @TableField("type")
    @ApiModelProperty("消息类型0、1、2、3,4,5分别代表普通消息、语音、图片、视屏、音频、文件")
    private String type;

    @TableField("file_name")
    @ApiModelProperty("文件名")
    private String fileName;

    @TableField("receive_count_photo")
    @ApiModelProperty("接收帐号头像")
    private String receiveCountPhoto;

    @TableField(select = false)
    private String countIdentify;


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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
