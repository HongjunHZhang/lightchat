package com.zhj.entity.puke;

import com.zhj.pukeyule.Card;

import java.util.List;

public class Lord {
    private int[] choose;
    private List<List<Card>> cardRole;
    private int firstLord;
    private int roleChoose;
    private int chooseIndex;
    private boolean hasChoose;
    private String roomId;

    public Lord(int[] choose, List<List<Card>> cardRole, int firstLord,int roleChoose,int chooseIndex,boolean hasChoose,String roomId) {
        this.choose = choose;
        this.cardRole = cardRole;
        this.firstLord = firstLord;
        this.roleChoose = roleChoose;
        this.chooseIndex = chooseIndex;
        this.hasChoose = hasChoose;
        this.roomId = roomId;
    }

    public Lord() {
    }

    public int[] getChoose() {
        return choose;
    }

    public void setChoose(int[] choose) {
        this.choose = choose;
    }

    public List<List<Card>> getCardRole() {
        return cardRole;
    }

    public void setCardRole(List<List<Card>> cardRole) {
        this.cardRole = cardRole;
    }

    public int getFirstLord() {
        return firstLord;
    }

    public void setFirstLord(int firstLord) {
        this.firstLord = firstLord;
    }

    public int getRoleChoose() {
        return roleChoose;
    }

    public void setRoleChoose(int roleChoose) {
        this.roleChoose = roleChoose;
    }

    public int getChooseIndex() {
        return chooseIndex;
    }

    public void setChooseIndex(int chooseIndex) {
        this.chooseIndex = chooseIndex;
    }

    public boolean isHasChoose() {
        return hasChoose;
    }

    public void setHasChoose(boolean hasChoose) {
        this.hasChoose = hasChoose;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
