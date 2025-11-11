<template>
  <div
    class="app-shell-root"
    :style="themeVars"
    :data-theme="appConfig.theme"
    :data-device="appConfig.deviceType"
    data-testid="app-shell-root"
    :data-side-theme="appConfig.sideTheme"
  >
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
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { darkTheme, zhCN, dateZhCN } from 'naive-ui'
import { useAppConfigStore } from '@/store/modules/app-config'
import { ThemeMode } from '@/store/types'
import { useThemeTokens } from '@/theme/useThemeTokens'

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
const themeVars = useThemeTokens()
</script>

<style scoped>
.app-shell-root {
  min-height: 100vh;
  background-color: var(--shell-page-bg);
  color: var(--shell-text-color);
  transition: background-color 0.3s ease, color 0.3s ease;
}

:global(body) {
  background-color: var(--shell-page-bg);
  color: var(--shell-text-color);
}
</style>
