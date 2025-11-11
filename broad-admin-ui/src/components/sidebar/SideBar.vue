<template>
  <n-card
    class="vaw-side-bar-wrapper"
    :bordered="false"
    :content-style="{ padding: 0, height: '100%' }"
    :class="sidebarClasses"
  >
    <transition name="logo">
      <LogoView v-if="showLogo" />
    </transition>
    <ScrollerMenu :routes="permissionStore.getPermissionSideBar" />
    <div class="mobile-shadow"></div>
  </n-card>
</template>

<script lang="ts" setup>
import { computed } from 'vue'
import { useAppConfigStore } from '@/store/modules/app-config'
import { usePermissionStore } from '@/store/modules/permission'
import LogoView from '../logo/LogoView.vue'
import ScrollerMenu from './components/ScrollerMenu.vue'

defineProps({
  showLogo: {
    type: Boolean,
    default: true
  }
})

const appConfig = useAppConfigStore()
const permissionStore = usePermissionStore()

const sidebarClasses = computed(() => [
  !appConfig.isCollapse ? 'open-status' : 'close-status',
  `sidebar-theme--${appConfig.sideTheme}`
])
</script>

<style scoped lang="scss">
.sidebar-theme--dark,
.sidebar-theme--white,
.sidebar-theme--image {
  background: var(--sidebar-surface);
  color: var(--sidebar-text-color);
  border: 1px solid var(--sidebar-border-color);
  box-shadow: var(--sidebar-shadow);
}

.open-status {
  box-shadow: var(--sidebar-shadow, var(--shell-shadow));
  transition: box-shadow $transitionTime;
}

.close-status {
  box-shadow: none;
  transition: box-shadow $transitionTime;
}

.vaw-side-bar-wrapper {
  height: 100%;
  box-sizing: border-box;
  overflow: hidden;
  border-radius: var(--shell-radius-lg);
  background-color: transparent;
  display: flex;
  flex-direction: column;
  position: relative;
  isolation: isolate;

  :deep(.n-card__content) {
    height: 100%;
    display: flex;
    flex-direction: column;
    position: relative;
    z-index: 1;
  }
}

.sidebar-theme--image::before {
  content: '';
  position: absolute;
  inset: 0;
  background-image: var(--sidebar-surface-pattern);
  background-size: cover;
  background-position: center;
  opacity: 0.35;
  z-index: 0;
}

.is-mobile {
  .vaw-side-bar-wrapper {
    max-width: min(var(--shell-sidebar-width), 80vw);
  }
}
</style>
