<template>
  <div class="dict-data">
    <!-- 操作栏 -->
    <n-card class="action-card">
      <n-space>
        <n-button type="default" @click="handleBack">
          <template #icon>
            <n-icon><ArrowBackOutline /></n-icon>
          </template>
          返回字典类型
        </n-button>
        <n-button type="primary" @click="handleAdd" v-auth="['sys:dict:add']">
          <template #icon>
            <n-icon><AddOutline /></n-icon>
          </template>
          新增字典数据
        </n-button>
        <n-button type="success" @click="handleRefresh">
          <template #icon>
            <n-icon><RefreshOutline /></n-icon>
          </template>
          刷新
        </n-button>
      </n-space>
    </n-card>

    <!-- 表格区域 -->
    <TableMain
      :columns="columns"
      :data="tableData"
      :loading="loading"
      :pagination="pagination"
      row-key="dictCode"
    />

    <!-- 编辑对话框 -->
    <n-modal
      v-model:show="showModal"
      :title="modalTitle"
      preset="card"
      :style="{ width: '600px' }"
      :mask-closable="false"
    >
      <n-spin :show="submitting">
        <n-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-placement="left"
          label-width="100"
          require-mark-placement="right-hanging"
        >
          <n-grid :cols="24" :x-gap="24">
            <n-form-item-gi :span="24" label="字典标签" path="dictLabel">
              <n-input v-model:value="formData.dictLabel" placeholder="请输入字典标签" />
            </n-form-item-gi>
            <n-form-item-gi :span="24" label="字典键值" path="dictValue">
              <n-input v-model:value="formData.dictValue" placeholder="请输入字典键值" />
            </n-form-item-gi>
            <n-form-item-gi :span="24" label="字典类型" path="dictType">
              <n-input v-model:value="formData.dictType" disabled />
            </n-form-item-gi>
            <n-form-item-gi :span="24" label="样式属性" path="listClass">
              <n-space vertical>
                <n-space>
                  <n-tag
                    v-for="item in tagTypes"
                    :key="item.value"
                    :type="item.value"
                    :bordered="false"
                    style="cursor: pointer"
                    @click="handleSelectTag(item.value)"
                  >
                    {{ item.label }}
                  </n-tag>
                </n-space>
                <n-space align="center">
                  <span>已选择：</span>
                  <n-tag :type="formData.listClass" :bordered="false">
                    {{ formData.listClass || '请选择标签样式' }}
                  </n-tag>
                </n-space>
              </n-space>
            </n-form-item-gi>
            <n-form-item-gi :span="24" label="显示排序" path="dictSort">
              <n-input-number
                v-model:value="formData.dictSort"
                :min="0"
                :max="999"
                placeholder="请输入显示排序"
              />
            </n-form-item-gi>
            <n-form-item-gi :span="24" label="状态" path="status">
              <n-radio-group v-model:value="formData.status" name="status">
                <n-space>
                  <n-radio
                    v-for="item in sys_normal_disable"
                    :key="item.value"
                    :value="item.value"
                    :label="item.label"
                  />
                </n-space>
              </n-radio-group>
            </n-form-item-gi>
            <n-form-item-gi :span="24" label="备注" path="remark">
              <n-input
                v-model:value="formData.remark"
                type="textarea"
                placeholder="请输入备注"
                :autosize="{ minRows: 3, maxRows: 5 }"
              />
            </n-form-item-gi>
          </n-grid>
        </n-form>
      </n-spin>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showModal = false">取消</n-button>
          <n-button
            type="primary"
            :loading="submitting"
            :disabled="submitting"
            @click="handleSubmit"
          >
            确定
          </n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script lang="ts" setup>
import { h, ref, onMounted, watch } from 'vue'
import { useMessage, useDialog } from 'naive-ui'
import { AddOutline, RefreshOutline, ArrowBackOutline } from '@vicons/ionicons5'
import type { FormInst } from 'naive-ui'
import { usePagination } from '@/hooks/useTable'
import { useDict } from '@/utils/useDict'
import { getDict, addDict, updateDict, detectDict } from '@/api/system/dict'

// 组件通信
const props = defineProps<{
  dictType: string
}>()
const emit = defineEmits(['back'])

// 组件实例
const message = useMessage()
const dialog = useDialog()
const formRef = ref<FormInst | null>(null)

// 字典数据
const { sys_normal_disable } = useDict('sys_normal_disable')

// 状态管理
const loading = ref(false)
const submitting = ref(false)
const showModal = ref(false)
const modalTitle = ref('')
const tableData = ref<any[]>([])

// 表单数据
const searchForm = ref({
  dictLabel: '',
  dictValue: '',
  status: null
})

const formData = ref({
  dictCode: null,
  dictLabel: '',
  dictValue: '',
  dictType: props.dictType,
  dictSort: 0,
  listClass: '',
  status: '0',
  remark: ''
})

// 标签类型
const tagTypes = [
  { label: '默认', value: 'default' },
  { label: '成功', value: 'success' },
  { label: '信息', value: 'info' },
  { label: '警告', value: 'warning' },
  { label: '错误', value: 'error' }
]

// 表单校验规则
const rules = {
  dictLabel: {
    required: true,
    message: '请输入字典标签',
    trigger: ['blur', 'input']
  },
  dictValue: {
    required: true,
    message: '请输入字典键值',
    trigger: ['blur', 'input']
  },
  dictSort: {
    required: true,
    message: '请输入显示排序',
    trigger: ['blur', 'change']
  },
  listClass: {
    required: true,
    message: '请选择标签样式',
    trigger: ['blur', 'change']
  }
}

