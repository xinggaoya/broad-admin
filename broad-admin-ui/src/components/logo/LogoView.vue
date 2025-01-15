<template>
  <div class="logo-wrapper">
    <img v-if="showLogo" class="logo-img" :src="Logo" />
    <div
      v-if="showTitle"
      :class="[!appConfig.isCollapse || alwaysShow ? 'show-title' : 'close-title']"
    >
      <span class="logo-title">{{ projectName }}</span>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useAppConfigStore } from '@/store/modules/app-config'
import Logo from '@/assets/logo.png'
import { projectName } from '@/setting'

// 定义props
defineProps({
  showTitle: {
    type: Boolean,
    default: true
  },
  showLogo: {
    type: Boolean,
    default: true
  },
  alwaysShow: {
    type: Boolean,
    default: false
  }
})

// 获取store实例
const appConfig = useAppConfigStore()
</script>

<style lang="scss" scoped>
.logo-wrapper {
  height: $logoHeight;
  display: flex;
  justify-content: center;
  align-items: center;
  border-bottom: 1px dashed var(--border-color);

  .logo-img {
    width: 30px;
  }

  .logo-title {
    font-weight: bold;
    font-size: 1rem;
    padding: 0 10px;
  }

  .show-title {
    transform: scale(1);
    width: auto;
    transition: transform 0.2s ease-in;
  }

  .close-title {
    transform: scale(0);
    width: 0;
  }
}
</style>
