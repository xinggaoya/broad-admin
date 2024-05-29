<template>
  <div>
    <TableSearch @search="handleSearch" @reset="handleReset">
      <n-form ref="formRef" :label-width="100" inline :model="form" label-placement="left">
        <n-form-item-row>
          <n-form-item label="姓名" path="name">
            <n-input v-model:value="form.name" placeholder="请输入姓名" />
          </n-form-item>
          <n-form-item label="年龄" path="age">
            <n-input v-model:value="form.age" placeholder="请输入年龄" />
          </n-form-item>
          <n-form-item label="性别" path="sex">
            <n-input v-model:value="form.sex" placeholder="请输入性别" />
          </n-form-item>
        </n-form-item-row>
      </n-form>
    </TableSearch>

    <TableMain
      :data="tableData"
      :columns="tableColumns"
      :loading="tableLoading"
      :pagination="pagination"
    >
      <template v-slot:header>
        <AddButton @add="handleAddUser" v-auth="['sys:user:add']" />
        <DeleteButton @delete="onDeleteItem" />
      </template>
    </TableMain>
  </div>
</template>

<script lang="ts" setup>
import { usePagination } from '@/hooks/useTable'
import { useMessage } from 'naive-ui'
import { onMounted, ref } from 'vue'
import { data, columns } from '@/views/template/list/data'
import AddButton from '@/components/table/button/AddButton.vue'
import DeleteButton from '@/components/table/button/DeleteButton.vue'
import TableSearch from '@/components/table/search/TableSearch.vue'
import TableMain from '@/components/table/main/TableMain.vue'

const tableColumns = ref(columns)
const tableData = ref(data)
const tableLoading = ref(false)
const title = ref<string>('新增')
const form = ref({
  name: '',
  age: '',
  sex: ''
})
const message = useMessage()
const pagination = usePagination(doRefresh)

function doRefresh() {
  tableData.value = []
  tableLoading.value = true
  // 随机数number
  const number = Math.floor(Math.random() * 100)
  for (let i = number; i < number + 10; i++) {
    tableData.value.push({
      id: i,
      firstName: 'David_' + i,
      lastName: 'Lee',
      age: 44,
      email: 'david.lee@example.com',
      address: '789 Oak St',
      city: 'Somewhere',
      state: 'TX',
      country: 'USA',
      phone: '555-345-6789'
    })
  }
  pagination.setTotalSize(tableData.value.length + 3)
  tableLoading.value = false
}

function handleAddUser() {
  doRefresh()
  message.success('新增成功')
}

function onDeleteItem(item: any) {
  message.success('删除成功')
}

// 修改
function handleEditItem(item: any) {
  title.value = '修改'
}

// 搜索
function handleSearch() {
  doRefresh()
  message.success('搜索成功')
}

// 重置
function handleReset() {
  message.success('重置成功')
}

onMounted(async () => {
  doRefresh()
})
</script>

<style lang="scss" scoped></style>
