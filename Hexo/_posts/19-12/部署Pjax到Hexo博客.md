---
title: Hexo 博客部署 Pjax 局部刷新
toc: true
date: 2019/12/12
tag:
  - Hexo
  - Pjax
  - Material X
categories: other
description: 为静态博客 Hexo 添加了 Pjax 支持，顺便还有一些 balabal 的测试呀。
abbrlink: 80b5f235
music:
  enable: true      # true（文章内和文章列表都显示）
  server: netease   # netease（网易云音乐）tencent（QQ音乐） xiami（虾米） kugou（酷狗）
  type: song        # song （单曲） album （专辑） playlist （歌单） search （搜索）
  id: 22703777      # 歌曲/专辑/歌单 ID
mathjax: false
icons: [fal fa-fire]  
---

&ensp;&emsp;对博客部署 Pjax 的好处在于局部重载时可以保留一些元素（如音乐播放器）不被刷新去除掉。然后是关于 Pjax 的版本选择，最早一版为 [defunkt](https://github.com/defunk)/[jquery-pjax](https://github.com/defunkt/jquery-pjax)，需要依赖 jQuery 框架，上一次更新已经是三年前了。所以本博客在挑选框架时，使用的是另一个版本： [MoOx](https://github.com/MoOx/)/[pjax](https://github.com/MoOx/pjax)。这版去除了 jQuery 依赖，当然这不是重点，主要是更新的挺勤，我喜新厌旧。

&ensp;&emsp;本篇定位于理论知识，实际应用详见 [Volantis 主题部署 Pjax](/article/other/76993423.html)，前置知识，SPA 页面。

## 一、Pjax 简介

*不负责任的复制粘贴 呱*  🐸 ~

> Easily enable fast AJAX navigation on any website (using pushState() + XHR)

Pjax is **a standalone JavaScript module** that uses [AJAX](https://developer.mozilla.org/en-US/docs/Web/Guide/AJAX) (XmlHttpRequest) and [pushState()](https://developer.mozilla.org/en-US/docs/Web/Guide/API/DOM/Manipulating_the_browser_history) to deliver a fast browsing experience.It allows you to completely transform the user experience of standard websites (server-side generated or static ones) to make users feel like they are browsing an app, especially for those with low bandwidth connections.

**No more full page reloads. No more multiple HTTP requests.**

_Pjax does not rely on other libraries, like jQuery or similar. It is written entirely in vanilla JS._

## 二、前期准备

### 2.1 分析网站布局

&ensp;&emsp;对于 Hexo 这类静态博客来说，网站内容是根据模板文件生成的，所以其中存在着大量共有的元素。以本主题为例，可以划分成四部分：*导航栏*、*文章部分*、*侧边栏*、*页脚*。很明显，各个页面中相似的元素是 **导航栏** 和 **页脚** ，那么对应到主题布局上，这部分是由 `layout.ejs` 文件控制的。我们找到核心部分，去除掉无用的干扰项后分析一番：

{% folding 一个简单的页面分析 %}
```ejs
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
```
{% endfolding %}

&ensp;&emsp;由此可知，页面中变动的部分都位于 `<%- body %>` 这个标签中了，那么它就是目标了，在 `class='body-wrapper'` 后面添加 `id="pjax-container"` 以备后用。

### 2.2 Pjax 的配置项

&ensp;&emsp;详细的文档可在项目的 readme 中查看，传送链接：[Pjax Document](https://github.com/MoOx/pjax/blob/master/README.md)。

&ensp;&emsp;在正式使用 Pjax 之前，需要先添加它的 js 文件，位置上没那么讲究，但建议放在 `scripts.ejs` 的首行。Pjax 的初始化写法与 jquery-pjax 不完全相同，本博中的初始化函数是这样的：

{% folding Pjax 初始化函数 %}
```js
var pjax = new Pjax({
    elements: 'a[href]:not([href^="#"]):not([href="javascript:void(0)"])',
    selectors: ["#pjax-container","title"]
});
```
{% endfolding %}

&ensp;&emsp;这里共配置了两部分，**选择器（selectors）** 和 **元素 （elements）** 。默认情况下 pjax 处理的元素为 `a[href], form[action]` ，但是并不是所有的 `<a>` 标签都可追踪，所以使用 `:not` 语法排除一些不需要使用 pjax 跳转的元素；接着是选择器（class 或 id 选择都行），选择器用以选定重载的范围，个人理解为在 **指定标签内的内容**，在跳转页面时均被替换，不在这个范围内的不做处理。

&ensp;&emsp;对于本博客，一些按钮的点击无需处理，所以忽略掉；重载页面时，除了上文中提到的需要重载区域外，还需要重载掉 `head` 中的 `title` `meta` 等标签（其实这个 meta 的范围可以进一步指定），除此之外，还附加了一个重载区域：文章导航栏，这个原因最后说。一般情况下，在初始化之后，Pjax 就已经在工作了，此时点击页面上的链接就可以看到重载页面。但是重载局部页面还带来了另外一些影响，原来绑定的元素因为内容变动的缘故，都会失效，所以这里还有另外三个有用的 Pjax 函数：

{% folding 比较有用的 Pjax 函数 %}
```js
pjax.loadUrl();
document.addEventListener('pjax:send', function () {});
document.addEventListener('pjax:complete', function () {});
document.addEventListener('pjax:error', function () {});
```
{% endfolding %}

&ensp;&emsp;他们的作用分别是：pjax 事件，监听 pjax 请求开始后和请求完成后时的事件、单独发送 pjax 请求。比如加载动画可以在 `send` 事件中开始，在 `complete` 事件中结束，再比如重新绑定的函数都可以放在完成事件里。而单独发送 pjax 请求，这里用在了搜索结果上，搜索结果是后期渲染进去的，并未被 pjax 追踪，所以采用追加点击事件，单独发起 pjax 请求的处理方法。

### 2.3 局部重载：Javascript

&ensp;&emsp;首先，是一些需要每次进入页面，都必须重新加载的 JS 文件，典型的有不蒜子网页计数、各类分析脚本，自然而然他们必须要多次加载，对于这类的操作，简单的处理方案就是在引入相关 js 文件时，加入格外的属性，如 `data-pjax` ，统一处理具有这个属性的 JS 文件，在跳转页面时重新导入（以不蒜子为例子）：

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

&ensp;&emsp;另一种情况是，引入的 JS 文件无需重复导入，但是绑定的函数需要重新处理，比如 Fancybox 这类弹窗，这类的可以写在函数里，在页面加载完成后 `$(document).ready(function(){})` 和 Pjax 重载完成后重新调用，比如本站的 Fancybox 初始化函数：

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

&ensp;&emsp;Fancybox 在每一页重新绑定，同类的代码都可以采用以上的处理方式。需要重新加载的整个 JS 文件的就单独重载，需要重新执行绑定函数的，就在 Pjax 的事件监听函数中重新调用。

### 2.4 文章独有的变量

&ensp;&emsp;这里的变量是指那种写在头部的变量，他们在页面上不存在（发挥在 Hexo 的渲染阶段）。典型例子为本主题的评论部分的配置，这部分基本位于 `scripts.ejs` 文件中，当读取相应的配置属性时，从主题配置文件读取的也还好，毕竟无需在意变动的问题，麻烦就麻烦在从文章页面中读取的属性。

&ensp;&emsp;比如评论的占位符和地址的自定义，渲染阶段所产生的结果在页面经过 Pjax 局部重载后拿不到。所以这里换个思路，将一些变量藏在文章区域内，在使用时通过元素选择器调用。

## 三、后记

### 3.1 参考链接

吃水不忘挖井人，除 Pjax 的官方网站外，另外两个站点也对我的帮助很大，这里予以记录：

- 来自 liuyib 的 [集成 Pjax 实现网站无刷新加载](https://liuyib.github.io/2019/09/24/use-pjax-to-your-site/) 。
- 相同主题下的另一个 Pjax 部署思路：[YuGao's Blog](https://sxyugao.top/) 。

### 3.2 本页面的独特修改

&ensp;&emsp; 因为文章的代码不出预料的会很多，所以萌生了使用折叠框的念头，在不改动渲染器的情况下，采取了直接写 html 代码的思路。~~但是未曾想到折叠框在伸缩、展开的过程中，因改变了页面高度的缘故影响到目录导航栏的激活跟随，思索了一阵子后没发现较好的解决办法，特此记录，代办代办（*有想法的小伙伴也可以留言啊，或者有更好的代码块折叠方案什么的*）。~~

&ensp;&emsp; *主题已原生支持。*

### 3.3 功能性测试


{% p center logo large, '<i class="fal fa-narwhal"></i>' %}
{% p center logo large, Volantis %}
{% p center small, A Wonderful Theme for Hexo %}

#### 3.3.1 Tab

{% tabs tab-id %}

<!-- tab 失落的宇宙 -->

&ensp;&emsp;《失落的宇宙》（日文名：ロスト・ユニバース，LOST UNIVERSE，大陆译名： 宇宙刑警）是神坂一原著的日本科幻小说。亦改编成漫画与动画。小说版共5卷，漫画版共4册。动画版1998年4月3日发行，总共26集。

<!-- endtab -->

<!-- tab 背景介绍 -->

&ensp;&emsp;在远古的时代，宇宙中存在着分别代表光明与黑暗的两个文明，正如光与影永远是相互矛盾的，两个文明也长年处于战争之中，代表黑暗的文明为了取得胜利，就制造了一种高科技的战舰，这种战舰拥有超强的作战能力及穿越时空的能力，而且，还拥有自己的思想。然而，**这种战舰却会把驾驶者作为自己的食粮**，以此来增强自己的力量。
&ensp;&emsp;这种黑色的战舰就像魔鬼一样，会把敌人彻底毁灭，但同时也会吞噬驾驶员的生命。而在同一时间，为了对抗敌人的黑色战舰，代表光明的文明也创造了一艘白色战舰与黑色战舰抗衡。后来，两个文明都原因不明地消失了，而那些战舰却依然存在在宇宙的某个角落里，新时代的人为他们起了个名字——遗失战舰（LostShips）

<!-- endtab -->

<!-- tab 豆瓣评价 -->

&ensp;&emsp;小时候在电视台看动画片《宇宙刑警》（失落的宇宙），结局是男主角去敌窝生死未卜，女主角回复了平静的生活等待他回来。电视里没放完就插了广告，最后一集看的这么多年一直耿耿于怀，我想知道他是否平安回来。昨天终于偶然间看到了最后一集，老牌怀旧的硬朗画风和人物设定，她在风和日丽的天气里晒洁白的被单，去他的墓前放一束花，突然看到他的宇宙飞船缓缓降落，这么多年的等待终于有了结果。于是我这么多年的等待也有了结果。
&ensp;&emsp;然而这是动画，所以就在美好的这一刻打住。现实中也许宇宙飞船上早已物是人非，躺着他的尸体残骸也说不一定。得不到和已失去，总是在二者中间的状态才是最好。 —— honey 

<!-- endtab -->

<!-- tab 相关图片 -->

{% image https://img.inkss.cn/szyink_com/typecho/2019/10/04/1570189514.jpg, 失落的宇宙 %}

<!-- endtab -->

{% endtabs %}

#### 3.3.2 Folding

{% folding 查看图片测试 %}

![](https://cdn.jsdelivr.net/gh/xaoxuu/cdn-wallpaper/abstract/41F215B9-261F-48B4-80B5-4E86E165259E.jpeg)

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

#### 3.3.3 Checkbox & Radio

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
