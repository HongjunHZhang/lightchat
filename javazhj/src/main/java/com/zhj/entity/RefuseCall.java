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
 * @since 2022-04-08
 */
@TableName("refuse_call")
@ApiModel(value = "RefuseCall对象", description = "拒接")
public class RefuseCall implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String refuseUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getRefuseUserId() {
        return refuseUserId;
    }

    public void setRefuseUserId(String refuseUserId) {
        this.refuseUserId = refuseUserId;
    }

    @Override
    public String toString() {
        return "RefuseCall{" +
            "id=" + id +
            ", refuseUserId=" + refuseUserId +
        "}";
    }
}
