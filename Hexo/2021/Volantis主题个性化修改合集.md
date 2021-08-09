---
title: Volantis 主题个性化修改合集
toc: true
indent: true
tag:
  - Hexo
  - Volantis
categories: 教程
date: '2021-08-05 09:20'
updated: '2021-08-09 00:00'
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
{% note bug red, 正在完善中，尽请期待... %}

## 一、引言

修改主题最大的麻烦之处就是可能会与官方分支相差太大，更新主题时面临一堆的冲突文件，所以要尽可能的减少冲突文件的产生，你可以在以下链接找到有关冲突处理的办法：

{% link 如何正确地更新主题, https://github.com/volantis-x/hexo-theme-volantis/issues/459 %}

所谓主题修改无外乎改改样式，加加新功能，事实上 Volantis 已经抽出各种选项到配置文件了，如果只是修改*滚动条*、*导航栏*、*自定义字体*、*颜色*等等，都可以在配置文件中完成，所以建议先熟读文档 [主题配置](https://volantis.js.org/v5/theme-settings/) 一节，当然文档的维护不一定赶得上开发进度，一切以主题下的配置文件为主。

而若是新增主题没有的功能，确实得改主题了，只需要新增 `js`、`css` 这种文件的可以参考文档的这节内容：[使用 Import 导入外部文件](https://volantis.js.org/v5/site-settings/#%E4%BD%BF%E7%94%A8-Import-%E5%AF%BC%E5%85%A5%E5%A4%96%E9%83%A8%E6%96%87%E4%BB%B6)。

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

调整移动端的显示，平铺显示，效果参考本站移动端下的显示。

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
```styl
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
  p
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

#### 2.1.5 修改二级标题的样式

样式借鉴了 Handsome 主题，效果参考本文二级标题。

{% folding cyan, 样式修订 HandSome H1 H2 标题 %}
```styl
article#post
  & > h1,h2
    font-weight: 700
    background: linear-gradient(to bottom,transparent 60%,rgba(189,202,219,.3) 0) no-repeat
    display: initial
    width: auto
    border-bottom: none
```
{% endfolding %}

#### 2.1.6 封面标题居中

效果参考：[ES6](/tags/ES6/)

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

效果参见本站，文章页透明会影响阅读体验，所以文章下没加透明度的。

{% folding cyan, 卡片添加透明度 %}
```styl
// 添加透明度
#l_header
  background-color: hsla(0,0%,100%,.9)
#l_main .post,
#l_side .widget
  background-color: rgba(255,255,255,0.9)
  box-shadow: 0 4px 10px -4px #ebedf0
// 文章不透明
#post.post
  background-color: #ffffff
```
{% endfolding %}

#### 2.1.8 文章页不显示头图

即头图只在列表页显示，进入文章时不显示。

{% folding cyan, 文章页不显示头图 %}
```styl
#l_main .article .headimg-div
  display: none
```
{% endfolding %}

### 2.2 样式增强

在主题基础上新增的样式文件，实现更多的效果。

#### 2.2.1 自定义 Note 图标

此处特指主题的 `note` 和 `noteblock` 标签，如果需要更多类型的图片就需要自己添加了。新建 `note.styl` 文件，个人新增的图标都存在此处。

然后是找图标，我们需要图标的代码而不是名称，进入 [fontawesome](https://fontawesome.com/v5.15/icons?d=gallery&p=2),打开 **开发人员工具**，使用 {% kbd Ctrl %} + {% kbd Shift %} + {% kbd C %} 选取需要新增的图标，在 **元素** 选项卡右侧的 **样式** 栏，找到对应内容：

```css “\f368” 就是我们需要的代码
.fa-accessible-icon:before {
  content: "\f368";
}
```

接着在 note.styl 一个个添加就好了：

```styl
// 自定义 Note
div.note
  &.alien-monster::before
    content: '\f8f6'
```

{% note alien-monster blue, 效果见本条内容：note alien-monster blue  %}

#### 2.2.2 页脚响应式处理

所谓响应式处理主要是利用 CSS 的媒体查询，利用 `max-width` 和 `display`，可以达到当小于一定可视范围时主动隐藏某些元素，这样在使用时只需要添加对应的类名即可，具体的尺寸根据实际情况实际对待。注：主题在 500px 以下便会显示移动端样式。

```styl
.footerMax560
  @media screen and (max-width: 560px)
    display: none
```

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

```styl 暗色系配色
#read_bkg
  background: #21252b !important
.post_read
  background-color: #282c34 !important
```

#### 3.1.2 添加元素代码

在 `layout/_partial/rightmenu.ejs` 文件中的 `<% } else if (item == 'print') { %>` 下新加一个判断：

```ejs 新建一个对 reading 的判断
<% } else if (item == 'reading') { %>
  <li class='option menuOption-Content'>
    <span class='vlts-menu opt fix-cursor-default' id='readingModel'>
      <i class='<%= theme.rightmenu.reading.icon %> fa-fw '></i> <%- trim(theme.rightmenu.reading.name) %>
    </span>
  </li> 
```

#### 3.1.3 添加事件代码

在 `source/js/rightMenu.js` 处添它的逻辑处理部分，注意：目前开发版的右键已经移除了对 Jquery 的依赖，但是目前使用的右键依旧需要 Jquery 的支持，此处需要留意。

{% tabs rightMenu  %}
<!-- tab 选取阅读模式 -->
```js 在前面的选择器部分新增阅读模式
const _readingModel = document.getElementById('readingModel'),
  _readBkg = document.getElementById('read_bkg');
```
<!-- endtab -->

<!-- tab 初始化加载阅读模式 -->
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
{% endtabs %}

### 3.2 阅读更多样式更改

为列表页的阅读更多添加一个动画，不过按照 @xaoxuu 的意见，`v5.0` 未来会去掉「阅读全文」「去原站阅读」等废话，整个卡片是一整个按钮，分类、标签等不可点击，看情况选择啦。

#### 3.2.1 添加样式文件

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

### 添加一种时间线样式

### 添加 Github 暗黑模式动画

### 文章页自定义背景

### 文章页首行缩进开关

### 列表页文章标题开关

### TwiKoo 与 Beaudar 的评论共用

### 自定义右键和评论

### 引入 Iconfont 图标

### 全局 Img 加载失败的默认提示

### 恶意反代防御

### 整合文章归档模板

## 四、小技巧

### 透明图片的背景色设定

### 替代标题 Meta 的显示

### 引用更多类型的自定义字体

### 动态修改网页标题
