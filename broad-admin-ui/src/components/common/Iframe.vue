<template>
  <n-spin :show="loading" description="努力加载中...">
    <n-scrollbar :style="'max-height:' + height">
      <div :style="'height:' + height">
        <iframe
          :src="prop.src"
          frameborder="no"
          style="width: 100%; height: 100%"
          scrolling="auto"
        ></iframe>
      </div>
    </n-scrollbar>
  </n-spin>
</template>

<script lang="ts" setup>
  import { ref, onMounted } from 'vue'

  const prop = defineProps<{
    src: string
  }>()

  const loading = ref(true)
  const height = ref(document.documentElement.clientHeight - 94.5 + 'px;')

  onMounted(() => {
    setTimeout(() => {
      loading.value = false
    }, 300)
    window.onresize = function temp() {
      height.value = document.documentElement.clientHeight - 94.5 + 'px;'
    }
  })
</script>
