<template>
  <div>
    <TableSearch @search="handleSearch" @reset="handleReset">
      <n-form label-placement="left" inline>
        <n-grid :cols="6" :x-gap="10">
          <n-form-item-gi label="昵称">
            <n-input v-model:value="searchForm.nickName" />
          </n-form-item-gi>
          <n-form-item-gi label="用户名">
            <n-input v-model:value="searchForm.userName" />
          </n-form-item-gi>
          <n-form-item-gi label="手机号">
            <n-input v-model:value="searchForm.mobile" />
          </n-form-item-gi>
          <n-form-item-gi label="邮箱">
            <n-input v-model:value="searchForm.email" />
          </n-form-item-gi>
          <n-form-item-gi label="状态">
            <n-select
              v-model:value="searchForm.userStatus"
              :options="sys_normal_disable"
              placeholder="请选择状态"
            />
          </n-form-item-gi>
        </n-grid>
      </n-form>
    </TableSearch>
    <n-flex :wrap="false">
      <div>
        <n-card
          :content-style="{ padding: '5px' }"
          :header-style="{ padding: '5px' }"
          v-loading="deptLoading"
          :segmented="true"
        >
          <template #header>
            <div class="flex items-center">
              <n-input class="mr-2" v-model:value="pattern" placeholder="搜索" size="small" />
              <n-switch size="small" v-model:value="expandAllFlag" />
            </div>
          </template>
          <template #default>
            <n-tree
              block-line
              :pattern="pattern"
              :data="departmentData"
              selectable
              v-model:default-expand-all="expandAllFlag"
              :on-update:selected-keys="onCheckedKeys"
            />
          </template>
        </n-card>
      </div>
      <div style="width: 100%">
        <TableMain
          :data="dataList"
          :columns="tableColumns"
          :loading="tableLoading"
          row-key="id"
          :pagination="pagination"
        >
          <template v-slot:header>
            <AddButton @add="handleAddUser" v-auth="['sys:user:add']" />
          </template>
        </TableMain>
        <div>
          <ModalDialog
            v-model="showModelUpdate"
            :title="title"
            :mask-closable="false"
            :submit-loading="submitLoading"
            @confirm="handleSubmit"
          >
            <template #content>
              <n-form
                ref="userFormRef"
                :rules="userRules"
                :model="userForm"
                label-width="auto"
                v-loading="updateLoading"
              >
                <n-grid :cols="2" :x-gap="20">
                  <n-form-item-gi label="用户名" path="userName">
                    <n-input
                      v-model:value="userForm.userName"
                      :disabled="userForm.id != undefined"
                    />
                  </n-form-item-gi>
                  <n-form-item-gi label="昵称" path="nickName">
                    <n-input v-model:value="userForm.nickName" />
                  </n-form-item-gi>
                  <n-form-item-gi label="部门" path="deptId">
                    <n-cascader
                      v-model:value="userForm.deptId"
                      filterable
                      placeholder="选择归属部门"
                      :options="departmentData"
                    />
                  </n-form-item-gi>
                  <n-form-item-gi label="角色" path="roleIds">
                    <n-select
                      v-model:value="userForm.roleIds"
                      multiple
                      :options="roleData"
                      placeholder="请选择角色"
                    />
                  </n-form-item-gi>
                  <n-form-item-gi label="邮箱" path="email">
                    <n-auto-complete
                      v-model:value="userForm.email"
                      :options="emailOptions"
                      :input-props="{ autocomplete: 'disabled' }"
                    />
                  </n-form-item-gi>
                  <n-form-item-gi label="手机号" path="mobile">
                    <n-input v-model:value="userForm.mobile" />
                  </n-form-item-gi>
                  <n-form-item-gi label="状态">
                    <RadioGroup :list="sys_normal_disable" v-model:value="userForm.userStatus" />
                  </n-form-item-gi>
                </n-grid>
              </n-form>
            </template>
          </ModalDialog>

          <!-- 密码修改 -->
          <ModalDialog
            v-model="showUpdatePasswordModel"
            :title="title"
            :mask-closable="false"
            @confirm="handleUpdatePassWordSubmit"
          >
            <template #content>
              <n-form
                ref="updatePassWordFormRef"
                :rules="updatePassWordRules"
                :model="updatePassWordForm"
                label-width="auto"
              >
                <n-form-item label="密码" path="password">
                  <n-input type="password" clearable v-model:value="updatePassWordForm.password" />
                </n-form-item>
                <n-form-item label="重复密码" path="password_two">
                  <n-input
                    type="password"
                    clearable
                    v-model:value="updatePassWordForm.password_two"
                  />
                </n-form-item>
              </n-form>
            </template>
          </ModalDialog>
        </div>
      </div>
    </n-flex>
  </div>
