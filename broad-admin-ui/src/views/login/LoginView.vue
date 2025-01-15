<template>
  <div class="login-container" :class="{ dark: isDarkMode }">
    <!-- 背景动画效果 -->
    <div class="login-background">
      <div class="circles">
        <div v-for="i in 10" :key="i" class="circle"></div>
      </div>
    </div>

    <!-- 登录卡片 -->
    <n-card class="login-card" :bordered="false">
      <!-- 主题切换 -->
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

      <!-- 登录表单区域 -->
      <div class="login-content">
        <!-- Logo和标题 -->
        <div class="login-header">
          <img src="@/assets/logo.png" alt="logo" class="logo" />
          <h1 class="title">{{ title }}</h1>
          <p class="subtitle">欢迎回来! 请登录您的账号</p>
        </div>

        <!-- 登录表单 -->
        <n-form
          ref="formRef"
          :model="loginForm"
          :rules="rules"
          label-placement="left"
          class="login-form"
        >
          <!-- 用户名输入框 -->
          <n-form-item path="userName">
            <n-input v-model:value="loginForm.userName" placeholder="请输入用户名" :maxlength="20">
              <template #prefix>
                <n-icon><PersonOutline /></n-icon>
              </template>
            </n-input>
          </n-form-item>

          <!-- 密码输入框 -->
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

          <!-- 验证码 -->
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

          <!-- 记住密码选项 -->
          <div class="login-options">
            <n-checkbox v-model:checked="rememberMe">记住我</n-checkbox>
            <n-button text type="primary" @click="handleForgotPassword"> 忘记密码? </n-button>
          </div>

          <!-- 登录按钮 -->
          <n-button
            type="primary"
            block
            :loading="loading"
            :disabled="loading"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登录' }}
          </n-button>

          <!-- 注册链接 -->
          <div class="register-link">
            <span>还没有账号?</span>
            <n-button text type="primary" @click="handleRegister"> 立即注册 </n-button>
          </div>
        </n-form>
      </div>

      <!-- 页脚版权信息 -->
      <div class="login-footer">
        <p>Copyright © 2022-{{ new Date().getFullYear() }} XingGao All Rights Reserved.</p>
      </div>
    </n-card>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useMessage, useNotification } from 'naive-ui'
import { useUserStore } from '@/store/modules/user'
import { useAppConfigStore } from '@/store/modules/app-config'
import { getCaptchaImage, login } from '@/api/common'
import type { UserState } from '@/store/types'
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

      await userStore.saveUser(data as UserState)

      // 登录成功后跳转
      await router.replace({
        path: route.query.redirect ? (route.query.redirect as string) : '/'
      })

      notification.success({
        content: '登录成功',
        meta: `欢迎回来, ${data.nickName}`,
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
.login-container {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0f2f5;
  position: relative;
  overflow: hidden;
  transition: all 0.3s ease;

  &.dark {
    background: #101014;
  }
}

.login-background {
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 1;

  .circles {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    overflow: hidden;
  }

  .circle {
    position: absolute;
    display: block;
    list-style: none;
    width: 20px;
    height: 20px;
    background: rgba(0, 0, 0, 0.05);
    animation: animate 25s linear infinite;
    bottom: -150px;
    border-radius: 50%;

    .dark & {
      background: rgba(255, 255, 255, 0.05);
    }

    @for $i from 1 through 10 {
      &:nth-child(#{$i}) {
        left: random(90) + 0%;
        width: random(80) + 20px;
        height: random(80) + 20px;
        animation-delay: random(5) + 0s;
        animation-duration: random(10) + 15s;
      }
    }
  }
}

@keyframes animate {
  0% {
    transform: translateY(0) rotate(0deg);
    opacity: 1;
    border-radius: 0;
  }
  100% {
    transform: translateY(-1000px) rotate(720deg);
    opacity: 0;
    border-radius: 50%;
  }
}

.login-card {
  position: relative;
  z-index: 2;
  width: 420px;
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;

  .dark & {
    background: rgba(24, 24, 28, 0.95);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  }
}

.theme-switch {
  position: absolute;
  top: 20px;
  right: 20px;
}

.login-content {
  padding: 20px 40px;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;

  .logo {
    width: 64px;
    height: 64px;
    margin-bottom: 16px;
  }

  .title {
    font-size: 24px;
    font-weight: 600;
    color: #000000;
    margin-bottom: 8px;
  }

  .subtitle {
    font-size: 14px;
    color: #666666;
  }

  .dark & {
    .title {
      color: #ffffff;
    }
    .subtitle {
      color: #999999;
    }
  }
}

.login-form {
  :deep(.n-input) {
    .n-input__prefix {
      margin-right: 8px;
    }
  }
}

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 24px 0;
}

.captcha-img {
  width: 100px;
  height: 32px;
  margin-left: 12px;
  cursor: pointer;
  border-radius: 4px;
}

.register-link {
  margin-top: 16px;
  text-align: center;
  font-size: 14px;
  color: #666666;

  .dark & {
    color: #999999;
  }

  span {
    margin-right: 4px;
  }
}

.login-footer {
  text-align: center;
  padding: 16px;
  color: #666666;
  font-size: 12px;

  .dark & {
    color: #999999;
  }
}
</style>
