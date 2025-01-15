import type { RouteRecordRaw } from 'vue-router'
import { defineStore } from 'pinia'
import router from '@/router'
import pinia from '@/store/pinia'
import {
  findAffixedRoutes,
  findPermission,
  findRootPathRoute,
  generatorRoutes,
  mapTwoLevelRouter
} from '../help'
import { getRoutes } from '@/api/common'
import { asyncRoutes } from '@/router/routes/async'
import useVisitedRouteStore from '@/store/modules/visited-routes'
import { ref, computed } from 'vue'
import type { StoreDefinition } from 'pinia'

interface PermissionStoreState {
  permissionRoutes: RouteRecordRaw[]
  permission: string[]
}

export const usePermissionStore: StoreDefinition = defineStore('permission-route', () => {
  // 状态定义
  const permissionRoutes = ref<RouteRecordRaw[]>([])
  const permission = ref<string[]>([])

  // 计算属性
  const getPermissionSideBar = computed(() => {
    return permissionRoutes.value.filter((it) => {
      return it.meta && !it.meta.hidden
    })
  })

  const getPermissionSplitTabs = computed(() => {
    return permissionRoutes.value.filter((it) => {
      return it.meta && !it.meta.hidden && it.children && it.children.length > 0
    })
  })

  // Actions
  const getRoutesAction = async () => {
    const res: any = await getRoutes()
    const tempRoutes = generatorRoutes(res.data || [])
    permission.value = findPermission(res.data || [])
    // 写入默认路由
    tempRoutes.unshift(...asyncRoutes)
    return tempRoutes
  }

  const initPermissionRoute = async () => {
    // 加载路由
    const tempRoutes = await getRoutesAction()
    const mapRoutes = mapTwoLevelRouter(tempRoutes)
    mapRoutes.forEach((it: any) => {
      router.addRoute(it)
    })
    const rootPath = findRootPathRoute(tempRoutes)
    // 配置 `/` 路由的默认跳转地址
    if (rootPath && rootPath !== '/') {
      router.addRoute({
        path: '/',
        redirect: rootPath,
        meta: {
          hidden: true
        }
      })
    }
    // 这个路由一定要放在最后
    router.addRoute({
      path: '/:pathMatch(.*)*',
      redirect: '/404',
      meta: {
        hidden: true
      }
    })
    permissionRoutes.value = [...tempRoutes]

    // 加载路由 affix
    const visitedRouteStore = useVisitedRouteStore()
    const affixRoutes = findAffixedRoutes(router.getRoutes())
    visitedRouteStore.initAffixRoutes(affixRoutes)
  }

  const isEmptyPermissionRoute = () => {
    return !permissionRoutes.value || permissionRoutes.value.length === 0
  }

  const reset = () => {
    permissionRoutes.value = []
    permission.value = []
  }

  const hasPermissionExists = (permissions: Array<string> | string) => {
    const permissionList = Array.isArray(permissions) ? permissions : [permissions]
    return permissionList.every((item: string) => permission.value.includes(item))
  }

  const clearPermissionRoute = () => {
    permissionRoutes.value = []
    permission.value = []
  }

  return {
    // 状态
    permissionRoutes,
    permission,
    // 计算属性
    getPermissionSideBar,
    getPermissionSplitTabs,
    // Actions
    getRoutesAction,
    initPermissionRoute,
    isEmptyPermissionRoute,
    reset,
    hasPermissionExists,
    clearPermissionRoute
  }
})

export default function usePermissionStoreHook(): ReturnType<typeof usePermissionStore> {
  return usePermissionStore(pinia)
}
