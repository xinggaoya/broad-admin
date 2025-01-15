<template>
  <n-card
    :content-style="{ padding: '10px' }"
    :header-style="{ padding: '10px' }"
    :segmented="true"
  >
    <template #header>
      <n-skeleton text style="width: 50%" v-if="loading" />
      <template v-else>
        <div class="text-sm">招生渠道分析图</div>
      </template>
    </template>
    <div class="chart-item-container">
      <n-skeleton text v-if="loading" :repeat="4" />
      <template v-else>
        <div ref="channelsChart" class="chart-item"></div>
      </template>
    </div>
  </n-card>
</template>

<script lang="ts" setup name="EnrollmentChannelsChart">
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import useEcharts from '@/hooks/useEcharts'
import type { EChartsOption } from 'echarts'

// 组件状态
const loading = ref(true)
const channelsChart = ref<HTMLDivElement | null>(null)
let chartInstance: ReturnType<typeof useEcharts> | null = null

// 初始化图表
const init = () => {
  const option: EChartsOption = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      right: 10,
      top: 'center'
    },
    series: [
      {
        name: '访问来源',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '16',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: true,
          length: 5,
          length2: 5,
          smooth: true
        },
        data: [
          { value: 1969, name: '线上' },
          { value: 743, name: '互推' },
          { value: 1594, name: '电话' },
          { value: 1347, name: '地推' },
          { value: 635, name: '直播' }
        ]
      }
    ]
  }

  // 使用 requestAnimationFrame 确保 DOM 已经渲染
  requestAnimationFrame(() => {
    loading.value = false
    nextTick(() => {
      if (channelsChart.value && document.contains(channelsChart.value)) {
        chartInstance = useEcharts(channelsChart.value)
        chartInstance.setOption(option)
      }
    })
  })
}

// 更新图表尺寸
const updateChart = () => {
  if (chartInstance && channelsChart.value && document.contains(channelsChart.value)) {
    chartInstance.resize()
  }
}

// 生命周期钩子
onMounted(() => {
  init()
})

onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
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
