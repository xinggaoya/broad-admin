// 主题模式
export enum ThemeMode {
  LIGHT = 'light',
  DARK = 'dark'
}

// 侧边栏主题
export enum SideTheme {
  DARK = 'dark',
  WHITE = 'white',
  IMAGE = 'image'
}

// 主题配置接口
export interface ThemeConfig {
  // 主题模式
  themeMode: ThemeMode
  // 侧边栏主题
  sideTheme: SideTheme
  // 主题色
  primaryColor: string
  // 是否开启暗黑模式
  isDark: boolean
}

// 主题颜色配置
export interface ThemeColor {
  primaryColor: string
  primaryColorHover: string
  primaryColorPressed: string
  primaryColorSuppl: string
  [key: string]: string
}

// 主题预设
export interface ThemePreset {
  name: string
  value: string
  checked?: boolean
}
