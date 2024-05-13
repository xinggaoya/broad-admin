// 表格操作栏
export interface TableOperate {
  border: boolean
  striped: boolean
}

export interface TableActionModel {
  label: string
  type: 'default' | 'warning' | 'primary' | 'success' | 'error' | 'info'
  disabled?: boolean
  hidden?: boolean
  auth?: Array<string> | string
  onClick: () => void
}
