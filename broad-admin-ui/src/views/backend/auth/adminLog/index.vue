<template>
    <div class="default-main ba-table-box">
        <TableHeader :data="tableData" :checkbox="true" :columns="columns"/>
    </div>
</template>

<script setup lang="ts">
import TableHeader from '/@/components/tableHeader/table.vue'
import {h} from 'vue'
import {ElButton, ElTag} from "element-plus";
import {TableRenderProps} from "/@/utils/tableRender";


const columns: TableRenderProps[] = [
    {
        title: '日期',
        key: 'date',
        render: (row: any) => {
            return h(ElTag, {}, row.date)
        }
    },
    {
        title: '姓名',
        key: 'name',
    },
    {
        title: '地址',
        key: 'address',
        align: 'center',

    },
    {
        title: '操作',
        key: 'action',
        fixed: 'right',
        width: 150,
        align: 'center',
        render: (row: any) => {
            const btn = [
                h(ElButton, {
                    icon: 'el-icon-EditPen',
                    onClick: () => {
                        console.log(row)
                    }
                }),
                h(ElButton, {
                    icon: 'el-icon-Delete',
                    onClick: () => {
                        console.log(row)
                    }
                })
            ]
            return btn;
        }
    }
]

const tableData = [
    {
        date: '2016-05-03',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-02',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-04',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-01',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
]

const x = (row: any) => {
    console.log(row)
}
</script>


<style scoped lang="scss">
</style>
