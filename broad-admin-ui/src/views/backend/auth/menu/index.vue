<template>
    <div class="default-main ba-table-box">
        <TableHeader :data="tableData"
                     :columns="columns"
                     :btn-list="['add','query']"
                     :total="queryParams.total"
                     @on-add="handelAdd"
                     @on-refresh="getData"
                     :loading="tableLoading"
                     v-model:current-page="queryParams.current"
                     v-model:page-size="queryParams.size"
                     placeholder="请输入菜单名称"/>

        <FromDialog v-model="dialogVisible" :title="title" :state="state" :menuData="tableData" :menuInfo="menuInfo"
                    @on-submit="getData"/>
    </div>
</template>
<script setup lang="ts">
import TableHeader from '/@/components/tableHeader/table.vue'
import FromDialog from './from.vue'
import {ref, onMounted, reactive} from 'vue'
import {getMenuRulesAll, delMenuRules} from '/@/api/backend/auth/menu'
import {ElButton, ElMessage, ElMessageBox} from "element-plus";
import Icon from '/@/components/icon/index.vue'

const tableData = ref([])
const queryParams = reactive({
    title: null,
    name: null,
    component: null,
    current: 1,
    size: 10,
    total: 0,
})


const columns = [
    {
        title: '菜单名称',
        key: 'title',
    },
    {
        title: '菜单图标',
        key: 'icon',
        render: (row: any, h: any) => {
            if (row['icon']) {
                return h(Icon, {name: row['icon']})
            }
            return;

        }
    },
    {
        title: '菜单路径',
        key: 'path',
    },
    {
        title: '组件路径',
        key: 'component',
    },
    {
        title: '菜单权限',
        key: 'name',
    },
    {
        title: '操作',
        key: 'action',
        fixed: "right",
        width: 180,
        align: 'center',
        render: (params: object, h: any) => {
            return [
                h(ElButton, {
                    type: 'primary',
                    size: 'small',
                    icon: 'el-icon-EditPen',
                    onClick: () => {
                        handelEdit(params)
                    }
                }),
                h(ElButton, {
                    type: 'danger',
                    size: 'small',
                    icon: 'el-icon-Delete',
                    onClick: () => {
                        handelDelete(params)
                    }
                }),
            ]
        }
    }
]
const title = ref('新增菜单')
const state = ref('add')
const menuInfo = ref({})
const tableLoading = ref(false)
const dialogVisible = ref(false)

const getData = () => {
    tableLoading.value = true
    getMenuRulesAll(queryParams).then((res: any) => {
        tableData.value = res.data
        queryParams.total = res.data.total
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
            delMenuRules({'ids': row.id}).then((res: any) => {
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
