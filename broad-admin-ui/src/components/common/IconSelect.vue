<template>
  <n-popover
    trigger="click"
    placement="bottom"
    :width="400"
    :show="showPopover"
    @update:show="handlePopoverShow"
  >
    <template #trigger>
      <n-button :type="type" :size="size" class="icon-select-btn">
        <template #icon>
          <span v-if="selectedIcon" class="selected-icon">
            <template v-if="selectedType === 'ionicons'">
              <IconView :icon="selectedIcon" :size="20" />
            </template>
            <template v-else>
              <SvgIcon prefix="iconfont" :name="selectedIcon" style="font-size: 20px" />
            </template>
          </span>
          <n-icon v-else><SearchOutline /></n-icon>
        </template>
        {{ selectedIcon || placeholder }}
      </n-button>
    </template>

    <div class="icon-select-popover">
      <div class="search-wrapper">
        <n-input v-model:value="searchText" placeholder="搜索图标" clearable @input="handleSearch">
          <template #prefix>
            <n-icon><SearchOutline /></n-icon>
          </template>
        </n-input>
      </div>

      <n-tabs v-model:value="activeTab" class="icon-tabs">
        <n-tab-pane name="ionicons" tab="Ionicons">
          <n-scrollbar ref="scrollbarRef" class="icons-container">
            <n-grid :cols="4" :x-gap="8" :y-gap="8">
              <n-grid-item
                v-for="icon in displayIonicons"
                :key="icon"
                class="icon-item"
                @click="handleIconSelect(icon, 'ionicons')"
              >
                <n-tooltip placement="bottom">
                  <template #trigger>
                    <div
                      class="icon-wrapper"
                      :class="{ active: selectedIcon === icon && selectedType === 'ionicons' }"
                    >
                      <IconView :icon="icon" :size="24" />
                      <div class="icon-name">{{ icon }}</div>
                    </div>
                  </template>
                  {{ icon }}
                </n-tooltip>
              </n-grid-item>
            </n-grid>
          </n-scrollbar>
        </n-tab-pane>

        <n-tab-pane name="iconfont" tab="Iconfont">
          <n-scrollbar ref="scrollbarRef" class="icons-container">
            <n-grid :cols="4" :x-gap="8" :y-gap="8">
              <n-grid-item
                v-for="icon in displayIconfonts"
                :key="icon.icon_id"
                class="icon-item"
                @click="handleIconSelect(icon.font_class, 'iconfont')"
              >
                <n-tooltip placement="bottom">
                  <template #trigger>
                    <div
                      class="icon-wrapper"
                      :class="{
                        active: selectedIcon === icon.font_class && selectedType === 'iconfont'
                      }"
                    >
                      <SvgIcon prefix="iconfont" :name="icon.font_class" class="icon" />
                      <div class="icon-name">{{ icon.name || icon.font_class }}</div>
                    </div>
                  </template>
                  {{ icon.name || icon.font_class }}
                </n-tooltip>
              </n-grid-item>
            </n-grid>
          </n-scrollbar>
        </n-tab-pane>
      </n-tabs>

      <div class="pagination-wrapper">
        <n-pagination
          v-model:page="currentPage"
          :page-size="pageSize"
          :item-count="currentIconList.length"
          :page-slot="5"
          size="small"
          @update:page="handlePageChange"
        />
      </div>
    </div>
  </n-popover>
</template>

<script lang="ts" setup>
import { ref, computed, watch } from 'vue'
import * as AllIcons from '@vicons/ionicons5'
import { SearchOutline } from '@vicons/ionicons5'
import { useDebounceFn } from '@vueuse/core'
import IconView from '../Icon/IconView.vue'
import Iconfonts from '@/icons/iconfont/iconfont.json'

interface Props {
  modelValue?: string
  placeholder?: string
  type?: 'default' | 'primary' | 'info' | 'success' | 'warning' | 'error'
  size?: 'tiny' | 'small' | 'medium' | 'large'
  iconType?: 'ionicons' | 'iconfont'
}

interface IconFontItem {
  font_class: string
  icon_id: string
  name: string
  unicode: string
  unicode_decimal: number
}

const props = withDefaults(defineProps<Props>(), {
  modelValue: '',
  placeholder: '选择图标',
  type: 'default',
  size: 'medium',
  iconType: 'ionicons'
})

