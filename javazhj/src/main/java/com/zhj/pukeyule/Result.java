package com.zhj.pukeyule;

/**
 * @author 789
 */
public class Result {
    String code;
    String msg;
    String cardList;

    public Result(String code, String msg, String cardList) {
        this.code = code;
        this.msg = msg;
        this.cardList = cardList;
    }

    public Result() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCardList() {
        return cardList;
    }

    public void setCardList(String cardList) {
        this.cardList = cardList;
    }
}
