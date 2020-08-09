---
title: Linux 命令概述
toc: true
date: 2018/09/10 13:48
updated: 2020/04/16 16:03
tag:
  - Linux
categories: linux
abbrlink: e8e370d
type: linux
description: Linux 基础命令。
---

OS： **Deepin 15.5**

Shell： **zsh 5.3.1**

---

## 1.基础命令

### 1.1 `clear`：清屏

### 1.2 `pwd`： 显示路径

```shell
➜  Desktop pwd
/home/inkss/Desktop
```

### 1.3 `ls`：列出目录内容

|命令|含义|
|-|-|
|ls|列出当前路径的文件和目录|
|ls -a|列出当前路径的所有文件和目录|
|ls -l|以列表形式列出当前路径的文件和目录|
|ls -l -h|优化 -l 时的文件大小显示|
|ls D*|列出以 D 开头的文件和目录|

>ls命令后可跟正则表达式,同时选项也可以全部出现，即 `ls -alh` 的形式，作用为三者的集合；`ls -h` 命令中的h单独出现时是不起作用，需配合另外两个共同使用； Linux 中以 `.` 开头的文件/文件夹都是隐藏文件。

```shell
➜  Pictures ls
a.txt  Wallpapers

➜  Pictures ls -l
总用量 4
-rw-r--r-- 1 inkss inkss    0 1月  22 17:59 a.txt
drwxr-xr-x 2 inkss inkss 4096 12月 10 19:41 Wallpapers

➜  Pictures ls -lh
总用量 4.0K
-rw-r--r-- 1 inkss inkss    0 1月  22 17:59 a.txt
drwxr-xr-x 2 inkss inkss 4.0K 12月 10 19:41 Wallpapers

```

### 1.4 `cd`：跳转目录

|命令|含义|
|-|-|
|cd 目录|进入目录|
|cd 目录1/目录2|从当前目录起进入指定目录|
|cd /目录1/目录2|从根目录起进入指定目录|
|cd ..|跳转到当前路径的上一层|
|cd -|返回上一操作所处在的目录|
|cd ~|跳转到当前用户的家目录|

```shell
➜  Desktop cd ..
➜  ~ cd Documents
➜  Documents cd ../..
➜  /home cd inkss/Desktop
➜  Desktop cd -
/home
➜  /home
```

### 1.5 `touch`：创建文件

*（创建文件，已存在的文件不覆盖）*。

```shell
➜  Desktop touch a.txt
➜  Desktop ls -l
总用量 0
-rw-r--r-- 1 inkss inkss 0 1月  23 15:01 a.txt
```

### 1.6 `cat`：查看文件

`cat filename.txt` 查看文件的内容。

---

## 2.重定向 分屏 管道

### 2.1 `>`：重定向

修改内容输出方向（重新设定原显示方向）。

 `>` ： 文件不存在则创建，存在则覆盖。

  `>>` ：文件不存在则创建，存在则追加。

```shell
➜  ~ ls
Desktop  Documents  Downloads  Music  Pictures  Templates  Videos
➜  ~ ls > test.txt
➜  ~ cat test.txt
Desktop
Documents
Downloads
Music
Pictures
Templates
test.txt
Videos
```

### 2.2 `more`：分屏

查看内容时，当信息过长无法在一屏上显示时，会出现快速滚屏，此时可以使用 `more` 命令，每次只显示一页，通过空格键切换下一页，按下 q 键退出，按下 h 键获取帮助。

### 2.3 `|`：管道

一个命令的输出可以通过管道作为另外一个命令的输入。

管道的左端写入东西，右端取出东西，用以连接两条命令。

```shell
➜  Desktop cd ~
➜  ~ ls -alh | more
总用量 320K
drwxr-xr-x 40 inkss inkss 4.0K 1月  23 15:08 .
drwxr-xr-x  3 root  root  4.0K 12月  8 19:17 ..
-rw-------  1 inkss inkss  268 12月  8 19:55 .bash_history
-rw-r--r--  1 inkss inkss  220 12月  8 19:04 .bash_logout
-rw-r--r--  1 inkss inkss 3.7K 12月  8 19:04 .bashrc
drwxr-xr-x 15 inkss inkss 4.0K 1月  13 18:15 .cache
drwxr-xr-x  3 inkss inkss 4.0K 12月  8 19:04 .icons
drwxr-xr-x  3 inkss inkss 4.0K 12月  8 21:58 .java
drwxr-xr-x  3 inkss inkss 4.0K 12月 11 12:40 .kingsoft
--More--
```

### 2.4 利用`cat、>`合并文件

