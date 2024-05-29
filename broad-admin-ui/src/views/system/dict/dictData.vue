<template>
  <div class="main-container">
    <n-card content-style="padding: 10px;" style="margin-bottom: 5px">
      <n-space>
        <n-button type="info" @click="handleBack">返回</n-button>
      </n-space>
    </n-card>
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
          <n-form-item label="字典名称" path="dictLabel">
            <n-input v-model:value="dictForm.dictLabel" placeholder="输入字典名称" />
          </n-form-item>
          <n-form-item label="字典类型" path="dictType">
            <n-select
              disabled
              v-model:value="dictForm.dictType"
              :loading="selectLoading"
              :options="options"
              filterable
              placeholder="选择字典类型"
            />
          </n-form-item>
          <n-form-item label="字典键值" path="dictValue">
            <n-input v-model:value="dictForm.dictValue" placeholder="输入字典键值" />
          </n-form-item>
          <n-form-item label="字典样式" path="listClass">
            <n-space>
              <template v-for="(item, index) in dictColor" :key="index">
                <n-tag :type="item.value" class="form_tag" @click="handleUpdateTag(item.value)"
                >{{ item.label }}
                </n-tag>
              </template>
              <p>已选择：</p>
              <n-tag :bordered="false" :type="dictForm.listClass">当前选中</n-tag>
            </n-space>
          </n-form-item>
          <n-form-item label="字典排序" path="dictSort">
            <n-input-number v-model:value="dictForm.dictSort" clearable min="1" />
          </n-form-item>
          <n-form-item label="字典备注" path="remark">
            <n-input v-model:value="dictForm.remark" placeholder="输入字典备注" type="textarea" />
          </n-form-item>
          <n-form-item label="字典状态" path="status">
            <n-switch v-model:value="dictForm.status" checked-value="0" unchecked-value="1" />
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
import { getDict, addDict, updateDict, detectDict } from '@/api/system/dict'
import { getDictType } from '@/api/system/dictType'
import { h, onMounted, ref } from 'vue'
import { NTag, useMessage, useDialog } from 'naive-ui'
import DictTag from '@/components/tag/DictTag.vue'
import AddButton from '@/components/table/button/AddButton.vue'
import TableMain from '@/components/table/main/TableMain.vue'
import type { TableActionModel } from '@/types/table'

const props = defineProps({
  dictType: {
    type: String,
    default: ''
  }
})
const emit = defineEmits(['update-value'])
const refreshTable = () => {
  tableLoading.value = true
  searchForm.value.dictType = props.dictType
  getDict(pagination.getPageInfo(searchForm.value)).then((res: any) => {
    tableList.value = res.rows
    pagination.setTotalSize(res.total)
    handleSearch()
    tableLoading.value = false
  })
}
const modelDialogTitle = ref('')
const rowKey = useRowKey('dictType')
const tableList = ref([])
const tableLoading = ref(false)
const message = useMessage()
const dialog = useDialog()
const { sys_normal_disable } = useDict('sys_normal_disable')
const selectLoading = ref(false)
const pagination = usePagination(refreshTable)
const modalDialog = ref<boolean>(false)
const dictForm: any = ref({})
const searchForm: any = ref({})
const options: any = ref([])
const formRef = ref<any>(null)
const dictColor = [
  {
    label: 'default',
    value: 'default'
  },
  {
    label: 'success',
    value: 'success'
  },
  {
    label: 'info',
    value: 'info'
  },
  {
    label: 'warning',
    value: 'warning'
  },
  {
    label: 'error',
    value: 'error'
  }
]

const tableColumns = ref(
  [
    {
      title: '字典名称',
      key: 'dictLabel'
    },
    {
      title: '字典键值',
      key: 'dictValue'
    },
    {
      title: '字典类型',
      key: 'dictType'
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
  dictLabel: [
    {
      required: true,
      message: '请输入字典名称',
      trigger: ['blur', 'input']
    }
  ],
  dictValue: [
    {
      required: true,
      message: '请输入字典键值',
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
  ],
  dictSort: [
    {
      required: true,
      validator: (rule: any, value: any, callback: any) => {
        if (value === '' || value === null || value === undefined) {
          callback(new Error('请输入字典排序'))
        } else if (!/^[1-9]\d*$/.test(value)) {
          callback(new Error('请输入正整数'))
        } else {
          callback()
        }
      },
      trigger: ['input', 'blur']
    }
  ]
}
// 选择框
const handleSearch = () => {
  if (options.value.length === 0) {
    selectLoading.value = true
    getDictType({}).then((res: any) => {
      res.data.forEach((item: any) => {
        options.value.push({
          label: item.dictName + '(' + item.dictType + ')',
          value: item.dictType
        })
      })
      selectLoading.value = false
    })
  }
}

// 返回
const handleBack = () => {
  emit('update-value', false)
}

const onAddItem = () => {
  modelDialogTitle.value = '新增字典'
  dictForm.value = {}
  dictForm.value.dictType = props.dictType
  modalDialog.value = true
}
const handleUpdateDict = (rowData: any) => {
  modelDialogTitle.value = '修改字典'
  dictForm.value = rowData
  dictForm.value.dictType = props.dictType
  modalDialog.value = true
}

// 表单标签更新
const handleUpdateTag = (color: string) => {
  dictForm.value.listClass = color
}
/**
 * 弹出框确认
 */
const onConfirm = () => {
  formRef.value?.validate().then(() => {
    if (dictForm.value.dictCode) {
      updateDict(dictForm.value).then(() => {
        modalDialog.value = false
        message.success('修改成功')
        refreshTable()
      })
    } else {
      addDict(dictForm.value).then(() => {
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
      detectDict({ idList: row.dictCode }).then(() => {
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
