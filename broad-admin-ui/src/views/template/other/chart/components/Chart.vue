<template>
  <div ref="eChartContainer" style="height: 180px; width: 100%"></div>
</template>

<script lang="ts">
import useEcharts from '@/hooks/useEcharts'
import { onMounted, ref, toRef } from 'vue'
import type { EChartsCoreOption } from 'echarts'

const prop = defineProps({
  id: {
    type: Number,
    default: 0
  },
  options: {
    type: Object,
    default: () => ({})
  }
})

const eChartContainer = ref<HTMLDivElement | null>(null)
onMounted(() => {
  const options = toRef(prop, 'options')
  const instance = useEcharts(eChartContainer.value as HTMLDivElement)
  instance.setOption(options.value as EChartsCoreOption)
  instance.resize()
})

</script>

<style></style>
