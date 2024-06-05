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
@TableName("space_talk")
@ApiModel(value = "SpaceTalk对象", description = "空间说说")
public class SpaceTalk implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("空间账号")
    private Integer spaceCount;

    @ApiModelProperty("用户账户")
    private String userCount;

    @ApiModelProperty("说说信息")
    private String msg;

    @ApiModelProperty("发起时间")
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getSpaceCount() {
        return spaceCount;
    }

    public void setSpaceCount(Integer spaceCount) {
        this.spaceCount = spaceCount;
    }
    public String getMsg() {
        return msg;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
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


}
