---
title: DNSPod & ACME
toc: true
tag:
  - DNSPod
  - ACME
  - 泛域名证书
categories: 资料
description: 泛域名证书的申请与使用
date: '2021-01-29 16:00'
updated: '2021-01-29 16:00'
abbrlink: e9bd7c2d
---

&ensp;&emsp;2021 年，新年的第一篇文章~

## ACME

- 下载并安装 acme

```sh
curl https://cdn.jsdelivr.net/gh/Neilpang/acme.sh/acme.sh | INSTALLONLINE=1  sh
```

- 开启自动更新

```sh
acme.sh --upgrade --auto-upgrade
```

- 设置 DNSPod 环境变量

```sh
export DP_Id="******"
export DP_Key="*******************************"
```

- 生成泛域名证书

```sh
acme.sh --issue --dns dns_dp -d "*.inkss.cn" -d "*.szyink.com"
```

## Nginx

- 部署证书至宝塔 SSL 目录

```sh
acme.sh --install-cert -d *.szyink.com \
--key-file       /www/server/panel/vhost/cert/szyink.com/privkey.pem  \
--fullchain-file /www/server/panel/vhost/cert/szyink.com/fullchain.pem \
--reloadcmd     "service nginx reload"
```

- OR 手动指定

