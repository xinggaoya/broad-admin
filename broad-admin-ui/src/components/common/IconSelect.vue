<template>
  <n-popover trigger="click" placement="bottom" width="400">
    <template #trigger>
      <n-button>
        <template #icon v-if="selectItem.font_class">
          <span
            class="iconfont"
            :class="['icon-' + selectItem.font_class]"
            style="font-size: 20px"
          ></span
        ></template>
        {{ selectItem.name }}
      </n-button>
    </template>
    <template v-slot:header>
      <div>
        <n-input v-model:value="search" placeholder="搜索图标" @input="onSearch" />
      </div>
    </template>
    <template v-slot:default>
      <n-scrollbar ref="scrollbarRef" class="grid-wrapper">
        <n-grid :cols="4" style="height: 300px">
          <n-grid-item v-for="item of icons" :key="item.font_class">
            <n-space class="icon-wrapper" align="center" @click="onIconClick(item)" vertical>
              <div>
                <span
                  class="iconfont"
                  :class="['icon-' + item.font_class]"
                  style="font-size: 20px"
                ></span>
              </div>
              <n-ellipsis :line-clamp="1" style="font-size: 12px">{{ item.name }}</n-ellipsis>
            </n-space>
          </n-grid-item>
        </n-grid>
      </n-scrollbar>
    </template>
    <template v-slot:footer>
      <div class="flex justify-end mt-2 mb-2">
        <n-pagination
          :page="currentPage"
          :page-size="pageSize"
          :page-slot="5"
          :item-count="itemCount"
          @update-page="onUpdatePage"
        />
      </div>
    </template>
  </n-popover>
</template>

<script lang="ts" setup>
import { computed, ref, shallowReactive, toRef } from 'vue'
import Iconfonts from '@/icons/iconfont/iconfont.json'

const props = defineProps({
  value: {
    type: String,
    default: ''
  },
  onUpdateIcon: Function
})

const emit = defineEmits(['selected', 'update:value'])

const defaultIcon = toRef(props, 'value')
const search = ref('')
const pageSize = 40
const scrollbarRef = ref()
const icons = shallowReactive(Iconfonts.glyphs.slice(0, 40))
const currentPage = ref(1)
const { onUpdateIcon } = props

const itemCount = computed(() => Iconfonts.glyphs.length)
const selectItem = ref({
  font_class: defaultIcon.value || '',
  name: defaultIcon.value || '选择图标'
})

function onUpdatePage(page: number) {
  currentPage.value = page
  icons.length = 0
  const start = (currentPage.value - 1) * pageSize
  icons.push(...Iconfonts.glyphs.slice(start, start + pageSize))
  scrollbarRef.value.scrollTo(0, 0)
}

function onSearch() {
  if (search.value) {
    icons.length = 0
    const start = (currentPage.value - 1) * pageSize
    const end = start + pageSize
    const result = Iconfonts.glyphs.filter((item) => {
      return item.name.includes(search.value)
    })
    icons.push(...result.slice(start, end))
    scrollbarRef.value.scrollTo(0, 0)
  } else {
    icons.length = 0
  }
}

function onIconClick(item: any) {
  selectItem.value = item
  if (onUpdateIcon) {
    onUpdateIcon(item)
  } else {
    emit('update:value', item.name)
  }
}
</script>
<style lang="scss" scoped>
.grid-wrapper {
  .icon-wrapper {
    cursor: pointer;
    border: 1px solid #f5f5f5;
  }
}
</style>
