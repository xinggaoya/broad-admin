<template>
  <section class="sys-dept-page">
    <header class="page-header">
      <div>
        <p class="header-subtitle">系统管理</p>
        <h2>部门管理</h2>
        <p class="header-desc">构建清晰的组织树，随时掌握部门结构与状态。</p>
      </div>
      <div class="header-actions">
        <n-button tertiary round size="small" :loading="tableLoading" @click="handleRefresh">
          <template #icon>
            <n-icon><RefreshOutline /></n-icon>
          </template>
          刷新
        </n-button>
        <n-button type="primary" round @click="handleAdd" v-auth="['sys:dept:add']">
          <template #icon>
            <n-icon><AddOutline /></n-icon>
          </template>
          新增部门
        </n-button>
      </div>
    </header>

    <div class="dept-content">
      <aside class="dept-sidebar">
        <n-card class="dept-tree-card" size="small" :bordered="false">
          <template #header>
            <div class="sidebar-header">
              <div>
                <p class="sidebar-label">组织视图</p>
                <h3>部门树结构</h3>
              </div>
              <n-button text size="small" @click="toggleTreeExpand">
                {{ expandAllFlag ? '收起全部' : '展开全部' }}
              </n-button>
            </div>
          </template>
          <div class="sidebar-search">
            <n-input v-model:value="treePattern" placeholder="搜索部门" size="small" clearable>
              <template #prefix>
                <n-icon><SearchOutline /></n-icon>
              </template>
            </n-input>
          </div>
          <div class="sidebar-stats">
            <div class="stat-item">
              <span class="stat-label">部门总数</span>
              <strong>{{ deptSummary.total }}</strong>
            </div>
            <div class="stat-item">
              <span class="stat-label">启用中</span>
              <strong>{{ deptSummary.active }}</strong>
            </div>
          </div>
          <n-spin :show="tableLoading">
            <n-tree
              block-line
              :pattern="treePattern"
              :data="deptTree"
              selectable
              :selected-keys="selectedTreeKeys"
              :expanded-keys="treeExpandedKeys"
              @update:selected-keys="handleTreeSelect"
              @update:expanded-keys="handleTreeExpand"
            />
          </n-spin>
        </n-card>
      </aside>

      <main class="dept-table">
        <n-card :bordered="false" class="table-wrapper">
          <!-- 独立搜索区域 -->
          <n-card size="small" class="search-card" style="margin-bottom: 16px;">
            <template #header>
              <span>搜索条件</span>
            </template>
            <template #default>
              <n-form inline :label-width="80" :model="searchModel">
                <n-form-item label="部门名称">
                  <n-input
                    v-model:value="searchModel.deptName"
                    placeholder="请输入部门名称"
                    clearable
                    @keydown.enter="handleSearchClick"
                  />
                </n-form-item>
                <n-form-item label="状态">
                  <n-select
                    v-model:value="searchModel.status"
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
            ref="tableMainRef"
            :columns="columns"
            :data="tableData"
            :loading="tableLoading"
            row-key="deptId"
            :children-key="childrenKey"
            sticky-toolbar
            :default-expand-all="false"
            v-model:expanded-row-keys="expandedRowKeys"
            :scroll-x="deptTableScrollX"
            :toolbar-config="{ column: true, density: true, refresh: true, fullscreen: true }"
            @refresh="handleRefresh"
          >
            <template #header>
              <div class="table-title">
                <h3>部门列表</h3>
                <p>组织架构实时同步，当前共 {{ deptSummary.total }} 个部门</p>
              </div>
            </template>
            <template #header-extra>
              <n-space>
                <n-button tertiary round size="small" @click="toggleExpandRows">
                  <template #icon>
                    <n-icon>
                      <component :is="expandAllFlag ? ChevronUpOutline : ChevronDownOutline" />
                    </n-icon>
                  </template>
                  {{ expandAllFlag ? '全部收起' : '全部展开' }}
                </n-button>
                <n-button type="primary" round @click="handleAdd" v-auth="['sys:dept:add']">
                  <template #icon>
                    <n-icon><AddOutline /></n-icon>
                  </template>
                  新增部门
                </n-button>
              </n-space>
            </template>
          </TableMain>
        </n-card>
      </main>
    </div>

    <n-modal
      v-model:show="showEditModal"
      :title="editType === 'add' ? '新增部门' : '编辑部门'"
      preset="card"
      :style="{ width: '640px' }"
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
          <n-divider title-placement="left">基础信息</n-divider>
          <n-grid :cols="24" :x-gap="24">
            <n-form-item-gi :span="24" label="上级部门" path="parentId">
              <n-tree-select
                v-model:value="formData.parentId"
                :options="treeSelectOptions"
                placeholder="选择上级部门"
                clearable
              />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="部门名称" path="deptName">
              <n-input v-model:value="formData.deptName" placeholder="请输入部门名称" />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="显示顺序" path="orderNum">
              <n-input-number
                v-model:value="formData.orderNum"
                :min="0"
                placeholder="请输入排序"
                style="width: 100%"
              />
            </n-form-item-gi>
          </n-grid>

          <n-divider title-placement="left">负责人信息</n-divider>
          <n-grid :cols="24" :x-gap="24">
            <n-form-item-gi :span="12" label="负责人" path="leader">
              <n-input v-model:value="formData.leader" placeholder="请输入负责人" />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="联系电话" path="phone">
              <n-input v-model:value="formData.phone" placeholder="请输入联系电话" />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="邮箱" path="email">
              <n-input v-model:value="formData.email" placeholder="请输入邮箱" />
            </n-form-item-gi>
            <n-form-item-gi :span="12" label="状态" path="status">
              <n-radio-group v-model:value="formData.status" name="status">
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
          <n-button @click="showEditModal = false">取消</n-button>
          <n-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</n-button>
        </n-space>
      </template>
    </n-modal>
  </section>
