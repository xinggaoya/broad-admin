<template>
  <div class="personal-container">
    <div class="personal-content">
      <!-- 左侧个人信息卡片 -->
      <n-card class="personal-info-card" :content-style="{ padding: '20px' }">
        <div class="info-wrapper">
          <!-- 头像上传区域 -->
          <div class="avatar-wrapper">
            <Upload :show-file-list="false" @success="handleUpload">
              <template #default>
                <div
                  class="avatar-container"
                  @mouseenter="handleAvatarHover"
                  @mouseleave="handleAvatarLeave"
                >
                  <n-avatar
                    round
                    :size="100"
                    :src="avatar"
                    :fallback-src="defaultAvatar"
                    class="user-avatar"
                    :class="{ 'avatar-hover': isAvatarHovered }"
                  />
                  <div class="avatar-overlay" :class="{ 'overlay-show': isAvatarHovered }">
                    <n-icon size="24" color="#fff">
                      <CameraOutline />
                    </n-icon>
                    <span class="upload-text">更换头像</span>
                  </div>
                </div>
              </template>
            </Upload>
          </div>

          <!-- 用户基本信息 -->
          <div class="user-info">
            <h2 class="nickname">{{ userInfo.nickName || userInfo.userName }}</h2>
            <div class="user-bio">
              <n-input
                v-model:value="userBio"
                type="textarea"
                placeholder="编辑个人简介..."
                :autosize="{ minRows: 2, maxRows: 3 }"
                class="bio-input"
                @blur="handleBioUpdate"
              />
            </div>

            <!-- 用户详细信息 -->
            <div class="info-list">
              <div class="info-item" v-for="(item, index) in userInfoList" :key="index">
                <n-icon :size="16" :color="item.color">
                  <component :is="item.icon" />
                </n-icon>
                <span class="info-label">{{ item.label }}：</span>
                <span class="info-value">{{ item.value }}</span>
              </div>
            </div>

            <!-- 用户标签 -->
            <div class="user-tags">
              <n-space>
                <n-tag
                  v-for="(tag, index) in userTags"
                  :key="index"
                  :type="tag.type"
                  size="small"
                  round
                  class="user-tag"
                >
                  {{ tag.name }}
                </n-tag>
                <n-button quaternary circle size="small" @click="handleAddTag" class="add-tag-btn">
                  <template #icon>
                    <n-icon><AddCircleOutline /></n-icon>
                  </template>
                </n-button>
              </n-space>
            </div>
          </div>
        </div>
      </n-card>

      <!-- 右侧内容区域 -->
      <div class="right-content">
        <!-- 待办事项卡片 -->
        <n-card class="todo-card" title="待办事项">
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
                :class="{ 'todo-completed': item.status === 1 }"
              >
                <n-checkbox
                  :checked="item.status === 1"
                  @update:checked="handleTodoStatusChange(index)"
                >
                  <span class="todo-text">{{ item.title }}</span>
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
            <div v-if="todoItems.length === 0" class="empty-status">
              <n-empty description="暂无待办事项" />
            </div>
          </div>
        </n-card>

        <!-- 消息中心卡片 -->
        <n-card class="message-card" title="消息中心">
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
              <div
                v-for="(item, index) in messages"
                :key="index"
                class="message-item"
                :class="{ 'message-unread': item.status === 0 }"
                @click="handleReadMessage(index)"
              >
                <div class="message-status">
                  <div class="status-dot" :class="{ 'dot-unread': item.status === 0 }" />
                </div>
                <div class="message-content">
                  <div class="message-header">
                    <h4 class="message-title">{{ item.title }}</h4>
                    <span class="message-time">{{ item.time || '刚刚' }}</span>
                  </div>
                  <p class="message-text">{{ item.content }}</p>
                </div>
              </div>
            </TransitionGroup>
            <div v-if="messages.length === 0" class="empty-status">
              <n-empty description="暂无消息" />
            </div>
          </div>
        </n-card>
      </div>
    </div>

    <!-- 添加/编辑待办对话框 -->
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

    <!-- 添加标签对话框 -->
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
  </div>
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

