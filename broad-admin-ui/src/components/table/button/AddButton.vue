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
        <AddIcon />
      </n-icon>
    </template>
    {{ title }}
  </n-button>
</template>

<script lang="ts" setup>
import { Add as AddIcon } from '@vicons/ionicons5'

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
}

const props = withDefaults(defineProps<Props>(), {
  title: '新增',
  disabled: false,
  loading: false,
  type: 'primary',
  size: 'small',
  ghost: true,
  round: false,
  circle: false,
  quaternary: false,
  dashed: false
})

const emit = defineEmits<{
  (e: 'click', event: MouseEvent): void
  (e: 'add'): void
}>()

function handleClick(event: MouseEvent) {
  if (!props.disabled && !props.loading) {
    emit('click', event)
    emit('add')
  }
}
</script>

<style lang="scss" scoped>
.n-button {
  display: inline-flex;
  align-items: center;

  .n-icon {
    margin-right: 4px;
  }
}
</style>
