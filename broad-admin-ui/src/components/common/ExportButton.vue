<template>
  <n-button class="ml-4" size="small" @click="onConfirm">{{ title }}</n-button>
</template>

<script lang="ts" setup>
  import { withDefaults } from 'vue'

  const props = withDefaults(
    defineProps<{
      title: string
    }>(),
    {
      title: '导出',
    }
  )
  const emits = defineEmits(['confirm'])

  const onConfirm = () => {
    emits('confirm')
  }
</script>

<style scoped></style>
