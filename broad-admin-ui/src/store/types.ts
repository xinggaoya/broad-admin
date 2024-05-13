import type { Ref, UnwrapRef } from 'vue'
import type { RouteMeta, RouteRecordNormalized, RouteRecordRaw } from 'vue-router'

export enum LayoutMode {
  LTR = 'ltr',
  LCR = 'lcr',
  TTB = 'ttb'
}

export enum DeviceType {
  PC = 'pc',
  PAD = 'pad',
  MOBILE = 'mobile'
}

export enum ThemeMode {
  LIGHT = 'light',
  DARK = 'dark'
}

export enum SideTheme {
  DARK = 'dark',
  WHITE = 'white',
  BLUE = 'blue',
  IMAGE = 'image'
}

export enum PageAnim {
  FADE = 'fade',
  OPACITY = 'opacity',
  DOWN = 'down',
  SCALE = 'scale'
}

export interface UserState {
  id: number
  tokenValue: string
  roleId: number
  roles: string[] | null
  userName: string
  nickName: string
  avatar: string
}

export interface AppConfigState {
  projectName: string
  theme: ThemeMode
  sideTheme: SideTheme
  themeColor: string
  themeColorHover: string
  themeColorPressed: string
  successColor: string
  successColorHover: string
  successColorPressed: string
  layoutMode: LayoutMode
  deviceType: DeviceType
  sideWidth: number
  pageAnim: PageAnim
  isFixedNavBar: boolean
  isCollapse: boolean
  isAccordion: boolean
  actionBar: {
    isShowSearch: boolean
    isShowMessage: boolean
    isShowRefresh: boolean
    isShowFullScreen: boolean
    isShowFooter: boolean
  }
}

export interface VisitedRouteState {
  visitedRoutes: RouteRecordNormalized[]
  affixRoutes: RouteRecordNormalized[]
}

export interface CachedRouteState {
  cachedRoutes: string[]
}

export interface OriginRoute {
  iframeUrl: string
  parentPath?: string
  menuUrl: string
  menuName?: string
  menuType?: number
  routeName?: string
  perme: string
  hidden?: string
  outLink?: string
  affix?: string
  cacheable?: string
  isRootPath?: string
  iconPrefix?: string
  icon?: string
  badge?: string | number
  isSingle?: string
  localFilePath?: string
  children?: Array<OriginRoute>
}

export interface RouteMetaType extends RouteMeta {
  icon?: string
  title?: string
  cacheable?: boolean
  affix?: boolean
}

export interface SplitTab {
  label: string
  iconPrefix?: string | unknown
  icon: string
  fullPath: string
  children?: Array<RouteRecordRaw>
  checked: Ref<UnwrapRef<boolean>>
}
