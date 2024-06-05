package com.zhj.pukeyule;

import com.google.common.collect.Lists;
import com.zhj.entity.puke.*;
import com.zhj.entity.puke.dic.CardType;
import com.zhj.entity.puke.dic.WarningLevel;
import com.zhj.util.ListUtil;
import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.bag.HashBag;

import java.util.*;
import java.util.stream.Collectors;

import static com.zhj.entity.puke.PukeRole.firstSendThreeTakeOne;
import static com.zhj.entity.puke.PukeRole.myCardCouldReceive;

public class PuKe {
    public final static Card[] card = new Card[]{new Card(Card.HEITAO, 3, "黑桃3", 0), new Card(Card.YINHUA, 3, "樱花3", 1), new Card(Card.FANGKUAI, 3, "方块3", 2), new Card(Card.HONGTAO, 3, "红桃3", 3),
            new Card(Card.HEITAO, 4, "黑桃4", 4), new Card(Card.YINHUA, 4, "樱花4", 5), new Card(Card.FANGKUAI, 4, "方块4", 6), new Card(Card.HONGTAO, 4, "红桃4", 7),
            new Card(Card.HEITAO, 5, "黑桃5", 8), new Card(Card.YINHUA, 5, "樱花5", 9), new Card(Card.FANGKUAI, 5, "方块5", 10), new Card(Card.HONGTAO, 5, "红桃5", 11),
            new Card(Card.HEITAO, 6, "黑桃6", 12), new Card(Card.YINHUA, 6, "樱花6", 13), new Card(Card.FANGKUAI, 6, "方块6", 14), new Card(Card.HONGTAO, 6, "红桃6", 15),
            new Card(Card.HEITAO, 7, "黑桃7", 16), new Card(Card.YINHUA, 7, "樱花7", 17), new Card(Card.FANGKUAI, 7, "方块7", 18), new Card(Card.HONGTAO, 7, "红桃7", 19),
            new Card(Card.HEITAO, 8, "黑桃8", 20), new Card(Card.YINHUA, 8, "樱花8", 21), new Card(Card.FANGKUAI, 8, "方块8", 22), new Card(Card.HONGTAO, 8, "红桃8", 23),
            new Card(Card.HEITAO, 9, "黑桃9", 24), new Card(Card.YINHUA, 9, "樱花9", 25), new Card(Card.FANGKUAI, 9, "方块9", 26), new Card(Card.HONGTAO, 9, "红桃9", 27),
            new Card(Card.HEITAO, 10, "黑桃10", 28), new Card(Card.YINHUA, 10, "樱花10", 29), new Card(Card.FANGKUAI, 10, "方块10", 30), new Card(Card.HONGTAO, 10, "红桃10", 31),
            new Card(Card.HEITAO, 11, "黑桃J", 32), new Card(Card.YINHUA, 11, "樱花J", 33), new Card(Card.FANGKUAI, 11, "方块J", 34), new Card(Card.HONGTAO, 11, "红桃J", 35),
            new Card(Card.HEITAO, 12, "黑桃Q", 36), new Card(Card.YINHUA, 12, "樱花Q", 37), new Card(Card.FANGKUAI, 12, "方块Q", 38), new Card(Card.HONGTAO, 12, "红桃Q", 39),
            new Card(Card.HEITAO, 13, "黑桃K", 40), new Card(Card.YINHUA, 13, "樱花K", 41), new Card(Card.FANGKUAI, 13, "方块K", 42), new Card(Card.HONGTAO, 13, "红桃K", 43),
            new Card(Card.HEITAO, 14, "黑桃A", 44), new Card(Card.YINHUA, 14, "樱花A", 45), new Card(Card.FANGKUAI, 14, "方块A", 46), new Card(Card.HONGTAO, 14, "红桃A", 47),
            new Card(Card.HEITAO, 15, "黑桃2", 48), new Card(Card.YINHUA, 15, "樱花2", 49), new Card(Card.FANGKUAI, 15, "方块2", 50), new Card(Card.HONGTAO, 15, "红桃2", 51),
            new Card(Card.WANG, 16, "小王", 52), new Card(Card.WANG, 17, "大王", 53)
    };

    public static CardValue singleCard(List<Card> cardList, int id) {
        if (cardList.size() == 1) {
            return new CardValue(true, cardList.get(0).getValue(), CardType.SINGLE_CARD, 1, cardList, null, id);
        } else {
            return CardValue.errorWay();
        }
    }

    public static CardValue doubleCard(List<Card> cardList, int id) {
        if (cardList.size() == 2 && cardSimilar(cardList)) {
            return new CardValue(true, cardList.get(0).getValue(), CardType.DOUBLE_CARD, 2, cardList, null, id);
        } else {
            return CardValue.errorWay();
        }

    }


    public static CardValue threeTakeOne(List<Card> cardList, int id) {
        if (cardList.size() == 3 && cardSimilar(cardList)) {
            return CardValue.threeTakeOne(cardList,null,id);
        }

        if (cardList.size() == 4 || cardList.size() == 5) {
            Map<Integer, Integer> cardMap = new HashMap<>();
            int head = -1;
            int body = -1;
            for (Card card : cardList) {
                if (cardMap.containsKey(card.getValue())) {
                    cardMap.put(card.getValue(), cardMap.get(card.getValue()) + 1);
                } else {
                    cardMap.put(card.getValue(), 1);
                }
            }
            for (Map.Entry<Integer, Integer> integerIntegerEntry : cardMap.entrySet()) {
                if (integerIntegerEntry.getValue() == 3) {
                    head = integerIntegerEntry.getKey();
                } else {
                    body = integerIntegerEntry.getKey();
                }

            }
            if (head == -1 || (cardList.size() == 5 && cardMap.get(body) != 2)) {
                return CardValue.errorWay();
            }
            final int headFilter = head;
            final int bodyFilter = body;
            List<Card> headList = cardList.stream().filter(T -> T.getValue() == headFilter).collect(Collectors.toList());
            List<Card> bodyList = cardList.stream().filter(T -> T.getValue() == bodyFilter).collect(Collectors.toList());
            return CardValue.threeTakeOne(headList,bodyList,id);
        } else {
            return CardValue.errorWay();
        }

    }

    public static CardValue zhaDan(List<Card> cardList, int id) {
        if (cardList.size() == 2 && (16 == cardList.get(0).getValue() || 17 == cardList.get(0).getValue()) && (16 == cardList.get(1).getValue() || 17 == cardList.get(1).getValue())) {
            return new CardValue(true, 16, CardType.KING_BOMB, 2, cardList, null, id);
        }
        if (cardList.size() == 4) {
            Set<Integer> collect = new HashSet<>();
            for (Card card : cardList) {
                collect.add(card.getValue());
            }
            if (collect.size() == 1) {
                return new CardValue(true, cardList.get(0).getValue(), CardType.BOMB, 4, cardList, null, id);
            } else {
                return CardValue.errorWay();
            }
        } else {
            return CardValue.errorWay();
        }

    }

    public static CardValue lianDui(List<Card> cardList, int id) {
        int length = cardList.size();
        if (length >= 6 && length % 2 == 0) {
            Map<Integer, Integer> cardMap = new HashMap<>();
            for (Card card : cardList) {
                if (cardMap.containsKey(card.getValue())) {
                    cardMap.put(card.getValue(), cardMap.get(card.getValue()) + 1);
                } else {
                    cardMap.put(card.getValue(), 1);
                }
            }
            for (Integer value : cardMap.values()) {
                if (value != 2) {
                    return CardValue.errorWay();
                }
            }
            cardList.sort(Comparator.comparingInt(Card::getId));
            for (int i = 0; i < length - 2; i++) {
                if (cardList.get(i + 2).getValue() - cardList.get(i).getValue() != 1) {
                    return CardValue.errorWay();
                }
            }
            return new CardValue(true, cardList.get(length - 1).getValue(), CardType.LIAN_DUI, length, cardList, null, id);
        } else {
            return CardValue.errorWay();
        }

    }

    public static CardValue sunZi(List<Card> cardList, int id) {
        int length = cardList.size();
        if (length >= 5) {
            cardList.sort(Comparator.comparingInt(Card::getId));
            for (int i = 0; i < length - 1; i++) {
                if (cardList.get(i + 1).getValue() - cardList.get(i).getValue() != 1) {
                    return CardValue.errorWay();
                }
            }
            return new CardValue(true, cardList.get(length - 1).getValue(), CardType.SUN_ZI, length, cardList, null, id);

        } else {
            return CardValue.errorWay();
        }

    }

    public static CardValue fourTakeTwo(List<Card> cardList, int id) {
        int length = cardList.size();
        if (length == 6 || length == 8) {
            Map<Integer, Integer> integerIntegerMap = putListToMap(cardList);
            int flag = 0;
            int signalFlag = 1;
            for (Map.Entry<Integer, Integer> integerIntegerEntry : integerIntegerMap.entrySet()) {
                if (integerIntegerEntry.getValue() == 4) {
                    flag = integerIntegerEntry.getKey();
                }
                if (integerIntegerEntry.getValue() == 3) {
                    signalFlag = 0;
                }
            }
            if (integerIntegerMap.size() != 3 && integerIntegerMap.size() != 2 || signalFlag == 0 || flag == 0) {
                return CardValue.errorWay();
            }
            final int finalFlag = flag;
            List<Card> cardListRet = cardList.stream().filter(T -> T.getValue() == finalFlag).collect(Collectors.toList());
            List<Card> takeListRet = cardList.stream().filter(T -> T.getValue() != finalFlag).collect(Collectors.toList());
            return new CardValue(true, finalFlag, CardType.FOUR_TAKE_TWO, length, cardListRet, takeListRet, id);
        } else {
            return CardValue.errorWay();
        }

    }

    public static CardValue airPlan(List<Card> cardList, int id) {
        int length = cardList.size();
        Map<Integer, Integer> integerIntegerMap = putListToMap(cardList);
        int normalSize;
        if (length % 3 == 0) {
            normalSize = length / 3;
            List<Integer> retList = abtainMayList(integerIntegerMap, normalSize);
            //不允许2当成飞机出
            if (retList.size() != 0 && retList.get(retList.size() - 1) < 15){
                List<List<Card>> cardAndTakeCard = getCardAndTakeCard(cardList, retList);
                if (normalSize > 0 && retList.size() == normalSize) {
                    return new CardValue(true, retList.get(retList.size() - 1), CardType.AIR_PLAN, length, cardAndTakeCard.get(0), cardAndTakeCard.get(1), id);
                }
            }
        }

        if (length % 4 == 0) {
            normalSize = length / 4;
            List<Integer> retList = abtainMayList(integerIntegerMap, normalSize);
            if (retList.size() != 0 && retList.get(retList.size() - 1) < 15) {
                List<List<Card>> cardAndTakeCard = getCardAndTakeCard(cardList, retList);
                if (normalSize > 0 && retList.size() == normalSize) {
                    return new CardValue(true, retList.get(retList.size() - 1), CardType.AIR_PLAN, length, cardAndTakeCard.get(0), cardAndTakeCard.get(1), id);
                }
            }

        }
        if (length % 5 == 0) {
            normalSize = length / 5;
            List<Integer> retList = abtainMayList(integerIntegerMap, normalSize);
            if (retList.size() != 0 && retList.get(retList.size() - 1) < 15) {
                List<List<Card>> cardAndTakeCard = getCardAndTakeCard(cardList, retList);
                Map<Integer, Integer> cardMap = putListToMap(cardAndTakeCard.get(1));
                for (Map.Entry<Integer, Integer> integerIntegerEntry : cardMap.entrySet()) {
                    if (integerIntegerEntry.getValue() != 2) {
                        return CardValue.errorWay();
                    }
                }
                if (normalSize > 0 && retList.size() == normalSize) {
                    return new CardValue(true, retList.get(retList.size() - 1), CardType.AIR_PLAN, length, cardAndTakeCard.get(0), cardAndTakeCard.get(1), id);
                }
            }
        }
        return CardValue.errorWay();


    }


    public static CardValue receiveCard(CardValue previous, List<Card> sendCard, int id) {
        if (previous == null) {
            return CardValue.errorWay();
        }
        CardType type = previous.getType();
        CardValue cardValue = getValueByType(type,sendCard,id);
        if (cardValue.getCheck() && cardValue.getValue() > previous.getValue() && cardValue.getNum() == previous.getNum()) {
            return cardValue;
        }
        CardValue boom = zhaDan(sendCard, id);
        if (CardType.isSameType(type,CardType.BOMB)) {
            if (cardValue.getCheck() && cardValue.getValue() > previous.getValue()) {
                return cardValue;
            }
        }else {
            if (boom.getCheck()){
                return boom;
            }
        }
        return null;
    }

    public static CardValue getValueByType(CardType type,List<Card> sendCard, int id){
        if (CardType.isSameType(type,CardType.SINGLE_CARD)) {
                return singleCard(sendCard, id);
        }
        if (CardType.isSameType(type,CardType.DOUBLE_CARD)) {
                return doubleCard(sendCard, id);
        }

        if (CardType.isSameType(type,CardType.THREE_TAKE_ONE)) {
                return threeTakeOne(sendCard, id);
        }

        if (CardType.isSameType(type,CardType.AIR_PLAN)) {
            return airPlan(sendCard, id);
        }

        if (CardType.isSameType(type,CardType.LIAN_DUI)) {
                return lianDui(sendCard, id);
        }

        if (CardType.isSameType(type,CardType.SUN_ZI)) {
          return sunZi(sendCard, id);
        }

        if (CardType.isSameType(type,CardType.FOUR_TAKE_TWO)){
           return fourTakeTwo(sendCard,id);
        }

        if (CardType.isSameType(type,CardType.BOMB)) {
            return zhaDan(sendCard, id);
        }

        return CardValue.errorWay();
    }

