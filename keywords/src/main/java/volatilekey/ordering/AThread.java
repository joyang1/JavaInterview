package volatilekey.ordering;

/**
 * @author TommyYang on 2019/1/30
 */
public class AThread implements Runnable {

    private Model model;

    public AThread(Model model) {
        this.model = model;
    }

    public void run() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        model.setA(11);
    }

}
