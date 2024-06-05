package com.zhj.entity.puke;

import com.zhj.entity.puke.dic.CardType;
import com.zhj.pukeyule.Card;
import com.zhj.pukeyule.CardValue;
import com.zhj.pukeyule.PuKe;

import java.util.*;
import java.util.stream.Collectors;

/**
 * PukeRoom
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/8/25 20:00
 */
public class PukeRoom {
    public static Map<String,List<UserSend>> room = new HashMap<>();
    public static Map<String,List<String>> initRoom = new HashMap<>();
    private static final String ROOM_SUFFIX = "record";
    private static final int MAX_SIZE = 500;


    public static List<String> printRoomInfoByRoomId(String roomId){
        String key = ROOM_SUFFIX + roomId;
        List<String> initInfo = initRoom.getOrDefault(key,new ArrayList<>());
        if (initInfo.size() == 0){
            return new ArrayList<>();
        }
        List<String> codeList = new ArrayList<>();
         codeList.add("Card[] cardF = " + initInfo.get(0) +";");
         codeList.add("Card[] cardS = " + initInfo.get(1) +";");
         codeList.add("Card[] cardT = " + initInfo.get(2) +";");

        List<String> ret = new ArrayList<>(codeList);
        for (String s : codeList) {
            System.out.println(s);
        }

        System.out.print("房间初始信息:\n");
        for (int i = 0; i < initInfo.size(); i++) {
            String temp = "玩家" + i + "初始化牌:" + initInfo.get(i);
            System.out.println(temp);
            ret.add(temp);
        }


        List<UserSend> sendRecord = room.get(key);
        if (sendRecord == null){
            return new ArrayList<>();
        }

        for (UserSend userSend : sendRecord) {
            if (userSend.getCardIdList().size() == 0){
                System.out.print("玩家"+userSend.getSendId()+"出牌:pass");
                ret.add("玩家"+userSend.getSendId()+"出牌:pass");
                continue;
            }
            System.out.print("玩家"+userSend.getSendId()+"出牌:");
            StringJoiner sj = new StringJoiner("],","{","]}");
            for (int id : userSend.getCardIdList()) {
                sj.add("card["+id);
            }
            System.out.println(sj);
            ret.add("玩家"+userSend.getSendId()+"出牌:" + sj);
        }
        return ret;
    }

    public static void addRecordByRoomId(String roomId, CardValue cardValue,int id){
        if (room.size() > MAX_SIZE){
            room.clear();
            initRoom.clear();
        }
        String key = ROOM_SUFFIX+roomId;
        List<UserSend> roomInfo = room.get(key);
        if (roomInfo == null){
            return;
        }
        UserSend userSend = new UserSend();
        if (cardValue == null || !cardValue.getCheck()){
            userSend.setSendId(id);
            userSend.setCardIdList(new ArrayList<>());
            roomInfo.add(userSend);
            return;
        }
        List<Card> cardList = new ArrayList<>(cardValue.getCard());
        userSend.setSendId(cardValue.getSendId());
        userSend.setCardIdList(cardList.stream().map(Card::getId).collect(Collectors.toList()));
        roomInfo.add(userSend);
    }

    public static void initRoom(String roomId,List<List<Card>> allList){
        String key = ROOM_SUFFIX+roomId;
        room.put(key,new ArrayList<>());
        List<String> ret = new ArrayList<>();
        for (List<Card> cards : allList) {
            StringJoiner sj = new StringJoiner("],", "{", "]}");
            for (Card card : cards) {
                sj.add("card[" + card.getId());
            }
            ret.add(sj.toString());
        }
        initRoom.put(key,ret);
    }

    public static Map<Integer,CardValue> getUserLastNotReceive(String roomId,int oppId,boolean isFriend){
        List<UserSend> userSends = room.get(ROOM_SUFFIX+roomId);
        Map<Integer,CardValue> map = new HashMap<>(16);
        if (userSends == null || userSends.size() == 0){
            return map;
        }
        boolean flag = true;
        for (int i = userSends.size() - 1; i >= 0 ; i--) {
            if (userSends.get(i).sendId == oppId && userSends.get(i).getCardIdList().size() != 0 && !isFriend){
                break;
            }
            if (flag && userSends.get(i).sendId == oppId){
                    flag = false;
                    continue;
            }

            if (!flag){
                if (userSends.get(i).getCardIdList().size() == 0){
                    continue;
                }
                List<Card> ret = new ArrayList<>();
                for (int j = 0; j < userSends.get(i).getCardIdList().size(); j++) {
                    ret.add(PuKe.card[userSends.get(i).getCardIdList().get(j)]);
                }
                CardValue cardValue = PuKe.getCardType(ret, userSends.get(i).getSendId());
                map.put(cardValue.getType().getCode(),findLessCardValue(map.get(cardValue.getType().getCode()),cardValue));
                flag = true;
            }
        }

        return map;
    }

    public static List<List<Card>> packageList(List<Card> one, List<Card> two, List<Card> three){
        List<List<Card>> ret = new ArrayList<>();
        ret.add(one);
        ret.add(two);
        ret.add(three);
        return ret;
    }

    public static CardValue findLessCardValue(CardValue cardValue,CardValue nextCardValue){
        if (cardValue == null || !cardValue.getCheck()){
            return nextCardValue;
        }
        return cardValue.getValue() > nextCardValue.getValue()?nextCardValue:cardValue;
    }

   static class UserSend{
       private int sendId;
       private List<Integer> cardIdList;

       public int getSendId() {
           return sendId;
       }

       public void setSendId(int sendId) {
           this.sendId = sendId;
       }

       public List<Integer> getCardIdList() {
           return cardIdList;
       }

       public void setCardIdList(List<Integer> cardIdList) {
           this.cardIdList = cardIdList;
       }
   }


}
