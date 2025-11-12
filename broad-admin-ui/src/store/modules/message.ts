import { defineStore } from 'pinia'
import pinia from '@/store/pinia'
import { ref, computed } from 'vue'
import { getMessageList, getUnreadCount, markAsRead, deleteMessage } from '@/api/message'
import type { ResultData } from '@/api/request'

export interface MessageState {
  id: number
  userId: number
  title: string
  content: string
  status: number // 0-未读 1-已读
  type: number // 0系统消息 1通知公告 2待办提醒
  createTime: string
  updateTime?: string
}

export const useMessageStore = defineStore(
  'message',
  () => {
    const messages = ref<MessageState[]>([])
    const unreadCount = ref(0)
    const loading = ref(false)
    const userId = ref('')

    // 未读消息列表
    const unreadMessages = computed(() => {
      return messages.value.filter(msg => msg.status === 0)
    })

    // 已读消息列表
    const readMessages = computed(() => {
      return messages.value.filter(msg => msg.status === 1)
    })

    // 加载消息列表
    const loadMessages = async (uid: string) => {
      if (!uid) return

      userId.value = uid
      loading.value = true
      try {
        const res = await getMessageList(uid) as ResultData
        if (res.code === 200) {
          messages.value = res.data || []
          await updateUnreadCount(uid)
        }
      } catch (error) {
        console.error('加载消息失败:', error)
      } finally {
        loading.value = false
      }
    }

    // 更新未读数量
    const updateUnreadCount = async (uid: string) => {
      if (!uid) return

      try {
        const res = await getUnreadCount(uid) as ResultData
        if (res.code === 200) {
          unreadCount.value = res.data || 0
        }
      } catch (error) {
        console.error('获取未读数量失败:', error)
      }
    }

    // 标记单条消息为已读
    const markMessageAsRead = async (messageId: number) => {
      try {
        const res = await markAsRead(messageId) as ResultData
        if (res.code === 200) {
          const message = messages.value.find(msg => msg.id === messageId)
          if (message && message.status === 0) {
            message.status = 1
            unreadCount.value = Math.max(0, unreadCount.value - 1)
          }
        }
        return res
      } catch (error) {
        console.error('标记已读失败:', error)
        throw error
      }
    }

    // 新增消息（通过WebSocket推送）
    const addNewMessage = (message: MessageState) => {
      messages.value.unshift(message)
      if (message.status === 0) {
        unreadCount.value++
      }
    }

    // 全部标记为已读
    const markAllAsRead = async () => {
      if (unreadMessages.value.length === 0) return

      try {
        const unreadIds = unreadMessages.value.map(msg => msg.id)
        for (const id of unreadIds) {
          await markAsRead(id)
        }

        // 更新本地状态
        messages.value.forEach(msg => {
          if (msg.status === 0) {
            msg.status = 1
          }
        })
        unreadCount.value = 0
      } catch (error) {
        console.error('全部标记已读失败:', error)
        throw error
      }
    }

    // 删除消息
    const removeMessage = async (messageId: number) => {
      try {
        const res = await deleteMessage([messageId]) as ResultData
        if (res.code === 200) {
          const index = messages.value.findIndex(msg => msg.id === messageId)
          if (index > -1) {
            const message = messages.value[index]
            if (message.status === 0) {
              unreadCount.value = Math.max(0, unreadCount.value - 1)
            }
            messages.value.splice(index, 1)
          }
        }
        return res
      } catch (error) {
        console.error('删除消息失败:', error)
        throw error
      }
    }

    // 批量删除消息
    const batchDeleteMessages = async (messageIds: number[]) => {
      try {
        const res = await deleteMessage(messageIds) as ResultData
        if (res.code === 200) {
          const deletedMessages = messages.value.filter(msg => messageIds.includes(msg.id))
          const deletedUnreadCount = deletedMessages.filter(msg => msg.status === 0).length

          messages.value = messages.value.filter(msg => !messageIds.includes(msg.id))
          unreadCount.value = Math.max(0, unreadCount.value - deletedUnreadCount)
        }
        return res
      } catch (error) {
        console.error('批量删除消息失败:', error)
        throw error
      }
    }

    // 清空消息
    const clearMessages = () => {
      messages.value = []
      unreadCount.value = 0
    }

    return {
      // state
      messages,
      unreadCount,
      loading,
      userId,

      // getters
      unreadMessages,
      readMessages,

      // actions
      loadMessages,
      updateUnreadCount,
      markMessageAsRead,
      addNewMessage,
      markAllAsRead,
      removeMessage,
      batchDeleteMessages,
      clearMessages
    }
  },
  {
    persist: {
      enabled: false // 不持久化，每次都从服务器获取最新数据
    }
  }
)

export default useMessageStore

// 外部使用
export const useMessageStoreHook = () => {
  return useMessageStore(pinia)
}
