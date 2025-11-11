<template>
  <section class="druid-page">
    <header class="page-header">
      <div>
        <p class="header-subtitle">数据库监控</p>
        <h2>Druid 控制台</h2>
      </div>
      <n-tag type="info" round>iframe 嵌入</n-tag>
    </header>
    <n-card class="iframe-card" :bordered="false">
      <Iframe :src="url" />
    </n-card>
  </section>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import Iframe from '@/components/common/Iframe.vue'

const url = ref(import.meta.env.VITE_BASE_AXIOS_URL + '/druid/login.html')
</script>

<style scoped lang="scss">
.druid-page {
  display: flex;
  flex-direction: column;
  gap: var(--shell-gap);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--shell-surface);
  border-radius: var(--shell-radius-lg);
  padding: 20px;
  box-shadow: var(--shell-shadow);
}

.page-header h2 {
  margin: 0;
}

.header-subtitle {
  margin: 0;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--shell-muted-text-color);
}

.iframe-card {
  border-radius: var(--shell-radius-lg);
  background: var(--shell-surface);
  box-shadow: var(--shell-shadow);

  :deep(iframe) {
    min-height: 70vh;
    border: none;
    width: 100%;
  }
}
</style>
