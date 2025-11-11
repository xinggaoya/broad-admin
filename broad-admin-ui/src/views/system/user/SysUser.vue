<template>
  <section class="sys-user-page">
    <header class="page-header">
      <div>
        <p class="header-subtitle">系统管理</p>
        <h2>用户管理</h2>
      </div>
      <div class="header-actions">
        <n-button tertiary round size="small" :loading="tableLoading" @click="doRefresh">
          <template #icon>
            <n-icon><RefreshOutline /></n-icon>
          </template>
          刷新
        </n-button>
        <AddButton @add="handleAddUser" title="新增用户" v-auth="['sys:user:add']" />
        <DeleteButton
          @delete="handleBatchDelete"
          :disabled="!selectedRows.length"
          confirm-content="确定要删除选中的用户吗？"
          v-auth="['sys:user:delete']"
        />
        <ExportButton @export="handleExport" filename="用户列表" v-auth="['sys:user:export']" />
      </div>
    </header>

    <div class="sys-user-layout">
      <aside class="dept-panel">
        <div class="dept-panel__header">
          <h3>部门列表</h3>
          <n-input-group size="small">
            <n-input v-model:value="pattern" placeholder="搜索部门" clearable>
              <template #prefix>
                <n-icon><SearchOutline /></n-icon>
              </template>
            </n-input>
            <n-tooltip trigger="hover" placement="top">
              <template #trigger>
                <n-button size="small" quaternary circle @click="expandAllFlag = !expandAllFlag">
                  <template #icon>
                    <n-icon>
                      <component :is="expandAllFlag ? ChevronUpOutline : ChevronDownOutline" />
                    </n-icon>
                  </template>
                </n-button>
              </template>
              {{ expandAllFlag ? '收起' : '展开' }}
            </n-tooltip>
          </n-input-group>
        </div>
        <div class="dept-panel__body">
          <n-spin :show="deptLoading">
            <n-tree
              block-line
              :pattern="pattern"
              :data="departmentData"
              selectable
              v-model:default-expand-all="expandAllFlag"
              :on-update:selected-keys="onCheckedKeys"
            />
          </n-spin>
        </div>
      </aside>

      <n-card class="table-panel" :bordered="false">
        <TableMain
          :data="dataList"
          :columns="tableColumns"
          :loading="tableLoading"
          row-key="id"
          :pagination="pagination"
          :search-config="searchConfig"
          :search-form="searchForm"
          v-model:search-model="searchModel"
          @search="handleSearch"
          @reset="handleReset"
          @update:page="handlePageChange"
          @update:page-size="handlePageSizeChange"
          @update:checked-row-keys="handleSelectionChange"
        >
          <template #footer>
            <div class="table-footer">
              <n-space justify="space-between">
                <span>共 {{ pagination.total }} 条数据</span>
                <span>已选择 {{ selectedRows.length }} 项</span>
              </n-space>
            </div>
          </template>
        </TableMain>
      </n-card>
    </div>

    <!-- 用户表单弹窗 -->
    <n-modal
      v-model:show="showModelUpdate"
      :title="title"
      preset="card"
      :style="{ width: '600px' }"
      :mask-closable="false"
    >
      <n-spin :show="submitLoading">
        <n-form
          ref="userFormRef"
          :rules="userRules"
          :model="userForm"
          label-placement="left"
          label-width="80"
          require-mark-placement="right-hanging"
        >
          <n-grid :cols="24" :x-gap="24">
            <n-form-item-gi :span="12" label="用户名" path="userName">
              <n-input
                v-model:value="userForm.userName"
                :disabled="userForm.id != undefined"
                placeholder="请输入用户名"
              />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="昵称" path="nickName">
              <n-input v-model:value="userForm.nickName" placeholder="请输入昵称" />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="部门" path="deptId">
              <n-cascader
                v-model:value="userForm.deptId"
                :options="departmentData"
                placeholder="请选择部门"
                filterable
              />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="角色" path="roleIds">
              <n-select
                v-model:value="userForm.roleIds"
                :options="roleData"
                placeholder="请选择角色"
                multiple
                filterable
              />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="邮箱" path="email">
              <n-auto-complete
                v-model:value="userForm.email"
                :options="emailOptions"
                placeholder="请输入邮箱"
                :input-props="{ autocomplete: 'disabled' }"
              />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="手机号" path="mobile">
              <n-input v-model:value="userForm.mobile" placeholder="请输入手机号" />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="状态">
              <n-radio-group v-model:value="userForm.userStatus" name="userStatus">
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
          <n-button @click="showModelUpdate = false">取消</n-button>
          <n-button type="primary" :loading="submitLoading" @click="handleSubmit"> 确定 </n-button>
        </n-space>
      </template>
    </n-modal>

    <!-- 修改密码弹窗 -->
    <n-modal
      v-model:show="showUpdatePasswordModel"
      :title="'修改密码'"
      preset="card"
      :style="{ width: '400px' }"
      :mask-closable="false"
    >
      <n-form
        ref="updatePassWordFormRef"
        :rules="updatePassWordRules"
        :model="updatePassWordForm"
        label-placement="left"
        label-width="80"
        require-mark-placement="right-hanging"
      >
        <n-form-item label="密码" path="password">
          <n-input
            type="password"
            show-password-on="click"
            v-model:value="updatePassWordForm.password"
            placeholder="请输入密码"
          />
        </n-form-item>
        <n-form-item label="确认密码" path="password_two">
          <n-input
            type="password"
            show-password-on="click"
            v-model:value="updatePassWordForm.password_two"
            placeholder="请再次输入密码"
          />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showUpdatePasswordModel = false">取消</n-button>
          <n-button type="primary" @click="handleUpdatePassWordSubmit"> 确定 </n-button>
        </n-space>
      </template>
    </n-modal>
  </section>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted, h, reactive } from 'vue'
