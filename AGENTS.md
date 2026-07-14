# AGENTS.md — Property Management System

## Project Overview

Auto-generated Property Management System (物业管理系统). Spring Boot 3 backend + two Vue 3 frontends + MySQL 8.

Source code lives under `01-项目源码/` (not at repo root).

## Ports & Database

| Service | Port | Notes |
|---------|------|-------|
| Backend API | 2010 | `http://localhost:2010/api/dashboard/stats` to verify |
| Admin frontend | 4010 | Account: `admin` / `admin123` |
| Client frontend | 3010 | Account: `student001` / `123456` |

Database: MySQL 8, name `rb_pms`, user `root`, password `123456`.

## Quick Start

```bash
# 1. Initialize database (from 02-SQL完整脚本/)
mysql -u root -p < 02-SQL完整脚本/00_full.sql

# 2. Start backend (requires Java 17, Maven)
cd 01-项目源码/backend && mvn spring-boot:run

# 3. Start admin frontend
cd 01-项目源码/frontend-admin && npm install && npm run dev

# 4. Start client frontend
cd 01-项目源码/frontend-client && npm install && npm run dev
```

## Architecture

**Backend** (`01-项目源码/backend/src/main/java/com/autogen/propmgmt/`):
- Standard Spring Boot layered: `controller` → `service` → `repository` → `entity`
- DTOs in `dto/`, common utilities in `common/` (Result wrapper, GlobalExceptionHandler)
- CORS configured for `localhost:4010` and `localhost:3010` only
- File uploads go to `${user.dir}/data/uploads`, served at `/resources/uploads/`
- `ddl-auto: none` — schema managed by SQL scripts, not Hibernate

**Frontend** (both `frontend-admin` and `frontend-client`):
- Vue 3 + Pinia + Vue Router + Axios
- API calls via `src/api/request.js` — axios instance with `baseURL: '/api'`
- Response interceptor unwraps `body.data` automatically (callers receive the payload, not the wrapper)
- Vite proxies `/api` and `/resources/uploads` to backend at port 2010

## Key Conventions

- All API responses wrapped in `Result<T>` with `code: 200` for success
- Frontend routes are permission-gated via `userStore.menus` (fetched from backend after login)
- Entity table names use prefixes: `sys_` (system), `prop_` (property), no prefix for business tables
- Package name is `propmgmt` (not `propertymanagement`)
- Design system primary color: `#059669` (Community Emerald style), reference: `design-system/MASTER.md`

## Test Accounts

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Teacher | teacher01 | 123456 |
| Student | student001~006 | 123456 |

## Gotchas

- Windows: use `python ../templates/system/meta/init_db.py sql/` instead of `mysql < file.sql` to avoid encoding issues
- Frontend `node_modules/` already present — `npm install` is optional if deps haven't changed
- No test suite in backend (`spring-boot-starter-test` in pom but no test files found)
- No linting or formatting configured for either frontend
