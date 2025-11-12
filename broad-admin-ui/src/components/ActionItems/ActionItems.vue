<template>
  <div class="action-items-wrapper">
    <span v-if="appConfig.actionBar.isShowSearch" class="action-item" @click="onShowSearch">
      <n-icon size="18">
        <SearchIcon />
      </n-icon>
    </span>
    <n-popover :width="320" placement="bottom" trigger="click">
      <template #trigger>
        <n-badge
          v-if="appConfig.actionBar.isShowMessage"
          :value="badgeValue"
          :max="99"
          class="badge-action-item"
        >
          <n-icon size="18">
            <NotificationsIcon />
          </n-icon>
        </n-badge>
        <div v-else></div>
      </template>
      <PopoverMessage
        @clear="handleClear"
        @read="handleRead"
        @viewAll="goToMessagePage"
        ref="popoverMessageRef"
      />
    </n-popover>
    <span v-if="appConfig.actionBar.isShowRefresh" class="action-item" @click="onRefrehRoute">
      <n-icon size="18">
        <RefreshIcon />
      </n-icon>
    </span>
    <span v-if="appConfig.actionBar.isShowFullScreen" class="action-item" @click="onScreenFull">
      <n-icon size="18">
        <ExpandIcon />
      </n-icon>
    </span>
    <button
      class="action-item action-item--button"
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
watch(() => messageStore.unreadCount, (val) => {
  badgeValue.value = val
}, { immediate: true })

// 监听用户ID变化，加载消息
watch(() => userStore.userId, (userId) => {
  if (userId) {
    messageStore.loadMessages(userId.toString())
  } else {
    messageStore.clearMessages()
  }
}, { immediate: true })

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
  position: relative;
  height: 100%;
  display: flex;
  align-items: center;
  z-index: 1;

  .action-item {
    min-width: 40px;
    display: flex;
    align-items: center;
    justify-content: center;

    &:hover {
      cursor: pointer;
      color: var(--primary-color-hover);
    }
  }

  .action-item--button {
    border: none;
    background: transparent;
    padding: 0;
    color: inherit;
  }

  .badge-action-item {
    cursor: pointer;
    margin-right: 30px;
  }
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
