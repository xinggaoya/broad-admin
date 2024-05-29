<template>
  <div class="main-container">
    <TableSearch @search="doRefresh"></TableSearch>
    <TableMain :data="tableList" row-key="deptId" :columns="tableColumns" :loading="tableLoading">
      <template v-slot:header>
        <AddButton @add="onAddItem" />
      </template>
    </TableMain>
    <ModalDialog v-model="modalDialog" @confirm="onDataFormConfirm" :title="title">
      <template #content>
        <n-form
          ref="deptFormRef"
          label-placement="left"
          label-width="auto"
          :model="deptForm"
          :rules="deptRules"
        >
          <n-form-item label="上级部门" path="parentId">
            <n-tree-select
              v-model:value="deptForm.parentId"
              :options="options"
              placeholder="选择上级部门"
            />
          </n-form-item>
          <n-form-item label="部门名称" path="deptName">
            <n-input v-model:value="deptForm.deptName" placeholder="输入部门名称" />
          </n-form-item>
          <n-form-item label="负责人" path="leader">
            <n-input v-model:value="deptForm.leader" placeholder="输入负责人" />
          </n-form-item>
          <n-form-item label="联系电话" path="phone">
            <n-input v-model:value="deptForm.phone" placeholder="输入联系电话" />
          </n-form-item>
          <n-form-item label="邮箱" path="email">
            <n-input v-model:value="deptForm.email" placeholder="输入邮箱" />
          </n-form-item>
          <n-form-item label="显示顺序" path="orderNum">
            <n-input-number min="0" v-model:value="deptForm.orderNum" placeholder="输入显示顺序" />
          </n-form-item>
          <n-form-item label="状态" path="status">
            <n-switch v-model:value="deptForm.status" checked-value="0" unchecked-value="1" />
          </n-form-item>
        </n-form>
      </template>
    </ModalDialog>
  </div>
</template>

<script lang="ts" setup>
import { addDept, deleteDept, getDeptPage, updateDept } from '@/api/system/dept'
import { useRenderAction } from '@/hooks/useTable'
import { h, nextTick, onMounted, ref } from 'vue'
import { type FormInst, useDialog, useMessage } from 'naive-ui'
import { useDict } from '@/utils/useDict'
import DictTag from '@/components/tag/DictTag.vue'
import TableMain from '@/components/table/main/TableMain.vue'
import AddButton from '@/components/table/button/AddButton.vue'
import TableSearch from '@/components/table/search/TableSearch.vue'
import type { TableActionModel } from '@/types/table'

const deptFormRef: any = ref<FormInst | null>(null)
const modalDialog = ref<boolean>(false)
const title = ref<string>('新增部门')
const options: any = ref([])
const { sys_normal_disable } = useDict('sys_normal_disable')
const tableList = ref([])
const tableLoading = ref<boolean>(false)
const message = useMessage()
const naiveDailog = useDialog()
const deptForm: any = ref({})
const deptRules = {
  deptName: [
    {
      required: true,
      message: '请输入部门名称',
      trigger: 'blur'
    }
  ]
}
const tableColumns = ref([
  {
    title: '部门名称',
    key: 'deptName'
  },
  {
    title: '负责人',
    key: 'leader'
  },
  {
    title: '状态',
    key: 'status',
    render: (rowData: { status: any }) => {
      return h(DictTag, {
        options: sys_normal_disable.value,
        value: rowData.status
      })
    }
  },
  {
    title: '创建时间',
    key: 'createTime'
  },
  {
    title: '操作',
    key: 'actions',
    render: (rowData) => {
      return useRenderAction([
        {
          label: '编辑',
          onClick: onUpdateItem.bind(null, rowData)
        },
        {
          label: '删除',
          type: 'error',
          onClick() {
            onDeleteItem(rowData)
          }
        }
      ] as TableActionModel[])
    }
  }
])

function doRefresh() {
  tableLoading.value = true
  getDeptPage({}).then((res: any) => {
    tableList.value = res.data
    options.value = handleTreeData(res.data)
    tableLoading.value = false
  })
}

// 返回处理后的树形数据
function handleTreeData(data: any) {
  return data?.map((item: any) => {
    if (item.children.length > 0) {
      return {
        key: item.deptId,
        label: item.deptName,
        value: item.deptId,
        children: handleTreeData(item.children)
      }
    } else {
      return {
        key: item.deptId,
        label: item.deptName,
        value: item.deptId,
        isLeaf: true
      }
    }
  })
}

const onDeleteItem = (item: any) => {
  naiveDailog.warning({
    title: '提示',
    content: '确定要删除此信息，删除后不可恢复？',
    positiveText: '删除',
    negativeText: '再想想',
    onPositiveClick: () => {
      deleteDept({ idList: item.deptId }).then(() => {
        message.success('删除成功')
        doRefresh()
      })
    }
  })
}

function onAddItem() {
  modalDialog.value = true
  title.value = '新增部门'
  nextTick(() => {
    deptForm.value = {}
  })
}

function onDataFormConfirm() {
  if (deptFormRef.value.validate()) {
    modalDialog.value = false
    if (deptForm.value?.deptId) {
      updateDept(deptForm.value).then(() => {
        message.success('修改成功')
        doRefresh()
      })
    } else {
      addDept(deptForm.value).then(() => {
        message.success('添加成功')
        doRefresh()
      })
    }
  }
}

function onUpdateItem(item: any) {
  modalDialog.value = true
  title.value = '修改部门'
  deptForm.value = item
}

function rowKey(rowData: any) {
  return rowData.deptId
}

onMounted(doRefresh)
</script>
