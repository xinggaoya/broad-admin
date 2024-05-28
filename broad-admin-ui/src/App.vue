<template>
  <n-config-provider
    :locale="zhCN"
    :theme="theme"
    :theme-overrides="themeOverrides"
    style="height: 100%"
  >
    <n-notification-provider>
      <n-message-provider>
        <router-view />
      </n-message-provider>
    </n-notification-provider>
  </n-config-provider>
</template>

<script lang="ts" setup>
import { computed } from 'vue'
import { darkTheme, zhCN } from 'naive-ui'
import useAppConfigStore from '@/store/modules/app-config'
import { ThemeMode } from '@/store/types'

const appConfig = useAppConfigStore()
const theme = computed(() => {
  return appConfig.theme === ThemeMode.DARK ? darkTheme : null
})
const themeOverrides = computed(() => {
  return {
    common: appConfig.themeColor
  }
})
</script>
