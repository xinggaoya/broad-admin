<template>
  <div class="icon-view" :style="{ fontSize: `${prop.size}px` }">
    <template v-if="prop.type === 'ionicons'">
      <n-icon :size="prop.size" :color="prop.color" :depth="prop.depth">
        <component :is="iconComponent" />
      </n-icon>
    </template>
    <template v-else>
      <SvgIcon
        prefix="iconfont"
        :name="prop.icon"
        :style="{
          fontSize: `${prop.size}px`,
          color: prop.color
        }"
      />
    </template>
    <n-tooltip v-if="prop.showTooltip" placement="bottom" trigger="hover">
      <template #trigger>
        <div class="icon-name" v-if="prop.showName">{{ prop.icon }}</div>
      </template>
      {{ prop.icon }}
    </n-tooltip>
  </div>
</template>

<script lang="ts" setup>
import * as AllIcons from '@vicons/ionicons5'
import { computed } from 'vue'
import SvgIcon from '../svg-icon/SvgIcon.vue'

// 定义详细的props类型
interface IconViewProps {
  icon: string
  type?: 'ionicons' | 'iconfont'
  size?: number
  color?: string
  depth?: 1 | 2 | 3
  showName?: boolean
  showTooltip?: boolean
}

const prop = withDefaults(defineProps<IconViewProps>(), {
  type: 'ionicons',
  size: 20,
  color: undefined,
  depth: undefined,
  showName: false,
  showTooltip: false
})

// 使用计算属性获取图标组件
const iconComponent = computed(() => {
  if (prop.type === 'ionicons') {
    const component = AllIcons[prop.icon as keyof typeof AllIcons]
    if (!component) {
      console.warn(`Icon ${prop.icon} not found in @vicons/ionicons5`)
    }
    return component
  }
  return null
})

// 定义事件
defineEmits<{
  (e: 'click', icon: string): void
}>()
</script>

<style lang="scss" scoped>
.icon-view {
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: transform 0.2s ease-in-out;

  &:hover {
    transform: scale(1.1);
  }

  .icon-name {
    margin-top: 4px;
    font-size: 12px;
    color: var(--text-color-2);
    text-align: center;
  }
}
</style>
