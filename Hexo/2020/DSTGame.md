---
title: Don't Starve Together
toc: true
indent: true
date: 2020/07/17 13:58
updated: 2020/07/18 16:45
tag:
  - Steam
categories: 游戏
descriptions: Don't Starve Together Server !!!
abbrlink: 8fafff0e
icons: [fas fa-gamepad-alt blue]
---

{% note alien-monster cyan , 预告：饥荒联机版即将上线啦，欢迎各位围观，吼吼吼~ %}

{% note bell red , 本文章分为两部分，一部分备份记录 DST 的搭建，另一部分记录游戏订阅信息~ %}

{% note poo gray , 服务器订阅至 2022/11 月，可放心食用且性能尚可，不用像当初那样抠抠搜搜的使用啦~ %}

<!-- more -->

## 一、饥荒联机版搭建

大致的流程是这样的：

![](../../img/article/DSTGame/image-20200718154336813.png)

{% tabs Steam %}

<!-- tab 1.安装 SteamCMD -->

- **新建目录 steamcmd**

此目录用于 steam 程序的安装目录。

{% codeblock lang:sh line_number:false  %}
mkdir ~/steamcmd
{% endcodeblock %}

- **下载 SteamCMD 安装文件**

{% codeblock lang:sh line_number:false  %}
wget -P ~/steamcmd https://steamcdn-a.akamaihd.net/client/installer/steamcmd_linux.tar.gz
{% endcodeblock %}

- **解压压缩包**

{% codeblock lang:sh line_number:false  %}
cd ~/steamcmd
{% endcodeblock %}

{% codeblock lang:sh line_number:false  %}
tar -xvzf ~/steamcmd/steamcmd_linux.tar.gz
{% endcodeblock %}

<!-- endtab -->

<!-- tab 2.安装饥荒服务端 -->

- **启动 steamcmd**

{% codeblock lang:sh line_number:false  %}
./steamcmd.sh
{% endcodeblock %}

- **使用公共账户登录 Steam**

{% codeblock lang:sh line_number:false  %}
login anonymous
{% endcodeblock %}

- **指定游戏文件的安装目录**

{% codeblock lang:sh line_number:false  %}
force_install_dir ../dstserver
{% endcodeblock %}

- **安装饥荒服务端**

{% codeblock lang:sh line_number:false  %}
app_update 343050 validate
{% endcodeblock %}

- **退出 Steam 客户端**

{% codeblock lang:sh line_number:false  %}
quit
{% endcodeblock %}

<!-- endtab -->

<!-- tab 3.解决依赖 -->

- **CentoS 依赖安装**

{% codeblock lang:sh line_number:false  %}
sudo yum install libcurl.i686
{% endcodeblock %}

- **跳转到 DST /bin 目录**

{% codeblock lang:sh line_number:false  %}
cd ~/dstserver/bin/lib32
{% endcodeblock %}

- **链接组件**

{% codeblock lang:sh line_number:false  %}
ln -s /usr/lib/libcurl.so.4 libcurl-gnutls.so.4
{% endcodeblock %}

<!-- endtab -->

<!-- tab 4.上传配置文件 -->

- **上传 MOD 订阅文件**

{% codeblock lang:sh line_number:false  %}
cd ~/dstserver/mods
{% endcodeblock %}