cat 的作用可以视为将文本内容打印到终端上，同时 cat 又可以显示（打印）多个文件到终端，配合重定向 > 输出到文件里，即完成了多个文件的合并操作。

```shell
➜  Desktop cat a1.txt
1111111111
➜  Desktop cat a2.txt
2222222222
➜  Desktop cat a1.txt a2.txt a3.txt
1111111111
2222222222
3333333333
➜  Desktop cat a*.txt > b.txt
➜  Desktop cat b.txt
1111111111
2222222222
3333333333
➜  Desktop ls -l
总用量 16
-rw-r--r-- 1 inkss inkss 11 1月  23 18:05 a1.txt
-rw-r--r-- 1 inkss inkss 11 1月  23 18:05 a2.txt
-rw-r--r-- 1 inkss inkss 11 1月  23 18:05 a3.txt
-rw-r--r-- 1 inkss inkss 33 1月  23 18:07 b.txt
➜  Desktop
```

## 3.查阅帮助文档

### 3.1 `命令 --help`

```shell
➜  Desktop grep --help | more
用法: grep [选项]... PATTERN [FILE]...
Search for PATTERN in each FILE.
Example: grep -i 'hello world' menu.h main.c

Pattern selection and interpretation:
  -E, --extended-regexp     PATTERN is an extended regular expression
  -F, --fixed-strings       PATTERN is a set of newline-separated strings
--More--
```

### 3.2 `man 命令`

```shell
➜  Desktop man grep
```

---

## 4.链接

|命令|含义|
|-|-|
|ln 源文件 链接文件|硬链接|
|ln -s 源文件 链接文件|软链接|

链接：软链接和硬链接：软链接与 Windows 上的快捷方式概念完全一致。而硬链接则很类似 C 语言中的指针，源文件与链接文件**共同指向同一份数据内容**，所以在删除源文件时只是**将源文件与数据内容的关联**删除，通过链接文件依然能访问数据内容。

```shell
┌ d:目录 -：文件 l：链接
│          ┌ 硬链接数
↓          ↓
drwxr-xr-x 2 inkss inkss 4096 1月 23 16:14 a
-rw-r--r-- 2 inkss inkss   25 1月 23 16:13 a_hard.txt
-rw-r--r-- 2 inkss inkss   25 1月 23 16:13 a.txt
lrwxrwxrwx 1 inkss inkss    5 1月 23 16:16 b_soft.txt -> b.txt
-rw-r--r-- 1 inkss inkss   11 1月 23 16:15 b.txt
 └───┬───┘   └────┬────┘
     │            └─ 用户 用户组
     └─ 权限
```

## 5.目录

### 5.1 `mkdir`：创建目录

|命令|含义|
|-|-|
|mkdir 目录名|在当前路径下创建目录|
|mkdir -p 目录1/目录2|递归的创建目录|

### 5.2 tree：树形显示当前目录结构

```shell
➜  / cd ~/Desktop
➜  Desktop ls
a  a.txt
➜  Desktop mkdir python_haha
➜  Desktop ls -l
总用量 8
-rw-r--r-- 1 inkss inkss    0 1月  23 15:04 a
-rw-r--r-- 1 inkss inkss   17 1月  23 15:03 a.txt
drwxr-xr-x 2 inkss inkss 4096 1月  23 15:16 python_haha
➜  Desktop mkdir -p A/B
➜  Desktop cd A/B
➜  B pwd
/home/inkss/Desktop/A/B
➜  B cd ~/Desktop
➜  Desktop tree
.
├── a
├── A
│   └── B
├── a.txt
└── python_haha

3 directories, 2 files
```

> Deepin 默认没有安装 tree，如需使用请先安装：`sudo apt install tree`

---

## 6.文件操作

### 6.1 `cp`：复制

用法：cp [选项]... [-T] 源文件 目标文件

|选项|含义|
|-|-|
|-a|保留文件属性（递归的复制）|
|-f|目录文件已经存在而不提示|
|-i|交互式复制，在覆盖文件前需要用户确认|
|-r|如果源文件是目录，则递归的复制该目录下的所有子目录和文件|
|-v|显示复制进度|

>f 用以强制复制，a 用以保留诸如链接之类的属性，r 用来复制目录。
>
>`cp a b`: 将 a 文件夹整体复制到 b 文件夹里；`cp a/* b`：将 a 文件夹里的内容复制到 b 文件夹里.

