<template>
  <div class="dept-container">
    <!-- 搜索区域 -->
    <TableSearch @search="handleSearch" @reset="handleReset">
      <n-form label-placement="left" inline>
        <n-grid :cols="24" :x-gap="24">
          <n-form-item-gi :span="6" label="部门名称">
            <n-input v-model:value="searchForm.deptName" placeholder="请输入部门名称" clearable />
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
      :loading="tableLoading"
      row-key="deptId"
      :children-key="childrenKey"
    >
      <template #header>
        <n-button-group>
          <n-button type="primary" @click="handleAdd" v-auth="['sys:dept:add']">
            <template #icon>
              <n-icon><AddOutline /></n-icon>
            </template>
            新增部门
          </n-button>
        </n-button-group>
      </template>
    </TableMain>

    <!-- 编辑部门对话框 -->
    <n-modal
      v-model:show="showEditModal"
      :title="editType === 'add' ? '新增部门' : '编辑部门'"
      preset="card"
      :style="{ width: '600px' }"
      :mask-closable="false"
    >
      <n-spin :show="submitLoading">
        <n-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-placement="left"
          label-width="100"
          require-mark-placement="right-hanging"
        >
          <n-grid :cols="24" :x-gap="24">
            <n-form-item-gi :span="24" label="上级部门" path="parentId">
              <n-tree-select
                v-model:value="formData.parentId"
                :options="treeSelectOptions"
                placeholder="选择上级部门"
                clearable
              />
            </n-form-item-gi>
            <n-form-item-gi :span="24" label="部门名称" path="deptName">
              <n-input v-model:value="formData.deptName" placeholder="请输入部门名称" />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="负责人" path="leader">
              <n-input v-model:value="formData.leader" placeholder="请输入负责人" />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="联系电话" path="phone">
              <n-input v-model:value="formData.phone" placeholder="请输入联系电话" />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="邮箱" path="email">
              <n-input v-model:value="formData.email" placeholder="请输入邮箱" />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="显示顺序" path="orderNum">
              <n-input-number
                v-model:value="formData.orderNum"
                :min="0"
                placeholder="请输入显示顺序"
                clearable
              />
            </n-form-item-gi>
            <n-form-item-gi :span="24" label="状态" path="status">
              <n-radio-group v-model:value="formData.status" name="status">
                <n-space>
                  <n-radio v-for="item in sys_normal_disable" :key="item.value" :value="item.value">
                    {{ item.label }}
                  </n-radio>
                </n-space>
              </n-radio-group>
            </n-form-item-gi>
          </n-grid>
        </n-form>
      </n-spin>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showEditModal = false">取消</n-button>
          <n-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script lang="ts" setup>
import { h, ref, reactive, onMounted } from 'vue'
import { useMessage, useDialog, NTag, NSpace, NButton, NPopconfirm } from 'naive-ui'
import type { FormInst, TreeSelectOption } from 'naive-ui'
import { SearchOutline, RefreshOutline, AddOutline } from '@vicons/ionicons5'
import { getDeptPage, addDept, updateDept, deleteDept } from '@/api/system/dept'
import { useDict } from '@/utils/useDict'
import TableSearch from '@/components/table/search/TableSearch.vue'
import TableMain from '@/components/table/main/TableMain.vue'

// 字典数据
const { sys_normal_disable } = useDict('sys_normal_disable')

// 组件实例
const message = useMessage()
const dialog = useDialog()
const formRef = ref<FormInst | null>(null)

// 搜索表单
const searchForm = reactive({
  deptName: '',
  status: null
})

// 表格数据
const tableLoading = ref(false)
const tableData = ref<any[]>([])
const rowKey = (row: any) => row.deptId
const childrenKey = 'children'

