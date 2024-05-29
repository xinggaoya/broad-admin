<template>
  <n-card
    :bordered="false"
    class="vaw-tab-split-side-bar-wrapper"
    :content-style="{ padding: 0 }"
    style="border-radius: 0px"
    :class="[
        !appConfig.isCollapse ? 'open-status' : 'close-status',
        appConfig.sideTheme === 'image' ? 'sidebar-bg-img' : '',
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
              <SvgIcon :prefix="item.iconPrefix" :name="item.icon" />
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
import { type RouteLocationNormalizedLoaded, type RouteRecordRaw, useRoute, useRouter } from 'vue-router'
import { isExternal } from '@/utils'
import useAppConfigStore from '@/store/modules/app-config'
import { SideTheme, type SplitTab, ThemeMode } from '@/store/types'
import usePermissionStore from '@/store/modules/permission'
import { transformSplitTabMenu } from '@/store/help'

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
    appConfig.theme === 'light' || appConfig.sideTheme === 'white'
      ? '#fff'
      : 'var(--text-color)'
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
  width: $menuWidth;
  box-shadow: 2px 5px 10px rgba(0, 0, 0, 0.12);
  transition: all $transitionTime;
}

.close-status {
  width: $minMenuWidth;
  box-shadow: none;
  transition: all $transitionTime;
}

.vaw-tab-split-side-bar-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  overflow: hidden;
  height: 100vh;
  box-sizing: border-box;
  z-index: 999;

  .tab-split-tab-wrapper {
    position: relative;
    top: 0;
    left: 0;
    width: $tabSplitMenuWidth;
    min-width: $tabSplitMenuWidth;
    max-width: $tabSplitMenuWidth;
    overflow: hidden;
    height: 100vh;
    box-sizing: border-box;

    .tab-split-logo-wrapper {
      max-width: $tabSplitMenuWidth;
      min-width: $tabSplitMenuWidth;
    }

    .tab-split-content-wrapper {
      position: relative;
      height: 100%;
      @mixin after {
        content: '';
        position: absolute;
        left: 5px;
        top: 5px;
        right: 5px;
        bottom: 5px;
        border-radius: 3px;
        z-index: -1;
      }

      .label-item-wrapper {
        position: relative;
        min-height: $logoHeight * 1.2;
        padding: 10px 0;
        display: flex;
        flex-direction: column;
        overflow: hidden;
        align-items: center;
        justify-content: center;
        box-sizing: border-box;
        color: var(--text-color);
        z-index: 1;

        & > i {
          font-size: 16px;
        }

        & > span {
          font-size: 12px;
          line-height: 14px;
          margin-top: 5px;
        }

        &:hover {
          cursor: pointer;
        }

        &::after {
          @include after;
        }

        & svg {
          width: 26px;
          height: 26px;
        }
      }

      .label-item-wrapper:hover::after {
        background-color: var(--primary-color);
      }

      .vaw-tab-split-item-is-active {
        color: var(--select-text-color);
      }

      .vaw-tab-split-item-is-active::after {
        background-color: var(--primary-color);
        @include after;
      }
    }
  }

  .tab-split-menu-wrapper {
    position: absolute;
    top: 0;
    right: 0;
    bottom: 0;
    left: $tabSplitMenuWidth;
    overflow-x: hidden;
  }

  .vaw-menu-wrapper {
    overflow-x: hidden;
    color: white;
  }
}

.is-mobile {
  .open-status {
    width: $menuWidth;
    transform: translateX(0);
    transition: transform $transitionTime;
  }

  .close-status {
    width: $menuWidth;
    transform: translateX(-$menuWidth);
    transition: transform $transitionTime;
    box-shadow: none;
  }
}
</style>
