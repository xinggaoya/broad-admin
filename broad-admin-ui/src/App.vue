<template>
  <n-config-provider
    :theme="theme"
    :theme-overrides="themeOverrides"
    :locale="zhCN"
    :date-locale="dateZhCN"
  >
    <n-loading-bar-provider>
      <n-dialog-provider>
        <n-notification-provider>
          <n-message-provider>
            <router-view></router-view>
          </n-message-provider>
        </n-notification-provider>
      </n-dialog-provider>
    </n-loading-bar-provider>
  </n-config-provider>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { darkTheme, zhCN, dateZhCN } from 'naive-ui'
import { useAppConfigStore } from '@/store/modules/app-config'
import { ThemeMode } from '@/store/types'

const appConfig = useAppConfigStore()
const theme = computed(() => (appConfig.theme === ThemeMode.DARK ? darkTheme : null))
const themeOverrides = computed(() => ({
  common: {
    primaryColor: appConfig.themeColor.primaryColor,
    primaryColorHover: appConfig.themeColor.primaryColorHover,
    primaryColorPressed: appConfig.themeColor.primaryColorPressed,
    primaryColorSuppl: appConfig.themeColor.primaryColorSuppl
  }
}))
</script>

<style>
:root {
  --primary-color: v-bind('appConfig.themeColor.primaryColor');
  --primary-color-hover: v-bind('appConfig.themeColor.primaryColorHover');
  --primary-color-pressed: v-bind('appConfig.themeColor.primaryColorPressed');
  --menu-width: v-bind('appConfig.sideWidth + "px"');
}
</style>
