<template>
  <n-card
    :content-style="{ padding: '0px' }"
    :footer-style="{ padding: '0px' }"
    :bordered="false"
    :segmented="{ footer: true }"
    class="message-popover"
  >
    <div class="message-panel">
      <n-spin :show="loading" size="small">
        <n-scrollbar v-if="messages.length" class="message-scroll">
          <article
            v-for="item in messages"
            :key="item.id"
            class="message-card"
            :class="{ 'message-card--unread': item.status === 0 }"
            @click="handleMessageClick(item)"
          >
            <div class="message-card__icon">
              <n-icon size="20">
                <NotificationsCircle />
              </n-icon>
            </div>
            <div class="message-card__content">
              <header class="message-card__header">
                <span class="message-card__title" :title="item.title">{{ item.title }}</span>
                <n-badge v-if="item.status === 0" dot type="error" />
              </header>
              <n-ellipsis :line-clamp="2" class="message-card__body">
                {{ item.content }}
              </n-ellipsis>
              <time class="message-card__time">{{ formatTime(item.createTime) }}</time>
            </div>
          </article>
        </n-scrollbar>
        <n-empty v-else description="暂无消息" class="message-empty" size="small" />
      </n-spin>
    </div>

    <template #footer>
      <div class="message-footer">
        <n-button text size="small" class="message-footer__btn" @click="markAllAsRead">
          <template #icon>
            <n-icon><Checkmark /></n-icon>
          </template>
          全部已读
        </n-button>
        <n-button text size="small" class="message-footer__btn" @click="$emit('viewAll')">
          <template #icon>
            <n-icon><MailOpen /></n-icon>
          </template>
          查看全部
        </n-button>
      </div>
    </template>
  </n-card>
</template>

<script lang="ts" setup>
import { onMounted, ref, watch } from 'vue'
import { NotificationsCircle, Checkmark, MailOpen } from '@vicons/ionicons5'
import { useUserStore } from '@/store/modules/user'
import { getMessageList, markAsRead } from '@/api/message'
import { formatTime } from '@/utils'

interface MessageItem {
  id: number
  title: string
  content: string
  status: number
  createTime: string
}

const emit = defineEmits<{
  (e: 'clear'): void
  (e: 'viewAll'): void
  (e: 'read', messageId: number): void
}>()

const userStore = useUserStore()
const messages = ref<MessageItem[]>([])
const loading = ref(false)

async function loadMessages() {
  if (!userStore.userId) {
    messages.value = []
    return
  }

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

async function handleMessageClick(message: MessageItem) {
  if (message.status !== 0) return
  try {
    await markAsRead(message.id)
    message.status = 1
    emit('read', message.id)
  } catch (error) {
    console.error('标记已读失败:', error)
  }
}

async function markAllAsRead() {
  const unreadIds = messages.value.filter((item) => item.status === 0).map((item) => item.id)
  if (!unreadIds.length) return

  try {
    await Promise.all(unreadIds.map((id) => markAsRead(id)))
    messages.value = messages.value.map((item) => ({ ...item, status: 1 }))
    emit('clear')
  } catch (error) {
    console.error('全部标记已读失败:', error)
  }
}

defineExpose({ loadMessages })

watch(
  () => userStore.userId,
  () => {
    loadMessages()
  },
  { immediate: true }
)

onMounted(() => {
  loadMessages()
})
</script>

<style scoped>
.message-popover {
  width: 320px;
  background: var(--shell-panel-bg);
  border-radius: var(--shell-radius-lg);
  box-shadow: var(--shell-shadow);
}

.message-panel {
  padding: 4px 0;
}

.message-scroll {
  max-height: 360px;
}

.message-empty {
  padding: 32px 0;
}

.message-card {
  display: flex;
  gap: 12px;
  padding: 12px 16px;
  border-bottom: 1px solid var(--shell-panel-border);
  cursor: pointer;
  transition: background 0.2s ease, border-color 0.2s ease;
}

.message-card:last-of-type {
  border-bottom: none;
}

.message-card:hover {
  background: var(--shell-control-bg);
  border-color: var(--shell-control-border);
}

.message-card--unread .message-card__title {
  color: var(--color-primary);
}

.message-card__icon {
  width: 36px;
  height: 36px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--shell-control-bg);
  color: var(--color-primary);
  flex-shrink: 0;
}

.message-card__content {
  flex: 1;
  min-width: 0;
}

.message-card__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.message-card__title {
  font-size: 14px;
  font-weight: 600;
  color: var(--shell-text-color);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.message-card__body {
  margin-top: 4px;
  color: var(--shell-muted-text-color);
}

.message-card__time {
  display: block;
  margin-top: 6px;
  font-size: 12px;
  color: var(--shell-muted-text-color);
}

.message-footer {
  display: flex;
  justify-content: space-between;
  padding: 6px 4px;
}

.message-footer__btn {
  color: var(--shell-text-color);
}

:deep(.n-scrollbar-content) {
  padding: 0 !important;
}
</style>
