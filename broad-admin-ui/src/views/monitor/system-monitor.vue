<template>
  <div class="system-monitor-container">
    <n-grid :cols="24" :x-gap="16" :y-gap="16">
      <!-- 操作按钮区 -->
      <n-grid-item :span="24">
        <div class="action-buttons">
          <n-space>
            <n-button
              type="primary"
              :color="autoRefresh ? '#18a058' : ''"
              @click="toggleAutoRefresh"
            >
              <template #icon>
                <n-icon>
                  <RefreshCircleOutline />
                </n-icon>
              </template>
              {{ autoRefresh ? '停止自动刷新' : '开始自动刷新' }}
            </n-button>
            <n-button v-if="!autoRefresh" @click="fetchMonitorData">
              <template #icon>
                <n-icon>
                  <RefreshOutline />
                </n-icon>
              </template>
              手动刷新
            </n-button>
            <n-text v-if="autoRefresh" depth="3">每2秒自动刷新一次</n-text>
          </n-space>
        </div>
      </n-grid-item>

      <!-- 服务器信息卡片 -->
      <n-grid-item :span="24">
        <n-card title="服务器信息" class="server-info-card">
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
            <n-descriptions-item label="IP地址">{{
              serverInfo.hostIp || '加载中...'
            }}</n-descriptions-item>
            <n-descriptions-item label="JDK版本">{{
              serverInfo.jdkVersion || '加载中...'
            }}</n-descriptions-item>
            <n-descriptions-item label="系统时区">{{
              serverInfo.timeZone || '加载中...'
            }}</n-descriptions-item>
          </n-descriptions>
        </n-card>
      </n-grid-item>

      <!-- CPU信息卡片 -->
      <n-grid-item :span="12">
        <n-card title="CPU信息" class="resource-card">
          <div class="gauge-container">
            <div class="gauge-wrapper">
              <div ref="cpuGaugeRef" class="gauge-chart"></div>
              <div class="gauge-text">
                <h4>CPU使用率</h4>
                <p>{{ cpu.usageRate }}%</p>
              </div>
            </div>
          </div>
          <n-descriptions bordered>
            <n-descriptions-item label="核心数">{{ cpu.cpuNum }}</n-descriptions-item>
            <n-descriptions-item label="用户使用率">{{ cpu.used }}%</n-descriptions-item>
            <n-descriptions-item label="系统使用率">{{ cpu.sys }}%</n-descriptions-item>
            <n-descriptions-item label="当前空闲率">{{ cpu.free }}%</n-descriptions-item>
          </n-descriptions>
        </n-card>
      </n-grid-item>

      <!-- 内存信息卡片 -->
      <n-grid-item :span="12">
        <n-card title="内存信息" class="resource-card">
          <div class="gauge-container">
            <div class="gauge-wrapper">
              <div ref="memoryGaugeRef" class="gauge-chart"></div>
              <div class="gauge-text">
                <h4>内存使用率</h4>
                <p>{{ memory.usageRate }}%</p>
              </div>
            </div>
          </div>
          <n-descriptions bordered>
            <n-descriptions-item label="总内存">{{ formatSize(memory.total) }}</n-descriptions-item>
            <n-descriptions-item label="已用内存">{{
              formatSize(memory.used)
            }}</n-descriptions-item>
            <n-descriptions-item label="剩余内存">{{
              formatSize(memory.free)
            }}</n-descriptions-item>
          </n-descriptions>
        </n-card>
      </n-grid-item>

      <!-- 磁盘信息卡片 -->
      <n-grid-item :span="12">
        <n-card title="磁盘信息" class="resource-card">
          <div v-if="disk.disks && disk.disks.length > 0">
            <div v-for="(item, index) in disk.disks" :key="index" class="disk-item">
              <div class="disk-info">
                <span class="disk-name">{{ item.dirName }}</span>
                <span class="disk-usage">{{ item.usage }}%</span>
              </div>
              <n-progress
                type="line"
                :percentage="item.usage"
                :color="getDiskProgressColor(item.usage)"
                :height="12"
                :border-radius="6"
              />
              <div class="disk-detail">
                <span>总容量: {{ formatSize(item.total) }}</span>
                <span>已用: {{ formatSize(item.used) }}</span>
                <span>可用: {{ formatSize(item.free) }}</span>
              </div>
            </div>
          </div>
          <n-empty v-else description="暂无磁盘信息" />
        </n-card>
      </n-grid-item>

      <!-- JVM信息卡片 -->
      <n-grid-item :span="12">
        <n-card title="JVM信息" class="resource-card">
          <div class="gauge-container">
            <div class="gauge-wrapper">
              <div ref="jvmGaugeRef" class="gauge-chart"></div>
              <div class="gauge-text">
                <h4>JVM内存使用率</h4>
                <p>{{ jvm.usageRate }}%</p>
              </div>
            </div>
          </div>
          <n-descriptions bordered>
            <n-descriptions-item label="JVM最大内存">{{ formatSize(jvm.max) }}</n-descriptions-item>
            <n-descriptions-item label="JVM已用内存">{{
              formatSize(jvm.used)
            }}</n-descriptions-item>
            <n-descriptions-item label="JVM空闲内存">{{
              formatSize(jvm.free)
            }}</n-descriptions-item>
            <n-descriptions-item label="JVM版本">{{ jvm.version }}</n-descriptions-item>
            <n-descriptions-item label="JVM开始时间">{{ jvm.startTime }}</n-descriptions-item>
            <n-descriptions-item label="JVM运行时间">{{ jvm.runTime }}</n-descriptions-item>
          </n-descriptions>
        </n-card>
      </n-grid-item>

      <!-- 服务状态卡片 -->
      <n-grid-item :span="24">
        <n-card title="服务状态监控" class="services-card">
          <n-spin :show="loadingServices">
            <n-data-table
              :columns="servicesColumns"
              :data="servicesList"
              :pagination="{ pageSize: 10 }"
              :bordered="true"
            />
          </n-spin>
        </n-card>
      </n-grid-item>
    </n-grid>

    <!-- 自动刷新选项 -->
    <div class="refresh-options">
      <n-tooltip trigger="hover" placement="left">
        <template #trigger>
          <n-button
            quaternary
            circle
            :type="autoRefresh ? 'primary' : undefined"
            @click="toggleAutoRefresh"
          >
            <template #icon>
              <n-icon>
                <RefreshCircleOutline />
              </n-icon>
            </template>
          </n-button>
        </template>
        {{ autoRefresh ? '停止自动刷新' : '开始自动刷新' }}
      </n-tooltip>
      <n-tooltip v-if="!autoRefresh" trigger="hover" placement="left">
        <template #trigger>
          <n-button quaternary circle @click="fetchMonitorData">
            <template #icon>
              <n-icon>
                <RefreshOutline />
              </n-icon>
            </template>
          </n-button>
        </template>
        手动刷新
      </n-tooltip>
    </div>
  </div>
