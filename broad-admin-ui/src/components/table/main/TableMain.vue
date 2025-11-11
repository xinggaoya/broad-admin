<template>
  <div class="table-main-wrapper" :class="{ fullscreen: isFullscreen }">
    <!-- 表格主体 -->
    <div class="table-card-shell" ref="fullscreenContainerRef">
      <n-card class="table-main-container" :bordered="false">
        <!-- 表格头部 -->
        <template #header v-if="$slots.header || $slots['header-extra'] || toolbarVisible">
          <div class="table-header">
            <div class="header-left">
              <slot name="header" />
            </div>
            <div class="header-right">
              <slot name="header-extra" />
              <div
                v-if="toolbarVisible || $slots['toolbar-extra']"
                class="table-toolbar"
                :class="{ sticky: stickyToolbar }"
              >
                <n-space align="center" :wrap="false">
                  <slot v-if="$slots['toolbar-extra']" name="toolbar-extra" />
                  <div v-if="toolbarVisible" class="toolbar-system">
                    <!-- 刷新按钮 -->
                    <n-tooltip v-if="mergedToolbarConfig.refresh" trigger="hover" placement="top">
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
                    <n-popover
                      v-if="mergedToolbarConfig.density"
                      trigger="click"
                      placement="bottom-end"
                      :show-arrow="false"
                    >
                      <template #trigger>
                        <n-tooltip trigger="hover" placement="top">
                          <template #trigger>
                            <n-button strong circle type="tertiary" size="small">
                              <template #icon>
                                <n-icon>
                                  <DensityIcon />
                                </n-icon>
                              </template>
                            </n-button>
                          </template>
                          行高设置
                        </n-tooltip>
                      </template>
                      <n-space vertical size="small" class="density-selector">
                        <n-button
                          v-for="item in densityOptions"
                          :key="item.key"
                          quaternary
                          size="small"
                          :type="tableConfig.size === item.key ? 'primary' : 'default'"
                          @click="handleDensitySelect(item.key)"
                        >
                          {{ item.label }}
                        </n-button>
                      </n-space>
                    </n-popover>

                    <TableConfigPanel
                      v-if="mergedToolbarConfig.density"
                      ref="configPanelRef"
                      :config="tableConfig"
                      :config-key="configKey"
                      @update="updateConfig"
                    />

                    <!-- 列设置 -->
                    <SortableTable
                      v-if="mergedToolbarConfig.column"
                      ref="sortableTableRef"
                      :columns="internalColumns"
                      @update:columns="handleColumnsChange"
                    />

                    <!-- 全屏按钮 -->
                    <n-tooltip
                      v-if="mergedToolbarConfig.fullscreen"
                      trigger="hover"
                      placement="top"
                    >
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
                  </div>
                </n-space>
              </div>
            </div>
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
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import type { DataTableColumns, DataTableBaseColumn } from 'naive-ui'
import {
  Refresh as RefreshIcon,
  Expand as FullscreenIcon,
  Contract as FullscreenExitIcon,
  Resize as DensityIcon
} from '@vicons/ionicons5'
import type {
  TableConfig,
  TableMainProps,
  TableMainEmits,
  TableToolbarConfig
} from '@/types/table'
import SortableTable from './SortableTable.vue'
import TableConfigPanel from './TableConfig.vue'

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
  configKey: undefined,
  scrollX: undefined,
  childrenKey: 'children',
  indent: 16,
  expandable: true,
  defaultExpandAll: false,
  stickyToolbar: false,
  toolbarConfig: () => ({})
})

// Emits 定义
const emit = defineEmits<TableMainEmits>()

// 内部列状态管理
const internalColumns = ref<DataTableColumns>([])

// 表单引用
const tableRef = ref()
const configPanelRef = ref()
const sortableTableRef = ref()
const fullscreenContainerRef = ref<HTMLElement | null>(null)

