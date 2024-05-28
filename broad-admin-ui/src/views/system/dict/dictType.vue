<template>
  <div class="main-container">
    <TableSearch @search="handleRefresh">
      <n-form ref="searchFormRef" :model="searchForm" inline label-width="auto" label-placement="left">
        <n-form-item label="字典名称" path="dictName">
          <n-input v-model:value="searchForm.dictName" placeholder="输入字典名称" />
        </n-form-item>
        <n-form-item label="字典类型" path="dictType">
          <n-input v-model:value="searchForm.dictLabel" placeholder="输入字典名称" />
        </n-form-item>
      </n-form>
    </TableSearch>
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
    <ModalDialog
      v-model="modalDialog"
      :mask-closable="false"
      :title="modelDialogTitle"
      content-height="50vh"
      @confirm="onConfirm"
    >
      <template #content>
        <n-form
          ref="formRef"
          :model="dictForm"
          :rules="dictRules"
          label-placement="left"
          label-width="auto"
        >
          <n-form-item label="字典名称" path="dictName">
            <n-input v-model:value="dictForm.dictName" placeholder="输入字典名称" />
          </n-form-item>
          <n-form-item label="字典类型" path="dictType">
            <n-input v-model:value="dictForm.dictType" :disabled="dictForm.dictId" placeholder="输入字典类型" />
          </n-form-item>
          <n-form-item label="字典备注" path="remark">
            <n-input v-model:value="dictForm.remark" placeholder="输入字典备注" type="textarea" />
          </n-form-item>
          <n-form-item label="字典状态" path="status">
            <n-switch v-model:value="dictForm.status" checked-value="0" unchecked-value="1" default-value="0" />
          </n-form-item>
        </n-form>
      </template>
    </ModalDialog>
  </div>
</template>

<script lang="ts" setup>
import {
  useRenderAction,
  useRowKey,
  usePagination
} from '@/hooks/useTable'
import { useDict } from '@/utils/useDict'
import {
  getDictTypePage,
  addDictType,
  updateDictType,
  detectDictType
} from '@/api/system/dictType'
import { h, onMounted, ref } from 'vue'
import { useMessage, useDialog, NButton } from 'naive-ui'
import DictTag from '@/components/tag/DictTag.vue'
import TableMain from '@/components/table/main/TableMain.vue'
import TableSearch from '@/components/table/search/TableSearch.vue'
import type { TableActionModel } from '@/types/table'

const emit = defineEmits(['update-value'])

const refreshTable = () => {
  tableLoading.value = true
  getDictTypePage(pagination.getPageInfo(searchForm.value)).then((res: any) => {
    tableList.value = res.rows
    pagination.setTotalSize(res.total)
    tableLoading.value = false
  })
}
const modelDialogTitle = ref('')
const rowKey = useRowKey('dictName')
const tableList = ref([])
const tableLoading = ref(false)
const message = useMessage()
const dialog = useDialog()
const { sys_normal_disable } = useDict('sys_normal_disable')
const pagination = usePagination(refreshTable)
const modalDialog = ref<boolean>(false)
const dictForm: any = ref({})
const searchForm: any = ref({})
const formRef = ref<any>(null)


const tableColumns = ref(
  [
    {
      title: '字典名称',
      key: 'dictName'
    },
    {
      title: '字典类型',
      key: 'dictType',
      render: (rowData: { dictType: any; }) => {
        return h(NButton, {
          text: true,
          type: 'error',
          onClick: () => {
            emit('update-value', rowData.dictType)
          }
        }, rowData.dictType)
      }
    },
    {
      title: '状态',
      key: 'status',
      render: (rowData: { status: any; }) => {
        return h(DictTag, {
          options: sys_normal_disable.value,
          value: rowData.status
        })
      }
    },
    {
      title: '备注',
      key: 'remark'
    },
    {
      title: '创建时间',
      key: 'createTime'
    },
    {
      title: '操作',
      key: 'actions',
      fixed: 'right',
      render: (rowData: any) => {
        return useRenderAction([
          {
            label: '编辑',
            type: 'warning',
            onClick: () => {
              handleUpdateDict(rowData)
            }
          },
          {
            label: '删除',
            type: 'error',
            onClick: () => {
              handleDelete(rowData)
            }
          }
        ] as TableActionModel[])
      }
    }
  ]
)
const dictRules = {
  dictName: [
    {
      required: true,
      message: '请输入字典名称',
      trigger: ['blur', 'input']
    }
  ],
  dictType: [
    {
      required: true,
      message: '请选择字典类型',
      trigger: ['blur']
    }
  ],
  listClass: [
    {
      required: true,
      message: '请选择字典样式',
      trigger: ['blur']
    }
  ]
}

const onAddItem = () => {
  modelDialogTitle.value = '新增字典'
  dictForm.value = {}
  modalDialog.value = true
}
const handleUpdateDict = (rowData: any) => {
  modelDialogTitle.value = '修改字典'
  dictForm.value = rowData
  modalDialog.value = true
}

/**
 * 弹出框确认
 */
const onConfirm = () => {
  formRef.value?.validate().then(() => {
    if (dictForm.value.dictId) {
      updateDictType(dictForm.value).then(() => {
        modalDialog.value = false
        message.success('修改成功')
        refreshTable()
      })
    } else {
      addDictType(dictForm.value).then(() => {
        modalDialog.value = false
        message.success('添加成功')
        refreshTable()
      })
    }
  })
}
// 删除
const handleDelete = (row: any) => {
  dialog.warning({
    title: '提示',
    content: '请再次确认删除当前数据？',
    positiveText: '确定',
    negativeText: '不确定',
    maskClosable: false,
    onPositiveClick: () => {
      detectDictType(row.dictId).then(() => {
        message.success('删除成功')
        refreshTable()
      })
    }
  })
}

// 刷新表格
const handleRefresh = () => {
  searchForm.value = {}
  refreshTable()
}

onMounted(() => {
  refreshTable()
})
</script>

<style scoped>
.form_tag {
  cursor: pointer;
}
</style>
