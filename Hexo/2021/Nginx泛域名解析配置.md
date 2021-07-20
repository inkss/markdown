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
updated: '2021-07-20 22:28'
abbrlink: e7617c8b
description: 本文共包含以下内容：Nginx 泛域名解析、根据 subdomain 匹配反代地址、防止恶意泛域名解析、BasicAuth 的认证校验、宝塔面板的反代访问、泛域名 SSL 证书申请、泛域名 CDN 分发和泛域名证书的自部署。
headimg: ../../img/article/Nginx泛域名解析配置/main.png
background: /img/article/Nginx泛域名解析配置/wallhaven-k778dq.jpg
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

![泛域名监听](../../img/article/Nginx泛域名解析配置/image-20210720224409500.png)

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

{% image ../../img/article/Nginx泛域名解析配置/image-20210721005003928.png, alt='验证不通过将返回 401 Authorization Required', height=250px, bg=var(--color-card) %}

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

![泛域名证书申请](../../img/article/Nginx泛域名解析配置/image-20210720224604524.png)

## 二、CDN 侧配置

此处基于腾讯云 CDN 操作，为什么要使用 CDN 呢，除了分发外还有一个好处就是可以隐藏服务器的真实地址，毕竟万一真有小贱人扫服务器也是无奈呀 {% emoji liuhan %}。在腾讯云 CDN 处直接新建泛域名地址即可，按照提示进行域名验证云云。

### 泛域名 CDN 分发

关于 SSL，使用 CDN 访问网站时，如果需要通过 https 的形式访问，是需要将证书部署到 CDN 侧的。，由于商用的泛域名证书贵的离谱，所以这里我们将宝塔申请的 `Let's Encrypt` 证书上传上去就行了。

关于缓存，原则上说应该全程不缓存，CDN 侧只能通过目录/格式等条件进行特定的缓存，没办法根据二级域名进行回源。例如我们无法在 CDN 上区分来自 `/static/` 路径的请求是哪一个二级域发出的。

### 泛域名证书的自部署

由于 `Let's Encrypt` 的证书只有三个月的有效期，所以势必需要每隔一段时间就要对证书进行一次更新。服务器这边可以通过宝塔的定时任务自动更新部署，但是 CDN 侧腾讯云并没有提供类似的功能。不过由它的云 API 得知，所有的控制台操作行为的背后都是基于 Api 进行的。

