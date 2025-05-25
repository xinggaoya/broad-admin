<template>
  <div class="table-custom-page">
    <!-- 使用融合搜索功能的 TableMain 组件 -->
    <TableMain
      :data="tableData"
      :columns="tableColumns"
      :loading="tableLoading"
      :pagination="pagination"
      :search-config="searchConfig"
      :search-form="searchForm"
      v-model:search-model="searchModel"
      @search="handleSearch"
      @reset="handleReset"
      @update:page="handlePageChange"
      @update:page-size="handlePageSizeChange"
    >
      <!-- 表格标题 -->
      <template #header>
        <div class="table-title">
          <h3>用户管理</h3>
          <p>基于重构后的表格组件，融合搜索功能</p>
        </div>
      </template>

      <!-- 表格操作按钮 -->
      <template #header-extra>
        <n-space>
          <AddButton @add="handleAddUser" v-auth="['sys:user:add']" />
          <DeleteButton @delete="onDeleteItem" />
          <ExportButton type="info" @export="handleExport" />
        </n-space>
      </template>

      <!-- 自定义搜索表单（可选，如果需要更复杂的搜索表单） -->
      <template #search-form>
        <n-form ref="customFormRef" :label-width="100" :model="searchModel" label-placement="left">
          <n-grid :cols="24" :x-gap="16" :y-gap="16">
            <n-form-item-gi :span="6" label="姓名" path="name">
              <n-input v-model:value="searchModel.name" placeholder="请输入姓名" clearable />
            </n-form-item-gi>
            <n-form-item-gi :span="6" label="年龄" path="age">
              <n-input-number
                v-model:value="searchModel.age"
                placeholder="请输入年龄"
                clearable
                style="width: 100%"
              />
            </n-form-item-gi>
            <n-form-item-gi :span="6" label="性别" path="sex">
              <n-select
                v-model:value="searchModel.sex"
                placeholder="请选择性别"
                :options="sexOptions"
                clearable
              />
            </n-form-item-gi>
            <n-form-item-gi :span="6" label="状态" path="status">
              <n-select
                v-model:value="searchModel.status"
                placeholder="请选择状态"
                :options="statusOptions"
                clearable
              />
            </n-form-item-gi>
          </n-grid>
        </n-form>
      </template>

      <!-- 表格底部统计信息 -->
      <template #footer>
        <div class="table-footer">
          <n-space justify="space-between">
            <span>共 {{ pagination.total }} 条数据</span>
            <span>当前页显示 {{ tableData.length }} 条</span>
          </n-space>
        </div>
      </template>
    </TableMain>
  </div>
</template>

<script lang="ts" setup>
import { usePagination } from '@/hooks/useTable'
import { useMessage } from 'naive-ui'
import { onMounted, ref, reactive } from 'vue'
import { data, columns } from '@/views/template/list/data'
import { Download as DownloadIcon } from '@vicons/ionicons5'
import AddButton from '@/components/table/button/AddButton.vue'
import DeleteButton from '@/components/table/button/DeleteButton.vue'
import ExportButton from '@/components/table/button/ExportButton.vue'
import TableMain from '@/components/table/main/TableMain.vue'
import type { SearchConfig, SearchFormConfig } from '@/types/table'

// 表格列配置 - 使用 ref 并创建新数组引用
const tableColumns = ref([...columns])
const tableData = ref(data)
const tableLoading = ref(false)
const customFormRef = ref()
const message = useMessage()
const pagination = usePagination(doRefresh)

// 搜索模型
const searchModel = ref({
  name: '',
  age: null,
  sex: '',
  status: ''
})

// 搜索配置
const searchConfig: SearchConfig = {
  show: true,
  title: '搜索条件',
  defaultCollapse: true,
  resetOnSearch: true,
  autoSearch: false, // 手动触发搜索
  searchDelay: 300
}

// 选项数据
const sexOptions = [
  { label: '男', value: '男' },
  { label: '女', value: '女' }
]

const statusOptions = [
  { label: '启用', value: '1' },
  { label: '禁用', value: '0' }
]

