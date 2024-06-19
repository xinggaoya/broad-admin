<template>
  <n-button @click="onDelete">
    <template #icon>
      <n-icon>
        <TrashIcon />
      </n-icon>
    </template>
    删除
  </n-button>
</template>

<script lang="ts" setup>
import { Trash as TrashIcon } from '@vicons/ionicons5'

const emit = defineEmits(['delete'])


function onDelete() {
  emit('delete')
}

</script>
