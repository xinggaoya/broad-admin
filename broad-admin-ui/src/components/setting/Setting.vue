<template>
  <div>
    <n-scrollbar class="setting-shell">

  <n-drawer v-model:show="opened" width="420" placement="right" :auto-focus="false">
    <n-drawer-content closable body-content-style="padding:20px">
        <header class="setting-shell__header">
          <div>
            <p class="setting-shell__title">{{ appConfig.projectName }}</p>
            <p class="setting-shell__subtitle">实时调校主题、布局与交互体验</p>
          </div>
          <n-tag size="small" type="primary" round>
            {{ appConfig.theme === ThemeMode.DARK ? '暗黑模式' : '明亮模式' }}
          </n-tag>
        </header>

        <n-tabs v-model:value="activeTab" type="segment" animated>
          <n-tab-pane name="theme" tab="外观主题">
            <section class="setting-panel">
              <p class="setting-panel__title">主题模式</p>
              <div class="theme-mode-grid">
                <button
                  v-for="item in themeList"
                  :key="item.themeId"
                  class="theme-mode-card"
                  :class="{ 'is-active': item.checked }"
                  type="button"
                  @click="themeClick(item)"
                >
                  <StyleExample
                    :checked="item.checked"
                    :left-bg="item.leftBg"
                    :right-top-bg="item.rightTopBg"
                    :right-bottom-bg="item.rightBottomBg"
                    :tip-text="item.tipText"
                  />
                </button>
              </div>

              <p class="setting-panel__title">侧边栏皮肤</p>
              <div class="side-theme-grid">
                <button
                  v-for="item in sideThemePresets"
                  :key="item.themeId"
                  class="side-theme-card"
                  :class="{ 'is-active': item.checked }"
                  type="button"
                  :aria-pressed="item.checked.toString()"
                  :aria-label="`切换至${item.title}`"
                  :data-testid="`side-theme-card-${item.themeId}`"
                  @click="handleSideThemeSelect(item)"
                >
                  <div class="side-theme-card__preview" :style="item.previewStyle">
                    <span class="side-theme-card__badge">{{ item.badge }}</span>
                    <span class="side-theme-card__glow" :style="{ background: item.accent }"></span>
                  </div>
                  <div class="side-theme-card__meta">
                    <p class="side-theme-card__title">{{ item.title }}</p>
                    <p class="side-theme-card__desc">{{ item.description }}</p>
                  </div>
                </button>
              </div>

              <p class="setting-panel__title">主色切换</p>
              <div class="color-swatch-grid">
                <button
                  v-for="item in primartyColorList"
                  :key="item.name"
                  class="color-swatch"
                  :class="{ 'is-active': item.checked }"
                  type="button"
                  :style="{ backgroundColor: item.value }"
                  @click="colorClick(item)"
                >
                  <span>{{ item.name }}</span>
                </button>
              </div>
            </section>
          </n-tab-pane>

          <n-tab-pane name="layout" tab="布局与密度">
            <section class="setting-panel">
              <p class="setting-panel__title">菜单宽度</p>
              <n-slider
                v-model:value="menuWidth"
                :min="200"
                :max="380"
                :step="5"
                :format-tooltip="(value) => `${value}px`"
              />
              <div class="setting-panel__summary">
                当前宽度：<strong>{{ menuWidth }}px</strong>
              </div>

              <p class="setting-panel__title">内容密度</p>
              <n-radio-group v-model:value="layoutDensityValue" class="preset-radio-group">
                <n-radio
                  v-for="preset in layoutDensityPresets"
                  :key="preset.value"
                  :value="preset.value"
                  :label="preset.title"
                >
                  <span class="preset-radio__desc">{{ preset.description }}</span>
                </n-radio>
              </n-radio-group>

              <p class="setting-panel__title">圆角风格</p>
              <n-radio-group v-model:value="cornerStyleValue" class="preset-radio-group">
                <n-radio
                  v-for="preset in cornerStylePresets"
                  :key="preset.value"
                  :value="preset.value"
                  :label="preset.title"
                >
                  <span class="preset-radio__desc">{{ preset.description }}</span>
                </n-radio>
              </n-radio-group>

              <p class="setting-panel__title">导航配置</p>
              <div class="setting-switch-list">
                <label>
                  <div>
                    <span>固定顶部导航</span>
                    <small>滚动时保持导航与标签浮层</small>
                  </div>
                  <n-switch v-model:value="appConfig.isFixedNavBar" />
                </label>
                <label>
                  <div>
                    <span>菜单手风琴</span>
                    <small>同一层级只展开一个菜单组</small>
                  </div>
                  <n-switch v-model:value="appConfig.isAccordion" />
                </label>
              </div>
            </section>
          </n-tab-pane>

          <n-tab-pane name="interaction" tab="交互与快捷">
            <section class="setting-panel">
              <p class="setting-panel__title">页面动效</p>
              <n-select
                v-model:value="appConfig.pageAnim"
                :options="animOptions"
                @update:value="onAnimUpdate"
              />

              <p class="setting-panel__title">动作按钮</p>
              <div class="setting-switch-list">
                <label v-for="item in actionBarSwitchers" :key="item.field">
                  <div>
                    <span>{{ item.label }}</span>
                    <small>{{ item.description }}</small>
                  </div>
                  <n-switch
                    :value="appConfig.actionBar[item.field]"
                    @update:value="handleActionBarToggle(item.field, $event)"
                  />
                </label>
              </div>
            </section>
          </n-tab-pane>
        </n-tabs>

        <footer class="setting-shell__footer">
          <n-button tertiary @click="resetToDefault">恢复默认</n-button>
          <n-button type="primary" @click="closeDrawer">关闭</n-button>
        </footer>
    </n-drawer-content>

  </n-drawer>
  </n-scrollbar>

  </div>
