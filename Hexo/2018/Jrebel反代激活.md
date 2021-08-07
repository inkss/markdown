---
title: Jrebel 反代激活
toc: true
indent: true
date: 2018/09/08 13:48
updated: 2020/04/16 16:03
tag:
  - Java
  - 破解
categories: 资料
abbrlink: 89be439
description: JRebel 的社区激活取消了，搜索了一番本地激活方法，于此记录。
background: ../../img/bkg/wallhaven-rdyyr1.png
references:
  - title: ReverseProxy
    url: 'https://github.com/ilanyu/ReverseProxy'
  - title: Online GUID / UUID Generator
    url: 'https://www.guidgenerator.com/online-guid-generator.aspx'  
---

JRebel 的社区激活取消了，不能用分享到脸书的方式激活插件了，有点桑心。搜索了一番激活方法，于此记录。

## 1 准备工具

* 在 [ilanyu/ReverseProxy](https://github.com/ilanyu/ReverseProxy) 下载工具，相应操作系统选择自己对应的版本。

* 在 [Online GUID Generator](https://www.guidgenerator.com/online-guid-generator.aspx) 获取一串 GUID，记录之。

* IDEA 上在线下载 JRebel 插件。

## 2 激活

* 运行步奏一下载的程序（如果端口冲突参考 GitHub 文档使用帮助）。

* 修改本机 Hosts 文件，对于 Windows 目录为：`C:\Windows\System32\drivers\etc`

```shell
# 任意添加一个网站代理到本地回环上
127.0.0.1 omg.cc
```

* IDEA 中打开 JRebel 的激活界面。URL 处填写 `http://omg.cc:8888/你找的GUID`

## 3 以上

哦，记得将激活类型改为 work offline，离线 180 天，不然每次开还是有些麻烦的。
