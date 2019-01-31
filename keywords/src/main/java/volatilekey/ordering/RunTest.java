package volatilekey.ordering;

/**
 * @author TommyYang on 2019/1/30
 */
public class RunTest {

    public static void main(String[] args) throws InterruptedException {


        Thread at = new Thread(new AThread());
        Thread bt = new Thread(new BThread());

        bt.start();
        at.start();

        bt.join();
        at.join();


    }

}
