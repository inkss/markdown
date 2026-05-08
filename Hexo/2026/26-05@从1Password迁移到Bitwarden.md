---
title: 从 1Password 迁移到 Bitwarden：一次涨价引发的“搬家”
title_display: true
toc: true
indent: true
tag:
  - 1Password
  - Bitwarden
  - Docker
categories: 杂谈
description: >-
  因 1Password 订阅涨价 33%，从使用六年的 1Password 迁移到自部署的
  Bitwarden。本文记录迁移的原因、过程，以及迁移后发现的不兼容问题。
date: '2026-05-08 11:55'
updated: '2026-05-08 11:55'
abbrlink: 5a013cf6
headimg: https://cdn.jsdelivr.net/gh/inkss/inkss-cdn@main/img/article/26-05@从1Password迁移到Bitwarden/Hexo博客封面.png
headimg_display: false
---

2026 年 2 月 25 日，1Password 发来一封涨价通知邮件，个人订阅从 $35.88/年 涨到 $47.88/年，涨幅达 33%。果断取消订阅，慢走不送~ {% emoji xieyan %}

<!-- more -->

## 一、涨价风波

我是在二月下旬收到 1Password 的涨价邮件的。大概意思就是：{% wavy 我们加了很多新功能、好多年没涨价了、还要继续投入研发和安全基础设施 %}。但其中那句“AI 驱动”怎么看都更像是借着订阅制绑架用户付费。

> “We are investing heavily in new technologies, including AI‑powered capabilities, to improve how you protect and access your data.”

问题是，这些所谓的“AI 功能”^[AI 辅助安全检查、自动填写与识别敏感信息、风险判断与反钓鱼提示等。]我几乎用不到，却要为此多付 **33%**。怎么算都不划算。邮件收到的当天就去后台取消了订阅。这不是冲动，而是认真评估后的结论——它真的不值。

## 二、与 1Password 的六年

回头看，我最早注册 **1Password** 是在 2020 年 9 月（见：[关于个人密码安全的三两事](https://me.szyink.com/archives/543/)）。那时候我开始认真对待密码安全这件事：不想再依赖浏览器那种“能用但不太放心”的粗放式密码管理，也想彻底摆脱“多个网站共用同一个密码”的坏习惯。综合口碑和体验之后，最后选了支持全平台的 1Password。

说实话，即便在涨价之前，它的订阅价格对我来说也不算便宜，但考虑到安全性和使用体验，勉强还能接受。早期对国内用户来说，付费流程相当麻烦：不支持银联，也不支持常见的移动支付方式。发邮件询问后给的建议是用 PayPal 买礼品卡再充值到账户余额里[^1]，流程繁琐不说，又会造成余额溢出。 期间我也试过两年 **家庭版拼车**，但限制很明显：只能使用默认的个人密码库，不能创建其他类型的 Vault[^2]，用久了非常别扭。

最糟糕的是，家庭版订阅到期后，1Password 会直接 **冻结账户**。虽然数据不会被删，但你既不能继续用，也不能主动清除数据，只能被动卡在那里。这种“被锁住但又删不掉”的状态，让人非常不舒服。

![订阅账单](https://cdn.jsdelivr.net/gh/inkss/inkss-cdn@main/img/article/26-05@从1Password迁移到Bitwarden/image-20260508091039498.png)

[^1]: 直到 2024 年末，1Password 全面切换至 Stripe Billing 后，才得以支持国内的银联信用卡支付。
[^2]: 虽然管理员无法查看你的密码，但他们却可以更改除个人密码库之外所有密码库的归属。

## 三、迁移到 Bitwarden

由于我的订阅要到十月份才到期，原本并未打算立刻迁移。直到刷到一篇介绍如何在安卓设备上通过 Bitwarden 使用 **通行密钥（Passkey）** 登录网站的帖子，起了兴致。考虑到最终还是要迁移，便趁着有闲暇时间，提前开始迁移计划。

Bitwarden 有一个开源的社区后端实现 **Vaultwarden**，支持通过 Docker 自部署，这确保数了据掌握在自己手中，同时也能够导入 1Password 的导出数据。开源 + 代码透明，可谓是不二之选，计划通~

详细的部署过程可以参考这份整理过的文档，相当简单：

{% link Bitwarden Docker 部署文档, https://cnb.cool/inkss/qclaw_workspace-agent-6ba8207d/-/tree/main/docker/2026-04-10-bitwarden %}

## 四、迁移与使用体验

### 不兼容项目

迁移过程中遇到的问题如下：

0. **导入失败问题**

   1pux 文件在 Bitwarden 客户端始终无法成功导入，通过 Vaultwarden 的网页版才顺利完成导入。

1. **保险库未导入**

   1Password 中的“保险库”（Vault）概念在 Bitwarden 中对应的是“文件夹”（Folder），但两者并非一一映射关系。导入后所有条目都变成“无文件夹”，需要手动重新归类。

2. **分组信息丢失**

   1Password 的 Group 概念在 Bitwarden 中不存在。导入后组名丢失，组内条目会被统一归类进自定义字段。

3. **多行自定义字段被丢弃**

   Bitwarden 只有备注字段支持换行，自定义字段不支持。导入逻辑是：

   - 如果备注为空 → 将多行内容写入备注
   - 如果备注已有内容 → **直接丢弃这些文本**（非常坑）

4. **已链接的应用丢失**

   1Password 的“已链接的应用”登录方式在 Bitwarden 中没有对应功能，导入后全部丢失。

5. **登录方式元信息丢失**

   1Password 的“登录方式为”（Sign in with）元数据在 Bitwarden 中没有对应字段，导入后直接消失。

6. **URL 填充规则丢失**

   1Password 的 URL 网址自动填充规则在 Bitwarden 中完全无法导入。

7. **附件与通行密钥无法迁移**

   条目附件会被丢弃，通行密钥属于 1Password 不允许导出的内容[^3]，无法迁移。

### 使用体验

Bitwarden 和 1Password 在日常使用上的差异，最明显的主要有三点：

1. **界面与图标体验**

   Bitwarden 的 UI 确实不如 1Password 精致，尤其是条目详情页的视觉层级和信息密度更“工具化”。另外，网站图标（favicon）偶尔会拉取失败，导致部分条目显示默认图标，观感上略显粗糙。

2. **解锁体验割裂**

   桌面版应用解锁后，浏览器扩展不会同步解锁。只要关闭再打开浏览器，就必须重新在扩展里输入一次主密码或使用生物识别解锁。这点和 1Password 的“统一解锁体验”差距明显，个人使用观感最为强烈。

3. **搜索能力差异**

   在 1Password 中，可以直接输入通行密钥直接筛选出所有设置了该信息的条目，但在 Bitwarden 不可以，它也不可以筛选备注（附加字段）中的关键字，搜索范围有限。


[^3]: 1Password 可以在 iPhone 上通过 Apple 的 Credential Exchange 标准来转移通行密钥到其他支持的应用。

## 五、碎碎念

说实话，我对密码管理器的要求一直都很朴素：**全平台可用、体验稳定[^4]并接受付费**。密码管理器的核心价值本来也就很简单——**安全存储 + 自动填充**。但 1Password 这次以“AI 投入”为名的涨价，把价格从“还能接受”推向“有点贵”后，就成了压死骆驼的最后一根稻草。

在取消订阅后，这部分支出转移到腾讯云的轻量应用服务器上，{% emoji feiwen %} 意外之喜~

[^4]: 基础能力要求：密码记录 + 附件存储 + TOTP +  Passkey。
