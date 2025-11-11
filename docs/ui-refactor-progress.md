# 前端页面重构进度（2025Q1）

## 1. 目标
- 视觉一致：全局色板/阴影/圆角/密度依赖 design tokens，统一页头、操作区和卡片骨架。
- 操作体验升级：表格页支持显式工具栏、检索折叠、树+表、梯度卡片等现代交互。
- 性能与可维护性：引入更轻的自动刷新/懒加载策略，减少内联样式和重复逻辑，向 `TableMain/TableSearch` 集中能力。

## 2. 已完成页面
| 模块 | 路径 | 说明 |
| --- | --- | --- |
| 首页 | `views/index/main.vue`, `views/index/work-place.vue` | 新骨架 + 胶囊指标 + 面板化图表 |
| 个人中心 | `views/personal/index.vue` | Hero/待办/消息重构，使用 design token |
| 监控 | `views/monitor/system-monitor.vue`, `online.vue`, `job.vue`, `druid.vue` | 统一页头 & 卡片布局，自动刷新逻辑重写 |
| 日志 | `views/log/loginlog.vue`, `jobLog.vue`, `operationLog.vue` | 表格页头+刷新/导出入口，卡片化搜索 |
| 字典 | `views/system/dict/SysDict.vue`, `dictType.vue`, `dictData.vue` | segment Tabs + 卡片化表格/操作区 |
| 用户 | `views/system/user/SysUser.vue` | 新页头、部门面板、卡片表格，模态样式更新 |
| 通用组件 | `components/table/search/TableSearch.vue` | 样式迁移到 design token，重置折叠交互 |

## 3. 待完成页面
| 状态 | 模块/页面 | 备注 |
| --- | --- | --- |
| TODO | `views/system/dept/SysDept.vue` | 表格+树仍为旧布局，需要重构页头、操作按钮+卡片式表单 |
| TODO | `views/system/role/SysRole.vue` | 搜索/表格/弹窗保持旧样式，需迁移至统一结构 |
| TODO | `views/system/menu/SysMenu.vue` | 交互复杂，需将树+详情拆分为主副面板并套用 design token |
| TODO | `views/project/infomation.vue` | 目前仅展示依赖，需要重写为信息面板（版本/依赖卡片） |
| TODO | 异常页 `views/exception/{403,404,500}.vue` | 仍使用旧模板，需统一空状态视觉 |
| TODO | 登录/重定向 `views/login/index.vue`, `login/LoginView.vue`, `views/redirect/index.vue` | 迁移到新的 login shell（渐变背景、表单卡片） |
| TODO | 其它：`views/system/dict/dictType.vue` 子表单体验（细节已部分优化但需继续） | 统一 form/modal 规范 |
| TODO | 通用组件：`TableMain.vue` | 调整工具栏、密度、全屏逻辑；暴露 hooks 支持更灵活操作 |

> 页面统计：共 17 个视图文件，已完成 11 个，剩余 6 个核心页面 + 登录/异常等基础页。

## 4. 下一步计划
1. **System 模块**：优先完成 `dept/menu/role` 三个页面，复用用户页布局（页头+树/表/详情），同步抽离公用树形卡片样式。
2. **基础页**：重构 `project/infomation`、登录、异常页，统一为空状态/品牌化体验。
3. **组件提升**：在页面全部迁移后，检视 `TableMain`、`TableSearch`、`TableConfig` 的交互（状态保存、密度、折叠、工具栏插槽）并产出 v2 计划。