// 搜索表单配置（使用配置方式，也可以使用上面的自定义模板）
const searchForm: SearchFormConfig = {
  items: [
    {
      key: 'name',
      label: '姓名',
      type: 'input',
      placeholder: '请输入姓名',
      span: 6
    },
    {
      key: 'age',
      label: '年龄',
      type: 'number',
      placeholder: '请输入年龄',
      span: 6
    },
    {
      key: 'sex',
      label: '性别',
      type: 'select',
      placeholder: '请选择性别',
      options: sexOptions,
      span: 6
    },
    {
      key: 'status',
      label: '状态',
      type: 'select',
      placeholder: '请选择状态',
      options: statusOptions,
      span: 6
    }
  ],
  cols: 24,
  xGap: 16,
  yGap: 16,
  labelWidth: 80,
  labelPlacement: 'left'
}

// 数据刷新函数
function doRefresh() {
  tableData.value = []
  tableLoading.value = true

  // 模拟API调用
  setTimeout(() => {
    // 随机数number
    const number = Math.floor(Math.random() * 100)
    const mockData = []

    for (let i = number; i < number + 10; i++) {
      mockData.push({
        id: i,
        firstName: 'David_' + i,
        lastName: 'Lee',
        age: 20 + (i % 50),
        email: 'david.lee@example.com',
        address: '789 Oak St',
        city: 'Somewhere',
        state: 'TX',
        country: 'USA',
        phone: '555-345-6789',
        sex: i % 2 === 0 ? '男' : '女',
        status: i % 3 === 0 ? '0' : '1'
      })
    }

    // 根据搜索条件过滤数据
    let filteredData = mockData

    if (searchModel.value.name) {
      filteredData = filteredData.filter((item) =>
        item.firstName.toLowerCase().includes(searchModel.value.name.toLowerCase())
      )
    }

    if (searchModel.value.age) {
      filteredData = filteredData.filter((item) => item.age === searchModel.value.age)
    }

    if (searchModel.value.sex) {
      filteredData = filteredData.filter((item) => item.sex === searchModel.value.sex)
    }

    if (searchModel.value.status) {
      filteredData = filteredData.filter((item) => item.status === searchModel.value.status)
    }

    tableData.value = filteredData
    pagination.setTotalSize(filteredData.length + Math.floor(Math.random() * 50))
    tableLoading.value = false
  }, 800)
}

// 事件处理函数
function handleAddUser() {
  doRefresh()
  message.success('新增成功')
}

function onDeleteItem() {
  message.success('删除成功')
  doRefresh()
}

function handleExport() {
  message.info('导出功能开发中...')
}

// 搜索处理
function handleSearch(params: Record<string, any>) {
  console.log('搜索参数:', params)
  // 更新搜索模型
  Object.assign(searchModel.value, params)
  // 重置到第一页
  pagination.page = 1
  doRefresh()
  message.success('搜索完成')
}

// 重置处理
function handleReset() {
  console.log('重置搜索条件')
  // 清空搜索模型
  Object.assign(searchModel.value, {
    name: '',
    age: null,
    sex: '',
    status: ''
  })
  // 重置到第一页
  pagination.page = 1
  doRefresh()
  message.success('重置成功')
}

// 分页处理
function handlePageChange(page: number) {
  pagination.page = page
  doRefresh()
}

function handlePageSizeChange(pageSize: number) {
  pagination.pageSize = pageSize
  pagination.page = 1
  doRefresh()
}

// 生命周期
onMounted(async () => {
  doRefresh()
})
</script>

<style lang="scss" scoped>
.table-custom-page {
  .table-title {
    h3 {
      margin: 0 0 4px 0;
      font-size: 18px;
      font-weight: 600;
      color: var(--text-color);
    }

    p {
      margin: 0;
      font-size: 14px;
      color: var(--text-color-2);
    }
  }

  .table-footer {
    padding: 12px 0;
    font-size: 14px;
    color: var(--text-color-2);
    border-top: 1px solid var(--divider-color);
  }
}
</style>
