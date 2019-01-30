package volatilekey.unsafe;

import sun.misc.Unsafe;

/**
 * @author TommyYang on 2019/1/28
 */
public class Counter{

    private final Long serialVersionUID = 0L;

    private static final Unsafe unsafe = OwnUnSafe.getUnSafe();
    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                    (Counter.class.getDeclaredField("value"));
        } catch (Exception ex) { throw new Error(ex); }
    }

    private volatile int value;

    public Counter(int value) {
        this.value = value;
    }

    public final void increment(){
        for(;;){
            int current = getValue();
            int next = current + 1;
            if(compareAndSet(current, next)){
                return;
            }
        }
    }

    public final void decrement(){
        for(;;){
            int current = getValue();
            int next = current - 1;
            if(compareAndSet(current, next)){
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

}
