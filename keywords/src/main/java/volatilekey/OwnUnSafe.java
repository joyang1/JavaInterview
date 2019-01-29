package volatilekey;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author TommyYang on 2019/1/29
 */
public class OwnUnSafe {

    public static Unsafe getUnSafe() {
        try {
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
            boolean orignialAccessible = theUnsafeField.isAccessible();
            theUnsafeField.setAccessible(true);
            Object unsafeInstance = theUnsafeField.get(null); //unsafeInstance就是Unsafe的实例
            theUnsafeField.setAccessible(orignialAccessible);
            return (Unsafe)unsafeInstance;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


}
