import { type App, type VNode, createVNode, render } from 'vue'
import { NSpin } from 'naive-ui'
import { useAppConfigStore } from '@/store/modules/app-config'

// 页面加载指令
export function loading(app: App) {
  app.directive('loading', {
    mounted: (el: HTMLElement, binding: { value: boolean }) => {
      const { value } = binding
      renderLoading(value, el)
    },
    updated: (el: HTMLElement, binding: { value: boolean }) => {
      const { value } = binding
      renderLoading(value, el)
    },
    unmounted: (el: HTMLElement) => {
      renderLoading(false, el)
    }
  })
}

function renderLoading(value: boolean, el: HTMLElement) {
  const appConfigStore = useAppConfigStore()

  const primaryColor = appConfigStore.themeColor.primaryColor
  el.style.position = 'relative'
  let overlayContainer = el.querySelector('.v-loading-overlay') as HTMLElement

  if (!overlayContainer) {
    overlayContainer = document.createElement('div')
    overlayContainer.className = 'v-loading-overlay'
    overlayContainer.style.position = 'absolute'
    overlayContainer.style.top = '0'
    overlayContainer.style.left = '0'
    overlayContainer.style.width = '100%'
    overlayContainer.style.height = '100%'
    overlayContainer.style.display = 'flex'
    overlayContainer.style.justifyContent = 'center'
    overlayContainer.style.alignItems = 'center'
    overlayContainer.style.backgroundColor = 'rgba(255, 255, 255, 0.8)'
    overlayContainer.style.zIndex = '9999'
    el.appendChild(overlayContainer)
  }

  if (value) {
    const vnode: VNode = createVNode(NSpin, {
      stroke: primaryColor
    })
    render(vnode, overlayContainer)
    overlayContainer.style.display = 'flex'
  } else {
    render(null, overlayContainer)
    overlayContainer.style.display = 'none'
  }
}
