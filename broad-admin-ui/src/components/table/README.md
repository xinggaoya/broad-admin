# 表格组件使用指南

## 📋 概述

本项目的表格组件已经过重构，采用**单一职责原则**，将核心表格功能与搜索功能分离，提供更专业、更灵活的表格解决方案。

## 🏗️ 组件架构

### 核心组件

- **TableMain** - 专业的表格组件（仅处理表格核心功能）
- **TableSearchCard** - 独立的搜索卡片组件
- **SortableTable** - 列配置组件
- **TableConfigPanel** - 表格配置面板

### 按钮组件

- **AddButton** - 新增按钮
- **EditButton** - 编辑按钮
- **DeleteButton** - 删除按钮
- **ExportButton** - 导出按钮
- **SearchButton** - 搜索按钮
- **ResetButton** - 重置按钮

## 🚀 快速开始

### 基础表格用法

```vue
<template>
  <div class="page">
    <!-- 页面头部 -->
    <header class="page-header">
      <h2>用户管理</h2>
      <div class="header-actions">
        <AddButton @add="handleAdd" />
      </div>
    </header>

    <!-- 搜索区域（可选） -->
    <TableSearchCard
      v-model="searchModel"
      :fields="searchFields"
      :loading="tableLoading"
      @search="handleSearch"
      @reset="handleReset"
    />

    <!-- 表格主体 -->
    <n-card class="table-panel">
      <TableMain
        :data="tableData"
        :columns="tableColumns"
        :loading="tableLoading"
        :pagination="pagination"
        row-key="id"
        @update:checked-row-keys="handleSelectionChange"
        @update:page="handlePageChange"
        @update:page-size="handlePageSizeChange"
        @refresh="handleRefresh"
      />
    </n-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import TableMain from '@/components/table/main/TableMain.vue'
import TableSearchCard from '@/components/table/search/TableSearchCard.vue'
import { usePagination } from '@/hooks/useTable'

// 搜索配置
const searchModel = ref({
  name: '',
  status: null,
  createTime: null
})

const searchFields = [
  { key: 'name', label: '用户名', type: 'input' },
  { key: 'status', label: '状态', type: 'select', options: [
    { label: '启用', value: '0' },
    { label: '停用', value: '1' }
  ]},
  { key: 'createTime', label: '创建时间', type: 'daterange' }
]

// 表格数据
const tableData = ref([])
const tableLoading = ref(false)
const selectedRows = ref([])

// 表格列配置
const tableColumns = ref([
  { type: 'selection' },
  { title: 'ID', key: 'id', width: 80 },
  { title: '用户名', key: 'name', width: 120 },
  { title: '邮箱', key: 'email', width: 200 },
  { title: '状态', key: 'status', width: 100 },
  { title: '创建时间', key: 'createTime', width: 180 },
  {
    title: '操作',
    key: 'actions',
    width: 200,
    fixed: 'right',
    render: (row) => {
      // 自定义操作按钮渲染
    }
  }
])

// 分页配置
const pagination = usePagination(doRefresh)

// 搜索处理
function handleSearch(params) {
  Object.assign(searchModel.value, params)
  pagination.page = 1
  doRefresh()
}

function handleReset() {
  searchModel.value = {
    name: '',
    status: null,
    createTime: null
  }
  pagination.page = 1
  doRefresh()
}

// 数据加载
function doRefresh() {
  tableLoading.value = true
  getUserList(pagination.getPageInfo(searchModel.value))
    .then(res => {
      tableData.value = res.rows
      pagination.setTotalSize(res.total)
    })
    .finally(() => {
      tableLoading.value = false
    })
}

// 事件处理
function handleSelectionChange(keys) {
  selectedRows.value = tableData.value.filter(item => keys.includes(item.id))
}

function handlePageChange(page) {
  pagination.page = page
  doRefresh()
}

function handlePageSizeChange(pageSize) {
  pagination.pageSize = pageSize
  pagination.page = 1
  doRefresh()
}

function handleRefresh() {
  doRefresh()
}

onMounted(() => {
  doRefresh()
})
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: var(--card-color);
  border-radius: 8px;
  box-shadow: var(--box-shadow);
}

.table-panel {
  border-radius: 8px;
  box-shadow: var(--box-shadow);
}
</style>
```

## 📝 TableSearchCard 组件

### 基础用法

```vue
<TableSearchCard
  v-model="searchModel"
  title="高级搜索"
  :fields="searchFields"
  :loading="loading"
  @search="handleSearch"
  @reset="handleReset"
/>
```

### Props

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| modelValue | `Record<string, any>` | `{}` | 搜索表单数据（v-model） |
| title | `string` | `'搜索条件'` | 搜索卡片标题 |
| fields | `SearchField[]` | `[]` | 搜索字段配置 |
| loading | `boolean` | `false` | 搜索加载状态 |
| collapsible | `boolean` | `false` | 是否可折叠 |
| defaultExpanded | `boolean` | `true` | 默认展开状态 |
| labelWidth | `string \| number` | `80` | 标签宽度 |
| labelPlacement | `'left' \| 'top'` | `'left'` | 标签位置 |
| size | `'small' \| 'medium' \| 'large'` | `'medium'` | 表单尺寸 |