</template>

<script lang="ts" setup name="SystemMonitorView">
import { ref, reactive, onMounted, onBeforeUnmount, h } from 'vue'
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
import { RefreshOutline, RefreshCircleOutline } from '@vicons/ionicons5'
import { getSystemInfo, getServiceStatus } from '@/api/monitor/system'

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
let refreshTimer: any = null
const refreshInterval = 5000 // 修改为5秒刷新一次

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
        radius: '100%',
        startAngle: 180,
        endAngle: 0,
        min: 0,
        max: 100,
        splitNumber: 10,
        axisLine: {
          lineStyle: {
            width: 8,
            color: [
              [0.3, '#67e0e3'],
              [0.7, '#37a2da'],
              [1, '#fd666d']
            ]
          }
        },
        pointer: {
          icon: 'path://M12.8,0.7l12,40.1H0.7L12.8,0.7z',
          length: '12%',
          width: 8,
          offsetCenter: [0, '-60%'],
          itemStyle: {
            color: 'auto'
          }
        },
        axisTick: {
          length: 12,
          lineStyle: {
            color: 'auto',
            width: 2
          }
        },
        splitLine: {
          length: 20,
          lineStyle: {
            color: 'auto',
            width: 5
          }
        },
        axisLabel: {
          color: '#999',
          fontSize: 12,
          distance: -60,
          formatter: function (value: number) {
            if (value === 0 || value === 100) {
              return value + '%'
            }
            return ''
          }
        },
        title: {
          show: false
        },
        detail: {
          valueAnimation: true,
          formatter: '{value}%',
          color: 'auto',
          fontSize: 24,
          fontWeight: 'bold',
          offsetCenter: [0, '30%']
        },
        data: [
          {
            value: value,
            name: ''
          }
        ]
      }
    ]
  }
}

