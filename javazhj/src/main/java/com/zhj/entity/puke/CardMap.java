package com.zhj.entity.puke;

import com.zhj.pukeyule.Card;
import com.zhj.pukeyule.CardValue;

import java.util.Map;

/**
 * CardMap
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/7/24 20:55
 */
public class CardMap {
   private Map<Integer, Integer> cardRankMap;
   private int maxValue;
   private int minValue;
   private int size;

    public Map<Integer, Integer> getCardRankMap() {
        return cardRankMap;
    }

    public void setCardRankMap(Map<Integer, Integer> cardRankMap) {
        this.cardRankMap = cardRankMap;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public  int compareRank(CardValue cardValue,CardValue otherCardValue){
        return getMyNotExistValue(cardValue) - getMyNotExistValue(cardValue) ;
    }
    public  int compareRank(Card card, Card otherCard){
        return getMyNotExistValue(card) - cardRankMap.get(otherCard.getValue());
    }
    public  int compareRank(Card card, CardValue cardValue){
        return getMyNotExistValue(card) - getMyNotExistValue(cardValue) ;
    }

    public  int compareRankOfMax(CardValue otherCardValue){
        return cardRankMap.get(maxValue) - getMyNotExistValue(otherCardValue);
    }

    public  int compareRankOfMax(Card card){
        return cardRankMap.get(maxValue) - getMyNotExistValue(card.getValue());
    }

    public  int compareRankOfMin(CardValue otherCardValue){
        return getMyNotExistValue(otherCardValue) - cardRankMap.get(minValue);
    }

    public  int compareRankOfMin(Card card){
        return   getMyNotExistValue(card.getValue()) - cardRankMap.get(minValue);
    }

    public int getMyNotExistValue(CardValue cardValue){
       return getMyNotExistValue(cardValue.getValue());
    }

    public int getMyNotExistValue(int val){
        int value = cardRankMap.getOrDefault(val,-1);
        int count = 0;
        while (value == -1 ){
            count++;
            value = cardRankMap.getOrDefault(val - count,-1);
            if (count >= 20){
                break;
            }
        }
        return value == -1 ? 0 : value;
    }

    public int getMyNotExistValue(Card card){
        return getMyNotExistValue(card.getValue());
    }

    public int twoAndThirdMapSize(){
        return size / 3  * 2;
    }

    public int thirdMapSize(){
        return size / 3 ;
    }

    public int halfMapSize(){
        return size / 2;
    }

    public int fourMapSize(){
        return size / 4;
    }

    public int fiveMapSize(){
        return size / 5;
    }


}
