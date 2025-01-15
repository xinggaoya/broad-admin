import { useUserStoreHook } from '@/store/modules/user'
import { usePermissionStore } from '@/store/modules/permission'
import router from '..'

const whiteRoutes: string[] = ['/login']

function usePermissionGuard() {
  router.beforeEach(async (to, from) => {
    if (whiteRoutes.includes(to.path)) {
      return true
    }
    const userStore = useUserStoreHook()
    if (userStore.isTokenExpire()) {
      userStore.clearUserInfo()
      return
    }
    const permissionStore = usePermissionStore()
    const isEmptyRoute = permissionStore.isEmptyPermissionRoute()
    if (isEmptyRoute) {
      await permissionStore.initPermissionRoute()
      return { ...to, replace: true }
    }
    return true
  })
}

export default usePermissionGuard
