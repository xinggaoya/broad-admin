<template>
  <div class="tab-strip" role="tablist" aria-label="页面标签" data-testid="tab-strip">
    <button
      class="tab-strip__arrow"
      type="button"
      :disabled="!canScrollPrev"
      aria-label="向左滚动标签"
      @click="scrollBy(-160)"
    >
      <n-icon size="18">
        <ChevronBack />
      </n-icon>
    </button>

    <div
      ref="scrollAreaRef"
      class="tab-strip__viewport"
      @wheel.prevent="handleWheel"
      @scroll.passive="updateScrollAbility"
    >
      <div class="tab-strip__list">
        <button
          v-for="item in visitedRoutes"
          :key="item.path"
          class="tab-pill"
          :class="{
            'is-active': currentTab === item.path,
            'is-affix': item.meta?.affix
          }"
          type="button"
          role="tab"
          :aria-selected="(currentTab === item.path).toString()"
          :data-path="item.path"
          @click="itemClick(item)"
          @contextmenu.prevent="openContextMenu(item, $event)"
        >
          <span class="tab-pill__text">{{ item.meta?.title || item.name }}</span>
          <button
            v-if="!item.meta?.affix"
            class="tab-pill__close"
            type="button"
            aria-label="关闭标签"
            @click.stop="removeTab(item)"
          >
            <n-icon size="14">
              <Close />
            </n-icon>
          </button>
        </button>
      </div>
    </div>

    <button
      class="tab-strip__arrow"
      type="button"
      :disabled="!canScrollNext"
      aria-label="向右滚动标签"
      @click="scrollBy(160)"
    >
      <n-icon size="18">
        <ChevronForward />
      </n-icon>
    </button>

    <n-dropdown
      trigger="click"
      :options="dropdownOptions"
      placement="bottom-end"
      @select="handleDropdownSelect"
    >
      <button class="tab-strip__more" type="button" aria-label="更多标签操作">
        <n-icon size="18">
          <Menu />
        </n-icon>
      </button>
    </n-dropdown>

    <n-dropdown
      trigger="manual"
      :x="contextMenu.x"
      :y="contextMenu.y"
      :show="contextMenu.visible"
      placement="bottom-start"
      :options="contextMenuOptions"
      @select="handleContextSelect"
      @clickoutside="contextMenu.visible = false"
    />
  </div>
</template>

<script lang="ts" setup>
import { computed, nextTick, reactive, ref, watch, h } from 'vue'
import { NDropdown, NIcon } from 'naive-ui'
import {
  Close,
  ChevronBack,
  ChevronForward,
  Refresh,
  ArrowBack,
  ArrowForward,
  Menu
} from '@vicons/ionicons5'
import useVisitedRouteStore from '@/store/modules/visited-routes'
import type { RouteRecordRaw } from 'vue-router'
import { useRoute, useRouter } from 'vue-router'
import { useResizeObserver } from '@vueuse/core'

defineOptions({ name: 'TabBar' })

const router = useRouter()
const route = useRoute()
const visitedRouteStore = useVisitedRouteStore()

const visitedRoutes = computed(() => visitedRouteStore.getVisitedRoutes)
const currentTab = ref(route.fullPath)
const scrollAreaRef = ref<HTMLDivElement | null>(null)
const canScrollPrev = ref(false)
const canScrollNext = ref(false)

const dropdownOptions = [
  { label: '刷新页面', key: 'refresh', icon: hIcon(Refresh) },
  { label: '关闭所有', key: 'close-all', icon: hIcon(Close) }
]

const contextMenuOptions = [
  { label: '刷新', key: 'refresh' },
  { label: '关闭左侧', key: 'close-left', icon: hIcon(ArrowBack) },
  { label: '关闭右侧', key: 'close-right', icon: hIcon(ArrowForward) },
  { label: '关闭所有', key: 'close-all', icon: hIcon(Close) }
]

const contextMenu = reactive({
  visible: false,
  x: 0,
  y: 0,
  target: null as RouteRecordRaw | null
})

watch(
  () => route.fullPath,
  (val) => {
    currentTab.value = val
    nextTick(() => scrollActiveIntoView())
  }
)

useResizeObserver(scrollAreaRef, () => updateScrollAbility())

function hIcon(component: any) {
  return () =>
    h(
      NIcon,
      { size: 16 },
      {
        default: () => h(component)
      }
    )
}

function itemClick(item: RouteRecordRaw) {
  router.push(item.path || '/')
}

function removeTab(routeItem: RouteRecordRaw) {
  visitedRouteStore.removeVisitedRoute(routeItem).then((path) => {
    if (routeItem.path === route.fullPath) {
      router.push(path)
    }
  })
}

