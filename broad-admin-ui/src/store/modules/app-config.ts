import { defineStore } from 'pinia'

import defaultSetting from '@/setting'
import { DeviceType, LayoutMode, PageAnim, SideTheme, ThemeMode } from '../types'

import { useChangeMenuWidth } from '@/hooks/useMenuWidth'

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
      this.themeColor = color
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
