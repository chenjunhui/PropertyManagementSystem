-- 物业管理系统 — 完整 SQL（建库 + 表结构 + Mock 数据）
-- 生成时间: 2026-06-13 10:20:16
-- 数据库: rb_pms

-- 物业管理系统 — 创建数据库
-- MySQL 8.x · 账号 root

CREATE DATABASE IF NOT EXISTS `rb_pms`
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE `rb_pms`;


-- ============================================================
-- 来源: 02_schema.sql
-- ============================================================

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


-- ============================================================
-- 来源: 03_mock_data.sql
-- ============================================================

-- 物业管理系统 — Mock 测试数据
-- 默认登录：admin/admin123 · propmgr01/123456 · owner001/123456

USE `rb_pms`;

SET NAMES utf8mb4;

INSERT INTO `sys_user` (`username`, `password`, `role`, `name`, `phone`, `email`, `avatar`, `status`) VALUES
('admin',      'admin123', 'ADMIN',            '系统管理员', '13900000000', 'admin@property.com',      NULL, 1),
('propmgr01',  '123456',   'PROPERTY_MANAGER', '张管家',     '13900000001', 'propmgr01@property.com',  NULL, 1),
('owner001',   '123456',   'OWNER',            '李明',       '13800000001', 'liming@property.com',     NULL, 1),
('owner002',   '123456',   'OWNER',            '王芳',       '13800000002', 'wangfang@property.com',   NULL, 1),
('owner003',   '123456',   'OWNER',            '赵强',       '13800000003', 'zhaoqiang@property.com',  NULL, 1),
('owner004',   '123456',   'OWNER',            '刘洋',       '13800000004', 'liuyang@property.com',    NULL, 1),
('owner005',   '123456',   'OWNER',            '陈静',       '13800000005', 'chenjing@property.com',   NULL, 1),
('owner006',   '123456',   'OWNER',            '周磊',       '13800000006', 'zhoulei@property.com',    NULL, 1);

INSERT INTO `prop_building` (`building_name`, `building_code`, `floors`, `building_type`, `address`, `description`, `sort_order`) VALUES
('翠湖苑1号楼', 'CH1', 18, '住宅', '翠湖苑小区东区', '高层住宅', 1),
('翠湖苑2号楼', 'CH2', 18, '住宅', '翠湖苑小区东区', '高层住宅', 2),
('翠湖苑商业楼', 'CH_B', 6, '商业', '翠湖苑小区南门', '底商与办公', 3);

INSERT INTO `prop_unit` (`building_id`, `unit_no`, `floor`, `unit_type`, `area_sqm`, `owner_count`, `status`, `description`) VALUES
(1, '1-101', 1, '两室一厅', 89.00, 2, 1, '南向'),
(1, '1-102', 1, '一室一厅', 76.00, 1, 1, '北向'),
(1, '12-01', 12, '复式', 120.00, 1, 1, '顶层复式'),
(1, '8-803', 8, '两室一厅', 89.00, 1, 1, NULL),
(2, '5-501', 5, '三室两厅', 95.00, 1, 1, NULL),
(2, '5-502', 5, '三室两厅', 95.00, 1, 1, NULL),
(3, 'B101', 1, '商铺', 150.00, 1, 1, '临街商铺'),
(3, 'B102', 1, '商铺', 80.00, 0, 0, '装修中');

INSERT INTO `owner` (`owner_no`, `name`, `gender`, `phone`, `email`, `avatar`, `user_id`, `building_id`, `room_id`, `property_cert`, `unit_section`, `register_date`, `status`) VALUES
('OWN2024001', '李明',   '男', '13800000001', 'liming@property.com',    NULL, 3, 1, 1, '湘(2022)不动产权第001号', '1单元', '2022-06-01', 1),
('OWN2024002', '王芳',   '女', '13800000002', 'wangfang@property.com',  NULL, 4, 2, 5, '湘(2021)不动产权第002号', '5单元', '2021-03-15', 1),
('OWN2024003', '赵强',   '男', '13800000003', 'zhaoqiang@property.com', NULL, 5, 1, 1, '湘(2022)不动产权第003号', '1单元', '2022-06-01', 1),
('OWN2024004', '刘洋',   '男', '13800000004', 'liuyang@property.com',   NULL, 6, 1, 3, '湘(2020)不动产权第004号', '12单元', '2020-09-01', 1),
('OWN2024005', '陈静',   '女', '13800000005', 'chenjing@property.com',  NULL, 7, 2, 6, '湘(2021)不动产权第005号', '5单元', '2021-03-15', 1),
('OWN2024006', '周磊',   '男', '13800000006', 'zhoulei@property.com',   NULL, 8, 1, 4, '湘(2023)不动产权第006号', '8单元', '2023-01-10', 1);

