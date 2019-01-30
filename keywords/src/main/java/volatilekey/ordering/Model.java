package volatilekey.ordering;

/**
 * @author TommyYang on 2019/1/30
 */
public class Model {

    private volatile int a = 10;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

}