    public static CardValue getCardType(List<Card> cardList, int id) {
        CardValue cardValue;
        cardValue = zhaDan(cardList, id);
        if (cardValue.getCheck()) {
            return cardValue;
        }
        cardValue = sunZi(cardList, id);
        if (cardValue.getCheck()) {
            return cardValue;
        }
        cardValue = lianDui(cardList, id);
        if (cardValue.getCheck()) {
            return cardValue;
        }
        cardValue = threeTakeOne(cardList, id);
        if (cardValue.getCheck()) {
            return cardValue;
        }
        cardValue = doubleCard(cardList, id);
        if (cardValue.getCheck()) {
            return cardValue;
        }
        cardValue = singleCard(cardList, id);
        if (cardValue.getCheck()) {
            return cardValue;
        }
        cardValue = fourTakeTwo(cardList, id);
        if (cardValue.getCheck()) {
            return cardValue;
        }
        cardValue = airPlan(cardList, id);
        if (cardValue.getCheck()) {
            return cardValue;
        }
        return CardValue.errorWay();
    }

    public static CardValue autoReceive(CardValue previous, List<Card> myCard, List<List<Card>> allList, int id, int lordId,String roomId) {
        if (previous == null || CardType.isSameType(previous.getType(),CardType.KING_BOMB)){
            return CardValue.errorWay();
        }
        SpecialList specialList = PukeUtil.packageSpecialCard(myCard);
        if (!PukeRole.myCardCouldReceive(previous,specialList,false)){
            return CardValue.errorWay();
        }
        CardMap cardMap = PukeRole.getCardRankMap(allList);
        List<Card> otherList = new ArrayList<>(allList.get((id+1)%3));
        otherList.addAll(allList.get((id + 2) % 3));
        SpecialList otherSpecialList = PukeUtil.packageSpecialCard(otherList);
        myCard.sort(Comparator.comparingInt(Card::getId));
        List<Card> ret = new ArrayList<>();
        boolean isMyFriendSend = PukeRole.isMyFriendSend(previous.getSendId(),lordId,id);
        boolean friendWillGetRounds  = PukeRole.friendWillGetRounds(previous.getSendId(),lordId,id);
        WarningLevel warningLevel = PukeRole.getWarningLevel(allList,lordId,id,previous.getSendId());
        OppInfo oppInfo = PukeRole.getOppInfo(allList,lordId,id);
        CardValue dfOneTimeSend;
        int friendId = 3-id-lordId;
        if (friendWillGetRounds && allList.get(friendId).size() == 1){
            return CardValue.errorWay();
        }

        dfOneTimeSend = PukeRole.dfSendCard(specialList,otherSpecialList,previous,warningLevel,id,!isMyFriendSend,false,System.currentTimeMillis(),oppInfo);

        if (dfOneTimeSend.getCheck()){
            return dfOneTimeSend;
        }

        Map<Integer, CardValue> userLastNotReceiveMap = new HashMap<>(2);
        if (id != lordId){
            userLastNotReceiveMap = PukeRoom.getUserLastNotReceive(roomId, friendId,true);
            CardValue cardTemp = PukeRole.couldHelpMyFriendOnReceive(otherSpecialList,previous,userLastNotReceiveMap,warningLevel,id,lordId,allList.get(friendId).size(),roomId);
            // 选择不出帮助好友出牌
            if (cardTemp.getCheck()){
                return CardValue.errorWay();
            }
        }

        if ( (isMyFriendSend && !PukeRole.myCardCouldReceive(previous,otherSpecialList,false))){
            return CardValue.errorWay();
        }

        if (CardType.isSameType(previous.getType(),CardType.SINGLE_CARD)) {
            CardValue cardValue = autoRecSingle(allList, specialList,otherSpecialList, previous,userLastNotReceiveMap, id, lordId,warningLevel,isMyFriendSend,friendWillGetRounds,cardMap);
            if (cardValue.getCheck()) {
                return cardValue;
            }
            if (isMyFriendSend){
                return CardValue.errorWay();
            }
        }

        if (CardType.isSameType(previous.getType(),CardType.DOUBLE_CARD) && PukeRole.getMaxByTimes(specialList,2) != -1) {
            List<Card> firstList = allList.get((id+1)%3);
            List<Card> secondList = allList.get((id+2)%3);
            boolean nextOpp = PukeRole.nextOpp(id,lordId);
            if (friendWillGetRounds && cardMap.compareRankOfMax(previous) < cardMap.thirdMapSize()){
                return CardValue.errorWay();
            }
            if (!isMyFriendSend) {
                CardValue cardValue = getDoubleFromDoubleList(specialList,otherSpecialList,userLastNotReceiveMap,warningLevel,cardMap,previous,isMyFriendSend,id,nextOpp);
                if (cardValue.getCheck()){
                    return cardValue;
                }
                if (WarningLevel.levelHigher(WarningLevel.ELEVEN_WARNING,warningLevel)) {
                    cardValue = getDoubleByTimes(specialList, cardMap, previous, warningLevel, ret, 2, id);
                    if (cardValue.getCheck()) {
                        return cardValue;
                    }

                    cardValue = getDoubleByTimes(specialList, cardMap, previous, warningLevel, ret, 3, id);
                    if (cardValue.getCheck()) {
                        return cardValue;
                    }
                }

            } else {
                int maxValue = Math.max(firstList.get(firstList.size() - 1).getValue(), secondList.get(secondList.size() - 1).getValue());
                boolean hasLittleSingle = hasLittleSingle(specialList, maxValue);
                if (WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING, warningLevel)) {
                    if (hasLittleSingle && previous.getSendId() != lordId) {
                        return CardValue.errorWay();
                    }
                    if (specialList.getDoubleList().size() > 0 && specialList.getDoubleList().get(specialList.getDoubleList().size() - 1).get(0).getValue() > previous.getValue()) {
                        ret.addAll(specialList.getDoubleList().get(specialList.getDoubleList().size() - 1));
                        return new CardValue(true, ret.get(0).getValue(), CardType.DOUBLE_CARD, previous.getNum(), ret, null, id);
                    }
                }
                if (friendWillGetRounds) {
                    return CardValue.errorWay();
                }

                if (WarningLevel.isSameLevel(WarningLevel.DOUBLE_WARNING, warningLevel)) {
                    int maxDouble = PukeRole.getMaxByTimes(otherSpecialList,2);
                    if (previous.getValue() >= maxDouble){
                        return CardValue.errorWay();
                    }
                    for (List<Card> list : specialList.getCardCountList()) {
                        if (list == null || list.size() == 0) {
                            continue;
                        }

                        if (list.get(0).getValue() < maxDouble){
                            continue;
                        }

                        if (list.size() > 1 && list.size() < 4) {
                            if (list.get(0).getValue() > previous.getValue()) {
                                ret.add(list.get(0));
                                ret.add(list.get(1));
                                return CardValue.doubleCard(ret, id);
                            }
                        }

                    }

                    for (List<Card> list : specialList.getCardCountList()) {
                        if (list == null || list.size() == 0) {
                            continue;
                        }

                        if (list.size() > 1 && list.size() < 4) {
                            if (list.get(0).getValue() > previous.getValue()) {
                                ret.add(list.get(0));
                                ret.add(list.get(1));
                                return CardValue.doubleCard(ret, id);
                            }
                        }

                    }
                }

                CardValue cardValue = getDoubleFromDoubleList(specialList,otherSpecialList,userLastNotReceiveMap,warningLevel,cardMap,previous,isMyFriendSend,id,nextOpp);
                if (cardValue.getCheck()){
                    return cardValue;
                }

                if (WarningLevel.levelHigher(WarningLevel.ELEVEN_WARNING,warningLevel)) {
                    for (List<Card> list : specialList.getDoubleList()) {
                        if (cardMap.compareRankOfMax(list.get(0)) < cardMap.halfMapSize() && specialList.getMyCard().size() > 3) {
                            return CardValue.errorWay();
                        }
                        if (list.get(0).getValue() > previous.getValue()) {
                            ret.add(list.get(0));
                            ret.add(list.get(1));
                            return CardValue.doubleCard(ret, id);
                        }
                    }
                }

                return CardValue.errorWay();
            }
        }


        if (CardType.isSameType(previous.getType(),CardType.THREE_TAKE_ONE)) {
            if (isMyFriendSend && (myCard.size() > 5 || previous.getValue() > 9)){
                return CardValue.errorWay();
            }
            List<Card> takeList = new ArrayList<>();
            for (List<Card> cardList : specialList.getAirList()) {
                if (cardList.get(0).getValue() <= previous.getValue()){
                    continue;
                }

                if (cardMap.compareRankOfMax(cardList.get(0)) < 7 && cardMap.compareRank(cardList.get(0),previous) > cardMap.thirdMapSize() && !WarningLevel.levelHigher(WarningLevel.NINE_WARNING,warningLevel)){
                    break;
                }

                if (cardMap.compareRank(cardList.get(0),previous) > cardMap.thirdMapSize() && !WarningLevel.levelHigher(WarningLevel.SAFE,warningLevel)){
                    break;
                }

                if (cardMap.compareRank(cardList.get(0),previous) > cardMap.halfMapSize() && (!mayOneTimeSend(otherSpecialList,warningLevel) || !WarningLevel.levelHigher(WarningLevel.FIVE_WARNING,warningLevel)) ){
                    break;
                }

                if (previous.getNum() == 3){
                    return CardValue.threeTakeOne(cardList,new ArrayList<>(),id);
                }

                if (previous.getNum() == 4){
                    if (specialList.getNormalList().size() == 0 && specialList.getDoubleList().size() == 0){
                         break;
                    }

                    if (specialList.getNormalList().size() == 0 && specialList.getDoubleList().size() > 0){
                        if (cardMap.compareRankOfMax(specialList.getDoubleList().get(0).get(0)) < cardMap.fourMapSize() && !WarningLevel.levelHigher(WarningLevel.FIVE_WARNING,warningLevel)){
                            break;
                        }
                        takeList.add(specialList.getDoubleList().get(0).get(0));
                        return CardValue.threeTakeOne(cardList,takeList,id);
                    }

                    if (specialList.getNormalList().size() > 0 && specialList.getDoubleList().size() == 0){
                        if (cardMap.compareRankOfMax(specialList.getNormalList().get(0)) < cardMap.fourMapSize() && !WarningLevel.levelHigher(WarningLevel.FIVE_WARNING,warningLevel)){
                            break;
                        }
                        takeList.add(specialList.getNormalList().get(0));
                        return CardValue.threeTakeOne(cardList,takeList,id);
                    }

                    if ( specialList.getNormalList().get(0).getValue() < 11 || specialList.getNormalList().get(0).getValue() < specialList.getDoubleList().get(0).get(0).getValue()){
                        takeList.add(specialList.getNormalList().get(0));
                        return CardValue.threeTakeOne(cardList,takeList,id);
                    }

                    takeList.add(specialList.getDoubleList().get(0).get(0));
                    return CardValue.threeTakeOne(cardList,takeList,id);

                }

                if (previous.getNum() == 5){
                    if (specialList.getDoubleList().size() == 0){
                        break;
                    }
                    if (cardMap.compareRankOfMax(specialList.getDoubleList().get(0).get(0)) < cardMap.halfMapSize() && !WarningLevel.levelHigher(WarningLevel.FIVE_WARNING,warningLevel)){
                        break;
                    }

                    return CardValue.threeTakeOne(cardList,specialList.getDoubleList().get(0),id);
                }
            }



            if (WarningLevel.levelHigher(WarningLevel.NINE_WARNING,warningLevel)){
               CardValue cardValue = dangerReplayThreeTakeOne(myCard,previous,isMyFriendSend,warningLevel,id);
               if (cardValue.getCheck()){
                   return cardValue;
               }
            }
        }

        if (CardType.isSameType(previous.getType(),CardType.BOMB)) {
            if (isMyFriendSend) {
                return CardValue.errorWay();
            }

            if (!mayOneTimeSend(otherSpecialList,warningLevel)){
                return CardValue.errorWay();
            }

            for (List<Card> list : specialList.getZhaDanList()) {
                if (list.get(0).getValue() > previous.getValue()){
                    return CardValue.zhDan(list,id);
                }
            }

            if (specialList.getKingBombList().size() != 0){
                return CardValue.kingBomb(specialList.getKingBombList(),id);
            }
            return CardValue.errorWay();

        }

        if (CardType.isSameType(previous.getType(),CardType.LIAN_DUI)) {
            if (isMyFriendSend) {
                return CardValue.errorWay();
            }
            for (List<Card> list : specialList.getLianDuiList()) {
                if (list.size() >= previous.getNum()){
                    for (int i = 0; i < list.size(); i = i+2) {
                        int endIndex = i+ previous.getNum() - 1;
                        if (endIndex >= list.size()){
                            break;
                        }
                        if (list.get(endIndex).getValue() <= previous.getValue()){
                            continue;
                        }

                        if (cardMap.compareRank(list.get(endIndex),previous) > cardMap.halfMapSize() && !WarningLevel.levelHigher(WarningLevel.HAVE_SOME_WARNING,warningLevel)){
                            break;
                        }


                        for (int j = i; j <= endIndex; j++) {
                            ret.add(list.get(j));
                        }

                        return CardValue.lianDui(ret,id);
                    }

                }

            }

        }

