<template>
  <n-card
    :content-style="{ padding: '10px' }"
    :header-style="{ padding: '10px' }"
    :segmented="true"
  >
    <template #header>
      <n-skeleton text style="width: 50%" v-if="loading" />
      <template v-else>
        <div class="text-sm">半年招生对比图（单位：人）</div>
      </template>
    </template>
    <div class="chart-item-container">
      <n-skeleton text v-if="loading" :repeat="4" />
      <template v-else>
        <div ref="studentChart" class="chart-item"></div>
      </template>
    </div>
  </n-card>
</template>

<script lang="ts" setup name="StudentChart">
import { ref, onMounted, onBeforeUnmount, nextTick } from 'vue'
import { graphic } from 'echarts'
import type { EChartsOption } from 'echarts'
import useEcharts from '@/hooks/useEcharts'

// 组件状态
const loading = ref(true)
const studentChart = ref<HTMLDivElement | null>(null)
let chartInstance: ReturnType<typeof useEcharts> | null = null

// 初始化图表
const init = () => {
  const option: EChartsOption = {
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
    yAxis: {
      type: 'category',
      data: ['一月', '二月', '三月', '四月', '五月', '六月'],
      boundaryGap: false,
      axisTick: {
        show: false
      }
    },
    xAxis: {
      type: 'value',
      splitLine: {
        show: false
      }
    },
    series: [
      {
        data: [480, 289, 711, 618, 393, 571],
        type: 'bar',
        barWidth: 'auto',
        itemStyle: {
          borderRadius: [0, 15, 15, 0],
          color: new graphic.LinearGradient(1, 0, 0, 1, [
            {
              offset: 0,
              color: 'rgba(12, 124, 182)'
            },
            {
              offset: 1,
              color: 'rgba(244, 187, 236)'
            }
          ])
        }
      }
    ]
  }

  // 使用 requestAnimationFrame 确保 DOM 已经渲染
  requestAnimationFrame(() => {
    loading.value = false
    nextTick(() => {
      if (studentChart.value && document.contains(studentChart.value)) {
        chartInstance = useEcharts(studentChart.value)
        chartInstance.setOption(option)
      }
    })
  })
}

// 更新图表尺寸
const updateChart = () => {
  if (chartInstance && studentChart.value && document.contains(studentChart.value)) {
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
