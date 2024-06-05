package com.zhj.entity.puke.dic;

import com.zhj.pukeyule.Card;

import java.util.List;

/**
 * CardType
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/7/22 22:07
 */
public enum WarningLevel {
    /**
     * 危险警告
     */
    SINGLE_WARNING(1,"一张牌警告"),
    DOUBLE_WARNING(2,"两张牌警告"),
    THIRD_WARNING(3,"三张牌警告"),
    FOUR_WARNING(4,"四张牌警告"),
    FIVE_WARNING(5,"五张牌警告"),
    SIMPLE_WARNING(6,"简单警告"),
    SEVEN_WARNING(7,"七张牌警告"),
    HAVE_SOME_WARNING(8,"有一些危险警告"),
    NINE_WARNING(9,"九张牌警告"),
    TEN_WARNING(10,"十张牌警告"),
    ELEVEN_WARNING(11,"十一张牌警告"),
    NO_WARNING(12,"暂时没有危险"),
    SAFE(15,"安全"),
    Error(999,"异常")
    ;

    private int code;
    private  String msg;

    WarningLevel(int code, String msg) {
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

    public static WarningLevel getWarningLevel(int cardNum){
        WarningLevel[] values = WarningLevel.values();
        for (int i = values.length - 1; i >= 0 ; i--) {
            if (cardNum >= values[i].code){
                return values[i];
            }
        }
        return WarningLevel.Error;
    }

    public static WarningLevel getWarningLevel(int cardNum,int otherCardNum){
        return getWarningLevel(Math.min(cardNum,otherCardNum));
    }

    public static WarningLevel getWarningLevel(List<Card> list){
        return getWarningLevel(list.size());
    }
    public static WarningLevel getWarningLevel(List<Card> list,List<Card> otherList){
        return getWarningLevel(list.size(),otherList.size());
    }

    public static boolean isSameLevel(WarningLevel warningLevel,WarningLevel level){
        return warningLevel.getCode() == level.getCode();
    }
    public static boolean levelHigher(WarningLevel warningLevel,WarningLevel level){
        return warningLevel.getCode() >= level.getCode();
    }


}
