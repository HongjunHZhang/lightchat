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
 * @since 2022-03-02
 */
@TableName("add_ship")
@ApiModel(value = "AddShip对象", description = "更改好友关系")
public class AddShip implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("发送方帐户")
    private String sendId;

    @ApiModelProperty("接收方帐户")
    private String receiveId;

    @ApiModelProperty("发送消息")
    private String msg;

    @ApiModelProperty("请求达成关系")
    private String relationShip;

    @ApiModelProperty("可接收状态")
    private String addStatus;

    @ApiModelProperty("发送方昵称")
    private String sendNickName;

    @ApiModelProperty("发送方照片")
    private String sendPhoto;

    @ApiModelProperty("时间")
    private String createTime;

    @ApiModelProperty("接收方昵称")
    private String receiveNickName;

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
    public String getRelationShip() {
        return relationShip;
    }

    public void setRelationShip(String relationShip) {
        this.relationShip = relationShip;
    }
    public String getAddStatus() {
        return addStatus;
    }

    public void setAddStatus(String addStatus) {
        this.addStatus = addStatus;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getReceiveNickName() {
        return receiveNickName;
    }

    public void setReceiveNickName(String receiveNickName) {
        this.receiveNickName = receiveNickName;
    }

    @Override
    public String toString() {
        return "AddShip{" +
            "id=" + id +
            ", sendId=" + sendId +
            ", receiveId=" + receiveId +
            ", msg=" + msg +
            ", relationShip=" + relationShip +
            ", addStatus=" + addStatus +
        "}";
    }
}
