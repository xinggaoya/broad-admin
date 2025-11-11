<template>
  <div
    ref="shellRef"
    class="app-shell"
    :class="shellClass"
    role="application"
    :aria-busy="false"
    data-testid="app-shell"
    :data-device="appConfig.deviceType"
    :data-collapsed="String(appConfig.isCollapse)"
  >
    <aside
      class="app-shell__sidebar"
      :class="[{ 'app-shell__sidebar--floating': isMobile, 'app-shell__sidebar--collapsed': appConfig.isCollapse }]"
      aria-label="侧边栏菜单"
      :aria-hidden="isMobile && appConfig.isCollapse"
      :aria-expanded="(!appConfig.isCollapse).toString()"
      id="shell-sidebar"
      data-testid="app-shell-sidebar"
    >
      <div class="app-shell__sidebar-inner">
        <SideBar :show-logo="!isMobile" />
      </div>
    </aside>

    <section class="app-shell__main">
      <MainLayout />
    </section>

    <transition name="shell-fade">
      <div
        v-if="showOverlay"
        class="app-shell__overlay"
        role="button"
        tabindex="0"
        aria-label="关闭侧边栏"
        aria-controls="shell-sidebar"
        data-testid="app-shell-overlay"
        @click="closeMenu"
        @keydown.enter="closeMenu"
        @keydown.space.prevent="closeMenu"
      ></div>
    </transition>
  </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useResizeObserver } from '@vueuse/core'
import { useAppConfigStore } from '@/store/modules/app-config'
import { DeviceType, ThemeMode } from '@/store/types'
import SideBar from '@/components/sidebar/SideBar.vue'
import MainLayout from '@/components/MainLayout.vue'

const shellRef = ref<HTMLElement | null>(null)
const appConfig = useAppConfigStore()
const isMobile = computed(() => appConfig.deviceType === DeviceType.MOBILE)
const showOverlay = computed(() => isMobile.value && !appConfig.isCollapse)
const isDark = computed(() => appConfig.theme === ThemeMode.DARK)

const shellClass = computed(() => ({
  'app-shell--collapsed': appConfig.isCollapse,
  'app-shell--dark': isDark.value,
  'app-shell--mobile': isMobile.value
}))

const closeMenu = () => {
  appConfig.toggleCollapse(true)
}

const syncDeviceByWidth = (width: number) => {
  if (width <= 768) {
    appConfig.changeDevice(DeviceType.MOBILE)
    appConfig.toggleCollapse(true)
  } else if (width < 992) {
    appConfig.changeDevice(DeviceType.PAD)
    appConfig.toggleCollapse(true)
  } else {
    appConfig.changeDevice(DeviceType.PC)
    appConfig.toggleCollapse(false)
  }
}

useResizeObserver(shellRef, (entries) => {
  const entry = entries[0]
  if (!entry) return
  syncDeviceByWidth(entry.contentRect.width)
})
</script>

<style scoped lang="scss">
.app-shell {
  position: relative;
  display: grid;
  grid-template-columns: var(--shell-sidebar-width) minmax(0, 1fr);
  grid-template-rows: minmax(0, 1fr);
  min-height: 100vh;
  background-color: var(--shell-page-bg);
  transition:
    grid-template-columns 0.3s ease,
    background-color 0.3s ease;
}

.app-shell--collapsed {
  grid-template-columns: var(--shell-sidebar-collapsed-width) minmax(0, 1fr);
}

.app-shell__sidebar {
  position: relative;
  padding: var(--shell-content-padding);
  transition: transform 0.3s ease;
}

.app-shell__sidebar-inner {
  position: sticky;
  top: var(--shell-content-padding);
  height: calc(100vh - (var(--shell-content-padding) * 2));
}

.app-shell__sidebar--collapsed .app-shell__sidebar-inner {
  max-width: var(--shell-sidebar-collapsed-width);
}

.app-shell__sidebar--floating {
  position: fixed;
  inset: 0 auto 0 0;
  width: min(var(--shell-sidebar-width), 80vw);
  padding: 24px 16px;
  z-index: 2000;
  background: transparent;
  transform: translateX(-100%);
}

.app-shell--mobile:not(.app-shell--collapsed) .app-shell__sidebar--floating {
  transform: translateX(0);
}

.app-shell__main {
  grid-column: 2 / -1;
  padding: var(--shell-content-padding);
}

.app-shell__overlay {
  position: fixed;
  inset: 0;
  background: var(--shell-overlay-color);
  backdrop-filter: blur(4px);
  z-index: 1500;
}

.shell-fade-enter-active,
.shell-fade-leave-active {
  transition: opacity 0.2s ease;
}

.shell-fade-enter-from,
.shell-fade-leave-to {
  opacity: 0;
}

@media (max-width: 1024px) {
  .app-shell {
    grid-template-columns: minmax(0, 1fr);
  }

  .app-shell__main {
    grid-column: 1 / -1;
  }

  .app-shell__sidebar {
    display: none;
  }

  .app-shell--mobile .app-shell__sidebar {
    display: block;
  }
}
</style>
