# 物业管理系统 — 运行指南

> 目录：`output/PropertyManagementSystem/` · 由 AutomatedGeneration 模板自动生成

## 端口与数据库

| 项 | 值 |
|----|-----|
| 项目目录 | `PropertyManagementSystem` |
| 后端端口 | 2010 |
| 管理端端口 | 4010 |
| 客户端端口 | 3010 |
| 数据库名 | `rb_pms`（MySQL 8） |

## 一、初始化数据库（必须先执行）

在 MySQL 8 中按顺序执行 `sql/` 目录下脚本：

```bash
mysql -u root -p < sql/01_create_database.sql
mysql -u root -p < sql/02_schema.sql
mysql -u root -p < sql/03_mock_data.sql
```

或在项目目录执行（推荐，避免 Windows 乱码）：

```bash
pip install pymysql
python ../templates/system/meta/init_db.py sql/
```

## Mock 数据账号

## 二、启动服务（需人工检验）

### 1. 后端

```bash
cd backend
# 需要 Java 17
mvn spring-boot:run
```

验证：访问 http://localhost:2010/api/dashboard/stats

API文档
- **Swagger UI**：http://localhost:2010/swagger-ui/index.html
- **OpenAPI JSON**：http://localhost:2010/v3/api-docs

### 2. 管理端

```bash
cd frontend-admin
npm install
npm run dev
```

访问 http://localhost:4010 ，账号 `admin` / `admin123`

### 3. 客户端

```bash
cd frontend-client
npm install
npm run dev
```

访问 http://localhost:3010 ，账号 `student001` / `123456`

## 三、人工检验清单

生成后请人工启动三端并完成以下检验，记录问题后再优化模板：

- [ ] 数据库脚本执行成功，表与初始数据正确
- [ ] 后端启动无报错，API 可访问
- [ ] 管理端页面正常，各模块 CRUD 可用
- [ ] 客户端页面正常，个人信息/课程/成绩可查看
- [ ] 端口、数据库名与配置一致

## 项目结构

```
PropertyManagementSystem/
├── backend/              # Spring Boot API
├── frontend-admin/       # 管理端 Vue
├── frontend-client/      # 客户端 Vue
├── sql/                  # MySQL 建库建表与初始数据
├── design-system/        # UI 设计规范
├── docs/                 # 项目文档
└── system-definition.json
```

## 技术栈

Spring Boot 3 + Vue 3 + MySQL 8
