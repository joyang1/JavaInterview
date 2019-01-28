package cn.tommyyang.demo.lock;

/**
 * @author TommyYang on 2019/1/28
 */
public class SynchronizedDemo implements Runnable {

    public static int common = 0;

    public void run() {
        for(int j=0;j<1000000;j++){
            this.add();
        }
    }

    public synchronized void add(){
        synchronized (this){
            common++;
        }
    }

}