INSERT INTO `repair_request` (`owner_id`, `room_id`, `title`, `description`, `status`, `submit_date`, `finish_date`, `remark`) VALUES
(1, 1, '水龙头漏水', '厨房洗菜池水龙头持续滴水', 'PENDING',    '2025-03-01', NULL,         NULL),
(1, 1, '空调不制冷', '客厅空调开启后无冷风',       'PROCESSING', '2025-02-20', NULL,         '已联系维修师傅'),
(3, 1, '门锁损坏',   '入户门锁难以正常开启',       'DONE',       '2025-02-10', '2025-02-12', '已更换锁芯'),
(4, 3, '灯管闪烁',   '书房灯管频繁闪烁',           'PENDING',    '2025-03-05', NULL,         NULL),
(5, 6, '网络故障',   '家中宽带无法连接',           'PROCESSING', '2025-03-08', NULL,         NULL);

INSERT INTO `sys_role` (`role_code`, `role_name`, `description`, `status`) VALUES
('ADMIN',            '管理员',   '系统全部权限',           1),
('PROPERTY_MANAGER', '物业管家', '管理楼栋、房屋与报修',   1),
('OWNER',            '业主',     '客户端自助服务',         1);

INSERT INTO `sys_menu` (`parent_id`, `title`, `path`, `icon`, `sort_order`, `portal`, `status`) VALUES
(0, '数据概览', '/dashboard',          'dashboard',  1, 'admin', 1),
(0, '楼栋管理', '/buildings',          'buildings',  2, 'admin', 1),
(0, '房屋管理', '/units',              'rooms',      3, 'admin', 1),
(0, '业主管理', '/owners',             'owners',     4, 'admin', 1),
(0, '报修管理', '/repairs',            'repairs',    5, 'admin', 1),
(0, '巡检记录', '/inspection-records', 'hygiene',    6, 'admin', 1),
(0, '访客登记', '/visitors',           'visitors',   7, 'admin', 1),
(0, '公告通知', '/announcements',      'announce',   8, 'admin', 1),
(0, '物业费管理', '/property-fees',    'utility',    9, 'admin', 1),
(0, '个人资料', '/profile',            'profile',   10, 'admin', 1);

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`)
SELECT 1, id FROM `sys_menu` WHERE `portal` = 'admin';

INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7), (2, 8), (2, 9), (2, 10);

INSERT INTO `inspection_record` (`room_id`, `inspector_name`, `score`, `check_date`, `issues`, `result`) VALUES
(1, '张管家', 92, '2025-03-01', NULL, 'PASS'),
(2, '张管家', 78, '2025-03-01', '消防通道堆放杂物', 'RECTIFY');

INSERT INTO `visitor_record` (`owner_id`, `visitor_name`, `visitor_phone`, `visit_date`, `reason`, `status`) VALUES
(1, '李父', '13700000001', '2025-03-10', '亲属探望', 'APPROVED'),
(2, '王同事', '13700000002', '2025-03-12', '朋友来访', 'PENDING');

INSERT INTO `prop_announcement` (`title`, `content`, `publish_date`, `publisher`, `status`) VALUES
('春季消防演练通知', '请于3月20日参加小区消防演练，届时请配合物业安排。', '2025-03-01', '物业中心', 'PUBLISHED'),
('电梯维保公告', '3月15日-17日1号楼电梯例行维保，请业主错峰出行。', '2025-03-05', '物业中心', 'PUBLISHED');

INSERT INTO `property_fee` (`room_id`, `bill_month`, `management_fee`, `public_fee`, `total_fee`, `pay_status`) VALUES
(1, '2025-02', 280.00, 120.00, 400.00, 'PAID'),
(1, '2025-03', 280.00, 135.50, 415.50, 'UNPAID'),
(5, '2025-03', 320.00, 98.00, 418.00, 'UNPAID');

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
