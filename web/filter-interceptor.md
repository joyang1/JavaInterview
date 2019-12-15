# Filter 和 Interceptor 详解
Filter 和 Interceptor 都是可以处理 HttpServletRequest、HttpServletResponse 的。Filter 是 JDK 自带的接口，而 Interceptor 是 Spring 封装的接口，可以理解成一种特殊的 Filter，下面会具体分析。

## Filter
Filter 对**用户请求**进行**预处理**，接着将请求交给 Servlet 进行**处理**并**生成响应**，最后Filter再对**服务器响应**进行后处理。Filter 是可以复用的代码片段，常用来转换**Http 请求**、**响应**和**头信息**。Filter 不像 Servlet，它不能产生**响应**，而是只**修改**对某一资源的**请求**或者**响应**。

## Interceptor
类似**面向切面编程**中的**切面**和**通知**，我们通过**动态代理**对一个 service() 方法添加**通知**进行功能增强。比如说在方法执行前进行**初始化处理**，在方法执行后进行**后置处理**。**拦截器**的思想和**AOP**类似，区别就是**拦截器**只能对 Controller 的 HTTP 请求进行拦截。

## 两者的区别
- Filter 是基于**函数回调**的，而 Interceptor 则是基于 Java **反射**和**动态代理**。
- Filter 依赖于 Servlet 容器，而 Interceptor 不依赖于 Servlet 容器。
- Filter 对几乎**所有的请求**起作用，而 Interceptor 只对 Controller 请求起作用。

## 两者在一个 Http 请求中的执行顺序
- 对于自定义 Servlet 对请求分发流程：
    
    - Filter 过滤请求处理。
    - Servlet 处理请求。
    - Filter 过滤响应处理。
    
- 对于自定义 Controller 的请求分发流程：
    
    Filter 过滤请求处理。
    Interceptor 拦截请求处理。
    对应的 HandlerAdapter 处理请求。
    Interceptor 拦截响应处理。
    Interceptor 的最终处理。
    Filter 过滤响应处理。

## 示例
更多 web 开发相关示例请关注[spring-demo](https://github.com/joyang1/spring-demo)。



