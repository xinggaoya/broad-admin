<template>
  <div class="table-custom-page">
    <!-- 页面头部 -->
    <header class="page-header">
      <div>
        <p class="header-subtitle">模板演示</p>
        <h2>自定义表格</h2>
      </div>
      <div class="header-actions">
        <n-button tertiary round size="small" :loading="tableLoading" @click="doRefresh">
          <template #icon>
            <n-icon><RefreshOutline /></n-icon>
          </template>
          刷新
        </n-button>
        <AddButton @add="handleAddUser" v-auth="['sys:user:add']" />
        <DeleteButton @delete="onDeleteItem" />
        <ExportButton type="info" @export="handleExport" />
      </div>
    </header>

    <n-card class="table-panel" :bordered="false">
      <!-- 独立搜索区域 -->
      <n-card size="small" class="search-card" style="margin-bottom: 16px;">
        <template #header>
          <span>搜索条件</span>
        </template>
        <template #default>
          <n-form inline :label-width="80" :model="searchModel">
            <n-form-item label="姓名">
              <n-input
                v-model:value="searchModel.name"
                placeholder="请输入姓名"
                clearable
                @keydown.enter="handleSearchClick"
              />
            </n-form-item>
            <n-form-item label="年龄">
              <n-input-number
                v-model:value="searchModel.age"
                placeholder="请输入年龄"
                clearable
                style="width: 100%"
                @keydown.enter="handleSearchClick"
              />
            </n-form-item>
            <n-form-item label="性别">
              <n-select
                v-model:value="searchModel.sex"
                placeholder="请选择性别"
                :options="sexOptions"
                clearable
              />
            </n-form-item>
            <n-form-item label="状态">
              <n-select
                v-model:value="searchModel.status"
                placeholder="请选择状态"
                :options="statusOptions"
                clearable
              />
            </n-form-item>
            <n-form-item>
              <n-space>
                <n-button type="primary" @click="handleSearchClick">搜索</n-button>
                <n-button @click="handleResetClick">重置</n-button>
              </n-space>
            </n-form-item>
          </n-form>
        </template>
      </n-card>

      <TableMain
        :data="tableData"
        :columns="tableColumns"
        :loading="tableLoading"
        :pagination="pagination"
        @update:page="handlePageChange"
        @update:page-size="handlePageSizeChange"
      >
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
    </n-card>
  </div>
</template>

<script lang="ts" setup>
import { usePagination } from '@/hooks/useTable'
import { useMessage } from 'naive-ui'
import { onMounted, ref, reactive } from 'vue'
import { data, columns } from '@/views/template/list/data'
import { RefreshOutline } from '@vicons/ionicons5'
import AddButton from '@/components/table/button/AddButton.vue'
import DeleteButton from '@/components/table/button/DeleteButton.vue'
import ExportButton from '@/components/table/button/ExportButton.vue'
import TableMain from '@/components/table/main/TableMain.vue'

// 表格列配置 - 使用 ref 并创建新数组引用
const tableColumns = ref([...columns])
const tableData = ref(data)
const tableLoading = ref(false)
const message = useMessage()
const pagination = usePagination(doRefresh)

// 搜索模型
const searchModel = ref({
  name: '',
  age: null,
  sex: '',
  status: ''
})

// 选项数据
const sexOptions = [
  { label: '男', value: '男' },
  { label: '女', value: '女' }
]

const statusOptions = [
  { label: '启用', value: '1' },
  { label: '禁用', value: '0' }
]

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
function handleSearchClick() {
  // 重置到第一页
  pagination.page = 1
  doRefresh()
  message.success('搜索完成')
}

// 重置处理
function handleResetClick() {
  // 清空搜索模型
  searchModel.value = {
    name: '',
    age: null,
    sex: '',
    status: ''
  }
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
  .table-panel {
    border-radius: var(--shell-radius-lg);
    background: var(--shell-surface);
    box-shadow: var(--shell-shadow);
  }

  .search-card {
    :deep(.n-card__content) {
      padding: 16px;
    }
  }

  .table-footer {
    padding: 12px 0;
    font-size: 14px;
    color: var(--text-color-2);
    border-top: 1px solid var(--divider-color);
  }
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--shell-surface);
  border-radius: var(--shell-radius-lg);
  padding: 20px;
  box-shadow: var(--shell-shadow);
  margin-bottom: var(--shell-gap);
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
}

.header-subtitle {
  margin: 0;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--shell-muted-text-color);
}

.header-actions {
  display: flex;
  gap: 12px;
  align-items: center;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 12px;
  }
}
</style>
