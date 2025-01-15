<template>
  <div class="sys-user-container">
    <!-- 搜索区域 -->
    <TableSearch @search="handleSearch" @reset="handleReset">
      <n-form label-placement="left" inline>
        <n-grid :cols="24" :x-gap="24">
          <n-form-item-gi :span="6" label="昵称">
            <n-input v-model:value="searchForm.nickName" placeholder="请输入昵称" />
          </n-form-item-gi>
          <n-form-item-gi :span="6" label="用户名">
            <n-input v-model:value="searchForm.userName" placeholder="请输入用户名" />
          </n-form-item-gi>
          <n-form-item-gi :span="6" label="手机号">
            <n-input v-model:value="searchForm.mobile" placeholder="请输入手机号" />
          </n-form-item-gi>
          <n-form-item-gi :span="6" label="邮箱">
            <n-input v-model:value="searchForm.email" placeholder="请输入邮箱" />
          </n-form-item-gi>
          <n-form-item-gi :span="6" label="状态">
            <n-select
              v-model:value="searchForm.userStatus"
              :options="sys_normal_disable"
              placeholder="请选择状态"
            />
          </n-form-item-gi>
        </n-grid>
      </n-form>
    </TableSearch>

    <!-- 主体区域 -->
    <n-grid :cols="24" :x-gap="24">
      <!-- 部门树 -->
      <n-grid-item :span="6">
        <n-card
          title="部门列表"
          :bordered="false"
          size="small"
          :segmented="{ content: true }"
          class="dept-tree-card"
        >
          <template #header-extra>
            <n-input-group>
              <n-input v-model:value="pattern" placeholder="搜索部门" size="small" clearable>
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
          </template>
          <div class="dept-tree-content">
            <n-spin :show="deptLoading">
              <n-tree
                block-line
                :pattern="pattern"
                :data="departmentData"
                selectable
                v-model:default-expand-all="expandAllFlag"
                :on-update:selected-keys="onCheckedKeys"
                class="dept-tree"
              />
            </n-spin>
          </div>
        </n-card>
      </n-grid-item>

      <!-- 用户表格 -->
      <n-grid-item :span="18">
        <TableMain
          :data="dataList"
          :columns="tableColumns"
          :loading="tableLoading"
          row-key="id"
          :pagination="pagination"
        >
          <template #header>
            <n-button-group>
              <n-button type="primary" @click="handleAddUser" v-auth="['sys:user:add']">
                <template #icon>
                  <n-icon><AddOutline /></n-icon>
                </template>
                新增用户
              </n-button>
              <n-button
                type="error"
                @click="handleBatchDelete"
                v-auth="['sys:user:delete']"
                :disabled="!selectedRows.length"
              >
                <template #icon>
                  <n-icon><TrashOutline /></n-icon>
                </template>
                批量删除
              </n-button>
              <n-button @click="handleExport" v-auth="['sys:user:export']">
                <template #icon>
                  <n-icon><DownloadOutline /></n-icon>
                </template>
                导出
              </n-button>
            </n-button-group>
          </template>
        </TableMain>
      </n-grid-item>
    </n-grid>

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
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted, h } from 'vue'
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
import {
  SearchOutline,
  AddOutline,
  TrashOutline,
  DownloadOutline,
  ChevronUpOutline,
  ChevronDownOutline
} from '@vicons/ionicons5'
import { getUserPage, getUserRole, addUser, updateUser, delUser } from '@/api/system/user'
import { getRolePage } from '@/api/system/role'
import { getDeptPage } from '@/api/system/dept'
import { useDict } from '@/utils/useDict'
import { usePagination } from '@/hooks/useTable'
import { clearFormObject } from '@/utils'

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

interface SearchForm {
  nickName?: string
  userName?: string
  mobile?: string
  email?: string
  userStatus?: number | null
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
  userStatus: number
  avatar?: string
  deptName?: string
}

const dataList = ref<UserForm[]>([])
const departmentData = ref([])
const roleData = ref([])
const selectedRows = ref<UserForm[]>([])

// 表单对象
const userFormRef = ref()
const searchForm = ref<SearchForm>({
  nickName: '',
  userName: '',
  mobile: '',
  email: '',
  userStatus: null
})
const userForm = ref<UserForm>({
  userName: '',
  nickName: '',
  deptId: null,
  roleIds: [],
  email: '',
  mobile: '',
  userStatus: 0
})
const updatePassWordForm = ref({
  id: 0,
  password: '',
  password_two: ''
})

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

// 表格列配置
const tableColumns: DataTableColumns = [
  { title: '昵称', key: 'nickName' },
  { title: '用户名', key: 'userName' },
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
  { title: '部门', key: 'deptName' },
  { title: '手机号', key: 'mobile' },
  { title: '邮箱', key: 'email' },
  {
    title: '状态',
    key: 'userStatus',
    render: (row) => {
      const user = row as unknown as UserForm
      return h(
        NTag,
        {
          type: user.userStatus === 0 ? 'success' : 'error',
          round: true
        },
        {
          default: () => (user.userStatus === 0 ? '正常' : '停用')
        }
      )
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 200,
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
]

// 方法定义
function doRefresh() {
  tableLoading.value = true
  getUserPage(pagination.getPageInfo(searchForm.value))
    .then((res) => {
      dataList.value = res.rows
      pagination.setTotalSize(res.total)
    })
    .finally(() => {
      tableLoading.value = false
    })
}

function handleSearch() {
  pagination.page = 1
  doRefresh()
}

function handleReset() {
  searchForm.value = {
    nickName: '',
    userName: '',
    mobile: '',
    email: '',
    userStatus: null
  }
  handleSearch()
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
    userStatus: 0
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
  dialog.warning({
    title: '警告',
    content: '确认删除选中的用户吗？',
    positiveText: '确定',
    negativeText: '取消',
    onPositiveClick: () => {
      const ids = selectedRows.value.map((row: UserForm) => row.id).filter(Boolean)
      if (ids.length) {
        delUser(ids.join(',')).then(() => {
          message.success('删除成功')
          selectedRows.value = []
          doRefresh()
        })
      }
    }
  })
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
    console.error(error)
  }
}

function onCheckedKeys(keys: string[], _event: MouseEvent) {
  if (keys.length) {
    searchForm.value.deptId = keys[0]
    handleSearch()
  }
}

// 初始化
onMounted(() => {
  // 加载部门树
  deptLoading.value = true
  getDeptPage({})
    .then(({ data }) => {
      // 处理部门树数据,添加key和value字段用于级联选择
      const processDeptData = (depts: any[]) => {
        return depts.map((dept) => ({
          ...dept,
          key: dept.deptId,
          value: dept.deptId,
          label: dept.deptName,
          children: dept.children ? processDeptData(dept.children) : []
        }))
      }
      departmentData.value = processDeptData(data)
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
  padding: 16px;

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

  :deep(.n-tag) {
    padding: 0 12px;
  }

  :deep(.n-button) {
    padding: 0 16px;
  }
}
</style>
