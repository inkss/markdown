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
updated: '2021-07-07 22:17'
abbrlink: e7617c8b
description: 本文共包含以下内容：Nginx 泛域名解析、根据 subdomain 匹配反代地址、防止恶意泛域名解析、BasicAuth 的认证校验、宝塔面板的反代访问、泛域名 SSL 证书申请、泛域名 CDN 分发和泛域名证书的自部署。
headimg: ../../img/article/Nginx泛域名解析配置/main.png
hideTitle: true
---

首先，七一快乐哇~ 热烈庆祝建党一百周年的说~ <span class="rem2">{% iconfont jr-qiyijiandangjie %}</span>

有段时间没更新文章了，最近处理了一个泛域名解析的问题，于此记录。背景是这样的，最近陆续在服务器上部署应用服务，为了方便起见，大都使用了 Docker 部署。由于不想过多的开放端口，所以打算通过 Nginx 反代进行访问，那么就有两种选择，二级域名/子目录，之前的亲身经历告诉我使用子目录区别应用绝对是大坑，不是所有的程序都能正确区分绝对/相对链接，用二级域才是最正确的选择。

在使用二级域访问的过程中倍感麻烦，需要确定二级域名，然后去 DNS 处新建解析，再在宝塔面板处新建网站，整个流程相当冗余无趣，仅仅为了反代一个地址还需要建立一堆的网站，真是太不优雅了。既然提出了问题那么就去解决它：理所应当的，想到了泛域名解析，统一使用一处网站配置。

<br>

{% noteblock quote, 本文共包含以下内容： %}

- Nginx 泛域名解析
- 根据 subdomain 匹配反代地址
- 防止恶意泛域名解析
- BasicAuth 的认证校验
- 宝塔面板的反代访问
- 泛域名 SSL 证书申请
- 泛域名 CDN 分发、
- 泛域名证书的自部署

{% endnoteblock %}

## 一、Nginx 侧配置

*关于 DNS 的泛域名解析此处不做重点，在云服务商的控制台配置一下就好。*

首先是在宝塔新建一个泛域名网站？大概是吧，虽然我这边设置时一直在提示【主域名不能为泛解析】，所以我这里是新建了一个 `www.domain.com` 的二级域（因为我的大部分泛域解析只是为了反代本地应用，无需具体的目录指向）。题外话，Nginx 的域名解析有一个优先级，简单理解就是 主域 > 精确的二级域 > 二级泛域。

### 泛域名网站监听

新建完网站后，先修改配置文件，将 `server_name` 替换掉，用以响应泛域名地址请求：

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

#ERROR-PAGE-START 错误页配置，可以注释、删除或修改
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

BasicAuth 是一个简洁但实用的权限校验，因为有现成的工具我们直接使用宝塔面板生成密文。在 **访问限制** 处添加密码访问。回到配置文件，找到 `#Directory protection rules, do not manually delete` 这句话，按照下面的地址进入到这个目录，复制其中 `conf` 文件中的内容。如果你仔细观察就会发现它就是在 `location` 中添加了 `auth_basic` 等命令，所以灵活点，先将此处的引入删除，我们手动添加。将复制到的内容粘贴到 `location ^~ /` 下面就好了。

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

都进行到这一步了，我们为什么不把宝塔也给反代了，虽然为了安全使用自定义端口更好，但是在访问地址后面追加端口号真的很影响心情（更何况我服务器还开启了安全入口、BasicAuth认证以及动态口令认证）。给宝塔面板进行反代的思路略微有点清奇，因为是限于内部规则，设置完安全域名后宝塔必须得通过这个域名进行访问（而且是带端口），所以我们在反代的时候得修改对应 `$host` 。

反代的本质相当于访问请求到 Nginx 后，由 Nginx 再次发出请求到你所设定的地址，假设我们将宝塔的安全域名设置为 `www.domain.com` ，我们本地访问时先是通过泛域名解析到服务器，此时不带自定义端口（通过80/443）访问。那么 Nginx 需要代理的就是 `www.domain.com:port`，所以你懂的我们不能让他触发公网上的 DNS 解析（PS：此处更多的意义上是指当 DNS 的泛域名解析是 CNAME 指向到 CDN 域名的情形，如果再次访问需要拿到服务器真实地址），所以我们本地修改一下 `hosts` 文件。

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

