<template>
  <section class="job-page">
    <header class="page-header">
      <div>
        <p class="header-subtitle">定时任务</p>
        <h2>任务调度</h2>
      </div>
      <div class="header-actions">
        <n-button tertiary round size="small" :loading="tableLoading" @click="doRefresh">
          <template #icon>
            <n-icon><RefreshOutline /></n-icon>
          </template>
          刷新
        </n-button>
        <AddButton @add="onAddItem" />
      </div>
    </header>

    <n-card class="table-panel" :bordered="false">
      <TableSearch :loading="tableLoading" @search="handleSearch" @reset="onResetSearch">
        <n-form class="search-form" :model="searchForm" label-placement="left" label-width="auto">
          <n-grid cols="1 640:2 1024:3" responsive="screen" :x-gap="16" :y-gap="12">
            <n-grid-item>
              <n-form-item label="任务名称">
                <n-input
                  v-model:value="searchForm.jobName"
                  placeholder="输入任务名称或关键字"
                  clearable
                />
              </n-form-item>
            </n-grid-item>
            <n-grid-item>
              <n-form-item label="任务分组">
                <n-select
                  v-model:value="searchForm.jobGroup"
                  :options="sys_job_group"
                  placeholder="选择任务分组"
                  clearable
                />
              </n-form-item>
            </n-grid-item>
            <n-grid-item>
              <n-form-item label="任务状态">
                <n-select
                  v-model:value="searchForm.status"
                  :options="sys_job_status"
                  placeholder="选择任务状态"
                  clearable
                />
              </n-form-item>
            </n-grid-item>
          </n-grid>
        </n-form>
      </TableSearch>

      <TableMain
        :data="tableList"
        :columns="tableColumns"
        :loading="tableLoading"
        :pagination="pagination"
      />
    </n-card>

    <ModalDialog
      v-model="modalDialog"
      :mask-closable="false"
      :title="modelDialogTitle"
      :submit-loading="formSubmitting"
      content-height="60vh"
      @confirm="onConfirm"
      @after-leave="handleDialogLeave"
    >
      <template #content>
        <n-form
          ref="formRef"
          :model="jobForm"
          :rules="jobRules"
          label-placement="left"
          label-width="auto"
        >
          <n-form-item label="任务名称" path="jobName">
            <n-input v-model:value="jobForm.jobName" placeholder="输入任务名称" maxlength="64" show-count />
          </n-form-item>
          <n-form-item label="任务分组" path="jobGroup">
            <n-select
              v-model:value="jobForm.jobGroup"
              :options="sys_job_group"
              placeholder="请选择任务分组"
            />
          </n-form-item>
          <n-form-item label="调用目标字符串" path="invokeTarget">
            <n-input
              v-model:value="jobForm.invokeTarget"
              placeholder="例如 broadTask.ryNoParams()"
              type="textarea"
              :autosize="{ minRows: 3, maxRows: 5 }"
            />
          </n-form-item>
          <n-form-item label="CRON 表达式" path="cronExpression">
            <n-input
              v-model:value="jobForm.cronExpression"
              placeholder="例如 0/10 * * * * ?"
              maxlength="255"
              show-count
            />
          </n-form-item>
          <n-form-item label="执行策略" path="misfirePolicy">
            <n-select
              v-model:value="jobForm.misfirePolicy"
              :options="misfireSelectOptions"
              placeholder="请选择触发策略"
            />
          </n-form-item>
          <n-form-item label="是否并发" path="concurrent">
            <n-radio-group v-model:value="jobForm.concurrent">
              <n-radio-button v-for="item in sys_yes_no" :key="item.value" :value="item.value">
                {{ item.label }}
              </n-radio-button>
            </n-radio-group>
          </n-form-item>
          <n-form-item label="状态" path="status">
            <n-radio-group v-model:value="jobForm.status">
              <n-radio-button v-for="item in sys_job_status" :key="item.value" :value="item.value">
                {{ item.label }}
              </n-radio-button>
            </n-radio-group>
          </n-form-item>
          <n-form-item label="备注" path="remark">
            <n-input
              v-model:value="jobForm.remark"
              placeholder="补充说明（选填）"
              type="textarea"
              :autosize="{ minRows: 2, maxRows: 4 }"
              maxlength="200"
              show-count
            />
          </n-form-item>
          <n-alert class="form-tip" type="info" :closable="false">
            调用目标需位于后端白名单（默认 com.broad 包）内，且禁止 http/ldap/rmi 等敏感前缀。
          </n-alert>
        </n-form>
      </template>
    </ModalDialog>
  </section>
</template>

