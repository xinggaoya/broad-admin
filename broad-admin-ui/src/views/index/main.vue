<template>
  <div class="dashboard-container">
    <!-- 数据概览卡片 -->
    <n-grid :x-gap="16" :y-gap="16" :cols="24">
      <n-grid-item :span="6" v-for="(item, index) in dataCards" :key="index">
        <n-card class="data-card" :class="item.type">
          <div class="card-content">
            <div class="card-left">
              <h3 class="card-title">{{ item.title }}</h3>
              <div class="card-value">{{ item.value }}</div>
              <div class="card-total">{{ item.total }}</div>
              <div class="card-compare" v-if="item.compare">
                <div>
                  <n-icon><TrendingUpOutline /></n-icon>
                  较昨日: {{ item.compare.day }}
                </div>
                <div>
                  <n-icon><CalendarOutline /></n-icon>
                  较上周: {{ item.compare.week }}
                </div>
              </div>
              <n-progress
                v-if="item.progress"
                type="line"
                :percentage="item.progress"
                :color="
                  item.type === 'success'
                    ? '#18a058'
                    : item.type === 'warning'
                      ? '#f0a020'
                      : '#2080f0'
                "
              />
            </div>
            <div class="card-right">
              <n-icon :size="40" :class="item.type">
                <component :is="item.icon" />
              </n-icon>
            </div>
          </div>
        </n-card>
      </n-grid-item>
    </n-grid>

    <!-- 年度销售趋势 -->
    <n-card class="chart-card" title="年度销售趋势" :class="{ 'is-dark': isDark }">
      <FullYearSalesChart ref="fullYearSalesChart" />
    </n-card>

    <!-- 数据分析图表 -->
    <n-grid :x-gap="16" :y-gap="16" :cols="24" class="mt-4">
      <n-grid-item :span="6">
        <n-card class="chart-card" title="销售分析" :class="{ 'is-dark': isDark }">
          <SalesChart ref="salesChart" />
        </n-card>
      </n-grid-item>
      <n-grid-item :span="6">
        <n-card class="chart-card" title="用户增长" :class="{ 'is-dark': isDark }">
          <StudentChart ref="studentChart" />
        </n-card>
      </n-grid-item>
      <n-grid-item :span="6">
        <n-card class="chart-card" title="渠道分布" :class="{ 'is-dark': isDark }">
          <EnrollmentChannelsChart ref="enrollmentChannelsChart" />
        </n-card>
      </n-grid-item>
      <n-grid-item :span="6">
        <n-card class="chart-card" title="部门分布" :class="{ 'is-dark': isDark }">
          <DepartmentChart ref="departmentChart" />
        </n-card>
      </n-grid-item>
    </n-grid>
  </div>
</template>

<script lang="ts" setup name="DashboardMain">
import { ref, computed, watch, onMounted } from 'vue'
import { useAppConfigStore } from '@/store/modules/app-config'
import {
  TrendingUpOutline,
  CalendarOutline,
  PeopleOutline,
  CartOutline,
  WalletOutline,
  EyeOutline
} from '@vicons/ionicons5'
import OrderChart from './components/chart/OrderChart.vue'
import SalesChart from './components/chart/SalesChart.vue'
import StudentChart from './components/chart/StudentChart.vue'
import EnrollmentChannelsChart from './components/chart/EnrollmentChannelsChart.vue'
import FullYearSalesChart from './components/chart/FullYearSalesChart.vue'
import DepartmentChart from './components/chart/DepartmentChart.vue'

// 组件实例
const appConfigStore = useAppConfigStore()
const mOrderChart = ref<InstanceType<typeof OrderChart>>()
const salesChart = ref<InstanceType<typeof SalesChart>>()
const enrollmentChannelsChart = ref<InstanceType<typeof EnrollmentChannelsChart>>()
const studentChart = ref<InstanceType<typeof StudentChart>>()
const fullYearSalesChart = ref<InstanceType<typeof FullYearSalesChart>>()
const departmentChart = ref<InstanceType<typeof DepartmentChart>>()

