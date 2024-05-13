<template>
  <n-modal
    v-model:show="showModal"
    preset="card"
    :title="title"
    class="modal-dialog-wrapper"
    header-style="padding: 10px 30px;font-size: 18px;"
    content-style="padding: 20px;"
    :bordered="false"
    :style="bodyStyle"
    :mask-closable="maskClosable"
    :segmented="segmented"
    display-directive="if"
  >
    <template v-slot:default>
      <slot name="content"></slot>
    </template>
    <template v-slot:footer>
      <n-space justify="end">
        <n-button type="default" @click="onCancel">取消</n-button>
        <n-button type="primary" :loading="submitLoading" @click="onConfirm">确定</n-button>
      </n-space>
    </template>
  </n-modal>
</template>

<script lang="ts" setup>
import { computed, nextTick, ref, watchEffect } from 'vue'
import { drag, unDrag } from '@/hooks/useDialogDragger'
import useAppConfigStore from '@/store/modules/app-config'
import { DeviceType } from '@/store/types'

defineProps({
  title: {
    type: String,
    default: '操作'
  },
  contentHeight: {
    type: String,
    default: '30vh'
  },
  submitLoading: {
    type: Boolean,
    default: false
  },
  maskClosable: {
    type: Boolean,
    default: true
  }
})
const emit = defineEmits(['confirm', 'cancel', 'update:modelValue'])

const showModal = defineModel('modelValue')

const appConfig = useAppConfigStore()
const header = ref<HTMLElement | null>()
const bodyStyle = computed(() => ({
  width: appConfig.deviceType === DeviceType.MOBILE ? '80%' : '640px',
  maxWidth: 'calc(100vw - 32px)',
  borderRadius: '6px'
}))

const segmented = {
  content: 'soft',
  footer: 'soft'
}

function onConfirm() {
  emit('confirm')
}

function onCancel() {
  showModal.value = false
  emit('cancel')
}

watchEffect(() => {
  if (showModal.value) {
    nextTick(() => {
      if (!header.value) {
        header.value = document.querySelector('.n-modal-container .n-card-header') as HTMLElement
      }
      drag(header.value)
    })
  } else {
    nextTick(() => {
      if (header.value) {
        unDrag(header.value as HTMLElement)
      }
    })
  }
})
</script>
