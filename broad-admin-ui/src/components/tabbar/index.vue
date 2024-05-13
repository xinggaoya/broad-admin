<template>
  <div class="vaw-tab-bar-container">
    <n-flex align="center">
      <n-icon
        class="arrow-wrapper"
        :class="{ 'arrow-wrapper__disabled': leftArrowDisabled }"
        @click="leftArrowClick"
      >
        <ChevronBack />
      </n-icon>
      <n-scrollbar ref="scrollbarRef" x-scrollable :size="0" style="flex: 1">
        <n-button-group style="top: 3px">
          <n-button
            v-for="item in visitedRoutes"
            :key="item.path"
            :type="currentTab === item.path ? 'primary' : 'default'"
            class="tab-item"
            strong
            secondary
            :data="item.path"
            @click.self="itemClick(item)"
            @contextmenu="onContextMenu(item, $event)"
          >
            <span
              style="font-size: 12px; margin-top: 2px"
              class="text-item"
              @click.self="itemClick(item)"
            >
              {{ item.meta ? item.meta.title : item.name }}
            </span>
            <n-icon v-if="!item.meta?.affix" class="icon-item" @click="removeTab(item)">
              <Close />
            </n-icon>
          </n-button>
        </n-button-group>
      </n-scrollbar>
      <n-flex align="center">
        <n-icon
          class="arrow-wrapper"
          :class="{ 'arrow-wrapper__disabled': rightArrowDisabled }"
          style="transform: rotate(180deg)"
          @click="rightArrowClick"
        >
          <ChevronBack />
        </n-icon>
        <n-dropdown :options="contextMenuOptions" placement="left-start" @select="onDropDownSelect">
          <n-icon class="arrow-wrapper" @click="rightArrowClick">
            <Menu />
          </n-icon>
        </n-dropdown>
      </n-flex>
    </n-flex>
    <ul v-show="showContextMenu" class="contex-menu-wrapper" :style="contextMenuStyle">
      <li>
        <n-button :underline="false" text @click="refreshRoute">
          <template #icon>
            <n-icon>
              <Refresh />
            </n-icon>
          </template>
          刷新页面
        </n-button>
      </li>
      <li>
        <n-button :disabled="showLeftMenu" text @click="closeLeft">
          <template #icon>
            <n-icon>
              <ArrowBack />
            </n-icon>
          </template>
          关闭左侧
        </n-button>
      </li>
      <li>
        <n-button :disabled="showRightMenu" text @click="closeRight">
          <template #icon>
            <n-icon>
              <ArrowForward />
            </n-icon>
          </template>
          关闭右侧
        </n-button>
      </li>
      <li>
        <n-button icon="el-icon-close" text @click="closeAll">
          <template #icon>
            <n-icon>
              <Close />
            </n-icon>
          </template>
          关闭所有
        </n-button>
      </li>
    </ul>
  </div>
</template>

<script lang="ts" setup>
import { computed, getCurrentInstance, h, ref, watch } from 'vue'
import { NIcon, NScrollbar } from 'naive-ui'
import { Close, ChevronBack, Refresh, ArrowBack, ArrowForward, Menu } from '@vicons/ionicons5'
import useVisitedRouteStore from '@/store/modules/visited-routes'
import type { RouteLocationRaw, RouteRecordRaw } from 'vue-router'
import { useRoute, useRouter } from 'vue-router'

let { proxy } = getCurrentInstance()

defineOptions({
  name: 'TabBar'
})
const router = useRouter()
const route = useRoute()
const currentTab = ref(route.fullPath)
const contextMenuStyle = ref({ left: '0', top: '0' })
const showContextMenu = ref(false)
const scrollbarRef = ref()
const selectRoute = ref<RouteRecordRaw>()
const showLeftMenu = ref(true)
const showRightMenu = ref(true)
const rightArrowDisabled = ref(false)
const leftArrowDisabled = ref(false)

const contextMenuOptions = ref([
  {
    label: '刷新页面',
    key: 'refresh',
    icon() {
      return h(NIcon, null, {
        default: () => h(Refresh)
      })
    }
  },
  {
    label: '关闭所有',
    key: 'close',
    icon() {
      return h(NIcon, null, {
        default: () => h(Close)
      })
    }
  }
])

const visitedRouteStore = useVisitedRouteStore()

const visitedRoutes = computed(() => {
  return visitedRouteStore.getVisitedRoutes
})

watch(route, (val) => {
  currentTab.value = val.fullPath || ''
  setTimeout(() => {
    const scrollbar = scrollbarRef.value as InstanceType<typeof NScrollbar>
    const el = document.querySelector(`[data="${currentTab.value}"]`) as HTMLElement
    el &&
      scrollbar.scrollTo(
        {
          left: el.offsetLeft,
          debounce: true,
          behavior: 'smooth'
        } as any,
        0
      )
  }, 0)
})

watch(showContextMenu, (val) => {
  if (val) {
    document.body.addEventListener('click', closeMenu)
  } else {
    document.body.removeEventListener('click', closeMenu)
  }
})

function itemClick(item: { path: any }) {
  handleTabClick(item.path || item.path || '/')
}

function handleTabClick(path: RouteLocationRaw) {
  router.push(path)
}

function isAffix(route: { meta: { affix: any } }) {
  return route.meta && route.meta.affix
}

