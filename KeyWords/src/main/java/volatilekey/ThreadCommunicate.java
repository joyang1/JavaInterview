package volatilekey;

import java.util.Random;

/**
 * Created by TommyYang on 2018/3/19.
 */
public class ThreadCommunicate {

    static class Counter{

        private volatile int value;

        private volatile int value1;

        public void increment(){
            value = value + 1;
            value1 = value;
        }

        public void decrement(){
            value = value - 1;
            value1 = value;
        }

        public int getValue1(){
            return value1;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static Counter counter = new Counter();

     class IncrementTask implements Runnable{

        public void run() {
            for (int i=0; i < 100000; i++){
                counter.increment();
                //System.out.println("increment: "+counter.getValue());
            }

        }
    }

     class DecrementTask implements Runnable{

        public void run() {
            for (int i=0; i < 100000; i++){
                counter.decrement();
                //System.out.println("decrement: "+counter.getValue());
            }

        }
    }

}
