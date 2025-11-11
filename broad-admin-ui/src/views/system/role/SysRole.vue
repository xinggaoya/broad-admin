<template>
  <section class="sys-role-page">
    <header class="page-header">
      <div>
        <p class="header-subtitle">系统管理</p>
        <h2>角色管理</h2>
        <p class="header-desc">灵活配置角色权限，让团队协作更安全。</p>
      </div>
      <div class="header-metrics">
        <div class="metric">
          <span>角色总数</span>
          <strong>{{ roleSummary.total }}</strong>
        </div>
        <div class="metric">
          <span>启用中</span>
          <strong>{{ roleSummary.active }}</strong>
        </div>
      </div>
      <div class="header-actions">
        <n-button tertiary round size="small" :loading="tableLoading" @click="handleRefresh">
          <template #icon>
            <n-icon><RefreshOutline /></n-icon>
          </template>
          刷新
        </n-button>
        <n-button type="primary" round @click="handleAdd" v-auth="['sys:role:add']">
          <template #icon>
            <n-icon><AddOutline /></n-icon>
          </template>
          新增角色
        </n-button>
      </div>
    </header>

    <n-card :bordered="false" class="role-table-card">
      <TableMain
        ref="tableMainRef"
        :columns="columns"
        :data="tableData"
        :loading="tableLoading"
        row-key="id"
        :pagination="tablePagination"
        :search-config="searchConfig"
        :search-form="searchFormConfig"
        v-model:search-model="searchModel"
        sticky-toolbar
        @search="handleSearch"
        @reset="handleReset"
        @refresh="handleRefresh"
        @update:page="pagination.onChange"
        @update:page-size="pagination.onPageSizeChange"
      >
        <template #header>
          <div class="table-title">
            <h3>角色列表</h3>
            <p>角色能力与权限实时同步，支持快速检索。</p>
          </div>
        </template>
        <template #header-extra>
          <n-space>
            <n-button tertiary round size="small" :loading="tableLoading" @click="handleRefresh">
              <template #icon>
                <n-icon><RefreshOutline /></n-icon>
              </template>
              刷新
            </n-button>
            <n-button type="primary" round @click="handleAdd" v-auth="['sys:role:add']">
              <template #icon>
                <n-icon><AddOutline /></n-icon>
              </template>
              新增角色
            </n-button>
          </n-space>
        </template>
      </TableMain>
    </n-card>

    <!-- 新增/编辑角色 -->
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
          <n-divider title-placement="left">基本信息</n-divider>
          <n-grid :cols="24" :x-gap="24">
            <n-form-item-gi :span="12" label="角色名称" path="name">
              <n-input v-model:value="formData.name" placeholder="请输入角色名称" />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="角色编号" path="halfRules">
              <n-input v-model:value="formData.halfRules" placeholder="请输入角色编号" />
            </n-form-item-gi>
            <n-form-item-gi :span="24" label="角色描述" path="remarks">
              <n-input
                v-model:value="formData.remarks"
                type="textarea"
                :autosize="{ minRows: 3, maxRows: 5 }"
                placeholder="请输入角色描述"
              />
            </n-form-item-gi>
          </n-grid>

          <n-divider title-placement="left">状态</n-divider>
          <n-form-item label="角色状态" path="status">
            <n-radio-group v-model:value="formData.status">
              <n-space>
                <n-radio v-for="item in sys_normal_disable" :key="item.value" :value="item.value">
                  {{ item.label }}
                </n-radio>
              </n-space>
            </n-radio-group>
          </n-form-item>
        </n-form>
      </n-spin>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showEditModal = false">取消</n-button>
          <n-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</n-button>
        </n-space>
      </template>
    </n-modal>

    <!-- 菜单权限分配 -->
    <n-modal
      v-model:show="showMenuModal"
      title="分配菜单权限"
      preset="card"
      :style="{ width: '640px' }"
      :mask-closable="false"
    >
      <n-spin :show="menuLoading">
        <div class="menu-tree-wrapper">
          <div class="menu-tree-header">
            <span>选择需要授权的菜单节点</span>
            <n-input
              v-model:value="menuTreeKeyword"
              size="small"
              placeholder="搜索菜单"
              clearable
            >
              <template #prefix>
                <n-icon><SearchOutline /></n-icon>
              </template>
            </n-input>
          </div>
          <n-tree
            block-line
            checkable
            cascade
            selectable
            :pattern="menuTreeKeyword"
            :data="menuTreeData"
            :checked-keys="checkedMenuKeys"
            :check-strategy="'all'"
            @update:checked-keys="(keys) => (checkedMenuKeys.value = keys as string[])"
          />
        </div>
      </n-spin>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showMenuModal = false">取消</n-button>
          <n-button type="primary" :loading="submitLoading" @click="handleMenuSubmit">
            保存
          </n-button>
        </n-space>
      </template>
    </n-modal>
  </section>
