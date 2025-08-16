---
title: Docker 的简要使用
toc: true
tag:
  - Docker
  - 容器部署
  - 常用命令
categories: 文档
description: 本文介绍 Docker 的简要使用方法，包括常用命令（镜像与容器的查看、下载、删除、启动等操作）、容器部署的相关参数及具体示例（如部署 portainer、code-service），还附带 oh-my-zsh 和 Node 的安装命令。
date: '2021-01-29 16:00'
updated: '2021-02-01 12:00'
abbrlink: e9bd7c2d
icons: [fab fa-docker blue]
headimg: ../../img/article/21-01@Docker的简要使用/main.png
hideTitle: false
---

<p class="p center logo large"><em>Docker 的简要使用 {% emoji jr-chunjie %}</em></p>
<br>
{% p center, 2021 年，新年的第一篇文章~ %}

## 一、常用命令

{% codeblock lang:shell 将当前用户加入docker组 line_number:false  %}
sudo gpasswd -a ${USER} docker
{% endcodeblock %}

{% codeblock lang:shell 重新启动docker服务 line_number:false  %}
sudo systemctl restart docker
{% endcodeblock %}

{% codeblock lang:shell 常用命令 line_number:false  %}
docker images                      #查看所有本地主机上的镜像
docker pull mysql                  #下载镜像
docker rmi -f ID/name              #删除镜像

docker ps 命令
      -a                           #列出当前正在运行的容器 + 带出历史运行中的容器
      -n=?                         #显示最近创建的容器
      -q                           #只显示容器的编号

docker rm id                       #删除指定的容器，不能删除在运行的容器，如果要强制删除 rm -f
docker rm -f $(docker ps -aq)      #删除所有的容器
docker ps -a -q|xargs docker rm    #删除所有的容器

docker start 容器id                #启动容器
docker restart 容器id              #重启容器
docker stop 容器id                 #停止当前正在运行的容器
docker kill 容器id                 #停止当前容器
{% endcodeblock %}

## 二、部署容器

{% codeblock lang:shell 相关命令 line_number:false  %}
--name xxx                    #为容器设置一个别名
--restart=always              #总是重启，主机重启后启动容器
-u "$(id -u):$(id -g)"        #绑定权限，也可单独指定 如 -u root
-p 8910:8080                  #绑定端口，前者为主机，后者为容器
-v "/home/coder:/home/coder"  #绑定目录，前者为主机，后者为容器
-e PASSWORD='******'          #环境变量，根据实际情况而定
{% endcodeblock %}

**部署 portaine**

{% codeblock lang:shell 部署 portaine line_number:false  %}
sudo docker run -d -p 8915:9000 --restart=always -v /var/run/docker.sock:/var/run/docker.sock --name=portainer  portainer/portainer-ce
{% endcodeblock %}

**部署 code-service**

{% codeblock lang:shell 部署 code-service line_number:false  %}
sudo docker run -d --name code-server --restart=always -u "$(id -u):$(id -g)" -p 8910:8080 -p 5500:5500 -v "/home/coder/coder-service:/home/coder/project" -v "/home/coder/coder-config:/home/coder/.config" -e PASSWORD='******' codercom/code-server:latest
{% endcodeblock %}

## 三、一些命令

{% codeblock lang:shell 安装 oh-my-zsh line_number:false  %}
sh -c "$(wget -O- https://cdn.jsdelivr.net/gh/robbyrussell/oh-my-zsh/tools/install.sh)"
chsh -s /usr/bin/zsh
{% endcodeblock %}

{% codeblock lang:shell 安装 Node line_number:false  %}
#安装 NVM
wget -qO- https://cdn.jsdelivr.net/gh/creationix/nvm/install.sh | sh

#刷新环境
osurce ~/.zshrc

#安装 Node 仅linux下可用
nvm install --lts
{% endcodeblock %}
