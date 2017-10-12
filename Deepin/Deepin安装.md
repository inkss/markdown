# Ubuntu迁移到Deepin的一些小心得

> 用Ubuntu系统也快半年了，最近因为Ubuntu系统出了些意外(我动内核搞挂了又懒得修)，于是就决定直接重装。然而手上又有一张刻录好的Deepin光盘，索性体验下Deepin系统(上次安装似乎是在一年前)，最终体验下来，感觉**Deepin真的很让人省心很多，人性化之处是方方面面的**。就打算正式使用Deepin系统，所以写这个文章也做*以后备份使用*。

![neofetch](https://camo.githubusercontent.com/e037953203654a4ddb69401b252cec7a1d4f862e/687474703a2f2f75706c6f61642d696d616765732e6a69616e7368752e696f2f75706c6f61645f696d616765732f363439303531322d323162376263653363616435613237322e706e673f696d6167654d6f6772322f6175746f2d6f7269656e742f7374726970253743696d61676556696577322f322f772f31323430)

---

## 一、基础性配置

### 1.安装

无论是刻录在光盘上还是U盘里，这一步都很简单，接下来就是安装Deepin系统。

因为我是准备了彻底重装系统，所以事先是**清空了整张硬盘删除了分区表**。而使用linux的习惯也是/home分区和/根分区分开，为了重装时节省同步数据的时间，所以在默认的操作下安装硬盘是自行选择的。

因为没有分区表，所以sd1先分出一个efi分区，至于大小deepin下默认的最小大小是300M，我搜索了一些资料没有找到详细的，索性我就分了512MB。接着是sd2挂载成根分区，sd3挂载成/home分区，具体大小视硬盘而定。因为我笔记本16G内存，所以就不分swap分区了。接下来就是正常的安装过程。

这样做和**win下的C、D盘，重装系统时删C保D**一个道理。

### 2.卸载

这里的卸载是指在安装完成系统后，卸载一些系统里的软件，主要原因是为了节省接下来更新系统时的下载量和一些用不到的软件也就卸载罢了。

对于我来说，我卸载了:

QQ(个人习惯，我喜欢Tim，其实Deepin最终留住我的最大原因是这个，因为Ubuntu花费些时间也能配置到类似Deepin的效果)

Chrome(默认的这个版本**无法登录浏览器的谷歌账号**，我需要同步书签设置之类，于是卸载掉它自己安装吧)

有道词典(没有离线翻译功能就只是个鸡肋了，有网络的情况下不如使用谷歌翻译)

此外还有:雷鸟邮件、远程协助、深度录音、深度云打印、深度云扫描、chm阅读器、扫描易、深度用户反馈。

### 3.修改开机等待时间

我不需要双系统，所以不需要Grub那个选择界面。

    # 终端输入:
    sudo gedit /etc/default/grub

    修改并保存返回:GRUB_TIME=0

    # 终端下更新:
    sudo update-grub

说起来，对双系统兴趣真的是不大了。曾经最疯狂的时候，仗着主板legacy和uefi都能使用，一台电脑里装了四个系统:*UEFI下Win10引导win7，Leagacy下Ubuntu引导Kali*。现在想想也是有毒。

### 4.修订Dock

![Dock](https://camo.githubusercontent.com/710ab5819ec133401882aacfd95c426cca5b9b6d/687474703a2f2f75706c6f61642d696d616765732e6a69616e7368752e696f2f75706c6f61645f696d616765732f363439303531322d396239343231386564363266363862342e706e673f696d6167654d6f6772322f6175746f2d6f7269656e742f7374726970253743696d61676556696577322f322f772f31323430)

在Deepin的官网里看到过，Deepin的Dock分可编辑区和不可编辑区，可是对我来说不可编辑区的一些是没用处的，在搜索时发现了这些是以插件的形式存在的，那解决方法就简单了。

以root身份打开一个文件管理器，定位到`/usr/lib/dde-dock`，首先整个备份此目录下的plugins目录，然后进入plugins删除不需要的插件就行了。
  我是删除了:时间、磁盘挂载的、网络和声音(libsound.so)

### 5.安装GTK主题

就像Ubuntu下Flatabulous主题泛滥一样，有着类似MacOs样式的osx-arc-collection主题应该也有很多人安装吧。

首先，去Github下载deb安装包，地址是:[LinxGem33/OSX-Arc-White](https://github.com/LinxGem33/OSX-Arc-White/releases)。然后，打开到安装包的目录，右键开启终端执行命令:`sudo dpkg -i xxxx.deb`安装主题。

Ubuntu下有个管理主题的unity-tweak-tool，同理Gtk桌面也有个**gnome-tweak-tool**，但是似乎deepin的设置里本就进行主题之类的设置了。

此外在Ubuntu下用惯了文泉驿微米黑，默认的Deepin字体不太适应而且11号也觉得小，改成微米黑后再设置12号大小。

### 6.安装oh-my-zsh

这个要比默认的bash好用好多，不多展开，安装代码是:

    # 安装Git
    sudo apt-get install git

    # 安装zsh
    sudo apt-get install zsh

    # 下载
    wget https://github.com/robbyrussell/oh-my-zsh/raw/master/tools/install.sh -O - | sh

    # 切换
    chsh -s /usr/bin/zsh

### 7.安装shadowsocks-qt5

商店或者终端里安装都可以，恩。

### 8.更新系统

至此，基本的配置就结束了，现在去**设置**升级里检查更新系统吧，更新完成后**重启一遍**即可。

---

## 二、一般性软件安装

### 1.商店应用

这里真的要感谢Deepin系统了，提供这么好的氛围。说一些商店里有趣的应用:

BieachBit:这个清理软件在Ubuntu的时候就在使用，很实用。

新立得软件包管理器。

Nautilus:只谈在终端下，`sudo nautilus`打开root权限文件比我记不住名字的深度文件管理器要方便。

Guake:F12刷出来一个终端。

### 2.安装谷歌浏览器

起码这样安装的可以**登录谷歌账号**，安装代码如下：

    sudo wget http://www.linuxidc.com/files/repo/google-chrome.list -P /etc/apt/sources.list.d/

    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | sudo apt-key add -

    sudo apt-get update

    sudo apt-get install google-chrome-stable

### 3.安装VMware

![VMware](https://camo.githubusercontent.com/570c4b188ab8770d8f9d6d239857819184d4b561/687474703a2f2f75706c6f61642d696d616765732e6a69616e7368752e696f2f75706c6f61645f696d616765732f363439303531322d653038306630303734386534623365662e706e673f696d6167654d6f6772322f6175746f2d6f7269656e742f7374726970253743696d61676556696577322f322f772f31323430)

商店里的自然不是最新的且有些不太一样的感觉，还是自行去官网下载自行安装罢了。下载地址：[vmware for linux](https://www.vmware.com/products/workstation-for-linux.html).下载下来的格式是.bundle，其安装方式为:

    # 赋予可执行权限 | 也可以右键属性设置
    chmod +x VMware-Workstation-Full-14.0.0-6661328.x86_64.bundle

    # 安装 | 可能有点慢，等待一会儿
    sudo ./VMware-Workstation-Full-14.0.0-6661328.x86_64.bundle

至于卸载方法是:`sudo vmware-installer -u vmware-workstation`

不太和谐的VMware14的激活密钥：`CG54H-D8D0H-H8DHY-C6X7X-N2KG6`，反正也是网上百度的。

### 4.安装FreeFileSync

在中在Windows时就使用的同步软件放在Linux下依旧好用，只时商店不是最新，而且同步的软件还是自己配置吧。

下载地址是：[Download FreeFileSync 9.4](https://www.freefilesync.org/download.php)

下载下来的是一个.tar.gz文件，但是没有安装文件，是属于解压后就可以执行的，类似win下的绿色版软件。这里说下我的个人习惯，我电脑命名为inks，所以对这种**能控制安装目录的软件**统一是放置在下面的这个目录（我的MyFile还叫inks）

    /home/yourPcName/MyFile/APP

不过这样子就没有图标了，但是可以手动添加，新建一个`freefilesync.desktop`，用gedit打开它，写入配置信息，我写入的是：

    #!/usr/bin/env xdg-open
    [Desktop Entry]
    Version=1.0
    Type=Application
    Terminal=false
    Icon[zh_CN]=gnome-panel-launcher
    Name[zh_CN]=FreeFileSync
    Exec=/home/inks/inks/APP/FreeFileSync/FreeFileSync
    Comment[zh_CN]=保持文件和目录同步
    Name=FreeFileSync
    Comment=Folder Comparison and Synchronization
    Icon=/home/inks/inks/APP/FreeFileSync/ico/FreeFileSync.png

保存返回，然后利用管理员权限把它扔到`/usr/share/applications`目录里。觉得很麻烦？恩，也有自动添加图标的方法：

    # 安装 Gnome 面板
    sudo apt-get install --no-install-recommends gnome-panel

    # 创建程序启动器
    sudo gnome-desktop-item-edit /usr/share/applications/ --create-new

此时会弹出一个**创建启动器**的窗口，填上名称点击图片修改好图片，至于命令，就打开到安装目录下的可执行文件就可以了。注释写不写无所谓。

### 5.安装simplescreenrecorder

![Simplescreenrecorder](https://camo.githubusercontent.com/424a6a791b7208a5850099f4488d75205e0ff70d/687474703a2f2f75706c6f61642d696d616765732e6a69616e7368752e696f2f75706c6f61645f696d616765732f363439303531322d623533316466366532643537313139652e706e673f696d6167654d6f6772322f6175746f2d6f7269656e742f7374726970253743696d61676556696577322f322f772f31323430)

这是一个录屏软件，在深度的源里有，特别好用。它就像Windows下的bdcamsetup一样爽，主要是那个**跟随鼠标录制**，讲真深度的录屏纯纯只是拿来截GIF了。

    # 终端执行
    sudo apt-get install simplescreenrecorder

---

## 三、开发环境搭建

### 1.Oracle Jdk配置

### 2.Anaconda3配置

### 3.JavaWeb开发环境搭建[IDEA+MySQL+Tomcat]

### 4.Android SDK配置

### 5.Android Studio 3.x预览版安装搭建

### 6.一些编辑器Atom/VsCode/sublime

Atom和VsCode均有deb包，所以也就是简单的去官网下载安装的过程。

至于sublime，说实话，我sublime用的最少，所以也就没装。值得注意的是Ubuntu下sublime有无法输入中文的问题，在Github的：[sublime-text-imfix](https://github.com/lyfeyaj/sublime-text-imfix)下有解决方案，搜狗输入法是基于Fcitx，Deepin应该也有这个问题。