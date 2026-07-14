# 数据库脚本说明

> 项目：物业管理系统 · 数据库：`rb_pms` · MySQL 8.x

## 文件清单

| 文件 | 说明 |
|------|------|
| `01_create_database.sql` | 创建数据库 |
| `02_schema.sql` | **完整表结构**（6 张表、外键、索引、注释） |
| `03_mock_data.sql` | **Mock 测试数据**（用户/班级/业主/课程/选课/成绩） |

## 执行顺序

```bash
mysql -u root -p < 01_create_database.sql
mysql -u root -p < 02_schema.sql
mysql -u root -p < 03_mock_data.sql
```

## 表结构概览

```
sys_user ──┬──> student ──┬──> enrollment ──> course
           │              └──> grade      ──> course
class_info ──> student
```

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| sys_user | 系统用户 | username, role, name |
| class_info | 班级 | class_name, grade, major |
| student | 业主 | owner_no, name, class_id, user_id |
| course | 课程 | course_code, course_name, credit |
| enrollment | 选课 | student_id, course_id, semester |
| grade | 成绩 | student_id, course_id, score, grade_level |

## Mock 数据账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | admin123 |
| 教师 | teacher01 | 123456 |
| 业主 | owner001 ~ student006 | 123456 |

## 连接配置

```yaml
spring.datasource.url: jdbc:mysql://localhost:3306/rb_pms
spring.datasource.username: root
spring.datasource.password: 123456
```
