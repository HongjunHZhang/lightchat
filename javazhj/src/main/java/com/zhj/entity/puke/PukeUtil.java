package com.zhj.entity.puke;

import com.zhj.entity.puke.dic.CardType;
import com.zhj.entity.puke.dic.WarningLevel;
import com.zhj.pukeyule.Card;
import com.zhj.pukeyule.CardValue;
import com.zhj.pukeyule.PuKe;
import com.zhj.util.ListUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 789
 */
public class PukeUtil {

    public static SpecialList packageSpecialCard(List<Card> myCard){
        if (isNeedSort(myCard)){
            myCard.sort(Comparator.comparingInt(Card::getId));
        }
        List<List<Integer>> lists = PuKe.putListToIndexList(myCard);
        Map<Integer, Integer> map = PuKe.putListToMap(myCard);
        SpecialList specialList = new SpecialList();
        putCardIntoCardCount(specialList,myCard);
        putListToMaxValue(lists,specialList);
        List<Card> list = distinctList(myCard,map);
        List<List<Card>> sunZi = getSunZi(list);
        List<List<Card>> linDui = distinctDoubleList(myCard,lists);
        specialList.setMyCard(myCard);
        specialList.setSunZiList(sunZi);
        getUsefulSunZi(specialList,specialList.getCardCount());
        specialList.setLianDuiList(linDui);
        getDoubleList(myCard,lists,specialList);
        getZhaDan(myCard,map,specialList);
        getAirAndAirPlan(myCard,lists,specialList);
        getAllList(myCard,specialList);
//        getNormalToThird(specialList);
        getPriorityCard(specialList);
        putNormalAfterSunZi(specialList);
        return specialList;
    }

    public static void getUsefulSunZi(SpecialList specialList,int[] cardCount){
        List<Card>[] cardCountList = specialList.getCardCountList();
        specialList.setTempIndex((int) Arrays.stream(specialList.getCardCount()).filter(t->t == 1).count());
        int startIndex = cardCountList[7] == null?7:3;
        int endIndex = cardCountList[10] == null?10:14;
        if (endIndex - startIndex < 4){
            specialList.setUsefulSunZiList(new ArrayList<>());
            return;
        }
        specialList.setUsefulSunZiList(new ArrayList<>());
        dfSunZi(specialList,cardCount,new ArrayList<>());
    }

    public static void dfSunZi(SpecialList specialList,int[] cardCount,List<List<Card>> retList){
        int[] ints = new int[2];
        int count = 0;
        for (int i = 3; i < 15; i++) {
            if (cardCount[i] == 0 || cardCount[i] == 4){
                if (count >= 5){
                    ints[0] = i-count;
                    ints[1] = i-1;
                    break;
                }
                count = 0;
                continue;
            }
            count++;
            if (i == 14 && count >= 5){
                ints[0] = i-count+1;
                ints[1] = i;
            }
        }

        shouldSetSunZi(specialList,retList,cardCount);
        if (ints[1] == 0){
            return;
        }

        int maxLength = ints[1] - ints[0];
        if (maxLength < 4){
            return;
        }
        for (int i = ints[0]; i <= ints[1] - 4; i++) {
            for (int j = 4; j <= maxLength ; j++) {
                int endIndex = i+j;
                if (endIndex > ints[1]){
                    break;
                }
                List<Card> list = new ArrayList<>();
                for (int m = i; m <= endIndex; m++) {
                    cardCount[m]--;
                    list.add(specialList.getCardCountList()[m].get(0));
                }
                retList.add(list);
                dfSunZi(specialList,cardCount,retList);
                for (int m = i; m <= endIndex; m++) {
                    cardCount[m]++;
                }
                retList.remove(retList.size()-1);
            }

        }
    }

    public static void shouldSetSunZi(SpecialList specialList,List<List<Card>> retList,int[] cardCount){
        int count = 0;
        for (int i = 0; i < cardCount.length; i++) {
            count += cardCount[i] == 1?1:0;
            if (cardCount[i] ==  2 && specialList.getCardCountList()[i] != null && specialList.getCardCountList()[i].size() == 3){
                count += 2;
            }
        }
        if (count < specialList.getTempIndex()){
            specialList.setUsefulSunZiList(new ArrayList<>(retList));
            specialList.setTempIndex(count);
        }

        if (count == specialList.getTempIndex() && retList.size() < specialList.getUsefulSunZiList().size()){
            specialList.setUsefulSunZiList(new ArrayList<>(retList));
        }
    }



