<template>
  <div class="workplace-container">
    <!-- 用户信息卡片 -->
    <n-card class="user-card" :bordered="false">
      <div class="user-info">
        <div class="user-avatar">
          <n-avatar :src="avatar" :fallback-src="defaultAvatar" :size="80" round />
        </div>
        <div class="user-detail">
          <h3 class="welcome">
            {{ greeting }}，{{ userStore.nickName || userStore.userName }}
            <n-tag type="success" class="role-tag">{{ userRole }}</n-tag>
          </h3>
          <p class="subtitle">
            <n-icon><SunnyOutline /></n-icon>
            {{ weatherInfo }}
          </p>
        </div>
        <div class="user-stats">
          <div class="stat-item">
            <span class="label">项目数</span>
            <span class="value">{{ projectCount }}</span>
          </div>
          <div class="stat-item">
            <span class="label">待办项</span>
            <span class="value">{{ todoCount }}/{{ totalTodos }}</span>
          </div>
          <div class="stat-item">
            <span class="label">当前日期</span>
            <span class="value">{{ formattedDate }}</span>
          </div>
        </div>
      </div>
    </n-card>

    <!-- 快捷操作区 -->
    <div class="quick-actions">
      <n-grid :cols="24" :x-gap="16" :y-gap="16">
        <n-grid-item v-for="action in quickActions" :key="action.key" class="grid-item" :span="4">
          <n-card
            class="action-card"
            :class="{ 'is-dark': isDark }"
            @click="handleQuickAction(action)"
          >
            <div class="action-content">
              <n-icon :size="32" :color="action.color">
                <component :is="action.icon" />
              </n-icon>
              <span class="action-title">{{ action.title }}</span>
            </div>
          </n-card>
        </n-grid-item>
      </n-grid>
    </div>

    <!-- 项目概览区 -->
    <div class="project-overview">
      <n-grid :cols="24" :x-gap="16">
        <n-grid-item :span="14">
          <n-card title="项目进度" class="project-card" :class="{ 'is-dark': isDark }">
            <n-data-table
              :columns="projectColumns"
              :data="projectList"
              :pagination="false"
              :bordered="false"
              :single-line="false"
            >
              <template #status="{ row }">
                <n-tag :type="getStatusType(row.status)">
                  {{ row.status }}
                </n-tag>
              </template>
              <template #progress="{ row }">
                <n-progress
                  type="line"
                  :percentage="row.progress"
                  :color="getProgressColor(row.progress)"
                  :height="6"
                />
              </template>
            </n-data-table>
          </n-card>
        </n-grid-item>
        <n-grid-item :span="10">
          <n-card title="项目动态" class="activity-card" :class="{ 'is-dark': isDark }">
            <n-timeline>
              <n-timeline-item
                v-for="activity in activityList"
                :key="activity.id"
                :type="activity.type"
                :title="activity.title"
                :content="activity.content"
                :time="activity.time"
              >
                <template #icon>
                  <n-avatar :src="activity.avatar" :round="true" :size="24" />
                </template>
              </n-timeline-item>
            </n-timeline>
          </n-card>
        </n-grid-item>
      </n-grid>
    </div>
  </div>
</template>

<script lang="ts" setup name="WorkplaceView">
import { ref, computed, h } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/modules/user'
import { useAppConfigStore } from '@/store/modules/app-config'
import { format } from 'date-fns'
import { NProgress, NTag } from 'naive-ui'
import {
  HomeOutline,
  SettingsOutline,
  ListOutline,
  DocumentTextOutline,
  LayersOutline,
  AppsOutline,
  SunnyOutline
} from '@vicons/ionicons5'
import defaultAvatar from '@/assets/defaultProfilePicture.gif'

// 组件实例
const router = useRouter()
const userStore = useUserStore()
const appConfigStore = useAppConfigStore()

// 计算属性
const isDark = computed(() => appConfigStore.theme === 'dark')
const avatar = computed(() => userStore.avatar)
const formattedDate = computed(() => format(new Date(), 'yyyy/MM/dd'))
const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 6) return '凌晨好'
  if (hour < 9) return '早上好'
  if (hour < 12) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 17) return '下午好'
  if (hour < 19) return '傍晚好'
  return '晚上好'
})

// 计算用户角色
const userRole = computed(() => {
  return '普通用户' // 这里可以根据实际的用户权限判断逻辑返回对应的角色名称
})

// 统计数据
const projectCount = ref(12)
const todoCount = ref(3)
const totalTodos = ref(20)
const weatherInfo = ref('今日晴朗，适合出门散步~')

// 快捷操作配置
const quickActions = [
  {
    key: 'home',
    title: '首页概览',
    icon: HomeOutline,
    path: '/',
    color: '#2080f0'
  },
  {
    key: 'system',
    title: '系统管理',
    icon: SettingsOutline,
    path: '/system/department',
    color: '#18a058'
  },
  {
    key: 'list',
    title: '数据列表',
    icon: ListOutline,
    path: '/list/table-custom',
    color: '#f0a020'
  },
  {
    key: 'form',
    title: '表单示例',
    icon: DocumentTextOutline,
    path: '/form/base-form-view',
    color: '#d03050'
  },
  {
    key: 'menu',
    title: '多级菜单',
    icon: LayersOutline,
    path: '/next/menu2/menu-2-1/menu-2-1-1',
    color: '#8f44ff'
  },
  {
    key: 'more',
    title: '更多功能',
    icon: AppsOutline,
    path: '/other/qrcode',
    color: '#1890ff'
  }
]

