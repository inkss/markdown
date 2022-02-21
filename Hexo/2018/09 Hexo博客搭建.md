---
title: Hexo 博客搭建
toc: true
indent: true
date: 2018/09/15 13:48
updated: 2020/05/12 11:27
tag:
  - 网站
  - Hexo
categories: 博客
abbrlink: 196d2ba7
description: 本文偏向于记录备份，非新手向教程，仅供参考。先说搭建这个博客的原因：在搭建博客之前我的所有的文章都是存储在 Github 仓库中，直到某天在投简历的时候发现有的要求填写博客链接 ，掐指一算在玩腻饥荒后似乎还闲置着一个腾讯云的学生主机，索性自己折腾一个博客出来。
headimg: ../../img/article/Hexo博客搭建/main.png
---

> 本文偏向于记录备份，非新手向教程，仅供参考。 :cat:

先说搭建这个博客的原因：在搭建博客之前我的所有的文章都是存储在 [inkss/markdown](https://github.com/inkss/markdown) 仓库中，直到某天 **在投简历的时候发现有的要求填写博客链接** ，掐指一算在玩腻饥荒后似乎还闲置着一个腾讯云的学生主机，索性自己折腾一个博客出来。而以简单快速为目的话，还是主推： `Hexo` 。

Hexo 可以帮忙生成全静态的网页，所以在存储位置上可以有：`Github Page、OSS 对象存储、云主机` 等多种选择。那么接下来就是研究一下如何使用这个框架，以及找一个好看顺心的主题（~~然后连续几天沉迷调试主题不可自拔...~~)。

## 1.基础环境

**基础环境**：`Git、Node、Hexo`；**OS** ：`Ubuntu 18.04 LTS` ；**服务器**：`CentOS 7.4` 。Git 和 Github 作为前置环境，本文不做重点阐述。Git 的安装命令：`sudo apt install git` 。

进入正文部分，这里的顺序是这样的：`NVM` → `Node.js` → `Hexo` 。

### 1.1 安装 NVM

```sh
# 打开终端，输入：
wget -qO- https://raw.github.com/creationix/nvm/master/install.sh | sh
```

### 1.2 安装 Node

```sh
# 打开新的终端，输入：
nvm install stable
```

### 1.3 安装 Hexo

```sh
# 打开新终端，输入：
npm install -g hexo-cli
```

以上所有步骤就可以将 Hexo 安装到你的电脑上了，总耗时 1-2 min 。

