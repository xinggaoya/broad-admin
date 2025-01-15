<template>
  <n-card>
    <template #header>
      <n-space>
        <slot name="header" />
      </n-space>
    </template>
    <template #header-extra>
      <n-space>
        <TableConfigPanel :config="tableConfig" @update="updateConfig" />
        <SortableTable class="ml-4" :columns="columns" @update:columns="handleColumnsChange" />
      </n-space>
    </template>
    <template #default>
      <n-data-table
        ref="tableRef"
        :data="data"
        :bordered="tableConfig.border"
        :loading="loading"
        :striped="tableConfig.striped"
        :remote="remote"
        :columns="displayColumns"
        :row-key="useRowKey"
        :pagination="pagination"
        :allow-checking-not-loaded="allowCheckingNotLoaded"
        :cascade="cascade"
        @load="onLoad"
        :scroll-x="scrollX"
        :size="tableConfig.size"
        :single-line="tableConfig.singleLine"
        :max-height="tableConfig.maxHeight"
        :children-key="childrenKey"
        :indent="indent"
        :expandable="expandable"
        :default-expand-all="defaultExpandAll"
        @update:checked-row-keys="handleCheckedRowKeysChange"
        @update:page="handlePageChange"
        @update:page-size="handlePageSizeChange"
        @update:sorter="handleSorterChange"
        @update:expanded-row-keys="$emit('update:expanded-row-keys', $event)"
      />
    </template>
  </n-card>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import type { DataTableColumns, DataTableBaseColumn } from 'naive-ui'
import type { TableConfig } from '@/types/table'
import SortableTable from './SortableTable.vue'
import TableConfigPanel from './TableConfig.vue'

interface Props {
  loading?: boolean
  remote?: boolean
  columns: DataTableColumns
  data: Array<any>
  pagination?: object | boolean
  onLoad?: (row: any) => Promise<void>
  cascade?: boolean
  allowCheckingNotLoaded?: boolean
  rowKey?: string | ((row: any) => string | number)
  scrollX?: number | string
  childrenKey?: string
  indent?: number
  expandable?: boolean
  defaultExpandAll?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  remote: true,
  columns: () => [],
  data: () => [],
  pagination: false,
  onLoad: undefined,
  cascade: true,
  allowCheckingNotLoaded: false,
  rowKey: '',
  scrollX: undefined,
  childrenKey: 'children',
  indent: 16,
  expandable: true,
  defaultExpandAll: false
})

const emit = defineEmits([
  'update:checked-row-keys',
  'update:page',
  'update:page-size',
  'update:sorter',
  'update:columns',
  'update:expanded-row-keys'
])

// 表格实例
const tableRef = ref()

// 表格配置
const tableConfig = ref<TableConfig>({
  border: false,
  striped: false,
  size: 'medium',
  singleLine: true,
  maxHeight: undefined
})

// 获取rowKey
const useRowKey = computed(() => {
  return (rowData: any) => {
    if (!props.rowKey) {
      const firstColumn = props.columns[0] as DataTableBaseColumn
      return firstColumn?.key ? rowData[firstColumn.key] : rowData.id
    }
    return rowData[props.rowKey]
  }
})

// 更新表格配置
function updateConfig(config: TableConfig) {
  tableConfig.value = { ...tableConfig.value, ...config }
}

// 选中行变化
function handleCheckedRowKeysChange(keys: Array<string | number>) {
  emit('update:checked-row-keys', keys)
}

// 页码变化
function handlePageChange(page: number) {
  emit('update:page', page)
}

// 每页条数变化
function handlePageSizeChange(pageSize: number) {
  emit('update:page-size', pageSize)
}

// 排序变化
function handleSorterChange(sorter: { columnKey: string; order: 'ascend' | 'descend' | false }) {
  emit('update:sorter', sorter)
}

// 处理列变化
function handleColumnsChange(columns: DataTableColumns) {
  emit('update:columns', columns)
}

// 暴露方法给父组件
defineExpose({
  clearChecked: () => tableRef.value?.clearChecked(),
  clearFilters: () => tableRef.value?.clearFilters(),
  clearSorter: () => tableRef.value?.clearSorter(),
  scrollTo: (options: { left?: number; top?: number; behavior?: 'smooth' | 'auto' }) =>
    tableRef.value?.scrollTo(options)
})

// 监听数据变化,自动调整表格高度
watch(
  () => props.data,
  () => {
    if (tableConfig.value.maxHeight === undefined) {
      nextTick(() => {
        const windowHeight = window.innerHeight
        const tableTop = tableRef.value?.$el?.getBoundingClientRect().top || 0
        tableConfig.value.maxHeight = windowHeight - tableTop - 120 // 120为底部预留空间
      })
    }
  }
)

// 组件挂载时初始化表格高度
onMounted(() => {
  if (tableConfig.value.maxHeight === undefined) {
    const windowHeight = window.innerHeight
    const tableTop = tableRef.value?.$el?.getBoundingClientRect().top || 0
    tableConfig.value.maxHeight = windowHeight - tableTop - 120
  }
})

// 表格列
const displayColumns = computed(() => {
  return props.columns.filter((col: any) => col.show !== false)
})
</script>

<style scoped>
.ml-4 {
  margin-left: 1rem;
}
</style>
