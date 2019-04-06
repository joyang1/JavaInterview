package cn.tommyyang.demo.lock;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author TommyYang on 2019-04-06
 */
public class Producer implements Runnable {

    public static LinkedList<Integer> data = new LinkedList<Integer>();

    private ReentrantLock lock;
    private Condition cCondition;

    public Producer() {
    }

    public Producer(ReentrantLock lock, Condition cCondition) {
        this.lock = lock;
        this.cCondition = cCondition;
    }

    public void produce() {
        System.out.println(String.format("thread id:%d, name:%s start to produce data",
                Thread.currentThread().getId(), Thread.currentThread().getName()));
        data.add(1);
    }

    public void produce1() {
        synchronized (data) {
            this.produce();
            data.notifyAll();
        }

    }

    public void produce2() {
        try {
            this.lock.lock();
            this.produce();
            this.cCondition.signalAll();
        } finally {
            this.lock.unlock();
        }
    }

    public void run() {
        for (; ; ) {
            //this.produce1();
            this.produce2();

        }
    }
}
