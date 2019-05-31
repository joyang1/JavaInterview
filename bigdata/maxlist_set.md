## MaxList以及Set模块

> MaxList模块主要是对Java集合大数据去重的相关介绍。

背景: 最近在项目中遇到了List集合中的数据要去重，大概一个2500万的数据，开始存储在List中，需要跟一个2万的List去去重。

### 直接两个List去重  

说到去重，稍微多讲一点啊，去重的时候有点小伙伴可能直接对2500万List foreach循环后直接删除,
其实这种是错误的(java.util.ConcurrentModificationException)，大家可以自己去试一下；(注: for循环遍历删除不报错，但是效率低，不推荐使用)
首先你需要去看下foreach和迭代器的实现。foreach的实现就是用到了迭代器，所以你在foreach的时候对list进行删除操作，
迭代器Iterator无法感知到list删除了，所以会报错。直接贴代码解释下。

ArrayList中Iterator的实现:
```
private class Itr implements Iterator<E> {
    int cursor;       // index of next element to return
    int lastRet = -1; // index of last element returned; -1 if no such
    int expectedModCount = modCount;

    public boolean hasNext() {
        return cursor != size;
    }

    @SuppressWarnings("unchecked")
    public E next() {
        checkForComodification();
        int i = cursor;
        if (i >= size)
            throw new NoSuchElementException();
        Object[] elementData = ArrayList.this.elementData;
        if (i >= elementData.length)
            throw new ConcurrentModificationException();
        cursor = i + 1;
        return (E) elementData[lastRet = i];
    }

    public void remove() {
        if (lastRet < 0)
            throw new IllegalStateException();
        checkForComodification();

        try {
            ArrayList.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
            expectedModCount = modCount;
        } catch (IndexOutOfBoundsException ex) {
            throw new ConcurrentModificationException();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void forEachRemaining(Consumer<? super E> consumer) {
        Objects.requireNonNull(consumer);
        final int size = ArrayList.this.size;
        int i = cursor;
        if (i >= size) {
            return;
        }
        final Object[] elementData = ArrayList.this.elementData;
        if (i >= elementData.length) {
            throw new ConcurrentModificationException();
        }
        while (i != size && modCount == expectedModCount) {
            consumer.accept((E) elementData[i++]);
        }
        // update once at end of iteration to reduce heap write traffic
        cursor = i;
        lastRet = i - 1;
        checkForComodification();
    }

    final void checkForComodification() {
        if (modCount != expectedModCount)
            throw new ConcurrentModificationException();
    }
}

```
通过上述的ArrayList里面的Iterator迭代器的实现我们可以看到:
基本上ArrayList采用size属性来维护自已的状态，而Iterator采用cursor来来维护自已的状态。
当你直接在foreach里面对list进行删除操作，size出现变化时，cursor并不一定能够得到同步，除非这种变化是Iterator主动导致的。(调用list.iterator()方法的原因)

从上面的代码可以看到当Iterator.remove方法导致ArrayList列表发生变化时，他会更新cursor来同步这一变化。但其他方式导致的ArrayList变化，Iterator是无法感知的。ArrayList自然也不会主动通知Iterator们，那将是一个繁重的工作。Iterator到底还是做了努力：为了防止状态不一致可能引发的无法设想的后果，Iterator会经常做checkForComodification检查，以防有变。如果有变，则以异常抛出，所以就出现了上面的异常。
如果对正在被迭代的集合进行结构上的改变（即对该集合使用add、remove或clear方法），那么迭代器就不再合法（并且在其后使用该迭代器将会有ConcurrentModificationException异常被抛出）.
如果使用迭代器自己的remove方法，那么这个迭代器就仍然是合法的。


```
public static void deWeightList(List<String> des, List<String> sourse){
        if(sourse == null || sourse.size() <= 0){
            return;
        }l
        Iterator<String> listStr = sourse.iterator();
        while (listStr.hasNext()){
            String item = listStr.next();
            for (String ditem: des) {
                if(item.equals(ditem)){
                    listStr.remove();
                    break;
                }
            }

        }
        logger.info("after deWight list size: " + sourse.size());
}
```


