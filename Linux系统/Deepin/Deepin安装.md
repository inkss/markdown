# 迁移系统到Deepin 15.5

## 一、安装

### 1.1 获取镜像

* 从深度官网下载镜像：[最新版本-深度科技](https://www.deepin.org/download/)
* 校验MD5值

### 1.2 制作启动盘

* Windows：[深度启动盘制作工具](https://www.deepin.org/original/deepin-boot-maker/)

### 1.3 安装

* UEFI引导与Legacy引导：
  参考[inux+Windows多系统单独引导](http://www.jianshu.com/p/ed52adff8651)
* 分区(*非必选项*)：
  安装时分区手动编辑：单独挂载`/`根分区，`/home`分区，`swap`交换分区。
  此处解释：*如果是UEFI引导，默认情况下需要一个UEFI分区（默认最低300M），将`/home`单独挂载出来是为了如果系统出现问题重装时可以保留`/home`分区中的文件（类似win下的C、D盘，重装系统时删C留D），`swap`分区根据电脑内存情况而定（例如16G内存不设置swap分区也无影响）*
---

## 二、个性化定制

### 2.1 卸载软件

首次安装完成之后，检查系统更新，所提供的更新同时包含软件更新，卸载不需要的软件有助于**减少更新下载量**。

### 2.2 修改GRUB时间

开机时进入的选择操作系统的界面（默认5s）
~~终端输入:~~
~~sudo gedit /etc/default/grub~~
~~修改并保存返回:GRUB_TIME=0~~
~~终端下更新:~~
~~sudo update-grub~~

**Deepin 15.5版本**：设置-系统信息-启动菜单

启动延时：**不勾选**

### 2.3 修订Dock

Deepin的Dock分可编辑区和不可编辑区，不可编辑区以插件形式存在于Dock。
~~以root身份打开文件管理器，定位到`/usr/lib/dde-dock`。~~
~~备份plugins目录，删除plugins中不需要的插件。~~
~~我是删除了:时间、磁盘挂载的、网络和声音(libsound.so)~~

**Deepin 15.5版本**：Dock-右键-插件
取消勾选不需要的插件

### 2.4 安装桌面主题

#### 2.4.1 主题推荐：

* OSX-Arc-White：[LinxGem33/OSX-Arc-White](https://github.com/LinxGem33/OSX-Arc-White/releases) 一款类似Mac效果的GTK主题。
* GTK2/3 [主题推荐](https://www.deepin.org/2012/04/12/gtk-2-and-gtk-3-theme-for-linux-deepin/)

#### 2.4.2 主题安装：

* deb包: 终端执行命令:`sudo dpkg -i xxxx.deb`安装主题。
* 压缩包（类似.tar.gz文件）：
  解压后复制到：
    ~/.themes （用户自定义配置）
    /usr/share/themes （全局配置，需要root权限）

#### 2.4.3 主题管理：

* 设置-个性化-主题
* 安装gnome-tweak-tool：`sudo apt install gnome-tweak-tool`

### 2.5 安装系统字体

* 右键单选`.font`文件单个安装
* 批量复制字体文件到`/usr/share/fonts`（需要root权限）

### 2.6.替换bash

将默认bash替换为oh-my-zsh:

    # 安装Git
    sudo apt-get install git

    # 安装zsh
    sudo apt-get install zsh

    # 下载
    wget https://github.com/robbyrussell/oh-my-zsh/raw/master/tools/install.sh -O - | sh

    # 切换
    chsh -s /usr/bin/zsh

---

## 三、基础软件配置

### 3.1 商店应用推荐

* **BleachBit**：清理系统垃圾
* **坚果云**：国内良心文件云同步APP（可能是所有同步类软件中唯一拥有Linux版）
* **Typora**：美观大方的Markdown编辑器
* **simplescreenrecorder**：类似Bandicam体验的Linux录屏软件
* More...

### 3.2 谷歌浏览器

不失为一种获取**最新**谷歌浏览器的方案：

    sudo wget http://www.linuxidc.com/files/repo/google-chrome.list -P /etc/apt/sources.list.d/

    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | sudo apt-key add -

    sudo apt-get update

    sudo apt-get install google-chrome-stable

### 3.3 安装VMware14

下载：[vmware for linux](https://www.vmware.com/products/workstation-for-linux.html)
安装:

    # 赋予可执行权限 | 也可以右键属性设置
    chmod +x VMware-Workstation-Full-14.0.0-6661328.x86_64.bundle
    # 安装 | 可能有点慢，等待一会儿
    sudo ./VMware-Workstation-Full-14.0.0-6661328.x86_64.bundle

卸载:`sudo vmware-installer -u vmware-workstation`
VMware14的激活密钥：`CG54H-D8D0H-H8DHY-C6X7X-N2KG6`

### 3.4 安装FreeFileSync

一款文件同步软件，使用场景：本地文件同步至移动硬盘等等。
下载：[Download FreeFileSync 9.4](https://www.freefilesync.org/download.php)
下载到.tar.gz文件，属于解压可执行型，类似win下的绿色版软件。

**添加图标**：

* 方案一：新建`freefilesync.desktop`，写入配置信息。

```base
 #!/usr/bin/env xdg-open
[Desktop Entry]
Version=1.0
Type=Application
Terminal=false
Icon[zh_CN]=gnome-panel-launcher
Name[zh_CN]=FreeFileSync
Exec=/你的可执行文件地址/FreeFileSync
Comment[zh_CN]=保持文件和目录同步
Name=FreeFileSync
Comment=Folder Comparison and Synchronization
Icon=/你的图标地址/FreeFileSync.png
```

移动到`/usr/share/applications`目录里。

* 方案二

```base
# 安装 Gnome 面板
sudo apt-get install --no-install-recommends gnome-panel
# 创建程序启动器
sudo gnome-desktop-item-edit /usr/share/applications/ --create-new
```

弹出**创建启动器**窗口，填入必要信息。

---

## 四、开发环境搭建

### 4.1 Oracle Jdk配置

目前（2017.11）最新JDK版本为1.9.
下载地址:[JDK1.8](http://www.oracle.com/technetwork/cn/java/javase/downloads/jdk8-downloads-2133151-zhs.html)、[JDK1.9](http://www.oracle.com/technetwork/java/javase/downloads/jdk9-downloads-3848520-zhs.html)。

解压下载文件，JDK目录原则上可以放在任意目录的。
本文中使用的JDK目录：`/usr/lib/jvm/jdk1.8`

**配置环境变量**：

    # 终端下输入
    sudo gedit /etc/profile

    # 打开文本编辑器后，在最下面添加：
    export JAVA_HOME=/home/inks/inks/APP/jdk1.8.0_144
    export JRE_HOME=${JAVA_HOME}/jre
    export CLASSPATH=.:${JAVA_HOME}/lib:{JRE_HOME}/lib
    export PATH=$PATH:{JAVA_HOME}/bin:

    # 使环境变量生效
    source /etc/profile

    # 添加软链接：
    sudo update-alternatives --install /usr/bin/java java /home/inks/inks/APP/jdk1.8.0_144/bin/java 300
    sudo update-alternatives --install /usr/bin/javac javac /home/inks/inks/APP/jdk1.8.0_144/bin/javac 300
    sudo update-alternatives --install /usr/bin/jar jar /home/inks/inks/APP/jdk1.8.0_144/bin/jar 300

**附录**：

* source /etc/profile时终端提示：`no matches found: tty[1-6]`
  解决方案：注释profile中第35行`# tty | egrep -q tty[1-6] && export LC_ALL=C`
* 输入`java -version`出现：
  `Picked up_JAVA_OPTIONS:-Dawt.useSystemAAFontSettings=gasp`
  解决方案：`rm /etc/profile.d/java-awt-font-gasp.sh
* 多个JDK的切换

```base
# 切换xxx
sudo update-alternatives --config xxx
```

### 4.2 Anaconda3配置

Anaconda3对应Python3.6.1。官方下载：[Anaconda installer archive](https://repo.continuum.io/archive/)

    # 执行
    ./Anaconda3-5.0.0.1-Linux-x86_64.sh

* 附录：

```base
# 修改zsh终端的环境变量
gedit /home/yourname/.zshc

# 修改bash终端的环境变量
gedit /home/yourname/.bashrc
```

### 4.3 IntelliJ全家桶

 JB家的软件通过edu邮箱可以**免费申请全系列**的使用权
>Free individual licenses for students and faculty members
>Are you learning Java, PHP, Ruby, Python, JavaScript, Objective-C or .NET technologies?
>Or maybe you just plan to? Do it right from the start, with award-winning professional developer tools from JetBrains. And the best part: it’s free of charge.

学生申请：
[学生授权申请教程](https://sales.jetbrains.com/hc/zh-cn/articles/207154369-%E5%AD%A6%E7%94%9F%E6%8E%88%E6%9D%83%E7%94%B3%E8%AF%B7%E6%96%B9%E5%BC%8F)
[学生申请地址](https://www.jetbrains.com/student/)

**JRebel插件**：
只需在Facebook或Twitter分享使用情况即可免费使用。
申请地址:[MyJrebel](https://my.jrebel.com/)
> JRebel has changed the way developers code in Java. Never again will you need to redeploy your application to see the impact of code updates. Make changes and JRebel applies them instantly to your running application.
> myJRebel brings these capabilities to you for free for personal, non-commercial use only. All you need to do is allow sharing your usage statistics on a social network.
