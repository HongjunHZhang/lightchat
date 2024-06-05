package com.zhj.entity.puke;

import com.zhj.entity.puke.dic.CardType;
import com.zhj.entity.puke.dic.WarningLevel;
import com.zhj.pukeyule.Card;
import com.zhj.pukeyule.CardValue;
import com.zhj.pukeyule.PuKe;
import com.zhj.util.ListUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * PukeRole
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/7/22 20:59
 */
public class PukeRole {

    private boolean notCareBomb,f;

    public static int getMyFriendId(int id, int lordId){
        return id == lordId ? -1:3-id-lordId;
    }

    public static boolean isMyFriendSend(int lastId,int lordId,int myId){
        return myId != lordId && lastId != lordId;
    }

    public static boolean nextOpp(int id,int lordId){
        return (id + 1) % 3 == lordId;
    }

    public static boolean friendWillGetRounds(int lastId,int lordId,int myId){
        return lastId != lordId && lastId != myId && (myId+1) % 3 != lordId && lordId != myId;
    }

    public static WarningLevel getWarningLevel(List<List<Card>> allCard, int lordId, int id,int sendId){
       if (id == lordId){
         return  WarningLevel.getWarningLevel(allCard.get(sendId).size(),allCard.get(sendId).size());
       }
        return  WarningLevel.getWarningLevel(allCard.get(lordId).size());
    }

    public static OppInfo getOppInfo(List<List<Card>> allCard, int lordId, int id){
        if (id == lordId){
            return OppInfo.instanceOppInfo(allCard.get((id+1)%3),allCard.get((id+2)%3));
        }
            return  OppInfo.instanceOppInfo(allCard.get(lordId));
    }

    public static boolean myCardCouldReceive(CardValue cardValue,SpecialList specialList,boolean notCareBomb){
        int size = specialList.getMyCard().size();
        if (CardType.isSameType(cardValue,CardType.KING_BOMB)){
            return false;
        }
        if (CardType.isSameType(cardValue,CardType.BOMB)){
            for (List<Card> cardList : specialList.getZhaDanList()) {
                if (cardList.get(0).getValue() > cardValue.getValue()){
                    return true;
                }
            }
            if (specialList.getKingBombList().size() != 0){
                return true;
            }
            return false;
        }

        if (specialList.getZhaDanList().size() + specialList.getKingBombList().size() != 0 && !notCareBomb){
            return true;
        }

        if (size < cardValue.getNum()){
            return false;
        }

        if (CardType.isSameType(cardValue,CardType.SINGLE_CARD)){
            Card myCard = ListUtil.getCardTail(specialList.getMyCard());
            if (myCard == null || myCard.getValue() <= cardValue.getValue()){
                return false;
            }
        }

        if (CardType.isSameType(cardValue,CardType.DOUBLE_CARD)){
            int maxDouble = PukeRole.getMaxByTimes(specialList,2) ;
            if (maxDouble <= cardValue.getValue()){
                return false;
            }
        }
        if (CardType.isSameType(cardValue,CardType.LIAN_DUI)){
            for (List<Card> cardList : specialList.getLianDuiList()) {
                 if (cardList.size() >= cardValue.getNum() && cardList.get(cardList.size()-1).getValue() > cardValue.getValue())  {
                     return true;
                 }
            }
            return false;
        }

        if (CardType.isSameType(cardValue,CardType.THREE_TAKE_ONE)){
           int maxThirdList = PukeRole.getMaxByTimes(specialList,3);
            if (maxThirdList <= cardValue.getValue()){
                return false;
            }
        }

        if (CardType.isSameType(cardValue,CardType.AIR_PLAN)){
            if (cardValue.getNum() % 5 == 0 && specialList.getDoubleList().size() + specialList.getAirList().size() - cardValue.getNum() / 5 < cardValue.getNum()  / 5){
                return false;
            }
            if (specialList.getMyCard().size() < cardValue.getNum()){
                return false;
            }

            int airSize = cardValue.getNum() % 4 == 0 ? cardValue.getNum() / 4 : cardValue.getNum() / 5;
            for (List<Card> cardList : specialList.getAirPlanList()) {
               if (cardList.size() / 3 >= airSize && cardList.get(cardList.size()-1).getValue() > cardValue.getValue()){
                   return true;
               }
            }
            return false;
        }

        if (CardType.isSameType(cardValue,CardType.FOUR_TAKE_TWO)){
            return false;
        }
        if (CardType.isSameType(cardValue,CardType.SUN_ZI)){
            for (List<Card> cardList : specialList.getSunZiList()) {
               if (cardList.size() >= cardValue.getNum() && cardList.get(cardList.size()-1).getValue() > cardValue.getValue()){
                   return true;
               }
            }
            return false;
        }

        return !CardType.isSameType(cardValue, CardType.FOUR_TAKE_TWO);
    }

    public CardValue afterReceiveICouldSendAll(CardValue cardValue){
        return null;
    }

    public static CardValue autoSendRole(SpecialList specialList,SpecialList otherSpecialList,List<List<Card>> allList,int id,int lordId,WarningLevel warningLevel,CardMap cardMap){
        CardValue cardValue =  firstSendSunZi(specialList,otherSpecialList,id,warningLevel);
        if (cardValue.getCheck()){
            return cardValue;
        }

        cardValue = firstSendLianDui(specialList,otherSpecialList,id,warningLevel);
        if (cardValue.getCheck()){
            return cardValue;
        }

        List<List<Card>> lists = new ArrayList<>();
        lists.add(allList.get(id));
        CardMap cardMapMy = PukeRole.getCardRankMap(lists);
        cardValue = firstSendAirPlan(specialList,otherSpecialList,id,warningLevel,cardMapMy);
        if (cardValue.getCheck()){
            return cardValue;
        }

        cardValue = firstSendThreeTakeOne(specialList,otherSpecialList,id,warningLevel);
        if (cardValue.getCheck()){
            return cardValue;
        }



        int friendId = PukeRole.getMyFriendId(id,lordId);
        //帮助友方的出法
        if (friendId != -1 && !WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel)) {
            int friendSize = allList.get(friendId).size();
            List<Card> retList = new ArrayList<>();
            if (friendSize == 1) {
                if (specialList.getMyCard().size() > 0 && otherSpecialList.getMyCard().size() > 0) {
                 if (specialList.getNormalList().size() > 0 && specialList.getNormalList().get(0).getValue() < ListUtil.getCardTail(otherSpecialList.getMyCard()).getValue()){
                     if (ListUtil.getCardTail(specialList.getMyCard()).getValue() > ListUtil.getCardTail(otherSpecialList.getMyCard()).getValue()) {
                         retList.add(specialList.getNormalList().get(0));
                         return CardValue.singleCard(retList, id);
                     }

                 }

                    if (ListUtil.getCardTail(specialList.getMyCard()).getValue() > ListUtil.getCardTail(otherSpecialList.getMyCard()).getValue()) {
                        retList.add(specialList.getMyCard().get(0));
                        return CardValue.singleCard(retList, id);
                    }
                }
            }

            if (friendSize == 2 && specialList.getDoubleList().size() > 0 && otherSpecialList.getDoubleList().size() > 0) {
                if (ListUtil.getCardListTail(specialList.getDoubleList()).get(0).getValue() > ListUtil.getCardListTail(otherSpecialList.getDoubleList()).get(0).getValue()) {
                    if (specialList.getDoubleList().get(0).get(0).getValue() < ListUtil.getCardListTail(otherSpecialList.getDoubleList()).get(0).getValue()){
                        return CardValue.doubleCard(specialList.getDoubleList().get(0),id);
                    }
                }
            }
        }


