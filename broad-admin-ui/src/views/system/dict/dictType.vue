<template>
  <div class="dict-type">
    <!-- 搜索区域 -->
    <TableSearch @search="handleSearch" @reset="handleReset">
      <n-form ref="searchFormRef" :model="searchForm" inline>
        <n-grid :cols="24" :x-gap="24">
          <n-form-item-gi :span="6" label="字典名称">
            <n-input v-model:value="searchForm.dictName" placeholder="请输入字典名称" clearable />
          </n-form-item-gi>
          <n-form-item-gi :span="6" label="字典类型">
            <n-input v-model:value="searchForm.dictType" placeholder="请输入字典类型" clearable />
          </n-form-item-gi>
          <n-form-item-gi :span="6" label="状态">
            <n-select
              v-model:value="searchForm.status"
              :options="sys_normal_disable"
              placeholder="请选择状态"
              clearable
            />
          </n-form-item-gi>
        </n-grid>
      </n-form>
    </TableSearch>

    <!-- 表格区域 -->
    <TableMain
      :columns="columns"
      :data="tableData"
      :loading="loading"
      :pagination="pagination"
      row-key="dictId"
    >
      <template #header>
        <n-button-group>
          <n-button type="primary" @click="handleAdd" v-auth="['sys:dict:add']">
            <template #icon>
              <n-icon><AddOutline /></n-icon>
            </template>
            新增字典
          </n-button>
          <n-button type="success" @click="handleRefresh">
            <template #icon>
              <n-icon><RefreshOutline /></n-icon>
            </template>
            刷新
          </n-button>
        </n-button-group>
      </template>
    </TableMain>

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
            <n-form-item-gi :span="24" label="字典名称" path="dictName">
              <n-input v-model:value="formData.dictName" placeholder="请输入字典名称" />
            </n-form-item-gi>
            <n-form-item-gi :span="24" label="字典类型" path="dictType">
              <n-input
                v-model:value="formData.dictType"
                :disabled="!!formData.dictId"
                placeholder="请输入字典类型"
              />
            </n-form-item-gi>
            <n-form-item-gi :span="24" label="字典备注" path="remark">
              <n-input
                v-model:value="formData.remark"
                type="textarea"
                placeholder="请输入字典备注"
                :autosize="{ minRows: 3, maxRows: 5 }"
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
import { h, ref, reactive, onMounted } from 'vue'
import { useMessage, useDialog } from 'naive-ui'
import { AddOutline, RefreshOutline } from '@vicons/ionicons5'
import type { FormInst } from 'naive-ui'
import { usePagination } from '@/hooks/useTable'
import { useDict } from '@/utils/useDict'
import { getDictTypePage, addDictType, updateDictType, detectDictType } from '@/api/system/dictType'

// 组件通信
const emit = defineEmits(['select-dict'])

// 组件实例
const message = useMessage()
const dialog = useDialog()
const formRef = ref<FormInst | null>(null)
const searchFormRef = ref<FormInst | null>(null)

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
  dictName: '',
  dictType: '',
  status: null
})

const formData = ref({
  dictId: null,
  dictName: '',
  dictType: '',
  remark: '',
  status: '0'
})

// 表单校验规则
const rules = {
  dictName: {
    required: true,
    message: '请输入字典名称',
    trigger: ['blur', 'input']
  },
  dictType: {
    required: true,
    message: '请输入字典类型',
    trigger: ['blur', 'input']
  }
}

// 表格列定义
const columns = [
  { title: '字典名称', key: 'dictName' },
  {
    title: '字典类型',
    key: 'dictType',
    render: (row: any) => {
      return h(
        'a',
        {
          style: {
            color: '#2d8cf0',
            cursor: 'pointer'
          },
          onClick: () => emit('select-dict', row.dictType)
        },
        { default: () => row.dictType }
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
              default: () => '确认删除该字典吗？',
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
  loading.value = true
  try {
    const params = {
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

    const res = await getDictTypePage(params)
    if (res.code === 200) {
      tableData.value = res.rows || []
      pagination.itemCount = res.total || 0
    } else {
      message.error(res.message || '获取字典类型列表失败')
    }
  } catch (error) {
    console.error('获取字典类型列表失败:', error)
    message.error('获取字典类型列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索和重置
const handleSearch = () => {
  pagination.page = 1
  loadTableData()
}

const handleReset = () => {
  searchFormRef.value?.restoreValidation()
  searchForm.value = {
    dictName: '',
    dictType: '',
    status: null
  }
  handleSearch()
}

// 刷新
const handleRefresh = () => {
  loadTableData()
}

// 新增
const handleAdd = () => {
  modalTitle.value = '新增字典'
  formData.value = {
    dictId: null,
    dictName: '',
    dictType: '',
    remark: '',
    status: '0'
  }
  showModal.value = true
}

// 编辑
const handleEdit = (row: any) => {
  modalTitle.value = '编辑字典'
  formData.value = { ...row }
  showModal.value = true
}

// 删除
const handleDelete = async (row: any) => {
  try {
    const res = await detectDictType(row.dictId)
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

// 提交表单
const handleSubmit = async () => {
  await formRef.value?.validate()
  submitting.value = true
  try {
    const params = Object.entries(formData.value).reduce(
      (acc, [key, value]) => {
        if (
          value !== null &&
          value !== undefined &&
          (typeof value === 'string' ? value.trim() !== '' : value !== '')
        ) {
          acc[key] = typeof value === 'string' ? value.trim() : value
        }
        return acc
      },
      {} as Record<string, any>
    )

    const api = formData.value.dictId ? updateDictType : addDictType
    const res = await api(params)
    if (res.code === 200) {
      message.success(formData.value.dictId ? '更新成功' : '新增成功')
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
.dict-type {
  :deep(.n-data-table-wrapper) {
    border-radius: 8px;
  }

  :deep(.n-button) {
    padding: 0 16px;
  }

  :deep(.n-tag) {
    padding: 0 12px;
  }

  :deep(.n-card) {
    border-radius: 8px;
    margin-bottom: 16px;
  }

  :deep(.n-space) {
    margin-bottom: 16px;
  }
}
</style>
