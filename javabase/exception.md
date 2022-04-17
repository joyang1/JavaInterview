# 异常介绍
异常就是有异于常态，和正常情况不一样，有错误出现。在java中，阻止当前方法或作用域的情况，称之为异常。

## 异常分类
![异常分类图](https://cdn.jsdelivr.net/gh/filess/img6@main/2021/03/27/1616860311636-a2466f2e-6bb2-437e-bd71-b2124af3fd89.png)

## Exception具体实现
在系统开发中，平时经常需要使用的两种异常，一种是需要检查(checked)的，一种是不需要检查(unchecked)的。
那为什么需要两种异常呢？
- 用来区分告警的优先级。系统异常优先级高，因为说明系统服务、代码存在问题。
    - 业务异常，是已知的，因为其他客观因素导致的，比如用户输入的身份证格式有问题、用户购买商品时金额不足等。
    - 系统异常，是未知的，不知道啥时候会发生，如果发生了说明系统本身或者系统上下游存在问题，需要立马告警出来，让相关开发者感知到；以便发现问题和后续优化问题。比如：系统上下游服务抖动、请求超时、请求参数存在问题等。
-  使代码更清洁，该处理（checked）的异常内部处理掉，无法处理（unchecked）的异常告警出来。

往往对于开发者来说，比较难区分，何为系统系统，何为业务异常。其中系统异常是unchecked的，业务异常是checked。

### RuntimeException
`RuntimeException`是在Java虚拟机的正常操作期间可以抛出的那些异常的超类，是Exception的子类，是Exception中unchecked子类的超类。
比如系统上下游抖动、请求超时等，是允许在系统运行期间抛出的，所以该类异常应该继承自`RuntimeException`；且无需检查（unchecked）。
所以系统异常应该继承自`RuntimeException`。

开发时具体实现：

```java

/**
 * @Author : TommyYang
 * @Time : 2021-03-27 12:40
 * @Software: IntelliJ IDEA
 * @File : SystemException.java
 */
public class SystemException extends RuntimeException {

    private String code;

    public SystemException(String code) {
        this.code = code;
    }

    public SystemException(String message, String code) {
        super(message);
        this.code = code;
    }

    @Override
    public String toString() {
        return "SystemException{" +
                "code='" + code + '\'' +
                '}';
    }

}

```

### Exception
异常类和任何不是RuntimeException的子类的子类都是检查异常。 检查的异常需要在方法或构造函数的throws子句中声明，如果它们可以通过执行方法或构造函数抛出，并在方法或构造函数边界之外传播。
比如用户输入的身份证格式有问题、用户购买商品时金额不足等，这些是在开发系统的时候，就会已经的会出现这样的问题，这类异常是应该内部处理（checked）掉，而不应该告警出来。
所以业务异常应该继承自`Exception`，且需要检查（checked）。

开发时具体实现：

```java

/**
 * @Author : TommyYang
 * @Time : 2021-03-27 12:40
 * @Software: IntelliJ IDEA
 * @File : BusinessException.java
 */
public class BusinessException extends Exception {

    private String code;

    public BusinessException(String code) {
        this.code = code;
    }

    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "code='" + code + '\'' +
                '}';
    }
}

```

### 测试

```java

/**
 * @Author : TommyYang
 * @Time : 2021-03-27 13:19
 * @Software: IntelliJ IDEA
 * @File : ExceptionTest.java
 */
public class ExceptionTest {

    /**
     * 测试checked异常
     */
    @Test
    public void testException()  {
        try {
            throwsBusinessException();
        } catch (BusinessException e) {
            System.out.println("这是一个业务异常，内部处理掉" + e.toString());
        }
    }

    /**
     * 测试unchecked异常
     */
    @Test
    public void testRuntimeException() {
        throwsSystemException();
    }

    /**
     * 抛出业务异常（checked）
     * 所以需要throw出去，让外部调用方感知到，这是一个checked的异常，是已经问题
     */
    private void throwsBusinessException() throws BusinessException {
        throw new BusinessException("403");
    }

    /**
     * 抛出系统异常（unchecked）
     */
    private void throwsSystemException() {
        throw new SystemException("400");
    }

}

```

## Error介绍
`Error`表示严重的问题，合理的应用程序不应该试图捕获。 大多数这样的错误是异常情况。 ThreadDeath错误虽然是“正常”的条件，但也是Error一个子类，因为大多数应用程序不应该试图抓住它。

`Error`是由虚拟机生成并抛出，大多数错误与代码开发者所执行的操作无关。
常见的Error，比如Java虚拟机运行错误（VirtualMachineError）；当JVM执行操作所需的内存资源不够时，将出现OutOfMemoryError；当这些异常发生时，JVM一般会选择线程终止。
还有部分Error是发生在虚拟机试图执行应用时，比如类定义错误（NoClassDefFoundError）、链接错误（LinkageError）；这些错误是不可查的，因为它们在应用程序的控制和处理能力之外，而且绝大多数是程序运行时不允许出现的状况。
对于合理设计的应用程序来说，即使确实发生了错误，本质上也不应该试图去处理它所引起的异常状况。
在Java中，错误通常是使用Error的子类描述。





