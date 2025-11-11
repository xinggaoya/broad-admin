<template>
  <section class="login-shell" :class="{ dark: isDarkMode }">
    <div class="login-hero">
      <div class="hero-content">
        <p class="hero-eyebrow">Broad Admin · Digital Workspace</p>
        <h1>更轻盈的管理体验，从这里开始。</h1>
        <p class="hero-desc">
          使用统一设计语言与实时权限体系，快速构建企业业务中台。多端联动、实时审计，助力团队高效协作。
        </p>
        <div class="hero-metrics">
          <div class="metric">
            <strong>120+</strong>
            <span>预置组件</span>
          </div>
          <div class="metric">
            <strong>2.2.x</strong>
            <span>持续迭代</span>
          </div>
        </div>
      </div>
    </div>

    <div class="login-panel">
      <n-card class="login-card" :bordered="false">
        <div class="theme-switch">
          <n-switch @update-value="darkModeSwitchEvent">
            <template #checked-icon>
              <n-icon><SunnyOutline /></n-icon>
            </template>
            <template #unchecked-icon>
              <n-icon><MoonOutline /></n-icon>
            </template>
          </n-switch>
        </div>

        <div class="login-header">
          <img src="@/assets/logo.png" alt="logo" class="logo" />
          <div>
            <h2>{{ title }}</h2>
            <p>欢迎登录，继续高效的数字办公之旅</p>
          </div>
        </div>

        <n-form
          ref="formRef"
          :model="loginForm"
          :rules="rules"
          label-placement="left"
          class="login-form"
        >
          <n-form-item path="userName">
            <n-input v-model:value="loginForm.userName" placeholder="请输入用户名" :maxlength="20">
              <template #prefix>
                <n-icon><PersonOutline /></n-icon>
              </template>
            </n-input>
          </n-form-item>

          <n-form-item path="password">
            <n-input
              v-model:value="loginForm.password"
              type="password"
              show-password-on="click"
              placeholder="请输入密码"
              :maxlength="30"
            >
              <template #prefix>
                <n-icon><LockClosedOutline /></n-icon>
              </template>
            </n-input>
          </n-form-item>

          <n-form-item path="codeValue">
            <n-input-group>
              <n-input
                v-model:value="loginForm.codeValue"
                placeholder="请输入验证码"
                @keydown.enter="handleLogin"
              >
                <template #prefix>
                  <n-icon><ShieldCheckmarkOutline /></n-icon>
                </template>
              </n-input>
              <n-image
                preview-disabled
                :src="captchaImage.captchaUrl"
                class="captcha-img"
                @click="getCaptcha"
              />
            </n-input-group>
          </n-form-item>

          <div class="form-extra">
            <n-checkbox v-model:checked="rememberMe">记住当前设备</n-checkbox>
            <n-button text type="primary" @click="handleForgotPassword">忘记密码？</n-button>
          </div>

          <n-button
            type="primary"
            block
            round
            :loading="loading"
            :disabled="loading"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '进入系统' }}
          </n-button>

          <div class="divider">
            <span>其它方式</span>
          </div>
          <div class="quick-links">
            <n-button tertiary block round @click="handleRegister">注册账号</n-button>
            <n-button tertiary block round @click="handleForgotPassword">获取演示账号</n-button>
          </div>
        </n-form>

        <footer class="login-footer">
          <p>Copyright © 2022-{{ new Date().getFullYear() }} XingGao.</p>
        </footer>
      </n-card>
    </div>
  </section>
</template>

<script lang="ts" setup>
import { ref, reactive, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useMessage, useNotification } from 'naive-ui'
import { useUserStore } from '@/store/modules/user'
import { useAppConfigStore } from '@/store/modules/app-config'
import { getCaptchaImage, login } from '@/api/common'
import type { UserState } from '@/store/types'
import { usePermissionStore } from '@/store/modules/permission'
import {
  PersonOutline,
  LockClosedOutline,
  ShieldCheckmarkOutline,
  SunnyOutline,
  MoonOutline
} from '@vicons/ionicons5'

// 组件实例
const router = useRouter()
const route = useRoute()
const message = useMessage()
const notification = useNotification()
const userStore = useUserStore()
const appConfig = useAppConfigStore()
const permissionStore = usePermissionStore()

// 计算暗黑模式状态
const isDarkMode = computed(() => appConfig.theme === 'dark')