function scrollBy(distance: number) {
  const el = scrollAreaRef.value
  if (!el) return
  el.scrollBy({ left: distance, behavior: 'smooth' })
  requestAnimationFrame(updateScrollAbility)
}

function handleWheel(event: WheelEvent) {
  const el = scrollAreaRef.value
  if (!el) return
  el.scrollBy({ left: event.deltaY > 0 ? 80 : -80 })
  updateScrollAbility()
}

function scrollActiveIntoView() {
  const el = scrollAreaRef.value
  if (!el) return
  const selector = `.tab-pill[data-path="${escapeSelector(currentTab.value)}"]`
  const target = el.querySelector<HTMLButtonElement>(selector)
  if (target) {
    target.scrollIntoView({ behavior: 'smooth', inline: 'center', block: 'nearest' })
  }
}

function updateScrollAbility() {
  const el = scrollAreaRef.value
  if (!el) return
  const { scrollLeft, scrollWidth, clientWidth } = el
  canScrollPrev.value = scrollLeft > 2
  canScrollNext.value = scrollLeft + clientWidth < scrollWidth - 2
}

function openContextMenu(item: RouteRecordRaw, event: MouseEvent) {
  contextMenu.target = item
  contextMenu.x = event.clientX
  contextMenu.y = event.clientY
  contextMenu.visible = true
}

function handleContextSelect(key: string) {
  contextMenu.visible = false
  handleTabAction(key, contextMenu.target)
}

function handleDropdownSelect(key: string) {
  handleTabAction(key, visitedRoutes.value.find((it) => it.path === currentTab.value) || null)
}

function handleTabAction(action: string, target: RouteRecordRaw | null) {
  switch (action) {
    case 'refresh':
      router.replace({ path: '/redirect' + route.fullPath })
      break
    case 'close-left':
      target &&
        visitedRouteStore.closeLeftVisitedView(target).then(() => {
          ensureActiveRoute()
        })
      break
    case 'close-right':
      target &&
        visitedRouteStore.closeRightVisitedView(target).then(() => {
          ensureActiveRoute()
        })
      break
    case 'close-all':
      visitedRouteStore.closeAllVisitedView().then(() => {
        router.push(visitedRouteStore.findLastRoutePath())
      })
      break
  }
}

function ensureActiveRoute() {
  const exists = visitedRoutes.value.some((it) => it.path === route.fullPath)
  if (!exists) {
    router.push(visitedRouteStore.findLastRoutePath())
  }
}

function escapeSelector(value: string) {
  if (typeof CSS !== 'undefined' && CSS.escape) {
    return CSS.escape(value)
  }
  return value.replace(/([.*+?^${}()|[\]\\])/g, '\\$1')
}
</script>

<style scoped lang="scss">
.tab-strip {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 6px 8px;
  background: var(--tabbar-bg);
  border-radius: var(--shell-radius-base);
  box-shadow: var(--tabbar-pill-shadow);
  position: relative;
}

.tab-strip__viewport {
  flex: 1;
  overflow: hidden;
  position: relative;
}

.tab-strip__list {
  display: flex;
  gap: 6px;
  min-height: var(--tabbar-height);
}

.tab-strip__arrow,
.tab-strip__more {
  width: 36px;
  height: 36px;
  border-radius: 999px;
  border: 1px solid var(--tabbar-border-color);
  background: transparent;
  color: var(--shell-text-color);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s ease, color 0.2s ease;
}

.tab-strip__arrow:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

.tab-strip__arrow:not(:disabled):hover,
.tab-strip__more:hover {
  background: var(--tabbar-pill-bg);
}

.tab-pill {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 0 14px;
  height: var(--tabbar-height);
  border-radius: calc(var(--shell-radius-base) + 6px);
  background: var(--tabbar-pill-bg);
  color: var(--tabbar-pill-text);
  border: 1px solid transparent;
  transition: background 0.2s ease, color 0.2s ease, transform 0.2s ease;
  max-width: 220px;
}

.tab-pill.is-active {
  background: var(--tabbar-pill-active-bg);
  color: var(--tabbar-pill-active-text);
  box-shadow: var(--tabbar-pill-shadow);
}

.tab-pill.is-affix::before {
  content: '·';
  font-size: 20px;
  line-height: 1;
}

.tab-pill__text {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-size: 13px;
  font-weight: 500;
}

.tab-pill__close {
  border: none;
  background: transparent;
  color: inherit;
  display: flex;
  align-items: center;
  cursor: pointer;
  opacity: 0.7;
  transition: opacity 0.2s ease;
}

.tab-pill__close:hover {
  opacity: 1;
}
</style>
