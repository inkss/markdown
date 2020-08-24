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
updated: 2020-08-24 12:01:15
references:
  - title: 个人向的隐私管理 - 枋柚梓
    url: https://inkss.cn/article/other/501e068e.html
  - title: DNS加密说明 - Gloudflare
    url: https://blog.cloudflare.com/zh/dns-encryption-explained-zh/
  - title: 超越DoH（DNS over HTTPS）：看DNS隐私不可信任的问题
    url: https://cloud.tencent.com/developer/news/670618
icons:
  - fal fa-shield-check
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

&ensp;&emsp;好吧，实话说前半段皆是介绍 Adguard，这里进入正文：DNS 协议的加密。PC 版 AdGuard 在最近的更新中正式支持自定义 DNS ，其中便包括自定义 DOH 和 DOT ，这弥补了 Windows 系统无法设置 SNDS 的遗憾。

### SDNS

&ensp;&emsp;就像是 Http 到 Https 的演变过程一样，DNS 的隐私安全也被提上了议程。我在学习计算机网络时就一直感叹那些网络协议设计只巧妙（向下兼容）。DNS 的加密过程也是非常巧妙的，目前来说有两种方式，一为走 tls 协议，另一位混进 https 协议里。

&ensp;&emsp;SNDS 多加了一层鉴权使得通信过程得以保证。但是 DOT 的口碑不佳，因为 DOT 独占 853 端口，虽然无法被投毒，但是端口特征过于明显。DOH 则不同啦，和 Https 共用 443 端口，流量混淆起来，特征不明显更为安全。和 Http 劫持一样，不加密的 DNS 也有被劫持投毒的风险，一些恶意的运营商甚至可以通过此手段悄然无声的在网站中加载广告。

- `https://dns.alidns.com/dns-query`
- `https://doh.pub/dns-query`
- `https://dns.google/dns-query`

&ensp;&emsp;以上三个，分别是阿里、腾讯、谷歌的 DOH 地址。加密是有代价的，但消耗却微不足道，所以势在必行！

### DNS 隐私

&ensp;&emsp;这是一段 DNS over HTTPS 的简单介绍：

> &ensp;&emsp;类似于 DNS over TLS 等机制， DNS over HTTPS 协议试图解决同样的安全和隐私问题。该机制并不考虑在原始 TLS 中包装 DNS 查询，而是包装在 HTTPS 中。而 HTTPS 协议本身是运行于TLS之上的。


> &ensp;&emsp;该机制的合理性在于，用户无需防护未知中间者，只需防护恶意的网络管理员。如果网络管理员可以选择网络中通信的DNS服务器，那么用户就会在毫不知情的情况下被路由到某个恶意服务器。实话讲，根本无法相信咖啡店的 Wi-Fi 会有什么安全的 DNS 服务器。如果要加密的服务器并不符合你的利益，那么加密本身也起不了什么作用。


> &ensp;&emsp;网络运营商可以强制人们始终使用他们所选择的服务器发出 DNS 请求，方法是针对其它服务器的请求实施中间人攻击（MITM），并自己回复它们，或者仅阻止 DNS 端口上与其他服务器之间的通信即可。DNS over TLS 可避免前者，并且由于 DNS over TLS 具有自己的服务端口，因此可以识别并阻止 DNS over TLS 流量。DNS over HTTPS 通过将 DNS 流量与所有其他 HTTPS 的流量混合在一起，完全避免产生上述问题。通常，管理者是无法禁止流向某个服务器的所有 HTTPS 流量。


> &ensp;&emsp;DNS over TLS 和 DNS over HTTPS 之间的差异在于，某些人认为他们有权限制其所提供公用事业服务的用户，而一些用户则想直接让这些管控者“滚得远远的”。

## Adguard Home

&ensp;&emsp;Adguard Home 可以说是一套自建 DNS 系统，除了它本身就有的精准屏蔽广告域名外，我们还可以利用它搭建一个属于自己的 SNDS。由于国内的政策对自建 DNS 方面允不允许不好说，流量特征明显的 DNS 和 DOT 显然是不行，万一被喝茶了可咋整，所以这里再次支持 DOH 啦，如同上文：通常，管理者是无法禁止流向某个服务器的所有 HTTPS 流量。

&ensp;&emsp;Https 默认端口为 443 端口，包括但不限于~ 部署一张 SSL 证书到服务器上，封死 53,853 。同时选择合适的上游 DNS ，最好用使用服务器终端 Ping 一番上游地址，否则，若是DNS 解析时间过长，直接影响浏览速度。