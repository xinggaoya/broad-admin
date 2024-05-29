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
        :row-key="useRowKey(rowKey)"
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
import { ref } from 'vue'

defineProps({
  loading: {
    type: Boolean,
    default: false
  },
  remote: {
    type: Boolean,
    default: true
  },
  columns: {
    type: Array,
    default: () => []
  },
  data: {
    type: Array,
    default: () => []
  },
  pagination: {
    type: Object,
    default: () => ({})
  },
  onLoad: {
    type: Function,
    default: undefined
  },
  cascade: {
    type: Boolean,
    default: true
  },
  allowCheckingNotLoaded: {
    type: Boolean,
    default: false
  },
  rowKey: {
    type: String,
    default: undefined
  },
  scrollX: {
    type: Number || String,
    default: undefined
  }
})

const tableConfig = ref<TableOperate>({
  border: false,
  striped: false
})

// 获取rowKey
const useRowKey = function (propName: string) {
  return function (rowData: any) {
    return rowData[propName]
  }
}
function updateConfig(config: TableOperate) {
  tableConfig.value = config
}
</script>

<style scoped lang="scss"></style>
