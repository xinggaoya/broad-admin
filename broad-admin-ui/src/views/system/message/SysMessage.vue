<template>
  <div class="p-4 h-full">
    <n-card class="h-full" :bordered="false" :segmented="{ content: true }">
      <template #header>
        <n-flex justify="space-between" align="center">
          <div class="text-lg font-medium">消息管理</div>
          <n-flex>
            <n-button @click="handleRefresh" :loading="loading">
              <template #icon>
                <n-icon>
                  <Refresh/>
                </n-icon>
              </template>
              刷新
            </n-button>
            <n-button type="primary" @click="handleSendMessage" v-auth="['system:message:send']">
              <template #icon>
                <n-icon>
                  <Send/>
                </n-icon>
              </template>
              发送消息
            </n-button>
            <n-button type="warning" @click="handleBroadcast" v-auth="['system:message:broadcast']">
              <template #icon>
                <n-icon>
                  <VolumeHigh/>
                </n-icon>
              </template>
              广播消息
            </n-button>
          </n-flex>
        </n-flex>
      </template>

      <!-- 搜索区域 -->
      <n-form inline :model="searchForm" label-placement="left" class="mb-4">
        <n-form-item label="标题">
          <n-input v-model:value="searchForm.title" placeholder="请输入标题" clearable/>
        </n-form-item>
        <n-form-item label="状态">
          <n-select
              v-model:value="searchForm.status"
              placeholder="请选择状态"
              clearable
              :options="statusOptions"
              style="width: 120px"
          />
        </n-form-item>
        <n-form-item label="类型">
          <n-select
              v-model:value="searchForm.type"
              placeholder="请选择类型"
              clearable
              :options="typeOptions"
              style="width: 120px"
          />
        </n-form-item>
        <n-form-item>
          <n-button type="primary" @click="handleSearch">
            <template #icon>
              <n-icon>
                <Search/>
              </n-icon>
            </template>
            搜索
          </n-button>
          <n-button @click="handleReset" class="ml-2">
            重置
          </n-button>
        </n-form-item>
      </n-form>

      <!-- 表格 -->
      <n-data-table
          :columns="columns"
          :data="filteredMessages"
          :loading="loading"
          :pagination="pagination"
          :row-key="(row) => row.id"
          @update:checked-row-keys="handleCheck"
          class="h-full"
      />
    </n-card>

    <!-- 发送消息模态框 -->
    <n-modal v-model:show="showSendModal" preset="dialog" title="发送消息" style="width: 600px">
      <n-form :model="messageForm" :rules="rules" ref="formRef" label-placement="left" label-width="80px" class="mt-4">
        <n-form-item label="接收用户" path="userId">
          <n-input-number v-model:value="messageForm.userId" placeholder="请输入用户ID" :show-button="false"
                          style="width: 100%"/>
        </n-form-item>
        <n-form-item label="标题" path="title">
          <n-input v-model:value="messageForm.title" placeholder="请输入消息标题" maxlength="100" show-count/>
        </n-form-item>
        <n-form-item label="内容" path="content">
          <n-input
              v-model:value="messageForm.content"
              type="textarea"
              placeholder="请输入消息内容"
              :rows="4"
              maxlength="500"
              show-count
          />
        </n-form-item>
        <n-form-item label="类型" path="type">
          <n-select v-model:value="messageForm.type" :options="typeOptions"/>
        </n-form-item>
      </n-form>
      <template #action>
        <n-button @click="showSendModal = false">取消</n-button>
        <n-button type="primary" @click="confirmSend" :loading="sending">发送</n-button>
      </template>
    </n-modal>

    <!-- 广播消息模态框 -->
    <n-modal v-model:show="showBroadcastModal" preset="dialog" title="广播消息" style="width: 600px">
      <n-form :model="broadcastForm" :rules="rules" ref="broadcastFormRef" label-placement="left" label-width="80px"
              class="mt-4">
        <n-alert title="此消息将发送给所有在线用户" type="warning" class="mb-4"/>
        <n-form-item label="标题" path="title">
          <n-input v-model:value="broadcastForm.title" placeholder="请输入消息标题" maxlength="100" show-count/>
        </n-form-item>
        <n-form-item label="内容" path="content">
          <n-input
              v-model:value="broadcastForm.content"
              type="textarea"
              placeholder="请输入消息内容"
              :rows="4"
              maxlength="500"
              show-count
          />
        </n-form-item>
        <n-form-item label="类型" path="type">
          <n-select v-model:value="broadcastForm.type" :options="typeOptions"/>
        </n-form-item>
      </n-form>
      <template #action>
        <n-button @click="showBroadcastModal = false">取消</n-button>
        <n-button type="warning" @click="confirmBroadcast" :loading="broadcasting">广播</n-button>
      </template>
    </n-modal>
  </div>
