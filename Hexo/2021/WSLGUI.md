---
title: WSL：Linux GUI 的深度体验
toc: true
indent: true
date: 2021/10/15
updated: 2021/10/15
tag:
  - WSL
  - Linux
  - Windows
categories: 资料
abbrlink: b5085776
description: WSL2 带来了 Linux GUI 的支持，现在你可以在 Windows 上以原生应用的方式运行 Linux GUI applications (X11 and Wayland)。本文记录了对其的尝试过程，包括基础的环境配置：中文环境及输入法配置；基本应用的体验：网易云、QQ 音乐、百度网盘等 Linux 版本的使用。
headimg: ../../img/article/WSLGUI/main.png
hideTitle: true
---

WSLG 是微软爸爸的一个开源项目，允许你原生使用 Linux GUI 程序，你可以在[此处](https://github.com/microsoft/wslg)找到项目地址。此外你可以安装相应显卡的 vGPU 的驱动程序以进行 GPU 加速，可以达到类似原生 Win 应用的使用体验。

下图为 WSLG 和 英伟达 CUDA 架构图：

{% gallery::stretch %}
![WSLg Architecture Overview](../../img/article/WSLGUI/WSLg_ArchitectureOverview.png)

![WSL launch stack diagram](../../img/article/WSLGUI/WSL-launch-stack-diagram-HR-r4.png)
{% endgallery %}

## 一、安装

在 Windwos 系统中启用 WSL2 的 GUI 支持的前置条件：

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

## 二、基础环境

未完待续