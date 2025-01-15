<template>
  <div class="menu-container">
    <!-- 表格区域 -->
    <TableMain
      ref="tableRef"
      :columns="tableColumns"
      :data="tableData.list"
      :loading="tableData.loading"
      :cascade="false"
      allow-checking-not-loaded
      row-key="menuId"
      :indent="20"
      remote
      :expandable="true"
      :default-expand-all="false"
      :on-load="handleLoad"
    >
      <template #header>
        <div class="table-header">
          <n-button-group>
            <n-button type="primary" @click="handleAdd">
              <template #icon>
                <n-icon>
                  <AddOutline />
                </n-icon>
              </template>
              新增菜单
            </n-button>
          </n-button-group>
        </div>
      </template>
    </TableMain>

    <!-- 表单弹窗 -->
    <n-modal
      v-model:show="formDialog.visible"
      :title="formDialog.title"
      preset="card"
      :style="{ width: '650px' }"
      :mask-closable="false"
    >
      <n-spin :show="formDialog.submitLoading">
        <n-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          label-placement="left"
          label-width="100"
          require-mark-placement="right-hanging"
        >
          <n-grid :cols="24" :x-gap="24">
            <!-- 菜单类型 -->
            <n-form-item-gi :span="12" label="菜单类型" path="menuType">
              <n-radio-group v-model:value="formData.menuType">
                <n-space>
                  <n-radio :value="1">菜单</n-radio>
                  <n-radio :value="0">目录</n-radio>
                  <n-radio :value="2">按钮</n-radio>
                </n-space>
              </n-radio-group>
            </n-form-item-gi>

            <!-- 上级菜单 -->
            <n-form-item-gi :span="12" label="上级菜单" path="parentId">
              <n-tree-select
                v-model:value="formData.parentId"
                :options="menuTreeOptions"
                placeholder="请选择上级菜单"
                clearable
              />
            </n-form-item-gi>

            <!-- 菜单名称 -->
            <n-form-item-gi :span="12" label="菜单名称" path="menuName">
              <n-input v-model:value="formData.menuName" placeholder="请输入菜单名称" />
            </n-form-item-gi>

            <!-- 菜单路径 -->
            <n-form-item-gi
              v-if="formData.menuType !== 2"
              :span="12"
              label="菜单路径"
              path="menuUrl"
            >
              <n-input
                v-model:value="formData.menuUrl"
                placeholder="请输入菜单路径，例：/system/menu"
              />
            </n-form-item-gi>

            <!-- 组件路径 -->
            <n-form-item-gi
              v-if="formData.menuType === 1"
              :span="12"
              label="组件路径"
              path="localFilePath"
            >
              <n-input
                v-model:value="formData.localFilePath"
                placeholder="请输入组件路径，例：/menu/index"
              />
            </n-form-item-gi>

            <!-- 权限标识 -->
            <n-form-item-gi :span="12" label="权限标识" path="perme">
              <n-input
                v-model:value="formData.perme"
                placeholder="请输入权限标识，例：system:menu:list"
              />
            </n-form-item-gi>

            <!-- 菜单图标 -->
            <n-form-item-gi v-if="formData.menuType !== 2" :span="12" label="菜单图标" path="icon">
              <IconSelect v-model:value="formData.icon" />
            </n-form-item-gi>

            <!-- 显示排序 -->
            <n-form-item-gi :span="12" label="显示排序" path="orderNum">
              <n-input-number
                v-model:value="formData.orderNum"
                :min="0"
                :max="999"
                placeholder="请输入显示排序"
              />
            </n-form-item-gi>

            <!-- 是否缓存 -->
            <n-form-item-gi
              v-if="formData.menuType !== 2"
              :span="8"
              label="是否缓存"
              path="cacheable"
            >
              <n-switch
                v-model:value="formData.cacheable"
                :checked-value="1"
                :unchecked-value="0"
              />
            </n-form-item-gi>

            <!-- 是否显示 -->
            <n-form-item-gi v-if="formData.menuType !== 2" :span="8" label="是否显示" path="hidden">
              <n-switch v-model:value="formData.hidden" :checked-value="0" :unchecked-value="1" />
            </n-form-item-gi>

            <!-- 是否固定 -->
            <n-form-item-gi v-if="formData.menuType !== 2" :span="8" label="是否固定" path="affix">
              <n-switch v-model:value="formData.affix" :checked-value="1" :unchecked-value="0" />
            </n-form-item-gi>

            <!-- 备注 -->
            <n-form-item-gi :span="24" label="备注" path="remark">
              <n-input
                v-model:value="formData.remark"
                type="textarea"
                placeholder="请输入备注信息"
              />
            </n-form-item-gi>
          </n-grid>
        </n-form>
      </n-spin>
      <template #footer>
        <n-space justify="end">
          <n-button @click="formDialog.visible = false">取消</n-button>
          <n-button type="primary" :loading="formDialog.submitLoading" @click="handleSubmit"
            >确定
          </n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script lang="ts" setup>
