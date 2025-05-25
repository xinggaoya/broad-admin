import { ref, reactive, computed, watch, nextTick } from 'vue'
import type { DataTableColumns } from 'naive-ui'
import type { SearchConfig, SearchFormConfig, TablePagination } from '@/types/table'

export interface UseTableWithSearchOptions {
  // 表格配置
  columns: DataTableColumns
  rowKey?: string | ((row: any) => string | number)

  // 搜索配置
  searchConfig?: SearchConfig
  searchForm?: SearchFormConfig
  initialSearchModel?: Record<string, any>

  // 分页配置
  pagination?: Partial<TablePagination>

  // 数据加载函数
  loadData: (params: {
    page: number
    pageSize: number
    searchParams: Record<string, any>
    sorter?: any
  }) => Promise<{
    data: any[]
    total: number
  }>

  // 其他配置
  immediate?: boolean
}

export function useTableWithSearch(options: UseTableWithSearchOptions) {
  const {
    columns,
    rowKey = 'id',
    searchConfig = {},
    searchForm,
    initialSearchModel = {},
    pagination: paginationOptions = {},
    loadData,
    immediate = true
  } = options

  // 响应式状态
  const loading = ref(false)
  const tableData = ref<any[]>([])
  const checkedRowKeys = ref<Array<string | number>>([])

  // 搜索模型
  const searchModel = reactive({ ...initialSearchModel })

  // 分页配置
  const pagination = reactive({
    page: 1,
    pageSize: 10,
    total: 0,
    showSizePicker: true,
    showQuickJumper: true,
    pageSizes: [10, 20, 50, 100],
    ...paginationOptions
  })

  // 排序状态
  const sorter = ref<any>(null)

  // 默认搜索配置
  const defaultSearchConfig: SearchConfig = {
    show: true,
    title: '搜索条件',
    defaultCollapse: true,
    resetOnSearch: true,
    autoSearch: true,
    ...searchConfig
  }

  // 加载数据
  const refresh = async (resetPage = false) => {
    if (resetPage) {
      pagination.page = 1
    }

    loading.value = true
    try {
      const result = await loadData({
        page: pagination.page,
        pageSize: pagination.pageSize,
        searchParams: { ...searchModel },
        sorter: sorter.value
      })

      tableData.value = result.data
      pagination.total = result.total
    } catch (error) {
      console.error('加载数据失败:', error)
      tableData.value = []
      pagination.total = 0
    } finally {
      loading.value = false
    }
  }

  // 搜索处理
  const handleSearch = (params?: Record<string, any>) => {
    if (params) {
      Object.assign(searchModel, params)
    }
    refresh(true)
  }

  // 重置搜索
  const handleReset = () => {
    // 重置搜索模型
    Object.keys(searchModel).forEach((key) => {
      const item = searchForm?.items.find((item) => item.key === key)
      searchModel[key] = item?.defaultValue ?? null
    })

    // 重置排序
    sorter.value = null

    // 如果配置了自动搜索，则触发搜索
    if (defaultSearchConfig.autoSearch !== false) {
      nextTick(() => {
        refresh(true)
      })
    }
  }

  // 分页处理
  const handlePageChange = (page: number) => {
    pagination.page = page
    refresh()
  }

  const handlePageSizeChange = (pageSize: number) => {
    pagination.pageSize = pageSize
    pagination.page = 1
    refresh()
  }

  // 排序处理
  const handleSorterChange = (newSorter: any) => {
    sorter.value = newSorter
    refresh()
  }

  // 选择处理
  const handleCheckedRowKeysChange = (keys: Array<string | number>) => {
    checkedRowKeys.value = keys
  }

  // 计算属性
  const hasSelected = computed(() => checkedRowKeys.value.length > 0)
  const selectedCount = computed(() => checkedRowKeys.value.length)

  // 工具方法
  const clearSelection = () => {
    checkedRowKeys.value = []
  }

  const selectAll = () => {
    checkedRowKeys.value = tableData.value.map((row) => {
      if (typeof rowKey === 'function') {
        return rowKey(row)
      }
      return row[rowKey as string]
    })
  }

  const getSelectedRows = () => {
    return tableData.value.filter((row) => {
      const key = typeof rowKey === 'function' ? rowKey(row) : row[rowKey as string]
      return checkedRowKeys.value.includes(key)
    })
  }

  // 监听搜索模型变化（防抖）
  let searchTimer: number | null = null
  watch(
    searchModel,
    () => {
      if (defaultSearchConfig.autoSearch && defaultSearchConfig.searchDelay) {
        if (searchTimer) {
          clearTimeout(searchTimer)
        }
        searchTimer = setTimeout(() => {
          handleSearch()
        }, defaultSearchConfig.searchDelay)
      }
    },
    { deep: true }
  )

  // 初始化加载
  if (immediate) {
    nextTick(() => {
      refresh()
    })
  }

  return {
    // 状态
    loading,
    tableData,
    checkedRowKeys,
    searchModel,
    pagination,
    sorter,

    // 配置
    columns,
    rowKey,
    searchConfig: defaultSearchConfig,
    searchForm,

    // 方法
    refresh,
    handleSearch,
    handleReset,
    handlePageChange,
    handlePageSizeChange,
    handleSorterChange,
    handleCheckedRowKeysChange,

    // 工具方法
    clearSelection,
    selectAll,
    getSelectedRows,

    // 计算属性
    hasSelected,
    selectedCount
  }
}

// 简化版本，用于基础表格
export function useSimpleTable(
  loadDataFn: (page: number, pageSize: number) => Promise<{ data: any[]; total: number }>,
  options: {
    immediate?: boolean
    pageSize?: number
  } = {}
) {
  const { immediate = true, pageSize = 10 } = options

  const loading = ref(false)
  const tableData = ref<any[]>([])
  const pagination = reactive({
    page: 1,
    pageSize,
    total: 0,
    showSizePicker: true,
    showQuickJumper: true,
    pageSizes: [10, 20, 50, 100]
  })

  const refresh = async (resetPage = false) => {
    if (resetPage) {
      pagination.page = 1
    }

    loading.value = true
    try {
      const result = await loadDataFn(pagination.page, pagination.pageSize)
      tableData.value = result.data
      pagination.total = result.total
    } catch (error) {
      console.error('加载数据失败:', error)
      tableData.value = []
      pagination.total = 0
    } finally {
      loading.value = false
    }
  }

  const handlePageChange = (page: number) => {
    pagination.page = page
    refresh()
  }

  const handlePageSizeChange = (newPageSize: number) => {
    pagination.pageSize = newPageSize
    pagination.page = 1
    refresh()
  }

  if (immediate) {
    nextTick(() => {
      refresh()
    })
  }

  return {
    loading,
    tableData,
    pagination,
    refresh,
    handlePageChange,
    handlePageSizeChange
  }
}
