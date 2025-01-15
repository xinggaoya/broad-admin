<template>
  <n-popover trigger="click" placement="bottom-end">
    <template #trigger>
      <n-button strong circle type="tertiary" size="small">
        <template #icon>
          <n-icon>
            <SettingsIcon />
          </n-icon>
        </template>
      </n-button>
    </template>
    <template #header>
      <div>表格设置</div>
    </template>
    <template #default>
      <n-space vertical>
        <div class="config-item">
          <label class="label">边框</label>
          <n-switch :round="false" v-model:value="config.border" @update:value="onUpdateValue" />
        </div>
        <div class="config-item">
          <label class="label">条纹</label>
          <n-switch :round="false" v-model:value="config.striped" @update:value="onUpdateValue" />
        </div>
        <div class="config-item">
          <label class="label">尺寸</label>
          <n-select
            v-model:value="config.size"
            :options="sizeOptions"
            size="small"
            @update:value="onUpdateValue"
          />
        </div>
        <div class="config-item">
          <label class="label">单行</label>
          <n-switch
            :round="false"
            v-model:value="config.singleLine"
            @update:value="onUpdateValue"
          />
        </div>
      </n-space>
    </template>
  </n-popover>
</template>

<script lang="ts" setup>
import { ref, watch } from 'vue'
import { Settings as SettingsIcon } from '@vicons/ionicons5'
import type { TableConfig } from '@/types/table'

const props = defineProps<{
  config: TableConfig
}>()

const emit = defineEmits<{
  (e: 'update', config: TableConfig): void
}>()

const config = ref<TableConfig>({
  border: props.config?.border ?? false,
  striped: props.config?.striped ?? false,
  size: props.config?.size ?? 'medium',
  singleLine: props.config?.singleLine ?? true,
  maxHeight: props.config?.maxHeight
})

const sizeOptions = [
  { label: '小', value: 'small' },
  { label: '中', value: 'medium' },
  { label: '大', value: 'large' }
]

// 监听props变化
watch(
  () => props.config,
  (newConfig) => {
    if (newConfig) {
      config.value = { ...config.value, ...newConfig }
    }
  },
  { deep: true }
)

function onUpdateValue() {
  emit('update', config.value)
}
</script>

<style lang="scss" scoped>
.config-item {
  display: flex;
  align-items: center;
  margin-bottom: 12px;

  &:last-child {
    margin-bottom: 0;
  }

  .label {
    width: 60px;
    margin-right: 12px;
    color: var(--text-color);
  }
}
</style>
