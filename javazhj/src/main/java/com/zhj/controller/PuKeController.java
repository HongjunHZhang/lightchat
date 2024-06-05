package com.zhj.controller;

import com.zhj.entity.puke.*;
import com.zhj.entity.result.DataResult;
import com.zhj.exception.CustomException;
import com.zhj.pukeyule.Card;
import com.zhj.pukeyule.CardValue;
import com.zhj.pukeyule.PuKe;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author 789
 */
@RestController
@RequestMapping("/lightChat/PuKeGame")
public class PuKeController {

    @RequestMapping("/PuKeGame")
    public DataResult<PuKeCard> puKeGame() {
          List<List<Card>> lists = PuKe.distributeCard(3);
          PuKeCard puKeCard = new PuKeCard(lists.get(0),lists.get(1),lists.get(2),lists.get(3),(int)(Math.random()*3),PuKeCard.createRoom());
//        List<List<Card>> lists = PuKe.distributeCardSpecial();
//        PuKeCard puKeCard = new PuKeCard(lists.get(0), lists.get(1), lists.get(2), new ArrayList<>(), 0,PuKeCard.createRoom());
          return DataResult.successOfData(puKeCard);
        // return new DataResult(200,"成功",PuKe.autoDistributeCard());

    }

    @PostMapping("/sendCard")
    public DataResult sendCard(@RequestBody SendCard sendCard) {
        CardValue cardType;
        if (sendCard.getCardValue() == null) {
            cardType = PuKe.getCardType(sendCard.getSendCard(), 0);
        } else {
            cardType = PuKe.receiveCard(sendCard.getCardValue(), sendCard.getSendCard(), 0);
        }
        if (cardType == null || !cardType.getCheck()) {
            throw CustomException.createCustomException("出牌不符合规则");
        }
        List<Card> firstList = PuKe.afterAutoRecive(sendCard.getFirstRole(), cardType);
        RetCard retCard = new RetCard();
        retCard.setFirstRole(firstList);
        retCard.setFirstSend(PuKe.jointCardAndTake(cardType));
        PukeRoom.addRecordByRoomId(sendCard.getRoomId(),cardType,0);
        if (firstList.size() == 0) {
            CardValue cardValue = new CardValue();
            cardValue.setCard(sendCard.getSecondRole());
            retCard.setSecondRole(sendCard.getSecondRole());
            retCard.setSecondSend(new ArrayList<>());
            cardValue.setCard(sendCard.getThirdRole());
            cardValue.setSendId(1);
            retCard.setCardValue(cardValue);
            retCard.setThirdRole(sendCard.getThirdRole());
            retCard.setThirdSend(new ArrayList<>());
            retCard.setWinner(0 == sendCard.getLordId() ? 1 : 0);
            return DataResult.successOfData(retCard);
        }
        List<List<Card>> lists = PukeUtil.combAllList(retCard.getFirstRole(), sendCard.getSecondRole(), sendCard.getThirdRole());
        CardValue cardValue = PuKe.autoReceive(cardType, sendCard.getSecondRole(), lists, 1, sendCard.getLordId(),sendCard.getRoomId());
        PukeRoom.addRecordByRoomId(sendCard.getRoomId(),cardValue,1);
        if (cardValue.getCheck()) {
            cardType = cardValue;
            retCard.setSecondRole(PuKe.afterAutoRecive(sendCard.getSecondRole(), cardType));
            retCard.setSecondSend(PuKe.jointCardAndTakeTwoRows(cardType));
            if (retCard.getSecondRole().size() == 0) {
                retCard.setThirdRole(sendCard.getThirdRole());
                retCard.setCardValue(cardType);
                // retCard.setThirdSend(PuKe.jointCardAndTakeTwoRows(cardValue));
                retCard.setWinner(1 == sendCard.getLordId() ? 1 : 0);
                return DataResult.successOfData(retCard);
            }
        } else {
            retCard.setSecondRole(sendCard.getSecondRole());
            retCard.setSecondSend(new ArrayList<>());
        }
        lists = PukeUtil.combAllList(retCard.getFirstRole(), retCard.getSecondRole(), sendCard.getThirdRole());
        cardValue = PuKe.autoReceive(cardType, sendCard.getThirdRole(), lists, 2, sendCard.getLordId(),sendCard.getRoomId());
        PukeRoom.addRecordByRoomId(sendCard.getRoomId(),cardValue,2);
        if (cardValue.getCheck()) {
            cardType = cardValue;
            retCard.setThirdRole(PuKe.afterAutoRecive(sendCard.getThirdRole(), cardType));
            retCard.setThirdSend(PuKe.jointCardAndTakeTwoRows(cardType));
            if (retCard.getThirdRole().size() == 0) {
                retCard.setCardValue(cardValue);
                retCard.setWinner(2 == sendCard.getLordId() + 1 ? 1 : 0);
                return DataResult.successOfData(retCard);
            }
        } else {
            retCard.setThirdRole(sendCard.getThirdRole());
            retCard.setThirdSend(new ArrayList<>());
        }
        retCard.setCardValue(cardType);
        retCard.setWinner(-1);
        return DataResult.successOfData(retCard);
    }

