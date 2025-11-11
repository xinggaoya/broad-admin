<template>
  <section class="project-info-page">
    <header class="page-header">
      <div>
        <p class="header-subtitle">项目概览</p>
        <h2>Broad Admin 前端</h2>
        <p class="header-desc">基于 Vue 3 + Naive UI 打造的现代化管理后台，持续迭代以保持最佳体验。</p>
      </div>
      <div class="version-card">
        <span>当前版本</span>
        <strong>{{ version || '--' }}</strong>
        <n-button text type="primary" size="small" :loading="copying" @click="handleCopyVersion">
          <template #icon>
            <n-icon><CopyOutline /></n-icon>
          </template>
          复制版本号
        </n-button>
      </div>
    </header>

    <div class="info-grid">
      <n-card class="info-card" :bordered="false">
        <template #header>
          <div class="card-title">
            <div>
              <p>运行依赖</p>
              <h3>核心技术栈</h3>
            </div>
            <n-tag round type="success">{{ runtimeDeps.length }} 项</n-tag>
          </div>
        </template>
        <div class="dependency-grid">
          <div class="dep-item" v-for="item in runtimeDeps" :key="item.label">
            <span>{{ item.label }}</span>
            <code>{{ item.value }}</code>
          </div>
        </div>
      </n-card>

      <n-card class="info-card" :bordered="false">
        <template #header>
          <div class="card-title">
            <div>
              <p>开发依赖</p>
              <h3>工程工具链</h3>
            </div>
            <n-tag round type="info">{{ devDeps.length }} 项</n-tag>
          </div>
        </template>
        <div class="dependency-grid">
          <div class="dep-item" v-for="item in devDeps" :key="item.label">
            <span>{{ item.label }}</span>
            <code>{{ item.value }}</code>
          </div>
        </div>
      </n-card>
    </div>
  </section>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue'
import { useMessage } from 'naive-ui'
import { CopyOutline } from '@vicons/ionicons5'
import useAppInfo from '@/hooks/useAppInfo'

const { version, dependencies, devDependencies } = useAppInfo()
const message = useMessage()
const copying = ref(false)

const runtimeDeps = computed(() => {
  return Object.entries(dependencies.value || {}).map(([label, value]) => ({
    label,
    value
  }))
})

const devDeps = computed(() => {
  return Object.entries(devDependencies.value || {}).map(([label, value]) => ({
    label,
    value
  }))
})

const handleCopyVersion = async () => {
  if (!version.value || copying.value) return
  copying.value = true
  try {
    await navigator.clipboard.writeText(version.value)
    message.success('版本号已复制')
  } catch (error) {
    console.error('复制版本失败', error)
    message.error('复制版本号失败')
  } finally {
    copying.value = false
  }
}
</script>

<style lang="scss" scoped>
.project-info-page {
  display: flex;
  flex-direction: column;
  gap: 16px;

  .page-header {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    gap: 16px;
    padding: 24px;
    border-radius: 16px;
    background: linear-gradient(135deg, #f5f7ff, #ffffff);
    border: 1px solid rgba(82, 106, 255, 0.12);

    .header-subtitle {
      font-size: 13px;
      color: var(--text-color-3);
      margin-bottom: 4px;
    }

    .header-desc {
      margin-top: 4px;
      color: var(--text-color-2);
      max-width: 520px;
    }
  }

  .version-card {
    min-width: 200px;
    padding: 16px;
    border-radius: 12px;
    background: var(--card-color);
    border: 1px solid var(--divider-color);
    display: flex;
    flex-direction: column;
    gap: 8px;

    span {
      font-size: 12px;
      color: var(--text-color-3);
    }

    strong {
      font-size: 32px;
      line-height: 1;
    }
  }

  .info-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
    gap: 16px;
  }

  .info-card {
    min-height: 320px;

    .card-title {
      display: flex;
      align-items: center;
      justify-content: space-between;

      p {
        margin: 0;
        font-size: 12px;
        color: var(--text-color-3);
      }

      h3 {
        margin: 4px 0 0;
        font-size: 18px;
      }
    }
  }

  .dependency-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(180px, 1fr));
    gap: 12px;
    margin-top: 12px;
  }

  .dep-item {
    padding: 12px;
    border-radius: 10px;
    background: var(--card-color);
    border: 1px solid var(--divider-color);
    display: flex;
    flex-direction: column;
    gap: 6px;

    span {
      font-weight: 500;
      color: var(--text-color);
    }

    code {
      font-family: var(--mono-font, 'JetBrains Mono', monospace);
      font-size: 13px;
      color: var(--text-color-3);
    }
  }
}
</style>
