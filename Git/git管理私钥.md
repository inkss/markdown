# 管理ssr私钥

## 一、添加私钥

1.Linux平台：打开终端到id_rsa文件所在目录

2.Windows平台：id_rsa文件所在目录右键打开Git Bash Here

```shell
# 终端执行
ssh-add id_rsa

# 如果提示：Could not open a connection to your authentication agent
# 执行下列语句
ssh-agent bash
```

## 二、新建`config`文件

文件内容如下：

需要注意的是：`IdentityFile`指向id_rsa文件,不一定非要放在.ssh目录

```shell
# github
Host github.com
HostName github.com
PreferredAuthentications publickey
IdentityFile /home/yourname/Documents/ssh/id_rsa
User yourname
```

复制该文件到ssh目录：

1.Linux平台：`/home/yourname/.ssh/`

2.Windows平台：`/c/Users/yourname/.ssh/`

## 三、测试链接

```shell
ssh -T git@github.com

# 设置name与email
git config --global user.name "name"
git config --global user.email "email"
```
