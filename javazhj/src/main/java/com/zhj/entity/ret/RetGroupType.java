package com.zhj.entity.ret;

import com.zhj.entity.GroupCount;

import java.util.List;

/**
 * @author 789
 */
public class RetGroupType {
    private List<GroupCount> groupCount;
    private String shipType;

    public List<GroupCount> getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(List<GroupCount> groupCountList) {
        this.groupCount = groupCountList;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }
}
