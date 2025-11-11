<template>
  <section class="sys-menu-page">
    <header class="page-header">
      <div>
        <p class="header-subtitle">系统管理</p>
        <h2>菜单管理</h2>
        <p class="header-desc">维护系统导航与权限入口，快速定位任意菜单。</p>
      </div>
      <div class="header-actions">
        <n-input
          v-model:value="searchText"
          placeholder="搜索菜单名称"
          clearable
          @keydown.enter="onSearch"
        >
          <template #prefix>
            <n-icon><SearchOutline /></n-icon>
          </template>
          <template #suffix>
            <n-button text type="primary" size="small" @click="onSearch">搜索</n-button>
          </template>
        </n-input>
        <n-button tertiary round size="small" :loading="tableData.loading" @click="loadTableData">
          <template #icon>
            <n-icon><RefreshOutline /></n-icon>
          </template>
          刷新
        </n-button>
        <n-button type="primary" round @click="handleAdd">
          <template #icon>
            <n-icon><AddOutline /></n-icon>
          </template>
          新增菜单
        </n-button>
      </div>
    </header>

    <div class="summary-cards">
      <div class="summary-card">
        <span>目录</span>
        <strong>{{ menuSummary.dir }}</strong>
      </div>
      <div class="summary-card">
        <span>菜单</span>
        <strong>{{ menuSummary.menu }}</strong>
      </div>
      <div class="summary-card">
        <span>按钮</span>
        <strong>{{ menuSummary.button }}</strong>
      </div>
    </div>

    <div class="menu-layout">
      <aside class="menu-tree-panel">
        <n-card size="small" :bordered="false">
          <template #header>
            <div class="card-header">
              <div>
                <p class="header-label">结构视图</p>
                <h3>菜单树</h3>
              </div>
              <n-button text size="small" @click="toggleTreeExpand">
                {{ expandAllTree ? '收起全部' : '展开全部' }}
              </n-button>
            </div>
          </template>
          <div class="tree-actions">
            <n-input v-model:value="treePattern" size="small" placeholder="过滤节点" clearable>
              <template #prefix>
                <n-icon><SearchOutline /></n-icon>
              </template>
            </n-input>
          </div>
          <n-spin :show="tableData.loading">
            <n-tree
              block-line
              :pattern="treePattern"
              :data="menuTreeData"
              :selected-keys="selectedKeys"
              :expanded-keys="expandedKeys"
              selectable
              @update:selected-keys="handleSelectMenu"
              @update:expanded-keys="handleExpandMenu"
            >
              <template #default="{ option }">
                <div class="tree-node-label">
                  <div class="tree-node-icon">
                    <n-icon v-if="option.menuType !== 2">
                      <SvgIcon
                        :prefix="option.iconPrefix || 'iconfont'"
                        :name="option.icon || 'menu'"
                      />
                    </n-icon>
                    <n-icon v-else>
                      <KeyOutline />
                    </n-icon>
                  </div>
                  <span>{{ option.label }}</span>
                  <n-tag
                    size="small"
                    class="type-tag"
                    :type="getMenuTypeTagType(option.menuType)"
                  >
                    {{ getMenuTypeLabel(option.menuType) }}
                  </n-tag>
                </div>
              </template>
            </n-tree>
            <n-empty v-if="menuTreeData.length === 0" description="暂无菜单数据" />
          </n-spin>
        </n-card>
      </aside>

      <main class="menu-detail-panel">
        <n-card class="detail-card" :bordered="false">
          <template #header>
            <div class="detail-header">
              <div>
                <p class="header-label">菜单详情</p>
                <h3>{{ selectedMenu?.menuName || '请选择左侧菜单节点' }}</h3>
                <p v-if="selectedMenu" class="detail-path">
                  {{ selectedMenu.menuUrl || '未配置路径' }}
                </p>
              </div>
              <n-space v-if="selectedMenu">
                <n-button secondary strong @click="handleEdit(selectedMenu)">
                  <template #icon>
                    <n-icon><PencilOutline /></n-icon>
                  </template>
                  编辑
                </n-button>
                <n-popconfirm @positive-click="handleDelete(selectedMenu)">
                  <template #trigger>
                    <n-button secondary strong type="error">
                      <template #icon>
                        <n-icon><TrashOutline /></n-icon>
                      </template>
                      删除
                    </n-button>
                  </template>
                  确定要删除此菜单吗？
                </n-popconfirm>
              </n-space>
            </div>
          </template>
          <div v-if="selectedMenu">
            <n-descriptions bordered :column="2" class="info-descriptions">
              <n-descriptions-item label="菜单名称">
                <n-ellipsis>{{ selectedMenu.menuName }}</n-ellipsis>
              </n-descriptions-item>
              <n-descriptions-item label="菜单类型">
                <n-tag :type="getMenuTypeTagType(selectedMenu.menuType)">
                  {{ getMenuTypeLabel(selectedMenu.menuType) }}
                </n-tag>
              </n-descriptions-item>
              <n-descriptions-item label="菜单图标" v-if="selectedMenu.menuType !== 2">
                <n-space align="center">
                  <SvgIcon
                    v-if="selectedMenu.icon"
                    :prefix="selectedMenu.iconPrefix || 'iconfont'"
                    :name="selectedMenu.icon"
                  />
                  <span v-if="selectedMenu.icon">{{ selectedMenu.icon }}</span>
                  <span v-else>无</span>
                </n-space>
              </n-descriptions-item>
              <n-descriptions-item label="显示排序">
                {{ selectedMenu.orderNum }}
              </n-descriptions-item>
              <n-descriptions-item label="权限标识" :span="selectedMenu.menuType !== 2 ? 1 : 2">
                <n-ellipsis>{{ selectedMenu.perme || '无' }}</n-ellipsis>
              </n-descriptions-item>
              <n-descriptions-item v-if="selectedMenu.menuType !== 2" label="路由地址">
                <n-ellipsis>{{ selectedMenu.menuUrl || '无' }}</n-ellipsis>
              </n-descriptions-item>
              <n-descriptions-item v-if="selectedMenu.menuType === 1" label="组件路径">
                <n-ellipsis>{{ selectedMenu.localFilePath || '无' }}</n-ellipsis>
              </n-descriptions-item>
              <n-descriptions-item v-if="selectedMenu.menuType !== 2" label="状态设置" :span="2">
                <n-space>
                  <n-tag :type="selectedMenu.hidden ? 'warning' : 'success'">
                    {{ selectedMenu.hidden ? '隐藏' : '显示' }}
                  </n-tag>
                  <n-tag :type="selectedMenu.cacheable ? 'info' : 'default'">
                    {{ selectedMenu.cacheable ? '缓存' : '不缓存' }}
                  </n-tag>
                  <n-tag :type="selectedMenu.affix ? 'error' : 'default'">
                    {{ selectedMenu.affix ? '固定' : '不固定' }}
                  </n-tag>
                </n-space>
              </n-descriptions-item>
              <n-descriptions-item label="备注" :span="2">
                {{ selectedMenu.remark || '无' }}
              </n-descriptions-item>
            </n-descriptions>
          </div>
          <n-empty v-else description="请选择左侧菜单项查看详情" />
        </n-card>
      </main>
    </div>

    <!-- 表单弹窗 -->
    <n-modal
      v-model:show="formDialog.visible"
      :title="formDialog.title"
      preset="card"
      :style="{ width: '700px', maxWidth: '90vw' }"
      :mask-closable="false"
      transform-origin="center"
      class="menu-form-modal"
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
          <n-tabs type="line" animated>
            <!-- 基本信息 -->
            <n-tab-pane name="basic" tab="基本信息">
              <n-grid :cols="24" :x-gap="24">
                <!-- 菜单类型 -->
                <n-form-item-gi :span="24" label="菜单类型" path="menuType">
                  <n-radio-group v-model:value="formData.menuType">
                    <n-radio-button :value="0">
                      <n-space align="center">
                        <n-icon><FolderOpenOutline /></n-icon>
                        <span>目录</span>
                      </n-space>
                    </n-radio-button>
                    <n-radio-button :value="1">
                      <n-space align="center">
                        <n-icon><MenuOutline /></n-icon>
                        <span>菜单</span>
                      </n-space>
                    </n-radio-button>
                    <n-radio-button :value="2">
                      <n-space align="center">
                        <n-icon><KeyOutline /></n-icon>
                        <span>按钮</span>
                      </n-space>
                    </n-radio-button>
                  </n-radio-group>
                </n-form-item-gi>

                <!-- 上级菜单 -->
                <n-form-item-gi :span="24" label="上级菜单" path="parentId">
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

                <!-- 显示排序 -->
                <n-form-item-gi :span="12" label="显示排序" path="orderNum">
                  <n-input-number
                    v-model:value="formData.orderNum"
                    :min="0"
                    :max="999"
                    placeholder="请输入显示排序"
                    class="full-width"
                  />
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
                <n-form-item-gi
                  :span="formData.menuType === 2 ? 24 : 12"
                  label="权限标识"
                  path="perme"
                >
                  <n-input
                    v-model:value="formData.perme"
                    placeholder="请输入权限标识，例：system:menu:list"
                  />
                </n-form-item-gi>

                <!-- 菜单图标 -->
                <n-form-item-gi
                  v-if="formData.menuType !== 2"
                  :span="24"
                  label="菜单图标"
                  path="icon"
                >
                  <IconSelect v-model:value="formData.icon" />
                </n-form-item-gi>
              </n-grid>
            </n-tab-pane>

            <!-- 高级设置 -->
            <n-tab-pane name="advanced" tab="高级设置" v-if="formData.menuType !== 2">
              <n-divider title-placement="left">显示设置</n-divider>
              <n-grid :cols="24" :x-gap="24">
                <n-grid-item :span="8">
                  <n-space vertical align="center">
                    <n-switch
                      v-model:value="formData.hidden"
                      :checked-value="0"
                      :unchecked-value="1"
                    />
                    <span>{{ formData.hidden ? '隐藏菜单' : '显示菜单' }}</span>
                  </n-space>
                </n-grid-item>
                <n-grid-item :span="8">
                  <n-space vertical align="center">
                    <n-switch
                      v-model:value="formData.cacheable"
                      :checked-value="1"
                      :unchecked-value="0"
                    />
                    <span>{{ formData.cacheable ? '开启缓存' : '关闭缓存' }}</span>
                  </n-space>
                </n-grid-item>
                <n-grid-item :span="8">
                  <n-space vertical align="center">
                    <n-switch
                      v-model:value="formData.affix"
                      :checked-value="1"
                      :unchecked-value="0"
                    />
                    <span>{{ formData.affix ? '固定标签' : '不固定标签' }}</span>
                  </n-space>
                </n-grid-item>
              </n-grid>

              <n-divider title-placement="left">备注</n-divider>
              <n-input
                v-model:value="formData.remark"
                type="textarea"
                placeholder="请输入备注信息"
                :autosize="{ minRows: 3, maxRows: 5 }"
              />
            </n-tab-pane>

            <!-- 按钮设置 -->
            <n-tab-pane name="button" tab="按钮设置" v-if="formData.menuType === 2">
              <n-divider title-placement="left">备注</n-divider>
              <n-input
                v-model:value="formData.remark"
                type="textarea"
                placeholder="请输入备注信息"
                :autosize="{ minRows: 3, maxRows: 5 }"
              />
            </n-tab-pane>
          </n-tabs>
        </n-form>
      </n-spin>
      <template #footer>
        <n-space justify="end">
          <n-button @click="formDialog.visible = false">取消</n-button>
          <n-button type="primary" :loading="formDialog.submitLoading" @click="handleSubmit">
            保存
          </n-button>
        </n-space>
      </template>
    </n-modal>
  </section>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, computed } from 'vue'
