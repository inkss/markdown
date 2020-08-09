---
title: Office 365 免费订阅申请
tags:
  - Office
categories: info
toc: true
translate_title: free-office365
description: 免费申请一年 Office 365 E3 订阅，自定义 Office 域设置。
abbrlink: 82e6dcea
date: 2018/09/29 21:26
updated: 2020/03/26 16:30
---

> **Office 365 开发人员计划** ：通过这个计划可以免费申请到一年 `Office 365 企业版 E3（开发人员）` 订阅。

## 1.Office 365 开发人员计划

![Office 365](../../static/Office-365-免费订阅申请.assets/01.png)

 :arrow_right: **相关链接**：[Office 365 开发者计划](https://developer.microsoft.com/zh-cn/office/dev-program)

申请过程这里不多阐述了，登录微软账户后按照提示操作即可，最终能够获得：

![订阅信息](../../static/Office-365-免费订阅申请.assets/02.png)

总共有 25 个账户，24 个可分配用户，每一个用户等同一个 Office 365 家庭版订阅。

![安装状态](../../static/Office-365-免费订阅申请.assets/03.png)

## 2.设置 Office 域

在 **Microsoft 365 admin center** 界面，有一个被称作 **域** 的功能：

默认的域名为：`username.onmicrosoft.com` ，用户默认登录名则是：`xxxx.username.onmicrosoft.com` 。

本质上只是一个三级域名，长且繁琐，所以最好使用自己的域名。鉴于成本原因（外加不需要备案），此处推荐从   **Freenom** 处申请。

 :arrow_right: **相关链接**：[Freenom](https://www.freenom.com/zh/index.html)

![Freenom](../../static/Office-365-免费订阅申请.assets/04.png)

可以在这个网站上 **以免费的价格申请到最长一年** 的域名（在域名到期前会发邮件通知）。

拿到域名后，回到 Office 设置，添加域信息即可（其中一步需要更改域名的 DNS 解析）。

![DNS 设置](../../static/Office-365-免费订阅申请.assets/05.png)

在 Office 域中显示如下：

![Office 域](../../static/Office-365-免费订阅申请.assets/06.png)

## 3.免费域名

虽然 Freenom 提供了免费域名申请，但是其用户协议里也要求域名必须能够正常使用（也就是通过域名必须能够访问成功），否则域名就会被收回。

这里的处理措施是用 Github Page 规避：https://github.com/July7183/july7183.github.io

------

**https://www.inkss.cf**

* 通过s开发者认证可以申请到一年 25 账户 的 Office 365 E3 订阅。
* Office 365 提供了一个域功能，允许用户使用自有域名设置账户信息。
* Freenom 提供免费域名的申请，但是域名必须能够正常访问，否则域名就会被收回。
* 本域名并未备案，因为域的原因使用了微软 DNS 解析，不确保中国境内的正常访问。
* 微软的 DNS 竟然不能直接解析主域名 `@` 。

> 更多信息参阅：https://inkss.cn/2018/09/29/free-office-365/
