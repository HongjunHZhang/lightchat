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
 * @since 2022-04-12
 */
@TableName("report_info")
@ApiModel(value = "ReportInfo对象", description = "举报实体")
public class ReportInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("举报人id")
    private String userId;

    @ApiModelProperty("被举报人id")
    private String beReportId;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("举报原因")
    private String reportReason;

    @ApiModelProperty("0代表未处理，1表示已处理")
    private String processed;

    @ApiModelProperty("1代表合法数据，0代表非法数据，即是否撤销")
    private String isValid;

    @ApiModelProperty("举报的消息id")
    private Integer msgId;

    @ApiModelProperty("msg的类型，0群聊，1私聊")
    private String msgType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getBeReportId() {
        return beReportId;
    }

    public void setBeReportId(String beReportId) {
        this.beReportId = beReportId;
    }
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }
    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }
    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
    public Integer getMsgId() {
        return msgId;
    }

    public void setMsgId(Integer msgId) {
        this.msgId = msgId;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return "ReportInfo{" +
            "id=" + id +
            ", userId=" + userId +
            ", beReportId=" + beReportId +
            ", createTime=" + createTime +
            ", reportReason=" + reportReason +
            ", processed=" + processed +
            ", isValid=" + isValid +
            ", msgId=" + msgId +
        "}";
    }
}
