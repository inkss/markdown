# linux服务器配置ASF云挂卡

>本篇文章将同时以Ubuntu和CentOS进行教程说明。
>
> ASF是由C#编写，能同时挂载多个Steam账号的挂卡工具。其不像Idle Master那样：同一时间只能为一个账号挂卡，需要后台运行Steam客户端，需启动额外进程模拟‘正在游戏’状态。ASF不需要后台运行任何Steam客户端，不需要启动额外进程，而且能为不限数目的Steam账号同时挂卡。不仅如此，该软件还能在服务器和其他非桌面电脑上运行，并拥有完整支持Mono的特性，这能让其在Windows、Linux以及OS X等任何支持Mono的操作系统上运行。ASF存在的基础要归功于SteamKit2。

## 0.事先的约定：

Steam社区目前处于**被墙状态**，目前连接社区的手段有两种：

1.修改hosts，社区Https链接暂时不受影响。

```ssh
vim /etc/hosts
```

添加以下内容：

```vim
23.50.18.229 steamcommunity.com #服务器重启后失效
```

2.使用[AnotherSteamCommunityFix](https://github.com/zyfworks/AnotherSteamCommunityFix)。

>通过修改hosts转发HTTP请求的方式临时性修复SteamCommunity在中国大陆无法访问的小工具

注意：这个程序**监听443和80端口**，如果服务器运行WEB程序，则无法使用。

```txt
使用步骤:

1.下载并解压缩:
https://github.com/zyfworks/AnotherSteamCommunityFix/releases
2.打开终端（Terminal），进入到ascf程序目录:如ascf程序在 /Users/Makazeu/Downloads/文件夹中，那么在终端中输入:
cd /Users/Makazeu/Downloads
3.赋予程序可执行权限，在终端中输入命令：
chmod +x ./ascf
4.使用root用户（管理员用户）运行程序，在终端中输入:
sudo ./ascf
5.输入root用户密码后，看程序是否运行
6.因为程序涉及到hosts文件修改，需要高权限，所以你需要输入root密码
若程序已经成功运行，此时就不要关闭终端窗口了，否则程序就会退出！试下Steam社区能否正常打开。
7.一切都没问题后，在终端窗口中退出程序（按Ctrl+C），然后以后台的方式运行程序，输入
nohup sudo ./ascf &
8.之后就可以关闭终端窗口了，此时程序在后台运行！现在steamcommunity可以打开咯！
```

## 1.下载ASF

选择合适的版本从GitHub处下载：[ArchiSteamFarm/releases](https://github.com/JustArchi/ArchiSteamFarm/releases)

## 2.安装 .NET Core

2.1 解决包依赖：

Ubuntu:

```shell
sudo apt-get install libunwind8 libunwind8-dev gettext libicu-dev liblttng-ust-dev libcurl4-openssl-dev libssl-dev uuid-dev unzip
```

CentOS:

```shell
yum install libunwind8 libunwind8-dev gettext libicu-dev liblttng-ust-dev libcurl4-openssl-dev libssl-dev uuid-dev unzip
```

2.2 注册微软签名

Ubuntu:

```shell
curl https://packages.microsoft.com/keys/microsoft.asc | gpg --dearmor > microsoft.gpg
sudo mv microsoft.gpg /etc/apt/trusted.gpg.d/microsoft.gpg
```

CentOS:

```shell
sudo rpm --import https://packages.microsoft.com/keys/microsoft.asc
```

2.3 添加微软源

Ubuntu:

```shell
sudo sh -c 'echo "deb [arch=amd64] https://packages.microsoft.com/repos/microsoft-ubuntu-xenial-prod xenial main" > /etc/apt/sources.list.d/dotnetdev.list'
```

CentOS:

```shell
sudo sh -c 'echo -e "[packages-microsoft-com-prod]\nname=packages-microsoft-com-prod \nbaseurl= https://packages.microsoft.com/yumrepos/microsoft-rhel7.3-prod\nenabled=1\ngpgcheck=1\ngpgkey=https://packages.microsoft.com/keys/microsoft.asc" > /etc/yum.repos.d/dotnetdev.repo'
```

2.4 安装 .NET Core SDK

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

3.1 安装

当前最新正式版为：3.1.1.1

```shell
mkdir ASF
wget https://github.com/JustArchi/ArchiSteamFarm/releases/download/3.1.1.1/ASF-generic.zip
mv ASF-generic.zip ASF
cd ASF
uzip ASF-generic.zip
```

3.2 配置ASF本地化

进入到config目录，修改ASF.json文件

```shell
cd config
vim ASF.json
```

修改CurrentCulture字段：

```vim
”CurrentCulture”:”zh-CN”,
```

3.3 配置Bot文件

官方的链接生成bot文件：[ASF 配置文件生成器](https://justarchi.github.io/ArchiSteamFarm/#/bot)

SteamLogin中输入steam账号id

SteamPassword中输入steam密码

Enabled选√

IsBotAccount选×

点击Download就会下载一个和Name名字一样的.json文件

将文件通过FTP上传到~/ASF/config/

> 详细的属性配置介绍：[ASF官方WIKI中文版](https://steamcn.com/t187703-1-1)

3.4 启动 ASF 开始挂卡

创建一个新窗口用于后台挂卡

```shell
screen -S ASF
cd /ASF #进入到ASF所在目录
```

添加可执行文件ArchiSteamFarm 权限

```shell
chmod +x ArchiSteamFarm
```

执行程序

```shell
./ArchiSteamFarm
```

当前页面按ctrl +a +d进入后台

恢复screen请终端输入：`screen -r ASF`

---

参考资料：

1. [Ubuntu16.0.4搭建ASF云挂卡环境](https://www.jianshu.com/p/13beaf40fa0a)

1. [Linux下使用ASF(ArchiSteamFarm)V3实现云挂卡](http://itdream.me/2017/10/366)

1. [Get started with .NET in 10 minutes](https://www.microsoft.com/net/learn/get-started/linux/centos)

1. [linux screen 命令详解 - David_Tang - 博客园](https://www.cnblogs.com/mchina/archive/2013/01/30/2880680.html)

1. [zyfworks/AnotherSteamCommunityFix](https://github.com/zyfworks/AnotherSteamCommunityFix)

1. [JustArchi/ArchiSteamFarm](https://github.com/JustArchi/ArchiSteamFarm)

1. [ASF官方WIKI中文版](https://steamcn.com/t187703-1-1)

1. [另一个SteamCommunityFix](https://steamcn.com/t339641-1-1)

1. [ASF 配置文件生成器](https://justarchi.github.io/ArchiSteamFarm/#/bot)