---
title: JS 前端的自定义右键与剪切板操作实现
toc: true
tag:
  - Clipboard
  - Permissions-API
  - JS
categories: 前端
description: JS 前端实现自定义右键 | JS 前端复制文字/图片到剪切板 | JS 前端从剪切板获取文本
date: '2021-03-01 00:00'
updated: '2021-03-01 00:00'
abbrlink: '63296e49'
---

&ensp;&emsp;本文分为两部分：自定义右键和剪切板的事件处理。主要目标是在自定义网页右键的基础上，实现诸如复制、粘贴的剪切板操作，文本选中的复制、图片的复制和输入框下的粘贴。

## 一、自定义右键

&ensp;&emsp;前端页面的自定义右键通过 `oncontextmenu` 实现，相关兼容性如下：

![兼容性](../../static/rightMenu.assets/image-20210228145517668.png)

&ensp;&emsp;基本不用担心兼容性问题，对全局右键的修改如下，需要注意的是得返回 `false` 用以阻止默认菜单。

{% codeblock lang:js 全局修改右键替换 line_number:false  %}
window.oncontextmenu = function (event) {
  // do..
  return false;
}
{% endcodeblock %}

### 1. 右键菜单的绘制

&ensp;&emsp;这里，在参数 `event` 我们可以拿到所需要的变量。

## 二、剪切板操作

{% note quote, Clipboard 接口实现了 Clipboard API，如果用户授予了相应的权限，就能提供系统剪贴板的读写访问。在 Web 应用程序中，Clipboard API 可用于实现剪切、复制和粘贴功能。 %}
