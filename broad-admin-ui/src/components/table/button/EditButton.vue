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
        <EditIcon />
      </n-icon>
    </template>
    {{ title }}
  </n-button>
</template>

<script lang="ts" setup>
import { CreateOutline as EditIcon } from '@vicons/ionicons5'

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
  title: '编辑',
  disabled: false,
  loading: false,
  type: 'warning',
  size: 'small',
  ghost: true,
  round: false,
  circle: false,
  quaternary: false,
  dashed: false
})

const emit = defineEmits<{
  (e: 'click', event: MouseEvent): void
  (e: 'edit'): void
}>()

function handleClick(event: MouseEvent) {
  if (!props.disabled && !props.loading) {
    emit('click', event)
    emit('edit')
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
