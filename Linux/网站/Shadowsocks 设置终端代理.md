# Shadowsocks 设置终端代理

今天在使用 NVM 安装 Node.js 时，在终端下到克隆仓库这一步时体验简直崩溃，几乎没有网速。所以就迫切的需要 Shadowsocks 也能够代理终端下的命令。Shadowsoks 相关配置参阅这里： [Shadowsocks](https://github.com/inkss/markdown/blob/master/Linux/Ubuntu/Ubuntu-18.04-%E5%AE%89%E8%A3%85%E8%AE%B0%E5%BD%95.md#32-shadowsocks)

## 一、Git 的代理

如果不需要终端下全局代理，仅仅只是代理 Git 相关的命令，则有一个简单的方案：

```sh
git config --global http.proxy 'socks5://127.0.0.1:1080' 
git config --global https.proxy 'socks5://127.0.0.1:1080'
```

## 二、终端下代理

虽说对 Git 设置了代理可以解决克隆仓库无进度的问题，但是一些命令（安装 Node.js ）依旧没网速。

所以还是需要对终端进行代理，首先需要修改终端的环境变量，修改文件并追加以下内容：

```sh

```

* 如果你的 Shell 还是默认的  Bash ，那么修改需要修改的文件位于：`~/.bashrc`
* 而如果 Shell 已经换成了 ZSH ，此时需要修改的文件就位于：`~/.zshrc`

然后接下来只需要使其生效：

```sh
sourch ~/.bashrc # 或者 .zshrc
```

关闭这个终端，重新打开一个新的终端进行测试：

```sh
curl ip.cn # 观察输出内容
```

------

参考资料：

* [用 shadowsocks 加速 git clone](https://blog.fazero.me/2015/07/11/%E7%94%A8shadowsocks%E5%8A%A0%E9%80%9Fgit-clone/)
* [Linux bash终端设置代理（proxy）访问](https://aiezu.com/article/linux_bash_set_proxy.html)