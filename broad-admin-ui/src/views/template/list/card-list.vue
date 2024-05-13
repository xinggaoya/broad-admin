<template>
  <n-card>
    <n-space vertical>
      <div>
        <n-h2 prefix="bar" align-text>
          <n-text type="primary"> v-auth 指令 </n-text>
        </n-h2>
        <div>
          <n-text type="secondary" v-auth="['sys:user:list']">
            1. v-auth指令用于控制元素的显示与隐藏，当前用户拥有用户管理的权限，这段文字才会显示。
          </n-text>
          <div
            >2. 有人就问了那表格渲染的按钮怎么办？那也很简单，例如我们只需要设置{{
              JSON.stringify({ auth: ['sys:user:list'] })
            }}</div
          >
        </div>
      </div>
      <div>
        <n-h2 prefix="bar" align-text>
          <n-text type="primary"> v-loading 指令 </n-text>
        </n-h2>
        <p>
          <n-text type="secondary" v-auth="['sys:user:list']">
            1. v-loading指令用于控制元素加载状态，例如我们需要在按钮上显示加载状态;
          </n-text>
        </p>
      </div>
      <n-button type="primary" @click="loading = !loading">{{ !loading ? '变' : '再变' }}</n-button>
      <div style="width: 400px; height: 100px; background: #7a9ad7" v-loading="loading"> </div>
    </n-space>
  </n-card>
</template>

<script lang="ts" setup>
  import { ref } from 'vue'

  const loading = ref(false)
</script>

<style lang="scss" scoped></style>
