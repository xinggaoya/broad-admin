import { isExternal, toHump } from '@/utils'
import { resolve } from 'path-browserify'
import { h, ref } from 'vue'
import type { RouteRecordRaw } from 'vue-router'
import type { OriginRoute, SplitTab } from '../types'
import type { MenuOption, NIcon } from 'naive-ui'
import { LAYOUT } from '../keys'
import SvgIcon from '@/components/svg-icon/SvgIcon.vue'

export function loadComponents() {
  return import.meta.glob('/src/views/**/*.vue')
}

enum Boolean {
  true = '0',
  false = '1'
}

export const asynComponents = loadComponents()

export function getComponent(it: OriginRoute) {
  return asynComponents[getFilePath(it)]
}

export function getFilePath(it: OriginRoute) {
  if (!it.localFilePath) {
    console.log(it)
    throw new Error('localFilePath is not null')
  }
  it.localFilePath = resolve('/', it.localFilePath)
  return '/src/views' + it.localFilePath + '.vue'
}

export function isMenu(it: OriginRoute) {
  return it.menuType === 1
}

function normalizeRoutePath(menuUrl?: string) {
  if (!menuUrl) {
    return '/'
  }
  return menuUrl.startsWith('/') ? menuUrl : `/${menuUrl}`
}

function resolveRoutePath(menuUrl: string | undefined, parentAbsolutePath = '', level = 0) {
  const normalizedPath = normalizeRoutePath(menuUrl)
  const sanitizedParent = parentAbsolutePath.replace(/\/+$/, '')

  let absolutePath = normalizedPath
  if (level > 0 && sanitizedParent) {
    if (!normalizedPath.startsWith(`${sanitizedParent}/`) && normalizedPath !== sanitizedParent) {
      const relative = normalizedPath.replace(/^\//, '')
      absolutePath = `${sanitizedParent}/${relative}`.replace(/\/+/g, '/')
    }
  }

  if (level === 0 || !sanitizedParent) {
    return {
      absolutePath,
      routePath: absolutePath
    }
  }

  let relativePath = absolutePath
  if (absolutePath.startsWith(`${sanitizedParent}/`)) {
    relativePath = absolutePath.slice(sanitizedParent.length + 1)
  } else {
    relativePath = absolutePath.replace(/^\//, '')
  }

  if (!relativePath) {
    const segments = absolutePath.split('/').filter((segment) => !!segment)
    relativePath = segments[segments.length - 1] || ''
  }

  return {
    absolutePath,
    routePath: relativePath
  }
}

function resolveRouteName(route: OriginRoute) {
  if (route.routeName) {
    return route.routeName
  }
  const resolvedPath = route.localFilePath || route.menuUrl || ''
  return getNameByUrl(resolvedPath)
}

function resolveRouteComponent(route: OriginRoute, isDirectory: boolean) {
  if (isDirectory || route.iframeUrl) {
    return LAYOUT
  }
  try {
    const component = getComponent(route)
    return component || LAYOUT
  } catch (error) {
    console.warn(`[router] 组件解析失败: ${route.localFilePath}`, error)
    return LAYOUT
  }
}

// 获取路由名称
export function getNameByUrl(menuUrl: string) {
  if (!menuUrl) {
    return ''
  }
  const temp = menuUrl.split('/')
  return toHump(temp[temp.length - 1])
}

/**
 * 递归创建路由
 * @param res
 */
function ensureUniqueRouteName(
  rawName: string | undefined,
  path: string,
  nameUsage: Record<string, number>
) {
  const fallbackKey = path.replace(/[:/]+/g, '-') || 'route'
  const baseName = rawName && rawName.length > 0 ? rawName : toHump(fallbackKey)
  const normalizedName =
    baseName && baseName.length > 0 ? baseName : `Route${Object.keys(nameUsage).length + 1}`
  nameUsage[normalizedName] = (nameUsage[normalizedName] || 0) + 1
  return nameUsage[normalizedName] === 1 ? normalizedName : `${normalizedName}_${nameUsage[normalizedName]}`
}

export function generatorRoutes(
  res: Array<OriginRoute>,
  nameUsage: Record<string, number> = {},
  parentAbsolutePath = '',
  level = 0
) {
  const tempRoutes: Array<RouteRecordRaw> = []

  res?.forEach((it) => {
    if (!it) {
      return
    }
    const menuType = typeof it.menuType === 'number' ? it.menuType : Number(it.menuType || 1)
    if (menuType === 2) {
      // 仅按钮权限，无需生成菜单
      return
    }
    const isDirectory = menuType !== 1
    const { absolutePath, routePath } = resolveRoutePath(it.menuUrl, parentAbsolutePath, level)
    const routeName = ensureUniqueRouteName(resolveRouteName(it), absolutePath, nameUsage)
    const route: RouteRecordRaw = {
      path: level === 0 ? routePath : routePath || '',
      name: routeName,
      component: resolveRouteComponent(it, isDirectory),
      children: [],
      meta: {
        hidden: it.hidden == Boolean.true,
        title: it.menuName,
        affix: it.affix == Boolean.true,
        cacheable: it.cacheable == Boolean.true,
        icon: it.icon || 'menu',
        iconPrefix: it.iconPrefix || 'iconfont',
        badge: it.badge,
        isRootPath: !!it.isRootPath,
        isSingle: !!it.isSingle,
        iframeUrl: it.iframeUrl || undefined
      }
    }
    if (Array.isArray(it.children) && it.children.length > 0) {
      route.children = generatorRoutes(it.children, nameUsage, absolutePath, level + 1)
    }
    tempRoutes.push(route)
  })
  return tempRoutes
}

export function mapTwoLevelRouter(srcRoutes: Array<RouteRecordRaw>) {
  function addParentRoute(routes: any, parent: any, parentPath: string) {
    routes.forEach((it: RouteRecordRaw) => {
      if (!isExternal(it.path)) {
        it.path = resolve(parentPath, it.path)
      }
      parent.push(it)
      if (it.children && it.children.length > 0) {
        addParentRoute(it.children, parent, it.path)
      }
    })
  }

  if (srcRoutes && srcRoutes.length > 0) {
    const tempRoutes = [] as Array<RouteRecordRaw>
    srcRoutes.forEach((it) => {
      const route = { ...it }
      const parentRoutes = [] as Array<RouteRecordRaw>
      if (route.children && route.children.length > 0) {
        addParentRoute(route.children, parentRoutes, route.path)
      }
      parentRoutes && parentRoutes.length > 0 && (route.children = parentRoutes)
      tempRoutes.push(route)
    })
    return tempRoutes
  }
  return []
}

export function findAffixedRoutes(routes: Array<RouteRecordRaw>) {
  const temp = [] as Array<RouteRecordRaw>
  routes.forEach((it) => {
    if (it.meta && it.meta.affix) {
      temp.push(it)
    }
  })
  return temp
}

export function findCachedRoutes(routes: Array<RouteRecordRaw>) {
  const temp = [] as Array<string>
  routes.forEach((it) => {
    if (it.name && it.meta && it.meta.cacheable) {
      temp.push(it.name as string)
    }
  })
  return temp
}

export function transfromMenu(originRoutes: Array<RouteRecordRaw>): Array<MenuOption> {
  function getLabel(item: RouteRecordRaw) {
    if (isExternal(item.path as string)) {
      return () =>
        h(
          'a',
          {
            href: item.path,
            target: '_blank',
            rel: 'noopenner noreferrer'
          },
          (item.meta as any).title
        )
    }
    return item.meta?.title || ''
  }

  if (!originRoutes) {
    return []
  }
  const tempMenus: Array<MenuOption> = []
  originRoutes
    .filter((it) => {
      if (!it.meta) {
        return false
      }
      return !it.meta.hidden
    })
    .forEach((it) => {
      const tempMenu = {
        key: it.path,
        label: getLabel(it),
        icon: renderMenuIcon(
          it.meta ? (it.meta.iconPrefix ? (it.meta.iconPrefix as string) : 'icon') : 'icon',
          it.meta?.icon
        )
      } as MenuOption
      if (it.children && it.children.length > 0) {
        if (it.meta && it.meta.isSingle && it.children.length === 1) {
          const item = it.children[0]
          tempMenu.key = resolve(tempMenu.key as string, item.path)
          tempMenu.label =
            item.meta && item.meta.title ? getLabel(item as RouteRecordRaw) : tempMenu.label
          tempMenu.icon =
            item.meta && item.meta.icon
              ? renderMenuIcon(
                item.meta
                  ? item.meta.iconPrefix
                    ? (item.meta.iconPrefix as string)
                    : 'icon'
                  : 'icon',
                item.meta?.icon
              )
              : tempMenu.icon
        } else {
          tempMenu.children = transfromMenu(it.children as RouteRecordRaw[])
        }
      }
      tempMenus.push(tempMenu)
    })
  return tempMenus
}

export function transformSplitTabMenu(routes: Array<RouteRecordRaw>): Array<SplitTab> {
  const tempTabs = [] as Array<SplitTab>
  routes.forEach((it) => {
    const splitTab: SplitTab = {
      label: it.meta ? (it.meta?.title as string) : '',
      fullPath: it.path || '',
      iconPrefix: it.meta?.iconPrefix || 'icon',
      icon: it.meta ? (it.meta?.icon as any) : undefined,
      children: it.children as RouteRecordRaw[],
      checked: ref(false)
    }
    tempTabs.push(splitTab)
  })
  return tempTabs
}

export function renderMenuIcon(iconPrefix: string, icon?: any) {
  if (!icon) {
    return undefined
  }
  return () =>
    // @ts-ignore
    h(NIcon, null, {
      default: () =>
        h(SvgIcon, {
          prefix: iconPrefix,
          name: icon
        })
    })
}

// 递归获取权限码
export function findPermission(res: Array<OriginRoute>) {
  const permission: Array<string> = []
  res.forEach((it) => {
    if (it.perme) {
      permission.push(it.perme)
    }
    if (it.children && it.children.length > 0) {
      permission.push(...findPermission(it.children))
    }
  })
  return permission
}
