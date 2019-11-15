package com._520.juc.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatch_await_countdown {

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private void m1(){
        System.out.println("t1 start");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("t1 end");
    }

    private void m2(){
        System.out.println("m2");
        countDownLatch.countDown();
    }

    private long getCount(){
        // 返回当前拿到锁的对象个数
        return countDownLatch.getCount();
    }
    public static void main(String[] args) {
        CountDownLatch_await_countdown c = new CountDownLatch_await_countdown();

        System.out.println(c.getCount());
        new Thread(c::m1).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(c::m2).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(c.getCount());

    }
}
