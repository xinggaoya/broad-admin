<template>
  <n-modal
    v-model:show="showModal"
    preset="card"
    :title="title"
    class="modal-dialog-wrapper"
    :header-style="headerStyle"
    :content-style="contentStyle"
    :style="bodyStyle"
    :mask-closable="maskClosable"
    :close-on-esc="closeOnEsc"
    :segmented="segmented"
    :bordered="false"
    :loading="loading"
    display-directive="if"
    @after-leave="handleAfterLeave"
    @close="handleClose"
    @esc="handleEsc"
  >
    <!-- 自定义头部插槽 -->
    <template v-if="$slots.header" #header>
      <slot name="header"></slot>
    </template>

    <!-- 内容插槽 -->
    <div :style="{ minHeight: contentHeight }">
      <slot name="content"></slot>
    </div>

    <!-- 自定义底部插槽 -->
    <template v-if="$slots.footer" #footer>
      <slot name="footer"></slot>
    </template>

    <!-- 默认底部按钮 -->
    <template v-else #footer>
      <n-space justify="end">
        <n-button v-if="showCancel" :disabled="loading" @click="handleCancel">
          {{ cancelText }}
        </n-button>
        <n-button
          v-if="showConfirm"
          type="primary"
          :loading="submitLoading || loading"
          @click="handleConfirm"
        >
          {{ confirmText }}
        </n-button>
      </n-space>
    </template>
  </n-modal>
</template>

<script lang="ts" setup name="ModalDialog">
import { computed, nextTick, ref, watchEffect } from 'vue'
import { drag, unDrag } from '@/hooks/useDialogDragger'
import { useAppConfigStore } from '@/store/modules/app-config'
import { DeviceType } from '@/store/types'

interface Props {
  /** 标题 */
  title?: string
  /** 内容区域最小高度 */
  contentHeight?: string
  /** 确认按钮loading */
  submitLoading?: boolean
  /** 整体loading */
  loading?: boolean
  /** 点击遮罩是否可关闭 */
  maskClosable?: boolean
  /** 是否可以通过 ESC 关闭 */
  closeOnEsc?: boolean
  /** 是否显示取消按钮 */
  showCancel?: boolean
  /** 是否显示确认按钮 */
  showConfirm?: boolean
  /** 取消按钮文本 */
  cancelText?: string
  /** 确认按钮文本 */
  confirmText?: string
  /** 自定义头部样式 */
  customHeaderStyle?: object
  /** 自定义内容样式 */
  customContentStyle?: object
}

const props = withDefaults(defineProps<Props>(), {
  title: '操作',
  contentHeight: '30vh',
  submitLoading: false,
  loading: false,
  maskClosable: true,
  closeOnEsc: true,
  showCancel: true,
  showConfirm: true,
  cancelText: '取消',
  confirmText: '确定',
  customHeaderStyle: () => ({}),
  customContentStyle: () => ({})
})

const emit = defineEmits<{
  confirm: []
  cancel: []
  close: []
  esc: []
  afterLeave: []
  'update:modelValue': [value: boolean]
}>()

// v-model 绑定
const showModal = defineModel<boolean>('modelValue')

const appConfig = useAppConfigStore()
const header = ref<HTMLElement | null>()

// 计算属性：响应式样式
const bodyStyle = computed(() => ({
  width: appConfig.deviceType === DeviceType.MOBILE ? '90%' : '640px',
  maxWidth: 'calc(100vw - 32px)'
}))

const headerStyle = computed(() => ({
  padding: '14px 20px',
  fontSize: '16px',
  fontWeight: 500,
  ...props.customHeaderStyle
}))

const contentStyle = computed(() => ({
  padding: '20px',
  ...props.customContentStyle
}))

const segmented = {
  content: 'soft',
  footer: 'soft'
}

// 事件处理
function handleConfirm() {
  emit('confirm')
}

function handleCancel() {
  showModal.value = false
  emit('cancel')
}

function handleClose() {
  emit('close')
}

function handleEsc() {
  emit('esc')
}

function handleAfterLeave() {
  emit('afterLeave')
}

// 拖拽功能
watchEffect(() => {
  if (showModal.value) {
    nextTick(() => {
      if (!header.value) {
        header.value = document.querySelector('.n-modal-container .n-card-header') as HTMLElement
      }
      if (header.value) {
        drag(header.value)
      }
    })
  } else {
    nextTick(() => {
      if (header.value) {
        unDrag(header.value)
      }
    })
  }
})
</script>

<style lang="scss" scoped>
.modal-dialog-wrapper {
  :deep(.n-card-header) {
    cursor: move;
  }

  :deep(.n-modal-body-wrapper) {
    max-height: 90vh;
    overflow: auto;
  }
}
</style>