import type { FormInst, TreeOption } from 'naive-ui'
import {
  NButton,
  NIcon,
  NPopconfirm,
  NSpace,
  NTag,
  useDialog,
  useMessage,
  NEmpty,
  NTree
} from 'naive-ui'
import {
  AddOutline,
  RefreshOutline,
  SearchOutline,
  PencilOutline,
  TrashOutline,
  FolderOpenOutline,
  MenuOutline,
  KeyOutline
} from '@vicons/ionicons5'
import {
  addMenus,
  deleteMenus,
  getMenuChild,
  getMenus,
  getMenusTree,
  updateMenus
} from '@/api/system/menu'
import type { MenuData } from './types'
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

const menuSummary = computed(() => {
  const summary = {
    dir: 0,
    menu: 0,
    button: 0
  }
  const traverse = (list: MenuData[]) => {
    list.forEach((item) => {
      if (item.menuType === 0) {
        summary.dir += 1
      } else if (item.menuType === 1) {
        summary.menu += 1
      } else {
        summary.button += 1
      }
      if (item.children?.length) {
        traverse(item.children)
      }
    })
  }
  traverse(tableData.list || [])
  return summary
})

// 搜索相关
const searchText = ref('')
const onSearch = () => {
  loadTableData(searchText.value)
}

