---
title: 宝塔面板部署 ModSecurity
toc: true
indent: true
tag:
  - 宝塔
  - Nginx
  - Ubuntu
  - ModSecurity
  - WAF
categories: 教程
description: 本文介绍了如何部署开源免费的 ModSecurity 防火墙，并导入相应的规则集文件等。
date: '2022-03-25 12:46'
updated: '2022-03-25 12:46'
references:
  - title: Compilation recipes for v3.x
    url: >-
      https://github.com/SpiderLabs/ModSecurity/wiki/Compilation-recipes-for-v3.x
  - title: 宝塔面板搭建 WEB 防火墙 ModSecurity 3.0 Nginx
    url: 'https://www.ydqic.com/130.html'
  - title: How to Install ModSecurity for Nginx on Debian/Ubuntu
    url: 'https://www.tecmint.com/install-modsecurity-nginx-debian-ubuntu/'
  - title: ModSecurity 误拦截处理方法（ModSecurity 白名单）
    url: 'http://www.modsecurity.cn/practice/post/6.html'
  - title: ModSecurity 中文手册（推荐查阅）
    url: 'http://www.modsecurity.cn/'
abbrlink: f0451fab
headimg: ../../img/article/22-03@宝塔面板部署ModSecurity/Hexo博客封面.png
---

宝塔面板的 WAF 防火墙太贵啦，买不起的说，不如支持下社区开源免费的 ModSecurity 防火墙啦。下面是本次使用到的环境：

