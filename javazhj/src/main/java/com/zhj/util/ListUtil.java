package com.zhj.util;

import com.zhj.pukeyule.Card;

import java.util.List;

/**
 * ListUtil
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/7/23 13:27
 */
public class ListUtil {


    public static Card getCardTail(List<Card> list){
        return list.get(list.size()-1);
    }

    public static List<Card> getCardListTail(List<List<Card>> list){
        return list.size() == 0 ? null:list.get(list.size()-1);
    }

}
