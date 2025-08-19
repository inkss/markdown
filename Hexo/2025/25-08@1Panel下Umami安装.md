---
title: 1Panel + MariaDB 安装 Umami 报错实录：问题分析与解决方案
toc: true
indent: true
tag:
  - Umami
  - 网站统计
  - 数据分析
categories: 工具
description: >-
  本文详细介绍在 1Panel 面板中使用 MariaDB 安装 Umami 的完整步骤，解决数据库连接失败和迁移报错问题，实现无 MySQL 数据只有
  MariaDB 数据库的 Umami 安装。
date: '2025-08-19 17:20'
updated: '2025-08-19 17:20'
copyright:
  type: type5
headimg: ../../img/article/25-08@1Panel下Umami安装/Hexo博客封面.png
background: ../../img/background/wallhaven-k778dq.jpg
abbrlink: 9eba8252
---

在 1Panel 的应用商店中，Umami 镜像默认支持的数据库为 MySQL 和 PostgreSQL。但个人常用的数据库是 MariaDB，尽管理论上它应与 MySQL 完全兼容，但在实际安装过程中仍出现了报错。

<!-- more -->

## 问题一：数据库连接失败

首先是连接信息，如果在不做任何修改的情况下启动 Umami 容器，将会得到如下报错：

```shell
$ node scripts/check-db.js
✓ DATABASE_URL is defined.
✗ Unable to connect to the database: Authentication failed against database server, the provided database credentials for `umami` are not valid.

Please make sure to provide valid database credentials for the database server at the configured address.
```

根据应用的 Compose 文件，可以得知 `DATABASE_URL` 的定义如下：

```yaml
DATABASE_URL: ${PANEL_DB_TYPE}://${PANEL_DB_USER}:${PANEL_DB_USER_PASSWORD}@${PANEL_DB_HOST}:${PANEL_DB_PORT}/${PANEL_DB_NAME}
```

而在数据库服务选择了 MySQL 后，`PANEL_DB_HOST` 将被设置为 `mysql`，除非你的 MariaDB 数据库镜像的容器名称设置的也是改值，否则启动镜像后 Umami 将会无法连接到数据库。我们可以点击高级设置 - 编辑 compose 文件，手动指定连接信息为 MariaDB 容器的名称，也可以进入 Umami 的安装目录，修改 `.env` 文件，更改 `PANEL_DB_HOST` 的值。两者皆可，根据个人习惯选择。

## 问题二：迁移失败（函数不存在）

在解决完数据库连接 URL 后，继续重新启动，又会报出一个新的错误：

```shell
Error: P3018

A migration failed to apply. New migrations cannot be applied before the error is recovered from. Read more about how to resolve migration issues in a production database: https://pris.ly/d/migrate-resolve

Migration name: 05_add_visit_id
Database error code: 1305
Database error:
FUNCTION umami.BIN_TO_UUID does not exist

Please check the query number 2 from the migration file.
```

这是由于 MariaDB 和 MySQL 的差异所导致的报错，`BIN_TO_UUID` 是 MySQL 8.0+ 内置的函数（用于将二进制数据转换为 UUID 字符串）。MariaDB (v11.8.3) 没有该函数，但我们可以手动创建。

首先清空 Umami 数据库，确保其不存在未完成的错误迁移记录，接着连接到数据库执行以下 SQL 语句手动创建该函数：

```sql
-- 切换到 umami 数据库（替换为你的实际数据库名）
USE umami;

-- 创建 BIN_TO_UUID 函数
DELIMITER //
CREATE FUNCTION BIN_TO_UUID(bin_uuid BINARY(16)) 
RETURNS CHAR(36)
DETERMINISTIC
BEGIN
    DECLARE hex_str CHAR(32);
    SET hex_str = HEX(bin_uuid);
    RETURN LOWER(CONCAT(
        SUBSTRING(hex_str, 1, 8), '-',
        SUBSTRING(hex_str, 9, 4), '-',
        SUBSTRING(hex_str, 13, 4), '-',
        SUBSTRING(hex_str, 17, 4), '-',
        SUBSTRING(hex_str, 21, 12)
    ));
END //
DELIMITER ;
```

## 总结

至此，配置已完成、问题解决😄。经过上述两项调整后，即可在 1Panel 面板中使用 MariaDB 成功部署 Umami 应用。
