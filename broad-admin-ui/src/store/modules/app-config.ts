import { defineStore } from 'pinia'
import defaultSetting from '@/setting'
import { DeviceType, LayoutMode, PageAnim, SideTheme, ThemeMode } from '../types'

import { useChangeMenuWidth } from '@/hooks/useMenuWidth'
import { calculateHoverActiveColors } from '@/utils/ColorUtils'

useChangeMenuWidth(defaultSetting.sideWidth)

export const useAppConfigStore = defineStore('app-config', {
  state: () => {
    return defaultSetting
  },
  getters: {
    getLayoutMode(state) {
      return state.layoutMode
    }
  },
  actions: {
    changeTheme(theme: ThemeMode) {
      this.theme = theme
    },
    changeThemeToggle(bool: boolean) {
      if (bool) {
        this.theme = ThemeMode.DARK
      } else {
        this.theme = ThemeMode.LIGHT
      }
    },
    changeLayoutMode(mode: LayoutMode) {
      this.layoutMode = mode
    },
    changeDevice(deviceType: DeviceType) {
      this.deviceType = deviceType
    },
    changeSideBarTheme(sideTheme: SideTheme) {
      this.sideTheme = sideTheme
    },
    changePageAnim(pageAnim: PageAnim) {
      this.pageAnim = pageAnim
    },
    changePrimaryColor(color: string) {
      // 计算出对于Hover  颜色
      const baseColor = calculateHoverActiveColors(color)

      this.themeColor.primaryColor = baseColor.baseColor
      this.themeColor.primaryColorHover = baseColor.hoverColor
      this.themeColor.primaryColorPressed = baseColor.activeColor
      this.themeColor.primaryColorSuppl = baseColor.baseColor
    },
    changeSideWith(sideWidth: number) {
      this.sideWidth = sideWidth
      const r = document.querySelector(':root') as HTMLElement
      r.style.setProperty('--menu-width', sideWidth + 'px')
    },
    toggleCollapse(isCollapse: boolean) {
      this.isCollapse = isCollapse
    }
  },
  presist: {
    enable: true,
    resetToState: true
  }
})

export default useAppConfigStore
