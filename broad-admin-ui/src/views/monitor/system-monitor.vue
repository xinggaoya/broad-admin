<template>
  <section class="monitor-page">
    <header class="monitor-header">
      <div>
        <p class="header-subtitle">实时监控</p>
        <h2>系统运行状况</h2>
      </div>
      <div class="header-actions">
        <n-button tertiary round size="small" @click="fetchMonitorData" :disabled="autoRefresh">
          <template #icon>
            <n-icon><RefreshOutline /></n-icon>
          </template>
          手动刷新
        </n-button>
        <n-switch
          :value="autoRefresh"
          :round="false"
          size="large"
          @update:value="toggleAutoRefresh"
        >
          <template #checked>自动刷新</template>
          <template #unchecked>自动刷新</template>
        </n-switch>
      </div>
    </header>

    <section class="monitor-grid">
      <n-card class="panel" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div>
              <p class="panel-subtitle">基础信息</p>
              <h3>服务器信息</h3>
            </div>
          </div>
        </template>
        <n-descriptions :column="3" bordered>
          <n-descriptions-item label="服务器名称">{{
            serverInfo.computerName || '加载中...'
          }}</n-descriptions-item>
          <n-descriptions-item label="操作系统">{{
            serverInfo.osName || '加载中...'
          }}</n-descriptions-item>
          <n-descriptions-item label="系统架构">{{
            serverInfo.osArch || '加载中...'
          }}</n-descriptions-item>
          <n-descriptions-item label="IP 地址">{{
            serverInfo.hostIp || '加载中...'
          }}</n-descriptions-item>
          <n-descriptions-item label="JDK 版本">{{
            serverInfo.jdkVersion || '加载中...'
          }}</n-descriptions-item>
          <n-descriptions-item label="系统时区">{{
            serverInfo.timeZone || '加载中...'
          }}</n-descriptions-item>
        </n-descriptions>
      </n-card>

      <n-card class="panel" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div>
              <p class="panel-subtitle">资源占用</p>
              <h3>CPU</h3>
            </div>
          </div>
        </template>
        <div class="gauge-wrapper">
          <div ref="cpuGaugeRef" class="gauge-chart"></div>
          <div class="gauge-stats">
            <p class="gauge-title">CPU 使用率</p>
            <p class="gauge-value">{{ cpu.usageRate }}%</p>
            <ul>
              <li>核心数：{{ cpu.cpuNum }}</li>
              <li>用户：{{ cpu.used }}%</li>
              <li>系统：{{ cpu.sys }}%</li>
              <li>空闲：{{ cpu.free }}%</li>
            </ul>
          </div>
        </div>
      </n-card>

      <n-card class="panel" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div>
              <p class="panel-subtitle">资源占用</p>
              <h3>内存</h3>
            </div>
          </div>
        </template>
        <div class="gauge-wrapper">
          <div ref="memoryGaugeRef" class="gauge-chart"></div>
          <div class="gauge-stats">
            <p class="gauge-title">内存使用率</p>
            <p class="gauge-value">{{ memory.usageRate }}%</p>
            <ul>
              <li>总计：{{ formatSize(memory.total) }}</li>
              <li>已用：{{ formatSize(memory.used) }}</li>
              <li>剩余：{{ formatSize(memory.free) }}</li>
            </ul>
          </div>
        </div>
      </n-card>

      <n-card class="panel" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div>
              <p class="panel-subtitle">资源占用</p>
              <h3>JVM</h3>
            </div>
          </div>
        </template>
        <div class="gauge-wrapper">
          <div ref="jvmGaugeRef" class="gauge-chart"></div>
          <div class="gauge-stats">
            <p class="gauge-title">JVM 使用率</p>
            <p class="gauge-value">{{ jvm.usageRate }}%</p>
            <ul>
              <li>最大：{{ formatSize(jvm.max) }}</li>
              <li>已用：{{ formatSize(jvm.used) }}</li>
              <li>空闲：{{ formatSize(jvm.free) }}</li>
              <li>运行：{{ jvm.runTime }}</li>
            </ul>
          </div>
        </div>
      </n-card>

      <n-card class="panel" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div>
              <p class="panel-subtitle">磁盘阵列</p>
              <h3>磁盘信息</h3>
            </div>
          </div>
        </template>
        <div v-if="disk.disks && disk.disks.length" class="disk-grid">
          <div v-for="(item, index) in disk.disks" :key="index" class="disk-card">
            <div class="disk-card__head">
              <span>{{ item.dirName }}</span>
              <strong>{{ item.usage }}%</strong>
            </div>
            <n-progress
              type="line"
              :percentage="item.usage"
              :color="getDiskProgressColor(item.usage)"
              :height="10"
              :border-radius="6"
            />
            <div class="disk-card__meta">
              <span>总: {{ formatSize(item.total) }}</span>
              <span>已用: {{ formatSize(item.used) }}</span>
              <span>可用: {{ formatSize(item.free) }}</span>
            </div>
          </div>
        </div>
        <n-empty v-else description="暂无磁盘信息" />
      </n-card>

      <n-card class="panel panel--wide" :bordered="false">
        <template #header>
          <div class="panel-header">
            <div>
              <p class="panel-subtitle">可用性</p>
              <h3>服务状态监控</h3>
            </div>
          </div>
        </template>
        <n-spin :show="loadingServices">
          <n-data-table
            :columns="servicesColumns"
            :data="servicesList"
            :pagination="{ pageSize: 10 }"
            :bordered="false"
          />
        </n-spin>
      </n-card>
    </section>
  </section>
