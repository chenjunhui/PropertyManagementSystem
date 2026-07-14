## 项目简介

　　物业管理系统面向住宅小区日常运营，覆盖楼栋、房屋、业主档案、物业费账单、报修受理与公告通知等核心业务。管理端负责小区资源与工单处理，客户端支持业主注册、查看房屋信息、在线报修与查阅通知。采用 Spring Boot + Vue 3 前后端分离架构，内置演示数据，部署后即可体验完整业务流程。

![](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/admin-login.png)

---

## 技术栈与架构

- **架构**：前后端分离 · RESTful API · 三端部署（后端 2010 / 管理端 4010 / 客户端 3010）
- **后端技术栈**：Spring Boot 3.2 · Spring Web · Spring Data JPA · Hibernate · Bean Validation · Lombok · MySQL Connector/J · Maven · JDK 17
- **前端技术栈**：Vue 3 · Vue Router 4 · Pinia · Axios · Vite 5 · @iconify-icons/mdi · 组件化 SPA（管理端 4010 / 客户端 3010）
- **数据库**：MySQL 8 · utf8mb4 · 库名 `rb_pms`（建库 + 表结构 + Mock 数据一键脚本）
- **其他**：本地静态资源登录背景 · 文件上传（头像/附件）· 角色菜单权限 · CORS 跨域

---

## 功能模块

### 登录与注册

#### 客户端登录

会员通过客户端登录页进入个人中心，支持账号密码登录；未注册用户可跳转注册页完成会员开户。

![客户端登录页](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/client-login.png)

### 管理端

#### 数据概览

登录管理端后的首页模块，集中展示楼栋、房屋、在册业主与报修等核心指标，并通过图表直观呈现房屋分布与报修状态。

![数据概览](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/dashboard.png)

#### 楼栋管理

小区楼栋信息维护。该模块界面清晰、操作流程简单，适合日常楼栋管理场景使用。

![楼栋管理](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/building.png)

#### 房屋管理

房屋单元与面积管理。该模块界面清晰、操作流程简单，适合日常房屋管理场景使用。

![房屋管理](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/unit.png)

#### 业主管理

业主档案与房屋绑定。该模块界面清晰、操作流程简单，适合日常业主管理场景使用。

![业主管理](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/owner.png)

#### 物业费管理

物业费账单生成与缴费状态。该模块界面清晰、操作流程简单，适合日常物业费管理场景使用。

![物业费管理](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/property_fee.png)

#### 报修管理

报修受理与处理跟踪。该模块界面清晰、操作流程简单，适合日常报修管理场景使用。

![报修管理](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/repair.png)

#### 访客登记

访客来访记录与审批。该模块界面清晰、操作流程简单，适合日常访客登记场景使用。

![访客登记](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/visitor.png)

#### 公告通知

小区通知与物业公告发布。该模块界面清晰、操作流程简单，适合日常公告通知场景使用。

![公告通知](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/announce.png)

#### 巡检记录

公共区域巡检与整改记录。该模块界面清晰、操作流程简单，适合日常巡检记录场景使用。

![巡检记录](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/inspection.png)

### 客户端

#### 业主注册

客户端提供学生自助注册入口，填写用户名、学号、姓名、性别、班级及联系方式即可创建账号。注册成功后可直接登录使用个人中心与查询功能。

![业主注册](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/register.png)

#### 个人信息

学生登录后可查看并修改个人资料，包括姓名、手机、邮箱等；支持上传与更换头像。学号、性别等关键信息只读展示，避免误改。

![个人信息](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/profile.png)

#### 我的房屋

查看房屋与产权信息。该模块界面清晰、操作流程简单，适合日常我的房屋场景使用。

![我的房屋](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/my_property.png)

#### 报修申请

在线提交房屋设施报修。该模块界面清晰、操作流程简单，适合日常报修申请场景使用。

![报修申请](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/repair_apply.png)

#### 公告通知

查看小区通知与物业公告。该模块界面清晰、操作流程简单，适合日常公告通知场景使用。

![公告通知](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/announcements.png)

#### 访客预约

业主为访客提交来访预约。该模块界面清晰、操作流程简单，适合日常访客预约场景使用。

![访客预约](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/visitor_apply.png)

---

## 快速运行

1. 执行 `sql/` 目录下三个脚本初始化数据库
2. 启动后端：`cd backend && mvn spring-boot:run`
3. 启动管理端：`cd frontend-admin && npm install && npm run dev`
4. 启动客户端：`cd frontend-client && npm install && npm run dev`

**演示账号**

| 端 | 账号 | 密码 |
|----|------|------|
| 管理端 | admin | admin123 |
| 客户端 | student001 | 123456 |

---

## 获取源码

###### 关注公众号，点击下图进入小程序，即可获取完整项目源码。

![](https://ruobing-bucket.oss-cn-beijing.aliyuncs.com/articles/毕业设计/物业管理系统/admin-login.png)