    @PostMapping("/notSendCard")
    public DataResult notSendCard(@RequestBody NotSendCard notSendCard) {
        List<List<Card>> lists;
        RetCard retCard = new RetCard();
        retCard.setFirstRole(notSendCard.getFirstRole());
        retCard.setFirstSend(new ArrayList<>());
        PukeRoom.addRecordByRoomId(notSendCard.getRoomId(),CardValue.errorWay(),0);
        int lastSend = notSendCard.getCardValue().getSendId();
        CardValue cardType = notSendCard.getCardValue();
        lists = PukeUtil.combAllList(notSendCard.getFirstRole(), notSendCard.getSecondRole(), notSendCard.getThirdRole());
        SpecialList specialListSecond = PukeUtil.packageSpecialCard(notSendCard.getSecondRole());
        if (lastSend == 1) {
            cardType = PuKe.autoSendCardTel(specialListSecond, lists, notSendCard.getCardValue(), notSendCard.getLordId(), 1,notSendCard.getRoomId());
            retCard.setSecondRole(PuKe.afterAutoRecive(notSendCard.getSecondRole(), cardType));
            retCard.setSecondSend(PuKe.jointCardAndTakeTwoRows(cardType));
            PukeRoom.addRecordByRoomId(notSendCard.getRoomId(),cardType,1);
        } else {
            CardValue cardValue = PuKe.autoReceive(cardType, notSendCard.getSecondRole(), lists, 1, notSendCard.getLordId(),notSendCard.getRoomId());
            PukeRoom.addRecordByRoomId(notSendCard.getRoomId(),cardValue,1);
            if (cardValue.getCheck()) {
                cardType = cardValue;
                lastSend = 1;
                retCard.setSecondRole(PuKe.afterAutoRecive(notSendCard.getSecondRole(), cardType));
                retCard.setSecondSend(PuKe.jointCardAndTakeTwoRows(cardType));
            } else {
                retCard.setSecondRole(notSendCard.getSecondRole());
                retCard.setSecondSend(new ArrayList<>());
            }
        }


        if (retCard.getSecondRole().size() == 0) {
            retCard.setWinner(1 == notSendCard.getLordId() ? 1 : 0);
            retCard.setThirdRole(notSendCard.getThirdRole());
            retCard.setThirdSend(new ArrayList<>());
            retCard.setCardValue(cardType);
            return DataResult.successOfData(retCard);
        }

        if (lastSend == 2) {
            lists = PukeUtil.combAllList(notSendCard.getFirstRole(), retCard.getSecondRole(), notSendCard.getThirdRole());
            SpecialList specialListThird = PukeUtil.packageSpecialCard(notSendCard.getThirdRole());
            cardType = PuKe.autoSendCardTel(specialListThird, lists, null, notSendCard.getLordId(), 2,notSendCard.getRoomId());
            retCard.setThirdRole(PuKe.afterAutoRecive(notSendCard.getThirdRole(), cardType));
            retCard.setThirdSend(PuKe.jointCardAndTakeTwoRows(cardType));
            PukeRoom.addRecordByRoomId(notSendCard.getRoomId(),cardType,2);
        } else {
            lists = PukeUtil.combAllList(notSendCard.getFirstRole(), retCard.getSecondRole(), notSendCard.getThirdRole());
            CardValue cardValue = PuKe.autoReceive(cardType, notSendCard.getThirdRole(), lists, 2, notSendCard.getLordId(),notSendCard.getRoomId());
            PukeRoom.addRecordByRoomId(notSendCard.getRoomId(),cardValue,2);
            if (cardValue.getCheck()) {
                cardType = cardValue;
                retCard.setThirdRole(PuKe.afterAutoRecive(notSendCard.getThirdRole(), cardType));
                retCard.setThirdSend(PuKe.jointCardAndTakeTwoRows(cardType));
            } else {
                retCard.setThirdRole(notSendCard.getThirdRole());
                retCard.setThirdSend(new ArrayList<>());
            }
        }
        retCard.setWinner(-1);
        retCard.setCardValue(cardType);
        if (retCard.getThirdRole().size() == 0) {
            retCard.setWinner(2 == notSendCard.getLordId() ? 1 : 0);
        }
        return DataResult.successOfData(retCard);
    }

