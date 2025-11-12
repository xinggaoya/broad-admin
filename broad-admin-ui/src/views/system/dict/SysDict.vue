<template>
  <section class="dict-page">
    <header class="page-header">
      <div>
        <p class="header-subtitle">系统管理</p>
        <h2>字典管理</h2>
        <p class="header-desc">左侧维护字典类型，右侧实时查看并编辑对应的数据项。</p>
      </div>
      <div class="header-meta">
        <n-tag v-if="selectedDictType" type="primary" round :bordered="false">
          当前类型：{{ selectedDictType }}
        </n-tag>
        <n-button v-if="selectedDictType" text size="small" @click="clearSelection">清空选择</n-button>
      </div>
    </header>

    <n-card class="tabs-panel" :bordered="false">
      <n-tabs type="segment" size="large" :value="tabValue" @update:value="handleTabChange">
        <n-tab-pane name="type" tab="字典类型">
          <DictType @select-dict="handleSelectDict" />
        </n-tab-pane>
        <n-tab-pane name="data" tab="字典数据">
          <div class="dict-data-card__header">
            <div>
              <p class="header-subtitle">数据面板</p>
              <h3>{{ selectedDictType ? '字典数据' : '请选择字典类型' }}</h3>
            </div>
            <n-button text size="small" @click="handleRefreshHint" :disabled="!selectedDictType">
              刷新提示
            </n-button>
          </div>
          <div class="dict-data-card__body">
            <Transition name="fade-slide" mode="out-in">
              <DictData
                v-if="selectedDictType"
                key="dict-data"
                :dict-type="selectedDictType"
                compact
                @back="handleDataBack"
              />
              <n-empty
                v-else
                key="dict-empty"
                class="dict-empty"
                description="请选择字典类型后查看数据"
              >
                <template #extra>
                  <n-button type="primary" ghost @click="promptToSelect">前往选择</n-button>
                </template>
              </n-empty>
            </Transition>
          </div>
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </section>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { useMessage } from 'naive-ui'
import DictType from './dictType.vue'
import DictData from './dictData.vue'

const message = useMessage()
const selectedDictType = ref('')
const tabValue = ref<'type' | 'data'>('type')

const handleSelectDict = (dictType: string) => {
  selectedDictType.value = dictType
  message.success(`已切换至字典类型：${dictType}`)
  tabValue.value = 'data'
}

const clearSelection = () => {
  selectedDictType.value = ''
  message.info('已清空当前字典类型')
}

const promptToSelect = () => {
  message.info('请在左侧列表中选择需要维护的字典类型')
}

const handleRefreshHint = () => {
  if (!selectedDictType.value) {
    promptToSelect()
  } else {
    message.info('请使用右侧数据面板中的刷新按钮同步最新数据')
  }
}

const handleTabChange = (value: 'type' | 'data') => {
  if (value === 'data' && !selectedDictType.value) {
    promptToSelect()
    return
  }
  tabValue.value = value
}

const handleDataBack = () => {
  tabValue.value = 'type'
}
</script>

<style scoped lang="scss">
.dict-page {
  display: flex;
  flex-direction: column;
  gap: var(--shell-gap);
  width: 100%;
  min-width: 0;
}

.page-header {
  background: var(--shell-surface);
  border-radius: var(--shell-radius-lg);
  padding: 20px;
  box-shadow: var(--shell-shadow);
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

.header-subtitle {
  margin: 0;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--shell-muted-text-color);
}

.header-desc {
  margin: 6px 0 0;
  font-size: 14px;
  color: var(--shell-muted-text-color);
}

.header-meta {
  display: flex;
  gap: 12px;
  align-items: center;
}

.dict-data-card__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 0;
}

.dict-data-card__body {
  min-height: 360px;
}

.dict-empty {
  padding: 80px 0;
}

.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(6px);
}

.tabs-panel {
  border-radius: var(--shell-radius-lg);
  box-shadow: var(--shell-shadow);
}
</style>
