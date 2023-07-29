---
title: 极空间虚拟机部署 Ubuntu Server
toc: true
indent: true
tag:
  - NAS
  - 极空间
categories: 教程
date: '2023-07-28 17:00'
updated: '2023-07-28 17:00'
description: '利用极空间虚拟机安装 Ubunut Server，并部署 1Panel 面板。'
abbrlink: 4e35fed5
---

## 一、Ubuntu Server 安装

### 1.1 虚拟机创建

首先准备 Ubuntu Server 安装镜像，下载地址：[获取Ubuntu服务器版 | Ubuntu](https://cn.ubuntu.com/download/server/step1)

将下载完的镜像上传至极空间，然后在极空间虚拟机中选择新增 -> Linux 系统，根据个人需要选择合适的配置，Ubuntu Server 要求的最低配置为 CPU 1GHz 或更高、内存 1GB 或更多、磁盘至少 2.5 GB，下图仅供参考：

![虚拟机配置](../../img/article/23-07@极空间虚拟机部署UbuntuServer/23-07-27_160956.png)

![创建虚拟机-硬盘设置](../../img/article/23-07@极空间虚拟机部署UbuntuServer/23-07-27_161023.png)

网络部分设置为桥接，根据提示解绑 bond，建议使用网口 2（极空间主用网口 1）。

![虚拟机配置-网络设置](../../img/article/23-07@极空间虚拟机部署UbuntuServer/23-07-27_161033.png)

### 1.2 虚拟机安装

> 本节只对需要留意的项目加以描述，未展示项默认回车即可。

使用极空间访问功能，打开 VNC 窗口，选择 *Try or Install Ubuntu Server* 进行安装。

![虚拟机安装1](../../img/article/23-07@极空间虚拟机部署UbuntuServer/23-07-27_161116.png)

#### 1.2.1 网络设置

为了便于后期管理及访问，此处建议绑定静态地址，输入项从下往下依次为：子网、静态IP地址、网关、DNS解析地址，根据个人实际情况填写。

例如网关为 192.168.1.1，DHCP 子网掩码为 255.255.255.0，希望绑定静态地址为 192.168.1.10，那么可依次填入：*192.168.1.0/24*, *192.168.1.10*, *192.168.1.1*, *192.168.1.1*。

![虚拟机安装-网络设定1](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728142624252.png)

![虚拟机安装-网络设定2](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728143819778.png)

#### 1.2.2 存储配置

使用默认的 LVM 格式的引导分区，便于以后动态扩增。但需注意，默认没有把所有空间都分配给根目录。见下图，50G 分区中根目录只使用了 23.996G 空间，此处主动修改。

![存储配置1](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728144416384.png)

![存储配置2](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728144509944.png)

![存储配置3](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728144520950.png)

#### 1.2.3 个人配置

根据实际情况自行设置：系统名称、用户名、密码等。

![个人配置](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728144822616.png)

#### 1.2.4 SSH 设定

此步骤可选，可从 Github 处导入公钥，便于后续 SSH 秘钥登录。

![SSH 设定](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728144952940.png)

### 1.3 虚拟机启动

接下来就是等待系统安装，当上方红色提示框出现 Install complete 时代表安装完成（也可等待更新完成后再重启），一路回车确认直到出现 Login 页面，此时可以关闭这个窗口。在极空间的虚拟机窗口中将本虚拟机关机，点击编辑把绑定的系统镜像卸载，之后可再重新开机。

![Install complete](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728161926521.png)

### 1.4 虚拟机初始设定

#### 1.4.1首次更新

在安装系统前先对 Ubuntu 进行基础的修改，使用任意一款 SSH 工具连接进系统，先进行初步的更新。

```bash
ssh username@ip
```

```bash
sudo apt update
sudo apt upgrade
```

![初步更新](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728160238930.png)

#### 1.4.2 更换 Shell

> Oh My Zsh is a delightful, open source, community-driven framework for managing your Zsh configuration. It comes bundled with thousands of helpful functions, helpers, plugins, themes, and a few things that make you shout...

简言之，Oh my zsh 的补全能力更强，使用更方便一些，且功能更丰富。非必要步骤，可选。

```bash
sudo apt install git zsh
sh -c "$(curl -fsSL https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)"
```

![oh-my-zsh](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728160659509.png)

## 二、1Panel 面板安装

### 2.1 1Panel 介绍

1Panel 是一个以 Dokcer 为主的服务器管理面板，除了基础的 Nginx、PHP、MySQL 等服务外，支持了常见应用的一键部署：如 Bitwarden、Cloudreve、兰空图床、可道云、ddns-go、AdGuardHome、青龙、Jellyfin、alist 等应用。

![应用列表](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728162257226.png)

至于为什么选择 1Panel 而非宝塔面板，原因如下：宝塔面板闭源、激活时上传服务器信息、强制绑定手机号、商业化严重（内部存在大量的付费功能推广），而 1Panel 开源、界面美观现代化、功能上简单易用，更适合虚拟机使用。

### 2.2 1Panel 安装

在飞致云官网获取安装脚本，[在线安装 - 1Panel 文档](https://1panel.cn/docs/installation/online_installation/)：

```bash
curl -sSL https://resource.fit2cloud.com/1panel/package/quick_start.sh -o quick_start.sh && sudo bash quick_start.sh
```

脚本包含了 1Panel 本体和 Docker 的安装，可能需要较长的时间，耐心等待即可。

安装过程中根据个人需求自行设定对应的用户名、密码、端口，待安装完成后使用面板地址浏览器打开控制台。

![面板安装](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728161450013.png)

![面板控制台](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728161621326.png)

## 三、1Panel 面板设定

### 3.1 初始设定

#### 3.1.1 防火墙

进入到 **主机** -> **防火墙** 页面，启用 UFW 防火墙，根据个人需求删除/开放端口，见下图。

![IP 规则](../../img/article/23-07@极空间虚拟机部署UbuntuServer/23-07-28_163302.png)

在 IP 规则页面创建 IP 规则，填写对应的内网地址，使服务器对内网设备放行。

![内网开放](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728163035613.png)

#### 3.1.2 SSH 设定

- 更换默认 SSH 端口为高位端口；
- 修改 root 用户登录权限为仅允许秘钥登录；
- 关闭密码认证；
- 启用秘钥认证。

### 3.2 通过 SMB 挂载极空间

为了方便切换到 root 账户执行：

```bahs
sudo su
```

安装所需要的应用：

```bash
apt update
apt install cifs-utils
```

创建共享凭证：

```bash
mkdir /opt/credentials
vim /opt/credentials/smb
```

写入如下内容，将 user 和 password 更换为极空间的账户和密码：

```bash
username=user
password=password
```

创建挂载点：

```bash
mkdir /z4s
```

挂载共享目录，自行替换命令中的极空间 IP 地址及所需要的挂载目录：

```bash
mount -t cifs -o credentials=/opt/credentials/smb,uid=1000,gid=1000,file_mode=0755,dir_mode=0755 //极空间IP地址/所需要的挂载目录 /z4s
```

设置开机自动挂载，编辑 `/etc/fstab` 文件，在最后添加如下内容：

```bash
//极空间IP地址/所需要的挂载目录 /z4s cifs credentials=/opt/credentials/smb,uid=1000,gid=1000,file_mode=0755,dir_mode=0755 0 0
```

如需卸载挂载，可使用如下命令：

```bash
umount /z4s
```

如遇卸载失败，可使用如下命令查看程序占用：

```bash
lsof /z4s
```

![SMB 挂载](../../img/article/23-07@极空间虚拟机部署UbuntuServer/image-20230728170525373.png)