```shell
➜  Desktop ls -l
总用量 0
-rw-r--r-- 2 inkss inkss 0 1月  23 19:20 a_hard.txt
lrwxrwxrwx 1 inkss inkss 5 1月  23 19:43 a_soft.txt -> a.txt
-rw-r--r-- 2 inkss inkss 0 1月  23 19:20 a.txt
➜  Desktop cp a_soft.txt b1.txt
➜  Desktop cp a_soft.txt -a b2.txt
➜  Desktop ls -l
总用量 0
-rw-r--r-- 2 inkss inkss 0 1月  23 19:20 a_hard.txt
lrwxrwxrwx 1 inkss inkss 5 1月  23 19:43 a_soft.txt -> a.txt
-rw-r--r-- 2 inkss inkss 0 1月  23 19:20 a.txt
-rw-r--r-- 1 inkss inkss 0 1月  23 19:45 b1.txt
lrwxrwxrwx 1 inkss inkss 5 1月  23 19:43 b2.txt -> a.txt
➜  Desktop
```

### 6.2 `mv`：移动

用法：mv [选项]... [-T] 源文件 目标文件

|选项|含义|
|-|-|
|-f|强制移动，不会用任何提示|
|-i|交互式操作，覆盖前会询问|
|-v|显示移动进度|

```shell
➜  Desktop ls
a.txt
➜  Desktop mv a.txt a.ttt
➜  Desktop ls
a.ttt
➜  Desktop
```

### 6.3 删除

#### 6.3.1 `rmdir`：删除目录

```shell
➜  Desktop rmdir python_haha
➜  Desktop ls
a  A  a.txt
➜  Desktop rmdir A
rmdir: 删除 'A' 失败: 目录非空
➜  Desktop
```

#### 6.3.2 `rm`：删除文件/文件夹

|命令|含义|
|-|-|
|rm 文件名|删除文件|
|rm -r 目录|删除目录（递归）|
|rm -i 文件|交互式删除（删除前需确认）|
|rm -f 文件|强制删除|

> rm 后跟文件名支持正则表达式。

```shell
➜  Desktop tree
.
└── a
    └── b
        ├── bbb.ccc
        └── c
            └── d

4 directories, 1 file
➜  Desktop rm -rf a/*
zsh: sure you want to delete the only file in /home/inkss/Desktop/a [yn]? y
➜  Desktop
➜  Desktop tree
.
└── a

1 directory, 0 files
```

```shell
➜  Desktop tree
.
└── a
    ├── aaa
    │   └── a.txt
    └── b.txt

2 directories, 2 files
➜  Desktop rm -r a/aaa
➜  Desktop tree
.
└── a
    └── b.txt

1 directory, 1 file
```

### 6.4 `grep`：查找

|命令|含义|
|-|-|
|grep -v '搜索内容串' 文件名|不匹配文本的所有行（求反）|
|grep -n '搜索内容串' 文件名|显示匹配行及其行号|
|grep -i '搜索内容串' 文件名|忽略大小写|

```shell
➜  Desktop grep -n 'printf' test.c
4:  printf("Hello,Nice to meet you!\n");
5:  printf("Bye!\n");
➜  Desktop grep -nv 'printf' test.c
1:#include<stdio.h>
2:int main(void)
3:{
6:  return 0;
7:}
➜  Desktop
```

> 允许对文本进行模式查找，如果找到匹配模式，则打印包含模式的所有行。同时，grep 也支持正则表达式。

### 6.5 `find`：搜索

|命令|含义|
|-|-|
|find ./ -name test.txt|查找当前目录下所有名为 test.txt 的文件|
|find ./ -name '*.sh'|查找当前目录下所有后缀为 .sh 的文件|
|find /tmp -size 2M|查找 /tmp 目录下等于 2M 的文件|
|find /tmp -size +2M|查找 /tmp 目录下大于 2M 的文件|
|find /tmp -size -2M|查找 /tmp 目录下小于 2M 的文件|
|find /tmp -size +4k -size -5M|查找 /tmp 目录下大于 4K、小于 5M 的文件|
|find ./ -perm 0777|查找当前目录下权限为 777 的文件或目录|

```shell
➜  Desktop tree
.
├── a
│   └── a.txt
└── a.txt

1 directory, 2 files
➜  Desktop find ./ -name a.txt
./a/a.txt
./a.txt
➜  Desktop
```

```shell
➜  share pwd
/usr/share
➜  share find ./ -size +50M -size -60M
./tesseract-ocr/tessdata/chi_tra.traineddata
➜  share find ./ -size +50M
./code/code
./tesseract-ocr/tessdata/chi_tra.traineddata
./qt4/doc/qch/qt.qch
➜  share
```

---

## 7.压缩、解压缩

### 7.1 `tar`：打包、解包文件

