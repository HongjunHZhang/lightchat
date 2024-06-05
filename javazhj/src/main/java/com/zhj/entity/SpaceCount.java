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
@TableName("space_count")
@ApiModel(value = "SpaceCount对象", description = "空间")
public class SpaceCount implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户账户")
    private String userCount;

    @ApiModelProperty("用户积分")
    private Integer score;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("神兽id")
    private String animal;

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
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    @Override
    public String toString() {
        return "SpaceCount{" +
            "id=" + id +
            ", userCount=" + userCount +
            ", score=" + score +
            ", createTime=" + createTime +
            ", animal=" + animal +
        "}";
    }
}
