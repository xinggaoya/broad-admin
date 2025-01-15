<template>
  <n-card
    :content-style="{ padding: '10px' }"
    :header-style="{ padding: '10px' }"
    :segmented="true"
  >
    <template #header>
      <n-skeleton text style="width: 50%" v-if="loading" />
      <template v-else>
        <div class="text-sm">半年销售额分析图（数据为模拟，只为演示效果）</div>
      </template>
    </template>
    <div class="chart-item-container">
      <n-skeleton text v-if="loading" :repeat="8" />
      <template v-else>
        <div ref="fullYearSalesChart" class="chart-item"></div>
      </template>
    </div>
  </n-card>
</template>

<script lang="ts" setup name="FullYearSalesChart">
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { graphic } from 'echarts'
import useEcharts from '@/hooks/useEcharts'

// 生成随机数据
function getData() {
  const data: number[] = []
  while (data.length < 6) {
    data.push(Math.floor(Math.random() * 200 + 100))
  }
  return data
}

const months = ['一月', '二月', '三月', '四月', '五月', '六月']

// 组件状态
const loading = ref(true)
const fullYearSalesChart = ref<HTMLDivElement | null>(null)
let chartInstance: ReturnType<typeof useEcharts> | null = null
let updateInterval: number | null = null

// 初始化图表
const init = () => {
  const option = {
    color: ['rgba(64, 58, 255)', 'rgba(136, 188, 241)'],
    grid: {
      top: '10%',
      left: '2%',
      right: '2%',
      bottom: '5%',
      containLabel: true
    },
    legend: {
      data: ['2023半年销售额', '2024半年销售额']
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: months,
      boundaryGap: false
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        type: 'line',
        name: '2023半年销售额',
        stack: '总量',
        data: getData(),
        smooth: true,
        lineStyle: {
          color: 'rgba(24, 160, 88, 0.5)'
        },
        label: {
          show: true,
          formatter(val: any) {
            return val.dataIndex === 0 ? '' : val.data
          }
        },
        areaStyle: {
          opacity: 0.8,
          color: new graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: 'rgba(85, 193, 250, 0.1)'
            },
            {
              offset: 1,
              color: 'rgba(156, 21, 214, 0.2)'
            }
          ])
        }
      },
      {
        type: 'line',
        name: '2024半年销售额',
        stack: '总量',
        data: getData(),
        smooth: true,
        lineStyle: {
          color: 'rgba(24, 160, 88, 0.5)'
        },
        label: {
          show: true,
          formatter(val: any) {
            return val.dataIndex === 0 ? '' : val.data
          }
        },
        areaStyle: {
          opacity: 0.8,
          color: new graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: 'rgba(132, 248, 187, 0.1)'
            },
            {
              offset: 1,
              color: 'rgba(51, 209, 125, 0.2)'
            }
          ])
        }
      }
    ]
  }

  setTimeout(() => {
    loading.value = false
    nextTick(() => {
      if (fullYearSalesChart.value) {
        chartInstance = useEcharts(fullYearSalesChart.value)
        chartInstance.setOption(option)

        // 定时更新数据
        updateInterval = window.setInterval(() => {
          chartInstance?.setOption({
            series: [{ data: getData() }, { data: getData() }]
          })
        }, 5000)
      }
    })
  }, 1000)
}

// 更新图表尺寸
const updateChart = () => {
  chartInstance?.resize()
}

// 生命周期钩子
onMounted(() => {
  init()
})

onBeforeUnmount(() => {
  if (updateInterval) {
    clearInterval(updateInterval)
    updateInterval = null
  }
  chartInstance?.dispose()
  chartInstance = null
})

// 导出方法供父组件调用
defineExpose({
  updateChart
})
</script>

<style lang="scss" scoped>
.chart-item-container {
  width: 100%;
  .chart-item {
    height: 345px;
  }
}
</style>
