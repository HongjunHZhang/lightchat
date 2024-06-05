package com.zhj.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 *
 * </p>
 *
 * @author zhj
 * @since 2022-03-17
 */
@ApiModel(value = "Visitors对象", description = "拜访者信息")
public class Visitors implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("拜访者ID")
    private String visitorsId;

    @ApiModelProperty("被拜访者ID")
    private String bevisitorsId;

    @ApiModelProperty("记录时间")
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getVisitorsId() {
        return visitorsId;
    }

    public void setVisitorsId(String visitorsId) {
        this.visitorsId = visitorsId;
    }
    public String getBevisitorsId() {
        return bevisitorsId;
    }

    public void setBevisitorsId(String bevisitorsId) {
        this.bevisitorsId = bevisitorsId;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Visitors{" +
            "id=" + id +
            ", visitorsId=" + visitorsId +
            ", bevisitorsId=" + bevisitorsId +
            ", createTime=" + createTime +
        "}";
    }
}
