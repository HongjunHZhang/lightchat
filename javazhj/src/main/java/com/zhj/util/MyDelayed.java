package com.zhj.util;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author 789
 */
public class MyDelayed implements Delayed {
    private final String name;
    private final long continueTime;
    private final long endTime;
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");

    public MyDelayed(String name, long continueTime) {
        this.name = name;
        this.continueTime = continueTime;
        this.endTime = System.currentTimeMillis() + continueTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return this.endTime - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        return (int) (this.endTime - ((MyDelayed) o).getEndTime());
    }

    public void show() {
        System.out.println(this.name + "执行");
        System.out.println(dateTimeFormatter.format(TimeUtil.getLocalDateTimeOfNow()));
    }

    public String getName() {
        return name;
    }

    public long getContinueTime() {
        return continueTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public static void main(String[] args) throws InterruptedException {
        DelayQueue<MyDelayed> delayQueue = new DelayQueue<>();
        while (true){
            MyDelayed take = delayQueue.take();
        }
    }
}
