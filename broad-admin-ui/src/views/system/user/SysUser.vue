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
      <n-card class="dept-panel" :bordered="false">
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
      </n-card>

      <n-card class="table-panel" :bordered="false">
        <!-- 独立搜索区域 -->
        <n-card size="small" class="search-card" style="margin-bottom: 16px;">
          <template #header>
            <span>搜索条件</span>
          </template>
          <template #default>
            <n-form inline :label-width="80" :model="searchModel">
              <n-form-item label="昵称">
                <n-input
                  v-model:value="searchModel.nickName"
                  placeholder="请输入昵称"
                  clearable
                  @keydown.enter="handleSearchClick"
                />
              </n-form-item>
              <n-form-item label="用户名">
                <n-input
                  v-model:value="searchModel.userName"
                  placeholder="请输入用户名"
                  clearable
                  @keydown.enter="handleSearchClick"
                />
              </n-form-item>
              <n-form-item label="手机号">
                <n-input
                  v-model:value="searchModel.mobile"
                  placeholder="请输入手机号"
                  clearable
                  @keydown.enter="handleSearchClick"
                />
              </n-form-item>
              <n-form-item label="状态">
                <n-select
                  v-model:value="searchModel.userStatus"
                  placeholder="请选择状态"
                  clearable
                  :options="sys_normal_disable.value"
                />
              </n-form-item>
              <n-form-item>
                <n-space>
                  <n-button type="primary" @click="handleSearchClick">搜索</n-button>
                  <n-button @click="handleResetClick">重置</n-button>
                </n-space>
              </n-form-item>
            </n-form>
          </template>
        </n-card>

        <TableMain
          :data="dataList"
          :columns="tableColumns"
          :loading="tableLoading"
          row-key="id"
          :pagination="pagination"
          :scroll-x="tableScrollX"
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
            <n-form-item-gi v-if="!userForm.id" :span="12" label="初始密码" path="password">
              <n-input
                v-model:value="userForm.password"
                type="password"
                show-password-on="click"
                placeholder="请输入初始密码"
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
                :loading="roleLoading"
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
import { ref, computed, onMounted, h } from 'vue'
import {
  useMessage,
  NAvatar,
  NSpace,
  NButton,
  NPopconfirm,
  NSwitch,
  type DataTableColumns
} from 'naive-ui'
import {
  SearchOutline,
  ChevronUpOutline,
  ChevronDownOutline,
  RefreshOutline
} from '@vicons/ionicons5'
import {
  getUserPage,
  getUserMeta,
  addUser,
  updateUser,
  delUser,
  getUserInfo,
  resetUserPasswordApi,
  updateUserStatusApi
} from '@/api/system/user'
import { useDict } from '@/utils/useDict'
import { usePagination } from '@/hooks/useTable'
import TableMain from '@/components/table/main/TableMain.vue'
import AddButton from '@/components/table/button/AddButton.vue'
import DeleteButton from '@/components/table/button/DeleteButton.vue'
import ExportButton from '@/components/table/button/ExportButton.vue'
import type { SearchConfig, SearchFormConfig } from '@/types/table'

// 状态定义
const message = useMessage()
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

interface DeptMetaNode {
  id: number
  label: string
  children?: DeptMetaNode[]
}

interface DeptTreeNode {
  key: number
  value: number
  label: string
  children?: DeptTreeNode[]
}

type RoleOption = { label: string; value: number; disabled?: boolean }

interface UserForm {
  id?: number
  userName: string
  nickName: string
  deptId: number | null
  roleIds: number[]
  email: string
  mobile: string
  userStatus: string
  password?: string
  avatar?: string
  deptName?: string
}

type UserTableItem = UserForm & { deptName?: string }

interface PasswordForm {
  id: number | null
  password: string
  password_two: string
}

const dataList = ref<UserTableItem[]>([])
const departmentData = ref<DeptTreeNode[]>([])
const roleData = ref<RoleOption[]>([])
const selectedRows = ref<UserTableItem[]>([])

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

const createEmptyUserForm = (): UserForm => ({
  userName: '',
  nickName: '',
  deptId: null,
  roleIds: [],
  email: '',
  mobile: '',
  userStatus: '0',
  password: ''
})

