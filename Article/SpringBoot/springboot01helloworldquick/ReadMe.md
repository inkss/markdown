# Spring Initializer 快速创建

**HelloController.java**

```java
/*
@Controller 注解：表明该类是控制类
@RequestMapping 注解：处理请求
@ResponseBody 注解：返回数据给浏览器

>>>> REST API 一个请求，一个数据
>>>> 所以可能会有多个请求返回，每一个返回都需要 ResponseBody ，故可以把它放到类前面

>> 原：
@Controller
public class HelloController {

    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){}
}

>> 现：表明这个类的所有方法的返回数据都交给浏览器
@Controller
@ResponseBody
public class HelloController {
    ...
}

@RestController 注解：可以视为 Controller 和 ResponseBody

>>>> RestController 为组合注解，具体内容有：
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody

 */
```

*szyink 2019.3.7*

------

IDE都支持使用Spring的项目创建向导快速创建一个Spring Boot项目；

选择我们需要的模块；向导会联网创建Spring Boot项目；

默认生成的Spring Boot项目；

- 主程序已经生成好了，我们只需要我们自己的逻辑

- resources文件夹中目录结构

  - static：保存所有的静态资源； js css images；

  - templates：保存所有的模板页面；（Spring Boot默认jar包使用嵌入式的Tomcat，默认不支持JSP页
面）；可以使用模板引擎（freemarker、thymeleaf）；

  - application.properties：Spring Boot应用的配置文件；可以修改一些默认设置；