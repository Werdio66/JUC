package com._520.juc.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLock_Lock_unLock {
    private ReentrantLock lock = new ReentrantLock();
    void m1(){
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

    void m2(){
        try {
            lock.lock();
            System.out.println("m2");

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public static void main(String[] args) {
        ReentrantLock_Lock_unLock r = new ReentrantLock_Lock_unLock();

        new Thread(r::m1).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(r::m2).start();




    }
}
