<template>
  <n-card class="footer-container"> Copyright © {{ projectName }} 2022 Made by XingGao</n-card>
</template>

<script lang="ts" setup>
import { projectName } from '@/setting'
</script>

<style lang="scss" scoped>
.footer-container {
  height: $footerHeight;
  display: flex;
  justify-content: center;
  align-items: center;
  box-sizing: border-box;
  border-top: 1px dashed var(--border-color);
}
</style>