         if (WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel)){
             cardValue = firstSendFourTakeTwo(specialList,id,warningLevel);
             if (cardValue.getCheck()){
                 return cardValue;
             }

             cardValue = firstSendDouble(specialList,otherSpecialList,id,warningLevel,true);
             if (cardValue.getCheck()){
                 return cardValue;
             }
             cardValue = firstSendSingle(specialList,otherSpecialList,id,warningLevel);
             if (cardValue.getCheck()){
                 return cardValue;
             }
         }

         if (WarningLevel.isSameLevel(WarningLevel.DOUBLE_WARNING,warningLevel)){
             if(specialList.getDoubleList().size() > 0 && cardMapMy.compareRankOfMin(specialList.getDoubleList().get(0).get(0)) < 2) {
                 cardValue = CardValue.doubleCard(specialList.getDoubleList().get(0), id);
                 if (!PukeRole.myCardCouldReceive(cardValue, otherSpecialList, true)) {
                     return cardValue;
                 }
             }
             cardValue = firstSendSingle(specialList,otherSpecialList,id,warningLevel);
             if (cardValue.getCheck()){
                 return cardValue;
             }
             cardValue = firstSendDouble(specialList,otherSpecialList,id,warningLevel,true);
             if (cardValue.getCheck()){
                 return cardValue;
             }

         }

         List<List<Card>> tempList = new ArrayList<>();
         tempList.add(allList.get(id));

         boolean shouldFirstSendSingle = PukeUtil.shouldSendSingle(specialList,otherSpecialList,cardMap,warningLevel,cardMapMy);
         if (shouldFirstSendSingle){
             cardValue = firstSendSingle(specialList,otherSpecialList,id,warningLevel);
             if (cardValue.getCheck()){
                 return cardValue;
             }
             cardValue = firstSendDouble(specialList,otherSpecialList,id,warningLevel,true);
             if (cardValue.getCheck()){
                 return cardValue;
             }
         }else{
             cardValue = firstSendDouble(specialList,otherSpecialList,id,warningLevel,true);
             if (cardValue.getCheck()){
                 return cardValue;
             }
             cardValue = firstSendSingle(specialList,otherSpecialList,id,warningLevel);
             if (cardValue.getCheck()){
                 return cardValue;
             }
         }

        List<Card> ret = new ArrayList<>();
         if (specialList.getDoubleList().size() == 0 || specialList.getDoubleList().get(0).get(0).getValue() > specialList.getMyCard().get(0).getValue()){
             ret.add(specialList.getMyCard().get(0));
             return CardValue.singleCard(ret,id);
         }else {
             if (specialList.getDoubleList().size() != 0){
                 return CardValue.doubleCard(specialList.getDoubleList().get(0),id);
             }
         }
        ret.add(specialList.getMyCard().get(0));
         return CardValue.singleCard(ret,id);

    }

    public static boolean couldSendAll(SpecialList specialList,SpecialList otherSpecialList,int id){
        boolean noBombProbable = otherSpecialList.getKingBombList().size() / 2 + otherSpecialList.getZhaDanList().size() < 3;
        if (!noBombProbable){
            return false;
        }
        int times = getCouldBeReceiveTimes(specialList,otherSpecialList,id);
        boolean signal = false;
        if (times <= 3){
            if ((int)(Math.random()*times) == 0){
                signal = true;
            }
        }

        if (times == 0){
            signal = true;
        }

        if (times > 3 || !signal){
            return false;
        }
        return true;
    }

    public static CardValue sendSingleFromValue(SpecialList specialList,SpecialList otherSpecialList,CardValue cardValue,List<Card> retList,int id,int value){
        List<Card>[] cardCountList = specialList.getCardCountList();
//        int allSize = otherSpecialList.getCardCountList()[15] == null?0:otherSpecialList.getCardCountList()[15].size();
//        allSize += otherSpecialList.getCardCountList()[16] == null?0:otherSpecialList.getCardCountList()[16].size();
        for (int i = value; i < cardCountList.length; i++) {
            if (value == 16 && (specialList.getCardCountList()[15] !=null && specialList.getCardCountList()[15].size() > 1)){
                return CardValue.errorWay();
            }
            if (value == 17 && (specialList.getCardCountList()[16] !=null && specialList.getCardCountList()[16].size() > 0)){
                    return CardValue.errorWay();
            }
            List<Card> list = cardCountList[i];
            if (list == null || list.size() > 3 || (list.size() > 2 && i < 9)){
                continue;
            }
            if (i > cardValue.getValue()){
                retList.add(cardCountList[i].get(0));
                return CardValue.singleCard(retList,id);
            }
        }

        return CardValue.errorWay();
    }

    public static CardValue couldHelpFriend(SpecialList specialList,SpecialList otherSpecialList,List<List<Card>> allList,int id,int lordId,String roomId,WarningLevel warningLevel){
     boolean  friendWillGetRounds =  (id+1) % 3 != lordId;
     int friendId = 3 - id -lordId;
     int friendSize = allList.get((id+1)%3).size();
     if (!friendWillGetRounds || friendSize > 2){
         return CardValue.errorWay();
     }
     Map<Integer, CardValue> userLastNotReceiveMap = PukeRoom.getUserLastNotReceive(roomId, friendId,false);
     if (WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel) && !PuKe.hasLittleSingle(specialList,ListUtil.getCardTail(otherSpecialList.getMyCard()).getValue())){
         return CardValue.errorWay();
     }


     List<Card> retList = new ArrayList<>();
     if (friendSize == 1){
          retList.add(specialList.getMyCard().get(0));
          CardValue cardValue = CardValue.singleCard(retList,id);
         if (myCardCouldReceive(cardValue,otherSpecialList,true) && userLastNotReceiveMap.getOrDefault(CardType.SINGLE_CARD.getCode(),CardValue.maxValue()).getValue() > cardValue.getValue()){
             return  cardValue;
         }
     }

     if (friendSize == 2){
         if (specialList.getDoubleList().size() > 0){
             retList.addAll(specialList.getDoubleList().get(0));
             CardValue cardValue = CardValue.doubleCard(retList,id);
             if (myCardCouldReceive(cardValue,otherSpecialList,true) && userLastNotReceiveMap.getOrDefault(CardType.DOUBLE_CARD.getCode(),CardValue.maxValue()).getValue() > cardValue.getValue()){
                 return  cardValue;
             }
         }
     }

     return CardValue.errorWay();
    }

    public static int getCouldBeReceiveTimes(SpecialList specialList,SpecialList otherSpecialList,int id){
        int mayBeWillBeReceive = 0;
        int count = 0;
        for (List<Card> cardList : specialList.getSunZiList()) {
            count +=  myCardCouldReceive(CardValue.sunZi( cardList, id), otherSpecialList, true) ?1:0;
        }
        mayBeWillBeReceive += count / 2;

        int maxSingle = ListUtil.getCardTail(otherSpecialList.getMyCard()).getValue();
        mayBeWillBeReceive += specialList.getNormalList().stream().filter(t->t.getValue() < maxSingle).count();

        List<Integer> maxValueList = otherSpecialList.getMaxValue();
        int maxDouble = Math.max(maxValueList.get(1),maxValueList.get(2));
        mayBeWillBeReceive += specialList.getDoubleList().stream().filter(t->t.get(0).getValue() < maxDouble).count();

        count = 0;
        int maxThird = maxValueList.get(2);
        for (List<Card> cardList : specialList.getAirList()) {
            if (cardList.get(0).getValue() < maxThird){
                count++;
            }else if (mayBeWillBeReceive + count > 0){
                count--;
            }
        }
        mayBeWillBeReceive += count;
        return mayBeWillBeReceive;
    }

    public static CardValue firstSendSunZi(SpecialList specialList,SpecialList otherSpecialList,int id,WarningLevel warningLevel){
        if (specialList.getUsefulSunZiList().size() == 0){
            return CardValue.errorWay();
        }

        List<Card>  list = specialList.getUsefulSunZiList().get(0);
        if (WarningLevel.isSameLevel(WarningLevel.FIVE_WARNING,warningLevel) && list.size() == 5 && myCardCouldReceive(CardValue.sunZi(list,id),otherSpecialList,false) ){
            return CardValue.errorWay();
        }
        return CardValue.sunZi(list,id);
    }

    public static CardValue firstSendFourTakeTwo(SpecialList specialList,int id,WarningLevel warningLevel){
        if (specialList.getZhaDanList().size() == 0 || specialList.getMyCard().size() < 6){
            return CardValue.errorWay();
        }

        List<Card>  list = specialList.getZhaDanList().get(0);
        List<Card> takeList = new ArrayList<>();
        for (Card card : specialList.getNormalList()) {
            if (takeList.size() >= 2){
                break;
            }
            if (card.getValue() < 17){
                takeList.add(card);
            }
        }
        if (takeList.size() == 2){
            return CardValue.fourTakeTwo(list,takeList,id);
        }else {
            return CardValue.errorWay();
        }
    }

    public static CardValue firstSendThreeTakeOne(SpecialList specialList,SpecialList otherSpecialList,int id,WarningLevel warningLevel){
        if (specialList.getAirList().size() == 0){
            return CardValue.errorWay();
        }

        if (WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel)){
            List<Card> takeList = new ArrayList<>();
            if (specialList.getNormalList().size() > 0){
                takeList.add(specialList.getNormalList().get(0));
                return CardValue.threeTakeOne(specialList.getAirList().get(0),takeList,id);
            }
            if (specialList.getDoubleList().size() > 0){
                return CardValue.threeTakeOne(specialList.getAirList().get(0),specialList.getDoubleList().get(0),id);
            }
            return CardValue.threeTakeOne(specialList.getAirList().get(0),new ArrayList<>(),id);
        }

        List<Card> takeList = new ArrayList<>();
        List<Card> list = specialList.getAirList().get(0);

        if (list.get(0).getValue() > 10 && specialList.getMyCard().size() > 5 && PukeRole.getRank(specialList.getMyCard(),list.get(0)) > 1){
            return CardValue.errorWay();
        }
        boolean hasReceiveRisk = otherSpecialList.getAirList().size() != 0 && ListUtil.getCardListTail(otherSpecialList.getAirList()).get(0).getValue() > list.get(0).getValue();
        if (WarningLevel.isSameLevel(WarningLevel.FIVE_WARNING,warningLevel) ){
            if (hasReceiveRisk){
                for (Card card : specialList.getNormalList()) {
                    if (takeList.size() > 0){
                        break;
                    }
                    if (card.getValue() < 14){
                        takeList.add(card);
                    }
                }
                return CardValue.threeTakeOne(list,takeList,id);
            }
        }

        if (WarningLevel.isSameLevel(WarningLevel.FOUR_WARNING,warningLevel) ){
            if (hasReceiveRisk){
                for (List<Card> card : specialList.getDoubleList()) {
                    if (takeList.size() > 0){
                        break;
                    }
                    if (card.get(0).getValue() < 10){
                        takeList.addAll(card);
                    }
                }
                return CardValue.threeTakeOne(list,takeList,id);
            }

        }

        if (takeList.size() == 0){
            if (specialList.getNormalList().size() != 0 || specialList.getDoubleList().size() != 0) {
                if (specialList.getNormalList().size() == 0 && specialList.getDoubleList().size() != 0) {
                    takeList.addAll(specialList.getDoubleList().get(0));
                }
                if (specialList.getNormalList().size() != 0 && specialList.getDoubleList().size() == 0) {
                    takeList.add(specialList.getNormalList().get(0));
                }
                if (specialList.getNormalList().size() != 0 && specialList.getDoubleList().size() != 0) {
                    if (specialList.getNormalList().get(0).getValue() < 11 || specialList.getNormalList().get(0).getValue() < specialList.getDoubleList().get(0).get(0).getValue()) {
                        takeList.add(specialList.getNormalList().get(0));
                    } else {
                        takeList.addAll(specialList.getDoubleList().get(0));
                    }
                }
            }

        }
        return CardValue.threeTakeOne(list,takeList,id);
    }

    public static CardValue firstSendLianDui(SpecialList specialList,SpecialList otherSpecialList,int id,WarningLevel warningLevel){
        if (specialList.getLianDuiList().size() == 0){
            return CardValue.errorWay();
        }

        if (specialList.getLianDuiList().get(0).get(specialList.getLianDuiList().get(0).size()-1).getValue() < 11){
            return CardValue.lianDui(specialList.getLianDuiList().get(0),id);
        }
        return CardValue.errorWay();
    }

    public static CardValue firstSendAirPlan(SpecialList specialList,SpecialList otherSpecialList,int id,WarningLevel warningLevel,CardMap cardMapMy){
       if (specialList.getAirPlanList().size() == 0){
           return CardValue.errorWay();
       }

       if (specialList.getAirPlanList().get(0).get(specialList.getAirPlanList().get(0).size()-1).getValue() > 10){
           return CardValue.errorWay();
       }

       int size = specialList.getAirPlanList().get(0).size() / 3;
       List<Card> takeList = new ArrayList<>();
        if (specialList.getNormalList().size() >= size){
           if (specialList.getNormalList().get(size-1).getValue() < 12 || cardMapMy.compareRankOfMin(specialList.getNormalList().get(size-1)) < 2 ){
               for (int i = 0; i <= size-1; i++) {
                   takeList.add(specialList.getNormalList().get(i));
               }
               return CardValue.airPlan(specialList.getAirPlanList().get(0),takeList,id);
           }
       }

        if (specialList.getDoubleList().size() >= size){
            if (specialList.getDoubleList().get(size-1).get(0).getValue() < 12 || cardMapMy.compareRankOfMin(specialList.getNormalList().get(size-1)) < 2) {
                for (int i = 0; i <= size-1; i++) {
                    takeList.addAll(specialList.getDoubleList().get(i));
                }
                return CardValue.airPlan(specialList.getAirPlanList().get(0),takeList,id);
            }
        }

        List<Card> tempList = specialList.getNormalAndDouble();
        tempList.sort(Comparator.comparing(Card::getId));
        if (tempList.size() >= size){
            for (int i = 0; i <= size-1; i++) {
                takeList.add(tempList.get(i));
            }
            return CardValue.airPlan(specialList.getAirPlanList().get(0),takeList,id);
        }


        return CardValue.airPlan(specialList.getAirPlanList().get(0),takeList,id);
    }

    public static CardValue firstSendDouble(SpecialList specialList,SpecialList otherSpecialList,int id,WarningLevel warningLevel,boolean doubleCouldReceive){
        if (specialList.getDoubleList().size() == 0){
            return CardValue.errorWay();
        }

        List<Card>  list = specialList.getDoubleList().get(0);
        if (WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel) || !doubleCouldReceive){
            return CardValue.doubleCard(list,id);
        }

        if (WarningLevel.isSameLevel(WarningLevel.DOUBLE_WARNING,warningLevel)){
            CardValue cardValue = CardValue.doubleCard(list,id);
            if (!myCardCouldReceive(cardValue,otherSpecialList,true)){
                return cardValue;
            }else {
                return CardValue.errorWay();
            }
        }

        if (specialList.getDoubleList().size() > 0){
            return CardValue.doubleCard(specialList.getDoubleList().get(0),id);
        }

        return CardValue.errorWay();
    }

    public  static CardValue firstSendSingle(SpecialList specialList,SpecialList otherSpecialList,int id,WarningLevel warningLevel){
        if (specialList.getNormalList().size() == 0 && specialList.getNormalAndDouble().size() == 0){
            return CardValue.errorWay();
        }

        List<Card> cardList = new ArrayList<>();
        if (WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel)){
                cardList.add(specialList.getNormalAndDouble().get(specialList.getNormalAndDouble().size()-1));
                return CardValue.singleCard(cardList,id);
        }


        if (specialList.getNormalList().size() != 0){
            if (otherSpecialList.getMyCard().size() > 0){
                int max = ListUtil.getCardTail(otherSpecialList.getMyCard()).getValue();
                if (ListUtil.getCardTail(specialList.getMyCard()).getValue() > max && (specialList.getNormalList().get(0).getValue() < max || specialList.getDoubleList().size() < 2 || specialList.getNormalList().get(0).getValue() < 11)){
                    cardList.add(specialList.getNormalList().get(0));
                    return CardValue.singleCard(cardList,id);
                }
            }
            if (specialList.getDoubleList().size() != 0 && (specialList.getNormalList().get(0).getValue() - specialList.getDoubleList().get(0).get(0).getValue() > 5 && specialList.getNormalList().get(0).getValue() > 10)){
                cardList.add(specialList.getDoubleList().get(0).get(0));
                return CardValue.singleCard(cardList,id);
            }

            cardList.add(specialList.getNormalList().get(0));
//            if (specialList.getDoubleList().size() > 0 && cardList.get(0).getValue() - specialList.getDoubleList().get(0).get(0).getValue() > 0 && cardList.get(0).getValue() > 10){
//                return CardValue.errorWay();
//            }
            return CardValue.singleCard(cardList,id);
        }

        if (specialList.getDoubleList().size() != 0){
            cardList.add(specialList.getDoubleList().get(0).get(0));
            return CardValue.singleCard(cardList,id);
        }

            return CardValue.errorWay();
    }

    public static boolean noNeedSendKing(int value,int smallCardSize,int bigCardSize){
      return value==15?smallCardSize<2:bigCardSize<2;
    }

    public  static CardMap getCardRankMap(List<List<Card>> allList){
        List<Card> list = new ArrayList<>();
        allList.forEach(list::addAll);
        list.sort(Comparator.comparing(Card::getId));
        Map<Integer,Integer> map = new HashMap<>();
        for (int i = list.size()-1; i >= 0; i--) {
            Integer cardValue = list.get(i).getValue();
            if (!map.containsKey(cardValue)){
                map.put(cardValue,i);
            }
        }
        CardMap cardMap = new CardMap();
        cardMap.setCardRankMap(map);
        cardMap.setMinValue(list.get(0).getValue());
        cardMap.setMaxValue(list.get(list.size()-1).getValue());
        cardMap.setSize(list.size());
        return cardMap;
    }

    public static CardValue dfSendCard(SpecialList specialList,SpecialList otherSpecialList,CardValue previous,WarningLevel warningLevel,int id,boolean notCareBomb,boolean isOpponentSend,long startTime,OppInfo oppInfo){
        CardValue cardValue;
        CardValue nextCardValue = null;
        List<List<Card>> tempList;
        List<Card> tempCard;

        int zhDanSize = otherSpecialList.getZhaDanList().size() + otherSpecialList.getKingBombList().size();
        //这两个返回情况可能导致能出完时检测到有炸弹也不出，之后再完善了，改起来太麻烦了
//        if (!notCareBomb && zhDanSize > 0 && !sendKingBomb){
//            return CardValue.errorWay();
//        }
//        if (zhDanSize > 2 && !sendKingBomb){
//            return CardValue.errorWay();
//        }
        cardValue = dfHaveSendAll(specialList,id);
        if (cardValue.getCheck()){
            return cardValue;
        }

        //超时退出，避免长时间不响应
        if (System.currentTimeMillis() - startTime > 5000){
            return CardValue.errorWay();
        }

        boolean couldNotReverse = zhDanSize > 0 && ((warningLevel.getCode() >= 2 && specialList.getKingBombList().size() > 0) || (warningLevel.getCode() >= 4 && specialList.getZhaDanList().size() > 0));
        boolean notCareBeReceive = notCareBeReceive(specialList, id);
        if (isOpponentSend){
            if (CardType.isSameType(previous,CardType.KING_BOMB)){
                return dfSendCard(specialList,otherSpecialList,null,warningLevel,id,notCareBomb,false,startTime,oppInfo);
            }

            if (!notCareBomb || zhDanSize > 0 || CardType.isSameType(previous,CardType.BOMB)){
                if (otherSpecialList.getKingBombList().size() > 0){
                    return dfSendCard(specialList,otherSpecialList,CardValue.kingBomb(otherSpecialList.getKingBombList(),id),warningLevel,id,notCareBomb,false,startTime,oppInfo);
                }
                if (!CardType.isSameType(previous,CardType.BOMB)){
                   if (otherSpecialList.getZhaDanList().size() > 0){
                       tempList = new ArrayList<>(otherSpecialList.getZhaDanList());
                       cardValue = CardValue.zhDan(otherSpecialList.getZhaDanList().get(otherSpecialList.getZhaDanList().size()-1),id);
                       otherSpecialList.getZhaDanList().remove(otherSpecialList.getZhaDanList().size()-1);
                       nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,false,startTime,oppInfo);
                       otherSpecialList.setZhaDanList(tempList);
                       return nextCardValue;
                   }
                }else {
                       if (otherSpecialList.getZhaDanList().size() == 0 || ListUtil.getCardListTail(otherSpecialList.getZhaDanList()).get(0).getValue() <= previous.getValue()){
                           return dfSendCard(specialList,otherSpecialList,null,warningLevel,id,notCareBomb,false,startTime,oppInfo);
                       }
                        tempList = new ArrayList<>(otherSpecialList.getZhaDanList());
                        cardValue = CardValue.zhDan(ListUtil.getCardListTail(otherSpecialList.getZhaDanList()),id);
                        otherSpecialList.getZhaDanList().remove(otherSpecialList.getZhaDanList().size()-1);
                        nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,false,startTime,oppInfo);
                        otherSpecialList.setZhaDanList(tempList);
                         return nextCardValue;
                }

            }

            if (CardType.isSameType(previous,CardType.FOUR_TAKE_TWO)){
                if (otherSpecialList.getKingBombList().size() > 0){
                    return dfSendCard(specialList,otherSpecialList,CardValue.kingBomb(otherSpecialList.getKingBombList(),id),warningLevel,id,notCareBomb,false,startTime,oppInfo);
                }

                if (otherSpecialList.getZhaDanList().size() > 0) {
                    tempList = new ArrayList<>(otherSpecialList.getZhaDanList());
                    cardValue = CardValue.zhDan(otherSpecialList.getZhaDanList().get(otherSpecialList.getZhaDanList().size() - 1), id);
                    otherSpecialList.getZhaDanList().remove(otherSpecialList.getZhaDanList().size() - 1);
                    nextCardValue = dfSendCard(specialList, otherSpecialList, cardValue, warningLevel, id, notCareBomb, false, startTime,oppInfo);
                    otherSpecialList.setZhaDanList(tempList);
                    return nextCardValue;
                }

            }

            if (oppInfo.getMaxSize() < previous.getNum()){
                return dfSendCard(specialList,otherSpecialList,null,warningLevel,id,notCareBomb,false,startTime,oppInfo);
            }

            if (CardType.isSameType(previous,CardType.SINGLE_CARD)){
                for (int i = otherSpecialList.getCardCountList().length-1; i >= 0; i--) {
                    if (otherSpecialList.getCardCountList()[i] == null || otherSpecialList.getCardCountList()[i].size() == 0){
                        continue;
                    }

                    if (otherSpecialList.getCardCountList()[i].get(0).getValue() <= previous.getValue()){
                        break;
                    }

                    tempCard = new ArrayList<>(otherSpecialList.getCardCountList()[i]);
                    List<Card> retList = new ArrayList<>();
                    retList.add(ListUtil.getCardTail(otherSpecialList.getCardCountList()[i]));
                    cardValue = CardValue.singleCard(retList,id);
                    otherSpecialList.getCardCountList()[i].remove(otherSpecialList.getCardCountList()[i].size()-1);
                    nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,false,startTime,oppInfo);
                    otherSpecialList.getCardCountList()[i] = tempCard;
                    if (!nextCardValue.getCheck()){
                        return nextCardValue;
                    }
                }
            }

            if (CardType.isSameType(previous,CardType.DOUBLE_CARD)){
                for (int i = otherSpecialList.getCardCountList().length-1; i >= 0; i--) {
                    if (otherSpecialList.getCardCountList()[i] == null || otherSpecialList.getCardCountList()[i].size() < 2){
                        continue;
                    }

                    if (otherSpecialList.getCardCountList()[i].get(0).getValue() <= previous.getValue()){
                        break;
                    }

                    tempCard = new ArrayList<>(otherSpecialList.getCardCountList()[i]);
                    List<Card> retList = new ArrayList<>();
                    retList.add(otherSpecialList.getCardCountList()[i].get(0));
                    retList.add(otherSpecialList.getCardCountList()[i].get(1));
                    cardValue = CardValue.doubleCard(retList,id);
                    otherSpecialList.getCardCountList()[i].remove(0);
                    otherSpecialList.getCardCountList()[i].remove(0);
                    nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,false,startTime,oppInfo);
                    otherSpecialList.getCardCountList()[i] = tempCard;
                    if (!nextCardValue.getCheck()){
                        return nextCardValue;
                    }
                }
            }


            if (CardType.isSameType(previous,CardType.THREE_TAKE_ONE)){
                if (previous.getValue() > 13){
                    if (otherSpecialList.getKingBombList().size() > 0){
                        return dfSendCard(specialList,otherSpecialList,CardValue.kingBomb(otherSpecialList.getKingBombList(),id),warningLevel,id,notCareBomb,false,startTime,oppInfo);
                    }

                    if (otherSpecialList.getZhaDanList().size() > 0){
                        tempList = new ArrayList<>(otherSpecialList.getZhaDanList());
                        cardValue = CardValue.zhDan(otherSpecialList.getZhaDanList().get(otherSpecialList.getZhaDanList().size()-1),id);
                        otherSpecialList.getZhaDanList().remove(otherSpecialList.getZhaDanList().size()-1);
                        nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,false,startTime,oppInfo);
                        otherSpecialList.setZhaDanList(tempList);
                            return nextCardValue;
                    }
                }

                for (int i = otherSpecialList.getCardCountList().length - 1; i >= 0 ; i--) {
                    if (otherSpecialList.getCardCountList()[i] == null || otherSpecialList.getCardCountList()[i].size() < 3){
                        continue;
                    }

                    if (otherSpecialList.getCardCountList()[i].get(0).getValue() > previous.getValue()){
                        tempCard = new ArrayList<>(otherSpecialList.getCardCountList()[i]);
                        cardValue = CardValue.threeTakeOne(tempCard, previous.getTakeCard(), id);
                        otherSpecialList.getCardCountList()[i] = new ArrayList<>();
                        nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,false,startTime,oppInfo);
                        otherSpecialList.getCardCountList()[i] = tempCard;
                        if (!nextCardValue.getCheck()){
                            return nextCardValue;
                        }
                    }
                    break;
                }


            }

            if (CardType.isSameType(previous,CardType.SUN_ZI)){
                tempList = new ArrayList<>(otherSpecialList.getSunZiList());
                for (int i = 0; i < otherSpecialList.getSunZiList().size(); i++) {
                    if (otherSpecialList.getSunZiList().get(i).size() < previous.getNum() || otherSpecialList.getSunZiList().get(i).get(otherSpecialList.getSunZiList().get(i).size()-1).getValue() <= previous.getValue()){
                        continue;
                    }
                    List<Card> ret = new ArrayList<>();
                    for (int j = otherSpecialList.getSunZiList().get(i).size() - previous.getNum(); j < otherSpecialList.getSunZiList().get(i).size(); j++) {
                        ret.add(otherSpecialList.getSunZiList().get(i).get(j));
                    }
                    otherSpecialList.getSunZiList().remove(i);
                    cardValue = CardValue.sunZi(ret,id);
                    nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,false,startTime,oppInfo);
                    otherSpecialList.setSunZiList(tempList);
                    return nextCardValue;
                }
            }

            return dfSendCard(specialList,otherSpecialList,null,warningLevel,id,notCareBomb,false,startTime,oppInfo);

        }
        if (previous == null){
            for (int i = 0; i < specialList.getUsefulSunZiList().size(); i++) {
               cardValue  = CardValue.sunZi(specialList.getUsefulSunZiList().get(i),id);
               tempList = new ArrayList<>(specialList.getUsefulSunZiList());
               specialList.getUsefulSunZiList().remove(i);
               nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
               specialList.setUsefulSunZiList(tempList);
               if (nextCardValue.getCheck()){
                   return cardValue;
               }

            }

            for (int i = 0; i < specialList.getZhaDanList().size(); i++) {
                if (specialList.getNormalList().size() > 1){
                    tempList = new ArrayList<>(specialList.getZhaDanList());
                    List<Card> takeList = new ArrayList<>();
                    tempCard = new ArrayList<>(specialList.getNormalList());
                    takeList.add(specialList.getNormalList().get(0));
                    takeList.add(specialList.getNormalList().get(1));
                    cardValue = CardValue.fourTakeTwo(specialList.getZhaDanList().get(i),takeList,id);
                    specialList.getNormalList().remove(0);
                    specialList.getNormalList().remove(0);
                    specialList.getZhaDanList().remove(i);
                    nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                    specialList.setZhaDanList(tempList);
                    specialList.setNormalList(tempCard);
                    if (nextCardValue.getCheck()){
                        return cardValue;
                    }
                }

                if (specialList.getDoubleList().size() > 1){
                    tempList = new ArrayList<>(specialList.getZhaDanList());
                    List<Card> takeList = new ArrayList<>();
                    List<List<Card>> doubleList = new ArrayList<>(specialList.getDoubleList());
                    takeList.addAll(specialList.getDoubleList().get(0));
                    takeList.addAll(specialList.getDoubleList().get(1));
                    cardValue = CardValue.fourTakeTwo(specialList.getZhaDanList().get(i),takeList,id);
                    specialList.getDoubleList().remove(0);
                    specialList.getDoubleList().remove(0);
                    specialList.getZhaDanList().remove(i);
                    nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                    specialList.setZhaDanList(tempList);
                    specialList.setDoubleList(doubleList);
                    if (nextCardValue.getCheck()){
                        return cardValue;
                    }
                }

                if (specialList.getDoubleList().size() > 0 && specialList.getNormalList().size() > 0){
                    tempList = new ArrayList<>(specialList.getZhaDanList());
                    if (specialList.getDoubleList().get(0).get(0).getValue() > specialList.getNormalList().get(0).getValue()){
                        List<Card> takeList = new ArrayList<>();
                        List<List<Card>> doubleList = new ArrayList<>(specialList.getDoubleList());
                        tempCard = new ArrayList<>(specialList.getNormalList());
                        takeList.add(specialList.getDoubleList().get(0).get(0));
                        takeList.add(specialList.getNormalList().get(0));
                        cardValue = CardValue.fourTakeTwo(specialList.getZhaDanList().get(i),takeList,id);
                        specialList.getNormalList().add(specialList.getDoubleList().get(0).get(1));
                        specialList.getDoubleList().remove(0);
                        specialList.getZhaDanList().remove(i);
                        specialList.getNormalList().remove(0);
                        specialList.getNormalList().sort(Comparator.comparing(Card::getId));
                        nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                        specialList.setZhaDanList(tempList);
                        specialList.setDoubleList(doubleList);
                        specialList.setNormalList(tempCard);
                        if (nextCardValue.getCheck()){
                            return cardValue;
                        }
                    }else{
                        tempList = new ArrayList<>(specialList.getZhaDanList());
                        List<List<Card>> doubleList = new ArrayList<>(specialList.getDoubleList());
                        List<Card> takeList = new ArrayList<>(specialList.getDoubleList().get(0));
                        cardValue = CardValue.fourTakeTwo(specialList.getZhaDanList().get(i),takeList,id);
                        specialList.getZhaDanList().remove(i);
                        specialList.getDoubleList().remove(0);
                        specialList.setKingBombList(new ArrayList<>());
                       nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                        specialList.setZhaDanList(tempList);
                        specialList.setDoubleList(doubleList);
                        if (nextCardValue.getCheck()){
                            return cardValue;
                        }
                    }

                }

                tempList = new ArrayList<>(specialList.getZhaDanList());
                cardValue = CardValue.zhDan(specialList.getZhaDanList().get(i),id);
                specialList.getZhaDanList().remove(i);
                nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                specialList.setZhaDanList(tempList);
                if (nextCardValue.getCheck()){
                    return cardValue;
                }
            }

            for (int i = 0; i < specialList.getLianDuiList().size(); i++) {
                tempList = new ArrayList<>(specialList.getLianDuiList());
                List<List<Card>> retList = new ArrayList<>(specialList.getDoubleList());
                cardValue = CardValue.lianDui(specialList.getLianDuiList().get(i),id);
                Map<Integer,Integer> map = new HashMap<>();
                specialList.getLianDuiList().get(i).forEach(t->map.put(t.getValue(),t.getValue()));
                specialList.getLianDuiList().remove(i);
                specialList.setDoubleList(specialList.getDoubleList().stream().filter(t->!map.containsKey(t.get(0).getValue())).collect(Collectors.toList()));
                nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                specialList.setLianDuiList(tempList);
                specialList.setDoubleList(retList);
                if (nextCardValue.getCheck()){
                    return cardValue;
                }
            }

            for (int i = 0; i < specialList.getAirList().size(); i++) {
                //防止三带一拆掉王炸
                if (specialList.getNormalList().size() > 0 && (specialList.getNormalList().get(0).getValue() < 16 || specialList.getKingBombList().size() == 0)){
                    tempList = new ArrayList<>(specialList.getAirList());
                    List<Card> takeList = new ArrayList<>();
                    takeList.add(specialList.getNormalList().get(0));
                    cardValue = CardValue.threeTakeOne(specialList.getAirList().get(i), takeList,id);
                    tempCard = new ArrayList<>(specialList.getNormalList());
                    specialList.getNormalList().remove(0);
                    specialList.getAirList().remove(i);
                    nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                    specialList.setNormalList(tempCard);
                    specialList.setAirList(tempList);
                    if (nextCardValue.getCheck()){
                        return cardValue;
                    }
                    if (couldNotReverse){
                        break;
                    }
                }

                if (specialList.getDoubleList().size() > 0){
                    tempList = new ArrayList<>(specialList.getAirList());
                        cardValue = CardValue.threeTakeOne(specialList.getAirList().get(i), specialList.getDoubleList().get(0),id);
                    if (myCardCouldReceive(cardValue,otherSpecialList,notCareBomb) && !notCareBeReceive){
                        continue;
                    }
                    List<List<Card>> doubleList = new ArrayList<>(specialList.getDoubleList());
                    specialList.getDoubleList().remove(0);
                    specialList.getAirList().remove(i);
                   nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                    specialList.setDoubleList(doubleList);
                    specialList.setAirList(tempList);
                    if (nextCardValue.getCheck()){
                        return cardValue;
                    }
                }

                tempList = new ArrayList<>(specialList.getAirList());
                cardValue = CardValue.threeTakeOne(specialList.getAirList().get(i), new ArrayList<>(),id);
                if (myCardCouldReceive(cardValue,otherSpecialList,notCareBomb) && !notCareBeReceive){
                    continue;
                }
                specialList.getAirList().remove(i);
               nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                specialList.setAirList(tempList);
                if (nextCardValue.getCheck()){
                    return cardValue;
                }
            }

            if (specialList.getKingBombList().size() != 0){
                if (shouldSendKingBomb(specialList)) {
                    tempCard = new ArrayList<>(specialList.getNormalList());
                    List<Card> retList = new ArrayList<>(specialList.getKingBombList());
                    specialList.getNormalList().remove(specialList.getNormalList().size() - 1);
                    specialList.getNormalList().remove(specialList.getNormalList().size() - 1);
                    specialList.setKingBombList(new ArrayList<>());
                    cardValue = CardValue.kingBomb(retList, id);
                    nextCardValue = dfSendCard(specialList, otherSpecialList, cardValue, warningLevel, id, notCareBomb,true,startTime,oppInfo);
                    specialList.setKingBombList(retList);
                    specialList.setNormalList(tempCard);
                    if (nextCardValue.getCheck()) {
                        return cardValue;
                    }
                }
            }

            for (int i = 0; i < specialList.getDoubleList().size(); i++) {
                tempList = new ArrayList<>(specialList.getDoubleList());
                cardValue = CardValue.doubleCard(specialList.getDoubleList().get(0),id);
                int maxDouble = -1;
                for (int j = otherSpecialList.getCardCountList().length - 1; j >= 0 ; j--) {
                    if (otherSpecialList.getCardCountList()[j] == null || otherSpecialList.getCardCountList()[j].size() < 2){
                        continue;
                    }
                    maxDouble = otherSpecialList.getCardCountList()[j].get(0).getValue();
                    break;
                }
                if (maxDouble > cardValue.getValue()) {
                    if (WarningLevel.isSameLevel(WarningLevel.DOUBLE_WARNING, warningLevel) && !notCareBeReceive) {
                        continue;
                    }
                }
                specialList.getDoubleList().remove(i);
                      nextCardValue =  dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                        specialList.setDoubleList(tempList);
                        if (nextCardValue.getCheck()){
                            return cardValue;
                        }
            }

            for (int i = 0; i < specialList.getNormalList().size(); i++) {
                tempCard = new ArrayList<>(specialList.getNormalList());
                List<Card> takeList = new ArrayList<>();
                takeList.add(specialList.getNormalList().get(i));
                cardValue = CardValue.singleCard(takeList,id);
                boolean kingBomb = specialList.getKingBombList().size() > 0 && specialList.getNormalList().get(i).getValue() > 15;
                List<Card> kingBombList = new ArrayList<>(specialList.getKingBombList());
                specialList.getNormalList().remove(i);
                if (WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel) && !notCareBeReceive){
                    if (otherSpecialList.getMyCard().size() > 0 && ListUtil.getCardTail(specialList.getMyCard()).getValue() > cardValue.getValue()){
                        specialList.setNormalList(tempCard);
                        continue;
                    }
                    }
//                CardValue receiveCardValue = getMaxSingle(otherSpecialList,id);
//                if (receiveCardValue.getValue() > cardValue.getValue()){
//                    if (WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel) && !notCareBeReceive){
//                        specialList.setNormalList(tempCard);
//                        continue;
//                    }
//                   nextCardValue = dfSendCard(specialList,otherSpecialList,receiveCardValue,warningLevel,id,notCareBomb);
//                    specialList.setNormalList(tempCard);
//                    if (nextCardValue.getCheck()){
//                        return cardValue;
//                    }
//                }else {
                    if (kingBomb){
                        specialList.setKingBombList(new ArrayList<>());
                    }
                  nextCardValue =  dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                    specialList.setNormalList(tempCard);
                    specialList.setKingBombList(kingBombList);
                    if (nextCardValue.getCheck()){
                        return cardValue;
                    }
//                }
                specialList.setNormalList(tempCard);
                if (couldNotReverse){
                    break;
                }
            }



        }else {
            if (specialList.getKingBombList().size() > 0){
                tempCard = new ArrayList<>(specialList.getNormalList());
                List<Card> retList = new ArrayList<>(specialList.getKingBombList());
                specialList.setNormalList(specialList.getNormalList().stream().filter(t->t.getValue() != 16 && t.getValue()!= 17).collect(Collectors.toList()));
                specialList.setKingBombList(new ArrayList<>());
                cardValue = CardValue.kingBomb(retList,id);
                nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                specialList.setKingBombList(retList);
                specialList.setNormalList(tempCard);
                if (nextCardValue.getCheck()){
                    return cardValue;
                }
            }

            if (CardType.isSameType(previous,CardType.BOMB)){
                for (int i = 0; i < specialList.getZhaDanList().size(); i++) {
                    if (specialList.getZhaDanList().get(i).get(0).getValue() <= previous.getValue()){
                        continue;
                    }

                    tempList = new ArrayList<>(specialList.getZhaDanList());
                    cardValue = CardValue.zhDan(specialList.getZhaDanList().get(i),id);
                    specialList.getZhaDanList().remove(i);
                    nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                    specialList.setZhaDanList(tempList);
                    if (nextCardValue.getCheck()){
                        return cardValue;
                    }
                }

            }

            if (CardType.isSameType(previous,CardType.BOMB)){
                return CardValue.errorWay();
            }

            for (int i = 0; i < specialList.getZhaDanList().size(); i++) {
                tempList = new ArrayList<>(specialList.getZhaDanList());
                cardValue = CardValue.zhDan(specialList.getZhaDanList().get(i),id);
                specialList.getZhaDanList().remove(i);
                nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                specialList.setZhaDanList(tempList);
                if (nextCardValue.getCheck()){
                    return cardValue;
                }
            }
            if (CardType.isSameType(CardType.SINGLE_CARD,previous.getType())){
                for (int i = 0; i < specialList.getNormalList().size(); i++) {
                    if (WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel) && !notCareBeReceive){
                        if (specialList.getNormalList().get(i).getValue() < ListUtil.getCardTail(otherSpecialList.getMyCard()).getValue()){
                            continue;
                        }
                    }
                    if (specialList.getNormalList().get(i).getValue() > previous.getValue()){
                        tempCard = new ArrayList<>(specialList.getNormalList());
                        List<Card> takeList = new ArrayList<>();
                        takeList.add(specialList.getNormalList().get(i));
                        boolean kingBomb = specialList.getKingBombList().size() > 0 && specialList.getNormalList().get(i).getValue() >15;
                        specialList.getNormalList().remove(i);
                        cardValue = CardValue.singleCard(takeList,id);
//                        nextCardValue = getMaxSingle(otherSpecialList,id);
                        List<Card> kingBombList = new ArrayList<>(specialList.getKingBombList());
//                        if (nextCardValue.getValue() > cardValue.getValue()){
//                            nextCardValue = dfSendCard(specialList,otherSpecialList,nextCardValue,warningLevel,id,notCareBomb);
//                            specialList.setNormalList(tempCard);
//                            if (nextCardValue.getCheck()){
//                                return cardValue;
//                            }
//                        }else {
                            if (kingBomb){
                                specialList.setKingBombList(new ArrayList<>());
                            }
                            nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                            specialList.setNormalList(tempCard);
                            specialList.setKingBombList(kingBombList);
                            if (nextCardValue.getCheck()){
                                return cardValue;
                            }
//                        }

                    }
                }

                for (int i = 0; i < specialList.getDoubleList().size(); i++) {
                    if (WarningLevel.isSameLevel(WarningLevel.DOUBLE_WARNING,warningLevel) && !notCareBeReceive && otherSpecialList.getDoubleList().size() > 0){
                        if (specialList.getDoubleList().get(i).get(0).getValue() < ListUtil.getCardListTail(otherSpecialList.getDoubleList()).get(0).getValue()){
                            continue;
                        }
                    }
                    if (specialList.getDoubleList().get(i).get(0).getValue() > previous.getValue()){
                        tempList = new ArrayList<>(specialList.getDoubleList());
                        tempCard = new ArrayList<>(specialList.getNormalList());
                        List<Card> takeList = new ArrayList<>();
                        takeList.add(specialList.getDoubleList().get(i).get(0));
                        cardValue = CardValue.singleCard(takeList,id);
                        specialList.getNormalList().add(specialList.getDoubleList().get(i).get(1));
                        specialList.getNormalList().sort(Comparator.comparing(Card::getId));
                        specialList.getDoubleList().remove(i);
//                        nextCardValue = getMaxSingle(otherSpecialList,id);
//                        if (nextCardValue.getValue() > cardValue.getValue()){
//                            nextCardValue = dfSendCard(specialList,otherSpecialList,nextCardValue,warningLevel,id,notCareBomb);
//                            specialList.setDoubleList(tempList);
//                            specialList.setNormalList(tempCard);
//                            if (nextCardValue.getCheck()){
//                                return cardValue;
//                            }
//                        }else {
                            nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                            specialList.setDoubleList(tempList);
                            specialList.setNormalList(tempCard);
                            if (nextCardValue.getCheck()){
                                return cardValue;
                            }
                        }
//                        if (nextCardValue.getCheck()){
//                            return cardValue;
//                        }
//                    }
                }

                for (int i = 0; i < specialList.getAirList().size(); i++) {
                    if (specialList.getAirList().get(i).get(0).getValue() > previous.getValue()) {
                        tempList = new ArrayList<>(specialList.getDoubleList());
                        List<List<Card>> retList = new ArrayList<>(specialList.getAirList());
                        List<Card> takeList = new ArrayList<>();
                        takeList.add(specialList.getAirList().get(i).get(0));
                        List<Card> doubleList = new ArrayList<>();
                        doubleList.add(specialList.getAirList().get(i).get(1));
                        doubleList.add(specialList.getAirList().get(i).get(2));
                        specialList.getDoubleList().add(doubleList);
                        specialList.getDoubleList().sort(Comparator.comparingInt(t -> t.get(0).getValue()));
                        specialList.getAirList().remove(i);
                        cardValue = CardValue.singleCard(takeList,id);
//                        nextCardValue = getMaxSingle(otherSpecialList,id);
//                        if (nextCardValue.getValue() > cardValue.getValue()){
//                            specialList.getAirList().remove(i);
//                            nextCardValue = dfSendCard(specialList,otherSpecialList,nextCardValue,warningLevel,id,notCareBomb);
//                            specialList.setDoubleList(tempList);
//                            specialList.setAirList(retList);
//                            if (nextCardValue.getCheck()){
//                                return cardValue;
//                            }
//                        }else {
                            nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                            specialList.setDoubleList(tempList);
                            specialList.setAirList(retList);
                            if (nextCardValue.getCheck()){
                                return cardValue;
                            }
//                        }

                    }
                }
            }

            if (CardType.isSameType(CardType.DOUBLE_CARD,previous.getType())){
                for (int i = 0; i < specialList.getDoubleList().size(); i++) {
                    if (WarningLevel.isSameLevel(WarningLevel.DOUBLE_WARNING,warningLevel) && !notCareBeReceive){
                        if (specialList.getDoubleList().get(i).get(0).getValue() < getMaxByTimes(otherSpecialList,2)){
                            continue;
                        }
                    }
                    if (specialList.getDoubleList().get(i).get(0).getValue() > previous.getValue()){
                        tempList = new ArrayList<>(specialList.getDoubleList());
                        cardValue = CardValue.doubleCard(specialList.getDoubleList().get(i),id);
                        specialList.getDoubleList().remove(i);
                        nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                        specialList.setDoubleList(tempList);
                        if (nextCardValue.getCheck()){
                            return cardValue;
                        }
                    }
                }

                for (int i = 0; i < specialList.getAirList().size(); i++) {
                    if (specialList.getAirList().get(i).get(0).getValue() > previous.getValue()) {
                        tempList = new ArrayList<>(specialList.getAirList());
                        tempCard = new ArrayList<>(specialList.getNormalList());
                        List<Card> retList = new ArrayList<>();
                        retList.add(specialList.getAirList().get(i).get(1));
                        retList.add(specialList.getAirList().get(i).get(2));
                        specialList.getNormalList().add(specialList.getAirList().get(i).get(0));
                        specialList.getNormalList().sort(Comparator.comparing(Card::getId));
                        cardValue = CardValue.doubleCard(retList,id);
                        specialList.getAirList().remove(i);
                        nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                        specialList.setAirList(tempList);
                        specialList.setNormalList(tempCard);
                        if (nextCardValue.getCheck()){
                            return cardValue;
                        }
                    }

                }
            }

            if (CardType.isSameType(CardType.THREE_TAKE_ONE,previous.getType())){
                for (int i = 0 ; i < specialList.getAirList().size(); i++) {
                    if (specialList.getAirList().get(i).get(0).getValue() <= previous.getValue()){
                        continue;
                    }

                    if (previous.getNum() == 4){
                        if (specialList.getNormalList().size() > 0){
                            tempCard = new ArrayList<>(specialList.getNormalList());
                            tempList = new ArrayList<>(specialList.getAirList());
                            List<Card> ret = new ArrayList<>();
                            ret.add(specialList.getNormalList().get(0));
                            cardValue = CardValue.threeTakeOne(specialList.getAirList().get(i),ret,id);
                            specialList.getAirList().remove(i);
                            specialList.getNormalList().remove(0);
                            nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                            specialList.setNormalList(tempCard);
                            specialList.setAirList(tempList);
                            if (nextCardValue.getCheck()){
                                return cardValue;
                            }else {
                                return CardValue.errorWay();
                            }
                        }

                        if (specialList.getDoubleList().size() > 0){
                            tempCard = new ArrayList<>(specialList.getNormalList());
                            List<List<Card>> doubleList = new ArrayList<>(specialList.getDoubleList());
                            tempList = new ArrayList<>(specialList.getAirList());
                            List<Card> ret = new ArrayList<>();
                            ret.add(specialList.getDoubleList().get(0).get(1));
                            cardValue = CardValue.threeTakeOne(specialList.getAirList().get(i),ret,id);
                            specialList.getAirList().remove(i);
                            specialList.getNormalList().add(specialList.getDoubleList().get(0).get(0));
                            specialList.getDoubleList().remove(0);
                            specialList.getNormalList().sort(Comparator.comparing(Card::getId));
                            nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                            specialList.setNormalList(tempCard);
                            specialList.setAirList(tempList);
                            specialList.setDoubleList(doubleList);
                            if (nextCardValue.getCheck()){
                                return cardValue;
                            }
                        }

                    }

                    if (previous.getNum() == 5){
                        if (specialList.getDoubleList().size() > 0){
                            List<List<Card>> doubleList = new ArrayList<>(specialList.getDoubleList());
                            tempList = new ArrayList<>(specialList.getAirList());
                            List<Card> ret = new ArrayList<>();
                            ret.add(specialList.getDoubleList().get(0).get(0));
                            ret.add(specialList.getDoubleList().get(0).get(1));
                            cardValue = CardValue.threeTakeOne(specialList.getAirList().get(i),ret,id);
                            specialList.getAirList().remove(i);
                            specialList.getDoubleList().remove(0);
                            nextCardValue = dfSendCard(specialList,otherSpecialList,cardValue,warningLevel,id,notCareBomb,true,startTime,oppInfo);
                            specialList.setAirList(tempList);
                            specialList.setDoubleList(doubleList);
                            if (nextCardValue.getCheck()){
                                return cardValue;
                            }
                        }

                    }


                }
            }




        }

        return CardValue.errorWay();

    }

