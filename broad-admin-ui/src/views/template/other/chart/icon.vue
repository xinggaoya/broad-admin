<template>
  <n-card :content-style="{ padding: '10px' }">
    <n-tabs :default-value="activeName">
      <n-tab-pane tab="icon-font" name="iconfont">
        <IconFont />
      </n-tab-pane>
      <n-tab-pane tab="xicons" name="xicons">
        <XIcons />
      </n-tab-pane>
    </n-tabs>
  </n-card>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import IconFont from './components/IconFont.vue'
import XIcons from './components/xicons.vue'

const activeName = ref('iconfont')

</script>
