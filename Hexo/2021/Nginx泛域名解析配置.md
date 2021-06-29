---
title: 泛域名解析配置：从 Nginx 到 CDN
toc: true
indent: true
tag:
  - Nginx
  - CDN
  - 泛域名
  - 宝塔
categories: 教程
date: '2021-06-29 18:53'
updated: '2021-06-29 18:53'
abbrlink: e7617c8b
---

首先，七一快乐哇~ 热烈庆祝建党一百周年的说~ <span class="rem2">{% iconfont jr-qiyijiandangjie %}</span>

有段时间没更新文章了，最近处理了一个泛域名解析的问题，于此记录。背景是这样的，最近陆续在服务器上部署应用服务，为了方便起见，大都使用了 Docker 部署。由于不想过多的开发端口，所以打算通过 Nginx 反代进行访问，那么就有两种选择，二级域名/目录，之前的亲身经历告诉我使用目录区别应用绝对是大坑，不是所有的应用程序都能正确区分绝对/相对链接，用二级域才是最正确的选择。

在使用二级域代理访问的过程中倍感麻烦，需要确定二级域名，然后取 DNS 处新建解析，再在宝塔面板处新建网站，整个流程相当冗余，我仅仅为了反代一个地址还建立一堆的网站，真是太不优雅了。既然提出了问题那么就去解决它：理所应当的，想到了泛域名解析，统一使用一处网站配置。

<br>

{% noteblock quote, 本文共包含以下内容： %}

- Nginx 泛域名解析
- 根据 subdomain 匹配反代地址
- 防止恶意泛域名解析
- BasicAuth 的认证校验
- 宝塔面板的反代访问
- 泛域名 SSL 证书申请
- 泛域名 CDN 分发

{% endnoteblock %}

## 一、Nginx 侧配置

*关于 DNS 的泛域名解析此处不做重点，从云服务商那里配置一下就好。*

首先是在宝塔新建一个泛域名网站？大概是吧，虽然我这边设置时一直在提示【主域名不能为泛解析】，所以我这里是新建了一个 `www` 的二级域（因为我的大部分泛域解析只是为了反代本地应用）。题外话，Nginx 的域名解析有一个优先级，简单理解就是 主域 > 精确的二级域 > 二级泛域。

### 泛域名网站监听

新建完网站后，先修改配置文件，将 `server_name` 处的 `www.domain.com` 替换掉，用以监听泛域名地址访问：

```conf 网站配置
server_name  ~^(?<subdomain>.+)\.domain\.com$;
```

### 根据二级域匹配地址

这样子配置，在接下来的内容中，可以利用 **`$subdomain`** 获取二级域的具体值。然后是反代配置，我们将代码放在 `#ERROR-PAGE-START  错误页配置，可以注释、删除或修改` 之前。

```conf 反代配置
location ^~ / {
  if ($subdomain = "test1") {
    proxy_pass http://127.0.0.1:8881;
  }

  if ($subdomain = "test2") {
    proxy_pass http://127.0.0.1:8882;
  }

  proxy_set_header Host $host;
  proxy_set_header X-Real-IP $remote_addr;
  proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
  proxy_set_header REMOTE-HOST $remote_addr;
  proxy_set_header Upgrade $http_upgrade;
  proxy_set_header Connection upgrade;
  proxy_set_header Accept-Encoding gzip;
  add_header X-Cache $upstream_cache_status;
  #Set Nginx Cache
  add_header Cache-Control no-cache;
}
```

按照上面的例子，如果我使用 `test1.domain.com` 访问，那么响应的就是位于 `8881` 端口的应用。

### 防止恶意泛域名解析

由于我们在原 `www.domain.com` 的配置上进行的泛域名监听修改，那么意味着任意一个二级域名访问都会被指向到此处目录，我们需要对 *不存在、没有实际使用* 的二级域进行屏蔽（或者跳转到其它地址中）。这个需求的实现非常简单且巧妙，由于 Nginx 没有 `else` 语句，我们通过变量换种思路实现。

```conf 屏蔽未使用的二级域
location ^~ / {
  set $flag 0;

  if ($subdomain = "test1") {
    set $flag 1;
    proxy_pass http://127.0.0.1:8881;
  }

  if ($subdomain = "test2") {
    set $flag 1;
    proxy_pass http://127.0.0.1:8882;
  }

  if ($flag = 0) {
    return 444;  # 也可以作跳转
  }

  #......
}
```

### BasicAuth 的认证校验

BasicAuth 是一个简洁但使用的权限校验，因为有现成的工具我们直接使用宝塔面板先生成密文。在访问限制处添加密码访问。回到配置文件，找到 `#Directory protection rules, do not manually delete` 这句话，按照下面的地址进入到这个目录，复制其中 `conf` 文件中的内容。如果你仔细观察就会发现它就是在 `location` 中添加了 `auth_basic` 等命令，所以灵活点，先将 *protection rules* 的引入删除，我们手动添加。将复制到的内容粘贴到 `location ^~ /` 下面就好了。

