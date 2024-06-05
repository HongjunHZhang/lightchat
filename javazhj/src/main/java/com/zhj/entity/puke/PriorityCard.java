package com.zhj.entity.puke;

import com.zhj.pukeyule.Card;

import java.util.List;
import java.util.PriorityQueue;

/**
 * ProrityCard
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/8/10 10:31
 */
public class PriorityCard {
    PriorityQueue<Integer> singleMaxQueue;
    PriorityQueue<Integer> doubleMaxQueue;
    PriorityQueue<Integer> threeMaxQueue;
    PriorityQueue<Integer> fourMaxQueue;

    public PriorityQueue<Integer> getSingleMaxQueue() {
        return singleMaxQueue;
    }

    public void setSingleMaxQueue(PriorityQueue<Integer> singleMaxQueue) {
        this.singleMaxQueue = singleMaxQueue;
    }

    public PriorityQueue<Integer> getDoubleMaxQueue() {
        return doubleMaxQueue;
    }

    public void setDoubleMaxQueue(PriorityQueue<Integer> doubleMaxQueue) {
        this.doubleMaxQueue = doubleMaxQueue;
    }

    public PriorityQueue<Integer> getThreeMaxQueue() {
        return threeMaxQueue;
    }

    public void setThreeMaxQueue(PriorityQueue<Integer> threeMaxQueue) {
        this.threeMaxQueue = threeMaxQueue;
    }

    public PriorityQueue<Integer> getFourMaxQueue() {
        return fourMaxQueue;
    }

    public void setFourMaxQueue(PriorityQueue<Integer> fourMaxQueue) {
        this.fourMaxQueue = fourMaxQueue;
    }

    public static PriorityCard parseSpecialToPriorityCard(SpecialList specialList){
        PriorityQueue<Integer> singleMaxQueue = new PriorityQueue<>((t1,t2)->t2-t1);
        PriorityQueue<Integer> doubleMaxQueue = new PriorityQueue<>((t1,t2)->t2-t1);
        PriorityQueue<Integer> threeMaxQueue = new PriorityQueue<>((t1,t2)->t2-t1);
        PriorityQueue<Integer> fourMaxQueue = new PriorityQueue<>((t1,t2)->t2-t1);
        for (List<Card> list : specialList.getCardCountList()) {
            if (list == null){
                continue;
            }
            if (list.size() >= 1){
                singleMaxQueue.add(list.get(0).getValue());
            }
            if (list.size() >= 2){
                doubleMaxQueue.add(list.get(0).getValue());
            }
            if (list.size() >= 3){
                threeMaxQueue.add(list.get(0).getValue());
            }
            if (list.size() == 4){
                fourMaxQueue.add(list.get(0).getValue());
            }
        }
        PriorityCard priorityCard = new PriorityCard();
        priorityCard.setSingleMaxQueue(singleMaxQueue);
        priorityCard.setDoubleMaxQueue(doubleMaxQueue);
        priorityCard.setThreeMaxQueue(threeMaxQueue);
        priorityCard.setFourMaxQueue(fourMaxQueue);
        return priorityCard;
    }


}
