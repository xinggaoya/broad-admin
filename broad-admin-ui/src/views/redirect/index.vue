<template>
  <section class="redirect-page">
    <n-spin size="large" :show="true">
      <div class="redirect-content">
        <p>正在为您跳转至目标页面...</p>
        <small>若长时间无响应，请检查网络或重新登录</small>
      </div>
    </n-spin>
  </section>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

onMounted(() => {
  const { params, query } = route
  const target = params.path
  const targetPath = Array.isArray(target) ? target.join('/') : (target as string) || ''
  router.replace({
    path: `/${targetPath}`,
    query
  })
})
</script>

<style scoped lang="scss">
.redirect-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;

  .redirect-content {
    margin-top: 16px;
    text-align: center;
    color: var(--text-color-2);

    small {
      display: block;
      margin-top: 4px;
      color: var(--text-color-3);
    }
  }
}
</style>
