<template>
  <n-popover :style="{ width: '240px' }" placement="left-start" trigger="click">
    <template #default>
      <div class="header">
        <n-checkbox
          v-model:checked="allChecked"
          :indeterminate="isIndeterminate"
          @update:checked="onAllChange"
        >
          全选
        </n-checkbox>
        <n-button text type="primary" size="tiny" @click="onReset"> 重置 </n-button>
      </div>
      <n-scrollbar style="max-height: 300px">
        <draggable
          :list="innerColumns"
          animation="300"
          item-key="key"
          handle=".drag-handle"
          @end="onDragEnd"
        >
          <template #item="{ element }">
            <div class="column-item">
              <n-checkbox
                v-model:checked="element.checked"
                :label="element.key"
                @update:checked="onChange"
              >
                {{ element.title }}
              </n-checkbox>
              <n-icon class="drag-handle">
                <MenuIcon />
              </n-icon>
            </div>
          </template>
        </draggable>
      </n-scrollbar>
    </template>
    <template #trigger>
      <n-button type="tertiary" size="small" circle>
        <template #icon>
          <n-icon>
            <ListIcon />
          </n-icon>
        </template>
      </n-button>
    </template>
  </n-popover>
</template>

<script lang="ts" setup>
import { computed, reactive, watch } from 'vue'
import type { DataTableColumn, DataTableColumns } from 'naive-ui'
import draggable from 'vuedraggable'
import { List as ListIcon, Menu as MenuIcon } from '@vicons/ionicons5'

type InnerColumn = {
  checked: boolean
  key: string | number
  title: string
  type?: 'default' | 'selection' | 'expand'
} & Partial<DataTableColumn>

const props = defineProps<{
  columns: DataTableColumns
}>()

const emit = defineEmits<{
  (e: 'update:columns', columns: DataTableColumns): void
}>()

// 内部列数据
const innerColumns = reactive<InnerColumn[]>([])

// 监听columns变化
watch(
  () => props.columns,
  (newColumns) => {
    // 只在初始化或有新列时更新 innerColumns
    const normalColumns = newColumns.filter((col: any) => col.key && !col.type)
    if (innerColumns.length === 0) {
      // 初始化
      normalColumns.forEach((col) => {
        innerColumns.push({
          ...(col as any),
          checked: true
        })
      })
    } else {
      // 检查新增的列
      const existingKeys = innerColumns.map((col) => col.key)
      normalColumns.forEach((col) => {
        if (!existingKeys.includes(col.key)) {
          innerColumns.push({
            ...(col as any),
            checked: true
          })
        }
      })
    }
  },
  { immediate: true }
)

// 全选状态
const allChecked = computed(() => innerColumns.every((col) => col.checked))
const isIndeterminate = computed(() => innerColumns.some((col) => col.checked) && !allChecked.value)

// 全选/取消全选
function onAllChange(checked: boolean) {
  innerColumns.forEach((col) => {
    col.checked = checked
  })
  updateColumns()
}

// 单个选择变化
function onChange() {
  updateColumns()
}

// 重置
function onReset() {
  // 恢复原始顺序
  const originalOrder = props.columns
    .filter((col: any) => col.key && !col.type)
    .map((col: any) => col.key)

  // 创建新数组并排序
  const sortedColumns = [...innerColumns].sort((a, b) => {
    const aIndex = originalOrder.indexOf(a.key)
    const bIndex = originalOrder.indexOf(b.key)
    return aIndex - bIndex
  })

  // 清空原数组并添加排序后的元素
  innerColumns.splice(0, innerColumns.length, ...sortedColumns)

  // 恢复选中状态
  innerColumns.forEach((col) => {
    col.checked = true
  })

  updateColumns()
}

// 拖拽结束
function onDragEnd() {
  updateColumns()
}

// 更新列
function updateColumns() {
  // 获取特殊列
  const specialColumns = props.columns.filter((col: any) => !col.key || col.type)

  // 获取普通列并按照 innerColumns 的顺序排序
  const normalColumns = innerColumns
    .map((innerCol) => {
      const originalCol = props.columns.find((col: any) => col.key === innerCol.key)
      if (originalCol) {
        return {
          ...originalCol,
          show: innerCol.checked
        }
      }
      return null
    })
    .filter(Boolean)

  // 更新原数组
  const columnsArray = props.columns as any[]
  columnsArray.length = 0 // 清空数组
  columnsArray.push(...specialColumns, ...normalColumns) // 添加所有列
}
</script>

<style lang="scss" scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px;
  border-bottom: 1px solid var(--divider-color);
}

.column-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px;
  transition: background-color 0.3s;

  &:hover {
    background-color: var(--hover-color);
  }

  .drag-handle {
    cursor: move;
    color: var(--text-color-3);

    &:hover {
      color: var(--text-color-2);
    }
  }
}
</style>
