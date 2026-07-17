-- MySQL 主从复制初始化脚本
-- 在主库(MASTER)上执行

-- 1. 创建复制用户
CREATE USER IF NOT EXISTS 'repl_user'@'%' IDENTIFIED BY 'repl_password_2024';
GRANT REPLICATION SLAVE ON *.* TO 'repl_user'@'%';
FLUSH PRIVILEGES;

-- 2. 查看主库状态 (记录 File 和 Position)
SHOW MASTER STATUS;