        if (CardType.isSameType(previous.getType(),CardType.SUN_ZI) && !isMyFriendSend) {
                Map<Integer, Integer> integerMap = putListToMap(myCard);
                CardValue sunZi = PukeRole.receiveSunZi(specialList.getUsefulSunZiList(),previous,warningLevel,id);
                if (sunZi.getCheck()){
                    return sunZi;
                }
                if (WarningLevel.levelHigher(WarningLevel.SAFE,warningLevel)){
                    sunZi = PukeRole.receiveSunZi(specialList.getSunZiList(),previous,warningLevel,id);
                    if (sunZi.getCheck()){
                        return sunZi;
                    }
                }
        }


        if (CardType.isSameType(previous.getType(),CardType.FOUR_TAKE_TWO)) {
            if (previous.getSendId() != lordId && id != lordId) {
                return CardValue.errorWay();
            }
            Map<Integer, Integer> integerMap = putListToMap(myCard);
            CardValue fourTakeTwo = checkMapFourTakeTwo(integerMap, myCard, previous, CardType.FOUR_TAKE_TWO, id);
            if (fourTakeTwo.getCheck()) {
                return fourTakeTwo;
            }
        }

        if (CardType.isSameType(previous.getType(),CardType.AIR_PLAN)) {
            if (isMyFriendSend) {
                return CardValue.errorWay();
            }

            CardValue airPlan = checkMapAirPlan(specialList, myCard, previous, id);
            if (airPlan.getCheck()) {
                return airPlan;
            }
        }


        if (!CardType.isSameType(previous.getType(),CardType.BOMB)) {
            if (isMyFriendSend) {
                return CardValue.errorWay();
            }
            if (!mayOneTimeSend(otherSpecialList,warningLevel)){
                return CardValue.errorWay();
            }
            for (List<Card> list : specialList.getZhaDanList()) {
                return CardValue.zhDan(list,id);
            }
            if (specialList.getKingBombList().size() != 0 && (otherSpecialList.getCardCount()[15] < 2 || WarningLevel.levelHigher(WarningLevel.FIVE_WARNING,warningLevel))){
                return CardValue.kingBomb(specialList.getKingBombList(),id);
            }
        }
        return CardValue.errorWay();

    }

    public static CardValue dangerReplayThreeTakeOne(List<Card> myCard,CardValue previous,boolean isMyFriendSend,WarningLevel warningLevel,int id){
        int length = myCard.size();
        List<Card> ret = new ArrayList<>();
        List<Card> takeOne = new ArrayList<>();
        if (!isMyFriendSend) {
            for (int i = 0; i < length - 2; i++) {
                if (myCard.get(i).getValue() - previous.getValue() > 4 && !WarningLevel.levelHigher(WarningLevel.NO_WARNING,warningLevel)){
                    break;
                }

                if (myCard.get(i).getValue() - previous.getValue() > 6 && !WarningLevel.levelHigher(WarningLevel.HAVE_SOME_WARNING,warningLevel)){
                    break;
                }

                if (myCard.get(i).getValue() - previous.getValue() > 8 && !WarningLevel.levelHigher(WarningLevel.FIVE_WARNING,warningLevel)){
                    break;
                }

                if (myCard.get(i).getValue() > previous.getValue() && myCard.get(i).getValue() == myCard.get(i + 1).getValue() && myCard.get(i).getValue() == myCard.get(i + 2).getValue()) {
                    int ii = i;
                    List<Card> filterList = myCard.stream().filter(T -> T.getValue() != myCard.get(ii).getValue()).sorted(Comparator.comparing(Card::getId)).collect(Collectors.toList());
                    if (previous.getNum() == 3){
                        ret.add(myCard.get(i));
                        ret.add(myCard.get(i + 1));
                        ret.add(myCard.get(i + 2));
                        return CardValue.threeTakeOne(ret,null,id);
                    }
                    else if (previous.getNum() == 4) {
                        ret.add(myCard.get(i));
                        ret.add(myCard.get(i + 1));
                        ret.add(myCard.get(i + 2));
                        takeOne.add(filterList.get(0));
                        return CardValue.threeTakeOne(ret,takeOne,id);
                    } else if (previous.getNum() == 5) {
                        for (int j = 0; j < filterList.size() - 1; j++) {
                            if (filterList.get(j).getValue() == filterList.get(j + 1).getValue()) {
                                ret.add(myCard.get(i));
                                ret.add(myCard.get(i + 1));
                                ret.add(myCard.get(i + 2));
                                takeOne.add(filterList.get(j));
                                takeOne.add(filterList.get(j + 1));
                                return CardValue.threeTakeOne(ret,takeOne,id);
                            }

                        }

                    }

                }
            }
        } else {
            return CardValue.errorWay();
        }
        return CardValue.errorWay();
    }


    public static boolean mayOneTimeSend(SpecialList otherSpecialList,WarningLevel warningLevel){
        if (warningLevel.getCode() == 1){
            return true;
        }

        if (warningLevel.getCode() == 2 && otherSpecialList.getDoubleList().size() > 0){
            return true;
        }

        List<Card> retList = new ArrayList<>();
        if ((warningLevel.getCode() == 4 || warningLevel.getCode() == 3) && PukeRole.getMaxByTimes(otherSpecialList,3) != -1){
//            retList.add(otherSpecialList.getAirList().get(0).get(0));
//            retList.add(otherSpecialList.getAirList().get(0).get(1));
//            Set<Integer> set = retList.stream().map(Card::getId).collect(Collectors.toSet());
//            List<Card> cardList = otherSpecialList.getMyCard().stream().filter(t -> !set.contains(t.getId())).collect(Collectors.toList());
//            getCardByRandom(cardList,retList, warningLevel.getCode()-2);
//            return retList.get(0).getValue() == retList.get(2).getValue();
            return true;
        }

        if (warningLevel.getCode() == 5){
            if (otherSpecialList.getSunZiList().size() > 0){
                retList.add(otherSpecialList.getSunZiList().get(0).get(0));
                retList.add(otherSpecialList.getSunZiList().get(0).get(1));
                retList.add(otherSpecialList.getSunZiList().get(0).get(2));
                retList.add(otherSpecialList.getSunZiList().get(0).get(3));
                Set<Integer> set = retList.stream().map(Card::getId).collect(Collectors.toSet());
                List<Card> cardList = otherSpecialList.getMyCard().stream().filter(t -> !set.contains(t.getId())).collect(Collectors.toList());
                getCardByRandom(cardList,retList, warningLevel.getCode()-4);
                boolean flag = true;
                for (int i = 1; i < retList.size(); i++) {
                    if (retList.get(i).getValue() - retList.get(i-1).getValue() == 1){
                        continue;
                    }
                    flag = false;
                    break;
                }

                if (flag){
                    return true;
                }

            }


            if ((otherSpecialList.getAirList().size() > 0 && otherSpecialList.getDoubleList().size() > 0)){
                List<Card> cardList = otherSpecialList.getMyCard();
                retList = new ArrayList<>();
                getCardByRandom(cardList,retList, 3);
                Map<Integer,Integer> map = new HashMap<>();
                retList.forEach(t->map.put(t.getValue(),map.getOrDefault(t.getValue(),0)+1));
                for (Integer value : map.values()) {
                    if (value == 2){
                        return true;
                    }
                }
            }
            return false;
        }

        if (warningLevel.getCode() == 6 && otherSpecialList.getLianDuiList().size() > 0){
            retList.add(otherSpecialList.getLianDuiList().get(0).get(0));
            retList.add(otherSpecialList.getLianDuiList().get(0).get(1));
            retList.add(otherSpecialList.getLianDuiList().get(0).get(2));
            retList.add(otherSpecialList.getLianDuiList().get(0).get(3));
            retList.add(otherSpecialList.getLianDuiList().get(0).get(4));
            Set<Integer> set = retList.stream().map(Card::getId).collect(Collectors.toSet());
            List<Card> cardList = otherSpecialList.getMyCard().stream().filter(t -> !set.contains(t.getId())).collect(Collectors.toList());
            getCardByRandom(cardList,retList, 1);
            boolean flag = true;
            for (int i = 1; i < cardList.size(); i++) {
                if (i % 2 == 1){
                    flag = cardList.get(i).getValue() == cardList.get(i-1).getValue();
                }else {
                    flag = cardList.get(i).getValue() - cardList.get(i-2).getValue() == 1;
                }
                if (!flag){
                    break;
                }
            }
            return flag;
        }

        return false;
    }

    public static void getCardByRandom(List<Card> cardList,List<Card> retList,int needSize){
        int size = cardList.size();
        Map<Integer,Integer> map = new HashMap<>();
        Random random = new Random();
        for (int i = 0; i < needSize; i++) {
          int index = random.nextInt(size);
          while (map.containsKey(index)){
              index = (index+1) % size;
          }
          map.put(index,index);
          retList.add(cardList.get(index));
        }

        retList.sort(Comparator.comparing(Card::getId));
    }

    public static Map<Integer, Integer> putListToMap(List<Card> cardList) {
        Map<Integer, Integer> cardMap = new HashMap<>();
        for (Card card : cardList) {
            cardMap.put(card.getValue(),cardMap.getOrDefault(card.getValue(),0)+1);
        }
        return cardMap;
    }

    public static List<List<Integer>> putListToIndexList(List<Card> cardList) {
        List<List<Integer>> ret = new ArrayList<>();
        Map<Integer, Integer> integerIntegerMap = putListToMap(cardList);
        for (int i = 1; i <= 4; i++) {
            HashSet<Integer> hashSet = new HashSet<>();
            for (Card cardd : cardList) {
                if (integerIntegerMap.get(cardd.getValue()) == i) {
                    hashSet.add(cardd.getValue());
                }
            }
            List<Integer> tempList = new ArrayList<>(hashSet);
            tempList.sort(Comparator.comparing(Integer::valueOf));
            ret.add(tempList);
        }
        return ret;

    }

    public static CardValue checkMapSunZi(Map<Integer, Integer> map, List<Card> myCard, CardValue card, CardType type, int id) {
        int num = card.getNum();
        int value = card.getValue();
        for (int i = value + 1; i < 15; i++) {
            if (map.containsKey(i)) {
                int count;
                if (CardType.isSameType(type,CardType.LIAN_DUI)) {
                    count = 2;
                    for (int j = 1; j < num / 2; j++) {
                        if (map.containsKey(i - j)) {
                            count = count + 2;
                        }

                    }
                } else {
                    count = 1;
                    for (int j = 1; j < num; j++) {
                        if (map.containsKey(i - j)) {
                            count++;
                        }

                    }
                }
                if (count == num) {
                    List<Card> cardList = new ArrayList<>();
                    if (CardType.isSameType(type,CardType.LIAN_DUI)) {
                        for (int j = 0; j < num / 2; j++) {
                            int sum = 0;
                            for (Card cardValue : myCard) {
                                if (i - j == cardValue.getValue() && sum < 2) {
                                    cardList.add(cardValue);
                                    sum++;
                                }

                            }

                        }
                    } else if (CardType.isSameType(type,CardType.SUN_ZI)) {
                        for (int j = 0; j < num; j++) {
                            int sum = 0;
                            for (Card cardValue : myCard) {
                                if (i - j == cardValue.getValue() && sum < 1) {
                                    cardList.add(cardValue);
                                    sum++;
                                }
                            }

                        }
                    }
                    cardList.sort(Comparator.comparingInt(Card::getId));
                    return new CardValue(true, i, type, num, cardList, null, id);
                }

            }
        }
        return CardValue.errorWay();
    }

    public static CardValue checkMapFourTakeTwo(Map<Integer, Integer> map, List<Card> myCard, CardValue card, CardType type, int id) {
        List<Integer> oneCount = new ArrayList<>();
        List<Integer> twoCount = new ArrayList<>();
        List<Integer> fourCount = new ArrayList<>();
        for (Map.Entry<Integer, Integer> integerIntegerEntry : map.entrySet()) {
            if (integerIntegerEntry.getValue() == 1) {
                oneCount.add(integerIntegerEntry.getKey());
            }
            if (integerIntegerEntry.getValue() == 2) {
                twoCount.add(integerIntegerEntry.getKey());
                oneCount.add(integerIntegerEntry.getKey());
                oneCount.add(integerIntegerEntry.getKey());
            }
            if (integerIntegerEntry.getValue() == 4) {
                fourCount.add(integerIntegerEntry.getKey());
            }
        }
        List<Card> cardList;
        List<Card> takeList = new ArrayList<>();

        if (card.getNum() == 6) {
            for (Integer integer : fourCount) {
                if (integer > card.getValue()) {
                    if (oneCount.size() >= 2) {
                        oneCount.sort(Comparator.comparingInt(Integer::valueOf));
                        cardList = myCard.stream().filter(T -> T.getValue() == integer).collect(Collectors.toList());
                        int count = 0;
                        for (Card value : myCard) {
                            if (count >= 2) {
                                break;
                            }
                            if (value.getValue() == oneCount.get(0) || value.getValue() == oneCount.get(1)) {
                                takeList.add(value);
                                count++;
                            }
                        }
                        return new CardValue(true, integer, type, card.getNum(), cardList, takeList, id);

                    }

                }

            }

        }


        if (card.getNum() == 8) {
            for (Integer integer : fourCount) {
                if (integer > card.getValue()) {
                    if (twoCount.size() >= 2) {
                        twoCount.sort(Comparator.comparingInt(Integer::valueOf));
                        cardList = myCard.stream().filter(T -> T.getValue() == integer).collect(Collectors.toList());
                        int count = 0;
                        for (Card value : myCard) {
                            if (count >= 4) {
                                break;
                            }
                            if (value.getValue() == twoCount.get(0) || value.getValue() == twoCount.get(1)) {
                                takeList.add(value);
                                count++;
                            }
                        }
                        return new CardValue(true, integer, type, card.getNum(), cardList, takeList, id);

                    }

                }

            }

        }
        return CardValue.errorWay();

    }

    public static CardValue checkMapAirPlan(SpecialList specialList, List<Card> myCard, CardValue cardValue, int id) {
        List<Card> retList = new ArrayList<>();
        List<Card> takeList = new ArrayList<>();
        List<List<Card>> airPlanList = specialList.getAirPlanList();
        int airSize = cardValue.getNum() % 4 == 0 ? cardValue.getNum() / 4 : cardValue.getNum() / 5;
        for (List<Card> list : airPlanList) {
            for (int i = 0; i < list.size(); i = i+3) {
                if (i + airSize * 3 > list.size()){
                    break;
                }

                int endIndex = i + airSize * 3 - 1;
                if (list.get(endIndex).getValue() > cardValue.getValue()){
                    for (int j = i; j <= endIndex; j++) {
                        retList.add(list.get(j));
                    }
                    break;
                }
            }
            if (retList.size() != 0){
                break;
            }
        }

        if (retList.size() == 0){
            return CardValue.errorWay();
        }

        Set<Integer> airSet = new HashSet<>();
        for (Card cardTemp : retList) {
            airSet.add(cardTemp.getValue());
        }
        boolean takeSingle = cardValue.getNum() % 4 == 0;
        if (takeSingle){
            for (Card cardTemp : specialList.getNormalAndDouble()) {
                if(takeList.size() < airSize){
                    takeList.add(cardTemp);
                }else {
                    break;
                }
            }
            if (takeList.size() == airSize) {
                return CardValue.airPlan(retList,takeList,id);
            }

                takeList.clear();
                for (List<Card> list : specialList.getCardCountList()) {
                    if (list == null || list.size() == 0 || list.size() == 4 || (list.size() == 3 && airSet.contains(list.get(0).getValue()))){
                        continue;
                    }

                    for (Card cardTemp : list) {
                        takeList.add(cardTemp);
                        if (takeList.size() == airSize){
                            break;
                        }
                    }
                }

            if (takeList.size() == airSize){
                return CardValue.airPlan(retList,takeList,id);
            }

            }else {
            for (List<Card> list : specialList.getDoubleList()) {
                takeList.addAll(list);
                if (takeList.size() == airSize * 2){
                    break;
                }
            }

            if (takeList.size() == airSize * 2){
                return CardValue.airPlan(retList,takeList,id);
            }
            takeList.clear();
            for (List<Card> list : specialList.getCardCountList()) {
                if (list == null || list.size() == 0 || list.size() == 1 || list.size() == 4 || (list.size() == 3 && airSet.contains(list.get(0).getValue()))){
                    continue;
                }

                for (int i = 0; i < 2; i++) {
                    takeList.add(list.get(0));
                    if (takeList.size() == airSize){
                        break;
                    }
                }

            }

            if (takeList.size() == airSize * 2){
                return CardValue.airPlan(retList,takeList,id);
            }

        }
        return CardValue.errorWay();
    }

    public static List<Card> findValueByTime(List<Card> cardList, List<Integer> fileList, int count) {
        List<Card> retList = new ArrayList<>();
        for (Integer integer : fileList) {
            int countt = 0;
            for (Card card : cardList) {
                if (card.getValue() == integer) {
                    retList.add(card);
                    countt++;
                }
                if (countt >= count) {
                    break;
                }
            }

        }
        retList.sort(Comparator.comparing(Card::getId));
        return retList;
    }

    public static List<Card> findValueByTime(List<Card> cardList, Integer fileInteger, int count) {
        List<Card> retList = new ArrayList<>();
        int countt = 0;
        for (Card card : cardList) {
            if (card.getValue() == fileInteger) {
                retList.add(card);
                countt++;
            }
            if (countt >= count) {
                break;
            }
        }
        retList.sort(Comparator.comparing(Card::getId));
        return retList;
    }


    public static List<Card> afterAutoRecive(List<Card> previous, CardValue cardValue) {
        return  removePrevious(previous, cardValue);
    }

    public static List<Card> removePrevious(List<Card> previous, CardValue value) {
        if (value == null){
            return previous;
        }

        Map<Integer,Integer> map = new HashMap<>();
        List<Integer> removeId = new ArrayList<>();
        if (value.getCard() != null){
            removeId.addAll(value.getCard().stream().map(Card::getId).collect(Collectors.toList()));
        }
        if (value.getTakeCard() != null){
            removeId.addAll(value.getTakeCard().stream().map(Card::getId).collect(Collectors.toList()));
        }
        removeId.forEach(t->map.put(t,t));
        return previous.stream().filter(t->!map.containsKey(t.getId())).collect(Collectors.toList());
    }


    public static List<List<Card>> distributeCard(int count) {
        Set<Integer> cardDataBase = new HashSet<>();
        List<Integer> ret = new ArrayList<>();
        while (ret.size() < 54) {
            int random = (int) (Math.random() * 54);
            if (!cardDataBase.contains(random)) {
                ret.add(random);
                cardDataBase.add(random);
            }

        }
        int cardSize = (54 / count) - 1;
        List<List<Card>> retCardList = new ArrayList<>();
        for (int j = 0; j < count; j++) {
            List<Card> tempList = new ArrayList<>();
            for (int i = 0; i < cardSize; i++) {
                tempList.add(card[ret.get(j * cardSize + i)]);
            }
            tempList.sort((T1, T2) -> T2.getId() - T1.getId());
            retCardList.add(tempList);
        }
        List<Card> tempList = new ArrayList<>();
        tempList.add(card[ret.get(51)]);
        tempList.add(card[ret.get(52)]);
        tempList.add(card[ret.get(53)]);
        tempList.sort((T1, T2) -> T2.getId() - T1.getId());
        retCardList.add(tempList);

        return retCardList;
    }

    public static List<List<Card>> distributeCardSpecial() {
//        Card[] cards = {card[0],card[1],card[2],card[4],card[5],card[6],card[8],card[9],card[10],card[16],card[23],card[24],card[25],card[30],card[48]};
//        Card[] cardF = {card[51],card[44],card[40],card[36],card[37],card[28],card[29],card[30],card[31],card[24],card[25],card[20],card[4],card[5]};
        Card[] cardF = {card[53],card[45],card[39],card[37],card[35],card[34],card[29],card[28],card[25],card[24],card[21],card[15],card[10],card[9],card[6],card[4],card[1]};
        Card[] cardS = {card[51],card[49],card[48],card[47],card[46],card[44],card[43],card[42],card[41],card[40],card[38],card[36],card[33],card[32],card[23],card[20],card[17],card[12],card[11],card[5]};
        Card[] cardT = {card[52],card[50],card[31],card[30],card[27],card[26],card[22],card[19],card[18],card[16],card[14],card[13],card[8],card[7],card[3],card[2],card[0]};
        List<Card> first = Arrays.asList(cardF);
        List<Card> second = Arrays.asList(cardS);
        List<Card> third = Arrays.asList(cardT);
        List<List<Card>> retList = new ArrayList<>();
        retList.add(first);
        retList.add(second);
        retList.add(third);
        return retList;

    }

