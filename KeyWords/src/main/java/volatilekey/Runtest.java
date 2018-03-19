package volatilekey;

/**
 * Created by TommyYang on 2018/3/19.
 */
public class Runtest {

    public static void main(String[] args) throws InterruptedException {
        Thread incrementTask = new Thread( new ThreadCommunicate().new IncrementTask());
        Thread decrementTask = new Thread(new ThreadCommunicate().new DecrementTask());
        incrementTask.start();
        decrementTask.start();
        incrementTask.join(); //让主线程等待子线程结束之后才能继续运行。
        decrementTask.join();
        System.out.println(ThreadCommunicate.counter.getValue());
        System.out.println(ThreadCommunicate.counter.getValue1());
    }

}
