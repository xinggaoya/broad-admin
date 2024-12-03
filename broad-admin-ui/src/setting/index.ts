import { DeviceType, LayoutMode, PageAnim, SideTheme, ThemeMode } from '@/store/types'
import type { AppConfigState } from '@/store/types'

export const projectName = import.meta.env.VITE_BASE_TITLE as string

const themeColor = {
  common: {
    primaryColor: '#1677ff',
    primaryColorHover: '#4A90E2',
    primaryColorPressed: '#1161AE',
    primaryColorSuppl: '#1677ff',
    infoColor: '#7B7B7B',
    infoColorHover: '#A8A8A8',
    infoColorPressed: '#5C5C5C',
    infoColorSuppl: '#7B7B7B',
    successColor: '#67C23A',
    successColorHover: '#9FCF5B',
    successColorPressed: '#4B9B2F',
    warningColor: '#FFA726',
    warningColorHover: '#FFB74D',
    warningColorPressed: '#F57C00',
    warningColorSuppl: '#FF9800',
    errorColor: '#DC3545',
    errorColorHover: '#E57373',
    errorColorPressed: '#C62828',
    errorColorSuppl: '#F44336',
    borderRadius: '4px',
    borderRadiusSmall: '3px'
  },
  Tag: {
    borderRadius: '5px'
  }
} as any

export default {
  theme: ThemeMode.LIGHT,
  sideTheme: SideTheme.WHITE,
  themeColor: themeColor,
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
