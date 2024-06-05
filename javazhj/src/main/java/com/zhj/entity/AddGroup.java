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
 * @since 2022-02-23
 */
@TableName("add_group")
@ApiModel(value = "AddGroup对象", description = "添加群聊的")
public class AddGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户id")
    private String userCount;

    @ApiModelProperty("群聊id")
    private String groupCount;

    @ApiModelProperty("发起时间")
    private String createTime;

    @ApiModelProperty("用户昵称")
    private String userNickName;

    @ApiModelProperty("用户头像")
    private String userPhoto;

    @ApiModelProperty("验证消息")
    private String msg;

    @ApiModelProperty("次数")
    private Integer time;

    @ApiModelProperty("群名字")
    private String groupName;

    @ApiModelProperty("添加状态")
    private String addStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }
    public String getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(String groupCount) {
        this.groupCount = groupCount;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }
    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAddStatus() {
        return addStatus;
    }

    public void setAddStatus(String addStatus) {
        this.addStatus = addStatus;
    }

    @Override
    public String toString() {
        return "AddGroup{" +
            "id=" + id +
            ", userCount=" + userCount +
            ", groupCount=" + groupCount +
            ", createTime=" + createTime +
            ", userNickName=" + userNickName +
            ", userPhoto=" + userPhoto +
            ", msg=" + msg +
            ", time=" + time +
        "}";
    }
}
