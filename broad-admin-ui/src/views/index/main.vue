<template>
  <section ref="dashboardRef" class="dashboard">
    <div class="dashboard__stats">
      <n-card
        v-for="card in statCards"
        :key="card.title"
        class="stat-card"
        :class="`stat-card--${card.intent}`"
        :bordered="false"
      >
        <div class="stat-card__body">
          <div>
            <p class="stat-card__label">{{ card.title }}</p>
            <p class="stat-card__value" aria-live="polite">{{ card.value }}</p>
            <p class="stat-card__total">{{ card.total }}</p>
            <ul class="stat-card__deltas">
              <li v-for="delta in card.deltas" :key="delta.label" :class="`is-${delta.trend}`">
                <n-icon size="16">
                  <component :is="delta.trend === 'up' ? TrendingUpOutline : CalendarOutline" />
                </n-icon>
                {{ delta.label }}：{{ delta.value }}
              </li>
            </ul>
          </div>
          <div class="stat-card__meta">
            <div class="stat-card__icon">
              <n-icon size="36">
                <component :is="card.icon" />
              </n-icon>
            </div>
            <n-progress
              v-if="card.progress !== undefined"
              type="line"
              :percentage="card.progress"
              :show-indicator="false"
              :stroke-width="8"
            />
          </div>
        </div>
      </n-card>
    </div>

    <n-card class="dashboard-panel dashboard-panel--highlight" :bordered="false">
      <template #header>
        <div class="panel-header">
          <div>
            <p class="panel-subtitle">FY2025</p>
            <h3>年度销售趋势</h3>
          </div>
          <n-tag type="success" round>实时监控</n-tag>
        </div>
      </template>
      <FullYearSalesChart ref="fullYearSalesChart" />
    </n-card>

    <div class="dashboard__grid">
      <n-card class="dashboard-panel" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div>
              <p class="panel-subtitle">销售漏斗</p>
              <h3>销售分析</h3>
            </div>
          </div>
        </template>
        <SalesChart ref="salesChart" />
      </n-card>
      <n-card class="dashboard-panel" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div>
              <p class="panel-subtitle">新增 & 活跃</p>
              <h3>用户增长</h3>
            </div>
          </div>
        </template>
        <StudentChart ref="studentChart" />
      </n-card>
      <n-card class="dashboard-panel" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div>
              <p class="panel-subtitle">触达渠道</p>
              <h3>渠道分布</h3>
            </div>
          </div>
        </template>
        <EnrollmentChannelsChart ref="enrollmentChannelsChart" />
      </n-card>
      <n-card class="dashboard-panel" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div>
              <p class="panel-subtitle">组织画像</p>
              <h3>部门分布</h3>
            </div>
          </div>
        </template>
        <DepartmentChart ref="departmentChart" />
      </n-card>
    </div>
  </section>
</template>

<script lang="ts" setup name="DashboardMain">
import { computed, onBeforeUnmount, ref, watch } from 'vue'
import { useDebounceFn, useEventListener, useResizeObserver } from '@vueuse/core'
import { useAppConfigStore } from '@/store/modules/app-config'
import { ThemeMode } from '@/store/types'
import {
  TrendingUpOutline,
  CalendarOutline,
  PeopleOutline,
  CartOutline,
  WalletOutline,
  EyeOutline
} from '@vicons/ionicons5'
import SalesChart from './components/chart/SalesChart.vue'
import StudentChart from './components/chart/StudentChart.vue'
import EnrollmentChannelsChart from './components/chart/EnrollmentChannelsChart.vue'
import FullYearSalesChart from './components/chart/FullYearSalesChart.vue'
import DepartmentChart from './components/chart/DepartmentChart.vue'

type StatIntent = 'primary' | 'success' | 'warning' | 'info'

const appConfigStore = useAppConfigStore()
const dashboardRef = ref<HTMLElement | null>(null)

const salesChart = ref<InstanceType<typeof SalesChart>>()
const enrollmentChannelsChart = ref<InstanceType<typeof EnrollmentChannelsChart>>()
const studentChart = ref<InstanceType<typeof StudentChart>>()
const fullYearSalesChart = ref<InstanceType<typeof FullYearSalesChart>>()
const departmentChart = ref<InstanceType<typeof DepartmentChart>>()

const chartRefs = [
  salesChart,
  enrollmentChannelsChart,
  studentChart,
  fullYearSalesChart,
  departmentChart
]

const isDark = computed(() => appConfigStore.theme === ThemeMode.DARK)

