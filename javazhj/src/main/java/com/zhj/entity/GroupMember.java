package com.zhj.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 *
 * </p>
 *
 * @author zhj
 * @since 2022-02-21
 */
@ApiModel(value = "GroupMember对象", description = "群成员")
@TableName("group_member")
public class GroupMember implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("群账号")
    private String groupCount;

    @ApiModelProperty("用户账号")
    private String userCount;

    @ApiModelProperty("用户等级0群主,1管理员,2群员")
    private String userLevel;

    @ApiModelProperty("进群时间")
    private String createTime;

    @ApiModelProperty("群员昵称")
    private String nickName;

    @ApiModelProperty("头像路径")
    private String photoSrc;

    @ApiModelProperty("上一次阅读到的时间")
    private String lastReadTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(String groupCount) {
        this.groupCount = groupCount;
    }
    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }
    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getPhotoSrc() {
        return photoSrc;
    }

    public void setPhotoSrc(String photoSrc) {
        this.photoSrc = photoSrc;
    }

    public String getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(String lastReadTime) {
        this.lastReadTime = lastReadTime;
    }

    @Override
    public String toString() {
        return "Groupmember{" +
            "id=" + id +
            ", groupCount=" + groupCount +
            ", userCount=" + userCount +
            ", userLevel=" + userLevel +
            ", createTime=" + createTime +
            ", nickName=" + nickName +
            ", photoSrc=" + photoSrc +
        "}";
    }
}
