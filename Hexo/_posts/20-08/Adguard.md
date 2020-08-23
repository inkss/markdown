---
title: AdGuard | 保护隐私 | 拦截广告 | DOH 支持
toc: true
tag:
  - 隐私
  - Adguard
  - DNS
  - DOH
categories: other
description: 从 Http 到 Https ，从 DNS 到 SDNS ，人们在追究安全的脚步上从未停歇。
abbrlink: c949262
date: 2020-08-23 17:36:15
updated: 2020-08-23 17:36:15
---

&ensp;&emsp;这是一篇水文。

## 隐私保护

&ensp;&emsp;在 AdGuard 的设置模块中有一个名为隐形模式的选择项，这里定义是 **从成千上万的互联网跟踪器中保护您的身份和个人敏感信息不被泄露** 。早期在浏览器的请求中，有一个名为 *请勿跟踪（DNT）* 的设置，设计本身到是好意，但他更像是个君子协议（总有小人），还是不完全靠谱的。

### 跟踪方式

&ensp;&emsp;主要是对于 cookie 的处理，一般来说网站会利用 cookie 存储一些信息以标记用户，AdGuard 可以修改所有 cookie 的过期时间，对于第三方 cookie ，大可以直接销毁掉，同类型的还有第三方的请求头或授权头，统统勾选拦截即可。另外第一方 cookie 一般会涉及用户自动登录，到可以不去拦截，这类内容的过滤主要是过滤身份标记，特征值等等。

### 浏览器 API 与 Windows 跟踪

&ensp;&emsp;WebRTC 通信协议，即使是开启了代理也有暴漏真实 IP 地址的风险，一般来说也不会用浏览器在线聊天，建议关闭。除此之外还有推送 API 和定位 API ，这种相当小众的需求还是拦截微妙。Flash ，一般来说这类严重过时的产物就应该直接关闭，最起码已经不被官方所支持了。 Windows 跟踪 中则是遥测、广告 ID，自动提交可疑病毒样本和 WAP 消息路由，无脑全勾选才是最佳选择呀。

### 杂项

&ensp;&emsp;前面所述都是拦截，而这部分则是伪装，包括自定义 Referrer 、自定义 User-Agent 和自定义 IP 地址。AdGuard 可以拦截替换请求头中的相关信息达到欺负服务器的目的（一些笨笨的检测方法会被欺骗到），提供一些假数据，隐藏真实信息。

## DOH 与 DOT

&ensp;&emsp;好吧，实话说前半段皆是水文，这里进入正文。PC 版 AdGuard 在最近的更新中正式支持自定义 DNS ，其中便包括自定义 DOH 和 DOT ，这弥补了 Windows 系统无法设置 SNDS 的遗憾。

### SDNS

&ensp;&emsp;就像是和 Http 到 Https 的演变过程一样，DNS 的隐私安全也被提上了议程。我在学习计算机网络时就一直感叹那些网络协议设计只巧妙（向下兼容）。DNS 的加密过程也是非常巧妙的，目前来说有两种方式，一为走 tls 协议，另一位混进 https 协议里。

&ensp;&emsp;SNDS 多加了一层鉴权使得通信过程得以保证。但是 DOT 的口碑不佳，因为 DOT 独占 853 端口，虽然无法被投毒，但是端口特征过于明显。DOH 则不同啦，和 Https 共用 443 端口，流量混淆起来，特征不明显更为安全。和 Http 劫持一样，不加密的 DNS 也有被劫持投毒的风险，一些恶意的运营商甚至可以通过此手段悄然无声的在网站中加载广告。

- `https://dns.alidns.com/dns-query`
- `https://doh.pub/dns-query`
- `https://dns.google/dns-query`

&ensp;&emsp;以上三个，分别是阿里、腾讯、谷歌的 DOH 地址。