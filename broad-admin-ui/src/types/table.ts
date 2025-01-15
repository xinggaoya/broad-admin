import type { DataTableColumn, DataTableColumns } from 'naive-ui'

export interface TableConfig {
  border: boolean
  striped: boolean
  size?: 'small' | 'medium' | 'large'
  singleLine?: boolean
  maxHeight?: number
}

export interface ColumnState {
  key: string | number
  title: string
  visible: boolean
  order: number
}

export type TableColumn = DataTableColumn
export type TableColumns = DataTableColumns

export interface TableSorter {
  columnKey: string
  order: 'ascend' | 'descend' | false
}

export interface TablePagination {
  page: number
  pageSize: number
  total: number
  pageSizes?: number[]
  showSizePicker?: boolean
  showQuickJumper?: boolean
}
