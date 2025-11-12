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

<script lang="ts" setup name="MainLayout">
import { useAppConfigStore } from '@/store/modules/app-config'
import { useUserStore } from '@/store/modules/user'
import { useMessageStore } from '@/store/modules/message'
import { computed, onMounted, ref } from 'vue'
import { useNotification } from 'naive-ui'
import { checkUser } from '@/api/common'
import { useVisitedRouteStore } from '@/store/modules/visited-routes'
import { getWebSocketUrl } from '@/utils'
import Iframe from '@/components/common/Iframe.vue'

const visitedRouteStore = useVisitedRouteStore()
const notification = useNotification()
const userStore = useUserStore()
const messageStore = useMessageStore()
const appConfig = useAppConfigStore()
const pageAnim = computed(() => appConfig.pageAnim)

let url = getWebSocketUrl()
const ws = ref<WebSocket | null>(null)

const cachedNames = computed(() => {
  return visitedRouteStore.getCachedRouteNames
})

/**
 * 解析WebSocket消息
 * @param data 消息数据
 * @returns 解析后的消息对象
 */
const parseWebSocketMessage = (data: string): any => {
  try {
    // 先尝试解析为JSON
    return JSON.parse(data)
  } catch (e) {
    // 如果解析失败，返回原始文本
    return {
      type: 'TEXT',
      payload: data
    }
  }
}

/**
 * Websoket连接成功事件
 * @param res
 */
const websocketonopen = (res: any) => {
  console.log('WebSocket连接成功', res)
}

/**
 * Websoket接收消息事件
 * @param res
 */
const websocketonmessage = (res: any) => {
  try {
    const message = parseWebSocketMessage(res.data)

    // 处理不同类型的消息
    switch (message.type) {
      case 'NOTIFICATION':
        handleNotification(message.payload)
        break
      case 'SYSTEM':
        handleSystemMessage(message.payload)
        break
      case 'TEXT':
        handleTextMessage(message.payload || message)
        break
      case 'ERROR':
        handleErrorMessage(message.payload || message)
        break
      case 'ACK':
        console.log('收到消息确认:', message.id)
        break
      case 'PING':
        // 心跳响应已在服务器端处理
        break
      case 'PONG':
        console.log('收到心跳响应')
        break
      default:
        console.log('收到未知类型消息:', message)
    }
  } catch (error) {
    console.error('处理WebSocket消息失败:', error)
  }
}

/**
 * 处理通知消息
 * @param data
 */
const handleNotification = (data: any) => {
  // 添加到消息列表
  if (data) {
    messageStore.addNewMessage(data)

    // 显示通知
    notification.create({
      title: data.title || '新消息',
      content: data.content || '您有一条新消息',
      meta: data.createTime || new Date().toLocaleString(),
      duration: 4500,
      closable: true,
      onClick: () => {
        // 点击通知时标记为已读
        if (data.id) {
          messageStore.markMessageAsRead(data.id)
        }
      }
    })
  }
}

/**
 * 处理系统消息
 * @param data
 */
const handleSystemMessage = (data: any) => {
  // 检查是否是SaToken的会话检查
  if (data?.code === 401 || data?.payload?.code === 401) {
    notification.error({
      title: '会话过期',
      content: '您的会话已过期，请重新登录',
      duration: 3000,
      closable: true
    })
    setTimeout(() => {
      userStore.logout()
    }, 2000)
    return
  }

  // 普通的系统消息
  if (data?.data) {
    notification.create({
      title: data.data.title || '系统消息',
      content: data.data.content || '',
      duration: 4000,
      closable: true
    })
  }
}

/**
 * 处理文本消息
 * @param data
 */
const handleTextMessage = (data: any) => {
  console.log('收到文本消息:', data)
}

/**
 * 处理错误消息
 * @param data
 */
const handleErrorMessage = (data: any) => {
  const errorMessage = typeof data === 'string' ? data : (data?.message || '未知错误')

  notification.error({
    title: '错误',
    content: errorMessage,
    duration: 5000,
    closable: true
  })
}

/**
 * Websoket连接错误事件
 * @param res
 */
const websocketonerror = (res: any) => {
  console.error('WebSocket连接错误:', res)
  // 尝试重连
  const timer = setTimeout(() => {
    createWebSocket()
    clearTimeout(timer)
  }, 5000)
}

/**
 * Websoket断开事件
 * @param res
 */
const websocketclose = (res: any) => {
  console.log('WebSocket连接断开:', res)
  // 断开连接后尝试重连
  const timer = setTimeout(() => {
    createWebSocket()
    clearTimeout(timer)
  }, 5000)
}

/**
 * 创建WebSocket连接
 */
const createWebSocket = () => {
  if (!userStore.userId) return
  if (ws.value) {
    ws.value.close()
    ws.value = null
  }

  try {
    // WebSocket握手时无法设置请求头，通过URL参数传递token
    const token = userStore.token
    const socketUrl = `${url}/api/ws/session/${userStore.userId}${token ? '?satoken=' + token : ''}`
    console.log('创建WebSocket连接:', socketUrl)
    ws.value = new WebSocket(socketUrl)
    ws.value.onopen = websocketonopen
    ws.value.onmessage = websocketonmessage
    ws.value.onerror = websocketonerror
    ws.value.onclose = websocketclose
  } catch (e) {
    console.error('创建WebSocket失败:', e)
  }
}

// 监听userId变化，重新连接
import { watch } from 'vue'
watch(() => userStore.userId, (newUserId) => {
  if (newUserId) {
    createWebSocket()
  } else if (ws.value) {
    ws.value.close()
    ws.value = null
  }
})

onMounted(() => {
  createWebSocket()
})
</script>
