<template>
<button
  :class="[appConfig.isCollapse ? 'fold-close-status' : 'fold-open-status']"
  class="fold-wrapper iconfont"
  type="button"
  aria-label="切换侧边栏"
  aria-controls="shell-sidebar"
  :aria-expanded="(!appConfig.isCollapse).toString()"
  data-testid="shell-toggle-sidebar"
  @click="toggleFold"
>
  <SvgIcon name="expand" />
</button>
</template>

<script lang="ts" setup>
import { useAppConfigStore } from '@/store/modules/app-config'

const appConfig = useAppConfigStore()

function toggleFold() {
  appConfig.toggleCollapse(!appConfig.isCollapse)
}
</script>

<style lang="scss" scoped>
.fold-open-status {
  transform: rotate(180deg);
}

.fold-close-status {
  transform: rotate(0);
}

.fold-wrapper {
  box-sizing: border-box;
  display: inline-block;
  line-height: $logoHeight;
  text-align: center;
  padding: 0 10px;
  font-size: 22px;
  transition: transform $transitionTime;
  border: none;
  background: transparent;
  color: inherit;
}

.fold-wrapper:hover {
  color: #999999;
  cursor: pointer;
}
</style>
