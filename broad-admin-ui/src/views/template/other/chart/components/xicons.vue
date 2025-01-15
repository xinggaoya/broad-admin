<template>
  <div class="xicons-container">
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
      <n-grid-item v-for="item of displayIcons" :key="item">
        <div class="icon-wrapper" @click="onCopy(item)">
          <n-icon size="30">
            <component :is="AllIcons[item]" />
          </n-icon>
          <div class="icon-name">{{ item }}</div>
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
import * as AllIcons from '@vicons/ionicons5'
import { useMessage } from 'naive-ui'
import { SearchOutline, CopyOutline } from '@vicons/ionicons5'
import { useDebounceFn } from '@vueuse/core'

const message = useMessage()
const iconArray = Object.keys(AllIcons)
const searchText = ref('')
const loading = ref(false)
const pageSize = 50
const currentPage = ref(1)

// 使用计算属性过滤图标
const filteredIcons = computed(() => {
  const search = searchText.value.toLowerCase()
  return search ? iconArray.filter((name) => name.toLowerCase().includes(search)) : iconArray
})

// 当前显示的图标
const displayIcons = reactive([] as string[])

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

// 复制图标名称
async function onCopy(item: string) {
  try {
    await navigator.clipboard.writeText(item)
    message.success('复制成功: ' + item)
  } catch (err) {
    message.error('复制失败，请手动复制')
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
.xicons-container {
  padding: 16px;

  .search-input {
    margin-bottom: 16px;
    max-width: 300px;
  }

  .icon-grid {
    .icon-wrapper {
      position: relative;
      height: 100px;
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
      }

      .icon-name {
        font-size: 12px;
        color: var(--text-color-2);
        text-align: center;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        width: 100%;
      }

      .copy-btn {
        position: absolute;
        left: 0;
        right: 0;
        bottom: 0;
        height: 24px;
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
