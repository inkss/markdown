# MD 语法参考

## 1 代码

### 1.1 代码：

 `System.out.println("Hello");`

### 1.2 代码块：

```C
#include<stdio.h>
int main(void)
{
    printf("整段代码语法");
    printf("代码类型写在第一个```后面");
    printf("且与其他要有一行空格间隔");
    return 0;
}
```

---

## 2 列表

### 2.1 无序列表：

* 一级列表
  * 二级列表 与上一级多两个空格
    * 三级列表 同理 再次多两个空格
  * 二级列表
* 一级列表

### 2.2 有序列表：

1. 第一个
1. 第二个
1. 第三个

---

## 3 基础

### 3.1 引用：

>Parameters: style ("one", "ordered"; default "one").This rule is triggered on ordered lists that do not either start with '1.' or do not have a prefix that increases in numerical order (depending on the configured style, which defaults to 'one').xample valid list if the style is configured as 'one'

### 3.2 链接：

众所周知[百度一下](https://www.baidu.com)，你就爆炸。

### 3.3 表格：

|姓名（左对齐）|分数（中对齐）|排名（右对齐）|
|:-|:-:|-:|
|二狗子|99|1|

### 3.4 斜体：*斜体*

### 3.5 加粗：**粗体**

### 3.6 删除线：~~删除线~~

### 3.7 并非全能支持

#### 3.7.1 高亮： ==高亮==

#### 3.7.2 下标：H~2~O

#### 3.7.3 上标：10^2^ = 100