//    public void puKeGame(int count) {
//        List<List<Card>> list = distributeCard(count);
//        autoShowDetail(list);
//        int lunCi = 1;
//        int reciveLunCi = 1;
//        CardValue cardType = null;
//        CardValue tempType;
//        while (list.get(0).size() != 0 && list.get(1).size() != 0 && list.get(2).size() != 0) {
//            if (lunCi == 1) {
//                System.out.println("请您出牌:");
//                Scanner scanner = new Scanner(System.in);
//                String s = scanner.nextLine();
//                List<String> inputList = stringToListNew(s);
//                tempType = actualListByWant(list.get(0), inputList,1);
//                while ("weiZhi".equals(tempType.getType())) {
//                    System.out.println("您出的牌型有误,请重新输入:");
//                    s = scanner.nextLine();
//                    inputList = stringToListNew(s);
//                    tempType = actualListByWant(list.get(0), inputList,1);
//                }
//                cardType = tempType;
//                reciveLunCi = 2;
//                afterAutoRecive(list.get(0), cardType);
//                lunCi = 1;
//                showSendCard(cardType, 1);
//                autoShowDetail(list);
//            }
//            if (lunCi != 1 ) {
//                System.out.println("请您接牌:");
//                Scanner scanner = new Scanner(System.in);
//                String s = scanner.nextLine();
//                if ("pass".equals(s)) {
//                    reciveLunCi = 2;
//                } else {
//                    List<String> inputList = stringToListNew(s);
//                    tempType = actualListByWant(list.get(0), inputList,1);
//                    while ("weiZhi".equals(tempType.getType())) {
//                        System.out.println("您出的牌型有误,请重新输入:");
//                        s = scanner.nextLine();
//                        inputList = stringToListNew(s);
//                        if ("pass".equals(s)) {
//                            reciveLunCi = 2;
//                            break;
//                        }
//                        tempType = actualListByWant(list.get(0), inputList,1);
//                    }
//                    if (reciveLunCi == 2) {
//                        break;         // 提前退出
//                    }
//                    cardType = tempType;
//                    reciveLunCi = 2;
//                    afterAutoRecive(list.get(0), cardType);
//                    lunCi = 1;
//                    showSendCard(cardType, 1);
//                    autoShowDetail(list);
//                }
//            }
//            if (lunCi == 2 ) {
//                CardValue autosend = autoSendCard(list.get(1),2);
//                reciveLunCi = 3;
//                cardType = autosend;
//                afterAutoRecive(list.get(1), cardType);
//                lunCi = 2;
//                showSendCard(cardType, 2);
//                autoShowDetail(list);
//
//            }
//            if (lunCi != 2 ) {
//                CardValue cardValue = autoReceive(cardType, list.get(1),2,1);
//                if ("weiZhi".equals(cardValue.getType())) {
//                    reciveLunCi = 3;
//                } else {
//                    reciveLunCi = 3;
//                    lunCi = 2;
//                    cardType = cardValue;
//                    afterAutoRecive(list.get(1), cardType);
//                    showSendCard(cardType, 2);
//                    autoShowDetail(list);
//                }
//
//            }
//
//            if (lunCi == 3 ) {
//                cardType = autoSendCard(list.get(2),3);
//                reciveLunCi = 1;
//                afterAutoRecive(list.get(2), cardType);
//                lunCi = 3;
//                showSendCard(cardType, 3);
//                autoShowDetail(list);
//            }
//            if (lunCi != 3 ) {
//                CardValue cardValue = autoReceive(cardType, list.get(2),3,1);
//                cardType = cardValue;
//                if ("weiZhi".equals(cardValue.getType())) {
//                    reciveLunCi = 1;
//                } else {
//                    reciveLunCi = 1;
//                    lunCi = 3;
//                    afterAutoRecive(list.get(2), cardType);
//                    showSendCard(cardType, 3);
//                    autoShowDetail(list);
//                }
//            }
//
//
//        }
//        for (int i = 0; i < count; i++) {
//            if (list.get(i).size() == 0) {
//                System.out.println("玩家" + (i + 1) + "胜利");
//            }
//        }
//
//
//    }

    public static CardValue attemptCombSunZi(Card card, Map<Integer, Integer> map, List<Card> myCard, int id) {
        int value = card.getValue();
        int count = 0;
        List<Card> retCard = new ArrayList<>();
        while (true) {
            if (value + count >= 15 || !map.containsKey(value + count) || map.get(value + count) == 4) {
                if (count < 5) {
                    return CardValue.errorWay();
                } else {
                    for (int i = 0; i < count; i++) {
                        for (Card cardd : myCard) {
                            if (cardd.getValue() == value + i) {
                                retCard.add(cardd);
                                break;
                            }
                        }
                    }
                    return new CardValue(true, value + count - 1, CardType.SUN_ZI, count, retCard, null, id);

                }

            }
            count++;
        }

    }

    public void autoShowDetail(List<List<Card>> list) {
        System.out.println("--------------------------------------------------------------------------");
        System.out.print("玩家一持牌:");
        list.get(0).forEach(T -> System.out.print(T.getDetail() + " "));
        System.out.print("(" + list.get(0).size() + ")");
        System.out.println();
        System.out.print("玩家二持牌:");
        list.get(1).forEach(T -> System.out.print(T.getDetail() + " "));
        System.out.print("(" + list.get(1).size() + ")");
        System.out.println();
        System.out.print("玩家三持牌:");
        list.get(2).forEach(T -> System.out.print(T.getDetail() + " "));
        System.out.print("(" + list.get(2).size() + ")");
        System.out.println();
        System.out.println("--------------------------------------------------------------------------");
    }

