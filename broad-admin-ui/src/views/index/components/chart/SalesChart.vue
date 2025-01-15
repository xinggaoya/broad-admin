<template>
  <n-card
    :content-style="{ padding: '10px' }"
    :header-style="{ padding: '10px' }"
    :segmented="true"
  >
    <template #header>
      <n-skeleton text style="width: 50%" v-if="loading" />
      <template v-else>
        <div class="text-sm">一周销售额（单位：万）</div>
      </template>
    </template>
    <div class="chart-item-container">
      <n-skeleton text v-if="loading" :repeat="4" />
      <template v-else>
        <div ref="salesChart" class="chart-item"></div>
      </template>
    </div>
  </n-card>
</template>

<script lang="ts" setup name="SalesChart">
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { graphic } from 'echarts'
import useEcharts from '@/hooks/useEcharts'

// 组件状态
const loading = ref(true)
const salesChart = ref<HTMLDivElement | null>(null)
let chartInstance: ReturnType<typeof useEcharts> | null = null

// 初始化图表
const init = () => {
  const option = {
    grid: {
      left: '2%',
      right: '5%',
      top: '5%',
      bottom: '3%',
      containLabel: true
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周天'],
      boundaryGap: false,
      splitLine: { show: false }
    },
    yAxis: {
      type: 'value',
      boundaryGap: false,
      splitLine: { show: false }
    },
    series: [
      {
        data: [150, 180, 224, 218, 200, 180, 150],
        type: 'line',
        smooth: true,
        showSymbol: false,
        lineStyle: {
          width: 0
        },
        areaStyle: {
          opacity: 0.8,
          color: new graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: 'rgba(55, 162, 255)'
            },
            {
              offset: 1,
              color: 'rgba(116, 21, 219)'
            }
          ])
        }
      }
    ]
  }

  setTimeout(() => {
    loading.value = false
    nextTick(() => {
      if (salesChart.value) {
        chartInstance = useEcharts(salesChart.value)
        chartInstance.setOption(option)
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
    height: 180px;
  }
}
</style>