    @PostMapping("/catchLord")
    public DataResult catchLord(@RequestBody Lord lord) {
        int[] temp = new int[6];
        Arrays.fill(temp,-1);
        for (int i = 0; i < lord.getChoose().length; i++) {
            temp[i] = lord.getChoose()[i];
        }
        RetLord retLord = new RetLord();
        retLord.setFirstLord(lord.getFirstLord());
        if (!lord.isHasChoose()){
            int result = PuKe.getLordChoose(lord.getCardRole().get((lord.getChooseIndex() + lord.getFirstLord())  % 3),lord);
            temp[lord.getChooseIndex()] = result;
            retLord.setChoose(result);
        }
        retLord.setResult(PuKe.resultLord(temp, lord.getFirstLord()));
        return DataResult.successOfData(retLord);
    }

    @PostMapping("/mixLord")
    public DataResult mixLord(@RequestBody Lord lord) {
        RetCard retCard = new RetCard();
        List<Card> tempList;
        if (lord.getFirstLord() == 0) {
            tempList = lord.getCardRole().get(0);
            tempList.addAll(lord.getCardRole().get(3));
            tempList.sort((t1, t2) -> t2.getId() - t1.getId());
            retCard.setFirstRole(tempList);
            retCard.setSecondRole(lord.getCardRole().get(1));
            retCard.setThirdRole(lord.getCardRole().get(2));
        }
        if (lord.getFirstLord() == 1) {
            tempList = lord.getCardRole().get(1);
            tempList.addAll(lord.getCardRole().get(3));
            tempList.sort((t1, t2) -> t2.getId() - t1.getId());
            retCard.setFirstRole(lord.getCardRole().get(0));
            retCard.setSecondRole(tempList);
            retCard.setThirdRole(lord.getCardRole().get(2));
        }
        if (lord.getFirstLord() == 2) {
            tempList = lord.getCardRole().get(2);
            tempList.addAll(lord.getCardRole().get(3));
            tempList.sort((t1, t2) -> t2.getId() - t1.getId());
            retCard.setFirstRole(lord.getCardRole().get(0));
            retCard.setSecondRole(lord.getCardRole().get(1));
            retCard.setThirdRole(tempList);
        }

        PukeRoom.initRoom(lord.getRoomId(),PukeRoom.packageList(retCard.getFirstRole(),retCard.getSecondRole(),retCard.getThirdRole()));
        return DataResult.successOfData(retCard);
    }

