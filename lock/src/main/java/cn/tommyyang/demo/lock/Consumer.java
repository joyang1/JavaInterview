package cn.tommyyang.demo.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author TommyYang on 2019-04-06
 */
public class Consumer implements Runnable {

    private ReentrantLock lock;
    private Condition cCondition;
    private Condition pCondition;

    public Consumer() {
    }

    public Consumer(ReentrantLock lock, Condition cCondition, Condition pCondition) {
        this.lock = lock;
        this.cCondition = cCondition;
        this.pCondition = pCondition;
    }

    public void consume1() throws InterruptedException {
        synchronized (Producer.data) {
            while (!Producer.flag){
                System.out.println("进入Consumer wait");
                Producer.data.wait();
            }
            this.consume();
            Producer.flag = false;
            Producer.data.notifyAll();
        }

    }

    public void consume2() throws InterruptedException {
        try {
            this.lock.lock();
            while (!Producer.flag){
                System.out.println("进入Consumer await");
                this.cCondition.await();
            }
            this.consume();
            Producer.flag = false;
            this.pCondition.signalAll();
        }  finally {
            this.lock.unlock();
        }



    }

    public void consume(){
        System.out.println(String.format("thread id:%d, name:%s start to consume data",
                Thread.currentThread().getId(), Thread.currentThread().getName()));
        if (Producer.data.size() > 0) {
            System.out.println(String.format("consume value: %d", Producer.data.remove()));
        } else {
            System.out.println("no data");
        }
    }

    public void run() {

        for (; ; ) {
            try {
                this.consume1();
                //this.consume2();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
