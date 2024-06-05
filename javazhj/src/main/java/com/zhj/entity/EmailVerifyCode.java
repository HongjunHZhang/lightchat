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
 * @since 2022-06-24
 */
@TableName("email_verify_code")
@ApiModel(value = "EmailVerifyCode对象", description = "邮箱验证码")
public class EmailVerifyCode implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String email;

    private String verifyCode;

    @ApiModelProperty("生成时间")
    private String generateTime;

    @ApiModelProperty("过期时间")
    private String overdueTime;

    @ApiModelProperty("类型")
    private String type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
    public String getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(String generateTime) {
        this.generateTime = generateTime;
    }
    public String getOverdueTime() {
        return overdueTime;
    }

    public void setOverdueTime(String overdueTime) {
        this.overdueTime = overdueTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "EmailVerifyCode{" +
            "id=" + id +
            ", email=" + email +
            ", verifyCode=" + verifyCode +
            ", generateTime=" + generateTime +
            ", overdueTime=" + overdueTime +
        "}";
    }
}
