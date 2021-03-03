---
layout: friends     # 必须
seo_title: 友链   # 可选，这是友链页的标题
date: 2099/12/31 23:59
updated: 2021/02/08 11:17
sidebar: []
meta:
  header: []
  footer: []
valine:
  placeholder: 这里是友链，虽然我一直不怎么处理友链的啊喂...
---

*{% p center logo small gray, 今夕复何夕，共此灯烛光。 %}*
<p style="margin-bottom: -20px;"></p>

<!-- more -->

{% tabs tab-id %}

<!-- tab <i class="fad fa-galaxy"></i><i style="font-weight: normal;font-style: normal;">&nbsp;举个栗子</i> -->

{% codeblock lang:yml mark:1-3 友链格式，除高亮行，其余均可选~ line_number:false %}
- title: # 网站名称
  url: # 访问地址
  avatar: # 头像地址
  description: # 描述/一句话概述/格言
  screenshot: # 网站截图/展示图
  backgroundColor: # 头像背景颜色
  textColor: # 文本颜色
  keywords:
    - 标签一
    - 标签二
{% endcodeblock %}

<!-- endtab -->

<!-- tab <i class="fad fa-greater-than-equal"></i><i style="font-weight: normal;font-style: normal;">&nbsp;前置要求 </i> -->

{% checkbox green checked, Https 站点（大势所趋） %}
{% checkbox green checked, 网站加载速度正常（待定...） %}
{% checkbox minus blue checked, 免费类域名站点请略过（潜在的不可靠性） %}
{% checkbox minus blue checked, 新创站点请略过（建议拥有一定的原创内容） %}
{% checkbox times red checked, 不接受一切商业性或强烈侵入类广告之站点 %}
{% checkbox times red checked, 不接受违反中华人民共和国法律法规之站点 %}

<!-- endtab -->

{% endtabs %}