### SearchField 接口

```typescript
interface SearchField {
  key: string              // 字段名
  label: string            // 字段标签
  type: 'input' | 'select' | 'date' | 'daterange' | 'custom'
  placeholder?: string     // 占位符
  options?: Array<{ label: string; value: any }>  // 选项（select类型）
  defaultValue?: any       // 默认值
}
```

### 字段类型

#### Input 输入框
```javascript
{ key: 'name', label: '用户名', type: 'input', placeholder: '请输入用户名' }
```

#### Select 选择器
```javascript
{
  key: 'status',
  label: '状态',
  type: 'select',
  options: [
    { label: '启用', value: '0' },
    { label: '停用', value: '1' }
  ]
}
```

#### Date 日期选择
```javascript
{ key: 'createTime', label: '创建时间', type: 'date' }
```

#### DateRange 日期范围
```javascript
{ key: 'dateRange', label: '日期范围', type: 'daterange' }
```

#### Custom 自定义
```javascript
{ key: 'custom', label: '自定义', type: 'custom' }
```

在模板中使用自定义插槽：
```vue
<TableSearchCard v-model="searchModel" :fields="fields">
  <template #field-custom="{ field }">
    <n-input-number v-model:value="searchModel.custom" placeholder="请输入数字" />
  </template>
</TableSearchCard>
```

## ⚙️ TableMain 组件

### Props

| 属性 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| data | `Array<any>` | `[]` | 表格数据 |
| columns | `DataTableColumns` | `[]` | 表格列配置 |
| loading | `boolean` | `false` | 加载状态 |
| pagination | `object \| boolean` | `false` | 分页配置 |
| remote | `boolean` | `true` | 是否远程分页 |
| rowKey | `string \| Function` | - | 行键 |
| toolbarConfig | `TableToolbarConfig` | `{}` | 工具栏配置 |

### ToolbarConfig

```typescript
interface TableToolbarConfig {
  refresh?: boolean      // 刷新按钮
  density?: boolean      // 密度设置
  column?: boolean       // 列设置
  fullscreen?: boolean   // 全屏按钮
}
```

### Events

| 事件 | 参数 | 说明 |
|------|------|------|
| update:checked-row-keys | `Array<string \| number>` | 选择变化 |
| update:page | `number` | 页码变化 |
| update:page-size | `number` | 每页条数变化 |
| update:sorter | `object` | 排序变化 |
| update:columns | `DataTableColumns` | 列变化 |
| refresh | - | 刷新数据 |

## 🎨 最佳实践

### 1. 统一的数据加载模式

```javascript
// 使用 usePagination hook
const pagination = usePagination(doRefresh)

function doRefresh() {
  tableLoading.value = true
  api.getData(pagination.getPageInfo(searchModel.value))
    .then(res => {
      tableData.value = res.rows
      pagination.setTotalSize(res.total)
    })
    .finally(() => {
      tableLoading.value = false
    })
}
```

### 2. 搜索参数过滤

```javascript
function handleSearch(params) {
  // 过滤空值，只传递有效参数
  const filteredParams = Object.keys(params).reduce((acc, key) => {
    const value = params[key]
    if (value !== null && value !== undefined && value !== '') {
      acc[key] = value
    }
    return acc
  }, {})

  Object.assign(searchModel.value, filteredParams)
  pagination.page = 1
  doRefresh()
}
```

### 3. 响应式设计

```scss
.page {
  display: flex;
  flex-direction: column;
  gap: 16px;

  @media (max-width: 768px) {
    .page-header {
      flex-direction: column;
      gap: 12px;
    }
  }
}
```

## 🔧 迁移指南

### 从旧版本迁移

1. **移除搜索相关配置**
```vue
<!-- 旧版本 -->
<TableMain
  :search-config="searchConfig"
  :search-form="searchFormConfig"
  v-model:search-model="searchModel"
  @search="handleSearch"
/>

<!-- 新版本 -->
<TableSearchCard
  v-model="searchModel"
  :fields="searchFields"
  @search="handleSearch"
/>
<TableMain
  :data="data"
  :columns="columns"
/>
```

2. **更新搜索实现**
```javascript
// 旧版本 - 复杂配置
const searchFormConfig = {
  items: [
    { key: 'name', label: '名称', type: 'input' },
    // ...
  ]
}

// 新版本 - 简化配置
const searchFields = [
  { key: 'name', label: '名称', type: 'input' },
  // ...
]
```

## 📱 主题集成

表格组件完全支持项目的主题系统：

- 自动适配明暗模式
- 响应式设计
- 统一的视觉风格
- 优雅的动画效果

## 🚨 注意事项

1. **搜索功能分离** - 搜索功能现在由独立的 `TableSearchCard` 组件提供
2. **简化配置** - TableMain 组件专注于表格核心功能
3. **向后兼容** - 现有页面只需要简单调整即可使用新架构
4. **性能优化** - 避免了复杂配置导致的渲染问题