package com.mrgao.demo.juc;

import java.util.concurrent.*;

/**
 * @author: Mr.Gao
 * @date: 2022年11月18日 9:58
 * @description:
 */
public class CompletionServiceTest {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executorService);
        Callable<Integer> task1 = () -> {
            try {
                // 模拟业务逻辑执行时间
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 5;
        };
        // 线程2需要执行3s
        Callable<Integer> task2 = () -> {
            try {
                // 模拟逻辑执行
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 3;
        };
        // 线程3需要执行1s
        Callable<Integer> task3 = () -> {
            try {
                // 模拟逻辑执行
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 1;
        };
        // 提交三个任务
        completionService.submit(task1);
        completionService.submit(task2);
        completionService.submit(task3);
        long begin = System.currentTimeMillis();
        // --获取执行结果 提交了三个任务 故循环次数为3
        for (int i = 0; i < 3; i++) {
            Integer result = null;
            try {
                // completionService.poll(); 会报NPE
                //Future<Integer> poll = completionService.take(); //不会报错
                Future<Integer> poll = completionService.poll(1, TimeUnit.SECONDS);// 会出现NPE
                result = poll.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("响应结果:" + result);
        }
        long end = System.currentTimeMillis();
        System.out.println("总耗时:" + (end - begin) + "ms");
        executorService.shutdown();
    }
}
