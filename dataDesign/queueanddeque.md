# ArrayBlockingQueue、 LinkedBlockingQueue、 LinkedBlockingDeque 和 ConcurrentLinkedQueue

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


## LinkedBlockingQueue


## LinkedBlockingDeque


## ConcurrentLinkedQueue