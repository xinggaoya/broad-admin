<template>
  <div class="main-container">
    <TableMain
      :data="tableList"
      :columns="tableColumns"
      :loading="tableLoading"
      :pagination="pagination"
    >
      <template v-slot:header>
        <AddButton @add="onAddItem" />
      </template>
    </TableMain>
    <ModalDialog v-model="modalDialogRef" @confirm="onDataFormConfirm">
      <template #content>
        <n-form ref="formDataRef" :model="formData">
          <n-form-item label="角色名称" path="name">
            <n-input v-model:value="formData.name" />
          </n-form-item>
          <n-form-item label="角色编号" path="halfRules">
            <n-input v-model:value="formData.halfRules" />
          </n-form-item>
          <n-form-item label="角色描述" path="remarks">
            <n-input v-model:value="formData.remarks" />
          </n-form-item>
          <n-form-item label="状态" path="status">
            <n-switch v-model:value="formData.status" checked-value="0" unchecked-value="1" />
          </n-form-item>
        </n-form>
      </template>
    </ModalDialog>
    <ModalDialog
      v-model="menuModalDialogRef"
      title="菜单权限"
      contentHeight="40vh"
      @confirm="handleSubmit"
    >
      <template #content>
        <n-tree
          v-loading="treeLoading"
          :data="menuData"
          checkable
          block-line
          :on-update:checked-keys="handleCheckedKeys"
          :default-expanded-keys="defaultExpandedKeys"
          :default-checked-keys="defaultCheckedKeys"
        />
      </template>
    </ModalDialog>
  </div>
</template>

<script lang="ts" setup>
import { getRolePage, addRole, updateRole, delRole } from '@/api/system/role'
import { getMenusTree } from '@/api/system/menu'
import { getRoleMenu, addRoleMenu } from '@/api/system/roleMenu'
import { useRenderAction, usePagination, useRenderTag } from '@/hooks/useTable'
import { NInput, TreeOption, useDialog, useMessage } from 'naive-ui'
import { onMounted, ref, shallowReactive } from 'vue'
import TableMain from '@/components/table/main/TableMain.vue'
import AddButton from '@/components/table/button/AddButton.vue'
import { useDict } from '@/utils/useDict'

function handleMenuData(
  menuData: Array<any>,
  defaultCheckedKeys: Array<string>,
  defaultExpandedKeys: Array<string>
) {
  const tempMenus = [] as Array<TreeOption>
  menuData.forEach((it) => {
    const tempMenu = {} as TreeOption
    tempMenu.key = it.menuId
    tempMenu.label = it.menuName
    if (it.children && it.children.length > 0) {
      tempMenu.children = handleMenuData(it.children, defaultCheckedKeys, defaultExpandedKeys)
    }
    tempMenus.push(tempMenu)
  })
  return tempMenus
}

const { sys_normal_disable } = useDict('sys_normal_disable')
const modalDialogRef = ref<boolean>(false)
const treeLoading = ref<boolean>(true)
const menuModalDialogRef = ref<boolean>(false)
const formDataRef = ref()
const formData = ref({})
const tableList = ref([])
const tableLoading = ref<boolean>(false)
const selectRow: any = ref({})
const pagination = usePagination(doRefresh)
const checkedKeys = ref<Array<string>>([])
const naiveDialog = useDialog()
const saveType = ref<string>('update')
const message = useMessage()
const menuData = shallowReactive([] as Array<TreeOption>)
const tableColumns = ref([
  {
    title: '角色名称',
    key: 'name'
  },
  {
    title: '角色编号',
    key: 'halfRules'
  },
  {
    title: '角色描述',
    key: 'remarks'
  },
  {
    title: '状态',
    key: 'status',
    render: (row: any) => {
      return useRenderTag(sys_normal_disable.value, row.status)
    }
  },
  {
    title: '创建时间',
    key: 'createTime'
  },
  {
    title: '操作',
    key: 'actions',
    render: (rowData: any) => {
      return useRenderAction([
        {
          label: '编辑',
          type: 'primary',
          onClick: onUpdateItem.bind(null, rowData)
        },
        {
          label: '删除',
          type: 'error',
          onClick: onDeleteItem.bind(null, rowData)
        },
        {
          label: '菜单权限',
          type: 'success',
          onClick: onShowMenu.bind(null, rowData)
        }
      ])
    }
  }
])
const defaultCheckedKeys = ref([] as Array<string>)
const defaultExpandedKeys = shallowReactive([] as Array<string>)

function doRefresh() {
  tableLoading.value = true
  getRolePage({})
    .then((res: any) => {
      tableList.value = res.rows
      pagination.setTotalSize(res.total)
      getMenuTree()
      tableLoading.value = false
    })
    .catch(console.log)
}

function onAddItem() {
  formData.value = {}
  modalDialogRef.value = true
}

function onUpdateItem(item: any) {
  formData.value = item
  modalDialogRef.value = true
}

function handleCheckedKeys(keys: [], option: []) {
  checkedKeys.value = keys
}

function handleSubmit() {
  addRoleMenu({
    roleId: selectRow.value.id,
    menuIds: checkedKeys.value
  })
    .then(() => {
      message.success('操作成功')
      menuModalDialogRef.value = false
    })
    .catch(console.log)
}

function onDeleteItem(data: any) {
  naiveDialog.warning({
    title: '提示',
    content: '是否要删除此菜单？',
    positiveText: '删除',
    onPositiveClick: () => {
      delRole(data.id)
        .then(() => {
          message.success('删除成功')
          doRefresh()
        })
        .catch(console.log)
    }
  })
}

function onDataFormConfirm() {
  let params = formData.value
  if (params.id) {
    updateRole(params)
      .then(() => {
        message.success('操作成功')
        doRefresh()
        modalDialogRef.value = false
      })
      .catch(console.log)
  } else {
    addRole(params)
      .then(() => {
        message.success('操作成功')
        doRefresh()
        modalDialogRef.value = false
      })
      .catch(console.log)
  }
}

function getMenuTree() {
  getMenusTree()
    .then((res: any) => {
      menuData.splice(
        0,
        menuData.length,
        ...handleMenuData(res.data, defaultCheckedKeys.value, defaultExpandedKeys)
      )
    })
    .catch(console.log)
}

function onShowMenu(item: any) {
  treeLoading.value = true
  defaultCheckedKeys.value = []
  getRoleMenu({
    roleId: item.id
  }).then((res) => {
    res.data?.forEach((it: any) => {
      defaultCheckedKeys.value.push(it.menuId)
    })
    treeLoading.value = false
  })
  selectRow.value = item
  menuModalDialogRef.value = true
}

onMounted(doRefresh)
</script>