{% codeblock lang:lua 覆盖 dedicated_server_mods_setup.lua 文件 line_number:false  %}
ServerModSetup("362175979") -- Wormhole Marks        标记相联通的虫洞
ServerModSetup("378160973") -- Global Positions      小地图显示玩家位置 共享地图发现
ServerModSetup("385006082") -- DST Path Lights       路径灯在黄昏时开启，在黎明时关闭
ServerModSetup("396822875") -- Spike Trap            增加两个陷阱 
ServerModSetup("444235588") -- Deluxe Campfires      豪华营火 增加燃烧时间
ServerModSetup("458940297") -- FFood Values          显示食物价值
ServerModSetup("462434129") -- Restart               #重生 #复活 #自杀 Y:公聊 U:私聊
ServerModSetup("623286817") -- Free transplant       移植无需施肥
ServerModSetup("661253977") -- Don't Drop Everything 死亡不掉落
ServerModSetup("663554209") -- Magic Freezer         更大个的冰箱 无损
ServerModSetup("666155465") -- Show Me               鼠标显示更多信息
ServerModSetup("659459255") -- Mandrake Tree         生成萤火虫
ServerModSetup("714712361") -- Starting NovicePacks  小橘子的新手礼包
ServerModSetup("785295023") -- Super Wall DST        超级墙DST 无敌的超级墙、自动门、栅栏和栅栏门
ServerModSetup("786556008") -- 45 Inventory Slots    45个格子
ServerModSetup("1216718131") -- 防卡两招              防卡两招
ServerModSetup("1301033176") -- Chinese Language     中文语言包 汉化了人物台词
ServerModSetup("1463539363") -- Automatic Gardener   自动园丁
ServerModSetup("1510231311") -- 人物 Ringo
ServerModSetup("1548459642") -- 人物 Hachi
ServerModSetup("1592689346") -- 人物 花花
ServerModSetup("1418746242") -- 汉化增强 Chinese++ (含中文高清字体)
ServerModSetup("572538624") --  Chinese Plus 中文汉化增强
ServerModSetup("367546858") --  Chinese Language Pack 中文语言包
{% endcodeblock %}

- **上传存档配置文件**

{% codeblock lang:sh line_number:false  %}
mkdir -p ~/.klei/DoNotStarveTogether
{% endcodeblock %}

- 上传存档文件至本目录，从本地饥荒客户端拿出的文件其实还是需要配置补充一番的。
- 配置补全环境可参考: [Linux 环境搭建饥荒服务器教程-配置饥荒服务端](/article/game/77face98.html#五、配置饥荒服务端-（二）)
- 实际操作体验可参考: [Ubuntu Server 搭建饥荒联机版服务端](https://cloud.tencent.com/developer/labs/lab/10382)

<!-- endtab -->

<!-- tab 5.定制启动脚本 -->

- **创建启动脚本**

{% codeblock lang:sh line_number:false  %}
cd ~ && touch startDST.sh
{% endcodeblock %}

{% codeblock lang:sh PS：此脚本同时启动地上和地下 line_number:false  %}
#!/bin/bash
steamcmd_dir="$HOME/steamcmd"
install_dir="$HOME/dstserver"
dontstarve_dir="$HOME/.klei/DoNotStarveTogether"
cluster_name="Cluster_1"

cd "$steamcmd_dir" || fail "Missing $steamcmd_dir directory!"
cd "$install_dir/bin" || fail 

run_shared=(./dontstarve_dedicated_server_nullrenderer)
run_shared+=(-console)
run_shared+=(-cluster "$cluster_name")
run_shared+=(-monitor_parent_process $$)

"${run_shared[@]}" -shard Caves  | sed 's/^/Caves:  /' &
"${run_shared[@]}" -shard Master | sed 's/^/Master: /'
{% endcodeblock %}

- **添加执行权限**

{% codeblock lang:sh line_number:false  %}
chmod u+x ./startDST.sh
{% endcodeblock %}

<!-- endtab -->

{% endtabs %}

## 二、服务器信息

<br>

{% p center logo large, Don't Starve Together %}

<div style='text-align: center;font-size: 90%;font-weight: bold;margin: 10px;'>
  服务器名称：inkss  服务器密码：{% psw inkss.cn %}
</div>

<br>

{% folding cyan, 额外添加了三个人物 %}

{% gallery %}
![饥荒](../../img/article/DSTGame/image-20200718164133476.png)
{% endgallery %}

{% endfolding %}