</template>

<script lang="ts" setup>
import { h, ref, reactive, computed } from 'vue'
import { useMessage, useDialog, NTag, NSpace, NButton, NPopconfirm } from 'naive-ui'
import type { FormInst, TreeOption } from 'naive-ui'
import {
  AddOutline,
  RefreshOutline,
  SearchOutline,
  ShieldCheckmarkOutline
} from '@vicons/ionicons5'
import { useDict } from '@/utils/useDict'
import { usePagination } from '@/hooks/useTable'
import TableMain from '@/components/table/main/TableMain.vue'
import type { SearchFormConfig } from '@/types/table'
import { getRolePage, addRole, updateRole, delRole } from '@/api/system/role'
import { getRoleMenu, addRoleMenu } from '@/api/system/roleMenu'
import { getMenusTree } from '@/api/system/menu'

interface RoleEntity {
  id: number
  name: string
  halfRules: string
  remarks?: string
  status: string
  createTime?: string
}

const tableMainRef = ref()
const message = useMessage()
const dialog = useDialog()
const formRef = ref<FormInst | null>(null)
const { sys_normal_disable } = useDict('sys_normal_disable')

const tableLoading = ref(false)
const tableData = ref<RoleEntity[]>([])
const searchModel = ref({
  name: '',
  halfRules: '',
  status: null as string | null
})

const searchConfig = {
  title: '筛选条件',
  defaultCollapse: true
}

