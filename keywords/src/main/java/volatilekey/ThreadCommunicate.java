package volatilekey;

import sun.misc.Unsafe;

/**
 * Created by TommyYang on 2018/3/19.
 */
public class ThreadCommunicate {


    Counter counter = new Counter(0);

    public Counter getCounter() {
        return counter;
    }

    class IncrementTask implements Runnable {

        public void run() {
            for(int i = 0; i < 100000; i++){
                counter.increment();
            }

        }
    }

    class DecrementTask implements Runnable {

        public void run() {
            for(int i = 0; i < 100000; i++){
                counter.decrement();
            }
        }
    }

}
