<template>
  <div class="table-main-wrapper">
    <!-- 搜索区域 -->
    <n-card v-if="searchConfig?.show !== false && searchForm" size="small" class="search-card">
      <template #header>
        <n-space align="center" justify="space-between">
          <div class="search-title">
            <slot name="search-title">{{ searchConfig?.title || '搜索' }}</slot>
          </div>
          <n-space>
            <SearchButton @search="handleSearch" :loading="searchConfig?.loading || loading" />
            <ResetButton @reset="handleReset" />
            <n-button type="tertiary" ghost @click="handleSearchCollapse" size="small">
              <template #icon>
                <n-icon>
                  <ArrowUpIcon v-if="searchCollapse" />
                  <ArrowDownIcon v-else />
                </n-icon>
              </template>
              {{ !searchCollapse ? '展开' : '收起' }}
            </n-button>
          </n-space>
        </n-space>
      </template>
      <template #default>
        <n-collapse-transition :show="searchCollapse">
          <div class="search-form-container">
            <slot name="search-form">
              <SearchForm
                v-if="searchForm"
                ref="searchFormRef"
                :config="searchForm"
                v-model="internalSearchModel"
              />
            </slot>
          </div>
        </n-collapse-transition>
      </template>
    </n-card>

    <!-- 表格主体 -->
    <n-card class="table-main-container">
      <!-- 表格头部 -->
      <template #header>
        <div class="table-header">
          <div class="header-left">
            <slot name="header" />
          </div>
          <div class="header-right">
            <slot name="header-extra" />
          </div>
        </div>
      </template>

      <!-- 表格工具栏 -->
      <template #header-extra>
        <div class="table-toolbar">
          <n-space>
            <!-- 刷新按钮 -->
            <n-tooltip trigger="hover" placement="top">
              <template #trigger>
                <n-button
                  type="tertiary"
                  size="small"
                  circle
                  :loading="loading"
                  @click="handleRefresh"
                >
                  <template #icon>
                    <n-icon>
                      <RefreshIcon />
                    </n-icon>
                  </template>
                </n-button>
              </template>
              刷新数据
            </n-tooltip>

            <!-- 密度设置 -->
            <TableConfigPanel ref="configPanelRef" :config="tableConfig" @update="updateConfig" />

            <!-- 列设置 -->
            <SortableTable
              ref="sortableTableRef"
              :columns="internalColumns"
              @update:columns="handleColumnsChange"
            />

            <!-- 全屏按钮 -->
            <n-tooltip trigger="hover" placement="top">
              <template #trigger>
                <n-button type="tertiary" size="small" circle @click="toggleFullscreen">
                  <template #icon>
                    <n-icon>
                      <FullscreenIcon v-if="!isFullscreen" />
                      <FullscreenExitIcon v-else />
                    </n-icon>
                  </template>
                </n-button>
              </template>
              {{ isFullscreen ? '退出全屏' : '全屏显示' }}
            </n-tooltip>
          </n-space>
        </div>
      </template>

      <!-- 表格内容 -->
      <template #default>
        <div class="table-content" :class="{ fullscreen: isFullscreen }">
          <n-data-table
            ref="tableRef"
            :data="data"
            :bordered="tableConfig.border"
            :loading="loading"
            :striped="tableConfig.striped"
            :remote="remote"
            :columns="displayColumns"
            :row-key="getRowKey"
            :pagination="paginationConfig"
            :allow-checking-not-loaded="allowCheckingNotLoaded"
            :cascade="cascade"
            :scroll-x="scrollX"
            :size="tableConfig.size"
            :single-line="tableConfig.singleLine"
            :max-height="computedMaxHeight"
            :children-key="childrenKey"
            :indent="indent"
            :expandable="expandable"
            :default-expand-all="defaultExpandAll"
            :virtual-scroll="virtualScroll"
            :flex-height="flexHeight"
            @load="onLoad"
            @update:checked-row-keys="handleCheckedRowKeysChange"
            @update:page="handlePageChange"
            @update:page-size="handlePageSizeChange"
            @update:sorter="handleSorterChange"
            @update:expanded-row-keys="handleExpandedRowKeysChange"
            @update:filters="handleFiltersChange"
          />
        </div>
      </template>

      <!-- 表格底部 -->
      <template #footer v-if="$slots.footer">
        <div class="table-footer">
          <slot name="footer" />
        </div>
      </template>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch, nextTick } from 'vue'
import type { DataTableColumns, DataTableBaseColumn } from 'naive-ui'
import {
  Refresh as RefreshIcon,
  Expand as FullscreenIcon,
  Contract as FullscreenExitIcon,
  ChevronUp as ArrowUpIcon,
  ChevronDown as ArrowDownIcon
} from '@vicons/ionicons5'
import type { TableConfig, TableMainProps, TableMainEmits } from '@/types/table'
import SortableTable from './SortableTable.vue'
import TableConfigPanel from './TableConfig.vue'
import SearchForm from '../search/SearchForm.vue'
import SearchButton from '../button/SearchButton.vue'
import ResetButton from '../button/ResetButton.vue'

