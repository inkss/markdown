---
title: RustDesk 自编译版本：预置服务器配置
title_display: true
headimg_display: true
toc: true
indent: true
tag:
  - RustDesk
  - 远程桌面
  - GitHub Actions
  - Android
categories: 工具
description: >-
  基于上游 RustDesk 的自编译版本，内置私有 ID/Relay 服务器，分发后无需手动填写服务器配置。
  本文介绍通过 GitHub Actions 自动同步上游、编译 Windows、Linux、Android 客户端的流程，以及 Android 端去弹窗、调整授权等定制改动。
date: '2026-11-21 17:58'
updated: '2026-11-22 00:50'
abbrlink: a889ded0
headimg: https://cdn.jsdelivr.net/gh/inkss/inkss-cdn@main/img/article/26-11@RustDesk自编译版本/2.png
---

顺应潮流，借助 AI “纯靠许愿”搓出来的 RustDesk 自编译版本。相比官方版本，主要改动是内置了自己的 ID/Relay 服务器地址，把安装包发给小伙伴后直接就能用，不用再教他们填一堆服务器配置。

<!-- more -->

由于本来就是给自己和熟人使用的，还顺手做了一些定制化修改：

| 维度 | 自编译版本 | 上游 |
| --- | --- | --- |
| ID/API 服务器 | 编译时内置，客户端默认隐藏不显示 | 默认官方服务器 |
| 连接认证 | 未设固定密码时直接连接；但保留安全能力 | 默认需要密码或点击同意 |
| 连接窗口 | 连接时默认最小化，用户可从任务栏恢复 | 连接时弹出并置顶，3 秒后最小化 |
| Windows x64 无人值守版 | 无托盘、连接时不显示窗口、后台运行；普通版保留完整窗口/托盘 | 无此形态 |

项目地址：