// 菜单树相关
const menuTreeData = ref<any[]>([])
const selectedKeys = ref<string[]>([])
const expandedKeys = ref<string[]>([])
const selectedMenu = ref<MenuData | null>(null)
const treePattern = ref('')
const expandAllTree = ref(true)

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

// 选择菜单
const handleSelectMenu = (keys: string[]) => {
  selectedKeys.value = keys
  if (keys.length > 0) {
    const id = keys[0]
    const menu = findMenuById(id)
    if (menu) {
      selectedMenu.value = menu
    }
  } else {
    selectedMenu.value = null
  }
}

// 展开菜单
const handleExpandMenu = (keys: string[]) => {
  expandedKeys.value = keys
  const allKeys = getAllTreeKeys()
  expandAllTree.value = keys.length > 0 && keys.length >= allKeys.length
}

const getAllTreeKeys = () => {
  const keys: string[] = []
  const traverse = (nodes: any[]) => {
    nodes.forEach((node) => {
      keys.push(String(node.key))
      if (node.children?.length) {
        traverse(node.children)
      }
    })
  }
  traverse(menuTreeData.value || [])
  return keys
}

const syncExpandedKeys = () => {
  if (expandAllTree.value) {
    expandedKeys.value = getAllTreeKeys()
  } else {
    expandedKeys.value = []
  }
}

