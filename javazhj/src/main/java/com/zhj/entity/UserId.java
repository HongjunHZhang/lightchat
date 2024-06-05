package com.zhj.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;

@TableName("user_id")
public class UserId {
    @TableField("user_count")
    @ApiModelProperty("用户帐户")
    private String userCount;

    @TableField("password")
    @ApiModelProperty("用户帐户")
    private String password;

    public UserId(String userCount, String password) {
        this.userCount = userCount;
        this.password = password;
    }

    public UserId() {
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
