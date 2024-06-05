package com.zhj.test.entity;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * MyDelayDequel
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/12/25 21:49
 */
public class MyDelayDeque implements Delayed {
    private final String name;
    private final long delayTime;
    private final long startTime;
    private final long endTime;

    public MyDelayDeque(String name, long delayTime) {
        this.name = name;
        this.delayTime = delayTime;
        this.startTime = System.currentTimeMillis();
        this.endTime = startTime + delayTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return endTime - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        return 0;
    }

    @Override
    public String toString() {
        return "MyDelayDeque{" +
                "name='" + name + '\'' +
                ", delayTime=" + delayTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public static void main(String[] args) throws InterruptedException {
        MyDelayDeque myDelayDequeOne = new MyDelayDeque("delay1",10000);
        MyDelayDeque myDelayDequeSecond = new MyDelayDeque("delay2",20000);
        MyDelayDeque myDelayDequeThird = new MyDelayDeque("delay3",15000);
        DelayQueue<MyDelayDeque> delayDeque = new DelayQueue<>();
        delayDeque.offer(myDelayDequeOne);
        delayDeque.offer(myDelayDequeSecond);
        delayDeque.offer(myDelayDequeThird);
        while (true){
            MyDelayDeque take = delayDeque.take();
            System.out.println(take);
        }

    }


}