</template>

<script lang="ts" setup>
import { h, reactive, ref, computed, nextTick } from 'vue'
import { useMessage, NTag, NSpace, NButton, NPopconfirm } from 'naive-ui'
import type { FormInst, TreeSelectOption, TreeOption } from 'naive-ui'
import {
  AddOutline,
  RefreshOutline,
  SearchOutline,
  ChevronUpOutline,
  ChevronDownOutline
} from '@vicons/ionicons5'
import TableMain from '@/components/table/main/TableMain.vue'
import { useDict } from '@/utils/useDict'
import { useTableMain } from '@/hooks/useTableMain'
import { getDeptPage, addDept, updateDept, deleteDept } from '@/api/system/dept'

interface DeptEntity {
  deptId: number
  parentId: number
  deptName: string
  leader?: string
  phone?: string
  email?: string
  orderNum: number
  status: string
  ancestors?: string
  children?: DeptEntity[]
}

const { tableMainRef } = useTableMain()
const message = useMessage()
const formRef = ref<FormInst | null>(null)
const { sys_normal_disable } = useDict('sys_normal_disable')

const childrenKey = 'children'
const tableLoading = ref(false)
const tableData = ref<DeptEntity[]>([])
const expandedRowKeys = ref<Array<number | string>>([])
const expandAllFlag = ref(true)

const deptTree = ref<TreeOption[]>([])
const treePattern = ref('')
const selectedTreeKeys = ref<Array<string | number>>([])
const treeExpandedKeys = ref<Array<string | number>>([])
const activeTreeKey = ref<number | null>(null)

const searchModel = ref({
  deptName: '',
  status: null as string | null
})

const flattenDeptData = computed(() => {
  const result: DeptEntity[] = []
  const traverse = (list: DeptEntity[]) => {
    list.forEach((item) => {
      result.push(item)
      if (item.children?.length) {
        traverse(item.children)
      }
    })
  }
  traverse(tableData.value || [])
  return result
})

const deptSummary = computed(() => {
  const total = flattenDeptData.value.length
  const active = flattenDeptData.value.filter((item) => item.status === '0').length
  return {
    total,
    active
  }
})

const treeSelectOptions = computed<TreeSelectOption[]>(() => {
  const formatTreeForSelect = (list: DeptEntity[]): TreeSelectOption[] => {
    return list.map((item) => ({
      label: item.deptName,
      key: item.deptId,
      value: item.deptId,
      disabled:
        editType.value === 'edit' &&
        (item.deptId === formData.deptId ||
          (formData.ancestors && formData.ancestors.split(',').includes(String(item.deptId)))),
      children: item.children?.length ? formatTreeForSelect(item.children) : undefined
    }))
  }
  return [
    {
      label: '顶级部门',
      key: 0,
      value: 0
    },
    ...formatTreeForSelect(tableData.value || [])
  ]
})

const cleanParams = (params: Record<string, any>) => {
  return Object.entries(params).reduce((acc, [key, value]) => {
    if (value !== '' && value !== null && value !== undefined) {
      acc[key] = value
    }
    return acc
  }, {} as Record<string, any>)
}

