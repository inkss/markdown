> IDEA与Eclipse理念上的差异：**不赞同直接在WEB-INF建立classes、lib目录**。

---
在使用Eclipse开发JavaWeb项目中，往往在新建完项目后，就同时在WEB-INF目录下*新建classes和lib目录*，所以网上大部分关于IDEA如何配置JavaWeb的博客中，同样提到了*新建目录，修改项目的导出目录*，步骤是很繁琐。但是在IDEA中，因为和Eclipse设计理念的不同：**编译目录，资源目录和运行目录是相分离的**。

---

IDEA的项目管理会隐藏掉一些文件目录，我们使用Atom打开项目。

![](http://upload-images.jianshu.io/upload_images/6490512-d2f0449df9e51aa4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

一个标准的IDEA项目，从上往下，依次是：

* **.idea**：IDEA的配置目录。
* **lib**：存放一下第三方jar包，默认和src同级。
* **out**：artifacts输出路径，也就是给Tomcat的web程序目录，也就是projectname_war_explodea，我们能很清楚的发现**WEB-INF俨然存在这里，classes与lib目录存放正确。**但是这里只是输出目录，资源目录仍然在web目录下；与artifacts同级的production目录是src目录中.java文件编译出的class文件。
* **src**：存放你Java代码的目录。
* **web**：存放你jsp前端文件的目录。
* **testWeb.iml**：配置文件。

---

我们可以注意到，除out目录外，src与web目录与Eclipse类似，但是lib目录却被放置到src同级目录了，在给tomcat前段页面时，是使用了out目录里projectname_war_explodea文件夹的文件。

**总之，我们无需各种修改默认的导出目录，因为没有那个必要呀**.

---

##### 附录1：

lib与out目录在初始建立的时候不存在，lib需要手动建立，在Project Structure的Libraries里添加新建的lib文件夹（*这个文件夹的位置可以不仅仅放在这儿，但是发现了IDEA对默认的lib是这儿后不如遵从*）至于out目录，项目在编译运行一次后自动生成。

##### 附录2：

* 参考链接1：[IntelliJ IDEA WEB项目的部署配置](https://my.oschina.net/lujianing/blog/186737)
* 参考链接2：[IntelliJ使用指南——深入理解IntelliJ的Web部署逻辑](http://white-crucifix.iteye.com/blog/2070830)

##### 附录3：

推荐的目录结构：

```
src
  com/yours/projectName
    login             //login对象
      model           //实体类
      dao             //数据层
      service         //业务层
      controller      //逻辑控制层
  resources           //资源配置文件
  sql                 //数据库脚本等
  test                //测试类
web
  WEB-INF
  css
  js
  fonts
  images
```
