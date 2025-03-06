import { request } from '@/api/request'

// 获取用户标签列表
export const getUserTags = (userId: string) => {
  return request.get({
    url: 'sysUserTag',
    data: { userId }
  })
}

// 添加用户标签
export const addUserTag = (data: any) => {
  return request.post({
    url: 'sysUserTag',
    data
  })
}

// 删除用户标签
export const deleteUserTag = (id: string) => {
  return request.delete({
    url: 'sysUserTag',
    data: { idList: [id] }
  })
}