// 响应式状态
const isFullscreen = ref(false)
const virtualScroll = ref(false)
const flexHeight = ref(false)
const hasWindow = typeof window !== 'undefined'
const stickyToolbar = computed(() => props.stickyToolbar)
const fallbackRowKeySymbol = Symbol('table-row-key')
let fallbackRowKeySequence = 0
let rowKeyWarningLogged = false

// 表格配置
const tableConfig = ref<TableConfig>({
  border: false,
  striped: false,
  size: 'medium',
  singleLine: true,
  maxHeight: undefined
})

const mergedToolbarConfig = computed<TableToolbarConfig>(() => ({
  refresh: true,
  density: true,
  column: true,
  fullscreen: true,
  ...props.toolbarConfig
}))

const toolbarVisible = computed(() =>
  Object.values(mergedToolbarConfig.value).some((item) => item !== false)
)

const densityOptions = [
  { label: '紧凑', key: 'small' as const },
  { label: '默认', key: 'medium' as const },
  { label: '宽松', key: 'large' as const }
]

// 计算属性
const getRowKey = computed(() => {
  if (typeof props.rowKey === 'function') {
    return props.rowKey
  }

  const rowKeyField = typeof props.rowKey === 'string' ? props.rowKey : undefined

  return (rowData: any) => {
    if (!rowData) {
      return undefined
    }

    if (rowKeyField && rowData[rowKeyField] !== undefined) {
      return rowData[rowKeyField]
    }

    const firstColumn = props.columns[0] as DataTableBaseColumn
    if (firstColumn?.key && rowData[firstColumn.key] !== undefined) {
      return rowData[firstColumn.key]
    }

    const fallbackKey =
      rowData.id ??
      rowData.key ??
      rowData.uid ??
      rowData.uuid ??
      rowData._id ??
      rowData.primaryKey ??
      rowData.code

    if (fallbackKey !== undefined && fallbackKey !== null) {
      return fallbackKey
    }

    if (!rowData[fallbackRowKeySymbol]) {
      rowData[fallbackRowKeySymbol] = `table-row-${fallbackRowKeySequence++}`
      if (!rowKeyWarningLogged) {
        console.warn(
          'TableMain: rowKey 未配置，请通过 rowKey 或 columns 第一列指定稳定主键，当前已使用内部 fallbackKey'
        )
        rowKeyWarningLogged = true
      }
    }

    return rowData[fallbackRowKeySymbol]
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
    const config = {
      showSizePicker: true,
      showQuickJumper: true,
      pageSizes: [10, 20, 50, 100],
      ...props.pagination
    }

    // 确保远程分页时有正确的字段映射
    if (props.remote) {
      return {
        ...config,
        page: config.page || 1,
        pageSize: config.pageSize || 10,
        itemCount: config.total || 0
      }
    }

    return config
  }

  const defaultConfig = {
    showSizePicker: true,
    showQuickJumper: true,
    pageSizes: [10, 20, 50, 100]
  }

  // 确保远程分页时有正确的字段映射
  if (props.remote) {
    return {
      ...defaultConfig,
      page: 1,
      pageSize: 10,
      itemCount: 0
    }
  }

  return defaultConfig
})

// 计算表格最大高度
const computedMaxHeight = computed(() => {
  if (tableConfig.value.maxHeight) {
    return tableConfig.value.maxHeight
  }

  if (isFullscreen.value) {
    return typeof window !== 'undefined' ? window.innerHeight - 120 : undefined
  }

  // 自动计算高度
  return undefined
})

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
  const payload = filters && typeof filters === 'object' ? { ...filters } : filters
  emit('update:filters', payload)
  emit('filters-change', payload)
}

const handleRefresh = () => {
  // 触发数据刷新
  emit('update:page', 1)
  emit('refresh')
}

const handleDensitySelect = (size: string | number) => {
  if (size === 'small' || size === 'medium' || size === 'large') {
    tableConfig.value.size = size
  }
}

// 全屏切换
const toggleFullscreen = () => {
  const target = fullscreenContainerRef.value
  if (!target) {
    return
  }

  if (!isFullscreen.value) {
    target.requestFullscreen?.()
  } else if (document.fullscreenElement) {
    document.exitFullscreen?.()
  }
}

