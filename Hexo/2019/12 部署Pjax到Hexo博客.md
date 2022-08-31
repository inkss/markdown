---
uuid: 1bf9e6d2-283a-11ed-a81c-e5c20e2f9c97
title: Hexo 博客部署 Pjax 局部刷新
seo_title: Hexo 博客部署 Pjax 局部刷新
toc: true
indent: true
date: 2019/12/12 23:56
updated: 2020/08/05 23:56
tag:
  - Hexo
  - Pjax
categories: 博客
description: 本篇定位于理论知识，概述性简介如何处理 Pjax 与 Hexo 的兼容过程。
abbrlink: 80b5f235
music:
  enable: true
  server: netease
  type: song
  id: 22703777
mathjax: false
icons: [fad fa-fire]
---

<p class="p center logo large"><em>Hexo 博客部署 Pjax 局部刷新 <sup>理论篇</sup></em></p>
<br><br>

本篇定位于 **理论知识** ，实际应用见 [Volantis 主题部署 Pjax](/blog/76993423/)，前置知识，SPA 页面。

我在对博客做 Pjax 兼容时，发现文档资料都很少，零零散散，整个过程几乎是摸索着进行的，百思不得其解，所以此处留下记录以备后用，文章中的思路不一定是最佳的，姑且算是个抛砖引玉。

## 一、Pjax 加速的原理

在进入正文之前，我们先简单的了解：**单页应用**（英语：single-page application，缩写**SPA**）是一种网络应用程序或网站的模型，它通过动态重写当前页面来与用户交互，而非传统的从服务器重新加载整个新页面。这种方法避免了页面之间切换打断用户体验，使应用程序更像一个桌面应用程序。**与单页应用的交互通常涉及到与网页服务器后端的动态通信**。说白了也就是通过 `pushState() + XHR` 技术实现的页面切换。

而 Hexo 站点本身是个静态页面，无法发出动态请求，所以这里便引出了本文的主角 Pjax 框架了。其思路是通过拦截 `a` 链接，发送 XHR 请求，获取下级页面内容，接着替换指定区域完成整个过程。由于不是动态网站，Pjax 在请求过程中获取的是整个站点的 Html 内容，所以请求本身是无法达到加速的，但是可以减少页面中 JS 文件的重复请求，此外还可以利用一些预加载技术（预读缓存）和磁盘缓存进一步提升访问速度，实际体验效果是极佳的。

## 二、前期的准备工作

正如前文中所说，在一次 Pjax 请求获取完整的 html 过程中，从获取到的结果中找到选中的内容替换到页面里，所以我们需要先划分页面结构，确定被替换的区域。

### 2.1 分析网站布局

一个网站的内容有什么？对于 Hexo 这类静态博客来说，网站内容是根据模板文件生成的，其中存在着大量共有的元素。大概上可以划分成这几部分：*导航栏*、*文章部分*、*侧边栏*、*页脚*。在这些区域中，潜在的重复内容是 **导航栏** 和 **页脚** ，一般来说文章区域和侧边栏区域是不会相同的，那么对应到 Hexo 主题里，这部分是由 `layout.ejs` 文件控制的。我们找到核心部分，去除掉无用的干扰项后分析一番：

{% folding 一个简单的页面分析 %}

{% codeblock lang:html line_number:false 以本主题的样式文件为例  %}
<!DOCTYPE html>
<html>
<%- partial('_partial/head') %>                             // 加载 head 标签
<body>
  <%- partial('_partial/cover', {showCover: showCover}) %>  // 加载导航栏
  <div class="l_body<%- showCover ? '' : ' nocover' %>">    // 加载封面
    <div class='body-wrapper'>
      <%- body %>                                           // 页面内容主体
    </div>
  </div>
  <%- partial('_partial/scripts') %>                        // 引入 js 文件
</body>
</html>
{% endcodeblock %}

{% endfolding %}

由此可知，页面中变动的部分都位于 `<%- body %>` 这个标签中了，那么它就是目标了，在 `class='body-wrapper'` 后面添加 `id="pjax-container"` 以方便选中元素。

### 2.2 Pjax 的配置项

