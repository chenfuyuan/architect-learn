package com.architect.learn.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.architect.learn.concurrency.SleepHelper.sleepSeconds;

/**
 * @Description: 测试线程打断各个方法及场景
 * @Author: chenfuyuan
 * @Date: 2021/3/24 21:19
 */
public class InterruptThread {

    public static void main(String[] args) {
        //1. 测试isInterrupted
        //testIsInterrupted();

        //2. 测试interrupted
        //testInterrupted();

        //3. 测试sleep时，打断抛出异常
        //testSleep();

        //4. 测试wait时，打断抛出异常
        //testWait();

        //5. 测试synchronized状态时，能否打断争抢锁过程
        //testSynchronized();


        //6. 测试lock
        //testLock();

        //7. 测试lockInterruptibly
        testLockInterruptibly();
    }

    /**
     * 测试使用lockInterruptibly()争抢锁，可不可以被打断
     * @author chenfuyuan
     * @date 2021/3/24 22:04
     */
    private static void testLockInterruptibly() {
        Lock lock = new ReentrantLock();
        new Thread(()->{
            lock.lock();
            try {
                sleepSeconds(5);
            }finally {
                lock.unlock();
            }
        }).start();
        sleepSeconds(1);

        Thread thread = new Thread(()->{
            try {
                lock.lockInterruptibly();
                try {

                }finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                System.out.println("争抢锁时被打断");
            }
            System.out.println("线程抢到锁，并结束");
            System.out.println("线程打断状态:"+Thread.currentThread().isInterrupted());
        });
        thread.start();
        sleepSeconds(1);
        thread.interrupt();
    }

    /**
     * 测试使用lock()争抢锁，可不可以被打断
     * @author chenfuyuan
     * @date 2021/3/24 22:04
     */
    private static void testLock() {
        Lock lock = new ReentrantLock();
        new Thread(()->{
            lock.lock();
            try {
                sleepSeconds(5);
            }finally {
                lock.unlock();
            }
        }).start();
        sleepSeconds(1);

        Thread thread = new Thread(()->{
            lock.lock();
            try {

            }finally {
                lock.unlock();
            }
            System.out.println("线程抢到锁，并结束");
            System.out.println("线程打断状态:"+Thread.currentThread().isInterrupted());
        });
        thread.start();
        sleepSeconds(1);
        thread.interrupt();
    }


    /**
     * 测试能否Synchronized状态，能否打断争抢锁过程
     * @author chenfuyuan
     * @date 2021/3/24 21:59
     */
    private static void testSynchronized() {
        Object o = new Object();

        new Thread(()->{
            synchronized (o) {
                sleepSeconds(5);
            }
        }).start();
        sleepSeconds(1);

        Thread thread = new Thread(()->{
            synchronized (o) {
            }
            System.out.println("线程抢到锁，并结束");
            System.out.println("线程打断状态:"+Thread.currentThread().isInterrupted());
        });
        thread.start();
        sleepSeconds(1);
        thread.interrupt();
    }

    /**
     * 测试线程在wait情况下，进行打断
     * @author chenfuyuan
     * @date 2021/3/24 21:55
     */
    private static void testWait() {
        Object o = new Object();
        Thread thread = new Thread(() -> {
            synchronized (o) {
                try {
                    o.wait(5000);
                } catch (InterruptedException e) {
                    //当抛出InterruptedException时，会自动将打断标识重置
                    System.out.println("线程wait时被打断");
                    System.out.println("线程打断标识:" + Thread.currentThread().isInterrupted());
                }
            }
        });
        thread.start();
        sleepSeconds(1);
        thread.interrupt();
    }

    /**
     * 测试线程在sleep状态时，被打断
     * @author chenfuyuan
     * @date 2021/3/24 21:50
     */
    private static void testSleep() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                //当抛出InterruptedException时，会自动将打断标识重置
                System.out.println("线程sleep时被打断");
                System.out.println("线程打断标识:"+Thread.currentThread().isInterrupted());
            }
        });
        thread.start();
        sleepSeconds(1);
        thread.interrupt();
    }


    /**
     * 测试interrupted
     * @author chenfuyuan
     * @date 2021/3/24 21:47
     */
    public static void testInterrupted() {
        Thread thread = new Thread(() -> {
            while(true){
                if (Thread.interrupted()) {
                    System.out.println("线程被打断");
                    System.out.println("打断标识为:"+Thread.interrupted());
                    break;
                }
            }
        });
        thread.start();
        sleepSeconds(1);
        thread.interrupt();
    }



    /*
     * 测试isInterrupted
     * @author chenfuyuan
     * @date 2021/3/24 21:45
     */
    public static void testIsInterrupted() {
        Thread thread = new Thread(() -> {
            while(true){
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("线程被打断");
                    System.out.println("打断标识为:"+Thread.currentThread().isInterrupted());
                    break;
                }
            }
        });
        thread.start();
        sleepSeconds(1);
        thread.interrupt();
    }
}
