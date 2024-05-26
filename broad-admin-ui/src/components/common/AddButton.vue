<template>
  <n-button type="primary" @click="onAdd" secondary>
    <template #icon>
      <n-icon>
        <AddIcon />
      </n-icon>
    </template>
    {{ title }}
  </n-button>
</template>

<script lang="ts">
  import { defineComponent } from 'vue'
  import { Add as AddIcon } from '@vicons/ionicons5'

  export default defineComponent({
    name: 'AddButton',
    components: {
      AddIcon,
    },
    props: {
      title: {
        type: String,
        default: '新增',
      },
    },
    emits: ['add'],
    setup(props, { emit }) {
      function onAdd() {
        emit('add')
      }

      return {
        onAdd,
      }
    },
  })
</script>
