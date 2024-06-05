package com.zhj.pukeyule;

/**
 * @author 789
 */
public class Card {
   private String type;
   private int value;
   private String detail;
   private int id;
   public static final String HEITAO = "黑桃";
   public static final String YINHUA = "樱花";
   public static final String FANGKUAI = "方块";
   public static final String HONGTAO = "红桃";
   public static final String WANG = "王";

    public Card(String type,int value ,String detail,int id) {
        this.type = type;
        this.value = value;
        this.detail = detail;
        this.id = id;
    }

    public Card() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Card{" +
                "type='" + type + '\'' +
                ", value=" + value +
                ", detail='" + detail + '\'' +
                ", id=" + id +
                '}';
    }


}
