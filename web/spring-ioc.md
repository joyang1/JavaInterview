# Spring IoC
全称 Inversion of Control，控制反转；别名依赖注入（Dependency Injection）。

IoC 容器是整个 Spring 框架的核心和基础。 IoC 其实就是为了帮助我们避免"大费周折"地创建各种依赖对象，而提供了更加轻松简洁的方式。
它的的反转就是让你从之前的事必躬亲，转变为现在的享受服务。所以 IoC 的理念就是让别人为你服务。其实也就是让 IoC Service Provider 来为你服务。

## IoC 的注入方式

IoC 的 三种注入方式：
- 构造方法注入

    **被注入对象**可以通过在其构造方法中声明依赖对象的参数列表，让 IoC 容器知道它需要哪些依赖对象。
    
- setter 方法注入

    IoC 容器可以通过调用依赖对象的 setter 方法为**被注入对象**注入依赖对象。
    
- 接口注入

    接口方法注入就是新建一个接口，名字自定义，然后接口的方法名也自定义，只是方法的参数为**被注入对象**所依赖的对象，
    然后**被注入对象**实现这个接口即可。
    
## Ioc Service Provider - 掌握大局
Ioc Service Provider 是抽象出来的一个概念，它指代任何将 IoC 场景中的业务对象绑定到一起的实现方式。

**职责**

- 业务对象的构建管理

    在 IoC 场景中，业务对象无需关心所依赖的对象如何构建如何取得，这部分工作恰好就交给 IoC Service Provider 来做，
    将对象的构建逻辑从客户端对象中剥离出来，避免这部分逻辑污染业务对象的实现。

- 业务对象间的依赖绑定
   
    对于 IoC 来说，这个职责是最艰巨也是最重要的，这是它的使命之所在。IoC Service Provider 通过结合之前构建和管理的
    所有业务对象，以及各个业务间可以识别的依赖关系，将这些对象所依赖的对象注入绑定，从而保证每个业务对象在使用的时候，可以
    处于就绪状态。
    
## Spring IoC 容器
- BeanFactory（Spring 的 Ioc Service Provider）
    
    基础类型 IoC 容器，提供完整的 IoC 服务支持。如果没有特殊指定，默认采用延迟初始化策略（`lazy-load`）。
    
- ApplicationContext

    ApplicationContext 在 BeanFactory 的基础上构建，是相对比较高级的容器实现，拥有 BeanFactory 的所有支持，还提供其它高级特性，
    包括事件发布、国际化信息支持等。

Spring IoC 容器不仅提供了 IoC 支持，还提供了 IoC 之外的支持：
- 对象生命周期管理
- 线程管理
- 企业服务集成
- 查找服务
- AOP 支持

### BeanFactory 的对象注册与依赖绑定方式

- 直接编码方式
    
    相关类：DefaultListableBeanFactory 间接地实现了 BeanFactory 接口和 BeanDefinitionRegistry 接口。其中 BeanDefinitionRegistry 接口才是
    在 BeanFactory 的实现中担当 Bean 注册管理的角色。
    举例说明下：
    BeanFactory - 图书馆
    BeanDefinitionRegistry - 书架
    BeanDefinition - 各种各样的书

- 外部配置文件方式
    
    - properties 文件 (PropertiesBeanDefinitionReader)
    - xml 文件 (XmlBeanDefinitionReader)
    - 自定义文件（自己实现 BeanDefinitionReader 接口）
    
- 注解方式
    
    - @Service
    - @Autowired
    
    <mvc:annotation-driven/>
    <context:component-scan base-package="com.dianping"/> 然后通过设置自动扫描就行了。
    