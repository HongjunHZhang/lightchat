package com.zhj.test;

import com.zhj.util.FileUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * DuiToZhan
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/8/29 19:09
 */
public class DuiToZhan {
    Queue<Integer> queueFirst = new LinkedList<Integer>();
    Queue<Integer> queueSecond = new LinkedList<Integer>();

    public void offer(int num){
        Queue<Integer> queue = queueFirst.isEmpty()?queueSecond:queueFirst;
        queue.offer(num);
    }

    public int poll(){
        if (queueFirst.isEmpty() && queueSecond.isEmpty()){
            return -1;
        }

        Queue<Integer> notEmptyQueue =  queueFirst.isEmpty()?queueSecond:queueFirst;
        Queue<Integer> emptyQueue =  queueFirst.isEmpty()?queueFirst:queueSecond;

        while (!notEmptyQueue.isEmpty()){
            int a = notEmptyQueue.poll();
            if (notEmptyQueue.isEmpty()){
                return a;
            }
            emptyQueue.offer(a);
        }

        return -1;
    }

    public boolean isEmpty(){
        return queueFirst.isEmpty() && queueSecond.isEmpty();
    }

    public static void main(String[] args) {
//        DuiToZhan deque = new DuiToZhan();
//        deque.offer(1);
//        deque.offer(7);
//        deque.offer(3);
//        deque.offer(2);
//        deque.offer(9);
//        deque.offer(10);
//        while (!deque.isEmpty()){
//            System.out.println(deque.poll());
//        }
        Map<String,String> map = new HashMap<>();

        String temp = "12\\45\\7812\\dsa.txt";
        System.out.println(FileUtil.createFileNameWhenFileRepetitive(temp,3));


    }

}
