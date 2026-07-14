-- 操作日志表
CREATE TABLE IF NOT EXISTS `sys_operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `module` VARCHAR(50) NOT NULL COMMENT '模块',
  `operation` VARCHAR(100) NOT NULL COMMENT '操作',
  `operator` VARCHAR(50) COMMENT '操作人',
  `operator_role` VARCHAR(20) COMMENT '操作人角色',
  `detail` VARCHAR(255) COMMENT '详情',
  `ip` VARCHAR(50) COMMENT 'IP地址',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态 1成功 0失败',
  `error_msg` VARCHAR(500) COMMENT '错误信息',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `idx_created_at` (`created_at`),
  INDEX `idx_module` (`module`),
  INDEX `idx_operator` (`operator`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';
