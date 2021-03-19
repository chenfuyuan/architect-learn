package com.architect.learn.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 创建线程的5种方式
 * @Author chenfuyuan
 * @Date 2021/3/19 23:02
 */
public class CreateThread {

    /**
     * 第一种方法: 继承Thread类，重写run方法
     */
    public static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("继承Thread类方式，进行线程创建");
        }
    }

    /**
     * 第二种方法: 实现Runnable接口，实现run方法
     */
    public static class MyRun implements Runnable {

        @Override
        public void run() {
            System.out.println("实现Runnable接口，进行线程创建");
        }
    }


    /**
     * 第三种方法: 实现Callable接口，实现call方法。
     * Callable接口可允许返回值，需搭配Future接口使用
     */
    public static class MyCall implements Callable<String>{

        @Override
        public String call() throws Exception {
            System.out.println("实现Callable接口，进行线程创建");
            return "执行Callable线程成功";
        }
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //测试线程创建方式
        //1. 继承Thread类
        Thread myThread = new MyThread();
        myThread.start();

        //2. 实现Runnable接口
        Thread myRun = new Thread(new MyRun());
        myRun.start();

        //3. 实现Callable接口
        Callable callable;
        FutureTask futureTask = new FutureTask<>(new MyCall());
        Thread myCall = new Thread(futureTask);
        myCall.start();
        System.out.println(futureTask.get());


        //4. lambda表达式
        Thread lambdaThread = new Thread(()->{
            System.out.println("通过lambda表达式创建线程");
        });
        lambdaThread.start();

        //5. 线程池
        int cpuSize = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPool=new ThreadPoolExecutor(cpuSize,cpuSize+2,
                1L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        //直接执行
        threadPool.execute(()->{
            System.out.println("通过线程池execute方法进行线程创建");
        });

        //提交线程
        threadPool.submit(() -> {
            System.out.println("线程池submit方法进行线程创建");
        });

        //提交实现Callable接口
        Future<String> future = threadPool.submit(new MyCall());
        System.out.println(future.get());
        threadPool.shutdown();




    }



}
