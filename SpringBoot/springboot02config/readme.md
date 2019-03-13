# SpringBoot 配置 yml

全局配置文件：`application.properties` `application.yml`

------

YAML ：以数据为中心，大概就是比 xml 强的东东。

YAML ：

```yaml
server:
  port: 8081
```

XML :

```xml
<server>
    <port>8801</port>
</server>
``` 

------

- **基本语法**


对象：
 
 ````yaml
 key:
   k1: v1
   k2: v2
````

```yaml
key: {k1: v2,k2: v2}
```

数组：

```yaml
key:
 - v1
 - v2
```

```yaml
key: [v1,v2]
```

------

@Value获取值和@ConfigurationProperties获取值比较

|                      | @ConfigurationProperties | @Value     |
| -------------------- | ------------------------ | ---------- |
| 功能                 | 批量注入配置文件中的属性 | 一个个指定 |
| 松散绑定（松散语法） | 支持                     | 不支持     |
| SpEL (表达式)                 | 不支持                   | 支持       |
| JSR303数据校验       | 支持                     | 不支持     |
| 复杂类型封装         | 支持                     | 不支持     |

配置文件yml还是properties他们都能获取到值；

如果说，我们只是在某个业务逻辑中需要获取一下配置文件中的某项值，使用@Value；

如果说，我们专门编写了一个javaBean来和配置文件进行映射，我们就直接使用@ConfigurationProperties；

