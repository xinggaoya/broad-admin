<template>
  <n-collapse display-directive="show" arrow-placement="right" :trigger-areas="triggerAreas">
    <n-card style="margin-bottom: 5px" :content-style="{}" size="small">
      <n-collapse-item title="筛选查询" :bordered="false">
        <template v-slot:default>
          <slot name="default" />
        </template>
        <template v-slot:header-extra>
          <n-space justify="end">
            <n-button :round="true" type="success" @click="handleSearch" size="small">
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
          </n-space>
        </template>
      </n-collapse-item>
    </n-card>
  </n-collapse>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { Search, ExitOutline } from '@vicons/ionicons5'

const triggerAreas = ref(['main', 'arrow'])
const emit = defineEmits(['search', 'reset'])

function handleSearch() {
  emit('search')
}

function handleReset() {
  emit('reset')
}
</script>

<style scoped lang="scss"></style>
