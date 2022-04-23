# Java 基础知识

## 基础
### Integer类
```java

public class Test {
    public static void main(String[] args){
      Integer a = new Integer(129);
      Integer b = new Integer(129);
      
      // 1
      System.out.println(a == b);
      
      
      Integer c = new Integer(127);
      Integer d = new Integer(127);
      // 2
      System.out.println(c == d);
    }
}

```

上述代码可以看出1处 输出 false； 2 处输出 true。
从Integer类源码中，有一个内部类，代码如下：
```java

private static class IntegerCache {
        static final int low = -128;
        static final int high;
        static final Integer cache[];

        static {
            // high value may be configured by property
            int h = 127;
            String integerCacheHighPropValue =
                sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
            if (integerCacheHighPropValue != null) {
                try {
                    int i = parseInt(integerCacheHighPropValue);
                    i = Math.max(i, 127);
                    // Maximum array size is Integer.MAX_VALUE
                    h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
                } catch( NumberFormatException nfe) {
                    // If the property cannot be parsed into an int, ignore it.
                }
            }
            high = h;

            cache = new Integer[(high - low) + 1];
            int j = low;
            for(int k = 0; k < cache.length; k++)
                cache[k] = new Integer(j++);

            // range [-128, 127] must be interned (JLS7 5.1.7)
            assert IntegerCache.high >= 127;
        }

        private IntegerCache() {}
    }

```
从上述源码可以找出出现上述1处、2处结果的原因。


### Calendar类
> 添加天数：<font color="red">DAY_OF_MONTH、DAY_OF_YEAR、DAY_OF_WEEK、DAY_OF_WEEK_IN_MONTH</font>的区别

就单纯的add操作结果都一样，因为都是将日期+1, 不管是月的日期中加1还是年的日期中加1。<br/>
强行解释区别如下：<br/>
- `DAY_OF_MONTH` 的主要作用是get(DAY_OF_MONTH)，用来获得这一天在是这个月的第多少天。
- `DAY_OF_YEAR` 的主要作用get(DAY_OF_YEAR)，用来获得这一天在是这个年的第多少天。
- 同样，`DAY_OF_WEEK`，用来获得当前日期是一周的第几天；`DAY_OF_WEEK_IN_MONTH`，用来获取 day 所在的周是这个月的第几周

### [String、StringBuilder和StringBuffer](stringbuilderandstringbuffer.md)
|  类名|  描述|  是否可|  线程安全性|
|:----         |:----    |:----    |:---- |
|String        |字符串常量 | 不可变类  | 线程安全   |
|StringBuilder |字符串变量 | 可变类   |  线程不安全 |
|StringBuffer  |字符串变量 | 可变类   |  线程安全 |

###  [关键字篇](keywords)
- [transient](keywords/transient.md)
- [volatile](keywords/volatile.md)


## [异常篇](exception.md)

## [Java8 篇](java8)

## :house_with_garden: [数据结构篇](datastructure)
- [BlockingQueue和BlockingDeque](datastructure/blockingqueueanddeque.md)
  * Queue
  * Deque
  * LinkedList
  * ArrayBlockingQueue
  * LinkedBlockingQueue
  * LinkedBlockingDeque
  
## 多线程相关
- [线程池](threadpool.md)

