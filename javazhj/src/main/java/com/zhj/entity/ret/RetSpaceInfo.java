package com.zhj.entity.ret;

import com.zhj.entity.SpaceCount;
import com.zhj.entity.UserCount;

import java.util.List;

/**
 * @author 789
 */
public class RetSpaceInfo {
    private List<TalkBox> talkBox;
    private Integer maxPageSize;

    public List<TalkBox> getTalkBox() {
        return talkBox;
    }

    public void setTalkBox(List<TalkBox> talkBox) {
        this.talkBox = talkBox;
    }

    public Integer getMaxPageSize() {
        return maxPageSize;
    }

    public void setMaxPageSize(Integer maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

}
