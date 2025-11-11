import { CornerStyle, DeviceType, LayoutDensity, PageAnim, SideTheme, ThemeMode } from '@/store/types'
import type { AppConfigState } from '@/store/types'

export const projectName = import.meta.env.VITE_BASE_TITLE as string

const defaultSetting: AppConfigState = {
  projectName,
  theme: ThemeMode.LIGHT,
  sideTheme: SideTheme.WHITE,
  themeColor: {
    primaryColor: '#2B5CE7',
    primaryColorHover: '#4776F0',
    primaryColorPressed: '#1E44B9',
    primaryColorSuppl: '#2B5CE7'
  },
  sideWidth: 220,
  deviceType: DeviceType.PC,
  pageAnim: PageAnim.FADE,
  isCollapse: false,
  isFixedNavBar: true,
  isAccordion: true,
  layoutDensity: LayoutDensity.COZY,
  cornerStyle: CornerStyle.SOFT,
  actionBar: {
    isShowSearch: true,
    isShowMessage: true,
    isShowRefresh: true,
    isShowFullScreen: true,
    isShowFooter: true
  }
}

export default defaultSetting
