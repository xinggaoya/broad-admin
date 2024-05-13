<template>
  <router-view v-slot="{ Component, route }">
    <transition :name="pageAnim + '-transform'" mode="out-in" appear>
      <keep-alive :include="cachedNames">
        <Iframe
          v-if="$route.meta.iframeUrl !== '' && $route.meta.iframeUrl !== undefined"
          :src="$route.meta.iframeUrl"
        />
        <component v-else :is="Component" :key="route.fullPath" />
      </keep-alive>
    </transition>
  </router-view>
</template>

<script lang="ts" setup>
import useAppConfigStore from '@/store/modules/app-config'
import { useUserStore } from '@/store/modules/user'
import { computed, onMounted } from 'vue'
import { useNotification } from 'naive-ui'
import { checkUser } from '@/api/common'
import { useVisitedRouteStore } from '@/store/modules/visited-routes'
import { getWebSocketUrl } from '@/utils'

const visitedRouteStore = useVisitedRouteStore()
const notification = useNotification()
const userStore = useUserStore()
const appConfig = useAppConfigStore()
const pageAnim = computed(() => appConfig.pageAnim)

let url = getWebSocketUrl()
const cachedNames = computed(() => {
  return visitedRouteStore.getCachedRouteNames
})
/**
 * Websoket连接成功事件
 * @param res
 */
const websocketonopen = (res: any) => {
  // console.log('WebSocket连接成功', res)
}
/**
 * Websoket接收消息事件
 * @param res
 */
const websocketonmessage = (res: any) => {
  const { data, code } = JSON.parse(res.data)
  if (code === 200 && userStore.token) {
    notification.create({
      title: data.title,
      content: data.content,
      description: data.description,
      meta: data.meta,
      duration: 0,
      closable: true
    })
    return
  }
  // 主动验证用户状态
  checkUser()
}
/**
 * Websoket连接错误事件
 * @param res
 */
const websocketonerror = (res: any) => {
  // 尝试重连
  const timer = setTimeout(() => {
    createWebSocket()
    clearTimeout(timer)
  }, 50000)
}
/**
 * Websoket断开事件
 * @param res
 */
const websocketclose = (res: any) => {
  // 断开连接后尝试重连
  const timer = setTimeout(() => {
    createWebSocket()
    clearTimeout(timer)
  }, 50000)
}

const createWebSocket = () => {
  try {
    const socket = new WebSocket(`${url}/api/ws/session/${userStore.userId}`)
    socket.onopen = websocketonopen
    socket.onmessage = websocketonmessage
    socket.onerror = websocketonerror
    socket.onclose = websocketclose
  } catch (e) {
    console.log(e)
  }
}

onMounted(() => {
  createWebSocket()
})
</script>
