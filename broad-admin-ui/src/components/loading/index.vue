<template>
  <div class="loading-wrapper">
    <n-spin :stroke="props.themeColor" :description="props.description" />
  </div>
</template>

<script lang="ts" setup>
import { withDefaults, defineProps } from 'vue'
import setting from '@/setting'

defineOptions({
  name: 'SysLoading'
})
const props = withDefaults(
  defineProps<{
    themeColor: string
    description: string
  }>(),
  {
    themeColor: setting.themeColor,
    description: '拼命加载中...'
  }
)
</script>

<style lang="scss" scoped>
.loading-wrapper {
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  width: 100%;
  overflow: hidden;
  background: rgba($color: #ffffff, $alpha: 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
