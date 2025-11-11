<template>
  <section class="workplace">
    <n-card class="workplace-hero" :bordered="false">
      <div class="workplace-hero__primary">
        <n-avatar :src="avatar" :fallback-src="defaultAvatar" :size="86" round />
        <div class="workplace-hero__intro">
          <p class="hero-greeting">{{ greeting }}，{{ displayName }}</p>
          <h2 class="hero-title">今天也要保持高效</h2>
          <div class="hero-meta">
            <n-tag type="success" round size="small">{{ userRole }}</n-tag>
            <span class="hero-meta__weather">
              <n-icon size="16"><SunnyOutline /></n-icon>
              {{ weatherInfo }}
            </span>
            <span class="hero-meta__date">{{ formattedDate }}</span>
          </div>
        </div>
      </div>
      <div class="workplace-hero__stats">
        <div v-for="stat in heroStats" :key="stat.label" class="hero-stat">
          <p class="hero-stat__label">{{ stat.label }}</p>
          <p class="hero-stat__value">{{ stat.value }}</p>
          <small v-if="stat.helper">{{ stat.helper }}</small>
        </div>
      </div>
    </n-card>

    <section class="workplace-actions">
      <header class="section-header">
        <div>
          <p class="section-subtitle">常用功能</p>
          <h3>快捷操作</h3>
        </div>
      </header>
      <div class="action-grid">
        <button
          v-for="action in quickActions"
          :key="action.key"
          class="action-pill"
          type="button"
          @click="handleQuickAction(action)"
        >
          <span class="action-pill__icon" :style="{ color: action.color }">
            <n-icon size="22">
              <component :is="action.icon" />
            </n-icon>
          </span>
          <div>
            <span class="action-pill__title">{{ action.title }}</span>
            <small>{{ action.description }}</small>
          </div>
        </button>
      </div>
    </section>

    <div class="workplace-panels">
      <n-card class="panel" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div>
              <p class="panel-subtitle">项目进度</p>
              <h3>项目概览</h3>
            </div>
            <n-tag type="info" round>共 {{ projectList.length }} 个</n-tag>
          </div>
        </template>
        <n-data-table
          :columns="projectColumns"
          :data="projectList"
          :pagination="false"
          :bordered="false"
          :single-line="false"
        />
      </n-card>
      <n-card class="panel" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div>
              <p class="panel-subtitle">实时动态</p>
              <h3>项目活动</h3>
            </div>
          </div>
        </template>
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
              <n-avatar :src="activity.avatar" round :size="24" />
            </template>
          </n-timeline-item>
        </n-timeline>
      </n-card>
    </div>
  </section>
</template>