const userForm = ref<UserForm>(createEmptyUserForm())

const updatePassWordForm = ref<PasswordForm>({
  id: null,
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
  password: {
    validator: (_rule: any, value: string) => {
      if (userForm.value.id) return true
      return !!value && value.length >= 6 && value.length <= 32
    },
    message: '请输入6-32位初始密码',
    trigger: 'blur'
  },
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
      const user = row as unknown as UserTableItem
      return h(
        NSwitch,
        {
          size: 'small',
          round: true,
          value: user.userStatus === '0',
          'on-update:value': (value: boolean) => handleStatusToggle(user, value)
        },
        {
          checked: () => '启用',
          unchecked: () => '停用'
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

const tableScrollX = computed(() => {
  const total = tableColumns.value.reduce((sum, column) => {
    const width = (column as any).width
    const minWidth = (column as any).minWidth
    if (typeof width === 'number') return sum + width
    if (typeof minWidth === 'number') return sum + minWidth
    return sum + 140
  }, 0)
  return Math.max(total, 960)
})

function mapDeptOptions(nodes: DeptMetaNode[] = []): DeptTreeNode[] {
  return nodes.map((node) => ({
    key: node.id,
    value: node.id,
    label: node.label,
    children: node.children && node.children.length ? mapDeptOptions(node.children) : []
  }))
}

async function ensureMetaReady() {
  if (departmentData.value.length === 0 || roleData.value.length === 0) {
    await loadMeta()
  }
}

// 方法定义
function doRefresh() {
  tableLoading.value = true
  getUserPage(pagination.getPageInfo(searchModel.value))
    .then((res: any) => {
      dataList.value = res.rows
      pagination.setTotalSize(res.total)
      tableLoading.value = false
    })
    .catch(console.log)
}

function handleSearchClick() {
  // 重置到第一页
  pagination.page = 1
  doRefresh()
  selectedRows.value = []
  message.success('搜索完成')
}

function handleResetClick() {
  // 清空搜索模型
  searchModel.value = {
    nickName: '',
    userName: '',
    mobile: '',
    email: '',
    userStatus: null,
    deptId: undefined
  }
  // 重置到第一页
  pagination.page = 1
  doRefresh()
  selectedRows.value = []
  message.success('重置成功')
}

function handleSearch(params: Record<string, any>) {
  // 更新搜索模型
  Object.assign(searchModel.value, params)
  // 重置到第一页
  pagination.page = 1
  doRefresh()
  selectedRows.value = []
  message.success('搜索完成')
}

function handleReset() {
  handleResetClick()
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
  const normalized = keys.map((key) => Number(key))
  selectedRows.value = dataList.value.filter((item) => item.id && normalized.includes(Number(item.id)))
}

async function loadMeta() {
  deptLoading.value = true
  roleLoading.value = true
  try {
    const res = await getUserMeta()
    departmentData.value = mapDeptOptions(res.data?.deptTree || [])
    roleData.value = (res.data?.roleOptions || [])
      .filter((item: any) => item.value !== null && item.value !== undefined)
      .map((item: any) => ({
        label: item.label,
        value: Number(item.value),
        disabled: item.status === '1'
      }))
  } catch (error) {
    console.error('获取部门或角色数据失败:', error)
    message.error('加载部门或角色信息失败')
  } finally {
    deptLoading.value = false
    roleLoading.value = false
  }
}

async function handleAddUser() {
  await ensureMetaReady()
  title.value = '新增用户'
  userForm.value = createEmptyUserForm()
  showModelUpdate.value = true
}

async function handleEdit(row: UserTableItem) {
  await ensureMetaReady()
  if (!row.id) {
    message.warning('无法识别该用户')
    return
  }
  title.value = '编辑用户'
  submitLoading.value = true
  try {
    const res = await getUserInfo(row.id)
    if (res.data) {
      userForm.value = {
        ...res.data,
        deptId: res.data.deptId ?? null,
        roleIds: Array.isArray(res.data.roleIds) ? res.data.roleIds : [],
        password: ''
      }
    }
    showModelUpdate.value = true
  } catch (error) {
    console.error('获取用户详情失败:', error)
    message.error('获取用户详情失败')
  } finally {
    submitLoading.value = false
  }
}

async function handleDelete(row: UserTableItem) {
  if (!row.id) return
  try {
    await delUser(row.id)
    message.success('删除成功')
    doRefresh()
  } catch (error) {
    console.error('删除用户失败:', error)
  }
}

async function handleBatchDelete() {
  if (!selectedRows.value.length) {
    message.warning('请选择要删除的用户')
    return
  }

  const ids = selectedRows.value.map((row: UserTableItem) => row.id).filter(Boolean)
  if (ids.length) {
    try {
      await delUser(ids as number[])
      message.success('批量删除成功')
      selectedRows.value = []
      doRefresh()
    } catch (error) {
      console.error('批量删除失败:', error)
    }
  }
}

function handleExport() {
  // TODO: 实现导出功能
  message.info('导出功能开发中')
}

function handleResetPassword(row: UserTableItem) {
  if (!row.id) return
  updatePassWordForm.value = {
    id: row.id ?? null,
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

    const payload = { ...userForm.value }
    if (payload.id) {
      delete payload.password
      await updateUser(payload)
      message.success('更新成功')
    } else {
      await addUser(payload)
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
    if (!updatePassWordForm.value.id) {
      message.warning('未选择用户')
      return
    }
    await resetUserPasswordApi({
      id: updatePassWordForm.value.id,
      password: updatePassWordForm.value.password
    })
    message.success('密码修改成功')
    showUpdatePasswordModel.value = false
  } catch (error) {
    message.error('密码修改失败')
  }
}

async function handleStatusToggle(row: UserTableItem, enabled: boolean) {
  if (!row.id) return
  const previous = row.userStatus
  row.userStatus = enabled ? '0' : '1'
  try {
    await updateUserStatusApi({
      id: row.id,
      userStatus: row.userStatus
    })
    message.success(enabled ? '已启用该用户' : '已停用该用户')
  } catch (error) {
    row.userStatus = previous
    console.error('更新状态失败:', error)
    message.error('更新用户状态失败')
  }
}

function onCheckedKeys(keys: Array<string | number>, _event: MouseEvent) {
  if (keys.length) {
    searchModel.value.deptId = Number(keys[0])
    handleSearch(searchModel.value)
  }
}

// 初始化
onMounted(async () => {
  await loadMeta()
  doRefresh()
})
</script>

<style lang="scss" scoped>
.sys-user-page {
  width: 100%;
  min-width: 0;
  .sys-user-layout {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    align-items: flex-start;
  }

  .dept-panel {
    flex: 0 0 280px;
    max-width: 100%;
    flex-shrink: 0;
    border-radius: var(--shell-radius-lg);
    background: var(--shell-surface);
    box-shadow: var(--shell-shadow);
  }

  .dept-panel__header {
    padding: 16px;
    border-bottom: 1px solid var(--divider-color);
    display: flex;
    flex-direction: column;
    gap: 12px;

    h3 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
      color: var(--shell-text-color);
    }
  }

  .dept-panel__body {
    padding: 16px;
    max-height: 600px;
    overflow: auto;
  }

  .table-panel {
    flex: 1 1 520px;
    min-width: 0;
    border-radius: var(--shell-radius-lg);
    background: var(--shell-surface);
    box-shadow: var(--shell-shadow);
  }

  .search-card {
    :deep(.n-card__content) {
      padding: 16px;
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

  :deep(.n-form.n-form--inline) {
    flex-wrap: wrap;
    gap: 12px 16px;
  }

  :deep(.n-form-item) {
    margin-right: 0;
  }
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--shell-surface);
  border-radius: var(--shell-radius-lg);
  padding: 20px;
  box-shadow: var(--shell-shadow);
  margin-bottom: var(--shell-gap);
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

@media (max-width: 768px) {
  .page-header {
    flex-direction: column;
    gap: 12px;
  }

  .sys-user-page .sys-user-layout {
    flex-direction: column;
  }

  .dept-panel {
    width: 100%;
    flex: 1 1 auto;
  }

  .table-panel {
    width: 100%;
  }
}
</style>
