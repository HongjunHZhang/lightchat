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
 * @since 2022-03-09
 */
@TableName("space_remark")
@ApiModel(value = "SpaceRemark对象", description = "空间评论")
public class SpaceRemark implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("说说id")
    private Integer talkId;

    @ApiModelProperty("评论人账号")
    private String sendId;

    @ApiModelProperty("评论人昵称")
    private String sendNickName;

    @ApiModelProperty("接收人账号")
    private String receiveId;

    @ApiModelProperty("接收人昵称")
    private String receiveNickName;

    @ApiModelProperty("回复消息")
    private String msg;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("发送方头像")
    private String sendPhoto;

    @ApiModelProperty("接收方头像")
    private String receivePhoto;

    @ApiModelProperty("上一层id")
    private String lastId;

    @ApiModelProperty("层次")
    private Integer height;

    @ApiModelProperty("最外层节点id")
    private String parentId;

    @TableField(exist = false)
    private String changeId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getTalkId() {
        return talkId;
    }

    public void setTalkId(Integer talkId) {
        this.talkId = talkId;
    }
    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }
    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
    }
    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }
    public String getReceiveNickName() {
        return receiveNickName;
    }

    public void setReceiveNickName(String receiveNickName) {
        this.receiveNickName = receiveNickName;
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

    public String getSendPhoto() {
        return sendPhoto;
    }

    public void setSendPhoto(String sendPhoto) {
        this.sendPhoto = sendPhoto;
    }

    public String getReceivePhoto() {
        return receivePhoto;
    }

    public void setReceivePhoto(String receivePhoto) {
        this.receivePhoto = receivePhoto;
    }

    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

    public String getChangeId() {
        return changeId;
    }

    public void setChangeId(String changeId) {
        this.changeId = changeId;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
