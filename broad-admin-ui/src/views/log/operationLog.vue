<template>
  <div>
    <TableSearch @search="doRefresh" @reset="onResetSearch"></TableSearch>
    <TableMain
      :data="tableList"
      :columns="tableColumns"
      :loading="tableLoading"
      :pagination="pagination"
      :scroll-x="1800"
    >
    </TableMain>
  </div>
</template>

<script lang="ts" setup>
import { getUserLogPage } from '@/api/monitor/operationLog'
import { usePagination, useRenderTag } from '@/hooks/useTable'
import type { DataFormType } from '@/types/components'
import { onMounted, ref } from 'vue'
import { useDict } from '@/utils/useDict'
import TableMain from '@/components/table/main/TableMain.vue'
import TableSearch from '@/components/table/search/TableSearch.vue'

const searchForm = ref<DataFormType | null>(null)
const pagination = usePagination(doRefresh)
const tableList = ref([])
const tableLoading = ref(false)
const { sys_log_status } = useDict('sys_log_status')

const tableColumns = ref([
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
  getUserLogPage(pagination.getPageInfo())
    .then((res: any) => {
      tableList.value = res.rows
      pagination.setTotalSize(res.total)
      tableLoading.value = false
    })
    .catch(console.log)
}

function onResetSearch() {
  searchForm.value?.reset()
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
