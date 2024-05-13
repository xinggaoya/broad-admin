<template>
  <n-upload
    :action="action"
    :headers="headers"
    :data="props.data"
    :default-upload="props.defaultUpload"
    @finish="handleFinish"
  >
    <slot></slot>
  </n-upload>
</template>

<script lang="ts" setup>
  import { defineProps, withDefaults, computed, defineEmits } from 'vue'
  import { useUserStore } from '@/store/modules/user'
  import { UploadFileInfo } from 'naive-ui'

  const REQUEST_HEADER = import.meta.env.VITE_BASE_REQUEST_HEADER as string
  const defaultUrl = import.meta.env.VITE_BASE_AXIOS_URL as string

  const emit = defineEmits<{
    (e: 'success', url: string): void
  }>()

  const props = withDefaults(
    defineProps<{
      data: any
      defaultUpload: boolean
    }>(),
    {
      data: {},
      defaultUpload: true,
    }
  )
  const headers = computed(() => {
    return {
      [REQUEST_HEADER]: useUserStore().token,
    }
  })
  const action = computed(() => {
    return defaultUrl + '/file/upload'
  })

  const handleFinish = ({ file, event }: { file: UploadFileInfo; event?: ProgressEvent }) => {
    const { response } = event?.currentTarget as XMLHttpRequest
    if (response) {
      const { code, data } = JSON.parse(response)
      if (code === 200) {
        emit('success', data)
      }
    }
  }
</script>

<style scoped></style>