import {
  useMessage,
  useDialog,
  NAvatar,
  NTag,
  NSpace,
  NButton,
  NPopconfirm,
  type DataTableColumns
} from 'naive-ui'
import { SearchOutline, ChevronUpOutline, ChevronDownOutline } from '@vicons/ionicons5'
import { getUserPage, getUserRole, addUser, updateUser, delUser } from '@/api/system/user'
import { getRolePage } from '@/api/system/role'
import { getDeptPage } from '@/api/system/dept'
import { useDict } from '@/utils/useDict'
import { usePagination } from '@/hooks/useTable'
import TableMain from '@/components/table/main/TableMain.vue'
import AddButton from '@/components/table/button/AddButton.vue'
import DeleteButton from '@/components/table/button/DeleteButton.vue'
import ExportButton from '@/components/table/button/ExportButton.vue'
import type { SearchConfig, SearchFormConfig } from '@/types/table'

// 状态定义
const message = useMessage()
const dialog = useDialog()
const { sys_normal_disable } = useDict('sys_normal_disable')

// 弹窗标题
const title = ref('')

// 弹窗显示状态
const showModelUpdate = ref(false)
const showUpdatePasswordModel = ref(false)

// 加载状态
const deptLoading = ref(false)
const tableLoading = ref(false)
const submitLoading = ref(false)
const roleLoading = ref(false)

// 部门树相关
const pattern = ref('')
const expandAllFlag = ref(false)

interface SearchForm {
  nickName?: string
  userName?: string
  mobile?: string
  email?: string
  userStatus?: string | null
  deptId?: string | number
}

interface UserForm {
  id?: number
  userName: string
  nickName: string
  deptId: number | null
  roleIds: number[]
  email: string
  mobile: string
  userStatus: string // 改为字符串类型
  avatar?: string
  deptName?: string
}

const dataList = ref<UserForm[]>([])
const departmentData = ref<any[]>([])
const roleData = ref<Array<{ label: string; value: number; disabled?: boolean }>>([])
const selectedRows = ref<UserForm[]>([])

// 表单对象
const userFormRef = ref()
const updatePassWordFormRef = ref()

// 搜索模型
const searchModel = ref<SearchForm>({
  nickName: '',
  userName: '',
  mobile: '',
  email: '',
  userStatus: null,
  deptId: undefined
})

const userForm = ref<UserForm>({
  userName: '',
  nickName: '',
  deptId: null,
  roleIds: [],
  email: '',
  mobile: '',
  userStatus: '0'
})

const updatePassWordForm = ref({
  id: 0,
  password: '',
  password_two: ''
})

// 搜索配置
const searchConfig: SearchConfig = {
  show: true,
  title: '搜索条件',
  defaultCollapse: true,
  resetOnSearch: true,
  autoSearch: false, // 手动触发搜索
  searchDelay: 300
}

// 搜索表单配置
const searchForm: SearchFormConfig = {
  items: [
    {
      key: 'nickName',
      label: '昵称',
      type: 'input',
      placeholder: '请输入昵称',
      span: 6
    },
    {
      key: 'userName',
      label: '用户名',
      type: 'input',
      placeholder: '请输入用户名',
      span: 6
    },
    {
      key: 'mobile',
      label: '手机号',
      type: 'input',
      placeholder: '请输入手机号',
      span: 6
    },
    {
      key: 'email',
      label: '邮箱',
      type: 'input',
      placeholder: '请输入邮箱',
      span: 6
    },
    {
      key: 'userStatus',
      label: '状态',
      type: 'select',
      placeholder: '请选择状态',
      options: sys_normal_disable.value,
      span: 6
    }
  ],
  cols: 24,
  xGap: 24,
  yGap: 16,
  labelWidth: 80,
  labelPlacement: 'left'
}

