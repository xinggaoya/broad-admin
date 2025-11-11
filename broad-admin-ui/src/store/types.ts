import type { Ref, UnwrapRef } from 'vue'
import type { RouteMeta, RouteRecordNormalized, RouteRecordRaw } from 'vue-router'
import type { ThemeCommonVars } from 'naive-ui'

export enum ThemeMode {
  LIGHT = 'light',
  DARK = 'dark'
}

export enum SideTheme {
  DARK = 'dark',
  WHITE = 'white',
  IMAGE = 'image'
}

export enum DeviceType {
  PC = 'pc',
  PAD = 'pad',
  MOBILE = 'mobile'
}

export enum PageAnim {
  FADE = 'fade',
  OPACITY = 'opacity',
  DOWN = 'down',
  SCALE = 'scale'
}

export enum LayoutDensity {
  COMFORTABLE = 'comfortable',
  COZY = 'cozy',
  COMPACT = 'compact'
}

export enum CornerStyle {
  SOFT = 'soft',
  ROUND = 'round',
  SHARP = 'sharp'
}

export interface ThemeColor {
  primaryColor: string
  primaryColorHover: string
  primaryColorPressed: string
  primaryColorSuppl: string
}

export interface ActionBar {
  isShowSearch: boolean
  isShowMessage: boolean
  isShowRefresh: boolean
  isShowFullScreen: boolean
  isShowFooter: boolean
}

export interface AppConfigState {
  projectName: string
  theme: ThemeMode
  sideTheme: SideTheme
  themeColor: ThemeColor
  sideWidth: number
  deviceType: DeviceType
  pageAnim: PageAnim
  isCollapse: boolean
  isFixedNavBar: boolean
  isAccordion: boolean
  actionBar: ActionBar
  layoutDensity: LayoutDensity
  cornerStyle: CornerStyle
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