    public static void getNormalToThird(SpecialList specialList){
        List<Card> list = new ArrayList<>(specialList.getNormalList());
        specialList.getDoubleList().forEach(list::addAll);
        specialList.getAirList().forEach(list::addAll);
        specialList.setNormalAndThird(list);
    }

    public static void putListToMaxValue(List<List<Integer>> list,SpecialList specialList){
        List<Integer> ret = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
           List<Integer> tempList = list.get(i);
           ret.add(tempList.size() == 0?0:tempList.get(tempList.size()-1));
        }
        specialList.setMaxValue(ret);

    }

    public static List<Card> distinctList(List<Card> myCard,Map<Integer, Integer> indexMap){
        Map<Integer,Card> map = new HashMap<>();
        for (Card card : myCard) {
            if (indexMap.get(card.getValue())!=4){
                map.put(card.getValue(), card);
            }

        }
        List<Card> cardList = new ArrayList<>(map.values());
        cardList.sort(Comparator.comparingInt(Card::getId));
        return cardList;
    }
    public static List<List<Card>> distinctDoubleList(List<Card> myCard,List<List<Integer>> lists){
        List<Integer> doubleList = lists.get(1);
        doubleList.sort(Comparator.comparingInt(Integer::valueOf));
        List<Integer> tempList = new ArrayList<>();
        List<List<Integer>> specialList = new ArrayList<>();
        List<List<Card>> retList = new ArrayList<>();
        boolean flag = false;
        for (int i = 0; i < doubleList.size()-1; i++) {
            if (tempList.size()==0){
                tempList.add(doubleList.get(i));
            }
            if (i==doubleList.size()-2){
                flag = true;
            }
            if (doubleList.get(i+1)<15&&doubleList.get(i+1)-doubleList.get(i)==1){
                tempList.add(doubleList.get(i+1));
            }else {
                flag = true;
            }

            if (flag){
                if (tempList.size()>2){
                    List<Integer> ret = new ArrayList<>(tempList);
                    specialList.add(ret);
                }
                tempList.clear();
                flag = false;
            }
        }

        for (List<Integer> list : specialList) {
            retList.add(PuKe.findValueByTime(myCard,list,2));
        }

        return retList;
    }

    public static List<List<Card>> getSunZi(List<Card> list){
        boolean flag = false;
        List<List<Card>> specialList = new ArrayList<>();
        List<Card> tempList = new ArrayList<>();
        for (int i = 0; i < list.size()-1; i++) {
            if (tempList.size()==0){
                tempList.add(list.get(i));
            }
            if (i == list.size()-2){
                flag = true;
            }
            if (list.get(i+1).getValue()<15&&list.get(i+1).getValue()-list.get(i).getValue()==1){
                tempList.add(list.get(i+1));
            }else {
                flag = true;
            }

            if (flag){
                if (tempList.size()>4){
                    List<Card> retList = new ArrayList<>(tempList);
                    specialList.add(retList);
                }
                tempList.clear();
                flag = false;
            }

        }
        return specialList;

    }

    public static void getZhaDan(List<Card> myCard,Map<Integer, Integer> map,SpecialList specialList){
        List<Integer> list = new ArrayList<>();
        for (Map.Entry<Integer, Integer> integerIntegerEntry : map.entrySet()) {
            if (integerIntegerEntry.getValue()==4){
                list.add(integerIntegerEntry.getKey());
            }
        }
        list.sort(Comparator.comparing(Integer::valueOf));
        List<List<Card>> retList = new ArrayList<>();
        for (Integer integer : list) {
            retList.add(myCard.stream().filter(T->T.getValue()==integer).collect(Collectors.toList()));
        }
        specialList.setZhaDanList(retList);
        if (map.containsKey(16)&&map.containsKey(17)){
            List<Card> tempList = new ArrayList<>();
            tempList.add(PuKe.card[52]);
            tempList.add(PuKe.card[53]);
            specialList.setKingBombList(tempList);
        }else{
            specialList.setKingBombList(new ArrayList<>());
        }

    }
    public static void getDoubleList(List<Card> myCard,List<List<Integer>> lists,SpecialList specialList){
        List<Integer> list = lists.get(1);
        List<List<Card>> ret = new ArrayList<>();
        for (Integer integer : list) {
        ret.add(PuKe.findValueByTime(myCard,integer,2));
        }
        specialList.setDoubleList(ret);
    }

    public static void getAirAndAirPlan(List<Card> myCard,List<List<Integer>> lists,SpecialList specialList){
        List<Integer> list = lists.get(2);
        boolean flag = false;
        List<Integer> tempList = new ArrayList<>();
        List<List<Integer>> airList = new ArrayList<>();
        for (int i = 0; i < list.size()-1; i++) {
            if (tempList.size()==0){
                tempList.add(list.get(i));
            }
            if (i==list.size()-2){
                flag = true;
            }
            if (list.get(i+1)<15&&list.get(i+1)-list.get(i)==1){
                tempList.add(list.get(i+1));
            }else {
                flag = true;
            }

            if (flag){
                if (tempList.size()>1){
                    List<Integer> ret = new ArrayList<>(tempList);
                    airList.add(ret);
                }
                tempList.clear();
                flag = false;
            }
        }

        List<List<Card>> retAirPlanList = new ArrayList<>();
        for (List<Integer> integers : airList) {
            retAirPlanList.add(PuKe.findValueByTime(myCard,integers,3));
        }
        specialList.setAirPlanList(retAirPlanList);
        List<List<Card>> retAirList = new ArrayList<>();
        for (Integer integer : list) {
            retAirList.add(PuKe.findValueByTime(myCard,integer,3));
        }
        specialList.setAirList(retAirList);
    }

    public static void getAllList(List<Card> myCard,SpecialList specialList){
        Set<Integer> set = new HashSet<>();
        listToSet(specialList.getUsefulSunZiList(),set);
        listToSet(specialList.getLianDuiList(), set);
        listToSet(specialList.getAirList(), set);
        listToSet(specialList.getAirPlanList(), set);
        listToSet(specialList.getZhaDanList(), set);
        listToSet(specialList.getDoubleList(),set);
        specialList.setSize(myCard.size());
        fillAllList(myCard,specialList,set);
        fillNormalDoubleList(specialList);
    }

    public static void listToSet(List<List<Card>> list,Set<Integer> set){
        for (List<Card> cardList : list) {
            for (Card card : cardList) {
                set.add(card.getId());
            }
        }
    }

    public static void fillAllList(List<Card> myCard,SpecialList specialList,Set<Integer> set){
        List<Card> allList = new ArrayList<>();
        List<Card> normalList = new ArrayList<>();
        for (Card card : myCard) {
            if (card.getValue() >= 16 || !set.contains(card.getId())){
                normalList.add(card);
            }else {
                allList.add(card);
            }

        }
        specialList.setAllList(allList);
        specialList.setNormalList(normalList);
    }

    public static void fillNormalDoubleList(SpecialList specialList){
        List<Card> list = new ArrayList<>(specialList.getNormalList());
        for (int i = 0; i < specialList.getDoubleList().size(); i++) {
            list.addAll(specialList.getDoubleList().get(i));
        }
        list.sort(Comparator.comparing(Card::getId));

        specialList.setNormalAndDouble(list);
    }

    public static boolean isNeedSort(List<Card> myCard){
        for (int i = 0; i < myCard.size()-1; i++) {
            if (myCard.get(i+1).getValue()-myCard.get(i).getValue()<0){
                return true;
            }
        }
           return false;
    }

    public static List<List<Card>> combAllList(List<Card> firstRole,List<Card> secondRole,List<Card> thirdRole){
        List<List<Card>> retList = new ArrayList<>();
        retList.add(firstRole);
        retList.add(secondRole);
        retList.add(thirdRole);
        return retList;
    }

    public static int getZhaDanSize(SpecialList specialList){
       int count = 0;
        for (List<Card> cardList : specialList.getZhaDanList()) {
            count += cardList.size();
        }
        return count;
    }

    public static int getSunZiSize(SpecialList specialList){
        int count = 0;
        for (List<Card> cardList : specialList.getSunZiList()) {
            count += cardList.size();
        }
        return count;
    }

    public static boolean shouldSendSingle(SpecialList specialList, SpecialList otherSpecialList, CardMap cardMap, WarningLevel warningLevel,CardMap cardMapMy){
        List<Card> myCard = specialList.getMyCard();
        int singleCount = 0,doubleCount = 0;
        int maxValue = otherSpecialList.getMyCard().get(otherSpecialList.getMyCard().size()-1).getValue();
        if (specialList.getNormalList().size() == 0){
            return false;
        }
        if (specialList.getDoubleList().size() == 0){
            return true;
        }

        if (specialList.getNormalList().get(0).getValue() < 11 && cardMapMy.compareRankOfMax(specialList.getNormalList().get(0)) > cardMapMy.thirdMapSize()){
            return true;
        }

        if (warningLevel.getCode() < 5 && specialList.getMyCard().size() > 0 && otherSpecialList.getMyCard().size() > 0){
            if (ListUtil.getCardTail(specialList.getMyCard()).getValue() > ListUtil.getCardTail(otherSpecialList.getMyCard()).getValue()){
                return true;
            }
        }

        if (warningLevel.getCode() < 5){
            if (specialList.getDoubleList().size() == 0){
                return true;
            }

            if (otherSpecialList.getDoubleList().size() == 0){
                return false;
            }

            if (ListUtil.getCardListTail(specialList.getDoubleList()).get(0).getValue() > ListUtil.getCardListTail(otherSpecialList.getDoubleList()).get(0).getValue()){
                if (specialList.getMyCard().size() - specialList.getDoubleList().size() * 2 < 2){
                    return false;
                }
            }

        }

        for (Card card : myCard) {
            if (card.getValue() >= maxValue){
               singleCount++;
            }
        }

        for (Card card : specialList.getNormalList()) {
            if (cardMap.compareRankOfMax(card) > cardMap.halfMapSize()){
                singleCount ++;
            }
        }

        for (List<Card> cardList : specialList.getDoubleList()) {
              if (otherSpecialList.getDoubleList().size() == 0 || cardList.get(0).getValue() > otherSpecialList.getDoubleList().get(otherSpecialList.getDoubleList().size()-1).get(0).getValue()){
                  doubleCount++;
              }
        }

        for (List<Card> cardList : specialList.getDoubleList()) {
            if (cardMap.compareRankOfMax(cardList.get(0)) > cardMap.halfMapSize()){
                doubleCount += 2;
            }
        }

            if (specialList.getNormalList().get(0).getValue() < specialList.getDoubleList().get(0).get(0).getValue()){
                singleCount++;
            }else {
                doubleCount++;
            }



         return singleCount >= doubleCount;
    }

    public static CardValue doubleSend(SpecialList specialList,int id){
        if (specialList.getDoubleList().size() > 0) {
            int doubleMinValue = specialList.getDoubleList().get(0).get(0).getValue();
            int singleMinValue = specialList.getNormalAndDouble().get(0).getValue();
            boolean justHasDoubleAndZd = specialList.getDoubleList().size() * 2 + PukeUtil.getZhaDanSize(specialList) == specialList.getMyCard().size();
            if (doubleMinValue <= singleMinValue || specialList.getDoubleList().get(0).get(0).getValue() < 12 || justHasDoubleAndZd) {
                return new CardValue(true, specialList.getDoubleList().get(0).get(0).getValue(), CardType.DOUBLE_CARD, 2, specialList.getDoubleList().get(0), null, id);
            }
        }
        return CardValue.errorWay();
    }

    public static CardValue singleSend(SpecialList specialList,List<List<Card>> allList,List<Card> ret,int lordId,int id){
        if ((id+1) % 3 == lordId && allList.get((id+2) % 3).size() < 8){
            for (int i = 0; i < specialList.getNormalList().size(); i++) {
                if (specialList.getNormalList().get(i).getValue() < 8){
                    continue;
                }
                if (specialList.getNormalList().get(i).getValue() > 13){
                    break;
                }
                ret.add(specialList.getNormalList().get(i));
                return new CardValue(true, specialList.getNormalList().get(i).getValue(), CardType.SINGLE_CARD, 1, ret, null, id);
            }

        }
        return CardValue.errorWay();
    }

    public static void getPriorityCard(SpecialList specialList){
        specialList.setPriorityMaxCard(PriorityCard.parseSpecialToPriorityCard(specialList));
    }

    public static void putCardIntoCardCount(SpecialList specialList,List<Card> list){
        int[] cardCount = new int[18];
        List<Card>[] cardCountList = new ArrayList[18];
        for (Card card : list) {
            int index = card.getValue();
            cardCount[index]++;
            if (cardCountList[index] == null){
                cardCountList[index] = new ArrayList<>();
            }
            cardCountList[index].add(card);
        }
        specialList.setCardCount(cardCount);
        specialList.setCardCountList(cardCountList);
    }

    public static void  putNormalAfterSunZi(SpecialList specialList){
        Map<Integer,Integer> map = new HashMap<>();
        for (List<Card> list : specialList.getUsefulSunZiList()) {
           list.forEach(t->map.put(t.getValue(),map.getOrDefault(t.getValue(),0)+1));
        }

        Map<Integer,Integer> reduceMap = new HashMap<>();
        for (List<Card> list : specialList.getDoubleList()) {
            if (map.getOrDefault(list.get(0).getValue(),0) == 1){
                reduceMap.put(list.get(0).getValue(),list.get(0).getValue());
                specialList.getNormalList().add(list.get(0));
            }
        }

        //移除从对子到单牌的牌
        specialList.setDoubleList(specialList.getDoubleList().stream().filter(t->!reduceMap.containsKey(t.get(0).getValue())).collect(Collectors.toList()));
        reduceMap.clear();
        for (List<Card> list : specialList.getAirList()) {
            if (map.getOrDefault(list.get(0).getValue(),0) == 1){
                List<Card> ret = new ArrayList<>();
                ret.add(list.get(0));
                ret.add(list.get(1));
                map.put(list.get(0).getValue(),list.get(0).getValue());
                specialList.getDoubleList().add(ret);
            }

            if (map.getOrDefault(list.get(0).getValue(),0) == 2){
                specialList.getNormalList().add(list.get(0));
            }
        }
        //过滤三连
        specialList.setAirList(specialList.getAirList().stream().filter(t->!map.containsKey(t.get(0).getValue())).collect(Collectors.toList()));
        //过滤飞机
        specialList.setAirPlanList(specialList.getAirPlanList().stream().filter(t->filterAirPlan(t,map)).collect(Collectors.toList()));

        specialList.getNormalList().sort(Comparator.comparing(Card::getId));
        specialList.getDoubleList().sort(Comparator.comparingInt(t -> t.get(0).getValue()));
        specialList.getAirList().sort(Comparator.comparingInt(t -> t.get(0).getValue()));
        List<Card> cardList = new ArrayList<>(specialList.getNormalList());
        specialList.getDoubleList().forEach(cardList::addAll);
        specialList.setNormalAndDouble(new ArrayList<>(cardList));
        specialList.getAirList().forEach(cardList::addAll);
        for (List<Card> cards : specialList.getZhaDanList()) {
            if (cards.get(0).getValue() > 12){
                cardList.addAll(cards);
            }
        }
        specialList.setNormalAndThird(cardList);
    }

    public static boolean filterAirPlan(List<Card> list,Map<Integer,Integer> map){
        for (Card card : list) {
            if (map.containsKey(card.getValue())){
                return false;
            }

        }
        return true;
    }

    public CardValue haveChanceOneTimeSend(SpecialList specialList,List<Card> otherList){
//        otherList.sort(Comparator.comparing(Card::getId));
//        if (specialList.)
        return null;
    }


