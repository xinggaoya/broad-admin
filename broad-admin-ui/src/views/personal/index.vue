<template>
  <section class="personal-page">
    <div class="personal-layout">
      <n-card class="profile-card" :bordered="false">
        <div class="profile-card__grid">
          <Upload :show-file-list="false" @success="handleUpload">
            <template #default>
              <div
                class="avatar-shell"
                @mouseenter="handleAvatarHover"
                @mouseleave="handleAvatarLeave"
              >
                <n-avatar
                  round
                  :size="110"
                  :src="avatar"
                  :fallback-src="defaultAvatar"
                  class="user-avatar"
                />
                <div class="avatar-overlay" :class="{ 'is-visible': isAvatarHovered }">
                  <n-icon size="20" color="#fff">
                    <CameraOutline />
                  </n-icon>
                  <span>更换头像</span>
                </div>
              </div>
            </template>
          </Upload>
          <div class="profile-card__content">
            <header class="profile-header">
              <div>
                <h2>{{ userInfo.nickName || userInfo.userName }}</h2>
                <p>保持个人资料最新，帮助团队更好协作</p>
              </div>
              <n-button quaternary round size="small" @click="handleAddTag">
                <template #icon>
                  <n-icon><AddCircleOutline /></n-icon>
                </template>
                添加标签
              </n-button>
            </header>
            <n-input
              v-model:value="userBio"
              type="textarea"
              placeholder="编辑个人简介..."
              :autosize="{ minRows: 2, maxRows: 3 }"
              class="bio-input"
              @blur="handleBioUpdate"
            />
            <div class="profile-meta">
              <div class="meta-row" v-for="(item, index) in userInfoList" :key="index">
                <n-icon :size="16" :color="item.color">
                  <component :is="item.icon" />
                </n-icon>
                <span class="meta-label">{{ item.label }}</span>
                <span class="meta-value">{{ item.value }}</span>
              </div>
            </div>
            <div class="profile-tags">
              <n-tag
                v-for="(tag, index) in userTags"
                :key="index"
                :type="tag.type"
                size="small"
                round
              >
                {{ tag.name }}
              </n-tag>
            </div>
          </div>
        </div>
      </n-card>

      <div class="personal-panels">
        <n-card class="todo-panel" title="待办事项">
          <template #header-extra>
            <n-button text @click="handleAddTodo">
              <template #icon>
                <n-icon><AddCircleOutline /></n-icon>
              </template>
              添加待办
            </n-button>
          </template>
          <div class="todo-list">
            <TransitionGroup name="list">
              <div
                v-for="(item, index) in todoItems"
                :key="item.id"
                class="todo-item"
                :class="{ 'is-complete': item.status === 1 }"
              >
                <n-checkbox
                  :checked="item.status === 1"
                  @update:checked="handleTodoStatusChange(index)"
                >
                  <span>{{ item.title }}</span>
                </n-checkbox>
                <div class="todo-actions">
                  <n-button quaternary circle size="small" @click="handleEditTodo(index)">
                    <template #icon>
                      <n-icon><PencilOutline /></n-icon>
                    </template>
                  </n-button>
                  <n-button quaternary circle size="small" @click="handleDeleteTodo(index)">
                    <template #icon>
                      <n-icon><TrashOutline /></n-icon>
                    </template>
                  </n-button>
                </div>
              </div>
            </TransitionGroup>
            <n-empty v-if="todoItems.length === 0" description="暂无待办事项" />
          </div>
        </n-card>

        <n-card class="message-panel" title="消息中心">
          <template #header-extra>
            <n-button text @click="handleViewAllMessages">
              查看全部
              <template #icon>
                <n-icon><ChevronForward /></n-icon>
              </template>
            </n-button>
          </template>
          <div class="message-list">
            <TransitionGroup name="list">
              <button
                v-for="(item, index) in messages"
                :key="index"
                type="button"
                class="message-item"
                :class="{ 'is-unread': item.status === 0 }"
                @click="handleReadMessage(index)"
              >
                <span class="message-dot" :class="{ 'is-unread': item.status === 0 }"></span>
                <div class="message-body">
                  <div class="message-head">
                    <h4>{{ item.title }}</h4>
                    <span>{{ item.time || '刚刚' }}</span>
                  </div>
                  <p>{{ item.content }}</p>
                </div>
              </button>
            </TransitionGroup>
            <n-empty v-if="messages.length === 0" description="暂无消息" />
          </div>
        </n-card>
      </div>
    </div>

    <n-modal
      v-model:show="showTodoModal"
      :title="todoModalTitle"
      preset="dialog"
      positive-text="确定"
      negative-text="取消"
      @positive-click="handleTodoSubmit"
      @negative-click="handleTodoCancel"
    >
      <n-input
        v-model:value="todoForm.title"
        type="textarea"
        placeholder="请输入待办事项..."
        :autosize="{ minRows: 2, maxRows: 4 }"
      />
    </n-modal>

    <n-modal
      v-model:show="showTagModal"
      title="添加标签"
      preset="dialog"
      positive-text="确定"
      negative-text="取消"
      @positive-click="handleTagSubmit"
      @negative-click="handleTagCancel"
    >
      <n-input v-model:value="newTag" placeholder="请输入标签名称" />
      <n-select
        v-model:value="newTagType"
        :options="tagTypeOptions"
        placeholder="请选择标签类型"
        class="mt-4"
      />
    </n-modal>
  </section>
