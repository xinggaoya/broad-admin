import { LAYOUT } from '@/store/keys'

export const asyncRoutes = [
  {
    path: '/',
    component: LAYOUT,
    name: 'Index',
    meta: {
      title: 'Dashboard',
      iconPrefix: 'iconfont',
      icon: 'dashboard'
    },
    children: [
      {
        path: '/',
        name: 'Home',
        component: () => import('@/views/index/main.vue'),
        meta: {
          title: '主控台',
          affix: true,
          cacheable: true,
          iconPrefix: 'iconfont',
          icon: 'menu'
        }
      },
      {
        path: '/work',
        name: 'Work',
        component: () => import('@/views/index/work-place.vue'),
        meta: {
          title: '工作台',
          affix: true,
          iconPrefix: 'iconfont',
          icon: 'menu'
        }
      }
    ]
  }
]
