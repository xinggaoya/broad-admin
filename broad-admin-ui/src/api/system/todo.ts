import { request } from '@/api/request'

// 获取用户待办事项列表
export const getTodoList = (userId: string) => {
  return request.get({
    url: 'sysTodo',
    data: { userId }
  })
}

// 添加待办事项
export const addTodo = (data: any) => {
  return request.post({
    url: 'sysTodo',
    data
  })
}

// 更新待办事项
export const updateTodo = (data: any) => {
  return request.put({
    url: 'sysTodo',
    data
  })
}

// 删除待办事项
export const deleteTodo = (id: string) => {
  return request.delete({
    url: 'sysTodo',
    data: { idList: [id] }
  })
}
