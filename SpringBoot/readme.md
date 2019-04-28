# SpringBoot 笔记

## Spring 注解

```java
@Controller //注解：表明该类是控制类
@RequestMapping //注解：处理请求
@ResponseBody //注解：返回数据给浏览器

//===================================

@Controller
public class HelloController {
    @ResponseBody
    @RequestMapping("/hello")
    public String hello(){}
}
//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello(){}
}
//@RestController 注解：可以视为 Controller 和 ResponseBody

//=================================
```

## SpringBoot 静态文件存放位置

- 默认的静态文件位置（resources 所在目录为 classpath）：

```sh
/src/main/resources
            ├── resources
            │   └── 静态文件
            ├── public
            │   └── 静态文件
            └── static
                └── 静态文件
```

- 如何修改（如果修改，默认的静态文件失效）：

  ```properties
  spring.resources.static-locations=classpath:/resources,classpath:/public
  ```

- 图标和 index 也是在静态资源文件夹中寻找。

## SpringBoot 日志

- SLF4j

  ```java
  Logger logger = LoggerFactory.getLogger(HelloWorld.class);
  logger.info("Hello World"); // 日志级别 trace<debug<info<warn<error
  ```

- SpringBoot 底层使用 slf4j+logback 实现。

- 修改日志存储位置：

  ```properties
  # 指定级别
  logging.level.com.xxxxxx=trace
  # 格式化显示
  logging.pattern.console=%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(---){faint} %clr([%-80.80logger{79}]){cyan} %clr(:){faint} %m%n
  # 指定位置
  logging.file=filename.log
  ```

## SpringBoot 配置文件位置

- 默认的位置（优先级由高到低）（高优先级覆盖低优先级）（不同内容互补）：

  ```sh
  - file:./config/
  - file:./
  - classpath:/config/
  - classpath:/
  ```

- 使用配置文件追加：

  ```properties
  spring.config.location=
  ```

- 使用命令行参数指定配置文件：

  `java -jar xxxxx.jar --spring.config.location=xxxxx`

## SpringBoot 配置文件

- 全局配置文件：`application.properties` `application.yml`

- yaml 示例：

  ```yaml
  server:
    port: 8081
  ```

- properties 示例：

  ```properties
  server.port=8081
  ```

## SpringBoot 自动配置原理

- 主配置类 `@SpringBootApplication`
  - 主配置类开启自动配置 `@EnableAutoConfiguration`
    - 进一步的给容器导入组件 `@Import({AutoConfigurationImportSelector.class})`



- AutoConfigurationImportSelector

```java
public String[] selectImports(AnnotationMetadata annotationMetadata) {
    //.....
	return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
}
```

- getConfigurations()