{% link inkss/rustdesk, https://github.com/inkss/rustdesk %}

------

## 使用指南

### 1. Fork 仓库

1. 打开 [inkss/rustdesk](https://github.com/inkss/rustdesk)，点击右上角 **Fork**。
2. 进入你自己的 fork，在分支下拉框中切换到长期分支 `custom-build`。
3. 后续的 Secrets、Actions 都在你的 fork 中操作。

### 2. 配置 GitHub Secrets

在仓库 **Settings → Secrets and variables → actions → Repository secrets** 中添加：

| Secret 名称 | 说明 | 必填 |
| --- | --- | --- |
| `RENDEZVOUS_SERVER` | ID 服务器地址（含端口，如 `your-server.com:21116`） | ✅ |
| `RS_PUB_KEY` | 服务器公钥（Base64 编码，位于服务器 `id_ed25519.pub`） | ✅ |
| `API_SERVER` | API 服务器地址（如 `https://api.your-server.com`） | 可选 |
| `ANDROID_SIGNING_KEY` | Base64 编码的 keystore 文件 | 可选 |
| `ANDROID_ALIAS` | keystore 别名 | 可选 |
| `ANDROID_KEY_STORE_PASSWORD` | keystore 密码 | 可选 |
| `ANDROID_KEY_PASSWORD` | key 密码 | 可选 |
| `RELEASE_REPO` | 私有仓库名（如 `inkss/rustdesk-releases`） | 可选 |
| `RELEASE_PAT` | Fine-grained PAT，对私有仓库有 Contents 读写权限 | 可选 |
| `SYNC_PAT` | Classic PAT（`repo` + `workflow`），用于同步后推送变更并触发编译 | 可选 |

> ID 和 Key 必须同时填写，否则会出现 key 不匹配。都不填则等效于官方客户端。
>
> `ANDROID_*` 类的变量是编译安卓版本时需要的；未配置私有仓库时，产物发布到当前仓库的 Releases。

### 3. 编译平台调整


- **编译平台调整**：编辑 `.build-config.yml`。

```yaml
platforms:
  windows: true        # Windows x86_64（普通版 + 无感版，MSI + EXE）
  windows_arm64: false # Windows ARM64（需要 Flutter 3.44；无感版不构建 ARM64）
  linux: true          # Linux x86_64 + aarch64（DEB + RPM）
  android: true        # Android aarch64（APK）
```

### 4. 触发编译

- **自动触发**：每天 UTC 03:23（北京时间 11:23）检查上游新版本，有更新则自动合并并编译。
- **手动触发**：Actions → Sync Upstream Release → Run workflow（或直接触发 Build RustDesk）。
- **PR 合并触发**：解决冲突后合并 PR 时，commit 消息包含 `[build]` 即可自动编译。

命令行手动触发（`platforms` 可选 `all` / `windows` / `linux` / `android`）。把 `YOUR_ACCOUNT` 换成你的 GitHub 账号；如果直接在原仓库操作，用 `inkss/rustdesk`：

```bash
gh workflow run build.yml -R YOUR_ACCOUNT/rustdesk --ref custom-build -f platforms=all
```

只补跑 Windows（包含普通版与无感版）：

```bash
gh workflow run build.yml -R YOUR_ACCOUNT/rustdesk --ref custom-build -f platforms=windows
```

### 5. 下载产物

从 Releases 页面下载安装包（配置了私有仓库则去私有仓库的 Releases）：

- Windows：MSI + EXE（x86_64）；额外提供 `-ui-invisible` MSI + EXE
- Linux：DEB（x86_64 / aarch64）、RPM（x86_64 / aarch64）、Arch 包
- Android：APK（aarch64）

```bash
# 默认发布到当前仓库
gh release download <version> -R YOUR_ACCOUNT/rustdesk -p "rustdesk-<version>-x86_64.deb"

# 配置了私有 Release 仓库时，把仓库名换成私有仓库
gh release download <version> -R YOUR_ACCOUNT/rustdesk-releases -p "rustdesk-<version>-x86_64.deb"
```

## 参考内容

### 1. 生成 Android 签名密钥

```bash
# 1. 生成 keystore（只需执行一次）
keytool -genkey -v -keystore release.keystore \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias rustdesk -storepass 你的密码 -keypass 你的密码 \
  -dname "CN=RustDesk, OU=Dev, O=RustDesk, L=Unknown, ST=Unknown, C=US"

# 2. 编码为 Base64
base64 -w 0 release.keystore   # Linux
# base64 release.keystore      # macOS
```

将输出的 Base64 字符串填入 `ANDROID_SIGNING_KEY`，别名填 `rustdesk`，密码填你设的值。

### 2. 私有 Release 仓库

公开仓库享受免费 Actions 额度，但 Releases 对所有人可见，存在泄露风险。

1\. 创建私有仓库

GitHub 上新建一个 private 仓库（如 `rustdesk-releases`），创建时勾选 **Initialize this repository with a README**[^1]。

[^1]: 仓库不能为空，否则 Release 创建会失败。

2\. 生成 PAT

GitHub → Settings → Developer settings → Personal access tokens → Fine-grained tokens → Generate new token：

- Token name：随意（如 `rustdesk-release`）
- Expiration：选 1 年
- Repository access：选 `Only select repositories` → 选刚建的私有仓库
- Permissions → Repository permissions → **Contents**：`Read and write`

3\. 配置 Secret

在公开仓库 Settings → Secrets → Repository secrets 中添加：

| Secret 名称 | 值 |
| --- | --- |
| `RELEASE_REPO` | 私有仓库名（如 `inkss/rustdesk-releases`） |
| `RELEASE_PAT` | 上一步复制的 token |

配置后，编译产物会推送到私有仓库的 Releases，当前仓库不再有产物。

### 3. PAT 汇总

如果使用私有 Release 仓库，需要两个 PAT；默认发布到当前仓库时，`RELEASE_PAT` 不需要，`SYNC_PAT` 仍可选。创建方式相同（GitHub → Settings → Developer settings → Personal access tokens）：

| Secret | 用途 | 推荐类型 | 权限 | 必填 |
| --- | --- | --- | --- | --- |
| `RELEASE_PAT` | 推送编译产物到私有仓库 | Fine-grained | 选私有仓库 → Contents: `Read and write` | 配置私有仓库时必填 |
| `SYNC_PAT` | 上游同步时推送变更、创建 PR 并触发编译 | Classic | 勾选 `repo` + `workflow` scope | 可选；未配置时回退 `GITHUB_TOKEN`，但自动触发编译会失效（`REPO_TOKEN` 作为历史 fallback 仍受支持） |

### 4. rustdesk-api 兼容

本版本兼容 [lejianwen/rustdesk-api](https://github.com/lejianwen/rustdesk-api)，跳过了 `secure_tcp` 与 `secure_tcp_required` 握手，登录 API 账户后不会出现连接超时。
