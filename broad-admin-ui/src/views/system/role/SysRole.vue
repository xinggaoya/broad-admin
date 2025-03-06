<template>
  <div class="role-container">
    <!-- 搜索区域 -->
    <TableSearch @search="handleSearch" @reset="handleReset">
      <n-form label-placement="left" inline>
        <n-grid :cols="24" :x-gap="24">
          <n-form-item-gi :span="6" label="角色名称">
            <n-input v-model:value="searchForm.name" placeholder="请输入角色名称" clearable />
          </n-form-item-gi>
          <n-form-item-gi :span="6" label="角色编号">
            <n-input v-model:value="searchForm.halfRules" placeholder="请输入角色编号" clearable />
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
      :pagination="pagination"
      row-key="id"
    >
      <template #header>
        <n-button-group>
          <n-button type="primary" @click="handleAdd" v-auth="['sys:role:add']">
            <template #icon>
              <n-icon><AddOutline /></n-icon>
            </template>
            新增角色
          </n-button>
        </n-button-group>
      </template>
    </TableMain>

    <!-- 编辑角色对话框 -->
    <n-modal
      v-model:show="showEditModal"
      :title="editType === 'add' ? '新增角色' : '编辑角色'"
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
            <n-form-item-gi :span="24" label="角色名称" path="name">
              <n-input v-model:value="formData.name" placeholder="请输入角色名称" />
            </n-form-item-gi>
            <n-form-item-gi :span="24" label="角色编号" path="halfRules">
              <n-input v-model:value="formData.halfRules" placeholder="请输入角色编号" />
            </n-form-item-gi>
            <n-form-item-gi :span="24" label="角色描述" path="remarks">
              <n-input
                v-model:value="formData.remarks"
                type="textarea"
                placeholder="请输入角色描述"
                :autosize="{ minRows: 3, maxRows: 5 }"
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

    <!-- 菜单权限对话框 -->
    <n-modal
      v-model:show="showMenuModal"
      title="分配菜单权限"
      preset="card"
      :style="{ width: '600px' }"
      :mask-closable="false"
    >
      <n-spin :show="menuLoading">
        <n-tree
          v-model:checked-keys="checkedMenuKeys"
          :data="menuTreeData"
          checkable
          cascade
          selectable
          :check-strategy="'all'"
        />
      </n-spin>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showMenuModal = false">取消</n-button>
          <n-button type="primary" :loading="submitLoading" @click="handleMenuSubmit"
            >确定</n-button
          >
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script lang="ts" setup>
import { h, ref, reactive, onMounted } from 'vue'
import { useMessage, useDialog, NTag, NSpace, NButton, NPopconfirm } from 'naive-ui'
import type { FormInst, TreeOption } from 'naive-ui'
import { SearchOutline, RefreshOutline, AddOutline } from '@vicons/ionicons5'
import { getRolePage, addRole, updateRole, delRole } from '@/api/system/role'
import { getRoleMenu, addRoleMenu } from '@/api/system/roleMenu'
import { getMenusTree } from '@/api/system/menu'
import { useDict } from '@/utils/useDict'
import { usePagination } from '@/hooks/useTable'
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
  name: '',
  halfRules: '',
  status: null
})

// 表格数据
const tableLoading = ref(false)
const tableData = ref<any[]>([])

// 分页配置
const pagination = usePagination(() => loadTableData())

