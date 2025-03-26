---
title: Docker 应用分享
toc: true
indent: true
hiddenTitle: false
tag:
  - Docker
categories: 文档
description: >-
  本文分享了一些 Docker 应用，探索 Docker 应用的高效技巧，包括 RustDesk 远程控制、PT 一体化工具以及内网浏览器 Firefox 的配置，简化操作流程，提升用户体验。
date: '2025-03-26 08:55'
updated: '2025-03-26 08:55'
headimg: ../../img/article/25-03@Docker应用分享/Hexo博客封面.png
abbrlink: 3f9c44bf
---

一些 Docker 应用分享及使用配置附录，包含远程控制（RustDesk），PT 一体化工具（MoviePilot, Qbittorrent, Transmission, Audiobookshelf）和内网浏览器（Firefox）。

<!-- more -->

{% note quote::提示：根据个人的实际情况，自行替换配置文件中的路径、端口、口令等内容。 %}

## 远程控制

- RustDesk-s6：多端远程控制。

### Docker Compose

{% folding cyan, rustdesk/rustdesk-server-s6:latest %}

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
      - "RELAY=服务器IP:36013"
      - "ENCRYPTED_ONLY=1"
      - "PORT=36012"
    volumes:
      - /opt/docker/rustdesk-s6:/data
    restart: unless-stopped
    network_mode: bridge
```

{% endfolding %}

### 附录

- [rustdesk-s6](https://github.com/rustdesk/rustdesk-server#s6-overlay-based-images) 相较 rustdesk 镜像，s6 版单容器运行、简化配置 hbbs（ID 服务器）和 hbbr（中继服务器）。

- 关于环境变量`RELAY`，用于 ID 服务器告诉客户端中继服务器的地址^[当然你也可以不配置该变量，而是在客户端手动输入中继服务器的值。]。

- 关于客户端配置：

  - ID 服务器：*服务器:21115*（自动更换端口）。
  - 中继服务器：配置 *RELAY* 后可置空。
  - API 服务器：置空。
  - key：输入配置文件中自动生成的公钥文件 *id_ed25519.pub* 中的内容。

- 关于默认端口：

  - | 端口      | 协议    | 服务 | 用途                                     |
    | --------- | ------- | ---- | ---------------------------------------- |
    | 21114     | TCP     | hbbs | API（订阅用户专用）                      |
    | **21115** | TCP     | hbbs | ID 注册、管理                            |
    | **21116** | TCP/UDP | hbbs | NAT 穿透、打孔（可由环境变量`PORT`指定） |
    | **21117** | TCP     | hbbr | 服务器中继                               |
    | 21118     | TCP     | hbbs | 网页客户端（订阅用户专用）               |
    | 21119     | TCP     | hbbr | 网页客户端（订阅用户专用）               |

- 关于自定义端口

  - 综上，只需要关注 21115，21116，21117 这三个端口即可。
  - 环境变量`PORT`定义的是 NAT 端口，另外两个端口由该端口通过 +1, -1 自行计算得到^[信息来源：[rustdesk-server/issues/191#issuecomment-1942185062](https://github.com/rustdesk/rustdesk-server/issues/191#issuecomment-1942185062)]。
  - 桥接模式下，务必保持映射前后的端口一致。
  - 防火墙放行相应端口。

- 碎碎念，{% psw Rustdesk 的文档稀烂 %} 。

## PT 一体化工具

- MoviePilot：自动订阅、整理。
- Audiobookshelf：自托管有声书架。
- QBittorrent：常见 PT 站所允许的下载器。
- Transmission：带快校的下载（保种）器。

### Docker Compose

{% folding cyan, moviepilot-v2, qbittorrent, transmission, audiobookshelf %}

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

