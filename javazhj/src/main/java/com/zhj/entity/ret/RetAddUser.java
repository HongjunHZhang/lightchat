package com.zhj.entity.ret;

/**
 * @author 789
 */
public class RetAddUser {
    private Integer id;
    private String userCount;
    private String nickName;
    private String photo;
    private String simpleInfo;
    /**
     * 1可加,2不可加
     */
    private Integer couldAdd;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getSimpleInfo() {
        return simpleInfo;
    }

    public void setSimpleInfo(String simpleInfo) {
        this.simpleInfo = simpleInfo;
    }

    public Integer getCouldAdd() {
        return couldAdd;
    }

    public void setCouldAdd(Integer couldAdd) {
        this.couldAdd = couldAdd;
    }
}
