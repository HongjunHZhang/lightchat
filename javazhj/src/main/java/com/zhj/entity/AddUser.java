package com.zhj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 *
 * </p>
 *
 * @author zhj
 * @since 2022-02-22
 */
@ApiModel(value = "Adduser对象", description = "添加好友")
public class AddUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("发起方ID")
    private String userId;

    @ApiModelProperty("加好友ID")
    private String friendId;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("发起次数")
    private Integer time;

    @ApiModelProperty("发起方昵称")
    private String userNickName;

    @ApiModelProperty("验证消息")
    private String msg;

    @ApiModelProperty("头像")
    private String photo;

    @ApiModelProperty("添加状态")
    private String addStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAddStatus() {
        return addStatus;
    }

    public void setAddStatus(String addStatus) {
        this.addStatus = addStatus;
    }

    @Override
    public String toString() {
        return "Adduser{" +
            "id=" + id +
            ", userId=" + userId +
            ", friendId=" + friendId +
            ", createTime=" + createTime +
            ", time=" + time +
        "}";
    }
}