聪明的你一定会想到了，这相当于对所有二级域的根目录做了加密访问，而并不是所有的二级域都需要加密，所以参考上文的 `if` 判断，我们稍微处理下。

```conf 指定域名的加密

#AUTH_START 地址加密访问
auth_basic_user_file /www/server/pass/www.domain.com/common.pass;
include enable-php-00.conf;
#AUTH_END

location ^~ / {
  set $flag 0;
  set $client_verify off;

  if ($subdomain = "test1") {
    set $flag 1;
    set $client_verify "Authorization";
    proxy_pass http://127.0.0.1:8881;
  }

  auth_basic $client_verify;

  #......
}
```

如此，需要加密的二级域，只需要添加 `set $client_verify "Authorization";` 即可。

### 宝塔面板的反代访问

都进行到这一步了，我们为什么不把宝塔也给反代了，虽然为了安全使用自定义端口更好，但是在访问地址后面追加端口号真的很影响心情（更何况我服务器还开启了安全入口、BasicAuth认证以及动态口令认证）。给宝塔面板进行反代的思路略微有点清奇，因为是限于内部规则，设置完安全域名后宝塔必须得通过这个域名进行访问（而且是带端口），所以我们在反代的时候得修改 `$host` 。

反代的本质相当于访问请求到 Nginx 后，由 Nginx 再次发出请求到你所设定的地址，假设我们将宝塔的安全域名设置为 `www.domain.com` ，我们本地访问时现实通过泛域名解析到服务器，此时是不带自定义端口（通过80/443）访问。那么 Nginx 需要代理的就是 `www.domain.com:port`，所以你懂的我们不能让他触发公网上的 DNS 解析（PS：此处更多的意义上是指当 DNS 的泛域名解析是通过 CNAME 指向到 CDN 域名，如果再次访问拿到的不是服务器真实地址），所以我们本地修改一下 `hosts` 文件。

```hosts /etc/hosts
127.0.0.1 www.domain.com
```

那么，反代宝塔面板的配置文件就可以写成这样：

```conf 反代宝塔面板
location ^~ / {
  set $xheader "$host";

  if ($subdomain = "www") {
    proxy_pass https://www.domain.com:8888;
    set $xheader "www.domain.com:8888";
  }

  proxy_set_header Host $xheader;
}
```

*自行结合上面的配置文件整合哈~*

现在，我们做到了通过 `https://www.domain.com` 直接访问宝塔面板咯。试问反代有什么好处？通过一个端口进行访问啊，所以这里可以把宝塔面板用到的防火墙关闭了。虽然但是，我们是通过本地 hosts 解析转了一圈访问的面板，所以面板的端口还真不能删，不过可以将此端口的权限只给服务器 IP 使用，四舍五入就约等于对外界关闭了端口呀。

此部分需要在终端操作。

```sh 此处假设服务器 ip 为 8.8.8.8，宝塔面板端口为 8888
# 删除原端口授权
firewall-cmd --permanent --remove-port=8888/tcp 

# 为服务器 IP 设置宝塔面板端口的访问权限
firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="8.8.8.8" port protocol="tcp" port="8888" accept'
```

### 泛域名 SSL 证书申请

SSL 是必须的，泛域名解析自然需要泛域名证书，我们移步 `www.domain.com` 的 SSL 选项，选择 `Let's Encrypt` ，使用 DNS 验证，勾选自动组合泛域名证书，进行正常的申请即可了，由于我们的配置文件是共用一个的，所以一处配置其它地方都能生效了。

宝塔的泛域名证书可以手动设置定时任务，一般还剩 30 天时就会自动进行申请，这点无需担心。

## 二、CDN 侧配置

此处基于腾讯云 CDN 操作，为什么要使用 CDN 呢，除了分发外还有一个好处就是可以隐藏服务器的真实地址，毕竟万一真有小贱人扫打服务器也是无奈呀。{% emoji liuhan %}。在腾讯云 CDN 处直接新建泛域名地址即可，按照提示进行域名验证云云。

关于 SSL ，CDN 侧也需要上传泛域名证书，有效期一年的泛域名证书简直是贵的离谱，这里我们将宝塔申请的 Let's Encrypt 证书上传上去就行了，好处是免费，坏处那自然是每隔两个月就得重新上传一次。

虽然但是，隐藏了服务器真实地址是真的舒服。

![IPV6 访问](../../img/article/Nginx泛域名解析配置/image-20210630010742601.png)

## 三、末尾

最最终，我的服务器就算是只开放了两个端口，Https & SSH {% emoji xiaodiaodaya %}