const collectTreeKeys = (list: TreeOption[], bucket: Array<string | number>) => {
  list.forEach((item) => {
    bucket.push(item.key as string | number)
    if (item.children?.length) {
      collectTreeKeys(item.children, bucket)
    }
  })
}

const syncTreeExpandedKeys = () => {
  if (expandAllFlag.value) {
    const keys: Array<string | number> = []
    collectTreeKeys(deptTree.value, keys)
    treeExpandedKeys.value = keys
  } else {
    treeExpandedKeys.value = []
  }
}

const syncRowExpandedKeys = () => {
  expandedRowKeys.value = expandAllFlag.value
    ? flattenDeptData.value.map((item) => item.deptId)
    : []
}

const loadTableData = async () => {
  tableLoading.value = true
  try {
    const payload = cleanParams({
      ...searchModel.value,
      parentId: activeTreeKey.value ?? undefined
    })
    const res = await getDeptPage(payload)
    if (res.code === 200) {
      tableData.value = res.data || []
      deptTree.value = (res.data || []).map(transformDeptToTree)
      nextTick(() => {
        syncRowExpandedKeys()
        syncTreeExpandedKeys()
      })
    } else {
      message.error(res.message || '加载部门列表失败')
    }
  } catch (error) {
    console.error('加载部门列表失败:', error)
    message.error('加载部门列表失败')
  } finally {
    tableLoading.value = false
  }
}

const transformDeptToTree = (item: DeptEntity): TreeOption => {
  return {
    key: item.deptId,
    label: item.deptName,
    children: item.children?.length ? item.children.map(transformDeptToTree) : undefined
  }
}

const handleTreeSelect = (keys: Array<string | number>) => {
  selectedTreeKeys.value = keys
  activeTreeKey.value = keys.length ? Number(keys[0]) : null
  loadTableData()
}

const handleTreeExpand = (keys: Array<string | number>) => {
  treeExpandedKeys.value = keys
  const allKeys: Array<string | number> = []
  collectTreeKeys(deptTree.value, allKeys)
  expandAllFlag.value = keys.length > 0 && keys.length >= allKeys.length
}

const toggleTreeExpand = () => {
  expandAllFlag.value = !expandAllFlag.value
  syncTreeExpandedKeys()
  syncRowExpandedKeys()
}

const toggleExpandRows = () => {
  expandAllFlag.value = !expandAllFlag.value
  syncRowExpandedKeys()
  syncTreeExpandedKeys()
}

const handleSearchClick = () => {
  loadTableData()
}

const handleResetClick = () => {
  searchModel.value = {
    deptName: '',
    status: null
  }
  activeTreeKey.value = null
  selectedTreeKeys.value = []
  treePattern.value = ''
  loadTableData()
}

const handleRefresh = () => {
  loadTableData()
}

