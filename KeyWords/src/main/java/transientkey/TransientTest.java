package transientkey;

import java.io.Serializable;

/**
 * Created by TommyYang on 2018/3/19.
 */
public class TransientTest implements Serializable{

    private static final long serialVersionUID = -2670851086407643335L;

    private transient int a = 1; //不会被序列化(持久化)，将该对象保存到磁盘中的时候，该属性不会被保存

    private int b = 8;

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }
}
