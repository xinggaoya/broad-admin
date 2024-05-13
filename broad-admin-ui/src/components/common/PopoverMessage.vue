<template>
  <n-card
    :content-style="{ padding: '0px' }"
    :footer-style="{ padding: '0px' }"
    :bordered="false"
    :segmented="true"
  >
    <n-space v-if="messages.length > 0">
      <div v-for="(item, index) of messages" :key="index">
        <n-flex>
          <n-icon size="40" color="#f00">
            <NotificationsCircle />
          </n-icon>
          <div style="flex: 1">
            <div>{{ item.title }}</div>
            <n-ellipsis :line-clamp="1" class="text-gray-500">{{ item.desc }}</n-ellipsis>
          </div>
        </n-flex>
      </div>
    </n-space>
    <n-empty v-else description="暂无消息哦~" class="pt-20 pb-20" />
    <template #footer>
      <n-space style="padding: 7px 0" justify="end">
        <n-button text @click="onClearMessage">清空消息</n-button>
        <n-button text>查看更多</n-button>
      </n-space>
    </template>
  </n-card>
</template>

<script lang="ts" setup>
import { shallowReactive } from 'vue'
import { NotificationsCircle } from '@vicons/ionicons5'

const emit = defineEmits(['clear'])
const messages = shallowReactive([
  {
    title: '您收到一个消息',
    desc: '来自好友张三的生日祝福'
  },
  {
    title: '您收到一个消息',
    desc: '来自好友张三的生日祝福来自好友张三的生日祝福'
  },
  {
    title: '您收到一个消息',
    desc: '来自好友张三的生日祝福来自好友张三的生日祝福'
  },
  {
    title: '您收到一个消息',
    desc: '来自好友张三的生日祝福来自好友张三的生日祝福来自好友张三的生日祝福来自好友张三的生日祝福'
  },
  {
    title: '您收到一个消息',
    desc: '来自好友张三的生日祝福'
  },
  {
    title: '您收到一个消息',
    desc: '来自好友张三的生日祝福'
  }
])

function onClearMessage() {
  messages.length = 0
  emit('clear')
}
</script>