// 切换自动刷新
const toggleAutoRefresh = () => {
  autoRefresh.value = !autoRefresh.value
  if (autoRefresh.value) {
    message.success('已开启自动刷新，每2秒刷新一次')
    refreshTimer = setInterval(() => {
      fetchMonitorData()
      fetchServiceStatus()
    }, refreshInterval)
  } else {
    message.info('已关闭自动刷新')
    if (refreshTimer) {
      clearInterval(refreshTimer)
      refreshTimer = null
    }
  }
}

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

  // 自动开启定时刷新
  refreshTimer = setInterval(() => {
    fetchMonitorData()
    fetchServiceStatus()
  }, refreshInterval)

  // 窗口大小变化时重新调整图表大小
  window.addEventListener('resize', handleResize)
})

// 组件卸载前清理
onBeforeUnmount(() => {
  if (refreshTimer) {
    clearInterval(refreshTimer)
    refreshTimer = null
  }

  window.removeEventListener('resize', handleResize)

  if (cpuGaugeChart) {
    cpuGaugeChart.dispose()
    cpuGaugeChart = null
  }

  if (memoryGaugeChart) {
    memoryGaugeChart.dispose()
    memoryGaugeChart = null
  }

  if (jvmGaugeChart) {
    jvmGaugeChart.dispose()
    jvmGaugeChart = null
  }
})

// 调整图表大小
const handleResize = () => {
  cpuGaugeChart?.resize()
  memoryGaugeChart?.resize()
  jvmGaugeChart?.resize()
}
</script>

<style lang="scss" scoped>
.system-monitor-container {
  padding: 16px;
  background-color: var(--body-color);
  min-height: 100%;
  position: relative;

  .action-buttons {
    margin-bottom: 16px;
    padding: 12px;
    border-radius: 8px;
    background-color: var(--card-color);
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  }

  .server-info-card,
  .resource-card,
  .services-card {
    margin-bottom: 16px;
  }

  .gauge-container {
    display: flex;
    justify-content: center;
    margin-bottom: 16px;

    .gauge-wrapper {
      position: relative;
      width: 200px;
      height: 150px;

      .gauge-chart {
        width: 100%;
        height: 100%;
      }

      .gauge-text {
        position: absolute;
        top: 40%;
        left: 50%;
        transform: translate(-50%, -50%);
        text-align: center;

        h4 {
          font-size: 14px;
          margin: 0;
          color: var(--text-color-2);
        }

        p {
          font-size: 22px;
          font-weight: bold;
          margin: 0;
          color: var(--text-color-1);
        }
      }
    }
  }

  .disk-item {
    margin-bottom: 16px;

    &:last-child {
      margin-bottom: 0;
    }

    .disk-info {
      display: flex;
      justify-content: space-between;
      margin-bottom: 5px;

      .disk-name {
        font-weight: 500;
        color: var(--text-color-1);
      }

      .disk-usage {
        font-weight: 600;
      }
    }

    .disk-detail {
      display: flex;
      justify-content: space-between;
      margin-top: 8px;
      font-size: 12px;
      color: var(--text-color-3);
    }
  }

  .refresh-options {
    display: none; /* 隐藏原来的固钉按钮 */
  }
}
</style>
