<template>
  <n-drawer v-model:show="opened" placement="right" :auto-focus="false">
    <n-drawer-content title="系统设置" closable body-content-style="padding:0">
      <n-scrollbar>
        <div class="wrapper">
          <n-divider dashed>主题设置</n-divider>
          <n-grid>
            <n-grid-item
              v-for="(item, index) of themeList"
              :key="index"
              :span="12"
              class="example-wrapper"
            >
              <StyleExample
                :checked="item.checked"
                :left-bg="item.leftBg"
                :right-top-bg="item.rightTopBg"
                :right-bottom-bg="item.rightBottomBg"
                :tip-text="item.tipText"
                @click="themeClick(item)"
              />
            </n-grid-item>
          </n-grid>
          <n-divider dashed>侧边栏样式</n-divider>
          <n-grid>
            <n-grid-item
              v-for="(item, index) of sideExampleList"
              :key="index"
              :span="8"
              class="example-wrapper"
            >
              <StyleExample
                :checked="item.checked"
                :left-bg="item.leftBg"
                :right-top-bg="item.rightTopBg"
                :right-bottom-bg="item.rightBottomBg"
                @click="exampleClick(item)"
              />
            </n-grid-item>
          </n-grid>
          <n-divider dashed>布局模式</n-divider>
          <n-grid>
            <n-grid-item
              v-for="(item, index) of layoutExampleList"
              :key="index"
              :span="8"
              class="example-wrapper"
            >
              <StyleExample
                :checked="item.checked"
                :left-bg="item.leftBg"
                :right-top-bg="item.rightTopBg"
                :right-bottom-bg="item.rightBottomBg"
                :class="[item.class || '']"
                :tip-text="item.tipText"
                @click="layoutExampleClick(item)"
              />
            </n-grid-item>
          </n-grid>
          <n-divider dashed>主题颜色</n-divider>
          <n-grid class="colors-wrapper">
            <n-grid-item
              v-for="(item, index) of primartyColorList"
              :key="index"
              :span="4"
              class="color-wrapper"
              :class="{ circle: item.checked }"
              :style="{ backgroundColor: item.value }"
              @click="colorClick(item)"
            />
          </n-grid>
          <div style="height: 20px"></div>
          <n-divider dashed>菜单设置</n-divider>
          <div class="setting-item-wrapper">
            <span style="width: 150px">菜单宽度</span>
            <n-input-number v-model:value="menuWidth" size="small" :min="200" :max="400" :step="10">
              <template #suffix>px</template>
            </n-input-number>
          </div>
          <div class="setting-item-wrapper">
            <span style="width: 150px">手风琴</span>
            <n-switch v-model:value="appConfig.isAccordion" />
          </div>
          <n-divider dashed>页面切换动画</n-divider>
          <div class="setting-item-wrapper">
            <span style="width: 100px">动画效果</span>
            <n-select
              v-model:value="appConfig.pageAnim"
              :options="animOptions"
              @update:value="onAnimUpdate"
            />
          </div>
          <n-divider dashed>按钮显示</n-divider>
          <div class="setting-item-wrapper">
            <span>固定顶部导航</span>
            <n-switch
              v-model:value="appConfig.isFixedNavBar"
              :disabled="appConfig.layoutMode === 'ttb'"
            />
          </div>
          <div class="setting-item-wrapper">
            <span>搜索</span>
            <n-switch v-model:value="appConfig.actionBar.isShowSearch" />
          </div>
          <div class="setting-item-wrapper">
            <span>消息</span>
            <n-switch v-model:value="appConfig.actionBar.isShowMessage" />
          </div>
          <div class="setting-item-wrapper">
            <span>刷新</span>
            <n-switch v-model:value="appConfig.actionBar.isShowRefresh" />
          </div>
          <div class="setting-item-wrapper">
            <span>全屏</span>
            <n-switch v-model:value="appConfig.actionBar.isShowFullScreen" />
          </div>
          <div class="setting-item-wrapper">
            <span>底部栏</span>
            <n-switch v-model:value="appConfig.actionBar.isShowFooter" />
          </div>
          <n-divider />
          <!-- 底部操作 -->
          <div class="footer-actions">
            <n-button block secondary type="primary" @click="resetToDefault">
              恢复默认配置
            </n-button>
          </div>
        </div>
      </n-scrollbar>
    </n-drawer-content>
  </n-drawer>
</template>

<script lang="ts" setup name="SystemSetting">
import { onMounted, reactive, ref, watch } from 'vue'
import type { ModalDialogType } from '@/types/components'
import LeftBg from '@/assets/bg_img.webp'
import { useAppConfigStore } from '@/store/modules/app-config'
import { PageAnim, ThemeMode } from '@/store/types'
import { useMessage } from 'naive-ui'

