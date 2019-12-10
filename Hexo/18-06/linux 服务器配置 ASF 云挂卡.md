---
title: Linux 服务器配置 ASF 云挂卡
toc: true
date: 2018/06/04
tag:
  - Steam
  - Linux
categories: info
abbrlink: b46ef429
type: linux
---

> ASF是由C#编写，能同时挂载多个 Steam 账号的挂卡工具。其不像 Idle Master 那样：同一时间只能为一个账号挂卡，需要后台运行 Steam 客户端，需启动额外进程模拟‘正在游戏’状态。ASF 不需要后台运行任何 Steam 客户端，不需要启动额外进程，而且能为不限数目的 Steam 账号同时挂卡。不仅如此，该软件还能在服务器和其他非桌面电脑上运行，并拥有完整支持 Mono 的特性，这能让其在 Windows、Linux 以及 OS X 等任何支持 Mon o的操作系统上运行。ASF 存在的基础要归功于 SteamKit2。

## 0.事先的约定

Steam 社区目前处于**被墙状态**，目前连接社区的手段有两种：

（1） 修改 hosts，社区 Https 链接暂时不受影响。

```ssh
vim /etc/hosts
```

添加以下内容：

```vim
23.50.18.229 steamcommunity.com #服务器重启后失效
```

（2） 使用 [AnotherSteamCommunityFix](https://github.com/zyfworks/AnotherSteamCommunityFix)。

>通过修改 hosts 转发 HTTP 请求的方式临时性修复 SteamCommunity 在中国大陆无法访问的小工具

注意：这个程序**监听 443 和 80 端口**，如果服务器运行 WEB 程序，则无法使用。

## 1.下载 ASF

选择合适的版本从 GitHub 处下载：[ArchiSteamFarm/releases](https://github.com/JustArchi/ArchiSteamFarm/releases)

## 2.安装 .NET Core

ASF 是 C# 编写，所以需要安装 .NET 运行时。

### 2.1 解决包依赖

Ubuntu:

```shell
sudo apt-get install libunwind8 libunwind8-dev gettext libicu-dev liblttng-ust-dev libcurl4-openssl-dev libssl-dev uuid-dev unzip
```

CentOS:

```shell
yum install libunwind8 libunwind8-dev gettext libicu-dev liblttng-ust-dev libcurl4-openssl-dev libssl-dev uuid-dev unzip
```

### 2.2 注册微软签名 & 添加微软源

Ubuntu:

```shell
curl https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > microsoft.gpg
sudo mv microsoft.gpg /etc/apt/trusted.gpg.d/microsoft.gpg
sudo sh -c 'echo "deb [arch=amd64] https://packages.microsoft.com/repos/microsoft-ubuntu-xenial-prod xenial main" > /etc/apt/sources.list.d/dotnetdev.list'
```

CentOS:

```shell
sudo rpm --import https://packages.microsoft.com/keys/microsoft.asc
sudo sh -c 'echo -e "[packages-microsoft-com-prod]\nname=packages-microsoft-com-prod \nbaseurl= https://packages.microsoft.com/yumrepos/microsoft-rhel7.3-prod\nenabled=1\ngpgcheck=1\ngpgkey=https://packages.microsoft.com/keys/microsoft.asc" > /etc/yum.repos.d/dotnetdev.repo'
```

### 2.3 安装 .NET Core SDK

Ubuntu:

```shell
sudo apt-get install apt-transport-https
sudo apt-get update
sudo apt-get install dotnet-sdk-2.1.103
```

CentOS:

```shell
sudo yum update
sudo yum install libunwind libicu
sudo yum install dotnet-sdk-2.1.103
```

## 3.配置 ASF

### 3.1 安装

当前最新正式版为：3.1.1.1 （你也可以照抄代码，因为 ASF 默认自动更新）

```shell
mkdir ASF
wget https://github.com/JustArchi/ArchiSteamFarm/releases/download/3.1.1.1/ASF-generic.zip
mv ASF-generic.zip ASF
cd ASF
unzip ASF-generic.zip
```

### 3.2 配置ASF本地化

进入到 config 目录，修改 ASF.json 文件

```shell
cd config
vim ASF.json
```

修改 CurrentCulture 字段：

```vim
”CurrentCulture”:”zh-CN”,
```

### 3.3 配置 Bot 文件

官方的链接生成 bot 文件：[ASF 配置文件生成器](https://justarchi.github.io/ArchiSteamFarm/#/bot)

最简洁配置选项：

```sh
SteamLogin 中输入 steam 账号 id
SteamPassword 中输入 steam 密码
Enabled 选 √
IsBotAccount 选 ×
```

点击 Download 就会下载一个和 Name 名字一样的 .json 文件

将文件通过 FTP 上传到 ~/ASF/config/

> 详细的属性配置介绍：[ASF 官方 WIKI 中文版](https://steamcn.com/t187703-1-1)

推荐的配置内容：

```json
{
  "SteamLogin": "steam 账户名称",
  "SteamPassword": "steam 账户密码",
  "Enabled": true,
  "AcceptGifts":true,
  "FarmOffline":false,
  "CustomGamePlayedWhileIdle": "状态描述",
  "GamesPlayedWhileIdle": [
    550,50
  ]
}
```

### 3.4 启动 ASF 开始挂卡

创建一个新窗口用于后台挂卡

```shell
screen -S ASF
cd /ASF #进入到 ASF 所在目录
```

添加可执行文件 ArchiSteamFarm 权限

```shell
chmod +x ArchiSteamFarm.sh
```

执行程序

```shell
./ArchiSteamFarm.sh
```

当前页面按 ctrl +a +d 进入后台

恢复 screen 请终端输入：`screen -r ASF`

---

参考资料：

1. [Ubuntu16.0.4 搭建ASF云挂卡环境](https://www.jianshu.com/p/13beaf40fa0a)

2. [Linux 下使用 ASF (ArchiSteamFarm) V3实现云挂卡](http://itdream.me/2017/10/366)

3. [Get started with .NET in 10 minutes](https://www.microsoft.com/net/learn/get-started/linux/centos)

4. [linux screen 命令详解 - David_Tang - 博客园](https://www.cnblogs.com/mchina/archive/2013/01/30/2880680.html)

5. [zyfworks/AnotherSteamCommunityFix](https://github.com/zyfworks/AnotherSteamCommunityFix)

6. [JustArchi/ArchiSteamFarm](https://github.com/JustArchi/ArchiSteamFarm)

7. [ASF官方 WIKI 中文版](https://steamcn.com/t187703-1-1)

8. [另一个 SteamCommunityFix](https://steamcn.com/t339641-1-1)

9. [ASF 配置文件生成器](https://justarchi.github.io/ArchiSteamFarm/#/bot)