//    public List<String> stringToList(String s) {
//        String[] split = s.split("-");
//        int length = split.length;
//        return new ArrayList<>(Arrays.asList(split).subList(0, length));
//    }

    public List<String> stringToListNew(String s) {
        String[] split = s.split("");
        int length = split.length;
        return new ArrayList<>(Arrays.asList(split).subList(0, length));
    }

//    public List<Card> actualList(List<Card> myCard, List<String> inputCard) {
//        List<Card> ret = new ArrayList<>();
//        inputCard.forEach(T -> ret.add(myCard.get(Integer.parseInt(T) - 1)));
//        return ret;
//
//    }

    public static boolean cardSimilar(List<Card> list) {
        int size = list.size();
        int temp = list.get(0).getValue();
        for (int i = 1; i < size; i++) {
            if (list.get(i).getValue() != temp) {
                return false;
            }
        }
        return true;
    }


    public CardValue actualListByWant(List<Card> myCard, List<String> input, int id) {
        List<String> inputCard = new ArrayList<>();
        for (String s : input) {
            if ("j".equals(s) || "J".equals(s)) {
                inputCard.add("11");
            } else if ("q".equals(s) || "Q".equals(s)) {
                inputCard.add("12");
            } else if ("k".equals(s) || "K".equals(s)) {
                inputCard.add("13");
            } else if ("a".equals(s) || "A".equals(s)) {
                inputCard.add("14");
            } else if ("2".equals(s)) {
                inputCard.add("15");
            } else if ("w".equals(s)) {
                inputCard.add("16");
            } else if ("W".equals(s)) {
                inputCard.add("17");
            } else if ("l".equals(s)) {
                inputCard.add("10");
            } else {
                inputCard.add(s);
            }


        }

        Map<String, List<String>> collect = inputCard.stream().collect(Collectors.groupingBy(String::valueOf));
        List<Integer> valueList = new ArrayList<>();
        myCard.forEach(T -> valueList.add(T.getValue()));
        Bag bag = new HashBag<>(valueList);
        boolean flag = true;
        for (Map.Entry<String, List<String>> stringListEntry : collect.entrySet()) {
            if (bag.getCount(Integer.valueOf(stringListEntry.getKey())) < stringListEntry.getValue().size()) {
                flag = false;
            }
        }
        if (flag) {
            List<Card> inputCardList = new ArrayList<>(myCard);
            List<Card> actualList = new ArrayList<>();
            for (String s : inputCard) {
                for (int i = 0; i < inputCardList.size(); i++) {
                    if (Integer.parseInt(s) == inputCardList.get(i).getValue()) {
                        actualList.add(inputCardList.get(i));
                        inputCardList.remove(i);
                        break;
                    }
                }

            }

            return getCardType(actualList, id);
        } else {
            return CardValue.errorWay();
        }

    }

//    public List<List<String>> saveList(int index, List<String> after, List<List<String>> precious) {
//        int size = precious.size();
//        List<List<String>> ret = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//
//            if (i == index) {
//                ret.add(after);
//            } else {
//                ret.add(precious.get(i));
//            }
//
//        }
//        return ret;
//    }

    public void showSendCard(CardValue cardValue, int role) {
        int cardLength = cardValue.getCard().size();
        StringBuilder s = new StringBuilder("玩家");
        if (role == 1) {
            s.append("一:\n");
        }
        if (role == 2) {
            s.append("二:\n");
        }
        if (role == 3) {
            s.append("三:\n");
        }
        if (!cardValue.getCheck()) {
            s.append("pass");
        } else {
            if ("singleCard".equals(cardValue.getType())) {
                s.append("出单牌---");
                s.append(cardValue.getCard().get(0).getDetail());
            } else if ("doubleCard".equals(cardValue.getType())) {
                s.append("出对子---");
                for (int i = 0; i < cardValue.getCard().size(); i++) {
                    if (i != cardLength - 1) {
                        s.append(cardValue.getCard().get(i).getDetail()).append("、");
                    } else {
                        s.append(cardValue.getCard().get(i).getDetail());
                    }
                }
            } else if ("threeCard".equals(cardValue.getType())) {
                s.append("出三不带---");
                for (int i = 0; i < cardValue.getCard().size(); i++) {
                    if (i != cardLength - 1) {
                        s.append(cardValue.getCard().get(i).getDetail()).append("、");
                    } else {
                        s.append(cardValue.getCard().get(i).getDetail());
                    }
                }
            } else if ("threeTakeOne".equals(cardValue.getType())) {
                if (cardValue.getNum() == 4) {
                    s.append("出三带一---");
                    for (int i = 0; i < cardValue.getCard().size(); i++) {
                        if (i != cardLength - 1) {
                            s.append(cardValue.getCard().get(i).getDetail()).append("、");
                        } else {
                            s.append(cardValue.getCard().get(i).getDetail());
                        }
                    }
                } else if (cardValue.getNum() == 5) {
                    s.append("出三带一对---");

                    for (int i = 0; i < cardValue.getCard().size(); i++) {
                        if (i != cardLength - 1) {
                            s.append(cardValue.getCard().get(i).getDetail()).append("、");
                        } else {
                            s.append(cardValue.getCard().get(i).getDetail());
                        }
                    }
                }
                s.append("\n带上");
                for (int i = 0; i < cardValue.getTakeCard().size(); i++) {
                    if (i != cardValue.getTakeCard().size() - 1) {
                        s.append(cardValue.getTakeCard().get(i).getDetail()).append("、");
                    } else {
                        s.append(cardValue.getTakeCard().get(i).getDetail());
                    }
                }
            } else if ("zhaDan".equals(cardValue.getType())) {
                if (cardValue.getNum() == 2) {
                    s.append("出王炸---");
                } else if (cardValue.getNum() == 4) {
                    s.append("出炸弹---");
                }
                for (int i = 0; i < cardValue.getCard().size(); i++) {
                    if (i != cardLength - 1) {
                        s.append(cardValue.getCard().get(i).getDetail()).append("、");
                    } else {
                        s.append(cardValue.getCard().get(i).getDetail());
                    }
                }
            } else if ("sunZi".equals(cardValue.getType())) {
                s.append("出顺子---");
                for (int i = 0; i < cardValue.getCard().size(); i++) {
                    if (i != cardLength - 1) {
                        s.append(cardValue.getCard().get(i).getDetail()).append("、");
                    } else {
                        s.append(cardValue.getCard().get(i).getDetail());
                    }
                }
            } else if ("lianDui".equals(cardValue.getType())) {
                s.append("出连对---");
                for (int i = 0; i < cardValue.getCard().size(); i++) {
                    if (i != cardLength - 1) {
                        s.append(cardValue.getCard().get(i).getDetail()).append("、");
                    } else {
                        s.append(cardValue.getCard().get(i).getDetail());
                    }
                }
            }


        }
        System.out.println(s);
    }

    public static List<Integer> abtainMayList(Map<Integer, Integer> integerIntegerMap, int normalSize) {
        List<Integer> mayList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> integerIntegerEntry : integerIntegerMap.entrySet()) {
            if (integerIntegerEntry.getValue() >= 3) {
                mayList.add(integerIntegerEntry.getKey());
            }
        }
        mayList.sort(Comparator.comparingInt(Integer::valueOf));
        Map<Integer, Integer> tempMap = new HashMap<>();
        mayList.forEach(T -> tempMap.put(T, T));
        int k;
        int flag = 0;
        List<Integer> retList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> integerIntegerEntry : tempMap.entrySet()) {
            k = integerIntegerEntry.getValue();
            int count = 1;
            if (flag == 1) {
                break;
            }
            retList.add(k);
            for (int i = 1; i < 13; i++) {
                flag = 0;
                if (tempMap.containsKey(k + i)) {
                    retList.add(k + i);
                    flag = 1;
                    count++;
                }

                if (count == normalSize) {
                    flag = 1;
                    break;
                }

                if (flag == 0) {
                    retList.clear();
                    break;
                }

            }

        }

        if (retList.size() != normalSize) {
            return new ArrayList<>();
        }
        retList.sort(Comparator.comparingInt(Integer::valueOf));
        return retList;
    }

    public static List<List<Card>> getCardAndTakeCard(List<Card> cardList, List<Integer> retList) {
        List<List<Card>> allList = new ArrayList<>();
        List<Card> cardListRet = new ArrayList<>();
        List<Card> takeListRet = new ArrayList<>();
        for (Card card : cardList) {
            int number = 0;
            for (Integer integer : retList) {
                if (card.getValue() == integer) {
                    cardListRet.add(card);
                    number = 1;
                }
            }
            if (number == 0) {
                takeListRet.add(card);
            }
        }
        Map<Integer, List<Card>> collect = cardListRet.stream().collect(Collectors.groupingBy(Card::getValue));
        for (Map.Entry<Integer, List<Card>> integerListEntry : collect.entrySet()) {        //解决三带一存在出四个的情况，全放在card列表
            if (integerListEntry.getValue().size() == 4) {
                int id = integerListEntry.getValue().get(integerListEntry.getValue().size() - 1).getId();
                cardListRet = cardListRet.stream().filter(T -> T.getId() != id).collect(Collectors.toList());
                takeListRet.add(cardList.stream().filter(T -> T.getId() == id).collect(Collectors.toList()).get(0));
            }

        }
        takeListRet.sort(Comparator.comparing(Card::getId));
        allList.add(cardListRet);
        allList.add(takeListRet);
        return allList;

    }

    public static List<Card> jointCardAndTake(CardValue cardValue) {
        List<Card> card = cardValue.getCard();
        if (cardValue.getTakeCard() != null) {
            card.addAll(cardValue.getTakeCard());
        }

        card.sort((T1, T2) -> T2.getId() - T1.getId());
        return card;
    }

    public static List<List<Card>> jointCardAndTakeTwoRows(CardValue cardValue) {
        List<Card> card = cardValue.getCard();
        if (cardValue.getTakeCard() != null) {
            card.addAll(cardValue.getTakeCard());
        }

        card.sort((T1, T2) -> T2.getId() - T1.getId());
        return Lists.partition(card, 10);
    }


    public static CardValue autoRecSingle(List<List<Card>> allList, SpecialList specialList,SpecialList otherSpecialList, CardValue cardValue,Map<Integer, CardValue> userLastNotReceiveMap, int id, int lordId,WarningLevel warningLevel,boolean isFriendSend,boolean friendWillGetRounds,CardMap cardMap) {
        CardValue cardType;
        List<Card> retList = new ArrayList<>();
        List<Card> myCard = specialList.getMyCard();
        if (myCard.size() == 1 && myCard.get(0).getValue() > cardValue.getValue()){
            return CardValue.singleCard(myCard,id);
        }
       int friendId = PukeRole.getMyFriendId(id,lordId);
        if (isFriendSend){
            List<Card> friendList = friendId == -1? new ArrayList<>():allList.get(friendId);
            cardType = unDangerSingle(specialList,otherSpecialList,specialList.getNormalAndThird(), cardValue,lordId, id,isFriendSend,warningLevel,friendWillGetRounds,cardMap,friendList);
            return cardType;
        }

        if (WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel)){
                cardType = dangerPlay(allList, specialList, specialList.getMyCard(), cardValue, id, lordId, 1,cardMap);
                if (cardType.getCheck()) {
                    return cardType;
                }

                if (specialList.getZhaDanList().size() > 0) {
                    return sendZhaDan(specialList, id);
                }
                cardType = dangerPlay(allList, specialList, specialList.getMyCard(), cardValue, id, lordId, 2,cardMap);
                if (cardType.getCheck()) {
                    return cardType;
                }

                cardType = playBySunZi( specialList, cardValue,true,id);
                if (cardType.getCheck()) {
                    return cardType;
                }

        }

        if (WarningLevel.levelHigher(WarningLevel.DOUBLE_WARNING,warningLevel)){
            cardType =  PukeRole.sendSingleFromValue(specialList,otherSpecialList,cardValue,retList,id,14);
            if (cardType.getCheck()){
                return cardType;
            }
        }

        if (WarningLevel.levelHigher(WarningLevel.FIVE_WARNING,warningLevel)){
          cardType =  PukeRole.sendSingleFromValue(specialList,otherSpecialList,cardValue,retList,id,12);
          if (cardType.getCheck()){
              return cardType;
          }
        }

            boolean nextOpp = (id+1) % 3 == lordId || id == lordId;
            List<Card> tempList = specialList.getNormalAndThird();
            int minSingle = PukeRole.getMaxCouldNotReceiveFromRoom(userLastNotReceiveMap,cardValue.getType()).getValue();
        if (friendWillGetRounds && cardMap.compareRankOfMax(cardValue) < cardMap.halfMapSize() * 3 /2){
            return CardValue.errorWay();
        }
            for (Card value : tempList) {
                if (value.getValue() <= cardValue.getValue()) {
                    continue;
                }
                if (id != lordId && nextOpp && cardMap.compareRankOfMax(value) > cardMap.thirdMapSize() && value.getValue() > 11){
                    continue;
                }
                int temp = cardMap.compareRank(value,cardValue);
                boolean flag1 = temp > cardMap.getSize() / 2;
                boolean flag2 = !WarningLevel.levelHigher(WarningLevel.NO_WARNING,warningLevel);
                if (flag1 && flag2 && value.getValue() > 12) {
                    break;
                }


                if (!nextOpp && !WarningLevel.levelHigher(WarningLevel.SIMPLE_WARNING,warningLevel)  && value.getValue() > 12 && couldBeReceiveBySmall(otherSpecialList,cardValue,value,1) &&  minSingle > cardValue.getValue() && myCard.size() > 6){
                    continue;
                }

                if (value.getValue() == 16 && cardMap.compareRank(value,cardValue) > 2 && !WarningLevel.levelHigher(WarningLevel.FIVE_WARNING,warningLevel)) {
                    continue;
                }
                if (value.getValue() == 17 && cardMap.compareRank(value,cardValue) > 3 && !WarningLevel.levelHigher(WarningLevel.FIVE_WARNING,warningLevel)) {
                    continue;
                }

                    return new CardValue(true, value.getValue(), CardType.SINGLE_CARD, 1, searchCardFromList(tempList, value.getValue(), 1), null, id);
                }

            if (nextOpp){
                for (Card value : tempList) {
                    if (value.getValue() <= cardValue.getValue()) {
                        continue;
                    }

                    if (value.getValue() == 16 && cardMap.compareRank(value,cardValue) > 1 && !WarningLevel.levelHigher(WarningLevel.NO_WARNING,warningLevel)) {
                        continue;
                    }
                    if (value.getValue() == 17 && otherSpecialList.getCardCount()[16] != 0 && !WarningLevel.levelHigher(WarningLevel.NO_WARNING,warningLevel)) {
                        continue;
                    }

                    return new CardValue(true, value.getValue(), CardType.SINGLE_CARD, 1, searchCardFromList(tempList, value.getValue(), 1), null, id);
                }
            }
            cardType =  playBySunZi(specialList,cardValue,false,id);
            if (cardType.getCheck()){
                return cardType;
            }
        return CardValue.errorWay();
    }

    /**
     * 检测到对手的牌量并不多的情况
     */
    public static boolean checkDanger(List<List<Card>> allList, int lordId, int id) {
        return (allList.get(lordId).size() <= 2 && id != lordId) || ((allList.get((lordId + 1) % 3).size() <= 2 && id == lordId) || (allList.get((lordId + 2) % 3).size() <= 2) && id == lordId);
    }

    public static boolean checkHasDanger(List<List<Card>> allList, int lordId, int id) {
        return (allList.get(lordId).size() <= 10 && id != lordId) || ((allList.get((lordId + 1) % 3).size() <= 10) || (allList.get((lordId + 2) % 3).size() <= 10) && id == lordId);
    }

    public static CardValue sendZhaDan(SpecialList specialList, int id) {
        return new CardValue(true, specialList.getZhaDanList().get(0).get(0).getValue(), CardType.BOMB, specialList.getZhaDanList().get(0).size(), specialList.getZhaDanList().get(0), null, id);
    }

    public static CardValue playBySunZi(SpecialList specialList,CardValue lastSend,boolean isDanger,int id){
        Card tempCard = null;
        for (List<Card> list : specialList.getUsefulSunZiList()) {
            if (!isDanger && list.size() <= 5){
                break;
            }

            if (list.get(list.size()-1).getValue() > lastSend.getValue()){
                if (tempCard == null || list.get(list.size()-1).getValue() > tempCard.getValue()){
                    tempCard = list.get(list.size()-1);
                }

            }

        }

        if (tempCard == null){
            return CardValue.errorWay();
        }else {
            List<Card> retList = new ArrayList<>();
            retList.add(tempCard);
            return  CardValue.singleCard(retList,id);
        }
    }

    public static CardValue dangerPlay(List<List<Card>> allList,SpecialList specialList,List<Card> cardList, CardValue cardValue, int id, int lordId, int createTime,CardMap cardMap) {
         int maxValue = 0;
         List<Card> firstList = allList.get((id+1)%3);
         List<Card> secondList = allList.get((id+2)%3);
         maxValue = Math.max(firstList.get(firstList.size()-1).getValue(),secondList.get(secondList.size()-1).getValue());
         boolean noLittleSingle = hasLittleSingle(specialList,maxValue);
         int index = -1;
        for (int i = cardList.size() - 1; i >= 0; i--) {
            if (noLittleSingle) {
                if (cardList.get(cardList.size()-1).getValue() > cardValue.getValue()){
                    return new CardValue(true, cardList.get(cardList.size()-1).getValue(), CardType.SINGLE_CARD, 1, searchCardFromList(cardList, cardList.get(cardList.size()-1).getValue(), 1), null, id);
                }
            }
            if ((cardValue.getSendId() != lordId && id != lordId) && (cardValue.getValue() > 14 || cardValue.getValue() >= maxValue)) {
                break;
            }
            if (cardList.get(i).getValue() > cardValue.getValue()) {
                if (index == -1) {
                    index = i;
                }else {
                    if (cardList.get(i).getValue() >= cardList.get(index).getValue()){
                        index = i;
                    }
                }
            }
        }
        if (index == -1) {
            return CardValue.errorWay();
        }
        return new CardValue(true, cardList.get(index).getValue(), CardType.SINGLE_CARD, 1, searchCardFromList(cardList, cardList.get(index).getValue(), 1), null, id);
    }

    public static boolean hasLittleSingle(SpecialList specialList,int maxValue){
      return  specialList.getNormalList().stream().filter(t -> t.getValue() < maxValue).count() - specialList.getAirList().size() > 1;
    }


    public static CardValue unDangerSingle(SpecialList specialList,SpecialList otherSpecialList,List<Card> cardList, CardValue cardValue,int lordId, int id,boolean isMyFriend,WarningLevel warningLevel,boolean friendWillGetRounds,CardMap cardMap,List<Card> friendList) {
        if (specialList.getMyCard().size() == 0 || otherSpecialList.getMyCard().size() == 0){
            return CardValue.errorWay();
        }
        int maxValue = ListUtil.getCardTail(otherSpecialList.getMyCard()).getValue();
        if (WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel) && !friendWillGetRounds && (hasLittleSingle(specialList, maxValue) || (cardValue.getValue() < maxValue &&(id +1)%3==lordId))){
            List<Card> ret = new ArrayList<>();
            ret.add(ListUtil.getCardTail(specialList.getMyCard()));
            if (ret.get(0).getValue() > cardValue.getValue()){
                return CardValue.singleCard(ret,id);
            }
        }
        if (friendWillGetRounds && (friendList.size() < 6 || cardMap.compareRankOfMax(cardValue) < cardMap.halfMapSize() * 3 / 2) ){
            return CardValue.errorWay();
        }
        if (cardMap.compareRankOfMax(cardValue)  < cardMap.fourMapSize()){
            return CardValue.errorWay();
        }

        if (cardMap.compareRankOfMax(cardValue)  < cardMap.thirdMapSize() && !WarningLevel.levelHigher(WarningLevel.NO_WARNING,warningLevel) ){
            return CardValue.errorWay();
        }

        if (cardMap.compareRankOfMax(cardValue)  < cardMap.thirdMapSize() && ListUtil.getCardTail(specialList.getMyCard()).getValue() > ListUtil.getCardTail(otherSpecialList.getMyCard()).getValue() ){
            return CardValue.errorWay();
        }

