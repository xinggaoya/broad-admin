import SideTexture from '@/assets/bg_img.webp'
import {
  CornerStyle,
  DeviceType,
  LayoutDensity,
  type ThemeColor,
  ThemeMode,
  SideTheme
} from '@/store/types'

interface SurfacePalette {
  page: string
  surface: string
  card: string
  overlay: string
  border: string
  text: string
  muted: string
  shadow: string
}

interface SidebarPalette {
  surface: string
  text: string
  muted: string
  border: string
  shadow: string
  pattern?: string
  glow?: string
}

interface StateToken {
  bg: string
  text: string
  border: string
}

interface StatePalette {
  success: StateToken
  warning: StateToken
  danger: StateToken
  info: StateToken
}

const surfacePalette: Record<ThemeMode, SurfacePalette> = {
  [ThemeMode.LIGHT]: {
    page: '#f4f6fb',
    surface: '#ffffff',
    card: '#ffffff',
    overlay: 'rgba(15, 23, 42, 0.35)',
    border: 'rgba(15, 23, 42, 0.08)',
    text: '#0f172a',
    muted: '#475569',
    shadow: '0 20px 50px rgba(15, 23, 42, 0.08)'
  },
  [ThemeMode.DARK]: {
    page: '#0f172a',
    surface: '#111827',
    card: '#1f2937',
    overlay: 'rgba(15, 23, 42, 0.65)',
    border: 'rgba(148, 163, 184, 0.24)',
    text: '#f8fafc',
    muted: '#94a3b8',
    shadow: '0 20px 50px rgba(0, 0, 0, 0.55)'
  }
}

const sidebarPalette: Record<SideTheme, Record<ThemeMode, SidebarPalette>> = {
  [SideTheme.DARK]: {
    [ThemeMode.LIGHT]: {
      surface: 'linear-gradient(156deg, #0f172a 0%, #1e293b 100%)',
      text: '#f8fafc',
      muted: 'rgba(248, 250, 252, 0.65)',
      border: 'rgba(148, 163, 184, 0.32)',
      shadow: '0 25px 60px rgba(15, 23, 42, 0.35)'
    },
    [ThemeMode.DARK]: {
      surface: 'linear-gradient(156deg, #020617 0%, #0f172a 100%)',
      text: '#f8fafc',
      muted: 'rgba(148, 163, 184, 0.85)',
      border: 'rgba(51, 65, 85, 0.55)',
      shadow: '0 25px 60px rgba(2, 6, 23, 0.75)'
    }
  },
  [SideTheme.WHITE]: {
    [ThemeMode.LIGHT]: {
      surface: 'linear-gradient(156deg, #ffffff 0%, #f8fafc 100%)',
      text: '#0f172a',
      muted: 'rgba(71, 85, 105, 0.75)',
      border: 'rgba(15, 23, 42, 0.08)',
      shadow: '0 15px 35px rgba(15, 23, 42, 0.15)'
    },
    [ThemeMode.DARK]: {
      surface: 'linear-gradient(156deg, #1f2937 0%, #0f172a 100%)',
      text: '#f8fafc',
      muted: 'rgba(226, 232, 240, 0.75)',
      border: 'rgba(148, 163, 184, 0.3)',
      shadow: '0 20px 45px rgba(15, 23, 42, 0.45)'
    }
  },
  [SideTheme.IMAGE]: {
    [ThemeMode.LIGHT]: {
      surface: 'linear-gradient(156deg, rgba(15, 23, 42, 0.85) 0%, rgba(15, 23, 42, 0.65) 100%)',
      text: '#f8fafc',
      muted: 'rgba(226, 232, 240, 0.8)',
      border: 'rgba(148, 163, 184, 0.32)',
      shadow: '0 25px 60px rgba(15, 23, 42, 0.5)',
      pattern: `url(${SideTexture})`,
      glow: 'rgba(59, 130, 246, 0.35)'
    },
    [ThemeMode.DARK]: {
      surface: 'linear-gradient(156deg, rgba(2, 6, 23, 0.9) 0%, rgba(2, 6, 23, 0.7) 100%)',
      text: '#f8fafc',
      muted: 'rgba(226, 232, 240, 0.75)',
      border: 'rgba(30, 41, 59, 0.8)',
      shadow: '0 25px 60px rgba(0, 0, 0, 0.65)',
      pattern: `url(${SideTexture})`,
      glow: 'rgba(14, 165, 233, 0.35)'
    }
  }
}

const statePalette: Record<ThemeMode, StatePalette> = {
  [ThemeMode.LIGHT]: {
    success: { bg: '#ecfdf3', text: '#03744c', border: 'rgba(16, 185, 129, 0.35)' },
    warning: { bg: '#fff7ed', text: '#b45309', border: 'rgba(255, 159, 67, 0.4)' },
    danger: { bg: '#fef2f2', text: '#b91c1c', border: 'rgba(248, 113, 113, 0.4)' },
    info: { bg: '#eff6ff', text: '#0359b6', border: 'rgba(59, 130, 246, 0.4)' }
  },
  [ThemeMode.DARK]: {
    success: { bg: 'rgba(16, 185, 129, 0.18)', text: '#4ade80', border: 'rgba(16, 185, 129, 0.45)' },
    warning: { bg: 'rgba(251, 146, 60, 0.16)', text: '#fcd34d', border: 'rgba(251, 146, 60, 0.45)' },
    danger: { bg: 'rgba(248, 113, 113, 0.16)', text: '#fca5a5', border: 'rgba(248, 113, 113, 0.45)' },
    info: { bg: 'rgba(59, 130, 246, 0.16)', text: '#93c5fd', border: 'rgba(59, 130, 246, 0.45)' }
  }
}

