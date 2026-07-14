# 项目进度记录

## 已完成

### 1. AGENTS.md
- 为未来 OpenCode 会话创建了项目说明文件

### 2. 角色命名修复
- 修复 `PROJECT_DOC.md`、`system-definition.json`、`Profile.vue` 中的角色名称
- 教师→物业管家，学生→业主

### 3. 密码加密
- 添加 `spring-security-crypto` 依赖
- 创建 `PasswordConfig` 提供 BCryptPasswordEncoder
- 创建 `DataInitializer` 自动迁移明文密码
- 更新 `AuthService` 和 `RegisterService` 使用 BCrypt

### 4. JWT Token 机制
- 添加 `jjwt` 依赖
- 创建 `JwtTokenUtil` 工具类
- 创建 `JwtAuthenticationFilter` 拦截器
- 实现 refresh token 端点
- 前端添加 token 自动刷新（80% 过期时刷新）
- Access Token 30 分钟过期，Refresh Token 7 天有效

### 5. 国际化 (i18n) - 三语支持
- **后端**：
  - 创建 `I18nConfig` 配置 MessageSource + LocaleResolver
  - 创建 `messages/` 目录，包含 zh_CN、en、ja 三个 properties 文件
  - 更新 14 个 Service 类 + JwtAuthenticationFilter 使用 MessageSource
  - DashboardService 返回 status key 而非中文标签

- **前端 Admin**：
  - 安装 vue-i18n（使用 --legacy-peer-deps）
  - 创建 i18n 配置和 locale 文件（zh-CN、en、ja）
  - 创建 LangSwitcher 组件
  - 更新所有 16 个 Vue 视图使用 $t() 调用
  - 更新 request.js 使用 i18n 错误消息
  - 修复 AdminLayout 导航菜单翻译（menuTitleMap）

- **前端 Client**：
  - 安装 vue-i18n
  - 创建 i18n 配置和 locale 文件
  - 创建 LangSwitcher 组件
  - 更新所有 9 个 Vue 视图使用 $t() 调用
  - 修复 ClientLayout 导航标签（computed 替代静态数组）
  - 添加 LangSwitcher 到登录和注册页面

### 6. 多色主题功能（Admin 前端完成）
- 创建 `stores/theme.js` 主题状态管理
- 创建 `ThemeSwitcher.vue` 主题切换组件
- 5 种主题：翡翠绿、天空蓝、优雅紫、玫瑰红、琥珀橙
- 主题通过 localStorage 持久化
- 集成到 AdminLayout 侧边栏

## 待完成

### 1. 客户端主题集成
- ✅ 完成：main.js 初始化 theme store
- ✅ 完成：ThemeSwitcher 组件集成到 ClientLayout
- ✅ 完成：global.css 使用主题 CSS 变量
- ✅ 完成：右上角毛玻璃效果控件
- ✅ 完成：登录/注册页面背景跟随主题

### 2. 可选优化
- ✅ 删除未使用的教育模板文件（Teachers.vue、Attendance.vue、LeaveRequests.vue）
- ✅ 添加操作日志
  - 创建 `OperationLog` 实体
  - 创建 `OperationLogRepository`
  - 创建 `OperationLogService` 记录操作
  - 创建 `OperationLogController` 查询接口
  - 创建 SQL 迁移脚本 `04_operation_log.sql`
  - AuthService 添加登录日志
  - RepairRequestService 添加报修日志
  - OwnerService 添加业主管理日志
  - Admin 前端创建 Logs.vue 日志查看页面
  - 添加三语 i18n 翻译（log.time/module/operation/operator/detail/success/fail）
- ⏳ 添加单元测试（待定）

## 关键文件路径

### 后端
- `01-项目源码/backend/src/main/java/com/autogen/propmgmt/`
  - `config/I18nConfig.java` — i18n 配置
  - `config/PasswordConfig.java` — BCrypt 配置
  - `config/DataInitializer.java` — 密码迁移
  - `util/JwtTokenUtil.java` — JWT 工具
  - `config/JwtAuthenticationFilter.java` — JWT 拦截器
  - `resources/messages/messages_*.properties` — 国际化消息

### 前端 Admin
- `01-项目源码/frontend-admin/src/`
  - `i18n/` — i18n 配置和 locale 文件
  - `stores/theme.js` — 主题状态管理
  - `components/LangSwitcher.vue` — 语言切换
  - `components/ThemeSwitcher.vue` — 主题切换

### 前端 Client
- `01-项目源码/frontend-client/src/`
  - `i18n/` — i18n 配置和 locale 文件
  - `stores/theme.js` — 主题状态管理（已创建，未集成）
  - `components/LangSwitcher.vue` — 语言切换
  - `components/ThemeSwitcher.vue` — 主题切换（已创建，未集成）

## 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 物业管家 | propmgr01 | 123456 |
| 业主 | student001~006 | 123456 |

## 端口

| 服务 | 端口 |
|------|------|
| 后端 API | 2010 |
| 管理端 | 4010 |
| 客户端 | 3010 |
