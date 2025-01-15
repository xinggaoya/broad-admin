<template>
  <div
    class="vaw-main-layout-container scrollbar"
    :class="[
      layoutMode === 'ttb'
        ? 'main-layout-ttb-status'
        : !appConfig.isCollapse
          ? 'main-layout-open-status'
          : 'main-layout-close-status',
      appConfig.isFixedNavBar ? 'main-layout_fixed-nav-bar' : 'main-layout'
    ]"
  >
    <section
      :class="[
        layoutMode === 'ttb'
          ? 'nav-bar-ttb-status'
          : !appConfig.isCollapse
            ? 'nav-bar-open-status'
            : 'nav-bar-close-status',
        appConfig.isFixedNavBar ? 'fixed-nav-bar' : '',
        !showNavBar ? 'tab-bar-top' : ''
      ]"
    >
      <NavBar v-if="showNavBar" />
      <TabBar />
    </section>
    <div class="main-base-style scrollbar" :class="[mainClass]">
      <section class="main-section">
        <Main />
      </section>
      <section class="footer-wrapper">
        <FooterView v-if="appConfig.actionBar.isShowFooter" />
      </section>
      <n-back-top :listen-to="listenTo1" />
    </div>
    <n-back-top :listen-to="listenTo2" />
  </div>
</template>

<script lang="ts" setup>
import { useAppConfigStore } from '@/store/modules/app-config'
import { ThemeMode } from '@/store/types'
import { useLoadingBar } from 'naive-ui'
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

defineProps({
  showNavBar: {
    type: Boolean,
    default: true
  }
})

const appConfig = useAppConfigStore()
const listenTo1 = ref<HTMLElement | null>(null)
const listenTo2 = ref<HTMLElement | null>(null)
const mainClass = computed(() => {
  return appConfig.theme === ThemeMode.DARK ? 'main-base-dark-theme' : 'main-base-light-theme'
})
const layoutMode = computed(() => {
  return appConfig.getLayoutMode
})
const router = useRouter()
const loadingBar = useLoadingBar()
router.beforeEach(() => {
  loadingBar?.start()
})
router.afterEach(() => {
  loadingBar?.finish()
})
onMounted(() => {
  listenTo1.value = document.querySelector('.main-base-style')
  listenTo2.value = document.querySelector('.vaw-main-layout-container')
})
</script>

<style lang="scss" scoped>
.scrollbar::-webkit-scrollbar {
  width: 0;
}

.main-layout-ttb-status {
  margin-left: 0;
}

.main-layout-open-status {
  margin-left: $menuWidth;
}

.main-layout-close-status {
  margin-left: $minMenuWidth;
}

.nav-bar-open-status.fixed-nav-bar {
  width: calc(100% - #{$menuWidth});
  background-color: var(--body-color);
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.nav-bar-close-status.fixed-nav-bar {
  width: calc(100% - #{$minMenuWidth});
  background-color: var(--body-color);
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.nav-bar-ttb-status {
  width: 100%;
  background-color: var(--body-color);
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.main-layout {
  padding-top: 0;
  overflow-y: auto;
}

.main-layout_fixed-nav-bar {
  padding-top: $logoHeight + $tabHeight;
  height: 100vh;
  overflow: hidden;

  .main-base-style {
    height: calc(100vh - #{$logoHeight} - #{$tabHeight});
    overflow-y: auto;
    position: relative;
  }
}

.vaw-main-layout-container {
  height: 100vh;
  box-sizing: border-box;
  transition: margin-left $transitionTime;

  .main-base-style {
    height: 100%;
    box-sizing: border-box;
    padding: 16px;
    position: relative;
  }

  .main-base-light-theme {
    background-color: #f0f2f5;
  }

  .main-base-dark-theme {
    background-color: #333333;
  }

  .main-section {
    min-height: calc(100% - #{$footerHeight});
    overflow-x: hidden;
    padding-bottom: $footerHeight;
  }

  .fixed-nav-bar {
    position: fixed;
    top: 0;
    transition: width $transitionTime;
    z-index: 99;
  }

  .tab-bar-top {
    padding-top: $logoHeight;
  }
}

.footer-wrapper {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: var(--body-color);
  z-index: 98;
  margin-top: 0;
  box-shadow: 0 -1px 4px rgba(0, 21, 41, 0.08);
}

.is-mobile {
  .main-layout-open-status,
  .main-layout-close-status {
    margin-left: 0;
    transition: none;
  }

  .nav-bar-open-status,
  .nav-bar-close-status {
    width: 100%;
  }

  .footer-wrapper {
    left: 0;
  }
}

.scrollbar {
  &::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background-color: rgba(0, 0, 0, 0.2);
    border-radius: 3px;
  }

  &::-webkit-scrollbar-track {
    background-color: transparent;
  }
}
</style>