// 项目列表配置
const projectColumns = [
  { title: '项目名称', key: 'name' },
  { title: '开始时间', key: 'startTime' },
  { title: '结束时间', key: 'endTime' },
  {
    title: '进度',
    key: 'progress',
    render: (row: { progress: number }) => {
      return h(NProgress, {
        type: 'line',
        percentage: row.progress,
        color: getProgressColor(row.progress),
        height: 6
      })
    }
  },
  {
    title: '状态',
    key: 'status',
    render: (row: { status: string }) => {
      return h(
        NTag,
        {
          type: getStatusType(row.status)
        },
        { default: () => row.status }
      )
    }
  }
]

const projectList = [
  {
    id: 1,
    name: '后台管理系统',
    startTime: '2024-01-01',
    endTime: '2024-03-31',
    progress: 85,
    status: '进行中'
  },
  {
    id: 2,
    name: '移动端APP开发',
    startTime: '2024-02-01',
    endTime: '2024-05-31',
    progress: 35,
    status: '进行中'
  },
  {
    id: 3,
    name: '数据分析平台',
    startTime: '2024-01-15',
    endTime: '2024-02-28',
    progress: 100,
    status: '已完成'
  },
  {
    id: 4,
    name: '官网改版',
    startTime: '2024-03-01',
    endTime: '2024-04-15',
    progress: 0,
    status: '未开始'
  }
]

// 项目动态
const activityList = [
  {
    id: 1,
    type: 'success',
    title: '项目发布',
    content: '后台管理系统V2.0版本发布',
    time: '2小时前',
    avatar: 'https://07akioni.oss-cn-beijing.aliyuncs.com/07akioni.jpeg'
  },
  {
    id: 2,
    type: 'info',
    title: '团队协作',
    content: '邀请新成员加入项目组',
    time: '4小时前',
    avatar: 'https://07akioni.oss-cn-beijing.aliyuncs.com/07akioni.jpeg'
  },
  {
    id: 3,
    type: 'warning',
    title: '系统通知',
    content: '服务器计划于今晚维护升级',
    time: '1天前',
    avatar: 'https://07akioni.oss-cn-beijing.aliyuncs.com/07akioni.jpeg'
  }
]

// 工具函数
const getStatusType = (status: string) => {
  switch (status) {
    case '已完成':
      return 'success'
    case '进行中':
      return 'info'
    case '未开始':
      return 'warning'
    default:
      return 'default'
  }
}

const getProgressColor = (progress: number) => {
  if (progress >= 90) return '#18a058'
  if (progress >= 60) return '#2080f0'
  if (progress >= 30) return '#f0a020'
  return '#d03050'
}

// 事件处理
const handleQuickAction = (action: (typeof quickActions)[0]) => {
  router.push(action.path)
}
</script>

<style lang="scss" scoped>
.workplace-container {
  padding: 16px;
  background: var(--n-color);
  min-height: 100%;

  .user-card {
    margin-bottom: 16px;
    transition: all 0.3s;

    .user-info {
      display: flex;
      align-items: center;
      padding: 16px;

      .user-avatar {
        margin-right: 24px;
      }

      .user-detail {
        flex: 1;

        .welcome {
          font-size: 20px;
          font-weight: 500;
          color: var(--n-text-color);
          margin: 0 0 8px;
          display: flex;
          align-items: center;

          .role-tag {
            margin-left: 12px;
          }
        }

        .subtitle {
          color: var(--n-text-color-3);
          margin: 0;
          display: flex;
          align-items: center;

          .n-icon {
            margin-right: 4px;
          }
        }
      }

      .user-stats {
        display: flex;
        gap: 32px;

        .stat-item {
          text-align: center;

          .label {
            font-size: 13px;
            color: var(--n-text-color-3);
            margin-bottom: 4px;
            display: block;
          }

          .value {
            font-size: 20px;
            font-weight: 600;
            color: var(--n-text-color);
          }
        }
      }
    }
  }

  .quick-actions {
    margin-bottom: 16px;

    .action-card {
      cursor: pointer;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-4px);
      }

      &.is-dark {
        background: rgba(24, 24, 28, 0.9);
      }

      .action-content {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 8px;
        padding: 16px;

        .action-title {
          font-size: 14px;
          color: var(--n-text-color);
        }
      }
    }
  }

  .project-overview {
    .project-card,
    .activity-card {
      height: 100%;
      transition: all 0.3s;

      &.is-dark {
        background: rgba(24, 24, 28, 0.9);
      }
    }

    .activity-card {
      :deep(.n-timeline) {
        padding: 16px;
      }
    }
  }
}

// 响应式布局
@media screen and (max-width: 1400px) {
  .workplace-container {
    .quick-actions {
      .grid-item {
        --n-span: 6 !important;
      }
    }
  }
}

@media screen and (max-width: 768px) {
  .workplace-container {
    .user-info {
      flex-direction: column;
      text-align: center;

      .user-avatar {
        margin: 0 0 16px;
      }

      .user-stats {
        margin-top: 16px;
        width: 100%;
        justify-content: space-around;
      }
    }

    .quick-actions {
      .grid-item {
        --n-span: 12 !important;
      }
    }

    .project-overview {
      .n-grid-item {
        --n-span: 24 !important;
      }
    }
  }
}
</style>