const emit = defineEmits<{
  (e: 'update:modelValue', value: string): void
  (e: 'update:iconType', value: 'ionicons' | 'iconfont'): void
  (e: 'change', value: string, type: 'ionicons' | 'iconfont'): void
}>()

const showPopover = ref(false)
const searchText = ref('')
const currentPage = ref(1)
const pageSize = 20
const scrollbarRef = ref()
const activeTab = ref<'ionicons' | 'iconfont'>(props.iconType)

// 选中的图标和类型
const selectedIcon = ref(props.modelValue)
const selectedType = ref(props.iconType)

// 监听父组件传入的值变化
watch(
  () => props.modelValue,
  (newVal) => {
    selectedIcon.value = newVal
  }
)

watch(
  () => props.iconType,
  (newVal) => {
    selectedType.value = newVal
    activeTab.value = newVal
  }
)

// 图标列表
const ioniconsArray = Object.keys(AllIcons)
const iconfontArray = (Iconfonts.glyphs || []) as IconFontItem[]

// 过滤后的图标列表
const filteredIonicons = computed(() => {
  const search = searchText.value.toLowerCase()
  return search
    ? ioniconsArray.filter((name) => name.toLowerCase().includes(search))
    : ioniconsArray
})

const filteredIconfonts = computed(() => {
  const search = searchText.value.toLowerCase()
  return search
    ? iconfontArray.filter(
        (icon) =>
          icon.name?.toLowerCase().includes(search) ||
          icon.font_class.toLowerCase().includes(search)
      )
    : iconfontArray
})

// 当前显示的图标
const displayIonicons = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return filteredIonicons.value.slice(start, end)
})

const displayIconfonts = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return filteredIconfonts.value.slice(start, end)
})

// 当前选中的图标列表
const currentIconList = computed(() => {
  return activeTab.value === 'ionicons' ? filteredIonicons.value : filteredIconfonts.value
})

// 防抖处理搜索
const handleSearch = useDebounceFn(() => {
  currentPage.value = 1
  scrollbarRef.value?.scrollTo({ top: 0 })
}, 300)

// 处理图标选择
function handleIconSelect(icon: string, type: 'ionicons' | 'iconfont') {
  selectedIcon.value = icon
  selectedType.value = type
  emit('update:modelValue', icon)
  emit('update:iconType', type)
  emit('change', icon, type)
  showPopover.value = false
}

// 处理分页变化
function handlePageChange() {
  scrollbarRef.value?.scrollTo({ top: 0 })
}

// 处理弹出层显示状态
function handlePopoverShow(show: boolean) {
  showPopover.value = show
  if (show) {
    searchText.value = ''
    currentPage.value = 1
    activeTab.value = selectedType.value
  }
}

// 监听标签页切换
watch(activeTab, () => {
  currentPage.value = 1
  scrollbarRef.value?.scrollTo({ top: 0 })
})
</script>

<style lang="scss" scoped>
.icon-select-btn {
  min-width: 120px;

  .selected-icon {
    display: inline-flex;
    align-items: center;
  }
}

.icon-select-popover {
  .search-wrapper {
    padding: 8px;
    border-bottom: 1px solid var(--border-color);
  }

  .icon-tabs {
    :deep(.n-tabs-nav) {
      padding: 0 8px;
    }
  }

  .icons-container {
    height: 300px;
    padding: 8px;

    .icon-item {
      .icon-wrapper {
        padding: 8px;
        border-radius: 4px;
        cursor: pointer;
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 4px;
        border: 1px solid transparent;
        transition: all 0.3s ease;

        &:hover {
          background-color: var(--hover-color);
          border-color: var(--border-color);
        }

        &.active {
          background-color: var(--primary-color-hover);
          border-color: var(--primary-color);
          color: var(--primary-color);
        }

        .icon {
          font-size: 24px;
          transition: all 0.3s ease;
        }

        .icon-name {
          font-size: 12px;
          color: var(--text-color-2);
          width: 100%;
          text-align: center;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
          line-height: 1.4;
        }
      }
    }
  }

  .pagination-wrapper {
    padding: 8px;
    border-top: 1px solid var(--border-color);
    display: flex;
    justify-content: flex-end;
  }
}
</style>
