---
title: ES6 快速入坑
toc: true
tag:
  - ES6
  - Const
  - Arrow Function
  - Desctructuring
categories: 前端
description: 'ES6 快速入门，从三个角度展开：Const, 箭头函数, 解构赋值。'
references:
  - title: 带你快速入坑 ES6
    url: 'https://www.imooc.com/learn/1246'
  - title: This 指向详细解析（箭头函数）
    url: 'https://www.cnblogs.com/dongcanliang/p/7054176.html'
abbrlink: ff35080b
date: 2020-09-06 12:10
updated: 2020-09-06 12:10
---

## 一、Const 与 Var 的区别

- 只允许声明事赋值

- 先定义后使用

- 不允许重复声明

- 不支持变量提升

  ```js 不支持变量提升
  console.log(a);  //undefined
  console.log(b);  //Uncaught ReferenceError: b is not defined
  var a = 123; 
  ```

  而 let / const 会在第一行报 `Cannot access 'a' before initialization` ，即需要先定义后使用。

- 解决了全局变量污染（不属于顶层对象 window）

- 暂时性死区 （区块中的 let/const 所声明变量，形成封闭作用域，覆盖作用域之外的同名变量，也就是需要遵循先定义后使用原则）

  ```js 暂时性死区
  var URL = 'https://xxx.com';
  if(true) {
      URL = 'xxx'; // 此处报错
      const xxx;
  }
  ```

  此时报：`Uncaught SyntaxError: Missing initializer in const declaration`

- 块级作用域特性

## 二、Const 变量的可变性

{% image '../../static/ES6学习.assets/image-20200906164049377.png', width=400px, bg=var(--color-card), alt='Const 变量的可变性' %}

- const 定义的引用类型变量，其内容可以更改，可以利用 freeze 冻结保证内容不被改变。
- Object.freeze(obj) : 只能冻结一层，例如 const 定义对象中的某个属性为数组，数组中内容依旧可改。

- 自定义方法遍历实现深层次冻结。

  ```js
  function myFreeze(obj) {
      Object.freeze(obj);
      Object.keys(obj).forEach(function(key) {
          if(typeof obj[key] === 'object')
              myFreeze(obj[key]);
      });
  }
  ```

## 三、箭头函数

- 箭头函数的 this ：它会捕获其所在上下文的 this 值， 作为自己的 this 值。
- 参考链接：[This 指向详细解析（箭头函数）](https://www.cnblogs.com/dongcanliang/p/7054176.html)

**箭头函数需要注意的场景：**

- 箭头函数作为事件回调函数时。
- 对象中方法的箭头函数时（建议不用）。
- 构造函数中的箭头函数。
- 箭头函数中 arguments 失效。
- 函数原型下的方法。

## 四、解构赋值

{% folding yellow, 等号左右两边完全匹配 %}

```js
const people = {
    name: 'xxx',
    age: 18
}

const {name, age} = people;
console.log(name, age)  // xxx 18
```

```js
const people = {
    name: 'xiaoming',
    age: 18,
    mother: {
        name: 'xiaohong'
    }
}

const {
    name,
    age,
    mother: {
        name: motherName  // 别名
    }
} = people;

console.log(name, age, motherName) // xiaoming 18 xiaohong
```

{% endfolding %}

{% folding green, 如何正确的使用解构赋值 %}

```js
const sum = ([a, b, c]) => {
    console.log(a + b + c);
};

sum([1, 2, 4]);
```

```js
const foo = ({name, age, sex = 'boy'}) => {
    console.log(name, age, sex);
}

foo({
    name: 'xiaohong',
    age: '18',
    sex: 'girl'
});  // xiaohong 18 girl
```

```js
let a = 1;
let b = 2;
[b, a] = [a, b];
console.log(a, b);  // 2 1
```

```js
const json = '{"name": "xiaoming", "age": 18}';
const {name, age} = JSON.parse(json);
console.log(name, age); // xiaoming 18
```

```js
axios.get('./data.json').then(({data: {name, age}}) => {
    console.log(name, age)
});
```

{% endfolding %}