    @PostMapping("/autoSendCard")
    public DataResult autoSendCard(@RequestBody AutoSendCard autoSendCard) {
        RetCard retCard = new RetCard();
        List<List<Card>> list;
        CardValue cardValue;
        list = PukeUtil.combAllList(autoSendCard.getFirstRole(), autoSendCard.getFirstRole(), autoSendCard.getThirdRole());
        if (autoSendCard.getLunCi() == 1) {
            SpecialList specialListSecond = PukeUtil.packageSpecialCard(autoSendCard.getSecondRole());
            cardValue = PuKe.autoSendCardTel(specialListSecond, list, null, autoSendCard.getLordId(), 1,autoSendCard.getRoomId());
            retCard.setCardValue(cardValue);
            retCard.setSecondSend(PuKe.jointCardAndTakeTwoRows(cardValue));
            retCard.setSecondRole(PuKe.afterAutoRecive(autoSendCard.getSecondRole(), cardValue));
            PukeRoom.addRecordByRoomId(autoSendCard.getRoomId(),cardValue,1);
            if (retCard.getSecondRole().size() == 0) {
                retCard.setWinner(1 == autoSendCard.getLordId() ? 1 : 0);
                retCard.setThirdRole(autoSendCard.getThirdRole());
                retCard.setSecondRole(autoSendCard.getSecondRole());
                return DataResult.successOfData(retCard);
            }
            list = PukeUtil.combAllList(autoSendCard.getFirstRole(), retCard.getSecondRole(), autoSendCard.getThirdRole());
            CardValue cardType = PuKe.autoReceive(cardValue, autoSendCard.getThirdRole(), list, 2, autoSendCard.getLordId(),autoSendCard.getRoomId());
            PukeRoom.addRecordByRoomId(autoSendCard.getRoomId(),cardType,2);
            if (cardType.getCheck()) {
                retCard.setThirdSend(PuKe.jointCardAndTakeTwoRows(cardType));
                retCard.setThirdRole(PuKe.afterAutoRecive(autoSendCard.getThirdRole(), cardType));
                retCard.setCardValue(cardType);
            } else {
                retCard.setThirdSend(new ArrayList<>());
                retCard.setThirdRole(autoSendCard.getThirdRole());
            }

        }

        if (autoSendCard.getLunCi() == 2) {
            list = PukeUtil.combAllList(autoSendCard.getFirstRole(), autoSendCard.getSecondRole(), autoSendCard.getThirdRole());
            SpecialList specialListThird = PukeUtil.packageSpecialCard(autoSendCard.getThirdRole());
            cardValue = PuKe.autoSendCardTel(specialListThird, list, null, autoSendCard.getLordId(), 2,autoSendCard.getRoomId());
            retCard.setCardValue(cardValue);
            retCard.setThirdSend(PuKe.jointCardAndTakeTwoRows(cardValue));
            retCard.setThirdRole(PuKe.afterAutoRecive(autoSendCard.getThirdRole(), cardValue));
            if (retCard.getThirdRole().size() == 0) {
                retCard.setWinner(2 == autoSendCard.getLordId() ? 2 : 0);
                retCard.setSecondRole(autoSendCard.getSecondRole());
                retCard.setFirstRole(autoSendCard.getFirstRole());
                return DataResult.successOfData(retCard);
            }
            retCard.setSecondRole(autoSendCard.getSecondRole());
            PukeRoom.addRecordByRoomId(autoSendCard.getRoomId(),cardValue,2);
        }
        retCard.setWinner(-1);
        retCard.setFirstRole(autoSendCard.getFirstRole());
        return DataResult.successOfData(retCard);
    }

    @PostMapping("/test")
    public DataResult test(@RequestBody NotSendCard notSendCard) {
        StringJoiner sj = new StringJoiner("],","Card[] cardF = {","]};");
        for (Card card : notSendCard.getFirstRole()) {
            sj.add("card[" + card.getId());
            sj.add("card[" + card.getId());
        }
        StringJoiner sjS = new StringJoiner("],","Card[] cards = {","]};");
        for (Card card : notSendCard.getSecondRole()) {
            sjS.add("card[" + card.getId());
        }
        StringJoiner sjT = new StringJoiner("],","Card[] cardT = {","]};");
        for (Card card : notSendCard.getThirdRole()) {
            sjT.add("card[" + card.getId());
        }
        System.out.println(sj);
        System.out.println(sjS);
        System.out.println(sjT);
        return DataResult.successOfData("ok");
    }

    @PostMapping("/printInfoByRoom")
    public DataResult printInfoByRoom(@RequestParam("roomId") String roomId){
        return  DataResult.successOfData(PukeRoom.printRoomInfoByRoomId(roomId));
    }


}
