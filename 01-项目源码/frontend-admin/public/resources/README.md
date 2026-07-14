# 项目静态资源



> 存放于各子项目 `resources/` 或 `public/resources/` 目录，路径以 `/resources/` 访问。



## 目录结构



```

resources/

├── images/

│   ├── login/          # 登录页背景

│   │   ├── client-bg.svg   # 客户端登录/注册背景

│   │   └── admin-bg.svg    # 管理端登录背景

│   └── avatars/        # 可选占位图（Mock 数据不再默认引用）

│       ├── default-admin.svg

│       ├── default-teacher.svg

│       └── default-student.svg

└── uploads/            # 用户上传（运行时生成，不在模板内）

```



## 使用说明



- 登录页背景使用 `client-bg.svg` / `admin-bg.svg`，可按项目替换为 PNG（需同步修改 Login.vue / Register.vue 中的 URL）

- 头像由用户上传，存 `/api/files/view/{filename}`；无头像时前端显示「暂无头像」

- 上传文件运行时保存至后端 `uploads/` 目录



## 部署位置



| 端 | 路径 |

|----|------|

| 后端静态 | `backend/src/main/resources/static/resources/` |

| 管理端 | `frontend-admin/public/resources/` |

| 客户端 | `frontend-client/public/resources/` |