- Nginx：v1.21.4
- OS：Ubuntu 20.04.4 LTS x86_64(Py3.7.9)
- [ModSecurity](https://github.com/SpiderLabs/ModSecurity)：v3/master
- [ModSecurity-nginx](https://github.com/SpiderLabs/ModSecurity-nginx)：v1.0.2
- [coreruleset](https://github.com/coreruleset/coreruleset)：v3.3.2

## 二、ModSecurity 部署

总的流程如下：

![流程](../../img/article/22-02@宝塔面板配置/流程.svg)

### 1. 编译部署 ModSecurity

- ① 安装依赖环境

```sh
sudo apt install make gcc build-essential autoconf automake libtool libfuzzy-dev ssdeep gettext pkg-config libcurl4-openssl-dev liblua5.3-dev libpcre3 libpcre3-dev libxml2 libxml2-dev libyajl-dev doxygen libcurl4 libgeoip-dev libssl-dev zlib1g-dev libxslt-dev liblmdb-dev libpcre++-dev libgd-dev
```

- ② 拉取资源仓库

```sh
cd /home/ubuntu/WAF
git clone https://github.com/SpiderLabs/ModSecurity.git
cd ModSecurity/
git submodule init
git submodule update
```

- ③ 构建环境

```sh
sudo ./build.sh 
sudo ./configure
```

- ④ 编译部署

```sh
sudo make -j4
sudo make install
```

### 2. 下载连接件

- ① 拉取资源仓库

```sh
cd /home/ubuntu/WAF
git clone https://github.com/SpiderLabs/ModSecurity-nginx.git
```

- ② 检出到最新正式版，具体版本见：[ModSecurity-nginx/releases](https://github.com/SpiderLabs/ModSecurity-nginx/releases)

```sh
cd ModSecurity-nginx
git checkout v1.0.2
```

### 3. 编译安装 Nginx

将 ModSecurity-nginx 以动态模块的方式添加至 Nginx，由于宝塔面板的安装特性，你需要先卸载已经存在的 Nginx 应用。在【应用商店-运行环境】中安装 Nginx，选择编译安装，添加模块：

```conf 命令填写如下内容，其他随意
--with-compat --add-dynamic-module=/home/ubuntu/WAF/ModSecurity-nginx
```

{% note warning:: 注：重装 Nginx 应用会丢失主配置文件，网站配置文件不会丢失。 %}

### 4. 下载规则集

- ① 拉取资源仓库

```sh
cd /home/ubuntu/WAF
git clone https://github.com/coreruleset/coreruleset.git
```

- ② 检出到最新正式版，具体版本见：[coreruleset/releases](https://github.com/coreruleset/coreruleset/releases)

```sh
cd ModSecurity-nginx
git checkout v3.3.2
```

### 5. 整合配置文件

- ① 开启 Modsecurity 的阻止攻击

```sh
cd /home/ubuntu/WAF/ModSecurity
cp modsecurity.conf-recommended modsecurity.conf
```

将 `SecRuleEngine DetectionOnly` 修改为 `on`，DetectionOnly 表示仅记录，修改为 on 用以阻止攻击。

```sh
vim modsecurity.conf
```

```conf
SecRuleEngine On
```

- ② 配置规则集文件

```sh
cd /home/ubuntu/WAF/coreruleset
cp crs-setup.conf.example crs-setup.conf
```

- ③ 新增配置文件

```sh
cd /home/ubuntu/WAF
touch modsec_includes.conf
```

写入如下内容：

```conf
include ModSecurity/modsecurity.conf
include coreruleset/crs-setup.conf
include coreruleset/rules/REQUEST-901-INITIALIZATION.conf
include coreruleset/rules/REQUEST-903.9002-WORDPRESS-EXCLUSION-RULES.conf
include coreruleset/rules/REQUEST-905-COMMON-EXCEPTIONS.conf
include coreruleset/rules/REQUEST-911-METHOD-ENFORCEMENT.conf
include coreruleset/rules/REQUEST-912-DOS-PROTECTION.conf
include coreruleset/rules/REQUEST-913-SCANNER-DETECTION.conf
include coreruleset/rules/REQUEST-920-PROTOCOL-ENFORCEMENT.conf
include coreruleset/rules/REQUEST-921-PROTOCOL-ATTACK.conf
include coreruleset/rules/REQUEST-930-APPLICATION-ATTACK-LFI.conf
include coreruleset/rules/REQUEST-931-APPLICATION-ATTACK-RFI.conf
include coreruleset/rules/REQUEST-932-APPLICATION-ATTACK-RCE.conf
include coreruleset/rules/REQUEST-933-APPLICATION-ATTACK-PHP.conf
include coreruleset/rules/REQUEST-941-APPLICATION-ATTACK-XSS.conf
include coreruleset/rules/REQUEST-942-APPLICATION-ATTACK-SQLI.conf
include coreruleset/rules/REQUEST-943-APPLICATION-ATTACK-SESSION-FIXATION.conf
include coreruleset/rules/REQUEST-949-BLOCKING-EVALUATION.conf
include coreruleset/rules/RESPONSE-950-DATA-LEAKAGES.conf
include coreruleset/rules/RESPONSE-951-DATA-LEAKAGES-SQL.conf
include coreruleset/rules/RESPONSE-952-DATA-LEAKAGES-JAVA.conf
include coreruleset/rules/RESPONSE-953-DATA-LEAKAGES-PHP.conf
include coreruleset/rules/RESPONSE-954-DATA-LEAKAGES-IIS.conf
include coreruleset/rules/RESPONSE-959-BLOCKING-EVALUATION.conf
include coreruleset/rules/RESPONSE-980-CORRELATION.conf
```

- ④ 加载 Nginx 连接模块

宝塔面板找到 Nginx 设置，选择配置修改，添加如下内容：

{% codeblock 添加高亮行内容，位置见结构 lang:conf mark:2,13-14  %}
worker_rlimit_nofile 51200;
load_module modules/ngx_http_modsecurity_module.so;

events
{
  use epoll;
  worker_connections 51200;
  multi_accept on;
}

http
{
  modsecurity on;
  modsecurity_rules_file /home/ubuntu/WAF/modsec_includes.conf;

  include       mime.types;
{% endcodeblock %}

保存并重启 Nginx 即可~

## 二、额外

### 1. 测试 ModSecurity 是否生效

修改 `/home/ubuntu/WAF/ModSecurity/modsecurity.conf`，添加如下内容：

{% codeblock 添加高亮行内容，位置见结构 lang:conf mark:2  %}
SecRuleEngine On
SecRule ARGS:testparam "@contains test" "id:1000,deny,status:403,msg:'Test Successful'"
{% endcodeblock %}

重启 Nginx 服务器，浏览器访问带有 `?testparam=test` 后缀的 URL，如果收到 `403 Forbidden` 提示则代表规则已经生效，同时你还可以在对应网站的错误日志中找到本次拦截记录。

### 2. 白名单调整

事实上宝塔面板的操作过程中会撞到很多个规则，这些或多或少都可以归类为误判，所以我们需要将宝塔面板的所有操作加入进白名单中，这里采用的是校验请求头的方式，通过检查指定请求头的参数值，如果符合匹配的结果则放行。因为安全考虑为了隐藏服务器的真实 IP，在通过域名访问面板前是套有一层 CDN，所以我们可以在各家的 CDN 配置处手动添加特殊的请求头用以校验。

同样是修改 *modsecurity.conf* 文件，在 `SecRuleEngine On` 后添加如下内容：

```conf 如果访问的请求头中包含 head-check: test 则放行所有规则
SecRule REQUEST_HEADERS:head-check "test" "id:1002,phase:1,pass,nolog,ctl:ruleEngine=Off"
```

### 3. Cloudflare 添加自定义请求头

这里以 Cloudflare 举例如何添加请求头，找到 **规则** ➡ **转换规则** 中的 **HTTP 请求头修改**，传入请求匹配可以根据你的真实情况填写，修改请求头使用 `Set static`，然后正常填入你自己的 *标头名称* 和 *值* 即可。

### 三、其它

关于 ModSecurity，与宝塔的插件版本无非就是少了可视化界面，虽然这也是面板类应用存在的意义。
