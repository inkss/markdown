# Ubuntu主题篇 - Arc Theme

>Arc is a flat theme with transparent elements for GTK 3, GTK 2 and GNOME Shell which supports GTK 3 and GTK 2 based desktop environments like GNOME, Unity, Budgie, Pantheon, Xfce, MATE, etc.

## 1. 安装步骤

1.利用git克隆到本地

    git clone https://github.com/horst3180/arc-theme --depth 1 && cd arc-

2.构建并安装主题

    ./autogen.sh --prefix=/usr
    sudo make install

Other options to pass to autogen.sh are

    --disable-transparency     disable transparency in the GTK3 theme
    --disable-light            disable Arc Light support
    --disable-darker           disable Arc Darker support
    --disable-dark             disable Arc Dark support
    --disable-cinnamon         disable Cinnamon support
    --disable-gnome-shell      disable GNOME Shell support
    --disable-gtk2             disable GTK2 support
    --disable-gtk3             disable GTK3 support
    --disable-metacity         disable Metacity support
    --disable-unity            disable Unity support
    --disable-xfwm             disable XFWM support

    --with-gnome=<version>     build the theme for a specific GNOME version (3.14, 3.16, 3.18, 3.20, 3.22)
                               Note 1: Normally the correct version is detected automatically and this
                               option should not be needed.
                               Note 2: For GNOME 3.24, use --with-gnome-version=3.22
                               (this works for now, the build system will be improved in the future)

安装完成以后就可以通过`unity-tweak-tool`选择主题了。

---

## 2. 卸载步骤

终端执行：

    sudo make uninstall

或者把git克隆到本地的库直接删掉即可。

    sudo rm -rf /usr/share/themes/{Arc,Arc-Darker,Arc-Dark}

---

## 3.桌面预览

![A full screenshot of the Arc theme](http://upload-images.jianshu.io/upload_images/6490512-99e8c64a70edcad2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
<sub>Screenshot Details: Icons: [Arc](https://github.com/horst3180/arc-icon-theme) | Launcher Icons based on [White Pixel Icons](http://darkdawg.deviantart.com/art/White-Pixel-Icons-252310560) | [Wallpaper](https://pixabay.com/photo-869593/) | Font: Futura Bk bt</sub>

[obs-repo]: http://software.opensuse.org/download.html?project=home%3AHorst3180&package=arc-theme
[sk-overlay]: https://c.darenet.org/scriptkitties/overlay

---

### 1.Arc-Flatabulous

![](http://upload-images.jianshu.io/upload_images/6490512-01571eefbe35cfb2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 2.Arc-Flatabulous-Darker

![](http://upload-images.jianshu.io/upload_images/6490512-31d8ccca74bbabe2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

## 3.Arc-Flatabulous-Dark

![](http://upload-images.jianshu.io/upload_images/6490512-ca15f2482ec2c669.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

---

Github地址：[andreisergiu98/arc-flatabulous-theme](
https://github.com/andreisergiu98/arc-flatabulous-theme )