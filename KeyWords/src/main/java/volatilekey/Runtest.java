package volatilekey;

/**
 * Created by TommyYang on 2018/3/19.
 */
public class Runtest {

    public static void main(String[] args) throws InterruptedException {
//        Thread incrementTask = new Thread(new ThreadCommunicate().new IncrementTask());
//        Thread decrementTask = new Thread(new ThreadCommunicate().new DecrementTask());
//        incrementTask.start();
//        decrementTask.start();
//        incrementTask.join();
//        decrementTask.join();
//        System.out.println(new ThreadCommunicate().new Counter().getValue());

        Thread incrementTask = new Thread( new ThreadCommunicate().new IncrementTask());
        Thread decrementTask = new Thread(new ThreadCommunicate().new DecrementTask());
        incrementTask.start();
        decrementTask.start();
        incrementTask.join();
        decrementTask.join();
        System.out.println(new ThreadCommunicate.Counter().getValue());
        System.out.println(new ThreadCommunicate.Counter().getValue1());

    }

}
