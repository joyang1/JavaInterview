package volatilekey;

/**
 * Created by TommyYang on 2018/3/19.
 */
public class ThreadCommunicate {


    public Counter counter = new Counter();

    public Counter getCounter() {
        return counter;
    }

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
