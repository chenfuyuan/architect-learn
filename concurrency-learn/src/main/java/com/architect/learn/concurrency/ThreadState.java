package com.architect.learn.concurrency;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.architect.learn.concurrency.SleepHelper.sleepSeconds;

/**
 * @Description: 线程状态
 * 1. NEW: 线程刚刚创建。还没有启动
 * 2. RUNNABLE: 可运行状态,由线程调度器安排执行
 * 3. WAITING:等待被唤醒
 * 4. TIMED WAITING: 隔一段时间后自动唤醒
 * 5. BLOCKED:被阻塞，正在等待锁
 * 6. TERMINATED:线程结束
 * @Author chenfuyuan
 * @Date 2021/3/21 1:12
 */
public class ThreadState {

    public static void outThreadStatus() {
        outThreadStatus(Thread.currentThread());
    }

    public static void outThreadStatus(Thread thread) {
        System.out.println(String.format("线程{%s}:状态{%s}", thread.getName(), thread.getState()));

    }

    public static void main(String[] args) throws Exception {
        //NEW RUNNABLE TERMINATED
        Thread t1 = new Thread(() -> {
            outThreadStatus();     //RUNNABLE
            for (int i = 0; i < 1; i++) {
                sleepSeconds(1);
                System.out.print(i + " ");
            }
            System.out.println();
        });

        outThreadStatus(t1);    //NEW
        t1.start();
        t1.join();
        outThreadStatus(t1);    //TERMINATED

        /*=================WAITING and  TIMED WAITING=====================*/
        Thread t2 = new Thread(() -> {
            LockSupport.park();
            System.out.println("t2 ge on!");
            sleepSeconds(5);
        });
        t2.start();
        sleepSeconds(1);
        outThreadStatus(t2);    //WAITING

        LockSupport.unpark(t2);
        sleepSeconds(1);
        outThreadStatus(t2);    //TIMED_WAITING

        final Object o = new Object();
        Thread t3 = new Thread(()->{
            synchronized (o) {
                System.out.println("t3得到了锁o");
            }
        });

        new Thread(()->{
            synchronized (o) {
                sleepSeconds(5);
            }
        }).start();

        sleepSeconds(1);
        t3.start();
        sleepSeconds(1);
        outThreadStatus(t3);    //BLOCKED


        //==========================================
        final Lock lock = new ReentrantLock();
        Thread t4 = new Thread(()->{
            lock.lock();
            try{
                System.out.println("t4得到了锁lock");
            }finally {
                lock.unlock();
            }
        });

        new Thread(() -> {
            lock.lock();
            try{
                sleepSeconds(5);
            }finally {
                lock.unlock();
            }

        }).start();

        sleepSeconds(1);
        t4.start();
        sleepSeconds(1);
        outThreadStatus(t4);


        //============================================
        Thread t5 = new Thread(()->{
            LockSupport.park();
        });

        t5.start();

        sleepSeconds(1);
        outThreadStatus(t5);
        LockSupport.unpark(t5);
    }
}
