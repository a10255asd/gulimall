package com.atjixue.gulimall.search.thread;

import io.netty.util.concurrent.CompleteFuture;
import org.springframework.cache.annotation.Cacheable;

import java.util.concurrent.*;

/**
 * @Author LiuJixue
 * @Date 2022/8/28 00:06
 * @PackageName:com.atjixue.gulimall.search.thread
 * @ClassName: ThreadTest
 * @Description: TODO
 * @Version 1.0
 */
public class ThreadTest {
    public static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main ..... start");
        // 没返回值
//        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 2;
//            System.out.println("运行结果" + i);
//        }, executor);
        // 有返回值
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 0;
//            System.out.println("运行结果" + i);
//            return i;
//        }, executor).whenComplete((result,exception)->{
//            //虽然能得到异常信息，但是没法修改返回数据。
//                System.out.println("异步任务成功完成了....结果是：" + result + "异常是：" + exception);
//        }).exceptionally(throwable -> {
//            //可以感知异常同时返回默认值。
//            return 10;
//        });

        // 方法执行完成后的处理
//        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 4;
//            System.out.println("运行结果" + i);
//            return i;
//        }, executor).handle((res,thr)->{
//            if(res!= null){
//                return  res * 2;
//            }
//            if(thr != null){
//                return 0;
//            }
//            return 0;
//        });
       /**
        * 线程串行化
        * 1）、 thenRun :不能或得到上一步的执行结果，无返回值
        * .thenRunAsync(() -> {
        *             System.out.println("任务2 启动了");
        *         });
        * 2）、thenAcceptAsync() 能接收上一步结果，但是无返回值
        * .thenAcceptAsync(res->{
        *             System.out.println("任务2 启动了 " + res);
        *         });
        * 3)、 thenApplyAsync() 既能接收到上一步的结果，有返回值
        *
        * */

//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("当前线程：" + Thread.currentThread().getId());
//            int i = 10 / 4;
//            System.out.println("运行结果" + i);
//            return i;
//        }, executor).thenApplyAsync(res -> {
//            System.out.println("任务2 启动了 " + res);
//            return "hello" + res;
//        }, executor);
        // R apply(T t)
        //Integer integer = future1.get();

        /**
         * 两个都完成
         * */
//        CompletableFuture<Object> future01 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务1线程：" + Thread.currentThread().getId());
//            int i = 10 / 4;
//            System.out.println("任务1结果" + i);
//            return i;
//        }, executor);
//
//        CompletableFuture<Object> future02 = CompletableFuture.supplyAsync(() -> {
//            System.out.println("任务2线程：" + Thread.currentThread().getId());
//
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            System.out.println("任务2结果" );
//
//            return "hello";
//        }, executor);
        // 不能感知到前两个的执行结果
//        future01.runAfterBothAsync(future02,()->{
//            System.out.println("任务3开始");
//        },executor);
        // 可以获取 前两个的执行结果
//        future01.thenAcceptBothAsync(future02,(f1,f2)->{
//            System.out.println("任务3开始。。之前的结果：" + f1 + "----->" + f2);
//        },executor);
        //可以获得结果并且有返回值
//        CompletableFuture<String> future = future01.thenCombineAsync(future02, (f1, f2) -> {
//            return f1 + ":" + f2 + "--> hahaa";
//        }, executor);
        /**
         * 两个任务，只有一个完成，我们就执行任务3
         * 不感知结果，自己也无返回值
         * */
//        future01.runAfterEitherAsync(future02,()->{
//            System.out.println("任务3开始。。之前的结果：" );
//        },executor);
        // 感知结果，没有返回值
//        future01.acceptEitherAsync(future02,(res)->{
//            System.out.println("任务3开始。。之前的结果：" + res);
//        },executor);
        // 自己感知结果，且自己有返回值
//        CompletableFuture<String> future = future01.applyToEitherAsync(future02, (res) -> {
//            System.out.println("任务3开始。。之前的结果：" + res);
//            return res + "----> hahah";
//        }, executor);

        CompletableFuture<String> futureImg = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品的图片信息");
            return "hello.jpg";
        },executor);

        CompletableFuture<String> futureAttr = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("查询商品属性");
            return "黑色+256g";
        },executor);

        CompletableFuture<String> futureDesc = CompletableFuture.supplyAsync(() -> {
            System.out.println("查询商品介绍");
            return "华为";
        },executor);

        // 阻塞式等待
//        futureImg.get();
//        futureAttr.get();
//        futureDesc.get();

//        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureImg, futureAttr, futureDesc);
//        allOf.get();//等待所有结果完成

        CompletableFuture<Object> anyOf = CompletableFuture.anyOf(futureImg, futureAttr, futureDesc);
        anyOf.get();//等待所有结果完成

//        System.out.println("main ..... end...." + futureImg.get() + "=>" +
//                futureAttr.get() +"=>" +futureDesc.get());
        System.out.println("main ..... end...." + anyOf.get());
    }


    public static void thread(String[] args) throws ExecutionException, InterruptedException {
        /***
         * 1.继承Thread
         * 2.实现Runnable接口
         * 3.实现Callable接口 + FutureTask（可以拿到返回结果，可以处理异常）
         * 以后在业务代码里面以上三种启动线程的方式都不用，应该将所有的多线程异步任务交给线程池执行
         * 4.线程池，给线程池直接提交任务
         * 去呗：1、2不能得到返回值，3、可以获取返回值 1、2、3都不能控制资源
         * 4可以控制资源，性能是稳定的，无论面对多高的并发，都不会导致资源耗尽
         */

        System.out.println("main ..... start");
        //Thread01 thread = new Thread01();
        //thread.start();// 启动线程
//        Runable01 runable01 = new Runable01();
//        new Thread(runable01).start();

        // new Thread(()-> System.out.println("hello")).start();
//        FutureTask<Integer> futureTask = new FutureTask<>(new Callable01());
//        new Thread(futureTask).start();
        //等待整个线程执行完成获取返回结果,是阻塞等待。
//        Integer integer = futureTask.get();

        // 当前系统中池只有一两个，每一个异步任务 直接提交给线程池，让她自己去执行

        //System.out.println("main ..... end...." +integer );

        //service.execute(new Runable01());

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5,
                200,
                10,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
//        Executors.newCachedThreadPool();
//        Executors.newFixedThreadPool();
//        Executors.newScheduledThreadPool();
//        Executors.newScheduledThreadPool();
        System.out.println("main ..... end....");

    }

    public static class Thread01 extends Thread {
        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果" + i);
        }
    }

    public static class Runable01 implements Runnable {

        @Override
        public void run() {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果" + i);
        }
    }

    public static class Callable01 implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            System.out.println("当前线程：" + Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果" + i);
            return i;
        }
    }
}
