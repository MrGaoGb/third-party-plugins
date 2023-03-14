package com.mrgao.demo.juc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: Mr.Gao
 * @date: 2022年11月18日 10:41
 * @description:
 */
public class FutureTaskTest {
    public static void main(String[] args) {
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        Callable<Integer> task1 = () -> {
            TimeUnit.SECONDS.sleep(3);
            return 3;
        };
        Callable<Integer> task2 = () -> {
            TimeUnit.SECONDS.sleep(5);
            return 5;
        };
        Callable<Integer> task3 = () -> {
            TimeUnit.SECONDS.sleep(1);
            return 1;
        };
        List<Callable<Integer>> taskList = new ArrayList<>();
        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);
        long begin = System.currentTimeMillis();
        List<Future<Integer>> futures = forkJoinPool.invokeAll(taskList);
        for (int i = 0; i < futures.size(); i++) {
            Integer result = null;
            try {
                result = futures.get(i).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("响应结果:" + result);
        }
        long end = System.currentTimeMillis();
        System.out.println("总耗时:" + (end - begin) + "ms");
        // 关闭池
        executorService.shutdown();
    }
}
