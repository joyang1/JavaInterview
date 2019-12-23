# Spring 配置
说到 Spring，首先我们应该熟悉 Spring 里面的常用配置，以及区分开 Spring 的注解和 Java 本身自带的注解。

Spring 配置的方式可以分为：
- 手动配置
- 自动配置

手动配置就是在 spring 的 xml 手动去配置需要的 bean；自动配置就是使用注解，然后 Spring 自动扫描注解配置。

## Spring 注解与 Java 注解
- Spring 注解
    
    - @Controller
    - @Service
    - @Repository
    - @Component
    - @Autowired 
    - @ModelAttribute
    - @RequestBody
    - @PathVariable
    - @RequestParam
    - @ResponseBody
    
- Java 注解
    
    - @Resource
    - @PostConstruct
        
        被 @PostConstruct 修饰的方法会在服务器加载 Servlet 的时候运行，并且只会被服务器调用一次，类似于 Servlet 的 init()方法。被 @PostConstruct 修饰的方法会在构造函数之后，init() 方法之前运行。
        
    - @PreConstruct
    
         被 @PreConstruct 修饰的方法会在服务器卸载 Servlet 的时候运行，并且只会被服务器调用一次，类似于 Servlet 的 destroy() 方法。被 @PreConstruct 修饰的方法会在 destroy() 方法之后运行，在 Servlet 被彻底卸载之前。
 
- 自定义注解(annotation)
    
    [参考](https://github.com/joyang1/spring-demo)  
    
### Spring 中 @Autowired, @Resource 和 @Inject 有何差异？  
三个注释中的两个属于 Java 扩展包：@Resource 属于 javax.annotation.Resource 包和 @Inject 属于 javax.inject.Inject 包。
@Autowired注解属于org.springframework.beans.factory.annotation包。 

`@Autowired`

|  特性	  | 说明    |
| :----: | :----: |
|  原理	 | 根据类型来自动注入（ByType）|
| 注入类型 |	既可以注入一个接口，也可以直接注入一个实例 |
| 限制	 |  1.当注入一个接口时，这个接口只能有一个实现类，如果存在一个以上的实现类，那么Spring会抛出异常，因为两个同样的接口实现类，它不知道该选择哪一个来注入。<br/>2.当注入一个实例时，跟接口类似，如果这个实例在XML配置文件中声明了两个不同的Bean,那么Spring也会抛出异常。|
| 解决办法 |	@Autowired配合@Qualifier来使用，通过@Qualifier来指明要注入Bean的name。|

`@Resource`

|  特性	  | 说明    |
| :----: | :----: |
|  原理	 | 如果指定了name属性, 那么就按name属性的名称装配;<br/>如果没有指定name属性, 那就按照要注入对象的字段名查找依赖对象;<br/>如果按默认名称查找不到依赖对象, 那么就按照类型查找。 |
| 注入类型 |	既可以注入一个接口，也可以直接注入一个实例 |

## Spring 在 xml 如何配置 Map、List
1. 使用 `xmlns:util="http://www.springframework.org/schema/util` 在 xml 中配置 map 和 list 的 bean。
2. 使用 Java 自带的 @Resource(name="***") 给具体需要的元素赋值。

