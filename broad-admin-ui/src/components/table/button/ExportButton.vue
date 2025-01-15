<template>
  <n-button
    @click="handleClick"
    :disabled="disabled"
    :loading="loading"
    :type="type"
    :size="size"
    :ghost="ghost"
    :round="round"
    :circle="circle"
    :quaternary="quaternary"
    :dashed="dashed"
  >
    <template #icon>
      <n-icon>
        <ExportIcon />
      </n-icon>
    </template>
    {{ title }}
  </n-button>
</template>

<script lang="ts" setup>
import { LogOutOutline as ExportIcon } from '@vicons/ionicons5'

interface Props {
  title?: string
  disabled?: boolean
  loading?: boolean
  type?: 'default' | 'tertiary' | 'primary' | 'info' | 'success' | 'warning' | 'error'
  size?: 'tiny' | 'small' | 'medium' | 'large'
  ghost?: boolean
  round?: boolean
  circle?: boolean
  quaternary?: boolean
  dashed?: boolean
  filename?: string
  fileType?: 'csv' | 'xlsx' | 'pdf'
}

const props = withDefaults(defineProps<Props>(), {
  title: '导出',
  disabled: false,
  loading: false,
  type: 'default',
  size: 'small',
  ghost: true,
  round: false,
  circle: false,
  quaternary: false,
  dashed: false,
  filename: 'export',
  fileType: 'xlsx'
})

const emit = defineEmits<{
  (e: 'click', event: MouseEvent): void
  (e: 'success', filename: string): void
  (e: 'error', error: Error): void
}>()

function handleClick(event: MouseEvent) {
  if (!props.disabled && !props.loading) {
    emit('click', event)
  }
}
</script>

<style lang="scss" scoped>
.export-button {
  display: inline-flex;
  align-items: center;

  .n-icon {
    margin-right: 4px;
  }
}
</style>
