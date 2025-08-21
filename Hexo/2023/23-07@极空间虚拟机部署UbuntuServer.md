---
title: 极空间虚拟机部署UbuntuServer
toc: true
indent: true
tag:
  - Ubuntu Server
  - 虚拟机部署
  - 极空间
categories: Linux
date: '2023-12-05 19:25'
updated: '2024-11-09 22:26'
description: 本文记录在极空间虚拟机中部署 Ubuntu Server（选择 Ubuntu 22.04.3 LTS 桌面版）的过程，包括系统安装的前置条件与步骤、开箱后的系统设置（更新、远程访问、个性化定制等）、系统美化（主题、图标、扩展配置），以及后续的 1Panel 面板安装、SMB 挂载、文件同步等完善操作，提供详细命令与配置参考。
headimg: ../../img/article/23-07@极空间虚拟机部署UbuntuServer/Hexo博客封面.png
abbrlink: 4e35fed5
---

> 本文记录使用极空间虚拟机安装、配置 Ubuntu 桌面版过程中的心得内容，供大家参考。

关于操作系统的选择，由于极空间提供了便捷的图形化页面的访问功能，可以在客户端或浏览器上远程访问，故此处选取最常用的 Linux 端的桌面版发行版： Ubuntu 22.04.3 LTS，下载地址：[下载Ubuntu桌面系统](https://cn.ubuntu.com/download/desktop) 。此外桌面版的操作系统也不是说不可以拿来干 Server 的活：装个 1Panel 面板充当服务器。

言归正传，下面是一些基本的前置条件：

- 虚拟机需要桥接网络，所以先在极空间网络管理中解 Bound 并创建网桥。
- 为了便于管理，建议提前在路由器中做好 DHCP 静态绑定。

## 一、系统安装

作为桌面版系统，请至少为 Ubuntu 留下 **2 CPU、4GB 内存、50G 存储空间；**

![虚拟机属性](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656dfc7e64d86.png)

创建网络时，选择**桥接 bridge、网口 2**，另可点右侧齿轮按钮提前设置 MAC 地址，方便在路由器固定 IP；

在创建后系统后，选择**访问**，进入到 VNC 页面，继续完成系统的安装，依据流程提示按个人习惯选择即可。

注：安装时建议选择**最小安装**、**安装时下载更新**，以及在对硬盘分区时选择高级特性，勾选使用 **LVM**，方便日后在空间容量不足时扩容根目录；

![最小安装+更新系统](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656dfd39abd21.png)

![安装类型-高级特性](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656dfd39e4f42.png)

## 二、系统开箱

Ubuntu 的开箱体验不那么易用，需要花费一些时间完成自定义设定，下面将按照线性顺序依次展开说明。

### 2.1 首次更新

现在，你已经进入了 Ubuntu 桌面环境，在一切开始前，先完成两个步骤：**更新系统**和**补全语言翻译**：

先移步到 **所有软件 (<kbd>Win</kbd> + <kbd>A</kbd>)**→**软件更新器** ，等待系统完成更新。（其实等待一会儿也会自动弹出）

接着移步到 **所有软件**→**设置**→**区域和语言**→**管理已安装的语言** ，完成语言列表的更新。

![初次更新](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656e001af3597.png)

### 2.2 远程访问

现在正式开始对 Ubuntu 系统进行初始化设定，极空间提供了一个简单 VNC 系统来访问桌面环境，它的优势在于可以参与到系统的安装过程，缺点是目前不支持剪切板同步，考虑到接下来会有大量的代码复制、文件拷贝的过程，我们先解决这个问题，目前有两个方案：

#### 2.2.1 极空间中转传递

使用极空间中转，在 Ubunut 系统中打开浏览器（默认为火狐），通过内网环境访问极空间网页，利用记事本中转两个系统的文本内容（*浏览器套娃*）。

![浏览器套娃](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656e013593fe9.png)

#### 2.2.2 Windows 远程桌面

GNOME 42 采用 Microsoft RDP 协议作为其内置的远程桌面功能，这也就意味着你可以使用 Windows 上的**远程桌面连接**访问 Ubunut 桌面环境了。从桌面顶部的系统托盘处打开设置，从左侧导航到“共享”，然后打开应用标题右上角的切换图标。最后点击“远程桌面”即可启用该功能并配置用户、密码等。

缺点是家庭版的 Windows 没有这个软件，仅在专业版等版本支持。

![共享](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656e016812f29.png)

![远程桌面](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656e01684c5d9.png)

注：第一次会由于服务未启动，无法连接成功，打开终端（桌面下右键）运行下列命令启动远程桌面服务：

```shell
sudo systemctl --user restart gnome-remote-desktop.service
```

![Windows 远程桌面连接](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656e01689e104.png)

### 2.3 个性化定制

接下来就是根据个人习惯完成桌面环境定制，首先先解决**网络问题**，我就直说了：你至少需要一个翻墙软件去帮你完成资源的获取，大部分资源在国内网络下访问体验极差、甚至无法打开。如果你确实没有选择，考虑到部分资源需从 Github 下载，故尚有一个解决方案，对 Github 的 raw 域名添加如下 hosts 解析，它可能无法帮你获得很快的速度，但至少在下载源自 Github 的文件时不会是令人绝望般的连接失败：

桌面下右键打开终端，然后编辑 hosts 文件：

```shell
sudo vim /etc/hosts
```

在文件末尾追加如下内容：

```txt
185.199.110.133 raw.githubusercontent.com
```

该解析地址不一定最新，如有变动请从该网站获取域名 A 解析地址：[DNS Propagation Checker](https://www.whatsmydns.net/)

注：如果你对 Vim 不熟悉，可使用文本编辑器修改，将上面的 `vim` 替换为 `gedit`。

#### 2.3.1 替换 Shell

此步骤是为了获得更佳的命令补全能力，类似于代码补全，将默认的 Bash 替换为 Zsh 的 oh-my-zsh 。

```shell
sudo apt install git zsh
sh -c "$(wget -O- https://raw.githubusercontent.com/ohmyzsh/ohmyzsh/master/tools/install.sh)"
chsh -s /usr/bin/zsh
```

#### 2.3.2 替换输入法

此处以基于 ibus 的中州韵（rime）输入法为例：

```shell
sudo apt install ibus-rime
```

部署完成后，将默认输入方案的繁体中文修改成简体中文，终端下输入以下命令用文件编辑器打开文件：

```shell
gedit ~/.config/ibus/rime/build/default.yaml
```

查找 *schema_list*，将 *luna_pinyin_simp* 提到最前列，保存文件后，在顶部面板的语言栏中重新部署即可。

![输入法属性修改](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656ea442b609f.png)

![重新部署](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656ea442ad43f.png)

注：如果你希望使用搜狗/百度输入法，请留意它们是基于 Fcitx 方案的，需要在**语言支持**中更改输入法系统。

#### 2.3.3 替换包安装器

Ubuntu 的软件包 deb 使用 dpkg 命令安装，但它不会自动解析软件依赖问题，这里使用 gdebi 它。

```shell
sudo apt install gdebi
```

在具体的使用中，所对应的安装命令为 sudo gdebi xxxx.deb，当然你也可以在**文件管理器中右键安装包**，选择**使用其它程序打开**，选择 **Gdebi 软件包安装程序**以图形化页面的方式完成程序安装。

![Gdebi 软件包安装程序](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656ea55f3885d.png)

#### 2.3.4 恢复 SSH 秘钥

可选步骤，为了便于后期终端进系统，先安装 ssh 程序：

```shel
sudo apt install ssh
```

将公钥拷贝到 ~/.ssh/authorized_keys 文件中（没有文件夹/文件自行创建），此外留意文件/夹权限设定：

| 目录 | 文件                        | 权限 |
| ---- | --------------------------- | ---- |
| .ssh |                             | 700  |
|      | authorized_keys             | 600  |
|      | id_rsa / id_ed25519         | 600  |
|      | id_rsa.pub / id_ed25519.pub | 644  |

```shell
sudo chmod 700 ~/.ssh
sudo chmod 600 ~/.ssh/authorized_keys
```

#### 2.3.5 调整家目录为英文

可选步骤，为了便于终端下进出目录，依次输入以下命令，在弹出页面点击更新名称，**更新成英文后==再==勾选** Don’t ask me again.

```shell
export LANG=en_US
xdg-user-dirs-gtk-update
export LANG=zh_CN
```

![英文目录名](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656ea6e6d771c.png)

## 三、系统美化

根据上文截图，你可能发现了展示用的系统与原始的 Ubuntu 桌面样式上略有不同，此处的更改主要是围绕：Gnome tweaks、Gnome Shell 和 Opendesktop 展开，下面介绍它们的具体功能和如何去使用。

*注：虚拟机内使用，适当安装扩展。*

### 3.1 安装 Gnome Shell

首先终端下安装本地连接器：

```shell
sudo apt install chrome-gnome-shell
```

如果你还是火狐浏览器，请安装这个浏览器插件：[GNOME Shell integration](https://addons.mozilla.org/en-US/firefox/addon/gnome-shell-integration/)。

如果你已顺手更换浏览器为谷歌/Edge浏览器，则安装这个：[GNOME Shell 集成](https://chromewebstore.google.com/detail/gnome-shell-集成/gphhapmejobijbbhgpjhcjognlahblep)。

完成上述步骤后便可以在个网站安装你所需要的扩展了：[GNOME Shell Extensions](https://extensions.gnome.org/)。

注意，你至少需要安装 [User Themes](https://extensions.gnome.org/extension/19/user-themes/)，它允许从本地加载 Shell 主题，否则很多样式无法修改。

### 3.2 安装优化软件

在完成 3.1 后，接着在终端下安装 Gnome tweaks，它提供了一个图形化页面选取设定主题：

```shell
sudo apt install gnome-tweaks
```

接着在 **所有软件 (<kbd>Win</kbd> + <kbd>A</kbd>) ** 中就可以找到它 『优化』，对主题的应用修改在此软件中进行。

![优化](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656eca295bcc9.png)

### 3.3 下载样式文件

对应的主题、图标、光标在如下网站获取：

- 应用程序（主题）/ Shell：[GTK3/4 Themes - OpenDesktop.org](https://www.opendesktop.org/browse?cat=135&ord=rating)。

- （应用）图标：[Full Icon Themes - pling.com](https://www.pling.com/browse?cat=132&ord=rating)。

- （鼠标）光标：[Cursors - pling.com](https://www.pling.com/browse?cat=107&ord=rating)。

- 字体：

  - OPPO Sans：[【字体】OPPO Sans，用文字探索科技美感](https://open.oppomobile.com/bbs/forum.php?mod=viewthread&tid=2274)。

  - JetBrains Mono：[JetBrains Mono: A free and open source typeface for developers](https://www.jetbrains.com/lp/mono/)。

  - 文泉驿字体 微米黑/正黑：

    ```shell
    sudo apt install fonts-wqy-microhei fonts-wqy-zenhei
    ```

下载完毕后，解压存放至如下目录（如不存在自行创建）：

- 主题存放目录：`/usr/share/themes` 或 `~/.themes`
- 图标/光标存放目录：`/usr/share/icons` 或 `~/.icons`
- 字体存放目录：`/usr/share/fonts` 或 `~/.fonts`

它们的区别类似于 Windows 环境变量里的个人和系统，其中 */usr/share* 目录需要管理员权限才能修改，可以对文件管理提权后打开处理：

```shell
sudo nautilus
```

### 3.4 应用图标调整

一般情况下，应用图标会读取主题中的图标样式，但部分应用可能会使用自己的图标，与整体样式不和谐，我们可以主动修改对图标文件引的用，主动修改，而这些图标的存储位置为：

- 大部分启动图标都在此：`/usr/share/applications` 

- 一部分本地应用图标：`~/.local/share/applications`

- snap 类应用：`/var/lib/snapd/desktop/applications`

此处以更改 QQ 图标为例：

![修改 QQ 图标](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656ed6472041e.png)

更改 ICON 字段为主题图标中的具体图标文件：

![修改 ICON 字段](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656ed6477a23b.png)

如果对 Vim 不熟悉，同样可以换成 gedit 以图形化页面编辑：

![修改图标](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656ed647ad02b.png)

重启系统后可以观察到新图标已经生效：

![图标更改](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656ed647a36f2.png)

### 3.5 个人用样式

下面是演示用系统所使用的资源链接，可供参考：

- 主题 `~/.themes`：[WhiteSur Gtk Theme - OpenDesktop.org](https://www.opendesktop.org/p/1403328/)
- 图标 `~/.icons`：[Uos Deepin V20 - pling.com](https://www.pling.com/p/1349376)
- 光标 `~/.icons`：[Breeze Dark Teal Cursors - pling.com](https://www.pling.com/p/2108647)

![外观](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656ecb5d2ee51.png)

![字体](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656ecb5d582b5.png)

![系统信息](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656ece0d86241.png)

### 3.6 个人用扩展

- [ArcMenu](https://extensions.gnome.org/extension/3628/arcmenu/)：在面板处添加应用菜单功能，可实现多种样式的菜单，比如 Windows11 风格；
- [Clipboard Indicator](https://extensions.gnome.org/extension/779/clipboard-indicator/)：在面板处添加一个剪切板指示器，存储历史复制数据；
- [Color App Menu Icon for GNOME 40+](https://extensions.gnome.org/extension/5473/color-app-menu-icon-for-gnome-40/)：使面板的应用菜单处图标显示为常规图标；
- [Dash to Dock](https://extensions.gnome.org/extension/307/dash-to-dock/)：将概览页面的 Dash 调整为 Dock 使用，可固定于左侧/底部；
- [Dash to Panel](https://extensions.gnome.org/extension/1160/dash-to-panel/)：将概览页面的 Dash 调整到面板，另可调整面板处图标位置等；
- [Extension List](https://extensions.gnome.org/extension/3088/extension-list/)：在面板处添加一个控制所有 Gnome Shell 插件的图标；
- [Hide Lock item in System Menu](https://extensions.gnome.org/extension/5091/hide-lock-item-in-system-menu/)：在系统菜单的位置隐藏「锁定」选项；
- [Lock Keys](https://extensions.gnome.org/extension/36/lock-keys/)：在面板处添加一个 Numlock & Capslock 是否锁定的指示图标；
- [Notification Banner Reloaded](https://extensions.gnome.org/extension/4651/notification-banner-reloaded/)：为通知提醒添加弹出位置/动画调整功能；
- [OpenWeather](https://extensions.gnome.org/extension/750/openweather/)：在面板处添加一个可显示制定位置天气状态信息的图标；
- [Places Status Indicator](https://extensions.gnome.org/extension/8/places-status-indicator/)：在面板处添加一个用于显示文件目录导航的快捷菜单；
- [Recent Items](https://extensions.gnome.org/extension/72/recent-items/)：在面板处添加一个用于显示最近使用文件的图标；
- [Screenshot Tool](https://extensions.gnome.org/extension/1112/screenshot-tool/)：在面板处添加一个快捷截图功能的图标，可自定义 Imgur 上传；
- [Top Bar Organizer](https://extensions.gnome.org/extension/4356/top-bar-organizer/)：为面板处图标显示顺序提供组织功能，强烈推荐，拯救强迫症；
- [User Themes](https://extensions.gnome.org/extension/19/user-themes/)：允许从用户目录加载 shell 主题，美化必备插件；

## 四、后续完善

### 4.1 1Panel 面板安装

宝塔面板的商业化气息太重了，还强制要求绑手机、上传服务器信息，而我们要放在虚拟机中运行，选宝塔的话这不扯犊子，我们这里选择开源的 1Panel 面板：[在线安装 - 1Panel 文档](https://1panel.cn/docs/installation/online_installation/)。

打开终端，进行面板安装：

```shell
curl -sSL https://resource.fit2cloud.com/1panel/package/quick_start.sh -o quick_start.sh && sudo bash quick_start.sh
```

根据脚本提示，依次安装即可，视网络情况，耐心等待安装。

![1Panel 安装](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656edcb11c3b9.png)

### 4.2 SMB 自动挂载极空间文件

通过 SMB 自动挂载极空间中的文件到 Ubuntu 系统：

安装 LinuxCIFS utils：

```shell
sudo apt update
sudo apt install cifs-utils
```

创建共享凭证，具体目录依个人喜好而定，此处为 `/opt/credentials/smb`，最后在改文件中写入以下内容：

```txt
username = 你的极空间账户
password = 你的极空间密码
```

创建挂载点，具体位置同样按照个人喜好而定，根据习惯，这里放置于 opt 目录下：

```shell
sudo mkdir -p /opt/nvme
```

编辑系统文件表，完成开机自动挂载：

```shell
sudo vim /etc/fstab
```

在文件末尾按照 `文件系统 挂载目录 文件系统类型 挂载选项 <dump> <fsck> ` 的格式追内容，举例：

```txt
//极空间IP及需要挂载的文件目录 /opt/nvme cifs credentials=/opt/credentials/smb,uid=0,gid=0,file_mode=0755,dir_mode=0755 0 0
```

![挂载详情](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656eecd794897.png)

![1Panel 挂载详情展示](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656eeebf7527a.png)

### 4.3 FreeFileSync 自动同步文件

极空间没有 Linux 下的客户端，利用 SMB 挂载目录后可以通过 RealTimeSync 自动化同步文件。

从 [Download the Latest Version - FreeFileSync](https://freefilesync.org/download.php) 下载 Linux 版安装包，解压后右键执行，根据提示安装。

安装完成后，打开 FreeFileSync，完成如下流程：

![同步设定](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656ef5b78e8bc.png)

另存后，关闭软件接着打开 RealTimeSync，导入上一步中保存的批处理文件：

![自动同步](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656ef5f2b9633.png)

### 4.4 关闭 systemd-resolved 移除对 53 端口占用

如果需要安装 AdguardHome 需要先解除本地 DNS 缓存对 53 端口的占用：

```shell
sudo vim /etc/systemd/resolved.conf
```

找到 `DNSStubListener` 的注释行，并更改为 `no`：

```conf
DNSStubListener=no
```

重启服务以生效：

```shell
sudo systemctl restart systemd-resolved.service
```

#### 4.4.1 AdguardHome安装

补充内容，AdguardHome的安装，以 1Panel 面板举例，其面板中提供了一个安装，但是对外暴漏的端口太多了，很多用不上，所以这里采用自行创建模版来安装：

```yml
version: "3"
services:
  adguardhome: 
    image: adguard/adguardhome
    container_name: app_adguardhome
    network_mode: bridge
    restart: always
    volumes:
      - '/opt/docker/adguardhome/work:/opt/adguardhome/work'
      - '/opt/docker/adguardhome/conf:/opt/adguardhome/conf'
    ports:
      - 53:53
      - 53:53/udp
      - 23301:80
      # - 23301:3000
```

注：第一次安装时将 23301:80 注释，将 23301:3000 取消注释，解释原因：对于容器， 3000 端口仅在初始化时使用，之后使用 80 端口访问应用，所以完成初始化后再重新编辑一下端口映射即可（没有必要在保持 3000 端口的映射了）。

![AdguardHome](../../img/article/23-07@极空间虚拟机部署UbuntuServer/656efc436c8b1.png)

推荐订阅的拦截规则：

- [anti-AD Filters](https://anti-ad.net/)：`https://anti-ad.net/easylist.txt`；

### 4.5 卸载 Snap

Snap 不遵循系统样式，虽然解决了环境依赖，但还是不喜欢，此处记录卸载的方式：

读取系统中的 Snap 应用列表：

```shell
sudo snap list
```

根据软件名称，将所有软件全部卸载，此火狐举例：

```shell
sudo snap remove --purge firefox
```

完成所有软件卸载后，最后移除 Snap 服务：

```shell
sudo apt remove --autoremove snapd
```

### 4.6 提供原生支持国产软件

- [微信](https://www.ubuntukylin.com/applications/106-cn.html)：来源优麒麟商店，没有托盘，无法后台。

- [QQ](https://im.qq.com/linuxqq/download.html)：全新重构，各项功能可正常使用，点个赞。

- [QQ 音乐](https://y.qq.com/download/download.html)：功能上和 Windwos 版本类似。

  - Ubuntu 22.04 目前打开崩溃，修改启动命令去掉沙盒。

    ```shell
    sudo gedit /usr/share/applications/qqmusic.desktop
    ```

    ```desktop
    Exec=/opt/qqmusic/qqmusic --no-sandbox %U
    ```

  - （说来搞笑，最先支持的网易云反而在官网把 Linux 应用下架了）

- [腾讯视频](https://v.qq.com/download.html#Linux)：腾讯视频 Linux 客户端，其实网页看也不是说不可以。

- [WPS](https://www.wps.cn/product/wpslinux)：2019 版本，满足轻度办公，另 PDF 可以使用 [福昕阅读器](https://www.foxitsoftware.cn/downloads/)。

- [CAJViewer](http://cajviewer.cnki.net/download.html)：支持 CAJ、PDF、KDH、NH、CAA、TEB 文件类型。

- [百度网盘](https://pan.baidu.com/download#linux)：在限速这块儿，是不分平台拿捏的死死的。

- [坚果云](https://www.jianguoyun.com/s/downloads)：同步盘，价格不菲，优势是速度很快。

- [钉钉](https://www.dingtalk.com/download?source=1121&isLite=0#/)：钉钉 Linux 版本客户端。

- [有道](http://cidian.youdao.com/multi.html)：有道词典 Linux 版。

- [Xmind](https://xmind.cn/download/)：思维导图。
