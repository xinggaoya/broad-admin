<template>
    <div class="default-main ba-table-box">
        <TableHeader :data="tableData" :checkbox="true"
                     :columns="columns"
                     :loading="tableLoading"
                     :total="queryParameters.total"
                     @on-refresh="getTabelData"
                     @on-delete="deleteData"
                     @on-edit="editData"
                     v-model:page-size="queryParameters.size"
                     v-model:current-page="queryParameters.current"
        />
    </div>
</template>

<script setup lang="ts">
import TableHeader from '/@/components/tableHeader/table.vue'
import {h, ref} from 'vue'
import {ElTag} from "element-plus";
import {TableRenderProps} from "/@/utils/tableRender";
import {getAdminLog} from "/@/api/backend/auth/adminLog";

const queryParameters = ref({
    current: 1,
    size: 10,
    total: 10,
    start_time: '',
    end_time: '',
})
const tableLoading = ref(false)

const columns: TableRenderProps[] = [
    {
        title: '标题',
        key: 'logDescription',
    },
    {
        title: '请求方式',
        key: 'logHttpMethod',
        render: (row: any) => {
            return h(ElTag, {}, row.logHttpMethod)
        }
    },
    {
        title: '请求地址',
        key: 'logUrl',
        align: 'center',

    },
    {
        title: '请求IP',
        key: 'logIpAddress',
        align: 'center',
    },
    {
        title: '操作类型',
        key: 'logMethodType',
        align: 'center',
        render: (row: any) => {
            let type = ''
            switch (row.logMethodType) {
                case 'INSERT':
                    type = '新增'
                    break
                case 'UPDATE':
                    type = '修改'
                    break
                case 'DELETE':
                    type = '删除'
                    break
                case 'GRANT':
                    type = '授权'
                    break
                case 'EXPORT':
                    type = '导出'
                    break
                case 'IMPORT':
                    type = '导入'
                    break
                case 'FORCE':
                    type = '强退'
                    break
                case 'GENCODE':
                    type = '生成代码'
                    break
                case 'CLEAN':
                    type = '清空数据'
                    break
                case 'OTHER':
                    type = '其他'
                    break
                default:
                    type = '未知'
                    break
            }
            return h(ElTag, {type: 'success'}, type)
        }
    },
    {
        title: '请求参数',
        key: 'logParams',
        showOverflowTooltip: true,
    },
    {
        title: '返回参数',
        key: 'logResult',
        showOverflowTooltip: true,
    },
    {
        title: '操作人',
        key: 'adminName',
    },
    {
        title: '操作时间',
        key: 'createTime',
        width: 200,
    },
    // {
    //     title: '操作',
    //     key: 'action',
    //     fixed: 'right',
    //     width: 150,
    //     align: 'center',
    //     render: (row: any) => {
    //         return [
    //             h(ElButton, {
    //                 icon: 'el-icon-EditPen',
    //                 onClick: () => {
    //                     console.log(row)
    //                 }
    //             }),
    //             h(ElButton, {
    //                 icon: 'el-icon-Delete',
    //                 onClick: () => {
    //                     console.log(row)
    //                 }
    //             })
    //         ];
    //     }
    // }
]

const tableData = ref([])

const getTabelData = () => {
    tableLoading.value = true
    getAdminLog(queryParameters.value).then((res) => {
        tableData.value = res.data.records;
        queryParameters.value.total = res.data.total;
        tableLoading.value = false
    })
}
getTabelData()

const deleteData = (rows: any) => {
    console.log(rows)
}

const editData = (rows: any) => {
    console.log(rows)
}
</script>


<style scoped lang="scss">


</style>
