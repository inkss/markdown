---
layout: docs
seo_title: Volantis 主题用户手册
sidebar: [toc]
top_meta: false
bottom_meta: false
date: 2021/03/04 00:00
updated: 2021/03/04 00:00
---

<p class="p center logo large"><em>Volantis 主题用户手册 <sup>自用</sup></em></p>

## 一、Front-matter

| 字段        | 含义          |   值类型    |            值             | 备注                           |
| :---------- | :------------ | :---------: | :-----------------------: | :----------------------------- |
| layout      | 页面布局模版  |   String    |      post,page,docs       | 独立页面,文章页面,文档页面     |
| title       | 页面标题      |   String    |             -             |                                |
| seo_title   | 网页标题      |   String    |        page.title         | 可替代页面标题                 |
| date        | 创建时间      |    Date     |       文件创建时间        |                                |
| updated     | 更新日期      |    Date     |       文件修改时间        |                                |
| link        | 外部文章网址  |   String    |             -             | 去源站阅读                     |
| music       | 内部音乐控件  |  [Object]   |             -             |                                |
| keywords    | 文章关键词    |   String    |             -             |                                |
| description | 文章摘要      |   String    |             -             |                                |
| cover       | 是否显示封面  |    Bool     |           true            | 强制指定时可以覆盖主题封面配置 |
| top_meta    | 是否显示 meta |    Bool     |           true            | 顶部 meta 信息                 |
| bottom_meta | 是否显示 meta |    Bool     |           true            | 底部 meta 信息                 |
| sidebar     | 页面侧边栏    | Bool, Array | sidebar.for_page/for_post | 手动指定侧边栏                 |
| mathjax     | 是否渲染公式  |    Bool     |           false           |                                |
| thumbnail   | 缩略图        |   String    |           false           | 显示在文章页右上角             |
| icons       | 图标          |    Array    |            []             | 显示在归档页                   |
| pin         | 是否置顶      |    Bool     |           false           |                                |
| headimg     | 文章头图      |     url     |             -             | **目前修改为只在列表页显示**   |
| indent      | 是否首行缩进  |    Bool     |           false           | **自定义修改：缩进 2 字符**    |

## 二、标签插件

待补充