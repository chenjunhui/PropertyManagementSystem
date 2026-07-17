# MySQL 主从复制部署指南

## 一、架构说明

```
Master (主库) :3306  ──复制──▶  Slave (从库) :3307
    │                            │
    │  写入                      │  只读
    │  binlog                    │  relay-log
    └────────────────────────────┘
            GTID 自动同步
```

- **Master**：处理所有写操作，产生 binlog
- **Slave**：自动同步 Master 数据，可承担读操作
- **GTID**：全局事务标识，自动定位复制位点，故障切换更简单

## 二、前置条件

| 条件 | 说明 |
|------|------|
| MySQL 8.0+ | 两端版本一致 |
| 两个 MySQL 实例 | Master :3306，Slave :3307 |
| 端口未被占用 | 3306 和 3307 均可用 |

## 三、部署步骤

### 步骤 1：准备 MySQL 实例

**方式 A：两个独立 MySQL 安装**

如果你已有 MySQL 运行在 3306，可以再安装一个 MySQL 实例运行在 3307。

**方式 B：Docker 快速启动（推荐）**

```bash
# 启动主库
docker run -d --name mysql-master \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -e MYSQL_DATABASE=rb_pms \
  -v mysql-master-data:/var/lib/mysql \
  mysql:8.0

# 启动从库
docker run -d --name mysql-slave \
  -p 3307:3306 \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -v mysql-slave-data:/var/lib/mysql \
  mysql:8.0
```

### 步骤 2：配置主库（Master）

登录主库 MySQL：

```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
```

执行以下 SQL：

```sql
-- 1. 开启 GTID 模式
SET GLOBAL gtid_mode = ON;
SET GLOBAL enforce_gtid_consistency = ON;

-- 2. 开启 binlog（如果尚未开启）
-- 需要编辑 my.cnf/my.ini，添加以下配置后重启 MySQL：
-- [mysqld]
-- server-id=1
-- log-bin=mysql-bin
-- binlog-format=ROW
-- gtid-mode=ON
-- enforce-gtid-consistency=ON

-- 3. 创建复制用户
CREATE USER 'repl_user'@'%' IDENTIFIED BY 'repl_password_2024';
GRANT REPLICATION SLAVE ON *.* TO 'repl_user'@'%';
FLUSH PRIVILEGES;

-- 4. 查看主库状态（记录 File 和 Position）
SHOW MASTER STATUS;
```

输出示例：
```
+------------------+----------+--------------+------------------+-------------------------------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set                         |
+------------------+----------+--------------+------------------+-------------------------------------------+
| mysql-bin.000003 |      856 |              |                  | aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee:1-10 |
+------------------+----------+--------------+------------------+-------------------------------------------+
```

### 步骤 3：导入业务数据到主库

```bash
# 初始化数据库结构和数据
mysql -h 127.0.0.1 -P 3306 -u root -p < 02-SQL完整脚本/00_full.sql
```

### 步骤 4：配置从库（Slave）

登录从库 MySQL：

```bash
mysql -h 127.0.0.1 -P 3307 -u root -p
```

执行以下 SQL：

```sql
-- 1. 开启 GTID 模式
SET GLOBAL gtid_mode = ON;
SET GLOBAL enforce_gtid_consistency = ON;

-- 2. 配置主库信息
-- 将 MASTER_HOST 替换为实际的主库地址
-- Docker 内部用容器名：mysql-master
-- 本地用 127.0.0.1

CHANGE MASTER TO
    MASTER_HOST='127.0.0.1',
    MASTER_PORT=3306,
    MASTER_USER='repl_user',
    MASTER_PASSWORD='repl_password_2024',
    MASTER_AUTO_POSITION=1;

-- 3. 启动复制
START SLAVE;

-- 4. 验证复制状态
SHOW SLAVE STATUS\G
```

**关键检查项**：

```
Slave_IO_Running: Yes          ← IO 线程正常
Slave_SQL_Running: Yes         ← SQL 线程正常
Seconds_Behind_Master: 0       ← 复制延迟（秒）
Retrieved_Gtid_Set: ...        ← 已获取的 GTID
Executed_Gtid_Set: ...         ← 已执行的 GTID
```

> 如果 `Seconds_Behind_Master` 为 NULL 或 `Slave_IO_Running` 为 No，说明复制未正常工作，需要排查。

### 步骤 5：验证数据同步

**在主库写入测试数据**：

```bash
mysql -h 127.0.0.1 -P 3306 -u root -p rb_pms
```

```sql
INSERT INTO prop_building (building_name, building_code, floors)
VALUES ('测试楼栋', 'TEST01', 10);

SELECT * FROM prop_building WHERE building_code = 'TEST01';
```

