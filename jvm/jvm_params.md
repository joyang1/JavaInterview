# JVM 参数
- 查看 JVM 使用什么垃圾回收器：java -XX:+PrintCommandLineFlags -version

``` properties

-XX:InitialHeapSize=2147483648 -XX:MaxHeapSize=32210157568 -XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC
openjdk version "1.8.0_222"
OpenJDK Runtime Environment (build 1.8.0_222-8u222-b10-1ubuntu1~16.04.1-b10)
OpenJDK 64-Bit Server VM (build 25.222-b10, mixed mode)

```
