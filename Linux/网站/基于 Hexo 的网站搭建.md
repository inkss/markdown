# 基于 Hexo 的网站搭建

虽说我不是很想搭建一个博客网站，但是记录一下搭建博客的能力还是有必要的。（OS：Ubuntu 18.04）

## 一、前置环境安装

安装 Hexo ，顺序：NVM → Node.js → Hexo 

* NVM

```sh
wget -qO- https://raw.github.com/creationix/nvm/master/install.sh | sh
```

* Node.js

```sh
nvm install stable
```

* Hexo

```sh
npm install -g hexo-cli
```

以上所有步骤就可以将 Hexo 安装到你的电脑上了，总耗时 1-2 min 。

## 二、网站属性初配置

这部分真的有太多太多介绍了，这里流水记录下终端输入的命令：

（1）初始化博客

```shell
hexo init
```

（2）克隆主题
```shell
git clone git@github.com:dmego/hexo-theme-pure.git themes/pure
```

（3）安装插件
```shell
npm install hexo-wordcount --save
npm install hexo-generator-json-content --save
npm install hexo-generator-feed --save
npm install hexo-generator-sitemap --save
npm install hexo-generator-baidu-sitemap --save

npm install hexo-neat --save
npm install hexo-translate-title --save

npm install request
npm install hexo-deployer-git --save
npm install
```

（4）配置插件：在博客的 _config.yml 末尾添加以下内容
```js
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

# translate_title
translate_title:
  translate_way: google    #google | baidu | youdao
  youdao_api_key: XXX
  youdao_keyfrom: XXX
  is_need_proxy: false   #true | false
  proxy_url: http://localhost:1080
```

（5）配置网站属性：修改博客的  _config.yml  文件（部分内容）

```shell
# Site
language: zh-CN

# URL
permalink: :year/:month/:day/:translate_title/

# Directory
skip_render: README.md 

# Extensions
## Plugins: https://hexo.io/plugins/
## Themes: https://hexo.io/themes/
theme: pure

# Deployment
## Docs: https://hexo.io/docs/deployment.html
deploy:
  type: git
  repo: git@github.com:xxxx/xxxxxxx
  branch: master
```

（6）配置主题属性：修改主题的 _config.yml  文件（部分内容）

```shell
# 各种
```

（7）发布博客

```shell
hexo clean && hexo g -d
```
## 三、网站自定义修改

在这个主题之上又做了很多修改，这里总结一下：

* 去除 book 豆瓣书单的所有内容
* 把 link 友链当成留言板用，文本替换
* 主题用到的图片全部在本地删掉，换成对象存储的
* 关闭所有的统计，还有一些导航、分享内容之类的
* 修改文章的许可协议为 CC BY-NC-SA 4.0
* 主题配置文件里的连接全部换成 Https
* 资源文件能用 CDN 的就用，不能的就缓存
* 取消 TOC 的自动编号，直接应用文章中的标题

## 四、网站访问速度优化

（1）CDN 全站缓存

首先，本来就全都是 **静态文件** ，所以可以把 **缓存过期时间** 设置的久一些，有新内容的时候主动去服务商哪里提交更新缓存；然后借助 CDN 可以在域名解析上做处理：

* 对 CDN 的域名解析类型设置为 **国内** ，即国内地址走 CDN 加速。
* 而 **国外** 就直接解析到 Github 的的 IP 上，类型 A 记录。

（2）OSS 对象存储

俗称图床，图片等大文件放对象存储里进一步加速。另外，注意一下防盗链，毕竟免费额度有限。

（3）Https 加密传输

免费的 Https 证书很好申请，可以直接给 CDN 用，301 或者 302 跳转，这样就可以强制 Http 跳 Https 了。