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

    public static void main(String[] args) {
        initData();

        POOL_SERVICE.execute(new PrintThread(QUEUE, 0));
        POOL_SERVICE.execute(new PrintThread(QUEUE, 1));
        POOL_SERVICE.execute(new PrintThread(QUEUE, 2));

        POOL_SERVICE.shutdown();

        while (true){
            if(POOL_SERVICE.isTerminated()){
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

}

class PrintThread implements Runnable {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition c = lock.newCondition();

    private static int count = 0;
    private LinkedList<Integer> queue;
    private Integer threadNo;

    public PrintThread(LinkedList<Integer> queue, Integer threadNo) {
        this.queue = queue;
        this.threadNo = threadNo;
    }

    @Override
    public void run() {
        while (true) {
            try {
                lock.lock();
                int mod = count % 3;
                if (mod != this.threadNo) {
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
                Integer val = this.queue.poll();
                System.out.println("thread-" + this.threadNo + ":" + val);
                count++;

                c.signal();

            } finally {
                lock.unlock();
            }
        }
    }
}