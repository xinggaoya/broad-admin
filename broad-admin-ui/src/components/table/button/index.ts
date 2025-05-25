// 表格按钮组件统一导出
export { default as AddButton } from './AddButton.vue'
export { default as EditButton } from './EditButton.vue'
export { default as DeleteButton } from './DeleteButton.vue'
export { default as SearchButton } from './SearchButton.vue'
export { default as ResetButton } from './ResetButton.vue'
export { default as ExportButton } from './ExportButton.vue'

// 按钮组件类型定义
export interface ButtonProps {
  title?: string
  disabled?: boolean
  loading?: boolean
  type?: 'default' | 'tertiary' | 'primary' | 'info' | 'success' | 'warning' | 'error'
  size?: 'tiny' | 'small' | 'medium' | 'large'
  ghost?: boolean
  round?: boolean
  circle?: boolean
  quaternary?: boolean
  dashed?: boolean
}

export interface DeleteButtonProps extends ButtonProps {
  confirmContent?: string
  confirmText?: string
  cancelText?: string
  needConfirm?: boolean
}

export interface ExportButtonProps extends ButtonProps {
  filename?: string
  fileType?: 'csv' | 'xlsx' | 'pdf'
}