</template>

<script lang="ts" setup>
import {h, ref, computed, onMounted} from 'vue'
import {
  NButton,
  NTag,
  NEllipsis,
  NSpace,
  NPopconfirm,
  type DataTableColumns
} from 'naive-ui'
import {
  Refresh,
  Send,
  VolumeHigh,
  Search,
  Trash,
  Eye,
  CheckmarkCircle,
  MailOpen
} from '@vicons/ionicons5'
import {useMessageStore} from '@/store/modules/message'
import {useUserStore} from '@/store/modules/user'
import {useDialog, useMessage} from 'naive-ui'
import {deleteMessage, sendNotification, broadcastNotification} from '@/api/message'
import type {ResultData} from '@/api/request'
import {formatTime} from '@/utils'

const messageStore = useMessageStore()
const userStore = useUserStore()
const dialog = useDialog()
const message = useMessage()

const loading = ref(false)
const sending = ref(false)
const broadcasting = ref(false)
const showSendModal = ref(false)
const showBroadcastModal = ref(false)
const formRef = ref()
const broadcastFormRef = ref()
const checkedRowKeys = ref<number[]>([])

const searchForm = ref({
  title: '',
  status: null,
  type: null
})

const messageForm = ref({
  userId: null as number | null,
  title: '',
  content: '',
  type: 1
})

const broadcastForm = ref({
  title: '',
  content: '',
  type: 1
})

const statusOptions = [
  {label: '未读', value: 0},
  {label: '已读', value: 1}
]

const typeOptions = [
  {label: '系统消息', value: 0},
  {label: '通知公告', value: 1},
  {label: '待办提醒', value: 2}
]

const rules = {
  userId: {required: true, message: '请输入用户ID', trigger: 'blur'},
  title: {required: true, message: '请输入标题', trigger: 'blur'},
  content: {required: true, message: '请输入内容', trigger: 'blur'},
  type: {required: true, message: '请选择类型', trigger: 'change', type: 'number'}
}

const pagination = {
  pageSize: 10
}

const createColumns = (): DataTableColumns<any> => [
  {
    type: 'selection',
    width: 50
  },
  {
    title: '标题',
    key: 'title',
    width: 200,
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '内容',
    key: 'content',
    width: 300,
    render: (row) => {
      return h(NEllipsis, {lineClamp: 2}, () => row.content)
    }
  },
  {
    title: '类型',
    key: 'type',
    width: 100,
    render: (row) => {
      const typeMap = {
        0: {label: '系统', type: 'info'},
        1: {label: '通知', type: 'success'},
        2: {label: '待办', type: 'warning'}
      } as any
      const config = typeMap[row.type] || {label: '未知', type: 'default'}
      return h(NTag, {type: config.type, size: 'small'}, () => config.label)
    }
  },
  {
    title: '状态',
    key: 'status',
    width: 80,
    render: (row) => {
      return h(
          NTag,
          {type: row.status === 0 ? 'error' : 'success', size: 'small'},
          () => (row.status === 0 ? '未读' : '已读')
      )
    }
  },
  {
    title: '时间',
    key: 'createTime',
    width: 150,
    render: (row) => formatTime(row.createTime)
  },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    fixed: 'right',
    render: (row) => {
      return h(NSpace, {}, () => [
        row.status === 0
            ? h(
                NButton,
                {
                  size: 'small',
                  type: 'primary',
                  ghost: true,
                  onClick: () => handleMarkAsRead(row.id)
                },
                {
                  icon: () => h('n-icon', {}, () => h(CheckmarkCircle)),
                  default: () => '已读'
                }
            )
            : null,
        h(
            NPopconfirm,
            {
              onPositiveClick: () => handleDelete(row.id)
            },
            {
              trigger: () =>
                  h(
                      NButton,
                      {size: 'small', type: 'error', ghost: true},
                      {
                        icon: () => h('n-icon', {}, () => h(Trash)),
                        default: () => '删除'
                      }
                  ),
              default: () => '确定删除吗？'
            }
        )
      ])
    }
  }
]

