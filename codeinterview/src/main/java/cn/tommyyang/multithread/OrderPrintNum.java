package cn.tommyyang.multithread;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author : TommyYang
 * @Time : 2019-06-25 23:20
 * @Software: IntelliJ IDEA
 * @File : OrderPrintNum.java
 */
public class OrderPrintNum {

    private static final LinkedList<Integer> QUEUE = new LinkedList();
    private static final ExecutorService POOL_SERVICE = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws InterruptedException {

//        runPrint2();

        initData();

        POOL_SERVICE.execute(new PrintThread1(QUEUE, 2));
        POOL_SERVICE.execute(new PrintThread1(QUEUE, 1));
        POOL_SERVICE.execute(new PrintThread1(QUEUE, 0));

        POOL_SERVICE.shutdown();

        while (true) {
            if (POOL_SERVICE.isTerminated()) {
                System.out.println("finished!!");
                break;
            }
        }
    }

    private static void initData() {
        for (int i = 0; i <= 100; i++) {
            QUEUE.add(i);
        }
    }


    // 方案二
    private static void runPrint2() {
        POOL_SERVICE.execute(new PrintThread2(0));
        POOL_SERVICE.execute(new PrintThread2(1));
        POOL_SERVICE.execute(new PrintThread2(2));

        POOL_SERVICE.shutdown();

        while (true) {
            if (POOL_SERVICE.isTerminated()) {
                System.out.println("finished!!");
                break;
            }
        }
    }
}

class PrintThread1 implements Runnable {
    private static final Object LOCK = new Object();

    private static int count = 0; // 计数，同时确定线程是否要加入等待队列，还是可以直接去资源队列里面去获取数据进行打印
    private LinkedList<Integer> queue;
    private Integer threadNo;

    public PrintThread1(LinkedList<Integer> queue, Integer threadNo) {
        this.queue = queue;
        this.threadNo = threadNo;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (LOCK) {
                while (count % 3 != this.threadNo) {
                    if (count >= 101) {
                        break;
                    }
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (count >= 101) {
                    break;
                }

                Integer val = this.queue.poll();
                System.out.println("thread-" + this.threadNo + ":" + val);
                count++;

                LOCK.notifyAll();
            }
        }
    }
}

class PrintThread2 implements Runnable {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition c = lock.newCondition();

    private static int count = 0; //作为计数，同时也作为资源；因为这道题目是自然数作为资源，所以正好可以公用；
    private Integer threadNo;

    public PrintThread2(Integer threadNo) {
        this.threadNo = threadNo;
    }

    @Override
    public void run() {
        while (true) {
            try {
                lock.lock();
                while (count % 3 != this.threadNo) {
                    if (count >= 101) {
                        break;
                    }
                    try {
                        c.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (count >= 101) {
                    break;
                }
                System.out.println("thread-" + this.threadNo + ":" + count);
                count++;

                c.signalAll();

            } finally {
                lock.unlock();
            }
        }
    }
}