<script lang="ts" setup>
import { h, computed, nextTick, onMounted, reactive, ref } from 'vue'
import {
  usePagination,
  useRenderAction,
  renderDictTag,
  useTableLoading
} from '@/hooks/useTable'
import {
  useDialog,
  useMessage,
  NIcon,
  NSwitch,
  NTooltip,
  NTag,
  NEllipsis,
  NTime,
  type FormInst,
  type FormRules,
  type DataTableColumns,
  type SelectOption
} from 'naive-ui'
import {
  addJob,
  getJobList,
  getJobRun,
  jobUpdate,
  jobDelete,
  getJobMeta,
  changeJobStatus
} from '@/api/monitor/job'
import { useDict } from '@/utils/useDict'
import AddButton from '@/components/table/button/AddButton.vue'
import TableMain from '@/components/table/main/TableMain.vue'
import TableSearch from '@/components/table/search/TableSearch.vue'
import type { TableActionModel } from '@/types/table'
import { RefreshOutline } from '@vicons/ionicons5'

interface JobMetaOption {
  label: string
  value: string
  description?: string
}

interface JobItem {
  jobId?: number | null
  jobName: string
  jobGroup: string
  invokeTarget: string
  cronExpression: string
  misfirePolicy: string
  concurrent: string
  status: string
  remark?: string
  nextValidTime?: string
}

const DEFAULT_JOB_GROUP = 'DEFAULT'
const DEFAULT_MISFIRE = '3'

const { sys_job_status, sys_yes_no, sys_job_group } = useDict(
  'sys_job_status',
  'sys_yes_no',
  'sys_job_group'
)
const { loading: tableLoading, withLoading } = useTableLoading()
const tableList = ref<JobItem[]>([])
const modalDialog = ref(false)
const modelDialogTitle = ref('')
const dialogMode = ref<'create' | 'update'>('create')
const formRef = ref<FormInst | null>(null)
const formSubmitting = ref(false)
const searchForm = reactive({
  jobName: '',
  jobGroup: '',
  status: ''
})
const statusLoadingMap = reactive<Record<number, boolean>>({})
const jobForm = reactive<JobItem>(createDefaultJobForm())
const misfireOptions = ref<JobMetaOption[]>([])
const misfireSelectOptions = computed<SelectOption[]>(() =>
  misfireOptions.value.map((item) => ({
    label: `${item.label}（${item.description}）`,
    value: item.value
  }))
)
const misfireOptionMap = computed<Record<string, JobMetaOption>>(() => {
  const map: Record<string, JobMetaOption> = {}
  misfireOptions.value.forEach((item) => {
    map[item.value] = item
  })
  return map
})
const navieDialog = useDialog()
const message = useMessage()
const pagination = usePagination(doRefresh)

const jobRules: FormRules = {
  jobName: [
    { required: true, message: '请输入任务名称', trigger: 'blur' },
    { max: 64, message: '任务名称不能超过 64 个字符', trigger: 'blur' }
  ],
  jobGroup: [{ required: true, message: '请选择任务分组', trigger: 'change' }],
  invokeTarget: [{ required: true, message: '请输入调用目标字符串', trigger: 'blur' }],
  cronExpression: [{ required: true, message: '请输入 CRON 表达式', trigger: 'blur' }],
  misfirePolicy: [{ required: true, message: '请选择执行策略', trigger: 'change' }],
  concurrent: [{ required: true, message: '请选择并发策略', trigger: 'change' }],
  status: [{ required: true, message: '请选择任务状态', trigger: 'change' }]
}

const tableColumns = computed<DataTableColumns<JobItem>>(() => [
  {
    title: '任务名称',
    key: 'jobName',
    minWidth: 160,
    ellipsis: true,
    render: (row) =>
      h(NEllipsis, null, {
        default: () => row.jobName
      })
  },
  {
    title: '任务分组',
    key: 'jobGroup',
    width: 120,
    render: (row) => renderDictTag(sys_job_group.value, row.jobGroup)
  },
  {
    title: '调用目标',
    key: 'invokeTarget',
    minWidth: 220,
    render: (row) =>
      h(NEllipsis, { tooltip: true }, {
        default: () => row.invokeTarget
      })
  },
  {
    title: 'CRON',
    key: 'cronExpression',
    minWidth: 160,
    render: (row) =>
      h('code', { class: 'cron-text' }, row.cronExpression)
  },
  {
    title: '执行策略',
    key: 'misfirePolicy',
    width: 140,
    render: (row) => {
      const option = misfireOptionMap.value[row.misfirePolicy || '']
      if (!option) return row.misfirePolicy || '--'
      return h(
        NTooltip,
        null,
        {
          trigger: () =>
            h(
              NTag,
              { size: 'small', type: 'info', bordered: false },
              { default: () => option.label }
            ),
          default: () => option.description
        }
      )
    }
  },
  {
    title: '并发控制',
    key: 'concurrent',
    width: 120,
    render: (row) => renderDictTag(sys_yes_no.value, row.concurrent)
  },
  {
    title: '下次执行时间',
    key: 'nextValidTime',
    width: 180,
    render: (row) =>
      row.nextValidTime
        ? h(NTime, { time: row.nextValidTime, format: 'yyyy-MM-dd HH:mm:ss' })
        : '--'
  },
  {
    title: '状态',
    key: 'status',
    width: 130,
    render: (row) =>
      h(
        NSwitch,
        {
          size: 'small',
          value: row.status === '0',
          loading: Boolean(row.jobId && statusLoadingMap[row.jobId]),
          onUpdateValue: () => onToggleStatus(row)
        },
        {
          checked: () => '启用',
          unchecked: () => '暂停'
        }
      )
  },
  {
    title: '操作',
    key: 'actions',
    fixed: 'right',
    width: 220,
    render: (row) =>
      useRenderAction([
        {
          label: '执行一次',
          type: 'primary',
          onClick: () => onRunItem(row)
        },
        {
          label: '编辑',
          type: 'success',
          onClick: () => onUpdated(row)
        },
        {
          label: '删除',
          type: 'error',
          onClick: () => onDeleteItem(row)
        }
      ] as TableActionModel[])
  }
])