//    public static void getNormalAndDouble(List<Card> myCard,List<List<Integer>> lists,SpecialList specialList) {
//        List<Card> cardList = new ArrayList<>(specialList.getNormalList());
//        for (List<Card> cards : specialList.getDoubleList()) {
//            cardList.addAll(cards);
//        }
//        specialList.setNormalAndDouble();
//    }
    public static void main(String[] args) {
        List<Card> list = new ArrayList<>();
        list.add(PuKe.card[0]);
        list.add(PuKe.card[1]);
        list.add(PuKe.card[2]);
        list.add(PuKe.card[3]);
        list.add(PuKe.card[4]);
        list.add(PuKe.card[5]);
        list.add(PuKe.card[6]);
        list.add(PuKe.card[8]);
        list.add(PuKe.card[9]);
        list.add(PuKe.card[10]);
        list.add(PuKe.card[12]);
        list.add(PuKe.card[13]);
        list.add(PuKe.card[16]);
        list.add(PuKe.card[17]);
        list.add(PuKe.card[20]);
        list.add(PuKe.card[21]);
       // list.add(PuKe.card[24]);
        list.add(PuKe.card[28]);
        list.add(PuKe.card[32]);
        list.add(PuKe.card[36]);
        list.add(PuKe.card[40]);
        list.add(PuKe.card[44]);
        list.add(PuKe.card[48]);
        list.add(PuKe.card[49]);
        list.add(PuKe.card[52]);
      //  list.add(PuKe.card[53]);
//        list.add(PuKe.card[43]);
//        list.add(PuKe.card[41]);
        packageSpecialCard(list);

    }



}