<style lang="scss" scoped>
.personal-container {
  padding: 20px;
  background-color: var(--body-color);
  min-height: 100%;

  .personal-content {
    display: flex;
    gap: 20px;
    flex-wrap: wrap;

    // 左侧个人信息卡片
    .personal-info-card {
      width: 360px;
      height: fit-content;

      @media (max-width: 768px) {
        width: 100%;
      }

      .info-wrapper {
        .avatar-container {
          position: relative;
          width: 100px;
          height: 100px;
          margin: 0 auto 20px;
          cursor: pointer;

          .user-avatar {
            border: 2px solid var(--primary-color);
            transition: all 0.3s ease;
          }

          .avatar-hover {
            filter: brightness(0.8);
          }

          .avatar-overlay {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            border-radius: 50%;
            background-color: rgba(0, 0, 0, 0.5);
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            opacity: 0;
            transition: opacity 0.3s ease;

            .upload-text {
              color: #fff;
              font-size: 12px;
              margin-top: 4px;
            }
          }

          .overlay-show {
            opacity: 1;
          }
        }

        .user-info {
          text-align: center;

          .nickname {
            font-size: 1.5rem;
            font-weight: 600;
            margin: 0 0 16px;
            color: var(--text-color-1);
          }

          .user-bio {
            margin-bottom: 20px;

            .bio-input {
              text-align: left;
            }
          }

          .info-list {
            text-align: left;
            padding: 0 20px;

            .info-item {
              display: flex;
              align-items: center;
              margin-bottom: 12px;
              color: var(--text-color-2);

              .info-label {
                margin: 0 8px;
                color: var(--text-color-3);
              }

              .info-value {
                flex: 1;
                color: var(--text-color-1);
              }
            }
          }

          .user-tags {
            margin-top: 20px;
            padding: 0 20px;

            .user-tag {
              transition: all 0.3s ease;

              &:hover {
                transform: translateY(-2px);
              }
            }

            .add-tag-btn {
              transition: all 0.3s ease;

              &:hover {
                transform: rotate(180deg);
              }
            }
          }
        }
      }
    }

    // 右侧内容区域
    .right-content {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 20px;
      min-width: 300px;

      // 待办事项卡片
      .todo-card {
        .todo-list {
          min-height: 80px;

          .todo-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 12px;
            border-radius: 4px;
            transition: all 0.3s ease;

            &:hover {
              background-color: var(--hover-color);

              .todo-actions {
                opacity: 1;
              }
            }

            .todo-text {
              margin-left: 8px;
              transition: all 0.3s ease;
            }

            &.todo-completed .todo-text {
              text-decoration: line-through;
              color: var(--text-color-3);
            }

            .todo-actions {
              opacity: 0;
              transition: opacity 0.3s ease;
              display: flex;
              gap: 8px;
            }
          }

          .empty-status {
            padding: 24px 0;
          }
        }
      }

      // 消息中心卡片
      .message-card {
        .message-list {
          min-height: 80px;

          .message-item {
            display: flex;
            padding: 16px;
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.3s ease;

            &:hover {
              background-color: var(--hover-color);
            }

            .message-status {
              padding-right: 12px;
              padding-top: 6px;

              .status-dot {
                width: 8px;
                height: 8px;
                border-radius: 50%;
                background-color: var(--text-color-3);

                &.dot-unread {
                  background-color: var(--primary-color);
                }
              }
            }

            .message-content {
              flex: 1;

              .message-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 8px;

                .message-title {
                  margin: 0;
                  font-size: 14px;
                  font-weight: 500;
                  color: var(--text-color-1);
                }

                .message-time {
                  font-size: 12px;
                  color: var(--text-color-3);
                }
              }

              .message-text {
                margin: 0;
                font-size: 13px;
                color: var(--text-color-2);
                line-height: 1.5;
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
                overflow: hidden;
              }
            }

            &.message-unread {
              .message-title {
                color: var(--primary-color);
                font-weight: 600;
              }
            }
          }

          .empty-status {
            padding: 24px 0;
          }
        }
      }
    }
  }
}

// 列表动画
.list-enter-active,
.list-leave-active {
  transition: all 0.3s ease;
}

.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

// 响应式优化
@media (max-width: 1024px) {
  .personal-container {
    .personal-content {
      flex-direction: column;

      .personal-info-card {
        width: 100%;
      }
    }
  }
}
</style>
