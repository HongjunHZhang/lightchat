package com.zhj.test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * TestSafePoint
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/12/26 22:03
 */
public class TestSafePoint {

    public static void main(String[] args) throws InterruptedException {
//        System.out.println("xbc".compareTo("xbcz"));
        testInt();
    }

    /**
     * int作为遍历索引时被视为有限次数，不会增加安全点，当Thread.sleep()方法结束后触发GC因为等待线程执行完循环而阻塞，
     * 固主线程程序会等待子线程程序结束后再执行，使用long定义则每次循环都会添加安全点，不需要等待线程结束既可以继续执行主线程。
     * 注(在jdk10之后int也会添加安全节点，不适用)
     * @throws InterruptedException
     */
    public static void testInt() throws InterruptedException {
        AtomicInteger  num = new AtomicInteger(0);
        Runnable one = () -> {
                for (int i = 0; i < 1000000000; i++) {
                    num.getAndAdd(1);
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        };

        Runnable two = () -> {
            for (int i = 0; i < 1000000000; i++) {
                num.getAndAdd(1);
                try {
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t1 = new Thread(one);
        Thread t2 = new Thread(two);

        t1.start();
        t2.start();
        System.out.println("start");
        Thread.sleep(1000);
        System.out.println(num);
    }



}
