import { DeviceType, LayoutMode, PageAnim, SideTheme, ThemeMode } from '@/store/types'
import type { AppConfigState } from '@/store/types'
import type { ThemeCommonVars } from 'naive-ui'
import { calculateHoverActiveColors } from '@/utils/ColorUtils'

export const projectName = import.meta.env.VITE_BASE_TITLE as string

const themeColor= {
  borderRadius: '6px',
  // primaryColor: '#409EFF',
  // primaryColorHover: '#66b1ff',
  // primaryColorPressed: '#3a8ee6',
  // primaryColorSuppl: '#409eff',
  infoColor: '#909399',
  infoColorHover: '#a6a9ad',
  infoColorPressed: '#82848a',
  infoColorSuppl: '#909399',
  successColor: '#67c23a',
  successColorHover: '#85ce61',
  successColorPressed: '#5daf34',
  successColorSuppl: '#67c23a',
  // warningColor: '#e6a23c',
  // warningColorHover: '#ebb563',
  // warningColorPressed: '#cf9236',
  // warningColorSuppl: '#e6a23c',
  // errorColor: '#f56c6c',
  // errorColorHover: '#f78989',
  // errorColorPressed: '#dd6161',
  // errorColorSuppl: '#f56c6c'
} as ThemeCommonVars

// 获取主题颜色
function getColor() {
  const baseColor = calculateHoverActiveColors('#18a058')

  themeColor.primaryColor = baseColor.baseColor
  themeColor.primaryColorHover = baseColor.hoverColor
  themeColor.primaryColorPressed = baseColor.activeColor
  themeColor.primaryColorSuppl = baseColor.baseColor
  return themeColor
}


export default {
  theme: ThemeMode.LIGHT,
  sideTheme: SideTheme.WHITE,
  themeColor: getColor(),
  layoutMode: LayoutMode.LTR,
  sideWidth: 210,
  deviceType: DeviceType.PC,
  pageAnim: PageAnim.DOWN,
  isFixedNavBar: true,
  isAccordion: true,
  isCollapse: false,
  actionBar: {
    isShowSearch: false,
    isShowMessage: true,
    isShowRefresh: false,
    isShowFullScreen: true,
    isShowFooter: false
  }
} as AppConfigState
