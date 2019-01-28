package volatilekey;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by TommyYang on 2018/3/19.
 */
public class Runtest {

    public static void main(String[] args) throws InterruptedException {
        ThreadCommunicate th = new ThreadCommunicate();
        Thread incrementTask = new Thread(th.new IncrementTask());
        Thread decrementTask = new Thread(th.new DecrementTask());
        incrementTask.start();
        decrementTask.start();
        incrementTask.join(); //让主线程等待子线程结束之后才能继续运行。
        decrementTask.join();
        System.out.println(th.getCounter().getValue());
    }

}
