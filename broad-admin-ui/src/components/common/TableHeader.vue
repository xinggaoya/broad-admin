<template>
  <div id="tableHeaderContainer" class="relative">
    <n-card
      :content-style="{ padding: '0px' }"
      :bordered="false"
      header-style="font-size: 16px; padding: 5px 5px; border-radius: 0"
    >
      <template #header>
        <n-space>
          <slot name="top-right"></slot>
        </n-space>
      </template>
      <template #header-extra>
        <n-space>
          <slot name="table-config"></slot>
        </n-space>
      </template>
    </n-card>
  </div>
</template>

<script lang="ts">
import { defineComponent, nextTick, onMounted, ref } from 'vue'

export default defineComponent({
  name: 'TableHeader',
  props: {
    modelValue: {
      type: Boolean,
      default: false
    },
    showFilter: {
      type: Boolean,
      default: true
    }
  },
  emits: ['search', 'reset-search'],
  setup(props, { emit }) {
    const target = ref<HTMLElement | null>(null)
    onMounted(() => {
      nextTick(() => {
        target.value = document.getElementById('tableHeaderContainer')
      })
    })

    function doSearch() {
      doResetSearch()
      emit('search')
    }

    function doResetSearch() {
      emit('reset-search')
    }

    return {
      target
    }
  }
})
</script>