//    public static CardValue dfDoubleOfDouble(SpecialList specialList,SpecialList otherSpecialList,List<List<Card>> tempList,WarningLevel warningLevel,int id,CardValue nextCardValue,boolean notCareBomb,long startTime){
//        tempList = new ArrayList<>(otherSpecialList.getDoubleList());
//        otherSpecialList.getDoubleList().remove(otherSpecialList.getDoubleList().size()-1);
//        nextCardValue = dfSendCard(specialList,otherSpecialList,CardValue.doubleCard(tempList.get(tempList.size()-1),id),warningLevel,id,notCareBomb,true,startTime);
//        otherSpecialList.setDoubleList(tempList);
//           if (nextCardValue.getCheck()){
//               return nextCardValue;
//           }
//
//        return CardValue.errorWay();
//    }
//
//    public static CardValue dfThreeOfDouble(SpecialList specialList,SpecialList otherSpecialList,List<List<Card>> tempList,List<Card> tempCard,WarningLevel warningLevel,int id,CardValue nextCardValue,boolean notCareBomb,long startTime){
//        tempList = new ArrayList<>(otherSpecialList.getAirList());
//        tempCard = new ArrayList<>(otherSpecialList.getNormalList());
//        List<Card> takeList = new ArrayList<>();
//        takeList.add(otherSpecialList.getAirList().get(otherSpecialList.getAirList().size()-1).get(1));
//        takeList.add(otherSpecialList.getAirList().get(otherSpecialList.getAirList().size()-1).get(2));
//        otherSpecialList.getNormalList().add(otherSpecialList.getAirList().get(otherSpecialList.getAirList().size()-1).get(0));
//        otherSpecialList.getAirList().remove(otherSpecialList.getAirList().size()-1);
//        nextCardValue = dfSendCard(specialList,otherSpecialList,CardValue.doubleCard(takeList,id),warningLevel,id,notCareBomb,true,startTime);
//        otherSpecialList.setAirList(tempList);
//        otherSpecialList.setNormalList(tempCard);
//        if (nextCardValue.getCheck()){
//            return nextCardValue;
//        }
//        return CardValue.errorWay();
//    }
    public static int getMaxByTimes(SpecialList specialList,int times){
        for (int i = specialList.getCardCountList().length - 1; i >= 0 ; i--) {
            if (specialList.getCardCountList()[i] == null || specialList.getCardCountList()[i].size() < times){
                continue;
            }
            return specialList.getCardCountList()[i].get(0).getValue();
        }
        return -1;
    }

    public static int getRank(List<Card> myCard , Card card){
        int rank = 0;
        for (Card cardTemp : myCard) {
            if (cardTemp.getValue() < card.getValue()){
                rank++;
            }
        }
        return rank;
    }

    public static int getMinByTimes(SpecialList specialList,int times){
        for (int i = 0; i < specialList.getCardCountList().length ; i++) {
            if (specialList.getCardCountList()[i] == null || specialList.getCardCountList()[i].size() != times){
                continue;
            }
            return specialList.getCardCountList()[i].get(0).getValue();
        }
        return 99;
    }

    public static CardValue dfHaveSendAll(SpecialList specialList,int id){
        if (specialList.getNormalList().size() + specialList.getDoubleList().size() + specialList.getAirList().size() + specialList.getZhaDanList().size() +
        specialList.getKingBombList().size() + specialList.getUsefulSunZiList().size() + specialList.getLianDuiList().size() == 0){
            return CardValue.trueWay();
        }
        return CardValue.errorWay();
    }

    public static boolean notCareBeReceive(SpecialList specialList,int id){
        int allTile = specialList.getNormalList().size() + specialList.getDoubleList().size() + specialList.getAirList().size() + specialList.getZhaDanList().size() +
                specialList.getKingBombList().size()  + specialList.getUsefulSunZiList().size() + specialList.getLianDuiList().size();
        if (allTile == 1){
            return true;
        }

        if (allTile == 2){
            if (specialList.getAirList().size() == 1 && (specialList.getNormalList().size() ==1 || specialList.getDoubleList().size() == 1)){
                return true;
            }

            if (specialList.getZhaDanList().size() == 1 && specialList.getDoubleList().size() == 1){
                return true;
            }

            if (specialList.getKingBombList().size() > 0){
                return true;
            }

        }

        if (allTile == 3){
            if (specialList.getZhaDanList().size() == 1 && (specialList.getNormalList().size() == 2 || specialList.getDoubleList().size() == 2)){
                return true;
            }
        }
        return false;
    }

    public static boolean shouldSendKingBomb(SpecialList specialList){
        if (specialList.getKingBombList().size() == 0){
            return false;
        }
        int allTile = specialList.getNormalList().size() + specialList.getDoubleList().size() + specialList.getAirList().size() + specialList.getZhaDanList().size() +
                specialList.getKingBombList().size() / 2  + specialList.getUsefulSunZiList().size() + specialList.getLianDuiList().size();
          return allTile == 4 || allTile == 3 || (allTile == 5 && specialList.getAirList().size() == 1);
    }

    public static CardValue receiveSunZi(List<List<Card>> sunZiList,CardValue cardValue,WarningLevel warningLevel,int id){
        List<Card> retList = new ArrayList<>();
        for (List<Card> list : sunZiList) {
            if (list.size() < cardValue.getNum() || (list.size() - cardValue.getNum() > 2 && !WarningLevel.levelHigher(WarningLevel.NO_WARNING,warningLevel))){
                continue;
            }

            for (int i = 0; i < list.size(); i++) {
                int endIndex = i+cardValue.getNum() - 1;
                if (endIndex >= list.size()){
                    break;
                }
                if (list.get(endIndex).getValue() <= cardValue.getValue()){
                    continue;
                }

                for (int j = i; j <= endIndex; j++) {
                    retList.add(list.get(j));
                }
               return CardValue.sunZi(retList,id);

            }


        }
             return CardValue.errorWay();

    }


    public static CardValue getMaxSingle(SpecialList otherSpecialList,int id){
        int maxValue = 0;
        List<Card> takeList = new ArrayList<>();
        if (otherSpecialList.getNormalList().size() > 0){
            if (ListUtil.getCardTail(otherSpecialList.getNormalList()).getValue() > maxValue){
                takeList.add(ListUtil.getCardTail(otherSpecialList.getNormalList()));
                maxValue = takeList.get(0).getValue();
            }
        }
        if (otherSpecialList.getDoubleList().size() > 0){
            if (ListUtil.getCardListTail(otherSpecialList.getDoubleList()).get(0).getValue() > maxValue) {
                takeList.clear();
                takeList.add(ListUtil.getCardListTail(otherSpecialList.getDoubleList()).get(0));
                maxValue = takeList.get(0).getValue();
            }
        }
        if (otherSpecialList.getAirList().size() > 0){
            if (ListUtil.getCardListTail(otherSpecialList.getAirList()).get(0).getValue() > maxValue) {
                takeList.clear();
                takeList.add(ListUtil.getCardListTail(otherSpecialList.getAirList()).get(0));
                maxValue = takeList.get(0).getValue();
            }
        }
        if (otherSpecialList.getZhaDanList().size() > 0){
            if (ListUtil.getCardListTail(otherSpecialList.getZhaDanList()).get(0).getValue() > maxValue) {
                takeList.clear();
                takeList.add(ListUtil.getCardListTail(otherSpecialList.getZhaDanList()).get(0));
            }
        }
        return CardValue.singleCard(takeList,id);
    }

    public static CardValue sendByRoomRecord(SpecialList specialList,SpecialList otherSpecialList,CardMap cardMapMy,WarningLevel warningLevel,int id,int lordId,String roomId){
        Map<Integer, CardValue> userLastNotReceiveMap = PukeRoom.getUserLastNotReceive(roomId, lordId,false);
        if (specialList.getAirList().size() != 0 && userLastNotReceiveMap.get(CardType.THREE_TAKE_ONE.getCode()) != null) {
            CardValue cardValue = PukeRole.firstSendThreeTakeOne(specialList, otherSpecialList, id, warningLevel);
            if (cardValue.getCheck()){
                return cardValue;
            }
        }
        if (userLastNotReceiveMap.get(CardType.DOUBLE_CARD.getCode()) != null && specialList.getDoubleList().size() > 0){
            if ((specialList.getNormalList().size() == 1 && specialList.getMyCard().size() <= 3) || specialList.getDoubleList().size() > 1){
                for (List<Card> list : specialList.getDoubleList()) {
                    if (list.get(0).getValue() < userLastNotReceiveMap.get(CardType.DOUBLE_CARD.getCode()).getValue()){
                        break;
                    }

                    if (cardMapMy.compareRankOfMin(list.get(0)) > 1){
                        break;
                    }

                    CardValue cardValue = CardValue.doubleCard(list,id);
                    if (WarningLevel.isSameLevel(WarningLevel.DOUBLE_WARNING,warningLevel)){
                        if (myCardCouldReceive(cardValue,otherSpecialList,false)){
                            return CardValue.errorWay();
                        }

                    }
                    return cardValue;
                }

            }
        }

        if (userLastNotReceiveMap.get(CardType.SINGLE_CARD.getCode()) != null && !WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel)) {
            CardValue  userLastNotReceive = userLastNotReceiveMap.get(CardType.SINGLE_CARD.getCode());
            List<Card> cardList = new ArrayList<>();
            int normalSize = specialList.getNormalList().size();
            int doubleSize = specialList.getDoubleList().size();
            if (specialList.getMyCard().size() >= 2 && specialList.getMyCard().get(1).getValue() >= userLastNotReceive.getValue()){
                cardList.add(specialList.getMyCard().get(1));
                return CardValue.singleCard(cardList, id);
            }
            if (normalSize + doubleSize != specialList.getSize()){
                return CardValue.errorWay();
            }
            if (normalSize > 0) {
                if (specialList.getNormalList().get(0).getValue() >= userLastNotReceive.getValue()) {
                    cardList.add(specialList.getNormalList().get(0));
                    return CardValue.singleCard(cardList, id);
                }

            }

            if (doubleSize > 0 && normalSize == 0) {
                if (specialList.getDoubleList().get(0).get(0).getValue() >= userLastNotReceive.getValue()) {
                    cardList.add(specialList.getDoubleList().get(0).get(0));
                    return CardValue.singleCard(cardList, id);
                }
            }

//            if (specialList.getDoubleList().size() > 0 && specialList.getNormalList().size() > 0) {
//                if (specialList.getDoubleList().get(0).get(0).getValue() >= userLastNotReceive.getValue() && specialList.getDoubleList().get(0).get(0).getValue() < specialList.getNormalList().get(0).getValue()) {
//                    cardList.add(specialList.getDoubleList().get(0).get(0));
//                    return CardValue.singleCard(cardList, id);
//                }
//
//                if (specialList.getNormalList().get(0).getValue() >= userLastNotReceive.getValue() && specialList.getDoubleList().get(0).get(0).getValue() > specialList.getNormalList().get(0).getValue()) {
//                    cardList.add(specialList.getNormalList().get(0));
//                    return CardValue.singleCard(cardList, id);
//                }
//            }

        }

         return CardValue.errorWay();
    }

    public static CardValue couldHelpMyFriendOnReceive(SpecialList otherSpecialList,CardValue lastCardValue,Map<Integer, CardValue> userLastNotReceiveMap,WarningLevel warningLevel,int id,int lordId,int friendSize,String roomId){
        if (WarningLevel.levelHigher(WarningLevel.DOUBLE_WARNING,warningLevel) || friendSize > 2 || (id+1) % 3 == lordId){
            return CardValue.errorWay();
        }

        if (!CardType.isSameType(CardType.SINGLE_CARD,lastCardValue.getType()) && !CardType.isSameType(CardType.DOUBLE_CARD,lastCardValue.getType())){
            return CardValue.errorWay();
        }


        if (CardType.isSameType(CardType.SINGLE_CARD,lastCardValue.getType()) && friendSize == 1){
            if (myCardCouldReceive(lastCardValue,otherSpecialList,true) && getMaxCouldNotReceiveFromRoom(userLastNotReceiveMap,lastCardValue.getType()).getValue() > lastCardValue.getValue()){
                return CardValue.trueWay();
            }
        }

        if (CardType.isSameType(CardType.DOUBLE_CARD,lastCardValue.getType()) && friendSize == 2){
            if (myCardCouldReceive(lastCardValue,otherSpecialList,true) && getMaxCouldNotReceiveFromRoom(userLastNotReceiveMap,lastCardValue.getType()).getValue() > lastCardValue.getValue()){
                return CardValue.trueWay();
            }
        }
        return CardValue.errorWay();

    }

    public static CardValue getMaxCouldNotReceiveFromRoom(Map<Integer, CardValue> userLastNotReceiveMap,CardType cardType){
        return userLastNotReceiveMap.getOrDefault(cardType.getCode(),CardValue.maxValue());
    }


    public static void main(String[] args) {
        System.out.println(WarningLevel.getWarningLevel(9));
    }








}