import { h, onMounted, reactive, ref } from 'vue'
import type { DataTableColumns, FormInst, TreeOption } from 'naive-ui'
import { NButton, NIcon, NPopconfirm, NSpace, NTag, useDialog, useMessage } from 'naive-ui'
import { AddOutline } from '@vicons/ionicons5'
import {
  addMenus,
  deleteMenus,
  getMenuChild,
  getMenus,
  getMenusTree,
  updateMenus
} from '@/api/system/menu'
import type { MenuData } from './types'
import TableMain from '@/components/table/main/TableMain.vue'
import IconSelect from '@/components/common/IconSelect.vue'
import SvgIcon from '@/components/svg-icon/SvgIcon.vue'

// 消息和对话框
const message = useMessage()
const dialog = useDialog()

// 表格数据和加载状态
const tableData = reactive({
  list: [] as MenuData[],
  loading: false
})

// 表单相关
const formRef = ref<FormInst | null>(null)
const formDialog = reactive({
  visible: false,
  title: '',
  submitLoading: false,
  mode: 'add'
})

// 表单数据
const formData = reactive<Partial<MenuData>>({
  menuName: '',
  menuUrl: '',
  localFilePath: '',
  perme: '',
  icon: '',
  iconPrefix: 'iconfont',
  orderNum: 0,
  menuType: 1,
  parentId: undefined,
  cacheable: 0,
  hidden: 0,
  affix: 0,
  remark: ''
})

// 表单校验规则
const formRules = {
  menuName: {
    required: true,
    message: '请输入菜单名称',
    trigger: ['blur', 'input']
  },
  menuType: {
    required: true,
    message: '请选择菜单类型',
    trigger: ['blur', 'change'],
    type: 'number'
  },
  menuUrl: {
    required: true,
    message: '请输入菜单路径',
    trigger: ['blur', 'input']
  },
  orderNum: {
    required: true,
    type: 'number',
    message: '请输入显示排序',
    trigger: ['blur', 'change']
  }
}

// 菜单树选项
const menuTreeOptions = ref<TreeOption[]>([])

