package cn.tommyyang.streamapi;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author : TommyYang
 * @Time : 2019-08-06 16:58
 * @Software: IntelliJ IDEA
 * @File : CountService.java
 */
public class CountService {

    private long a;
    private AtomicLong atomicLong = new AtomicLong(0);

    public void count(int b) {
        this.a = a + b;
    }

    public void atomicCount(int b) {
        this.atomicLong.addAndGet(b);
    }

    public long getA() {
        return a;
    }

    public long getCount() {
        return this.atomicLong.get();
    }

}
