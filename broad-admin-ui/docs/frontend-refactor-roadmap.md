# Broad Admin 前端现代化改造路线

## 1. 背景与目标

- **现状问题**：组件样式分散、布局逻辑耦合严重（如 `src/components/Layout.vue` 内部直接处理响应式/模式切换），主题变量与 Naive UI 主题配置割裂，响应式体验与可访问性不足。
- **总体目标**：基于现有主题体系快速迭代，打造响应及时、主题统一、易于扩展的 App Shell，逐步迁移子模块，减少全局状态耦合并提升性能。

## 2. 现状诊断（2024-12）

| 维度 | 痛点 | 影响 |
| --- | --- | --- |
| 布局 | 布局模式（LTR/TTB/LCR）与设备判断混杂在 `Layout.vue`，缺少语义化容器与断点体系 | 难以扩展/定制，移动端体验卡顿 |
| 主题 | `App.vue` 只透传 primary 色，`styles/variables.scss` 中的尺寸/颜色无法动态更新 | 难以主题切换，无法实现品牌定制 |
| 性能 | 宽度监听依赖 `window.resize` + DOM 操作，缺乏最小重绘策略；部分组件全量渲染 | 页面交互延迟、低端设备卡顿 |
| 结构 | `components` 下布局/业务杂糅，缺少 `layouts` 级别的清晰模块边界 | 难以进行渐进式重构 |

## 3. 设计原则

1. **主题即设计令牌（Design Tokens）**：从 Pinia `app-config` 派生 CSS Variables（颜色/阴影/半径/间距/阴影层级），统一控制 Naive UI 与自定义组件。
2. **App Shell 分层**：拆分为 `ShellHeader`、`ShellSidebar`、`ShellMain`、`ShellFooter`、`ShellOverlay` 等语义化单元，使用 CSS Grid/Flex 构建响应式主骨架。
3. **优雅降级 + 渐进增强**：保留现有路由与业务组件接口，通过 `Layout` 包装新壳，减少一次性重写风险。
4. **性能优先**：使用 `ResizeObserver`、`matchMedia`、`prefers-reduced-motion` 等原生能力替换手写逻辑，推行延迟挂载与区块骨架屏。
5. **可访问性**：引入 Landmark (`header/nav/main/aside/footer`) 语义标签、焦点管理、对比度校验。

## 4. 新架构蓝图

```
src
 ├─ layouts/
 │   └─ app-shell/
 │       ├─ AppShell.vue        # 布局主容器（网格定义、断点监听）
 │       ├─ context.ts          # 提供 Shell 上下文/断点信息
 │       └─ components/
 │           ├─ ShellHeader.vue # 包裹 HeaderView + 操作区
 │           ├─ ShellSidebar.vue# 封装 SideBar / TabSplit
 │           ├─ ShellMain.vue   # 宿主 MainLayout 与滚动容器
 │           └─ ShellFooter.vue # 承载 FooterView
 └─ styles/
     └─ tokens.css              # 由 Pinia Theme 注入的 CSS Variables
```

- `components/Layout.vue` 将被瘦身为对 `AppShell` 的简单导出，Router 侧无需改动。
- `MainLayout` 负责内容区域导航/多标签，减少与侧边栏/响应式的耦合。

## 5. 实施阶段

1. **Stage 0 – 设计令牌基座**
   - 从 `app-config` store 生成 `--color-*` / `--surface-*` / `--radius-*` 等变量，落地在 `App.vue`。
   - 梳理 `styles/variables.scss`，迁移到 CSS Variables 并开放 JS 读写。
2. **Stage 1 – App Shell 重构（本轮目标）**
   - 新建 `layouts/app-shell` 目录，实现 `AppShell.vue` 与子组件。
   - 引入 `ResizeObserver` + `matchMedia` 抽象断点，统一写入 `app-config`。
   - 提供骨架动画、移动端抽屉/遮罩、主区域容器化。
3. **Stage 2 – 功能区块迁移**
   - 将 Header/Sidebar/NavBar/TabBar 重构为独立 UI 区块，消除历史样式。
   - 引入图标体系/国际化抽象，逐模块接入。
4. **Stage 3 – 交互 & 性能优化**
   - 引入路由层级懒加载提升，提供视图级骨架屏。
   - 结合后端接口对 Dashboard/表单等场景优化。

## 6. 交付与验证

- 每阶段提交：代码改动 + 自测记录 + 截图（必要时）+ 文档更新。
- 自动化：计划补充 Playwright 针对布局/导航的基础冒烟用例。
- 指标：首次渲染时间 < 1.5s（本地 dev 模式），移动端交互帧率 > 55fps。

## 7. 下一步

- [x] 实现 Stage 0/1：落地 Token 与 App Shell。
- [x] 输出 Theme/Breakpoints 的类型定义，方便业务模块消费。
- [x] 为 App Shell 关键交互补充 Playwright E2E + 截图基线。
- [x] 重构系统设置抽屉与标签栏，串联密度/圆角/标签体验。
- [ ] 与后端确认需暴露的全局配置接口（如主题色保存）。

## 8. 2025-02 进展纪要

- **主题令牌**：新增 `src/theme/tokens.ts`，统一 Shell/Sidebar/Motion/Spacing Token，`useThemeTokens` 负责将变量同步到 `documentElement` 与 `body data-*`，并在 `styles/tokens.css` 提供初始兜底。
- **组件接入**：`AppShell`、`SideBar`、`HumBurger`、`ActionItems`、`Setting`、`TabBar` 均切换为 Token 驱动，补齐 `data-testid` / ARIA 属性，移动端遮罩、侧栏浮层使用 `--shell-overlay-color`。
- **自动化**：新增 `playwright.config.ts` 与 `tests/e2e/app-shell.spec.ts`，覆盖「移动端抽屉」与「侧边栏皮肤切换」，包含网络拦截与截图回归示例。
- **页面重构**：首批业务页（`views/index/main.vue`, `views/index/work-place.vue`）完成视觉/交互升级：引入分层面板、胶囊标签、渐变 Hero、快捷操作胶囊，配合 `layoutDensity`/`cornerStyle` Token 动态响应不同终端，后续将按模块逐步推进。

> 所有方案与实现细节会保持在 `docs/` 下同步更新，确保与代码一致。