</template>

<script lang="ts" setup name="SystemSetting">
import { onMounted, reactive, ref, watch } from 'vue'
import LeftBg from '@/assets/bg_img.webp'
import { useAppConfigStore } from '@/store/modules/app-config'
import type { ActionBar } from '@/store/types'
import { CornerStyle, LayoutDensity, PageAnim, SideTheme, ThemeMode } from '@/store/types'
import { useMessage } from 'naive-ui'
import StyleExample from '@/components/setting/components/StyleExample.vue'

const message = useMessage()
const opened = ref(false)
const activeTab = ref<'theme' | 'layout' | 'interaction'>('theme')
const appConfig = useAppConfigStore()
const menuWidth = ref(appConfig.sideWidth)
const layoutDensityValue = ref<LayoutDensity>(appConfig.layoutDensity)
const cornerStyleValue = ref<CornerStyle>(appConfig.cornerStyle)

const themeList = reactive([
  {
    leftBg: '#ffffff',
    rightTopBg: '#ffffff',
    rightBottomBg: '#f5f5f5',
    checked: false,
    themeId: ThemeMode.LIGHT,
    tipText: '明亮'
  },
  {
    leftBg: '#000000',
    rightTopBg: '#000000',
    rightBottomBg: '#333333',
    checked: false,
    themeId: ThemeMode.DARK,
    tipText: '暗黑'
  }
])

interface SideThemePreset {
  themeId: SideTheme
  title: string
  description: string
  badge: string
  accent: string
  previewStyle: Record<string, string>
  checked: boolean
}

const sideThemePresets = reactive<SideThemePreset[]>([
  {
    themeId: SideTheme.DARK,
    title: '沉浸暗色',
    description: '夜间 / 低光环境首选',
    badge: 'DARK',
    accent: 'rgba(59, 130, 246, 0.45)',
    previewStyle: {
      background: 'linear-gradient(135deg, #0f172a, #1f2937)'
    },
    checked: false
  },
  {
    themeId: SideTheme.WHITE,
    title: '极简亮色',
    description: '保持内容聚焦与清爽视感',
    badge: 'LIGHT',
    accent: 'rgba(14, 165, 233, 0.35)',
    previewStyle: {
      background: 'linear-gradient(135deg, #f8fafc, #e2e8f0)'
    },
    checked: false
  },
  {
    themeId: SideTheme.IMAGE,
    title: '氛围纹理',
    description: '融合渐变壁纸，营造高级仪式感',
    badge: 'ART',
    accent: 'rgba(255, 255, 255, 0.4)',
    previewStyle: {
      backgroundImage: `url(${LeftBg})`,
      backgroundSize: 'cover',
      backgroundPosition: 'center'
    },
    checked: false
  }
])

