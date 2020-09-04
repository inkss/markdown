---
title: 微信小程序 text 组件文本换行
toc: false
date: 2020/04/03 15:34
updated: 2020/04/03 15:34
tag:
  - 微信小程序
  - 组件
categories: 前端
description: 调用接口返回的数据前端显示时不能换行，颇为波折的处理一番。
abbrlink: d24a2bc6
---


&ensp;&emsp;使用小程序发现了一个现象，后端返回的数据在前端显示时没有换行，原原本本的合并为一行显示了，原因大概是从数据库返回给前端时部分数据被转义了。如此便使用正则处理返回的数据，顺便按照换行符分割成数组吧：

```js
onLoad: function(options) {
  var that = this;
  let item = JSON.parse(options.item);
  let content = item.rumourContent.replace(/\\n/g, "\n"); // 替换出换行符
  that.setData({
    rumourInfo: item,
    rumourContent: content.split("\n") // 分割吧
  });
}
```

```html
<view class="content ">
  <text class="textShow" wx:for="{{rumourContent}}" wx:key="index">{{item}}</text>
</view>
```

------

- 换行符

![换行符](https://img.inkss.cn/inkss/static/小程序text组件内容换行.assets/image-20200403153035661.png)

- 效果

![效果](https://img.inkss.cn/inkss/static/小程序text组件内容换行.assets/image-20200403152910344.png)