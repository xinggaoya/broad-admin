import type { App } from 'vue'
import { auth } from './auth'
import { loading } from './loading'

export const createDirectives = (app: App) => {
  auth(app)
  loading(app)
}
