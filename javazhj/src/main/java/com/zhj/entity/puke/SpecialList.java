package com.zhj.entity.puke;

import com.zhj.pukeyule.Card;

import java.util.List;

/**
 * @author 789
 */
public class SpecialList {
    private int size;
    private List<Integer> maxValue;
    private List<List<Card>> doubleList;
    private List<List<Card>> usefulSunZiList;
    private List<List<Card>> sunZiList;
    private List<List<Card>> lianDuiList;
    private List<List<Card>> zhaDanList;
    private List<Card> kingBombList;
    private List<List<Card>> airList;
    private List<List<Card>> airPlanList;
    private List<Card> allList;
    private List<Card> normalList;
    private List<Card> myCard;
    private List<Card> normalAndDouble;
    private List<Card> normalAndThird;
    private int[]  cardCount;
    private List<Card>[] cardCountList;
    private int tempIndex;
    private PriorityCard priorityMaxCard;


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<List<Card>> getSunZiList() {
        return sunZiList;
    }

    public void setSunZiList(List<List<Card>> sunZiList) {
        this.sunZiList = sunZiList;
    }

    public List<List<Card>> getLianDuiList() {
        return lianDuiList;
    }

    public void setLianDuiList(List<List<Card>> lianDuiList) {
        this.lianDuiList = lianDuiList;
    }

    public List<List<Card>> getZhaDanList() {
        return zhaDanList;
    }

    public void setZhaDanList(List<List<Card>> zhaDanList) {
        this.zhaDanList = zhaDanList;
    }

    public List<List<Card>> getAirPlanList() {
        return airPlanList;
    }

    public void setAirPlanList(List<List<Card>> airPlanList) {
        this.airPlanList = airPlanList;
    }

    public List<Card> getAllList() {
        return allList;
    }

    public void setAllList(List<Card> allList) {
        this.allList = allList;
    }

    public List<List<Card>> getAirList() {
        return airList;
    }

    public void setAirList(List<List<Card>> airList) {
        this.airList = airList;
    }

    public List<Card> getNormalList() {
        return normalList;
    }

    public void setNormalList(List<Card> normalList) {
        this.normalList = normalList;
    }

    public List<List<Card>> getDoubleList() {
        return doubleList;
    }

    public void setDoubleList(List<List<Card>> doubleList) {
        this.doubleList = doubleList;
    }

    public List<Card> getMyCard() {
        return myCard;
    }

    public void setMyCard(List<Card> myCard) {
        this.myCard = myCard;
    }

    public List<Card> getNormalAndDouble() {
        return normalAndDouble;
    }

    public void setNormalAndDouble(List<Card> normalAndDouble) {
        this.normalAndDouble = normalAndDouble;
    }

    public List<Integer> getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(List<Integer> maxValue) {
        this.maxValue = maxValue;
    }

    public int[] getCardCount() {
        return cardCount;
    }

    public void setCardCount(int[] cardCount) {
        this.cardCount = cardCount;
    }

    public List<Card>[] getCardCountList() {
        return cardCountList;
    }

    public void setCardCountList(List<Card>[] cardCountList) {
        this.cardCountList = cardCountList;
    }

    public List<Card> getKingBombList() {
        return kingBombList;
    }

    public void setKingBombList(List<Card> kingBombList) {
        this.kingBombList = kingBombList;
    }

    public List<Card> getNormalAndThird() {
        return normalAndThird;
    }

    public void setNormalAndThird(List<Card> normalAndThird) {
        this.normalAndThird = normalAndThird;
    }

    public List<List<Card>> getUsefulSunZiList() {
        return usefulSunZiList;
    }

    public void setUsefulSunZiList(List<List<Card>> usefulSunZiList) {
        this.usefulSunZiList = usefulSunZiList;
    }

    public int getTempIndex() {
        return tempIndex;
    }

    public void setTempIndex(int tempIndex) {
        this.tempIndex = tempIndex;
    }

    public PriorityCard getPriorityMaxCard() {
        return priorityMaxCard;
    }

    public void setPriorityMaxCard(PriorityCard priorityMaxCard) {
        this.priorityMaxCard = priorityMaxCard;
    }
}
