# 设计系统 MASTER — 教育管理系统

> 基于 UI UX Pro Max 设计原则，适用于毕业设计类「管理系统 + 客户端」双端项目。

## 目标产品

- **类型**：教育 / 高校管理系统
- **模式**：Data-Dense Dashboard + 客户端自助查询
- **风格**：Accessible & Ethical + Soft UI Evolution

## 色彩

| 角色 | 色值 | 用途 |
|------|------|------|
| Primary | `#1E40AF` | 主按钮、导航高亮、链接 |
| Secondary | `#3B82F6` | 次要操作、图标强调 |
| Success | `#059669` | 成功状态 |
| Warning | `#D97706` | 警告提示 |
| Danger | `#DC2626` | 删除、错误 |
| Background | `#F8FAFC` | 页面背景 |
| Surface | `#FFFFFF` | 卡片、面板 |
| Text Primary | `#1E293B` | 主文字 |
| Text Secondary | `#64748B` | 次要文字 |
| Border | `#E2E8F0` | 分割线、边框 |

## 字体

- **中文**：`"Noto Sans SC", "PingFang SC", "Microsoft YaHei", sans-serif`
- **英文/数字**：`Inter, system-ui, sans-serif`
- **标题**：600 weight
- **正文**：400 weight

## 布局

- **管理端**：左侧固定侧边栏（240px）+ 顶栏 + 内容区
- **客户端**：顶栏导航 + 居中内容（max-width 1200px）
- **圆角**：8px（卡片）、6px（按钮、输入框）
- **阴影**：`0 1px 3px rgba(15, 23, 42, 0.08)` 轻阴影

## 交互

- 过渡动画：150–250ms ease
- 所有可点击元素：`cursor: pointer`
- 悬停：背景/边框轻微变化，禁止剧烈缩放
- 表单：聚焦态可见边框高亮
- 响应式断点：375 / 768 / 1024 / 1440

## 反模式（避免）

- 不使用 emoji 作为功能图标（使用 SVG）
- 不使用 AI 紫粉渐变
- 管理端避免过度动画
- 保证文字对比度 ≥ 4.5:1

## 组件约定

- 表格：斑马纹可选，操作列右对齐
- 统计卡片：图标 + 数值 + 标签
- 空状态：简洁插画区 + 引导文案
- 登录页：左右分栏或居中卡片，教育蓝主色；背景图路径见 [08-系统模板开发规范](../../../ProjectDocumentation/08-系统模板开发规范.md) §2.2

## 头像与上传（v1.2）

- 无默认头像：Mock 与注册 `avatar` 为 NULL
- `AvatarUpload`：无头像显示「暂无头像」+「上传头像」；有头像显示预览 +「更换头像」
- **禁止**移除头像、恢复默认、列表展示头像列
- 详见 [08-系统模板开发规范 §3](../../../ProjectDocumentation/08-系统模板开发规范.md)
