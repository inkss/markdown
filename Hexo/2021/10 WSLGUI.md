---
uuid: 1bfa5c03-283a-11ed-a81c-e5c20e2f9c97
title: WSL：Linux GUI 的安装与使用
toc: true
indent: true
date: 2021/10/15
updated: 2021/10/17
tag:
  - WSL
  - Linux
  - Windows
categories: 资料
abbrlink: b5085776
description: WSL2 带来了 Linux GUI 的支持，现在你可以在 Windows 上以原生应用的方式运行 Linux GUI applications (X11 and Wayland)。本文记录了对其的尝试过程，包括基础的环境配置：中文环境及输入法配置；基本应用的体验：网易云、QQ 音乐、百度网盘等 Linux 版本的使用。
headimg: ../../img/article/WSLGUI/main.png
backgrounds: ../../img/article/WSLGUI/wallhaven-rd6d81.png
hideTitle: false
references:
  - title: 'Run Linux GUI apps on the Windows Subsystem for Linux'
    url: 'https://docs.microsoft.com/en-us/windows/wsl/tutorials/gui-apps'
  - title: 'How to use wsl development environment in product'
    url: 'https://www.jetbrains.com/help/idea/how-to-use-wsl-development-environment-in-product.html'
  - title: Chinese input method doesn't work when launching app from Start Menu
    url: 'https://github.com/microsoft/wslg/issues/278'
  - title: 'WSL (Windows 10) - OI Wiki'
    url: 'https://oi-wiki.org/tools/wsl/' 
  - title: 'wslg 初体验：最佳 Linux 发行版？'
    url: 'https://ddadaal.me/articles/wslg-first-experience'
  - title: 在 WSL 上配置输入法
    url: https://patrickwu.space/2019/10/28/wsl-fcitx-setup-cn/
---