用法：tar [选项]... [FILE]...

|选项|含义|
|-|-|
|-c|创建档案文件|
|-x|解开档案文件|
|-v|显示归档解档进度|
|-t|列出档案中包含的文件|
|-f|指定档案文件名称（**必须位于选项中最后位置**）|

```shell
➜  Desktop cd ../Pictures
➜  Pictures ls
Wallpapers
➜  Pictures tar -cvf test.tar ./Wallpapers/*
./Wallpapers/1.png
./Wallpapers/2.jpg
./Wallpapers/3.jpg
./Wallpapers/4.jpg
➜  Pictures ls
test.tar  Wallpapers
➜  Pictures
```

### 7.2 `gzip`：压缩、解压缩

tar 仅仅是打包了文件，没有压缩，所用通过 gzip 生成 xxxx.tar.gz 的压缩文件.

用法：gzip [选项]... [FILE]...（不跟选项情况下为压缩）

|选项|含义|
|-|-|
|-d|解压|
|-r|压缩|

```shell
➜  Desktop ls -lh
总用量 3.7M
-rw-r--r-- 1 inkss inkss 3.7M 1月  23 21:46 test.tar.gz
➜  Desktop gzip -d test.tar.gz
➜  Desktop ls -lh
总用量 3.8M
-rw-r--r-- 1 inkss inkss 3.8M 1月  23 21:46 test.tar
➜  Desktop
```

截止到目前，是先通过 tar 打包，再通过 gzip 压缩；

解压则是先通过 gzip 解压，再通过 tar 解包。

|||
|-|-|
|tar -cvf xxx.tar * 打包|gzip xxx.tar 压缩|
|gzip -d xxx.tar.gz 解压|tar -xvf xxx.tar 解包|

### 7.3 解压与压缩

#### 7.3.1 tar

* 一步到位的压缩命令：`tar -zcvf xxx.tar.gz *`

* 一步到位的解压命令：`tar -zxvf xxx.tar.gz`

#### 7.3.2 bzip2算法

* `tar -jcvf xxx.tar.bz2 *`：压缩

* `tar -jxvf xxx.tar.bz2`：解压

```shell
➜  Desktop tar -jcvf Wallpapers.tar.bz2 Wallpapers
Wallpapers/
Wallpapers/1.jpg
Wallpapers/2.png
Wallpapers/3.jpg
Wallpapers/4.jpg
➜  Desktop tar -zcvf wallpapers.tar.gz Wallpapers
Wallpapers/
Wallpapers/1.jpg
Wallpapers/2.png
Wallpapers/3.jpg
Wallpapers/4.jpg
➜  Desktop ls -l
总用量 7456
drwxr-xr-x 2 inkss inkss    4096 1月  23 21:59 Wallpapers
-rw-r--r-- 1 inkss inkss 3784740 1月  23 22:13 Wallpapers.tar.bz2
-rw-r--r-- 1 inkss inkss 3839708 1月  23 22:13 wallpapers.tar.gz
➜  Desktop
```

#### 7.3.3 指定解压路径

`tar -zxvf xxx.tar.gz -C ./xxx/xxx`：指定解压路径

#### 7.3.4 `zip、upzip`：压缩、解压缩

|命令|含义|
|-|-|
|zip 目标文件 源文件|压缩文件|
|unzip 目录 压缩文件|解压文件|

---

## 8.系统相关命令

### 8.1 `cal`：在终端下查看日历

### 8.2 `date`：显示或设置时间

### 8.3 `ps`：查看进程信息

|||
|-|-|
|ps -aux|详细显示终端上所有进程|

> 可以通过该命令获取进程 `PID` 号

### 8.4 `top`：动态显示进程信息

|按键|含义|
|-|-|
|M|按照内存使用量进行排序显示|
|P|按照 CPU 使用率进行排序显示|
|Q|退出程序|
|H|获取帮助|

### 8.5 `kill`：杀死进程

|命令|含义|
|-|-|
|kill pid|根据进程 PID 值杀死进程|
|kill -9 pid|强制结束|

### 8.6 `reboot、shutdown`：关机重启

|命令|含义|
|-|-|
|reboot|重启操作系统|
|shutdown -r now|重启操作系统，跟别的用户予以提示|
|shutdown -h now|立刻关机|
|shutdown -h +10|10min 后关机|
|shutdown -h 22：20|22：20 时关机|

### 8.7 `df`：查看磁盘所用空间

### 8.8 `du`：查看当前目录占用磁盘大小

### 8.9 `ifconfig`：查看网卡信息

### 8.10 `ping`：测试主机连通性
