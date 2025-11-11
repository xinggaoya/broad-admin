# App Shell 主题与回归测试指引

## 1. 主题令牌架构

```
Pinia(app-config)
    │ theme / sideTheme / device / width
    ▼
createThemeCssVars (src/theme/tokens.ts)
    │ 生成 Shell/Sidebar/Motion/Spacing 等 CSS Variables
    ▼
useThemeTokens (src/theme/useThemeTokens.ts)
    │ 1) 将变量写入 documentElement
    │ 2) 同步 data-theme / data-side-theme / data-device
    ▼
App.vue / 组件样式
```

- `tokens.ts` 负责按 `ThemeMode + SideTheme + DeviceType` 组合计算配色与间距。例如：
  - `--shell-content-padding`：PC 24px / Pad 20px / Mobile 16px。
  - `--sidebar-surface`、`--sidebar-border-color`：与 `SideTheme` 绑定的渐变/贴图。
- 所有布局组件只消费 CSS Variables，不直接硬编码色值，便于后续接入品牌主题或远程配置。
- 2025/02 起新增 `layoutDensity`、`cornerStyle` 两个维度，可通过设置页热更新 `--shell-radius-*`、`--shell-gap`、`--tabbar-*` 等变量，实现「密度/圆角/标签」一体联动。

## 2. 扩展规范

1. 在 `src/theme/tokens.ts` 内新增 Token 时，保持命名 `--<scope>-<prop>`，并在 `createThemeCssVars` 返回对象中补全。
2. 若 Token 与断点相关，优先复用 `DeviceType` 的 spacing 映射，避免在组件内重复判断。
3. 组件内使用 Token：
   - `var(--shell-shadow)`、`var(--sidebar-text-color)` 等；
   - 避免出现 `#fff` / `#000` 等魔法值，必要时提供 `var(--shell-overlay-color, rgba(...))` 形式的后备值。
4. 若需要在 JS 中读取 Token，可通过 `getComputedStyle(document.documentElement).getPropertyValue('--token')`。

## 3. 数据与可测试性标识

- `AppShell`、`HumBurger`、`Setting`、`SideBar` 等关键节点均暴露 `data-testid`，并在 `body` 与根节点同步 `data-theme / data-side-theme / data-device`。
- Playwright / 视觉回归统一使用这些属性来定位元素，避免依赖易变的类名。

## 4. Playwright 基础用例

> 代码位置：`tests/e2e/app-shell.spec.ts`

- **移动端抽屉**：将 viewport 设为 iPhone 宽度，点击汉堡按钮验证遮罩 + 侧栏动画，并做 `toHaveScreenshot('mobile-shell-open.png')` 快照。
- **侧边栏皮肤切换**：桌面视口下打开「系统设置」，切换暗色/亮色侧边栏并输出 `sidebar-dark.png` 快照。
- **TabBar 视觉**：Tab 标签使用 `--tabbar-*` token，随主题/密度同步；若需要新增截图回归，可在测试内操作 `data-testid="tab-strip"` 并调用 `toHaveScreenshot('tab-strip.png')`。
- 通过 `page.route('**/sysMenu/getRouters')` 等方式拦截后端依赖，保证前端独立跑通。

### 执行步骤

```bash
pnpm install
pnpm dev --host 0.0.0.0 --port 4173   # 另一个终端保持运行
pnpm test:e2e:update                  # 首次生成基线截图
pnpm test:e2e                         # 之后即可比对回归
```

> **提示**：首次如果缺少截图基线，Playwright 会报错提醒运行 `--update-snapshots`。确保在更新后将 `tests/e2e/__screenshots__` 目录纳入版本控制。

## 5. 常见问题

| 场景 | 建议 |
| --- | --- |
| 需要新增 SideTheme | 在 `tokens.ts` 的 `sidebarPalette` 中定义 light/dark 两套配色，并在 UI 中提供预览。 |
| 组件内仍有硬编码色值 | 优先抽象为 Token；若暂时无法迁移，请在注释中说明来源以便后续统一。 |
| 回归截图波动大 | 使用 `page.route` 固定接口返回、`page.setViewportSize` 固定视口；必要时在 `expect(...).toHaveScreenshot` 中收紧 `maxDiffPixelRatio`。 |

## 6. 设置面板与标签页

- `components/setting/Setting.vue` 采用 `n-tabs` + 语义化卡片区块，支持主题模式、侧边栏皮肤、主色、菜单宽度、内容密度(`layoutDensity`)、圆角风格(`cornerStyle`)以及动作按钮的集中配置。
- `components/tabbar/TabBar.vue` 新增渐变悬浮胶囊、滚动箭头/更多菜单、右键上下文菜单与可访问属性，所有视觉来自 `--tabbar-*` token，支持 Playwright 截图回归。
