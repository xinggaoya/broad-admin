<template>
  <div class="dict-container">
    <n-card>
      <n-tabs type="segment" size="large" :value="activeTab" @update:value="handleTabChange">
        <n-tab-pane name="type" tab="字典类型">
          <DictType v-if="activeTab === 'type'" @select-dict="handleSelectDict" />
        </n-tab-pane>
        <n-tab-pane name="data" tab="字典数据">
          <DictData v-if="activeTab === 'data'" :dict-type="selectedDictType" @back="handleBack" />
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </div>
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

<style lang="scss" scoped>
.dict-container {
  background-color: #f5f7fa;
  min-height: calc(100vh - 120px);

  :deep(.n-card) {
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  }

  :deep(.n-tabs) {
    margin: -16px;
  }

  :deep(.n-tab-pane) {
    padding: 16px;
  }
}
</style>