// 表单校验规则
const userRules = {
  userName: { required: true, message: '请输入用户名', trigger: 'blur' },
  nickName: { required: true, message: '请输入昵称', trigger: 'blur' },
  deptId: { required: true, message: '请选择部门', trigger: 'change', type: 'number' },
  roleIds: { required: true, type: 'array', message: '请选择角色', trigger: 'change' },
  email: {
    required: true,
    message: '请输入邮箱',
    trigger: 'blur',
    pattern: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/
  },
  mobile: {
    required: true,
    message: '请输入手机号',
    trigger: 'blur',
    pattern: /^1[3-9]\d{9}$/
  }
}

const updatePassWordRules = {
  password: { required: true, message: '请输入密码', trigger: 'blur' },
  password_two: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (rule: any, value: string) => {
        return value === updatePassWordForm.value.password
      },
      message: '两次输入密码不一致',
      trigger: 'blur'
    }
  ]
}

// 邮箱自动完成选项
const emailOptions = computed(() => {
  const prefix = userForm.value.email?.split('@')[0] || ''
  return ['@gmail.com', '@163.com', '@qq.com'].map((suffix) => ({
    label: prefix + suffix,
    value: prefix + suffix
  }))
})

// 分页配置
const pagination = usePagination(() => doRefresh())

