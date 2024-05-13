import type { App } from 'vue'
import { usePermissionStore } from '@/store/modules/permission'

// 权限指令，用于判断按钮权限
export function auth(app: App) {
  const permissionStore = usePermissionStore()
  app.directive('auth', {
    mounted: (el, binding) => {
      let { value } = binding
      value = Array.isArray(value) ? value : [value]
      const hasPermission = permissionStore.hasPermissionExists(value)
      if (!hasPermission) {
        el.remove()
      }
    }
  })
}