function createDefaultJobForm(): JobItem {
  return {
    jobId: null,
    jobName: '',
    jobGroup: DEFAULT_JOB_GROUP,
    invokeTarget: '',
    cronExpression: '',
    misfirePolicy: DEFAULT_MISFIRE,
    concurrent: '1',
    status: '0',
    remark: ''
  }
}

async function loadMeta() {
  try {
    const res = await getJobMeta()
    misfireOptions.value = res.data?.misfirePolicies ?? []
  } catch (error) {
    message.error('任务元数据加载失败，请稍后重试')
  }
}

async function doRefresh() {
  await withLoading(async () => {
    try {
      const res = await getJobList(pagination.getPageInfo(searchForm))
      tableList.value = res.rows || []
      pagination.setTotalSize(res.total || 0)
    } catch (error) {
      message.error('获取任务列表失败，请稍后重试')
    }
  })
}

function onAddItem() {
  dialogMode.value = 'create'
  modelDialogTitle.value = '新增任务'
  resetJobForm()
  modalDialog.value = true
}

function onUpdated(row: JobItem) {
  dialogMode.value = 'update'
  modelDialogTitle.value = '编辑任务'
  resetJobForm()
  Object.assign(jobForm, { ...row })
  modalDialog.value = true
}

function resetJobForm() {
  Object.assign(jobForm, createDefaultJobForm())
  nextTick(() => formRef.value?.restoreValidation())
}

function handleDialogLeave() {
  formSubmitting.value = false
  resetJobForm()
}

function handleSearch() {
  pagination.page = 1
  doRefresh()
}

function onResetSearch() {
  Object.assign(searchForm, {
    jobName: '',
    jobGroup: '',
    status: ''
  })
  pagination.page = 1
  doRefresh()
}

async function onConfirm() {
  if (!formRef.value) return
  try {
    await formRef.value.validate()
  } catch {
    return
  }
  formSubmitting.value = true
  const payload = { ...jobForm }
  try {
    if (payload.jobId) {
      await jobUpdate(payload)
      message.success('任务更新成功')
    } else {
      await addJob(payload)
      message.success('任务创建成功')
    }
    modalDialog.value = false
    await doRefresh()
  } catch (error) {
    message.error('操作失败，请检查填写内容')
  } finally {
    formSubmitting.value = false
  }
}

function onDeleteItem(data: JobItem) {
  if (!data.jobId) return
  navieDialog.warning({
    title: '删除任务',
    content: '任务删除后将无法恢复，确认继续吗？',
    positiveText: '确认删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      try {
        await jobDelete(data.jobId)
        message.success('删除成功')
        doRefresh()
      } catch (error) {
        message.error('删除失败，请稍后重试')
      }
    }
  })
}

function onRunItem(row: JobItem) {
  if (!row.jobId || !row.jobGroup) return
  navieDialog.warning({
    title: '执行任务',
    content: '立即执行一次任务不会影响下次调度，确认触发？',
    positiveText: '立即执行',
    negativeText: '取消',
    onPositiveClick: async () => {
      const loading = message.loading('正在触发任务...', { duration: 0 })
      try {
        await getJobRun({ jobId: row.jobId, jobGroup: row.jobGroup })
        message.success('任务执行指令已下发')
      } catch (error) {
        message.error('执行失败，请稍后重试')
      } finally {
        loading.destroy()
      }
    }
  })
}

async function onToggleStatus(row: JobItem) {
  if (!row.jobId) return
  const jobId = row.jobId
  if (statusLoadingMap[jobId]) return
  const originalStatus = row.status
  const targetStatus = row.status === '0' ? '1' : '0'
  statusLoadingMap[jobId] = true
  try {
    await changeJobStatus({ jobId, status: targetStatus })
    row.status = targetStatus
    message.success(`任务已${targetStatus === '0' ? '启用' : '暂停'}`)
  } catch (error) {
    row.status = originalStatus
    message.error('状态切换失败，请稍后再试')
  } finally {
    statusLoadingMap[jobId] = false
  }
}

onMounted(async () => {
  await Promise.all([loadMeta(), doRefresh()])
})
</script>

<style scoped lang="scss">
.job-page {
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

.search-form :deep(.n-form-item) {
  margin-bottom: 0;
}

.cron-text {
  font-family: var(--shell-font-mono, 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, monospace);
  font-size: 12px;
}

.form-tip {
  margin-top: 12px;
}

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 12px;
  }
}
</style>
