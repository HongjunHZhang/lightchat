package com.zhj.entity.puke;

import com.zhj.pukeyule.Card;
import com.zhj.pukeyule.CardValue;

import java.util.ArrayList;
import java.util.List;

public class RetCard {
    private List<Card> firstRole;
    private List<Card> secondRole;
    private List<Card> thirdRole;
    private List<Card> firstSend;
    private List<List<Card>> secondSend;
    private List<List<Card>> thirdSend;
    private CardValue cardValue;
    private int winner;

    public RetCard(List<Card> firstRole, List<Card> secondRole, List<Card> thirdRole, List<Card> firstSend, List<List<Card>> secondSend, List<List<Card>> thirdSend,int winner) {
        this.firstRole = firstRole;
        this.secondRole = secondRole;
        this.thirdRole = thirdRole;
        this.firstSend = firstSend;
        this.secondSend = secondSend;
        this.thirdSend = thirdSend;
        this.winner = winner;
    }

    public RetCard() {
        this.secondSend = new ArrayList<>();
        this.thirdSend = new ArrayList<>();
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

    public List<Card> getFirstSend() {
        return firstSend;
    }

    public void setFirstSend(List<Card> firstSend) {
        this.firstSend = firstSend;
    }

    public List<List<Card>> getSecondSend() {
        return secondSend;
    }

    public void setSecondSend(List<List<Card>> secondSend) {
        this.secondSend = secondSend;
    }

    public List<List<Card>> getThirdSend() {
        return thirdSend;
    }

    public void setThirdSend(List<List<Card>> thirdSend) {
        this.thirdSend = thirdSend;
    }

    public CardValue getCardValue() {
        return cardValue;
    }

    public void setCardValue(CardValue cardValue) {
        this.cardValue = cardValue;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }
}
