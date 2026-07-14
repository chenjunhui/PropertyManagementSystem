-- 物业管理系统 — 数据库完整结构
-- 数据库：rb_pms · MySQL 8.x

USE `rb_pms`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `sys_role_menu`;
DROP TABLE IF EXISTS `sys_menu`;
DROP TABLE IF EXISTS `sys_role`;
DROP TABLE IF EXISTS `property_fee`;
DROP TABLE IF EXISTS `prop_announcement`;
DROP TABLE IF EXISTS `visitor_record`;
DROP TABLE IF EXISTS `inspection_record`;
DROP TABLE IF EXISTS `repair_request`;
DROP TABLE IF EXISTS `owner`;
DROP TABLE IF EXISTS `prop_unit`;
DROP TABLE IF EXISTS `prop_building`;
DROP TABLE IF EXISTS `grade`;
DROP TABLE IF EXISTS `enrollment`;
DROP TABLE IF EXISTS `student`;
DROP TABLE IF EXISTS `course`;
DROP TABLE IF EXISTS `class_info`;
DROP TABLE IF EXISTS `sys_user`;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `sys_user` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username`    VARCHAR(50)  NOT NULL COMMENT '登录账号',
  `password`    VARCHAR(100) NOT NULL COMMENT '登录密码',
  `role`        VARCHAR(20)  NOT NULL COMMENT '角色：ADMIN/PROPERTY_MANAGER/OWNER',
  `name`        VARCHAR(50)  NOT NULL COMMENT '姓名',
  `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
  `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar`      VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
  `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1启用 0禁用',
  `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户';