// 监听全屏状态变化
const handleFullscreenChange = () => {
  const target = fullscreenContainerRef.value
  isFullscreen.value = target ? document.fullscreenElement === target : false
}

// 自动调整表格配置
let resizeRaf: number | null = null

const runAutoAdjust = () => {
  const needsVirtual = props.data.length > 1000
  if (virtualScroll.value !== needsVirtual) {
    virtualScroll.value = needsVirtual
  }

  if (!hasWindow) {
    if (flexHeight.value) {
      flexHeight.value = false
    }
    return
  }

  const container = tableRef.value?.$el as HTMLElement | undefined
  if (!container) {
    if (flexHeight.value) {
      flexHeight.value = false
    }
    return
  }
  const rect = container.getBoundingClientRect()
  const nextFlexHeight = rect.height > window.innerHeight * 0.8
  if (flexHeight.value !== nextFlexHeight) {
    flexHeight.value = nextFlexHeight
  }
}

const autoAdjustTable = () => {
  if (!hasWindow) {
    if (flexHeight.value) {
      flexHeight.value = false
    }
    return
  }
  if (resizeRaf !== null) {
    cancelAnimationFrame(resizeRaf)
  }
  resizeRaf = requestAnimationFrame(() => {
    runAutoAdjust()
    resizeRaf = null
  })
}

const handleWindowResize = () => {
  if (!hasWindow) {
    return
  }
  autoAdjustTable()
}

// 监听数据变化
watch(
  () => props.data,
  () => {
    autoAdjustTable()
  },
  { immediate: true }
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

// 生命周期
onMounted(() => {
  // 监听全屏事件
  document.addEventListener('fullscreenchange', handleFullscreenChange)

  if (hasWindow) {
    window.addEventListener('resize', handleWindowResize)
  }

  // 加载保存的配置
  configPanelRef.value?.loadSavedConfig?.()

  // 自动调整表格
  autoAdjustTable()
})

onUnmounted(() => {
  if (resizeRaf !== null) {
    cancelAnimationFrame(resizeRaf)
  }
  document.removeEventListener('fullscreenchange', handleFullscreenChange)
  if (hasWindow) {
    window.removeEventListener('resize', handleWindowResize)
  }
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

  // 全屏方法
  toggleFullscreen,

  // 获取表格实例
  getTableRef: () => tableRef.value
})
</script>

<style scoped lang="scss">
.table-main-wrapper {
  display: flex;
  flex-direction: column;
  gap: 16px;

  .table-card-shell {
    width: 100%;
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
        display: flex;
        flex-direction: column;
        gap: 8px;
      }
    }

    .table-toolbar {
      width: 100%;
      display: flex;
      justify-content: flex-end;

      &.sticky {
        position: sticky;
        top: 0;
        z-index: 2;
        background: var(--card-color);
        padding-top: 8px;
        margin-top: -8px;
      }

      .toolbar-system {
        display: flex;
        align-items: center;
        gap: 8px;

        .density-selector {
          min-width: 120px;
        }
      }
    }

    .table-content {
      position: relative;
      transition: all 0.3s ease;
      min-height: 400px;

      &.fullscreen {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: 9999;
        background: var(--card-color);
        padding: 16px;
        display: flex;
        flex-direction: column;

        :deep(.n-data-table) {
          flex: 1;
          min-height: 0;
        }
      }
    }

    .table-footer {
      padding-top: 16px;
      border-top: 1px solid var(--divider-color);
    }
  }

  // 响应式设计
  @media (max-width: 768px) {
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
.table-main-wrapper.fullscreen {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9998;
  background: var(--body-color);
  padding: 0;

  .table-card-shell {
    width: 100%;
    height: 100%;
  }

  .table-main-container {
    height: 100%;
    box-shadow: none;
    border-radius: 0;
  }

  .table-content {
    height: calc(100vh - 80px); // 减去工具栏高度
    padding: 16px;
    overflow: auto;
  }
}
</style>