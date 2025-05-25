<template>
  <n-form
    ref="formRef"
    :model="formModel"
    :label-width="config.labelWidth || 'auto'"
    :label-placement="config.labelPlacement || 'left'"
    :size="config.size || 'medium'"
    :show-feedback="config.showFeedback !== false"
  >
    <n-grid
      :cols="config.cols || 24"
      :x-gap="config.xGap || 16"
      :y-gap="config.yGap || 16"
      responsive="screen"
    >
      <n-form-item-gi
        v-for="item in config.items"
        :key="item.key"
        :span="item.span || getDefaultSpan(item.type)"
        :label="item.label"
        :path="item.key"
      >
        <!-- 输入框 -->
        <n-input
          v-if="item.type === 'input'"
          v-model:value="formModel[item.key]"
          :placeholder="item.placeholder || `请输入${item.label}`"
          clearable
          v-bind="item.props"
        />

        <!-- 选择器 -->
        <n-select
          v-else-if="item.type === 'select'"
          v-model:value="formModel[item.key]"
          :placeholder="item.placeholder || `请选择${item.label}`"
          :options="item.options || []"
          clearable
          filterable
          v-bind="item.props"
        />

        <!-- 数字输入框 -->
        <n-input-number
          v-else-if="item.type === 'number'"
          v-model:value="formModel[item.key]"
          :placeholder="item.placeholder || `请输入${item.label}`"
          clearable
          style="width: 100%"
          v-bind="item.props"
        />

        <!-- 日期选择器 -->
        <n-date-picker
          v-else-if="item.type === 'date'"
          v-model:value="formModel[item.key]"
          :placeholder="item.placeholder || `请选择${item.label}`"
          clearable
          style="width: 100%"
          v-bind="item.props"
        />

        <!-- 日期范围选择器 -->
        <n-date-picker
          v-else-if="item.type === 'daterange'"
          v-model:value="formModel[item.key]"
          type="daterange"
          :placeholder="item.placeholder || [`开始${item.label}`, `结束${item.label}`]"
          clearable
          style="width: 100%"
          v-bind="item.props"
        />

        <!-- 开关 -->
        <n-switch
          v-else-if="item.type === 'switch'"
          v-model:value="formModel[item.key]"
          v-bind="item.props"
        />

        <!-- 自定义渲染 -->
        <component
          v-else-if="item.type === 'custom' && item.render"
          :is="item.render"
          v-model:value="formModel[item.key]"
          v-bind="item.props"
        />
      </n-form-item-gi>
    </n-grid>
  </n-form>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import type { SearchFormConfig, SearchFormItem } from '@/types/table'

interface Props {
  config: SearchFormConfig
  modelValue: Record<string, any>
}

interface Emits {
  (e: 'update:modelValue', value: Record<string, any>): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const formRef = ref()
const formModel = ref<Record<string, any>>({})

// 初始化表单模型
const initFormModel = () => {
  const model: Record<string, any> = {}
  props.config.items.forEach((item: SearchFormItem) => {
    model[item.key] = props.modelValue[item.key] ?? item.defaultValue ?? null
  })
  formModel.value = model
}

// 监听配置变化，重新初始化表单
watch(
  () => props.config,
  () => {
    initFormModel()
  },
  { immediate: true, deep: true }
)

// 监听外部模型变化
watch(
  () => props.modelValue,
  (newValue) => {
    if (newValue && typeof newValue === 'object') {
      formModel.value = { ...formModel.value, ...newValue }
    }
  },
  { deep: true }
)

// 监听内部模型变化，同步到外部
watch(
  formModel,
  (newValue) => {
    emit('update:modelValue', { ...newValue })
  },
  { deep: true }
)

// 获取默认跨度
const getDefaultSpan = (type: string) => {
  switch (type) {
    case 'switch':
      return 4
    case 'daterange':
      return 8
    case 'select':
    case 'date':
      return 6
    default:
      return 6
  }
}

// 重置表单
const reset = () => {
  const model: Record<string, any> = {}
  props.config.items.forEach((item: SearchFormItem) => {
    model[item.key] = item.defaultValue ?? null
  })
  formModel.value = model
}

// 验证表单
const validate = () => {
  return formRef.value?.validate()
}

// 获取表单数据
const getFormData = () => {
  return { ...formModel.value }
}

defineExpose({
  reset,
  validate,
  getFormData,
  formRef
})
</script>

<style scoped lang="scss">
:deep(.n-form-item) {
  margin-bottom: 0;
}

:deep(.n-form-item-label) {
  font-weight: 500;
}
</style>
