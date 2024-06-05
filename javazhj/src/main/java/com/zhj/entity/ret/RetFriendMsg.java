package com.zhj.entity.ret;

import com.github.pagehelper.PageInfo;
import com.zhj.entity.FriendMsg;
import com.zhj.entity.UserCount;

import java.util.List;

/**
 * @author 789
 */
public class RetFriendMsg {
    private UserCount userCount;
    private UserCount friendCount;
    private List<FriendMsg> friendMsg;
    PageMine pageMine;

    public UserCount getUserCount() {
        return userCount;
    }

    public void setUserCount(UserCount userCount) {
        this.userCount = userCount;
    }

    public UserCount getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(UserCount friendCount) {
        this.friendCount = friendCount;
    }

    public List<FriendMsg> getFriendMsg() {
        return friendMsg;
    }

    public void setFriendMsg(List<FriendMsg> friendMsg) {
        this.friendMsg = friendMsg;
    }

    public PageMine getPageMine() {
        return pageMine;
    }

    public void setPageMine(PageMine pageMine) {
        this.pageMine = pageMine;
    }
}
