package com._520.juc.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLock_tryLock {

    private ReentrantLock lock = new ReentrantLock();
//    private ReentrantLock lock = new ReentrantLock(true);     // 公平锁
    private void m1(){
        new Thread(() -> {
            System.out.println("t1 start");
            try{
                lock.lock();
                for (int i = 0; i < 10; i++) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(i);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                lock.unlock();
            }
            System.out.println("t1 end");
        }).start();
    }

    private void m2(){
        boolean locked = false;
        try {
            //locked = lock.tryLock();
            locked = lock.tryLock(1,TimeUnit.SECONDS);
            System.out.println("locked = " + locked);
            System.out.println("m2");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (locked)
            lock.unlock();
        }
    }
    public static void main(String[] args) {
        ReentrantLock_tryLock r = new ReentrantLock_tryLock();

        new Thread(r::m1).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(r::m2).start();




    }
}
