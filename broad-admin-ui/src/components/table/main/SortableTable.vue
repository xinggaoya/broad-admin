<template>
  <n-popover :style="{ width: '280px' }" placement="left-start" trigger="click">
    <template #default>
      <div class="sortable-table-content">
        <div class="header">
          <n-checkbox
            v-model:checked="allChecked"
            :indeterminate="isIndeterminate"
            @update:checked="onAllChange"
          >
            全选
          </n-checkbox>
          <n-space>
            <n-button text type="primary" size="tiny" @click="onReset"> 重置 </n-button>
            <n-button text type="info" size="tiny" @click="onSave"> 保存 </n-button>
          </n-space>
        </div>

        <n-divider style="margin: 8px 0" />

        <n-scrollbar style="max-height: 320px">
          <draggable
            :list="innerColumns"
            animation="300"
            item-key="key"
            handle=".drag-handle"
            @start="onDragStart"
            @end="onDragEnd"
            class="column-list"
            :component-data="{ name: 'transition-group', tag: 'div' }"
          >
            <template #item="{ element }">
              <div class="column-item">
                <div class="column-info">
                  <n-checkbox v-model:checked="element.checked" @update:checked="onChange">
                    <span class="column-title">{{ element.title }}</span>
                  </n-checkbox>
                </div>
                <n-icon class="drag-handle" size="16">
                  <MenuIcon />
                </n-icon>
              </div>
            </template>
          </draggable>
        </n-scrollbar>

        <n-divider style="margin: 8px 0" />

        <div class="footer">
          <n-text depth="3" style="font-size: 12px">
            已选择 {{ checkedCount }} / {{ totalCount }} 列
          </n-text>
        </div>
      </div>
    </template>

    <template #trigger>
      <n-tooltip trigger="hover" placement="top">
        <template #trigger>
          <n-button type="tertiary" size="small" circle>
            <template #icon>
              <n-icon>
                <ListIcon />
              </n-icon>
            </template>
          </n-button>
        </template>
        列设置
      </n-tooltip>
    </template>
  </n-popover>
</template>

<script lang="ts" setup>
import { computed, ref, watch, nextTick } from 'vue'
import type { DataTableColumns } from 'naive-ui'
import draggable from 'vuedraggable'
import { List as ListIcon, Menu as MenuIcon } from '@vicons/ionicons5'
import type { InnerColumn } from '@/types/table'

interface Props {
  columns: DataTableColumns
}

