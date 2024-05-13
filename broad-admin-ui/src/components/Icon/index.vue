<template>
  <n-icon :size="prop.size">
    <component :is="item" />
  </n-icon>
</template>

<script lang="ts" setup>
  import * as AllIcons from '@vicons/ionicons5'
  import { computed, defineProps } from 'vue'

  const prop = withDefaults(
    defineProps<{
      icon: string
      size?: number
    }>(),
    {
      size: 20,
    }
  )

  const item = computed(() => AllIcons[prop.icon])
</script>

<style scoped></style>