const toggleTreeExpand = () => {
  expandAllTree.value = !expandAllTree.value
  syncExpandedKeys()
}

// 根据ID查找菜单
const findMenuById = (id: string): MenuData | null => {
  const findInList = (list: any[]) => {
    for (const item of list) {
      if (item.menuId.toString() === id) {
        return item
      }
      if (item.children && item.children.length > 0) {
        const found = findInList(item.children)
        if (found) return found
      }
    }
    return null
  }
  return findInList(tableData.list)
}

// 获取菜单类型标签类型
const getMenuTypeTagType = (type: number) => {
  const typeMap = {
    0: 'default',
    1: 'info',
    2: 'success'
  }
  return typeMap[type as keyof typeof typeMap]
}

// 获取菜单类型标签文字
const getMenuTypeLabel = (type: number) => {
  const typeMap = {
    0: '目录',
    1: '菜单',
    2: '按钮'
  }
  return typeMap[type as keyof typeof typeMap]
}

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

// 转换菜单数据为树形结构
const transformMenuToTree = (menuList: MenuData[]) => {
  return menuList.map((menu) => {
    const node = {
      key: menu.menuId.toString(),
      label: menu.menuName,
      menuId: menu.menuId,
      menuName: menu.menuName,
      menuUrl: menu.menuUrl,
      localFilePath: menu.localFilePath,
      perme: menu.perme,
      icon: menu.icon,
      iconPrefix: menu.iconPrefix,
      orderNum: menu.orderNum,
      menuType: menu.menuType,
      hidden: menu.hidden,
      cacheable: menu.cacheable,
      affix: menu.affix,
      remark: menu.remark,
      children:
        menu.children && menu.children.length > 0 ? transformMenuToTree(menu.children) : undefined
    }
    return node
  })
}

