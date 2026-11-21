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
updated: '2026-11-21 17:58'
abbrlink: a889ded0
headimg: https://cdn.jsdelivr.net/gh/inkss/inkss-cdn@main/img/article/26-11@RustDesk自编译版本/2.png
---

顺应潮流，借助 AI “纯靠许愿”搓出来的 RustDesk 自编译版本。相比官方版本，主要改动是内置了自己的 ID/Relay 服务器地址，把安装包发给小伙伴后直接就能用，不用再教他们填一堆服务器配置。

<!-- more -->

由于本来就是给自己和熟人使用的，还顺手做了一些定制化修改：去掉了安卓端的反诈弹窗，调整了默认授权机制，尽量减少各种花里胡哨的提示和确认步骤，让连接流程更简单直接。

项目地址：

{% link inkss/rustdesk, https://github.com/inkss/rustdesk %}

------

## 一、快速开始

### 1. 配置 GitHub Secrets

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

> ID 和 Key 必须同时填写，否则会出现 key 不匹配。都不填则等同于官方客户端。
>
> 未配置私有仓库时，产物发布到当前仓库的 Releases。

### 2. 触发编译

- **自动触发**：每天 UTC 03:23（北京时间 11:23）检查上游新版本，有更新则自动合并并编译
- **手动触发**：Actions → Sync Upstream Release → Run workflow（或直接触发 Build RustDesk）
- **PR 合并触发**：解决冲突后合并 PR 时，commit 消息包含 `[build]` 即可自动编译

命令行手动触发（`platforms` 可选 `all` / `windows` / `linux` / `android`）：

```bash
gh workflow run build.yml -R inkss/rustdesk --ref custom-build -f platforms=all
```

### 3. 下载产物

从 Releases 页面下载安装包（配置了私有仓库则去私有仓库的 Releases）：

- Windows：MSI + EXE（x86_64）
- Linux：DEB（x86_64 / aarch64）、RPM（x86_64 / aarch64）、Arch 包
- Android：APK（aarch64）

```bash
gh release download <version> -R inkss/rustdesk-releases -p "rustdesk-<version>-x86_64.deb"
```

## 二、编译平台配置

编辑 `.build-config.yml` 控制编译哪些平台：

```yaml
platforms:
  windows: true        # Windows x86_64 (MSI + EXE)
  windows_arm64: false # Windows ARM64（需要 Flutter 3.44）
  linux: true          # Linux x86_64 + aarch64 (DEB + RPM)
  android: true        # Android aarch64 (APK)
```

## 三、工作流说明

| 工作流 | 触发方式 | 功能 |
| --- | --- | --- |
| `sync-upstream.yml` | 每天自动 / 手动 | 检查上游新版本，按需合并代码，触发编译 |
| `build.yml` | tag 推送 / 手动 dispatch / sync 调用 | 获取版本号，编译并发布到 Releases |
| `flutter-build.yml` | 被 build.yml 调用 | 实际编译逻辑（不直接触发） |

另有 `bridge.yml`（bridge 代码生成）与 `third-party-RustDeskTempTopMostWindow.yml`（Windows 置顶窗口组件）两个辅助工作流，详见 [CUSTOM.md](https://github.com/inkss/rustdesk/blob/custom-build/CUSTOM.md)。

## 四、上游同步

每天自动检查上游仓库（[rustdesk/rustdesk](https://github.com/rustdesk/rustdesk)）的新版本：

- 无冲突 → 自动合并，触发编译
- 有冲突 → 自动创建 PR，手动解决后合并
- 已是最新 → 跳过，不触发编译

手动触发且 `platforms=all` 时，会先清理同版本 Release 再重编；只补跑单个平台时保留其他平台已发布的资产。
