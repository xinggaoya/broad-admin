<template>
  <div class="scrollbar">
    <n-scrollbar>
      <n-menu
        :accordion="appConfig.isAccordion"
        :collapsed="appConfig.isCollapse"
        :collapsed-icon-size="22"
        :collapsed-width="63"
        :default-value="defaultPath"
        :expanded-keys="defaultExpandKeys"
        :indent="15"
        :options="menuOptions"
        :value="defaultPath"
        mode="vertical"
        @update:value="onMenuClick"
        @update:expanded-keys="onMenuExpandedKeysClick"
      />
    </n-scrollbar>
  </div>
</template>

<script lang="ts" setup>
import { useAppConfigStore } from '@/store/modules/app-config'
import { DeviceType } from '@/store/types'
import type { MenuOption } from 'naive-ui'
import { type PropType, ref, shallowReactive, watch, watchEffect } from 'vue'
import { type RouteRecordNormalized, type RouteRecordRaw, useRoute, useRouter } from 'vue-router'
import { isExternal } from '@/utils'
import { transfromMenu } from '@/store/help'

const props = defineProps({
  routes: {
    type: Array as PropType<RouteRecordRaw[]>,
    required: true
  }
})

const appConfig = useAppConfigStore()
const menuOptions = shallowReactive([] as Array<MenuOption>)
const defaultPath = ref('')
const defaultExpandKeys = ref<Array<string>>([])
const currentRoute = useRoute()
const router = useRouter()

defaultPath.value = currentRoute.fullPath
handleExpandPath()

function handleMenu(routes?: Array<RouteRecordRaw>) {
  menuOptions.length = 0
  const tempMenus = transfromMenu(routes || [])
  menuOptions.push(...tempMenus)
}

function handleExpandPath() {
  const keys = defaultPath.value.split('/')
  const results = keys
    .filter((it) => !!it)
    .reduce((pre, cur) => {
      const lastItem = pre[pre.length - 1]
      if (!lastItem) {
        pre.push('/' + cur)
      } else {
        pre.push(lastItem + '/' + cur)
      }
      return pre
    }, [] as string[])
  defaultExpandKeys.value = Array.from(new Set([...defaultExpandKeys.value, ...results]))
}

function onMenuClick(key: string) {
  if (isExternal(key)) return
  router.push(key)
  if (appConfig.deviceType === DeviceType.MOBILE) {
    appConfig.toggleCollapse(true)
  }
}

function onMenuExpandedKeysClick(keys: string[]) {
  defaultExpandKeys.value = keys
}

watch(
  () => currentRoute.fullPath,
  (newVal) => {
    defaultPath.value = newVal
    handleExpandPath()
  }
)
watchEffect(() => {
  handleMenu(props.routes)
})
</script>

<style lang="scss" scoped>
:deep(.n-menu .n-submenu .n-menu-item-content__icon) {
  font-size: 16px !important;
}

:deep(.n-menu .n-menu-item-content .n-menu-item-content__icon) {
  font-size: 16px !important;
}

:deep(.n-menu .n-menu-item) {
  margin-top: 0;
  margin-bottom: 5px;
}

:deep(.n-menu .n-menu-item::before) {
  left: 5px;
  right: 5px;
  border-radius: 5px;
}

:deep(.n-menu .n-menu-item:hover) {
  background-color: var(--item-color-active);
}

.scrollbar {
  height: calc(100vh - #{$logoHeight}) !important;
}
</style>