// 计算暗黑模式
const isDark = computed(() => appConfigStore.theme === 'dark')

// 数据卡片配置
const dataCards = [
  {
    title: '今日访问量',
    value: '1,234',
    total: '访问总量 100万+',
    type: 'primary',
    icon: EyeOutline,
    compare: {
      day: '+12%',
      week: '+25%'
    }
  },
  {
    title: '新增用户',
    value: '678',
    total: '用户总量 20万+',
    type: 'success',
    icon: PeopleOutline,
    compare: {
      day: '+8%',
      week: '+15%'
    }
  },
  {
    title: '月度销售额',
    value: '￥123,456',
    total: '总销售额 1000万+',
    type: 'warning',
    icon: WalletOutline,
    progress: 75
  },
  {
    title: '订单统计',
    value: '892',
    total: '订单总量 5万+',
    type: 'info',
    icon: CartOutline,
    progress: 65
  }
]

// 图表自适应
const updateCharts = () => {
  setTimeout(() => {
    mOrderChart.value?.updateChart()
    salesChart.value?.updateChart()
    enrollmentChannelsChart.value?.updateChart()
    studentChart.value?.updateChart()
    fullYearSalesChart.value?.updateChart()
    departmentChart.value?.updateChart()
  }, 200)
}

// 监听侧边栏折叠状态
watch(
  () => appConfigStore.isCollapse,
  () => {
    updateCharts()
  }
)

// 监听窗口大小变化
onMounted(() => {
  window.addEventListener('resize', updateCharts)
})
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 16px;
  background: var(--n-color);
  min-height: 100%;

  .data-card {
    transition: all 0.3s ease-in-out;
    height: 180px;
    overflow: hidden;

    &:hover {
      transform: translateY(-5px);
    }

    .card-content {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      height: 100%;
      padding: 4px 0;
    }

    .card-left {
      flex: 1;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      height: 100%;

      .card-title {
        font-size: 16px;
        color: var(--n-text-color-2);
        margin: 0 0 8px;
      }

      .card-value {
        font-size: 24px;
        font-weight: 600;
        color: var(--n-text-color);
        margin-bottom: 4px;
      }

      .card-total {
        font-size: 14px;
        color: var(--n-text-color-3);
        margin-bottom: 8px;
      }

      .card-compare {
        font-size: 13px;
        color: var(--n-text-color-3);
        margin-top: 4px;

        div {
          display: flex;
          align-items: center;
          margin-bottom: 2px;

          .n-icon {
            margin-right: 4px;
          }
        }
      }
    }

    .card-right {
      .n-icon {
        padding: 12px;
        border-radius: 12px;
        background: rgba(0, 0, 0, 0.04);

        &.primary {
          color: #2080f0;
        }
        &.success {
          color: #18a058;
        }
        &.warning {
          color: #f0a020;
        }
        &.info {
          color: #2080f0;
        }
      }
    }

    &.primary {
      border-left: 4px solid #2080f0;
    }
    &.success {
      border-left: 4px solid #18a058;
    }
    &.warning {
      border-left: 4px solid #f0a020;
    }
    &.info {
      border-left: 4px solid #2080f0;
    }
  }

  .chart-card {
    margin-top: 16px;
    transition: all 0.3s ease-in-out;

    &:hover {
      transform: translateY(-5px);
    }

    &.is-dark {
      background: rgba(24, 24, 28, 0.9);
    }
  }
}

// 响应式布局
@media screen and (max-width: 1400px) {
  .dashboard-container {
    .n-grid {
      --n-cols: 12 !important;

      .n-grid-item {
        --n-span: 6 !important;
      }
    }
  }
}

@media screen and (max-width: 768px) {
  .dashboard-container {
    .n-grid {
      .n-grid-item {
        --n-span: 12 !important;
      }
    }
  }
}
</style>
