---
layout: page
seo_title: 命令合集
sidebar: [wiki-hexo-theme, toc]
meta:
  header: []
  footer: [updated, counter]
date: 2020/06/05 15:12
updated: 2021/02/01 11:35
---

{% p center logo large, Shell command %}

### 泛域名申请与部署

{% codeblock 泛域名申请与部署 line_number:false %}
泛域名 SSL 证书相关，部署到宝塔相关 SSL 目录。

- 下载并安装 acme
curl https://cdn.jsdelivr.net/gh/Neilpang/acme.sh/acme.sh | INSTALLONLINE=1  sh

- 开启自动更新
acme.sh --upgrade --auto-upgrade

- 设置 DNSPod 环境变量
export DP_Id="******"
export DP_Key="*******************************"

- 生成泛域名证书
acme.sh --issue --dns dns_dp -d "*.inkss.cn" -d "*.szyink.com"

- 部署证书至宝塔 SSL 目录
acme.sh --install-cert -d *.szyink.com \
--key-file       /www/server/panel/vhost/cert/szyink.com/privkey.pem  \
--fullchain-file /www/server/panel/vhost/cert/szyink.com/fullchain.pem \
--reloadcmd     "service nginx reload"
{% endcodeblock %}

### CentOS 切换 Git

{% codeblock CentOS 切换 Git line_number:false %}
默认版本太低：git version 1.8.3.1

1.下载最新版：
  wget https://mirrors.edge.kernel.org/pub/software/scm/git/git-2.25.2.tar.gz
  （在此网站查看版本：https://mirrors.edge.kernel.org/pub/software/scm/git/）

2.安装依赖：
  sum yum install curl-devel expat-devel gettext-devel openssl-devel zlib-devel asciidoc xmlto docbook2x

3.卸载默认
  Git: sudo yum remove git

4.解压，编译，安装  
  make configure
{% endcodeblock %}

### Windows 文件夹软连接

{% codeblock Windows 文件夹软连接 line_number:false %}
mklink /J A B    （A目录不存在  B目录存在<真实存储目录>）

比如需要将C盘的文档目录，链接到D盘File文件夹下
mklink /J C:\Users\szhiy\Documents D:\File

作用：系统级别的文件软连接，支持多操作系统识别。
对 A 或者 B 的操作是相同的。

MKLINK [[/D] | [/H] | [/J]] Link Target

        /D       创建目录符号链接。默认为文件符号链接。
        /H       创建硬链接而非符号链接。
        /J        创建目录联接。
        Link    指定新的符号链接名称。
        Target  指定新链接引用的路径  (相对或绝对)。

仅 CMD
{% endcodeblock %}

### Scoop 安装、部署、下载

{% codeblock Scoop 安装、部署、下载 line_number:false %}

- 指定安装位置
$env:SCOOP='D:\app\Scoop'
[Environment]::SetEnvironmentVariable('SCOOP', $env:SCOOP, 'User')

- 安装 scoop
Invoke-Expression (New-Object System.Net.WebClient).DownloadString('https://get.scoop.sh')

- 安装基础软件 Git （同时会自动安装 7-zip）
scoop install git

- 添加软件仓库
scoop bucket add extras
scoop bucket add versions
scoop bucket add nonportable
scoop bucket add java

- 清除已下载缓存
scoop cache rm *

- 清除旧版本软件
scoop cleanup *

- 更新软件库
scoop update *
{% endcodeblock %}

### Goaccess 日志访问统计

{% codeblock Goaccess 日志访问统计 line_number:false %}
统计分析宝塔面板的网站日志，并生成到网站目录下。

- 宝塔相关
LANG="zh_CN.UTF-8" bash -c "goaccess -f /www/wwwlogs/szyink.com.log -p /www/wwwlogs/nginxlog.conf -o /www/wwwroot/szyink.com/report/index.html --ignore-status=404"
{% endcodeblock %}
