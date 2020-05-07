import requests
import pandas as pd
import json
import time

# time,sign 之间有关联，但是不知道是怎么个关系，有可能会判断过期什么的
currentPage = 1
urlBefo = "http://p.comments.youku.com/ycp/comment/pc/commentList?jsoncallback=n_commentList&app=100-DDwODVkv&objectId=959955945&objectType=1&listType=0&currentPage="
urlEnd = "&pageSize=30&sign=659f651985d73960105207fa80133473&time=1543129681"


def timestamp_to_date(time_stamp, format_string="%Y-%m-%d %H:%M:%S"):
    time_stamp = int(time_stamp * (10 ** (10 - len(str(time_stamp)))))
    time_array = time.localtime(time_stamp)
    str_date = time.strftime(format_string, time_array)
    return str_date


def getComment(comment_List):
    comment = comment_List['data']['comment']
    userInfo = []
    for user in comment:
        content = user['content']
        createTime = timestamp_to_date(user['createTime'])
        userName = user['user']['userName']
        info = {"评论内容": content, "用户名": userName, "评论时间": createTime}
        userInfo.append(info)
    return userInfo


def toCSV(info):
    csv = []
    for page in info:
        for comment in page:
            csv.append(comment)
    df1 = pd.DataFrame(csv)
    df1.to_csv("youku.csv")


try:
    lst = []
    while True:
        url = urlBefo + str(currentPage) + urlEnd
        r = requests.get(url)
        data = json.loads(r.text.strip('\r\nn_commentList()'))  # 转成 dict
        totalPage = data['data']['totalPage']
        print("电影《我不是药神》，共", str(totalPage), "评论页，当前正在抓取第", str(currentPage), "页")
        lst.append(getComment(data))
        if currentPage == totalPage:
            break
        else:
            currentPage = currentPage + 1
    toCSV(lst)
    print("OK")

except Exception as e:
    print(e)
