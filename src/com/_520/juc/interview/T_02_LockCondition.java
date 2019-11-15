package com._520.juc.interview;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 *    写一个固定容量同步容器，拥有put和get方法，以及getCount方法，
 *    能够支持2个生产者线程以及10个消费者线程的阻塞调用
 *    使用Lock和Condition来实现
 *    对比两种方式，Condition的方式可以更加精确的指定哪些线程被唤醒
 */
public class T_02_LockCondition<T> {


    private LinkedList<T> list = new LinkedList<>();
    private final static Integer MAX = 10;      // 最多十个消费者
    private static Integer count = 0;

    private Lock lock = new ReentrantLock();
    // 生产者锁
    private Condition prodector = lock.newCondition();
    // 消费者锁
    private Condition consumer = lock.newCondition();

    // 生产者
    private void put(T t){
        // 消费者的最大消费数量
        try{
            lock.lock();
            while (list.size() == MAX){
                // 生产者阻塞
                prodector.await();
            }
            System.out.println(t);
            list.add(t);
            count++;
            // 唤醒其余所有的消费者线程
            consumer.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    private  T get(){
        T t = null;
        try{
            lock.lock();
            // 没有东西供消费者消费
            while (list.size() == 0){
                // 消费者阻塞
                consumer.await();
            }
            t = list.removeFirst();
            count--;

            // 唤醒其余所有的生产者线程
            prodector.signalAll();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return t;
    }

    private int getCount(){
        return count;
    }
    public static void main(String[] args) {
        T_02_LockCondition<String> t = new T_02_LockCondition<>();

        // 消费者
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    System.out.println(Thread.currentThread().getName() + " " + t.get());
                }

            },"消费者" + i).start();
        }

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 生产者
        for (int i = 1; i <= 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 25; j++) {
                    t.put(Thread.currentThread().getName() + "  " + j);
                    System.out.println(t.getCount());
                }
            },"生产者" + i).start();
        }

    }
}
