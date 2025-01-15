<template>
  <n-card size="small" class="search-card">
    <template #header>
      <n-space align="center" justify="space-between">
        <div class="title">
          <slot name="title">搜索</slot>
        </div>
        <n-space>
          <n-button ghost type="primary" @click="handleSearch" size="small" :loading="loading">
            <template #icon>
              <n-icon>
                <SearchIcon />
              </n-icon>
            </template>
            查询
          </n-button>
          <n-button type="tertiary" ghost @click="handleReset" size="small">
            <template #icon>
              <n-icon>
                <RefreshIcon />
              </n-icon>
            </template>
            重置
          </n-button>
          <n-button type="tertiary" ghost @click="handleCollapse" size="small">
            <template #icon>
              <n-icon>
                <ArrowUpIcon v-if="collapse" />
                <ArrowDownIcon v-else />
              </n-icon>
            </template>
            {{ !collapse ? '展开' : '收起' }}
          </n-button>
        </n-space>
      </n-space>
    </template>
    <template #default>
      <n-collapse-transition :show="collapse">
        <div class="search-form">
          <slot name="default" />
        </div>
      </n-collapse-transition>
    </template>
  </n-card>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import {
  Search as SearchIcon,
  RefreshOutline as RefreshIcon,
  ChevronUp as ArrowUpIcon,
  ChevronDown as ArrowDownIcon
} from '@vicons/ionicons5'

interface Props {
  loading?: boolean
  defaultCollapse?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  defaultCollapse: true
})

const emit = defineEmits<{
  (e: 'search'): void
  (e: 'reset'): void
  (e: 'update:collapse', value: boolean): void
}>()

const collapse = ref(props.defaultCollapse)

function handleCollapse() {
  collapse.value = !collapse.value
  emit('update:collapse', collapse.value)
}

function handleSearch() {
  emit('search')
}

function handleReset() {
  emit('reset')
}
</script>

<style lang="scss" scoped>
.search-card {
  margin-bottom: 16px;

  :deep(.n-card__content) {
    padding: 0;
  }

  .title {
    font-size: 14px;
    font-weight: 500;
    color: var(--text-color);
  }

  .search-form {
    padding: 16px;
    background-color: var(--card-color);
    border-radius: 0 0 3px 3px;
  }
}
</style>
