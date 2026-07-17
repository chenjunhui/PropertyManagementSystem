# 物业管理系统 — 产品需求文档 (PRD)

## 1. 项目概述

| 项 | 内容 |
|----|------|
| 项目名称 | 物业管理系统 (Property Management System) |
| 项目类型 | B/S 架构管理系统 + 业主自助客户端 |
| 目标用户 | 物业管理公司、小区业主 |
| 技术栈 | Spring Boot 3 + Vue 3 + MySQL 8 |
| 部署方式 | 开发：三端分离（后端 :2010 + 管理端 :4010 + 客户端 :3010）；生产：Nginx 负载均衡 + 多实例后端 + MySQL 主从 |

## 2. 用户角色

| 角色 | 代码 | 访问端 | 说明 |
|------|------|--------|------|
| 管理员 | ADMIN | 管理端 | 系统全功能权限，负责小区资源与工单管理 |
| 物业管家 | PROPERTY_MANAGER | 管理端 | 辅助管理，权限可配置 |
| 业主 | OWNER | 客户端 | 查看房屋、提交报修、预约访客、查看公告 |

## 3. 功能模块

### 3.1 认证与权限

#### 3.1.1 登录
- 管理端 / 客户端独立登录入口
- 支持用户名 + 密码登录
- 登录时校验角色与门户匹配（ADMIN/PROPERTY_MANAGER → 管理端，OWNER → 客户端）
- 登录成功返回用户信息 + 菜单权限
- Token 采用 UUID 生成（当前实现）

#### 3.1.2 业主注册
- 客户端提供自助注册入口
- 注册字段：用户名、业主编号、姓名、性别、手机号、邮箱、密码
- 校验：用户名唯一、业主编号唯一、手机号格式、邮箱格式
- 注册成功同时创建 `sys_user`（角色=OWNER）和 `owner` 记录

#### 3.1.3 菜单权限
- 基于角色的菜单权限控制（RBAC）
- `sys_role` → `sys_role_menu` → `sys_menu` 三级关联
- 前端路由守卫根据用户菜单动态控制可访问页面

### 3.2 管理端功能

#### 3.2.1 数据概览（Dashboard）
- 统计卡片：楼栋总数、房屋总数、在册业主、待处理报修、待缴账单、待审访客
- 图表展示：
  - 楼栋房屋分布（柱状图）
  - 报修状态分布（待处理/处理中/已完成）
  - 房屋状态分布（已入住/空置/装修中）
  - 物业费收缴状态（已缴/未缴）

#### 3.2.2 楼栋管理
- 楼栋信息 CRUD
- 字段：楼栋名称、楼栋编码（唯一）、楼层数、类型（住宅/商业/混合）、地址、描述、排序
- 支持列表展示与搜索

#### 3.2.3 房屋管理
- 房屋单元 CRUD
- 字段：所属楼栋、房号、楼层、户型、面积(㎡)、状态（已入住/空置/装修中）、备注
- 联合唯一约束：楼栋ID + 房号
- 外键关联楼栋，级联删除

#### 3.2.4 业主管理
- 业主档案 CRUD
- 字段：业主编号（唯一）、姓名、性别、手机号、邮箱、头像、关联用户、所属楼栋、绑定房屋、产权证号、所属单元、登记日期、状态（在册/已迁出）
- 支持业主与房屋绑定/解绑

#### 3.2.5 物业费管理
- 物业费账单 CRUD
- 字段：房间ID、账期(YYYY-MM)、物业费、公摊费、合计、缴费状态（未缴/已缴）
- 联合索引：房间ID + 账期
- 支持按账期、房间筛选

#### 3.2.6 报修管理
- 报修工单处理
- 字段：报修业主、房间、标题、问题描述、状态（待处理/处理中/已完成）、提交日期、完成日期、处理备注
- 状态流转：PENDING → PROCESSING → DONE
- 支持按状态筛选

#### 3.2.7 访客登记
- 访客来访记录管理
- 字段：业主ID、访客姓名、访客电话、来访日期、来访事由、状态（待审批/已通过/已拒绝）
- 审批功能：PENDING → APPROVED / REJECTED

#### 3.2.8 公告通知
- 公告发布管理
- 字段：标题、内容、发布日期、发布人、状态（已发布/草稿）
- 支持草稿保存与发布

