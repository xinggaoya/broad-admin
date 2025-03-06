import { defineStore } from 'pinia'
import pinia from '@/store/pinia'
import type { UserState } from '../types'
import { logoutAdmin } from '@/api/common'
import Avatar from '@/assets/defaultProfilePicture.gif'
import router from '@/router'
import { ref } from 'vue'
import { usePermissionStore } from '@/store/modules/permission'
import { useVisitedRouteStore } from '@/store/modules/visited-routes'

const defaultAvatar = Avatar

export const useUserStore = defineStore(
  'user-info',
  () => {
    const userId = ref(0)
    const userName = ref('')
    const roleId = ref(0)
    const avatar = ref(defaultAvatar)
    const token = ref('')
    const nickName = ref('')

    const visitedRouteStore = useVisitedRouteStore()

    const clearUserInfo = () => {
      userId.value = 0
      userName.value = ''
      roleId.value = 0
      avatar.value = defaultAvatar
      token.value = ''
      nickName.value = ''
      // 清理路由
      usePermissionStore().clearPermissionRoute()
      // 清理本地存储的路由信息
      localStorage.removeItem('permission-routes')
      goLogin()
    }

    const saveUser = (userInfo: UserState) => {
        return new Promise<UserState>((resolve) => {
          userId.value = userInfo.id
          roleId.value = userInfo.roleId
          token.value = userInfo.tokenValue
          userName.value = userInfo.userName
          nickName.value = userInfo.nickName
          if (userInfo.avatar) {
            avatar.value = import.meta.env.VITE_BASE_AXIOS_URL + userInfo.avatar
          } else {
            avatar.value = defaultAvatar
          }
          resolve(userInfo)
        })
      },
      // 判断token是否过期
      isTokenExpire = () => {
        return !token.value
      },
      // 修改昵称
      changeNickName = (newNickName: string) => {
        nickName.value = newNickName
      },
      // 删除用户信息
      removeUser = () => {
        localStorage.removeItem('user-info')
        // 清理本地存储的路由信息
        localStorage.removeItem('permission-routes')
        visitedRouteStore.clearVisitedView()
        sessionStorage.clear()
      },
      // 跳转到登录页
      goLogin = () => {
        removeUser()
        router
          .replace({
            path: '/login'
          })
          .then((r) => r)
      },
      // 退出登录
      logout = () => {
        return new Promise<void>((resolve) => {
          logoutAdmin().then(() => {
            clearUserInfo()
            goLogin()
            resolve()
          })
        })
      },
      setAvatar = (newAvatar: string) => {
        avatar.value = newAvatar
      }

    return {
      setAvatar,
      clearUserInfo,
      userId,
      userName,
      roleId,
      avatar,
      token,
      nickName,
      saveUser,
      isTokenExpire,
      changeNickName,
      removeUser,
      goLogin,
      logout
    }
  },
  {
    presist: {
      enable: true,
      resetToState: true
    }
  }
)

export default useUserStore

// 外部使用
export const useUserStoreHook = () => {
  return useUserStore(pinia)
}
