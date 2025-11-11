import { computed, watchEffect } from 'vue'
import { useAppConfigStore } from '@/store/modules/app-config'
import { createThemeCssVars } from './tokens'

export function useThemeTokens() {
  const appConfig = useAppConfigStore()

  const cssVars = computed<Record<string, string>>(() =>
    createThemeCssVars({
      theme: appConfig.theme,
      themeColor: appConfig.themeColor,
      sideTheme: appConfig.sideTheme,
      sideWidth: appConfig.sideWidth,
      deviceType: appConfig.deviceType,
      showFooter: appConfig.actionBar.isShowFooter,
      layoutDensity: appConfig.layoutDensity,
      cornerStyle: appConfig.cornerStyle
    })
  )

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
  })

  return cssVars
}