<script lang="ts" setup name="WorkplaceView">
import { computed, h, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/modules/user'
import { useAppConfigStore } from '@/store/modules/app-config'
import { format } from 'date-fns'
import { zhCN } from 'date-fns/locale'
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

const router = useRouter()
const userStore = useUserStore()
const appConfigStore = useAppConfigStore()

const avatar = computed(() => userStore.avatar)
const displayName = computed(() => userStore.nickName || userStore.userName || '访客')
const formattedDate = computed(() => format(new Date(), 'yyyy/MM/dd', { locale: zhCN }))
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

const userRole = computed(() => {
  const role = userStore.roles?.[0] || '普通用户'
  return role.toUpperCase()
})

const projectCount = ref(12)
const todoCount = ref(3)
const totalTodos = ref(20)
const weatherInfo = ref('今日晴朗，适合出门散步~')
const tasksProgress = computed(() =>
  totalTodos.value === 0 ? 0 : Math.round((todoCount.value / totalTodos.value) * 100)
)

const heroStats = computed(() => [
  { label: '当前项目', value: `${projectCount.value}`, helper: '进行中' },
  { label: '待办事项', value: `${todoCount.value}/${totalTodos.value}`, helper: `完成度 ${tasksProgress.value}%` },
  { label: '系统主题', value: appConfigStore.theme === 'dark' ? '暗黑' : '明亮', helper: appConfigStore.deviceType.toUpperCase() }
])

const quickActions: Array<{
  key: string
  title: string
  description: string
  icon: any
  path: string
  color: string
}> = [
  { key: 'home', title: '首页概览', description: '数据监控中心', icon: HomeOutline, path: '/', color: '#2080f0' },
  { key: 'system', title: '系统管理', description: '组织架构与权限', icon: SettingsOutline, path: '/system/department', color: '#18a058' },
  { key: 'list', title: '数据列表', description: '高频数据维护', icon: ListOutline, path: '/list/table-custom', color: '#f0a020' },
  { key: 'form', title: '表单示例', description: '录入与审批', icon: DocumentTextOutline, path: '/form/base-form-view', color: '#d03050' },
  { key: 'menu', title: '多级菜单', description: '导航结构演示', icon: LayersOutline, path: '/next/menu2/menu-2-1/menu-2-1-1', color: '#8f44ff' },
  { key: 'more', title: '更多功能', description: '探索更多组件', icon: AppsOutline, path: '/other/qrcode', color: '#1890ff' }
]

const projectColumns = [
  { title: '项目名称', key: 'name' },
  { title: '开始时间', key: 'startTime' },
  { title: '结束时间', key: 'endTime' },
  {
    title: '进度',
    key: 'progress',
    render: (row: { progress: number }) =>
      h(NProgress, {
        type: 'line',
        percentage: row.progress,
        color: getProgressColor(row.progress),
        height: 6
      })
  },
  {
    title: '状态',
    key: 'status',
    render: (row: { status: string }) =>
      h(
        NTag,
        { type: getStatusType(row.status) },
        { default: () => row.status }
      )
  }
]

const projectList = [
  { id: 1, name: '后台管理系统', startTime: '2024-01-01', endTime: '2024-03-31', progress: 85, status: '进行中' },
  { id: 2, name: '移动端APP开发', startTime: '2024-02-01', endTime: '2024-05-31', progress: 35, status: '进行中' },
  { id: 3, name: '数据分析平台', startTime: '2024-01-15', endTime: '2024-02-28', progress: 100, status: '已完成' },
  { id: 4, name: '官网改版', startTime: '2024-03-01', endTime: '2024-04-15', progress: 0, status: '未开始' }
]

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

const handleQuickAction = (action: (typeof quickActions)[number]) => {
  router.push(action.path)
}
</script>

<style scoped lang="scss">
.workplace {
  display: flex;
  flex-direction: column;
  gap: var(--shell-gap);
}

.workplace-hero {
  border-radius: var(--shell-radius-xl);
  background: linear-gradient(135deg, rgba(43, 92, 231, 0.18), rgba(14, 165, 233, 0.12));
  border: 1px solid rgba(43, 92, 231, 0.2);
  box-shadow: var(--shell-shadow);
}

.workplace-hero__primary {
  display: flex;
  gap: 20px;
  align-items: center;
  padding-bottom: 12px;
}

.hero-greeting {
  margin: 0;
  font-size: 14px;
  color: var(--shell-muted-text-color);
}

.hero-title {
  margin: 4px 0 8px;
  font-size: 24px;
  font-weight: 600;
}

.hero-meta {
  display: flex;
  gap: 12px;
  align-items: center;
  font-size: 13px;
  color: var(--shell-muted-text-color);
}

.hero-meta__weather,
.hero-meta__date {
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.workplace-hero__stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 16px;
  margin-top: 12px;
}

.hero-stat {
  padding: 12px 16px;
  border-radius: var(--shell-radius-base);
  background: rgba(255, 255, 255, 0.55);
  color: #0f172a;

  .hero-stat__label {
    margin: 0;
    font-size: 13px;
    opacity: 0.75;
  }

  .hero-stat__value {
    margin: 0;
    font-size: 20px;
    font-weight: 600;
  }

  small {
    font-size: 12px;
    opacity: 0.85;
  }
}

.workplace-actions {
  background: var(--shell-surface);
  border-radius: var(--shell-radius-lg);
  padding: 20px;
  box-shadow: var(--shell-shadow);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;

  h3 {
    margin: 0;
    font-size: 18px;
  }
}

.section-subtitle {
  margin: 0;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--shell-muted-text-color);
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
  gap: 12px;
}

.action-pill {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  border-radius: var(--shell-radius-base);
  border: 1px solid var(--shell-border-color);
  background: transparent;
  cursor: pointer;
  transition: transform 0.2s ease, border-color 0.2s ease;

  &:hover {
    transform: translateY(-3px);
    border-color: var(--color-primary);
  }
}

.action-pill__icon {
  width: 42px;
  height: 42px;
  border-radius: 12px;
  background: rgba(15, 23, 42, 0.05);
  display: grid;
  place-items: center;
}

.action-pill__title {
  font-weight: 600;
  color: var(--shell-text-color);
  display: block;
}

.action-pill small {
  font-size: 12px;
  color: var(--shell-muted-text-color);
}

.workplace-panels {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: var(--shell-gap);
}

.panel {
  border-radius: var(--shell-radius-lg);
  background: var(--shell-surface);
  box-shadow: var(--shell-shadow);
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-subtitle {
  margin: 0;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: var(--shell-muted-text-color);
}

@media (max-width: 768px) {
  .workplace-hero__primary {
    flex-direction: column;
    text-align: center;
  }

  .hero-meta {
    justify-content: center;
    flex-wrap: wrap;
  }

  .action-grid {
    grid-template-columns: repeat(auto-fit, minmax(140px, 1fr));
  }
}
</style>