### List结合Set去重
```
public static void deWeightList(Set<String> des, List<String> sourse) {
        if (sourse == null || sourse.size() <= 0) {
            return;
        }
        Iterator<String> listStr = sourse.iterator();
        while (listStr.hasNext()) {
            String item = listStr.next();
            if (des.contains(item)) {
                listStr.remove();
            }
        }
        logger.info("after deWight list size: " + sourse.size());
}
```
### List结合Set去重(不是直接对list进行删除，而是组装新list，考虑到list删除效率低)
```
public static void deWeightListByNewList(Set<String> des, List<String> sourse) {
    if (sourse == null || sourse.size() <= 0) {
        return;
    }
    Iterator<String> listStr = sourse.iterator();
    List<String> existList = new ArrayList<String>();
    while (listStr.hasNext()) {
        String item = listStr.next();
        if(!des.contains(item)){
            //TODO 对去重后的数据进行逻辑操作，不一定要删除，可以换个思路（是否可以直接逻辑操作，不一定非要再把数据写进集合后，然后遍历集合在进行逻辑操作）
            existList.add(item); //改成添加进新的list，考虑到list的删除效率慢(非要得到删除后的集合的情况下，否则走else)
        }
//            if (des.contains(item)) {
//                //listStr.remove(); //考虑到list的删除效率慢，此种方法对于大数据集合来说不合适
//            }
    }
    sourse.clear();
    sourse = existList;
    logger.info("after deWight list size: " + sourse.size());
}
    
```

### 遍历过程中去重

个人最为推荐的一种,因为效率最高，也能达到功能的需要。

```
for (String item: maxArrayList) {
    if(testSet.contains(item)){
        //TODO
    }
}

```


### 测试结果如下
```
下面是1000万的list和20000的list去重两种方式所花的时间,可以看出使用set去重的效率要高很多。
1.list结合list去重时间:
14:52:02,408 INFO  [RunTest:37] start test list:17-11-07 14:52:02
14:59:49,828 INFO  [ListUtils:66] after deWight list size: 9980000
14:59:49,829 INFO  [RunTest:39] end test list:17-11-07 14:59:49

2.list结合set去重时间:
14:59:53,226 INFO  [RunTest:44] start test set:17-11-07 14:59:53
15:01:30,079 INFO  [ListUtils:80] after deWight list size: 9980000
15:01:30,079 INFO  [RunTest:46] end test set:17-11-07 15:01:30

下面是2500万的list和20000的list去重两种方式所花的时间,可以看出使用set去重的效率要更加的高，(数据量越大越明显)。
个人对set的大小为1500万也进行了测试，方案3,4的效率也是非常的高。

1.list结合list去重时间:
15:17:47,114 INFO  [RunTest:35] start test list, start time: 17-11-07 15:17:47
15:49:04,876 INFO  [ListUtils:57] after deWight list size: 24980000
15:49:04,877 INFO  [RunTest:39] end test list, end time: 17-11-07 15:49:04

2.list结合set去重时间:
15:49:17,842 INFO  [RunTest:44] start test set, start time: 17-11-07 15:49:17
15:53:22,716 INFO  [ListUtils:71] after deWight list size: 24980000
15:53:22,718 INFO  [RunTest:48] end test set, end time: 17-11-07 15:53:22

3. List结合Set去重(不是直接对list进行删除，而是组装新list，考虑到list删除效率低)
17:18:44,583 INFO  [RunTest:57] start test set, start time: 17-11-22 17:18:44
17:18:54,628 INFO  [ListUtils:92] after deWight list size: 23500000
17:18:54,628 INFO  [RunTest:61] end test set, end time: 17-11-22 17:18:49

4.遍历过程中结合set去重:(个人最为推荐的原因之一，效率高到令人爽到不行)
15:17:45,762 INFO  [RunTest:24] start test foreach list directly, start time: 17-11-07 15:17:45
15:17:47,114 INFO  [RunTest:32] end test foreach list directly, end time: 17-11-07 15:17:47

```

### 总结
通过上述测试我们可以看出，有时候我们排重的时候，不一定要拍完重再对排重后的数据进行遍历，可以在遍历的过程中进行排重，注意用来排重的那个集合放到Set中，
可以是HashSet,或者其他Set(推荐使用HashSet),因为Set的contains效率更高，比list高很多。

然后考虑到如果非要拿到去重后的list，考虑使用方案3《List结合Set去重(不是直接对list进行删除，而是组装新list，考虑到list删除效率低)》，通过测试，这种方法效率也是非常的高。
与方案4相比，稍微慢一点点。

对于上述方案1，测试也使用过组装新list的方式，而不是list.remove。但是效率还是比较慢。

这是实际工作中总结出来的经验。希望对大家有帮助！
欢迎大家来交流！
