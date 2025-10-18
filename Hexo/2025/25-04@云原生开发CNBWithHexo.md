---
title: 云原生开发  With Hexo
toc: true
indent: true
tag:
  - Hexo
  - CNB
  - 腾讯云
categories: 网络技术
description: >-
  本文介绍将 Hexo 博客与腾讯云原生构建（CNB）结合的云原生开发方式，包括 CNB 的免费额度信息，讲解将博客源码迁移至 CNB、配置云原生构建（实现部署到腾讯云对象存储及同步至 GitHub）和自定义云原生开发环境（基于 code-server）的过程，附相关配置示例。
date: '2025-04-20 15:20'
updated: '2025-04-20 15:20'
headimg: https://cdn.jsdelivr.net/gh/inkss/inkss-cdn@main/img/article/25-04@云原生开发CNBWithHexo/Hexo博客封面.png
abbrlink: 95d58968
---

云开发 + 云构建能激起怎样的火花，快来尝试下腾讯云原生构建项目 [CNB - Cloud Native Build](https://cnb.cool/)。

<!-- more -->

瓦解推荐，腾讯云新产品，目前处于公测状态，且免费额度很大：

| 计费项     | 免费额度     | 公测限免额度  | 超额计费标准  | 使用场景                   |
| :--------- | :----------- | :------------ | :------------ | -------------------------- |
| 仓库存储   | 100 GiB      | 100 GiB       | 1 元/GiB/月   | Git 对象                   |
| 对象存储   | 100 GiB      | 100 GiB       | 1 元/GiB/月   | 制品、LFS 对象、图片及附件 |
| 云原生构建 | 160 核时/月  | 1600 核时/月  | 0.125 元/核时 | 云原生构建                 |
| 云原生开发 | 1600 核时/月 | 16000 核时/月 | 0.125 元/核时 | 云原生开发                 |

所能申请的机器最大支持到64核128G内存，这下不白嫖说不过去了。

## 一、背景

本博客的源码托管于 GitHub，并通过 GitHub Actions 进行分发，分别推送至境内对象存储和境外 GitHub Pages。由于境内内容审查的限制，两者的推送内容并不完全一致。因此，在本次迁移过程中，只需将面向对象存储的部署流程转移至 CNB 平台，同时保留境外推送流程。

CNB 平台相当于有三部分组成：存储源码（和制品）的仓库、类似 Github Action 的云原生构建和类似 Github CodeSpace 的云原生开发。原则上只是推送代码执行自动化前两者就能满足，不过考虑到我们是以白嫖为目的，云开发（基于 code-server）必须尝试，何况实际使用时发现内置了腾讯云代码助手 CodeBuddy，无需登录开箱即用且性能强大。

## 二、CNB

<p>{% emoji huaixiao %} {% wavy 限于篇幅，CNB 的注册、访问令牌等使用流程略去... %}</p>

### 添加远端

新建一个私有仓库用于存储博客源码，根据提示我们采用 *方式2：分支迁移* 完成迁移仓库：

```sh 添加远端 cnb 并将 main 分支推送到远端
git remote add cnb https://cnb.cool/user/blog.git
git push -u cnb HEAD:main
```

当前我们的源码仓库已配置了两个远端，但由于 Git 在执行`git push`时默认仅推送到`HEAD`绑定的远端，因此如果 CNB 被设置为默认远端，代码管理工具的默认推送操作将不会同步推送到 GitHub 仓库。

当然，我们可以通过手动执行两次推送来完成同步，如下所示：

```sh 手动推送
git push cnb main
git push origin main
```

然而，这种方式不够优雅，既然我们已经采用自动化流程，完全可以将这一推送逻辑纳入云原生构建流程，实现 **本地 -> CNB -> GitHub** 的自动化部署。

### 云原生构建

#### 部署博客

本博的境内部署对象是腾讯云对象存储，采用的是自己写的轮子：[inkss/hexo-deployer-tencent: 一个用于将文件上传到腾讯COS和刷新CDN的Hexo部署器插件。](https://github.com/inkss/hexo-deployer-tencent)

所以照葫芦画瓢，把原 Github Action 流程[^原始流程]改写为 CNB 所支持的语法。

```yaml 原 Github Action 代码（部分）
- name: '配置密文填充'
  uses: microsoft/variable-substitution@v1
  with:
    files: '_tencent.yml'
  env:
    deploy.secret_id: ${{secrets.TENCENT_ID}}
    deploy.secret_key: ${{secrets.TENCENT_KEY}}
- name: '部署网站: Tencent COS'
  timeout-minutes: 15
  continue-on-error: true
  run: |
    npm run argt
    npm run dd
```

[^原始流程]: 关于`npm run argt`和`npm run dd`，存在如下定义：
    ```json package.json
    {
      "scripts": {
        "argt": "hexo clean && hexo generate --config _tencent.yml",
        "dd": "hexo deploy --config _tencent.yml",
      }
    }
    ```
    你可简单理解为这就是`hexo clean && hexo deploy`。

CNB 没有 secrets 这一概念，但实际上反而更灵活。我们先另建一个 env 仓库，类型为密钥仓库，该仓库用来统一存储密钥，并写入如下内容：

```yaml https://cnb.cool/user/env/-/blob/main/tencent-csc.yml
TENCENT_SECRET_ID: XXXXXXXXXXXXXXXXXXXXXX
TENCENT_SECRET_KEY: XXXXXXXXXXXXXXXXXXXXXX
```

最后在博客源码仓库根目录下创建 CNB 的配置文件`.cnb.yml`：

```yaml
# 定义云构建
main:
  push:
    - name: 部署博客
      docker:
        image: node:22
        volumes:
          - /root/.npm
          - ./node_modules
      imports: 
        - https://cnb.cool/user/env/-/blob/main/tencent-csc.yml
      stages:
        - name: 部署网站
          script: |
            sed -i "s/secret_id:.*/secret_id: '$TENCENT_SECRET_ID'/" _tencent.yml
            sed -i "s/secret_key:.*/secret_key: '$TENCENT_SECRET_KEY'/" _tencent.yml
            npm install
            npm run argt
            npm run dd
```

#### 同步仓库

我们需要在云构建中同步完成对 Github 仓库的推送，为了简化流程，我们采用 TOKEN 完成身份校验。首先自行在 Github 设置中创建一个 Personal Access Tokens，同样的将其存放到 CNB 密钥仓库中：

```yaml https://cnb.cool/user/env/-/blob/main/github-cnb-push.yml
GITHUB_TOKEN: XXXXXXXXXXXXXXXXXXXXXX
```

我们采用并行流程在`.cnb.yml`做如下修改：

```yaml
# 定义云构建
main:
  push:
    - name: 同步仓库
      stages:
        - name: 同步 Github 仓库
          imports: 
            - https://cnb.cool/user/env/-/blob/main/github-cnb-push.yml
          script: |
            git remote add github https://x-access-token:$GITHUB_TOKEN@github.com/user/repo
            git push github main
    - name: 部署博客
      # 同上，省略...
```

![云原生构建](https://cdn.jsdelivr.net/gh/inkss/inkss-cdn@main/img/article/25-04@云原生开发CNBWithHexo/25-04-20_153103.png)

### 云原生开发

其实只需点击仓库右侧的**云原生开发**按钮，即可轻松启动 WebIDE。然而，根据官方文档的说明，我们还可以利用**自定义镜像**来构建一个更加符合自身需求的开发环境，从而实现更灵活、高效的开发体验。

在博客仓库下创建`.ide/Dockerfile`和`.ide/setting.json`文件，分别写入如下内容：

```dockerfile .ide/Dockerfile
# 使用更轻量的基础镜像
FROM node:20-slim

# 设置环境变量
ENV LANG=C.UTF-8 \
    LANGUAGE=C.UTF-8 \
    NPM_CONFIG_REGISTRY=https://registry.npmmirror.com

# 安装系统依赖、工具和字体
RUN apt-get update && \
    apt-get install -y git curl unzip fontconfig vim zsh && \
    npm install -g npm-check-updates && \
    curl -fsSL -o /tmp/JetBrainsMono.zip https://github.com/JetBrains/JetBrainsMono/releases/download/v2.304/JetBrainsMono-2.304.zip && \
    unzip /tmp/JetBrainsMono.zip -d /tmp/JetBrainsMono && \
    mkdir -p /usr/share/fonts/truetype/jetbrainsmono && \
    cp /tmp/JetBrainsMono/fonts/ttf/*.ttf /usr/share/fonts/truetype/jetbrainsmono/ && \
    curl -fsSL -o /tmp/install.sh https://install.ohmyz.sh/ && \
    sh /tmp/install.sh && \
    usermod -s /usr/bin/zsh root && \
    fc-cache -f && \
    rm -rf /tmp/JetBrainsMono /tmp/JetBrainsMono.zip /tmp/install.sh && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# 安装 code-server 及在线扩展
RUN curl -fsSL https://code-server.dev/install.sh | sh -s -- --version=4.22.0 && \
    code-server --install-extension redhat.vscode-yaml \
                --install-extension dbaeumer.vscode-eslint \
                --install-extension waderyan.gitblame \
                --install-extension mhutchie.git-graph \
                --install-extension donjayamanne.githistory \
                --install-extension tencent-cloud.coding-copilot \
                --install-extension ms-ceintl.vscode-language-pack-zh-hans \
                --install-extension zhuangtongfa.material-theme \
                --install-extension vscode-icons-team.vscode-icons && \
    mkdir -p /root/.local/share/code-server/Machine /root/.vscode-server/data/Machine/

# 复制配置文件
COPY .ide/settings.json /root/.local/share/code-server/Machine/settings.json
COPY .ide/settings.json /root/.vscode-server/data/Machine/settings.json
```

```json .ide/setting.json
{
    "locale": "zh-cn",
    "workbench.iconTheme": "vscode-icons",
    "workbench.colorTheme": "One Dark Pro",
    "editor.fontFamily": "'JetBrains Mono', Consolas, 'Courier New', monospace"
}
```

最后在 `.cnb.yml`中显式声明（追加内容到末尾）：

```yaml
# 定义云开发环境
$:
  vscode:
    - docker:
        build:
          dockerfile: .ide/Dockerfile
          by:
            - .ide/settings.json
      services:
        - docker
        - vscode
```

简单解释一下所作的修改：

- 设置中文语言环境，配置使用 NPM 的淘宝源。
- 安装`npm-check-updates`进行`npm`依赖检查。
- 下载并安装 **JetBrains Mono** 字体，个人比较喜欢这个。
- 安装`Oh My Zsh`并修改用户的默认 Shell，为了终端下方便自动补全。
- 安装了一些基本的 code-server 扩展，并设定主题和图标。

![云原生开发](https://cdn.jsdelivr.net/gh/inkss/inkss-cdn@main/img/article/25-04@云原生开发CNBWithHexo/25-04-20_153229.png)
