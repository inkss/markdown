# SpringBoot HelloWorld 探析

## 一、POM 分析

### 1.父项目

```xml
<!--pom.xml 的父项目-->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.3.RELEASE</version>
</parent>

<!--spring-boot-starter-parent 的父项目-->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <version>2.1.3.RELEASE</version>
    <relativePath>../../spring-boot-dependencies</relativePath>
</parent>

<!--spring-boot-dependencies 的父项目-->
<!--控制版本-->
```

`pow.xml` 文件中父项目为： `spring-boot-starter-parent` ，而 spring-boot-starter-parent 的父项目为：`spring-boot-dependencies` 。在 spring-boot-dependencies 里有标签 `<properties></properties>` ，在其中指定了各种依赖包的版本号。即：**spring-boot-dependencies 控制了所有 Spring Boot 应用的版本号**。

### 2.启动器（依赖）

```xml
<!--pom.xml 的依赖-->
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```

**spring-boot-starter**-web ：

- spring-boot-starter ：Spring Boot 场景启动器。
- spring-boot-starter-web ： 导入了 WEB 项目所需的依赖。

> Spring Boot 将所有的功能场景抽取出来，做成一个一个 **starters ( 启动器 )** 。只要引入了相应的启动器，就会将相关场景的所有依赖导入进项目中。

## 二、主程序

```java
// HelloWorldMainApplication.java

@SpringBootApplication
public class HelloWorldMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloWorldMainApplication.class, args);
    }
}
```

- `@SpringBootApplication` ：Spring Boot 应用。表示被标注的类是 Spring Boot 的主配置类。

```java
// SpringBootApplication.class

...
@SpringBootConfiguration
@EnableAutoConfiguration
...
public @interface SpringBootApplication {
    //...
}
```

- `@SpringBootConfiguration` ：Spring Boot 配置类，表示被标注的类为配置类。
  - 其内部含有 `@Configuration` 注解，它是 Spring 的底层配置类，配置类 <----> 配置文件。
    - Configuration 的内部 `@Component` ：组件。配置类也是容器中的一个组件。

- `@EnableAutoConfiguration` ：开启自动配置，允许 Spring Boot 自动配置相关内容。 
  - `@AutoConfigurationPackage` ：自动配置包名，将主配置类（@SpringBootApplication 标注的类）所在包及下面所有子包里面的所有组件扫描到 Spring 容器。
  - `@Import({AutoConfigurationImportSelector.class})` ：@Import 为 Spring 的底层注解，导入所需的组件，AutoConfigurationImportSelector 中含有 `String[] selectImports() {} ` 方法，将所需要的导入的组件以全类名的方式返回，并添加到容器中。**最终会给容器中导入各类自动配置类，从而免去了手动编写注入功能组件等工作。**

> PS：EnableAutoConfiguration 这个注解我并没有完全理解，疑问是 Import 是根据 AutoConfigurationImportSelector 所返回的类名称进行导入的，只是不理解它是如何筛选出所需要的类名称的。根据视频中的说法，同时从 `getAutoConfigurationEntry` 获得具体的配置文件，但是视频中与我使用的 Spring 版本不一致，很多代码内容变动了，原有的继承关系都没了。**故存疑**。

> 但是在 spring-boot-autoconfigure-2.1.3-RELEASE.jar/META-INF/spring.factories 同样有配置文件的内容。

