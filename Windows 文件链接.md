# Windows 文件夹软连接

`mklink /J A B`  （A目录不存在 B目录存在<真实存储目录>）



比如需要将C盘的文档目录，链接到D盘File文件夹下：

`mklink /J C:\Users\szhiy\Documents D:\File`

 

作用：系统级别的文件软连接，支持多操作系统识别。

对 A 或者 B 的操作是相同的。

```cmd
MKLINK [[/D] | [/H] | [/J]] Link Target

        /D      创建目录符号链接。默认为文件
                符号链接。
        /H      创建硬链接而非符号链接。
        /J      创建目录联接。
        Link    指定新的符号链接名称。
        Target  指定新链接引用的路径
                (相对或绝对)。
```

**仅 CMD**