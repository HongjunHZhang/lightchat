package com.zhj.pukeyule;

import com.zhj.entity.puke.dic.CardType;
import com.zhj.util.ListUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 789
 */
public class CardValue {
    private Boolean check;
    private int value;
    private CardType type;
    private int num;
    private List<Card> card;
    private List<Card> takeCard;
    private int sendId;

    public CardValue(Boolean check, int value,CardType type,int num, List<Card> card) {
        this.check = check;
        this.value = value;
        this.type = type;
        this.num = num;
        this.card = card;
    }

    public CardValue(Boolean check, int value,CardType type,int num, List<Card> card,List<Card> takeCard,int sendId) {
        this.check = check;
        this.value = value;
        this.type = type;
        this.num = num;
        this.card = card;
        this.takeCard = takeCard;
        this.sendId = sendId;
    }

    public CardValue() {
    }
    public static CardValue kingBomb(List<Card> cardList,int id){
        return new CardValue(true, cardList.get(0).getValue(),CardType.KING_BOMB, cardList.size(), cardList,null,id);
    }

    public static CardValue zhDan(List<Card> cardList,int id){
        return new CardValue(true, cardList.get(0).getValue(),CardType.BOMB, cardList.size(), cardList,null,id);
    }

    public static CardValue sunZi(List<Card> cardList,int id){
        return new CardValue(true, cardList.get(cardList.size()-1).getValue(),CardType.SUN_ZI, cardList.size(), cardList,null,id);
    }
    public static CardValue lianDui(List<Card> cardList,int id){
        return new CardValue(true, cardList.get(cardList.size()-1).getValue(),CardType.LIAN_DUI, cardList.size(), cardList,null,id);
    }
    public static CardValue fourTakeTwo(List<Card> cardList,List<Card> takeList,int id){
        return new CardValue(true,cardList.get(0).getValue(),CardType.FOUR_TAKE_TWO,cardList.size()+takeList.size(),cardList,takeList,id);
    }

    public static CardValue threeTakeOne(List<Card> cardList,List<Card> takeList,int id)  {
        if (takeList != null && takeList.size() > 0 && cardList.size() >0){
            if (cardList.get(0).getValue() == takeList.get(0).getValue()){
                StringBuilder ret = new StringBuilder("三带一异常");
                for (Card card : cardList) {
                    ret.append(card.getDetail()).append(",");
                }
                for (Card card : takeList) {
                    ret.append(card.getDetail()).append(",");
                }
                return new CardValue(true,cardList.get(0).getValue(),CardType.THREE_TAKE_ONE,cardList.size(),cardList,null,id);
            }

        }
        if (takeList == null){
            takeList = new ArrayList<>();
        }
        return new CardValue(true,cardList.get(0).getValue(),CardType.THREE_TAKE_ONE,cardList.size()+takeList.size(),cardList,takeList.size() == 0?null:takeList,id);
    }

    public static CardValue airPlan(List<Card> cardList,List<Card> takeList,int id){
        return new CardValue(true,cardList.get(cardList.size()-1).getValue(),CardType.AIR_PLAN,cardList.size()+takeList.size(),cardList,takeList.size() == 0?null:takeList,id);
    }

    public static CardValue doubleCard(List<Card> card,int id){
        return new CardValue(true,card.get(0).getValue(),CardType.DOUBLE_CARD,card.size(),card,null,id);
    }
    public static CardValue singleCard(List<Card> card,int id){
        return new CardValue(true,card.get(0).getValue(),CardType.SINGLE_CARD,card.size(),card,null,id);
    }

    public static CardValue maxValue(){
        return new CardValue(true,100,CardType.UN_KNOW,0,null,null,-1);
    }

    public static CardValue trueWay(){
        return new CardValue(true,0,CardType.TRUE_TYPE,0,null,null,-1);
    }

    public static CardValue errorWay(){
        return new CardValue(false,0,CardType.UN_KNOW,0,null,null,-1);
    }

    public Boolean getCheck() {
        return check != null && check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public int getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<Card> getTakeCard() {
        return takeCard;
    }

    public void setTakeCard(List<Card> takeCard) {
        this.takeCard = takeCard;
    }

    public List<Card> getCard() {
        return card;
    }

    public void setCard(List<Card> card) {
        this.card = card;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getSendId() {
        return sendId;
    }

    public void setSendId(int sendId) {
        this.sendId = sendId;
    }

    @Override
    public String toString() {
        return "CardValue{" +
                "check=" + check +
                ", value=" + value +
                ", type='" + type + '\'' +
                ", num=" + num +
                ", card=" + card +
                ", takeCard=" + takeCard +
                '}';
    }
}
