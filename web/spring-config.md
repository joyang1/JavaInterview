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

## Spring 在 xml 如何配置 Map、List
1. 使用 `xmlns:util="http://www.springframework.org/schema/util` 在 xml 中配置 map 和 list 的 bean。
2. 使用 Java 自带的 @Resource(name="***") 给具体需要的元素赋值。
