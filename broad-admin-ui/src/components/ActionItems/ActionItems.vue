<template>
  <div class="action-items-wrapper">
    <button
      v-if="appConfig.actionBar.isShowSearch"
      class="action-item"
      type="button"
      aria-label="全局搜索"
      @click="onShowSearch"
    >
      <n-icon size="18">
        <SearchIcon />
      </n-icon>
    </button>

    <n-popover v-if="appConfig.actionBar.isShowMessage" :width="320" placement="bottom" trigger="click">
      <template #trigger>
        <button class="action-item action-item--badge" type="button" aria-label="查看消息">
          <n-badge :value="badgeValue" :max="99">
            <n-icon size="18">
              <NotificationsIcon />
            </n-icon>
          </n-badge>
        </button>
      </template>
      <PopoverMessage
        ref="popoverMessageRef"
        @clear="handleClear"
        @read="handleRead"
        @viewAll="goToMessagePage"
      />
    </n-popover>

    <button
      v-if="appConfig.actionBar.isShowRefresh"
      class="action-item"
      type="button"
      aria-label="刷新页面"
      @click="onRefrehRoute"
    >
      <n-icon size="18">
        <RefreshIcon />
      </n-icon>
    </button>

    <button
      v-if="appConfig.actionBar.isShowFullScreen"
      class="action-item"
      type="button"
      aria-label="切换全屏"
      @click="onScreenFull"
    >
      <n-icon size="18">
        <ExpandIcon />
      </n-icon>
    </button>

    <button
      class="action-item"
      type="button"
      aria-label="打开系统设置"
      data-testid="action-open-settings"
      @click="onShowSetting"
    >
      <n-icon size="18">
        <SettingIcon />
      </n-icon>
    </button>

    <SearchContent ref="searchContentRef" />
    <Setting ref="settingRef" />
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, watch } from 'vue'
import { useMessage } from 'naive-ui'
import screenfull from 'screenfull'
import { useRoute, useRouter } from 'vue-router'
import type { SearchContentType } from '@/types/components'
import {
  SettingsOutline as SettingIcon,
  SearchOutline as SearchIcon,
  Expand as ExpandIcon,
  NotificationsOutline as NotificationsIcon,
  RefreshOutline as RefreshIcon
} from '@vicons/ionicons5'
import { useAppConfigStore } from '@/store/modules/app-config'
import { useMessageStore } from '@/store/modules/message'
import { useUserStore } from '@/store/modules/user'
import { useDebounceFn } from '@vueuse/core'
import PopoverMessage from '@/components/common/PopoverMessage.vue'
import SearchContent from './SearchContent.vue'

const searchContentRef = ref<SearchContentType>()
const settingRef = ref()
const popoverMessageRef = ref()
const badgeValue = ref(0)
const appConfig = useAppConfigStore()
const messageStore = useMessageStore()
const userStore = useUserStore()
const message = useMessage()
const router = useRouter()
const route = useRoute()

// 监听未读数量变化
watch(
  () => messageStore.unreadCount,
  (val) => {
    badgeValue.value = val
  },
  { immediate: true }
)

// 监听用户ID变化，加载消息
watch(
  () => userStore.userId,
  (userId) => {
    if (userId) {
      messageStore.loadMessages(userId.toString())
    } else {
      messageStore.clearMessages()
    }
  },
  { immediate: true }
)

function onShowSearch() {
  searchContentRef.value?.onShow()
}

function onScreenFull() {
  if (!screenfull.isEnabled) {
    message.error('当前浏览器不支持全屏操作')
    return false
  }
  screenfull.toggle()
}

const fn = useDebounceFn(() => {
  router.replace({ path: '/redirect' + route.path })
})

function onRefrehRoute() {
  fn()
}

function onShowSetting() {
  settingRef.value.openDrawer()
}

function handleClear() {
  badgeValue.value = 0
}

function handleRead(messageId: number) {
  // 单个消息标记为已读时更新徽章
  badgeValue.value = Math.max(0, badgeValue.value - 1)
}

function goToMessagePage() {
  router.push('/system/message')
}

// 初始加载
onMounted(() => {
  if (userStore.userId) {
    messageStore.loadMessages(userStore.userId.toString())
  }
})
</script>

<style lang="scss" scoped>
.action-items-wrapper {
  display: flex;
  align-items: center;
  gap: 6px;
  z-index: 1;
}

.action-item {
  width: 40px;
  height: 40px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: var(--shell-radius-base);
  border: 1px solid transparent;
  background: transparent;
  color: inherit;
  transition: background 0.2s ease, border-color 0.2s ease, color 0.2s ease;

  &:hover {
    cursor: pointer;
    border-color: var(--shell-control-border);
    background: var(--shell-control-bg);
    color: var(--color-primary);
  }

  &:focus-visible {
    outline: none;
    border-color: var(--shell-control-border);
    box-shadow: 0 0 0 2px var(--shell-control-border);
  }
}

.action-item--badge {
  padding: 0 4px;
}
</style>
<style lang="scss" scoped>
:deep(.n-input .n-input__border, .n-input .n-input__state-border) {
  border: none;
  border-bottom: 1px solid currentColor;
}

:deep(.el-input__inner) {
  border: none !important;
  height: 35px;
  line-height: 35px;
  color: currentColor !important;
  background-color: transparent !important;
}
</style>
