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
 * @since 2022-03-09
 */
@TableName("space_value")
@ApiModel(value = "SpaceValue对象", description = "")
public class SpaceValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("关联id，用于连表")
    private Integer associatedId;

    @ApiModelProperty("值")
    private String valueInfo;

    @ApiModelProperty("生成时间")
    private String createTime;

    @ApiModelProperty("类型,0为图片路径,1为点赞信息")
    private String type;

    @ApiModelProperty("若为点赞则是点赞人昵称")
    private String nickName;

    @ApiModelProperty("若为点赞则是点赞人账号")
    private String sendId;

    @ApiModelProperty("图片")
    private String photo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getAssociatedId() {
        return associatedId;
    }

    public void setAssociatedId(Integer associatedId) {
        this.associatedId = associatedId;
    }
    public String getValueInfo() {
        return valueInfo;
    }

    public void setValueInfo(String valueInfo) {
        this.valueInfo = valueInfo;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
