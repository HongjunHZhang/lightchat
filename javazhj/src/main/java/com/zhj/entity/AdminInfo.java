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
 * @since 2022-03-19
 */
@TableName("admin_info")
@ApiModel(value = "AdminInfo对象", description = "管理员信息")
public class AdminInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("管理员账号")
    private String userName;

    @ApiModelProperty("管理员密码")
    private String password;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("有效时间")
    private String validTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    @Override
    public String toString() {
        return "AdminInfo{" +
            "id=" + id +
            ", userName=" + userName +
            ", password=" + password +
            ", createTime=" + createTime +
            ", validTime=" + validTime +
        "}";
    }
}
