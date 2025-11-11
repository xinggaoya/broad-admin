<template>
  <section class="dict-type-page">
    <header class="page-header">
      <div>
        <p class="header-subtitle">系统管理</p>
        <h2>字典管理</h2>
        <p class="header-desc">维护全局通用的字典配置，支撑前后端统一的枚举展示。</p>
      </div>
      <div class="header-actions">
        <n-button tertiary round size="small" :loading="loading" @click="handleRefresh">
          <template #icon>
            <n-icon><RefreshOutline /></n-icon>
          </template>
          刷新
        </n-button>
        <n-button type="primary" round @click="handleAdd" v-auth="['sys:dict:add']">
          <template #icon>
            <n-icon><AddOutline /></n-icon>
          </template>
          新增字典
        </n-button>
      </div>
    </header>

    <TableMain
      :columns="columns"
      :data="tableData"
      :loading="loading"
      :pagination="pagination"
      row-key="dictId"
      sticky-toolbar
      :search-config="searchConfig"
      :search-form="searchFormConfig"
      v-model:search-model="searchModel"
      @search="handleSearch"
      @reset="handleReset"
      @refresh="handleRefresh"
    >
      <template #header>
        <div class="table-header">
          <h3>字典类型列表</h3>
          <p>支持快速检索字典名称、类型与启用状态。</p>
        </div>
      </template>
      <template #header-extra>
        <n-space>
          <n-button type="primary" round size="small" @click="handleAdd" v-auth="['sys:dict:add']">
            <template #icon>
              <n-icon><AddOutline /></n-icon>
            </template>
            新增
          </n-button>
        </n-space>
      </template>
    </TableMain>

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
          <n-divider title-placement="left">基础信息</n-divider>
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
          </n-grid>

          <n-divider title-placement="left">扩展描述</n-divider>
          <n-grid :cols="24" :x-gap="24">
            <n-form-item-gi :span="24" label="备注" path="remark">
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
          <n-button type="primary" :loading="submitting" :disabled="submitting" @click="handleSubmit">
            确定
          </n-button>
        </n-space>
      </template>
    </n-modal>
  </section>
</template>

<script lang="ts" setup>
import { h, ref, reactive, onMounted, computed } from 'vue'
import { useMessage, useDialog, NButton, NPopconfirm, NSpace, NTag } from 'naive-ui'
import { AddOutline, RefreshOutline } from '@vicons/ionicons5'
import type { FormInst } from 'naive-ui'
import { usePagination } from '@/hooks/useTable'
import { useDict } from '@/utils/useDict'
import { getDictTypePage, addDictType, updateDictType, detectDictType } from '@/api/system/dictType'
import TableMain from '@/components/table/main/TableMain.vue'
import type { SearchFormConfig } from '@/types/table'

// 组件通信
const emit = defineEmits(['select-dict'])

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
const formData = ref({
  dictId: null,
  dictName: '',
  dictType: '',
  remark: '',
  status: '0'
})

const searchModel = ref({
  dictName: '',
  dictType: '',
  status: null as string | null
})

const searchConfig = {
  title: '筛选条件',
  defaultCollapse: true
}

const searchFormConfig = computed<SearchFormConfig>(() => ({
  cols: 24,
  xGap: 16,
  items: [
    {
      key: 'dictName',
      label: '字典名称',
      type: 'input',
      placeholder: '请输入字典名称',
      span: 6
    },
    {
      key: 'dictType',
      label: '字典类型',
      type: 'input',
      placeholder: '请输入字典类型',
      span: 6
    },
    {
      key: 'status',
      label: '状态',
      type: 'select',
      options: sys_normal_disable.value,
      span: 6
    }
  ]
}))

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
        NTag,
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
    width: 180,
    render: (row: any) => {
      return h(NSpace, null, {
        default: () => [
          h(
            NButton,
            {
              type: 'primary',
              size: 'small',
              ghost: true,
              onClick: () => handleEdit(row),
              'v-auth': "['sys:dict:edit']"
            },
            { default: () => '编辑' }
          ),
          h(
            NPopconfirm,
            {
              onPositiveClick: () => handleDelete(row)
            },
            {
              default: () => '确认删除？',
              trigger: () =>
                h(
                  NButton,
                  {
                    type: 'error',
                    size: 'small',
                    ghost: true,
                    'v-auth': "['sys:dict:delete']"
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
    const params = pagination.getPageInfo(searchModel.value)
    const res = await getDictTypePage(params)
    if (res.code === 200) {
      tableData.value = res.rows || []
      pagination.setTotalSize(res.total || 0)
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
const handleSearch = (params: Record<string, any>) => {
  searchModel.value = { ...searchModel.value, ...params }
  pagination.page = 1
  loadTableData()
}

const handleReset = () => {
  searchModel.value = {
    dictName: '',
    dictType: '',
    status: null
  }
  pagination.page = 1
  loadTableData()
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
.dict-type-page {
  display: flex;
  flex-direction: column;
  gap: 16px;

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 16px;
    padding: 24px;
    border-radius: 16px;
    background: linear-gradient(135deg, #f5f7ff, #ffffff);
    border: 1px solid rgba(82, 106, 255, 0.12);

    .header-subtitle {
      font-size: 13px;
      color: var(--text-color-3);
      margin-bottom: 4px;
    }

    .header-desc {
      margin-top: 4px;
      color: var(--text-color-3);
    }

    .header-actions {
      display: flex;
      gap: 12px;
    }
  }

  .table-header {
    h3 {
      margin: 0;
      font-size: 18px;
    }

    p {
      margin: 4px 0 0;
      color: var(--text-color-3);
      font-size: 13px;
    }
  }
}
</style>
