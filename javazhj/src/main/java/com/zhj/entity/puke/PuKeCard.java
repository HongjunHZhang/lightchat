package com.zhj.entity.puke;

import com.zhj.pukeyule.Card;

import java.util.List;

/**
 * @author 789
 */
public class PuKeCard {
    private List<Card> firstRole;
    private List<Card> secondRole;
    private List<Card> thirdRole;
    private List<Card> lordRole;
    private int firstLord;
    private String roomId;

    public PuKeCard(List<Card> firstRole, List<Card> secondRole, List<Card> thirdRole, List<Card> lordRole, int firstLord,String roomId) {
        this.firstRole = firstRole;
        this.secondRole = secondRole;
        this.thirdRole = thirdRole;
        this.lordRole = lordRole;
        this.firstLord = firstLord;
        this.roomId = roomId;
    }

    public PuKeCard() {
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

    public List<Card> getLordRole() {
        return lordRole;
    }

    public void setLordRole(List<Card> lordRole) {
        this.lordRole = lordRole;
    }

    public int getFirstLord() {
        return firstLord;
    }

    public void setFirstLord(int firstLord) {
        this.firstLord = firstLord;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public static String createRoom(){
        return String.valueOf(System.currentTimeMillis());
    }

}
