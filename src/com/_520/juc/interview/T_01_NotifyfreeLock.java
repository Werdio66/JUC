package com._520.juc.interview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
    实现一个容器，提供两个方法，add，size
    写两个线程，线程1添加10个元素到容器中，线程2实现监控元素的个数，当个数到5个时，线程2给出提示并结束
 */
public class T_01_NotifyfreeLock {

    public static void main(String[] args) {
        // 定义一个集合存储对象
        List<Object> list = new ArrayList<>();

        // 声明锁
        final Object lock = new Object();

        Thread t2 = new Thread(() -> {
            synchronized (lock){
                System.out.println("t2启动");
                try {
                    // 阻塞
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("t2结束");
                // 唤醒t1，不然t1会一直阻塞下去
                lock.notify();
            }
        });

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread t1 = new Thread(() -> {
            synchronized (lock){
                System.out.println("t1启动");
                for (int i = 0; i < 10; i++) {
                    list.add(new Object());
                    System.out.println("t1 add " + list.size());

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (list.size() == 5){
                        // notify不会释放锁
                        lock.notify();

                        try {
                            // 释放锁，使t2可以执行，自己等待
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("t1结束");
            }
        });

        t2.start();
        t1.start();



    }
}