</template>

<script lang="ts" setup name="SystemMonitorView">
import { ref, reactive, onMounted, onBeforeUnmount, h, watch } from 'vue'
import { useMessage, NTag } from 'naive-ui'
import * as echarts from 'echarts/core'
import { GaugeChart } from 'echarts/charts'
import {
  TooltipComponent,
  LegendComponent,
  GridComponent,
  TitleComponent
} from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { RefreshOutline } from '@vicons/ionicons5'
import { getSystemInfo, getServiceStatus } from '@/api/monitor/system'
import { useIntervalFn } from '@vueuse/core'

// 注册必须的组件
echarts.use([
  GaugeChart,
  TooltipComponent,
  LegendComponent,
  GridComponent,
  TitleComponent,
  CanvasRenderer
])

const message = useMessage()

// 服务器信息
const serverInfo = reactive({
  computerName: '',
  osName: '',
  osArch: '',
  hostIp: '',
  jdkVersion: '',
  timeZone: ''
})

// CPU信息
const cpu = reactive({
  cpuNum: 0,
  usageRate: 0,
  free: 0,
  used: 0,
  sys: 0
})

// 内存信息
const memory = reactive({
  total: 0,
  used: 0,
  free: 0,
  usageRate: 0
})

// 磁盘信息
const disk = reactive({
  total: 0,
  free: 0,
  used: 0,
  usageRate: 0,
  disks: [] as any[]
})

// JVM信息
const jvm = reactive({
  max: 0,
  total: 0,
  used: 0,
  free: 0,
  version: '',
  usageRate: 0,
  name: '',
  startTime: '',
  runTime: ''
})

// 服务状态
const servicesList = ref<any[]>([])
const loadingServices = ref(false)

// 自动刷新
const autoRefresh = ref(true)
const refreshInterval = 5000
const { pause: pauseAutoRefresh, resume: resumeAutoRefresh } = useIntervalFn(
  () => {
    fetchMonitorData()
    fetchServiceStatus()
  },
  refreshInterval,
  { immediate: false }
)

// 图表引用
const cpuGaugeRef = ref<HTMLElement | null>(null)
const memoryGaugeRef = ref<HTMLElement | null>(null)
const jvmGaugeRef = ref<HTMLElement | null>(null)

// 图表实例
let cpuGaugeChart: echarts.ECharts | null = null
let memoryGaugeChart: echarts.ECharts | null = null
let jvmGaugeChart: echarts.ECharts | null = null

