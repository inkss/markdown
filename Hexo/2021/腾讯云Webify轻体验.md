---
title: 腾讯云 Webify 轻体验
toc: true
indent: true
tag:
  - Hexo
  - Webify
categories: 资料
date: '2021-09-18 02:29'
updated: '2021-09-18'
hideTitle: true
headimg: ../../img/article/腾讯云Webify轻体验/ezgif-6-8de51b74f543.webp
background: ../../img/article/腾讯云Webify轻体验/wallhaven-rdplk7.jpg
description: 腾讯云站点扶持计划 —— Webify，博客托管的新选择。
music:
  enable: true
  server: tencent
  type: song
  id: 000RSpQM0n7KBP
abbrlink: 5134cc9f
---


前些日子收到了一封腾讯云的站内信『Webify 个人网站扶持计划：免费托管你的网站』，秉着体验的态度测试了下，一句话概括如下：

> {% span logo , 博客托管的新选择，稳定可靠的境内托管方式 %}。

## 一、Webify

对于 Hexo 这类静态博客来说，主观上体验类似于腾讯云的已有功能：位于 **云开发** 下的 **Web 应用托管**，Webify 则是对按量付费的免费扶持^[以代金券的形式展开：一张面值 300.00 元为期 60 个月的云开发无门槛券。]。关系类似于又拍云 CDN 和它的又拍云联盟计划，核心都需要在站点中添加指向到对方地址的链接，利用优惠券减免支出。*{% psw 俗称白嫖！ %}*

于用户来说，Webify 实现了从网站托管到 CDN 加速的整个流程，你只需要提供一个原始文件仓库，一个域名（*可选*）以及一份证书^[按照其微信公众号说明：Webify 计划提供免费的 DV 型证书，并提供自动续期功能。]便完成了站点的建立，整个流程简洁、快速、优雅。此外从官方文档中也表示，未来其也拥有支持动态服务的可能，不局限于纯静态网站。

### With Hexo

我们先不关注那些令人眼花缭绕的模板，就简单的说说对于 Hexo，Webify 有那些优点。

如果你需要部署博客供其他人访问，直觉上你可以选择 Github Page，进一步还可以使用 Vercel。但若是博客的目标人群为中文用户，它们的境内访问体验可能不那么理想/稳定；如果是选择使用境内资源进行托管，对标 Github Page 服务的是 Gitee Pages，但是根据朋友们的反馈，Gitee 由于某些原因总归是不那么方便。而另一个选择是部署到对象存储，以腾讯云举例，在 COS 发展的初期，其为用户提供了一定的免费额度，这些额度足够支撑小型博客的运行了，但是好巧不巧的是：目前这个免费额度对于新用户来说只有六个月 {% emoji nanguo %}。

所以，对于没有免费 COS 额度的用户来说，**既想使用境内的 CDN 资源加速访问又不想付费** 的话，Webify 是境内访问下对 COS 部署方案的完美替代。

### Other

然而，事实上，Webify 目前只有上海节点，所以自定义域名这块你还是需要备案，心理有芥蒂的小伙伴们这个还真是没辙，不过若是不在乎这点，那它还是蛮优秀的，流程简单、易上手。

## 二、CI

CI，Webify 具有自动部署的能力，类似于 Github Action，不过目前自定义的程度不高。

具体来说，你拥有两种选择，一是使用 Webify 进行博客部署，新建应用时导入的仓库为 **博客源码仓库**，框架预设选择 **Hexo**；另一种则是不使用 Webify 的自动部署，保持原先 deploy 到 Github 仓库的方式，此时新建应用导入的则是你 **网站源码仓库**，框架预设选择 **纯静态页面** 即可。

其实按照提示 Hexo 模板只是执行了 `npx hexo generate` 命令，有些时候可能不够用，你可以在 `package.json` 中添加自定义 `scripts` 命令，举个栗子：`"rebuild": "hexo clean && hexo g && && gulp"`，如此调用命令修改为 `npm run rebuild` 也不是说不行。

{% gallery::3::show %}
![纯静态部署](../../img/article/腾讯云Webify轻体验/image-20210918015250502.png)

![预设框架：Hexo](../../img/article/腾讯云Webify轻体验/image-20210918015319447.png)

![自定义设置](../../img/article/腾讯云Webify轻体验/image-20210918015341483.png)
{% endgallery %}

## 三、More

本站目前已经迁移到腾讯云 Webify 中，欢迎交流，以及这里是一些额外的信息：

<style>
.link-card .left img {
  border-radius: 50%;
}
</style>  

{% link 云开发 Webify 官网::https://webify.cloudbase.net/::../../img/article/腾讯云Webify轻体验/favicon.png %}

{% link Webify 个人站点扶持计划::https://cloud.tencent.com/developer/article/1871549::../../img/article/腾讯云Webify轻体验/favicon.png %}

