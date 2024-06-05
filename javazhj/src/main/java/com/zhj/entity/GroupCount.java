package com.zhj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

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
@ApiModel(value = "GroupCount对象", description = "群账号信息")
@TableName("group_count")
public class GroupCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("Id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("群账号")
    private String groupCount;

    @ApiModelProperty("群名字")
    private String groupName;

    @ApiModelProperty("群公告")
    private String notice;

    @ApiModelProperty("文件路径")
    private String fileSrc;

    @ApiModelProperty("群相册路径")
    private String imgSrc;

    @ApiModelProperty("日志")
    private String journal;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("创建者用户")
    private String creatorCount;

    @ApiModelProperty("群头像")
    private String photo;

    @ApiModelProperty("最大人数")
    private Integer maxNum;

    @ApiModelProperty("群类型")
    private String type;

    @ApiModelProperty("进群规则")
    private String enterRole;

    @ApiModelProperty("简介")
    private String simpleInfo;

    @ApiModelProperty("现有人数")
    private Integer peopleNum;


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
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
    public String getFileSrc() {
        return fileSrc;
    }

    public void setFileSrc(String fileSrc) {
        this.fileSrc = fileSrc;
    }
    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
    public String getJournal() {
        return journal;
    }

    public void setJournal(String journal) {
        this.journal = journal;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreatorCount() {
        return creatorCount;
    }

    public void setCreatorCount(String creatorCount) {
        this.creatorCount = creatorCount;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEnterRole() {
        return enterRole;
    }

    public void setEnterRole(String enterRole) {
        this.enterRole = enterRole;
    }

    public String getSimpleInfo() {
        return simpleInfo;
    }

    public void setSimpleInfo(String simpleInfo) {
        this.simpleInfo = simpleInfo;
    }

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    @Override
    public String toString() {
        return "Groupcount{" +
            "id=" + id +
            ", groupCount=" + groupCount +
            ", groupName=" + groupName +
            ", notice=" + notice +
            ", fileSrc=" + fileSrc +
            ", imgSrc=" + imgSrc +
            ", journal=" + journal +
            ", createTime=" + createTime +
            ", createrCount=" + creatorCount +
            ", photo=" + photo +
        "}";
    }
}
