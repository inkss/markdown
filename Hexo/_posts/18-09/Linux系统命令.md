---
title: Linux 系统命令
toc: false
date: 2018/09/10
tag:
  - Linux
categories: linux
abbrlink: e8c3347c
type: linux
description: Linux 系统命令。
---

OS： **Deepin 15.5**

Shell： **zsh 5.3.1**

---

1. 提权 `sudo -s`

   可以通过命令前添加 sudo 获得临时权限；也可以执行 sudo -s 后输入当前用户的密码切换到 root 用户下，通过 `exit` 命令退出 root 用户。

1. 添加用户 `useradd -m 用户名` 组名、家目录名同用户名。

1. 设置密码 `passwd 用户名` 管理员权限下执行该命令可强制修改密码，普通权限下仅能修改自身密码且需要验证原密码。**【新创建的用户无密码】**

1. 删除用户 `userdel -r 用户名` 删除用户名的同时删除其家目录。

1. 切换用户 `su 用户名` 可在用户名前加`-`回到切换后用户的家目录；Ubuntu 默认没有 root 账户，对管理员的权限大部分可以通过 sudo 命令调用，但是可以通过给 root 用户设置密码通过 su root 也能切过去。

1. 查看系统中的所有用户 `cat /etc/passwd`

1. 查看系统中的所有分组 `cat /etc/group` 也可以利用终端的自动补全功能输入`groupmod`后按下Tab列出所有组名。

   PS：**不在 adm 和 sudo 组中的用户，无法使用 sudo 命令**。

1. 添加组 `groupadd 组名`； 删除组 `groupdel 组名`

1. 查看用户所在组 `groups 用户名`；查看分组中的账户 `cat /etc/group | grep 组名` 本质上是穿给管道给 grep 进行搜索。

1. 修改用户默认组 `usermod -g 组名 用户名` ；给用户添加一个组 `usermod -a -G 组名 用户名`
