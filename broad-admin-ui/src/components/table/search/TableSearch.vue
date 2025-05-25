<template>
  <div class="table-search-wrapper">
    <n-card size="small" class="search-card">
      <template #header>
        <n-space align="center" justify="space-between">
          <div class="search-title">
            <slot name="title">搜索</slot>
          </div>
          <n-space>
            <SearchButton @search="handleSearch" :loading="loading" />
            <ResetButton @reset="handleReset" />
            <n-button type="tertiary" ghost @click="handleCollapse" size="small">
              <template #icon>
                <n-icon>
                  <ArrowUpIcon v-if="collapsed" />
                  <ArrowDownIcon v-else />
                </n-icon>
              </template>
              {{ collapsed ? '展开' : '收起' }}
            </n-button>
          </n-space>
        </n-space>
      </template>
      <template #default>
        <n-collapse-transition :show="!collapsed">
          <div class="search-form-container">
            <slot />
          </div>
        </n-collapse-transition>
      </template>
    </n-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { ChevronUp as ArrowUpIcon, ChevronDown as ArrowDownIcon } from '@vicons/ionicons5'
import SearchButton from '../button/SearchButton.vue'
import ResetButton from '../button/ResetButton.vue'

interface Props {
  loading?: boolean
  defaultCollapse?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  defaultCollapse: false
})

const emit = defineEmits<{
  search: []
  reset: []
}>()

const collapsed = ref(props.defaultCollapse)

const handleSearch = () => {
  emit('search')
}

const handleReset = () => {
  emit('reset')
}

const handleCollapse = () => {
  collapsed.value = !collapsed.value
}
</script>

<style scoped lang="scss">
.table-search-wrapper {
  margin-bottom: 16px;

  .search-card {
    :deep(.n-card__content) {
      padding: 0;
    }

    .search-title {
      font-size: 14px;
      font-weight: 500;
      color: var(--text-color);
    }

    .search-form-container {
      padding: 16px;
      background-color: var(--card-color);
      border-radius: 0 0 3px 3px;
    }
  }
}
</style>
