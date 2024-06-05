package com.zhj.entity.ret;

/**
 * @author 789
 */
public class RetAddGroup {
    private Integer id;
    private String groupCount;
    private String groupName;
    private String photo;
    private String simpleInfo;
    /**
     * 1可加,2不可加
     */
    private Integer couldAdd;
    private Integer peopleNum;
    private Integer maxNum;
    private String enterRole;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(String groupCount) {
        this.groupCount = groupCount;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public Integer getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(Integer peopleNum) {
        this.peopleNum = peopleNum;
    }

    public Integer getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(Integer maxNum) {
        this.maxNum = maxNum;
    }

    public String getEnterRole() {
        return enterRole;
    }

    public void setEnterRole(String enterRole) {
        this.enterRole = enterRole;
    }
}
