# MD 语法参考

```markdown
代码：
`System.out.println("Hello");`

代码块：
​```C
#include<stdio.h>
int main(void)
{
    printf("整段代码语法");
    printf("代码类型写在第一个```后面");
    printf("且与其他要有一行空格间隔");
    return 0;
}
​```

无序列表：
* 一级列表
    * 二级列表 与上一级多两个空格
        * 三级列表 同理 再次多两个空格
    * 二级列表
* 一级列表

有序列表：
1. 第一个
1. 第二个
1. 第三个

引用：
>Parameters: style ("one", "ordered"; default "one").This rule is triggered on ordered lists that do not either start with '1.' or do not have a prefix that increases in numerical order (depending on the configured style, which defaults to 'one').xample valid list if the style is configured as 'one'

链接：
众所周知[百度一下](https://www.baidu.com)，你就爆炸。

表格：
| 姓名（左对齐） | 分数（中对齐） | 排名（右对齐） |
| :------------- | :------------: | -------------: |
| 二狗子         |       99       |              1 |

斜体：
*斜体*

加粗：
**粗体**

删除线：
~~删除线~~

以下内容并非全都支持
高亮： 
==高亮==

下标：
H~2~O

上标：
10^2^ = 100
```
------

代码：

`System.out.println("Hello");`



代码块：

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



无序列表：

* 一级列表
    * 二级列表 与上一级多两个空格
        * 三级列表 同理 再次多两个空格
    * 二级列表
* 一级列表



有序列表：

1. 第一个
2. 第二个
3. 第三个



引用：

>Parameters: style ("one", "ordered"; default "one").This rule is triggered on ordered lists that do not either start with '1.' or do not have a prefix that increases in numerical order (depending on the configured style, which defaults to 'one').xample valid list if the style is configured as 'one'



链接：

众所周知[百度一下](https://www.baidu.com)，你就爆炸。



表格：

| 姓名（左对齐） | 分数（中对齐） | 排名（右对齐） |
| :------------- | :------------: | -------------: |
| 二狗子         |       99       |              1 |

| 斜体： |   *斜体*    |
| :----- | :---------: |
| 加粗： |  **粗体**   |
| 高亮： |  ==高亮==   |
| 下标： |    H~2~O    |
| 上标： | 10^2^ = 100 |