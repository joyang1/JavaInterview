# Java8
分享 Java8 的新特性。

## 行为参数化（函数式接口）

1. 筛选某种颜色的苹果
2. 筛选重量大于某个值的苹果

**Java8 之前的代码**

```java

public class Apple {
    
    public static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
        List<Apple> result = new ArrayList<Apple>();
        for (Apple apple: inventory){
            if ( apple.getColor().equals(color) ) { 
                result.add(apple);
            } 
        }
        return result;
    }
    
    public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
        List<Apple> result = new ArrayList<Apple>();
        for (Apple apple: inventory){
            if ( apple.getWeight() > weight ){ 
                result.add(apple);
            } 
        }
        return result;
    }
}

```

**Java8 的代码简化**

```java

@FunctionalInterface
public interface ApplePredicate{
    boolean test (Apple apple);
}

public class ProgressDemo {
    
    public static List<Apple> processApple(List<Apple> appleList, ApplePredicate p) {
        List<Apple> res = new ArrayList<>();
        for (Apple apple : appleList) {
            if (p.test(apple)) {
                res.add(apple);
            }
        }
        return res;
    }
    
}


```

## Stream API

1. stream()

2. parallelStream()

    非线程安全的；
    默认线程池的数量就是处理器的数量，特殊场景下可以使用系统属性：
    -Djava.util.concurrent.ForkJoinPool.common.parallelism={N} 调整

## Lambada 表达式（链式编程）

`基本语法`

1. (parameters) -> expression
2. (parameters) -> { statements; }

## 默认方法
实现有 default 方法的接口， 可以重写 default 方法，不重写则默认使用 interface 中的默认方法

```java

public interface DefaultInterface {

    void test();

    default String getName() {
        return "default";
    }
}

```