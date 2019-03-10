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