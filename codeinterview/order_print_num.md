问题分析：首先我们需要看清楚题目，多线程、顺序打印、然后就是几个线程交替顺序打印；

首先看下输出结果：

```

thread-0:0
thread-1:1
thread-2:2
thread-0:3
thread-1:4
thread-2:5
thread-0:6
thread-1:7
thread-2:8
thread-0:9
thread-1:10
thread-2:11
thread-0:12
thread-1:13
thread-2:14
thread-0:15
thread-1:16
thread-2:17
thread-0:18
thread-1:19
thread-2:20
thread-0:21
thread-1:22
thread-2:23
thread-0:24
thread-1:25
thread-2:26
thread-0:27
thread-1:28
thread-2:29
thread-0:30
thread-1:31
thread-2:32
thread-0:33
thread-1:34
thread-2:35
thread-0:36
thread-1:37
thread-2:38
thread-0:39
thread-1:40
thread-2:41
thread-0:42
thread-1:43
thread-2:44
thread-0:45
thread-1:46
thread-2:47
thread-0:48
thread-1:49
thread-2:50
thread-0:51
thread-1:52
thread-2:53
thread-0:54
thread-1:55
thread-2:56
thread-0:57
thread-1:58
thread-2:59
thread-0:60
thread-1:61
thread-2:62
thread-0:63
thread-1:64
thread-2:65
thread-0:66
thread-1:67
thread-2:68
thread-0:69
thread-1:70
thread-2:71
thread-0:72
thread-1:73
thread-2:74
thread-0:75
thread-1:76
thread-2:77
thread-0:78
thread-1:79
thread-2:80
thread-0:81
thread-1:82
thread-2:83
thread-0:84
thread-1:85
thread-2:86
thread-0:87
thread-1:88
thread-2:89
thread-0:90
thread-1:91
thread-2:92
thread-0:93
thread-1:94
thread-2:95
thread-0:96
thread-1:97
thread-2:98
thread-0:99
thread-1:100

```

其实这个题目的重点就是怎么实现多个线程交替顺序打印，想一想，这里我们需要用到的就是线程间的通信，然后怎么通信呢？
我们可以通过 synchronized + wait + notifyAll，或者 ReentrantLock + Condition + await + signalAll；

知道了线程间怎么通信，然后就是在每个线程内部怎么实现了，是不是多个线程共用一把锁，然后自然数0-100是公共资源，让3个线程去消费；

我们可以每个线程加上一个标号：0，1，2，来表示具体是哪个线程；
通过一个计数器对3进行求余，余数和具体的线程标号去比较，只有当余数和线程标号相等的时候才进行打印(不等加入等待队列)，打印完计数器进行自增；
然后唤醒等待队列里面线程去获取锁，获取锁成功则继续执行以上步骤，否则加入锁的同步队列中，等待上一个线程唤醒；

`用 synchronized 锁实现如下：`

```java
class PrintThread1 implements Runnable {
    private static final Object LOCK = new Object();

    private static int count = 0; // 计数，同时确定线程是否要加入等待队列，还是可以直接去资源队列里面去获取数据进行打印
    private LinkedList<Integer> queue;
    private Integer threadNo;

    public PrintThread1(LinkedList<Integer> queue, Integer threadNo) {
        this.queue = queue;
        this.threadNo = threadNo;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (LOCK) {
                while (count % 3 != this.threadNo) {
                    if (count >= 101) {
                        break;
                    }
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (count >= 101) {
                    break;
                }

                Integer val = this.queue.poll();
                System.out.println("thread-" + this.threadNo + ":" + val);
                count++;

                LOCK.notifyAll();
            }
        }
    }
}
```

`用 ReentrantLock 锁实现如下：`

```java
class PrintThread2 implements Runnable {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition c = lock.newCondition();

    private static int count = 0; //作为计数，同时也作为资源；因为这道题目是自然数作为资源，所以正好可以公用；
    private Integer threadNo;

    public PrintThread2(Integer threadNo) {
        this.threadNo = threadNo;
    }

    @Override
    public void run() {
        while (true) {
            try {
                lock.lock();
                while (count % 3 != this.threadNo) {
                    if (count >= 101) {
                        break;
                    }
                    try {
                        c.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (count >= 101) {
                    break;
                }
                System.out.println("thread-" + this.threadNo + ":" + count);
                count++;

                c.signalAll();

            } finally {
                lock.unlock();
            }
        }
    }
}
```

`具体代码实现：`

参见：[code](https://github.com/joyang1/JavaInterview/blob/master/codeinterview/src/main/java/cn/tommyyang/multithread/OrderPrintNum.java)