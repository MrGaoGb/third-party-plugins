package com.mrgao.demo.juc;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Gao
 * @date 2022/12/13 16:47
 * @apiNote:该类可开启一个异步线程任务的同时，允许接收返回以及异常情况
 */
public class ListenerFuture {
    public static void main(String[] args) {
        System.out.println("主任务执行完，开始异步执行副任务1.....");
        ListeningExecutorService pool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(5));
        ListenableFuture<String> future = pool.submit(new Task());
        Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("成功,结果是:" + result);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("出错,业务回滚或补偿");
            }
        });
        System.out.println("副本任务启动,回归主任务线，主业务正常返回2.....");
    }

    static class Task implements Callable<String> {

        @Override
        public String call() throws Exception {
            TimeUnit.SECONDS.sleep(5);
            // int a =1/0;
            return "task done";
        }
    }

}
