import { defineStore } from 'pinia'
import type { RouteRecordRaw } from 'vue-router'
import pinia from '../pinia'

const visitedRoutes = JSON.parse(localStorage.getItem('visited-routes') || '[]')

export const useVisitedRouteStore = defineStore('visited-routes', {
  state: () => {
    return {
      visitedRoutes: visitedRoutes as RouteRecordRaw[],
      isLoadAffix: false
    }
  },
  getters: {
    getVisitedRoutes(state) {
      return state.visitedRoutes
    },
    // 获取缓存路由名称 it.meta.cacheable = true
    getCachedRouteNames(state) {
      const cachedRoutes: string[] = []
      state.visitedRoutes.forEach((it) => {
        if (it.meta && it.meta.cacheable) {
          cachedRoutes.push(it.name as string)
        }
      })
      return cachedRoutes
    }
  },
  actions: {
    initAffixRoutes(affixRoutes: RouteRecordRaw[]) {
      affixRoutes.reverse().forEach((affixRoute) => {
        if (!this.visitedRoutes.find((it) => it.path === affixRoute.path)) {
          this.visitedRoutes.unshift(affixRoute)
        }
      })
      this.isLoadAffix = true
    },
    addVisitedRoute(route: RouteRecordRaw) {
      return new Promise((resolve) => {
        if (!this.visitedRoutes.find((it) => it.path === route.path)) {
          this.visitedRoutes.push(route)
          this.persistentVisitedView()
        }
        resolve(route)
      })
    },
    removeVisitedRoute(route: RouteRecordRaw) {
      return new Promise<string>((resolve) => {
        this.visitedRoutes.splice(this.visitedRoutes.indexOf(route), 1)
        this.persistentVisitedView()
        resolve(this.findLastRoutePath())
      })
    },
    findLastRoutePath() {
      return this.visitedRoutes && this.visitedRoutes.length > 0
        ? this.visitedRoutes[this.visitedRoutes.length - 1].path
        : '/'
    },
    closeLeftVisitedView(selectRoute: RouteRecordRaw) {
      return new Promise((resolve) => {
        const selectIndex = this.visitedRoutes.indexOf(selectRoute)
        if (selectIndex !== -1) {
          this.visitedRoutes = this.visitedRoutes.filter((it, index) => {
            return (it.meta && it.meta.affix) || index >= selectIndex
          })
          this.persistentVisitedView()
        }
        resolve(selectRoute)
      })
    },
    closeRightVisitedView(selectRoute: RouteRecordRaw) {
      return new Promise((resolve) => {
        const selectIndex = this.visitedRoutes.indexOf(selectRoute)
        if (selectIndex !== -1) {
          this.visitedRoutes = this.visitedRoutes.filter((it, index) => {
            return (it.meta && it.meta.affix) || index <= selectIndex
          })
          this.persistentVisitedView()
        }
        resolve(selectRoute)
      })
    },
    closeAllVisitedView() {
      return new Promise<void>((resolve) => {
        this.visitedRoutes = this.visitedRoutes.filter((it) => {
          return it.meta && it.meta.affix
        })
        this.persistentVisitedView()
        resolve()
      })
    },
    persistentVisitedView() {
      const tempPersistendRoutes = this.visitedRoutes.map((it) => {
        return {
          fullPath: it.path,
          meta: it.meta,
          name: it.name,
          path: it.path
        }
      })
      localStorage.setItem(this.$id, JSON.stringify(tempPersistendRoutes))
    },
    restoreVisitedView() {
      this.$reset()
    },
    clearVisitedView() {
      this.visitedRoutes = []
      this.isLoadAffix = false
      localStorage.removeItem(this.$id)
    }
  }
  // 由于需要自定义持久化过程，所以这里就不能用插件来实现
  // presist: {
  //   enable: true,
  // },
})

export function useVisitedRoutesContext() {
  return useVisitedRouteStore(pinia)
}

export default useVisitedRouteStore
