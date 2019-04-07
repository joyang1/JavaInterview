package cn.tommyyang.demo.lock;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author TommyYang on 2019-04-06
 */
public class Producer implements Runnable {

    public static LinkedList<Integer> data = new LinkedList<Integer>();
    public static boolean flag = false;

    private ReentrantLock lock;
    private Condition cCondition;
    private Condition pCondition;

    public Producer() {
    }

    public Producer(ReentrantLock lock, Condition cCondition, Condition pCondition) {
        this.lock = lock;
        this.cCondition = cCondition;
        this.pCondition = pCondition;
    }

    public void produce() {
        System.out.println(String.format("thread id:%d, name:%s start to produce data",
                Thread.currentThread().getId(), Thread.currentThread().getName()));
        data.add(1);
    }

    public void produce1() throws InterruptedException {
        synchronized (data) {
            while (flag){
                System.out.println("进入Producer wait");
                data.wait();
            }
            this.produce();
            flag = true;
            data.notifyAll();
        }

    }

    public void produce2() throws InterruptedException {
        try {
            this.lock.lock();
            while (flag){
                System.out.println("进入Producer await");
                this.pCondition.await();
            }
            this.produce();
            flag = true;
            this.cCondition.signalAll();
        } finally {
            this.lock.unlock();
        }

    }

    public void run() {
        for (; ; ) {
            try {
                this.produce1();
                //this.produce2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
