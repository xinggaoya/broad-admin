import type { ThemeColor, ThemePreset } from './theme.type'
import { SideTheme, ThemeMode } from './theme.type'

// 默认主题配置
export const defaultThemeConfig = {
  themeMode: ThemeMode.LIGHT,
  sideTheme: SideTheme.WHITE,
  primaryColor: '#2B5CE7',
  isDark: false
}

// 预设主题色
export const presetColors: ThemePreset[] = [
  { name: 'blue', value: '#2B5CE7' },
  { name: 'cyan', value: '#18a058' },
  { name: 'red', value: '#F5222D' },
  { name: 'purple', value: '#722ED1' },
  { name: 'orange', value: '#ee4f12' },
  { name: 'cyan-blue', value: '#0096c7' },
  { name: 'yellow', value: '#ff9801' },
  { name: 'pink', value: '#ff3d68' }
]

// 默认主题颜色配置
export const defaultThemeColors: ThemeColor = {
  primaryColor: '#2B5CE7',
  primaryColorHover: '#4776F0',
  primaryColorPressed: '#1E44B9',
  primaryColorSuppl: '#2B5CE7'
}

// 组件主题配置
export const componentThemeOverrides = {
  common: {
    borderRadius: '6px',
    borderRadiusSmall: '4px',
    heightSmall: '32px',
    heightMedium: '36px',
    heightLarge: '40px',
    fontSizeSmall: '14px',
    fontSizeMedium: '14px',
    fontSizeLarge: '16px',
    fontWeightStrong: '600'
  }
}
