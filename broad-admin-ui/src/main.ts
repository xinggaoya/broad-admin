import { createApp } from 'vue'
import App from './App.vue'
import './styles'
import { useAppRouter } from './router'
import useRouterGuard from './router/guard'
import useAppPinia from './store'
import { createDirectives } from './directives'

function vawBoot() {
  const app = createApp(App)
  useAppPinia(app)
  useAppRouter(app)
  useRouterGuard()
  createDirectives(app)
  app.mount('#app')
}

vawBoot()
