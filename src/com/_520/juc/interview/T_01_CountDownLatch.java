package com._520.juc.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/*
    实现一个容器，提供两个方法，add，size
    写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
 */

public class T_01_CountDownLatch {
    public static void main(String[] args) {
        // 定义一个集合存储对象
        List<Object> list = new ArrayList<>();

        // 声明锁
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CountDownLatch countDownLatch1 = new CountDownLatch(1);

        Thread t2 = new Thread(() -> {
             System.out.println("t2启动");
             try {
                 // 门栓
                 countDownLatch.await();
                 // 可以指定门闩时间
                 //countDownLatch.await(1,TimeUnit.SECONDS);

             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
             System.out.println("t2结束");
             // 使t1继续执行
//             countDownLatch1.countDown();
        });

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread t1 = new Thread(() -> {

            System.out.println("t1启动");
            for (int i = 0; i < 10; i++) {
                list.add(new Object());
                System.out.println("t1 add " + list.size());

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (list.size() == 5) {
                    // 打开门栓
                    countDownLatch.countDown();

                    // 不sleep不能保证打开门闩后t2继续执行,还可以采用俩个门闩
//                    try {
//                        TimeUnit.SECONDS.sleep(1);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }

                    // 直接调用t2.join也可以
                    try {
                        t2.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    try {
//                        // 把自己停在这
//                        countDownLatch1.await();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            }
            System.out.println("t1结束");
        });

        t2.start();
        t1.start();
    }
}
