package com.architect.learn.concurrency;

import static com.architect.learn.concurrency.SleepHelper.sleepSeconds;

/**
 * @Description: 结束线程
 * @Author: chenfuyuan
 * @Date: 2021/3/24 22:06
 */
public class EndThread {

    /**
     * 结束标识
     */
    public volatile static boolean endFlag = false;

    public static void main(String[] args) {
        //1. 测试stop方法，强制停止线程，已废弃。容易产生数据不一致问题。不建议使用
        //stop方法会释放线程内所有锁，容易导致数据不一致问题
        //testStop();

        //2. 测试suspend和resume,进行线程暂停和继续。已废弃。容易产生死锁问题
        //suspend暂停线程时，并不会释放其中的锁，如果不执行resume，锁将一直存在，导致死锁问题
        //testSuspendAndResume();

        //3. 使用volatile变量作为循环标识,改变变量来完成结束循环操作。推荐
        //如果遇到阻塞问题(wait,sleep,锁)，将导致无法结束线程
        //testVolatile();

        //4. 使用interrupt方法，结束线程。相较volatile优雅
        testInterrupt();
    }

    /**
     * 测试使用interrupt方法，结束线程
     * @author chenfuyuan
     * @date 2021/3/24 22:31
     */
    private static void testInterrupt() {
        Thread thread = new Thread(()->{
            int i = 1;
            while (!Thread.interrupted()) {
                System.out.println("循环第"+i+"次;");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("线程处于sleep被打断");
                    Thread.currentThread().interrupt();
                }
                i++;
            }

            System.out.println("线程结束");
        });

        thread.start();
        sleepSeconds(5);
        thread.interrupt();
    }

    /**
     * 测试volatile 变量，作为结束标识，结束线程
     * @author chenfuyuan
     * @date 2021/3/24 22:30
     */
    private static void testVolatile() {
        Thread thread = new Thread(()->{
            int i = 1;
            while (!endFlag) {
                System.out.println("循环第"+i+"次;");
                sleepSeconds(1);
                i++;
            }
        });

        thread.start();
        sleepSeconds(5);
        endFlag = true;

    }

    /**
     * 测试suspend和resume方法，对线程进行暂停和继续
     * @author chenfuyuan
     * @date 2021/3/24 22:22
     */
    private static void testSuspendAndResume() {
        Thread thread = new Thread(() -> {
            int i = 1;
            while (true) {
                System.out.println("循环第"+i+"次;");
                sleepSeconds(1);
                i++;
            }
        });

        thread.start();
        sleepSeconds(3);
        thread.suspend();
        sleepSeconds(2);
        thread.resume();
        sleepSeconds(3);
        thread.stop();
    }

    /**
     * 测试stop方法，进行线程的结束
     * @author chenfuyuan
     * @date 2021/3/24 22:19
     */
    public static void testStop() {
        Thread thread = new Thread(() -> {
            int i = 1;
            while (true) {
                System.out.println("循环第"+i+"次;");
                sleepSeconds(1);
                i++;
            }
        });

        thread.start();
        sleepSeconds(5);
        thread.stop();
    }
}
