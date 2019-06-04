# StringBuffer 和 StringBuilder
## 介绍
大多数情况下， StringBuffer 的速度要比 String 快； StringBuilder  要比StringBuffer快；
StringBuffer 和 StringBuilder 都是 AbstractStringBuilder 的子类，区别在于StringBuffer
的方法大部分都有 synchronized 修饰。<br/>

## 源码解析

### AbstractStringBuilder 

#### 变量及构造方法

``` java

/**
 * 用来存储字符的数组
 * The value is used for character storage.
 */
char[] value;

/**
 * 字符个数 
 * The count is the number of characters used.
 */
int count;

/**
 * This no-arg constructor is necessary for serialization of subclasses.
 */
AbstractStringBuilder() {
}

/**
 * 在构造方法中指定字符数组的长度
 * Creates an AbstractStringBuilder of the specified capacity.
 */
AbstractStringBuilder(int capacity) {
    value = new char[capacity];
}
    
```


#### 扩容

``` java
public void ensureCapacity(int minimumCapacity) {
    if (minimumCapacity > 0)
        ensureCapacityInternal(minimumCapacity);
}

private void ensureCapacityInternal(int minimumCapacity) {
    // overflow-conscious code
    if (minimumCapacity - value.length > 0) {
        value = Arrays.copyOf(value,
                newCapacity(minimumCapacity));
    }
}

private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

private int newCapacity(int minCapacity) {
    // overflow-conscious code
    int newCapacity = (value.length << 1) + 2;
    if (newCapacity - minCapacity < 0) {
        newCapacity = minCapacity;
    }
    return (newCapacity <= 0 || MAX_ARRAY_SIZE - newCapacity < 0)
        ? hugeCapacity(minCapacity)
        : newCapacity;
}

private int hugeCapacity(int minCapacity) {
    if (Integer.MAX_VALUE - minCapacity < 0) { // overflow
        throw new OutOfMemoryError();
    }
    return (minCapacity > MAX_ARRAY_SIZE)
        ? minCapacity : MAX_ARRAY_SIZE;
}
```

扩容的方法最终由 `newCapacity()`  实现的，首先将容量左移一位（即扩大2倍）同时加2，如果此时任小于指定的容量，
那么就将容量设置为 `minimumCapacity` 。
然后判断是否溢出，通过 `hugeCapacity` 实现，如果溢出了（长度大于 `Integer.MAX_VALUE` ），则抛错（ `OutOfMemoryError` ）；
否则根据 `minCapacity` 和 `Integer.MAX_VALUE - 8` 的大小比较确定数组容量为 `max(minCapacity, Integer.MAX_VALUE - 8)`。
最后将 `value`  值进行拷贝，这一步显然是最耗时的操作。

#### append() 方法

``` java
public AbstractStringBuilder append(String str) {
    if (str == null)
        return appendNull();
    int len = str.length();
    ensureCapacityInternal(count + len);
    str.getChars(0, len, value, count);
    count += len;
    return this;
}
```
`append()` 是最常用的方法，它有很多形式的重载。上面是最常用的一种，用于追加字符串。如果 `str` 是 `null` ，则直接调用
`appendNull()` 方法。这个方法就是直接追加 `'n'` 、 `'u'` 、`'l'`、`'l'` 这几个字符，方法如下：

``` java

private AbstractStringBuilder appendNull() {
    int c = count;
    ensureCapacityInternal(c + 4);
    final char[] value = this.value;
    value[c++] = 'n';
    value[c++] = 'u';
    value[c++] = 'l';
    value[c++] = 'l';
    count = c;
    return this;
}

```

如果不是` null `，则首先需要判断数组容量是否足够，不够则需要扩容（扩容则是调用上述分析的扩容方法）；
然后调用 `String` 的 `getChars()` 方法将 `str` 追加到 `value`  末尾；
最后返回对象本身，所以 `append()` 可以连续调用(就是一种类似于链式编程)。


### 思考
- 为什么每次扩容是扩容为原来的两倍？
个人觉得是为了避免经常扩容带来的成本消耗。
- 为什么会加2呢？
个人也没想出什么好的解释，觉得可能是因为 `Java` 开发者认为我们在 `append` 数据的时候，中间经常会加一个分隔符，
恰好这个分隔符在 `Java` 中正好占用两个字节。也不知道分析的对不对，有其他意见的大佬们可以在 [issue](https://github.com/joyang1/JavaInterview/issues/2)
中进行讨论。


### StringBuilder 与 StringBuffer 方法对比

通过查看源码分析发现两者都继承至 `AbstractStringBuilder` 。 而 `StringBuffer` 之所以是线程安全的，
是因为重写 `AbstractStringBuilder` 的方法的时候在前面加上了 `synchronzied` 修饰这些方法；
而 `StringBuilder` 重写的时候只是直接调用父类的方法，没有做其他的操作。

其实通过阅读源码发现 `StringBuilder` 和 `StringBuffer` 之间的关系，类似于 `HashMap` 和 `HashTable`
之间的关系。