现在，我们做到了通过 `https://www.domain.com` 直接访问宝塔面板。试问反代有什么好处？通过 443 端口进行面板访问，所以这里可以把宝塔面板用到的防火墙端口关闭了。虽然但是，我们是通过本地 hosts 解析转了一圈访问的面板，所以面板的端口还真不能删，不过可以将此端口的权限只给服务器 IP 使用，四舍五入就约等于对外界关闭了端口呀。

此部分需要在终端操作。

```sh 此处假设服务器 ip 为 8.8.8.8，宝塔面板端口为 8888
# 删除原端口授权
firewall-cmd --permanent --remove-port=8888/tcp 

# 为服务器 IP 设置宝塔面板端口的访问权限
firewall-cmd --permanent --add-rich-rule='rule family="ipv4" source address="8.8.8.8" port protocol="tcp" port="8888" accept'
```

### 泛域名 SSL 证书申请

SSL 是必须的，泛域名解析自然需要泛域名证书，我们移步 `www.domain.com` 的 SSL 选项，选择 `Let's Encrypt` ，使用 DNS 验证，勾选自动组合泛域名证书，进行正常的申请即可了，由于我们的配置文件是共用一个的，所以一次配置其它地方都能生效。

宝塔的泛域名证书可以手动设置定时任务，一般还剩 30 天时就会自动进行申请，这点无需担心。

## 二、CDN 侧配置

此处基于腾讯云 CDN 操作，为什么要使用 CDN 呢，除了分发外还有一个好处就是可以隐藏服务器的真实地址，毕竟万一真有小贱人扫服务器也是无奈呀 {% emoji liuhan %}。在腾讯云 CDN 处直接新建泛域名地址即可，按照提示进行域名验证云云。

### 泛域名 CDN 分发

关于 SSL，使用 CDN 访问网站时，如果需要通过 https 的形式访问，是需要将证书部署到 CDN 侧的。，由于商用的泛域名证书贵的离谱，所以这里我们将宝塔申请的 `Let's Encrypt` 证书上传上去就行了。

关于缓存，原则上说应该全程不缓存，CDN 侧只能通过目录/格式等条件进行特定的缓存，没办法根据二级域名进行回源。例如我们无法在 CDN 上区分来自 `/static/` 路径的请求是哪一个二级域发出的。

### 泛域名证书的自部署

由于 `Let's Encrypt` 的证书只有三个月的有效期，所以势必需要每隔一段时间就要对证书进行一次更新。服务器这边可以通过宝塔的定时任务自动更新部署，但是 CDN 侧腾讯云并没有提供类似的功能。不过由它的云 API 得知，所有的控制台操作行为的背后都是基于 Api 进行的。

通过查询文档我们可以得知 `UpdateDomainConfig` 接口，就可以完成对证书的设定。

> UpdateDomainConfig 用于修改内容分发网络加速域名配置信息。
>
> 注意：如果需要更新复杂类型的配置项，必须传递整个对象的所有属性，未传递的属性将使用默认值，建议通过查询接口获取配置属性后，直接修改后传递给本接口。Https 配置由于证书的特殊性，更新时不用传递证书和密钥字段。

然而事实上，更新/修改时，可以传递证书和密钥字段，此时证书不是以腾讯云托管证书的形式存在，而是自有证书。（本来我最初的思路是通过 SSL 接口将证书上传到托管证书里，CDN 这边通过 `CertId` 进行设置，但若是可以直接指定证书内容的话反而更简单。）

```js node 版本：核心内容
const client = new CdnClient(clientConfig);
const params = {
    "Domain": '*.domian.com*',
    "Https": {
        "Switch": "on",
        "CertInfo": {
            "Certificate": "证书(PEM格式)",
            "PrivateKey": "密钥(KEY)",
            "Message": "泛域名证书"
        }
    }
};
client.UpdateDomainConfig(params).then(
    data => {
        console.log(data);
    },
    err => {
        console.error(err);
    }
)
```

所以思路很简单，我们只需要按照证书路径读取文件内容，设置个定时任务，每隔一个月执行一遍更新即可。需要注意的是，定时任务的命令需要写到绝对命令，类似如下：

```sh 命令语句
/root/.nvm/versions/node/v14.17.3/bin/node /root/tencentcdn/index.js
```

## 三、末尾

最最终，我们完成了泛域名相关的各类处理，证书的部署上也做到了自动完成~ {% emoji xiaodiaodaya %}
