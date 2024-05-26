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
    common: {
      primaryColor: '#409EFF',
      primaryColorHover: '#66b1ff',
      primaryColorPressed: '#3a8ee6',
      primaryColorSuppl: '#409eff',
      infoColor: '#909399',
      infoColorHover: '#a6a9ad',
      infoColorPressed: '#82848a',
      infoColorSuppl: '#909399',
      successColor: '#67c23a',
      successColorHover: '#85ce61',
      successColorPressed: '#5daf34',
      successColorSuppl: '#67c23a',
      warningColor: '#e6a23c',
      warningColorHover: '#ebb563',
      warningColorPressed: '#cf9236',
      warningColorSuppl: '#e6a23c',
      errorColor: '#f56c6c',
      errorColorHover: '#f78989',
      errorColorPressed: '#dd6161',
      errorColorSuppl: '#f56c6c'
    }
  }
})
</script>
