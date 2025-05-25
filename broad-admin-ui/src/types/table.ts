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

// 搜索配置接口
export interface SearchConfig {
  show?: boolean
  title?: string
  defaultCollapse?: boolean
  loading?: boolean
  resetOnSearch?: boolean
  autoSearch?: boolean
  searchDelay?: number
}

// 搜索表单项接口
export interface SearchFormItem {
  key: string
  label: string
  type: 'input' | 'select' | 'date' | 'daterange' | 'number' | 'switch' | 'custom'
  placeholder?: string
  options?: Array<{ label: string; value: any }>
  defaultValue?: any
  span?: number
  props?: Record<string, any>
  render?: () => any
}

// 搜索表单配置
export interface SearchFormConfig {
  items: SearchFormItem[]
  labelWidth?: string | number
  labelPlacement?: 'left' | 'top'
  size?: 'small' | 'medium' | 'large'
  showFeedback?: boolean
  cols?: number
  xGap?: number
  yGap?: number
}

// 表格操作按钮模型
export interface TableActionModel {
  label: string
  type?: 'default' | 'primary' | 'info' | 'success' | 'warning' | 'error'
  auth?: string
  hidden?: boolean
  disabled?: boolean
  onClick: () => void
}

// 内部列类型
export interface InnerColumn {
  checked: boolean
  key: string | number
  title: string
  type?: 'default' | 'selection' | 'expand'
  [key: string]: any
}

// 表格主组件 Props
export interface TableMainProps {
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
  // 搜索相关配置
  searchConfig?: SearchConfig
  searchForm?: SearchFormConfig
  searchModel?: Record<string, any>
}

// 表格主组件 Emits
export interface TableMainEmits {
  (e: 'update:checked-row-keys', keys: Array<string | number>): void
  (e: 'update:page', page: number): void
  (e: 'update:page-size', pageSize: number): void
  (e: 'update:sorter', sorter: TableSorter): void
  (e: 'update:columns', columns: DataTableColumns): void
  (e: 'update:expanded-row-keys', keys: Array<string | number>): void
  // 搜索相关事件
  (e: 'update:search-model', model: Record<string, any>): void
  (e: 'search', params: Record<string, any>): void
  (e: 'reset'): void
  (e: 'update:search-collapse', value: boolean): void
}
