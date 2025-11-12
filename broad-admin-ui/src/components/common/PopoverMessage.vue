<template>
  <n-card
    :content-style="{ padding: '0px' }"
    :footer-style="{ padding: '0px' }"
    :bordered="false"
    :segmented="true"
    class="w-80"
  >
    <n-spin v-if="loading" />
    <n-scrollbar v-else-if="messages.length > 0" style="max-height: 400px">
      <div
        v-for="item in messages"
        :key="item.id"
        class="p-3 hover:bg-gray-50 cursor-pointer border-b border-gray-100 hover:border-gray-300 transition-colors"
        @click="handleMessageClick(item)"
      >
        <n-flex align="flex-start">
          <n-icon size="24" color="#1890ff" class="mt-1">
            <NotificationsCircle />
          </n-icon>
          <div class="flex-1 min-w-0">
            <div class="flex items-center justify-between">
              <div class="font-medium text-gray-900 truncate">{{ item.title }}</div>
              <n-badge v-if="item.status === 0" dot type="error" />
            </div>
            <n-ellipsis :line-clamp="2" class="text-gray-500 text-sm mt-1">
              {{ item.content }}
            </n-ellipsis>
            <div class="text-xs text-gray-400 mt-2">
              {{ formatTime(item.createTime) }}
            </div>
          </div>
        </n-flex>
      </div>
    </n-scrollbar>
    <n-empty v-else description="暂无消息" class="py-10" />

    <template #footer>
      <n-space style="padding: 7px 0" justify="space-between" class="w-full">
        <n-button text @click="markAllAsRead">
          <template #icon>
            <n-icon><Checkmark /></n-icon>
          </template>
          全部已读
        </n-button>
        <n-button text @click="$emit('viewAll')">
          <template #icon>
            <n-icon><MailOpen /></n-icon>
          </template>
          查看全部
        </n-button>
      </n-space>
    </template>
  </n-card>
</template>

<script lang="ts" setup>
import { ref, onMounted, watch } from 'vue'
import { NotificationsCircle, Checkmark, MailOpen } from '@vicons/ionicons5'
import { useUserStore } from '@/store/modules/user'
import { getMessageList, markAsRead } from '@/api/message'
import { formatTime } from '@/utils'

const emit = defineEmits(['clear', 'viewAll', 'read'])

const userStore = useUserStore()
const messages = ref<any[]>([])
const loading = ref(false)

// 加载消息列表
async function loadMessages() {
  if (!userStore.userId) return

  loading.value = true
  try {
    const res = await getMessageList(userStore.userId)
    if (res.code === 200) {
      messages.value = res.data || []
    }
  } catch (error) {
    console.error('加载消息失败:', error)
  } finally {
    loading.value = false
  }
}

// 处理消息点击
async function handleMessageClick(message: any) {
  if (message.status === 0) {
    try {
      await markAsRead(message.id)
      message.status = 1
      emit('read', message.id)
    } catch (error) {
      console.error('标记已读失败:', error)
    }
  }
}

// 全部标记为已读
async function markAllAsRead() {
  const unreadIds = messages.value.filter(m => m.status === 0).map(m => m.id)
  if (unreadIds.length === 0) return

  try {
    for (const id of unreadIds) {
      await markAsRead(id)
    }
    messages.value.forEach(m => {
      if (m.status === 0) m.status = 1
    })
    emit('clear')
  } catch (error) {
    console.error('全部标记已读失败:', error)
  }
}

// 暴露方法给父组件
defineExpose({
  loadMessages
})

// 监听userId变化
watch(() => userStore.userId, () => {
  loadMessages()
})

// 初始加载
onMounted(() => {
  loadMessages()
})
</script>

<style scoped>
:deep(.n-scrollbar-content) {
  padding: 0 !important;
}
</style>
