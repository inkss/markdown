# Ubuntu迁移到Deepin的一些小心得

> 用Ubuntu系统也快半年了，最近因为Ubuntu系统出了些意外(我动内核搞挂了又懒得修)，于是就决定直接重装。然而手上又有一张刻录好的Deepin光盘，索性体验下Deepin系统(上次安装似乎是在一年前)，最终体验下来，感觉**Deepin真的很让人省心很多，人性化之处是方方面面的**。就打算正式使用Deepin系统，所以写这个文章也做*以后备份使用*。

![neofetch](http://upload-images.jianshu.io/upload_images/6490512-21b7bce3cad5a272.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---

## 一、基础性配置

### 1.安装

无论是刻录在光盘上还是U盘里，这一步都很简单，接下来就是安装Deepin系统。

因为我是准备了彻底重装系统，所以事先是**清空了整张硬盘删除了分区表**。而使用linux的习惯也是`/home`分区和`/`根分区分开，为了重装时节省同步数据的时间，所以在默认的操作下安装硬盘这部分是自行选择的。

因为没有分区表，所以sd1先分成一个efi分区，至于大小deepin下默认的最小大小是300M，我搜索了一些资料没有找到详细的，索性我就分了512MB。接着是sd2挂载成根分区，sd3挂载成/home分区，具体大小视硬盘而定。因为我笔记本16G内存，所以就不分swap分区了。接下来就是正常的安装过程。

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

![Dock](http://upload-images.jianshu.io/upload_images/6490512-9b94218ed62f68b4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

在Deepin的官网里看到过，Deepin的Dock分可编辑区和不可编辑区，可是对我来说不可编辑区的一些是没用处的，在搜索时发现了这些是以插件的形式存在的，那解决方法就简单了。

以root身份打开一个文件管理器，定位到`/usr/lib/dde-dock`，首先整个备份此目录下的plugins目录，然后进入plugins删除不需要的插件就行了。
  我是删除了:时间、磁盘挂载的、网络和声音(libsound.so)

### 5.安装GTK主题

就像Ubuntu下Flatabulous主题泛滥一样，有着类似MacOs样式的osx-arc-collection主题应该也有很多人安装吧。

首先，去Github下载deb安装包，地址是:[LinxGem33/OSX-Arc-White](https://github.com/LinxGem33/OSX-Arc-White/releases)。然后，打开到安装包的目录，右键开启终端执行命令:`sudo dpkg -i xxxx.deb`安装主题。

Ubuntu下有个管理主题的unity-tweak-tool，同理Gtk桌面也有个**gnome-tweak-tool**，但是似乎deepin的设置里本就能进行主题之类的管理。

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

Nautilus:只谈在终端下，`sudo nautilus`打开root权限的文件管理器比我记不住名字的深度文件管理器要方便。

Guake:F12嗖的刷出来一个终端。

### 2.安装谷歌浏览器

起码这样安装的可以**登录谷歌账号**，安装代码如下：

    sudo wget http://www.linuxidc.com/files/repo/google-chrome.list -P /etc/apt/sources.list.d/

    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | sudo apt-key add -

    sudo apt-get update

    sudo apt-get install google-chrome-stable

### 3.安装VMware

![VMware](http://upload-images.jianshu.io/upload_images/6490512-e080f00748e4b3ef.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

商店里的自然不是最新的且有些不太一样的感觉，还是自行去官网下载自行安装罢了。下载地址：[vmware for linux](https://www.vmware.com/products/workstation-for-linux.html).下载下来的格式是.bundle，其安装方式为:

    # 赋予可执行权限 | 也可以右键属性设置
    chmod +x VMware-Workstation-Full-14.0.0-6661328.x86_64.bundle

    # 安装 | 可能有点慢，等待一会儿
    sudo ./VMware-Workstation-Full-14.0.0-6661328.x86_64.bundle

至于卸载方法是:`sudo vmware-installer -u vmware-workstation`

不太和谐的VMware14的激活密钥：`CG54H-D8D0H-H8DHY-C6X7X-N2KG6`，反正也是网上百度的。

### 4.安装FreeFileSync

在Windows时就使用的同步软件，放在Linux下依旧好用，只是商店不是最新，而且同步的软件还是自己配置吧。

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

![Simplescreenrecorder](http://upload-images.jianshu.io/upload_images/6490512-b531df6e2d57119e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

这是一个录屏软件，在深度的源里有，特别好用。它就像Windows下的Bandicam一样爽，主要是那个**跟随鼠标录制**，讲真深度的录屏纯纯只是拿来截GIF了。

    # 终端执行
    sudo apt-get install simplescreenrecorder

---

## 三、开发环境搭建

* 编程之类的个人喜欢**自行安装**

### 1.Oracle Jdk配置

最新的JDK是1.9，不过我用的是1.8。下载地址是[JDK1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)、[JSK1.9](http://www.oracle.com/technetwork/java/javase/downloads/jdk9-downloads-3848520.html)。

将下载的文件解压，JDK的目录原则上是可以放在任意目录的，但是我以前留下来的代码一直是把f放在：`/usr/lib/jvm/jdk1.8`，原因是如果安装OpenJDK，openjdk的目录就是在/usr/lib/jvm/目录下，潜意识里想放在这儿。

接下来是配置环境变量，看了一下，Deepin是用environment，但是profile也能其效果哇所以就用以前的代码哈哈。Linux下环境变量同一个变量多个值的写法是：`export PATH=$PATH:目录一:目录二:目录三`

    # 终端下输入
    sudo gedit /etc/profile

    # 打开文本编辑器后，在最下面添加：
    export JAVA_HOME=/usr/lib/jvm/jdk1.8

    export JRE_HOME=${JAVA_HOME}/jre

    export CLASSPATH=.:${JAVA_HOME}/lib:{JRE_HOME}/lib

    export PATH=$PATH:{JAVA_HOME}/bin:

然后保存退出，接下来在终端下继续输入

    # 使环境变量生效
    source /etc/profile

然后就是，在Profile的第35行`tty | egrep -q tty[1-6] && export LC_ALL=C`，把它注释掉吧，不然source时会报：`no matches found: tty[1-6]`

接下来是添加软链接：

    sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk1.8/bin/java 300

    sudo update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/jdk1.8/bin/javac 300

    sudo update-alternatives --install /usr/bin/jar jar /usr/lib/jvm/jdk1.8/bin/jar 300

此时，终端输入`java -version`应该能出结果了，但是此时也一定会出现：

    Picked up_JAVA_OPTIONS:-Dawt.useSystemAAFontSettings=gasp

Deepin的锅，Ubuntu下没见过。解决方案有两种：

* rm /etc/profile.d/java-awt-font-gasp.sh

* 没搜到，忘了，反正就是在profile里加一句什么来着

**多个JDK的切换**，就是在添加软链接时除了告诉系统这个路径存在这个JDk外，还提供了优先级。然而我们也可以手动切换：

    # 切换xxx
    sudo update-alternatives --config xxx

比如切换JDK，就用java替代xxx输入就行，当然如果系统里只有一个选择，那自然是不需要切换的。

### 2.Anaconda3配置

Anaconda3里的是Python3.6.1，首先自然是去官方下载：[Anaconda installer archive](https://repo.continuum.io/archive/),我备份的一个是Anaconda3-4.4.0-Linux-x86_64.sh，它最新都到5.0.0.1了嗝。下载下来是.sh文件，终端下输入：

    # 执行
    ./Anaconda3-5.0.0.1-Linux-x86_64.sh

然后是一堆东西，一直点回车。然后它会问你是否安装，这时候输入yes安装，接下来是**指定安装目录**，不指定就直接回车，我这里是老规矩统一改到/home/inks/inks/APP目录里。最后一个询问是问你要不要**把Anaconda3加入到shell的环境变量**里，这是选择yes是吧。

值得注意的是，它默认添加到bash的环境变量里了，如果你是跟我一样吧shell换成了oh-my-zsh，则手动修改下。

    # 修改zsh终端的环境变量
    gedit /home/yourname/.zshc

    # 修改bash终端的环境变量
    gedit /home/yourname/.bashrc

### 3.JavaWeb开发环境搭建[IDEA+MySQL+Tomcat]

轮到我的JavaWeb了，本学期的作业就在Deepin上写了。自然的，IDEA去官网下载：[IntelliJ IDEA](https://www.jetbrains.com/idea/)。至于激活，不负责任的讲就是万能某宝。接着下载Tomcat：[Apache Tomcat®](https://tomcat.apache.org/download-80.cgi)。先安装最简单的MySQL。

    sudo apt-get install mysql-server

    sudo apt-get install mysql-client

    sudo apt-get install mysql-workbench

Tomcat随便解压到一个地方，IDEA的特性，不需要配置它的环境变量。然后是IDEA，将下载下来的.tar.gz解压，进入到解压文件的`~/bin`目录里，打开终端输入./idea.sh开始安装。(jetbrains家的软件安装都是同样的到bin目录执行.sh文件安装)

简述一下安装：显示选择导入配置，没有就不选择，接下来就是选择一些主题之类的，此外启动器的图标也在此时设置，如果此时没有设置也可以在安装完成后通过`Configure->Create Desktob Entry`设置图标。

接下来详述JavaWeb项目的配置：

IDEA在默认情况下是不往Tomcat的webapps导出生成的项目，除非是采用Maven构建的项目。IDEA的编译后的输出目录是在项目目录下的/out目录里，所以与Eclipse最大的区别是不需要在web建立lib，和一些博客里提到的修改IDEA的导出目录。

建立JavaWeb项目步骤：

* Create New Project

* 选择JavaWeb Enterprise

* 下拉勾选Web Application

* 点开Application Server选择Tomcat server，修改Tomcat Home为你的Tomcat解压目录

* 设置Project SDK，点New就会直接到你环境变量里写的JDK目录，确认即可

* Next 然后输入你的Project name

* Finish 完成创建

接着是项目的结构配置，以前总结过，参考:[IDEA部署JavaWeb项目](http://www.jianshu.com/p/df68db55af12)。

![](http://upload-images.jianshu.io/upload_images/6490512-d2f0449df9e51aa4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 4.Android SDK配置

先下载：[sdk-tools-windows-3859397.zip](https://developer.android.com/studio/index.html?hl=zh-cn)

然后解压它，它包含了tools目录，比如现在的目录是`~/SDK/tools`，那么环境变量接写成：（**先参考JDK环境变量的配置**）

    # 这是我存在SDK的目录，依据自己情况更改
    export ANDROID_SDK_HOME=/home/inks/inks/APP/SDK

    # 前面的是JDK的
    export PATH=$PATH:${JAVA_HOME}/lib:${ANDROID_SDK_HOME}/tools:${ANDROID_SDK_HOME}/platform-tools

此时你是没有platform-tools目录的，这个是在Android Studio安装时把SDK目录指定到这个SDK文件夹后就有了。

### 5.Android Studio 3.x预览版安装搭建

预览版的下载地址是：[Android Studio 3.0](https://developer.android.com/studio/preview/index.html?hl=zh-cn),Android Studio是基于IDEA社区版，所以**安装方法和IDEA类似**，第一次安装会报一个让你**设置proxy**的，我这里一直挂着全局代理都没辙依旧报，**直接点取消**就行。然后**手动指定SDK的目录**，然后哗啦啦下载，SDK的下载速度最快了，我这边差不多每次都每秒十兆左右。

### 6.一些编辑器Atom/VsCode/sublime

Atom和VsCode均有deb包，所以也就是简单的去官网下载安装的过程。

至于sublime，说实话，我sublime用的最少，所以也就没装。值得注意的是Ubuntu下sublime有无法输入中文的问题，在Github的：[sublime-text-imfix](https://github.com/lyfeyaj/sublime-text-imfix)下有解决方案，搜狗输入法是基于Fcitx，Deepin应该也有这个问题。