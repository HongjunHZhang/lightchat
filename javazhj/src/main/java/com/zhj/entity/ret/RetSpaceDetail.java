package com.zhj.entity.ret;

import com.zhj.entity.SpaceCount;
import com.zhj.entity.UserCount;

/**
 * RetSpaceDetail
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2023/2/6 18:16
 */
public class RetSpaceDetail {
   private SpaceCount spaceCount;
    /**
     * 0代表不是,1代表是
     */
   private int isMaster;
    /**
     * 0代表不是,1代表是
     */
   private int isFriend;

   private UserCount userCount;


    public SpaceCount getSpaceCount() {
        return spaceCount;
    }

    public void setSpaceCount(SpaceCount spaceCount) {
        this.spaceCount = spaceCount;
    }

    public int getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(int isMaster) {
        this.isMaster = isMaster;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(int isFriend) {
        this.isFriend = isFriend;
    }

    public UserCount getUserCount() {
        return userCount;
    }

    public void setUserCount(UserCount userCount) {
        this.userCount = userCount;
    }

}