</template>

<script lang="ts" setup>
import { getUserPage, getUserRole, addUser, updateUser, delUser } from '@/api/system/user'
import { getRolePage } from '@/api/system/role'
import { getDeptPage } from '@/api/system/dept'
import { useDict } from '@/utils/useDict'
import { usePagination, useRenderTag, useRowKey } from '@/hooks/useTable'
import { useRenderAction } from '@/hooks/useTable'
import { NAvatar, useDialog, useMessage } from 'naive-ui'
import { computed, h, onMounted, ref } from 'vue'
import AddButton from '@/components/table/button/AddButton.vue'
import ModalDialog from '@/components/common/ModalDialog.vue'
import { clearFormObject } from '@/utils'
import { mapTree } from '@/utils'
import RadioGroup from '@/components/common/RadioGroup.vue'
import TableSearch from '@/components/table/search/TableSearch.vue'
import TableMain from '@/components/table/main/TableMain.vue'

const dataList = ref([])
const deptLoading = ref<boolean>(true)
const tableLoading = ref<boolean>(true)
const showUpdatePasswordModel = ref<boolean>(false)
const showModelUpdate = ref<boolean>(false)
const updateLoading = ref<boolean>(false)
const submitLoading = ref<boolean>(false)
const pattern = ref('')
const updatePassWordFormRef: any = ref('')
const emailOptions = computed(() => {
  return ['@gmail.com', '@163.com', '@qq.com'].map((suffix) => {
    const prefix = userForm.value.email?.split('@')[0]
    return {
      label: prefix + suffix,
      value: prefix + suffix
    }
  })
})
const updatePassWordForm = ref<{
  id: number
  password: string
  password_two: string
}>({
  id: 0,
  password: '',
  password_two: ''
})
const { sys_normal_disable } = useDict('sys_normal_disable')
const title = ref<string>('新增用户')
const userFormRef: any = ref(null)
const roleData = ref([])
const searchForm = ref<any>({})
const userForm: any = ref({})
const naiveDialog = useDialog()
const message = useMessage()
const pagination = usePagination(doRefresh)
const departmentData = ref([] as Array<any>)
const tableColumns = ref([
  {
    title: '昵称',
    key: 'nickName'
  },
  {
    title: '账号',
    key: 'userName'
  },
  {
    title: '头像',
    key: 'avatar',
    render: (rowData: any) => {
      return h(
        NAvatar,
        {
          src: rowData.avatar ? rowData.avatar : '',
          round: true,
          size: 'small',
          renderFallback() {
            return h(
              NAvatar,
              {
                round: true,
                size: 'small'
              },
              rowData.nickName.substring(0, 1)
            )
          }
        },
        `${rowData.avatar ? '' : rowData.nickName.substring(0, 1)}`
      )
    }
  },
  {
    title: '部门',
    key: 'deptName'
  },
  {
    title: '邮箱',
    key: 'email'
  },
  {
    title: '手机号',
    key: 'mobile'
  },
  {
    title: '状态',
    key: 'userStatus',
    render: (row: any) => {
      return useRenderTag(sys_normal_disable.value, row.userStatus)
    }
  },
  {
    title: '创建时间',
    key: 'createTime'
  },
  {
    title: '操作',
    key: 'actions',
    fixed: 'right',
    width: 200,
    render: (rowData: { id: number }) => {
      return useRenderAction([
        {
          type: 'primary',
          label: '编辑',
          auth: 'sys:user:update',
          onClick: () => onUpdateItem(rowData)
        },
        {
          label: '修改密码',
          type: 'warning',
          auth: 'sys:user:update',
          onClick: () => onUpdatePassword(rowData)
        },
        {
          disabled: rowData.id === 1,
          label: '删除',
          auth: ['sys:user:delete'],
          type: 'error',
          onClick: () => onDeleteItem(rowData)
        }
      ])
    }
  }
])
const expandAllFlag = ref(false)

