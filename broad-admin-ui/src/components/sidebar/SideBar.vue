<template>
  <n-card
    class="vaw-side-bar-wrapper"
    :bordered="false"
    :style="{ borderRadius: '0px', marginTop: appConfig.layoutMode === 'ttb' ? '48px' : 0 }"
    :content-style="{ padding: 0 }"
    :class="[
      !appConfig.isCollapse ? 'open-status' : 'close-status',
      appConfig.sideTheme === 'image' ? 'sidebar-bg-img' : ''
    ]"
  >
    <transition name="logo">
      <LogoView v-if="showLogo" />
    </transition>
    <ScrollerMenu :routes="permissionStore.getPermissionSideBar" />
    <div class="mobile-shadow"></div>
  </n-card>
</template>

<script lang="ts" setup>
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
</script>

<style scoped lang="scss">
.sidebar-bg-img {
  background-image: url('../../assets/bg_img.webp') !important;
  background-size: cover;
}

.open-status {
  width: $menuWidth;
  box-shadow: 2px 5px 10px rgba(0, 0, 0, 0.12);
  transition: all $transitionTime;
}

.close-status {
  width: $minMenuWidth;
  box-shadow: none;
  transition: all $transitionTime;
}

.vaw-side-bar-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  overflow-x: hidden;
  height: 100%;
  box-sizing: border-box;
  z-index: 999;

  .vaw-menu-wrapper {
    overflow-x: hidden;
    color: white;
  }
}

.is-mobile {
  .open-status {
    width: $menuWidth;
    transform: translateX(0);
    transition: transform $transitionTime;
  }

  .close-status {
    width: $menuWidth;
    $negativeMenuWidth: calc(#{$menuWidth} * -1);
    transform: translateX($negativeMenuWidth);
    transition: transform $transitionTime;
    box-shadow: none;
  }
}
</style>
