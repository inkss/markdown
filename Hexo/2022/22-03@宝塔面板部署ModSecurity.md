---
title: 宝塔面板部署 ModSecurity
toc: true
indent: true
tag:
  - 宝塔面板
  - ModSecurity
  - Nginx
categories: 网络技术
description: 本文介绍在宝塔面板中部署开源防火墙 ModSecurity 的方法，包括编译安装 ModSecurity、配置连接件与 Nginx 模块、整合规则集等。
date: '2022-03-25 12:46'
updated: '2026-06-10 09:00'
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
headimg: https://cdn.jsdelivr.net/gh/inkss/inkss-cdn@main/img/article/22-03@宝塔面板部署ModSecurity/Hexo博客封面.png
---

宝塔面板的 WAF 防火墙太贵啦，买不起的说，不如支持下社区开源免费的 ModSecurity 防火墙啦。下面是本次使用到的环境：

- Nginx：v1.24.0+
- OS：Ubuntu 24.04.4 LTS x86_64
- [ModSecurity](https://github.com/SpiderLabs/ModSecurity)：v3/master
- [ModSecurity-nginx](https://github.com/SpiderLabs/ModSecurity-nginx)：v1.0.4
- [coreruleset](https://github.com/coreruleset/coreruleset)：v4.27.0

## ModSecurity 部署

总的流程如下：

![流程](https://cdn.jsdelivr.net/gh/inkss/inkss-cdn@main/img/article/22-02@宝塔面板配置/流程.svg)

```code 目录结构示意
/opt/app/waf/
├── ModSecurity/                # 源码目录（保留，但配置文件不直接用）
├── coreruleset/                # CRS 规则集目录
│   ├── crs-setup.conf
│   └── rules/
├── modsecurity/                # 存放 ModSecurity 配置文件
│   ├── modsecurity.conf        # 主配置（从源码复制并修改）
│   ├── unicode.mapping         # 字符映射文件（必须）
│   └── main.conf               # 入口文件（替代 modsec_includes.conf）
├── rules/                      # 自定义规则目录
│   ├── whitelist.conf
│   ├── disablerule.conf
│   └── REQUEST-900-EXCLUSION-RULES-BEFORE-CRS.conf  # CRS 前置排除规则
└── logs/                       # 可选：审计日志输出目录
```

## 1. 编译部署 ModSecurity

- ① 安装依赖环境

```sh
sudo apt install -y git build-essential autoconf automake libtool pkg-config libpcre3-dev libxml2-dev libyajl-dev libcurl4-openssl-dev zlib1g-dev libssl-dev libmaxminddb-dev libpcre2-dev
```

- ② 拉取资源仓库

```sh 具体的存放目录随意，这个不重要，我是偷懒放到了 '/opt/app/waf' 目录下。
cd /opt/app/waf/
git clone https://github.com/SpiderLabs/ModSecurity.git
cd ModSecurity/
git submodule update --init --recursive
```

- ③ 构建环境

```sh
sudo ./build.sh 
sudo ./configure
```

- ④ 编译部署

```sh
sudo make -j"$(nproc)"
sudo make install
```

## 2. 下载连接模块

- ① 拉取资源仓库

```sh
cd /opt/app/waf/
git clone https://github.com/SpiderLabs/ModSecurity-nginx.git
```

- ② 检出到最新正式版，具体版本见：[ModSecurity-nginx/releases](https://github.com/SpiderLabs/ModSecurity-nginx/releases)

```sh
cd ModSecurity-nginx
git checkout "$(git describe --tags --abbrev=0)"
```

## 3. 编译安装 Nginx

将 ModSecurity-nginx 以动态模块的方式添加至 Nginx，由于宝塔面板的安装特性，你需要先卸载已经存在的 Nginx 应用。在【应用商店-运行环境】中安装 Nginx，选择编译安装，添加模块：

```conf 命令填写如下内容，其他随意
--with-compat --add-dynamic-module=/opt/app/waf/ModSecurity-nginx
```

{% note warning:: 注：重装 Nginx 应用会丢失主配置文件，网站配置文件不会丢失。 %}

## 4. 整合规则集

- ① 拉取资源仓库

```sh
cd /opt/app/waf/
git clone https://github.com/coreruleset/coreruleset.git
```

- ② 检出到最新正式版，具体版本见：[coreruleset/releases](https://github.com/coreruleset/coreruleset/releases)

```sh
cd coreruleset
git checkout "$(git describe --tags --abbrev=0)"
cp crs-setup.conf.example crs-setup.conf
```

- ③ 创建 ModSecurity 配置目录

```sh
cd /opt/app/waf/ && mkdir -p modsecurity

# 复制主配置模板和字符映射文件
cp ModSecurity/modsecurity.conf-recommended modsecurity/modsecurity.conf
cp ModSecurity/unicode.mapping modsecurity/
```

## 5. 定制配置文件

- ① 编辑 `modsecurity/modsecurity.conf`，修改或新增以下内容：

| 参数                          | 推荐值                    | 说明                                        |
| :---------------------------- | :----------------------- | :------------------------------------------ |
| `SecRuleEngine`               | `DetectionOnly`          | **初期仅记录不阻断**，确认无误后再改为 `On` |
| `SecAuditEngine`              | `RelevantOnly`           | 只记录重要事件，减少日志量                  |
| `SecAuditLogRelevantStatus`   | 参考值[^code]             | 记录 5xx 和 4xx（排除 404） |
| `SecAuditLogParts`            | `ABIFHZ`                 | 记录请求头、响应头等，不含大请求体          |
| `SecRequestBodyLimit`         | `134217728`              | 请求体最大 128MB（根据业务调整）            |
| `SecRequestBodyInMemoryLimit` | `131072`                 | 内存缓冲 128KB，超出存临时文件              |
| `SecResponseBodyLimit`        | `524288`                 | 响应体最大 512KB                            |
| `SecTmpDir`                   | `/tmp/modsec_tmp`        | 临时文件目录（需创建并授权）                |
| `SecDataDir`                  | `/tmp/modsec_data`       | 持久化数据目录（如 IP 集合）                |
| `SecAuditLogStorageDir`       | `/opt/app/waf/logs/`     | 审计日志文件的存储目录                      |

[^code]: 该内容在表格中被错误转码：```"^(?:5|4(?!04))"```

- ② 创建**运行时临时数据**目录

```sh 设置 Nginx 用户可写（假设 Nginx 用户为 www-data）
sudo mkdir -p /tmp/modsec_{tmp,data}
sudo chown -R www-data:www-data /tmp/modsec_{tmp,data}
```

- ③ 创建统一入口文件 `main.conf` (补全规则文件)

```sh
cd /opt/app/waf/

touch modsecurity/main.conf
touch rules/whitelist.conf
touch rules/disablerule.conf
touch rules/900-exclusions-before-crs.conf
```

在 `main.conf` 写入如下内容，其中 *rules* 目录中可以放入我们的自定义白名单、黑名单规则。

```conf
# ================================
# ModSecurity 统一入口（优化版）
# ================================

# 1. 基础配置
Include /opt/app/waf/modsecurity/modsecurity.conf

# 2. 字符映射
SecUnicodeMapFile /opt/app/waf/modsecurity/unicode.mapping 2004

# 3. CRS 规则集配置
Include /opt/app/waf/coreruleset/crs-setup.conf

# 4. 【关键】CRS 执行前的排除规则（白名单优先）
Include /opt/app/waf/rules/900-exclusions-before-crs.conf

# 5. 加载 CRS 核心规则文件
Include /opt/app/waf/coreruleset/rules/*.conf

# 6. 自定义全局禁用规则（后置）
Include /opt/app/waf/rules/whitelist.conf
Include /opt/app/waf/rules/disablerule.conf
```

在 `900-exclusions-before-crs.conf` 写入必要的排除规则。

```conf
# 此文件中的规则在 CRS 规则之前执行
# 示例：放行健康检查路径
# SecRule REQUEST_URI "@beginsWith /health" "id:9000001,phase:1,pass,nolog,ctl:ruleEngine=Off"

# 示例：放行内网 IP
# SecRule REMOTE_ADDR "@ipMatch 192.168.0.0/16" "id:9000002,phase:1,pass,nolog,ctl:ruleEngine=Off"
```

- ④ 在 Nginx 中加载 ModSecurity 动态模块

宝塔面板找到 Nginx 设置，选择配置修改，添加如下内容，然后保存并重启 Nginx 即可~

{% codeblock 添加高亮行内容，位置见结构 lang:conf mark:2,14-15  %}
# 在 events 块之前加载动态模块
load_module modules/ngx_http_modsecurity_module.so;

events
{
  use epoll;
  worker_connections 51200;
  multi_accept on;
}

http
{
  # 全局开启 ModSecurity（或仅在需要的 server 块中开启）
  modsecurity on;
  modsecurity_rules_file /opt/app/waf/modsecurity/main.conf;

  include       mime.types;
{% endcodeblock %}
