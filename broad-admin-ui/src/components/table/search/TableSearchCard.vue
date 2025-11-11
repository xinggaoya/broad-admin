<template>
  <n-card size="small" class="table-search-card" :bordered="false">
    <template #header>
      <div class="search-header">
        <div class="search-title">
          <n-icon size="16">
            <SearchIcon />
          </n-icon>
          <span>{{ title || '搜索条件' }}</span>
        </div>
        <n-button
          v-if="collapsible"
          text
          size="small"
          @click="toggleCollapse"
          :class="{ 'is-collapsed': !isExpanded }"
        >
          <template #icon>
            <n-icon>
              <ChevronDownIcon v-if="!isExpanded" />
              <ChevronUpIcon v-else />
            </n-icon>
          </template>
        </n-button>
      </div>
    </template>

    <template #default>
      <n-collapse-transition :show="isExpanded">
        <div class="search-content">
          <n-form
            ref="formRef"
            :model="modelValue"
            :label-width="labelWidth"
            :label-placement="labelPlacement"
            :size="size"
            inline
            class="search-form"
          >
            <slot name="default">
              <!-- 默认搜索字段 -->
              <n-form-item
                v-for="field in defaultFields"
                :key="field.key"
                :label="field.label"
                :path="field.key"
              >
                <template v-if="field.type === 'input'">
                  <n-input
                    v-model:value="modelValue[field.key]"
                    :placeholder="field.placeholder || `请输入${field.label}`"
                    clearable
                    @keydown.enter="handleSearch"
                  />
                </template>
                <template v-else-if="field.type === 'select'">
                  <n-select
                    v-model:value="modelValue[field.key]"
                    :placeholder="field.placeholder || `请选择${field.label}`"
                    :options="field.options"
                    clearable
                  />
                </template>
                <template v-else-if="field.type === 'date'">
                  <n-date-picker
                    v-model:value="modelValue[field.key]"
                    type="date"
                    :placeholder="field.placeholder || `请选择${field.label}`"
                    clearable
                  />
                </template>
                <template v-else-if="field.type === 'daterange'">
                  <n-date-picker
                    v-model:value="modelValue[field.key]"
                    type="daterange"
                    :placeholder="field.placeholder || `请选择${field.label}`"
                    clearable
                  />
                </template>
                <template v-else>
                  <slot :name="`field-${field.key}`" :field="field">
                    <n-input
                      v-model:value="modelValue[field.key]"
                      :placeholder="field.placeholder || `请输入${field.label}`"
                      clearable
                      @keydown.enter="handleSearch"
                    />
                  </slot>
                </template>
              </n-form-item>
            </slot>

            <!-- 操作按钮 -->
            <n-form-item>
              <n-space>
                <n-button
                  type="primary"
                  :loading="loading"
                  @click="handleSearch"
                >
                  <template #icon>
                    <n-icon><SearchIcon /></n-icon>
                  </template>
                  搜索
                </n-button>
                <n-button @click="handleReset">
                  <template #icon>
                    <n-icon><RefreshIcon /></n-icon>
                  </template>
                  重置
                </n-button>
                <slot name="actions" />
              </n-space>
            </n-form-item>
          </n-form>
        </div>
      </n-collapse-transition>
    </template>
  </n-card>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { Search as SearchIcon, ChevronDown as ChevronDownIcon, ChevronUp as ChevronUpIcon, Refresh as RefreshIcon } from '@vicons/ionicons5'

// 搜索字段接口
export interface SearchField {
  key: string
  label: string
  type: 'input' | 'select' | 'date' | 'daterange' | 'custom'
  placeholder?: string
  options?: Array<{ label: string; value: any }>
  defaultValue?: any
}

interface Props {
  modelValue: Record<string, any>
  title?: string
  fields?: SearchField[]
  loading?: boolean
  collapsible?: boolean
  defaultExpanded?: boolean
  labelWidth?: string | number
  labelPlacement?: 'left' | 'top'
  size?: 'small' | 'medium' | 'large'
}

interface Emits {
  (e: 'update:modelValue', value: Record<string, any>): void
  (e: 'search', params: Record<string, any>): void
  (e: 'reset'): void
}

const props = withDefaults(defineProps<Props>(), {
  title: '搜索条件',
  fields: () => [],
  loading: false,
  collapsible: false,
  defaultExpanded: true,
  labelWidth: 80,
  labelPlacement: 'left',
  size: 'medium'
})

const emit = defineEmits<Emits>()

const formRef = ref()
const isExpanded = ref(props.defaultExpanded)

// 默认搜索字段（兼容旧版本）
const defaultFields = computed(() => {
  if (props.fields.length > 0) {
    return props.fields
  }

  // 提供一些常用的默认字段
  return [
    { key: 'keyword', label: '关键词', type: 'input' as const },
    { key: 'status', label: '状态', type: 'select' as const, options: [] }
  ]
})

function toggleCollapse() {
  isExpanded.value = !isExpanded.value
}

function handleSearch() {
  // 过滤空值
  const searchParams = Object.keys(props.modelValue).reduce((params, key) => {
    const value = props.modelValue[key]
    if (value !== null && value !== undefined && value !== '') {
      params[key] = value
    }
    return params
  }, {} as Record<string, any>)

  emit('search', searchParams)
}

function handleReset() {
  // 重置所有字段为默认值或空值
  const resetValue = defaultFields.value.reduce((acc, field) => {
    acc[field.key] = field.defaultValue ?? (field.type === 'select' ? null : '')
    return acc
  }, {} as Record<string, any>)

  emit('update:modelValue', resetValue)
  emit('reset')
}

// 暴露方法
defineExpose({
  reset: handleReset,
  search: handleSearch,
  validate: () => formRef.value?.validate(),
  getFormData: () => formRef.value ? { ...props.modelValue } : {}
})
</script>

<style scoped lang="scss">
.table-search-card {
  .search-header {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .search-title {
      display: flex;
      align-items: center;
      gap: 8px;
      font-weight: 500;
      color: var(--text-color);
    }

    .is-collapsed {
      transform: rotate(-90deg);
      transition: transform 0.3s ease;
    }
  }

  .search-content {
    .search-form {
      .n-form-item {
        margin-bottom: 16px;

        &:last-child {
          margin-bottom: 0;
        }
      }
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .table-search-card {
    .search-form {
      .n-form-item {
        margin-bottom: 12px;

        &:last-child {
          margin-bottom: 0;
        }
      }
    }
  }
}
</style>