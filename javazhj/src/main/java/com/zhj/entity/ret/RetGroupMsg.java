package com.zhj.entity.ret;

import com.zhj.entity.GroupCount;
import com.zhj.entity.UserCount;

import java.util.List;

/**
 * @author 789
 */
public class RetGroupMsg {
    private UserCount userCount;
    private GroupCount groupCount;
    private List<RetGroup> groupMsg;
    private List<RetGroupMember> roleList;
    private List<String> countList;
    private PageMine pageMine;
    private Integer onlineNum;
    private Integer allNum;

    public UserCount getUserCount() {
        return userCount;
    }

    public void setUserCount(UserCount userCount) {
        this.userCount = userCount;
    }

    public GroupCount getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(GroupCount groupCount) {
        this.groupCount = groupCount;
    }

    public List<RetGroup> getGroupMsg() {
        return groupMsg;
    }

    public void setGroupMsg(List<RetGroup> groupMsg) {
        this.groupMsg = groupMsg;
    }

    public List<RetGroupMember> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RetGroupMember> roleList) {
        this.roleList = roleList;
    }

    public List<String> getCountList() {
        return countList;
    }

    public void setCountList(List<String> countList) {
        this.countList = countList;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    public Integer getAllNum() {
        return allNum;
    }

    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }

    public PageMine getPageMine() {
        return pageMine;
    }

    public void setPageMine(PageMine pageMine) {
        this.pageMine = pageMine;
    }
}