function onContextMenu(item: any, e: { preventDefault?: any; clientX?: any }) {
  const { clientX } = e
  const { x } = proxy.$el.getBoundingClientRect()
  e.preventDefault()
  selectRoute.value = item
  if (selectRoute.value) {
    showLeftMenu.value = isLeftLast(item.path || '/')
    showRightMenu.value = isRightLast(item.path || '/')
    const screenWidth = document.body.clientWidth
    contextMenuStyle.value.left =
      (clientX + 130 > screenWidth ? clientX - 130 - x - 15 : clientX - x + 15) + 'px'
    contextMenuStyle.value.top = '25px'
    showContextMenu.value = true
  }
}

function removeTab(item: RouteRecordRaw) {
  visitedRouteStore.removeVisitedRoute(item).then((lastPath) => {
    router.push(lastPath)
  })
}

function isLeftLast(tempRoute: string) {
  return visitedRouteStore.getVisitedRoutes.findIndex((it) => it.path === tempRoute) === 0
}

function isRightLast(tempRoute: string) {
  return (
    visitedRouteStore.getVisitedRoutes.findIndex((it) => it.path === tempRoute) ===
    visitedRouteStore.getVisitedRoutes.length - 1
  )
}

function onDropDownSelect(key: any) {
  switch (key) {
    case 'refresh':
      refreshRoute()
      break
    case 'close':
      closeAll()
      break
  }
}

function refreshRoute() {
  router.replace({ path: '/redirect' + route.path })
}

function closeLeft() {
  if (!selectRoute.value) return
  visitedRouteStore.closeLeftVisitedView(selectRoute.value).then(() => {
    if (route.fullPath !== selectRoute.value.path) {
      router.push(visitedRouteStore.findLastRoutePath())
    }
  })
}

function closeRight() {
  if (!selectRoute.value) return
  visitedRouteStore.closeRightVisitedView(selectRoute.value).then(() => {
    if (route.path !== selectRoute.value.path) {
      router.push(visitedRouteStore.findLastRoutePath())
    }
  })
}

function closeAll() {
  visitedRouteStore.closeAllVisitedView().then(() => {
    router.push(visitedRouteStore.findLastRoutePath())
  })
}

function closeMenu() {
  showContextMenu.value = false
}

function leftArrowClick() {
  const scrollbar = scrollbarRef.value
  const scrollX = scrollbar.$el?.scrollLeft || 0
  scrollbar.scrollTo(
    {
      left: Math.max(0, scrollX - 200),
      debounce: true,
      behavior: 'smooth'
    },
    0
  )
  isDisabledArrow()
}

function rightArrowClick() {
  const scrollbar = scrollbarRef.value
  const scrollX = scrollbar.$el?.scrollLeft || 0
  scrollbar.scrollTo(
    {
      left: scrollX + 200,
      debounce: false,
      behavior: 'smooth'
    },
    0
  )
  isDisabledArrow()
}

function isDisabledArrow() {
  setTimeout(() => {
    const scrollbar = scrollbarRef.value
    const { scrollLeft, scrollWidth, clientWidth } = scrollbar.$el
    leftArrowDisabled.value = scrollLeft === 0
    rightArrowDisabled.value = scrollLeft === scrollWidth - clientWidth
  }, 100)
}
</script>

<style lang="scss" scoped>
.vaw-tab-bar-container {
  position: relative;
  height: $tabHeight;
  line-height: calc(#{$tabHeight} - 3px);
  box-sizing: border-box;
  white-space: nowrap;
  box-shadow: 10px 5px 10px rgb(0 0 0 / 10%);

  .contex-menu-wrapper {
    position: absolute;
    width: 130px;
    z-index: 999;
    list-style: none;
    box-shadow:
      0 2px 4px rgba(0, 0, 0, 0.12),
      0 0 6px rgba(0, 0, 0, 0.04);
    background-color: var(--base-color);
    padding-left: 10px;

    & > li {
      width: 100%;
      box-sizing: border-box;
      display: flex;
      align-items: center;
      padding: 5px 0;
    }

    & > li:hover {
      color: var(--primary-color);
    }
  }

  .humburger-wrapper {
    position: absolute;
    top: 0;
    left: 0;
    overflow: hidden;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
  }

  .tab-humburger-wrapper {
    margin-left: 40px;
    transition: margin-left $transitionTime;
  }

  .tab-no-humburger-wrapper {
    margin-left: 0;
    transition: margin-left $transitionTime;
  }

  .tab-item {
    padding: 7px 10px;
    cursor: pointer;

    .icon-item {
      margin-left: 0;
      width: 0;
      height: 0;
      transition: all 0.2s ease-in-out;
      overflow: hidden;
    }

    &:hover {
      .icon-item {
        display: inline;
        width: 14px;
        height: 14px;
        margin-left: 5px;
        font-size: 12px;
        background-color: rgba(0, 0, 0, 0.12);
        border-radius: 50%;
        padding: 1px;
        transition: all 0.2s ease-in-out;
      }
    }
  }

  .tab-item + .tab-item {
    margin-left: 10px;
  }

  .arrow-wrapper {
    cursor: pointer;
    font-size: 20px;
    margin: 0 8px;
  }

  .arrow-wrapper_line {
    line-height: 40px;
  }

  .arrow-wrapper__disabled {
    cursor: not-allowed;
    color: #b9b9b9;
  }
}
</style>
