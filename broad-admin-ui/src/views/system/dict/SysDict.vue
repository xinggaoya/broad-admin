<template>
  <div class="main-container">
    <n-card :content-style="{ padding: '10px' }" class="table-body-container">
      <n-tabs animated default-value="0" type="line">
        <n-tab-pane name="0" tab="类型列表">
          <DicType />
        </n-tab-pane>
        <n-tab-pane name="1" tab="字典列表">
          <DicData />
        </n-tab-pane>
      </n-tabs>
    </n-card>
  </div>
</template>

<script lang="ts" setup>
import DicType from './dictType.vue'
import DicData from './dictData.vue'
</script>
