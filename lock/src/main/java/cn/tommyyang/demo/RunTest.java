package cn.tommyyang.demo;

import cn.tommyyang.demo.lock.SynchronizedDemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author TommyYang on 2019/1/28
 */
public class RunTest {

    private final static ExecutorService pool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        for(int i = 0; i < 2; i++){
            pool.execute(new SynchronizedDemo());
        }
        pool.shutdown();

        while (true){
            if(pool.isTerminated()){
                System.out.println(SynchronizedDemo.common);
                break;
            }
        }

    }

}