const updatePassWordRules = {
  password: [
    {
      required: true,
      message: '请输入密码',
      trigger: ['input', 'blur']
    },
    {
      min: 6,
      max: 20,
      message: '密码长度在 6 到 20 个字符',
      trigger: ['blur']
    }
  ],
  // 效验两次密码是否一致
  password_two: {
    required: true,
    trigger: ['input', 'blur'],
    validator: (rule: any, value: any, callback: any) => {
      if (value !== updatePassWordForm.value.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
  }
}
const userRules = {
  userName: {
    required: true,
    message: '请输入用户名',
    trigger: ['input', 'blur']
  },
  name: [
    {
      required: true,
      message: '请输入昵称',
      trigger: 'blur'
    }
  ],
  deptId: [
    {
      required: true,
      message: '请选择部门',
      trigger: 'blur',
      type: 'number'
    }
  ],
  roleIds: [
    {
      required: true,
      message: '请选择角色',
      trigger: 'blur',
      validator: (rule: any, value: any, callback: any) => {
        if (value.length === 0) {
          callback(new Error('请选择角色'))
        } else {
          callback()
        }
      }
    }
  ],
  mobile: [
    {
      required: true,
      pattern: /^1[3456789]\d{9}$/,
      message: '请输入正确的手机号',
      trigger: 'blur'
    }
  ],
  email: [
    {
      required: true,
      pattern: /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/,
      message: '请输入正确的邮箱',
      trigger: 'blur'
    }
  ]
}

function doRefresh() {
  tableLoading.value = true
  getUserPage(pagination.getPageInfo(searchForm.value))
    .then((res: any) => {
      dataList.value = res.rows
      pagination.setTotalSize((res as any).total)
      tableLoading.value = false
    })
    .catch(() => {
      tableLoading.value = false
    })
}

function onUpdateItem(item: any) {
  updateLoading.value = true
  title.value = '编辑用户'
  userForm.value = JSON.parse(JSON.stringify(item))
  showModelUpdate.value = true
  getUserRole(item.id).then((res: any) => {
    userForm.value.roleIds = res.data
    updateLoading.value = false
  })
}

function onUpdatePassword(item: any) {
  title.value = '修改密码'
  showUpdatePasswordModel.value = true
  updatePassWordForm.value.id = item.id
}

function handleAddUser() {
  userForm.value = {}
  title.value = '新增用户'
  showModelUpdate.value = true
}

function handleInitRole() {
  getRolePage({
    status: 0
  }).then((res: any) => {
    roleData.value = res.rows.map((item: any) => {
      expandAllFlag.value = true
      return {
        label: item.name,
        value: item.id
      }
    })
  })
}

function onDeleteItem(item: any) {
  naiveDialog.warning({
    title: '提示',
    content: '确定要删除此数据吗？',
    positiveText: '确定',
    onPositiveClick: () => {
      delUser({ idList: item.id })
        .then(() => {
          message.success('删除成功')
          doRefresh()
        })
        .catch(console.log)
    }
  })
}

function handleSubmit() {
  userFormRef.value?.validate().then(() => {
    submitLoading.value = true
    if (userForm.value.id) {
      updateUser(userForm.value).then(() => {
        message.success('修改成功')
        showModelUpdate.value = false
        submitLoading.value = false
        doRefresh()
      })
    } else {
      addUser(userForm.value).then(() => {
        message.success('新增成功')
        showModelUpdate.value = false
        submitLoading.value = false
        doRefresh()
      })
    }
  })
}

function handleUpdatePassWordSubmit() {
  updatePassWordFormRef.value?.validate().then(() => {
    updateUser(updatePassWordForm.value).then(() => {
      message.success('修改成功')
      showUpdatePasswordModel.value = false
      updatePassWordForm.value = {
        id: 0,
        password: '',
        password_two: ''
      }
      doRefresh()
    })
  })
}

// 获取部门
async function getDepartment() {
  const res = await getDeptPage({
    status: 0
  })
  departmentData.value = mapTree(res.data)
  deptLoading.value = false
}

function onCheckedKeys(keys: any) {
  searchForm.value.deptId = keys[0]
  doRefresh()
}

// 搜索
function handleSearch() {
  doRefresh()
}

// 重置
function handleReset() {
  clearFormObject(searchForm)
  handleSearch()
}

onMounted(async () => {
  doRefresh()
  await getDepartment()
  handleInitRole()
})
</script>

<style lang="scss" scoped>
.avatar-container {
  position: relative;
  width: 30px;
  margin: 0 auto;
  vertical-align: middle;

  .avatar {
    width: 100%;
    border-radius: 50%;
  }

  .avatar-vip {
    border: 2px solid #cece1e;
  }

  .vip {
    position: absolute;
    top: 0;
    right: -9px;
    width: 15px;
    transform: rotate(60deg);
  }
}

.gender-container {
  .gender-icon {
    width: 20px;
  }
}
</style>
