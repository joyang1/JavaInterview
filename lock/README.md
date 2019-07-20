# Lock 篇
其实本来 Lock 应该放到 data-structure 模块的，但是为了体现其重要性，
单独用一个篇章来整理。

## synchronized 与 Lock interface
Java 中两种实现加锁的方式：一种是使用 synchronized 关键字，另一种是使用 Lock 接口的实现类。

在一篇文章中看到一个好的对比，非常形象，synchronized 关键字就像是**自动挡**，可以满足一切的驾驶需求。
但是如果你想要做更高级的操作，比如玩漂移或者各种高级的骚操作，那么就需要**手动挡**，也就是 Lock 接口的实现类。

而 synchronized 在经过 Java 每个版本的各种优化后，效率也变得很高了。只是使用起来没有 Lock 接口的实现类那么方便。

### synchronized 锁升级过程就是其优化的核心：**偏向锁** -> **轻量级锁** -> **重量级锁**

```java

class Test{
    private static final Object object = new Object(); 
    
    public void test(){
        synchronized(object) {
            // do something        
        }   
    }
    
}

```

使用 synchronized 关键字锁住某个代码块的时候，一开始锁对象（就是上述代码中的 object）并不是**重量级锁**，而是偏向锁。
偏向锁的字面意思就是"偏向于第一个获取它的线程"的锁。线程执行完同步代码块之后，并**不会主动释放偏向锁**。当第二次到达同步
代码块时，线程会判断此时持有锁的线程是否就是自己（持有锁的线程 ID 在对象头里存储），如果是则正常往下执行。**由于之前没有释放，
这里就不需要重新加锁**，如果从头到尾都是一个线程在使用锁，很明显偏向锁几乎没有额外开销，性能极高。

一旦有第二个线程加入**锁竞争**，偏向锁


 