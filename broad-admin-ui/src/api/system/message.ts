import { request } from '@/api/request'

// 获取用户消息列表
export const getMessageList = (userId: string) => {
  return request.get({
    url: 'sysMessage',
    data: { userId }
  })
}

// 标记消息为已读
export const readMessage = (id: string) => {
  return request.put({
    url: 'sysMessage/read',
    data: { id }
  })
}

// 删除消息
export const deleteMessage = (id: string) => {
  return request.delete({
    url: 'sysMessage',
    data: { idList: [id] }
  })
}

// 获取未读消息数量
export const getUnreadCount = (userId: string) => {
  return request.get({
    url: 'sysMessage/unread',
    data: { userId }
  })
}