const spacingByDevice: Record<DeviceType, { contentPadding: number; gap: number }> = {
  [DeviceType.PC]: { contentPadding: 24, gap: 18 },
  [DeviceType.PAD]: { contentPadding: 20, gap: 16 },
  [DeviceType.MOBILE]: { contentPadding: 16, gap: 12 }
}

const densityScaleMap: Record<LayoutDensity, number> = {
  [LayoutDensity.COMFORTABLE]: 1.1,
  [LayoutDensity.COZY]: 1,
  [LayoutDensity.COMPACT]: 0.85
}

const cornerMap: Record<CornerStyle, { base: number; lg: number; xl: number }> = {
  [CornerStyle.SOFT]: { base: 12, lg: 20, xl: 28 },
  [CornerStyle.ROUND]: { base: 16, lg: 24, xl: 32 },
  [CornerStyle.SHARP]: { base: 8, lg: 14, xl: 18 }
}

interface TokenContext {
  theme: ThemeMode
  themeColor: ThemeColor
  sideTheme: SideTheme
  sideWidth: number
  deviceType: DeviceType
  showFooter: boolean
  layoutDensity: LayoutDensity
  cornerStyle: CornerStyle
}

export function createThemeCssVars(ctx: TokenContext) {
  const surfaces = surfacePalette[ctx.theme]
  const sidebar = sidebarPalette[ctx.sideTheme][ctx.theme]
  const spacing = spacingByDevice[ctx.deviceType]
  const densityScale = densityScaleMap[ctx.layoutDensity]
  const corner = cornerMap[ctx.cornerStyle]
  const states = statePalette[ctx.theme]
  const paddingPx = Math.round(spacing.contentPadding * densityScale)
  const gapPx = Math.round(spacing.gap * densityScale)
  const tabHeight = Math.round(44 * densityScale)
  const isDark = ctx.theme === ThemeMode.DARK
  const tabShadow = isDark ? '0 12px 30px rgba(15, 23, 42, 0.55)' : '0 10px 28px rgba(15, 23, 42, 0.12)'

  return {
    '--color-primary': ctx.themeColor.primaryColor,
    '--color-primary-hover': ctx.themeColor.primaryColorHover,
    '--color-primary-pressed': ctx.themeColor.primaryColorPressed,
    '--color-primary-suppl': ctx.themeColor.primaryColorSuppl,
    '--shell-text-color': surfaces.text,
    '--shell-muted-text-color': surfaces.muted,
    '--body-color': surfaces.surface,
    '--shell-page-bg': surfaces.page,
    '--shell-surface': surfaces.surface,
    '--shell-card': surfaces.card,
    '--shell-border-color': surfaces.border,
    '--shell-shadow': surfaces.shadow,
    '--shell-overlay-color': surfaces.overlay,
    '--shell-radius-base': `${corner.base}px`,
    '--shell-radius-lg': `${corner.lg}px`,
    '--shell-radius-xl': `${corner.xl}px`,
    '--shell-header-height': '56px',
    '--shell-footer-height': ctx.showFooter ? '48px' : '0px',
    '--shell-content-padding': `${paddingPx}px`,
    '--shell-gap': `${gapPx}px`,
    '--shell-transition-duration': '220ms',
    '--shell-transition-ease': 'cubic-bezier(0.4, 0, 0.2, 1)',
    '--shell-scrollbar-size': '6px',
    '--shell-scrollbar-color': isDark ? 'rgba(148, 163, 184, 0.45)' : 'rgba(100, 116, 139, 0.4)',
    '--shell-density-scale': densityScale.toString(),
    '--menu-width': `${ctx.sideWidth}px`,
    '--shell-sidebar-width': `${ctx.sideWidth}px`,
    '--shell-sidebar-collapsed-width': '72px',
    '--sidebar-surface': sidebar.surface,
    '--sidebar-text-color': sidebar.text,
    '--sidebar-muted-color': sidebar.muted,
    '--sidebar-border-color': sidebar.border,
    '--sidebar-shadow': sidebar.shadow,
    '--sidebar-surface-pattern': sidebar.pattern ?? 'none',
    '--sidebar-surface-glow': sidebar.glow ?? 'rgba(255,255,255,0.12)',
    '--state-success-bg': states.success.bg,
    '--state-success-text': states.success.text,
    '--state-success-border': states.success.border,
    '--state-warning-bg': states.warning.bg,
    '--state-warning-text': states.warning.text,
    '--state-warning-border': states.warning.border,
    '--state-danger-bg': states.danger.bg,
    '--state-danger-text': states.danger.text,
    '--state-danger-border': states.danger.border,
    '--state-info-bg': states.info.bg,
    '--state-info-text': states.info.text,
    '--state-info-border': states.info.border,
    '--tabbar-bg': `rgba(255,255,255,${isDark ? 0.04 : 0.85})`,
    '--tabbar-border-color': isDark ? 'rgba(148, 163, 184, 0.18)' : 'rgba(15, 23, 42, 0.08)',
    '--tabbar-pill-bg': isDark ? 'rgba(15, 23, 42, 0.55)' : 'rgba(15, 23, 42, 0.04)',
    '--tabbar-pill-text': surfaces.muted,
    '--tabbar-pill-active-text': isDark ? '#f8fafc' : '#0f172a',
    '--tabbar-pill-active-bg': ctx.themeColor.primaryColor,
    '--tabbar-pill-shadow': tabShadow,
    '--tabbar-height': `${tabHeight}px`
  }
}
