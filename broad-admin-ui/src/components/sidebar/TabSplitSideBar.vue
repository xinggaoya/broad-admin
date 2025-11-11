<template>
  <n-card
    :bordered="false"
    class="vaw-tab-split-side-bar-wrapper"
    :content-style="{ padding: 0, height: '100%' }"
    :class="[
      !appConfig.isCollapse ? 'open-status' : 'close-status',
      appConfig.sideTheme === 'image' ? 'sidebar-bg-img' : ''
    ]"
  >
    <div class="tab-split-tab-wrapper" :style="{ backgroundColor: bgColor }">
      <LogoView class="tab-split-logo-wrapper" :show-title="false" />
      <div style="height: calc(100% - 48px)">
        <n-scrollbar>
          <div
            id="tabSplitContentWrapper"
            class="tab-split-content-wrapper"
            :style="contentWrapperStyle"
          >
            <div
              v-for="item of tabs"
              :key="item.fullPath"
              class="label-item-wrapper"
              :class="{ 'vaw-tab-split-item-is-active': item.checked.value }"
              @click="changeTab(item)"
            >
              <SvgIcon :prefix="item.iconPrefix?.toString()" :name="item.icon" />
              <span>{{ item.label }}</span>
            </div>
          </div>
        </n-scrollbar>
      </div>
    </div>
    <div class="tab-split-menu-wrapper">
      <LogoView class="tab-split-logo-wrapper" :show-logo="false" />
      <ScrollerMenu :routes="routes" />
    </div>
    <div class="mobile-shadow"></div>
  </n-card>
</template>

<script lang="ts" setup>
import { computed, onMounted, ref, shallowReactive, watch } from 'vue'
import {
  type RouteLocationNormalizedLoaded,
  type RouteRecordRaw,
  useRoute,
  useRouter
} from 'vue-router'
import { isExternal } from '@/utils'
import { useAppConfigStore } from '@/store/modules/app-config'
import { SideTheme, type SplitTab, ThemeMode } from '@/store/types'
import { usePermissionStore } from '@/store/modules/permission'
import { transformSplitTabMenu } from '@/store/help'
import LogoView from '../logo/LogoView.vue'
import ScrollerMenu from './components/ScrollerMenu.vue'
import SvgIcon from '@/components/svg-icon/SvgIcon.vue'

defineProps({
  showLogo: {
    type: Boolean,
    default: true
  }
})

const appConfig = useAppConfigStore()
const permissionStore = usePermissionStore()
const tabs = shallowReactive<SplitTab[]>([])
const routes = shallowReactive<RouteRecordRaw[]>([])
const route = useRoute()
const router = useRouter()
watch(
  () => route.fullPath,
  () => {
    doChangeTab(route)
  }
)
onMounted(() => {
  tabs.length = 0
  tabs.push(...transformSplitTabMenu(permissionStore.getPermissionSplitTabs))
  doChangeTab(route)
})

function doChangeTab(route: RouteLocationNormalizedLoaded) {
  const matchedRoutes = route.matched
  if (matchedRoutes && matchedRoutes.length > 0) {
    tabs.forEach((it) => {
      if (it.fullPath === matchedRoutes[0].path) {
        it.checked.value = true
        if (it.children) {
          routes.length = 0
          routes.push(...(it.children as Array<RouteRecordRaw>))
        }
      } else {
        it.checked.value = false
      }
    })
  }
}

function changeTab(item: SplitTab) {
  tabs.forEach((it) => {
    it.checked.value = it.fullPath === item.fullPath
  })
  findPath(item)
}

function findPath(item: SplitTab) {
  if (item.children && item.children.length > 0) {
    const firstItem = item.children[0]
    if (firstItem.children && firstItem.children.length > 0) {
      findPath({
        label: firstItem.meta?.title,
        iconPrefix: firstItem.meta?.iconPrefix,
        icon: firstItem.meta?.icon,
        fullPath: firstItem.path,
        children: firstItem.children,
        checked: ref(false)
      } as SplitTab)
    } else {
      if (isExternal(firstItem.path as string)) {
        routes.length = 0
        routes.push(...item.children)
        window.open(firstItem.path)
      } else {
        router.push(firstItem.path || '/').then((error) => {
          if (error) {
            if (firstItem.path === route.path || firstItem.path === route.fullPath) {
              routes.length = 0
              item.children && routes.push(...item.children)
            }
          }
        })
      }
    }
  }
}

const contentWrapperStyle = computed(() => {
  return `--select-text-color: ${
    appConfig.theme === 'light' || appConfig.sideTheme === 'white' ? '#fff' : 'var(--text-color)'
  }`
})
const bgColor = computed(() => {
  if (appConfig.theme === ThemeMode.DARK) {
    return '#000000'
  }
  if (appConfig.sideTheme === SideTheme.DARK) return '#000000'
  if (appConfig.sideTheme === SideTheme.WHITE) return '#f5f5f5'
  if (appConfig.sideTheme === SideTheme.IMAGE) return 'rgba(255,255,255, 0.1)'
  return '#ffffff'
})
</script>

<style scoped lang="scss">
.sidebar-bg-img {
  background-image: url('../../assets/bg_img.webp') !important;
  background-size: cover;
}

.open-status {
  box-shadow: var(--shell-shadow);
  transition: box-shadow $transitionTime;
}

.close-status {
  box-shadow: none;
  transition: box-shadow $transitionTime;
}

.vaw-tab-split-side-bar-wrapper {
  height: 100%;
  box-sizing: border-box;
  overflow: hidden;
  border-radius: var(--shell-radius-lg);
  background-color: var(--shell-surface);
  display: flex;

  :deep(.n-card__content) {
    height: 100%;
    display: flex;
  }
}

.tab-split-tab-wrapper {
  width: $tabSplitMenuWidth;
  min-width: $tabSplitMenuWidth;
  max-width: $tabSplitMenuWidth;
  height: 100%;
  padding: 16px 10px;
  box-sizing: border-box;
}

.tab-split-content-wrapper {
  padding-top: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  color: var(--shell-text-color);

  .label-item-wrapper {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 6px;
    font-size: 12px;
    color: rgba(255, 255, 255, 0.7);
    cursor: pointer;
    border-radius: 12px;
    padding: 12px 4px;
    transition: all 0.2s ease;
  }

  .label-item-wrapper span {
    transform: scale(0.88);
  }

  .vaw-tab-split-item-is-active {
    background-color: rgba(255, 255, 255, 0.18);
    color: #fff;
  }
}

.tab-split-menu-wrapper {
  flex: 1;
  height: 100%;
  overflow: hidden;
}

.tab-split-menu-wrapper .tab-split-logo-wrapper {
  padding: 12px;
}

.is-mobile {
  .vaw-tab-split-side-bar-wrapper {
    max-width: min(var(--shell-sidebar-width), 85vw);
  }
}
</style>
