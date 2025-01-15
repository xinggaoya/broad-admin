<template>
  <div ref="orderChartWrapper" style="height: 100%"></div>
</template>

<script lang="ts" setup name="OrderChart">
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { graphic } from 'echarts'
import useEcharts from '@/hooks/useEcharts'

// 组件状态
const loading = ref(true)
const orderChartWrapper = ref<HTMLDivElement | null>(null)
let chartInstance: ReturnType<typeof useEcharts> | null = null

// 初始化图表
const init = () => {
  const option = {
    tooltip: {
      trigger: 'item',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985'
        }
      }
    },
    grid: {
      x: '-5%',
      y: 0,
      x2: '-5%',
      y2: 0
    },
    xAxis: {
      type: 'category',
      splitLine: { show: false }
    },
    yAxis: [
      {
        type: 'value',
        splitLine: { show: false }
      }
    ],
    series: [
      {
        data: [82, 93, 90, 74, 82, 60, 50],
        type: 'line',
        smooth: true,
        lineStyle: {
          width: 0
        },
        showSymbol: false,
        areaStyle: {
          opacity: 0.8,
          color: new graphic.LinearGradient(0, 0, 0, 1, [
            {
              offset: 0,
              color: 'rgba(128, 255, 165)'
            },
            {
              offset: 1,
              color: 'rgba(1, 191, 236)'
            }
          ])
        }
      }
    ]
  }

  setTimeout(() => {
    loading.value = false
    nextTick(() => {
      if (orderChartWrapper.value) {
        chartInstance = useEcharts(orderChartWrapper.value)
        chartInstance.setOption(option)
      }
    })
  }, 100)
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
