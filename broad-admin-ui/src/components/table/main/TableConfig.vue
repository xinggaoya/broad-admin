<template>
  <n-popover trigger="click" placement="bottom-end" :style="{ width: '260px' }">
    <template #trigger>
      <n-tooltip trigger="hover" placement="top">
        <template #trigger>
          <n-button strong circle type="tertiary" size="small">
            <template #icon>
              <n-icon>
                <SettingsIcon />
              </n-icon>
            </template>
          </n-button>
        </template>
        表格设置
      </n-tooltip>
    </template>

    <template #header>
      <div class="config-header">
        <n-icon size="16" style="margin-right: 6px">
          <SettingsIcon />
        </n-icon>
        表格设置
      </div>
    </template>

    <template #default>
      <div class="table-config-content">
        <n-space vertical size="large">
          <!-- 显示设置 -->
          <div class="config-section">
            <div class="section-title">显示设置</div>
            <n-space vertical size="medium">
              <div class="config-item">
                <label class="label">边框</label>
                <n-switch
                  :round="false"
                  v-model:value="localConfig.border"
                  @update:value="onUpdateValue"
                />
              </div>
              <div class="config-item">
                <label class="label">条纹</label>
                <n-switch
                  :round="false"
                  v-model:value="localConfig.striped"
                  @update:value="onUpdateValue"
                />
              </div>
              <div class="config-item">
                <label class="label">单行</label>
                <n-switch
                  :round="false"
                  v-model:value="localConfig.singleLine"
                  @update:value="onUpdateValue"
                />
              </div>
            </n-space>
          </div>

          <!-- 尺寸设置 -->
          <div class="config-section">
            <div class="section-title">尺寸设置</div>
            <div class="config-item">
              <label class="label">表格尺寸</label>
              <n-select
                v-model:value="localConfig.size"
                :options="sizeOptions"
                size="small"
                @update:value="onUpdateValue"
                style="width: 100px"
              />
            </div>
          </div>

          <!-- 高度设置 -->
          <div class="config-section">
            <div class="section-title">高度设置</div>
            <div class="config-item">
              <label class="label">最大高度</label>
              <n-input-number
                v-model:value="localConfig.maxHeight"
                :min="200"
                :max="1000"
                :step="50"
                size="small"
                placeholder="自动"
                @update:value="onUpdateValue"
                style="width: 100px"
              />
            </div>
          </div>
        </n-space>

        <!-- 操作按钮 -->
        <n-divider style="margin: 16px 0" />
        <div class="config-actions">
          <n-space justify="space-between">
            <n-button size="small" @click="onReset"> 重置 </n-button>
            <n-space>
              <n-button size="small" @click="onSave"> 保存 </n-button>
              <n-button type="primary" size="small" @click="onApply"> 应用 </n-button>
            </n-space>
          </n-space>
        </div>
      </div>
    </template>
  </n-popover>
</template>

<script lang="ts" setup>
import { ref, watch, reactive } from 'vue'
import { Settings as SettingsIcon } from '@vicons/ionicons5'
import type { TableConfig } from '@/types/table'

interface Props {
  config: TableConfig
}

interface Emits {
  (e: 'update', config: TableConfig): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 本地配置状态
const localConfig = reactive<TableConfig>({
  border: false,
  striped: false,
  size: 'medium',
  singleLine: true,
  maxHeight: undefined
})

// 默认配置
const defaultConfig: TableConfig = {
  border: false,
  striped: false,
  size: 'medium',
  singleLine: true,
  maxHeight: undefined
}

// 尺寸选项
const sizeOptions = [
  { label: '紧凑', value: 'small' },
  { label: '默认', value: 'medium' },
  { label: '宽松', value: 'large' }
]

// 监听props变化，同步到本地状态
watch(
  () => props.config,
  (newConfig) => {
    if (newConfig) {
      Object.assign(localConfig, {
        border: newConfig.border ?? defaultConfig.border,
        striped: newConfig.striped ?? defaultConfig.striped,
        size: newConfig.size ?? defaultConfig.size,
        singleLine: newConfig.singleLine ?? defaultConfig.singleLine,
        maxHeight: newConfig.maxHeight ?? defaultConfig.maxHeight
      })
    }
  },
  { immediate: true, deep: true }
)

// 配置更新
const onUpdateValue = () => {
  emit('update', { ...localConfig })
}

// 重置配置
const onReset = () => {
  Object.assign(localConfig, defaultConfig)
  onUpdateValue()
}

// 保存配置（可以扩展为保存到localStorage）
const onSave = () => {
  try {
    localStorage.setItem('table-config', JSON.stringify(localConfig))
    // 这里可以添加成功提示
  } catch (error) {
    console.warn('保存表格配置失败:', error)
  }
}

// 应用配置
const onApply = () => {
  onUpdateValue()
}

// 组件挂载时尝试加载保存的配置
const loadSavedConfig = () => {
  try {
    const saved = localStorage.getItem('table-config')
    if (saved) {
      const savedConfig = JSON.parse(saved)
      Object.assign(localConfig, savedConfig)
      onUpdateValue()
    }
  } catch (error) {
    console.warn('加载保存的表格配置失败:', error)
  }
}

// 暴露方法
defineExpose({
  reset: onReset,
  save: onSave,
  loadSavedConfig
})
</script>

<style lang="scss" scoped>
.config-header {
  display: flex;
  align-items: center;
  font-weight: 500;
  color: var(--text-color);
}

.table-config-content {
  .config-section {
    .section-title {
      font-size: 13px;
      font-weight: 500;
      color: var(--text-color);
      margin-bottom: 8px;
      padding-bottom: 4px;
      border-bottom: 1px solid var(--divider-color);
    }
  }

  .config-item {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .label {
      font-size: 13px;
      color: var(--text-color);
      white-space: nowrap;
    }
  }

  .config-actions {
    margin-top: 8px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .table-config-content {
    .config-item {
      flex-direction: column;
      align-items: flex-start;
      gap: 8px;

      .label {
        margin-bottom: 4px;
      }
    }
  }
}
</style>