#### 3.2.9 巡检记录
- 公共区域巡检管理
- 字段：房间ID、检查员、得分、检查日期、问题描述、结果（通过/需整改）
- 支持巡检结果记录

### 3.3 客户端功能

#### 3.3.1 首页
- 业主欢迎页，展示个人房屋概览

#### 3.3.2 个人信息
- 查看与修改个人资料（姓名、手机、邮箱）
- 头像上传与更换
- 学号、性别等关键信息只读

#### 3.3.3 我的房屋
- 查看绑定的房屋信息
- 展示：楼栋、房号、面积、户型、产权证号等

#### 3.3.4 报修申请
- 在线提交报修工单
- 填写：报修标题、问题描述
- 自动关联当前业主与房屋
- 提交后状态为 PENDING

#### 3.3.5 公告通知
- 查看已发布的公告列表
- 支持公告详情查看

#### 3.3.6 访客预约
- 业主为访客提交来访预约
- 填写：访客姓名、访客电话、来访日期、来访事由
- 自动关联当前业主
- 提交后状态为 PENDING，等待管理员审批

## 4. 数据库设计

### 4.1 核心表

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| `sys_user` | 系统用户 | username(唯一), password, role, status |
| `sys_role` | 角色定义 | role_code(唯一), role_name |
| `sys_menu` | 菜单定义 | parent_id, path, portal |
| `sys_role_menu` | 角色菜单关联 | role_id + menu_id(联合唯一) |
| `prop_building` | 楼栋 | building_code(唯一), floors, building_type |
| `prop_unit` | 房屋单元 | building_id(FK), unit_no, area_sqm, status |
| `owner` | 业主档案 | owner_no(唯一), user_id(FK), room_id(FK) |
| `property_fee` | 物业费账单 | room_id, bill_month, pay_status |
| `repair_request` | 报修申请 | owner_id(FK), room_id(FK), status |
| `visitor_record` | 访客登记 | owner_id(FK), visitor_name, status |
| `prop_announcement` | 公告通知 | title, content, status |
| `inspection_record` | 巡检记录 | room_id, inspector_name, score, result |

### 4.2 实体关系

```
sys_user ──1:1── owner
prop_building ──1:N── prop_unit
prop_unit ──1:N── owner (room_id)
owner ──1:N── repair_request
owner ──1:N── visitor_record
prop_unit ──1:N── property_fee
prop_unit ──1:N── inspection_record
sys_role ──N:N── sys_menu (通过 sys_role_menu)
```

## 5. API 设计

### 5.1 通用约定
- 基础路径：`/api`
- 响应格式：`{ code: 200, message: "success", data: T }`
- 错误响应：`{ code: 400, message: "错误信息", data: null }`
- 认证方式：JWT Bearer Token（通过 `Authorization: Bearer <token>` 传递）
- Access Token 过期时间：30 分钟
- Refresh Token 有效期：7 天

### 5.2 主要接口

| 模块 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 认证 | POST | /api/auth/login | 登录（返回 access + refresh token） |
| 认证 | POST | /api/auth/register | 业主注册 |
| 认证 | POST | /api/auth/refresh | 刷新 access token |
| 仪表盘 | GET | /api/dashboard/stats | 获取统计数据 |
| 楼栋 | CRUD | /api/buildings | 楼栋管理 |
| 房屋 | CRUD | /api/units | 房屋管理 |
| 业主 | CRUD | /api/owners | 业主管理 |
| 物业费 | CRUD | /api/property-fees | 物业费管理 |
| 报修 | CRUD | /api/repair-requests | 报修管理 |
| 访客 | CRUD | /api/visitor-records | 访客管理 |
| 公告 | CRUD | /api/announcements | 公告管理 |
| 巡检 | CRUD | /api/inspection-records | 巡检管理 |
| 菜单 | GET | /api/menus | 获取菜单权限 |
| 文件 | POST | /api/upload | 文件上传 |

## 6. 非功能需求

### 6.1 性能
- 支持多实例部署，通过 Nginx 负载均衡分发请求
- HikariCP 连接池优化：最小空闲 5、最大连接 20、泄漏检测 60s
- 前端静态资源 gzip 压缩 + 30 天缓存

