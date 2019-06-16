# BlockingQueue 和 BlockingDeque

## BlockingQueue 介绍
`BlockingQueue` 继承自  `Queue` 接口,下面看看阻塞队列提供的接口；
```java
public interface BlockingQueue<E> extends Queue<E> {
    /**
     * 插入数据到队列尾部（如果立即可行且不会超过该队列的容量）
     * 在成功时返回 true，如果此队列已满，则抛IllegalStateException。(与offer方法的区别)
     */
    boolean add(E e);

    /**
     * 插入数据到队列尾部，如果没有空间，直接返回false;
     * 有空间直接插入，返回true。
     */
    boolean offer(E e);

    /**
     * 插入数据到队列尾部，如果队列没有空间，一直阻塞；
     * 有空间直接插入。
     */
    void put(E e) throws InterruptedException;

    /**
     * 插入数据到队列尾部，如果没有额外的空间，等待一定的时间，有空间即插入，返回true，
     * 到时间了，还是没有额外空间，返回false。
     */
    boolean offer(E e, long timeout, TimeUnit unit)
        throws InterruptedException;

    /**
     * 取出和删除队列中的头元素，如果没有数据，会一直阻塞到有数据
     */
    E take() throws InterruptedException;

    /**
     * 取出和删除队列中的头元素，如果没有数据，需要会阻塞一定的时间，过期了还没有数据，返回null
     */
    E poll(long timeout, TimeUnit unit)
        throws InterruptedException;
    
    //除了上述方法还有继承自Queue接口的方法 
    /**
     * 取出和删除队列头元素，如果是空队列直接返回null。
     */
    E poll();

    /**
     * 取出但不删除头元素，该方法与peek方法的区别是当队列为空时会抛出NoSuchElementException异常
     */
    E element();

    /**
     * 取出但不删除头元素，空队列直接返回null
     */
    E peek();

    /**
     * 返回队列总额外的空间
     */
    int remainingCapacity();

    /**
     * 删除队列中存在的元素
     */
    boolean remove(Object o);

   /**
    * 判断队列中是否存在当前元素
    */
    boolean contains(Object o);

}
```

- 插入方法

`add(E e)`: 添加成功返回true，失败抛IllegalStateException异常

`offer(E e)`: 成功返回 true，如果此队列已满，则返回 false。

`put(E e)`: 将元素插入此队列的尾部，如果该队列已满，则一直阻塞

- 删除方法

`remove(Object o)`: 移除指定元素,成功返回true，失败返回false

`poll()`: 获取并移除此队列的头元素，若队列为空，则返回 null

`take()`: 获取并移除此队列的头元素，若队列为空，则一直阻塞

- 检查方法

`peek()`: 获取但不移除此队列的头元素，没有元素则抛NoSuchElementException异常

`element()`: 获取但不移除此队列的头；若队列为空，则返回 null。

## ArrayBlockingQueue
ArrayBlockingQueue() 是一个用数组实现的有界阻塞队列，内部按先进先出的原则对元素进行排序；
其中 `put` 方法和 `take` 方法为添加和删除元素的阻塞方法。

