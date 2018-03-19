package volatilekey;

/**
 * Created by TommyYang on 2018/3/19.
 */
public class ThreadCommunicate {

    static class Counter{

        private volatile int value;

        private int value1;

        public void increment(){
            value++;
            value1++;
        }

        public void decrement(){
            value--;
            value1--;
        }

        public int getValue() {
            return this.value;
        }

        public int getValue1() {
            return value1;
        }
    }

    private static Counter counter = new Counter();

     class IncrementTask implements Runnable{

        public void run() {
            for (int i=0; i < 1000; i++){
                counter.increment();
            }
            System.out.println("increment: "+counter.getValue());
        }
    }

     class DecrementTask implements Runnable{

        public void run() {
            for (int i=0; i < 1000; i++){
                counter.decrement();
            }
            System.out.println("decrement: "+counter.getValue());
        }
    }

}
