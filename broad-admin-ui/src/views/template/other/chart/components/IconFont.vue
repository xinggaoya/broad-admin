<template>
  <div class="iconfont-container">
    <n-input
      v-model:value="searchText"
      placeholder="搜索图标"
      class="search-input"
      clearable
      @input="handleSearch"
    >
      <template #prefix>
        <n-icon><SearchOutline /></n-icon>
      </template>
    </n-input>

    <n-grid
      cols="xs:2 s:3 m:4 l:6 xl:8"
      :x-gap="12"
      :y-gap="12"
      class="icon-grid"
      responsive="screen"
    >
      <n-grid-item v-for="item of displayIcons" :key="item.icon_id">
        <div class="icon-wrapper" @click="onCopy(item)">
          <SvgIcon prefix="iconfont" :name="item.font_class" class="icon" />
          <div class="icon-name">{{ item.name || item.font_class }}</div>
          <n-tooltip placement="bottom">
            <template #trigger>
              <div class="copy-btn">
                <n-icon size="16"><CopyOutline /></n-icon>
                复制
              </div>
            </template>
            点击复制图标名称
          </n-tooltip>
        </div>
      </n-grid-item>
    </n-grid>

    <div v-if="hasMore" class="load-more">
      <n-button type="primary" text @click="onLoadMore" :loading="loading" class="load-more-btn">
        加载更多
      </n-button>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed } from 'vue'
import Iconfonts from '@/icons/iconfont/iconfont.json'
import { useMessage } from 'naive-ui'
import { SearchOutline, CopyOutline } from '@vicons/ionicons5'
import { useDebounceFn } from '@vueuse/core'

interface IconItem {
  font_class: string
  icon_id: string
  name: string
  unicode: string
  unicode_decimal: number
}

const message = useMessage()
const searchText = ref('')
const loading = ref(false)
const pageSize = 50
const currentPage = ref(1)

// 所有图标列表
const iconList = Iconfonts.glyphs as IconItem[]

// 使用计算属性过滤图标
const filteredIcons = computed(() => {
  const search = searchText.value.toLowerCase()
  return search
    ? iconList.filter(
        (icon) =>
          icon.name?.toLowerCase().includes(search) ||
          icon.font_class.toLowerCase().includes(search)
      )
    : iconList
})

// 当前显示的图标
const displayIcons = reactive([] as IconItem[])

// 是否还有更多
const hasMore = computed(() => {
  return displayIcons.length < filteredIcons.value.length
})

// 防抖处理搜索
const handleSearch = useDebounceFn(() => {
  displayIcons.length = 0
  currentPage.value = 1
  loadIcons()
}, 300)

// 加载图标
function loadIcons() {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  const newIcons = filteredIcons.value.slice(start, end)
  displayIcons.push(...newIcons)
}

// 初始加载
loadIcons()

// 复制图标信息
async function onCopy(item: IconItem) {
  try {
    const copyText = `图标名称：${item.name || item.font_class}\n图标类名：${item.font_class}`
    await navigator.clipboard.writeText(copyText)
    message.success('复制成功')
  } catch (err) {
    message.error('复制失败，请手动复制')
    console.error('Copy failed:', err)
  }
}

// 加载更多
async function onLoadMore() {
  if (loading.value) return
  loading.value = true
  try {
    currentPage.value++
    await loadIcons()
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.iconfont-container {
  padding: 16px;

  .search-input {
    margin-bottom: 16px;
    max-width: 300px;
  }

  .icon-grid {
    .icon-wrapper {
      position: relative;
      height: 120px;
      padding: 16px;
      border-radius: 8px;
      background-color: var(--card-color);
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      gap: 8px;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 1px solid var(--border-color);

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        border-color: var(--primary-color);

        .copy-btn {
          opacity: 1;
          transform: translateY(0);
        }

        .icon {
          transform: scale(1.1);
          color: var(--primary-color);
        }
      }

      .icon {
        font-size: 32px;
        transition: all 0.3s ease;
        margin-bottom: 4px;
      }

      .icon-name {
        font-size: 13px;
        color: var(--text-color-2);
        text-align: center;
        line-height: 1.4;
        max-height: 36px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        width: 100%;
        padding: 0 4px;
      }

      .copy-btn {
        position: absolute;
        left: 0;
        right: 0;
        bottom: 0;
        height: 28px;
        background-color: var(--primary-color);
        color: #fff;
        font-size: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 4px;
        opacity: 0;
        transform: translateY(100%);
        transition: all 0.3s ease;
        border-bottom-left-radius: 8px;
        border-bottom-right-radius: 8px;
      }
    }
  }

  .load-more {
    text-align: center;
    margin-top: 24px;

    .load-more-btn {
      min-width: 120px;
    }
  }
}
</style>