// 响应式数据
const formRef = ref()
const loading = ref(false)
const rememberMe = ref(false)
const title = import.meta.env.VITE_BASE_TITLE

// 登录表单数据
const loginForm = reactive({
  userName: '',
  password: '',
  codeValue: ''
})

// 验证码数据
const captchaImage = reactive({
  captcha: '',
  captchaUrl: '',
  showCaptcha: false
})

// 表单验证规则
const rules = {
  userName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  codeValue: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

// 获取验证码
const getCaptcha = async () => {
  try {
    const res = await getCaptchaImage()
    captchaImage.captcha = res.data.captcha
    captchaImage.captchaUrl = 'data:image/gif;base64,' + res.data.captchaUrl
    captchaImage.showCaptcha = res.data.showCaptcha
  } catch (error) {
    message.error('获取验证码失败')
  }
}

// 主题切换
const darkModeSwitchEvent = (val: boolean) => {
  appConfig.changeThemeToggle(val)
}

// 登录处理
const handleLogin = () => {
  formRef.value?.validate(async (errors: any) => {
    if (errors) return

    loading.value = true
    try {
      const { data } = await login({
        codeId: captchaImage.captcha,
        ...loginForm
      })

      // 保存用户信息
      await userStore.saveUser(data.userInfo as UserState)

      // 保存路由信息到 permissionStore
      permissionStore.saveRoutes(data.routes || [])
      await permissionStore.initPermissionRoute()

      // 登录成功后跳转
      await router.replace({
        path: route.query.redirect ? (route.query.redirect as string) : '/'
      })

      notification.success({
        content: '登录成功',
        meta: `欢迎回来, ${data.userInfo.nickName}`,
        duration: 2500,
        keepAliveOnHover: true
      })
    } catch (error) {
      getCaptcha()
    } finally {
      loading.value = false
    }
  })
}

// 忘记密码
const handleForgotPassword = () => {
  message.info('忘记密码功能开发中')
}

// 注册账号
const handleRegister = () => {
  message.info('注册功能开发中')
}

// 初始化
getCaptcha()
</script>

<style lang="scss" scoped>
.login-shell {
  min-height: 100vh;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(360px, 1fr));
  background: linear-gradient(135deg, #eef2ff, #ffffff);

  &.dark {
    background: linear-gradient(135deg, #0f172a, #1e1f2c);
  }
}

.login-hero {
  padding: 64px;
  display: flex;
  align-items: center;
  justify-content: center;

  .hero-content {
    max-width: 420px;

    .hero-eyebrow {
      font-size: 12px;
      color: #94a3b8;
      letter-spacing: 2px;
      text-transform: uppercase;
      margin-bottom: 12px;
    }

    h1 {
      font-size: 36px;
      margin-bottom: 12px;
      color: #0f172a;
    }

    .hero-desc {
      color: #475569;
      line-height: 1.6;
      margin-bottom: 32px;
    }

    .hero-metrics {
      display: flex;
      gap: 32px;

      .metric {
        strong {
          display: block;
          font-size: 32px;
        }

        span {
          color: #64748b;
        }
      }
    }
  }
}

.login-panel {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.login-card {
  width: 420px;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.08);
  position: relative;
}

.theme-switch {
  position: absolute;
  top: 16px;
  right: 16px;
}

.login-header {
  display: flex;
  gap: 16px;
  align-items: center;
  margin-bottom: 24px;

  .logo {
    width: 48px;
    height: 48px;
  }

  h2 {
    margin: 0;
  }

  p {
    margin: 4px 0 0;
    color: var(--text-color-3);
  }
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 16px;

  .captcha-img {
    width: 120px;
    height: 40px;
    border-radius: 8px;
    cursor: pointer;
  }

  .form-extra {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 13px;
  }

  .divider {
    display: flex;
    align-items: center;
    gap: 12px;
    color: var(--text-color-3);
    font-size: 12px;

    &::before,
    &::after {
      content: '';
      flex: 1;
      height: 1px;
      background: var(--divider-color);
    }
  }

  .quick-links {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
  }
}

.login-footer {
  text-align: center;
  margin-top: 16px;
  font-size: 12px;
  color: var(--text-color-3);
}

@media (max-width: 900px) {
  .login-shell {
    grid-template-columns: 1fr;
  }

  .login-hero {
    display: none;
  }
}
</style>
