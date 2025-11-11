<template>
  <section class="log-page">
    <header class="page-header">
      <div>
        <p class="header-subtitle">日志中心</p>
        <h2>操作日志</h2>
      </div>
      <div class="header-actions">
        <n-button tertiary round size="small" :loading="tableLoading" @click="doRefresh">
          <template #icon>
            <n-icon><RefreshOutline /></n-icon>
          </template>
          刷新
        </n-button>
        <ExportButton :loading="loading" @click="exportOperationLogs" />
      </div>
    </header>

    <n-card class="table-panel" :bordered="false">
      <TableSearch @search="doRefresh" @reset="onResetSearch">
        <n-form :model="searchForm" ref="searchFormRef" label-placement="left" label-width="auto">
          <n-grid :cols="4" :x-gap="10">
            <n-form-item-gi label="标题">
              <n-input v-model:value="searchForm.logDescription" placeholder="请输入标题" />
            </n-form-item-gi>
            <n-form-item-gi label="状态">
              <n-select
                v-model:value="searchForm.logStatus"
                :options="sys_log_status"
                placeholder="请选择状态"
              />
            </n-form-item-gi>
            <n-form-item-gi label="操作时间">
              <n-date-picker
                v-model:value="searchForm.operatingTime"
                type="daterange"
                clearable
                placeholder="选择日期"
              />
            </n-form-item-gi>
          </n-grid>
        </n-form>
      </TableSearch>

      <TableMain
        :data="tableList"
        :columns="tableColumns"
        :loading="tableLoading"
        :pagination="pagination"
        :scroll-x="1800"
      />
    </n-card>
  </section>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { NButton, NIcon, useMessage } from 'naive-ui'
import { RefreshOutline } from '@vicons/ionicons5'
import { exportForm, getUserLogPage } from '@/api/monitor/operationLog'
import { usePagination, useRenderTag } from '@/hooks/useTable'
import { useDict } from '@/utils/useDict'
import TableMain from '@/components/table/main/TableMain.vue'
import TableSearch from '@/components/table/search/TableSearch.vue'
import ExportButton from '@/components/table/button/ExportButton.vue'

const searchForm = ref<any>({})
const searchFormRef = ref()
const message = useMessage()
const pagination = usePagination(() => doRefresh())
const tableList = ref([])
const tableLoading = ref(false)
const loading = ref(false)
const { sys_log_status } = useDict('sys_log_status')

const tableColumns = reactive([
  { title: '标题', width: 200, key: 'logDescription' },
  { title: '请求类型', key: 'logHttpMethod', width: 100 },
  {
    title: '请求地址',
    key: 'logUrl',
    width: 200,
    ellipsis: { tooltip: true }
  },
  {
    title: '请求参数',
    key: 'logParams',
    width: 220,
    ellipsis: { tooltip: { placement: 'top', width: 500 } }
  },
  {
    title: '返回参数',
    key: 'logResult',
    width: 220,
    ellipsis: { tooltip: { placement: 'top', width: 300 } }
  },
  { title: '请求 IP', key: 'logIp', width: 150 },
  { title: '请求地点', key: 'logIpAddress', width: 150 },
  {
    title: '请求方法',
    key: 'logMethod',
    width: 160,
    ellipsis: { tooltip: true }
  },
  {
    title: '状态',
    key: 'logStatus',
    width: 100,
    render: ({ logStatus }: any) => useRenderTag(sys_log_status.value, logStatus)
  },
  {
    title: '异常信息',
    key: 'exceptionInfo',
    width: 220,
    ellipsis: { tooltip: { placement: 'top', width: 320 } }
  },
  { title: '操作时间', key: 'createTime', width: 200 },
  { title: '操作人', key: 'adminName', width: 140 }
])

const doRefresh = () => {
  tableLoading.value = true
  getUserLogPage(pagination.getPageInfo(searchForm.value))
    .then((res: any) => {
      tableList.value = res.rows || []
      pagination.setTotalSize(res.total || 0)
    })
    .finally(() => {
      tableLoading.value = false
    })
}

const exportOperationLogs = () => {
  loading.value = true
  const timestamp = new Date().getTime()
  exportForm(pagination.getPageInfo(searchForm.value), `操作日志_${timestamp}.xlsx`)
    .then(() => message.success('导出成功'))
    .finally(() => {
      loading.value = false
    })
}

const onResetSearch = () => {
  searchForm.value = {}
  searchFormRef.value?.restoreValidation()
  pagination.page = 1
  doRefresh()
}

onMounted(() => {
  doRefresh()
})
</script>

<style scoped lang="scss">
.log-page {
  display: flex;
  flex-direction: column;
  gap: var(--shell-gap);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--shell-surface);
  border-radius: var(--shell-radius-lg);
  padding: 20px;
  box-shadow: var(--shell-shadow);
}

.page-header h2 {
  margin: 0;
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

.table-panel {
  border-radius: var(--shell-radius-lg);
  background: var(--shell-surface);
  box-shadow: var(--shell-shadow);
  padding: 16px;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 12px;
  }
}
</style>
