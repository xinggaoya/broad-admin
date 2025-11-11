<template>
  <section class="log-page">
    <header class="page-header">
      <div>
        <p class="header-subtitle">日志中心</p>
        <h2>登录日志</h2>
      </div>
      <n-button tertiary round size="small" :loading="tableLoading" @click="doRefresh">
        <template #icon>
          <n-icon><RefreshOutline /></n-icon>
        </template>
        刷新
      </n-button>
    </header>

    <n-card class="table-panel" :bordered="false">
      <TableSearch @search="doRefresh" @reset="onResetSearch" />
      <TableMain
        :data="tableList"
        :columns="tableColumns"
        :loading="tableLoading"
        :pagination="pagination"
      />
    </n-card>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { NButton, NIcon } from 'naive-ui'
import { RefreshOutline } from '@vicons/ionicons5'
import { getLoginLogPage } from '@/api/monitor/loginlog'
import { usePagination, useRenderTag } from '@/hooks/useTable'
import { useDict } from '@/utils/useDict'
import TableSearch from '@/components/table/search/TableSearch.vue'
import TableMain from '@/components/table/main/TableMain.vue'

const pagination = usePagination(() => doRefresh())
const tableList = ref([])
const tableLoading = ref(false)
const { sys_log_status } = useDict('sys_log_status')

const tableColumns = ref([
  { title: '用户名', key: 'userName' },
  { title: '登录平台', key: 'browser' },
  { title: '登录类型', key: 'clientType' },
  { title: '操作系统', key: 'operatingSystem' },
  { title: '登录 IP', key: 'loginIp' },
  { title: '登录地址', key: 'loginAddress' },
  {
    title: '状态',
    key: 'loginStatus',
    render: ({ loginStatus }: any) => useRenderTag(sys_log_status.value, loginStatus)
  },
  {
    title: '消息',
    key: 'message',
    ellipsis: {
      tooltip: {
        placement: 'top',
        width: 300
      }
    }
  },
  { title: '操作时间', key: 'createTime', width: 200 }
])

const doRefresh = () => {
  tableLoading.value = true
  getLoginLogPage(pagination.getPageInfo())
    .then((res: any) => {
      tableList.value = res.rows || []
      pagination.setTotalSize(res.total || 0)
    })
    .finally(() => {
      tableLoading.value = false
    })
}

const onResetSearch = () => {
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