const primartyColorList = reactive([
  { name: 'blue', value: '#2B5CE7', checked: true },
  { name: 'cyan', value: '#18a058', checked: false },
  { name: 'red', value: '#F5222D', checked: false },
  { name: 'purple', value: '#722ED1', checked: false },
  { name: 'orange', value: '#ee4f12', checked: false },
  { name: 'cyan-blue', value: '#0096c7', checked: false },
  { name: 'yellow', value: '#ff9801', checked: false },
  { name: 'pink', value: '#ff3d68', checked: false }
])

const layoutDensityPresets = [
  {
    value: LayoutDensity.COMPACT,
    title: '紧凑',
    description: '更密集的面板与标签间距'
  },
  {
    value: LayoutDensity.COZY,
    title: '协调',
    description: '推荐的平衡密度'
  },
  {
    value: LayoutDensity.COMFORTABLE,
    title: '宽松',
    description: '适合大屏 / 会议展示'
  }
]

const cornerStylePresets = [
  {
    value: CornerStyle.SHARP,
    title: '利落直角',
    description: '硬朗矩形、科技感更强'
  },
  {
    value: CornerStyle.SOFT,
    title: '柔和圆角',
    description: '当前默认视觉'
  },
  {
    value: CornerStyle.ROUND,
    title: '大圆角',
    description: '更具新拟态空间感'
  }
]

const animOptions = [
  { label: '渐隐渐现', value: PageAnim.OPACITY },
  { label: '水平滑动', value: PageAnim.FADE },
  { label: '上下滑动', value: PageAnim.DOWN },
  { label: '缩放效果', value: PageAnim.SCALE }
]

type ActionBarField = keyof ActionBar

const actionBarSwitchers: Array<{ field: ActionBarField; label: string; description: string }> = [
  { field: 'isShowSearch', label: '全局搜索', description: '支持命令面板式检索' },
  { field: 'isShowMessage', label: '消息提醒', description: '实时通知与公告' },
  { field: 'isShowRefresh', label: '刷新按钮', description: '便捷重载当前页面' },
  { field: 'isShowFullScreen', label: '全屏切换', description: '面向仪表屏展示' },
  { field: 'isShowFooter', label: '底部状态栏', description: '展示版权与版本信息' }
] as const

const handleActionBarToggle = (field: ActionBarField, value: boolean) => {
  appConfig.actionBar[field] = value
}

onMounted(() => {
  initCheckedState()
})

function initCheckedState() {
  themeList.forEach((it) => {
    it.checked = appConfig.theme === it.themeId
  })
  sideThemePresets.forEach((it) => {
    it.checked = appConfig.sideTheme === it.themeId
  })
  primartyColorList.forEach((it) => {
    it.checked = appConfig.themeColor.primaryColor === it.value
  })
  menuWidth.value = appConfig.sideWidth
  layoutDensityValue.value = appConfig.layoutDensity
  cornerStyleValue.value = appConfig.cornerStyle
}

function openDrawer() {
  opened.value = true
  initCheckedState()
}

function closeDrawer() {
  opened.value = false
}

function themeClick(item: (typeof themeList)[number]) {
  themeList.forEach((it) => {
    it.checked = it === item
  })
  appConfig.changeTheme(item.themeId)
  if (item.themeId === ThemeMode.DARK) {
    selectSideTheme(SideTheme.DARK)
  } else {
    selectSideTheme(SideTheme.WHITE)
  }
}

function selectSideTheme(themeId: SideTheme) {
  const preset = sideThemePresets.find((it) => it.themeId === themeId)
  if (preset) {
    handleSideThemeSelect(preset)
  }
}

function handleSideThemeSelect(item: SideThemePreset) {
  sideThemePresets.forEach((preset) => {
    preset.checked = preset === item
  })
  appConfig.changeSideBarTheme(item.themeId)
}

function colorClick(item: (typeof primartyColorList)[number]) {
  primartyColorList.forEach((it) => {
    it.checked = it === item
  })
  appConfig.changePrimaryColor(item.value)
}

function onAnimUpdate(val: PageAnim) {
  appConfig.changePageAnim(val)
}