//        if(ListUtil.getCardTail(otherSpecialList.getMyCard()).getValue() <= cardValue.getValue() && cardMap.compareRankOfMax(cardValue)  < cardMap.thirdMapSize()){
//            return CardValue.errorWay();
//        }

        boolean nextOpp = (id+1) % 3 == lordId;

        for (int i = 0; i < cardList.size(); i++) {
            if (nextOpp && (cardMap.compareRankOfMax(cardList.get(i)) > cardMap.thirdMapSize() || cardList.get(i).getValue() < 11)){
                continue;
            }

            if (cardMap.compareRankOfMax(cardList.get(i)) < 6 && cardMap.compareRankOfMax(cardList.get(i)) >= 2 && specialList.getMyCard().size() > 5 && !WarningLevel.levelHigher(WarningLevel.SINGLE_WARNING,warningLevel)){
                continue;
            }

            if (cardMap.compareRankOfMax(cardList.get(i)) < 2  && !WarningLevel.levelHigher(WarningLevel.SINGLE_WARNING,warningLevel)){
                continue;
            }

            if (cardList.get(i).getValue() > cardValue.getValue()) {
                if (WarningLevel.levelHigher(WarningLevel.FIVE_WARNING,warningLevel) && cardMap.compareRankOfMax(cardValue) < cardMap.fourMapSize()){
                    return CardValue.errorWay();
                }
                    List<Card> ret = new ArrayList<>();
                    ret.add(cardList.get(i));
                  return CardValue.singleCard(ret,id);
            }
        }

        CardValue cardValueTemp = playBySunZi(specialList, cardValue, false, id);
        if (cardValueTemp.getCheck()){
            return cardValueTemp;
        }

        for (int i = 0; i < cardList.size(); i++) {
            if ((!nextOpp || isMyFriend) && cardMap.compareRankOfMax(cardList.get(i)) < 6 && specialList.getMyCard().size() >5 && !WarningLevel.levelHigher(WarningLevel.FIVE_WARNING,warningLevel)){
                break;
            }
            if (cardList.get(i).getValue() > cardValue.getValue()) {
                if ((!nextOpp || isMyFriend) && !WarningLevel.levelHigher(WarningLevel.FIVE_WARNING,warningLevel) && cardMap.compareRankOfMax(cardValue) < cardMap.thirdMapSize()){
                    return CardValue.errorWay();
                }

                if ((!nextOpp || isMyFriend) && WarningLevel.levelHigher(WarningLevel.FIVE_WARNING,warningLevel) && cardMap.compareRankOfMax(cardValue) < cardMap.fourMapSize()){
                    return CardValue.errorWay();
                }
                int differValue = cardList.get(i).getValue() - cardValue.getValue();
                if ( differValue > 0){
                    List<Card> ret = new ArrayList<>();
                    ret.add(cardList.get(i));
                    return CardValue.singleCard(ret,id);
                }
            }
        }


        return CardValue.errorWay();
    }

    public static CardValue unDangerSingleList(List<List<Card>> allList,List<List<Card>> cardList, CardValue cardValue,int lordId, int id) {
        for (int i = 0; i < cardList.size(); i++) {
            if (cardList.get(i).get(0).getValue() > cardValue.getValue()) {
                int differValue = cardList.get(i).get(0).getValue() - cardValue.getValue();
                if (checkHasDanger(allList,lordId,id) || differValue < 5 || ((id+1) % 3 == lordId && differValue < 8)) {
                    return new CardValue(true, cardList.get(i).get(0).getValue(), CardType.SINGLE_CARD, 1, searchCardFromList(cardList.get(i), cardList.get(i).get(0).getValue(), 1), null, id);
                }else {
                    break;
                }
                }
        }
        return CardValue.errorWay();
    }



    public static List<Card> searchCardFromList(List<Card> myCard, int value, int num) {
        int count = 0;
        List<Card> retList = new ArrayList<>();
        for (Card card : myCard) {
            if (card.getValue() == value) {
                retList.add(card);
                count++;
            }
            if (count >= num) {
                break;
            }

        }
        return retList;
    }

    /**
     * 计算value值判断是不是应该抢地主
     */
    public static int getLordChoose(List<Card> myCard, Lord lord) {
        SpecialList specialList = PukeUtil.packageSpecialCard(myCard);
        List<List<Integer>> indexList = putListToIndexList(myCard);
        int value = 0;
        for (Card card : myCard) {
            value = value + card.getValue();
            if (card.getValue() > 14){
                value += 5;
            }
        }
        value = value + indexList.get(1).size() ;
        value = value + specialList.getAirList().size();
        value = value + specialList.getZhaDanList().size() * 4;
        for (int i : lord.getChoose()) {
            value -= i == 1 ? 5:0;
        }

        for (Card cardTemp : specialList.getNormalList()) {
            if (cardTemp.getValue() < 13){
                value -= 2;
            }
        }
        if (value > 180) {
            return 1;
        } else {
            return 0;
        }
//        return 1;
    }

    public static void fillArray(int[] temp, Lord lord) {
        if (temp[0] == 0) {
            temp[3] = 0;
        } else {
            temp[3] = lord.getRoleChoose();
        }
        if (temp[1] == 0) {
            temp[4] = 0;
        } else {
            temp[4] = PuKe.getLordChoose(lord.getCardRole().get(1), lord);
        }
        if (temp[2] == 0) {
            temp[5] = 0;
        } else {
            temp[5] = PuKe.getLordChoose(lord.getCardRole().get(2), lord);
        }

    }

    /**
     * 返回-2代表流局
     * 返回-1代表还未确立地主
     */
    public static int resultLord(int[] temp, int firstLord) {
        for (int i = 0; i < 3; i++) {
            if (temp[i] == -1){
                return -1;
            }
        }

        if (temp[0] == 1 && temp[1] == 0 && temp[2] == 0){
            return 0;
        }
        if (temp[0] == 0 && temp[1] == 1 && temp[2] == 0){
            return 1;
        }
        if (temp[0] == 0 && temp[1] == 0 && temp[2] == 1){
            return 2;
        }

        if (temp[0] == 1 && temp[3] == 1) {
            return 0;
        }
        if (temp[0] == 1 && temp[3] == 0) {
            if (temp[2] == 1) {
                return 2;
            } else {
                return 1;
            }
        }
        if (temp[0] == 0) {
            if (temp[1] == 1 && temp[4] == 1) {
                return 1;
            }
            if(temp[1] == 1 && temp[4] == 0){
                return 2;
            }
        }

        if (temp[0] == 0 && temp[1] == 0 && temp[2] == 0) {
            return -2;
        }
        return -1;
    }

    /**
     * 新的智能出牌方法
     */
    public static CardValue  autoSendCardTel(SpecialList specialList, List<List<Card>> allList, CardValue lastCardValue, int lordId, int id,String roomId) {
        List<Card> otherList = new ArrayList<>();
        otherList.addAll(allList.get((id+1)%3));
        otherList.addAll(allList.get((id+2)%3));
        SpecialList otherSpecialList = PukeUtil.packageSpecialCard(otherList);
        WarningLevel warningLevel;
        if (id == lordId) {
            warningLevel =  WarningLevel.getWarningLevel(allList.get((id+1)%3),allList.get((id+2)%3));
        }else {
            warningLevel =  WarningLevel.getWarningLevel(allList.get(lordId));
        }

        CardValue dfOneTimeSend = null;
        CardValue cardValue = CardValue.errorWay();
        if (id != lordId) {
            cardValue = PukeRole.couldHelpFriend(specialList, otherSpecialList, allList, id, lordId,roomId,warningLevel);
        }
        OppInfo oppInfo = PukeRole.getOppInfo(allList,lordId,id);
        //如果可以帮到队友就在递归自己是否可以出完时检测炸弹
        dfOneTimeSend = PukeRole.dfSendCard(specialList,otherSpecialList,null,warningLevel,id,!cardValue.getCheck(),false,System.currentTimeMillis(),oppInfo);
        if (dfOneTimeSend.getCheck()){
            return dfOneTimeSend;
        }

        if (cardValue.getCheck()){
            return cardValue;
        }

        List<List<Card>> cardListMy = new ArrayList<>();
        cardListMy.add(allList.get(id));
        CardMap cardMapMy = PukeRole.getCardRankMap(cardListMy);
        if (id != lordId){
           CardValue cardValueTemp = PukeRole.sendByRoomRecord(specialList,otherSpecialList,cardMapMy,warningLevel,id,lordId,roomId);
           if (cardValueTemp.getCheck()){
               return cardValueTemp;
           }

        }

        CardMap cardMap = PukeRole.getCardRankMap(allList);
        cardValue = PukeRole.autoSendRole(specialList, otherSpecialList,allList, id,lordId, warningLevel,cardMap);

        if (cardValue.getCheck()){
            return cardValue;
        }
        List<Card> ret = new ArrayList<>();
        ret.add(specialList.getMyCard().get(0));
        return new CardValue(true, specialList.getMyCard().get(0).getValue(), CardType.SINGLE_CARD, 1, ret, null, id);
    }

    public static CardValue couldSendAllAfterReceive(SpecialList specialList,SpecialList otherSpecialList,List<List<Card>> allList,CardValue cardValue,int id,int lordId,WarningLevel warningLevel){
        if (specialList.getZhaDanList().size() > 0){
            List<List<Card>> list = new ArrayList<>(specialList.getZhaDanList());
            specialList.getZhaDanList().remove(specialList.getZhaDanList().size()-1);
            CardValue cardValueOneTime = couldOneTimeSendAll(specialList,otherSpecialList,allList,id,lordId,warningLevel);
            specialList.setZhaDanList(list);
            if (cardValueOneTime.getCheck()){
                return CardValue.zhDan(list.get(list.size()-1),id);
            }
        }

        if (specialList.getKingBombList().size() > 0){
            List<Card> king = new ArrayList<>(specialList.getKingBombList());
            specialList.setKingBombList(new ArrayList<>());
            specialList.getMyCard().remove(specialList.getMyCard().size()-1);
            specialList.getMyCard().remove(specialList.getMyCard().size()-1);
            specialList.getNormalList().remove(specialList.getNormalList().size()-1);
            specialList.getNormalList().remove(specialList.getNormalList().size()-1);
            CardValue cardValueOneTime = couldOneTimeSendAll(specialList,otherSpecialList,allList,id,lordId,warningLevel);
            specialList.getMyCard().add(PuKe.card[52]);
            specialList.getMyCard().add(PuKe.card[53]);
            specialList.getNormalList().add(PuKe.card[52]);
            specialList.getNormalList().add(PuKe.card[53]);
            specialList.setKingBombList(king);
            if (cardValueOneTime.getCheck()){
                return CardValue.kingBomb(king,id);
            }
        }



        return CardValue.errorWay();


    }

    public static List<Card> getTakeList(SpecialList specialList) {
        List<Card> takeList = new ArrayList<>();
        for (Card card : specialList.getNormalList()) {
            if (card.getValue() < 11 && (specialList.getDoubleList().size() == 0 || specialList.getDoubleList().get(0).get(0).getValue() > card.getValue())) {
                takeList.add(card);
                break;
            }

        }
        return takeList;
    }

    public static List<Card> getTakeDoubleList(SpecialList specialList) {
        List<Card> takeList = new ArrayList<>();
        for (List<Card> cardList : specialList.getDoubleList()) {
            if (cardList.get(0).getValue() < 11 || (specialList.getMyCard().size() < 7 && cardList.get(0).getValue() < 14)) {
                takeList.addAll(cardList);
                break;
            }
        }
        return takeList;
    }

    public  static CardValue couldOneTimeSendAll(SpecialList specialList,SpecialList otherSpecialList,List<List<Card>> allList,int id,int lordId,WarningLevel warningLevel){
        List<Card> retList = new ArrayList<>();
        CardValue cardValue = getOneTimeSend(specialList,id);
        if (cardValue.getCheck()){
            return cardValue;
        }
        if (id != lordId && (id + 1) % 3 != lordId && allList.get((id + 1) % 3).size() == 1){
            if(otherSpecialList.getMyCard() != null && otherSpecialList.getMyCard().size() > 0 && specialList.getMyCard().get(0).getValue() < otherSpecialList.getMyCard().get(otherSpecialList.getMyCard().size()-1).getValue()){
                retList.add(specialList.getMyCard().get(0));
                return CardValue.singleCard(retList,id);
            }
        }
         cardValue = getSpecialCardCostTimes(specialList, otherSpecialList, id, warningLevel);
        if (cardValue.getCheck()){
            return cardValue;
        }
        return CardValue.errorWay();
    }

    public  static CardValue getOneTimeSend(SpecialList specialList,int id){
        int size = specialList.getMyCard().size();
        List<Card> ret = new ArrayList<>();
        if (size == 1){
            return CardValue.singleCard(specialList.getMyCard(),id);
        }

        if (specialList.getKingBombList().size() != 0 && (size == 2 || size == 3)){
            return CardValue.kingBomb(specialList.getKingBombList(),id);
        }

        if (specialList.getZhaDanList().size() != 0){
            if (size == 6){
                ret.addAll(specialList.getNormalList());
                for (List<Card> list : specialList.getDoubleList()) {
                    ret.addAll(list);
                }
                return CardValue.fourTakeTwo(specialList.getZhaDanList().get(0),ret,id);
            }

            if (size == 7){
                if (specialList.getDoubleList().size() == 1){
                    return CardValue.fourTakeTwo(specialList.getZhaDanList().get(0),specialList.getDoubleList().get(0),id);
                }
                if (specialList.getAirList().size() == 1){
                    ret.add(specialList.getAirList().get(0).get(0));
                    ret.add(specialList.getAirList().get(0).get(1));
                    return CardValue.fourTakeTwo(specialList.getZhaDanList().get(0),specialList.getDoubleList().get(0),id);
                }

                ret.add(specialList.getNormalList().get(0));
                ret.add(specialList.getNormalList().get(1));
                return CardValue.fourTakeTwo(specialList.getZhaDanList().get(0),ret,id);
            }

            if (size == 8 && specialList.getDoubleList().size() == 2){
                ret.addAll(specialList.getDoubleList().get(0));
                ret.addAll(specialList.getDoubleList().get(1));
                return CardValue.fourTakeTwo(specialList.getZhaDanList().get(0),ret,id);
            }

        }


        if (specialList.getAirList().size() == 1){
            if (size == 3 ){
                return CardValue.threeTakeOne(specialList.getAirList().get(0),new ArrayList<>(),id);
            }
            if (size == 4 && specialList.getNormalList().size() != 0){
                ret.add(specialList.getNormalList().get(0));
                return CardValue.threeTakeOne(specialList.getAirList().get(0),ret,id);
            }
            if (size == 5 && specialList.getDoubleList().size()==1){
                return CardValue.threeTakeOne(specialList.getAirList().get(0),specialList.getDoubleList().get(0),id);
            }
        }


        if (getListSize(specialList.getUsefulSunZiList()) == size){
            return CardValue.sunZi(specialList.getUsefulSunZiList().get(0),id);
        }

         return CardValue.errorWay();
    }

    public static int getListSize(List<List<Card>> list){
        int count = 0;
        for (List<Card> cardList : list) {
            count += cardList.size();
        }
        return count;
    }

    public static CardValue getSpecialCardCostTimes(SpecialList specialList,SpecialList otherSpecialList,int id,WarningLevel warningLevel){
        int count = 0;
        for (List<Card> list : specialList.getUsefulSunZiList()) {
            if (PukeRole.myCardCouldReceive(CardValue.sunZi(list,id),otherSpecialList,true)){
                count++;
            }
        }

        int doubleWillBeReceived = 0;
        for (List<Card> list : specialList.getDoubleList()) {
           if (myCardCouldReceive(CardValue.doubleCard(list,id),otherSpecialList,true)){
               count++;
               doubleWillBeReceived++;
           }
        }

        int index = 0;
        for (List<Card> list : specialList.getLianDuiList()) {
            if (PukeRole.myCardCouldReceive(CardValue.lianDui(list,id),otherSpecialList,true)){
                index++;
                count++;
            }
        }

        int maxValue = otherSpecialList.getMyCard().get(otherSpecialList.getMyCard().size()-1).getValue();
        int singleMaxIndex = (int)specialList.getNormalList().stream().filter(t->t.getValue() > maxValue).count();
        int equalMax = (int)specialList.getNormalList().stream().filter(t->t.getValue() == maxValue).count();
        int wouldReceiveIndex = (int)specialList.getNormalList().stream().filter(t->t.getValue() < maxValue).count();
        int singleWillBeReceived = specialList.getNormalList().size() - 2 * singleMaxIndex - equalMax;

        count += Math.max(singleWillBeReceived, 0);
        for (List<Card> list : specialList.getAirList()) {
             index--;
            if (PukeRole.myCardCouldReceive(CardValue.threeTakeOne(list,new ArrayList<>(),id),otherSpecialList,true)){
                count++;
            }
        }

        for (int i = 0; i < specialList.getZhaDanList().size(); i++) {
            index -= 2;
        }

        count += Math.max(index,0);
        if (count < 2){
            CardValue cardValue = PukeRole.firstSendFourTakeTwo(specialList, id, warningLevel);
            if (cardValue.getCheck()){
                return cardValue;
            }
            cardValue = PukeRole.firstSendSunZi(specialList,otherSpecialList,id,warningLevel);
            if (cardValue.getCheck()){
                return cardValue;
            }
            PukeRole.firstSendFourTakeTwo(specialList,id,warningLevel);
            if (cardValue.getCheck()){
                return cardValue;
            }
            cardValue =  PukeRole.firstSendThreeTakeOne(specialList,otherSpecialList,id,warningLevel);
            if (cardValue.getCheck()){
                return cardValue;
            }

            List<Card> ret = new ArrayList<>();

//            if (wouldReceiveIndex < 2){
//                if (specialList.getNormalList().size() == 0 && specialList.getDoubleList().size() != 0){
//                    return CardValue.doubleCard(ListUtil.getCardListTail(specialList.getDoubleList()),id);
//                }
//
//                if (specialList.getDoubleList().size() > 0 && !myCardCouldReceive(CardValue.doubleCard(ListUtil.getCardListTail(specialList.getDoubleList()),id),otherSpecialList,false)){
//                    return CardValue.doubleCard(ListUtil.getCardListTail(specialList.getDoubleList()),id);
//                }
//                ret.add(ListUtil.getCardTail(specialList.getMyCard()));
//                return CardValue.singleCard(ret,id);
//            }
            if (doubleWillBeReceived > 0 && specialList.getDoubleList().size() > 0 && specialList.getMyCard().size() == 2){
                return CardValue.doubleCard(specialList.getDoubleList().get(0),id);
            }

            if (doubleWillBeReceived + wouldReceiveIndex < 2){
                if (doubleWillBeReceived < 1 && specialList.getDoubleList().size() > 0){
                    return CardValue.doubleCard(ListUtil.getCardListTail(specialList.getDoubleList()),id);
                }
                if (specialList.getKingBombList().size() != 0 && specialList.getNormalList().size() > 3 ){
                    if (!WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel)){
                        ret.add(specialList.getNormalList().get(0));
                        return CardValue.singleCard(ret,id);
                    }

                }
                    ret.add(ListUtil.getCardTail(specialList.getMyCard()));
                    return CardValue.singleCard(ret,id);
            }


            if (WarningLevel.isSameLevel(WarningLevel.SINGLE_WARNING,warningLevel)){
               cardValue = PukeRole.firstSendDouble(specialList,otherSpecialList,id,warningLevel,true);
                if (cardValue.getCheck()){
                    return cardValue;
                }
                cardValue = sendSingleFirst(specialList,otherSpecialList,id,warningLevel, true);
                if (cardValue.getCheck()){
                    return cardValue;
                }
            }

            if (wouldReceiveIndex + doubleWillBeReceived > 1 && specialList.getNormalList().size() > 0){
                ret.add(specialList.getNormalList().get(0));
                return CardValue.singleCard(ret,id);
            }
            if (doubleWillBeReceived < 0 && specialList.getDoubleList().size() > 0){
                return CardValue.doubleCard(specialList.getDoubleList().get(0),id);
            }


        }
        return CardValue.errorWay();
    }





    public static CardValue sendSingleFirst(SpecialList specialList,SpecialList otherSpecialList,int id,WarningLevel warningLevel,boolean singleWillBeReceived){
        List<Card> ret = new ArrayList<>();
        if (!singleWillBeReceived){
            ret.add(ListUtil.getCardTail(specialList.getMyCard()));
            return CardValue.singleCard(ret,id);
        }

        return PukeRole.firstSendSingle(specialList,otherSpecialList,id,warningLevel);
    }

    public static CardValue couldOneTimeSendAll(SpecialList specialList,SpecialList otherSpecialList,List<List<Card>> allList,int id,int lordId){
        List<Card> ret = new ArrayList<>();
        List<Card> groupList = otherSpecialList.getMyCard();
        int groupSize = groupList.size();
        List<Card> myCard = specialList.getMyCard();
        int size = specialList.getMyCard().size();
        boolean hasNotZhDan = otherSpecialList.getZhaDanList().size() == 0;

        if ((size == 2 || size == 3) && specialList.getKingBombList().size() == 1){
            List<Card> cardList = specialList.getZhaDanList().get(0);
            return new CardValue(true,cardList.get(0).getValue(),CardType.KING_BOMB,2,cardList,null,id);
        }

        if (((size == 3 || size == 4) && specialList.getAirList().size() == 1) || (size == 5 && specialList.getAirList().size() == 1 && specialList.getDoubleList().size() == 1)){
            List<Card> cardList = specialList.getAirList().get(0);
            return new CardValue(true,cardList.get(0).getValue(),CardType.THREE_TAKE_ONE,size,cardList,specialList.getNormalAndDouble(),id);
        }

        if (specialList.getSunZiList().size() == 1 && specialList.getSunZiList().get(0).size() == size){
            List<Card> cardList = specialList.getSunZiList().get(0);
            return new CardValue(true,cardList.get(cardList.size()-1).getValue(),CardType.SUN_ZI,size,cardList,null,id);
        }

        if (specialList.getZhaDanList().size() == 1 && specialList.getZhaDanList().get(0).size() == 4){
            if (size == 4){
                return new CardValue(true,specialList.getZhaDanList().get(0).get(0).getValue(),CardType.BOMB,size,specialList.getZhaDanList().get(0),null,id);
            }
            if (size == 6 || (size == 8 && specialList.getDoubleList().size() == 2)){
                return new CardValue(true,specialList.getZhaDanList().get(0).get(0).getValue(),CardType.FOUR_TAKE_TWO,size,specialList.getZhaDanList().get(0),specialList.getNormalAndDouble(),id);
            }
        }

        if (specialList.getAirPlanList().size() == 1 &&(specialList.getAirPlanList().get(0).size() * 3 == size || specialList.getAirPlanList().get(0).size() * 4 == size)){
            return new CardValue(true,specialList.getAirPlanList().get(specialList.getAirPlanList().size()).get(0).getValue(),
            CardType.AIR_PLAN,size,specialList.getAirPlanList().get(0),specialList.getNormalAndDouble().size()==0?null:specialList.getNormalAndDouble(),id);
        }

        if (specialList.getLianDuiList().size() == 1 && (specialList.getLianDuiList().get(0).size() * 2 == size)) {
            return new CardValue(true, specialList.getLianDuiList().get(specialList.getLianDuiList().size() - 1).get(0).getValue(), CardType.LIAN_DUI, size, specialList.getLianDuiList().get(0), null, id);
        }


//        otherSpecialList.getDoubleList()
        int maxValue = Math.max(otherSpecialList.getMaxValue().get(1),otherSpecialList.getMaxValue().get(2));
        boolean doubleCouldSend = (size == 2 && specialList.getDoubleList().size() == 1)||
                (hasNotZhDan && specialList.getDoubleList().size() > 0 && (specialList.getDoubleList().get(specialList.getDoubleList().size()-1).get(0).getValue()
                        >= maxValue)
                        && ( size == 3 || (size == 4 && specialList.getDoubleList().size() > 1)));
        if (doubleCouldSend){
            List<Card> cardList = specialList.getDoubleList().get(specialList.getDoubleList().size()-1);
            return new CardValue(true,specialList.getDoubleList().get(specialList.getDoubleList().size()-1).get(0).getValue(),CardType.DOUBLE_CARD,2,cardList,null,id);
        }

        if (myCard.size() == 1 || (hasNotZhDan && myCard.get(1).getValue() >= groupList.get(groupSize-1).getValue() && otherSpecialList.getZhaDanList().size()==0)){
            Card card = myCard.get(size-1);
            ret.add(card);
            return new CardValue(true,card.getValue(),CardType.SINGLE_CARD,1,ret,null,id);
        };

         CardValue cardValue = hasChangeSendAndReceive(specialList,otherSpecialList,allList,ret,id,lordId);
         if (cardValue.getCheck()){
             return cardValue;
         }
        return CardValue.errorWay();
    }

