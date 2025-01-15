import { defineStore } from 'pinia'
import type { RouteRecordRaw } from 'vue-router'
import pinia from '../pinia'
import { ref, computed } from 'vue'
import type { StoreDefinition } from 'pinia'

export const useVisitedRouteStore: StoreDefinition = defineStore('visited-routes', () => {
  // 状态定义
  const routes = ref<RouteRecordRaw[]>([])
  const isLoadAffix = ref(false)

  // 计算属性
  const getVisitedRoutes = computed(() => routes.value)

  // 获取缓存路由名称
  const getCachedRouteNames = computed(() => {
    const cachedRoutes: string[] = []
    routes.value.forEach((it) => {
      if (it.meta && it.meta.cacheable) {
        cachedRoutes.push(it.name as string)
      }
    })
    return cachedRoutes
  })

  // Actions
  const initAffixRoutes = (affixRoutes: RouteRecordRaw[]) => {
    affixRoutes.reverse().forEach((affixRoute) => {
      if (!routes.value.find((it) => it.path === affixRoute.path)) {
        routes.value.unshift(affixRoute)
      }
    })
    isLoadAffix.value = true
  }

  const addVisitedRoute = (route: RouteRecordRaw) => {
    return new Promise((resolve) => {
      if (!routes.value.find((it) => it.path === route.path)) {
        routes.value.push(route)
      }
      resolve(route)
    })
  }

  const removeVisitedRoute = (route: RouteRecordRaw) => {
    return new Promise<string>((resolve) => {
      routes.value.splice(routes.value.indexOf(route), 1)
      resolve(findLastRoutePath())
    })
  }

  const findLastRoutePath = () => {
    return routes.value && routes.value.length > 0
      ? routes.value[routes.value.length - 1].path
      : '/'
  }

  const closeLeftVisitedView = (selectRoute: RouteRecordRaw) => {
    return new Promise((resolve) => {
      const selectIndex = routes.value.indexOf(selectRoute)
      if (selectIndex !== -1) {
        routes.value = routes.value.filter((it, index) => {
          return (it.meta && it.meta.affix) || index >= selectIndex
        })
      }
      resolve(selectRoute)
    })
  }

  const closeRightVisitedView = (selectRoute: RouteRecordRaw) => {
    return new Promise((resolve) => {
      const selectIndex = routes.value.indexOf(selectRoute)
      if (selectIndex !== -1) {
        routes.value = routes.value.filter((it, index) => {
          return (it.meta && it.meta.affix) || index <= selectIndex
        })
      }
      resolve(selectRoute)
    })
  }

  const closeAllVisitedView = () => {
    return new Promise<void>((resolve) => {
      routes.value = routes.value.filter((it) => {
        return it.meta && it.meta.affix
      })
      resolve()
    })
  }

  const restoreVisitedView = () => {
    routes.value = []
    isLoadAffix.value = false
  }

  const clearVisitedView = () => {
    routes.value = []
    isLoadAffix.value = false
  }

  return {
    // 状态
    routes,
    isLoadAffix,
    // 计算属性
    getVisitedRoutes,
    getCachedRouteNames,
    // Actions
    initAffixRoutes,
    addVisitedRoute,
    removeVisitedRoute,
    findLastRoutePath,
    closeLeftVisitedView,
    closeRightVisitedView,
    closeAllVisitedView,
    restoreVisitedView,
    clearVisitedView
  }
})

export function useVisitedRoutesContext(): ReturnType<typeof useVisitedRouteStore> {
  return useVisitedRouteStore(pinia)
}

export default useVisitedRouteStore
