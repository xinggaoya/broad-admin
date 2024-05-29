<template>
  <n-card :bordered="false" style="height: 100%" content-style="padding:0;height:100%">
    <div class="login-set">
      <n-space>
        <n-switch @update-value="darkModeSwitchEvent">
          <template v-slot:checked-icon>
            <n-icon>
              <PowerSharp />
            </n-icon>
          </template>
          <template v-slot:unchecked-icon>
            <n-icon>
              <PowerSharp />
            </n-icon>
          </template>
        </n-switch>
      </n-space>
    </div>
    <div class="login">
      <div class="login_bg" :style="{ backgroundImage: `url(${getAssetsFile})` }"></div>
      <div class="login-container">
        <div class="embellish left">
          <div class="embellish-up">
            <div class="up_2"></div>
            <div class="up_1"></div>
          </div>
          <div class="embellish-down"></div>
        </div>
        <div class="embellish right">
          <div class="embellish-up">
            <div class="up_1"></div>
            <div class="up_2"></div>
          </div>
          <div class="embellish-down"></div>
        </div>
        <div class="login-content">
          <n-grid cols="24" :x-gap="10" :y-gap="10" item-responsive responsive="screen">
            <n-grid-item span="0 m:0 l:12">
              <div class="slide-left">
                <img src="@/assets/login/xwc.svg" alt="" />
              </div>
            </n-grid-item>
            <n-grid-item span="24 m:24 l:12">
              <div class="slide-right">
                <n-h1>{{ title }} 欢迎您登录</n-h1>
                <n-text depth="3"> 请输入您的账号和密码进行登录</n-text>
                <n-form
                  autocomplete="off"
                  :rules="compData.rules"
                  ref="formRef"
                  class="login-form"
                  layout="vertical"
                  :model="compData.form"
                >
                  <n-form-item label="用户名" path="userName">
                    <n-input
                      size="large"
                      :maxlength="20"
                      autocomplete="off"
                      v-model:value="compData.form.userName"
                      placeholder="输入您的用户名"
                    >
                      <template v-slot:suffix>
                        <n-icon>
                          <PersonOutline />
                        </n-icon>
                      </template>
                    </n-input>
                  </n-form-item>
                  <n-form-item label="密码" path="password">
                    <n-input
                      size="large"
                      :maxlength="30"
                      :show-password-on="'click'"
                      type="password"
                      autocomplete="off"
                      v-model:value="compData.form.password"
                      placeholder="输入你的密码"
                    >
                    </n-input>
                  </n-form-item>
                  <n-form-item label="验证码" path="codeValue">
                    <n-input-group>
                      <n-input
                        size="large"
                        v-model:value="compData.form.codeValue"
                        @keydown.enter="handleLogin"
                        placeholder="输入验证码"
                      >
                        <template v-slot:suffix>
                          <PowerSharp />
                        </template>
                      </n-input>
                      <n-image
                        preview-disabled
                        :src="captchaImage.captchaUrl"
                        style="margin-left: 12px"
                        alt=""
                        @click="getCaptcha"
                      />
                    </n-input-group>
                  </n-form-item>
                </n-form>
                <n-space style="margin-bottom: 10px" justify="space-between">
                  <n-checkbox v-model:checked="compData.rememberPas">记住我</n-checkbox>
                  <n-text depth="3">忘记密码? 找回密码</n-text>
                </n-space>
                <n-space justify="space-between" :size="[10, 15]" vertical>
                  <n-button type="primary" size="large" style="width: 100%" @click="handleLogin">
                    登 录
                  </n-button>
                  <n-text style="text-align: right; cursor: pointer" depth="3"
                    >没有账号? 注册账号
                  </n-text>
                </n-space>
              </div>
            </n-grid-item>
          </n-grid>
          <img class="logo hidden-xs hidden-sm" src="@/assets/logo.png" alt="" />
          <div class="bottom_copyright_information">
            Copyright © 2022-2024 XingGao All Rights Reserved.
          </div>
        </div>
      </div>
    </div>
  </n-card>
