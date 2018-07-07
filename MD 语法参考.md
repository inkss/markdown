# 一级标题**语法** 与#号要有*一个*空格

一句代码语法 `System.out.println("Hello");` 它必须要紧邻符号

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
接下来是无序列表的语法，要有一行间隔。

* 一级列表
  * 二级列表 与上一级多两个空格
    * 三级列表 同理 再次多两个空格
  * 二级列表
* 一级列表

```markdown
* 一级列表
  * 二级列表 与上一级多两个空格
    * 三级列表 同理 再次多两个空格
  * 二级列表
* 一级列表
```

接下来是有序列表的语法

```markdown
1. 第一个
1. 第二个
1. 第三个
```

1. 第一个
1. 第二个
1. 第三个

对于有序排列 markdownlint 上给的原话是（顺便展示下引用的语法）：
>Parameters: style ("one", "ordered"; default "one").This rule is triggered on ordered lists that do not either start with '1.' or do not have a prefix that increases in numerical order (depending on the configured style, which defaults to 'one').xample valid list if the style is configured as 'one'

---
接下来是链接的语法：

众所周知[百度一下](https://www.baidu.com)，你就爆炸。

---
接下来是表格的语法：

|姓名|分数（居中显示）|排名|
|-|:-:|-|
|二狗子|99|1|

斜体：*斜体*

加粗：**粗体**

删除线：~~删除线~~的语法

高亮： ==高亮==

下标：H~2~O

上标：10^2^ = 100