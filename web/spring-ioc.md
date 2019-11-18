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
    
    然后在 xml 中添加上如下配置即可，这样就可以自动扫描相关注解，添加 Bean 到 BeanFactory 中。
    ```xml
        <xml>
              <mvc:annotation-driven/>
              <context:component-scan base-package="cn.tommyyang"/> 
        </xml>
    ```
    
### BeanFactory 之 xml 配置
所有注册到容器的业务对象，在 Spring 称之为 bean。故每一个对象在 xml 中的映射也自然地对应一个 <bean> 的元素。而把这些 <bean> 元素组织起来的就是 <beans>。

#### beans 作为 xml 配置文件中最顶层的元素，拥有如下几个元素：
- description 0 个或 1 个 
- bean 0 个 或 多个
- import
- alias

#### beans 对 bean 进行管理的属性
- **default-lazy-init**
    
    其值可以为 true 或 false，默认值为 false。用来标志对所有的 <bean> 进行延迟初始化。
    
- **default-autowire**

    可以取值为 no、byName、byType、constructor以及 autodetect。默认值为 no，如果使用自动绑定的话，用来标志全体 bean 使用哪一种默认绑定方式。
    
- **default-init-method**
    
    如果下面的所有 <bean> 元素按照某种规则，都有同样初始化方法的方法名，那么可以使用这个属性去统一设置。

- **default-destroy-method**
    
    与 default-init-method 相对应，如果下面的所有 <bean> 元素按照某种规则，都有同样对象销毁方法的方法名，那么可以使用该属性统一设置。
    
#### bean 的相关属性
- id: 对象注册到容器的唯一标志，就跟人的身份证号码一样（重复的话就非常麻烦）。

- name: 很灵活，name 可以使用 id 不能使用的一些字符，也可以空格、逗号或者冒号分割来给一个 bean 指定多个 name。name 的作用跟 alias 为 id 指定多个别名基本累似。demo 如下：
    
    ```
    <bean id="bean1" name="/b1,b2" class="..impl.bean1Impl">
    </bean>
  
    <alias name="bean1" alias="/b1">
    <alias name="bean1" alias="b2">
    ```
  
- class: 通过 class 指定注册到容器的对象的类型，也就是对象的具体 class 路径。

- parent: 实现继承。

- scope: BeanFactory 除了拥有作为 IoC Service Provider 的职责，作为一个轻量级容器，它还有着其它的一些职责，其中就包括对象生命周期管理。bean 的生命周期，决定在于 scope 属性的设置。
    
    - singleton: 在 Spring 的 IoC 容器中，只存在一个共享实例，所有对象共享该对象的引用。这种对象的生命周期基本与 IoC 容器相同，就是从容器启动后，到第一次被请求后初始化该实例，一直存活到容器退出。所以说这种类型对象的"寿命"基本和 IoC 容器相同。
    
    - prototype: IoC 容器在接收到该类型对象的请求后，会每次都重新生成一个新的实例对象给请求方。也就是说，如果一个实例对象不能共享给所有对象的话，那就需要将该 bean 的 scope 声明为 prototype。
    
    - request
    
    - session
    
    - global session
    
    - 自定义 scope
    
 #### Spring 容器之 ApplicationContext
 作为 Spring 提供的较之 BeanFactory 更为先进的 IoC 容器实现，ApplicationContext 除了拥有 BeanFactory 的所有功能外，还进一步扩展了基本容器的功能，包括 BeanFactoryPostProcessor、BeanPostProcessor 以及其他特殊类型 bean 的自动识别、容器启动后 bean 实例的自动初始化、国际化的信息支持、容器内时间发布等。
 
 Spring 为 BeanFactory 类型容器提供了 XmlBeanFactory 实现。相应地，它也为 ApplicationContext 类型容器提供了一下几个常用实现。
 - FileSystemXmlApplicationContext
    
    在默认情况下，从文件系统加载 bean 定义以及相关资源的 ApplicationContext 实现。
    
 - ClassPathXmlApplicationContext
    
    在默认情况下，从 Classpath 加载 bean 定义以及相关资源的 ApplicationContext 实现。
 
 - XmlWebApplicationContext