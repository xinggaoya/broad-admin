import { computed, watchEffect } from 'vue'
import { useAppConfigStore } from '@/store/modules/app-config'
import { useThemePresetStore } from '@/store/modules/theme-preset'
import { createThemeCssVars } from './tokens'

export function useThemeTokens() {
  const appConfig = useAppConfigStore()
  const themePresetStore = useThemePresetStore()

  const cssVars = computed<Record<string, string>>(() => {
    // 将 Pinia 中的语义色板同步为 CSS 变量，统一全局视觉体验
    const presetTokens =
      themePresetStore.currentModeTokens.value ??
      themePresetStore.presets[0].modes[appConfig.theme] ??
      Object.values(themePresetStore.presets[0].modes)[0]
    return Object.assign(
      createThemeCssVars({
        theme: appConfig.theme,
        themeColor: appConfig.themeColor,
        sideTheme: appConfig.sideTheme,
        sideWidth: appConfig.sideWidth,
        deviceType: appConfig.deviceType,
        showFooter: appConfig.actionBar.isShowFooter,
        layoutDensity: appConfig.layoutDensity,
        cornerStyle: appConfig.cornerStyle
      }),
      {
        '--shell-panel-bg': presetTokens.panelBg,
        '--shell-card-contrast': presetTokens.cardBg,
        '--shell-panel-border': presetTokens.borderColor,
        '--shell-control-bg': presetTokens.controlBg,
        '--shell-control-hover-bg': presetTokens.controlHoverBg,
        '--shell-control-active-bg': presetTokens.controlActiveBg,
        '--shell-control-border': presetTokens.controlBorderColor,
        '--shell-hero-gradient': presetTokens.gradient,
        '--shell-hero-glow': presetTokens.glow,
        '--shell-divider-color': presetTokens.divider,
        '--shell-glass-bg': presetTokens.glassBg,
        '--shell-menu-active-bg': presetTokens.menuActiveBg,
        '--shell-code-bg': presetTokens.codeBg
      }
    )
  })

  watchEffect(() => {
    if (typeof document === 'undefined') return
    const root = document.documentElement
    const vars = cssVars.value
    Object.entries(vars).forEach(([key, value]) => {
      root.style.setProperty(key, value)
    })
    const body = document.body
    body.dataset.theme = appConfig.theme
    body.dataset.sideTheme = appConfig.sideTheme
    body.dataset.device = appConfig.deviceType
    body.dataset.themePreset = themePresetStore.currentPresetKey.value
  })

  return cssVars
}
