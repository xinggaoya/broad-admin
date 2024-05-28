<template>
  <n-card style="margin-bottom: 5px" size="small">
    <template v-slot:header>
      <slot name="header" />
    </template>
    <template v-slot:default v-if="collapse">
      <slot name="default" />
    </template>
    <template v-slot:footer>
      <n-space justify="end">
        <n-button :round="true" type="primary" @click="handleSearch" size="small">
          <template #icon>
            <n-icon>
              <Search />
            </n-icon>
          </template>
          查询
        </n-button>
        <n-button type="tertiary" :round="true" @click="handleReset" size="small">
          <template #icon>
            <n-icon>
              <ExitOutline />
            </n-icon>
          </template>
          重置
        </n-button>
        <n-button type="tertiary" :round="true" @click="handleCollapse" size="small">
          <template #icon>
            <n-icon>
              <ArrowUpOutline v-if="collapse"/>
              <ArrowDownOutline v-else/>
            </n-icon>
          </template>
          {{ !collapse ? '展开' : '收起' }}
        </n-button>
      </n-space>
    </template>
  </n-card>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Search, ExitOutline ,ArrowUpOutline,ArrowDownOutline} from '@vicons/ionicons5'

const collapse = ref(true)
const emit = defineEmits(['search', 'reset'])

function handleCollapse() {
  collapse.value = !collapse.value
}

function handleSearch() {
  emit('search')
}

function handleReset() {
  emit('reset')
}
</script>

<style scoped lang="scss"></style>