</template>

<script lang="ts" setup name="PersonalCenterView">
import { ref, computed, onMounted } from 'vue'
import {
  CameraOutline,
  PersonOutline,
  CalendarOutline,
  BusinessOutline,
  MailOutline,
  PhonePortraitOutline,
  LocationOutline,
  AddCircleOutline,
  PencilOutline,
  TrashOutline,
  ChevronForward
} from '@vicons/ionicons5'
import { useUserStore } from '@/store/modules/user'
import Upload from '@/components/upload/UpdateView.vue'
import { updateUser, getUserInfo } from '@/api/system/user'
import { useMessage } from 'naive-ui'
import defaultAvatar from '@/assets/defaultProfilePicture.gif'
import { getUserTags, addUserTag, deleteUserTag } from '@/api/system/userTag'
import { getTodoList, addTodo, updateTodo, deleteTodo } from '@/api/system/todo'
import { getMessageList, readMessage } from '@/api/system/message'

const message = useMessage()
const userStore = useUserStore()

// 用户基本信息
const avatar = ref(userStore.avatar || '')
const userInfo = ref<any>({})
const userBio = ref('')
const isAvatarHovered = ref(false)
const displayName = computed(() => userInfo.value.nickName || userInfo.value.userName || '访客')
const greeting = computed(() => {
  const hour = new Date().getHours()
  if (hour < 6) return '凌晨好'
  if (hour < 9) return '早上好'
  if (hour < 12) return '上午好'
  if (hour < 14) return '中午好'
  if (hour < 17) return '下午好'
  if (hour < 19) return '傍晚好'
  return '晚上好'
})
const weatherInfo = ref('今日晴朗，适合整理待办~')

// 用户详细信息列表
const userInfoList = ref<any[]>([])

// 用户标签
const userTags = ref<any[]>([])

// 标签相关
const showTagModal = ref(false)
const newTag = ref('')
const newTagType = ref('info')
const tagTypeOptions = [
  { label: '信息', value: 'info' },
  { label: '成功', value: 'success' },
  { label: '警告', value: 'warning' },
  { label: '错误', value: 'error' }
]

// 待办事项相关
const showTodoModal = ref(false)
const todoModalTitle = ref('添加待办')
const todoForm = ref({
  title: '',
  id: ''
})
const editingTodoIndex = ref(-1)
const todoItems = ref<any[]>([])

// 消息数据
const messages = ref<any[]>([])

const heroStats = computed(() => {
  const unfinished = todoItems.value.filter((item) => item.status !== 1).length
  const unread = messages.value.filter((msg) => msg.status === 0).length
  return [
    { label: '标签数量', value: `${userTags.value.length}`, helper: '个标签' },
    {
      label: '待办完成度',
      value: `${unfinished}/${todoItems.value.length || 0}`,
      helper: '未完成/总数'
    },
    { label: '未读消息', value: `${unread} 条`, helper: '需要处理' }
  ]
})

// 获取用户信息
const loadUserInfo = async () => {
  try {
    const res = await getUserInfo(userStore.userId)
    if (res.code === 200) {
      userInfo.value = res.data
      avatar.value = userInfo.value.avatar || userStore.avatar || ''
      userBio.value = userInfo.value.bio || '暂无简介'

      // 构建用户详细信息列表
      userInfoList.value = [
        {
          label: '昵称',
          value: userInfo.value.nickName || userInfo.value.userName,
          icon: PersonOutline,
          color: '#2080f0'
        },
        {
          label: '性别',
          value: userInfo.value.sex === '0' ? '男' : '女',
          icon: PersonOutline,
          color: '#f0a020'
        },
        {
          label: '生日',
          value: userInfo.value.birthday || '暂无',
          icon: CalendarOutline,
          color: '#18a058'
        },
        {
          label: '部门',
          value: userInfo.value.deptName || '暂无',
          icon: BusinessOutline,
          color: '#d03050'
        },
        {
          label: '邮箱',
          value: userInfo.value.email || '暂无',
          icon: MailOutline,
          color: '#2080f0'
        },
        {
          label: '手机',
          value: userInfo.value.phoneNumber || '暂无',
          icon: PhonePortraitOutline,
          color: '#f0a020'
        },
        {
          label: '地址',
          value: userInfo.value.address || '暂无',
          icon: LocationOutline,
          color: '#18a058'
        }
      ]
    }
  } catch (error) {
    console.error('获取用户信息失败', error)
    message.error('获取用户信息失败')
  }
}