> 得益于某不可言的 BUFF 加持，在不作处理的情况下网速或能够让你绝望。两类选择：**换源** or **代理** 。
>
> 换源似乎是使用淘宝的 `cnpm` 源；虽然这里使用的是给终端设置代理：[Shadowsocks 设置终端代理](https://github.com/inkss/markdown/blob/master/Linux/%E7%BD%91%E7%AB%99/Shadowsocks%20%E8%AE%BE%E7%BD%AE%E7%BB%88%E7%AB%AF%E4%BB%A3%E7%90%86.md) 。

## 2.Hexo 使用

### 2.1 基础命令

这部分内容以 Hexo 官方文档为准，这里记录一些最常用的。传送门：[命令 | Hexo](https://hexo.io/zh-cn/docs/commands)

* 初始化：`hexo init && npm install`
* 生成博客：`hexo g`
* 部署博客：`hexo d`
* 清空缓存：`hexo clean`
* 启动本地服务器：`hexo s`
* 组合命令：`hexo clean && hexo g -d`

首先，拿到 Hexo 后，先是 **初始化** 一个文件夹，建立基本的框架结构，然后用 `npm install` 安装一些依赖。目录结构如下：

```sh
.
├── _config.yml            # 网站配置文件
├── .gitignore             # Git 忽略文件
├── node_modules           # 插件安装目录
├── package.json           # 描述插件
├── package-lock.json      # 描述插件 更详细
├── scaffolds              # 模板
├── source                 # 资源
└── themes                 # 主题
```

想必各位对 `.gitignore` 很眼熟吧，完全可以使用 `git init` 对这个文件夹初始化后上传到 Github 仓库。

而这样操作需要注意一点：一般主题文件多半是克隆自 Git 的，所以可能会出现嵌套的情况。可选的处理方案： 

* 将主题目录加入到忽略文件中
* 删除主题目录里的 `.git` 文件夹

> **关于恢复**：克隆仓库后，需要执行 `npm install --save` ，重新安装插件到 node_modules 目录中。

### 2.2 网站配置

**网站** 配置文件：`_config.yml` ，官方配置文档传送门：[配置 | Hexo](https://hexo.io/zh-cn/docs/configuration)

一般来说在该配置文件中只需要修改一些网站基本属性，下面记录一下指定主题和设置发布（部署）对象：

#### 2.2.1 修改主题类型

```yml
# Extensions
## Plugins: https://hexo.io/plugins/
## Themes: https://hexo.io/themes/
theme: pure
```

#### 2.2.2 部署到 Github

```yml
# Deployment
## Docs: https://hexo.io/docs/deployment.html
deploy:
  type: git
  repo: git@github.com:xxxx/xxxxx
  branch: master
```

#### 2.2.3 忽略解析指定的文件

```yml
# Directory
# 忽略解析指定文件：比如 Github 仓库的 ReadMe.md 文件。
skip_render: README.md
```

> PS：一些插件的属性设置也是在这个文件中配置的。

### 2.3 主题配置

> 如果不是找到了一个看着还很舒服的主题，肯定也没有那么大的兴趣折腾博客了。

这里使用的是 `Pure` ：原作者：[cofess](https://github.com/cofess) 、主题：[hexo-theme-pure](https://github.com/cofess/hexo-theme-pure) 。

#### 2.3.1 安装主题

```sh
# 在网站的根目录下执行
git clone git@github.com:dmego/hexo-theme-pure.git theme/pure
```

然后修改 **网站配置** 文件,修改主题：`theme: pure` 。

#### 2.3.2 安装插件

分为必装和可选两种吧，安装插件时终端目录需位于网站根目录下（Hexo 初始化的那个目录）。

* ① 必装插件（这部分主题 ReadMe 文件中有提及）：

```sh
npm install hexo-wordcount --save
npm install hexo-generator-json-content --save
npm install hexo-generator-feed --save
npm install hexo-generator-sitemap --save
npm install hexo-generator-baidu-sitemap --save
```

------

* ② 可选插件（一些需要在 **网站配置** 文件中修改属性）。

[hexo-neat](https://github.com/rozbo/hexo-neat)：压缩代码

```sh
npm install hexo-neat --save
```

```yml
# 网站配置文件 `_config.yml` 中添加：
# hexo-neat
neat_enable: true
neat_html:
  enable: true
  exclude: 
neat_css:
  enable: true
  exclude:
    - '*.min.css'
neat_js:
  enable: true
  mangle: true
  output:
  compress:
  exclude:
    - '*.min.js'
```

[hexo-translate-title](https://github.com/cometlj/hexo-translate-title)：自动翻译文章标题到英文

```sh
npm install hexo-translate-title --save
```

```yml
# 网站配置文件 `_config.yml` 中添加：
# translate
translate_title:
  translate_way: google    #google | baidu | youdao
  youdao_api_key: XXX
  youdao_keyfrom: XXX
  is_need_proxy: true     #true | false
  proxy_url: http://localhost:8123
```

```yml
# 修改文章的永久链接格式（同样是网站配置文件）：
# URL
permalink: :year/:month/:day/:translate_title/
```

[hexo-filter-github-emojis](https://github.com/crimx/hexo-filter-github-emojis)：支持 emoji 表情

```sh
npm install hexo-filter-github-emojis --save
```

```yml
# 网站配置文件 `_config.yml` 中添加：
# emoji
githubEmojis:
  enable: true
  className: github-emoji
  unicode: false
  styles:
    display: inline
  localEmojis:
```

------

* ③ 部署到 Github 所必须的插件：

```sh
nom install request --save
npm install hexo-deployer-git --save
```

#### 2.3.3 配置主题

主题所在目录下也有一份 `_config.yml` 文件，这里称它为 **主题配置** 文件。

可以在此处对主题属性进行修改，附：图片类的资源可以直接写外链进去。

## 3.自定义主题

> **此部分内容仅供参考，或因主题变更已不适用。2018.10.08 补充。**

### 3.1 CSS 样式修改

主要是网页样式微调，改改字体什么的。

**文件地址：** `themes/pure/source/css/style.css`

#### 3.1.1 修改字体大小

默认的字体是 14px ，似乎有些小了，所以将其改为 16px 。

```css
body {
  padding-right: 0 !important;
  font-family: -apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,"PingFang SC","Hiragino Sans GB","Microsoft YaHei",sans-serif;
  font-size: 16px;
  line-height: 1.42857;
  color: #333333;
  background-color: #fff;
}
```

#### 3.1.2 修改代码样式

原主题无法显示出这样的效果： `code` ，从 Github 扒的样式，这里对其补充。

```css
code {
  padding: 0.2em 0.3em;
  margin: 0;
  font-size: 85%;
  background-color: rgba(27,31,35,0.05);
  border-radius: 3px;
}
```

#### 3.1.3 修改图形居中

原文章中图片没有居中，这里将图片的显示更改为居中。

```css
.main img{
  box-sizing: border-box;
  margin: auto;
  padding: 3px;
  text-align: center;
  display: block;
}
```

#### 3.1.4 修改代码字体

Windows 下代码字体显示不是很美观，这里更改。

```css
code,
kbd,
pre,
samp {
  font-family: "SFMono-Regular",Consolas,"Liberation Mono",Menlo,Courier,monospace;
  font-size: 1em;
}
```

### 3.2 恢复 referrer

【已解决】详细见：[#43](https://github.com/cofess/hexo-theme-pure/issues/43)

### 3.3 TOC 编号

TOC 编号，就是当展开目录时，原章节的目录内容附加了一次自动编号，如果文章本身存在着编号那么编号就重复了，所以此处更改为取消自动编号。

**文件地址：** `themes/pure/layout/_partial/sidebar-toc.ejs`

```js
<div class="slimContent">
    <nav id="toc" class="article-toc">
      <h3 class="toc-title"><%= __('article.catalogue') %></h3>
      <%- toc(post.content,{list_number: false}) %>
    </nav>
</div>
```

### 3.4 回到顶部

长文章怎么能没有一个回到顶部的功能，然而恰巧主题没有，那么添加一个吧（位于右下角）。

**文件地址：** `themes/pure/layout/_commonn/script.ejs`

```html
<div id="go-top"></div>
```

```css
<style type="text/css">
#go-top {
 width:40px;height:36px;
 background-color:#777;
 position:relative;
 border-radius:2px;
 position:fixed;right:10px;bottom:60px;
 cursor:pointer;display:none;
}
#go-top:after {
 content:" ";
 position:absolute;left:14px;top:14px;
 border-top:2px solid #fff;border-right:2px solid #fff;
 width:12px;height:12px;
 transform:rotate(-45deg);
}
#go-top:hover {
 background-color:#333;
}
</style>
```

```js
<script>
$(function () {
  var top=$("#go-top");
  $(window).scroll(function () {
    ($(window).scrollTop() > 300) ? top.show(300) : top.hide(200);
    $("#go-top").click(function () {
      $('body,html').animate({scrollTop:0});
      return false();
    })
  });
});
</script>
```

### 3.5 数学公式支持

需要先更换 Hexo 的渲染引擎：

```sh
npm un hexo-renderer-marked --save
npm i hexo-renderer-markdown-it-plus --save
```

接着在网站配置文件末尾添加以下内容：

```yml
markdown_it_plus:
    highlight: true
    html: true
    xhtmlOut: true
    breaks: true
    langPrefix:
    linkify: true
    typographer:
    quotes: “”‘’
    plugins:
        - plugin:
            name: '@iktakahiro/markdown-it-katex'
            enable: true
```

最后需要修改主题文件，更新 Katex 版本。

**文件地址：** `themes/pure/layout/_commonn/plugin.ejs`

```js
<% if (page.mathjax) { %>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/katex@0.10.0-rc.1/dist/katex.min.css" integrity="sha384-D+9gmBxUQogRLqvARvNLmA9hS2x//eK1FhVb9PiU86gmcrBrJAQT8okdJ4LMp2uv" crossorigin="anonymous">
  <script defer src="https://cdn.jsdelivr.net/npm/katex@0.10.0-rc.1/dist/katex.min.js" integrity="sha384-483A6DwYfKeDa0Q52fJmxFXkcPCFfnXMoXblOkJ4JcA8zATN6Tm78UNL72AKk+0O" crossorigin="anonymous"></script>
<% } %>
```

## 4.网站优化

> 这部分内容将会涉及：自定义域名解析、CDN 分发、OSS 对象存储、Https 加密链接等。
>
> 本博客所使用的所有服务均来自于腾讯云。:kissing_heart:

### 4.1 自定义域名解析

Github 给的默认域名形如 `xxxx.github.io` 的样式，如果拥有自己的域名话就可以对 DNS 设置一条指向该域名的 CNAME 解析。此外在网站的 `source` 目录下也需要放置一个文件：`CNAME` ，内容上只需要填写你的个人域名。

### 4.2 CDN 分发

Github Page 的服务器毕竟不在国内（查过图片的 CDN 发现是来自日本的），虽然国外访问速度还好，但是国内访问速度就很悲伤了。不过国外的访问速度也不能浪费，所以上文的 DNS 记录就分为国内、国外两个：照顾所有位置的访问速度。

> 腾讯云的 CDN 似乎有 10G 免费额度，付费额度：100GB 20元/6个月。

#### 4.2.1 添加域名

申请 CDN 时，有两个需要填写的：**源站类型** 和 **域名** 。

域名自然是需要加速的域名，而源站类型则有以下几种：*源站 IP* 、*源站域名* 、 *对象存储* 。

**对象存储** 只能是腾讯云的，这里先不展开；**源站域名** 的话有一个前置要求：需要备案。

所以最方便的莫过于选择 **源站 IP** ，IP 的获取方法是用 `ping` 命令访问 Github 给你的域名后得到的。

当然，如果有自己的云主机，此处填写自己的主机 IP 也是可以的，本博客便是这样做的：好处是如果访问链接没有命中 CDN ，国内服务器的访问速度也比国外较快（回源时的时间消耗减少），同时 CDN 获取源站信息也有速度优势。

#### 4.2.2 缓存配置

另外一个需要关注的就是：**缓存配置** （缓存过期配置）。

Hexo 生成的网页是全静态的，为了提高 CDN 命中率，可以把全站缓存的时间设置的相对久一些。

### 4.3 Https 链接

Github **本身是支持** 设置 Https 链接的，证书由 Github 自动颁发、续期。不过需要手动开启：`Enforce HTTPS`

{% gallery %}
![Github 开启 Https 链接](../../img/article/Hexo博客搭建/01.png)
{% endgallery %}

此外，如果使用了和本博客类似的手段：DNS 根据 IP 指向不同域名的话，**在访问者位于国内时，仍需要配置一个证书**。原因是国内的 IP 被 DNS 指向了 CDN 域名了，没有指向 Github ，那么 Github 给的证书便是无效不起作用，这里有两种选择：

* 没有云主机的可以通过 CDN 开启强制跳转 HTTPS （国外访问 Github 会帮助你自动跳转）。
* 有云主机的设置服务器的 HTTP 强制跳转 HTTPS ，也可以同时勾选 CDN 的强制跳转。

> 在腾讯云可以免费申请到一年的 SSL 证书，一般一至两天申请便可以批下来。云主机配置可以参看这篇文章：[CentOS PHP 环境配置 [LAMP]](https://github.com/inkss/markdown/blob/master/Linux/%E6%9C%8D%E5%8A%A1%E5%99%A8/CentOS%20%E6%9C%8D%E5%8A%A1%E5%99%A8%E9%85%8D%E7%BD%AE.md)

### 4.4 OSS 对象存储

如果以减少成本为目的的话，最好的选择不是云主机而是 **对象存储** 。关于免费额度：**`50GB 空间、10 GB 流量/CDN 回源、100 万次请求`**。

如果是只当图床使用，一般默认的配置即可；如果是当静态网页使用需要先手动开启这个功能，然后把域名 **解析到对象存储分配给你的域名上** 。此外对象存储也支持 CDN ，不过同样也需要手动开启。最后的最后还需要将 Hexo 网页的部署对象换成腾讯云的 OSS （有相关插件）。

> 备注：记得给对象存储或者 CDN 开启防盗链。:anchor:

## 5.Hexo 部署

> 这部分单独拿出来说，因为现在一直在做一个假设：当服务器 **宕机/停止续费** 后如何保证网站的正常访问。
>
> 此外还有一个需求就是：降低网站总开销（最大的开支应该会出自服务器上）。

### 5.1 域名解析 DNS

先谈谈防止宕机，前面域名部分提到了现在对域名是直接解析，依靠 IP 地址指向不同的域名。

国外流量指向 Github Page ，这部分不用担心；而国内流量则被指向到腾讯云的 CDN，所以需要修改这里。

> 此处开支：.cn 域名 35 元/年、DNS 解析套餐免费。 

### 5.2 CDN 热备

CDN 的自有源支持热备，++也就是主源宕机，热备回源++。那么重点也就是这儿了：设置热备源：`xxxx.github.io` 。

> 此处开支：免费 10GB 0元/月、付费 100GB 20元/6月。

### 5.3 对象存储

主机宕机的问题解决了，但是如果主机停止续费呢，CDN 从 Github 回源又降低了速度，所以就考虑了 OSS 。

此部分的大标题叫做：Hexo 部署，既然是部署自然也就想到部署到对象存储里。需要插件：

```sh
npm install hexo-deployer-cos-enhanced --save
```

使用方法见：[hexo-deployer-cos-enhanced](https://github.com/75k/hexo-deployer-cos-enhanced/blob/master/README.md) 。相关额度如下：

| 资源类型 | 资源子类型          | 每月免费额度 |
| -------- | ------------------- | ------------ |
| 存储空间 | 存储空间            | 50 GB        |
| 流量     | 外网下行流量        | 10 GB        |
| 流量     | 腾讯云 CDN 回源流量 | 10 GB        |
| 请求     | 读请求              | 100 万次     |
| 请求     | 写请求              | 100 万次     |