ArrayBlockingQueue 实现的生产者消费者的 Demo，代码只是一个简单的 ArrayBlockingQueue 的
使用，Consumer 消费者和 Producer 生产者通过 ArrayBlockingQueue 来获取（take）和添加（put）
数据。具体代码请访问：[ABQ demo](https://github.com/joyang1/JavaInterview/blob/master/dataDesign/src/main/java/cn/tommyyang/queue/ArrayBlockingQueueDemo.java)。

ArrayBlockingQueue 内部的阻塞队列是通过 ReentrantLock 和 Condition 条件队列实现的，所以 ArrayBlockingQueue 中
的元素存在公平和非公平访问的区别，这是因为 ReentrantLock 里面存在公平锁和非公平锁的原因，ReentrantLock 的具体分析会
在 Lock 章节进行具体分析的； 对于 Lock 是公平锁的时候，则被阻塞的队列可以按照阻塞的先后顺序访问队列，Lock 是非公平锁的
时候，阻塞的线程将进入争夺锁资源的过程中，谁先抢到锁就可以先执行，没有固定的先后顺序。 

下面对 ArrayBlockingQueue 构造方法进行分析：
``` java
/**
 * 创建一个具体容量的队列，默认是非公平队列
 */
public ArrayBlockingQueue(int capacity) {
    this(capacity, false);
}

/**
 * 创建一个具体容量、是否公平的队列 
 */
public ArrayBlockingQueue(int capacity, boolean fair) {
    if (capacity <= 0)
        throw new IllegalArgumentException();
    this.items = new Object[capacity];
    lock = new ReentrantLock(fair);
    notEmpty = lock.newCondition();
    notFull =  lock.newCondition();
}

```

ArrayBlockingQueue 除了实现上述 BlockingQueue 接口的方法，其他方法介绍如下：
``` java
//返回队列剩余容量
public int remainingCapacity()

// 判断队列中是否存在当前元素o
public boolean contains(Object o) 

// 返回一个按正确顺序，包含队列中所有元素的数组
public Object[] toArray()

// 返回一个按正确顺序，包含队列中所有元素的数组；数组的运行时类型是指定数组的运行时类型
@SuppressWarnings("unchecked")
public <T> T[] toArray(T[] a)


// 自动清空队列中的所有元素
public void clear()

// 移除队列中所有可用元素，并将他们加入到给定的 Collection 中    
public int drainTo(Collection<? super E> c)

// 从队列中最多移除指定数量的可用元素，并将他们加入到给定的 Collection 中    
public int drainTo(Collection<? super E> c, int maxElements)

// 返回此队列中按正确顺序进行迭代的，包含所有元素的迭代器
public Iterator<E> iterator()

```

### ArrayBlockingQueue 源码和实现原理分析
#### 内部成员变量分析

``` java
public class ArrayBlockingQueue<E> extends AbstractQueue<E>
        implements BlockingQueue<E>, java.io.Serializable {

    /** 存储数据的数组 */
    final Object[] items;

    /** 获取数据的索引，用于下次 take, poll, peek or remove 等方法 */
    int takeIndex;

    /** 添加元素的索引， 用于下次 put, offer, or add 方法 */
    int putIndex;

    /** 队列元素的个数 */
    int count;

    /*
     * 并发控制使用任何教科书中的经典双条件算法
     */

    /** 控制并发访问的锁 */
    final ReentrantLock lock;

    /** 非空条件对象，用于通知 take 方法中在等待获取数据的线程，队列中已有数据，可以执行获取操作 */
    private final Condition notEmpty;

    /** 未满条件对象，用于通知 put 方法中在等待添加数据的线程，队列未满，可以执行添加操作 */
    private final Condition notFull;

    /** 迭代器 */
    transient Itrs itrs = null;
}

```

从上面成员变量中可以看出，内部使用数组对象 items 来存储所有的数据；通过同一个 ReentrantLock 来同时
控制添加数据线程和移除数据线程的并发访问，这个与 LinkedBlockingQueue 有很大区别(下面会进行分析)。
对于 `notEmpty` 条件对象是用于存放等待调用(此时队列中没有数据) take 方法的线程，这些线程会加入到 `notEmpty` 条
件对象的单链表中(会在 Lock 章节中 Condition 分析中进行整理)，同时当队列中有数据后会通过 `notEmpty` 条件对象
唤醒链表中等待的线程(第一个加入链表的线程)去 take 数据。对于 `notFull` 条件对象是用于存放等待调用(此时队列容量已满) put 方法
的线程，这些线程会加入到 `notFull` 条件对象的单链表中，同时当队列中数据被消费后会通过 `notFull` 条件对象唤醒链表中
等待的线程去 put 数据。takeIndex 表示的是下一个(take、poll、peek、remove)方法被调用时获取数组元素的索引，putIndex 表示
的是下一个(put、offer、add)被调用时添加元素的索引。

#### 添加(阻塞添加)的实现分析
``` java
/**
 * Inserts element at current put position, advances, and signals.
 * Call only when holding lock.
 */
private void enqueue(E x) {
    // assert lock.getHoldCount() == 1;
    // assert items[putIndex] == null;
    final Object[] items = this.items;
    items[putIndex] = x;
    if (++putIndex == items.length)
        putIndex = 0;
    count++;
    notEmpty.signal();
}
```


## LinkedBlockingQueue


## LinkedBlockingDeque


## ConcurrentLinkedQueue