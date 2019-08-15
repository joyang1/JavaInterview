# Lock 篇
其实本来 Lock 应该放到 data-structure 模块的，但是为了体现其重要性，
单独用一个篇章来整理。

# 锁的分类介绍

## 乐观锁与悲观锁
锁的一种宏观分类是**乐观锁**与**悲观锁**。乐观锁与悲观锁并不是特定的指哪个锁（Java 中也没有那个具体锁的实现名就叫
乐观锁或悲观锁），而是在并发情况下两种不同的策略。

乐观锁（Optimistic Lock）就是很乐观，每次去拿数据的时候都认为别人不会修改。所以不会上锁。但是如果想要更新数据，
则会在**更新之前检查在读取至更新这段时间别人有没有修改过这个数据**。如果修改过，则重新读取，再次尝试更新，循环上述
步骤直到更新成功（当然也允许更新失败的线程放弃更新操作）。

悲观锁（Pessimistic Lock）就是很悲观，每次去拿数据的时候都认为别人会修改。所以每次都在拿数据的时候上锁。
这样别人拿数据的时候就会被挡住，直到悲观锁释放，想获取数据的线程再去获取锁，然后再获取数据。

**悲观锁阻塞事务，乐观锁回滚重试**，它们个有优缺点，没有好坏之分，只有适应场景的不同区别。比如：乐观锁适合用于写
比较少的情况下，即冲突真的很少发生的场景，这样可以省去锁的开销，加大了系统的整个吞吐量。但是如果经常产生冲突，上层
应用会不断的进行重试，这样反而降低了性能，所以这种场景悲观锁比较合适。
总结：**乐观锁适合写比较少，冲突很少发生的场景；而写多，冲突多的场景适合使用悲观锁**。

## 乐观锁的基础 --- CAS
在乐观锁的实现中，我们必须要了解的一个概念：CAS。

什么是 CAS 呢？ Compare-and-Swap，即**比较并替换**，或者**比较并设置**。

- 比较：读取到一个值 A，在将其更新为 B 之前，检查原值是否为 A（未被其它线程修改过，**这里忽略 ABA 问题**）。

- 替换：如果是，更新 A 为 B，结束。如果不是，则不会更新。

上面两个步骤都是原子操作，可以理解为瞬间完成，在 CPU 看来就是一步操作。

有了 CAS，就可以实现一个乐观锁：

```java

public class OptimisticLockSample{
    
    public void test(){
        int data = 123; // 共享数据
        
        // 更新数据的线程会进行如下操作
        for (;;) {
            int oldData = data;
            int newData = doSomething(oldData);
            
            // 下面是模拟 CAS 更新操作，尝试更新 data 的值
            if (data == oldData) { // compare
                data = newData; // swap
                break; // finish
            } else {
                // 什么都不敢，循环重试
            }
        }   
    }
    
    /**
    * 
    * 很明显，test() 里面的代码根本不是原子性的，只是展示了下 CAS 的流程。
    * 因为真正的 CAS 利用了 CPU 指令。
    *  
    * */ 
    

}

```

在 Java 中也是通过 native 方法实现的 CAS。

```java

public final class Unsafe {
    
    ...
    
    public final native boolean compareAndSwapObject(Object var1, long var2, Object var4, Object var5);
    
    public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);
    
    public final native boolean compareAndSwapLong(Object var1, long var2, long var4, long var6);  
    
    ...
} 


```

上面写了一个简单直观的乐观锁（确切的来说应该是乐观锁流程）的实现，它允许多个线程同时读取（因为根本没有加锁操作），如果更新数据的话，
有且仅有一个线程可以成功更新数据，并导致其它线程需要回滚重试。CAS 利用 CPU 指令，从硬件层面保证了原子性，以达到类似于锁的效果。

从乐观锁的整个流程中可以看出，并没有**加锁**和**解锁**的操作，因此乐观锁策略也被称作为**无锁编程**。换句话说，乐观锁其实不是"锁"，
它仅仅是一个循环重试的 CAS 算法而已。


## 自旋锁

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

一旦有第二个线程加入**锁竞争**，偏向锁转换为**轻量级锁**（**自旋锁**）。锁竞争：如果多个线程轮流获取一个锁，但是每次获取的时候
都很顺利，没有发生阻塞，那么就不存在锁竞争。只有当某线程获取锁的时候，发现锁已经被占用，需要等待其释放，则说明发生了锁竞争。