// 表格列定义
const columns = [
  { title: '部门名称', key: 'deptName', width: 200 },
  { title: '负责人', key: 'leader' },
  { title: '联系电话', key: 'phone' },
  { title: '邮箱', key: 'email' },
  { title: '显示顺序', key: 'orderNum', width: 100 },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row: any) => {
      const status = sys_normal_disable.value.find(
        (item: { value: string; label: string }) => item.value === row.status
      )
      return h(
        NTag,
        {
          type: row.status === '0' ? 'success' : 'error',
          round: true
        },
        { default: () => status?.label || '未知' }
      )
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 200,
    fixed: 'right' as const,
    render: (row: any) => {
      return h(NSpace, null, {
        default: () => [
          h(
            NButton,
            {
              type: 'primary',
              size: 'small',
              ghost: true,
              onClick: () => handleEdit(row)
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
                    ghost: true
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

// 表单数据
const showEditModal = ref(false)
const editType = ref<'add' | 'edit'>('add')
const submitLoading = ref(false)
const formData = reactive({
  deptId: null as number | null,
  parentId: 0,
  ancestors: '',
  deptName: '',
  leader: '',
  phone: '',
  email: '',
  orderNum: 0,
  status: '0',
  delFlag: '0'
})

// 表单校验规则
const rules = {
  deptName: {
    required: true,
    message: '请输入部门名称',
    trigger: 'blur'
  },
  orderNum: {
    required: true,
    message: '请输入显示顺序',
    trigger: 'blur'
  }
}

// 部门树选择数据
const treeSelectOptions = ref<TreeSelectOption[]>([])

// 处理树形数据
const handleTreeData = (data: any[]): TreeSelectOption[] => {
  return data.map((item) => ({
    label: item.deptName,
    key: item.deptId,
    value: item.deptId,
    children: item.children && item.children.length > 0 ? handleTreeData(item.children) : undefined,
    disabled:
      editType.value === 'edit' &&
      (item.deptId === formData.deptId || // 不能选择自己作为父部门
        (formData.ancestors && formData.ancestors.split(',').includes(item.deptId.toString()))) // 不能选择子部门作为父部门
  }))
}

// 加载表格数据
const loadTableData = async () => {
  tableLoading.value = true
  try {
    // 处理请求参数，过滤掉值为null的字段
    const params = Object.entries(searchForm).reduce(
      (acc, [key, value]) => {
        if (value !== null && value !== '') {
          acc[key] = value
        }
        return acc
      },
      {} as Record<string, any>
    )

    const res = await getDeptPage(params)
    if (res.code === 200) {
      tableData.value = res.data || []
      // 构建树选择数据，需要排除当前编辑的部门及其子部门
      treeSelectOptions.value = [
        {
          label: '顶级部门',
          key: 0,
          value: 0
        },
        ...handleTreeData(res.data || [])
      ]
    } else {
      message.error(res.message || '加载部门列表失败')
    }
  } catch (error) {
    console.error('加载部门列表失败:', error)
    message.error('加载部门列表失败')
  } finally {
    tableLoading.value = false
  }
}

// 搜索和重置
const handleSearch = () => {
  loadTableData()
}

const handleReset = () => {
  searchForm.deptName = ''
  searchForm.status = null
  handleSearch()
}

// 新增和编辑部门
const handleAdd = () => {
  editType.value = 'add'
  formData.deptId = null
  formData.parentId = 0
  formData.deptName = ''
  formData.leader = ''
  formData.phone = ''
  formData.email = ''
  formData.orderNum = 0
  formData.status = '0'
  showEditModal.value = true
}

const handleEdit = (row: any) => {
  editType.value = 'edit'
  Object.assign(formData, row)
  showEditModal.value = true
}

const handleSubmit = async () => {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    // 处理请求参数，过滤掉值为null的字段
    const params = Object.entries(formData).reduce(
      (acc, [key, value]) => {
        if (value !== null && value !== '') {
          acc[key] = value
        }
        return acc
      },
      {} as Record<string, any>
    )

    const res = editType.value === 'add' ? await addDept(params) : await updateDept(params)

    if (res.code === 200) {
      message.success(editType.value === 'add' ? '新增成功' : '更新成功')
      showEditModal.value = false
      loadTableData()
    } else {
      message.error(res.message || '保存失败')
    }
  } catch (error) {
    console.error('保存部门失败:', error)
    message.error('保存失败')
  } finally {
    submitLoading.value = false
  }
}

// 删除部门
const handleDelete = async (row: any) => {
  try {
    const res = await deleteDept({ idList: [row.deptId] })
    if (res.code === 200) {
      message.success('删除成功')
      loadTableData()
    } else {
      message.error(res.message || '删除失败')
    }
  } catch (error) {
    console.error('删除部门失败:', error)
    message.error('删除失败')
  }
}

// 页面加载时获取数据
onMounted(() => {
  loadTableData()
})
</script>

<style lang="scss" scoped>
.dept-container {

  .search-card {
    margin-bottom: 16px;
  }

  .table-card {
    :deep(.n-data-table-wrapper) {
      border-radius: 8px;
    }
  }

  :deep(.n-tag) {
    padding: 0 12px;
  }

  :deep(.n-button) {
    padding: 0 16px;
  }
}
</style>
