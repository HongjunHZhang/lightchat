package com.zhj.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ThreadPoolUtil
 *
 * @author zhangHongJun
 * @description TODO * @date
 * 2022/12/27 21:59
 */
public class ThreadPoolUtil {
    public static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(4, 4,
            0L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(2),
            new ThreadPoolExecutor.DiscardPolicy());

    public static void execute(Runnable runnable){
        threadPool.execute(runnable);
    }




}
