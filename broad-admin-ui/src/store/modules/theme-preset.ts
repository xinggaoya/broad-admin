import { defineStore } from 'pinia'
import { computed, ref } from 'vue'
import type { GlobalThemeOverrides } from 'naive-ui'
import { ThemeMode } from '@/store/types'
import { useAppConfigStore } from './app-config'
import { getSurfacePalette } from '@/theme/tokens'

interface PresetModeTokens {
  cardBg: string
  panelBg: string
  borderColor: string
  controlBg: string
  controlHoverBg: string
  controlActiveBg: string
  controlBorderColor: string
  textPrimary: string
  textMuted: string
  gradient: string
  glow: string
  codeBg: string
  divider: string
  glassBg: string
  menuActiveBg: string
}

interface ThemePresetDefinition {
  key: string
  name: string
  description: string
  modes: Record<ThemeMode, PresetModeTokens>
}

// 主题预设，包含浅/深色下的卡片、边框、控制元素等语义色板，用于统一组件外观
const themePresetDefinitions: ThemePresetDefinition[] = [
  {
    key: 'aurora',
    name: '极光蓝',
    description: '冷静的霓虹蓝渐变，适合数据分析与企业后台',
    modes: {
      [ThemeMode.LIGHT]: {
        cardBg: 'rgba(255, 255, 255, 0.94)',
        panelBg: '#f5f7ff',
        borderColor: 'rgba(15, 23, 42, 0.08)',
        controlBg: 'rgba(43, 92, 231, 0.08)',
        controlHoverBg: 'rgba(43, 92, 231, 0.16)',
        controlActiveBg: 'rgba(43, 92, 231, 0.22)',
        controlBorderColor: 'rgba(43, 92, 231, 0.35)',
        textPrimary: '#0f172a',
        textMuted: '#475569',
        gradient: 'linear-gradient(135deg, #2B5CE7 0%, #5F8CFF 45%, #9AE9FF 100%)',
        glow: '0 30px 80px rgba(43, 92, 231, 0.28)',
        codeBg: '#eef2ff',
        divider: 'rgba(15, 23, 42, 0.05)',
        glassBg: 'rgba(255, 255, 255, 0.75)',
        menuActiveBg: 'rgba(43, 92, 231, 0.12)'
      },
      [ThemeMode.DARK]: {
        cardBg: 'rgba(15, 23, 42, 0.9)',
        panelBg: '#0f172a',
        borderColor: 'rgba(148, 163, 184, 0.25)',
        controlBg: 'rgba(59, 130, 246, 0.14)',
        controlHoverBg: 'rgba(59, 130, 246, 0.2)',
        controlActiveBg: 'rgba(59, 130, 246, 0.28)',
        controlBorderColor: 'rgba(59, 130, 246, 0.45)',
        textPrimary: '#f8fafc',
        textMuted: '#94a3b8',
        gradient: 'linear-gradient(135deg, #0f172a 0%, #1e3a8a 45%, #38bdf8 100%)',
        glow: '0 35px 95px rgba(37, 99, 235, 0.45)',
        codeBg: '#1e293b',
        divider: 'rgba(148, 163, 184, 0.18)',
        glassBg: 'rgba(15, 23, 42, 0.68)',
        menuActiveBg: 'rgba(59, 130, 246, 0.22)'
      }
    }
  },
  {
    key: 'velvet',
    name: '暮色绒',
    description: '柔和的琥珀与紫罗兰过渡，营造温暖的运营后台体验',
    modes: {
      [ThemeMode.LIGHT]: {
        cardBg: '#fff9f5',
        panelBg: '#fff5ef',
        borderColor: 'rgba(146, 64, 14, 0.15)',
        controlBg: 'rgba(217, 119, 6, 0.08)',
        controlHoverBg: 'rgba(217, 119, 6, 0.18)',
        controlActiveBg: 'rgba(217, 119, 6, 0.25)',
        controlBorderColor: 'rgba(217, 119, 6, 0.35)',
        textPrimary: '#311b0b',
        textMuted: '#7a4c2b',
        gradient: 'linear-gradient(135deg, #f97316 0%, #f472b6 55%, #c084fc 100%)',
        glow: '0 25px 75px rgba(244, 114, 182, 0.28)',
        codeBg: '#fff1e6',
        divider: 'rgba(122, 76, 43, 0.1)',
        glassBg: 'rgba(255, 241, 230, 0.86)',
        menuActiveBg: 'rgba(249, 115, 22, 0.12)'
      },
      [ThemeMode.DARK]: {
        cardBg: '#281419',
        panelBg: '#1c0f14',
        borderColor: 'rgba(248, 250, 252, 0.08)',
        controlBg: 'rgba(217, 119, 6, 0.18)',
        controlHoverBg: 'rgba(217, 119, 6, 0.26)',
        controlActiveBg: 'rgba(217, 119, 6, 0.34)',
        controlBorderColor: 'rgba(244, 114, 182, 0.45)',
        textPrimary: '#fff7ed',
        textMuted: 'rgba(255, 237, 213, 0.75)',
        gradient: 'linear-gradient(135deg, #4c0519 0%, #701a75 45%, #f97316 100%)',
        glow: '0 40px 90px rgba(249, 115, 22, 0.38)',
        codeBg: '#3b1d24',
        divider: 'rgba(248, 250, 252, 0.08)',
        glassBg: 'rgba(39, 18, 25, 0.85)',
        menuActiveBg: 'rgba(249, 115, 22, 0.22)'
      }
    }
  }
]