const statCards: Array<{
  title: string
  value: string
  total: string
  intent: StatIntent
  icon: any
  progress?: number
  deltas: Array<{ label: string; value: string; trend: 'up' | 'flat' | 'down' }>
}> = [
  {
    title: '今日访问量',
    value: '1,234',
    total: '访问总量 100万+',
    intent: 'primary',
    icon: EyeOutline,
    progress: 68,
    deltas: [
      { label: '较昨日', value: '+12%', trend: 'up' },
      { label: '较上周', value: '+25%', trend: 'up' }
    ]
  },
  {
    title: '新增用户',
    value: '678',
    total: '用户总量 20万+',
    intent: 'success',
    icon: PeopleOutline,
    progress: 52,
    deltas: [
      { label: '较昨日', value: '+8%', trend: 'up' },
      { label: '较上周', value: '+15%', trend: 'up' }
    ]
  },
  {
    title: '月度销售额',
    value: '￥123,456',
    total: '总销售额 1000万+',
    intent: 'warning',
    icon: WalletOutline,
    progress: 75,
    deltas: [{ label: '目标完成度', value: '75%', trend: 'up' }]
  },
  {
    title: '订单统计',
    value: '892',
    total: '订单总量 5万+',
    intent: 'info',
    icon: CartOutline,
    progress: 65,
    deltas: [{ label: '同比', value: '+9%', trend: 'up' }]
  }
]

const scheduleChartRefresh = useDebounceFn(() => {
  chartRefs.forEach((chart) => chart.value?.updateChart?.())
}, 160)

watch(
  () => [appConfigStore.isCollapse, appConfigStore.deviceType, isDark.value],
  () => scheduleChartRefresh()
)

useResizeObserver(dashboardRef, () => scheduleChartRefresh())
const stopWindowResize = useEventListener(window, 'resize', scheduleChartRefresh)

onBeforeUnmount(() => {
  stopWindowResize()
})
</script>

<style scoped lang="scss">
.dashboard {
  display: flex;
  flex-direction: column;
  gap: var(--shell-gap);
}

.dashboard__stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: var(--shell-gap);
}

.stat-card {
  border-radius: var(--shell-radius-lg);
  background: var(--shell-surface);
  box-shadow: var(--shell-shadow);
  transition: transform 0.25s ease, box-shadow 0.25s ease;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 20px 45px rgba(15, 23, 42, 0.18);
  }
}

.stat-card__body {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
}

.stat-card__label {
  font-size: 14px;
  color: var(--shell-muted-text-color);
  margin-bottom: 4px;
}

.stat-card__value {
  font-size: 30px;
  font-weight: 600;
  color: var(--shell-text-color);
  margin: 0 0 4px;
}

.stat-card__total {
  font-size: 13px;
  color: var(--shell-muted-text-color);
  margin-bottom: 8px;
}

.stat-card__deltas {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 12px;

  li {
    display: flex;
    align-items: center;
    gap: 4px;
    color: var(--shell-muted-text-color);

    &.is-up {
      color: #16a34a;
    }
  }
}

.stat-card__meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 12px;
  min-width: 90px;
}

.stat-card__icon {
  width: 48px;
  height: 48px;
  border-radius: var(--shell-radius-base);
  display: grid;
  place-items: center;
  background: rgba(255, 255, 255, 0.12);
}

.stat-card--primary .stat-card__icon {
  background: rgba(32, 128, 240, 0.18);
  color: #2080f0;
}
.stat-card--success .stat-card__icon {
  background: rgba(24, 160, 88, 0.18);
  color: #18a058;
}
.stat-card--warning .stat-card__icon {
  background: rgba(240, 160, 32, 0.18);
  color: #f0a020;
}
.stat-card--info .stat-card__icon {
  background: rgba(32, 128, 240, 0.12);
  color: #2080f0;
}

.dashboard-panel {
  border-radius: var(--shell-radius-lg);
  background: var(--shell-surface);
  box-shadow: var(--shell-shadow);
}

.dashboard-panel--highlight {
  border: 1px solid var(--shell-border-color);
}

.panel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;

  h3 {
    margin: 0;
    font-size: 18px;
    font-weight: 600;
    color: var(--shell-text-color);
  }
}

.panel-subtitle {
  margin: 0;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--shell-muted-text-color);
}

.dashboard__grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: var(--shell-gap);
}

@media (max-width: 768px) {
  .stat-card__body {
    flex-direction: column;
  }

  .stat-card__meta {
    flex-direction: row;
    justify-content: space-between;
    width: 100%;
  }
}
</style>
