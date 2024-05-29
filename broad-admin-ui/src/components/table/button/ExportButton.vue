<template>
  <n-button @click="onConfirm" :loading="loading">
    <template #icon>
      <n-icon>
        <LogInOutline />
      </n-icon>
    </template>
    {{ title }}
  </n-button>
</template>

<script lang="ts" setup>
import { withDefaults } from 'vue'
import { LogInOutline } from '@vicons/ionicons5'

withDefaults(
  defineProps<{
    title: string
    loading: boolean
  }>(),
  {
    title: '导出',
    loading: false
  }
)
const emits = defineEmits(['confirm'])

const onConfirm = () => {
  emits('confirm')
}
</script>

<style scoped></style>