// 表格列定义
const columns = [
  { title: '字典标签', key: 'dictLabel' },
  { title: '字典键值', key: 'dictValue' },
  {
    title: '字典样式',
    key: 'listClass',
    render: (row: any) => {
      return h(
        'n-tag',
        {
          type: row.listClass,
          bordered: false
        },
        { default: () => row.dictLabel }
      )
    }
  },
  {
    title: '状态',
    key: 'status',
    render: (row: any) => {
      const status = sys_normal_disable.value.find((item) => item.value === row.status)
      return h(
        'n-tag',
        {
          type: row.status === '0' ? 'success' : 'error',
          round: true,
          bordered: false
        },
        { default: () => status?.label || '未知' }
      )
    }
  },
  { title: '备注', key: 'remark' },
  { title: '创建时间', key: 'createTime' },
  {
    title: '操作',
    key: 'actions',
    fixed: 'right',
    render: (row: any) => {
      return h('n-space', null, {
        default: () => [
          h(
            'n-button',
            {
              text: true,
              type: 'primary',
              onClick: () => handleEdit(row)
            },
            { default: () => '编辑' }
          ),
          h(
            'n-popconfirm',
            {
              onPositiveClick: () => handleDelete(row)
            },
            {
              default: () => '确认删除该字典数据吗？',
              trigger: () =>
                h(
                  'n-button',
                  {
                    text: true,
                    type: 'error'
                  },
                  { default: () => '删除' }
                )
            }
          )
        ]
      })
    }
  }
]

// 分页配置
const pagination = usePagination(() => loadTableData())

// 加载表格数据
const loadTableData = async () => {
  // 如果没有dictType，不执行请求
  if (!props.dictType) {
    return
  }

  loading.value = true
  try {
    // 过滤掉空值、空字符串和null值
    const params = {
      dictType: props.dictType,
      page: pagination.page,
      pageSize: pagination.pageSize,
      ...Object.entries(searchForm.value).reduce((acc, [key, value]) => {
        if (
          value !== null &&
          value !== undefined &&
          (typeof value === 'string' ? value.trim() !== '' : value !== '')
        ) {
          acc[key] = typeof value === 'string' ? value.trim() : value
        }
        return acc
      }, {})
    }

    const res = await getDict(params)
    if (res.code === 200) {
      tableData.value = res.rows || []
      pagination.setTotalSize(res.total || 0)
    } else {
      message.error(res.message || '获取字典数据列表失败')
    }
  } catch (error) {
    console.error('获取字典数据列表失败:', error)
    message.error('获取字典数据列表失败')
  } finally {
    loading.value = false
  }
}

// 监听dictType变化
watch(
  () => props.dictType,
  (newVal) => {
    if (newVal) {
      // 重置分页
      pagination.page = 1
      // 重置搜索条件
      searchForm.value = {
        dictLabel: '',
        dictValue: '',
        status: null
      }
      // 重新加载数据
      loadTableData()
    }
  },
  { immediate: true }
)

// 搜索和重置
const handleSearch = () => {
  pagination.page = 1
  loadTableData()
}

const handleReset = () => {
  searchForm.value = {
    dictLabel: '',
    dictValue: '',
    status: null
  }
  handleSearch()
}

// 返回
const handleBack = () => {
  emit('back')
}

// 刷新
const handleRefresh = () => {
  loadTableData()
}

// 新增
const handleAdd = () => {
  modalTitle.value = '新增字典数据'
  formData.value = {
    dictCode: null,
    dictLabel: '',
    dictValue: '',
    dictType: props.dictType,
    dictSort: 0,
    listClass: '',
    status: '0',
    remark: ''
  }
  showModal.value = true
}

// 编辑
const handleEdit = (row: any) => {
  modalTitle.value = '编辑字典数据'
  formData.value = { ...row }
  showModal.value = true
}

// 删除
const handleDelete = async (row: any) => {
  try {
    const res = await detectDict(row.dictCode)
    if (res.code === 200) {
      message.success('删除成功')
      if (tableData.value.length === 1 && pagination.page > 1) {
        pagination.page--
      }
      loadTableData()
    } else {
      message.error(res.message || '删除失败')
    }
  } catch (error) {
    console.error('删除失败:', error)
    message.error('删除失败')
  }
}

// 选择标签样式
const handleSelectTag = (type: string) => {
  formData.value.listClass = type
}

// 提交表单
const handleSubmit = async () => {
  await formRef.value?.validate()
  submitting.value = true
  try {
    const params = Object.entries(formData.value).reduce(
      (acc, [key, value]) => {
        if (value !== null && value !== '' && value !== undefined) {
          acc[key] = value
        }
        return acc
      },
      {} as Record<string, any>
    )

    const api = formData.value.dictCode ? updateDict : addDict
    const res = await api(params)
    if (res.code === 200) {
      message.success(formData.value.dictCode ? '更新成功' : '新增成功')
      showModal.value = false
      loadTableData()
    } else {
      message.error(res.message || '操作失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
    message.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 初始化
onMounted(() => {
  loadTableData()
})
</script>

<style lang="scss" scoped>
.dict-data {
  :deep(.action-card) {
    margin-bottom: 16px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  }

  :deep(.n-data-table-wrapper) {
    border-radius: 8px;
  }

  :deep(.n-button) {
    padding: 0 16px;
  }

  :deep(.n-tag) {
    padding: 0 12px;
  }

  :deep(.n-card-header) {
    padding: 12px 16px;
  }

  :deep(.n-card__content) {
    padding: 12px 16px;
  }
}
</style>
