<template>
    <div class="default-main ba-table-box">
        <TableHeader v-model="queryParams.title" @AddMenu="handelAdd" @RefreshMenu="getData"
                     placeholder="请输入菜单名称"/>
        <el-table :data="tableData" row-key="id" v-loading="tableLoading" class="ba-data-table w100">
            <el-table-column label="菜单名称" prop="title"/>
            <el-table-column label="菜单路径" prop="path"/>
            <el-table-column label="组件路径" prop="component"/>
            <el-table-column label="菜单权限" prop="name"/>
            <el-table-column fixed="right" label="操作" width="150">
                <template #default="scope">
                    <el-button type="primary" :icon="Edit" size="small" @click="handelEdit(scope.row)"/>
                    <el-button type="danger" :icon="Delete" size="small" @click="handelDelete(scope.row)"/>
                </template>
            </el-table-column>
        </el-table>

        <FromDialog v-model="dialogVisible" :title="title" :state="state" :menuData="tableData" :menuInfo="menuInfo"
                    @on-submit="getData"/>
    </div>
</template>
<script setup lang="ts">
import {
    Edit,
    Delete,
} from '@element-plus/icons-vue'
import TableHeader from '/@/components/tableHeader/index.vue'
import FromDialog from './from.vue'
import {ref, onMounted, reactive} from 'vue'
import {getMenuRulesAll, delMenuRules} from '/@/api/backend/auth/menu'
import {ElMessage, ElMessageBox} from "element-plus";

const tableData = ref([])
const queryParams = reactive({
    title: null,
    name: null,
    component: null,
    permissions: null,
})
const title = ref('新增菜单')
const state = ref('add')
const menuInfo = ref({})
const tableLoading = ref(false)
const dialogVisible = ref(false)

const getData = () => {
    tableLoading.value = true
    getMenuRulesAll(queryParams).then((res: any) => {
        tableData.value = res.data
        tableLoading.value = false
    })
}

const handelDelete = (row: any) => {
    ElMessageBox.confirm(
        '请再次确认删除该菜单吗?',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(() => {
            delMenuRules({ids: row.id}).then((res: any) => {
                if (res.code === 200) {
                    ElMessage.success("删除成功！")
                    getData()
                }
            })
        })
}

const handelEdit = (row: any) => {
    title.value = '编辑菜单'
    menuInfo.value = row
    state.value = 'edit'
    dialogVisible.value = true
}
const handelAdd = () => {
    title.value = '新增菜单'
    state.value = 'add'
    dialogVisible.value = true
}
onMounted(() => {
    getData()
})

</script>


<style scoped lang="scss"></style>