const message = useMessage()
const opened = ref(false)
const appConfig = useAppConfigStore()
const menuWidth = ref(appConfig.sideWidth)
const themeList = reactive([
  {
    leftBg: '#ffffff',
    rightTopBg: '#ffffff',
    rightBottomBg: '#f5f5f5',
    checked: false,
    themeId: 'light',
    tipText: '明亮'
  },
  {
    leftBg: '#000000',
    rightTopBg: '#000000',
    rightBottomBg: '#333333',
    checked: false,
    themeId: 'dark',
    tipText: '暗黑'
  }
])
const sideExampleList = reactive([
  {
    leftBg: '#000000',
    rightTopBg: '#ffffff',
    rightBottomBg: '#f5f5f5',
    checked: false,
    themeId: 'dark'
  },
  {
    leftBg: '#ffffff',
    rightTopBg: '#ffffff',
    rightBottomBg: '#f5f5f5',
    checked: false,
    themeId: 'white'
  },
  {
    leftBg: `url(${LeftBg})`,
    rightTopBg: '#ffffff',
    rightBottomBg: '#f5f5f5',
    checked: false,
    themeId: 'image'
  }
])
const layoutExampleList = reactive([
  {
    leftBg: '#000000',
    rightTopBg: '#d4d4d4',
    rightBottomBg: '#d4d4d4',
    checked: true,
    layoutId: 'ltr',
    tipText: '左右'
  },
  {
    leftBg: '#d4d4d4',
    rightTopBg: '#ffffff',
    rightBottomBg: '#d4d4d4',
    checked: false,
    layoutId: 'ttb',
    class: 'extra-class',
    tipText: '上下'
  },
  {
    leftBg: '#000000',
    rightTopBg: '#d4d4d4',
    rightBottomBg: '#d4d4d4',
    checked: false,
    layoutId: 'lcr',
    class: 'extra-class-1',
    tipText: '分栏'
  }
])
const primartyColorList = reactive([
  {
    name: 'blue',
    value: '#2B5CE7',
    checked: true
  },
  {
    name: 'cyan',
    value: '#18a058',
    checked: false
  },
  {
    name: 'red',
    value: '#F5222D',
    checked: false
  },
  {
    name: 'purple',
    value: '#722ED1',
    checked: false
  },
  {
    name: 'orange',
    value: '#ee4f12',
    checked: false
  },
  {
    name: 'cyan-blue',
    value: '#0096c7',
    checked: false
  },
  {
    name: 'yellow',
    value: '#ff9801',
    checked: false
  },
  {
    name: 'pink',
    value: '#ff3d68',
    checked: false
  }
])
const animOptions = [
  {
    label: '渐隐渐现',
    value: 'opacity'
  },
  {
    label: '左右滑动',
    value: 'fade'
  },
  {
    label: '上下滑动',
    value: 'down'
  },
  {
    label: '缩放效果',
    value: 'scale'
  }
]

onMounted(() => {
  initCheckedState()
})

// 初始化选中状态
function initCheckedState() {
  themeList.forEach((it) => {
    it.checked = appConfig.theme === it.themeId
  })
  sideExampleList.forEach((it) => {
    it.checked = appConfig.sideTheme === it.themeId
  })
  layoutExampleList.forEach((it) => {
    it.checked = appConfig.layoutMode === it.layoutId
  })
  primartyColorList.forEach((it) => {
    it.checked = appConfig.themeColor.primaryColor === it.value
  })
}

function openDrawer() {
  opened.value = true
  // 打开时重新初始化选中状态
  initCheckedState()
}

async function themeClick(item: any) {
  themeList.forEach((it) => {
    it.checked = it === item
  })
  appConfig.changeTheme(item.themeId)
  if (item.themeId === ThemeMode.DARK) {
    exampleClick(sideExampleList[0])
  } else {
    exampleClick(sideExampleList[1])
  }
}

function exampleClick(item: any) {
  sideExampleList.forEach((it) => {
    it.checked = it === item
  })
  appConfig.changeSideBarTheme(item.themeId)
}

function layoutExampleClick(item: any) {
  layoutExampleList.forEach((it) => {
    it.checked = it === item
  })
  appConfig.changeLayoutMode(item.layoutId)
}

function colorClick(item: any) {
  primartyColorList.forEach((it) => {
    it.checked = it === item
  })
  appConfig.changePrimaryColor(item.value)
}

function onAnimUpdate(val: PageAnim) {
  appConfig.changePageAnim(val)
}

function resetToDefault() {
  // 重置状态
  appConfig.resetState()
  // 重新初始化选中状态
  initCheckedState()
  // 重置菜单宽度
  menuWidth.value = 220

  message.success('已恢复默认配置')
}

watch(
  () => menuWidth.value,
  (newVal) => {
    appConfig.changeSideWith(newVal)
  }
)

defineExpose({
  openDrawer
})
</script>

<style lang="scss" scoped>
$width: 60px;

.wrapper {
  padding: 0 20px;

  .close-wrapper {
    text-align: right;
    font-size: 20px;
  }

  .colors-wrapper {
    display: flex;
    justify-content: space-evenly;
    align-items: center;

    .color-wrapper {
      width: 20px;
      height: 20px;
      border-radius: 5px;
      border: 1px solid #c1c1c1;
      margin-bottom: 20px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    }

    .circle::after {
      content: '';
      display: block;
      margin: 0 auto;
      margin-top: 25px;
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background-color: rgb(3, 190, 50);
      text-align: center;
    }
  }

  .setting-item-wrapper {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    font-size: 14px;
  }

  .footer-actions {
    margin-top: 24px;
    padding: 0 12px;
  }
}

.example-wrapper + .example-wrapper {
  margin-bottom: 30px;
}

.info-key {
  width: 40%;
}
</style>
