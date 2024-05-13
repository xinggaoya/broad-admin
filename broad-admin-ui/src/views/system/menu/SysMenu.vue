<template>
  <div class="main-container">
    <TableMain
      :data="tableList"
      :columns="tableColumns"
      :loading="tableLoading"
      allow-checking-not-loaded
      :cascade="false"
      :row-key="rowKey"
      :scroll-x="1800"
      :on-load="onLoadData"
    >
      <template v-slot:header>
        <AddButton @add="onAddItem" />
      </template>
    </TableMain>

    <ModalDialog
      v-model="modalDialog"
      @confirm="onConfirm"
      :title="title"
      :submitLoading="submitLoading"
    >
      <template #content>
        <div style="width: 100%">
          <n-form ref="menuFromRef" :model="menuFromData" :rules="rules" label-width="auto">
            <n-grid :cols="2" :x-gap="20">
              <n-form-item-gi label="菜单类型" path="menuType">
                <n-radio-group v-model:value="menuFromData.menuType" name="menuType">
                  <n-radio-button :key="1" :value="1" label="菜单" />
                  <n-radio-button :key="0" :value="0" label="目录" />
                  <n-radio-button :key="2" :value="2" label="按钮" />
                </n-radio-group>
              </n-form-item-gi>
              <n-form-item-gi label="上级菜单" path="parentId">
                <n-tree-select
                  v-model:value="menuFromData.parentId"
                  :options="menuTree"
                  placeholder="请选择上级菜单"
                  :clearable="true"
                />
              </n-form-item-gi>
              <n-form-item-gi label="菜单名称" path="menuName">
                <n-input
                  v-model:value="menuFromData.menuName"
                  placeholder="输入菜单名称;例：菜单管理"
                />
              </n-form-item-gi>
              <n-form-item-gi v-if="menuFromData.menuType !== 2" label="菜单路径" path="menuUrl">
                <n-input
                  v-model:value="menuFromData.menuUrl"
                  placeholder="输入菜单路径（输入http开头为点击跳转）;例：/system/menu"
                />
              </n-form-item-gi>
              <n-form-item-gi v-if="menuFromData.menuType == 1" label="组件路径" path="localFilePath">
                <n-input
                  v-model:value="menuFromData.localFilePath"
                  placeholder="输入组件路径;例：/menu/index"
                />
              </n-form-item-gi>
              <n-form-item-gi v-if="menuFromData.menuType !== 2" label="外链地址" path="iframeUrl">
                <n-input
                  v-model:value="menuFromData.iframeUrl"
                  placeholder="输入外链地址（内嵌）;例：https://www.baidu.com"
                />
              </n-form-item-gi>
              <n-form-item-gi label="菜单权限" path="perme">
                <n-input
                  v-model:value="menuFromData.perme"
                  placeholder="输入菜单权限;例:system:user:menu"
                />
              </n-form-item-gi>
              <n-form-item-gi v-if="menuFromData.menuType !== 2" label="菜单图标" path="icon">
                <IconSelector v-model:value="menuFromData.icon" />
              </n-form-item-gi>
              <n-form-item-gi label="排序" path="orderNum">
                <n-input-number v-model:value="menuFromData.orderNum" :min="0" :max="100" />
              </n-form-item-gi>
              <n-form-item-gi v-if="menuFromData.menuType !== 2" label="是否缓存" path="cacheable">
                <n-radio-group v-model:value="menuFromData.cacheable" name="cacheradiogroup">
                  <n-space>
                    <n-radio-button
                      v-for="song in sys_yes_no"
                      :key="song.value"
                      :value="song.value"
                    >
                      {{ song.label }}
                    </n-radio-button>
                  </n-space>
                </n-radio-group>
              </n-form-item-gi>
              <n-form-item-gi v-if="menuFromData.menuType !== 2" label="是否隐藏" path="hidden">
                <n-radio-group v-model:value="menuFromData.hidden" name="hiddencacheradiogroup">
                  <n-space>
                    <n-radio-button
                      v-for="song in sys_yes_no"
                      :key="song.value"
                      :value="song.value"
                    >
                      {{ song.label }}
                    </n-radio-button>
                  </n-space>
                </n-radio-group>
              </n-form-item-gi>
              <n-form-item-gi v-if="menuFromData.menuType !== 2" label="是否固定" path="affix">
                <n-radio-group v-model:value="menuFromData.affix" name="affixcacheradiogroup">
                  <n-space>
                    <n-radio-button
                      v-for="song in sys_yes_no"
                      :key="song.value"
                      :value="song.value"
                    >
                      {{ song.label }}
                    </n-radio-button>
                  </n-space>
                </n-radio-group>
              </n-form-item-gi>
              <n-form-item-gi label="备注" path="remark">
                <n-input
                  v-model:value="menuFromData.remark"
                  type="textarea"
                  placeholder="输入备注"
                />
              </n-form-item-gi>
            </n-grid>
          </n-form>
        </div>
      </template>
    </ModalDialog>
  </div>
