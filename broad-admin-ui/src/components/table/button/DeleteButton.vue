<template>
  <n-popconfirm
    :show="showConfirm"
    :disabled="disabled"
    :positive-text="confirmText"
    :negative-text="cancelText"
    @positive-click="handleConfirm"
    @negative-click="handleCancel"
    @update:show="handleUpdateShow"
  >
    <template #trigger>
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
            <TrashIcon />
          </n-icon>
        </template>
        {{ title }}
      </n-button>
    </template>
    {{ confirmContent }}
  </n-popconfirm>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { Trash as TrashIcon } from '@vicons/ionicons5'

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
  confirmContent?: string
  confirmText?: string
  cancelText?: string
  needConfirm?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  title: '删除',
  disabled: false,
  loading: false,
  type: 'error',
  size: 'small',
  ghost: true,
  round: false,
  circle: false,
  quaternary: false,
  dashed: false,
  confirmContent: '确定要删除吗？',
  confirmText: '确定',
  cancelText: '取消',
  needConfirm: true
})

const emit = defineEmits<{
  (e: 'click', event: MouseEvent): void
  (e: 'delete'): void
  (e: 'confirm'): void
  (e: 'cancel'): void
}>()

const showConfirm = ref(false)

function handleClick(event: MouseEvent) {
  if (!props.disabled && !props.loading) {
    if (props.needConfirm) {
      showConfirm.value = true
    } else {
      emit('click', event)
    }
  }
}

function handleConfirm() {
  emit('confirm')
  emit('delete')
  showConfirm.value = false
}

function handleCancel() {
  emit('cancel')
  showConfirm.value = false
}

function handleUpdateShow(show: boolean) {
  showConfirm.value = show
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
