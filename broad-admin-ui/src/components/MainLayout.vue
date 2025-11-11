<template>
  <section class="shell-main" :class="{ 'shell-main--dark': isDarkTheme }">
    <div class="shell-main__nav" :class="[{ 'is-fixed': appConfig.isFixedNavBar, 'no-nav': !showNavBar }]">
      <NavBar v-if="showNavBar" />
      <TabBar />
    </div>

    <div class="shell-main__content" ref="contentRef">
      <section class="shell-main__page">
        <Main />
      </section>
      <footer v-if="appConfig.actionBar.isShowFooter" class="shell-main__footer">
        <FooterView />
      </footer>
    </div>

    <n-back-top :listen-to="contentRef" />
  </section>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useLoadingBar } from 'naive-ui'
import { useAppConfigStore } from '@/store/modules/app-config'
import { ThemeMode } from '@/store/types'
import NavBar from '@/components/navbar/NavBar.vue'
import TabBar from '@/components/tabbar/TabBar.vue'
import Main from '@/components/Main.vue'
import FooterView from '@/components/footer/FooterView.vue'

defineProps({
  showNavBar: {
    type: Boolean,
    default: true
  }
})

const appConfig = useAppConfigStore()
const contentRef = ref<HTMLElement | null>(null)
const isDarkTheme = computed(() => appConfig.theme === ThemeMode.DARK)
const router = useRouter()
const loadingBar = useLoadingBar()

router.beforeEach(() => {
  loadingBar?.start()
})

router.afterEach(() => {
  loadingBar?.finish()
})
</script>

<style scoped lang="scss">
.shell-main {
  position: relative;
  display: flex;
  flex-direction: column;
  gap: var(--shell-gap);
  min-height: calc(100vh - (var(--shell-content-padding) * 2));
  color: var(--shell-text-color);
  transition: color 0.3s ease;
}

.shell-main__nav {
  display: flex;
  flex-direction: column;
  gap: 8px;
  background: transparent;
}

.shell-main__nav.is-fixed {
  position: sticky;
  top: clamp(8px, var(--shell-content-padding), 32px);
  z-index: 5;
  background: var(--shell-surface);
  border-radius: var(--shell-radius-base);
  box-shadow: var(--shell-shadow);
  padding: 12px;
}

.shell-main__nav.no-nav {
  padding-top: 0;
}

.shell-main__content {
  flex: 1;
  display: flex;
  flex-direction: column;
  background: var(--shell-surface);
  border-radius: var(--shell-radius-lg);
  box-shadow: var(--shell-shadow);
  padding: 20px;
  overflow: hidden;
}

.shell-main__content::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.shell-main__content::-webkit-scrollbar-thumb {
  background-color: rgba(100, 116, 139, 0.35);
  border-radius: 4px;
}

.shell-main__page {
  flex: 1;
  min-height: 0;
  overflow: auto;
  background: var(--shell-page-bg);
  border-radius: calc(var(--shell-radius-lg) - 8px);
  padding: 16px;
}

.shell-main__page::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.shell-main__page::-webkit-scrollbar-thumb {
  background-color: rgba(15, 23, 42, 0.25);
  border-radius: 3px;
}

.shell-main__footer {
  margin-top: 20px;
  padding-top: 12px;
  border-top: 1px solid var(--shell-border-color);
}

@media (max-width: 768px) {
  .shell-main__content {
    padding: 12px;
  }

  .shell-main__page {
    padding: 12px;
  }
}
</style>
