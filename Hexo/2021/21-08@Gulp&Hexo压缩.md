---
title: Gulp & Hexo 处理方案
toc: true
indent: true
tag:
  - Hexo
  - Gulp
categories: 资料
date: '2021-08-21 14:30'
updated: '2021-08-23 10:32'
hideTitle: false
headimg: ../../img/article/21-08@Gulp&Hexo压缩/Hexo博客封面.png
description: 'Gulp & Hexo：通过 Gulp 对 Hexo 博客进行 html,css,js 压缩，img 转 webp 和替换图片访问链接。'
abbrlink: e19bba83
---

{% note quote, 利用 Gulp 对 Hexo 生成的博客源码进行内容的压缩、图片转 `webp` 格式以及替代图片的访问链接。 %}

## 一、Gulp 依赖环境

需要如下基础环境，例如：`npm i --save-dev gulp-cwebp` 。

``` json
{
  "devDependencies": {
    "gulp": "^4.0.2",
    "gulp-cwebp": "^4.0.2",
    "gulp-html-minifier-terser": "^6.0.1",
    "gulp-htmlclean": "^2.7.22",
    "gulp-htmlmin": "^5.0.1",
    "gulp-imagemin": "^7.1.0",
    "gulp-minify-css": "^1.2.4",
    "gulp-replace": "^1.1.3",
    "gulp-terser": "^2.0.1",
    "through2": "^4.0.2"
  }
}
```

## 二、Gulp File

可供参考的文件地址：

<div style="width: 50%;margin: 0 auto;">
{% btns rounded grid2 %}
{% cell gulpfile.js::https://gitea.szyink.com/szyink/Hexo-Blog/src/branch/main/gulpfile.js:: %}
{% cell gulp-webp-html.js::https://gitea.szyink.com/szyink/Hexo-Blog/src/branch/main/gulp-webp-html.js:: %}
{% endbtns %}
</div>

### 2.1 压缩 Html

```js
const htmlmin = require('gulp-html-minifier-terser');
const htmlclean = require('gulp-htmlclean');
const minify_html = () => (
  gulp.src(['./public/**/*.html', '!./public/{lib,lib/**}'])
    .pipe(htmlclean())
    .pipe(htmlmin({
      removeComments: true,
      minifyJS: true,
      minifyCSS: true,
      minifyURLs: true,
    }))
    .pipe(gulp.dest('./public'))
)
```

### 2.2 压缩 Js

```js
const terser = require('gulp-terser');
const minify_js = () => (
  gulp.src(['./public/**/*.js', '!./public/**/*.min.js', '!./public/{lib,lib/**}'])
    .pipe(terser())
    .pipe(gulp.dest('./public'))
)
```

### 2.3 压缩 Css

```js
const minifycss = require('gulp-minify-css');
const minify_css = () => (
  gulp.src(['./public/**/*.css'])
    .pipe(minifycss())
    .pipe(gulp.dest('./public'))
);
```

### 2.4 压缩 img

```js
const imagemin = require('gulp-imagemin');
const minify_img = async () => {
  gulp.src('./public/img/**/*')
    .pipe(imagemin([
      imagemin.gifsicle({
        interlaced: true
      }),
      imagemin.mozjpeg({
        quality: 75,
        progressive: true
      }),
      imagemin.optipng({
        optimizationLevel: 5
      }),
      imagemin.svgo({
        plugins: [{
          removeViewBox: true
        },{
          cleanupIDs: false
        }]
      })
    ]))
    .pipe(gulp.dest('./public/img'))
}
```

### 2.5 图片转 webp

```js
const cwebp = require('gulp-cwebp');
const img_webp = async () => {
  gulp.src('./public/img/**/*')
    .pipe(cwebp())
    .pipe(gulp.dest('./public/img'))
}
```

### 2.6 图片链接替换

```js
const webpHtml = require('./gulp-webp-html');
const html_webp = async () => {
  gulp.src(['./public/**/*.html', '!./public/{lib,lib/**}'])
    .pipe(webpHtml())
    .pipe(gulp.dest('./public'))
}
```

## 三、调用

图片压缩 -> 图片转换 -> 链接替换 -> 代码压缩

```js
gulp.task('one', gulp.parallel(
  minify_html,
  minify_css,
  minify_js
));

gulp.task('webp', gulp.parallel(
  img_webp,
  html_webp
));

gulp.task('img', gulp.parallel(
  minify_img
));

gulp.task('default', gulp.series('one'));
```

## 四、归纳

咳咳，上文其实说起来只算是抛{% psw 水 %}砖{% psw 文 %}引{% psw 章 %}玉{% psw 呢 %}，压缩类的正常使用即可，下面介绍下图片的替换：核心是用到了 `<picture>` 标签。

{% note quote, HTML `<picture>` 元素通过包含零或多个 `<source>` 元素和一个 `<img>` 元素来为不同的显示/设备场景提供图像版本。浏览器会选择最匹配的子 `<source>` 元素，如果没有匹配的，就选择 `<img>` 元素的 src 属性中的URL。然后，所选图像呈现在 `<img>` 元素占据的空间中。 %}

原理就是这个样子了，接下来就是选取 HTML 的 `<img>` 标签，在它的外层包裹 `<picture>` 就大功告成，现在需要的就是定位 img 和需要被替换的 img 标签。定位 img 可以用正则也可以简单的用 `startWith` 判断 `<img` ，而定位需要被替换的图片这点算是取巧了。

我的基础文件路径如下：

```sh
.
└── source
    ├── img
    │   ├── article
    │   │   └── file
    │   │       └── bkg.png
    │   ├── bkg
    │   └── friend
    └── _posts
        └── 2021
            └── file.md
```

文章调用图片是通过 Typora 处理，核心规则是**复制到指定路径**：`../../img/article/${filename}/` 。所以如果图片标签的 `src` 地址包含了 `../../img` 就可以认为是博客用到的图片，需要被替换标签，当然主要是有这样的特殊路径就直接拿来使用了。
