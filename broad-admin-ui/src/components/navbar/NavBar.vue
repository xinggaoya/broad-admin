<template>
  <div class="vaw-nav-bar-wrapper">
    <HumBurger />
    <Breadcrumb v-if="appConfig.deviceType !== 'mobile'" />
    <div style="flex: 1"></div>
    <div v-if="appConfig.deviceType !== 'mobile'" class="right-wrapper">
      <ActionItems />
    </div>
    <div class="avatar-wrapper">
      <VAWAvatar />
    </div>
  </div>
</template>

<script lang="ts" setup>
import useAppConfigStore from '@/store/modules/app-config'
import HumBurger from '@/components/humburger/HumBurger.vue'
import Breadcrumb from '@/components/breadcrumb/BreadCrumb.vue'

const appConfig = useAppConfigStore()

</script>

<style lang="scss" scoped>
.vaw-nav-bar-wrapper {
  height: $logoHeight;
  max-height: $logoHeight;
  min-height: $logoHeight;
  overflow: hidden;
  box-sizing: border-box;
  display: flex;
  align-items: center;
  border-bottom: 1px solid var(--border-color);

  .avatar-wrapper {
    padding-right: 15px;
  }

  .right-wrapper {
    height: 100%;
  }
}
</style>
