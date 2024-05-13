<template>
  <div style="height: 100%">
    <n-global-style />
    <n-loading-bar-provider>
      <n-dialog-provider>
        <n-el
          class="vaw-layout-container"
          :class="[appConfig.deviceType === 'mobile' && 'is-mobile']"
        >
          <template v-if="layoutMode === 'ttb'">
            <VAWHeader />
            <MainLayout :show-nav-bar="false" />
          </template>
          <template v-else-if="layoutMode === 'lcr'">
            <TabSplitSideBar />
            <MainLayout />
          </template>
          <template v-else>
            <SideBar />
            <MainLayout />
          </template>
          <div
            v-if="appConfig.deviceType === 'mobile'"
            class="mobile-shadow"
            :class="[appConfig.isCollapse ? 'close-shadow' : 'show-shadow']"
            @click="closeMenu"
          ></div>
        </n-el>
      </n-dialog-provider>
    </n-loading-bar-provider>
  </div>
</template>

<script lang="ts" setup>
  import useAppConfigStore from '@/store/modules/app-config'
  import { computed, onBeforeUnmount, onMounted } from 'vue'
  import { DeviceType } from '@/store/types'

  const appConfig = useAppConfigStore()
  const layoutMode = computed(() => {
    return appConfig.getLayoutMode
  })
  onMounted(() => {
    handleScreenResize()
    window.addEventListener('resize', handleScreenResize)
  })
  onBeforeUnmount(() => {
    window.removeEventListener('resize', handleScreenResize)
  })

  function handleScreenResize() {
    const width = document.body.clientWidth
    if (width <= 768) {
      appConfig.changeDevice(DeviceType.MOBILE)
      appConfig.toggleCollapse(true)
    } else if (width < 992 && width > 768) {
      appConfig.changeDevice(DeviceType.PAD)
      appConfig.toggleCollapse(true)
    } else if (width < 1200 && width >= 992) {
      appConfig.changeDevice(DeviceType.PC)
      appConfig.toggleCollapse(false)
    } else {
      appConfig.changeDevice(DeviceType.PC)
      appConfig.toggleCollapse(false)
    }
  }

  function closeMenu() {
    appConfig.toggleCollapse(true)
  }
</script>

<style scoped lang="scss">
  .vaw-layout-container {
    height: 100%;
    max-width: 100%;
    overflow-x: hidden;

    .mobile-shadow {
      display: none;
    }

    .layout-mode-ttb {
      margin-top: $logoHeight;
      transition: all $transitionTime;
    }
  }

  .is-mobile {
    .mobile-shadow {
      background-color: #000000;
      position: fixed;
      top: 0;
      left: 0;
      width: 100vw;
      height: 100vh;
      z-index: 997;
    }

    .close-shadow {
      display: none;
    }

    .show-shadow {
      display: block;
      opacity: 0.5;
      transition: all $transitionTime;
    }
  }
</style>
