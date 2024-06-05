package com.zhj.entity.puke;

import com.zhj.pukeyule.Card;
import com.zhj.pukeyule.CardValue;

import java.util.List;

public class SendCard {
    private List<Card> firstRole;
    private List<Card> secondRole;
    private List<Card> thirdRole;
    private List<Card> sendCard;
    private CardValue cardValue;
    private int lordId;
    private String roomId;


    public SendCard() {
    }

    public SendCard(List<Card> firstRole, List<Card> secondRole, List<Card> thirdRole, List<Card> sendCard,CardValue cardValue,int lordId,String roomId) {
        this.firstRole = firstRole;
        this.secondRole = secondRole;
        this.thirdRole = thirdRole;
        this.sendCard = sendCard;
        this.cardValue = cardValue;
        this.lordId = lordId;
        this.roomId = roomId;
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

    public List<Card> getSendCard() {
        return sendCard;
    }

    public void setSendCard(List<Card> sendCard) {
        this.sendCard = sendCard;
    }

    public CardValue getCardValue() {
        return cardValue;
    }

    public void setCardValue(CardValue cardValue) {
        this.cardValue = cardValue;
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
