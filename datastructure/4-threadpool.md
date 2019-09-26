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