在轻量级锁状态上继续锁竞争，没有抢到锁的线程进行**自旋**操作，即在一个循环中不停判断是否可以获取锁。获取锁的操作，就是通过 CAS 操
作修改对象头里的锁标志位。先**比较**当前锁标志位是否为**释放**状态，如果是，将其设置为**锁定**状态，比较并设置是原子性操作，这个
是 JVM 层面保证的。当前线程就算持有了锁，然后线程将当前锁的持有者信息改为自己。

假如我们获取到锁的线程操作时间很长，比如会进行复杂的计算，数据量很大的网络传输等；那么其它等待锁的线程就会进入长时间的自旋操作，这个
过程是非常耗资源的。其实这时候相当于只有一个线程在有效地工作，其它的线程什么都干不了，在白白地消耗 CPU，这种现象叫做**忙等
（busy-waiting）**。所以如果多个线程使用**独占锁**，但是没有发生锁竞争，或者发生了很轻微的锁竞争，那么 synchronized 就是轻量
级锁，允许短时间的忙等现象。这是一种择中的想法，**短时间的忙等，换取线程在用户态和内核态之间切换的开销**。

显然，忙等是有限度的（JVM 有一个计数器记录自旋次数，默认允许循环 10 次，可以通过[虚拟机参数更改](#参数介绍)）。如果锁竞争情况严重，
达到某个最大自旋次数的线程，会将轻量级锁升级为重量级锁（依然是通过 CAS 修改锁标志位，但不修改持有锁的线程 ID）。当后续线程尝试获取
锁时，发现被占用的锁是重量级锁，则直接将自己挂起（而不是上面说的忙等，即不会自旋），等待释放锁的线程去唤醒。在 JDK1.6 之前， synchronized
直接加重量级锁，很明显现在通过一系列的优化过后，性能明显得到了提升。

JVM 中，synchronized 锁只能按照偏向锁、轻量级锁、重量级锁的顺序逐渐升级（也有把这个称为**锁膨胀**的过程），不允许降级。

## 可重入锁（递归锁）
可重入锁的字面意思是"可以重新进入的锁"，即**允许同一个线程多次获取同一把锁**。比如一个递归函数里有加锁操作，递归函数里这个锁会阻塞自己么？
如果不会，那么这个锁就叫可重入锁（因为这个原因可重入锁也叫做**递归锁**）。

Java 中以 Reentrant 开头命名的锁都是可重入锁，而且 **JDK 提供的所有现成 Lock 的实现类，包括 synchronized 关键字锁都是可重入的**。
如果真的需要不可重入锁，那么就需要自己去实现了，获取去网上搜索一下，有很多，自己实现起来也很简单。

如果不是可重入锁，在递归函数中就会造成死锁，所以 Java 中的锁基本都是可重入锁，不可重入锁的意义不是很大，我暂时没有想到什么场景下会用到；
**注意：有想到需要不可重入锁场景的小伙伴们可以留言一起探讨**。

下图展示一下 Lock 的相关实现类：
<img src="https://blog.tommyyang.cn/img/java/lock/ilock.png">


## 公平锁和非公平锁
如果多个线程申请一把**公平锁**，那么获得锁的线程释放锁的时候，先申请的先得到，很公平。如果是**非公平锁**，后申请的线程可能先获得锁，是
随机获取还是其它方式，都是根据实现算法而定的。

对 ReentrantLock 类来说，通过构造函数可以**指定该锁是否是公平锁，默认是非公平锁**。因为在大多数情况下，非公平锁的吞吐量比公平锁的大，
如果没有特殊要求，优先考虑使用非公平锁。

而对于 synchronized 锁而言，它只能是一种非公平锁，没有任何方式使其变成公平锁。这也是 ReentrantLock 相对于 synchronized 锁的一个
优点，更加的灵活。

以下是 ReentrantLock 构造器代码：

``` java

/**
 * Creates an instance of {@code ReentrantLock} with the
 * given fairness policy.
 *
 * @param fair {@code true} if this lock should use a fair ordering policy
 */
public ReentrantLock(boolean fair) {
    sync = fair ? new FairSync() : new NonfairSync();
}

```

ReentrantLock 内部实现了 FairSync 和 NonfairSync 两个内部类来实现公平锁和非公平锁。具体源码分析会在接下来的章节给出，敬请关注
该项目，欢迎 fork 和 star。


## 可中断锁
字面意思是"可以**响应中断**的锁"。

首先，我们需要理解的是什么是**中断**。 Java 中并没有提供任何可以直接中断线程的方法，只提供了**中断机制**。那么何为**中断机制**呢？
线程 A 向线程 B 发出"请你停止运行"的请求，就是调用 Thread.interrupt() 的方法（当然线程 B 本身也可以给自己发送中断请求，
即 Thread.currentThread().interrupt()），但线程 B 并不会立即停止运行，而是自行选择在合适的时间点以自己的方式响应中断，也可以
直接忽略此中断。也就是说，Java 的**中断不能直接终止线程**，只是设置了状态为响应中断的状态，需要被中断的线程自己决定怎么处理。这就像
在读书的时候，老师在晚自习时叫学生自己复习功课，但学生是否复习功课，怎么复习功课则完全取决于学生自己。

回到锁的分析上来，如果线程 A 持有锁，线程 B 等待持获取该锁。由于线程 A 持有锁的时间过长，线程 B 不想继续等了，我们可以让线程 B 中断
自己或者在别的线程里面中断 B，这种就是 **可中段锁**。

在 Java 中， synchronized 锁是**不可中断锁**，而 Lock 的实现类都是 **可中断锁**。从而可以看出 JDK 自己实现的 Lock 锁更加的
灵活，这也就是有了 synchronized 锁后，为什么还要实现那么些 Lock 的实现类。

Lock 接口的相关定义：

```java

public interface Lock {

    void lock();

    void lockInterruptibly() throws InterruptedException;

    boolean tryLock();
    
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    
    void unlock();

    Condition newCondition();
}

```

其中 lockInterruptibly 就是获取可中断锁。

## 共享锁
字面意思是多个线程可以共享一个锁。一般用共享锁都是在读数据的时候，比如我们可以允许 10 个线程同时读取一份共享数据，这时候我们
可以设置一个有 10 个凭证的共享锁。

在 Java 中，也有具体的共享锁实现类，比如 Semaphore。 该类的源码分析会在后续章节进行分析，敬请关注该项目，欢迎 fork 和 star。

## 互斥锁
字面意思是线程之间互相排斥的锁，也就是表明锁只能被一个线程拥有。

在 Java 中， ReentrantLock、synchronized 锁都是互斥锁。

## 读写锁
读写锁其实是一对锁，一个读锁（共享锁）和一个写锁（互斥锁、排他锁）。

在 Java 中， ReadWriteLock 接口只规定了两个方法，一个返回读锁，一个返回写锁。

```java

public interface ReadWriteLock {
    /**
     * Returns the lock used for reading.
     *
     * @return the lock used for reading
     */
    Lock readLock();

    /**
     * Returns the lock used for writing.
     *
     * @return the lock used for writing
     */
    Lock writeLock();
}

```

文章前面讲过[乐观锁策略](#乐观锁的基础 --- CAS)，所有线程可以随时读，仅在写之前判断值有没有被更改。

读写锁其实做的事情是一样的，但是策略稍有不同。很多情况下，线程知道自己读取数据后，是否是为了更改它。那么为何不在加锁的时候直接明确
这一点呢？如果我读取值是为了更新它（SQL 的 for update 就是这个意思），那么加锁的时候直接加**写锁**，我持有写锁的时候，别的线程
无论是读还是写都需要等待；如果读取数据仅仅是为了前端展示，那么加锁时就明确加一个**读锁**，其它线程如果也要加读锁，不需要等待，可以
直接获取（读锁计数器加 1）。

虽然读写锁感觉与乐观锁有点像，但是**读写锁是悲观锁策略**。因为读写锁并没有在**更新前**判断值有没有被修改过，而是在**加锁前**决定
应该用读锁还是写锁。乐观锁特指无锁编程。

JDK 内部提供了一个唯一一个 ReadWriteLock 接口实现类是 ReentrantReadWriteLock。通过名字可以看到该锁提供了读写锁，并且也是
可重入锁。

## 总结
Java 中使用的各种锁基本都是**悲观锁**，那么 Java 中有乐观锁么？结果是肯定的，那就是 java.util.concurrent.atomic 下面的
原子类都是通过乐观锁实现的。如下：

``` java

public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

    return var5;
}

```

通过上述源码可以发现，在一个循环里面不断 CAS，直到成功为止。

## 参数介绍
```
-XX:-UseBiasedLocking=false 关闭偏向锁


JDK1.6 

-XX:+UseSpinning 开启自旋锁

-XX:PreBlockSpin=10 设置自旋次数 

JDK1.7 之后 去掉此参数，由 JVM 控制


```





 