// 加载表格数据
const loadTableData = async (keyword = '') => {
  try {
    tableData.loading = true
    const res = await getMenusTree({ menuName: keyword })
    if (res.code === 200 && res.data) {
      tableData.list = res.data || []

      // 转换为树形结构
      menuTreeData.value = transformMenuToTree(tableData.list)

      // 如果有选中菜单，重新获取菜单数据
      if (selectedKeys.value.length > 0) {
        const id = selectedKeys.value[0]
        const menu = findMenuById(id)
        if (menu) {
          selectedMenu.value = menu
        } else {
          selectedMenu.value = null
          selectedKeys.value = []
        }
      }

      // 默认同步展开状态
      syncExpandedKeys()
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
    parentId: selectedMenu.value?.menuId || undefined,
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
    dialog.warning({
      title: '确认删除',
      content: `确定要删除菜单 "${row.menuName}" 吗？删除后不可恢复！`,
      positiveText: '确定',
      negativeText: '取消',
      onPositiveClick: async () => {
        const res = await deleteMenus({ menuId: row.menuId })
        if (res.code === 200) {
          message.success('删除成功')
          if (selectedMenu.value?.menuId === row.menuId) {
            selectedMenu.value = null
            selectedKeys.value = []
          }
          loadTableData()
        } else {
          message.error(res.message || '删除失败')
        }
      }
    })
  } catch (error) {
    console.error('删除失败:', error)
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

      // 更新选中的菜单
      if (formDialog.mode === 'edit' && selectedMenu.value) {
        if (selectedMenu.value.menuId === formData.menuId) {
          setTimeout(() => {
            const menu = findMenuById(formData.menuId?.toString() || '')
            if (menu) selectedMenu.value = menu
          }, 500)
        }
      }
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
  // 移除默认调用展开节点函数
  // setTimeout(() => {
  //   expandAllNodes()
  // }, 500)
})
</script>

<style lang="scss" scoped>
.sys-menu-page {
  display: flex;
  flex-direction: column;
  gap: 16px;

  .page-header {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    gap: 16px;
    padding: 24px;
    border-radius: 16px;
    background: linear-gradient(135deg, #f5f7ff, #ffffff);
    border: 1px solid rgba(82, 106, 255, 0.12);

    .header-subtitle {
      font-size: 13px;
      color: var(--text-color-3);
      margin-bottom: 4px;
    }

    h2 {
      margin: 0;
      font-size: 24px;
    }

    .header-desc {
      margin-top: 4px;
      color: var(--text-color-2);
    }

    .header-actions {
      display: flex;
      align-items: center;
      gap: 12px;
    }
  }

  .summary-cards {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
    gap: 12px;

    .summary-card {
      padding: 16px;
      border-radius: 12px;
      background: var(--card-color);
      border: 1px solid var(--divider-color);
      display: flex;
      flex-direction: column;
      gap: 6px;

      span {
        font-size: 12px;
        color: var(--text-color-3);
      }

      strong {
        font-size: 20px;
      }
    }
  }

  .menu-layout {
    display: grid;
    grid-template-columns: 320px 1fr;
    gap: 16px;
    align-items: stretch;
  }

  .menu-tree-panel,
  .menu-detail-panel {
    width: 100%;
  }

  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .header-label {
      font-size: 12px;
      color: var(--text-color-3);
      margin-bottom: 4px;
    }

    h3 {
      margin: 0;
      font-size: 18px;
    }
  }

  .tree-actions {
    margin-bottom: 12px;
  }

  .tree-node-label {
    display: flex;
    align-items: center;
    padding: 4px 0;

    .tree-node-icon {
      margin-right: 6px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: var(--text-color-3);
    }

    .type-tag {
      margin-left: 8px;
      padding: 0 8px;
      font-size: 12px;
    }
  }

  .detail-card {
    height: 100%;

    .detail-header {
      display: flex;
      align-items: flex-start;
      justify-content: space-between;
      gap: 12px;

      .header-label {
        font-size: 12px;
        color: var(--text-color-3);
        margin-bottom: 4px;
      }

      h3 {
        margin: 0;
        font-size: 20px;
      }

      .detail-path {
        margin-top: 6px;
        color: var(--text-color-3);
        font-size: 13px;
      }
    }
  }

  .info-descriptions {
    margin-top: 12px;

    :deep(.n-descriptions-table-header) {
      background-color: var(--card-color);
    }
  }
}

@media (max-width: 1200px) {
  .sys-menu-page {
    .menu-layout {
      grid-template-columns: 1fr;
    }

    .header-actions {
      width: 100%;
      flex-direction: column;
      align-items: flex-start;
    }
  }
}
</style>
