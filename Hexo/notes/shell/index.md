---
layout: page
title: 命令合集
sidebar: [wiki-hexo-theme, toc]
top_meta: []
date: 2020/06/05 15:12
updated: 2020/07/14 18:19
---

{% p center logo large, Shell command %}

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

### 求对象数组某个值的和

{% codeblock 求对象数组某个值的和 line_number:false %}
constys_all_prices=moneyList.YS.reduce((p,e)=>p+e.money,0);
{% endcodeblock %}

### Scoop 相关

{% codeblock Scoop 的安装 line_number:false %}
# 指定安装位置
$env:SCOOP='D:\app\Scoop'
[Environment]::SetEnvironmentVariable('SCOOP', $env:SCOOP, 'User')

# 安装 scoop
Invoke-Expression (New-Object System.Net.WebClient).DownloadString('https://get.scoop.sh')

# 安装基础软件 Git （同时会自动安装 7-zip）
scoop install git

# 添加软件仓库
scoop bucket add extras
scoop bucket add versions
scoop bucket add nonportable
scoop bucket add java

# 清除已下载缓存
scoop cache rm *

# 清除旧版本软件
scoop cleanup *

# 更新软件库
scoop update *
{% endcodeblock %}