// 表格列定义
const tableColumns: DataTableColumns<MenuData> = [
  {
    title: '菜单名称',
    key: 'menuName',
    width: 250,
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '图标',
    key: 'icon',
    width: 60,
    align: 'center' as const,
    render: (row: MenuData) => {
      if (!row.icon) return null
      return h(SvgIcon, {
        prefix: row.iconPrefix || 'iconfont',
        name: row.icon
      })
    }
  },
  {
    title: '排序',
    key: 'orderNum',
    width: 60,
    align: 'center' as const
  },
  {
    title: '权限标识',
    key: 'perme',
    width: 180,
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '路由地址',
    key: 'menuUrl',
    width: 180,
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '组件路径',
    key: 'localFilePath',
    width: 180,
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '菜单类型',
    key: 'menuType',
    width: 80,
    render: (row: MenuData) => {
      const typeMap = {
        0: { label: '目录', type: 'default' },
        1: { label: '菜单', type: 'info' },
        2: { label: '按钮', type: 'success' }
      }
      const type = typeMap[row.menuType as keyof typeof typeMap]
      return h(
        NTag,
        { type: type.type as 'default' | 'info' | 'success', size: 'small' },
        {
          default: () => type.label
        }
      )
    }
  },
  {
    title: '状态',
    key: 'status',
    width: 250,
    render: (row: MenuData) => {
      return h(
        NSpace,
        { size: 'small' },
        {
          default: () => [
            h(
              NTag,
              { type: row.hidden ? 'warning' : 'success', size: 'small' },
              { default: () => (row.hidden ? '隐藏' : '显示') }
            ),
            h(
              NTag,
              { type: row.cacheable ? 'info' : 'default', size: 'small' },
              { default: () => (row.cacheable ? '缓存' : '不缓存') }
            ),
            h(
              NTag,
              { type: row.affix ? 'error' : 'default', size: 'small' },
              { default: () => (row.affix ? '固定' : '不固定') }
            )
          ]
        }
      )
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 200,
    fixed: 'right' as const,
    render: (row: MenuData) => {
      return h(
        NSpace,
        { size: 'small' },
        {
          default: () => [
            h(
              NButton,
              {
                size: 'small',
                type: 'primary',
                ghost: true,
                onClick: () => handleEdit(row)
              },
              { default: () => '编辑' }
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

// 处理加载子节点数据
const handleLoad = (row: MenuData): Promise<void> => {
  return new Promise<void>((resolve) => {
    getMenuChild({ menuId: row.menuId })
      .then((res) => {
        if (res.code === 200 && res.data) {
          row.children = res.data.map((item: any) => {
            item.isLeaf = !(item.isLeaf && item.isLeaf === '1')
            return item
          })
          resolve()
        }
      })
      .catch((error) => {
        console.error('加载子菜单失败:', error)
      })
  })
}

// 修改加载表格数据的函数
const loadTableData = async () => {
  try {
    tableData.loading = true
    const res = await getMenus({})
    if (res.code === 200 && res.data) {
      tableData.list = res.data.map((item: any) => {
        item.isLeaf = !(item.isLeaf && item.isLeaf === '1')
        return item
      })
    }
  } catch (error) {
    console.error('加载菜单数据失败:', error)
    message.error('加载菜单数据失败')
  } finally {
    tableData.loading = false
  }
}

// 加载菜单树数据
const loadMenuTree = async () => {
  try {
    const res = await getMenusTree()
    if (res.code === 200 && res.data) {
      menuTreeOptions.value = handleMenuTreeData(res.data)
    }
  } catch (error) {
    console.error('加载菜单树失败:', error)
    message.error('加载菜单树失败')
  }
}

// 处理菜单树数据
const handleMenuTreeData = (data: MenuData[]): TreeOption[] => {
  if (!data) return []
  return data.map((item) => ({
    label: item.menuName,
    key: item.menuId,
    children: item.children ? handleMenuTreeData(item.children) : undefined
  }))
}

// 新增菜单
const handleAdd = () => {
  formDialog.mode = 'add'
  formDialog.title = '新增菜单'
  Object.assign(formData, {
    menuName: '',
    menuUrl: '',
    localFilePath: '',
    perme: '',
    icon: '',
    iconPrefix: 'iconfont',
    orderNum: 0,
    menuType: 1,
    parentId: undefined,
    cacheable: 0,
    hidden: 0,
    affix: 0,
    remark: ''
  })
  formDialog.visible = true
}

// 编辑菜单
const handleEdit = (row: MenuData) => {
  formDialog.mode = 'edit'
  formDialog.title = '编辑菜单'
  Object.assign(formData, {
    ...row,
    parentId: row.parentId || undefined
  })
  formDialog.visible = true
}

// 删除菜单
const handleDelete = async (row: MenuData) => {
  try {
    await deleteMenus({ menuId: row.menuId })
    message.success('删除成功')
    loadTableData()
  } catch (error) {
    message.error('删除失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()
    formDialog.submitLoading = true

    const api = formDialog.mode === 'add' ? addMenus : updateMenus
    const res = await api(formData)

    if (res.code === 200) {
      message.success(`${formDialog.mode === 'add' ? '新增' : '编辑'}成功`)
      formDialog.visible = false
      loadTableData()
    } else {
      message.error(res.message || `${formDialog.mode === 'add' ? '新增' : '编辑'}失败`)
    }
  } catch (error) {
    console.error(error)
    message.error(`${formDialog.mode === 'add' ? '新增' : '编辑'}失败`)
  } finally {
    formDialog.submitLoading = false
  }
}

// 初始化
onMounted(() => {
  loadTableData()
  loadMenuTree()
})
</script>

<style lang="scss" scoped>
.menu-container {
  padding: 16px;
  background-color: #fff;
  border-radius: 4px;

  .table-header {
    margin-bottom: 16px;
  }

  :deep(.n-data-table .n-data-table-td) {
    padding: 8px;
  }

  :deep(.n-tag) {
    padding: 0 12px;
  }

  :deep(.n-button) {
    padding: 0 16px;
  }
}
</style>
