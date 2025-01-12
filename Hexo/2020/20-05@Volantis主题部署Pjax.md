---
title: Volantis 主题部署 Pjax
seo_title: Volantis 主题部署 Pjax
toc: true
indent: true
date: 2020/05/17 01:27
updated: 2020/06/02 14:18
tag:
  - Hexo
  - Pjax
  - Volantis
categories: 博客
description: 本篇文章记录了我对 Volantis 主题做 Pjax 兼容的种种，大抵算是种记录吧~
abbrlink: '76993423'
headimg: ../../img/article/20-05@Volantis主题部署Pjax/Hexo博客封面.png
---

<p class="p center logo large"><em>Volantis 主题部署 Pjax <sup>应用篇</sup></em></p>
<br>
<br>

{% p center, 前言：本篇定位于主题的各类处理上，理论篇见 <a href="/post/80b5f235/">Hexo 博客部署 Pjax 局部刷新</a>。 %}
{% p center, 不多废话，那么，让我们开始吧！ %}

## 一、主题分析

### 1.1 整体性分析

在对主题做兼容时，最废心力的莫过于处理导航栏、封面这块，理论篇里已经分析过了，Pjax 需要重载的区域为文章主体和侧边栏，而封面、导航栏、页脚是不需要进行重载的。其中麻烦程度以导航栏为最，绑定了大量的按钮、事件等等，此外考虑到移动端适配的缘故，导航栏事实上是有两个存在的，均需要对其处理。

而封面部分，早期我没有过于考虑去处理封面，原因是这样的：导航栏是嵌入在封面里的（代码层面），在设置为全屏封面下时，存在了全屏、半屏和无封三种情况，控制起来略有些麻烦。而且存在一个导航栏出现时机的问题，需要计算出浏览器在什么滚动高度下才放出导航栏显示，此外导航栏上还放着嵌套的菜单，这部分通过循环渲染出来，使用 hover 伪类控制其显示，偏偏这种情况 Javascript 又不好处理，在无封面到有封面切换时留下了已经激活的菜单小尾巴。

至于页脚，目前唯一的动态数据为访问量的 PV 显示，这部分是直接由 class 定位赋值的，无需过多烦恼，倒是原主题中各个页脚的引用是分散的，需要统一抽离出来。这里插一嘴侧边栏，侧边栏因为是可配置项，从栏目到显示都是可配置的，过于麻烦，还是放在了重载区域内处理吧。

### 1.2 插件分析

一个个来细数，大抵以下几种：搜索、评论、懒加载、图片弹出层、代码复制、背景幻灯片、音乐播放器、按钮涟漪动画、页面平滑滚动、公式、不蒜子计数、网址预加载等。

这其中，有些无需处理，有些简单的封装成函数重新调用一下即可，而有些则就需要特意分析处理了。除了以上几个插件外，主题里还有两个核心 JS 文件，一为 `app.js` ，另一为 `search.js`。这两个前者主要控制导航栏、按钮的激活等；后者则是用于搜索、渲染搜索结果查询等等。前者需要细致的处理，后者只需要修改下搜索结果列，点击搜索结果时，手动发送出一个 Pjax 请求，因为结果列是后期渲染的，没有在页面初始化时被 Pjax 监控到，需要手动发送跳转请求完成无刷新的跳转。

## 二、插件的处理

此部分，以几类特殊的插件作以说明，未提及者类似处理，相应文件主要位于 *_partial/scripts.ejs*。

### 2.1 函数封装与调用

如前文中所说，一些插件只需要封装成函数，在 Pjax 的 complete 事件中，重新调用即可；而另一些，单独的 JS 的文件引用，无额外函数设置的，只需要重新加载，以下简单介绍一番。

（1）Fancybox & lazyload 此两者，均为对图像部分的处理，用于弹窗放大显示和懒加载，其中对 Fancybox 的处理是将初始化函数封装为函数，加载完成时重新调用一下即可，相关代码：[fancybox.ejs](https://github.com/inkss/volantis/blob/master-theme/layout/_third-party/fancybox.ejs)（另：Fancybox 担任了图片描述信息的显示，已改为默认启用）。而 Lazyload 需要特殊处理，不过相关兼容已经解决，只需要确认自己的 *hexo-lazyload-image* 版本大于等于 *1.0.9* ，接着在全局配置文件下，为 lazyload 添加配置项：` isSPA: true`。

（2）不蒜子，用于文章/站点的计数，无各类配置项目，此类处理为在 JS 引用时加特殊标志 `data-pjax`，在 Pjax 的 complete 事件中，利用此标志将其移除，并重新加载，类似于：

{% codeblock lang:js mark:1,4 不蒜子 line_number:false  %}
<script defer src="<%- theme.plugins.busuanzi %>" data-pjax></script>
//....
$('script[data-pjax], .pjax-reload script').each(function ()
  $(this).parent().append($(this).remove());
});
{% endcodeblock %}

