---
title: Linux Shell 设置 Proxy
toc: true
indent: true
comments: false
date: 2018/09/10 21:26
updated: 2020/08/09 11:12
tag:
  - Shell
  - Proxy
categories: 教程
abbrlink: f44c3b52
description: Linux Shell 设置 Proxy。
background: ../../img/bkg/wallhaven-rdyyr1.png
references:
  - title: 终端加速 git clone
    url: 'https://blog.fazero.me/2015/07/11/%E7%94%A8shadowsocks%E5%8A%A0%E9%80%9Fgit-clone/'
  - title: Linux bash终端设置代理（proxy）访问
    url: 'https://aiezu.com/article/linux_bash_set_proxy.html'
icons:
  - fab fa-ubuntu red
---

## 一、Git Proxy

如果不需要终端下全局代理，仅仅只是代理 Git 相关的命令，则有一个简单的方案：

```sh
git config --global http.proxy 'socks5://127.0.0.1:1080'
git config --global https.proxy 'socks5://127.0.0.1:1080'
```

## 二、Terminal Proxy

需要修改终端（Shell）的环境变量，修改（相应的）文件并追加以下内容：

```sh
export http_proxy=socks5://127.0.0.1:1080
export https_proxy=socks5://127.0.0.1:1080
# 注意：wget 不支持的协议类型 “socks5”
# bashrc or zshrc
```

然后接下来只需要使其生效即可：

```sh
source ~/.bashrc # 或者 .zshrc
```

可以使用 curl 命令进行测试：`curl ip.cn`

## 三、Other

```sh
git config --global user.name "xxxx"
git config --global user.email "xxxx@xxx.com"
ssh-keygen -t rsa -C "xxxx@xxx.com"
ssh git@github.com
```
