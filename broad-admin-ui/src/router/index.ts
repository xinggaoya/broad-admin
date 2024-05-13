import type { App } from 'vue'
import { mapTwoLevelRouter } from '@/store/help'
import { createRouter, createWebHistory } from 'vue-router'
import { constantRoutes } from './routes/constants'

const router = createRouter({
  history: createWebHistory(),
  routes: mapTwoLevelRouter([...constantRoutes])
})

export function useAppRouter(app: App) {
  app.use(router)
}

export default router
