package com.zhj.entity.puke.dic;

import com.zhj.pukeyule.CardValue;

/**
 * CardType
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/7/22 22:07
 */
public enum CardType {
    /**
     * 卡牌型号
     */
    SINGLE_CARD(1,"单牌"),
    DOUBLE_CARD(2,"对子"),
    THREE_TAKE_ONE(3,"三带一"),
    AIR_PLAN(4,"飞机"),
    FOUR_TAKE_TWO(5,"四带二"),
    SUN_ZI(6,"顺子"),
    LIAN_DUI(7,"连对"),
    BOMB(8,"炸弹"),
    KING_BOMB(9,"王炸"),
    UN_KNOW(-1,"未知"),
    TRUE_TYPE(-2,"正确"),
    ;

    private int code;
    private  String msg;

    CardType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static boolean isSameType(CardType cardType,CardType otherCardType){
        return cardType.getCode() == otherCardType.getCode();
    }

    public static boolean isSameType(CardValue cardValue, CardType otherCardType){
        return cardValue.getType().getCode() == otherCardType.getCode();
    }
}