### 6.2 安全
- 密码使用 BCrypt 加密存储
- JWT Token 认证，Access Token 30 分钟过期，Refresh Token 7 天有效
- CORS 限制：仅允许配置的前端域名
- 文件上传限制：5MB，白名单校验文件类型
- Actuator 健康检查端点仅允许内网访问

### 6.3 可用性
- 响应式布局，支持 PC 端
- 表单校验：必填项、格式校验
- 操作反馈：成功/失败提示
- 前端请求自动重试：网络错误 / 5xx 错误自动重试 2 次
- 网络断连提示：断网时全屏提示，恢复后自动刷新

### 6.4 数据
- 内置 Mock 数据，部署后可直接体验
- 数据库定时备份（mysqldump + gzip，保留 30 天）

### 6.5 高可用与可靠性

| 特性 | 实现方式 |
|------|----------|
| 后端多实例 | 3 个 Spring Boot 实例（2010/2011/2012），单实例故障不影响服务 |
| 负载均衡 | Nginx least_conn 策略 + 自动故障转移（proxy_next_upstream） |
| 数据库高可用 | MySQL 主从复制（GTID），主库故障可切换到从库 |
| 健康检查 | Spring Boot Actuator /actuator/health 端点，含数据库连接状态 |
| 自动重启 | health-check.sh + cron 每 5 分钟检测，挂掉自动拉起 |
| 结构化日志 | logback-spring.xml，生产环境 JSON 格式，按天滚动保留 30 天 |
| 慢查询告警 | AccessLogFilter 记录请求耗时，>2s 标记为 SLOW_REQUEST |
| 异常处理 | GlobalExceptionHandler 区分业务异常/参数异常/系统异常，统一返回格式 |

### 6.6 部署架构

```
用户请求 → Nginx(:80) → Backend-1(:2010)
                       → Backend-2(:2011)
                       → Backend-3(:2012)
                       → MySQL Master(:3306) ←复制→ MySQL Slave(:3307)
```

支持两种部署方式：
- **直接部署**：scripts/start-backend.ps1 启动多实例 + Nginx 反向代理
- **容器化部署**：docker-compose.yml 一键启动（需安装 Docker）

## 7. 设计规范

- 主色：`#059669`（Community Emerald）
- 字体：Noto Sans SC / Inter
- 布局：管理端左侧边栏 + 顶栏；客户端顶栏导航
- 圆角：8px（卡片）、6px（按钮/输入框）
- 阴影：`0 1px 3px rgba(15, 23, 42, 0.08)`

## 7.1 部署目录结构

```
PropertyManagementSystem/
├── 01-项目源码/
│   ├── backend/                    # Spring Boot 后端
│   ├── frontend-admin/             # 管理端前端 (port 4010)
│   └── frontend-client/            # 业主端前端 (port 3010)
├── 02-SQL完整脚本/                  # 数据库脚本
├── 03-deploy/                      # 部署配置
│   ├── nginx/nginx.conf            # Nginx 负载均衡配置
│   ├── mysql/                      # MySQL 主从配置
│   └── docker-compose.yml          # Docker 一键部署
├── 04*/                            # 部署文档
├── scripts/                        # 运维脚本
│   ├── start-backend.ps1           # 启动多实例
│   ├── backup-db.sh                # 数据库备份
│   └── health-check.sh             # 健康检查 + 自动重启
└── PRD.md                          # 产品需求文档
```

## 8. 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 物业管家 | propmgr01 | 123456 |
| 业主 | student001~006 | 123456 |

## 9. 已知限制

1. ~~**认证机制简单**：Token 为 UUID，无过期、无刷新机制~~ ✅ 已修复
2. ~~**密码明文存储**：未使用 BCrypt 等加密~~ ✅ 已修复
3. ~~**无操作日志**：关键操作无审计记录~~ ✅ 已修复（OperationLogService + AccessLogFilter）
4. ~~**无数据备份**：无自动备份策略~~ ✅ 已修复（backup-db.sh + cron 定时任务）
5. ~~**无高可用**：单实例部署，故障即停服~~ ✅ 已修复（Nginx 负载均衡 + 多实例 + MySQL 主从）
6. ~~**无单元测试**：后端无测试用例~~ ✅ 已修复（JUnit 5 + H2 内存数据库）
7. ~~**无国际化**：仅中文界面~~ ✅ 已修复（vue-i18n，支持中文/英文/日文）