//    public static CardValue mostUsefulSend(SpecialList specialList,List<Card> allList,int lordId,int id){
//        boolean isLord = lordId == id;
//        List<Card> sunList = specialList.getSunZiList().get(0);
//        int maxValue =
//
//
//    }

    public static CardValue hasChangeSendAndReceive(SpecialList specialList,SpecialList otherSpecialList,List<List<Card>> allList,List<Card> ret,int id,int lordId){
        List<Card> list = specialList.getNormalAndDouble();
        int times = needSendTimesMin(specialList);
        if (times <= 2){
            return CardValue.errorWay();
        }
//        boolean justHasDouble = specialList.getDoubleList().size() == specialList.getMyCard().size();
//        if (!justHasDouble){
//            return CardValue.errorWay();
//        }

        if (specialList.getLianDuiList().size() != 0 || specialList.getSunZiList().size() != 0 || specialList.getAirList().size() != 0){
            return CardValue.errorWay();
        }

        if (list.get(list.size()-(times-2)).getValue() >= otherSpecialList.getMyCard().get(otherSpecialList.getMyCard().size()-1).getValue() && id == lordId && allList.get(lordId).size() != 1){
            if (specialList.getNormalList().size() > 0 && specialList.getDoubleList().size() < 2) {
                ret.add(specialList.getNormalList().get(0));
                return new CardValue(true,specialList.getNormalList().get(0).getValue()  , CardType.SINGLE_CARD, 1, ret, null, id);
            }
        }

       return CardValue.errorWay();
    }


    public static int needSendTimesMin(SpecialList specialList){
        int ret = specialList.getMyCard().size();
        ret -= specialList.getDoubleList().size();
        for (List<Card> cardList : specialList.getLianDuiList()) {
            ret -= cardList.size()-1;
        }
        ret -= specialList.getAirList().size()*3;
        for (List<Card> cardList : specialList.getAirPlanList()) {
            ret -= cardList.size();
        }

        for (List<Card> cardList : specialList.getSunZiList()) {
            ret -= cardList.size() -1;
        }

        for (List<Card> cardList : specialList.getZhaDanList()) {
           ret -= cardList.size() - 1;
        }

          ret -= specialList.getKingBombList().size() == 0?0:1;

          return  ret;
    }

    public static CardValue getDoubleByTimes(SpecialList specialList,CardMap cardMap,CardValue previous,WarningLevel warningLevel,List<Card> ret,int times,int id){
        for (List<Card> list : specialList.getCardCountList()) {
            if (list == null || list.size() != times){
                continue;
            }


            if (cardMap.compareRank(list.get(0),previous) > cardMap.thirdMapSize() && list.get(0).getValue() > 10  && !WarningLevel.levelHigher(WarningLevel.HAVE_SOME_WARNING,warningLevel) ){
                break;
            }

            if (cardMap.compareRank(list.get(0),previous) > cardMap.thirdMapSize() && list.get(0).getValue() > 10 && !WarningLevel.levelHigher(WarningLevel.NO_WARNING,warningLevel) && specialList.getMyCard().size() > 6){
                break;
            }

            if (list.get(0).getValue() > previous.getValue()){
                ret.add(list.get(0));
                ret.add(list.get(1));
                return CardValue.doubleCard(ret,id);
            }
        }
        return CardValue.errorWay();
    }

    public static CardValue getDoubleFromDoubleList(SpecialList specialList,SpecialList otherSpecialList,Map<Integer, CardValue> userLastNotReceiveMap,WarningLevel warningLevel,CardMap cardMap,CardValue cardValue,boolean isMyFriend,int id,boolean nextOpp){
        int maxDouble = PukeRole.getMaxByTimes(otherSpecialList,2);
        if (WarningLevel.isSameLevel(WarningLevel.DOUBLE_WARNING,warningLevel)){
            return CardValue.errorWay();
        }

        int minDouble = specialList.getDoubleList().size() == 0 ? 0 : specialList.getDoubleList().get(0).get(0).getValue();
        int minSingle = specialList.getNormalList().size() == 0 ? 0 : specialList.getNormalList().get(0).getValue();
        int minValue = Math.min(minDouble,minSingle);

        if (isMyFriend && minValue < cardValue.getValue()){
                if (cardMap.compareRankOfMax(cardValue) < cardMap.halfMapSize() || maxDouble <= cardValue.getValue()){
                    return CardValue.errorWay();
                }
        }


        for (List<Card> list : specialList.getDoubleList()) {
            if (list.get(0).getValue() <= cardValue.getValue()){
                continue;
            }

            if (!isMyFriend && !nextOpp && !WarningLevel.levelHigher(WarningLevel.SIMPLE_WARNING,warningLevel) && (cardMap.compareRankOfMax(cardValue) < cardMap.thirdMapSize() || list.get(0).getValue() - cardValue.getValue() > 5) && couldBeReceiveBySmall(otherSpecialList,cardValue,list.get(0),2) && PukeRole.getMaxCouldNotReceiveFromRoom(userLastNotReceiveMap,cardValue.getType()).getValue() > cardValue.getValue()){
                break;
            }

            if (isMyFriend && couldBeReceiveBySmall(otherSpecialList, cardValue, list.get(0),2) && !WarningLevel.levelHigher(WarningLevel.NO_WARNING, warningLevel)) {
                break;
            }

            if (isMyFriend && cardMap.compareRankOfMax(list.get(0)) < cardMap.fourMapSize() && !mayOneTimeSend(otherSpecialList, warningLevel)) {
                break;
            }

            return CardValue.doubleCard(list,id);
        }

        return CardValue.errorWay();
    }

    public static boolean couldBeReceiveBySmall(SpecialList otherSpecialList,CardValue cardValue,Card card,int times){
        for (List<Card> cardList : otherSpecialList.getCardCountList()) {
            if (cardList == null || cardList.size() < times || cardList.get(0).getValue() <= cardValue.getValue()){
                continue;
            }

            if (card.getValue() <= cardList.get(0).getValue()){
                break;
            }
            return true;
        }
        return false;
    }

    public static boolean couldBeReceiveBySmallSingle(SpecialList otherSpecialList,CardValue cardValue,Card card){
        for (Card tempCard : otherSpecialList.getMyCard()) {
            if (tempCard.getValue() <= cardValue.getValue() ){
                continue;
            }

            if (card.getValue() <= tempCard.getValue()){
                break;
            }
            return true;
        }
        return false;
    }

    public static PuKeCard autoDistributeCard() {
        PuKeCard puKeCard = new PuKeCard();
        List<Card> firstLord = new ArrayList<>();
        List<Card> firstCard = new ArrayList<>();
        List<Card> secondCard = new ArrayList<>();
        List<Card> thirdCard = new ArrayList<>();
        firstCard.add(card[50]);
        firstCard.add(card[24]);
        secondCard.add(card[1]);
        secondCard.add(card[52]);
        secondCard.add(card[24]);
        secondCard.add(card[25]);
        secondCard.add(card[40]);
        secondCard.add(card[41]);
        secondCard.add(card[16]);
        secondCard.add(card[17]);
        secondCard.add(card[20]);
        secondCard.add(card[21]);
        thirdCard.add(card[0]);
        thirdCard.add(card[44]);


        puKeCard.setFirstLord(2);
        puKeCard.setFirstRole(firstCard);
        puKeCard.setSecondRole(secondCard);
        puKeCard.setThirdRole(thirdCard);
        puKeCard.setLordRole(firstLord);
        return puKeCard;
    }


    public static void main(String[] args) {
        PuKe puKe = new PuKe();
        // puKe.puKeGame(3);
        List<Card> list = new ArrayList<>();
        list.add(card[0]);
        list.add(card[1]);
        list.add(card[2]);
        list.add(card[4]);
        list.add(card[8]);
        list.add(card[12]);
        list.add(card[16]);
        list.add(card[17]);
        list.add(card[18]);
        list.add(card[20]);
        list.add(card[21]);
        list.add(card[22]);
        list.add(card[23]);
        Map<Integer, Integer> integerIntegerMap = PuKe.putListToMap(list);
        CardValue cardValue = attemptCombSunZi(card[0], integerIntegerMap, list, 2);
        System.out.println(cardValue);


    }


}