通过查询文档我们可以得知 [UpdateDomainConfig](https://cloud.tencent.com/document/product/228/41116) 接口，就可以完成对证书的设定。

> UpdateDomainConfig 用于修改内容分发网络加速域名配置信息。
>
> 注意：如果需要更新复杂类型的配置项，必须传递整个对象的所有属性，未传递的属性将使用默认值，建议通过查询接口获取配置属性后，直接修改后传递给本接口。Https 配置由于证书的特殊性，更新时不用传递证书和密钥字段。

然而事实上，更新/修改时，可以传递证书和密钥字段，此时证书不是以腾讯云托管证书的形式存在，而是自有证书。（本来我最初的思路是通过 SSL 接口将证书上传到托管证书里，CDN 这边通过 `CertId` 进行设置，但若是可以直接指定证书内容的话反而更简单。）

可以通过腾讯云的在线 Api 自动生成对应语言的代码，由于我对 Python 不熟，这里使用了 Node 版本。

```js 主要是参数的定义部分，证书可以用 fs.readFileSync() 通过路径读取获得
const client = new CdnClient(clientConfig);
const params = {
  "Domain": "szyink.com",
  "Https": {
    "Switch": "on",
    "Http2": "on",
    "OcspStapling": "on",
    "Hsts": {
      "Switch": "on",
      "MaxAge": 31536000
    },
    "CertInfo": {
      "Certificate": pem.cert1,
      "PrivateKey": pem.key1,
      "Message": "更新日期：" + date.format(new Date(), 'YYYY-MM-DD HH:mm')
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

剩下的思路就很简单了，我们只需要按照证书路径读取文件内容，设置个定时任务，每隔一个月执行一遍更新即可。需要注意的是，定时任务的命令需要写到绝对命令，类似如下：

```sh 命令语句，本机的 Node 环境是通过 nvm 安装
/root/.nvm/versions/node/v14.17.3/bin/node /root/tencentcdn/index.js
```

## 三、配置的附录内容

### 获取真实 IP

当所有指向到服务器的域名都套上一层 CDN 后，理所应当的，在 Nginx 的日志处记录不到真实的客户端 IP 地址，对于宝塔用户来说，只需要修改 Nginx 的主配置文件即可：

{% codeblock lang:conf mark:4-5 在配置文件中添加高亮行，然后重启 Nginx 即可~ line_number:false %}
http
{
  include mime.types;
  set_real_ip_from 0.0.0.0/0;
  real_ip_header X-Forwarded-For;
  #include luawaf.conf;

  include proxy.conf;
  //...
}
{% endcodeblock %}

如果你对宝塔做了反代，那么不建议通过宝塔进行 Nginx 的重启/停止操作，在终端中执行最好。

### 屏蔽垃圾 UA

互联网上无时无刻存在着一些扫描器，这些大都是无人值守的程序，甚至没有刻意伪装 User-Agent ，下面是我收集的一些 UA 列表，可以在腾讯云 CDN 的访问控制中 **UA 黑白名单配置** 添加。

| **规则类型** | **规则内容**                                      | **生效类型** | **生效规则** | **备注**             |
| ------------ | ------------------------------------------------- | :----------: | :----------: | -------------------- |
| 黑名单       | ` *spider*|*bot*|*Spider*|*Bot*`                  |   全部内容   |      *       | 屏蔽蜘蛛（按需添加） |
| 黑名单       | `*nmap*|*NMAP*|*HTTrack*|*sqlmap*|*Java*|*zgrab*` |   全部内容   |      *       | 常见扫描器           |
| 黑名单       | `*Python*|*python*|*cur*l*Curl*|*wget|*Wget*`     |   全部内容   |      *       | 常见扫描器           |
| 黑名单       | `*MJ12bot*|*a Palo Alto*`                         |   全部内容   |      *       | 安全机器人           |
| 黑名单       | `*Go-http-client*`                                |   全部内容   |      *       | 安全机器人           |

### 屏蔽恶意 IP

UA 是可以伪装的，我通过分析日志抓取了一些恶意访问 IP，由于都是国外的地址，所以直接狠一点，如果所处于的 IP 段是机房类型的，就直接按照大的 IP 段封禁。这个部分是依靠宝塔插件 **系统防火墙** 实现的，下面是导出的规则信息，可以直接复制后导入：

```json 辣鸡国外 IP 段
[{"id": 32, "types": "drop", "address": "20.0.0.0/8", "brief": "美国慧与科技 & 微软数据中心", "addtime": "2021-07-20 13:07:01"}, {"id": 31, "types": "drop", "address": "167.248.133.0/24", "brief": "Censys 扫描", "addtime": "2021-07-20 09:42:39"}, {"id": 30, "types": "drop", "address": "74.120.14.0/24", "brief": "Censys 扫描", "addtime": "2021-07-20 09:42:38"}, {"id": 29, "types": "drop", "address": "162.142.125.0/24", "brief": "Censys 扫描", "addtime": "2021-07-20 09:42:38"}, {"id": 28, "types": "drop", "address": "192.35.168.0/23", "brief": "Censys 扫描", "addtime": "2021-07-20 09:42:38"}, {"id": 27, "types": "drop", "address": "192.241.128.0/17", "brief": "美国DigitalOcean数据中心", "addtime": "2021-07-20 09:40:45"}, {"id": 25, "types": "drop", "address": "18.224.0.0/14", "brief": "Amazon数据中心 224-227", "addtime": "2021-07-17 11:35:11"}, {"id": 24, "types": "drop", "address": "18.192.0.0/11", "brief": "Amazon数据中心 192-223", "addtime": "2021-07-17 11:35:18"}, {"id": 23, "types": "drop", "address": "18.160.0.0/11", "brief": "Amazon数据中心 160-191", "addtime": "2021-07-17 11:32:23"}, {"id": 20, "types": "drop", "address": "4.122.0.0/16", "brief": "美国德梅因Microsoft数据中心", "addtime": "2021-07-16 10:14:08"}, {"id": 17, "types": "drop", "address": "52.221.0.0/16", "brief": "新加坡Amazon数据中心", "addtime": "2021-07-16 10:08:16"}, {"id": 16, "types": "drop", "address": "128.14.0.0/16", "brief": "美国洛杉矶Zenlayer数据中心", "addtime": "2021-07-16 10:08:25"}]
```

如你所见，这个封禁是激进的，甚至对 20/8 整个上千万个 IP 段进行了大规模屏蔽，不过由于他们位于 IDC 段中，而且还是国外 IP，所以影响也不大。（此处真的感叹由于互联网的发源地是美国，谁能想到一个拥有近千万个地址段就给了两家公司来用，甚至于他们除了这个范围外在其它范围内还有地址，再想想国内家用公网 IP 的稀缺，真的是，得展望 IPV6 呀）

### 拦截恶意请求

Hexo 是个静态站，即使它放进了服务器中，你能想到一堆机器人按照 Wordpress 的方式请求 Hexo 站吗？看到日志的时候简直是无语死了，内心 OS：扫你妹呀！

```conf 在 Hexo 站的配置文件中添加如下内容，可以选择跳转到首页或者返回 444
#静态网站天天扫，简直神经病，扫你妹啊
location ~ .*\.(php)$
{
  rewrite ^/ https://domain.com permanent;  # or return 444;
}
```

## 四、最后的最后

最最终，我们完成了泛域名相关的各类处理，证书的部署上也做到了自动完成~ {% emoji xiaodiaodaya %}
