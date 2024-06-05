package com.zhj.entity.puke;

import com.zhj.pukeyule.Card;

import java.util.List;

/**
 * OppInfo
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/8/20 10:58
 */
public class OppInfo {
    private int maxSize;
    private int minSize;

    public OppInfo(int maxSize, int minSize) {
        this.maxSize = maxSize;
        this.minSize = minSize;
    }

    public OppInfo() {
    }

    public static OppInfo instanceOppInfo(List<Card> list,List<Card> tempList){
        return new OppInfo(Math.max(list.size(),tempList.size()),Math.min(list.size() ,tempList.size()));
    }

    public static OppInfo instanceOppInfo(List<Card> list){
        return new OppInfo(list.size(),list.size());
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMinSize() {
        return minSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }
}
