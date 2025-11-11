<template>
  <section class="exception-page">
    <div class="illustration">
      <img :src="statusImage" :alt="statusInfo.title" />
    </div>
    <div class="exception-content">
      <p class="status-code">{{ statusInfo.code }}</p>
      <h2>{{ statusInfo.title }}</h2>
      <p class="description">{{ statusInfo.description }}</p>
      <div class="actions">
        <n-button type="primary" round @click="backHome">返回首页</n-button>
        <n-button tertiary round @click="reloadPage">刷新页面</n-button>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import image404 from '@/assets/error/img_404.png'
import image403 from '@/assets/error/img_403.png'
import image500 from '@/assets/error/img_500.png'

interface ExceptionInfo {
  code: string
  title: string
  description: string
  image: string
}

const props = defineProps<{
  status: string | number
}>()

const router = useRouter()

const statusMap: Record<string, ExceptionInfo> = {
  '403': {
    code: '403',
    title: '抱歉，您没有访问权限',
    description: '当前账号暂无访问该资源的权限，请联系管理员开通或切换账号后再试。',
    image: image403
  },
  '404': {
    code: '404',
    title: '页面走丢了',
    description: '可能是链接过期、地址输入错误或页面已被删除，请检查后重新访问。',
    image: image404
  },
  '500': {
    code: '500',
    title: '服务器开小差了',
    description: '服务暂时不可用，我们正在紧急修复中，请稍后再试或联系维护人员。',
    image: image500
  }
}

const statusInfo = computed(() => {
  const key = String(props.status)
  return statusMap[key] || statusMap['404']
})

const statusImage = computed(() => statusInfo.value.image)

const backHome = () => {
  router.replace({ path: '/' })
}

const reloadPage = () => {
  window.location.reload()
}
</script>

<style scoped lang="scss">
.exception-page {
  min-height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  text-align: center;
  gap: 40px;
  padding: 48px 16px;

  .illustration {
    max-width: 420px;

    img {
      width: 100%;
      object-fit: contain;
    }
  }

  .exception-content {
    max-width: 420px;

    .status-code {
      font-size: 56px;
      font-weight: 700;
      color: var(--primary-color);
      margin: 0 0 12px;
    }

    h2 {
      font-size: 28px;
      margin-bottom: 12px;
    }

    .description {
      color: var(--text-color-3);
      margin-bottom: 24px;
      line-height: 1.6;
    }

    .actions {
      display: flex;
      justify-content: center;
      gap: 12px;
      flex-wrap: wrap;
    }
  }
}
</style>
