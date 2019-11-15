package com._520.juc.thread;

import java.lang.invoke.VarHandle;
import java.util.concurrent.locks.ReentrantLock;

public class CreatThread {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
    }
}
