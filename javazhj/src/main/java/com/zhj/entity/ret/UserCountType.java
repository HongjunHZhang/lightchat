package com.zhj.entity.ret;

import com.zhj.entity.UserCount;

import java.util.List;

/**
 * @author 789
 */
public class UserCountType {
    private List<RetUserCount> userCount;
    private String shipType;
    private Long online;

    public List<RetUserCount> getUserCount() {
        return userCount;
    }

    public void setUserCount(List<RetUserCount> userCount) {
        this.userCount = userCount;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public Long getOnline() {
        return online;
    }

    public void setOnline(Long online) {
        this.online = online;
    }
}
