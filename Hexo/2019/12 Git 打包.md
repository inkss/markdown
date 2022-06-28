---
title: Git 打包命令
date: 2019/12/31 11:27
updated: 2020/05/12 11:27
tag:
  - Git
  - Powershell
categories: 资料
toc: false
description: 一些 Git 打包命令，比如打包某次提交之后的所有文件，适合 Powershell 下使用。
abbrlink: 59d81549
---

{% note radiation yellow, Windows 下 powershell 测试通过 %}

{% codeblock lang:sh line_number:false 打包最后一次提交涉及到的文件  %}
git archive --format=zip --output=files.zip HEAD $(git diff-tree -r --no-commit-id --name-only --diff-filter=ACMRT HEAD)
{% endcodeblock %}

{% codeblock lang:sh line_number:false 打包指定（ID）提交涉及到的文件  %}
git archive --format=zip --output=files.zip HEAD $(git diff-tree -r --no-commit-id --name-only --diff-filter=ACMRT xxxxxx)
{% endcodeblock %}

{% codeblock lang:sh line_number:false 打包两个版本之间的差异  %}
git archive --format=zip --output=files.zip HEAD $(git diff-tree -r xxxxxx1 --name-only --diff-filter=ACMRT xxxxxx2)
{% endcodeblock %}

{% codeblock lang:sh line_number:false  打包某次提交之后的所有文件（不含当前）  %}
git archive --format=zip --output=files.zip HEAD $(git diff-tree -r xxxxxx --name-only --diff-filter=ACMRT HEAD)
{% endcodeblock %}

{% codeblock lang:sh line_number:false 打包 Master  %}
git archive --format=zip --output master.zip master
{% endcodeblock %}

{% codeblock lang:sh line_number:false 打包 Head  %}
git archive --format=zip --output head.zip HEAD
{% endcodeblock %}

{% codeblock lang:sh line_number:false 打包 Tag  %}
git archive --format=zip --output v1.0.0.zip v1.0.0
{% endcodeblock %}

{% codeblock lang:sh line_number:false 打包最后修改的文件（不管有没有 add 或 commit )，只要 git diff 有变化就会打包  %}
git archive --format=zip -o update.zip HEAD $(git diff --name-only HEAD^)
{% endcodeblock %}

{% codeblock lang:sh line_number:false 打包两个分支之间的差异  %}
git archive --format=zip -o update.zip HEAD $(git diff --name-only master 1.0.0)
{% endcodeblock %}

{% codeblock lang:sh line_number:false 打包两个版本 (commit) 间的差异  %}
git archive --format=zip -o update.zip HEAD $(git diff --name-only HEAD~2)
{% endcodeblock %}