详细的文档可在项目的 readme 中查看，传送链接：[Pjax Document](https://github.com/MoOx/pjax/blob/master/README.md)。

在正式使用 Pjax 之前，需要先添加它的 js 文件，位置上没那么讲究，Mox 的 Pjax 去除了对 Jquery 的依赖，但别放到重载区域内。Pjax 的初始化写法与 jquery-pjax 不完全相同，可以类似这样子写：

{% codeblock lang:js line_number:false Pjax 初始化函数 %}
var pjax = new Pjax({
    elements: 'a[href]:not([href^="#"]):not([href="javascript:void(0)"])',   // 拦截正常带链接的 a 标签
    selectors: ["#pjax-container","title"]                                   // 根据实际需要确认重载区域
});
{% endcodeblock %}

这里共配置了两部分，**选择器（selectors）** 和 **元素 （elements）** 。默认情况下 pjax 处理的元素为 `a[href], form[action]` ，但是并不是所有的 `<a>` 标签都可追踪，所以使用 `:not` 语法排除一些不需要使用 pjax 跳转的元素；接着是选择器（class 或 id 选择都行），选择器用以选定重载的范围，个人理解为在 **指定标签内的内容**，在跳转页面时均被替换，不在这个范围内的不做处理。

除此之外，我们还有三个相对重要的 Pjax 事件函数可以使用：

{% codeblock lang:js line_number:false 比较有用的 Pjax 函数 %}
document.addEventListener('pjax:send', function () {});
document.addEventListener('pjax:complete', function () {});
document.addEventListener('pjax:error', function () {});
{% endcodeblock %}

### 2.3 一些潜在的问题

至此，我们已经完成了引入 Pjax 文件，划定重载区域，初始化 Pjax 对象，是不是一切就高枕无忧了呢？当然，答案是否定的。这里可以引用一句古老的名言 ”大人，时代变了！”，当下的网站早已不是个孤零零的产物，而是和 Javascript 有着密切联系后的动态页面，存在着大量的事件监听处理。不巧的是在替换内容时，部分事件监听丢失了，异常、错误、功能失效等等，就愉快的上演了。

Javascript 部分自然是需要重新绑定注册的，但是是不是无脑全部重新绑定就行呢？答案也是否定的，我们会有个大敌：**重复事件监听**，它或许不会立即爆发危害，但是随着浏览页面的增加，事件绑定可能会越来越累加，影响效率，潜在造成错误等等。所以这里就需要利用 `send` 和 `complete` 这对事件了，合理利用，正确解决问题。

## 三、后期的兼容处理

### 3.1 几种兼容思路

首先，是一些需要每次进入页面，都必须重新加载的 JS 文件，典型的有不蒜子网页计数、各类分析脚本，自然而然他们必须要多次加载，对于这类的操作，简单的处理方案就是在引入相关 js 文件时，加入格外的属性，如 `data-pjax` ，统一处理具有这个属性的 JS 文件，在跳转页面时重新导入（以不蒜子为例子）：

{% folding JS 文件整体重载 %}
```js
<script async src="//busuanzi.ibruce.info/busuanzi/2.3/busuanzi.pure.mini.js" data-pjax></script>
<script>
document.addEventListener('pjax:complete', function () {
  $('script[data-pjax], .pjax-reload script').each(function () {
    $(this).parent().append($(this).remove());
  });
});
</script>
```
{% endfolding %}

另一种情况是，引入的 JS 文件无需重复导入，但是绑定的函数需要重新处理，比如 Fancybox 这类弹窗，这类的可以写在函数里，在页面加载完成后 `$(document).ready(function(){})` 和 Pjax 重载完成后重新调用，比如本站的 Fancybox 初始化函数：

{% folding Js 函数重载（以 Fancybox 为例） %}
```js
<script>
function pjax_fancybox() {
  $(".article-entry").find("img").not('.inline').not('a img').each(function () { //渲染 fancybox
    var element = document.createElement("a"); // a 标签
    $(element).attr("pjax-fancybox", "");  // 过滤 pjax
    $(element).attr("href", $(this).attr("src"));
    if ($(this).attr("data-original")) {
      $(element).attr("href", $(this).attr("data-original"));
    }
    $(element).attr("data-fancybox", "images");
    var caption = "";   // 描述信息
    if ($(this).attr('alt')) {  // 判断当前页面是否存在描述信息
      $(element).attr('data-caption', $(this).attr('alt'));
      caption = $(this).attr('alt');
    }
    var div = document.createElement("div");
    $(div).addClass("fancybox");
    $(this).wrap(div); // 最外层套 div ，其实主要作用还是 class 样式
    var span = document.createElement("span");
    $(span).addClass("image-caption");
    $(span).text(caption); // 加描述
    $(this).after(span);  // 再套一层描述
    $(this).wrap(element);  // 最后套 a 标签
  })
  $(".article-entry").find("img").fancybox({
    selector: '[data-fancybox="images"]',
    hash: false,
    loop: false,
    closeClick: true,
    helpers: {
      overlay: {closeClick: true}
    },
    buttons: [
      "zoom",
      "close"
    ]
  });
};
</script>
```
{% endfolding %}

Fancybox 在每一页重新绑定，同类的代码都可以采用以上的处理方式。需要重新加载的整个 JS 文件的就单独重载，需要重新执行绑定函数的，就在 Pjax 的事件监听函数中重新调用。

### 3.2 文章独有的变量

这里的变量是指那种写在头部的变量，他们在页面上不存在（发挥在 Hexo 的渲染阶段）。典型例子为本主题的评论部分的配置，这部分基本位于 `scripts.ejs` 文件中，当读取相应的配置属性时，从主题配置文件读取的也还好，毕竟无需在意变动的问题，麻烦就麻烦在从文章页面中读取的属性。

比如评论的占位符和地址的自定义，渲染阶段所产生的结果在页面经过 Pjax 局部重载后拿不到。所以这里换个思路，将一些变量藏在文章区域内，在使用时通过元素选择器调用。

## 四、后记

### 4.1 参考链接

吃水不忘挖井人，除 Pjax 的官方网站外，另外两个站点也对我的帮助很大，这里予以记录：

- 来自 liuyib 的 [集成 Pjax 实现网站无刷新加载](https://liuyib.github.io/2019/09/24/use-pjax-to-your-site/) 。
- 相同主题下的另一个 Pjax 部署思路：[YuGao's Blog](https://sxyugao.top/) 。

### 4.2 本页面的独特修改

 因为文章的代码不出预料的会很多，所以萌生了使用折叠框的念头，在不改动渲染器的情况下，采取了直接写 html 代码的思路。~~但是未曾想到折叠框在伸缩、展开的过程中，因改变了页面高度的缘故影响到目录导航栏的激活跟随，思索了一阵子后没发现较好的解决办法，特此记录，代办代办（*有想法的小伙伴也可以留言啊，或者有更好的代码块折叠方案什么的*）。~~

 *主题已原生支持。*

### 4.3 功能性测试


{% p center logo large, '<i class="fad fa-narwhal"></i>' %}
{% p center logo large, Volantis %}
{% p center small, A Wonderful Theme for Hexo %}

#### 4.3.1 Tab

{% tabs tabTest %}

<!-- tab 失落的宇宙 -->

&emsp;&emsp;《失落的宇宙》（日文名：ロスト・ユニバース，LOST UNIVERSE，大陆译名： 宇宙刑警）是神坂一原著的日本科幻小说。亦改编成漫画与动画。小说版共5卷，漫画版共4册。动画版1998年4月3日发行，总共26集。

<!-- endtab -->

<!-- tab 背景介绍 -->

&emsp;&emsp;在远古的时代，宇宙中存在着分别代表光明与黑暗的两个文明，正如光与影永远是相互矛盾的，两个文明也长年处于战争之中，代表黑暗的文明为了取得胜利，就制造了一种高科技的战舰，这种战舰拥有超强的作战能力及穿越时空的能力，而且，还拥有自己的思想。然而，**这种战舰却会把驾驶者作为自己的食粮**，以此来增强自己的力量。
这种黑色的战舰就像魔鬼一样，会把敌人彻底毁灭，但同时也会吞噬驾驶员的生命。而在同一时间，为了对抗敌人的黑色战舰，代表光明的文明也创造了一艘白色战舰与黑色战舰抗衡。后来，两个文明都原因不明地消失了，而那些战舰却依然存在在宇宙的某个角落里，新时代的人为他们起了个名字——遗失战舰（LostShips）

<!-- endtab -->

<!-- tab 豆瓣评价 -->

&emsp;&emsp;小时候在电视台看动画片《宇宙刑警》（失落的宇宙），结局是男主角去敌窝生死未卜，女主角回复了平静的生活等待他回来。电视里没放完就插了广告，最后一集看的这么多年一直耿耿于怀，我想知道他是否平安回来。昨天终于偶然间看到了最后一集，老牌怀旧的硬朗画风和人物设定，她在风和日丽的天气里晒洁白的被单，去他的墓前放一束花，突然看到他的宇宙飞船缓缓降落，这么多年的等待终于有了结果。于是我这么多年的等待也有了结果。
然而这是动画，所以就在美好的这一刻打住。现实中也许宇宙飞船上早已物是人非，躺着他的尸体残骸也说不一定。得不到和已失去，总是在二者中间的状态才是最好。 —— honey

<!-- endtab -->

<!-- tab 相关图片 -->

{% gallery %}

![失落的宇宙](../../img/article/部署Pjax到Hexo博客/1570189514.jpg)

{% endgallery %}

<!-- endtab -->

{% endtabs %}

#### 4.3.2 Folding

{% folding 查看图片测试 %}

![](../../img/article/部署Pjax到Hexo博客/41F215B9-261F-48B4-80B5-4E86E165259E.jpeg)

{% endfolding %}

{% folding cyan open, 查看默认打开的折叠框 %}

这是一个默认打开的折叠框。

{% endfolding %}

{% folding red, 查看嵌套测试 %}

{% folding blue, 查看嵌套测试2 %}

{% folding 查看嵌套测试3 %}

多级嵌套，又称套娃~

{% endfolding %}

{% endfolding %}

{% endfolding %}

#### 4.3.3 Checkbox & Radio

{% tabs tab-id %}

<!-- tab Checkbox -->

{% checkbox 纯文本测试 %}
{% checkbox checked, 支持简单的 [markdown](https://guides.github.com/features/mastering-markdown/) 语法 %}
{% checkbox red, 支持自定义颜色 %}
{% checkbox green checked, 绿色 + 默认选中 %}
{% checkbox plus green checked, 增加 %}
{% checkbox minus yellow checked, 减少 %}
{% checkbox times red checked, 叉 %}

<!-- endtab -->

<!-- tab Radio -->

{% radio 纯文本测试 %}
{% radio checked, 支持简单的 [markdown](https://guides.github.com/features/mastering-markdown/) 语法 %}
{% radio red, 支持自定义颜色 %}
{% radio green, 绿色 %}
{% radio yellow, 黄色 %}
{% radio cyan, 青色 %}
{% radio blue, 蓝色 %}

<!-- endtab -->

{% endtabs %}
