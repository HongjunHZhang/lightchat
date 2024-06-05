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
 * @since 2022-11-09
 */
@ApiModel(value = "Exception对象", description = "异常实体类")
@TableName("exception")
public class ExceptionEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("唯一id标识符")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("请求参数")
    private String param;

    @ApiModelProperty("方法名")
    private String methodName;

    @ApiModelProperty("类名")
    private String className;

    @ApiModelProperty("信息")
    private String message;

    @ApiModelProperty("请求uri路劲")
    private String uri;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("生成日期")
    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Exception{" +
            "id=" + id +
            ", param=" + param +
            ", methodName=" + methodName +
            ", className=" + className +
            ", message=" + message +
            ", uri=" + uri +
            ", ip=" + ip +
            ", createTime=" + createTime +
        "}";
    }
}