（3）Valine，对评论的处理就很有意思了，先谈谈 *scripts.ejs* 这个文件，与 *.js* 相比，它可以利用 Hexo 的函数引用主题配置或者页面中的 *Front-matter* 配置，这部分属性是在渲染阶段读取，并写入到 *html* 页面。而对于 Valine 来说，在本主题中，是允许不同页面自定义 placeholder 和 path 的，但是！这部分代码不在 Pjax 重加载区域内，也就是意味着丢失了这部分自定义值。自然而然，既然此处无法重载，那就放到能够重载到的位置。

这里我利用 ejs 的文件特征，建立了一个 [pjaxdata.ejs](https://github.com/inkss/volantis/blob/master-theme/layout/_third-party/pjaxdata.ejs)，将一些值藏（*display: none*）在这里，写入到 div 标签中，通过 ` $.trim($('#id').text()` 获取值。至于为什么不写成 `var xxx = <%=xxx%>` 的形式，那是因为在代码压缩时对于这类只声明不调用的变量，会被直接删掉的...

综上，对与动态的变量处理是这个样子的：

{% codeblock lang:js mark:13-14 pjax_valine() line_number:false  %}
function pjax_valine() {
  var valinePath = $.trim($('#valine-path').text()) === "none" ?
    window.location.pathname : $.trim($('#valine-path').text());
  var valinePlaceholder = $.trim($('#valine-placeholder').text()) === "none" ?
    "<%= theme.comments.valine.placeholder %>" : $.trim($('#valine-placeholder').text());
  var ALLPATH = '<%= theme.comments.valine.path %>';
  if(ALLPATH != '') valinePath = ALLPATH;

  var valine = new Valine();
  valine.init({
    el: '#valine_container',
    meta: meta,
    placeholder: valinePlaceholder,
    path: valinePath,
    //...
  )}
}
{% endcodeblock %}

（4）大部分插件都可以循着以上三个的思路完成兼容，更多内容可在 [**scripts.ejs**](https://github.com/inkss/volantis/blob/master-theme/layout/_partial/scripts.ejs) 查看。

### 2.2 Pjax 事件

说了第三方插件后，来谈谈 Pjax 本身吧，Pjax 在处理重载区域时，有一个要求，便是被重载部分必须为所有页面都存在（特指选择器），否则便会失败触发强制刷新。而在 Pjax 自带的四个事件中 `send` 、`complete` 、`success` 、`error` ，send 和 complete 可以用来处理一些函数的解绑与注册，此外还可以用来控制加载动画的显隐，error 可以在监听到失败时做个提醒呀之类的，success 用的不多，不是很需要。

这里附录一下本次兼容的 Pjax 函数内容吧：

{% folding blue, Pjax 函数 %}

{% codeblock lang:js pjax 函数 line_number:false  %}
var pjax;
document.addEventListener('DOMContentLoaded', function () {
  pjax = new Pjax({
    elements: 'a[href]:not([href^="#"]):not([href="javascript:void(0)"]):not([pjax-fancybox])',
    selectors: [
      "title",
      "#pjax-container"
    ],
    cacheBust: false,   // url 地址追加时间戳，用以避免浏览器缓存
    timeout: '5000'
  });
});

document.addEventListener('pjax:send', function () {
  window.stop(); // 相当于点击了浏览器的停止按钮
  window.subData = null; // 移除标题（用于一二级导航栏切换处）
  $.fancybox.close();    // 关闭弹窗
  $('.nav-main').find('.list-v').not('.menu-phone').css("display","none"); // 移除小尾巴
  $('.menu-phone.list-v').css("display","none"); // 移除小尾巴
  $('.l_header .switcher .s-search').removeClass('active'); // 关闭移动端激活的搜索框
  $('.l_header').removeClass('z_search-open'); // 关闭移动端激活的搜索框
  $('.wrapper').removeClass('sub'); // 跳转页面时关闭二级导航
  $(window).unbind('resize');    // 解绑
  $(window).unbind('scroll');    // 解绑
  $(window).unbind('click');     // 解绑
  $(document).unbind('scroll');  // 解绑
  NProgress.start();
});

document.addEventListener('pjax:complete', function () {
  // 关于百度统计对 SPA 页面的处理：
  // 方案一：百度统计>管理>单页应用设置中，打开开启按钮即可对SPA进行统计。 https://tongji.baidu.com/web/help/article?id=324
  // 方案二：取消注释下列代码。 https://tongji.baidu.com/web/help/article?id=235
  // <% if (config.baidu_analytics_key) { %>
  // _hmt.push(['_trackPageview', document.location.pathname]);
  // <% } %>

  // 关于谷歌统计对 SPA 页面的处理：
  // 当应用以动态方式加载内容并更新地址栏中的网址时，也应该更新通过 gtag.js 存储的网页网址。
  // https://developers.google.cn/analytics/devguides/collection/gtagjs/single-page-applications?hl=zh-cn
  <% if (config.google_analytics_key) { %>
    window.dataLayer = window.dataLayer || [];
    function gtag(){dataLayer.push(arguments);}
    gtag('config', '<%- config.google_analytics_key %>', {'page_path': document.location.pathname});
  <% } %>

  $('.nav-main').find('.list-v').not('.menu-phone').removeAttr("style",""); // 移除小尾巴的移除
  $('.menu-phone.list-v').removeAttr("style",""); // 移除小尾巴的移除
  $('script[data-pjax], .pjax-reload script').each(function () {
    $(this).parent().append($(this).remove());
  });

  try{
    pjax_fancybox();
    <% if (theme.plugins.scrollreveal && theme.plugins.scrollreveal.js) { %>
      pjax_scrollrebeal();
    <% } %>
    <% if (theme.plugins.clipboard && (theme.style.body.highlight.copy_btn == true)) { %>
      pjax_initCopyCode();
    <% } %>
    <% if (enableValine){ %>
      pjax_valine();
    <% } %>
    <% if (enableMiniValine){ %>
      pjax_minivaline();
    <% } %>
  } catch (e) {
    console.log(e);
  }
  NProgress.done();
});

document.addEventListener('pjax:error', function () {
  NProgress.done();
  window.location.href = event.triggerElement.href;
});
{% endcodeblock %}

{% endfolding %}

## 三、主题核心代码处理

主要为两个 js 文件，内容很翔实，改动量不小，慢慢更新系列~

### 3.1 搜索

在前面我就说了，搜索结果是后期渲染上的，所以没有被 Pjax 整成自己的样子（哈哈），所以那些结果标签点过去立马来个刷新跳转，故此部分便只需要对这点处理下就行啦。这里需要利用 Pjax 的 `pjax.refresh([el])` 函数：

Use this method to bind Pjax to children of a DOM element that didn't exist when Pjax was initialised e.g. content inserted dynamically by another library or script. If called with no arguments, Pjax will parse the entire document again to look for newly-inserted elements.

{% codeblock lang:js refresh([el]) line_number:false  %}
// Inside a callback or Promise that runs after new DOM content has been inserted
var newContent = document.querySelector(".new-content");

pjax.refresh(newContent);
{% endcodeblock %}

也就是重新加载 #u-search 区域，接着监听 send 事件，关闭搜索窗口：

{% codeblock lang:js Pjax 处理 line_number:false  %}
//...
html += "<script>try{pjax.refresh(document.querySelector('#u-search'))}catch(e){console.log(e)}</script>";
html += "<script>document.addEventListener('pjax:send',function(){$('#u-search').fadeOut(500);$('body').removeClass('modal-active')});</script>";
{% endcodeblock %}

### 3.2 导航栏

先放链接：[**app.js**](https://github.com/inkss/volantis/blob/master-theme/source/js/app.js)，看心情再决定补不补思路。

### 3.3 封面

是这样的，原主题对封面的处理是通过读取页面配置来控制渲染，大概三种不同渲染场景，而在 Pjax 处理时简化为两种场景，全局不开启封面和开启封面，别的就通过代码来控制样式啦。样式处理无非是控制显示、隐藏，但是呢，还需要控制导航栏出现时机，这部分在上一小节讲。

{% codeblock lang:js 封面控制 line_number:false  %}
<script>
  if("<%=frontMatterCover%>" == "none") {  // 移除封面
      document.getElementsByClassName('cover')[0].style.display = "none";  
      document.getElementsByClassName('l_body')[0].classList.add("nocover");
      document.getElementsByClassName('l_header','cover-wrapper')[0].classList.add("show"); 
  } else {
      if("<%=frontMatterCover%>" == "blog") {  // 半屏
          document.getElementsByClassName('cover')[0].classList.remove("full");
          document.getElementsByClassName('cover')[0].classList.add("half");
          document.getElementsByClassName('scroll-down')[0].style.display = "none";
      } else if("<%=frontMatterCover%>" == "docs") { // 全屏
          document.getElementsByClassName('cover')[0].classList.remove("half");
          document.getElementsByClassName('cover')[0].classList.add("full");
          document.getElementsByClassName('scroll-down')[0].style.display = "";
      }
      document.getElementsByClassName('cover')[0].style.display = "";
      document.getElementsByClassName('l_body','show')[0].classList.remove("nocover");
      document.getElementsByClassName('l_header','cover-wrapper')[0].classList.remove("show");
  }
</script>
{% endcodeblock %}

为了照顾显示效果，这部分处理是放在封面加载之后文章加载之前，好处是不会出现闪一下的效果（默认整个封面是隐藏的），坏处是此时 Jquery 没加载，得用原生 JS 来写。

## 四、后续

没有后续，完结撒花~
