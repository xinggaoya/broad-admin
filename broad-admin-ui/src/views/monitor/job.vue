<template>
  <div class="main-container">
    <TableSearch @search="doRefresh" @reset="onResetSearch"></TableSearch>
    <TableMain
      :data="tableList"
      :columns="tableColumns"
      :loading="tableLoading"
      :pagination="pagination"
    >
      <template v-slot:header>
        <AddButton @add="onAddItem" />
      </template>
    </TableMain>

    <ModalDialog
      v-model="modalDialog"
      :mask-closable="false"
      :title="modelDialogTitle"
      content-height="50vh"
      @confirm="onConfirm"
    >
      <template #content>
        <n-form
          ref="formRef"
          :model="jobFrom"
          :rules="jobRules"
          label-placement="left"
          label-width="auto"
        >
          <n-form-item label="任务名称" path="jobName">
            <n-input v-model:value="jobFrom.jobName" placeholder="输入任务名称" />
          </n-form-item>
          <n-form-item label="任务组名" path="jobGroup">
            <n-input v-model:value="jobFrom.jobGroup" placeholder="输入任务组名" />
          </n-form-item>
          <n-form-item label="调用目标字符串" path="invokeTarget">
            <n-input
              v-model:value="jobFrom.invokeTarget"
              placeholder="输入调用目标字符串"
              type="textarea"
            />
          </n-form-item>
          <n-form-item label="cron执行表达式" path="cronExpression">
            <n-input v-model:value="jobFrom.cronExpression" placeholder="输入cron执行表达式" />
          </n-form-item>
          <n-form-item label="执行策略" path="misfirePolicy">
            <n-select
              v-model:value="jobFrom.misfirePolicy"
              placeholder="选择执行策略"
            />
          </n-form-item>
          <n-form-item label="是否并发" path="concurrent">
            <n-radio-group v-model:value="jobFrom.concurrent">
              <n-radio-button v-for="(item, index) in sys_yes_no" :value="item.value" :key="index"
              >{{ item.label }}
              </n-radio-button>
            </n-radio-group>
          </n-form-item>
          <n-form-item label="状态" path="status">
            <n-radio-group v-model:value="jobFrom.status">
              <n-radio-button
                v-for="(item, index) in sys_job_status"
                :value="item.value"
                :key="index"
              >{{ item.label }}
              </n-radio-button>
            </n-radio-group>
          </n-form-item>
          <n-form-item label="备注" path="remark">
            <n-input v-model:value="jobFrom.remark" placeholder="输入备注" type="textarea" />
          </n-form-item>
        </n-form>
      </template>
    </ModalDialog>
  </div>
</template>

<script lang="ts" setup>
import { usePagination, useRenderAction, renderDictTag } from '@/hooks/useTable'
import { useDialog, useMessage } from 'naive-ui'
import { onMounted, ref } from 'vue'
import { addJob, getJobList, getJobRun, jobUpdate, jobDelete } from '@/api/monitor/job'
import { useDict } from '@/utils/useDict'
import AddButton from '@/components/common/AddButton.vue'
import TableMain from '@/components/table/main/TableMain.vue'
import TableSearch from '@/components/table/search/TableSearch.vue'
import type { TableActionModel } from '@/types/table'

const { sys_job_status, sys_yes_no } = useDict('sys_job_status', 'sys_yes_no')
const tableList = ref([])
const tableLoading = ref(false)
const jobFrom = ref<any>({})
const jobRules = {}
const modalDialog = ref<boolean>(false)
const pagination = usePagination(doRefresh)
const navieDialog = useDialog()
const message = useMessage()
const modelDialogTitle = ref('')
const tableColumns = ref([
  {
    title: '任务名称',
    key: 'jobName'
  },
  {
    title: '任务分组',
    key: 'jobGroup'
  },
  {
    title: '调用目标字符串',
    key: 'invokeTarget',
    width: 200,
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: 'cron执行表达式',
    key: 'cronExpression'
  },
  {
    title: '是否并发执行',
    key: 'concurrent',
    render: (rowData: { concurrent: any }) => renderDictTag(sys_yes_no.value, rowData.concurrent)
  },
  {
    title: '下次执行时间',
    key: 'nextValidTime',
    width: 200
  },
  {
    title: '状态',
    key: 'status',
    render: (rowData: { status: any }) => renderDictTag(sys_job_status.value, rowData.status)
  },
  {
    title: '操作',
    key: 'actions',
    fixed: 'right',
    width: 200,
    render: (rowData: any) => {
      return useRenderAction([
        {
          label: '执行',
          type: 'primary',
          onClick: () => onRunItem(rowData)
        },
        {
          label: '更新',
          type: 'success',
          onClick: () => onUpdated(rowData)
        },
        {
          label: '删除',
          type: 'error',
          onClick: () => onDeleteItem(rowData)
        }
      ] as TableActionModel[])
    }
  }
])

function doRefresh() {
  tableLoading.value = true
  getJobList(pagination.getPageInfo())
    .then((res: any) => {
      tableList.value = res.rows
      pagination.setTotalSize(res.total)
      tableLoading.value = false
    })
    .catch(console.log)
}

function onAddItem() {
  modelDialogTitle.value = '新增任务'
  jobFrom.value = {}
  modalDialog.value = true
}

function onUpdated(row: any) {
  modelDialogTitle.value = '更新任务'
  jobFrom.value = row
  modalDialog.value = true
}

function onDeleteItem(data: any) {
  navieDialog.warning({
    content: '是否要删除此数据，删除后不恢复？',
    positiveText: '删除',
    onPositiveClick: () => {
      jobDelete(data.jobId)
        .then(() => {
          message.success('删除成功')
          doRefresh()
        })
        .catch(console.log)
    }
  })
}

function onConfirm() {
  if (jobFrom.value.jobId) {
    jobUpdate(jobFrom.value)
      .then((res) => {
        message.success('更新成功')
        modalDialog.value = false
        doRefresh()
      })
      .catch(console.log)
  } else {
    addJob(jobFrom.value)
      .then((res) => {
        message.success('新增成功')
        modalDialog.value = false
        doRefresh()
      })
      .catch(console.log)
  }
}

function onRunItem(row: any) {
  navieDialog.warning({
    content: '是否立即执行一次？',
    title: '执行任务',
    positiveText: '确认',
    onPositiveClick: () => {
      let loading: any = message.loading('正在执行...', {
        duration: 0
      })
      getJobRun(row)
        .then((res: any) => {
          loading?.destroy()
          message.success('执行成功')
        })
        .catch(loading?.destroy())
    }
  })
}

// 重置
function onResetSearch() {
}

onMounted(doRefresh)
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
