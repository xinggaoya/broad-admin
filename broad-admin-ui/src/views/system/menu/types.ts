// API响应类型
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
}

// 菜单数据类型
export interface MenuData {
  menuId: string | number
  parentId: string | number
  routeName: string | null
  menuName: string
  menuUrl: string | null
  menuType: 0 | 1 | 2 // 0: 目录, 1: 菜单, 2: 按钮
  iframeUrl: string | null
  status: string | null
  perme: string | null
  icon: string | null
  iconPrefix: string | null
  localFilePath: string | null
  orderNum: number
  cacheable: string | number
  hidden: string | number
  affix: string | number
  remark: string | null
  createTime: string | null
  updateTime: string | null
  children: MenuData[] | null
  isLeaf: boolean
}

// 表格数据类型
export interface TableData {
  list: MenuData[]
  loading: boolean
}

// 表单对话框类型
export interface FormDialog {
  visible: boolean
  title: string
  submitLoading: boolean
  mode: 'add' | 'edit'
}

// 菜单树节点类型
export interface MenuTreeNode {
  label: string
  key: number | string
  children?: MenuTreeNode[]
}
