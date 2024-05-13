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

export const usePermissionStore = defineStore('permission-route', {
  state: () => {
    return {
      permissionRoutes: [] as RouteRecordRaw[],
      permission: [] as string[]
    }
  },
  getters: {
    getPermissionSideBar(state) {
      return state.permissionRoutes.filter((it) => {
        return it.meta && !it.meta.hidden
      })
    },
    getPermissionSplitTabs(state) {
      return state.permissionRoutes.filter((it) => {
        return it.meta && !it.meta.hidden && it.children && it.children.length > 0
      })
    }
  },
  actions: {
    async getRoutes() {
      const res: any = await getRoutes()
      const tempRoutes = generatorRoutes(res.data || [])
      this.permission = findPermission(res.data || [])
      // 写入默认路由
      tempRoutes.unshift(...asyncRoutes)
      return tempRoutes
    },
    async initPermissionRoute() {
      // 加载路由
      const tempRoutes = await this.getRoutes()
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
      this.permissionRoutes = [...tempRoutes]

      // 加载路由 affix
      const visitedRouteStore = useVisitedRouteStore()
      const affixRoutes = findAffixedRoutes(router.getRoutes())
      visitedRouteStore.initAffixRoutes(affixRoutes)
    },
    isEmptyPermissionRoute() {
      return !this.permissionRoutes || this.permissionRoutes.length === 0
    },
    reset() {
      this.$reset()
    },
    hasPermissionExists(permission: Array<string> | string) {
      permission = Array.isArray(permission) ? permission : [permission]
      return permission.every((item: string) => this.permission.includes(item))
    },
    clearPermissionRoute() {
      this.permissionRoutes = []
      this.permission = []
    }
  }
})

export default function usePermissionStoreHook() {
  return usePermissionStore(pinia)
}