// 表格列定义
const columns = [
  { title: '角色名称', key: 'name' },
  { title: '角色编号', key: 'halfRules' },
  { title: '角色描述', key: 'remarks' },
  {
    title: '状态',
    key: 'status',
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
  { title: '创建时间', key: 'createTime' },
  {
    title: '操作',
    key: 'actions',
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
            NButton,
            {
              type: 'info',
              size: 'small',
              ghost: true,
              onClick: () => handleAssignMenu(row)
            },
            { default: () => '分配权限' }
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
  id: null as number | null,
  name: '',
  halfRules: '',
  remarks: '',
  status: '0'
})

// 表单校验规则
const rules = {
  name: {
    required: true,
    message: '请输入角色名称',
    trigger: 'blur'
  },
  halfRules: {
    required: true,
    message: '请输入角色编号',
    trigger: 'blur'
  }
}

// 菜单权限相关
const showMenuModal = ref(false)
const menuLoading = ref(false)
const menuTreeData = ref<TreeOption[]>([])
const checkedMenuKeys = ref<string[]>([])
const currentRole = ref<any>(null)

// 加载表格数据
const loadTableData = async () => {
  tableLoading.value = true
  try {
    // 处理请求参数，过滤掉值为null的字段
    const params = Object.entries({
      ...searchForm,
      page: pagination.page,
      pageSize: pagination.pageSize
    }).reduce(
      (acc, [key, value]) => {
        if (value !== null && value !== '') {
          acc[key] = value
        }
        return acc
      },
      {} as Record<string, any>
    )

    const res = await getRolePage(params)
    if (res.code === 200) {
      tableData.value = res.rows || []
      pagination.itemCount = res.total || 0
    } else {
      message.error(res.message || '加载角色列表失败')
    }
  } catch (error) {
    console.error('加载角色列表失败:', error)
    message.error('加载角色列表失败')
  } finally {
    tableLoading.value = false
  }
}

// 搜索和重置
const handleSearch = () => {
  pagination.page = 1
  loadTableData()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.halfRules = ''
  searchForm.status = null
  handleSearch()
}

// 新增和编辑角色
const handleAdd = () => {
  editType.value = 'add'
  formData.id = null
  formData.name = ''
  formData.halfRules = ''
  formData.remarks = ''
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

    const res = editType.value === 'add' ? await addRole(params) : await updateRole(params)

    if (res.code === 200) {
      message.success(editType.value === 'add' ? '新增成功' : '更新成功')
      showEditModal.value = false
      loadTableData()
    } else {
      message.error(res.message || '保存失败')
    }
  } catch (error) {
    console.error('保存角色失败:', error)
    message.error('保存失败')
  } finally {
    submitLoading.value = false
  }
}

// 删除角色
const handleDelete = async (row: any) => {
  try {
    const res = await delRole(row.id)
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
    console.error('删除角色失败:', error)
    message.error('删除失败')
  }
}

// 处理菜单树数据
const handleMenuTreeData = (data: any[]): TreeOption[] => {
  return data.map((item) => ({
    key: String(item.menuId),
    label: item.menuName,
    children: item.children ? handleMenuTreeData(item.children) : undefined
  }))
}

// 分配菜单权限
const handleAssignMenu = async (row: any) => {
  currentRole.value = row
  menuLoading.value = true
  showMenuModal.value = true
  try {
    // 加载菜单树
    const menuRes = await getMenusTree()
    if (menuRes.code === 200) {
      menuTreeData.value = handleMenuTreeData(menuRes.data || [])
    }

    // 加载已分配的菜单
    const roleMenuRes = await getRoleMenu({ roleId: row.id })
    if (roleMenuRes.code === 200) {
      checkedMenuKeys.value = roleMenuRes.data?.map((item: any) => String(item.menuId)) || []
    }
  } catch (error) {
    console.error('加载菜单数据失败:', error)
    message.error('加载菜单数据失败')
  } finally {
    menuLoading.value = false
  }
}

const handleMenuSubmit = async () => {
  if (!currentRole.value) return
  submitLoading.value = true
  try {
    // 将字符串类型的菜单ID转换回数字类型，后端可能需要数字类型
    const menuIds = checkedMenuKeys.value.map((key) => Number(key))
    const res = await addRoleMenu({
      roleId: currentRole.value.id,
      menuIds: menuIds
    })
    if (res.code === 200) {
      message.success('分配权限成功')
      showMenuModal.value = false
    } else {
      message.error(res.message || '分配权限失败')
    }
  } catch (error) {
    console.error('分配权限失败:', error)
    message.error('分配权限失败')
  } finally {
    submitLoading.value = false
  }
}

// 页面加载时获取数据
onMounted(() => {
  loadTableData()
})
</script>

<style lang="scss" scoped>
.role-container {
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
