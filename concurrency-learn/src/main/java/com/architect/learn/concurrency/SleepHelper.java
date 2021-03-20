package com.architect.learn.concurrency;

import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author chenfuyuan
 * @Date 2021/3/21 1:25
 */
public class SleepHelper {
    public static void sleepSeconds(int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