**在从库查询**：

```bash
mysql -h 127.0.0.1 -P 3307 -u root -p rb_pms
```

```sql
SELECT * FROM prop_building WHERE building_code = 'TEST01';
-- 应能查到主库插入的数据
```

### 步骤 6：配置后端连接

修改 `application.yml`，或通过环境变量配置：

```yaml
spring:
  datasource:
    # 写操作连接主库
    url: jdbc:mysql://127.0.0.1:3306/rb_pms?...
    username: root
    password: 123456
```

> 后续如需读写分离，可引入 dynamic-datasource 实现读操作走从库。

## 四、常用运维命令

### 查看复制状态

```sql
-- 在从库执行
SHOW SLAVE STATUS\G

-- 关键指标
-- Slave_IO_Running: Yes
-- Slave_SQL_Running: Yes
-- Seconds_Behind_Master: 0
```

### 停止/启动复制

```sql
-- 在从库执行
STOP SLAVE;
START SLAVE;
```

### 跳过错误

```sql
-- 在从库执行（谨慎使用）
STOP SLAVE;
SET GLOBAL sql_slave_skip_counter = 1;
START SLAVE;
```

### 重置从库

```sql
-- 在从库执行（重新开始同步）
STOP SLAVE;
RESET SLAVE ALL;

-- 重新配置
CHANGE MASTER TO
    MASTER_HOST='127.0.0.1',
    MASTER_PORT=3306,
    MASTER_USER='repl_user',
    MASTER_PASSWORD='repl_password_2024',
    MASTER_AUTO_POSITION=1;
START SLAVE;
```

### 监控脚本

```bash
#!/bin/bash
# check-replication.sh - 检查复制状态

SLAVE_HOST=127.0.0.1
SLAVE_PORT=3307
SLAVE_USER=root
SLAVE_PASS=123456

STATUS=$(mysql -h $SLAVE_HOST -P $SLAVE_PORT -u $SLAVE_USER -p$SLAVE_PASS \
    -e "SHOW SLAVE STATUS\G" 2>/dev/null)

IO_RUNNING=$(echo "$STATUS" | grep "Slave_IO_Running:" | awk '{print $2}')
SQL_RUNNING=$(echo "$STATUS" | grep "Slave_SQL_Running:" | awk '{print $2}')
DELAY=$(echo "$STATUS" | grep "Seconds_Behind_Master:" | awk '{print $2}')

if [ "$IO_RUNNING" = "Yes" ] && [ "$SQL_RUNNING" = "Yes" ]; then
    echo "[$(date)] Replication OK (delay: ${DELAY}s)"
else
    echo "[$(date)] REPLICATION BROKEN! IO=$IO_RUNNING SQL=$SQL_RUNNING"
fi
```

配合 cron 定期检查：

```bash
# 每分钟检查一次
* * * * * /path/to/check-replication.sh >> /var/log/replication.log 2>&1
```

## 五、故障排查

| 问题 | 原因 | 解决方案 |
|------|------|----------|
| IO_Running: No | 网络不通 / 用户权限 / 密码错误 | 检查 MASTER_HOST、用户权限、防火墙 |
| SQL_Running: No | 主从数据不一致 | 查看 `Last_SQL_Error`，手动修复后跳过 |
| Seconds_Behind: NULL | 复制未启动 | 执行 `START SLAVE` |
| 延迟很大 | 从库性能不足 / 大事务 | 优化从库配置，拆分大事务 |
| Duplicate entry | 主键冲突 | 设置 `sql_slave_skip_counter` 或修复数据 |

## 六、安全建议

1. **修改默认密码**：`repl_password_2024` 改为强密码
2. **限制复制用户权限**：仅授予 `REPLICATION SLAVE`
3. **防火墙**：仅允许应用服务器访问 3306/3307
4. **SSL 复制**：生产环境建议启用 SSL 加密复制

```sql
-- 创建 SSL 复制用户
CREATE USER 'repl_user'@'%' IDENTIFIED BY '强密码' REQUIRE SSL;
GRANT REPLICATION SLAVE ON *.* TO 'repl_user'@'%';
```

## 七、后续优化

完成主从复制后，可进一步实施：

| 优化项 | 说明 |
|--------|------|
| 读写分离 | 引入 dynamic-datasource，读操作走从库 |
| 自动切换 | Master 故障时手动/自动将 Slave 提升为新 Master |
| 多从库 | 增加更多 Slave 分担读压力 |
| 半同步复制 | 确保主库写入至少一个从库后才返回成功 |
| 监控告警 | Prometheus + Grafana 监控复制延迟 |
