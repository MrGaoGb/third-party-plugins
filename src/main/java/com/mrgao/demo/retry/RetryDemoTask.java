package com.mrgao.demo.retry;


import lombok.extern.slf4j.XSlf4j;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Mr.Gao
 * @date 2023/7/11 10:05
 * @apiNote:
 */
public class RetryDemoTask {

    /**
     * 重试方法
     *
     * @return
     */
    public static boolean retryTask(String param) {
        System.out.println("收到请求参数:" + param);
        int i = ThreadLocalRandom.current().nextInt(0, 11);
        ;
        System.out.println("随机生成的数:" + i);
        if (i < 2) {
            System.out.println("小于2,抛出参数异常.");
            throw new IllegalArgumentException("参数异常");
        } else if (i < 5) {
            System.out.println("2-4,返回true.");
            return true;
        } else if (i < 7) {
            System.out.println("5-6,返回false.");
            return false;
        } else {
            //为其他
            System.out.println("抛出自定义异常.");
            throw new NullPointerException("大于6,抛出自定义异常");
        }
    }
}
