package com.zhj.entity.puke;

import com.zhj.pukeyule.Card;

import java.util.List;

/**
 * @author 789
 */
public class AutoSendCard {
    private List<Card> firstRole;
    private List<Card> secondRole;
    private List<Card> thirdRole;
    private int lunCi;
    private int lordId;
    private String roomId;

    public AutoSendCard(List<Card> firstRole, List<Card> secondRole, List<Card> thirdRole, int lunCi,int lordId,String roomId) {
        this.firstRole = firstRole;
        this.secondRole = secondRole;
        this.thirdRole = thirdRole;
        this.lunCi = lunCi;
        this.lordId = lordId;
        this.roomId = roomId;
    }

    public AutoSendCard() {
    }

    public List<Card> getFirstRole() {
        return firstRole;
    }

    public void setFirstRole(List<Card> firstRole) {
        this.firstRole = firstRole;
    }

    public List<Card> getSecondRole() {
        return secondRole;
    }

    public void setSecondRole(List<Card> secondRole) {
        this.secondRole = secondRole;
    }

    public List<Card> getThirdRole() {
        return thirdRole;
    }

    public void setThirdRole(List<Card> thirdRole) {
        this.thirdRole = thirdRole;
    }

    public int getLunCi() {
        return lunCi;
    }

    public void setLunCi(int lunCi) {
        this.lunCi = lunCi;
    }

    public int getLordId() {
        return lordId;
    }

    public void setLordId(int lordId) {
        this.lordId = lordId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
