package com._520.juc.interview;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *  写一个固定容量同步容器，拥有put和get方法，以及getCount方法，
 *  能够支持2个生产者线程以及10个消费者线程的阻塞调用
 *
 *   使用wait和notify/notifyAll来实现
 */
public class T_02<T> {

    private List<T> list = new LinkedList<>();
    private final static Integer MAX = 10;      // 最多十个消费者
    private static Integer count = 0;

    // 生产者
    private synchronized void put(T t){
        // 消费者的最大消费数量
        while (list.size() == MAX){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(t);
        list.add(t);
        count++;

        // 唤醒其余所有的线程（生产者和消费者）
        this.notifyAll();
    }

    private synchronized T get(){
        T t = null;
        // 没有东西供消费者消费
        while (list.size() == 0){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        t = list.get(0);
        list.remove(0);
        count--;

        // 唤醒其余所有的线程（生产者和消费者）
        this.notifyAll();
        return t;
    }

    private int getCount(){
        return count;
    }
    public static void main(String[] args) {
        T_02<String> t_02 = new T_02<>();

        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    System.out.println(Thread.currentThread().getName() + " " + t_02.get());
                }

            },"消费者" + i).start();
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 1; i <= 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    t_02.put(Thread.currentThread().getName() + "" + j);
                }
            },"生产者" + i).start();
        }


    }
}