const columns = createColumns()

// 过滤后的消息列表
const filteredMessages = computed(() => {
  let list = [...messageStore.messages]

  if (searchForm.value.title) {
    list = list.filter((msg) => msg.title.includes(searchForm.value.title))
  }
  if (searchForm.value.status !== null) {
    list = list.filter((msg) => msg.status === searchForm.value.status)
  }
  if (searchForm.value.type !== null) {
    list = list.filter((msg) => msg.type === searchForm.value.type)
  }

  return list
})

const handleCheck = (rowKeys: number[]) => {
  checkedRowKeys.value = rowKeys
}

const handleRefresh = async () => {
  if (userStore.userId) {
    loading.value = true
    await messageStore.loadMessages(userStore.userId.toString())
    loading.value = false
  }
}

const handleSearch = () => {
  // filteredMessages 是计算属性，会自动更新
}

const handleReset = () => {
  searchForm.value = {
    title: '',
    status: null,
    type: null
  }
}

const handleMarkAsRead = async (id: number) => {
  await messageStore.markMessageAsRead(id)
  message.success('标记已读成功')
}

const handleDelete = async (id: number) => {
  const res = await messageStore.removeMessage(id)
  if (res.code === 200) {
    message.success('删除成功')
  }
}

const handleBatchDelete = () => {
  if (checkedRowKeys.value.length === 0) {
    message.warning('请选择要删除的消息')
    return
  }

  dialog.warning({
    title: '警告',
    content: `确定删除选中的 ${checkedRowKeys.value.length} 条消息吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
      const res = await messageStore.batchDeleteMessages(checkedRowKeys.value)
      if (res.code === 200) {
        message.success('批量删除成功')
        checkedRowKeys.value = []
      }
    }
  })
}

const handleSendMessage = () => {
  messageForm.value = {
    userId: null,
    title: '',
    content: '',
    type: 1
  }
  showSendModal.value = true
}

const confirmSend = async () => {
  await formRef.value?.validate()
  sending.value = true
  try {
    const res = await sendNotification(messageForm.value) as ResultData
    if (res.code === 200) {
      message.success('发送成功')
      showSendModal.value = false
      handleRefresh()
    } else {
      message.error(res.message || '发送失败')
    }
  } catch (error) {
    message.error('发送失败')
  } finally {
    sending.value = false
  }
}

const handleBroadcast = () => {
  broadcastForm.value = {
    title: '',
    content: '',
    type: 1
  }
  showBroadcastModal.value = true
}

const confirmBroadcast = async () => {
  await broadcastFormRef.value?.validate()
  broadcasting.value = true
  try {
    const res = await broadcastNotification(broadcastForm.value) as ResultData
    if (res.code === 200) {
      message.success(res.message || '广播成功')
      showBroadcastModal.value = false
      handleRefresh()
    } else {
      message.error(res.message || '广播失败')
    }
  } catch (error) {
    message.error('广播失败')
  } finally {
    broadcasting.value = false
  }
}

// 初始加载
onMounted(() => {
  if (userStore.userId) {
    messageStore.loadMessages(userStore.userId.toString())
  }
})
</script>
