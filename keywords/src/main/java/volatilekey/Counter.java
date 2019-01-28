package volatilekey;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author TommyYang on 2019/1/28
 */
public class Counter{

    private final Long serialVersionUID = 0L;

    private static final Unsafe unsafe = getUnSafe();
    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                    (Counter.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    private volatile int value;

    private final void setValue(int value){
        this.value = value;
    }

    public final void increment(){
        for(;;){
            int current = getValue();
            int next = current + 1;
            if(compareAndSet(current, next)){
                setValue(next);
                return;
            }
        }
    }

    public final void decrement(){
        for(;;){
            int current = getValue();
            int next = current - 1;
            if(compareAndSet(current, next)){
                setValue(next);
                return;
            }
        }
    }

    public final int getValue() {
        return this.value;
    }

    public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }

    private static Unsafe getUnSafe() {
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
