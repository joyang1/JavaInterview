# 多线程 N 次写文件
记一次项目中多线程 N 次写文件的愚蠢做法，通过这次愚蠢的编码，从中体会到的就是我们需要对 JDK 里面源码尽量熟悉，多看源码，这样才能提高我们编码的性能。

愚蠢代码如下：

```java

public class FileUtils {

    public static synchronized void writeFile(String filePath, String content) {
        FileOutputStream outStream = null;
        BufferedWriter bfWriter = null;
        try {
            outStream = new FileOutputStream(filePath, true);
            bfWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
            bfWriter.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bfWriter != null) {
                    bfWriter.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
            } catch (Exception e) {

            }
        }
    }

}

```

## 问题描述
上述代码封装了一个通用的多线程 N 次追加写文件的类，上述代码虽然可以保证完成任务，但是代码是非常低效。
我发现这段代码的问题，也是因为，一开始我在项目里面只需要使用这段代码写一个文件，写入的次数是大概是在
1500 次左右；而后续由于功能需求增加，我需要使用这段代码写多个文件，每个文件写入次数都是 1500 次左右，
从而项目运行的效率变得异常缓慢，如龟速运行，让我无法接受。

## 改进版本一
于是我开始改进，首先，我觉得每次写入的时候都去 new BufferedWriter 实例，这个是不可取的；浪费了 new
对象的时间，其实我们写文件，BufferedWriter 对象 new 一次就可以了。改进代码如下：

```java

public class BigFileWriter1 {

    private FileOutputStream outStream = null;
    private BufferedWriter bfWriter = null;

    private String filePath;

    public BigFileWriter(String filePath) {
        this.filePath = filePath;
        open(); // new writer 对象的时候，open 需要使用的对象一次，节约每次写入的时候都去 new 对象的时间。
    }

    private void open() {
        try {
            outStream = new FileOutputStream(this.filePath, true);
            bfWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
        } catch (Exception e) {
            System.out.println("get big file writer error");
            e.printStackTrace();
        }
    }

    // synchronized 同步方法块， 非常抢眼且多余的 synchronized
    public synchronized void writeFile(String content) {
        try {
            bfWriter.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * 该方法没有同步，原因是该方法应该是 main thread 去进行 close 的。
     * 因为我们需要让所有线程都执行完了，最后才会 close writer 对象
     * 
     */
    public void close() {
        try {
            if (bfWriter != null) {
                bfWriter.close();
            }
            if (outStream != null) {
                outStream.close();
            }
        } catch (Exception e) {

        }
    }

}

```

经过上述代码改进后，代码效率基本可以了，但是由于对源码不熟悉，如果专业的人看到这段代码，会觉得我们比较业余，
因为那个 synchronized 是抢眼且多余的，在专业的人看来会非常的不舒服的。由于 IO 操作是我们日常编程中使用
到最多的 API，但是我们确对源码是那么的不熟悉。

## JDK 中 Writer 源码分析
我们先来看看 Writer 的构造方法：

``` java

/**
 * The object used to synchronize operations on this stream.  For
 * efficiency, a character-stream object may use an object other than
 * itself to protect critical sections.  A subclass should therefore use
 * the object in this field rather than <tt>this</tt> or a synchronized
 * method.
 */
protected Object lock;

/**
 * Creates a new character-stream writer whose critical sections will
 * synchronize on the writer itself.
 */
protected Writer() {
 this.lock = this;
}

/**
 * Creates a new character-stream writer whose critical sections will
 * synchronize on the given object.
 *
 * @param  lock
 *         Object to synchronize on
 */
protected Writer(Object lock) {
    if (lock == null) {
        throw new NullPointerException();
    }
    this.lock = lock;
}

```

从以上源码可以看出，Writer 里面关键的流部门，都会有 lock 锁进行同步；所以，对于同一个 writer instance 是
线程安全的；所以我们写同一个文件的时候使用同一个 writer instance 是线程安全的。也就是说我们使用的 Writer、
FileWriter、BufferedWriter 是线程安全的。

具体的 write 方法源码分析如下：

``` java

public void write(String str, int off, int len) throws IOException {
    synchronized (lock) {
        char cbuf[];
        if (len <= WRITE_BUFFER_SIZE) {
            if (writeBuffer == null) {
                writeBuffer = new char[WRITE_BUFFER_SIZE];
            }
            cbuf = writeBuffer;
        } else {    // Don't permanently allocate very large buffers.
            cbuf = new char[len];
        }
        str.getChars(off, (off + len), cbuf, 0);
        write(cbuf, 0, len);
    }
}

```  

在初始话 Writer Instance 的时候，我们会确定一个同步锁对象，所以只要我们使用的是一个 Writer 对象，则可以保证
线程安全。

## 改进版本二
通过以上源码分析，我们可以很清楚地改进最优的代码如下：

```java

public class BigFileWriter {

    private FileOutputStream outStream = null;
    private BufferedWriter bfWriter = null;

    private String filePath;

    public BigFileWriter(String filePath) {
        this.filePath = filePath;
        open(); // new writer 对象的时候，open 需要使用的对象一次，节约每次写入的时候都去 new 对象的时间。
    }

    private void open() {
        try {
            outStream = new FileOutputStream(this.filePath, true);
            bfWriter = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
        } catch (Exception e) {
            System.out.println("get big file writer error");
            e.printStackTrace();
        }
    }

    // 由于 bufferedWriter 对象是线程安全的，所以不需要 synchronized 关键字。
    public void writeFile(String content) {
        try {
            bfWriter.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * 该方法没有同步，原因是该方法应该是 main thread 去进行 close 的。
     * 因为我们需要让所有线程都执行完了，最后才会 close writer 对象
     * 
     */
    public void close() {
        try {
            if (bfWriter != null) {
                bfWriter.close();
            }
            if (outStream != null) {
                outStream.close();
            }
        } catch (Exception e) {

        }
    }

}

```

## 测试
通过以上三种方法进行测试，使用时间如下：

``` properties

big file writer const:114 ms
big file writer 1 const:124 ms
file utils const:23236 ms

```

大家可以通过如下测试源码自行测试：

[测试源码](https://github.com/joyang1/JavaInterview/blob/master/io/src/main/java/cn/tommyyang/file/FileTest.java)

通过测试我们可以发现，确实是改进版本二的代码效率最高。

## 总结
希望大家多看源码，了解 JDK 源码，学习优秀的编码方式。