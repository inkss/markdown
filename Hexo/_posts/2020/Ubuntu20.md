---
title: 记一次 Ubuntu 20.04 LTS 安装体验
toc: true
indent: true
date: 2020/06/07 22:19
updated: 2020/06/07 22:19
tag:
  - Ubuntu
  - Linux
categories: 随笔
description: 总的来说，整个的过程是非常优秀的，可以明显的感受到的进步，一些非常实用的功能也被整合进系统了，比如默认安装的英伟达显卡驱动。当然或许是因为刚发布的缘故，一些软件源还没有适配，需要稍作等待~
abbrlink: 481358a6
headimg: https://img.inkss.cn/inkss/static/Ubuntu20.assets/main.png
---

{% image https://img.inkss.cn/inkss/static/Ubuntu20.assets/main.png, bg=#292929, height=400px %}

*{% span center small gray, 老规矩，操作系统信息走一遍，PS 经过些许定制，图中显示不代表原版效果 %}*

## 一、序言

我也算是用了很久的 Ubuntu 系统（这个很久大概是一两年左右），初接触在 Ubuntu 14 ，正式入坑为 16，18 退坑。对于 Ubuntu 来说，以日常使用的角度来说，自然首选 LTS 版本，中间的迭代版本是肯定不做考虑的。此次对 Ubuntu 20.04 的安装纯粹是巧合，恰巧的下载了其镜像，再加上比较好奇 “猫猫镭射眼” ，所以就刻录 U 盘尝试了下啦。

说说电脑的情况，笔记本，一代 Y7KP ，两个硬盘：一个原装 500G 的 M2 固态，另一个是 500G 的 三星 860EVO 。大概在 Ubuntu18 开始吧，装双系统的流程就已经非常简单的，只需要分割出一片分区（保持未分配状态），然后在安装引导时选择共存，其它交给 Ubuntu 处理即可 {% bb 甚至还上升到了分区比例上的设定,早些年还煞有其事的要求设置各种分区 %}。所以前期准备上也只是分割出一个空白分区而已。

## 二、体验

直观上的体验：开机时，首先多出了个 Ubuntu 的 Logo ，其次登录界面大改，锁屏背景似乎被砍掉了。桌面壁纸这边，哈，猫猫镭射眼终于等到你。菜单栏，一些实用的功能被引入了系统里：允许自定义文件夹和重命名了（18 的时候还需要插件才能实现）

{% image https://img.inkss.cn/inkss/static/Ubuntu20.assets/img01.png, alt=自定义应用分组, bg=#101010, height=400px %}

接着是通知栏，通知面板支持了设置请勿打扰，此外在系统设置里，具体到了每一个软件的通知设置，非常的详尽（从设置的角度似乎这一点被提到了很高的优先级）。默认的主题方面，大同小异吧，谈不上多好也谈不上多差，不过倒是一个全新的主题，不一样的体验，此外声音通知上也有很大的改变，听起来非常的舒服，这个很赞 {% bb 但是我没找到这种包,似乎还支持自定义设置提示声音 %}。

{% image https://img.inkss.cn/inkss/static/Ubuntu20.assets/img03.png, alt=Do Not Disturb, bg=#292929, height=400px %}

关于显卡，老黄家的显卡毕竟闭源，确实不能打包进系统里，对新手来说，英伟达显卡的安装真的是有点麻烦（毕竟终端下操作不是谁都喜欢的），此处我在安装操作系统时除了选择最小安装外，还勾选了下载第三方驱动，与 18 相比，在开机后的应用列表里，惊喜的看到了 NVIDIA X Server ，竟然自动安装了。附加驱动了直接安装到了 440 版本，最新的版本号不太清楚，但感觉还是挺新的样子，当然这不重要，重要的是自动安装呀，省事儿~

{% image https://img.inkss.cn/inkss/static/Ubuntu20.assets/img04.png, alt=附加驱动, bg=var(--color-card), height=400px %}

在上图中，除了显卡驱动外，还有一个 9560 的网卡驱动，虽然不清楚这里为什么显示的是未工作，可是我这边一直用的是 Wifi 连接的网络呀，233 真奇怪（{% bb '我对 CMAKE 是真的不熟悉，难为我了...',18 时这个驱动得手动安装，还是编译安装你敢信 %}）。

## 三、问题

新的操作系统在最开始总归是阵痛的，一些软件如 PPA 这类源，没有 20 的发行版，无法导入，Typecho 我都没装上。此外一些样式类的改动很大，Gnome Shell 在样式上出了不兼容的现象（Ubuntu 的桌面样式全是用 CSS 写的） ，很奇怪没有向下兼容。此外，桌面上右键打开终端我点着一直没反应，我设置的快捷键也没有起作用。而且不知道是不是我主题改动的问题，总觉得白的背景页太亮，黑色背景页太暗，简言之就是觉得费眼，当然大部分都是小问题，系统本身还好。

{% image https://img.inkss.cn/inkss/static/Ubuntu20.assets/img05.png, alt=点着是没有反应的, bg=#292929, height=400px %}

默认浏览器是火狐，75 版本，我在下载一些东西后，嗯，世界太大，网速太慢，不部署 Clash 真能把你给墨迹死。所以，愉快的就下载 Chrome 啦，本来想下载 Edge 的，但是发现还没 Linux 版本，不过都已经放出消息了，总归会有的。

{% gallery 2 %}
![火狐](https://img.inkss.cn/inkss/static/Ubuntu20.assets/img06.png)
![Chrome, Yes!](https://img.inkss.cn/inkss/static/Ubuntu20.assets/img07.png)
{% endgallery %}

 软件生态呢？据我所知，国内软件上，输入法有搜狗和百度，文档有 WPS ，听歌有~~网易云~~（但是我讨厌它，各种原因导致的），网盘有百度云、坚果云。诸如此类吧，不赘述了，更多的去深度那边看应该更详细吧，哈哈。其实除了这些，还有 snap 呀，有更广泛的选择，比如我就在应用商店里看到了这个，前几天我就用它从 I tell u 下载 2004 的 Win10 镜像：

{% gallery 2 %}
![Motrix](https://img.inkss.cn/inkss/static/Ubuntu20.assets/img08.png)
![国产好软，全平台兼容](https://img.inkss.cn/inkss/static/Ubuntu20.assets/img10.png)
{% endgallery %}

## 四、后续

 这里有一个非常有意思的事情，Ubutnu 的默认安装行为将引导信息放到了系统所在盘，而我们高傲的 Windows 自然不会主动扫 Linux 的引导信息的。所以情况就是，两者的引导存到了不同的硬盘里，意味着我只需要在 BIOS 调整引导的加载顺序就能控制是否启用 Ubuntu 了（Ubuntu 可以引导 Win）。

最后，确实调整了加载顺序，目前来说，软件的缺失对我影响还是蛮大的，等待一段时间吧，补两张图：

{% gallery 2 %}
![主题配置信息](https://img.inkss.cn/inkss/static/Ubuntu20.assets/img09.png)
![操作系统信息](https://img.inkss.cn/inkss/static/Ubuntu20.assets/img11.png)
{% endgallery %}