// 获取用户标签
const loadUserTags = async () => {
  try {
    const res = await getUserTags(userStore.userId)
    if (res.code === 200) {
      userTags.value = res.data || []
    }
  } catch (error) {
    console.error('获取用户标签失败', error)
  }
}

// 获取待办事项
const loadTodoList = async () => {
  try {
    const res = await getTodoList(userStore.userId)
    if (res.code === 200) {
      todoItems.value = res.data || []
    }
  } catch (error) {
    console.error('获取待办事项失败', error)
  }
}

// 获取消息列表
const loadMessages = async () => {
  try {
    const res = await getMessageList(userStore.userId)
    if (res.code === 200) {
      messages.value = res.data || []
    }
  } catch (error) {
    console.error('获取消息列表失败', error)
  }
}

// 头像相关方法
function handleAvatarHover() {
  isAvatarHovered.value = true
}

function handleAvatarLeave() {
  isAvatarHovered.value = false
}

function handleUpload(url: string) {
  avatar.value = url
  userStore.setAvatar(url)
  updateUser({ avatar: url, id: userStore.userId }).then(() => {
    message.success('头像上传成功，重新登录后生效')
  })
}

// 个人简介更新
function handleBioUpdate() {
  updateUser({ bio: userBio.value, id: userStore.userId })
    .then(() => {
      message.success('个人简介更新成功')
    })
    .catch(() => {
      message.error('个人简介更新失败')
    })
}

// 待办事项方法
function handleAddTodo() {
  todoModalTitle.value = '添加待办'
  todoForm.value = { title: '', id: '' }
  editingTodoIndex.value = -1
  showTodoModal.value = true
}

function handleEditTodo(index: number) {
  const todo = todoItems.value[index]
  if (todo) {
    todoModalTitle.value = '编辑待办'
    todoForm.value = { ...todo }
    editingTodoIndex.value = index
    showTodoModal.value = true
  }
}

async function handleDeleteTodo(index: number) {
  try {
    const todo = todoItems.value[index]
    if (!todo.id) {
      todoItems.value.splice(index, 1)
      message.success('删除成功')
      return
    }

    const res = await deleteTodo(todo.id)
    if (res.code === 200) {
      todoItems.value.splice(index, 1)
      message.success('删除成功')
    }
  } catch (error) {
    message.error('删除失败')
    console.error('删除待办事项失败', error)
  }
}

async function handleTodoStatusChange(index: number) {
  const todo = todoItems.value[index]
  if (todo) {
    const newStatus = todo.status === 1 ? 0 : 1
    try {
      if (todo.id) {
        await updateTodo({
          id: todo.id,
          status: newStatus
        })
      }
      todo.status = newStatus
    } catch (error) {
      message.error('更新状态失败')
      console.error('更新待办状态失败', error)
    }
  }
}

async function handleTodoSubmit() {
  if (!todoForm.value.title.trim()) {
    message.warning('请输入待办内容')
    return
  }

  try {
    // 新增待办
    if (editingTodoIndex.value === -1) {
      const formData = {
        title: todoForm.value.title,
        status: 0,
        userId: userStore.userId
      }
      const res = await addTodo(formData)
      if (res.code === 200) {
        todoItems.value.unshift({
          id: res.data,
          title: todoForm.value.title,
          status: 0
        })
        message.success('添加成功')
      }
    }
    // 编辑待办
    else {
      const todo = todoItems.value[editingTodoIndex.value]
      const formData = {
        id: todo.id,
        title: todoForm.value.title,
        status: todo.status
      }
      const res = await updateTodo(formData)
      if (res.code === 200) {
        todoItems.value[editingTodoIndex.value].title = todoForm.value.title
        message.success('更新成功')
      }
    }
    showTodoModal.value = false
  } catch (error) {
    message.error('操作失败')
    console.error('保存待办失败', error)
  }
}

