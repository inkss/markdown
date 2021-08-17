---
title: Volantis 主题个性化修改合集
toc: true
indent: true
tag:
  - Hexo
  - Volantis
categories: 教程
date: '2021-08-05 09:20'
updated: '2021-08-17 00:00'
hideTitle: true
headimg: ../../img/article/Volantis主题个性化修改合集/main.gif
description: '记录一下 Volantis 主题的修改内容 ( •̀ ω •́ )✧'
music:
  enable: true
  server: tencent
  type: song
  id: 001RK9x72Cws4Q
abbrlink: 610620a9
---

本文记录了我对 Volantis 主题的大致修改内容，在正式开始前，请确认你的主题是通过 Git 克隆方式安装（也就是拥有独立的主题仓库），而非 NPM 安装^[指通过 `npm i hexo-theme-volantis` 命令安装的主题。]，你可以在 [此处](https://github.com/inkss/volantis) 找到我的个人主题仓库。

{% note quote, 基础环境：基于 Volantis v5.0 β %}
{% note warning, 本篇内容仅供参考，一切以实际呈现为准。 %}

{% folding cyan, 更新历史</span> %}
{% timeline %}

{% timenode 2021/08/17 %}

去掉部分模糊的描述，修改错别字等等。

{% endtimenode %}

{% timenode 2021/08/15  %}

大致完善了更改记录（虽然删除了部分预先打算写的内容）。

{% endtimenode %}

{% timenode 2021/08/05  %}

开坑文章：《Volantis 主题个性化修改合集》

{% endtimenode %}

{% endtimeline %}
{% endfolding %}

## 一、引言

修改主题最大的麻烦之处就是可能会与官方分支相差太大，更新主题时面临一堆的冲突文件，所以要尽可能的减少冲突文件的产生，你可以在以下链接找到有关冲突处理的办法：

{% link 如何正确地更新主题, https://github.com/volantis-x/hexo-theme-volantis/issues/459 %}

所谓主题修改无外乎改改样式，加加新功能，事实上 Volantis 已经抽出各种选项到配置文件了，如果只是修改*滚动条*、*导航栏*、*自定义字体*、*颜色*等等，都可以在配置文件中完成，所以建议先熟读文档 [主题配置](https://volantis.js.org/v5/theme-settings/) 一节，当然文档的维护不一定赶得上开发进度，一切以主题下的配置文件为主。

而若是新增主题没有的功能，确实得改主题了，只需新增 `js`、`css` 这种文件的可以参考文档的这节内容：[使用 Import 导入外部文件](https://volantis.js.org/v5/site-settings/#%E4%BD%BF%E7%94%A8-Import-%E5%AF%BC%E5%85%A5%E5%A4%96%E9%83%A8%E6%96%87%E4%BB%B6)。

> *Volantis 用户可以在不修改主题文件的情况下向 head 和 body 中添加各种标签。`meta` 和 `link` 对应 head 中的 <meta> 和 <link> 标签。`script` 可以在 body 末尾导入 js 代码。*

其余类型的另作它论，好了废话结束正文正式开始。

## 二、样式类

本段记录样式类的修改，为了避免冲突，个人样式类文件统一存放在 `/source/css/_szyink/` 目录中（*你可以修改文件夹名称为自己的*），同时需要在 `style.styl` 最下方引用它。

{% folding cyan, /source/css/style.styl %}
```styl 在最后一行导入自定义修改文件
// 自定义样式，文件夹名称可根据喜好修改
@import '_szyink/*'   
```
{% endfolding %}

### 2.1 样式覆盖

样式覆盖类，统一保存在 `/source/css/_szyink/cover.styl` 文件中。
#### 2.1.1 顶部导航栏铺满屏幕

导航栏铺满整个屏幕，只在大于 `2048px` 分辨率下出现，效果见本站。

{% folding cyan, 2K 分辨率以下铺满整个屏幕 %}
```styl
.l_header
  @media screen and (max-width: $device-2k)
    left: 0 !important
    border-radius: 0 !important
    max-width: 100% !important
```
{% endfolding %}

#### 2.1.2 移动端不显示卡片

调整移动端的显示，平铺展示，效果参考本站移动端下的显示。

{% folding cyan, 将卡片的边距去除  %}
```styl 
#safearea
  @media screen and (max-width: $device-mobile)
    margin: 0
    box-shadow: none
    .body-wrapper
      padding: 0
    #l_main
      article#comments,
      article#friends,
      article#post,
      article#page,
      article#arc      
        border-radius: 0
      article#cat,
      article#tag
        margin: 0
        border-radius: 0
      .post-wrapper
        margin: 0 0 16px
        .post
          border-radius: 0
    #l_side
      .widget.mobile
        border-radius: 0
```
{% endfolding %}

#### 2.1.3 禁止文本选中

在不想被选中的标签的 `class` 中添加 `not-select`，需要选中的添加 `allow-select`。

效果：<span class="allow-select">我可以被选中</span>，<span class="not-select">我不可以被选中</span>。

{% folding cyan, 禁用了部分类型的文字选中 %}
```styl 本站同时禁止了大部分元素的选中
.not-select,
figcaption,
footer,
section,
details summary,
.new-meta-box,
.nav-tabs
  -webkit-touch-callout none
  -webkit-user-select none
  -khtml-user-select none
  -moz-user-select none
  -ms-user-select none
  user-select none
  & p,
  & span
    cursor: $default,default
  &.default-cursor
    cursor: $default,default !important
.allow-select
  -webkit-touch-callout all
  -webkit-user-select all
  -khtml-user-select all
  -moz-user-select all
  -ms-user-select all
  user-select all
```
{% endfolding %}

#### 2.1.4 修改链接的下划线样式

效果参考：[枋柚梓的猫会发光](/)

{% folding cyan, 修改下划线的 hover 样式 %}
```styl
.article a:not([class]):not([data-fancybox]),
.content > a
  position: relative
  text-decoration: none
  &:before
    content: "";
    position: absolute;
    left: 0;
    bottom: -1px
    height: 1px
    width: 100%
    background: #ff5722
    transform: scale(0)
    transition: all 0.5s
  &:hover:before
    transform: scale(1)
```
{% endfolding %}

#### 2.1.5 修改标题的样式

样式借鉴了 Handsome 主题，效果参考本文二、三级标题。

{% folding cyan, 样式修订 HandSome H1 H2 H3 标题 %}
```styl
article#post
  & > h1,h2,h3
    font-weight: 700
    background: linear-gradient(to bottom,transparent 60%,rgba(189,202,219,.3) 0) no-repeat
    display: initial
    width: auto
    border-bottom: none
  & > h3
    font-weight: 500
    position: initial
```
{% endfolding %}

#### 2.1.6 封面标题居中

修改文章列表页^[展示文章列表的页面，例如首页、标签页和 `list` 类型的页面等。]的显示效果，效果参考：[ES6](/tags/ES6/)

{% folding cyan, 列表页的内容居中，内容居左且首行缩进 %}
```styl
// 列表页的内容居中
.post.post-v3.white-box
  text-align: center
  & a
    font-weight: 600 !important

// 内容居左且首行缩进
.post.post-v3.white-box  p
  text-align: left
  text-indent: 2em
```
{% endfolding %}

#### 2.1.7 卡片透明模式

效果参见本站，文章页^[Layout 为 post 类型的页面，例如放在 `_posts` 目录下的文章均为此类。]透明会影响阅读体验，所以文章下没加透明度的。

{% folding cyan, 卡片添加透明度 %}
```styl
// 添加透明度
#l_header
  background-color: rgba(255,255,255,0.9)
#l_main .post,
#l_side .widget
  background-color: rgba(255,255,255,0.9)
  box-shadow: 0 4px 10px -4px #ebedf0
// 文章不透明
#post.post
  background-color: var(--color-card)
```
{% endfolding %}

#### 2.1.8 文章页不显示头图

即头图只在列表页显示，进入文章页时不显示。

{% folding cyan, 文章页不显示头图 %}
```styl
#l_main .article .headimg-div
  display: none
```
{% endfolding %}

### 2.2 样式增强

在主题基础上新增的样式文件，实现更多的效果。

#### 2.2.1 自定义 Note 图标

此处特指主题的 `note` 和 `noteblock` 标签，如果需要更多类型的图标就需要自己添加了。新建 `note.styl` 文件，个人新增的图标都存在此处。

然后是找图标，我们需要图标的十六进制代码而不是名称，进入 [Fontawesome](https://fontawesome.com/v5.15/icons?d=gallery&p=2)，打开 **开发人员工具**，使用 {% kbd Ctrl %} + {% kbd Shift %} + {% kbd C %} 选取需要新增的图标，在 **样式** 栏，找到 `::before` 节点：

{% folding cyan, 其中 “\f368” 就是我们需要的代码 %}
```css
.fa-accessible-icon:before {
  content: "\f368";
}
```
{% endfolding %}

接着在 note.styl 一个个添加就好了，使用时依旧通过名称调用：

{% folding cyan, 自定义 Note %}
```styl
div.note
  &.alien-monster::before
    content: '\f8f6'
```
{% endfolding %}

{% note alien-monster blue, 效果见本条内容：note alien-monster blue  %}

#### 2.2.2 页脚响应式处理

所谓响应式处理主要是利用 CSS 的媒体查询，通过设定 `max-width` 和 `display`，可以达到当小于一定可视范围时主动隐藏某些元素，我们将其抽成可复用的样式类，这样在使用时只需要添加对应的类名即可，具体的尺寸根据实际情况选择^[几个关键的尺寸：500 以下为移动端、500-1024 为平板端，大于 1024 为桌面端。]。

{% folding cyan, .footerMax560 %}
```styl
.footerMax560
  @media screen and (max-width: 560px)
    display: none
```
{% endfolding %}

效果参考以下内容：

<div class="footer clearfix" style="display: block;text-align: center;font-size: 0.8rem;margin: 1rem 0;padding: 0;line-height: 1rem;border: 1px solid;border-radius: 6px;">
    <div><div class="footerMax560">博客内容遵循 <a target="_blank" rel="external nofollow noopener noreferrer" href="https://creativecommons.org/licenses/by-nc-sa/4.0/deed.zh" data-pjax-state="">「署名-非商业性使用-相同方式共享 4.0 国际 (CC BY-NC-SA 4.0)」</a></div></div>
    <div><div class="footerMax490">本站使用 <a href="https://github.com/volantis-x/hexo-theme-volantis" target="_blank" class="codename" rel="external nofollow noopener noreferrer" data-pjax-state="">Volantis</a> 作为主题 | 通过 <a href="https://hexo.io/zh-cn/" target="_blank" class="codename" rel="external nofollow noopener noreferrer" data-pjax-state="">Hexo</a> 渲染生成 | <span>由腾讯云 <a href="https://cloud.tencent.com/product/cdn" target="_blank" class="codename" rel="external nofollow noopener noreferrer" data-pjax-state="">CDN</a> 分发</span></div> </div>
    <div><div class="footerMax490">辽 ICP 备 <a href="https://beian.miit.gov.cn/" target="_blank" rel="external nofollow noopener noreferrer" style="cursor:wait;" data-pjax-state="">16006560</a> 号 | 辽公网安备 <a href="https://www.beian.gov.cn/" rel="external nofollow noopener noreferrer" target="_blank" style="cursor:wait;" data-pjax-state="">21021702000331</a> 号</div> </div>
    <div><div class="footerMax490">Copyright 2018 - 2021 szyink. All Rights Reserved</div></div>
    <div><a href="/" data-pjax-state="">枋柚梓的猫会发光</a></div>
    <div><i class="fad fa-spider-black-widow POMEGRANATE"></i></div>
</div>

#### 2.2.3 杂项修改

本小节记录一些不成体系的独立更改 {% emoji hanyan %} 。

{% folding cyan, 杂项修改合集 %}

{% emoji a %} ： {% bb 我可真是个小机灵鬼,  分栏只是为了缩短代码块的长度 %}

{% tabs otherCss  %}

<!-- tab 样式一 -->
```styl
// 设置code样式
code
  &:not([class])
    font-size: .8125rem
    padding: 4px
    font-weight: 600
    background: var(--color-codeblock)

// 代码块标题颜色
.highlight figcaption
  background: #ffeed2

// <kbd></kbd>
kbd
  background-color: #f2f2f2
  color: var(--color-p)
  padding: 3px 5px
  border-radius: 0.25em
  box-shadow: inset 0 -2px 0 hsl(240deg 1% 83%), 0 1px 1px rgba(0,0,0,.05)
  font: 11px SFMono-Regular,Consolas,Liberation Mono,Menlo,monospace
```
<!-- endtab -->

<!-- tab 样式二 -->
```styl
// 标题样式
h1.title
  margin: 10px
  text-align: center
  font-size: 2rem !important
  font-weight: 600
  font-family: $fontfamily-logo
h1.title.common_read_h1
  text-align: center !important
  font-size: 2.5rem !important

// noteblock 标题字号增大
.note strong
  font-size: 1.5rem
  font-family: 'sxls'

// 修改文章行间距
article > p
  line-height: 2 !important;
```
<!-- endtab -->

<!-- tab 样式三 -->
```styl
// 右键样式修改
ul.list-v.rightmenu
  box-shadow: 0 -2px 4px 0 rgba(0,0,0,.08), 0 4px 8px 0 rgba(0,0,0,.08), 0 8px 16px 0 rgba(0,0,0,.08)
  border-radius: 8px
  li
    a
      cursor: $pointer,pointer !important

// 标题大小
details summary
  font-size 1rem !important

// 缩短 Tab 内时间线标题高度
.tab-pane > .timeline > .h2
  padding-top: 0

// 强制评论按钮字体粗细
#s-comment
  font-weight: 300 !important
```
<!-- endtab -->

{% endtabs %}
{% endfolding %}

## 三、功能类

本小结记录对主题的新增能力，在主题基础上的增强能力。

### 3.1 添加阅读模式

阅读模式目前挂靠在右键模块内，只能通过自定义右键控制，配色参考了 **Handsome** 主题，实现思路则是参考了 **简悦** 的聚焦模式，利用切换样式类、控制层级实现阅读模式。

<p class="article">效果参考：
<span class="btn" id="initRight" style="text-indent: 0"><a class="button" href="javascript:;">阅读模式</a></span>
</p>

<script type="text/javascript">
  document.getElementById('initRight').addEventListener("click", () => {
    RightMenu.readingModel();
  });
</script>

#### 3.1.1 添加样式文件

在 `/source/css/_szyink/` 下新建 `reading.styl` 文件，写入以下内容：

{% folding cyan, reading.styl %}
```styl 阅读模式
.common_read
  z-index: auto !important
  opacity: 1 !important
  overflow: visible !important
  transform: none !important
  animation: none !important
  position: relative !important

.body-wrapper.common_read
  display: block

#safearea.common_read
  padding-bottom: 16px
  @media screen and (max-width: 900px)
    padding: 0
    margin: 0

#l_body.common_read
  z-index: 2147483646 !important;

.read_cover
  min-height: 10px !important
  @media screen and (max-width: 900px)
    min-height: 0 !important

.common_read_bkg
  background-color: #e0d8c8 !important
  opacity: 1 !important
  display: block !important
  position: fixed !important
  top: 0 !important
  left: 0 !important
  right: 0 !important
  bottom: 0 !important
  z-index: 2147483645 !important
  transition: opacity 1s cubic-bezier(.23,1,.32,1) 0ms !important

.common_read_hide
  opacity: 0 !important  
  z-index: -2147483645 !important  

.common_read_main
  width: 840px !important;
  padding: 0 !important;
  margin: 0 auto;
  float: initial !important;
  @media screen and (max-width: 900px)
    width: auto !important;

.post_read
  background-color: #f8f1e2 !important
  z-index: 2147483646 !important
  overflow: visible !important
  font-size: 1.15rem !important
  border-radius: 0 !important;
  box-shadow: 0 6px 12px 3px #00000033
```
{% endfolding %}

阅读模式有部分配色，需要在暗黑模式下修改，在 `source/css/_plugins/dark.styl` 文件内添加：

{% folding cyan, 暗色系配色 %}
```styl 此部分配色可根据你网站的实际情况进行修改
#read_bkg
  background: #21252b !important
.post_read
  background-color: #282c34 !important
```
{% endfolding %}

#### 3.1.2 添加元素代码

在 `layout/_partial/rightmenu.ejs` 文件中新加对右键选项的判断，使用时只需在配置文件的 `rightmenu.layout` 中添加 `reading` 即可。

{% folding cyan, 新建一个对 reading 的判断 %}
{% codeblock 添加高亮行内容 lang:ejs mark:8-13 %}
//...
<% } else if (item == 'print') { %>
  <li class='option menuOption-Content'>
    <span class='vlts-menu opt fix-cursor-default' id='printHtml'>
      <i class='<%= theme.rightmenu.print.icon %> fa-fw '></i> <%- trim(theme.rightmenu.print.name) %>
    </span>
  </li>  
<% } else if (item == 'reading') { %>
  <li class='option menuOption-Content'>
    <span class='vlts-menu opt fix-cursor-default' id='readingModel'>
      <i class='<%= theme.rightmenu.reading.icon %> fa-fw '></i> <%- trim(theme.rightmenu.reading.name) %>
    </span>
  </li>  
<% } else if (item == 'music' && theme.plugins.aplayer.enable == true) { %>
  <div id="menuMusic">
//...
{% endcodeblock %}
{% endfolding %}

另外目前开发版的右键已经移除了对 Jquery 的依赖，但是目前使用的右键依旧需要 Jquery 的支持，所以还需要修改 `rightmenu.js` 的导入方式。

{% folding cyan, 引入 Jquery 资源 %}
```
volantis.import.jQuery().then(()=>{
  volantis.js('<%- theme.cdn.map.js.rightMenu %>') // 阅读模式依赖 JQ
})
```
{% endfolding %}

#### 3.1.3 添加事件代码

在 `source/js/rightMenu.js` 处添它的逻辑处理部分。

{% folding cyan, 添加事件代码 %}
{% tabs rightMenu  %}
<!-- tab 选取阅读模式 -->

*可以添加在 `const urlRegx = ` 下方*

```js 在前面的选择器部分新增阅读模式
const _readingModel = document.getElementById('readingModel'),
  _readBkg = document.getElementById('read_bkg');
```
<!-- endtab -->

<!-- tab 初始化加载阅读模式 -->

*对 `fn.init` 的内容进行修改*

```js 在 fn.init() 函数中添加阅读模式的底层背景
fn.init = () => {
  fn.visible(_menuMusic, false);
  fn.visible(_menuOption, false);
  // 右键支持注销和新建，以防万一
  if (_readBkg) _readBkg.parentNode.removeChild(_readBkg);  

  const readBkg = document.createElement("div");
  readBkg.className = "common_read_bkg common_read_hide";
  readBkg.id = "read_bkg";
  window.document.body.appendChild(readBkg);
}
```
<!-- endtab -->

<!-- tab 逻辑处理 -->

*全局搜索 `if (!!_printArticle) {` 定位修改位置*

```js 阅读模式与打印逻辑相似，只在文章页下展示，修改如下
if (!!_printArticle) {
  fn.visible(_printHtml);
  fn.visible(_readingModel);

  _printHtml.onclick = () => {
    if (window.location.pathname === pathName) {
      volantis.question('', '是否打印当前页面？<br><em style="font-size: 80%">建议打印时勾选背景图形</em><br>', () => {
        fn.printHtml();
      })
    } else {
      fn.hideMenu();
    }
  }

  _readingModel.onclick = () => {
    if (window.location.pathname === pathName) {
      fn.readingModel();
    } else {
      fn.readingModel();
    }
  }
} else {
  fn.visible(_printHtml, false);
  fn.visible(_readingModel, false);
}
```
<!-- endtab -->

<!-- tab 调用函数 -->

*可以放在 `fn.printHtml = () => {` **函数体** 的下方*

```js 阅读模式
fn.readingModel = () => {
  if (typeof ScrollReveal === 'function') ScrollReveal().clean('#comments');
  $('#l_header').fadeToggle();
  $('footer').fadeToggle();
  $('#s-top').fadeToggle();
  $('.article-meta#bottom').fadeToggle();
  $('.prev-next').fadeToggle();
  $('.widget').fadeToggle();
  $('#comments').fadeToggle();
  $('#l_main').toggleClass('common_read common_read_main');
  $('#l_body').toggleClass('common_read');
  $('#safearea').toggleClass('common_read');
  $('#pjax-container').toggleClass('common_read');
  $('#read_bkg').toggleClass('common_read_hide');
  $('h1').toggleClass('common_read_h1');
  $('#post').toggleClass('post_read');
  $('#l_cover').toggleClass('read_cover');
  $('.widget.toc-wrapper').toggleClass('post_read');
  if ($('.cus-article-bkg')) {
    $('.cus-article-bkg').toggle();
  } else {
    $('#BKG').toggle();
  }
  volantis.isReadModel = volantis.isReadModel === undefined ? true : !volantis.isReadModel;
  if (volantis.isReadModel) {
    volantis.message('系统提示', '阅读模式已开启，您可以点击屏幕空白处退出。', 'fal fa-book-reader light-blue', 5000);
    $('#l_body').off('click.rightMenu').on('click.rightMenu', (event) => {
      if ($(event.target).hasClass('common_read')) {
        fn.readingModel();
      }
    })
  } else {
    $('#l_body').off('click.rightMenu');
    $('#post').off('click.rightMenu');
  }
}
```
<!-- endtab -->

<!-- tab 关闭阅读模式 -->

*添加到 `if (volantis.rightMenu.defaultStyles === true) {` 上方*

{% codeblock 打印时关闭可能开启的阅读模式 lang:js mark:2 %}
fn.printHtml = () => {
  if (volantis.isReadModel) fn.readingModel();
  if (volantis.rightMenu.defaultStyles === true) {
{% endcodeblock %}

<!-- endtab -->
{% endtabs %}
{% endfolding %}

### 3.2 阅读更多样式更改

为列表页的阅读更多添加一个动画，不过按照 @xaoxuu 的意见，`v5.0` 未来会去掉「阅读全文」「去原站阅读」等废话，整个卡片是一整个按钮，分类、标签等不可点击，看情况选择啦。

效果参考：<a style="text-indent: 0;margin: 0 10px;" class="link-fx-1 color-contrast-higher" href="#3-2-%E9%98%85%E8%AF%BB%E6%9B%B4%E5%A4%9A%E6%A0%B7%E5%BC%8F%E6%9B%B4%E6%94%B9">
  <span><i class="fal fa-books fa-fw" aria-hidden="true" title="Read Me"></i>阅读更多</span>
  <svg class="icon" viewBox="0 0 32 32" aria-hidden="true"><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"><circle cx="16" cy="16" r="15.5"></circle><line x1="10" y1="18" x2="16" y2="12"></line><line x1="16" y1="12" x2="22" y2="18"></line></g></svg>
</a>

#### 3.2.1 添加样式文件

{% note warning, 此后除特殊说明外，所有的 *添加样式文件* 、 *样式新增* 均放在自定义文件夹 `source/css/_szyink` 中，具体放在那个文件中自拟。 %}


{% folding cyan, 阅读更多的样式 %}
```styl
//===========================
// link-effects
// 阅读更多的样式
//===========================
.link-fx-1 {
  position: relative;
  display: inline-flex;
  align-items: center;
  height: auto; // !important - set fixed height
  right: 10px;
  padding: 0 6px;
  text-decoration: none;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale

  .icon {
    width: 1.5rem;
    position: absolute;
    right: 0;
    bottom: 1.5px;
    -webkit-transform: translateX(100%) rotate(90deg);
    transform: translateX(100%) rotate(90deg);

    circle {
      stroke-dasharray: 100;
      stroke-dashoffset: 100;
      transition: stroke-dashoffset .2s;
    }

    line {
      transition: -webkit-transform .4s;
      transition: transform .4s;
      transition: transform .4s,-webkit-transform .4s;
      -webkit-transform-origin: 13px 15px;
      transform-origin: 13px 15px
    }

    line:last-child {
      -webkit-transform-origin: 19px 15px;
      transform-origin: 19px 15px
    }
  }

  &::before {
    content: '';
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 1px;
    background-color: currentColor;
    -webkit-transform-origin: right center;
    transform-origin: right center;
    transition: -webkit-transform .2s .1s;
    transition: transform .2s .1s;
    transition: transform .2s .1s,-webkit-transform .2s .1s
  }

  &:hover {
    .icon {
      circle {
        stroke-dashoffset: 200;
        transition: stroke-dashoffset .2s .1s;
      }

      line {
        -webkit-transform: rotate(-180deg);
        transform: rotate(-180deg)
      }

      line:last-child {
        -webkit-transform: rotate(180deg);
        transform: rotate(180deg)
      }
    }

    &::before {
      -webkit-transform: translateX(17px) scaleX(0);
      transform: translateX(17px) scaleX(0);
      transition: -webkit-transform .2s;
      transition: transform .2s;
      transition: transform .2s,-webkit-transform .2s
    }
  }
}
```
{% endfolding %}

#### 3.2.2 添加元素代码

这部分需要修改 `layout/_partial/post.ejs` 部分：

{% folding cyan, 更换 if (showReadmore) 内的代码 %}
```ejs
<a class="link-fx-1 color-contrast-higher" href="<%- url_for(post.link || post.path) %>">
  <span><%- post.link ? __('post.readoriginal') : __('post.readmore') %></span>
  <svg class="icon" viewBox="0 0 32 32" aria-hidden="true"><g fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round"><circle cx="16" cy="16" r="15.5"/><line x1="10" y1="18" x2="16" y2="12"/><line x1="16" y1="12" x2="22" y2="18" /></g></svg>
</a>
```
{% endfolding %}

文字部分修改 `languages/zh-CN.yml` 的 `post.readmore` 的内容。
### 3.3 文章页自定义背景

允许为文章添加背景图片，通过 `page.background` 使用，效果参见：[自动化博客部署](/blog/42987b6b/)。

修改比较简单，不分类说了，需要修改 `article.ejs` 文件，并添加对应样式：

{% folding cyan, /layout/_partial/article.ejs %}
```ejs  在最后添加如下内容
<% if(page.background) { %>
  <div class="cus-article-bkg" style="background-image: url(<%- page.background %>)"></div>
<% } %>
```

```styl 在样式文件中添加
.cus-article-bkg 
  background-size: cover 
  background-position: center 
  position: fixed 
  top: 0 
  bottom: 0 
  right: 0 
  left: 0 
  z-index: -10 
```
{% endfolding %}

### 3.4 文章页首行缩进开关

文字类的文章最好还是开启首行缩进，但也不是所有文章都适合首行缩进，此处是做了一个开关，通过 `page.indent` 控制是否首行缩进。例如本篇文章是首行缩进的，而 [Linux Shell 设置 Proxy](/blog/f44c3b52/) 这篇文章是没有首行缩进。

修改起来也是非常简单，同样是修改 `article.ejs`，在最前面 article 的 `class` 中添加对应样式：

{% folding cyan, 添加 自定义样式 cus-indent %}
```ejs 
<article class="<%- page.indent == true ? 'cus-indent' : '' %> article post white-box reveal md <%- theme.custom_css.body.effect.join(' ') %> article-type-<%= post.layout %>" id="<%= post.layout %>" itemscope itemprop="blogPost">
```
{% endfolding %}

接着就只需要添加这个样式的实现：

{% folding cyan, 在样式文件中添加 %}
```ejs 
#l_main > article.cus-indent > p
  text-indent: 2em
  &.center
    text-indent: initial
  i
    text-indent: initial
```
{% endfolding %}

### 3.5 列表页文章标题开关

如你所见，如果列表页的头图已经包含了文章标题，再显示标题有些多余了，此处通过 `page.hideTitle` 控制是否显示文章标题，这里的修改也是非常简单，更改 `post.ejs` 的判断即可：

{% folding cyan, /layout/_partial/post.ejs %}
```ejs 在判断区域中新增一个判断即可
if (post.hideTitle && post.hideTitle === true) {
  showTitle = false;
}
```
{% endfolding %}

### 3.6 网站自定义右键开关

在写右键的时候，其实是提供了右键的注册和注销函数，所以可以实现用户侧在使用过程中主动关闭对右键事件的监听。使用起来就很灵活了，可以用变量记录开关状态，也可以将状态写入到 `LocalStorage` 中持久化。

|               函数                |      参数      | 备注                     |
| :-------------------------------  | :------------: | ------------------------ |
| `RightMenu.destroy(notic: boolean)` | notic: boolean | 参数为 `true` 时弹出提示 |
|  `RightMenu.init(notic: boolean)`   | notic: boolean | 参数为 `true` 时弹出提示 |

效果：

<div style="min-height: 30px;font-family: 'sxls';font-size: 1.5rem;font-weight: 600;">
  <div id="destroyRightContent" style="display: none">
    <p class="article center" style="text-align: center;">关闭自定义右键：<span id="destroyRightMenu" class="btn">
        <a class="button" href="javascript:;" title="注销右键">注销右键</a></span>
    </p>
  </div>
  <p id="initRightContent" class="article center" style="text-align: center;display: none">激活自定义右键：<span
      id="initRightMenu" class="btn"><a class="button" href="javascript:;" title="激活右键">激活右键</a></span></p>
</div>

### 3.7 整合文章归档模板

把归档和标签整合在一起，没必要整那么多分类。以归档为主，将标签页内容输出到归档列表的前面，效果：[文章归档](/navigation/archives/)。

{% folding cyan, /layout/archive.ejs %}
```ejs 在 article id 上方添加如下内容
<div class='post-wrapper'>
  <article id="tag" class="post article white-box reveal <%- theme.custom_css.body.effect.join(' ') %>">
    <h2>所有标签</h2>
    <div class="all-tags">
    <% let tc = theme.sidebar.widget_library.tagcloud; %>
    <%- list_tags({}) %>
    </div>
    <br>
    <%- page.content %>
  </article>
</div>
```
{% endfolding %}

## 四、通用类

此部分与主题关联不大，属于可以作为通用使用的新增功能。

### 4.1 引入 Iconfont 图标

引用阿里巴巴矢量图标库的图标，按照它的说明，推荐的是 symbol 引用^[这种用法其实是做了一个 svg 的集合，支持多色图标了，不再受单色限制。]。

#### 4.1.1 引用资源文件和样式

将你下载的 JS 文件拷贝到 `/source/js/` 目录中，接着修改 `cdnCtrl.ejs` 引用文件。

{% folding cyan, /layout/_partial/scripts/_ctrl/cdnCtrl.ejs %}
```ejs 在最下面引用文件，iconfont 为你的 JS 文件名称
theme.cdn.addJS("iconfont")
```
{% endfolding %}

接着添加它的公共样式文件：

{% folding cyan, 此样式文件建议导入到 first.styl 中 %}
```styl
.iconfont
  width: 1em;
  height: 1em;
  vertical-align: -0.15em;
  fill: currentColor
  overflow: hidden
  &.emoji
    width: 1.5em
    height: 1.5em
    vertical-align: -0.3em
```
{% endfolding %}

#### 4.1.2 制作自定义标签

为了方便使用对应的图标，将其制作成自定义标签，新建 `icon.js` 文件并写入如下内容，使用时只需要类似 `{% emoji aixin %}` 这样写即可，aixin 为图标/表情的名称。

{% folding cyan, /scripts/tags/icon.js %}
```js
'use strict';

function emoji(args) {
  args = args.join(' ').split(',');
  return `<svg class="iconfont emoji" aria-hidden="true"><use xlink:href="#icon-${args}"></use></svg>`;
}

hexo.extend.tag.register('emoji', emoji)
```
{% endfolding %}

效果：{% emoji aixin %} {% emoji daxiao %} {% emoji fankun %} {% emoji xieyan %} {% emoji shengqi %} {% emoji huaixiao %} {% emoji biti %}

### 4.2 添加一种时间线样式

样式参考了 Layui 的时间线，移植到主题里，效果如下：

{% timelines '一个自制时间线（标题可选）' %}

{% timenodes fal fa-bat %} 2021/13/32 巴啦啦小魔仙。{% endtimenodes %}
{% timenodes fal fa-glass-cheers %} 2021/13/16 好好学习，天天向上。 {% endtimenodes %}
{% timenodes fal fa-genderless %} 2021/13/15 风声雨声读书声声声入耳，国事家事天下事事事关心。 {% endtimenodes %}
{% timenodes fal fa-narwhal %} 2021/13/10 楼主以屎，有事烧纸。 {% endtimenodes %}
{% timenodes fal fa-genderless %} 2021/13/08 那么就可以有星期八了。 {% endtimenodes %}
{% timenodes fal fa-genderless %} 2021/13/01 是的没错，这里是 13 月。 {% endtimenodes %}
{% timenodes fal fa-fan fa-spin %}这里是时间线的起点~{% endtimenodes %}

{% endtimelines %}

#### 4.2.1 添加样式文件

参考 [timeline.styl](https://gitea.szyink.com/szyink/Hexo-Blog/src/branch/main/themes/volantis/source/css/_szyink/timeline.styl) 中的内容，引入到主题中即可。

#### 4.2.2 添加渲染器

为了方便使用，将其做成了一个标签。新建 `imelines.js` 文件到 `/scripts/tags/` 目录中，将 [timelines.js](https://gitea.szyink.com/szyink/Hexo-Blog/src/branch/main/themes/volantis/scripts/tags/timelines.js) 的内容复制到文件中，使用方式：

{% folding cyan, 一个自制时间线 %}
```md
{% timelines '一个自制时间线（标题可选）' %}

{% timenodes fal fa-bat %} 2021/13/32 巴啦啦小魔仙。{% endtimenodes %}
{% timenodes fal fa-glass-cheers %} 2021/13/16 好好学习，天天向上。 {% endtimenodes %}
{% timenodes fal fa-genderless %} 2021/13/15 风声雨声读书声声声入耳，国事家事天下事事事关心。 {% endtimenodes %}
{% timenodes fal fa-narwhal %} 2021/13/10 楼主以屎，有事烧纸。 {% endtimenodes %}
{% timenodes fal fa-genderless %} 2021/13/08 那么就可以有星期八了。 {% endtimenodes %}
{% timenodes fal fa-genderless %} 2021/13/01 是的没错，这里是 13 月。 {% endtimenodes %}
{% timenodes fal fa-fan fa-spin %}这里是时间线的起点~{% endtimenodes %}

{% endtimelines %}
```
{% endfolding %}

### 4.3 添加 Github 暗黑模式动画

从 Github 扒了一个小猫猫的动画，放在了导航栏，在暗黑模式切换时触发，你可以右键点击 **「暗黑模式」** 查看。

#### 4.3.1 添加样式文件

参考 [profile.styl](https://gitea.szyink.com/szyink/Hexo-Blog/src/branch/main/themes/volantis/source/css/_szyink/profile.styl) 中的内容，引入到主题中即可。

#### 4.3.2 主题文件修改

接着修改主题的导航栏，找到 `/layout/_partial/header.ejs` 文件，在 `<a class="title flat-box">...</a>` 的后面添加小猫猫的图标，另外为这个标签添加一个 ID 选择器。

{% folding cyan, /layout/_partial/header.ejs %}
```ejs 添加选择器和 SVG 图标
<a id="desktopNavTitle" class="title flat-box" target="_self" href='<%- url_for("/") %>'>
//....
</a>

<a id="githubEmoji" class="title flat-box" style="display: none;" target="_self" href='<%- url_for("/") %>'>
  <svg class="profile-color-modes" height="45" viewBox="0 0 106 60" fill="none" stroke-width="3" stroke-linecap="round" stroke-linejoin="round" xmlns="http://www.w3.org/2000/svg"><g class="profile-color-modes-illu-group profile-color-modes-illu-red"><path d="M37.5 58.5V57.5C37.5 49.768 43.768 43.5 51.5 43.5V43.5C59.232 43.5 65.5 49.768 65.5 57.5V58.5"></path></g><g class="profile-color-modes-illu-group profile-color-modes-illu-orange"><path d="M104.07 58.5C103.401 55.092 97.7635 54.3869 95.5375 57.489C97.4039 54.6411 99.7685 48.8845 94.6889 46.6592C89.4817 44.378 86.1428 50.1604 85.3786 54.1158C85.9519 50.4768 83.7226 43.294 78.219 44.6737C72.7154 46.0534 72.7793 51.3754 74.4992 55.489C74.169 54.7601 72.4917 53.3567 70.5 52.8196"></path></g><g class="profile-color-modes-illu-group profile-color-modes-illu-purple"><path d="M5.51109 58.5V52.5C5.51109 41.4543 14.4654 32.5 25.5111 32.5C31.4845 32.5 36.8464 35.1188 40.5111 39.2709C40.7212 39.5089 40.9258 39.7521 41.1245 40"></path><path d="M27.511 49.5C29.6777 49.5 28.911 49.5 32.511 49.5"></path><path d="M27.511 56.5C29.6776 56.5 26.911 56.5 30.511 56.5"></path></g><g class="profile-color-modes-illu-group profile-color-modes-illu-green"><circle cx="5.5" cy="12.5" r="4"></circle><circle cx="18.5" cy="5.5" r="4"></circle><path d="M18.5 9.5L18.5 27.5"></path><path d="M18.5 23.5C6 23.5 5.5 23.6064 5.5 16.5"></path></g><g class="profile-color-modes-illu-group profile-color-modes-illu-blue"><g class="profile-color-modes-illu-frame"><path d="M40.6983 31.5C40.5387 29.6246 40.6456 28.0199 41.1762 27.2317C42.9939 24.5312 49.7417 26.6027 52.5428 30.2409C54.2551 29.8552 56.0796 29.6619 57.9731 29.6619C59.8169 29.6619 61.5953 29.8452 63.2682 30.211C66.0833 26.5913 72.799 24.5386 74.6117 27.2317C75.6839 28.8246 75.0259 33.7525 73.9345 37.5094C74.2013 37.9848 74.4422 38.4817 74.6555 39"></path></g><g class="profile-color-modes-illu-frame"><path d="M41.508 31.5C41.6336 31.2259 41.7672 30.9582 41.9085 30.6968C40.7845 26.9182 40.086 21.8512 41.1762 20.2317C42.9939 17.5312 49.7417 19.6027 52.5428 23.2409C54.2551 22.8552 56.0796 22.6619 57.9731 22.6619C59.8169 22.6619 61.5953 22.8452 63.2682 23.211C66.0833 19.5913 72.799 17.5386 74.6117 20.2317C75.6839 21.8246 75.0259 26.7525 73.9345 30.5094C75.1352 32.6488 75.811 35.2229 75.811 38.2283C75.811 38.49 75.8058 38.7472 75.7957 39"></path><path d="M49.4996 33V35.6757"></path><path d="M67.3375 33V35.6757"></path></g><g class="profile-color-modes-illu-frame"><path d="M41.508 31.5C41.6336 31.2259 41.7672 30.9582 41.9085 30.6968C40.7845 26.9182 40.086 21.8512 41.1762 20.2317C42.9939 17.5312 49.7417 19.6027 52.5428 23.2409C54.2551 22.8552 56.0796 22.6619 57.9731 22.6619C59.8169 22.6619 61.5953 22.8452 63.2682 23.211C66.0833 19.5913 72.799 17.5386 74.6117 20.2317C75.6839 21.8246 75.0259 26.7525 73.9345 30.5094C75.1352 32.6488 75.811 35.2229 75.811 38.2283C75.811 38.49 75.8058 38.7472 75.7957 39"></path></g><g class="profile-color-modes-illu-frame"><path d="M41.508 31.5C41.6336 31.2259 41.7672 30.9582 41.9085 30.6968C40.7845 26.9182 40.086 21.8512 41.1762 20.2317C42.9939 17.5312 49.7417 19.6027 52.5428 23.2409C54.2551 22.8552 56.0796 22.6619 57.9731 22.6619C59.8169 22.6619 61.5953 22.8452 63.2682 23.211C66.0833 19.5913 72.799 17.5386 74.6117 20.2317C75.6839 21.8246 75.0259 26.7525 73.9345 30.5094C75.1352 32.6488 75.811 35.2229 75.811 38.2283C75.811 38.49 75.8058 38.7472 75.7957 39"></path><path d="M49.4996 33V35.6757"></path><path d="M67.3375 33V35.6757"></path></g><g class="profile-color-modes-illu-frame"><path d="M41.508 31.5C41.6336 31.2259 41.7672 30.9582 41.9085 30.6968C40.7845 26.9182 40.086 21.8512 41.1762 20.2317C42.9939 17.5312 49.7417 19.6027 52.5428 23.2409C54.2551 22.8552 56.0796 22.6619 57.9731 22.6619C59.8169 22.6619 61.5953 22.8452 63.2682 23.211C66.0833 19.5913 72.799 17.5386 74.6117 20.2317C75.6839 21.8246 75.0259 26.7525 73.9345 30.5094C75.1352 32.6488 75.811 35.2229 75.811 38.2283C75.811 38.49 75.8058 38.7472 75.7957 39"></path></g><g class="profile-color-modes-illu-frame"><path d="M41.508 31.5C41.6336 31.2259 41.7672 30.9582 41.9085 30.6968C40.7845 26.9182 40.086 21.8512 41.1762 20.2317C42.9939 17.5312 49.7417 19.6027 52.5428 23.2409C54.2551 22.8552 56.0796 22.6619 57.9731 22.6619C59.8169 22.6619 61.5953 22.8452 63.2682 23.211C66.0833 19.5913 72.799 17.5386 74.6117 20.2317C75.6839 21.8246 75.0259 26.7525 73.9345 30.5094C75.1352 32.6488 75.811 35.2229 75.811 38.2283C75.811 38.49 75.8058 38.7472 75.7957 39"></path><path d="M49.4996 33V35.6757"></path><path d="M67.3375 33V35.6757"></path></g><g class="profile-color-modes-illu-frame"><path d="M73.4999 40.2236C74.9709 38.2049 75.8108 35.5791 75.8108 32.2283C75.8108 29.2229 75.1351 26.6488 73.9344 24.5094C75.0258 20.7525 75.6838 15.8246 74.6116 14.2317C72.7989 11.5386 66.0832 13.5913 63.2681 17.211C61.5952 16.8452 59.8167 16.6619 57.973 16.6619C56.0795 16.6619 54.2549 16.8552 52.5427 17.2409C49.7416 13.6027 42.9938 11.5312 41.176 14.2317C40.0859 15.8512 40.7843 20.9182 41.9084 24.6968C41.003 26.3716 40.4146 28.3065 40.2129 30.5"></path><path d="M82.9458 30.5471L76.8413 31.657"></path><path d="M76.2867 34.4319L81.8362 37.7616"></path><path d="M49.4995 27.8242V30.4999"></path><path d="M67.3374 27.8242V30.4998"></path></g><g class="profile-color-modes-illu-frame"><path d="M45.3697 34.2658C41.8877 32.1376 39.7113 28.6222 39.7113 23.2283C39.7113 20.3101 40.3483 17.7986 41.4845 15.6968C40.3605 11.9182 39.662 6.85125 40.7522 5.23168C42.5699 2.53117 49.3177 4.6027 52.1188 8.24095C53.831 7.85521 55.6556 7.66186 57.5491 7.66186C59.3929 7.66186 61.1713 7.84519 62.8442 8.21095C65.6593 4.59134 72.375 2.5386 74.1877 5.23168C75.2599 6.82461 74.6019 11.7525 73.5105 15.5094C74.7112 17.6488 75.3869 20.2229 75.3869 23.2283C75.3869 28.6222 73.2105 32.1376 69.7285 34.2658C70.8603 35.5363 72.6057 38.3556 73.3076 40"></path><path d="M49.0747 19.8242V22.4999"></path><path d="M54.0991 28C54.6651 29.0893 55.7863 30.0812 57.9929 30.0812C59.0642 30.0812 59.8797 29.8461 60.5 29.4788"></path><path d="M66.9126 19.8242V22.4999"></path><path d="M33.2533 20.0237L39.0723 22.1767"></path><path d="M39.1369 25.0058L33.0935 27.3212"></path><path d="M81.8442 19.022L76.0252 21.1751"></path><path d="M75.961 24.0041L82.0045 26.3196"></path></g><g class="profile-color-modes-illu-frame"><path d="M73.4999 40.2236C74.9709 38.2049 75.8108 35.5791 75.8108 32.2283C75.8108 29.2229 75.1351 26.6488 73.9344 24.5094C75.0258 20.7525 75.6838 15.8246 74.6116 14.2317C72.7989 11.5386 66.0832 13.5913 63.2681 17.211C61.5952 16.8452 59.8167 16.6619 57.973 16.6619C56.0795 16.6619 54.2549 16.8552 52.5427 17.2409C49.7416 13.6027 42.9938 11.5312 41.176 14.2317C40.0859 15.8512 40.7843 20.9182 41.9084 24.6968C41.003 26.3716 40.4146 28.3065 40.2129 30.5"></path><path d="M82.9458 30.5471L76.8413 31.657"></path><path d="M76.2867 34.4319L81.8362 37.7616"></path><path d="M49.4995 27.8242V30.4999"></path><path d="M67.3374 27.8242V30.4998"></path></g><g class="profile-color-modes-illu-frame"><path d="M40.6983 31.5C40.5387 29.6246 40.6456 28.0199 41.1762 27.2317C42.9939 24.5312 49.7417 26.6027 52.5428 30.2409C54.2551 29.8552 56.0796 29.6619 57.9731 29.6619C59.8169 29.6619 61.5953 29.8452 63.2682 30.211C66.0833 26.5913 72.799 24.5386 74.6117 27.2317C75.6839 28.8246 75.0259 33.7525 73.9345 37.5094C74.2013 37.9848 74.4422 38.4817 74.6555 39"></path></g></g></svg>
</a>
```
{% endfolding %}

接下来修改样式，在暗黑模式下显示出图标：

{% folding cyan, /source/css/_plugins/dark.styl %}
```styl 夜间模式标题样式修改
#desktopNavTitle
  display: none !important
#githubEmoji
  display: block !important
```
{% endfolding %}

### 4.4 全局 Img 加载失败的默认提示

为图片加载失败时添加一个默认的错误输出，实现思路参考了：[图片加载失败后CSS样式处理最佳实践](https://www.zhangxinxu.com/wordpress/2020/10/css-style-image-load-fail/) 一文，效果如下：

<p class="center"><img no-lazy src=" " alt="一个图片加载失败的示例"></p>

#### 4.4.1 添加样式文件

首先是为其添加对应的样式，上图默认显示的图片是转成 Base64 记录的。

{% folding cyan, 图片加载失败的样式 %}
{% tabs img-error %}

<!-- tab 基础样式 -->
```styl 默认样式
img.error
  opacity: unset !important
  filter: unset !important
  min-height: 200px
  min-width: 200px
  display: inline-block
  transform: scale(1)
  content: ''
  color: transparent
  &:before
    content: ''
    position: absolute
    left: 0
    top: 0
    width: 100%
    height: 100%
    background: #f5f5f5 url("data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBzdGFuZGFsb25lPSJubyI/PjwhRE9DVFlQRSBzdmcgUFVCTElDICItLy9XM0MvL0RURCBTVkcgMS4xLy9FTiIgImh0dHA6Ly93d3cudzMub3JnL0dyYXBoaWNzL1NWRy8xLjEvRFREL3N2ZzExLmR0ZCI+PHN2ZyBjbGFzcz0iaWNvbiIgd2lkdGg9IjIwMHB4IiBoZWlnaHQ9IjIwMC4wMHB4IiB2aWV3Qm94PSIwIDAgMTAyNCAxMDI0IiB2ZXJzaW9uPSIxLjEiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PHBhdGggZmlsbD0iI2RjZGVlMCIgZD0iTTE5Ny4zMzMzMzMgMjgxLjY1MzMzM2E0OC40OCA0OC40OCAwIDEgMCA0OC40OCA0OC40OEE0OC40OCA0OC40OCAwIDAgMCAxOTcuMzMzMzMzIDI4MS42NTMzMzN6IiAgLz48cGF0aCBmaWxsPSIjZGNkZWUwIiBkPSJNOTcwLjY2NjY2NyAxNzAuNjY2NjY3SDUxOC42MTMzMzNhNS4zMzMzMzMgNS4zMzMzMzMgMCAwIDAtNC41ODY2NjYgMi42NjY2NjZsLTY0IDExMS42OGE1LjMzMzMzMyA1LjMzMzMzMyAwIDAgMCAwIDUuMzMzMzM0bDUzLjgxMzMzMyA5Ny40NGE1LjMzMzMzMyA1LjMzMzMzMyAwIDAgMSAwIDUuNjUzMzMzTDQwOC4yNjY2NjcgNTMwLjU2YTEuMDY2NjY3IDEuMDY2NjY3IDAgMCAxLTEuOTczMzM0LTAuNzQ2NjY3TDQyNi42NjY2NjcgMzg5LjMzMzMzM2E0LjkwNjY2NyA0LjkwNjY2NyAwIDAgMC0wLjQyNjY2Ny0yLjg4bC00My4yLTk3LjM4NjY2NmE1LjMzMzMzMyA1LjMzMzMzMyAwIDAgMSAwLTMuOTQ2NjY3TDQyMS4zMzMzMzMgMTc3LjgxMzMzM2E1LjMzMzMzMyA1LjMzMzMzMyAwIDAgMC00LjkwNjY2Ni03LjE0NjY2Nkg0Mi42NjY2NjdhNS4zMzMzMzMgNS4zMzMzMzMgMCAwIDAtNS4zMzMzMzQgNS4zMzMzMzN2NjcyYTUuMzMzMzMzIDUuMzMzMzMzIDAgMCAwIDUuMzMzMzM0IDUuMzMzMzMzaDkyOGE1LjMzMzMzMyA1LjMzMzMzMyAwIDAgMCA1LjMzMzMzMy01LjMzMzMzM1YxNzZhNS4zMzMzMzMgNS4zMzMzMzMgMCAwIDAtNS4zMzMzMzMtNS4zMzMzMzN6IG0tNjkuMzMzMzM0IDQ1Ni4wNTMzMzNhNS4zMzMzMzMgNS4zMzMzMzMgMCAwIDEtOS4yOCAzLjQ2NjY2N2wtMjE1LjQxMzMzMy0yNDQuMzJhNS4zMzMzMzMgNS4zMzMzMzMgMCAwIDAtOCAwbC0yOTEuODQgMzMxLjA5MzMzM2E1LjMzMzMzMyA1LjMzMzMzMyAwIDAgMS03LjQ2NjY2NyAwLjQ4bC0xNjcuNzMzMzMzLTE0Mi45MzMzMzNhNS4zMzMzMzMgNS4zMzMzMzMgMCAwIDAtNi44OCAwbC03My45NzMzMzMgNjMuMDRhNS4zMzMzMzMgNS4zMzMzMzMgMCAwIDEtOC43NDY2NjctNC4wNTMzMzRWMjUwLjY2NjY2N2E1LjMzMzMzMyA1LjMzMzMzMyAwIDAgMSA1LjMzMzMzMy01LjMzMzMzNGgyMDcuNTJsLTEzLjQ5MzMzMyA1MC43NzMzMzRhNS4zMzMzMzMgNS4zMzMzMzMgMCAwIDAgMC43NDY2NjcgNC4zNzMzMzNsNzAuNzczMzMzIDEwMi45MzMzMzNhNS4zMzMzMzMgNS4zMzMzMzMgMCAwIDEgMC45NiAzLjQxMzMzNGwtMTQuMjQgMTk1LjA5MzMzM2ExLjEyIDEuMTIgMCAwIDAgMS45MiAwLjhsMTgwLjE2LTIwMC44NTMzMzNhNS4zMzMzMzMgNS4zMzMzMzMgMCAwIDAgMS4xMi01LjMzMzMzNGwtMzItMTAxLjA2NjY2NmE1LjMzMzMzMyA1LjMzMzMzMyAwIDAgMSAwLjY0LTQuNTg2NjY3bDMxLjItNDUuNTQ2NjY3SDg5NmE1LjMzMzMzMyA1LjMzMzMzMyAwIDAgMSA1LjMzMzMzMyA1LjMzMzMzNHoiICAvPjwvc3ZnPg==") no-repeat center / 80% 80%
  &:after
    @media screen and (max-width: 300px)
      display: none
    content: attr(alt)
    position: absolute
    left: 0
    bottom: 0
    width: 100%
    line-height: 2
    background-color: rgba(0,0,0,.5)
    color: white
    font-size: 12px
    text-align: center
    white-space: nowrap
    overflow: hidden
    text-overflow: ellipsis
```
<!-- endtab -->

<!-- tab 额外样式 -->
```styl 这里是做了一些兼容
// 为评论框的图片加载失败文本
#comments
  img.error
    &:after
      content: 'img error'

// 重写图片大小：评论框头像
.fix-avatar-imgError > div > img.error
  min-height: 100%
  min-width: 100%
  &:after
    display: none

// 重写图片大小：作者头像，友链头像组
.fix-author-imgError > a > img.error,
.simpleuser-group > a > img.error
  min-height: unset
  min-width: unset

// 隐藏 Fancybox 弹窗
.hideFancybox
  margin: 0 2px
  span
    display: none !important
```
<!-- endtab -->

{% endtabs %}
{% endfolding %}

#### 4.4.2 添加事件代码

核心思路是监听图片的 `error` 事件，然后添加对应的样式类，此部分代码放到 `head.ejs` 中：

{% folding cyan, /layout/_partial/head.ejs %}
```js 记得放进 script 标签中
document.addEventListener("error", function(e) {
  const elem = e.target;
  const parentElem = e.target.parentElement;
  const parentElemClass = parentElem.classList.toString();
  const pParentElemClass = parentElem.parentElement.classList.toString();
  if (elem.tagName.toLowerCase() !== 'img') {
      return;
  }
  elem.classList.add('fix-cursor-default', 'error');
  if(parentElemClass === 'fancybox' && pParentElemClass === 'fancybox') {
    parentElem.parentElement.classList.add('hideFancybox');
    parentElem.parentElement.classList.remove('fancybox');
    parentElem.classList.remove('fancybox');
  } else if(parentElemClass === 'img-bg' && pParentElemClass === 'img-wrap') {
    parentElem.parentElement.classList.add('hideFancybox');
  } else if(parentElemClass === 'author') {
    parentElem.parentElement.classList.add('fix-author-imgError');
  } else if(parentElemClass.indexOf('tk-avatar') != -1 ) {
    parentElem.parentElement.classList.add('fix-avatar-imgError');
  }
}, true);
```
{% endfolding %}

### 4.5 恶意反代防御

静态文件似乎没有抵御反代的能力，简单的实现一下：

{% folding cyan, 反代防御 %}
```html 相关域名更改成自己的即可
<div style="display: none;">
  <img no-lazy style="display:none" src=" " onerror="this.onerror=null;var str1='ink'+'ss'+'.cn',str2='document.location.host',str3=eval(str2);if(str1!=str3&&str3!='localhost:4000'&&str3!='localhost:5050'&&str3!='live.szyink.com'){do_action='location.href=location.href.replace(document.location.host,'+'\'ink'+'ss'+'.cn\''+')';alert(decodeURI('%E6%82%A8%E7%8E%B0%E5%9C%A8%E5%A4%84%E4%BA%8E%E6%81%B6%E6%84%8F%E9%95%9C%E5%83%8F%E7%AB%99%E4%B8%AD%EF%BC%8C%E5%8D%B3%E5%B0%86%E8%B7%B3%E8%BD%AC%E5%9B%9E%E6%BA%90%E7%AB%99!'));eval(do_action)}" />
</div>
```
{% endfolding %}

### 4.6 动态修改网页标题

没有做过多的侵入（*个人认为把标题修改为 ‘你去哪儿了，快回来’ 等类似的行为相当幼稚、无趣、讨人厌*），只是在离开标签时去除文章标题，只保留网站标题。

{% folding cyan, /source/js/app.js %}
*可以在 locationHash(); 下方添加 changeTitle() 的调用*
```js 动态修改标题
const changeTitle = () => {
  sessionStorage.setItem("domTitle", document.title);
  document.addEventListener('visibilitychange', function () {
    const title = sessionStorage.getItem("domTitle") || document.title;
    const titleArr = title.split(' - ') || [];
    if (document.visibilityState == 'hidden') {
      document.title = titleArr.length === 2 ? titleArr[1] : titleArr[0];
    } else {
      document.title = title;
    }
  });
}
```
{% endfolding %}


## 五、小技巧

### 透明图片的背景色设定

透明图可以利用 `image` 标签 的 `bg` 参数，设定不同的背景色，在暗黑模式中下图有不同的表现。

{% gallery center::2::png %}
{% image ../../img/article/Volantis主题个性化修改合集/people.png, bg=var(--color-card), height=260px, alt='var(--color-card)' %}
{% image ../../img/article/Volantis主题个性化修改合集/people.png, bg=var(--color-white-png), height=260px, alt='var(--color-white-png)' %}
{% endgallery %}

### 替代标题 Meta 的显示

如果文章设置了 `seo_title` 则不会显示默认的顶部 Meta 块儿，此处可以整个更花哨的标题：

<p class="p center logo large"><em>Volantis 主题个性化修改<sup>V1.0</sup></em></p>

