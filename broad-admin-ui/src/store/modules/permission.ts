import type { RouteRecordRaw } from 'vue-router'
import { defineStore } from 'pinia'
import router from '@/router'
import pinia from '@/store/pinia'
import { findAffixedRoutes, findPermission, generatorRoutes, mapTwoLevelRouter } from '../help'
import { getRoutes } from '@/api/common'
import { asyncRoutes } from '@/router/routes/async'
import useVisitedRouteStore from '@/store/modules/visited-routes'
import { ref, computed } from 'vue'
import type { StoreDefinition } from 'pinia'

interface PermissionStoreState {
  permissionRoutes: RouteRecordRaw[]
  permission: string[]
  localRoutes: any[]
}

export const usePermissionStore: StoreDefinition = defineStore('permission-route', () => {
  // 状态定义
  const permissionRoutes = ref<RouteRecordRaw[]>([])
  const permission = ref<string[]>([])
  const localRoutes = ref<any[]>([])
  const addedRouteNames = ref<string[]>([])

  const NOT_FOUND_ROUTE_NAME = 'RouteNotFound'

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
  const saveRoutes = (routes: any[] = []) => {
    localRoutes.value = routes
    localStorage.setItem('permission-routes', JSON.stringify(routes))
  }

  const loadLocalRoutes = () => {
    const savedRoutes = localStorage.getItem('permission-routes')
    if (savedRoutes) {
      try {
        localRoutes.value = JSON.parse(savedRoutes)
        return true
      } catch (error) {
        console.warn('[permission] 解析本地路由失败，自动清空。', error)
        localRoutes.value = []
        localStorage.removeItem('permission-routes')
      }
    }
    return false
  }

  const mountNotFoundRoute = () => {
    if (!router.hasRoute(NOT_FOUND_ROUTE_NAME)) {
      router.addRoute({
        path: '/:pathMatch(.*)*',
        name: NOT_FOUND_ROUTE_NAME,
        redirect: '/404',
        meta: {
          hidden: true
        }
      })
    }
  }

  const unmountRuntimeRoutes = () => {
    addedRouteNames.value.forEach((name) => {
      if (name && router.hasRoute(name)) {
        router.removeRoute(name)
      }
    })
    addedRouteNames.value = []
    if (router.hasRoute(NOT_FOUND_ROUTE_NAME)) {
      router.removeRoute(NOT_FOUND_ROUTE_NAME)
    }
  }

  const getRoutesAction = async () => {
    let remoteRoutes: any[] = []
    // 优先从本地加载路由信息
    if (loadLocalRoutes() && localRoutes.value.length > 0) {
      remoteRoutes = localRoutes.value
    } else {
      // 如果本地没有路由信息，从后端获取
      const res: any = await getRoutes()
      remoteRoutes = res.data || []
      saveRoutes(remoteRoutes)
    }

    const dynamicRoutes = generatorRoutes(remoteRoutes || [])
    permission.value = findPermission(remoteRoutes || [])
    return [...asyncRoutes, ...dynamicRoutes]
  }

  const initPermissionRoute = async () => {
    unmountRuntimeRoutes()
    // 加载路由
    const tempRoutes = await getRoutesAction()
    const mapRoutes = mapTwoLevelRouter(tempRoutes)
    addedRouteNames.value = []
    mapRoutes.forEach((it: any) => {
      router.addRoute(it)
      it.name && addedRouteNames.value.push(it.name as string)
    })
    mountNotFoundRoute()
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
    clearPermissionRoute()
  }

  const hasPermissionExists = (permissions: Array<string> | string) => {
    const permissionList = Array.isArray(permissions) ? permissions : [permissions]
    return permissionList.every((item: string) => permission.value.includes(item))
  }

  const clearPermissionRoute = () => {
    unmountRuntimeRoutes()
    permissionRoutes.value = []
    permission.value = []
    localRoutes.value = []
    localStorage.removeItem('permission-routes')
  }

  return {
    // 状态
    permissionRoutes,
    permission,
    localRoutes,
    // 计算属性
    getPermissionSideBar,
    getPermissionSplitTabs,
    // Actions
    saveRoutes,
    loadLocalRoutes,
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
