<template>
  <n-popover :style="{ width: '200px' }" placement="left-start" trigger="click">
    <template #default>
      <div style="border-bottom: 1px solid #f5f5f5" class="flex items-center justify-between">
        <n-checkbox
          v-model:checked="allChecked"
          :indeterminate="isIndeterminate"
          @update:checked="onAllChange"
        >
          全选
        </n-checkbox>
      </div>
      <draggable :list="innerTableProps" animation="500" item-key="key" @end="onUpdateValue">
        <template v-slot:item="{ element }">
          <n-space size="large" justify="space-between">
            <n-checkbox
              v-model:checked="element.checked"
              :label="element.prop"
              @update:checked="onChange"
            >
              {{ element.title }}
            </n-checkbox>
            <n-icon>
              <MenuIcon />
            </n-icon>
          </n-space>
        </template>
      </draggable>
    </template>
    <template #trigger>
      <n-button type="tertiary" size="small" circle>
        <template #icon>
          <n-icon>
            <SettingsIcon />
          </n-icon>
        </template>
      </n-button>
    </template>
  </n-popover>
</template>

<script lang="ts" setup>
import type { TablePropsType } from '@/types/components'
import { reactive, ref, toRef } from 'vue'
import draggable from 'vuedraggable'
import { SettingsOutline as SettingsIcon } from '@vicons/ionicons5'
import { Menu as MenuIcon } from '@vicons/ionicons5'
import { sortColumns } from '@/utils'
import type { DataTableColumn } from 'naive-ui'

const props = defineProps({
  columns: {
    type: Array<DataTableColumn>,
    required: true,
    default: () => []
  }
})

const tempTableProps = toRef(props, 'columns')
const tempArray =
  tempTableProps.value
    ?.filter((it) => !!it.key)
    .map((it) => {
      return {
        ...it,
        checked: ref(true)
      } as TablePropsType
    }) || []
const innerTableProps = reactive(tempArray)
const isIndeterminate = ref(
  innerTableProps.filter((it) => it.checked).length !== innerTableProps.length
)
const allChecked = ref(innerTableProps.every((it) => it.checked))
const onAllChange = (value: boolean) => {
  innerTableProps.forEach((it) => (it.checked = value))
  sortColumns(props.columns, innerTableProps.filter((it) => it.checked) as any)
}
const onChange = () => {
  const checkedItems = innerTableProps.filter((it) => it.checked)
  allChecked.value = checkedItems.length === innerTableProps.length
  isIndeterminate.value = checkedItems.length > 0 && checkedItems.length !== innerTableProps.length
  sortColumns(props.columns, checkedItems as any)
}
const onReset = () => {
  innerTableProps.forEach((it) => (it.checked = true))
  onChange()
}

function onUpdateValue() {
  sortColumns(props.columns, innerTableProps as any)
}
</script>
