import { DeviceType, LayoutMode, PageAnim, SideTheme, ThemeMode } from '@/store/types'
import type { AppConfigState } from '@/store/types'

export const projectName = import.meta.env.VITE_BASE_TITLE as string

export default {
  theme: ThemeMode.LIGHT,
  sideTheme: SideTheme.WHITE,
  themeColor: '#18a058',
  themeColorHover: '#3C9BFFFF',
  themeColorPressed: '#317ECFFF',
  successColor: '#67C23A',
  successColorHover: '#5FB837FF',
  successColorPressed: '#4CA635FF',
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
