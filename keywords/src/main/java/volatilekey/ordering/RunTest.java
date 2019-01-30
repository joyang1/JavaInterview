package volatilekey.ordering;

/**
 * @author TommyYang on 2019/1/30
 */
public class RunTest {

    public static void main(String[] args) throws InterruptedException {

        for(int i = 0; i < 20; i++){
            Model m = new Model();
            Thread at = new Thread(new AThread(m));

            Thread bt = new Thread(new BThread(m));

            at.start();
            bt.start();

            System.out.println("main:" + m.getA());

//            bt.join();
//            at.join();

        }


    }

}
