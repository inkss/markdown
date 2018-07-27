# SSM 框架 -- 从入门到放弃

> 开个新坑，咸鱼的暑期学期计划。

------

* SSM ：Spring + SpringMVC + Mybatis
  
* 环境配置：IDEA + JDK 1.8 + Tomcat 9.0

------

项目结构：（标准 Maven 的 JavaWeb 项目）

```tree
.
└── src
    └── main
        ├── java
        │   └── com.inkss.ssm
        │       ├── controller
        │       ├── mapper
        │       ├── pojo
        │       └── service
        ├── resources
        ├── test
        └── webapp
            └── WEB-INF
```

------

日常无意义介绍时间：

1. **Spring**
    Spring是一个开源框架，Spring是于2003 年兴起的一个轻量级的Java 开发框架，由Rod Johnson 在其著作Expert One-On-One J2EE Development and Design中阐述的部分理念和原型衍生而来。它是为了解决企业应用开发的复杂性而创建的。Spring使用基本的JavaBean来完成以前只可能由EJB完成的事情。然而，Spring的用途不仅限于服务器端的开发。从简单性、可测试性和松耦合的角度而言，任何Java应用都可以从Spring中受益。 简单来说，Spring是一个轻量级的控制反转（IoC）和面向切面（AOP）的容器框架。
1. **SpringMVC**
    Spring MVC属于SpringFrameWork的后续产品，已经融合在Spring Web Flow里面。Spring MVC 分离了控制器、模型对象、分派器以及处理程序对象的角色，这种分离让它们更容易进行定制。
1. **MyBatis**
  MyBatis 本是apache的一个开源项目iBatis, 2010年这个项目由apache software foundation 迁移到了google code，并且改名为MyBatis 。MyBatis是一个基于Java的持久层框架。iBATIS提供的持久层框架包括SQL Maps和Data Access Objects（DAO）MyBatis 消除了几乎所有的JDBC代码和参数的手工设置以及结果集的检索。MyBatis 使用简单的 XML或注解用于配置和原始映射，将接口和 Java 的POJOs（Plain Old Java Objects，普通的 Java对象）映射成数据库中的记录。