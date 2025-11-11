<template>
  <n-modal v-model:show="visible" preset="card" :style="{ width: '520px' }" title="快速搜索">
    <div class="search-header">
      <n-input
        ref="inputRef"
        v-model:value="keyword"
        placeholder="输入菜单或功能关键字"
        clearable
        @keydown.enter.prevent="handleSubmit"
      >
        <template #prefix>
          <n-icon>
            <SearchIcon />
          </n-icon>
        </template>
      </n-input>
    </div>
    <div class="search-body">
      <n-empty v-if="!filteredList.length" description="暂无匹配项">
        <template #extra>
          <small>尝试其他关键词或联系管理员同步菜单</small>
        </template>
      </n-empty>
      <n-list v-else clickable>
        <n-list-item v-for="item in filteredList" :key="item.path" @click="navigate(item.path)">
          <div class="result-item">
            <div>
              <p class="result-title">{{ item.title }}</p>
              <small>{{ item.path }}</small>
            </div>
            <n-tag round size="small" type="info">进入</n-tag>
          </div>
        </n-list-item>
      </n-list>
    </div>
  </n-modal>
</template>

<script lang="ts" setup>
import { ref, computed, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { useMessage } from 'naive-ui'
import { SearchOutline as SearchIcon } from '@vicons/ionicons5'

const visible = ref(false)
const keyword = ref('')
const inputRef = ref()
const message = useMessage()
const router = useRouter()

const presetRoutes = [
  { title: '部门管理', path: '/system/dept/list' },
  { title: '菜单管理', path: '/system/menu/list' },
  { title: '角色管理', path: '/system/role/list' },
  { title: '字典管理', path: '/system/dict/list' },
  { title: '项目概览', path: '/project/info' }
]

const filteredList = computed(() => {
  if (!keyword.value) return presetRoutes
  return presetRoutes.filter((item) => item.title.includes(keyword.value))
})

const onShow = () => {
  visible.value = true
  keyword.value = ''
  nextTick(() => {
    inputRef.value?.focus()
  })
}

const navigate = (path: string) => {
  visible.value = false
  router.push(path)
}

const handleSubmit = () => {
  if (!filteredList.value.length) {
    message.warning('暂无匹配菜单')
    return
  }
  navigate(filteredList.value[0].path)
}

defineExpose({
  onShow
})
</script>

<style lang="scss" scoped>
.search-header {
  margin-bottom: 16px;
}

.search-body {
  max-height: 360px;
  overflow: auto;
}

.result-item {
  display: flex;
  align-items: center;
  justify-content: space-between;

  p {
    margin: 0;
    font-weight: 500;
    color: var(--text-color);
  }

  small {
    color: var(--text-color-3);
  }
}
</style>
