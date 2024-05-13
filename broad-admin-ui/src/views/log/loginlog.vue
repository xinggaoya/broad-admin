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
import { getLoginLogPage } from '@/api/monitor/loginlog'
import { usePagination, useRenderTag } from '@/hooks/useTable'
import { DataFormType } from '@/types/components'
import { onMounted, ref } from 'vue'
import { useDict } from '@/utils/useDict'
import TableSearch from '@/components/table/search/TableSearch.vue'

const searchForm = ref<DataFormType | null>(null)
const pagination = usePagination(doRefresh)
const tableList = ref([])
const tableLoading = ref(false)
const { sys_log_status } = useDict('sys_log_status')
const tableColumns = ref([
  {
    title: '用户名',
    key: 'userName'
  },
  {
    title: '登录平台',
    key: 'browser'
  },
  {
    title: '登录类型',
    key: 'clientType'
  },
  {
    title: '操作系统',
    key: 'operatingSystem'
  },
  {
    title: '登录IP',
    key: 'loginIp'
  },
  {
    title: '登录地址',
    key: 'loginAddress'
  },
  {
    title: '状态',
    key: 'loginStatus',
    render: ({ loginStatus }: any) => {
      return useRenderTag(sys_log_status.value, loginStatus)
    }
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
  {
    title: '操作时间',
    key: 'createTime',
    width: 200
  }
])

function doRefresh() {
  tableLoading.value = true
  getLoginLogPage(pagination.getPageInfo())
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