function resetToDefault() {
  appConfig.resetState()
  initCheckedState()
  message.success('已恢复默认配置')
}

watch(menuWidth, (val) => {
  appConfig.changeSideWith(val)
})

watch(layoutDensityValue, (val) => {
  appConfig.changeLayoutDensity(val)
})

watch(cornerStyleValue, (val) => {
  appConfig.changeCornerStyle(val)
})

defineExpose({
  openDrawer
})
</script>

<style lang="scss" scoped>
.setting-shell {
  padding: 0 24px 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.setting-shell__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24px 0 12px;
  border-bottom: 1px solid var(--shell-border-color);
  margin-bottom: 8px;
}

.setting-shell__title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
}

.setting-shell__subtitle {
  margin: 4px 0 0;
  color: var(--shell-muted-text-color);
  font-size: 13px;
}

.setting-panel {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 8px 0 4px;
}

.setting-panel__title {
  font-weight: 600;
  color: var(--shell-text-color);
  margin: 0;
}

.setting-panel__summary {
  font-size: 13px;
  color: var(--shell-muted-text-color);
}

.theme-mode-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
}

.theme-mode-card {
  border: none;
  padding: 0;
  background: transparent;
  border-radius: var(--shell-radius-base);
  transition: transform 0.2s ease;
  cursor: pointer;
}

.theme-mode-card.is-active {
  transform: translateY(-2px);
}

.side-theme-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 12px;
}

.side-theme-card {
  border: 1px solid var(--shell-border-color);
  border-radius: 16px;
  padding: 12px;
  background-color: var(--shell-surface, #fff);
  box-shadow: 0 6px 20px rgba(15, 23, 42, 0.08);
  cursor: pointer;
  text-align: left;
  display: flex;
  flex-direction: column;
  gap: 8px;
  transition:
    transform 0.2s ease,
    border-color 0.2s ease,
    box-shadow 0.2s ease;
}

.side-theme-card.is-active {
  transform: translateY(-2px);
  box-shadow: 0 12px 30px rgba(43, 92, 231, 0.2);
  border-color: transparent;
}

.side-theme-card__preview {
  position: relative;
  border-radius: 12px;
  height: 84px;
  overflow: hidden;
}

.side-theme-card__badge {
  position: absolute;
  top: 10px;
  left: 10px;
  padding: 2px 8px;
  border-radius: 999px;
  background-color: rgba(0, 0, 0, 0.45);
  color: #fff;
  font-size: 11px;
  letter-spacing: 0.5px;
}

.side-theme-card__glow {
  position: absolute;
  inset: auto auto -30px -30px;
  width: 120px;
  height: 120px;
  filter: blur(30px);
  opacity: 0.7;
}

.side-theme-card__meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.side-theme-card__title {
  font-size: 14px;
  font-weight: 600;
  margin: 0;
}

.side-theme-card__desc {
  font-size: 12px;
  color: var(--shell-muted-text-color);
  margin: 0;
}

.color-swatch-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 10px;
}

.color-swatch {
  position: relative;
  border: none;
  border-radius: 12px;
  min-height: 54px;
  color: #fff;
  font-size: 12px;
  font-weight: 500;
  text-transform: capitalize;
  cursor: pointer;
  display: flex;
  align-items: flex-end;
  justify-content: flex-start;
  padding: 8px;
  opacity: 0.85;
  transition: transform 0.2s ease, opacity 0.2s ease;
}

.color-swatch.is-active {
  transform: translateY(-3px);
  opacity: 1;
  box-shadow: 0 10px 20px rgba(15, 23, 42, 0.2);
}

.preset-radio-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.preset-radio__desc {
  display: block;
  font-size: 12px;
  color: var(--shell-muted-text-color);
}

.setting-switch-list {
  display: flex;
  flex-direction: column;
  gap: 12px;

  label {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 14px;
    border-radius: 12px;
    border: 1px solid var(--shell-border-color);
    background: var(--shell-surface);

    span {
      font-weight: 500;
    }

    small {
      display: block;
      color: var(--shell-muted-text-color);
      font-size: 12px;
      margin-top: 4px;
    }
  }
}

.setting-shell__footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 12px 0 24px;
  border-top: 1px solid var(--shell-border-color);
}

@media (max-width: 640px) {
  .color-swatch-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}
</style>
