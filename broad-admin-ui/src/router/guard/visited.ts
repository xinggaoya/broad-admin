import useVisitedRouteStore from '@/store/modules/visited-routes'
import type { RouteRecordRaw } from 'vue-router'
import router from '..'

function useVisitedGuard() {
  router.beforeEach((to) => {
    if (['404', '500', '403', 'not-found', 'Login'].includes(to.name as string)) {
      return true
    }
    const visitedRouteStore = useVisitedRouteStore()
    if (to.path.startsWith('/redirect')) {
      return true
    }
    if (to.meta.noShowTabbar) {
      return true
    }
    if (to.query?.noShowTabbar) {
      return true
    }
    if (to.meta.hidden === false) {
      visitedRouteStore.addVisitedRoute(to as unknown as RouteRecordRaw)
    }
    return true
  })
}

export default useVisitedGuard
