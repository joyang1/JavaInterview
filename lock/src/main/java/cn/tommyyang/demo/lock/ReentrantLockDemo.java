package cn.tommyyang.demo.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author TommyYang on 2019-03-21
 */
public class ReentrantLockDemo {

    public void newThread(final ReentrantLock lock, final String name){
        new Thread(new Runnable() {
            public void run() {
                lock.lock();
                System.out.println(name + ":第一次进入lock");
                try {
                    Thread.sleep(1000);
                    System.out.println(name + ":再次进入lock");
                    lock.lock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                    System.out.println(name + ":第一次unlock");
                    lock.unlock();
                    System.out.println(name + ":第二次unlock");
                }
            }
        }, name).start();
    }

}