</template>

<script lang="ts" setup>
import { h, onMounted, ref } from 'vue'
import {
  getMenus,
  getMenusTree,
  addMenus,
  updateMenus,
  getMenuChild,
  deleteMenus
} from '@/api/system/menu'
import { useRenderAction, useRowKey } from '@/hooks/useTable'
import { NIcon, useDialog, useMessage } from 'naive-ui'
import IconSelector from '@/components/common/IconSelector.vue'
import { useDict } from '@/utils/useDict'
import DictTag from '@/components/tag/DictTag.vue'
import TableMain from '@/components/table/main/TableMain.vue'
import AddButton from '@/components/common/AddButton.vue'
import SvgIcon from '@/components/svg-icon/index.vue'

const { sys_yes_no } = useDict('sys_yes_no')
let actionModel = 'add'
const tableList = ref([])
const tableLoading = ref<boolean>(false)
const title = ref('')
const naiveDialog = useDialog()
const submitLoading = ref<boolean>(false)
const message = useMessage()
const menuTree = ref()
const menuFromData = ref<any>({})
const modalDialog = ref<boolean>(false)
const menuFromRef = ref<any>(null)
const rowKey = useRowKey('menuUrl')
const searchForm: any = ref({})
const tableColumns = ref([
  {
    title: '菜单名称',
    key: 'menuName',
    align: 'left',
    width: 200
  },
  {
    title: '菜单地址',
    key: 'menuUrl',
    align: 'left',
    width: 200
  },
  {
    title: '组件路径',
    key: 'localFilePath',
    align: 'left',
    width: 200
  },
  {
    title: '权限规则',
    key: 'perme',
    align: 'left',
    width: 200
  },
  {
    title: '菜单图标',
    key: 'icon',
    width: 120,
    render: (rowData: { iconPrefix: string; icon: string }) => {
      return rowData.iconPrefix === 'iconfont'
        ? h(SvgIcon, {
          prefix: rowData.iconPrefix ? (rowData.iconPrefix as string) : 'iconfont',
          name: rowData.icon ? (rowData.icon as string) : 'menu'
        })
        : h(NIcon, null, {
          default: () => {
            return h(
              'svg',
              {
                'aria-hidden': false
              },
              {
                default: () => {
                  return [
                    h('use', {
                      href: '#icon-menu'
                    })
                  ]
                }
              }
            )
          }
        })
    }
  },
  {
    title: '排序',
    key: 'orderNum',
    width: 100
  },
  {
    title: '缓存',
    key: 'cacheable',
    width: 120,
    render: (rowData: { cacheable: any }) => {
      return h(DictTag, {
        options: sys_yes_no.value,
        value: rowData.cacheable
      })
    }
  },
  {
    title: '隐藏',
    key: 'hidden',
    width: 120,
    render: (rowData: { hidden: any }) => {
      return h(DictTag, {
        options: sys_yes_no.value,
        value: rowData.hidden
      })
    }
  },
  {
    title: '固定标题栏',
    key: 'affix',
    width: 120,
    render: (rowData: { affix: any }) => {
      return h(DictTag, {
        options: sys_yes_no.value,
        value: rowData.affix
      })
    }
  },
  {
    title: '操作',
    key: 'actions',
    fixed: 'right',
    width: 120,
    render: (rowData: any) => {
      return useRenderAction([
        {
          label: '编辑',
          type: 'primary',
          onClick: () => onUpdateItem(rowData)
        },
        {
          label: '删除',
          type: 'error',
          onClick: () => onDeleteItem(rowData)
        }
      ])
    }
  }
])

