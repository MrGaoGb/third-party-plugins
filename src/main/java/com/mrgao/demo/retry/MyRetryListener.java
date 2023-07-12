package com.mrgao.demo.retry;

import com.github.rholder.retry.Attempt;
import com.github.rholder.retry.RetryListener;

import java.util.concurrent.ExecutionException;

/**
 * @author Mr.Gao
 * @date 2023/7/11 10:10
 * @apiNote:
 */
public class MyRetryListener implements RetryListener {
    @Override
    public <V> void onRetry(Attempt<V> attempt) {
        // 第几次重试,(注意:第一次重试其实是第一次调用)
        System.out.println("[retry]time=" + attempt.getAttemptNumber());
        // 距离第一次重试的延迟
        System.out.println("delay=" + attempt.getDelaySinceFirstAttempt());
        // 重试结果: 是异常终止, 还是正常返回
        System.out.println("hasException=" + attempt.hasException());
        System.out.println("hasResult=" + attempt.hasResult());

        // 是什么原因导致异常
        if (attempt.hasException()) {
            System.out.println("causeBy=" + attempt.getExceptionCause().toString());
        } else {
            // 正常返回时的结果
            System.out.println("result=" + attempt.getResult());
        }

        // bad practice: 增加了额外的异常处理代码
        try {
            V result = attempt.get();
            System.out.println("rude get =" + result);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
