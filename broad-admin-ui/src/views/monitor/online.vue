<template>
  <div class="main-container">
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
import { usePagination, useRenderAction } from '@/hooks/useTable'
import { useMessage, useDialog } from 'naive-ui'
import { onMounted, ref } from 'vue'
import { getOnline, forceLogout } from '@/api/monitor/online'
import TableMain from '@/components/table/main/TableMain.vue'

const pagination = usePagination(doRefresh)
const naiveDialog = useDialog()
const tableList = ref([])
const tableLoading = ref(false)
const message = useMessage()
const tableColumns = ref([
  {
    title: '登录标识',
    key: 'tokenValue'
  },
  {
    title: '用户名',
    key: 'userName'
  },
  {
    title: '部门',
    key: 'deptName'
  },
  {
    title: '登录IP',
    key: 'lastIp'
  },
  {
    title: '登录地址',
    key: 'lastLoginip'
  },
  {
    title: '登录时间',
    key: 'lastLogintime'
  },
  {
    title: '操作',
    key: 'actions',
    fixed: 'right',
    render: (rowData: any) => {
      return useRenderAction([
        {
          label: '强退',
          type: 'warning',
          auth: 'online:delete',
          onClick: () => onDeleteItem(rowData)
        }
      ])
    }
  }
])

function doRefresh() {
  tableLoading.value = true
  getOnline(pagination.getPageInfo())
    .then((res: any) => {
      tableList.value = res.rows
      pagination.setTotalSize(res.total || 10)
      tableLoading.value = false
    })
    .catch(console.log)
}

function onDeleteItem(row: any) {
  naiveDialog.warning({
    title: '提示',
    content: '是否要强退该用户？',
    positiveText: '强退',
    onPositiveClick: () => {
      forceLogout(row)
        .then(() => {
          message.success('强退成功')
          doRefresh()
        })
        .catch(console.log)
    }
  })
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
