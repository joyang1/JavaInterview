# 线程池
线程池，从字面意义上来讲，是指管理一组同构工作线程的资源池。线程是与工作队列（Work Queue）密切相关的，其中在工作队列中保存了所有等待执行的任务。工作者线程（Work Thread）的任务很简单：从工作队列中获取一个任务，执行任务，然后返回线程池并等待下一个任务。线程池简化了线程的管理工作，为用户开发多线程应用提供了更加方便的 API。

**好处**
- 线程池可以重用现有的线程而不是创建新线程，可以在处理多个请求时分摊在线程创建和销毁过程中产生的巨大开销。
- 当请求到达时，工作线程通常已经存在，因此不会由于等待线程创建线程而延迟任务的执行，从而提高响应性。
- 通过适当调整线程池的大小，可以创建足够多的线程以便使处理器保证忙碌状态，同时还可以防止多线程相互竞争资源而使应用程序耗尽内存或失败。

## Executor 框架
采用生产者-消费者模式，提交任务的操作相当于生产者（生产待完成的工作单元），执行任务的线程相当于消费者（执行完这些工作单元）。

```java
public interface Executor {

    /**
     *
     * @param command the runnable task
     * @throws RejectedExecutionException if this task cannot be
     * accepted for execution
     * @throws NullPointerException if command is null
     */
    void execute(Runnable command);
}
```

虽然 Executor 是个很简单的接口，但它却为灵活且强大的异步执行框架提供了基础，该框架能支持多种不同类型的任务执行策略。它提供了一种标准的方法将任务的提交过程与执行过程解藕开来，并用 Runnable 来表示任务。Executor 的实现还提供了对生命周期的支持，以及统计信息收集、应用程序管理机制和性能监视等机制。

## ThreadPoolExecutor
线程池的具体实现类。以下的线程池都是由 ThreadPoolExecutor 来具体实现的。

## 分类
- newSingleThreadPool
- newFixedThreadPool
- newCachedThreadPool
- newScheduledThreadPool
- newWorkStealingPool

## 具体分析
### newSingleThreadPool
Executors.newSingleThreadPool() 是一个单线程的 Executor，它创建单个线程来执行任务，如果这个线程异常结束，会创建另一个线程来替代。newSingleThreadPool 能确保依照任务在队列中的顺序来串行执行（比如 FIFO、 LIFO、优先级）。

### newFixedThreadPool
Executors.newFixedThreadPool() 创建一个固定长度的线程池，每当提交一个任务的时就创建一个线程，直到达到线程池的最大数量，这时，线程池的规模将不在变化（如果某个线程由于发生了未预期的 Exception 而结束，那么线程池会补充一个新的线程）。

### newCachedThreadPool
Executors.newCachedThreadPool() 创建一个可缓存的线程池，如果线程池的当前规模超过了处理需求时，那么将回收空闲的线程，而当需求增加时，则可以添加新的线程，线程池的规模不存在任何限制。

### newScheduledThreadPool
Executors.newScheduledThreadPool() 创建了一个固定长度的线程池，而且以延迟或定时的方式来执行任务，类似于 Timer。

### newWorkStealingPool
Executors.newWorkStealingPool() 创建了一个工作窃取池，具体地由 ForkJoinPool 构成；具体就是先把大任务 fork 成小任务，然后再把小任务的结果 join 起来，最后得到一个具体的结果。

### ThreadPoolExecutor 源码分析

#### 构造方法

```java

public class ThreadPoolExecutor extends AbstractExecutorService {

    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             Executors.defaultThreadFactory(), defaultHandler);
    }

    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             threadFactory, defaultHandler);
    }

    public ThreadPoolExecutor(int corePoolSize,
                                  int maximumPoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue,
                                  RejectedExecutionHandler handler) {
            this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                 Executors.defaultThreadFactory(), handler);
    }

    public ThreadPoolExecutor(int corePoolSize,
                                  int maximumPoolSize,
                                  long keepAliveTime,
                                  TimeUnit unit,
                                  BlockingQueue<Runnable> workQueue,
                                  ThreadFactory threadFactory,
                                  RejectedExecutionHandler handler) {
            if (corePoolSize < 0 ||
                maximumPoolSize <= 0 ||
                maximumPoolSize < corePoolSize ||
                keepAliveTime < 0)
                throw new IllegalArgumentException();
            if (workQueue == null || threadFactory == null || handler == null)
                throw new NullPointerException();
            this.acc = System.getSecurityManager() == null ?
                    null :
                    AccessController.getContext();
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.workQueue = workQueue;
            this.keepAliveTime = unit.toNanos(keepAliveTime);
            this.threadFactory = threadFactory;
            this.handler = handler;
    }

}


```

#### 7 个参数
- corePoolSize

    核心线程数，默认情况下核心线程会一直存活，即使处于闲置状态也不会受存 keepAliveTime 限制。除非将 allowCoreThreadTimeOut 设置为 true。
    
- maximumPoolSize
    
    线程池所能容纳的最大线程数。超过这个数的线程将被阻塞。当任务队列为没有设置大小的 LinkedBlockingDeque 时，这个值无效。
    
- keepAliveTime

    非核心线程的闲置超时时间，超过这个时间就会被回收。
    
- unit

    指定 keepAliveTime 的单位，如 TimeUnit.SECONDS。当将 allowCoreThreadTimeOut 设置为 true 时对 corePoolSize 生效。
    
- workQueue
    
    线程池中的任务队列。常用的有三种队列，`SynchronousQueue`，`LinkedBlockingDeque`，`ArrayBlockingQueue`。

- threadFactory

    线程工厂，提供创建新线程的功能。ThreadFactory 是一个接口，只有一个方法，如下：
    
    ```java
    
    public interface ThreadFactory {
      Thread newThread(Runnable r);
    }
  
    ```
  
    通过线程工厂可以对线程的一些属性进行定制。
    
    默认工厂源码：
    
    ```java
    
    static class DefaultThreadFactory implements ThreadFactory {
            private static final AtomicInteger poolNumber = new AtomicInteger(1);
            private final ThreadGroup group;
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            private final String namePrefix;
    
            DefaultThreadFactory() {
                SecurityManager s = System.getSecurityManager();
                group = (s != null) ? s.getThreadGroup() :
                                      Thread.currentThread().getThreadGroup();
                namePrefix = "pool-" +
                              poolNumber.getAndIncrement() +
                             "-thread-";
            }
    
            public Thread newThread(Runnable r) {
                Thread t = new Thread(group, r,
                                      namePrefix + threadNumber.getAndIncrement(),
                                      0);
                if (t.isDaemon())
                    t.setDaemon(false);
                if (t.getPriority() != Thread.NORM_PRIORITY)
                    t.setPriority(Thread.NORM_PRIORITY);
                return t;
            }
    }
  
    ```
    
- handler
    
    `RejectedExecutionHandler` 也是一个接口，同时也只有一个方法，如下：
    ```java
    
    public interface RejectedExecutionHandler {
      void rejectedExecution(Runnable var1, ThreadPoolExecutor var2);
    }
  
    ```
    当线程池中的资源已经全部使用，添加新线程被拒绝时，会调用 RejectedExecutionHandler 的 rejectedExecution 方法。