# 垃圾回收器
垃圾回收算法是内存回收的方法论，垃圾收集器是内存回收的具体实现。

## 分类
以下分类是针对 HotSpot 虚拟机的垃圾回收器。

- Serial 收集器
- ParNew 收集器
- Parallel Scavenge 收集器
- Serial Old 收集器
- Parallel Old 收集器
- CMS 收集器
- G1 收集器

按收集器的回收对象以及收集器之间的联系，可以参照下图一：
<img src="https://blog.tommyyang.cn/img/java/jvm/jvm-garbage.png">

通过上图可以发现，Serial、ParNew、 Parallel Scavenge 是用来收集新生代的收集器，CMS、 Serial Old、Parallel Old 是用来收集老年代的收集器；然后 CMS 是不能和 Parallel Scavenge 结合起来用的，CMS 是可以和 Serial Old 联合起来收集老年代的。


## Serial 收集器
Serial 收集器是最基本、发展历史最悠久的收集器，（在JDK 1.3.1 之前）是虚拟机新生代收集的唯一选择。通过名字，大家也可以发现该收集器是一个单线程的收集器，但它的"单线程"的意义并不仅仅说明它只会使用一个 CPU 或 一条收集线程去完成垃圾收集工作，更重要的是在它进行收集时，必须暂停其它所有的工作线程，直到它收集结束。"Stop The World"就是该收集器的一个特点，这项工作是由虚拟机在后台自动发起和自动完成的，在用户不可见的情况下把用户正常工作的线程全部停掉，这对很多应用来说都是难以接受的。

Serial 收集器的工作过程如下图二：
<img src="https://blog.tommyyang.cn/img/java/jvm/serial_collector.png">

对于 "Stop The World" 带给用户的不良体验，虚拟机的设计者们表示完全理解，但是表示非常委屈："你妈妈在给你打扫房间的时候，肯定也会让你处在一个不会干扰她打扫的状态，比如坐在椅子上不动，或者到房间外面去，不然她一边打扫，你一边乱仍垃圾，这房间还能打扫完？"这确实是一个合情合理的矛盾，虽然垃圾收集这项工作听起来和打扫房间属于一个性质的，但实际上肯定还要比打扫房间复杂很多很多的。

垃圾收集器也是处在一个不断发展的过程中，由 Serial（串行）收集器到 Parallel（并行）收集器，再到 Concurrent Mark Sweep（CMS）乃至 Garbage First（G1）收集器，我们看到一个个越来越优秀（也越来越复杂）的收集器出现，用户线程的停顿时间在不断缩短。

Serial 收集器是虚拟机运行在 Client 模式下的默认新生代收集器。原因是由于在用户的桌面应用场景中，分配给虚拟机管理的内存一般来说不会很大，收集几十甚至一两百兆的新生代（仅仅是新声代的内存，桌面程序大多数情况下不会再大了），停顿时间完全可以控制在几十毫秒最多一百毫秒之内，只要不是频繁发生，这点停顿是可以接受的。所以，Serial 收集器对于运行在 Client 模式下的虚拟机来说是一个很好的选择。

## ParNew 收集器
ParNew 收集器其实就是 Serial 收集器的多线程版本。其它的与 Serial 收集器基本相同，也是 Stop The World，在实现上，这两种收集器也共用了相当多的代码。

ParNew 收集器的工作过程如下图三：
<img src="https://blog.tommyyang.cn/img/java/jvm/parnew_collector.png">

ParNew 收集器是许多运行在 Server 模式下的虚拟机中首选的新生代收集器，其中有一个与性能无关但很重要的原因，除了 Serial 收集器外，目前只有它能与 CMS 收集器配合工作。 CMS 收集器是在 JDK 1.5时期推出的一款真正意义上的并发（Concurrent）收集器，它第一次实现了让垃圾收集器与用户线程（基本上）同时工作，用前面的例子来说就是做到了在你妈妈打扫房间的同时你还能一边往地上扔垃圾。

但是，我们在图一中也可以看到， CMS 作为老年代的收集器，却无法与 JDK 1.4 时期推出的新生代收集器 Parallel Scavenge 配合工作，所以在 JDK 1.5 中使用 CMS 收集老年代的时候，新生代只能选择 ParNew 或者 Serial 收集器中的一个。

ParNew 在单 CPU 的环境中绝对不会比 Serial 收集器有更好的效果，甚至由于存在线程交互的开销（由用户态切到内核态之间的切换），该收集器在通过超线程技术实现的两个 CPU 的环境中都不能 100% 地保证可以超越 Serial 收集器。当然，随着 CPU 数量的增加，它对于 GC 是系统资源的有效利用还是很多效果的。它默认开启的线程数与 CPU 数量相同。

## Parallel Scavenge 收集器
Parallel Scavenge 也是并行收集器，与 ParNew 类似。那它的特点是什么？

Parallel Scavenge 收集器的特点是它的关注点与其他收集器不同，CMS 等收集器的关注点是尽可能地缩短垃圾收集时用户线程的停顿时间，而 Parallel Scavenge 收集器的目标是达到一个可控的吞吐量（Throughput）。所谓吞吐量就是 CPU 用于运行用户代码的时间与 CPU 总消耗时间的比值，即吞吐量 = 运行用户代码的时间 / (用户运行代码的时间 + 垃圾收集时间)，比如：虚拟机总共运行了 100 分钟，其中垃圾收集花掉了 1 分钟，那吞吐量就是 99%。

停顿时间越短越适合需要与用户交互的程序，良好的响应速度能提升用户体验，而高效率则可以高效率地利用 CPU 时间，尽快完成程序的运算任务，主要适合在后台运算而不需要太多交互的任务。

## Serial Old 收集器
Serial Old 是 Serial 收集器的老年代版本，它同样是一个单线程收集器，使用"标记-整理"算法。该收集器的主要意义也是在于给 Client 模式下的虚拟机使用。在 Server 模式下，它还有两大用途：一种用途是在 JDK 1.5 以及之前的版本中与 Parallel Scavenge 收集器搭配使用，另一种用途就是作为 CMS 收集器的后备方案，在并发收集发生 Concurrent Mode Failure 时使用。

## Parallel Old 收集器
Parallel Old 是 Parallel Scavenge 收集器的老年代版本，使用多线程和"标记-整理"算法。 

## CMS 收集器
CMS（Concurrent Mark Sweep）收集器是一种以获取最短回收停顿时间为目标的收集器。目前很大一部分的 Java 应用集中在互联网站或者 B/S 系统的服务端上，这类应用尤其重视服务的响应速度，希望系统停顿时间最短，给用户带来较好的体验（CMS 非常符合这类需求）。

CMS 的**收集过程**分为如下 4 个步骤：
- 初始标记（CMS initial mark）
- 并发标记（CMS concurrent mark）
- 重新标记（CMS remark）
- 并发清除（CMS concurrent sweep）

其中，初始标记和重新标记这两个步骤仍需"Stop The World"。

## G1 收集器


# 收集器中使用的参数列表

| 参数    | 参数说明 |
| :----: | :----: |
|  -XX:+UseConcMarkSweepGC  |  使用 CMS 收集器，ParNew 是使用该选项后的默认新生代收集器 |
|  -XX:+UseParNewGC  |  使用 ParNew 收集器  |
|  -XX:+ParallelGCThreads  |  设置垃圾回收线程数  |
|  -XX:MaxGCPauseMillis  |  Parallel Scavenge 收集器设置最大垃圾收集的停顿时间  |
|  -XX:GCTimeRatio  |  Parallel Scavenge 收集器设置最大垃圾收集的停顿时间  |



