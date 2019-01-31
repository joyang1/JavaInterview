package volatilekey.ordering;

/**
 * @author TommyYang on 2019/1/30
 */
public class BThread implements Runnable {

    public void run() {

        System.out.println("in b thread1");
        System.out.println(Model.a);
        System.out.println("in b thread2");
        System.out.println(Model.b);
        System.out.println("in b thread3");
        System.out.println(Model.c);
        System.out.println("in b thread4");
        System.out.println(Model.d);
        System.out.println("in b thread5");



    }

}
