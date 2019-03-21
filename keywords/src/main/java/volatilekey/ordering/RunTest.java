package volatilekey.ordering;

/**
 * @author TommyYang on 2019/1/30
 */
public class RunTest {

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 10000; i++){
            Thread at = new Thread(new AThread());
            Thread bt = new Thread(new BThread());


            at.start();
            Thread.sleep(1);
            bt.start();

            bt.join();
            at.join();
        }



    }

}
