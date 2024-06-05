package com.zhj.entity.ret;

import com.zhj.entity.GroupCount;

import java.util.List;

/**
 * @author 789
 */
public class RetFriendAndGroup {
    private List<RetGroupType> groupCountList;
    private List<UserCountType> userCountList;

    public List<RetGroupType> getGroupCountList() {
        return groupCountList;
    }

    public void setGroupCountList(List<RetGroupType> groupCountList) {
        this.groupCountList = groupCountList;
    }

    public List<UserCountType> getUserCountList() {
        return userCountList;
    }

    public void setUserCountList(List<UserCountType> userCountList) {
        this.userCountList = userCountList;
    }
}
