package com.zhj.entity.ret;

public class RetReportInfo {
    private int reportId;
    private String userId;
    private String userNickName;
    private String userPhoto;
    private String beReportId;
    private String BeReportNickName;
    private String BeReportPhoto;
    private String msg;
    private String type;
    private String reportReason;

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getBeReportId() {
        return beReportId;
    }

    public void setBeReportId(String beReportId) {
        this.beReportId = beReportId;
    }

    public String getBeReportNickName() {
        return BeReportNickName;
    }

    public void setBeReportNickName(String beReportNickName) {
        BeReportNickName = beReportNickName;
    }

    public String getBeReportPhoto() {
        return BeReportPhoto;
    }

    public void setBeReportPhoto(String beReportPhoto) {
        BeReportPhoto = beReportPhoto;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }
}
