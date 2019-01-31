package volatilekey.ordering;

/**
 * @author TommyYang on 2019/1/30
 */
public class AThread implements Runnable {

    public void run() {
        System.out.println("in a thread1");
        Model.b = 4;
        System.out.println("in a thread2");
        Model.c = 5;
        System.out.println("in a thread3");
        Model.a = 11;
        System.out.println("in a thread4");
        Model.d = 6;
        System.out.println("in a thread5");
    }

}
