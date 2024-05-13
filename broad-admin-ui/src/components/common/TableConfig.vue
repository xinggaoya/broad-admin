<template>
  <n-space align="center">
    <n-popover trigger="click" placement="bottom-end">
      <template #trigger>
        <n-button strong circle type="tertiary" size="small">
          <template #icon>
            <n-icon>
              <cash-icon />
            </n-icon>
          </template>
        </n-button>
      </template>
      <template #header>
        <div>表格设置</div>
      </template>
      <template #default>
        <n-space vertical>
          <div>
            <label class="label">边框</label>
            <n-switch :round="false" v-model:value="options.border" @update:value="onUpdateValue" />
          </div>
          <div>
            <label class="label">条纹</label>
            <n-switch
              :round="false"
              v-model:value="options.striped"
              @update:value="onUpdateValue"
            />
          </div>
        </n-space>
      </template>
    </n-popover>
  </n-space>
</template>

<script lang="ts" setup>
import { defineEmits, ref } from 'vue'
import { Menu as CashIcon } from '@vicons/ionicons5'
import { TableOperate } from '@/types/table'

const emits = defineEmits(['update'])

const options = ref<TableOperate>({
  border: false,
  striped: false
})

function onUpdateValue() {
  emits('update', options.value)
}
</script>

<style lang="scss" scoped>
.label {
  margin-right: 10px;
}
</style>
