# 命令行代码篇

1.neofetch 一款可以在 shell 图形显示系统信息的软件

    sudo add-apt-repository ppa:dawidd0811/neofetch
    sudo apt-get update
    sudo apt-get install neofetch

---

2.shutter 截图软件

    sudo apt-get install shutter

---

3.unity-tweak-tool 桌面主题管理器

    sudo apt-get install unity-tweak-tool

---

4.修改 zsh 终端的环境变量

    gedit /home/yourname/.zshc

---

5.修改 bash 终端的环境变量

    gedit /home/youname/.bashrc

---

6.Mysql 系列软件安装

    sudo apt-get install mysql-server
    sudo apt-get install mysql-client
    sudo apt-get install mysql-workbench

---

7.安装一个挺好看的图标 Papirus [我喜欢这个]

    sudo add-apt-repository ppa:papirus/papirus
    sudo apt-get update
    sudo apt-get install papirus-icons-theme

---

8.安装一套主题 Numix

    sudo add-apt-repository ppa:numix/ppa
    sudo apt-get update
    sudo apt-get install numix-gtk-theme numix-icon-theme-circle

---

9.安装一个底部 Dock 软件 Docky

    sudo add-apt-repository ppa:ricotz/docky
    sudo apt-get update
    sudo apt-get install docky

---

10.安装谷歌浏览器 Chrome

    sudo wget http://www.linuxidc.com/files/repo/google-chrome.list -P /etc/apt/sources.list.d/
    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | sudo apt-key add -
    sudo apt-get update
    sudo apt-get install google-chrome-stable

---

11.打开一个 root 权限的文件管理器

    sudo nautilus

---

12.双系统的孩子们时间差八小时的解决代码

    timedatectl set-local-rtc 1

---

13.安装代理软件 shadowsocks-qt5

    sudo add-apt-repository ppa:hzwhuang/ss-qt5
    sudo apt-get update
    sudo apt-get install shadowsocks-qt5

---

14.查看所有 shell 以及怎么切换默认 shell

    cat /etc/shells
    chsh -s /xxx/xxxx

---

15.安装 oh-my-zsh

    sudo apt-get install git
    sudo apt-get install zsh
    wget https://github.com/robbyrussell/oh-my-zsh/raw/master/tools/install.sh -O - | sh
    chsh -s /usr/bin/zsh

---

16.安装 flatabulous 主题

    sudo add-apt-repository ppa:noobslab/themes
    sudo apt-get update
    sudo apt-get install flatabulous-theme

---

17.关闭 Ubuntu 访客模式

    cd /usr/share/lightdm/lightdm.conf.d/
    sudo gedit 50-no-guest.conf

    # 在文本中输入：
    [SeatDefaults]
    greeter-session=unity-greeter
    allow-guest=false

---

18.安装 vim 并配置 vim

    sudo apt-get install vim

    #配置 vim
    sudo gedit ~/.vimrc

    #输入以下内容
    set mouse=a "鼠标可任意移动
    set number "显示行号.
    set ruler "显示当前光标的行列信息
    syntax on "语法高亮显示.(这个肯定是要的.)
    set showcmd "在状态栏显示正在输入的命令
    set history=50 "设置命令历史记录为50条.
    set hls "寻找匹配是高亮度显示的
    set lbr "不在单词中间断行。
    set fo+=mB "打开断行模块对亚洲语言支持
    set tabstop=4 "设置tab键为4个空格.
    set shiftwidth=4 "设置当行之间交错时使用4个空格
    set autoindent "使用自动对起，也就是把当前行的对起格式应用到下一行.
    set smartindent "依据上面的对起格式，智能的选择对起方式，对于类似C语言编.
    set showmatch "设置匹配模式，显示括号配对情况。
    set cin " 打开 C/C++ 风格的自动缩进。
    set cino=:4g4t4(sus "设定 C/C++ 风格自动缩进的选项
    set incsearch  "搜索时在未完全输入完毕要检索的文本时就开始检索。
    filetype on "检测文件的类型
    filetype plugin indent on "开启了Vim的三种智能:自动识别文件类型

---

19.强制清空回收站

    sudo rm -rf $HOME/.local/share/Trash/files/*

---

20.安装一个显示 CPU 内存的软件

    sudo add-apt-repository ppa:fossfreedom/indicator-sysmonitor
    sudo apt-get update
    派生到我的代码片

    sudo apt-get install indicator-sysmonitor

---

21.安装一个显示网速的软件

    sudo add-apt-repository ppa:nilarimogard/webupd8
    sudo apt-get update
    sudo apt-get install indicator-netspeed

---

22.修复 vmware 虚拟机中 Windows 没有显卡驱动支持

    #在 vmx 文件中末端添加
    svga.guestBackedPrimaryAware = "TRUE"

---

23.安装 sublime

    sudo add-apt-repository ppa:webupd8team/sublime-text-3
    sudo apt-get update
    sudo apt-get install sublime-text-installer

    # 安装 Package Control
    # https://packagecontrol.io/installation#st3
    # 获取安装代码 ↑↑↑

    import urllib.request,os,hashlib; h = '6f4c264a24d933ce70df5dedcf1dcaee' + 'ebe013ee18cced0ef93d5f746d80ef60'; pf = 'Package Control.sublime-package'; ipp = sublime.installed_packages_path(); urllib.request.install_opener( urllib.request.build_opener( urllib.request.ProxyHandler()) ); by = urllib.request.urlopen( 'http://packagecontrol.io/' + pf.replace(' ', '%20')).read(); dh = hashlib.sha256(by).hexdigest(); print('Error validating download (got %s instead of %s), please try manual install' % (dh, h)) if dh != h else open(os.path.join( ipp, pf), 'wb' ).write(by)

    激活 sublime
    Michael Barnes
    Single User License
    EA7E-821385
    8A353C41 872A0D5C DF9B2950 AFF6F667
    C458EA6D 8EA3C286 98D1D650 131A97AB
    AA919AEC EF20E143 B361B1E7 4C8B7F04
    B085E65E 2F5F5360 8489D422 FB8FC1AA
    93F6323C FD7F7544 3F39C318 D95E6480
    FCCC7561 8A4A1741 68FA4223 ADCEDE07
    200C25BE DBBC4855 C4CFB774 C5EC138C
    0FEC1CEF D9DCECEC D3A5DAD1 01316C36

---

24.安装一个截取 GIF 图案的软件

    sudo add-apt-repository ppa:peek-developers/stable
    sudo apt update
    sudo apt install peek

---
