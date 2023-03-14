package com.mrgao.demo.juc;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Mr.Gao
 * @date: 2022年12月07日 16:46
 * @description:延时队列 <p>
 * 1、基于DelayQueue实现的本地延时队列：
 * 缺点:多节点实例部署时，不能同步消息，同步消费，也不能持久化。
 * </p>
 * <p>
 * 2、基于RabbitMQ死信队列实现的延时队列
 * - 使用RabbitMQ实现延时队列主要用到它的两个特性: 一个是Time-To-Live Extension(TTL),另一个是 Dead Letter Exchanges(DLX).
 * <p>
 * - Time-To-Live Extensions: RabbitMQ允许我们为消息或者队列设置TTL,也就是过期时间。TTL表名了一条消息可在队列中存活的最大时间，单位为毫秒。
 * 也就是说，当某条消息被设置了TTL或者当某条消息进入了设置了TTL的队列时，这条消息会经过TTL秒后"死亡"，称为Dead Letter。如果既配置了消息的TTL，
 * 又配置了队列的TTL，那么较小的那个值会被取用。
 * <p>
 * - Dead Letter Exchange: 在被设置了TTL的消息在过期后会称为DeadLetter，其实RabbitMQ中，一共有三种消息的死亡形式：
 * - 1、消息被拒绝。通过basic.reject 或者basic.nack 并且设置的 request 参数为false.
 * - 2、消息因为设置了TTL而过期.
 * - 3、消息进入了一条已经达到最长长度的队列.
 * </p>
 */
public class DelayQueueTest {

    static class DelayMessage<T extends Runnable> implements Delayed {
        private final static long DELAY = 3 * 60 * 1000L;//默认延迟3分钟
        private long expireTime;
        private String desc;
        private T type;
        private String currentTime;

        public long getExpireTime () {
            return expireTime;
        }

        public void setExpireTime (long expireTime) {
            this.expireTime = expireTime;
        }

        public T getType () {
            return type;
        }

        public void setType (T type) {
            this.type = type;
        }

        public DelayMessage (T type, long createTime, String desc) {
            // 默认时间延时10s
            this.expireTime = createTime + DELAY;
            this.type = type;
            this.desc = desc;
            this.currentTime = LocalDateTime.now ().format (DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm:ss"));
            System.out.println (desc + "当前时间:" + LocalDateTime.now ().format (DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm:ss")) + ",过期时间:"
                    + LocalDateTime.ofInstant (new Date (expireTime).toInstant (), ZoneId.systemDefault ()).format (DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm:ss")));
        }

        @Override
        public long getDelay (TimeUnit unit) {
            System.out.println (this.desc + ":" + unit.convert (this.expireTime - System.currentTimeMillis (), TimeUnit.MILLISECONDS));
            return unit.convert (this.expireTime - System.currentTimeMillis (), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo (Delayed other) {
            if (other == this) {
                System.out.println (this.desc + ": @@@@@@===============");
                return 0;
            }
            if (other instanceof DelayMessage) {
                DelayMessage otherRequest = (DelayMessage) other;
                long otherStartTime = otherRequest.expireTime;
                System.out.println (this.desc + "<_>++++++++" + (this.expireTime - otherStartTime) + "[ C ]" + otherRequest.desc);
                return (int) (this.expireTime - otherStartTime);
            }
            System.out.println (this.desc + ": @@@@@@###############");
            return 0;
        }
    }

    private static volatile AtomicInteger delayQueueSize = new AtomicInteger (0);
    private static volatile AtomicInteger count = new AtomicInteger (0);
    private static volatile DelayQueue<DelayMessage> delayQueue = new DelayQueue ();
    private static ExecutorService executorService = Executors.newFixedThreadPool (5);

    public static void main (String[] args) {
        System.out.println ("##########################");
        System.out.println ("发起时间:" + LocalDateTime.now ().format (DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm:ss")));
        Thread fillValue = new Thread (() -> {
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                delayQueueSize.getAndIncrement ();// +1
                // 10s 11s 12s......
                delayQueue.add (new DelayMessage (() -> {
                    System.out.println ("D" + (finalI + 1));
                    try {
                        TimeUnit.SECONDS.sleep (1);
                    } catch (InterruptedException e) {
                        e.printStackTrace ();
                    }
                }, System.currentTimeMillis (), "T" + (finalI + 1)));

                try {
                    TimeUnit.SECONDS.sleep (1);
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
            }

            while (true) {
                DelayMessage<? extends Runnable> take = null;
                long cabicity = delayQueueSize.get ();
                try {
                    take = delayQueue.take ();
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
                if (null == take) {
                    continue;
                }
                delayQueueSize.decrementAndGet ();
                Runnable runnable = take.getType ();
                // 提交到线程池执行 task
                System.out.println (take.desc + "---队列容量:" + cabicity + "->创建时间:" + take.currentTime + "->当前时间:" + LocalDateTime.now ().format (DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm:ss")));
                // 模拟逻辑处理(超时)订单
                executorService.execute (runnable);
            }

        });
        System.out.println ("~~~~~~~~~~~~~~~~~~~~~");
        Thread thread = new Thread (() -> {
            while (true) {
                try {
                    TimeUnit.MILLISECONDS.sleep (100);
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }
                if (delayQueueSize.get () != 0 && (delayQueueSize.get () == delayQueue.size ())) {
                    System.out.println ("delayQueue 大小:" + delayQueueSize.get ());
                }
                if (delayQueue.size () == 0) {
                    int num = count.getAndIncrement ();
                    if (10 == num) break;
                    System.out.println ("队列中没有资源了~~~~");
                }
            }
        });
        thread.setDaemon (true);
        thread.start ();
        fillValue.start ();
    }
}
