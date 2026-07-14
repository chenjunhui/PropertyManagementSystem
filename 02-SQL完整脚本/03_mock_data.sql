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
