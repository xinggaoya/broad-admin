import { defineStore } from 'pinia'
import defaultSetting from '@/setting'
import { CornerStyle, DeviceType, LayoutDensity, PageAnim, SideTheme, ThemeMode } from '../types'
import { useChangeMenuWidth } from '@/hooks/useMenuWidth'
import { calculateHoverActiveColors } from '@/utils/ColorUtils'
import { ref } from 'vue'

// 初始化菜单宽度
useChangeMenuWidth(defaultSetting.sideWidth)

export const useAppConfigStore = defineStore(
  'app-config',
  () => {
    // 状态定义
    const theme = ref<ThemeMode>(defaultSetting.theme)
    const sideTheme = ref<SideTheme>(defaultSetting.sideTheme)
    const themeColor = ref(defaultSetting.themeColor)
    const sideWidth = ref(defaultSetting.sideWidth)
    const deviceType = ref<DeviceType>(defaultSetting.deviceType)
    const pageAnim = ref<PageAnim>(defaultSetting.pageAnim)
    const isCollapse = ref(defaultSetting.isCollapse)
    const isFixedNavBar = ref(defaultSetting.isFixedNavBar)
    const isAccordion = ref(defaultSetting.isAccordion)
    const actionBar = ref(defaultSetting.actionBar)
    const projectName = ref(defaultSetting.projectName)
    const layoutDensity = ref<LayoutDensity>(defaultSetting.layoutDensity)
    const cornerStyle = ref<CornerStyle>(defaultSetting.cornerStyle)

    // Actions
    const changeTheme = (newTheme: ThemeMode) => {
      theme.value = newTheme
      // 同步更新暗黑模式
      if (newTheme === ThemeMode.DARK) {
        document.documentElement.classList.add('dark')
        document.body.setAttribute('theme-mode', 'dark')
      } else {
        document.documentElement.classList.remove('dark')
        document.body.removeAttribute('theme-mode')
      }
    }

    const changeThemeToggle = (bool: boolean) => {
      changeTheme(bool ? ThemeMode.DARK : ThemeMode.LIGHT)
    }

    const changeDevice = (newDeviceType: DeviceType) => {
      deviceType.value = newDeviceType
    }

    const changeSideBarTheme = (newSideTheme: SideTheme) => {
      sideTheme.value = newSideTheme
    }

    const changePageAnim = (newPageAnim: PageAnim) => {
      pageAnim.value = newPageAnim
    }

    const changePrimaryColor = (color: string) => {
      const { baseColor, hoverColor, activeColor } = calculateHoverActiveColors(color)
      themeColor.value = {
        primaryColor: baseColor,
        primaryColorHover: hoverColor,
        primaryColorPressed: activeColor,
        primaryColorSuppl: baseColor
      }
    }

    const changeSideWith = (newSideWidth: number) => {
      sideWidth.value = newSideWidth
      const r = document.querySelector(':root') as HTMLElement
      r.style.setProperty('--menu-width', newSideWidth + 'px')
    }

    const changeLayoutDensity = (density: LayoutDensity) => {
      layoutDensity.value = density
    }

    const changeCornerStyle = (style: CornerStyle) => {
      cornerStyle.value = style
    }

    const toggleCollapse = (newIsCollapse: boolean) => {
      isCollapse.value = newIsCollapse
    }

    const resetState = () => {
      theme.value = defaultSetting.theme
      sideTheme.value = defaultSetting.sideTheme
      themeColor.value = defaultSetting.themeColor
      sideWidth.value = defaultSetting.sideWidth
      deviceType.value = defaultSetting.deviceType
      pageAnim.value = defaultSetting.pageAnim
      isCollapse.value = defaultSetting.isCollapse
      isFixedNavBar.value = defaultSetting.isFixedNavBar
      isAccordion.value = defaultSetting.isAccordion
      actionBar.value = defaultSetting.actionBar
      projectName.value = defaultSetting.projectName
      layoutDensity.value = defaultSetting.layoutDensity
      cornerStyle.value = defaultSetting.cornerStyle

      // 更新主题相关设置
      changeTheme(defaultSetting.theme)
      changePrimaryColor(defaultSetting.themeColor.primaryColor)
      changeSideWith(defaultSetting.sideWidth)
    }

    return {
      // 状态
      theme,
      sideTheme,
      themeColor,
      sideWidth,
      deviceType,
      pageAnim,
      isCollapse,
      isFixedNavBar,
      isAccordion,
      actionBar,
      projectName,
      layoutDensity,
      cornerStyle,
      // Actions
      changeTheme,
      changeThemeToggle,
      changeDevice,
      changeSideBarTheme,
      changePageAnim,
      changePrimaryColor,
      changeSideWith,
      changeLayoutDensity,
      changeCornerStyle,
      toggleCollapse,
      resetState
    }
  },
  {
    presist: {
      enable: true,
      resetToState: true
    }
  }
)
