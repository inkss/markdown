---
title: Ubuntu 18.04 安装笔记
toc: true
date: 2019/04/08 23:10
updated: 2020/05/10 10:42
tag:
  - Ubuntu 18.04
categories: 教程
description: 完整介绍 Ubuntu 18.04 LTS X86_64 的安装过程，拒绝零零散散。
abbrlink: c6df61d3
type: linux
icons: [fad fa-fire]
---

<!-- ![Ubuntu 18.04 安装配置和美化](https://img.inkss.cn/inkss/static/Ubuntu18.04安装记录.assets/image-20200507105832915.png) -->
<br>
{% p center logo ultra, '<i class="fab fa-linux"></i>' %}
{% p center logo large, Ubuntu 18.04  %}
{% p center logo small gray, 安装配置和美化 %}
<br>
<br>

------

{% folding cyan, 文章更新日志 %}

```sh
2019.04.28
  + 重构文章，修改并调整内容。
  + 补充来源链接，方便时效性内容的验证。
2019.07.29
  + 更正 Shell 的安装命令。
  + 解决下载速度慢的小工具。
  - 删除所有配图。
2020.05.07
  + 添加原来的配图（整理的时候，发现当初的桌面还怪好看的，容我置顶得瑟会儿）
```

{% endfolding %}

------

## 一、安装操作系统

### 1.1 引导盘

- 刻录进光盘：

  - 使用 *[UltraISO](https://www.ultraiso.com/download.html)* 对镜像文件进行刻录，同时存在 Legacy , UEFI 两种引导项，需通过 UEFI 形式读入光盘。

- 刻录进 U 盘：

  - **方案一：解压镜像文件** 【**推荐**】

    将 U 盘格式化为 **FAT32** ，然后解压操作系统的镜像文件至 U 盘根目录，完成 “刻录” 。

  - 方案二：使用刻录工具

    利用软件 *[Rufus](https://rufus.akeo.ie/?locale=zh_CN)* 完成镜像刻录。

> 刻录 Windows PE 系统，如“微 PE”可以选择三分区，也就是引导一个分区，PE 一个分区，U 盘剩余空间一个分区；而如果利用方案一的解压方式，那么 U 盘将被识别出两个引导，Windows & Linux 。

### 1.2 分区

分区，也就是提前为 Ubuntu 系统划分空间，这一步可以在 Windows 下进行，也可以在安装操作系统时进行。

Linux 系统的分区只建议划分根 `/` 和家目录 `/home` ，如此系统文件与个人文件分离，最大程度的保留数据。

交换分区：建议使用 swap 文件代替 swap 分区，削减多余的分区。

### 1.3 安装系统

一些注意事项：

- 硬盘格式：GPT ；引导类型：UEFI 。
- 单系统用户，务必准备一个 **EFI (ESP)** 分区，否则无法写入 Grub 引导。
- 安装过程中建议勾选 **最小安装** 、 **安装 Ubuntu 时下载更新** 和 **为图形或无线硬件安装第三方软件** 。
- 关于下载速度的问题：如无合适的解决方案，可安装系统换源后再进行下载操作。

------

## 二、开箱即用的操作系统

> 遗憾的是，Ubuntu 的开箱体验并不优秀，不过好在配置过程也不复杂，完成 2.1 和 2.3 便差不多可用了 。
>
> 本节按照线性顺序进行，虽然实际安装过程中，限于网速多半会先安装 Chrome 和 Clash （相关内容位于第三节）。此外操作系统中自带的火狐浏览器为国际版，而非国内谋智代理的火狐，两者账户不互通。

### 2.1 第一次重启前

#### 2.1.1 完成第一次更新

先移步到 **所有软件 (Win + A)**→**软件更新器** ，等待系统完成更新。（其实等待一会儿会自动弹出的）

接着移步到 **所有软件**→**设置**→**区域和语言**→**管理已安装的语言** ，完成语言列表的更新。

#### 2.2.2 转移备份文件

在选择重启操作系统前，拷贝文件到新系统中（如果存在的话）。

#### 2.2.3 解决双系统时差问题

Windows + Linux 需求用户可以使用以下代码在终端中执行解决此问题。

```sh
timedatectl set-local-rtc 1 --adjust-system-clock
```

#### 2.2.4 替换 Shell

不得不说，有一个智能的补全能力强大的终端还是非常有必要的，二选一。

(a). [oh-my-zsh](https://github.com/robbyrussell/oh-my-zsh)

```sh
sudo apt install git
sudo apt install zsh
sh -c "$(wget -O- https://raw.githubusercontent.com/robbyrussell/oh-my-zsh/master/tools/install.sh)"
chsh -s /usr/bin/zsh
```

(b). [fish](https://launchpad.net/~fish-shell/+archive/ubuntu/release-3)

```sh
sudo apt-add-repository ppa:fish-shell/release-3
sudo apt-get update
sudo apt-get install fish
chsh -s /usr/bin/fish
```

```sh
set fish_greeting 
fish_config
```

#### 2.2.5 终端下的包安装器

相比于图形界面 ，在终端下安装 deb 包可以获得更多的信息提示，但是使用 `dpkg` 命令又无法自动解决依赖问题，这里可以使用 `gdebi` 安装器解决此问题。

```sh
sudo apt install gdebi
```

### 2.2 换一套主题

> 此节是为伸手党准备的，详细的内容可参见 `2.3 主题自定义` （新手可以不必去看）。
>
> 可以通过本节内容快速完成主题方面的修改，注：`gnome-shell-extensions` 是一些常用扩展的集合，如 User Themes 等（事实上它可以在应用商店里搜索安装）。

![外观配置](https://img.inkss.cn/inkss/static/Ubuntu18.04安装记录.assets/image-20200507105957944.png)

#### 2.2.1 系统主题 Sieera

```sh
sudo add-apt-repository ppa:dyatlov-igor/sierra-theme
sudo apt update
sudo apt install sierra-gtk-theme       # point releases
sudo apt install sierra-gtk-theme-git   # git master branch
```

- 相关链接：[vinceliuice](https://github.com/vinceliuice)/[Sierra-gtk-theme](https://github.com/vinceliuice/Sierra-gtk-theme)

#### 2.2.2 应用图标 suru-plus

```sh
wget -qO- https://raw.githubusercontent.com/gusbemacbe/suru-plus/master/install.sh | env DESTDIR="$HOME/.icons" sh
```

- 相关链接：[gusbemacbe](https://github.com/gusbemacbe)/[suru-plus](https://github.com/gusbemacbe/suru-plus)

#### 2.2.3 Grub 引导主题 Fallout

```sh
wget -O - https://github.com/shvchk/fallout-grub-theme/raw/master/install.sh | bash
```

- 相关链接：[shvchk](https://github.com/shvchk)/[fallout-grub-theme](https://github.com/shvchk/fallout-grub-theme)

#### 2.2.4 鼠标光标 oxy-blue

- 下载 [oxy-blue.zip](https://www.opendesktop.org/p/1274872/)，解压文件到：`~/.icons` 。

#### 2.2.5 文泉驿字体

- 文泉驿字体 微米黑/正黑：

  ```sh
  sudo apt install fonts-wqy-microhei fonts-wqy-zenhei
  ```

- 终端字体 powerline ：

  ```sh
  sudo apt-get install fonts-powerline
  ```

#### 2.2.6 优化工具

```sh
sudo apt install gnome-tweak-tool
sudo apt install gnome-shell-extensions
```

- 然后，从应用列表中打开一个名为 **优化** 的软件，在扩展中启用 “ User Themes ”；

> 在外观中修改：应用程序、光标、图标和 Shell 就大功告成了，最后不要忘记换一个赏心悦目的桌面背景。

#### 2.2.7 输入法和 Dock

- 参阅 2.3 部分的相关内容。

### 2.3 主题自定义

> 主题自定义中共包含 6 个部分，涉及 2.2 中各项具体的解释。

#### 2.3.1 Gnome-tweak-tool

```sh
sudo apt install gnome-tweak-tool
```

移步到 **所有软件**→**Ubuntu 软件**→**附加组件** ，在此处安装相应的 Shell 组件（或者参考 3.2 节）。

> 为了自定义 Shell 主题（加载本地文件），需要安装、启用插件：*User Themes* 。

#### 2.3.2 主题 图标 字体

安装目录有两种，区别上类似于 Windows 环境变量里的个人和系统。

1. 主题存放目录：`/usr/share/themes` 或 `~/.themes`
2. 图标存放目录：`/usr/share/icons` 或 `~/.icons`
3. 字体存放目录：`/usr/share/fonts` 或 `~/.fonts`

其中 */usr/share* 目录需要 root 权限才能修改，可以对文件管理提权后打开：

```sh
sudo nautilus
```

#### 2.3.3 Grub 启动项主题

主题包地址：[Gnome Look - GRUB Themes](https://www.gnome-look.org/browse/cat/109/order/latest) （自行挑选喜欢的）

**(a)** **手写配置文件**

安装步骤 ：首先下载主题包，多为压缩包，解压出文件。使用 `sudo nautilus` 打开文件管理器。

定位到目录：`/boot/grub`，在该目录下 **新建文件夹** ：`themes`，将解压出的文件拷贝到文件夹中。

接着（终端下）使用 gedit 修改 *grub* 文件：

```sh
sudo gedit /etc/default/grub
```

在该文件末尾添加：

```sh
# GRUB_THEME="/boot/grub/themes/主题包文件夹名称/theme.txt"
GRUB_THEME="/boot/grub/themes/fallout-grub-theme-master/theme.txt"
```

更新配置文件：

```sh
sudo update-grub
```

**(b)** **利用软件 Grub Customizer** 【**推荐**】

```sh
sudo add-apt-repository ppa:danielrichter2007/grub-customizer
sudo apt install grub-customizer
```

可使用该软件定制 Grub ，如修改启动项名称，**默认启动改为上一次启动项** 。

- 相关链接：[Launchpad PPA for Grub Customizer](https://launchpad.net/~danielrichter2007/+archive/ubuntu/grub-customizer)

![Grub Customizer](https://img.inkss.cn/inkss/static/Ubuntu18.04安装记录.assets/image-20200507110107616.png)

#### 2.3.4 GDM 登录背景图

更换登录界面的背景图需要修改 `ubuntu.css`，它位于 `/usr/share/gnome-shell/theme` 。

```sh
sudo gedit /usr/share/gnome-shell/theme/ubuntu.css
```

在文件中找到关键字 `lockDialogGroup`，如下行：

```sh
#lockDialogGroup {
   background: #2c001e url(resource:///org/gnome/shell/theme/noise-texture.png);
   background-repeat: repeat; }
```

修改图片路径即可，样例如下：

```sh
#lockDialogGroup {
background: #2c001e url(file:///home/inkss/APP/ink_img/img.jpg);
   background-repeat: no-repeat;
   background-size: cover;
   background-position: center; }
```

![GDM](https://img.inkss.cn/inkss/static/Ubuntu18.04安装记录.assets/image-20200507110222935.png)

#### 2.3.5 输入法 中州韵和搜狗

> 首先，默认状态下 Ubuntu 的中文输入法属于可用但不完全好用的状态，这里记录两类输入法，二选一。

(a) **中州韵输入法**

前置提醒：中州韵没有 GUI 界面，只能通过配置文件进行定制，需要一定的研究能力。

基于 IBus 框架的中州韵（默认为此框架）：

```sh
sudo apt-get install ibus-rime
```

- 相关链接：[ibus-rime](https://github.com/rime/home/wiki/RimeWithIBus)

(b) **搜狗输入法**

搜狗基于 Fcitx 输入框架，默认没有安装，需要先安装框架：

```sh
sudo apt install fcitx
```

去 [搜狗输入法官网](https://pinyin.sogou.com/linux/?r=pinyin) 下载输入法安装包安装：

```sh
sudo gdebi xxxxxx.deb
```

然后移步到 **设置**→**区域和语言** ，删除一部分输入源，只保留汉语，接着选择 **管理已安装的语言** ，修改 *键盘输入法系统* 为 **fcitx** 。关闭窗口，打开所有程序，选择软件  **Fcitx 配置** ，选择加号添加搜狗输入法。

> 如果没有找到搜狗，就重启系统，再次重复以上步骤即可。（多半找不到，呱）
>
> 推荐一个搜狗输入法皮肤：[简约-信](https://pinyin.sogou.com/skins/detail/view/info/519557?rf=subject_jjzq&tf=p) 。
>
> PS：两个输入法可以共存。

#### 2.3.6 Dock - Docky

一个第三方 Dock 软件，颜值上比 Ubuntu 自带 Dock 好了些许。

```sh
sudo apt install docky
```

- 【可选】如何 [*去掉 Docky 第一个图标*](https://my.oschina.net/ic4907/blog/158747)

在 **Ubuntu SoftWare** 中搜索 *Configuration Editor* ，安装后打开软件，定位到：

**`/apps/docky-2/Docky/Items/DockyItem`**

取消 *ShowDockyItem* 的勾选状态（决定是否显示 第一个 Docky）

------

## 三、后续完善

> 此节内容可跳跃观看，仅供参考，**合理辨认内容是否过时**。

### 3.1 DeepinWine Wine QQ

首先需要在本机下载 Deepin-Wine 环境：[**deepin-wine-ubuntu**](https://github.com/wszqkzqk/deepin-wine-ubuntu)

克隆或下载压缩包到本机，**解压后** 在终端目录下执行命令：`./install.sh` 安装环境。

容器下载地址：[Index of /deepin/pool/non-free/d/](http://mirrors.aliyun.com/deepin/pool/non-free/d/) ，使用方法见仓库中的 [ReadMe](https://github.com/wszqkzqk/deepin-wine-ubuntu/blob/master/README.md) 文件。

> **关于托盘**：安装 *TopIconPlus* 的 gnome-shell 扩展。
>
> 然后在所有软件中找到 **优化 (Gnome-tweak-tool)** ，在扩展中打开 *Topicons plus* 。

### 3.2 Gnome 扩展

获取扩展的方法很多：终端命令，软件中心下载，浏览器下载等，这里主要介绍浏览器下载。

首先安装 Gnome Shell ：

```sh
sudo apt install chrome-gnome-shell
```

然后安装浏览器插件（**谷歌浏览器**）：[Chrome 网上应用商店](https://chrome.google.com/webstore/detail/gnome-shell-integration/gphhapmejobijbbhgpjhcjognlahblep) 。

无条件的同学可以使用火狐浏览器安装扩展：[GNOME Shell integration](https://addons.mozilla.org/zh-CN/firefox/addon/gnome-shell-integration/?src=search) 。

浏览器插件安装完成后点击 *插件图标* 就能进入：**[Shell 扩展商店](https://extensions.gnome.org/)** 。

**Gnome 扩展推荐**（点击链接进入，按需使用） :

| 扩展                                                         | 简要功能描述                               |
| :----------------------------------------------------------- | :----------------------------------------- |
| [Appfolders Management extension](https://extensions.gnome.org/extension/1217/appfolders-manager/) | 【荐】添加文件夹                           |
| [Applications Menu](https://extensions.gnome.org/extension/6/applications-menu/) | 在顶部添加一个应用程序入口                 |
| [Autohide Battery](https://extensions.gnome.org/extension/595/autohide-battery/) | 【荐】自动隐藏电源（充电状态下已满）       |
| [Caffeine](https://extensions.gnome.org/extension/517/caffeine/) | 取消自动锁屏（应用前台允许下）             |
| [Clipboard Indicator](https://extensions.gnome.org/extension/779/clipboard-indicator/) | 剪切板管理工具                             |
| [Coverflow Alt-Tab](https://extensions.gnome.org/extension/97/coverflow-alt-tab/) | 【荐】Alt Tab 切换应用（更酷炫的界面）     |
| [Dash to Dock](https://extensions.gnome.org/extension/307/dash-to-dock/) | 【荐】Dock （大名鼎鼎）                    |
| [Dash to Panel](https://extensions.gnome.org/extension/1160/dash-to-panel/) | 【荐】对顶栏的操作处理（诸如自动隐藏等）   |
| [EasyScreenCast](https://extensions.gnome.org/extension/690/easyscreencast/) | 录屏工具（录制质量优秀）                   |
| [Extension update notifier](https://extensions.gnome.org/extension/1166/extension-update-notifier/) | 【荐】自动推送所有扩展的更新信息           |
| [Keys Indicator](https://extensions.gnome.org/extension/1105/keys-indicator/) | 【荐】顶栏显示 shift,alt,ctrl,num,cap 状态 |
| [Never close calendar event](https://extensions.gnome.org/extension/1439/never-close-calendar-event/) | 从不清除日历事件                           |
| [OpenWeather](https://extensions.gnome.org/extension/750/openweather/) | 【荐】顶栏显示天气情况（支持中文）         |
| [Places Status Indicator](https://extensions.gnome.org/extension/8/places-status-indicator/) | 【荐】提供快捷目录入口（同文件管理器）     |
| [Popup dict Switcher](https://extensions.gnome.org/extension/1349/popup-dict-switcher/) | 一键开关划词翻译 >>> 参考下文<<<           |
| [Removable Drive Menu](https://extensions.gnome.org/extension/7/removable-drive-menu/) | 【荐】移除可移动设备                       |
| [Screenshot Tool](https://extensions.gnome.org/extension/1112/screenshot-tool/) | 【荐】截图工具（挺方便）                   |
| [Sound Input & Output Device Chooser](https://extensions.gnome.org/extension/906/sound-output-device-chooser/) | 更方便的调整声音、亮度                     |
| [System-monitor](https://extensions.gnome.org/extension/120/system-monitor/) | 在状态栏中显示系统信息（需要解决依赖）     |
| [TaskBar](https://extensions.gnome.org/extension/584/taskbar/) | 类似于 Windows 任务栏的显示效果            |
| [Time ++](https://extensions.gnome.org/extension/1238/time/) | 番茄钟（闹钟、秒表、计时器）               |
| [TopIcons Plus](https://extensions.gnome.org/extension/1031/topicons/) | 【荐】顶栏显示应用图标（托盘显示）         |
| [User Themes](https://extensions.gnome.org/extension/19/user-themes/) | 【荐】允许本地安装使用 Shell 主题          |

> 以上表格提到的所有扩展都能在 Ubuntu 18.04 中使用，若出现安装失败，请检查 **是否满足相关依赖** 。

### 3.3 文件备份合集

#### 3.3.1本地同步备份

[**FreeFileSync**](https://freefilesync.org/) 是一款本地同步 **备份** 软件：如将本地硬盘上的文件同步到移动硬盘上。可以做到增量备份、自动识别差异项等。同步方式有：*双向、镜像、更新* 。下载地址：[Download FreeFileSync](https://freefilesync.org/download.php) ，解压后直接点击 *FreeFileSync* 文件就能使用。

- 因为没有图标，这里给出写入图标的方式（有工具就绝对不手写）：

```sh
# –-no-install-recommends 参数避免安装非必须的文件，从而减小体积
sudo apt install --no-install-recommends gnome-panel
```

- 创建应用程序启动方式

```sh
sudo gnome-desktop-item-edit /usr/share/applications/ --create-new
```

命令：点浏览展开到解压目录，选择 *FreeFileSync* 的可执行文件。

图标：解压包中有一个名为 *Resources.zip* 的压缩包，含有一些图片，从中选取软件图标。

#### 3.3.2 云端同步备份

[**坚果云**](https://www.jianguoyun.com/) 是一款云端 **同步** 软件，与同类的 *OneDrive* 相比，坚果云做到了全平台兼容。

其免费版本流量限制、空间不限（下载 3G/月，上传 1G/月）；专业版一年 42G 空间、不限流量，*199.90* 元（*差不多是处于打折状态下 Office 365 的价格，有 1T OneDrive 空间*）。

下载地址：[坚果云 Linux 版](https://www.jianguoyun.com/s/downloads/linux) （普通的 deb 安装包）

#### 3.3.3 局域网文件互传

**Chfs** 是一个免费的、HTTP 协议的文件共享服务器，使用浏览器可以快速访问。它具有以下特点：

1. **单个文件**，整个软件只有一个可执行程序，无配置文件等其他文件
2. 跨平台运行，支持主流平台：Windows，Linux 和 Mac
3. 支持扫码下载和手机端访问，手机与电脑之间共享文件非常方便
4. 支持账户权限控制和地址过滤

下载地址：[CuteHttpFileServer](http://iscute.cn/chfs) ，使用方案见网站说明。

#### 3.3.4 在线文件管理器

基于 **Caddy** 的 **FileBrowser** 模块，除此之外还可以使用 webdav 模块启用 webdav 功能。

- 安装 Caddy

```sh
curl https://getcaddy.com | bash -s personal http.filebrowser
```

- 新建配置文件 `Caddyfile` ，文件位置 `/home/ubuntu/caddy/Caddyfile`

```sh
:8080 {
  gzip
  timeouts none
  filebrowser / / {
    database /home/ubuntu/caddy/filebrowser.db
  }
}
```

以上写法的作用是通过 IP 的 `8080` 端口访问网页，这个 IP 既可以是公网 IP 也可以是内网 IP 。具体的文件路径可以在登录网页后在网站设置内手动修改，数据库的存放目录要灵活自己选择，配置文件的存放目录同样可以自拟。

在 `/home/ubuntu/caddy/` 目录下打开终端，执行命令：`caddy` ，终端提示错误忽略即可。

打开网址：http://localhost:8080 访问，默认的用户名和密码均为：`admin` （可以手写一个脚本自动运行）。

**更多的内容可以参看这篇实验：**[基于 Caddy 搭建基于网页的文件共享管理系统](https://cloud.tencent.com/developer/labs/lab/10453) 。

相关文档：[FileBrowser # caddy](https://docs.filebrowser.xyz/installation#caddy) 。

#### 3.3.5 系统快照备份

制作快照的软件 **TimeShift** ，可以对整个分区进行备份，以分区为单位进行恢复。这里安装它即可：

```sh
sudo apt install timeshift
```

- 相关链接：[teejee2008](https://github.com/teejee2008)/[timeshift](https://github.com/teejee2008/timeshift)

![超级推荐的软件](https://img.inkss.cn/inkss/static/Ubuntu18.04安装记录.assets/image-20200507110243274.png)


### 3.4 网易云音乐

- **以下内容对应网易云音乐 1.1.0 版本。** 

首先去网易云音乐官网 [下载安装包](https://music.163.com/#/download)（Ubuntu 16.04 64 位），然后就是正常的 deb 包安装过程。

安装完毕后，会发现在应用列表中 **点击应用图标无法启动软件** ，解决方案：

修改网易云音乐的启动图标

```sh
sudo gedit /usr/share/applications/netease-cloud-music.desktop
```

修改 Exec 这一行内容

```sh
Exec=sh -c "unset SESSION_MANAGER && netease-cloud-music %U"
```

附录：网易云音乐配置及缓存目录

```sh
~/.config/netease-cloud-music
~/.cache/netease-cloud-music
```

> 参考资料地址：[Ubuntu 18.04 装了网易云音乐，难道只能用 sudo 启动吗？- @Fancy 解答](https://www.zhihu.com/question/277330447/answer/478510195)

### 3.5 Clash

> 地址：[Dreamacro](https://github.com/Dreamacro)/[clash](https://github.com/Dreamacro/clash)  A rule-based tunnel in Go.

- HTTP/HTTPS and SOCKS protocol
- Surge like configuration
- GeoIP rule support
- Support Vmess/Shadowsocks/Socks5
- Support for Netfilter TCP redirect

> 大部分 PC 下载 clash-linux-amd64.tar.gz 即可。
>
> 利用 [Clash Dashboard](http://clash.razord.top/) 切换，管理。
>
> 写个小脚本解决自启动。

### 3.6 Chrome

```sh
wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | sudo apt-key add - 
sudo sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list'
sudo apt update
sudo apt install google-chrome-stable
```

- 相关链接：[Google Linux Software Repositories](https://www.google.com/linuxrepositories/) , [UbuntuUpdates](https://www.ubuntuupdates.org/ppa/google_chrome) 。

### 3.7 Typora

```sh
wget -qO - https://typora.io/linux/public-key.asc | sudo apt-key add -
sudo add-apt-repository 'deb https://typora.io/linux ./'
sudo apt update
sudo apt install typora
```

- 相关链接：[Typora for Linux](https://www.typora.io/#linux)

### 3.8 Albert

```sh
sudo apt install curl
curl https://build.opensuse.org/projects/home:manuelschneid3r/public_key | sudo apt-key add -
sudo sh -c "echo 'deb http://download.opensuse.org/repositories/home:/manuelschneid3r/xUbuntu_18.04/ /' > /etc/apt/sources.list.d/home:manuelschneid3r.list"
sudo apt-get update
sudo apt-get install albert
```

- 相关链接：[Albert](https://albertlauncher.github.io/docs/installing/) , [OpenSUSE](https://software.opensuse.org/download.html?project=home:manuelschneid3r&package=albert)

### 3.9 popup-dict

 Linux 下的划词翻译工具，支持使用有道等多种翻译服务。

安装过程：安装 pip3 ，如果已有请忽略此步骤

```sh
sudo apt install python3-pip
```

安装 PyGObject 依赖

```sh
sudo apt install python-gi python-gi-cairo python3-gi python3-gi-cairo gir1.2-gtk-3.0
```

安装 popup-dict

```sh
sudo pip3 install popupdict
```

运行软件：使用 Gnome 扩展  [`Popup dict Switcher`](https://extensions.gnome.org/extension/1349/popup-dict-switcher/)

- 相关链接：[bianjp](https://github.com/bianjp)/[popup-dict](https://github.com/bianjp/popup-dict)

------

## 四、编程程序

### 4.1 Hexo | Node.js

> 三连击预备，Hexo 是一套静态博客系统。如果没有终端代理下载速度可能会绝望的。

- 安装 NVM ：`wget -qO- https://raw.github.com/creationix/nvm/master/install.sh | sh`
- 安装 Node：`nvm install stable`
- 安装 Hexo：`npm install -g hexo-cli`

### 4.2 Git 配置

- 生成 key ：`ssh-keygen -t rsa -C "youremail@example.com"`
- 配置用户名：`git config --global user.name "Your Name"`
- 配置邮箱：`git config --global user.email "email@example.com"`
- 测试 Github 联通：`ssh -T git@github.com`
- 将公钥提取出来命名为：`authorized_keys` 扔到服务器的 `~/.ssh` 目录就可以免密登录
- 访问远程主机：`ssh 用户名@域名/IP`

### 4.3 MySQL 8.X

> 在不做处理的情况下，命令行安装 MySQL 版本为 5.7 ，而它只适配到 Ubuntu 17.04 。

先下载 APT 存储库，地址：[MySQL APT Repository](https://dev.mysql.com/downloads/repo/apt/) 。

然后运行它，默认即为 MySQL 8.0 ，移动光标到 OK 确认即可。

接着就可以在终端下正常安装 MySQL 了：

```sh
sudo apt update
sudo apt install mysql-server
```

附录：执行安全脚本

```sh
sudo mysql_secure_installation
```

附录：安装 WorkBench

```sh
sudo apt install mysql-workbench-community
```

- 相关链接：[如何在Ubuntu 18.04中安装MySQL 8.0](https://www.howtoing.com/install-mysql-8-in-ubuntu)

## 五、补充内容

### 5.1 软件列表

- 音乐软件：[网易云音乐](https://music.163.com/#/download)、[Spotify](https://www.spotify.com/int/download/linux/)、*Audacious*
- 聊天软件：[TIM](http://mirrors.aliyun.com/deepin/pool/non-free/d/deepin.com.qq.office/)、[微信](http://mirrors.aliyun.com/deepin/pool/non-free/d/deepin.com.wechat/)、Telegram
- 办公软件：[WPS](http://www.wps.cn/product/wpslinux)、[Foxit Reader](https://www.foxitsoftware.cn/downloads/)、 *Kile*、[坚果云](https://www.jianguoyun.com/s/downloads)、XMind、百度脑图离线版
- 图形软件：*Converseen*（图片格式转换）、*Krita*（类似 PS）、*polarr* （泼辣修图）
- 下载软件：*aMule*（电驴）、*Deluge*（种子）、*qBittorrent*（种子）、*uGet*（有点类似 IDM）
- 截图录屏：*深度截图*、*深度取色器*、*Shutter*、*SimpleScreen*
- 版本管理：[GitKraken](https://www.gitkraken.com/git-client)、*Meld*
- 浏览器：Chrome、*Firefox*
- 软件启动器：Albert
- 应用商店：App Grid
- 剪切板管理：*Cliplt*
- 网络代理：electron-ssr
- 虚拟键盘：*Florence*
- 密码管理：*KeePassXC*
- 音频剪辑：*Audacity*

### 5.2 代码篇

#### 5.2.1 软件图标文件位置

`/usr/share/applications` # 大部分启动图标都在此

`~/.local/share/applications` # 一部分本地图标

`/var/lib/snapd/desktop/applications` # snap 类软件在此

对于 **.desktop* 文件，可以使用文本编辑器对图标或名称之类的进行修改。

#### 5.2.2 代码篇

```bash
# 查看所有 shell 以及如何切换
cat /etc/shells
chsh -s /xxx/xxxx

# 强制清空回收站
sudo rm -rf $HOME/.local/share/Trash/files/*

# 系统环境变量位置
/etc/profile

# 环境变量值 PATH 的一种写法
export PATH=$PATH:变量1:变量2:变量3

# 个人终端下的环境变量
~/.bashrc
~/.zshrc

# MySQL 的 root 账户密码
sudo mysql -u root # 使用管理员权限进数据库
DROP USER 'root'@'localhost'; # 删除原数据库中 root 账户
CREATE USER 'root'@'%' IDENTIFIED BY 'passwd'; # 新建并指定密码
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%'; # 赋权
FLUSH PRIVILEGES; # 更新

# 多版本切换（ java 等）
sudo update-alternatives --install <link> <name> <path> <priority>
sudo update-alternatives --remove <name> <path>
sudo update-alternatives --config <name>

# Java 环境变量的写法
# 假设 JDK 的解压目录为 /usr/lib/jvm/jdk
# 需要修改文件 /etc/profile
sudo gedit /etc/profile
# 打开文本编辑器后，在最下面添加：
export JAVA_HOME=/usr/lib/jvm/jdk
export JRE_HOME=${JAVA_HOME}/jre
export CLASSPATH=.:${JAVA_HOME}/lib:{JRE_HOME}/lib
export PATH=$PATH:{JAVA_HOME}/bin:
# 然后保存退出 刷新
source /etc/profile
# 添加连接
sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk/bin/java 300
sudo update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/jdk/bin/javac 300
sudo update-alternatives --install /usr/bin/jar jar /usr/lib/jvm/jdk/bin/jar 300

# Python Anaconda env 配置
conda create -n your_env_name python=3.7 # 创键环境
conda activate your_env_name # 激活环境

# MariaDB
# 使用自定义源 TUNA
sudo apt-get install software-properties-common
sudo apt-key adv --recv-keys --keyserver hkp://keyserver.ubuntu.com:80 0xF1656F24C74CD1D8
sudo add-apt-repository 'deb [arch=amd64,arm64,ppc64el] http://mirrors.tuna.tsinghua.edu.cn/mariadb/repo/10.3/ubuntu bionic main'
sudo apt update
sudo apt install mariadb-server
# 配置
sudo mysql_secure_installation
```

#### 5.2.3 踩坑记录

- VMware : 虚拟机安装的前置依赖为：`make` `gcc` 。

-  JetBrains toolbox ：使用 toolbox 安装软件比 snap 好太多（速度感人），它也能自动更新 IDE 。但是它会疯狂修改 `.desktop` 文件，强迫症表示必须要修改图标的样式和主题一致，把图标文件权限设置为只读就能解决。

- Minecraft 所需要的 JDK 只需要提前安装一个 `openjdk-8-jre` 。

- Life is Strange 奇异人生所需要的 Linux 版汉化：[百度网盘](https://pan.baidu.com/s/1dECSYfJ) 。

- XMind : 无启动图标，需要手动添加应用图标，有一个小麻烦是桌面图标文件所在的目录必须和启动软件同目录。此外 XMind 8 版本存在可用的破解激活方案。依赖 JDK8。
- 善用 TimeShift ，它提供了一个回滚系统的机会。

## 六、截图

![桌面](https://img.inkss.cn/inkss/static/Ubuntu18.04安装记录.assets/image-20200507110318847.png)

![软件列表](https://img.inkss.cn/inkss/static/Ubuntu18.04安装记录.assets/image-20200507110337914.png)

![系统](https://img.inkss.cn/inkss/static/Ubuntu18.04安装记录.assets/image-20200507110355777.png)