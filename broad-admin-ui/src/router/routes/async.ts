import type { RouteRecordRaw } from 'vue-router'
import { LAYOUT } from '@/store/keys'

/**
 * 默认异步路由：保障登录后至少有一个仪表盘入口
 */
export const asyncRoutes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Root',
    component: LAYOUT,
    redirect: '/dashboard',
    meta: {
      title: '仪表盘',
      iconPrefix: 'iconfont',
      icon: 'dashboard'
    },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/index/main.vue'),
        meta: {
          title: '主控台',
          affix: true,
          cacheable: true,
          iconPrefix: 'iconfont',
          icon: 'menu',
          isRootPath: true
        }
      },
      {
        path: 'workbench',
        name: 'Workbench',
        component: () => import('@/views/index/work-place.vue'),
        meta: {
          title: '工作台',
          iconPrefix: 'iconfont',
          icon: 'menu'
        }
      }
    ]
  }
]
