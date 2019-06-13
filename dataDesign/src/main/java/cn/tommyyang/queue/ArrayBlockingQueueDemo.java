package cn.tommyyang.queue;

/**
 * @Author : TommyYang
 * @Time : 2019-06-13 18:23
 * @Software: IntelliJ IDEA
 * @File : ArrayBlockingQueueDemo.java
 */

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用 ArrayBlockingQueue 实现的生产者消费者简单模型
 */
public class ArrayBlockingQueueDemo {

    private final static ExecutorService THREAD_POOL = Executors.newFixedThreadPool(4);
    private final static ArrayBlockingQueue<Data> QUEUE = new ArrayBlockingQueue<>(1);

    public static void main(String[] args) {
        THREAD_POOL.execute(new Producer(QUEUE));
        THREAD_POOL.execute(new Producer(QUEUE));
        THREAD_POOL.execute(new Consumer(QUEUE));
        THREAD_POOL.execute(new Consumer(QUEUE));
        THREAD_POOL.shutdown();
    }
}

class Data {

}

class Producer implements Runnable {

    private final ArrayBlockingQueue<Data> mAbq;

    public Producer(ArrayBlockingQueue<Data> mAbq) {
        this.mAbq = mAbq;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            produce();
        }

    }

    private void produce() {
        try {
            Data data = new Data();
            this.mAbq.put(data);
            System.out.println("生产了数据@" + data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {

    private final ArrayBlockingQueue<Data> mAbq;

    public Consumer(ArrayBlockingQueue<Data> mAbq) {
        this.mAbq = mAbq;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            consumer();
        }
    }

    private void consumer() {
        try {
            Data data = this.mAbq.take();
            System.out.println("消费数据-" + data);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