const columns = [
  {
    title: '部门名称',
    key: 'deptName',
    width: 260,
    render: (row: DeptEntity) =>
      h('div', { class: 'dept-name-cell' }, [
        h('span', row.deptName),
        h(
          NTag,
          { size: 'small', type: 'info', round: true },
          { default: () => `序号 ${row.orderNum}` }
        )
      ])
  },
  { title: '负责人', key: 'leader', width: 140 },
  { title: '联系电话', key: 'phone', width: 160 },
  { title: '邮箱', key: 'email', width: 200 },
  {
    title: '状态',
    key: 'status',
    width: 120,
    render: (row: DeptEntity) => {
      const status = sys_normal_disable.value.find((item) => item.value === row.status)
      return h(
        NTag,
        {
          size: 'small',
          round: true,
          type: row.status === '0' ? 'success' : 'error'
        },
        { default: () => status?.label || '未知' }
      )
    }
  },
  {
    title: '操作',
    key: 'actions',
    width: 220,
    fixed: 'right' as const,
    render: (row: DeptEntity) =>
      h(
        NSpace,
        { justify: 'start' },
        {
          default: () => [
            h(
              NButton,
              {
                type: 'primary',
                size: 'small',
                quaternary: true,
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
                      type: 'error',
                      size: 'small',
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

const deptTableScrollX = computed(() => {
  const total = columns.reduce((sum, column) => {
    const width = (column as any).width
    const minWidth = (column as any).minWidth
    if (typeof width === 'number') return sum + width
    if (typeof minWidth === 'number') return sum + minWidth
    return sum + 140
  }, 0)
  return Math.max(total, 960)
})

const showEditModal = ref(false)
const editType = ref<'add' | 'edit'>('add')
const submitLoading = ref(false)
const formData = reactive({
  deptId: null as number | null,
  parentId: 0,
  ancestors: '',
  deptName: '',
  leader: '',
  phone: '',
  email: '',
  orderNum: 0,
  status: '0'
})

const rules = {
  deptName: {
    required: true,
    message: '请输入部门名称',
    trigger: 'blur'
  },
  orderNum: {
    required: true,
    type: 'number',
    message: '请输入显示顺序',
    trigger: 'change'
  }
}

const handleAdd = () => {
  editType.value = 'add'
  formData.deptId = null
  formData.parentId = activeTreeKey.value ?? 0
  formData.deptName = ''
  formData.leader = ''
  formData.phone = ''
  formData.email = ''
  formData.orderNum = 0
  formData.status = '0'
  formData.ancestors = ''
  showEditModal.value = true
}

const handleEdit = (row: DeptEntity) => {
  editType.value = 'edit'
  Object.assign(formData, row)
  showEditModal.value = true
}

const handleSubmit = async () => {
  await formRef.value?.validate()
  submitLoading.value = true
  try {
    const payload = cleanParams({ ...formData })
    const api = editType.value === 'add' ? addDept : updateDept
    const res = await api(payload)
    if (res.code === 200) {
      message.success(editType.value === 'add' ? '新增成功' : '更新成功')
      showEditModal.value = false
      loadTableData()
    } else {
      message.error(res.message || '保存失败')
    }
  } catch (error) {
    console.error('保存部门失败:', error)
    message.error('保存失败')
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = async (row: DeptEntity) => {
  try {
    const res = await deleteDept({ idList: [row.deptId] })
    if (res.code === 200) {
      message.success('删除成功')
      loadTableData()
    } else {
      message.error(res.message || '删除失败')
    }
  } catch (error) {
    console.error('删除部门失败:', error)
    message.error('删除失败')
  }
}

loadTableData()
</script>

<style lang="scss" scoped>
.sys-dept-page {
  display: flex;
  flex-direction: column;
  gap: 16px;
  width: 100%;
  min-width: 0;

  .page-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 24px;
    border-radius: 16px;
    background: linear-gradient(135deg, #f5f7ff 0%, #fff 100%);
    border: 1px solid rgba(82, 106, 255, 0.12);

    .header-subtitle {
      font-size: 14px;
      color: var(--text-color-3);
      margin-bottom: 4px;
    }

    h2 {
      margin: 0;
      font-size: 24px;
      color: var(--text-color);
    }

    .header-desc {
      margin-top: 4px;
      color: var(--text-color-2);
    }

    .header-actions {
      display: flex;
      gap: 12px;
    }
  }

  .dept-content {
    display: grid;
    grid-template-columns: minmax(260px, 320px) minmax(0, 1fr);
    gap: 16px;
    align-items: flex-start;
  }

  .dept-sidebar {
    min-width: 0;

    .dept-tree-card {
      height: 100%;

      .sidebar-header {
        display: flex;
        align-items: center;
        justify-content: space-between;

        .sidebar-label {
          font-size: 12px;
          color: var(--text-color-3);
          margin-bottom: 4px;
        }

        h3 {
          margin: 0;
          font-size: 18px;
        }
      }

      .sidebar-search {
        margin-bottom: 12px;
      }

      .sidebar-stats {
        display: flex;
        justify-content: space-between;
        margin-bottom: 12px;

        .stat-item {
          display: flex;
          flex-direction: column;
          gap: 4px;

          .stat-label {
            font-size: 12px;
            color: var(--text-color-3);
          }

          strong {
            font-size: 18px;
          }
        }
      }
    }
  }

  .dept-table {
    min-width: 0;

    .search-card {
      background: linear-gradient(135deg, #f5f7ff 0%, #fff 100%);
      border: 1px solid rgba(82, 106, 255, 0.12);
    }

    .table-wrapper {
      min-height: 640px;
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
  }

  .dept-name-cell {
    display: flex;
    align-items: center;
    gap: 8px;
  }
}

@media (max-width: 1200px) {
  .sys-dept-page {
    .dept-content {
      grid-template-columns: 1fr;
    }

    .dept-sidebar,
    .dept-table {
      width: 100%;
    }
  }
}
</style>
