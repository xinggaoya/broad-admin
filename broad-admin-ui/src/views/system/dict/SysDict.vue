<template>
  <section class="dict-page">
    <header class="page-header">
      <div>
        <p class="header-subtitle">系统管理</p>
        <h2>字典管理</h2>
      </div>
    </header>
    <n-card class="tabs-panel" :bordered="false">
      <n-tabs type="segment" size="large" :value="activeTab" @update:value="handleTabChange">
        <n-tab-pane name="type" tab="字典类型">
          <DictType v-if="activeTab === 'type'" @select-dict="handleSelectDict" />
        </n-tab-pane>
        <n-tab-pane name="data" tab="字典数据">
          <DictData v-if="activeTab === 'data'" :dict-type="selectedDictType" @back="handleBack" />
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </section>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import DictType from './dictType.vue'
import DictData from './dictData.vue'

// 状态管理
const activeTab = ref<'type' | 'data'>('type')
const selectedDictType = ref('')

// 事件处理
const handleTabChange = (value: 'type' | 'data') => {
  activeTab.value = value
}

const handleSelectDict = (dictType: string) => {
  selectedDictType.value = dictType
  activeTab.value = 'data'
}

const handleBack = () => {
  activeTab.value = 'type'
  selectedDictType.value = ''
}
</script>

<style scoped lang="scss">
.dict-page {
  display: flex;
  flex-direction: column;
  gap: var(--shell-gap);
}

.page-header {
  background: var(--shell-surface);
  border-radius: var(--shell-radius-lg);
  padding: 20px;
  box-shadow: var(--shell-shadow);
}

.tabs-panel {
  border-radius: var(--shell-radius-lg);
  box-shadow: var(--shell-shadow);
}

:deep(.n-tab-pane) {
  padding-top: 16px;
}

.header-subtitle {
  margin: 0;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--shell-muted-text-color);
}
}
</style>