CREATE TABLE `prop_building` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `building_name` VARCHAR(50)  NOT NULL COMMENT '楼栋名称',
  `building_code` VARCHAR(20)  NOT NULL COMMENT '楼栋编码',
  `floors`        INT          NOT NULL DEFAULT 6 COMMENT '楼层数',
  `building_type`   VARCHAR(10)  DEFAULT '混合' COMMENT '类型：住宅/商业/混合',
  `address`       VARCHAR(200) DEFAULT NULL COMMENT '地址',
  `description`   VARCHAR(200) DEFAULT NULL COMMENT '描述',
  `sort_order`    INT          NOT NULL DEFAULT 0 COMMENT '排序',
  `created_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_building_code` (`building_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房屋楼栋';

CREATE TABLE `prop_unit` (
  `id`              BIGINT         NOT NULL AUTO_INCREMENT COMMENT '主键',
  `building_id`     BIGINT         NOT NULL COMMENT '楼栋ID',
  `unit_no`         VARCHAR(20)    NOT NULL COMMENT '房号',
  `floor`           INT            NOT NULL DEFAULT 1 COMMENT '楼层',
  `unit_type`       VARCHAR(30)    DEFAULT NULL COMMENT '户型',
  `area_sqm`        DECIMAL(8,2)   NOT NULL DEFAULT 80.00 COMMENT '面积(㎡)',
  `owner_count`     INT            NOT NULL DEFAULT 0 COMMENT '登记业主数',
  `status`          TINYINT        NOT NULL DEFAULT 1 COMMENT '1已入住 2空置 0装修中',
  `description`     VARCHAR(200)   DEFAULT NULL COMMENT '备注',
  `created_at`      DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_building_unit` (`building_id`, `unit_no`),
  KEY `idx_building_id` (`building_id`),
  CONSTRAINT `fk_unit_building` FOREIGN KEY (`building_id`) REFERENCES `prop_building` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房屋单元';

CREATE TABLE `owner` (
  `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `owner_no`        VARCHAR(20)  NOT NULL COMMENT '业主编号',
  `name`            VARCHAR(50)  NOT NULL COMMENT '姓名',
  `gender`          VARCHAR(10)  DEFAULT NULL COMMENT '性别',
  `phone`           VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
  `email`           VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar`          VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
  `user_id`         BIGINT       DEFAULT NULL COMMENT '关联用户ID',
  `building_id`     BIGINT       DEFAULT NULL COMMENT '楼栋ID',
  `room_id`         BIGINT       DEFAULT NULL COMMENT '房屋ID',
  `property_cert`   VARCHAR(30)  DEFAULT NULL COMMENT '产权证号',
  `unit_section`    VARCHAR(30)  DEFAULT NULL COMMENT '所属单元',
  `register_date`   DATE         DEFAULT NULL COMMENT '登记日期',
  `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '1在册 0已迁出',
  `created_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_owner_no` (`owner_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_room_id` (`room_id`),
  CONSTRAINT `fk_owner_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL,
  CONSTRAINT `fk_owner_room` FOREIGN KEY (`room_id`) REFERENCES `prop_unit` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='业主';

CREATE TABLE `repair_request` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `owner_id` BIGINT       NOT NULL COMMENT '报修业主ID',
  `room_id`     BIGINT       NOT NULL COMMENT '房间ID',
  `title`       VARCHAR(100) NOT NULL COMMENT '报修标题',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '问题描述',
  `status`      VARCHAR(20)  NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING/PROCESSING/DONE',
  `submit_date` DATE         NOT NULL COMMENT '提交日期',
  `finish_date` DATE         DEFAULT NULL COMMENT '完成日期',
  `remark`      VARCHAR(200) DEFAULT NULL COMMENT '处理备注',
  `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_owner_id` (`owner_id`),
  KEY `idx_room_id` (`room_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_repair_owner` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_repair_room` FOREIGN KEY (`room_id`) REFERENCES `prop_unit` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报修申请';

CREATE TABLE `inspection_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `room_id` BIGINT NOT NULL COMMENT '房间ID',
  `inspector_name` VARCHAR(50) DEFAULT NULL COMMENT '检查员',
  `score` INT NOT NULL COMMENT '得分',
  `check_date` DATE DEFAULT NULL COMMENT '检查日期',
  `issues` VARCHAR(500) DEFAULT NULL COMMENT '问题描述',
  `result` VARCHAR(20) DEFAULT 'PASS' COMMENT 'PASS/RECTIFY',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_room_id` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='巡检记录';

CREATE TABLE `visitor_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `owner_id` BIGINT NOT NULL COMMENT '业主ID',
  `visitor_name` VARCHAR(50) NOT NULL COMMENT '访客姓名',
  `visitor_phone` VARCHAR(20) DEFAULT NULL COMMENT '访客电话',
  `visit_date` DATE DEFAULT NULL COMMENT '来访日期',
  `reason` VARCHAR(200) DEFAULT NULL COMMENT '来访事由',
  `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING/APPROVED/REJECTED',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_owner_id` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='来访登记';

CREATE TABLE `prop_announcement` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(100) NOT NULL COMMENT '标题',
  `content` VARCHAR(1000) DEFAULT NULL COMMENT '内容',
  `publish_date` DATE DEFAULT NULL COMMENT '发布日期',
  `publisher` VARCHAR(50) DEFAULT NULL COMMENT '发布人',
  `status` VARCHAR(20) DEFAULT 'PUBLISHED' COMMENT 'PUBLISHED/DRAFT',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告通知';

CREATE TABLE `property_fee` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `room_id` BIGINT NOT NULL COMMENT '房间ID',
  `bill_month` VARCHAR(7) NOT NULL COMMENT '账期YYYY-MM',
  `management_fee` DECIMAL(10,2) DEFAULT NULL COMMENT '物业费',
  `public_fee` DECIMAL(10,2) DEFAULT NULL COMMENT '公摊费',
  `total_fee` DECIMAL(10,2) DEFAULT NULL COMMENT '合计',
  `pay_status` VARCHAR(20) DEFAULT 'UNPAID' COMMENT 'UNPAID/PAID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_room_month` (`room_id`, `bill_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='物业费账单';

CREATE TABLE `sys_role` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT,
  `role_code`   VARCHAR(20)  NOT NULL,
  `role_name`   VARCHAR(50)  NOT NULL,
  `description` VARCHAR(200) DEFAULT NULL,
  `status`      TINYINT      NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色';

CREATE TABLE `sys_menu` (
  `id`          BIGINT       NOT NULL AUTO_INCREMENT,
  `parent_id`   BIGINT       NOT NULL DEFAULT 0,
  `title`       VARCHAR(50)  NOT NULL,
  `path`        VARCHAR(100) NOT NULL,
  `icon`        VARCHAR(50)  DEFAULT NULL,
  `sort_order`  INT          NOT NULL DEFAULT 0,
  `portal`      VARCHAR(20)  NOT NULL DEFAULT 'admin',
  `status`      TINYINT      NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单';

CREATE TABLE `sys_role_menu` (
  `id`      BIGINT NOT NULL AUTO_INCREMENT,
  `role_id` BIGINT NOT NULL,
  `menu_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`),
  CONSTRAINT `fk_rm_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_rm_menu` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单';
