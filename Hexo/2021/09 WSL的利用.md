---
title: WSL：宝塔面板的安装与使用
toc: true
indent: true
date: 2021/09/29
updated: 2021/10/15
tag:
  - WSL
  - Linux
  - Windows
categories: 资料
description: Windows Subsystem for Linux (WSL) is a compatibility layer for running Linux binary executables (in ELF format) natively on Windows 10 and Windows Server 2019.
abbrlink: b2b02edd
backgrounds: ../../img/article/WSL的利用/wallhaven-8o96xo.jpg
---

最近重装了系统，使用的是 Windows11 Beta 版本，所以重新安装配置了这些，于此记录。与 Win10 相比，默认安装的是 WSL2 版本，默认的 Ubuntu 也升级到了 20.04LTS。

<!-- more -->

{% gallery::stretch %}
![Terminal](../../img/article/WSL的利用/Snipaste_2021-09-29_13-53-20.png)
{% endgallery %}

在程序和功能中开启 **适用于 Linux 的 Windows 子系统** 后，进入 Windows 应用商店选择安装 [Ubuntu](https://www.microsoft.com/zh-cn/p/ubuntu/9nblggh4msv6)。不过之后倒是出现了一个意料之外的情况：`WslRegisterDistribution failed with error: 0x800701bc`，解决方案^[[microsoft/WSL/issues/5393](https://github.com/microsoft/WSL/issues/5393)]如下：[下载 Linux 内核更新包](https://docs.microsoft.com/zh-cn/windows/wsl/install-manual#step-4---download-the-linux-kernel-update-package)。

## 一、部署

宝塔面板的部署没有特殊的地方，正常安装即可：

```sh Ubuntu/Deepin安装命令
wget -O install.sh http://download.bt.cn/install/install-ubuntu_6.0.sh && sudo bash install.sh
```

部署完成后，在宿主端通过 `127.0.0.1` 搭配给出的端口/入口访问即可。

需要注意的是： 在 WSL1 时代，如果计算机设置为可供 LAN 访问，那么在 WSL 中运行的应用程序也可供在 LAN 中访问。而在 WSL2 中，其有一个带有其自己独一无二的 IP 地址的虚拟化以太网适配器，所以局域网中无法通过宿主机的地址访问 WSL 中的应用。

你可以参考如下链接找到解决方案：[为 WSL2 一键设置代理](https://zhuanlan.zhihu.com/p/153124468)。  *{% psw 如果只是本机访问就不用管 %}*

{% gallery::stretch %}
![宝塔面板](../../img/article/WSL的利用/Snipaste_2021-09-29_14-18-45.png)
{% endgallery %}

## 二、启动

是的，没错，Windows 在启动时不会处理任何 WSL 的程序，包括不启动宝塔面板。我们需要主动启动宝塔程序，这里参考了 [Win10 子系统 ssh 服务自启动设置](https://blog.csdn.net/toopoo/article/details/85733566)。

思路很简单，利用 `wsl -d DistributionName -u UserName command` 启动子系统中的程序（子系统名称可以用 `wsl -l` 查看，宝塔的启动命令为 `/etc/init.d/bt start`），然后新建文件 `statr-bt.vbs` 并放入到启动目录中。

文件写入如下内容：

```vbs Ubuntu 为子系统名称，替代成你实际内容即可
Set ws = CreateObject("Wscript.Shell")
ws.run "wsl -d Ubuntu -u root /etc/init.d/bt start", vbhide
```

最后按 <kbd>Win</kbd> + <kbd>R</kbd> 输入 `shell:startup` 将该 vbs 文件放启动目录中。

如此，愉快的玩耍吧 {% emoji huaixiao %} ~

## 三、额外

WSL 还能做什么？真的很多，由于 WSL2 已经是一个拥有完整 Linux 内核的操作系统，很多在 WSL1 中无法使用的操作都能得以实现。

### 文件管理

在 Windows 文件管理器中可以原生的管理 WSL 中的文件。

{% gallery::stretch %}
![文件管理器](../../img/article/WSL的利用/Snipaste_2021-09-29_14-25-42.png)
{% endgallery %}

### Node 环境

可以在 Ubuntu 中配置 Node 环境，然后在 VS Code 中调用 WSL(Ubuntu) 终端正常使用。

{% gallery::stretch %}
![VS Code](../../img/article/WSL的利用/Snipaste_2021-09-29_14-30-52.png)
{% endgallery %}

### GUI 程序

{% blocknote quote %}
- 从应用程序启动 Linux Windows "开始" 菜单。
- 将 Linux 应用固定到 Windows 任务栏。
- 使用 alt-tab 在 Linux 和 Windows 应用之间切换。
- 跨应用和 Linux Windows 剪切 + 粘贴。
{% endblocknote %}

与安装桌面不同，在 Windows11 下可以近乎得到一种原生程序的启动体验，相应的软件甚至可以出现在 Window 的软件列表中，这种状态下，类似于 VMware 下的 Unity 模式，操作上有些许相似。

{% gallery::stretch %}
![GIMP](../../img/article/WSL的利用/Snipaste_2021-09-29_13-13-51.png)
{% endgallery %}

#### WSL GUI 安装

简单的摘抄下安装步骤^[[在预览版适用于 Linux 的 Windows 子系统 (运行 Linux GUI)](https://docs.microsoft.com/zh-cn/windows/wsl/tutorials/gui-apps)]，大致三个先决条件：**WSL2**，**Windows `22000` 或更高版本** 和 **适用于 vGPU 的驱动程序**。驱动列表：[Intel](https://downloadcenter.intel.com/download/29526)、[AMD](https://www.amd.com/en/support/kb/release-notes/rn-rad-win-wsl-support)、[NVIDIA](https://developer.nvidia.com/cuda/wsl)。

完善上述条件后，需要更新到包含 Linux GUI 支持的最新版本。

```powershell 选择 "开始"，键入 PowerShell， 右键 Windows PowerShell，然后选择"以管理员角色运行"。
wsl --update
```

```posershel 需要重启 WSL，运行 shutdown 命令来重启 WSL。
wsl --shutdown
```

#### 运行 Linux GUI 应用

```shell 安装 Gedit, Nautilus, VLC, X11
sudo apt install gedit -y
sudo apt install nautilus -y
sudo apt install vlc -y
sudo apt install x11-apps -y
```

```sh 安装适用于 Linux 的 Google Chrome
# 使用 wget 下载它： 
sudo wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb

# 获取当前稳定版本： 
sudo dpkg -i google-chrome-stable_current_amd64.deb

# 修复包： 
sudo apt install --fix-broken -y

# 配置包： 
sudo dpkg -i google-chrome-stable_current_amd64.deb
```

```sh 安装Microsoft Edge Linux 的浏览器
# 使用 curl 下载包
sudo curl https://packages.microsoft.com/repos/edge/pool/main/m/microsoft-edge-dev/microsoft-edge-dev_91.0.852.0-1_amd64.deb -o /tmp/edge.deb

# 使用 apt 安装它
sudo apt install /tmp/edge.deb -y
```