const rules = {
  menuName: [
    {
      required: true,
      message: '请输入菜单名称'
    }
  ],
  menuUrl: [
    {
      required: true,
      message: '请输入菜单地址'
    }
  ],
  cacheable: [
    {
      required: true,
      message: '请选择是否缓存'
    }
  ],
  hidden: [
    {
      required: true,
      message: '请选择是否隐藏'
    }
  ],
  affix: [
    {
      required: true,
      message: '请选择是否固定标题栏'
    }
  ],
  orderNum: [
    {
      required: true,
      type: 'number',
      message: '请输入排序'
    }
  ]
}

function doRefresh() {
  tableLoading.value = true
  getMenus(searchForm.value).then((res: any) => {
    tableList.value = hasChildren(res.data)
    tableLoading.value = false
  })
}

// 递归是否有子节点
function hasChildren(data: any) {
  data.forEach((item: any) => {
    if (item.children && item.children.length > 0) {
      item.isLeaf = true
      hasChildren(item.children)
    } else {
      item.isLeaf = false
    }
  })
  return data
}

function updateIcon(icon: any) {
  menuFromData.value.icon = icon.name
}

function onLoadData(rowData: any) {
  return new Promise<void>((resolve) => {
    getMenuChild({ menuId: rowData.menuId }).then((res: any) => {
      rowData.children = res.data
      resolve()
    })
  })
}

function onAddItem() {
  actionModel = 'add'
  title.value = '新增菜单'
  modalDialog.value = true
  menuFromData.value = {}
}

function onUpdateItem(item: any) {
  actionModel = 'edit'
  title.value = '编辑菜单'
  menuFromData.value = item
  modalDialog.value = true
}

function onConfirm() {
  submitLoading.value = true
  menuFromData.value.isLeaf = null
  menuFromData.value.iconPrefix = 'iconfont'
  menuFromData.value.children = null
  if (actionModel === 'add') {
    if (menuFromRef.value?.validate()) {
      addMenus(menuFromData.value).then((res: any) => {
        if (res.code === 200) {
          doRefresh()
          modalDialog.value = false
          message.success('添加成功')
        }
        submitLoading.value = false
      })
    }
  } else {
    if (menuFromRef.value?.validate()) {
      updateMenus(menuFromData.value).then((res: any) => {
        if (res.code === 200) {
          doRefresh()
          modalDialog.value = false
          message.success('修改成功')
        }
        submitLoading.value = false
      })
    }
  }
}

function onDeleteItem(item: any) {
  naiveDialog.warning({
    title: '提示',
    content: '是否要删除此数据？',
    positiveText: '删除',
    onPositiveClick: () => {
      deleteMenus({ idList: item.menuId }).then((res: any) => {
        if (res.code === 200) {
          doRefresh()
          message.success('删除成功')
        }
      })
    }
  })
}

function onSearch() {
  doRefresh()
}

function onResetSearch() {
  searchForm.value?.reset()
}

function getMenuTree() {
  getMenusTree().then((res: any) => {
    menuTree.value = mapTree(res.data)
  })
}

// map处理树形
function mapTree(data: any) {
  return data.map((item: any) => {
    const children = item.children
    if (children && children.length > 0) {
      return {
        label: item.menuName,
        key: item.menuId,
        children: mapTree(children)
      }
    } else {
      return {
        label: item.menuName,
        key: item.menuId
      }
    }
  })
}

onMounted(() => {
  doRefresh()
  getMenuTree()
})
</script>
<style lang="scss" scoped>
.icon-wrapper {
  list-style: none;
  border: 1px solid #f5f5f5;
  overflow: hidden;
  padding: 0;

  .icon-item {
    float: left;
    width: 25%;
    font-size: 26px;
    border-right: 1px solid #f5f5f5;
    border-bottom: 1px solid #f5f5f5;
    text-align: center;
    cursor: pointer;

    & > div {
      font-size: 12px;
    }

    &:hover {
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12),
      0 0 6px rgba(0, 0, 0, 0.04);
    }
  }
}
</style>
