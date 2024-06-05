package com.zhj.entity.ret;

import java.util.List;

/**
 * @author 789
 */
public class ActiveLb {
    private List<RetActive> threeList;
    private List<RetActive> otherList;

    public List<RetActive> getThreeList() {
        return threeList;
    }

    public void setThreeList(List<RetActive> threeList) {
        this.threeList = threeList;
    }

    public List<RetActive> getOtherList() {
        return otherList;
    }

    public void setOtherList(List<RetActive> otherList) {
        this.otherList = otherList;
    }
}