</template>
<script lang="ts" setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getCaptchaImage, login } from '@/api/common'
import { useUserStore } from '@/store/modules/user'
import { useAppConfigStore } from '@/store/modules/app-config'
import type { UserState } from '@/store/types'
import { type MessageReactive, useMessage, useNotification } from 'naive-ui'
import { PersonOutline, PowerSharp } from '@vicons/ionicons5'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const notification = useNotification()
const title = import.meta.env.VITE_BASE_TITLE
const appConfig = useAppConfigStore()
const message = useMessage()
const formRef = ref()
let messageReactive: MessageReactive | null = null
const loading = ref(false)
const compData = reactive({
  form: {
    userName: '',
    password: '',
    codeValue: ''
  },
  rememberPas: false,
  passView: false,
  rules: {
    userName: [{ required: true, message: '请输入你的用户名', trigger: 'blur' }],
    codeValue: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
    password: [{ required: true, message: '输入用户名密码', trigger: 'blur' }]
  }
})
const captchaImage = reactive({
  captcha: '',
  captchaUrl: '',
  showCaptcha: false
})
const getAssetsFile = computed(
  () => new URL(`../../assets/login/login_bg.webp`, import.meta.url).href
)

onMounted(() => {
  getCaptcha()
})

function getCaptcha() {
  getCaptchaImage().then((res) => {
    captchaImage.captcha = res.data.captcha
    captchaImage.captchaUrl = 'data:image/gif;base64,' + res.data.captchaUrl
    captchaImage.showCaptcha = res.data.showCaptcha
  })
}

function darkModeSwitchEvent(val: boolean) {
  appConfig.changeThemeToggle(val)
}

function handleLogin() {
  formRef.value.validate().then(() => {
    loading.value = true
    messageReactive = message.loading('登录中...', { duration: 0 })
    login({
      codeId: captchaImage.captcha,
      ...compData.form
    })
      .then(({ data }: any) => {
        userStore.saveUser(data as UserState).then(() => {
          router
            .replace({
              path: route.query.redirect ? (route.query.redirect as string) : '/'
            })
            .then(() => {
              loading.value = false
              messageReactive?.destroy()
              notification.success({
                content: '登录成功',
                meta: `欢迎回来,${data.nickName}`,
                duration: 2500,
                keepAliveOnHover: true
              })
              messageReactive = null
            })
        })
      })
      .catch(() => {
        loading.value = false
        messageReactive?.destroy()
        getCaptcha()
      })
  })
}
</script>
<style lang="scss" scoped>
.login {
  width: 100%;
  height: 100%;
  overflow: hidden;
  position: relative;

  .login_bg {
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center center;
    filter: blur(2px) brightness(1);
    position: absolute;
    top: -8px;
    right: -8px;
    bottom: -8px;
    left: -8px;
    transition: all 1s;

    &.black {
      filter: blur(5px) brightness(0.75);
    }
  }

  &-container {
    width: 75%;
    height: 80%;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
    position: relative;

    .embellish {
      width: 60px;
      height: 45px;
      position: absolute;

      &.left {
        bottom: 60px;
        left: -45px;

        .embellish-up {
          .up_2 {
            margin-right: 10px;
          }
        }

        .embellish-down {
          margin-left: 30px;
        }
      }

      &.right {
        right: -45px;
        top: 40px;

        .embellish-up {
          .up_1 {
            margin-right: 10px;
          }
        }

        .embellish-down {
          margin-left: -30px;
        }
      }

      .embellish-up {
        display: flex;
        align-items: center;

        .up_1 {
          width: 60px;
          height: 12px;
          border-radius: 20px;
          background-color: var(--n-color);
        }

        .up_2 {
          width: 12px;
          height: 12px;
          border-radius: 50%;

          background-color: var(--n-color);
          flex-shrink: 0;
        }
      }

      .embellish-down {
        margin-top: 10px;
        width: 60px;
        height: 12px;
        border-radius: 20px;
        background-color: var(--n-color);
      }
    }
  }

  &-content {
    box-sizing: border-box;
    height: 100%;
    position: relative;
    display: flex;
    z-index: 100;
    padding: 30px;
    border-radius: 30px;
    overflow: hidden;
    align-items: center;
    justify-content: center;
    background-color: var(--n-color);

    .logo {
      position: absolute;
      left: 25px;
      top: 25px;
      height: 25px;
      object-fit: cover;
    }

    .slide-right {
      margin-top: 10px;
    }

    .slide-left {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      height: 100%;

      img {
        width: 80%;
        object-fit: cover;
        transform: rotateY(180deg);
      }
    }

    .login-form {
      margin-top: 30px;
    }
  }

  .bottom_copyright_information {
    position: fixed;
    bottom: 2px;
    font-size: 12px;
    color: #8e8d8d;
  }
}

.login-set {
  position: fixed;
  top: 10px;
  right: 15px;
  color: #333333;
  z-index: 10;
}
</style>
