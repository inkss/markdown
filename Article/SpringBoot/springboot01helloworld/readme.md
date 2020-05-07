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

### 3.总结

综上，POW 文件控制导入的具体依赖类型和依赖的版本号。

- `spring-boot-starter-parent` 控制依赖组件的版本。
- `spring-boot-starter-*` 控制需要导入的依赖的组件。

## 二、主程序类

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

> Spring Boot 应用标注在某个类上说明这个类是 SpringBoot 的主配置类，SpringBoot就应该运行这个类的 main 方法来启动 SpringBoot 应用。

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

引用资料：

> 自动配置幕后英雄：SpringFactoriesLoader 详解：
>
> SpringFactoriesLoader 属于 Spring 框架私有的一种扩展方案，其主要功能就是从指定的配置文件META-INF/spring.factories加载配置。
>
> ...
>
> 所以，@EnableAutoConfiguration自动配置的魔法骑士就变成了：从classpath中搜寻所有的META-INF/spring.factories配置文件，并将其中org.springframework.boot.autoconfigure.EnableutoConfiguration对应的配置项通过反射（Java Refletion）实例化为对应的标注了@Configuration的JavaConfig形式的IoC容器配置类，然后汇总为一个并加载到IoC容器。
>
> Enable auto-configuration of the Spring Application Context, attempting to guess and configure beans that you are likely to need. Auto-configuration classes are usually applied based on your classpath and what beans you have defined. For example, if you have tomcat-embedded.jar on your classpath you are likely to want a TomcatServletWebServerFactory (unless you have defined your own ServletWebServerFactory bean).

------

*szyink 2019.3.7*