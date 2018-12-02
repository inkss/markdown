> Python 爬虫

抓取优酷视频某一电影的所有视频评论，并对其制作为词云。

`youku.py` : 对视频《我不是药神》的评论内容进行抓取，保存到文件 youku.csv 中。

`youkufile.py` : 根据 youku.csv 中的评论数据制作词云。

```tree
.
├── 1.jpg            # 主图
├── 2.jpg            # 生成的词云图 1
├── 3.jpg            # 生成的词云图 2
├── comment.txt      # 视频评论内容
├── font.ttf         # 中文字体
├── readme.md        # readme
├── stopword.txt     # 停止词库（筛选）
├── youku.csv        # 评论抓取列表
├── youkufile.py
└── youku.py
```

![2.jpg](2.jpg)

![3.jpg](3.jpg)

