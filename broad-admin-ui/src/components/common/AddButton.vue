<template>
  <n-button type="primary" @click="onAdd">
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

defineProps({
  title: {
    type: String,
    default: '新增'
  }
})

const emit = defineEmits(['add'])

function onAdd() {
  emit('add')
}

</script>
