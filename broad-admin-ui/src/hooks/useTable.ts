import usePermissionStoreHook from '@/store/modules/permission'
import { h, reactive, ref, computed, readonly, type VNode } from 'vue'
import { NButton } from 'naive-ui'
import type { TableActionModel, TablePagination } from '@/types/table'
import DictTag from '@/components/tag/DictTag.vue'

/**
 * 渲染表格操作按钮
 * @param actions 操作按钮配置数组
 * @returns VNode 数组
 */
export const useRenderAction = (actions: TableActionModel[]): VNode[] => {
  const permissionStore = usePermissionStoreHook()

  return actions
    .filter((action) => {
      // 权限控制和显示控制
      if (action.hidden) return false
      if (action.auth && !permissionStore.hasPermissionExists(action.auth)) return false
      return true
    })
    .map((action) => {
      return h(
        NButton,
        {
          type: action.type || 'primary',
          size: 'small',
          ghost: true,
          disabled: action.disabled || false,
          onClick: action.onClick,
          style: { marginRight: '8px' }
        },
        {
          default: () => action.label
        }
      )
    })
}

/**
 * 渲染字典标签
 * @param options 字典选项
 * @param value 值
 * @returns VNode
 */
export const useRenderTag = (
  options: Record<string, any>[] | Record<string, any>,
  value: any
): VNode => {
  return h(DictTag, {
    options,
    value
  })
}

/**
 * 表格分页 Hook
 * @param callback 数据刷新回调函数
 * @returns 分页配置对象
 */
export const usePagination = (callback: () => void) => {
  const paginationInfo = reactive<
    TablePagination & {
      onChange: (page: number) => void
      onPageSizeChange: (pageSize: number) => void
      getPageInfo: (searchParams?: Record<string, any>) => Record<string, any>
      setTotalSize: (totalSize: number) => void
    }
  >({
    page: 1,
    pageSize: 10,
    total: 0,
    showSizePicker: true,
    showQuickJumper: true,
    pageSizes: [10, 20, 30, 50, 100],

    onChange(page: number) {
      paginationInfo.page = page
      callback()
    },

    onPageSizeChange(pageSize: number) {
      paginationInfo.pageSize = pageSize
      paginationInfo.page = 1
      callback()
    },

    getPageInfo(searchParams?: Record<string, any>) {
      const parameters: Record<string, any> = {
        pageNum: paginationInfo.page,
        pageSize: paginationInfo.pageSize
      }

      if (searchParams) {
        // 筛选值不为空的属性
        Object.keys(searchParams).forEach((key) => {
          const value = searchParams[key]
          if (value !== null && value !== undefined && value !== '') {
            parameters[key] = value
          }
        })
      }

      return parameters
    },

    setTotalSize(totalSize: number) {
      paginationInfo.total = totalSize
    }
  })

  return paginationInfo
}

/**
 * 渲染字典标签（简化版本）
 * @param options 字典选项
 * @param value 值
 * @returns VNode
 */
export const renderDictTag = (
  options: Record<string, any>[] | Record<string, any>,
  value: any
): VNode => {
  return useRenderTag(options, value)
}

/**
 * 表格加载状态 Hook
 * @param initialLoading 初始加载状态
 * @returns 加载状态管理对象
 */
export const useTableLoading = (initialLoading = false) => {
  const loading = ref(initialLoading)

  const setLoading = (value: boolean) => {
    loading.value = value
  }

  const withLoading = async <T>(fn: () => Promise<T>): Promise<T> => {
    try {
      setLoading(true)
      return await fn()
    } finally {
      setLoading(false)
    }
  }

  return {
    loading: readonly(loading),
    setLoading,
    withLoading
  }
}

/**
 * 表格选择 Hook
 * @returns 选择状态管理对象
 */
export const useTableSelection = () => {
  const selectedRowKeys = ref<Array<string | number>>([])

  const setSelectedRowKeys = (keys: Array<string | number>) => {
    selectedRowKeys.value = keys
  }

  const clearSelection = () => {
    selectedRowKeys.value = []
  }

  const isSelected = (key: string | number) => {
    return selectedRowKeys.value.includes(key)
  }

  const hasSelection = computed(() => selectedRowKeys.value.length > 0)

  return {
    selectedRowKeys: readonly(selectedRowKeys),
    setSelectedRowKeys,
    clearSelection,
    isSelected,
    hasSelection
  }
}