interface Emits {
  (e: 'update:columns', columns: DataTableColumns): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

// 内部列数据 - 改为 ref 而不是 reactive
const innerColumns = ref<InnerColumn[]>([])
// 原始列顺序备份
const originalColumns = ref<InnerColumn[]>([])
// 拖拽状态标记
const isDragging = ref(false)

// 初始化列数据
const initColumns = (columns: DataTableColumns) => {
  const normalColumns = columns.filter((col: any) => {
    return col.key && !col.type && col.title
  }) as any[]

  const newColumns: InnerColumn[] = normalColumns.map((col, index) => ({
    key: col.key,
    title: col.title,
    checked: col.show !== false,
    type: col.type || 'default',
    order: index,
    ...col
  }))

  // 直接赋值而不是 splice
  innerColumns.value = [...newColumns]
  originalColumns.value = newColumns.map((col) => ({ ...col }))
}

// 监听columns变化 - 添加拖拽状态检查
watch(
  () => props.columns,
  (newColumns) => {
    // 如果正在拖拽，不要更新
    if (isDragging.value) {
      return
    }

    if (newColumns && newColumns.length > 0) {
      // 检查是否需要重新初始化
      const normalColumns = newColumns.filter((col: any) => col.key && !col.type && col.title)

      if (innerColumns.value.length === 0 || normalColumns.length !== innerColumns.value.length) {
        initColumns(newColumns)
      } else {
        // 更新现有列的状态，但保持当前顺序
        const updatedColumns = innerColumns.value.map((innerCol) => {
          const matchingCol = normalColumns.find((col: any) => col.key === innerCol.key)
          if (matchingCol) {
            return {
              ...innerCol,
              title: (matchingCol as any).title,
              checked: (matchingCol as any).show !== false,
              ...(matchingCol as any)
            }
          }
          return innerCol
        })

        innerColumns.value = updatedColumns
      }
    }
  },
  { immediate: true, deep: true }
)

// 计算属性
const allChecked = computed(
  () => innerColumns.value.length > 0 && innerColumns.value.every((col) => col.checked)
)

const isIndeterminate = computed(
  () => innerColumns.value.some((col) => col.checked) && !allChecked.value
)

const checkedCount = computed(() => innerColumns.value.filter((col) => col.checked).length)

const totalCount = computed(() => innerColumns.value.length)

// 拖拽开始
const onDragStart = () => {
  isDragging.value = true
}

// 拖拽结束
const onDragEnd = () => {
  isDragging.value = false
  // 延迟更新，确保拖拽完成
  nextTick(() => {
    updateColumns()
  })
}

// 全选/取消全选
const onAllChange = (checked: boolean) => {
  innerColumns.value = innerColumns.value.map((col) => ({
    ...col,
    checked
  }))
  updateColumns()
}

// 单个选择变化
const onChange = () => {
  nextTick(() => {
    updateColumns()
  })
}

// 重置到原始状态
const onReset = () => {
  // 恢复原始顺序和状态
  const resetColumns = originalColumns.value.map((col) => ({ ...col, checked: true }))
  innerColumns.value = [...resetColumns]
  updateColumns()
}

// 保存当前状态为默认状态
const onSave = () => {
  originalColumns.value = innerColumns.value.map((col) => ({ ...col }))
}

// 更新列配置
const updateColumns = () => {
  try {
    // 获取特殊列（selection、expand等）
    const specialColumns = props.columns.filter(
      (col: any) => !col.key || col.type === 'selection' || col.type === 'expand'
    )

    // 根据 innerColumns 的顺序和显示状态构建普通列
    const normalColumns = innerColumns.value
      .map((innerCol) => {
        // 从原始 columns 中找到对应的列配置
        const originalCol = props.columns.find((col: any) => col.key === innerCol.key)
        if (originalCol) {
          return {
            ...originalCol,
            show: innerCol.checked
          }
        }
        return null
      })
      .filter((col): col is NonNullable<typeof col> => col !== null)

    // 构建新的列数组
    const newColumns = [...specialColumns, ...normalColumns]

    // 触发更新事件
    emit('update:columns', newColumns)
  } catch (error) {
    console.warn('更新表格列配置失败:', error)
  }
}

// 暴露方法
defineExpose({
  reset: onReset,
  save: onSave
})
</script>

<style lang="scss" scoped>
.sortable-table-content {
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 4px 0;
  }

  .column-list {
    min-height: 60px;
  }

  .column-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 8px 4px;
    border-radius: 4px;
    transition: all 0.2s ease;
    cursor: pointer;

    &:hover {
      background-color: var(--hover-color);
    }

    .column-info {
      flex: 1;
      min-width: 0;

      .column-title {
        font-size: 13px;
        color: var(--text-color);
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }

    .drag-handle {
      cursor: move;
      color: var(--text-color-3);
      padding: 4px;
      border-radius: 2px;
      transition: all 0.2s ease;

      &:hover {
        color: var(--primary-color);
        background-color: var(--primary-color-hover);
      }
    }
  }

  .footer {
    display: flex;
    justify-content: center;
    padding: 4px 0;
  }
}

// 拖拽时的样式
.sortable-ghost {
  opacity: 0.5;
  background-color: var(--primary-color-hover);
}

.sortable-chosen {
  background-color: var(--primary-color-hover);
}

.sortable-drag {
  background-color: var(--card-color);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
}

// 过渡动画
.transition-group-move,
.transition-group-enter-active,
.transition-group-leave-active {
  transition: all 0.3s ease;
}

.transition-group-enter-from,
.transition-group-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

.transition-group-leave-active {
  position: absolute;
}
</style>
