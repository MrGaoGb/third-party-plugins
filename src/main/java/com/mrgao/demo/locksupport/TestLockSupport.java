package com.mrgao.demo.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 * @author Mr.Gao
 * @date 2023/7/13 16:57
 * @apiNote:
 */
public class TestLockSupport {
    /**
     * TODO 让线程等待和唤醒的方法:
     * <p>
     * 1、使用Object中的wait()方法让线程等待,使用Object中的notify()方法唤醒!
     * 2、使用JUC包下Condition的await()方法,使用signal()方法唤醒线程!
     * 3、使用LockSupport的park()方法阻塞线程，使用unpark()方法解除线程阻塞!
     *
     * @param args
     */
    public static void main(String[] args) {

        Thread a = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "----come in");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t" + "----被唤醒");
        }, "A");
        a.start();
        Thread b = new Thread(() -> {
            // 唤醒线程A
            LockSupport.unpark(a);
            System.out.println(Thread.currentThread().getName() + "\t" + "----唤醒动作");
        }, "B");
        b.start();

        // LockSupport中的park和unpark方法都是通过调用UNSAFE的native方法
    }
}