WSLg 是微软的一个开源项目，允许你在 WSL 的基础上原生使用 Linux GUI 程序，你可以在[此处](https://github.com/microsoft/wslg)找到具体的项目内容。此外你还可以安装相应显卡的 vGPU 驱动程序以进行 GPU 加速，达到类似原生 Win 应用般的使用体验。

下图为 WSLg 和 英伟达 CUDA 架构图：

{% gallery stretch::::diagram %}
![WSLg Architecture Overview](../../img/article/WSLGUI/WSLg_ArchitectureOverview.png)

![WSL launch stack diagram](../../img/article/WSLGUI/WSL-launch-stack-diagram-HR-r4.png)

{% endgallery %}

## 一、安装

在 Windows 系统中启用 WSLg 支持的前置条件：

{% tabs wsl %}
<!-- tab 前置条件 -->
{% checkbox checked::Windows 22000 或更高版本（建议使用 Windows11） %}
{% checkbox checked::WSL2（Linux GUI 应用仅支持 WSL 2，不适用于为 WSL 1 配置的 Linux 分发版） %}
{% checkbox::安装了 vGPU 的驱动程序（可选，但建议安装）%}
<!-- endtab -->

<!-- tab 驱动程序 -->
- [**Intel** 的 GPU 驱动程序](https://www.intel.com/content/www/us/en/download/19344/intel-graphics-windows-10-windows-11-dch-drivers.html)
- [**AMD** 的 GPU 驱动程序](https://www.amd.com/en/support/kb/release-notes/rn-rad-win-wsl-support)
- [**NVIDIA** 的 GPU 驱动程序](https://developer.nvidia.com/cuda/wsl)
<!-- endtab -->
{% endtabs %}

## 二、配置

WSLg 项目组推荐了一些基础的软件，比如 Gedit, GIMP, Nautilus, Chrome 等，这里摘抄一下。

{% folding cyan, Install and run GUI apps %}

> If you want to get started with some GUI apps, you can run the following commands from your Linux terminal to download and install some popular applications. If you are using a different distribution than Ubuntu, it may be using a different package manager.

> Once these applications are installed, you'll find them in your start menu under the distro name. For example `Ubuntu -> Microsoft Edge`.

```sh

## Update list of available packages
sudo apt update

## Gedit
sudo apt install gedit -y

## GIMP
sudo apt install gimp -y

## Nautilus
sudo apt install nautilus -y

## VLC
sudo apt install vlc -y

## X11 apps
sudo apt install x11-apps -y

## Google Chrome
cd /tmp
sudo wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
sudo dpkg -i google-chrome-stable_current_amd64.deb 
sudo apt install --fix-broken -y
sudo dpkg -i google-chrome-stable_current_amd64.deb

## Microsoft Teams
cd /tmp
sudo curl -L -o "./teams.deb" "https://teams.microsoft.com/downloads/desktopurl?env=production&plat=linux&arch=x64&download=true&linuxArchiveType=deb"
sudo apt install ./teams.deb -y

## Microsoft Edge Browser
sudo curl https://packages.microsoft.com/repos/edge/pool/main/m/microsoft-edge-dev/microsoft-edge-dev_93.0.946.1-1_amd64.deb -o /tmp/edge.deb
sudo apt install /tmp/edge.deb -y
```

{% endfolding %}

你可以参考一下上述内容安装一些基础软件，比如 Gedit 和 Nautilus，虽说可以在 Windows 中直接管理 WSL 文件了但最起码还是原生的用着舒服不是吗。

### 中文环境

初始状态下，WSL 中的系统是不包含中文语言环境的，如果只在终端时使用便也无所谓了，可现在可以使用桌面程序，这样的话还是配置一下语言比较好。

```sh 安装中文语言包
sudo apt install language-pack-zh-hans
```

```sh 编译语言
sudo locale-gen zh_CN.UTF-8
```

```sh 安装一些中文字体
sudo apt install fontconfig -y
sudo apt install fonts-noto-cjk -y
sudo apt install fonts-wqy-microhei -y
sudo apt install fonts-wqy-zenhei -y
```

```sh 设置默认语言，找到 zh_CN.UTF-8 并选中
sudo dpkg-reconfigure locales
```

{% gallery stretch %}
![软件包设置](../../img/article/WSLGUI/Snipaste_2021-10-11_18-11-43.png)
{% endgallery %}

完成上述操作后，关闭 WSL 并重启，登录查看即可。

### 中文输入法

关于中文输入法，由于最开始想使用搜狗输入法，所以框架选用了 Fcitx。不过实际体验后发现软件方面的问题太多严重影响使用，最后也是放弃它了。至于另一个备选方案中州韵也是因为懒得去搜配置文件也是放弃了。最后使用的是谷歌拼音输入法（*合着之前就是瞎折腾*）

以上是前提，接着就是安装配置 Fcitx 和谷歌拼音输入法。

```sh 安装 Fcitx 核心
sudo apt install fcitx fcitx-googlepinyin fonts-noto-cjk fonts-noto-color-emoji dbus-x11
```

```sh 配置环境变量
# 如果你当前 shell 为 bash，粘贴以下内容到 ~/.bash_profile
# 如果你当前 shell 为 zsh， 粘贴以下内容到 ~/.zprofile

export GTK_IM_MODULE=fcitx
export QT_IM_MODULE=fcitx
export XMODIFIERS=@im=fcitx
```

完成以上步骤后，可以在软件列表中双击打开 Fcitx 配置（也可终端下执行 `fcitx-config-gtk3`）选择输入法，如果列表为空，可以终端输入 `fcitx -r` 重启后查看。

{% gallery stretch %}
![语言支持](../../img/article/WSLGUI/image-20211017025544310.png)
![输入法配置](../../img/article/WSLGUI/image-20211017031108882.png)
{% endgallery %}

{% gallery stretch %}
![输入体验](../../img/article/WSLGUI/动画.gif)
{% endgallery %}

## 三、软件

### 国产软件

仅我所知的，可以在 Linux 系统中使用的国产软件就有：QQ 音乐、网易云音乐、百度云盘、WPS、福昕 PDF 等等，自然他们也可以通过 WSLg 运行。

这类软件安装很简单就不详细说明了，基本都是去各自官网下载 deb 安装包，然后安装即可。

{% gallery stretch::1 %}
![网易云音乐](../../img/article/WSLGUI/image-20211017033824274.png)
![QQ 音乐](../../img/article/WSLGUI/image-20211017033924592.png)
{% endgallery %}

一些额外的话，国内软件普遍大而臃肿，甚至还有不少的流氓行为。在 Windows 中我个人确实不太想一个个安装他们，但是放在 Linux 系统里我就无所谓了，因为个人资料很少，他们没有嚯嚯的空间。以网易云音乐为例，自从它将应用商店的 UWP 版本替换成 Win32 转制版本，我是再也没碰过网易云音乐，此次通过 WSL 倒也算的上是一次回归了。

事实上，有一点我没有尝试，Github 有一个项目为 Deepin-wine 环境的 Ubuntu/Debian 移植，或许这个也值得尝试。

### 编程软件

相比于日常应用的使用，编程软件才是关注的重点。首先是大型的 IDE 如 Jetbrains 全家桶自然支持 Ubuntu，并且有报道说明 Linux 下的应用速度或优于 Windows 下使用体验。此外得益于 WSL2 的完整内核，已经可以在 Windows 下利用 WSL 跑深度学习了。

而日常的简单开发，可以利用微软的 VS Code，一方面你可以直接在 WSL 中安装 Linux 版本的应用使用，另一方面你也可以利用 Windows 下的 Vs Code 通过 *Remote - WSL* 扩展连接到 WSL 系统中。

未来，可以将更多的开发环境从 Windows 迁移到 WSL 子系统里。编程开发有时候还是觉得 Linux 方便一些，比如在前一篇文章 [《WSL：宝塔面板的安装与使用》](/blog/b2b02edd/) 中所描述的，通过在 WSL 安装宝塔面板来管理 Nginx, MySql, PHP，在使用习惯上更贴近于服务器下的使用逻辑，更加方便。

总之，WSL 与 WSLg 带来的这一切真的很令人兴奋。

{% gallery stretch::1 %}
![Windows 下的 Vs Code 通过 Remote - WSL 扩展连接到 WSL 中](../../img/article/WSLGUI/image-20211017150123885.png)
{% endgallery %}

## 四、补充

### 关于 systemd

{% note info, WSL2 是用的自己的 init，不是用的 systemd，不直接支持传统的 systemd/init.d 脚本，所以 /etc/init.d 下的程序不会自动运行。 %}

这意味着一些软件无法开机自启，中文输入也在被影响的范围内，好在可以手动启动，通过以下命令启动输入法，具体的为什么就不作解释了，*{% bb 撑腰狂笑中, 好吧，其实真相是我也只是知其然 (●'◡'●) %}* 。

```sh 启动 DBUS 与 Fcitx
sudo /etc/init.d/dbus start
fcitx -d
```

既然 WSL 无法自启，那就利用 Windows 脚本启动它：新建文件 wsl.vbs 并写入如下内容：

```vbs
Set ws = CreateObject("Wscript.Shell")
ws.run "wsl -d Ubuntu -u root /etc/init.wsl", vbhide
ws.run "wsl -d Ubuntu -u szyink fcitx", vbhide
```

最后按 <kbd>Win</kbd> + <kbd>R</kbd> 输入 `shell:startup` 将该 vbs 文件放启动目录中。

### 关于备份/恢复/删除

```powershell
# 查看当前已经安装的子系统
wsl --list

# 删除已经安装的子系统
wsl --unregister wsl-name

# 导出子系统到指定路径
wsl --export wsl-name /path/to/backup-file.tar

# 导入子系统到指定路径
wsl --import wsl-name /wsl/path/ /path/to/backup-file.tar
```

### 关于应用

如果需要应用出现在 Windows 软件列表中，需要其启动图标位于 `/usr/share/applications` 中。

{% gallery stretch::1 %}
![](../../img/article/WSLGUI/image-20211017035446015.png)
{% endgallery %}
