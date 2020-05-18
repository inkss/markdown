---
title: Git 打包命令
date: 2019/12/31
tag:
  - Git
  - 打包
  - Powershell
categories: git
description: 一些 Git 打包命令，比如打包某次提交之后的所有文件，适合 Powershell 下使用。
abbrlink: 59d81549
---

> Windows 下 powershell 测试通过

- 打包最后一次提交涉及到的文件：

```sh
git archive --format=zip --output=files.zip HEAD $(git diff-tree -r --no-commit-id --name-only --diff-filter=ACMRT HEAD)
```

- 打包指定（ID）提交涉及到的文件：

```sh
git archive --format=zip --output=files.zip HEAD $(git diff-tree -r --no-commit-id --name-only --diff-filter=ACMRT xxxxxx)
```

- 打包两个版本之间的差异：

```sh
git archive --format=zip --output=files.zip HEAD $(git diff-tree -r xxxxxx1 --name-only --diff-filter=ACMRT xxxxxx2)
```

- 打包某次提交之后的所有文件（不含当前）

```sh
git archive --format=zip --output=files.zip HEAD $(git diff-tree -r xxxxxx --name-only --diff-filter=ACMRT HEAD)
```

- 打包 master

```sh
git archive --format=zip --output master.zip master
```

- 打包 head

```sh
git archive --format=zip --output head.zip HEAD
```

- 打包 tag

```sh
git archive --format=zip --output v1.0.0.zip v1.0.0
```

- 打包最后修改的文件（不管有没有 add 或 commit )，只要 git diff 有变化就会打包

```sh
git archive --format=zip -o update.zip HEAD $(git diff --name-only HEAD^)
```

- 打包两个分支之间的差异

```sh
git archive --format=zip -o update.zip HEAD $(git diff --name-only master 1.0.0)
```

- 打包两个版本 (commit) 间的差异

```sh
git archive --format=zip -o update.zip HEAD $(git diff --name-only HEAD~2)
```
