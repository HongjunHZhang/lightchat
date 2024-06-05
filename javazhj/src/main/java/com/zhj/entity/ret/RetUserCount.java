package com.zhj.entity.ret;

import com.zhj.entity.UserCount;

/**
 * @author 789
 */
public class RetUserCount {
    private UserCount userCount;
    private String ship;

    public UserCount getUserCount() {
        return userCount;
    }

    public void setUserCount(UserCount userCount) {
        this.userCount = userCount;
    }

    public String getShip() {
        return ship;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }
}
