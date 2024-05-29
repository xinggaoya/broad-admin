<template>
  <n-card>
    <template v-slot:header>
      <n-space>
        <slot name="header" />
      </n-space>
    </template>
    <template v-slot:header-extra>
      <n-space>
        <TableConfig @update="updateConfig" />
        <SortableTable class="ml-4" :columns="columns" />
      </n-space>
    </template>
    <template v-slot:default>
      <n-data-table
        :data="data"
        :bordered="tableConfig.border"
        :loading="loading"
        :striped="tableConfig.striped"
        :remote="remote"
        :columns="columns"
        :row-key="useRowKey"
        :pagination="pagination"
        :allow-checking-not-loaded="allowCheckingNotLoaded"
        :cascade="cascade"
        :on-load="onLoad"
        :scroll-x="scrollX"
      />
    </template>
  </n-card>
</template>

<script setup lang="ts">
import SortableTable from '@/components/table/main/SortableTable.vue'
import TableConfig from '@/components/table/main/TableConfig.vue'
import type { TableOperate } from '@/types/table'
import { ref, withDefaults } from 'vue'
import type { DataTableColumns } from 'naive-ui'

const props = withDefaults(defineProps<{
  loading: boolean
  remote: boolean
  columns: DataTableColumns | any
  data: Array<any>
  pagination: object | boolean
  onLoad: Function
  cascade: boolean
  allowCheckingNotLoaded: boolean
  rowKey: string
  scrollX: number | string
}>(), {
  loading: false,
  remote: true,
  columns: undefined,
  data: undefined,
  pagination: false,
  onLoad: () => {
  },
  cascade: true,
  allowCheckingNotLoaded: false,
  rowKey: '',
  scrollX: undefined
})
const tableConfig = ref<TableOperate>({
  border: false,
  striped: false
})

// 获取rowKey
function useRowKey(rowData: any) {
  if (!props.rowKey) {
    const firstKey = props.columns[0].key
    return rowData[firstKey]
  }
  return rowData[props.rowKey]
}

function updateConfig(config: TableOperate) {
  tableConfig.value = config
}
</script>

<style scoped lang="scss"></style>
