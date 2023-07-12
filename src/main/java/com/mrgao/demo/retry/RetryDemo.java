package com.mrgao.demo.retry;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;

import java.util.concurrent.TimeUnit;

/**
 * @author Mr.Gao
 * @date 2023/7/11 10:09
 * @apiNote:
 */
public class RetryDemo {

    public static void main(String[] args) {
        testRetry();
    }

    public static void testRetry() {
        // RetryerBuilder 构建重试实例 retryer,可以设置重试源且可以支持多个重试源，可以配置重试次数或重试超时时间，以及可以配置等待时间间隔
        Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
                //设置异常重试源 不可设置多个
                .retryIfExceptionOfType(RuntimeException.class)
                //设置根据结果重试
                .retryIfResult(res -> !res)
                //固定时长等待策略
                .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
                //指数等待策略 1s  2s  4s 3个参数, multiplier: 乘数, maximumTime: 最大等待时长, maximumTimeUnit: 最大等待时长单位
//                .withWaitStrategy(WaitStrategies.exponentialWait(1000, 2, TimeUnit.MILLISECONDS))
                //斐波那契等待策略 3个参数, multiplier: 乘数, maximumTime: 最大等待时长, maximumTimeUnit: 最大等待时长单位
                //.withWaitStrategy(WaitStrategies.fibonacciWait(100, 5, TimeUnit.MINUTES))
                //随机时长等待策略 随机区间配置，[2, 10] 2-10s随机等待，四个参数分别为:
                //minimumTime: 最小值，minimumTimeUnit: 最小值单位； maximumTime: 最大值, maximumTimeUnit: 最大值单位
//                .withWaitStrategy(WaitStrategies.randomWait(2, TimeUnit.SECONDS, 10, TimeUnit.SECONDS))
                //递增等待策略 四个参数 :
                // initialSleepTime: 初始等待时长，initialSleepTimeUnit: 初始等待时长单位, increment: 递增时长值, incrementTimeUnit: 递增时长单位
//                .withWaitStrategy(WaitStrategies.incrementingWait(2, TimeUnit.SECONDS, 3, TimeUnit.SECONDS))
                //异常等待策略
                // 参数: exceptionClass: 异常类，Function<T, Long> function: 处理函数，出现对应异常，返回等待时长
//                .withWaitStrategy(WaitStrategies.exceptionWait(RemoteAccessException.class, e -> 3000L))
                // 复合等待策略 等待时间为所有等待策略时间的总和
//                .withWaitStrategy(
//                        WaitStrategies.join(WaitStrategies.exceptionWait(NullPointerException.class, e -> 1000L),
//                                WaitStrategies.fixedWait(2, TimeUnit.SECONDS)))
                //重试指定次数停止
                .withStopStrategy(StopStrategies.stopAfterAttempt(30))
                //重试指定时长后结束
//                .withStopStrategy(StopStrategies.stopAfterDelay(10, TimeUnit.SECONDS))
                //.withRetryListener(new MyRetryListener())
                .build();
        try {
            Boolean flag = retryer.call(() -> RetryDemoTask.retryTask("test_retry"));
            System.out.println("end: " + flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