// 表格列配置 - 使用 ref 并创建新数组引用
const tableColumns = ref<DataTableColumns>([
  { type: 'selection' },
  { title: '昵称', key: 'nickName', width: 120 },
  { title: '用户名', key: 'userName', width: 120 },
  {
    title: '头像',
    key: 'avatar',
    width: 80,
    render: (row) => {
      const user = row as unknown as UserForm
      return h(
        NAvatar,
        {
          size: 'small',
          round: true,
          src: user.avatar || undefined,
          fallbackSrc: undefined
        },
        {
          default: () => user.nickName?.substring(0, 1) || ''
        }
      )
    }
  },
  { title: '部门', key: 'deptName', width: 120 },
  { title: '手机号', key: 'mobile', width: 140 },
  { title: '邮箱', key: 'email', width: 200 },
  {
    title: '状态',
    key: 'userStatus',
    width: 100,
    render: (row) => {
      const user = row as unknown as UserForm
      return h(
        NTag,
        {
          type: user.userStatus === '0' ? 'success' : 'error',
          round: true
        },
        {
          default: () => (user.userStatus === '0' ? '正常' : '停用')
        }
      )
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 300,
    fixed: 'right',
    render: (row) => {
      const user = row as unknown as UserForm
      return h(
        NSpace,
        {},
        {
          default: () => [
            h(
              NButton,
              {
                size: 'small',
                type: 'primary',
                ghost: true,
                onClick: () => handleEdit(user)
              },
              { default: () => '编辑' }
            ),
            h(
              NButton,
              {
                size: 'small',
                type: 'info',
                ghost: true,
                onClick: () => handleResetPassword(user)
              },
              { default: () => '重置密码' }
            ),
            h(
              NPopconfirm,
              {
                onPositiveClick: () => handleDelete(user)
              },
              {
                default: () => '确认删除该用户吗？',
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
  }
])

// 方法定义
function doRefresh() {
  tableLoading.value = true
  getUserPage(pagination.getPageInfo(searchModel.value))
    .then((res) => {
      dataList.value = res.rows || []
      pagination.setTotalSize(res.total || 0)
    })
    .catch((error) => {
      console.error('获取用户数据失败:', error)
      message.error('获取用户数据失败')
    })
    .finally(() => {
      tableLoading.value = false
    })
}

function handleSearch(params: Record<string, any>) {
  // 更新搜索模型
  Object.assign(searchModel.value, params)
  // 重置到第一页
  pagination.page = 1
  doRefresh()
  message.success('搜索完成')
}

function handleReset() {
  // 清空搜索模型
  Object.assign(searchModel.value, {
    nickName: '',
    userName: '',
    mobile: '',
    email: '',
    userStatus: null,
    deptId: undefined
  })
  // 重置到第一页
  pagination.page = 1
  doRefresh()
  message.success('重置成功')
}

// 分页处理
function handlePageChange(page: number) {
  pagination.page = page
  doRefresh()
}

function handlePageSizeChange(pageSize: number) {
  pagination.pageSize = pageSize
  pagination.page = 1
  doRefresh()
}

// 选择处理
function handleSelectionChange(keys: Array<string | number>) {
  selectedRows.value = dataList.value.filter((item) => keys.includes(item.id!))
}

async function loadRoles() {
  roleLoading.value = true
  try {
    const res = await getRolePage({})
    if (res.rows && Array.isArray(res.rows)) {
      roleData.value = res.rows.map((item: any) => ({
        label: item.name,
        value: item.id,
        disabled: item.status === '1' // 如果角色状态为1则禁用
      }))
    } else {
      console.warn('角色数据格式不正确:', res)
      message.warning('获取角色列表数据格式不正确')
    }
  } catch (error) {
    console.error('获取角色列表失败:', error)
    message.error('获取角色列表失败')
  } finally {
    roleLoading.value = false
  }
}

async function handleAddUser() {
  // 先加载角色列表
  if (roleData.value.length === 0) {
    await loadRoles()
  }

  title.value = '新增用户'
  userForm.value = {
    userName: '',
    nickName: '',
    deptId: null,
    roleIds: [],
    email: '',
    mobile: '',
    userStatus: '0'
  }
  showModelUpdate.value = true
}

async function handleEdit(row: UserForm) {
  // 先加载角色列表
  if (roleData.value.length === 0) {
    await loadRoles()
  }

  title.value = '编辑用户'
  userForm.value = { ...row }

  // 获取用户角色
  if (row.id) {
    submitLoading.value = true
    try {
      const res = await getUserRole(row.id)
      if (res.data) {
        userForm.value.roleIds = Array.isArray(res.data) ? res.data : [res.data]
      }
    } catch (error) {
      console.error('获取用户角色失败:', error)
      message.error('获取用户角色失败')
    } finally {
      submitLoading.value = false
    }
  }

  showModelUpdate.value = true
}

function handleDelete(row: UserForm) {
  if (!row.id) return
  delUser(row.id).then(() => {
    message.success('删除成功')
    doRefresh()
  })
}

function handleBatchDelete() {
  if (!selectedRows.value.length) {
    message.warning('请选择要删除的用户')
    return
  }

  const ids = selectedRows.value.map((row: UserForm) => row.id).filter(Boolean)
  if (ids.length) {
    delUser(ids.join(',')).then(() => {
      message.success('删除成功')
      selectedRows.value = []
      doRefresh()
    })
  }
}

function handleExport() {
  // TODO: 实现导出功能
  message.info('导出功能开发中')
}

function handleResetPassword(row: UserForm) {
  if (!row.id) return
  updatePassWordForm.value = {
    id: row.id,
    password: '',
    password_two: ''
  }
  showUpdatePasswordModel.value = true
}

async function handleSubmit() {
  const formRef = userFormRef.value
  if (!formRef?.validate) return

  try {
    await formRef.validate()
    submitLoading.value = true

    if (userForm.value.id) {
      await updateUser(userForm.value)
      message.success('更新成功')
    } else {
      await addUser(userForm.value)
      message.success('添加成功')
    }
    showModelUpdate.value = false
    doRefresh()
  } finally {
    submitLoading.value = false
  }
}

async function handleUpdatePassWordSubmit() {
  const formRef = updatePassWordFormRef.value
  if (!formRef?.validate) return

  try {
    await formRef.validate()
    // TODO: 实现密码修改
    message.success('密码修改成功')
    showUpdatePasswordModel.value = false
  } catch (error) {
    message.error('密码修改失败')
  }
}

function onCheckedKeys(keys: string[], _event: MouseEvent) {
  if (keys.length) {
    searchModel.value.deptId = keys[0]
    handleSearch(searchModel.value)
  }
}

// 初始化
onMounted(() => {
  // 加载部门树
  deptLoading.value = true
  getDeptPage({})
    .then(({ data }) => {
      // 处理部门树数据,添加key和value字段用于级联选择
      const processDeptData = (depts: any[]): any[] => {
        return depts.map((dept) => ({
          ...dept,
          key: dept.deptId,
          value: dept.deptId,
          label: dept.deptName,
          children: dept.children ? processDeptData(dept.children) : []
        }))
      }
      departmentData.value = processDeptData(data || [])
    })
    .finally(() => {
      deptLoading.value = false
    })

  // 加载角色列表
  loadRoles()

  // 加载用户列表
  doRefresh()
})
</script>

<style lang="scss" scoped>
.sys-user-container {
  .dept-tree-card {
    height: 100%;

    .dept-tree-content {
      height: calc(100vh - 300px);
      overflow: auto;

      .dept-tree {
        padding: 8px;
      }
    }
  }

  .table-title {
    h3 {
      margin: 0 0 4px 0;
      font-size: 18px;
      font-weight: 600;
      color: var(--text-color);
    }

    p {
      margin: 0;
      font-size: 14px;
      color: var(--text-color-2);
    }
  }

  .table-footer {
    padding: 12px 0;
    font-size: 14px;
    color: var(--text-color-2);
    border-top: 1px solid var(--divider-color);
  }

  :deep(.n-tag) {
    padding: 0 12px;
  }

  :deep(.n-button) {
    padding: 0 16px;
  }
}
</style>
