---
title: Docker 应用分享
toc: true
indent: true
hiddenTitle: false
tag:
  - Docker
categories: 文档
description: >-
  一些 Docker 应用分享及使用配置附录，包含远程控制（RustDesk，RustDesk-Api），PT 一体化工具（MoviePilot, Qbittorrent, Transmission, Audiobookshelf）和内网浏览器（Firefox）。
date: '2025-03-26 08:55'
updated: '2025-03-28 18:00'
headimg: ../../img/article/25-03@Docker应用分享/Hexo博客封面.png
abbrlink: 3f9c44bf
---

一些 Docker 应用分享及使用配置附录，包含远程控制（RustDesk），PT 一体化工具（MoviePilot, Qbittorrent, Transmission, Audiobookshelf）和内网浏览器（Firefox）。

<!-- more -->

{% note quote::提示：根据个人的实际情况，自行替换配置文件中的路径、端口、口令等内容。 %}

## 远程控制

- [rustdesk-server](https://github.com/rustdesk/rustdesk-server#classic-image)：官方原版镜像，独立运行 hbbs（ID 服务器）和 hbbr（中继服务器）。
- [rustdesk-server-s6](https://github.com/rustdesk/rustdesk-server#s6-overlay-based-images)：使用 s6-overlay 将 hbbs 和 hbbr 运行到同一个容器中。
- [rustdesk-api](https://github.com/lejianwen/rustdesk-api/)：第三方社区实现的 Api 工具，提供网页控制台。

考虑到单机部署，独立运行 hbbs, hbbr 意义不大，使用 rustdesk-server-s6 镜像即可。如果有访问网页端的需求，可再同时部署 rustdesk-api 镜像，一定程度上代替官方镜像的 21114 网页控制台的作用。

### Docker Compose

{% folding cyan, rustdesk-s6 %}

```yaml
version: '3'
services:
  rustdesk-server:
    container_name: rustdesk-s6
    ports:
      - 36011:36011
      - 36012:36012
      - 36012:36012/udp
      - 36013:36013
    image: rustdesk/rustdesk-server-s6:latest
    environment:
      - RELAY=<指向服务器的域名>:36013
      - ENCRYPTED_ONLY=1
      - PORT=36012
    volumes:
      - /opt/docker/rustdesk-s6:/data
    restart: unless-stopped
    network_mode: bridge
```

{% endfolding %}

{% folding cyan, rustdesk-s6 + rustdesk-api %}

```yaml
version: '3'
services:
  rustdesk-server:
    container_name: rustdesk-s6
    image: rustdesk/rustdesk-server-s6:latest
    restart: unless-stopped
    networks: 
      - rustdesk-network
    ports:
      - 36601-36605:36601-36605
      - 36602:36602/udp
    environment:
      - TZ=Asia/Shanghai
      - RELAY=<指向服务器的域名>:36603
      - ENCRYPTED_ONLY=1
      - PORT=36602
    volumes:
      - /opt/docker/rustdesk-s6:/data
  rustdesk-api:
    container_name: rustdesk-api
    image: lejianwen/rustdesk-api:latest
    restart: unless-stopped
    networks: 
      - rustdesk-network
    ports:
      - 36600:21114
    environment:
      - TZ=Asia/Shanghai
      - RUSTDESK_API_RUSTDESK_ID_SERVER=rustdesk-s6:36602
      - RUSTDESK_API_RUSTDESK_RELAY_SERVER=rustdesk-s6:36603
      - RUSTDESK_API_RUSTDESK_API_SERVER=https://<启用反代的Nginx域名>
      - RUSTDESK_API_RUSTDESK_WS_HOST=wss://<启用反代的Nginx域名>
      - RUSTDESK_API_RUSTDESK_KEY_FILE=./conf/data/id_ed25519.pub
    volumes:
      - /opt/docker/rustdesk-api/:/app/data
      - /opt/docker/rustdesk-s6:/app/conf/data
networks:
  rustdesk-network:
    driver: bridge
    enable_ipv6: true
    ipam:
      config:
        - subnet: 172.19.0.0/16
        - subnet: "2001:db8:2::/64"

```

```conf Nginx 配置
server {
    listen 443 ssl http2 ; 
    listen [::]:443 ssl http2 ; 
    server_name <启用反代的Nginx域名>; 
    access_log /www/sites/rustdesk/log/access.log main; 
    error_log /www/sites/rustdesk/log/error.log; 
    
    proxy_set_header Host $host; 
    proxy_set_header X-Real-IP $remote_addr; 
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; 
    proxy_http_version 1.1; 

    location / {
        proxy_pass http://127.0.0.1:36600;
    }

    location /ws/id {
        proxy_pass http://127.0.0.1:36604;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "Upgrade";
        proxy_read_timeout 86400s;
    }

    location /ws/relay {
        proxy_pass http://127.0.0.1:36605;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "Upgrade";
        proxy_read_timeout 86400s;
    }
    
    ssl_certificate /www/sites/rustdesk/ssl/fullchain.pem; 
    ssl_certificate_key /www/sites/rustdesk/ssl/privkey.pem; 
    ssl_protocols TLSv1.3; 
    ssl_ciphers ECDHE-ECDSA-AES256-GCM-SHA384:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-CHACHA20-POLY1305:ECDHE-RSA-CHACHA20-POLY1305:ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES128-GCM-SHA256:DHE-RSA-AES256-GCM-SHA384:DHE-RSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-SHA384:ECDHE-RSA-AES128-SHA256:!aNULL:!eNULL:!EXPORT:!DSS:!DES:!RC4:!3DES:!MD5:!PSK:!KRB5:!SRP:!CAMELLIA:!SEED; 
    ssl_prefer_server_ciphers on; 
    ssl_session_cache shared:SSL:10m; 
    ssl_session_timeout 10m; 
    error_page 497 https://$host$request_uri; 
    add_header Strict-Transport-Security "max-age=31536000"; 
}
```

{% endfolding %}

### 附录

- 关于 ID 服务器

  - 如果输入的是 IP，RustDesk 只会按照 IP （V4/V6）类型进行 NAT 检测。

  - 所以建议输入双栈域名，同时解析 AAAA 和 A，让客户端自行选择。

  - 推荐使用：[Free dynamic DNS for IPv6](https://dynv6.com/)。

- 关于环境变量`RELAY`

  - 用于 ID 服务器告诉客户端中继服务器的地址。
  - 当然你也可以不配置该变量，而是在客户端手动输入中继服务器的值。

- 关于客户端配置：

  - | 项目       | 内容                                                     |
    | ---------- | -------------------------------------------------------- |
    | ID 服务器  | *服务器:21116*（自定义端口需明确指出）。                 |
    | 中继服务器 | 环境变量配置 *RELAY* 后可置空。                          |
    | API 服务器 | 如果未部署 *rustdesk-api*  镜像可置空。                  |
    | key        | 输入公钥文件*id_ed25519.pub*的内容（也可在日志中找到）。 |

- 关于默认端口：

  - | 端口      | 协议        | 服务     | 用途                                                         |
    | --------- | ----------- | -------- | ------------------------------------------------------------ |
    | 21114     | TCP         | hbbs     | 网页控制台（仅专业版可用）。                                 |
    | **21115** | **TCP**     | **hbbs** | **NAT类型检测和在线状态查询。**                              |
    | **21116** | **TCP/UDP** | **hbbs** | **打洞与连接服务/ID注册与心跳服务。**                        |
    | **21117** | **TCP**     | **hbbr** | **中继服务。**                                               |
    | 21118     | TCP         | hbbs     | 网页客户端（[RustDesk Web Client](https://rustdesk.com/web/)）。 |
    | 21119     | TCP         | hbbr     | 网页客户端（[RustDesk Web Client](https://rustdesk.com/web/)）。 |

- 关于自定义端口

  - 综上，Rustdesk 正常运行的最小端口为 21115，21116，21117。
  - 环境变量`PORT`定义的是打洞端口（21116），其余端口通过该端口自动计算得出^[信息来源：[rustdesk-server/issues/191#issuecomment-1942185062](https://github.com/rustdesk/rustdesk-server/issues/191#issuecomment-1942185062)]。
  - 桥接模式下，务必保持映射前后的端口一致。

- 关于防火墙

  - 至少需要放行最小运行端口（21115-21117）。
  - 如果使用官方的 Web Client 还需要放行 21118，但在连接时需要在该网站输入设备 ID、服务器地址、Key 等信息：`<ID>@<服务器地址>?key=<密钥>`^[个人不习惯这种行为，如果有使用网页客户端需求，不如部署 rustdesk-api。]。

- 碎碎念

  - {% psw Rustdesk 的文档稀烂 %} ，关于自定义端口的描述是翻阅 issue 得知的。
  - 中国反诈中心似乎在扫描部署了 RustDesk 的境内服务器，进而封禁 IP，不建议使用默认端口。


## PT 一体化工具

- MoviePilot：自动订阅、整理。
- Audiobookshelf：自托管有声书架。
- QBittorrent：常见 PT 站所允许的下载器。
- Transmission：带快校的下载（保种）器。

### Docker Compose

{% folding cyan, moviepilot-v2、qbittorrent、transmission、audiobookshelf %}

```yaml
version: "3.3"
services:
  moviepilot:
    stdin_open: true
    tty: true
    image: jxxghp/moviepilot-v2:latest
    container_name: moviepilot-v2
    restart: always
    network_mode: bridge
    ports:
      - "23301:3000"
      - "23302:3001"
    volumes:
      - "/opt/Docker/moviepilot-v2/config:/config"
      - "/opt/Docker/moviepilot-v2/core:/moviepilot/.cache/ms-playwright"
      - "/媒体库目录:/媒体库目录"
    environment:
      - "NGINX_PORT=3000"
      - "PORT=3001"
      - "PUID=0"
      - "PGID=0"
      - "UMASK=000"
      - "TZ=Asia/Shanghai"
      - "MOVIEPILOT_AUTO_UPDATE=false"
      - "PROXY_HOST=http://10.0.10.3:1080"   # 代理
      - "SUPERUSER=admin"                    # 管理员账户
  qbittorrent:
    image: linuxserver/qbittorrent:latest
    container_name: qbittorrent
    restart: always
    network_mode: bridge
    volumes:
      - "/opt/Docker/qBittorrent:/config"
      - "/媒体库目录:/媒体库目录"
    ports:
      - "23303:8080"
      - "23304:6881"
      - "23304:6881/udp"
    environment:
      - "PUID=0"
      - "PGID=0"
      - "TZ=Asia/Shanghai"
      - "WEBUI_PORT=8080"
      - "TORRENTING_PORT=6881"
  transmission:
    image: chisbread/transmission:latest
    container_name: transmission
    restart: always
    network_mode: bridge
    volumes:
      - "/opt/Docker/transmission:/config"
      - "/媒体库目录:/媒体库目录"
    ports:
      - "23305:9091"
      - "23306:51413"
      - "23306:51413/udp"
    environment:
      - "PUID=0"
      - "PGID=0"
      - "TZ=Asia/Shanghai"
      - "PEERPORT=9091"
      - "RPCPORT=51413"
      - "USER=username"
      - "PASS=password"
  audiobookshelf:
    image: advplyr/audiobookshelf:latest
    container_name: audiobookshelf
    restart: always
    network_mode: bridge
    volumes:
      - "/opt/Docker/audiobook/metadata:/metadata"
      - "/opt/Docker/audiobook/podcasts:/podcasts"
      - "/opt/Docker/audiobook/config:/config"
      - "/有声资源:/audiobooks"
    ports:
      - "23307:80"
    environment:
      - "TZ=Asia/Shanghai"
```

{% endfolding %}

### 附录

- 可在下载器中，设置 *torrent 完成时运行外部程序* 手动触发 MoviePilot 的媒体入库动作。

```bash
curl "http://moviepilot_ip:23301/api/v1/transfer/now?token=moviepilot_token"
```

## 内网浏览器

- Firefox：火狐🦊。

### Docker Compose

{% folding cyan, jlesage/firefox:latest %}

```yaml
version: '3.8'
services:
  firefox:
    image: jlesage/firefox
    container_name: firefox
    network_mode: bridge
    ports:
      - "5800:5800"
    environment:
      - LANG=zh_CN.UTF-8
      - ENABLE_CJK_FONT=1
      - TZ=Asia/Shanghai
      - KEEP_APP_RUNNING=1
      - WEB_AUDIO=1
      - WEB_AUTHENTICATION=1
      - WEB_AUTHENTICATION_USERNAME=username
      - WEB_AUTHENTICATION_PASSWORD=password
      - VNC_PASSWORD=vnc_password
      - SECURE_CONNECTION=1
      - BASE_URL=https://反代用域名
    volumes:
      - /opt/Docker/firefox:/config
    restart: unless-stopped

```

{% endfolding %}

### 附录

- Nginx 反代配置，另如需在 CDN 中使用，请关闭**回源跟随301/302配置**。

{% folding cyan, Nginx 反代配置（局部） %}

```yaml "Firefox_IP：指向部署容器的 IP 地址。
location / {
    proxy_pass https://Firefox_IP:5800; 
    proxy_set_header Host $host; 
    proxy_set_header X-Real-IP $remote_addr; 
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; 
    proxy_set_header X-Forwarded-Proto $scheme; 
    proxy_http_version 1.1; 
    proxy_set_header Upgrade $http_upgrade; 
    proxy_set_header Connection "upgrade"; 
    proxy_buffering off; 
    proxy_ssl_verify off; 
}
location /websockify {
    proxy_pass https://Firefox_IP:5800; 
    proxy_ssl_verify off; 
    proxy_http_version 1.1; 
    proxy_set_header Upgrade $http_upgrade; 
    proxy_set_header Connection "upgrade"; 
    proxy_read_timeout 86400; 
}
location /websockify-audio {
    proxy_pass https://Firefox_IP:5800; 
    proxy_ssl_verify off; 
    proxy_http_version 1.1; 
    proxy_set_header Upgrade $http_upgrade; 
    proxy_set_header Connection "upgrade"; 
    proxy_read_timeout 86400; 
}
```

{% endfolding %}

- 如需在 Uptime Kuma 中配置检测，需在请求头中主动指定 Cookie：

```json
{
  "Cookie": "login_success_url=https://域名/;login_failure_url=https://域名/login/"
}
```