// 服务列表表格定义
const servicesColumns = [
  {
    title: '服务名称',
    key: 'serviceName',
    width: 200
  },
  {
    title: '服务地址',
    key: 'serviceAddress'
  },
  {
    title: '服务端口',
    key: 'servicePort',
    width: 100
  },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render(row: any) {
      return h(
        NTag,
        {
          type: row.status === 'UP' ? 'success' : 'error',
          round: true
        },
        { default: () => (row.status === 'UP' ? '正常' : '异常') }
      )
    }
  },
  {
    title: '最后检查时间',
    key: 'lastCheckTime',
    width: 180
  }
]

// 获取监控数据
const fetchMonitorData = async () => {
  try {
    const res = await getSystemInfo()
    if (res.code === 200) {
      const data = res.data

      // 更新服务器信息
      Object.assign(serverInfo, data.server || {})

      // 更新CPU信息
      Object.assign(cpu, data.cpu || {})

      // 更新内存信息
      Object.assign(memory, data.memory || {})

      // 更新磁盘信息
      Object.assign(disk, data.disk || {})

      // 更新JVM信息
      Object.assign(jvm, data.jvm || {})

      // 更新图表
      updateGaugeCharts()
    }
  } catch (error) {
    console.error('获取系统监控数据失败', error)
    message.error('获取监控数据失败')
  }
}

// 获取服务状态
const fetchServiceStatus = async () => {
  loadingServices.value = true
  try {
    const res = await getServiceStatus()
    if (res.code === 200) {
      servicesList.value = res.data || []
    }
  } catch (error) {
    console.error('获取服务状态失败', error)
    message.error('获取服务状态失败')
  } finally {
    loadingServices.value = false
  }
}

// 初始化图表
const initGaugeCharts = () => {
  // CPU图表
  if (cpuGaugeRef.value) {
    cpuGaugeChart = echarts.init(cpuGaugeRef.value)
    const cpuGaugeOption = getGaugeOption('CPU使用率', cpu.usageRate, '#2080f0')
    cpuGaugeChart.setOption(cpuGaugeOption)
  }

  // 内存图表
  if (memoryGaugeRef.value) {
    memoryGaugeChart = echarts.init(memoryGaugeRef.value)
    const memoryGaugeOption = getGaugeOption('内存使用率', memory.usageRate, '#18a058')
    memoryGaugeChart.setOption(memoryGaugeOption)
  }

  // JVM图表
  if (jvmGaugeRef.value) {
    jvmGaugeChart = echarts.init(jvmGaugeRef.value)
    const jvmGaugeOption = getGaugeOption('JVM使用率', jvm.usageRate, '#f0a020')
    jvmGaugeChart.setOption(jvmGaugeOption)
  }
}

// 更新图表
const updateGaugeCharts = () => {
  if (cpuGaugeChart) {
    cpuGaugeChart.setOption({
      series: [
        {
          data: [
            {
              value: cpu.usageRate,
              name: 'CPU使用率'
            }
          ]
        }
      ]
    })
  }

  if (memoryGaugeChart) {
    memoryGaugeChart.setOption({
      series: [
        {
          data: [
            {
              value: memory.usageRate,
              name: '内存使用率'
            }
          ]
        }
      ]
    })
  }

  if (jvmGaugeChart) {
    jvmGaugeChart.setOption({
      series: [
        {
          data: [
            {
              value: jvm.usageRate,
              name: 'JVM使用率'
            }
          ]
        }
      ]
    })
  }
}

// 获取仪表盘配置
const getGaugeOption = (name: string, value: number, color: string) => {
  return {
    series: [
      {
        type: 'gauge',
        radius: '90%',
        startAngle: 180,
        endAngle: 0,
        min: 0,
        max: 100,
        progress: {
          show: true,
          width: 14,
          itemStyle: {
            color
          }
        },
        axisLine: {
          lineStyle: {
            width: 14,
            color: [[1, 'rgba(148, 163, 184, 0.35)']]
          }
        },
        pointer: {
          show: false
        },
        axisTick: {
          show: false
        },
        splitLine: {
          show: false
        },
        axisLabel: {
          show: false
        },
        detail: {
          valueAnimation: true,
          formatter: '{value}%',
          color: color,
          fontSize: 28,
          offsetCenter: [0, 0]
        },
        data: [{ value, name }]
      }
    ]
  }
}

