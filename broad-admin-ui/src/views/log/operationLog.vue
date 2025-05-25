<template>
  <div>
    <TableSearch @search="doRefresh" @reset="onResetSearch">
      <n-form :model="searchForm" ref="searchFormRef" label-placement="left" label-width="auto">
        <n-grid :cols="4" :x-gap="10">
          <n-form-item-gi label="标题">
            <n-input v-model:value="searchForm.logDescription" placeholder="请输入用户名" />
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
    >
      <template v-slot:header>
        <ExportButton @click="exportOperationLogs" :loading="loading" />
      </template>
    </TableMain>
  </div>
</template>

<script lang="ts" setup>
import { exportForm, getUserLogPage } from '@/api/monitor/operationLog'
import { usePagination, useRenderTag } from '@/hooks/useTable'
import { onMounted, reactive, ref } from 'vue'
import { useDict } from '@/utils/useDict'
import TableMain from '@/components/table/main/TableMain.vue'
import TableSearch from '@/components/table/search/TableSearch.vue'
import ExportButton from '@/components/table/button/ExportButton.vue'
import { clearFormObject } from '@/utils'
import { type DataTableColumns, useMessage } from 'naive-ui'

const searchForm = ref<any>({})
const searchFormRef = ref({})
const message = useMessage()
const pagination = usePagination(doRefresh)
const tableList = ref([])
const tableLoading = ref(false)
const loading = ref(false)
const { sys_log_status } = useDict('sys_log_status')

const tableColumns: DataTableColumns[] = reactive([
  {
    title: '标题',
    width: 200,
    key: 'logDescription'
  },
  {
    title: '请求类型',
    key: 'logHttpMethod',
    width: 100
  },
  {
    title: '请求地址',
    key: 'logUrl',
    width: 200,
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '请求参数',
    key: 'logParams',
    width: 200,
    ellipsis: {
      tooltip: {
        placement: 'top',
        width: 500
      }
    }
  },
  {
    title: '返回参数',
    key: 'logResult',
    width: 200,
    ellipsis: {
      tooltip: {
        placement: 'top',
        width: 300
      }
    }
  },
  {
    title: '请求IP',
    key: 'logIp',
    width: 150
  },
  {
    title: '请求地点',
    key: 'logIpAddress',
    width: 150
  },
  {
    title: '请求方法',
    key: 'logMethod',
    width: 100,
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '状态',
    key: 'logStatus',
    width: 100,
    render: ({ logStatus }: any) => {
      return useRenderTag(sys_log_status.value, logStatus)
    }
  },
  {
    title: '异常信息',
    key: 'exceptionInfo',
    width: 200,
    ellipsis: {
      tooltip: {
        placement: 'top',
        width: 300
      }
    }
  },
  {
    title: '操作时间',
    key: 'createTime',
    width: 200
  },
  {
    title: '操作人',
    key: 'adminName',
    width: 100
  }
])

function doRefresh() {
  tableLoading.value = true
  getUserLogPage(pagination.getPageInfo(searchForm.value))
    .then((res: any) => {
      tableList.value = res.rows
      pagination.setTotalSize(res.total)
      tableLoading.value = false
    })
    .catch(console.log)
}

function exportOperationLogs() {
  loading.value = true
  const timestamp = new Date().getTime()
  exportForm(pagination.getPageInfo(searchForm.value), `操作日志_${timestamp}.xlsx`).finally(() => {
    message.success('导出成功')
    loading.value = false
  })
}

function onResetSearch() {
  clearFormObject(searchForm)
  doRefresh()
}

onMounted(async () => {
  doRefresh()
})
</script>

<style lang="scss" scoped>
.avatar-container {
  position: relative;
  width: 30px;
  height: 30px;
  margin: 0 auto;
  vertical-align: middle;

  .avatar {
    width: 100%;
    height: 100%;
    border-radius: 50%;
  }

  .avatar-vip {
    border: 2px solid #cece1e;
  }

  .vip {
    position: absolute;
    top: 0;
    right: -9px;
    width: 15px;
    transform: rotate(60deg);
  }
}

.gender-container {
  .gender-icon {
    width: 20px;
  }
}
</style>