export const useThemePresetStore = defineStore('theme-preset', () => {
  const currentPresetKey = ref(themePresetDefinitions[0].key)
  const appConfig = useAppConfigStore()

  const currentPreset = computed(() => {
    return (
      themePresetDefinitions.find((preset) => preset.key === currentPresetKey.value) ??
      themePresetDefinitions[0]
    )
  })

  const currentModeTokens = computed<PresetModeTokens>(() => {
    const preset = currentPreset.value
    const modeTokens = preset.modes[appConfig.theme]
    if (modeTokens) return modeTokens
    const fallbackPreset = themePresetDefinitions[0]
    return fallbackPreset.modes[appConfig.theme] ?? Object.values(fallbackPreset.modes)[0]
  })

  const naiveThemeOverrides = computed<GlobalThemeOverrides>(() => {
    const surfaces = getSurfacePalette(appConfig.theme)
    const tokens = currentModeTokens.value

    return {
      common: {
        primaryColor: appConfig.themeColor.primaryColor,
        primaryColorHover: appConfig.themeColor.primaryColorHover,
        primaryColorPressed: appConfig.themeColor.primaryColorPressed,
        primaryColorSuppl: appConfig.themeColor.primaryColorSuppl,
        bodyColor: surfaces.page,
        cardColor: tokens.cardBg,
        modalColor: tokens.panelBg,
        popoverColor: tokens.cardBg,
        tableHeaderColor: tokens.panelBg,
        borderColor: tokens.borderColor,
        borderColorPopover: tokens.borderColor,
        textColorBase: tokens.textPrimary,
        textColor2: tokens.textMuted,
        textColorDisabled: tokens.textMuted,
        dividerColor: tokens.divider,
        fontWeightStrong: '600',
        hoverColor: tokens.controlHoverBg,
        focusColor: tokens.controlBorderColor
      },
      Layout: {
        headerColor: tokens.panelBg,
        footerColor: tokens.panelBg,
        siderColor: tokens.panelBg,
        textColor: tokens.textPrimary
      },
      Card: {
        colorEmbedded: tokens.panelBg,
        borderColor: tokens.borderColor,
        titleTextColor: tokens.textPrimary,
        textColor: tokens.textMuted,
        boxShadow: tokens.glow,
        borderRadius: 'var(--shell-radius-lg)'
      },
      Button: {
        borderRadiusTiny: 'var(--shell-radius-base)',
        borderRadiusSmall: 'var(--shell-radius-base)',
        borderRadiusMedium: 'var(--shell-radius-base)',
        borderRadiusLarge: 'var(--shell-radius-lg)',
        textColor: tokens.textPrimary,
        textColorHover: tokens.textPrimary,
        textColorPressed: tokens.textPrimary,
        textColorPrimary: '#ffffff',
        color: tokens.controlBg,
        colorHover: tokens.controlHoverBg,
        colorPressed: tokens.controlActiveBg,
        colorFocus: tokens.controlHoverBg,
        colorDisabled: tokens.glassBg,
        borderColor: tokens.controlBg,
        borderColorHover: tokens.controlBorderColor,
        borderColorFocus: tokens.controlBorderColor,
        rippleColor: tokens.controlActiveBg
      },
      Input: {
        borderRadius: 'var(--shell-radius-base)',
        color: tokens.glassBg,
        colorFocus: tokens.glassBg,
        colorDisabled: tokens.cardBg,
        textColor: tokens.textPrimary,
        textColorDisabled: tokens.textMuted,
        borderColor: tokens.borderColor,
        borderColorHover: tokens.controlBorderColor,
        borderColorFocus: tokens.controlBorderColor,
        boxShadowFocus: `0 0 0 2px ${tokens.controlBorderColor}`
      },
      Menu: {
        itemColorActive: tokens.menuActiveBg,
        itemColorHover: tokens.menuActiveBg,
        itemIconColor: tokens.textMuted,
        itemTextColor: tokens.textPrimary,
        itemTextColorActive: appConfig.themeColor.primaryColor,
        itemTextColorHover: tokens.textPrimary,
        borderColorHorizontal: tokens.borderColor,
        borderRadius: 'var(--shell-radius-base)'
      },
      Tabs: {
        tabBorderColor: tokens.divider,
        tabTextColor: tokens.textMuted,
        tabTextColorHover: tokens.textPrimary,
        tabTextColorActive: appConfig.themeColor.primaryColor,
        tabTextColorDisabled: tokens.textMuted,
        barColor: appConfig.themeColor.primaryColor
      },
      Message: {
        borderRadius: 'var(--shell-radius-base)',
        textColor: tokens.textPrimary,
        colorInfo: tokens.cardBg,
        colorSuccess: tokens.cardBg,
        colorWarning: tokens.cardBg,
        colorError: tokens.cardBg
      }
    }
  })

  const setPreset = (presetKey: string) => {
    const hasPreset = themePresetDefinitions.some((preset) => preset.key === presetKey)
    if (hasPreset) {
      currentPresetKey.value = presetKey
    }
  }

  return {
    presets: themePresetDefinitions,
    currentPresetKey,
    currentPreset,
    currentModeTokens,
    naiveThemeOverrides,
    setPreset
  }
})