const searchFormConfig = computed<SearchFormConfig>(() => ({
  cols: 24,
  xGap: 16,
  yGap: 12,
  items: [
    {
      key: 'name',
      label: '角色名称',
      type: 'input',
      placeholder: '请输入角色名称',
      span: 6
    },
    {
      key: 'halfRules',
      label: '角色编号',
      type: 'input',
      placeholder: '请输入角色编号',
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

async function loadTableData(override?: Record<string, any>) {
  tableLoading.value = true
  try {
    const params = pagination.getPageInfo({
      ...searchModel.value,
      ...override
    })
    const res = await getRolePage(params)
    if (res.code === 200) {
      tableData.value = res.rows || []
      pagination.setTotalSize(res.total || 0)
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

const pagination = usePagination(() => loadTableData())

const tablePagination = computed(() => ({
  page: pagination.page,
  pageSize: pagination.pageSize,
  itemCount: pagination.total,
  showSizePicker: pagination.showSizePicker,
  showQuickJumper: pagination.showQuickJumper,
  pageSizes: pagination.pageSizes
}))

const roleSummary = computed(() => {
  const total = tableData.value.length
  const active = tableData.value.filter((item) => item.status === '0').length
  return { total, active }
})

const handleSearch = (params: Record<string, any>) => {
  searchModel.value = { ...searchModel.value, ...params }
  pagination.page = 1
  loadTableData(params)
}

const handleReset = () => {
  searchModel.value = {
    name: '',
    halfRules: '',
    status: null
  }
  pagination.page = 1
  loadTableData()
}

const handleRefresh = () => {
  loadTableData()
}

const columns = [
  { title: '角色名称', key: 'name', minWidth: 140 },
  { title: '角色编号', key: 'halfRules', minWidth: 120 },
  { title: '角色描述', key: 'remarks' },
  {
    title: '状态',
    key: 'status',
    width: 120,
    render: (row: RoleEntity) => {
      const status = sys_normal_disable.value.find((item) => item.value === row.status)
      return h(
        NTag,
        {
          round: true,
          size: 'small',
          type: row.status === '0' ? 'success' : 'error'
        },
        { default: () => status?.label || '未知' }
      )
    }
  },
  { title: '创建时间', key: 'createTime', width: 180 },
  {
    title: '操作',
    key: 'actions',
    width: 260,
    fixed: 'right' as const,
    render: (row: RoleEntity) =>
      h(
        NSpace,
        { justify: 'start' },
        {
          default: () => [
            h(
              NButton,
              {
                size: 'small',
                type: 'primary',
                quaternary: true,
                onClick: () => handleEdit(row)
              },
              { default: () => '编辑' }
            ),
            h(
              NButton,
              {
                size: 'small',
                type: 'info',
                quaternary: true,
                onClick: () => handleAssignMenu(row)
              },
              {
                default: () => [
                  h(
                    NIcon,
                    { size: 14, style: 'margin-right: 4px;' },
                    { default: () => h(ShieldCheckmarkOutline) }
                  ),
                  ' 权限'
                ]
              }
            ),
            h(
              NPopconfirm,
              { onPositiveClick: () => handleDelete(row) },
              {
                default: () => '确认删除？',
                trigger: () =>
                  h(
                    NButton,
                    {
                      size: 'small',
                      type: 'error',
                      ghost: true
                    },
                    { default: () => '删除' }
                  )
              }
            )
          ]
        }
      )
  }
]

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

const handleAdd = () => {
  editType.value = 'add'
  Object.assign(formData, {
    id: null,
    name: '',
    halfRules: '',
    remarks: '',
    status: '0'
  })
  showEditModal.value = true
}

const handleEdit = (row: RoleEntity) => {
  editType.value = 'edit'
  Object.assign(formData, row)
  showEditModal.value = true
}

const handleSubmit = async () => {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    const payload = Object.entries(formData).reduce((acc, [key, value]) => {
      if (value !== '' && value !== null && value !== undefined) {
        acc[key] = value
      }
      return acc
    }, {} as Record<string, any>)
    const res = editType.value === 'add' ? await addRole(payload) : await updateRole(payload)
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

const handleDelete = (row: RoleEntity) => {
  dialog.warning({
    title: '删除确认',
    content: `确定要删除角色「${row.name}」吗？`,
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: async () => {
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
  })
}

const showMenuModal = ref(false)
const menuLoading = ref(false)
const menuTreeData = ref<TreeOption[]>([])
const checkedMenuKeys = ref<string[]>([])
const menuTreeKeyword = ref('')
const currentRole = ref<RoleEntity | null>(null)

const buildMenuTree = (data: any[]): TreeOption[] => {
  return data.map((item) => ({
    key: String(item.menuId),
    label: item.menuName,
    children: item.children ? buildMenuTree(item.children) : undefined
  }))
}

const handleAssignMenu = async (row: RoleEntity) => {
  currentRole.value = row
  menuLoading.value = true
  showMenuModal.value = true
  try {
    const menuRes = await getMenusTree()
    if (menuRes.code === 200) {
      menuTreeData.value = buildMenuTree(menuRes.data || [])
    }
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
    const menuIds = checkedMenuKeys.value.map((key) => Number(key))
    const res = await addRoleMenu({
      roleId: currentRole.value.id,
      menuIds
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

loadTableData()
</script>

<style lang="scss" scoped>
.sys-role-page {
  display: flex;
  flex-direction: column;
  gap: 16px;

  .page-header {
    display: grid;
    grid-template-columns: 1fr auto auto;
    gap: 16px;
    padding: 24px;
    border-radius: 16px;
    background: linear-gradient(135deg, #f8fbff, #ffffff);
    border: 1px solid rgba(82, 106, 255, 0.12);

    .header-subtitle {
      font-size: 13px;
      color: var(--text-color-3);
      margin-bottom: 4px;
    }

    h2 {
      margin: 0;
      font-size: 24px;
    }

    .header-desc {
      margin-top: 4px;
      color: var(--text-color-2);
    }

    .header-metrics {
      display: flex;
      align-items: center;
      gap: 16px;

      .metric {
        min-width: 100px;
        padding: 12px 16px;
        border-radius: 12px;
        background: var(--card-color);
        border: 1px solid var(--divider-color);

        span {
          display: block;
          font-size: 12px;
          color: var(--text-color-3);
        }

        strong {
          font-size: 22px;
          color: var(--text-color);
        }
      }
    }

    .header-actions {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      gap: 12px;
    }
  }

  .role-table-card {
    min-height: 600px;
  }

  .table-title {
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

  .menu-tree-wrapper {
    max-height: 520px;
    overflow: auto;

    .menu-tree-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 12px;
      color: var(--text-color-3);
    }
  }
}

@media (max-width: 1200px) {
  .sys-role-page {
    .page-header {
      grid-template-columns: 1fr;
      text-align: left;
    }
  }
}
</style>