// Props 定义
const props = withDefaults(defineProps<TableMainProps>(), {
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
  defaultExpandAll: false,
  searchModel: () => ({})
})

// Emits 定义
const emit = defineEmits<TableMainEmits>()

// 内部列状态管理
const internalColumns = ref<DataTableColumns>([])

// 搜索相关状态
const searchFormRef = ref()
const internalSearchModel = ref<Record<string, any>>({})
const searchCollapse = ref(props.searchConfig?.defaultCollapse !== false)

// 初始化搜索模型
const initSearchModel = () => {
  if (props.searchForm && props.searchForm.items) {
    const model: Record<string, any> = {}
    props.searchForm.items.forEach((item) => {
      model[item.key] = props.searchModel[item.key] ?? item.defaultValue ?? null
    })
    internalSearchModel.value = { ...model, ...props.searchModel }
  } else {
    internalSearchModel.value = { ...props.searchModel }
  }
}

// 监听搜索模型变化
watch(
  () => props.searchModel,
  (newModel) => {
    if (newModel && typeof newModel === 'object') {
      internalSearchModel.value = { ...internalSearchModel.value, ...newModel }
    }
  },
  { deep: true }
)

// 监听内部搜索模型变化
watch(
  internalSearchModel,
  (newModel) => {
    emit('update:search-model', { ...newModel })
  },
  { deep: true }
)

// 监听搜索表单配置变化
watch(
  () => props.searchForm,
  () => {
    initSearchModel()
  },
  { immediate: true, deep: true }
)

// 初始化内部列
const initInternalColumns = () => {
  internalColumns.value = [...props.columns]
}

// 监听 props.columns 变化
watch(
  () => props.columns,
  (newColumns) => {
    if (newColumns && newColumns.length > 0) {
      internalColumns.value = [...newColumns]
    }
  },
  { immediate: true, deep: true }
)

// 组件引用
const tableRef = ref()
const configPanelRef = ref()
const sortableTableRef = ref()

// 响应式状态
const isFullscreen = ref(false)
const virtualScroll = ref(false)
const flexHeight = ref(false)

// 表格配置
const tableConfig = ref<TableConfig>({
  border: false,
  striped: false,
  size: 'medium',
  singleLine: true,
  maxHeight: undefined
})

// 计算属性
const getRowKey = computed(() => {
  if (typeof props.rowKey === 'function') {
    return props.rowKey
  }

  return (rowData: any) => {
    if (props.rowKey && typeof props.rowKey === 'string') {
      return rowData[props.rowKey]
    }

    // 自动推断 rowKey
    const firstColumn = props.columns[0] as DataTableBaseColumn
    if (firstColumn?.key) {
      return rowData[firstColumn.key]
    }

    // 默认使用 id 字段
    return rowData.id || rowData.key || Math.random()
  }
})

// 显示的列
const displayColumns = computed(() => {
  // 使用内部列状态，支持拖拽排序
  return internalColumns.value.filter((col: any) => col.show !== false)
})

// 分页配置
const paginationConfig = computed(() => {
  if (props.pagination === false) {
    return false
  }

  if (typeof props.pagination === 'object' && props.pagination !== null) {
    return {
      showSizePicker: true,
      showQuickJumper: true,
      pageSizes: [10, 20, 50, 100],
      ...props.pagination
    }
  }

  return {
    showSizePicker: true,
    showQuickJumper: true,
    pageSizes: [10, 20, 50, 100]
  }
})

// 计算表格最大高度
const computedMaxHeight = computed(() => {
  if (tableConfig.value.maxHeight) {
    return tableConfig.value.maxHeight
  }

  if (isFullscreen.value) {
    return window.innerHeight - 200
  }

  // 自动计算高度
  return undefined
})

// 搜索相关方法
const handleSearch = () => {
  const searchParams = searchFormRef.value?.getFormData() || internalSearchModel.value
  emit('search', searchParams)

  // 如果配置了重置页码，则重置到第一页
  if (props.searchConfig?.resetOnSearch !== false) {
    emit('update:page', 1)
  }
}

const handleReset = () => {
  // 重置搜索表单
  if (searchFormRef.value) {
    searchFormRef.value.reset()
  } else {
    // 如果没有表单组件，则重置内部模型
    const model: Record<string, any> = {}
    if (props.searchForm?.items) {
      props.searchForm.items.forEach((item) => {
        model[item.key] = item.defaultValue ?? null
      })
    }
    internalSearchModel.value = model
  }

  emit('reset')

  // 如果配置了自动搜索，则触发搜索
  if (props.searchConfig?.autoSearch !== false) {
    nextTick(() => {
      handleSearch()
    })
  }
}

const handleSearchCollapse = () => {
  searchCollapse.value = !searchCollapse.value
  emit('update:search-collapse', searchCollapse.value)
}

// 表格相关方法
const updateConfig = (config: TableConfig) => {
  tableConfig.value = { ...tableConfig.value, ...config }
}

const handleCheckedRowKeysChange = (keys: Array<string | number>) => {
  emit('update:checked-row-keys', keys)
}