function handleTodoCancel() {
  showTodoModal.value = false
  todoForm.value = { title: '', id: '' }
}

// 标签相关方法
function handleAddTag() {
  showTagModal.value = true
}

async function handleTagSubmit() {
  if (!newTag.value.trim()) {
    message.warning('请输入标签名称')
    return
  }

  try {
    const formData = {
      name: newTag.value,
      type: newTagType.value,
      userId: userStore.userId
    }
    const res = await addUserTag(formData)
    if (res.code === 200) {
      userTags.value.push({
        id: res.data,
        name: newTag.value,
        type: newTagType.value
      })
      showTagModal.value = false
      newTag.value = ''
      message.success('添加标签成功')
    }
  } catch (error) {
    message.error('添加标签失败')
    console.error('添加标签失败', error)
  }
}

function handleTagCancel() {
  showTagModal.value = false
  newTag.value = ''
}

// 消息相关方法
async function handleReadMessage(index: number) {
  const msg = messages.value[index]
  if (msg && msg.status === 0) {
    try {
      const res = await readMessage(msg.id)
      if (res.code === 200) {
        msg.status = 1
        message.success('已标记为已读')
      }
    } catch (error) {
      message.error('操作失败')
      console.error('标记消息已读失败', error)
    }
  }
}

function handleViewAllMessages() {
  // 可以导航到消息列表页面
  message.info('查看全部消息功能待实现')
}

// 生命周期钩子
onMounted(() => {
  loadUserInfo()
  loadUserTags()
  loadTodoList()
  loadMessages()
})
</script>

<style scoped lang="scss">
.personal-page {
  display: flex;
  flex-direction: column;
  gap: var(--shell-gap);
}

.personal-layout {
  display: flex;
  flex-direction: column;
  gap: var(--shell-gap);
}

.profile-card {
  border-radius: var(--shell-radius-xl);
  background: var(--shell-surface);
  box-shadow: var(--shell-shadow);
}

.profile-card__grid {
  display: grid;
  grid-template-columns: auto 1fr;
  gap: 24px;
  align-items: flex-start;
}

.avatar-shell {
  position: relative;
  width: 110px;
  height: 110px;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: 0 15px 30px rgba(15, 23, 42, 0.15);
}

.avatar-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.55);
  color: #fff;
  font-size: 12px;
  opacity: 0;
  transition: opacity 0.2s ease;
}

.avatar-overlay.is-visible {
  opacity: 1;
}

.profile-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.profile-header h2 {
  margin: 0;
  font-size: 24px;
}

.profile-header p {
  margin: 4px 0 0;
  color: var(--shell-muted-text-color);
}

.bio-input {
  margin-bottom: 16px;
}

.profile-meta {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 10px;
  margin-bottom: 16px;
}

.meta-row {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  padding: 8px 0;
  border-bottom: 1px solid var(--shell-border-color);
}

.meta-label {
  color: var(--shell-muted-text-color);
}

.profile-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.personal-panels {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: var(--shell-gap);
}

.todo-panel,
.message-panel {
  border-radius: var(--shell-radius-lg);
  background: var(--shell-surface);
  box-shadow: var(--shell-shadow);
}

.todo-list,
.message-list {
  max-height: 380px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.todo-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  border-radius: var(--shell-radius-base);
  border: 1px solid var(--shell-border-color);
}

.todo-item.is-complete {
  opacity: 0.6;
  text-decoration: line-through;
}

.todo-actions {
  display: flex;
  gap: 8px;
}

.message-item {
  border: 1px solid var(--shell-border-color);
  border-radius: var(--shell-radius-base);
  padding: 12px;
  background: transparent;
  display: flex;
  gap: 12px;
  text-align: left;
  cursor: pointer;
}

.message-item.is-unread {
  border-color: var(--color-primary);
}

.message-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--shell-border-color);
  margin-top: 6px;
}

.message-dot.is-unread {
  background: var(--color-primary);
}

.message-body {
  flex: 1;
}

.message-body h4 {
  margin: 0;
  font-size: 14px;
}

.message-body span {
  font-size: 12px;
  color: var(--shell-muted-text-color);
}

.message-body p {
  margin: 4px 0 0;
  color: var(--shell-muted-text-color);
  font-size: 13px;
}

.list-enter-active,
.list-leave-active {
  transition: all 0.3s ease;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.mt-4 {
  margin-top: 16px;
}

@media (max-width: 768px) {
  .profile-card__grid {
    grid-template-columns: 1fr;
    text-align: center;
  }

  .profile-header {
    flex-direction: column;
    gap: 8px;
  }
}
</style>
