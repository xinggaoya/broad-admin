import { request } from '@/api/request'

/**
 * 获取用户消息列表
 * @param userId 用户ID
 */
export function getMessageList(userId: string) {
  return request.get({
    url: `/sysMessage?userId=${userId}`,
  })
}

/**
 * 获取未读消息数量
 * @param userId 用户ID
 */
export function getUnreadCount(userId: string) {
  return request.get({
    url: `/sysMessage/unread?userId=${userId}`,
  })
}

/**
 * 标记消息为已读
 * @param id 消息ID
 */
export function markAsRead(id: number) {
  return request.put({
    url: `/sysMessage/read?id=${id}`,
  })
}

/**
 * 删除消息
 * @param idList 消息ID列表
 */
export function deleteMessage(idList: number[]) {
  return request.delete({
    url: `/sysMessage?idList=${idList.join(',')}`,
  })
}

/**
 * 发送通知（管理员）
 * @param message 消息对象
 */
export function sendNotification(message: any) {
  return request.post({
    url: '/sysMessage/send',
    data: message,
  })
}

/**
 * 广播通知（管理员）
 * @param message 消息对象
 */
export function broadcastNotification(message: any) {
  return request.post({
    url: '/sysMessage/broadcast',
    data: message,
  })
}
