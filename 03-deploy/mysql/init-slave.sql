-- 在从库(SLAVE)上执行
-- 将 master_host/master_port/master_log_file/master_log_pos 替换为实际值

CHANGE MASTER TO
    MASTER_HOST='mysql-master',
    MASTER_PORT=3306,
    MASTER_USER='repl_user',
    MASTER_PASSWORD='repl_password_2024',
    MASTER_AUTO_POSITION=1;

START SLAVE;

-- 验证复制状态
SHOW SLAVE STATUS\G
