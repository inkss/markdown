---
layout: page
title: Code Language
sidebar: [wiki-hexo-theme, toc]
meta:
  header: []
  footer: [updated, counter]
date: 2021/02/01 11:15
updated: 2021/02/01 11:15
---

{% p center logo large, Code Language %}

## 一、前端

### 求对象数组某个值的和

{% codeblock 求对象数组某个值的和 line_number:false %}
constys_all_prices = moneyList.YS.reduce((p,e) => p+e.money,0);
{% endcodeblock %}

### 求对象数组中某个值的去重结果

{% codeblock 求对象数组中某个值的去重结果 line_number:false %}
currencyArr = data.rows.map(item => {
    return item.currencyType;
}).filter((item, index, arr) => {
    return arr.indexOf(item, 0) === index;
})
{% endcodeblock %}
