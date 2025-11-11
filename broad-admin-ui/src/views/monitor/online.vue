<template>
  <section class="online-page">
    <header class="page-header">
      <div>
        <p class="header-subtitle">会话监控</p>
        <h2>在线用户</h2>
      </div>
      <div class="header-actions">
        <n-tag type="info" round>当前在线 {{ tableList.length }} 人</n-tag>
        <n-button tertiary round size="small" :loading="tableLoading" @click="doRefresh">
          <template #icon>
            <n-icon><RefreshOutline /></n-icon>
          </template>
          刷新
        </n-button>
      </div>
    </header>

    <n-card class="table-panel" :bordered="false">
      <TableMain
        :data="tableList"
        :columns="tableColumns"
        :loading="tableLoading"
        :pagination="pagination"
      >
        <template #header>
          <div class="table-toolbar">
            <n-badge :value="tableList.length" type="success">活跃会话</n-badge>
          </div>
        </template>
      </TableMain>
    </n-card>
  </section>
</template>

<script lang="ts" setup>
import { ref, onMounted, h } from 'vue'
import { useMessage, useDialog, NButton, NIcon } from 'naive-ui'
import { RefreshOutline, ExitOutline } from '@vicons/ionicons5'
import { usePagination, useRenderAction } from '@/hooks/useTable'
import { getOnline, forceLogout } from '@/api/monitor/online'
import TableMain from '@/components/table/main/TableMain.vue'

const message = useMessage()
const dialog = useDialog()
const tableList = ref<any[]>([])
const tableLoading = ref(false)
const pagination = usePagination(() => doRefresh())

const tableColumns = ref([
  { title: '登录标识', key: 'tokenValue', ellipsis: true },
  { title: '用户名', key: 'userName', ellipsis: true },
  { title: '部门', key: 'deptName', ellipsis: true },
  { title: '登录IP', key: 'lastIp', ellipsis: true },
  { title: '登录地址', key: 'lastLoginip', ellipsis: true },
  { title: '登录时间', key: 'lastLogintime', width: 180 },
  {
    title: '操作',
    key: 'actions',
    width: 120,
    fixed: 'right',
    render: (rowData: any) =>
      useRenderAction([
        {
          label: '强退',
          type: 'warning',
          icon: () => h(NIcon, null, { default: () => h(ExitOutline) }),
          auth: 'online:delete',
          onClick: () => onDeleteItem(rowData)
        }
      ])
  }
])

const doRefresh = () => {
  tableLoading.value = true
  getOnline(pagination.getPageInfo())
    .then((res: any) => {
      tableList.value = res.rows || []
      pagination.setTotalSize(res.total || 0)
    })
    .finally(() => {
      tableLoading.value = false
    })
}

const onDeleteItem = (row: any) => {
  dialog.warning({
    title: '提示',
    content: '是否要强制该用户下线？',
    positiveText: '强退',
    negativeText: '取消',
    onPositiveClick: () =>
      forceLogout(row)
        .then(() => {
          message.success('强退成功')
          doRefresh()
        })
        .catch(() => message.error('操作失败'))
  })
}

onMounted(() => {
  doRefresh()
})
</script>

<style scoped lang="scss">
.online-page {
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

.table-panel {
  border-radius: var(--shell-radius-lg);
  background: var(--shell-surface);
  box-shadow: var(--shell-shadow);
}

.table-toolbar {
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 12px;
  }
}
</style>