const handlePageChange = (page: number) => {
  emit('update:page', page)
}

const handlePageSizeChange = (pageSize: number) => {
  emit('update:page-size', pageSize)
}

const handleSorterChange = (sorter: any) => {
  emit('update:sorter', sorter)
}

const handleColumnsChange = (columns: DataTableColumns) => {
  // 更新内部列状态
  internalColumns.value = [...columns]
  // 同时触发事件，让父组件知道列已更新（可选）
  emit('update:columns', columns)
}

const handleExpandedRowKeysChange = (keys: Array<string | number>) => {
  emit('update:expanded-row-keys', keys)
}

const handleFiltersChange = (filters: any) => {
  // 可以添加过滤器变化的处理逻辑
  console.log('Filters changed:', filters)
}

const handleRefresh = () => {
  // 触发数据刷新
  emit('update:page', 1)
}

// 全屏切换
const toggleFullscreen = () => {
  isFullscreen.value = !isFullscreen.value

  if (isFullscreen.value) {
    document.documentElement.requestFullscreen?.()
  } else {
    document.exitFullscreen?.()
  }
}

// 监听全屏状态变化
const handleFullscreenChange = () => {
  isFullscreen.value = !!document.fullscreenElement
}

// 自动调整表格配置
const autoAdjustTable = () => {
  nextTick(() => {
    // 根据数据量自动启用虚拟滚动
    if (props.data.length > 1000) {
      virtualScroll.value = true
    }

    // 根据容器高度自动调整
    const container = tableRef.value?.$el
    if (container) {
      const rect = container.getBoundingClientRect()
      if (rect.height > window.innerHeight * 0.8) {
        flexHeight.value = true
      }
    }
  })
}

// 监听数据变化
watch(
  () => props.data,
  () => {
    autoAdjustTable()
  },
  { immediate: true }
)

// 生命周期
onMounted(() => {
  // 监听全屏事件
  document.addEventListener('fullscreenchange', handleFullscreenChange)

  // 加载保存的配置
  configPanelRef.value?.loadSavedConfig?.()

  // 自动调整表格
  autoAdjustTable()

  // 初始化搜索模型
  initSearchModel()
})

onUnmounted(() => {
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
})

// 暴露方法给父组件
defineExpose({
  // 表格方法
  clearChecked: () => tableRef.value?.clearChecked(),
  clearFilters: () => tableRef.value?.clearFilters(),
  clearSorter: () => tableRef.value?.clearSorter(),
  scrollTo: (options: { left?: number; top?: number; behavior?: 'smooth' | 'auto' }) =>
    tableRef.value?.scrollTo(options),

  // 配置方法
  resetConfig: () => configPanelRef.value?.reset(),
  saveConfig: () => configPanelRef.value?.save(),
  resetColumns: () => sortableTableRef.value?.reset(),
  saveColumns: () => sortableTableRef.value?.save(),

  // 搜索方法
  search: handleSearch,
  resetSearch: handleReset,
  getSearchData: () => searchFormRef.value?.getFormData() || internalSearchModel.value,
  validateSearch: () => searchFormRef.value?.validate(),

  // 全屏方法
  toggleFullscreen,

  // 获取表格实例
  getTableRef: () => tableRef.value,
  getSearchFormRef: () => searchFormRef.value
})
</script>

<style scoped lang="scss">
.table-main-wrapper {
  display: flex;
  flex-direction: column;
  gap: 16px;

  .search-card {
    :deep(.n-card__content) {
      padding: 0;
    }

    .search-title {
      font-size: 14px;
      font-weight: 500;
      color: var(--text-color);
    }

    .search-form-container {
      padding: 16px;
      background-color: var(--card-color);
      border-radius: 0 0 3px 3px;
    }
  }

  .table-main-container {
    position: relative;
    flex: 1;

    .table-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      min-height: 32px;

      .header-left {
        flex: 1;
        min-width: 0;
      }

      .header-right {
        flex-shrink: 0;
        margin: 0 16px;
      }
    }

    .table-toolbar {
      display: flex;
      align-items: center;
      gap: 8px;
    }

    .table-content {
      position: relative;
      transition: all 0.3s ease;

      &.fullscreen {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: 9999;
        background: var(--card-color);
        padding: 16px;
      }
    }

    .table-footer {
      padding-top: 16px;
      border-top: 1px solid var(--divider-color);
    }
  }

  // 响应式设计
  @media (max-width: 768px) {
    .search-card {
      .search-form-container {
        padding: 12px;
      }
    }

    .table-main-container {
      .table-header {
        flex-direction: column;
        align-items: flex-start;
        gap: 12px;

        .header-right {
          margin-left: 0;
          width: 100%;
        }
      }

      .table-toolbar {
        justify-content: flex-end;
        width: 100%;
      }
    }
  }
}

// 全屏时的样式调整
:global(.table-main-wrapper.fullscreen) {
  .table-main-container {
    .n-card__content {
      height: calc(100vh - 120px);
      overflow: auto;
    }
  }
}
</style>
