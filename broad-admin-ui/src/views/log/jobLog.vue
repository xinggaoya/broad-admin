<template>
  <div class="main-container">
    <TableSearch @search="doRefresh" @reset="onResetSearch"></TableSearch>
    <TableMain
      :data="tableList"
      :columns="tableColumns"
      :loading="tableLoading"
      :pagination="pagination"
    >
      <template v-slot:header></template>
    </TableMain>
  </div>
</template>

<script lang="ts" setup>
import { getJobLogPage } from '@/api/monitor/jobLog'
import { usePagination, useRenderTag } from '@/hooks/useTable'
import { DataFormType } from '@/types/components'
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
    title: '任务名称',
    width: 200,
    key: 'jobName'
  },
  {
    title: '任务分组',
    key: 'jobGroup'
  },
  {
    title: '调用目标字符串',
    key: 'invokeTarget',
    ellipsis: {
      tooltip: {
        placement: 'top',
        width: 300
      }
    }
  },
  {
    title: '任务消息',
    key: 'jobMessage',
    ellipsis: {
      tooltip: {
        placement: 'top',
        width: 300
      }
    }
  },
  {
    title: '状态',
    key: 'status',
    render: ({ status }: any) => {
      return useRenderTag(sys_log_status.value, status)
    }
  },
  {
    title: '异常信息',
    key: 'exceptionInfo',
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
  }
])

function doRefresh() {
  tableLoading.value = true
  getJobLogPage(pagination.getPageInfo())
    .then((res: any) => {
      tableList.value = res.rows
      pagination.setTotalSize(res.total)
      tableLoading.value = false
    })
    .catch(console.log)
}
function onAddItem() {
  // exportForm({ adminName: '120', logHttpMethod: 'DELETE' })
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
