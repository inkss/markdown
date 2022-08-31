---
uuid: 1bfa5c02-283a-11ed-a81c-e5c20e2f9c97
title: WSA：Andorid 子系统的轻度体验
toc: true
indent: true
date: 2021/10/23
updated: 2021/10/23
tag:
  - WSA
  - Android
  - Windows
categories: 资料
description: Windows Subsystem for Android：Windows 的安卓子系统，简称 WSA，此次简单体验一番。
backgrounds: ../../img/article/WSA的使用/image-20211023172702878.png
headimg: ../../img/article/WSA的使用/Hexo博客封面.png
abbrlink: b21c8751
---

前排提醒：本文章完成于 21/10/23 日，内容或有所变动，请留意。

{% gallery::stretch %}
![Windows Subsystem for Android™](../../img/article/WSA的使用/image-20211023025904144.png)
{% endgallery %}

## 一、系统部署

微软在不久前与美区 Beta 通道发布了第一版 WSA 的测试程序，虽说是测试程序且仅在美区，但是实际的体验却格外的完整。与 WSL 这种面向专业人士的技术不同，WSA 吸引了更多人的目光，于是也被找到了绕过美区 Beta 通道直接安装 WSA 的方法：

- [Windows Subsystem for Android™ with Amazon Appstore](https://www.microsoft.com/store/productId/9P3395VX91NR)
- [Microsoft Store - Generation Project (v1.2.3) [by @rgadguard & mkuba50]](https://store.rg-adguard.net/)

上面的链接，前者为 WSA 在微软应用商店的地址，后者为应用商店的抓包下载。进入 Generation Project 网站，输入前者地址，选择 Slow 通道^[共有四个选项，分别是 Fast，Slow，RP 和 Retail，分别对应 Windows 的 Dev 渠道，Beta 渠道，RP 渠道和正式版，目前只有 Beta 版本有发布]，在最下方找到 {% span logo  red, *.msixbundle %} 格式结尾的文件（文件大小约 1.2 Gb），下载该文件即可。

接着，利用管理员权限打开一个 PowerShell 窗口，执行 `add-appxpackage *.msixbundle` 安装命令，一切完成后可以在开始菜单的应用列表中找到 Windows Subsystem for Android 应用^[在正式启动前，你还需要开启 Windows 功能中的 **虚拟机平台**。]。

## 二、应用安装

与 WSA 合作的应用是亚马逊商店，它不支持国区，我们忽略它安装一个酷安进去即可。首先你需要打开 WSA 应用设置里的 **开发人员模式**，利用 {% span logo  red, adb %} 安装酷安进系统中。^[如果 IP 地址显示不可用或者 adb 连接时提示由于目标计算机积极拒绝，无法连接，代表 WSA 系统未启动，可以点击上方的文件启动 WSA 系统。]

{% note alien-monster cyan , Windows Subsystem for Android™️ enables your Windows 11 device to run Android applications that are available in the Amazon Appstore. %}

### 2.1 安装酷安

```powershell 确认 WSA 在运行状态后，把酷安装进系统中。
adb connect 127.0.0.1:58526
adb install .\Coolapk-11.4.3-2110131-coolapk-app-sign.apk
```

PS：酷安登录时，可能会出现登录按钮无法点击等情况，此类原因时触摸和点击行为不同造成。可以利用 <kbd>Tab</kbd> 键或者方向键使得按钮获得焦点，之后回车即可确认（或者你也可以使用触屏点击）。

### 2.2 静默应用安装

为了更方面的安装应用，即在酷安中实现静默安装，可以使用以下思路：

{% p center logo gray, Shizuku -> 安装狮 %}

以上两个应用均可在酷安中搜到，得益于 WSA 时基于 Android 11 的系统，我们可以利用无限调式激活 Shizuku，然后授权给安装狮，并开启静默安装。

{% gallery::stretch %}
![Shizuku](../../img/article/WSA的使用/image-20211023023506029.png)
![安装狮](../../img/article/WSA的使用/image-20211023023520137.png)
{% endgallery %}

## 三、测评体验

系统如何，拉出来跑一跑，根据部分网友描述，WSA 性能起码时蓝叠模拟器的 5 倍以上。

### 3.1 安兔兔跑分

我的笔记本 CPU 为 8 代 i5，由于之前使用 WSL 时已经安装了 WSLg 所需的驱动。按照描述 WSA 与 WSL 部分技术相同，所以 GPU 倒也能跑出一些分数，虽说就是惨不忍睹罢了。CPU 部分发挥的不错，起码还行，GPU 部分就惨不忍睹了，跑分时显卡占用很少，几乎没怎么调用。不过考虑到 WSL 的 VGPU 驱动毕竟时面向深度学习等设计的，或许与此还是不完全适用吧。

{% gallery::stretch %}
![安兔兔跑分](../../img/article/WSA的使用/image-20211023024100764.png)
{% endgallery %}

### 3.2 视频播放

还是由于 GPU 部分拉跨，在执行视频播放的时候全靠 CPU 处理，造成的后果便是 CPU 占用暴增，风扇狂转，这个问题只能寄希望于在 WSA 的正式版中解决了。实际体验中，虽说哔哩哔哩安卓版可以正常的播放视频，但是占用实在是太高了。

## 四、结尾

如果要给 WSA 做个评分，满风十分的话目前我可以给 7 分。主要是抛开播放视频、玩游戏的情况下，WSA 的使用性能相当优异，未来可期。

此外，众所周知国内平台的发展是畸形的，比如电商类移动端体验是优于桌面端体验的，甚至有些应用的功能需要去移动端完成，这就离谱了。所以既然平台将重心转移到移动端，那用户就在 WSA 里安装移动端软件。点名批评的就是微信，动不动就得拿出手机，麻不麻烦，这次绝对不打开手机了，在 WSA 中安装微信进行登录确认^[利用 adb 修改分辨率和 DPI，欺骗微信将 WSA 识别为平板，做到两者的同时登录。]，一了百了。

额外：WSA 的内存占用几乎是 WSL 的两倍（日常 3G 左右），虽说微软建议 WSA 的最低运行内存为 8G，但这里我是建议最低 16G，32G 及以上更佳。

{% gallery::stretch %}
![位于 WSL 中的网易云音乐和位于 WSA 中的 QQ 音乐](../../img/article/WSA的使用/image-20211023025652981.png)
{% endgallery %}