// 切换自动刷新
const toggleAutoRefresh = (value?: boolean) => {
  autoRefresh.value = typeof value === 'boolean' ? value : !autoRefresh.value
}

let refreshWatchInitialized = false
watch(
  () => autoRefresh.value,
  (val) => {
    if (val) {
      resumeAutoRefresh()
    } else {
      pauseAutoRefresh()
    }
    if (refreshWatchInitialized) {
      message[val ? 'success' : 'info'](val ? '已开启自动刷新' : '已关闭自动刷新')
    } else {
      refreshWatchInitialized = true
    }
  },
  { immediate: true }
)

// 格式化容量大小
const formatSize = (size: number) => {
  if (size === 0) return '0 B'

  const units = ['B', 'KB', 'MB', 'GB', 'TB', 'PB']
  const k = 1024
  const i = Math.floor(Math.log(size) / Math.log(k))

  return parseFloat((size / Math.pow(k, i)).toFixed(2)) + ' ' + units[i]
}

// 获取磁盘进度条颜色
const getDiskProgressColor = (usage: number) => {
  if (usage < 60) return '#18a058'
  if (usage < 80) return '#f0a020'
  return '#d03050'
}

// 组件挂载时初始化
onMounted(() => {
  fetchMonitorData()
  fetchServiceStatus()
  setTimeout(() => {
    initGaugeCharts()
  }, 100)
  window.addEventListener('resize', handleResize)
})

onBeforeUnmount(() => {
  pauseAutoRefresh()
  window.removeEventListener('resize', handleResize)
  cpuGaugeChart?.dispose()
  memoryGaugeChart?.dispose()
  jvmGaugeChart?.dispose()
})

const handleResize = () => {
  cpuGaugeChart?.resize()
  memoryGaugeChart?.resize()
  jvmGaugeChart?.resize()
}
</script>
<style scoped lang="scss">
.monitor-page {
  display: flex;
  flex-direction: column;
  gap: var(--shell-gap);
  padding-bottom: 24px;
}

.monitor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: var(--shell-surface);
  border-radius: var(--shell-radius-lg);
  padding: 20px;
  box-shadow: var(--shell-shadow);
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.monitor-header h2 {
  margin: 0;
  font-size: 24px;
}

.header-subtitle {
  margin: 0;
  font-size: 12px;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--shell-muted-text-color);
}

.monitor-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: var(--shell-gap);
}

.panel {
  border-radius: var(--shell-radius-lg);
  background: var(--shell-surface);
  box-shadow: var(--shell-shadow);
}

.panel--wide {
  grid-column: 1 / -1;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-subtitle {
  margin: 0;
  font-size: 12px;
  color: var(--shell-muted-text-color);
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

.gauge-wrapper {
  display: flex;
  gap: 24px;
  align-items: center;
}

.gauge-chart {
  width: 220px;
  height: 160px;
}

.gauge-stats {
  flex: 1;
}

.gauge-stats .gauge-title {
  margin: 0;
  font-size: 13px;
  color: var(--shell-muted-text-color);
}

.gauge-stats .gauge-value {
  margin: 4px 0 12px;
  font-size: 28px;
  font-weight: 600;
}

.gauge-stats ul {
  list-style: none;
  padding: 0;
  margin: 0;
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 6px;
  font-size: 13px;
  color: var(--shell-muted-text-color);
}

.disk-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 12px;
}

.disk-card {
  border: 1px solid var(--shell-border-color);
  border-radius: var(--shell-radius-base);
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.disk-card__head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: 600;
}

.disk-card__meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 12px;
  color: var(--shell-muted-text-color);
}

@media (max-width: 768px) {
  .monitor-header {
    flex-direction: column;
    gap: 12px;
  }

  .gauge-wrapper {
    flex-direction: column;
    align-items: flex-start;
  }

  .gauge-chart {
    width: 100%;
  }
}
</style>
