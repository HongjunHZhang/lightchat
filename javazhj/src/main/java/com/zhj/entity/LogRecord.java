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
 * @since 2022-12-28
 */
@TableName("log_record")
@ApiModel(value = "LogRecord对象", description = "日志记录")
public class LogRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("唯一id标识符")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户帐号")
    private String userCount;

    @ApiModelProperty("请求参数")
    private String param;

    @ApiModelProperty("方法名")
    private String methodName;

    @ApiModelProperty("请求uri路径")
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

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
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
        return "LogRecord{" +
                "id=" + id +
                ", param='" + param + '\'' +
                ", methodName='" + methodName + '\'' +
                ", uri='" + uri + '\'' +
                ", ip='" + ip + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
