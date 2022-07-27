<template>
    <div class="default-main ba-table-box">
        <TableHeader :data="tableData" :checkbox="true"
                     :columns="columns"
                     :btn-list="['add', 'delete']"
                     :loading="tableLoading"
                     :total="queryParameters.total"
                     @on-refresh="getTableData"
                     @on-delete="deleteData"
                     @on-edit="editData"
                     @on-add="handAdd"
                     :background="true"
                     v-model:page-size="queryParameters.size"
                     v-model:current-page="queryParameters.current"
        />

        <div>
            <el-dialog
                v-model="dialogVisible"
                :title="title"
                custom-class="ba-operate-dialog"
                :close-on-click-modal="false"
                :show-close="false"
                width="40%"
                draggable
            >
                <div style="padding: 20px">
                    <el-form ref="formRef" :model="formData" :rules="rules" label-width="80px">
                        <el-form-item label="昵称" prop="nickName">
                            <el-input v-model="formData.nickName" placeholder="请输入管理员昵称"></el-input>
                        </el-form-item>
                        <el-form-item label="账号" prop="userName">
                            <el-input v-model="formData.userName" placeholder="请输入管理员账号"></el-input>
                        </el-form-item>
                        <el-form-item label="密码" prop="password">
                            <el-input v-model="formData.password" type="password" placeholder="请输入管理员密码"></el-input>
                        </el-form-item>
                        <el-form-item label="重复密码" prop="password_two">
                            <el-input v-model="formData.password_two" type="password"
                                      placeholder="请再次输入管理员密码"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱" prop="email">
                            <el-input v-model="formData.email" placeholder="请输入邮箱"></el-input>
                        </el-form-item>
                        <el-form-item label="手机号" prop="mobile">
                            <el-input v-model="formData.mobile" placeholder="请输入手机号"></el-input>
                        </el-form-item>
                        <el-form-item label="角色" prop="roleId">
                            <el-tree-select
                                v-model="formData.roleId"
                                :data="roleList"
                                node-key="id"
                                :props="mirror"
                                check-strictly
                                :render-after-expand="false"
                                placeholder="请选择管理员菜单"
                            />
                        </el-form-item>
                        <el-form-item label="状态" prop="userStatus">
                            <el-radio-group v-model="formData.userStatus" placeholder="请选择管理员状态">
                                <el-radio label="0" border>启用</el-radio>
                                <el-radio label="1" border>禁用</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-form>
                </div>
                <template #footer>
                  <span class="dialog-footer">
                       <el-button @click="dialogVisible = false">取消</el-button>
                       <el-button v-if="title==='添加管理员'" type="primary" @click="handAddAdmin(formRef)"
                       >添加</el-button>
                      <el-button v-if="title!=='添加管理员'" type="primary" @click="handUpdateAdmin(formRef)"
                      >修改</el-button>
                  </span>
                </template>
            </el-dialog>
        </div>
    </div>
</template>

<script setup lang="ts">
import TableHeader from '/@/components/tableHeader/table.vue'
import {getAdminList, addAdmin, detectAdmin, updateAdmin} from '/@/api/backend/auth/admin'
import {getMenuRules} from '/@/api/backend/auth/group'
import {onMounted, reactive, ref} from "vue";
import {TableRenderProps} from "/@/utils/tableRender";
import {ElButton, ElMessage, ElTag} from "element-plus";
import {ElMessageBox} from "element-plus/es";

const formRef: any = ref('')

const tableData: any = ref([])

const title = ref('添加管理员')

const roleList: any = ref([])

const tableLoading = ref(false)

const formData = ref({
    nickName: '',
    userName: '',
    password: '',
    password_two: '',
    roleId: '',
    userStatus: 0,
})

const mirror = {
    id: 'id',
    label: 'name',
    children: 'children',
}

const validatePass = (rule: any, value: any, callback: any) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if (value != formData.value.password) {
        callback(new Error('两次输入密码不一致'))
    } else {
        callback()
    }
}

const rules = {
    nickName: [
        {required: true, message: '请输入管理员昵称', trigger: 'blur'},
        {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'},
    ],
    userName: [
        {required: true, message: '请输入管理员账号', trigger: 'blur'},
        {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'},
    ],
    password: [
        {required: true, message: '请输入管理员密码', trigger: 'blur'},
        {min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur'},
    ],
    password_two: [
        {required: true, validator: validatePass, trigger: 'blur'},
    ],
    roleId: [
        {required: true, message: '请选择管理员角色', trigger: 'blur'},
    ],
    userStatus: [
        {required: true, message: '请选择管理员状态', trigger: 'blur'},
    ],
}

const dialogVisible = ref(false)

const queryParameters = reactive({
    current: 1,
    size: 10,
    total: 0,
    start_time: '',
    end_time: '',
})

const columns: TableRenderProps[] = [
    {
        title: '账号',
        key: 'userName',
    },
    {
        title: '昵称',
        key: 'nickName',
    },
    {
        title: '邮箱',
        key: 'email',
    },
    {
        title: '手机号',
        key: 'mobile',
    },
    {
        title: '角色',
        key: 'groupName',
        render: (params: any, h: any) => {
            return h(ElTag, {}, params.groupName)
        }
    },
    {
        title: '上次登录时间',
        key: 'lastLogintime',
    },
    {
        title: '上次登录IP',
        key: 'lastLoginip',
    },
    {
        title: '状态',
        key: 'status',
        align: 'center',
        render: (row: any, h: any) => {
            let type = ''
            let message = ''
            switch (row.userStatus) {
                case '0':
                    type = 'success'
                    message = '启用'
                    break
                case '1':
                    type = 'error'
                    message = '禁用'
                    break
                default:
                    type = 'warning'
                    message = '未知'
                    break
            }
            return h(ElTag, {type: type}, message)
        }
    },
    {
        title: '注册时间',
        key: 'createTime',
        align: 'center',
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
];

onMounted(() => {
    getTableData()
})

const getTableData = () => {
    tableLoading.value = true
    getAdminList(queryParameters).then((res: { data: { records: any; total: number; }; }) => {
        tableData.value = res.data.records;
        queryParameters.total = res.data.total;
        tableLoading.value = false
        getRoles()
    })
}

const getRoles = () => {
    getMenuRules({size: 500}).then((res: { data: any; }) => {
        roleList.value = res.data.records
    })
}


const handUpdateAdmin = (refEml: any) => {
    refEml.validate((valid: boolean) => {
        if (valid) {
            updateAdmin(formData.value).then((res: any) => {
                if (res.code === 200) {
                    dialogVisible.value = false
                    getTableData()
                    ElMessage.success('修改成功')
                }
            })
        }
    })
}

const handAddAdmin = (refEml: any) => {
    refEml.validate((valid: boolean) => {
        if (valid) {
            addAdmin(formData.value).then((res: any) => {
                if (res.code === 200) {
                    dialogVisible.value = false
                    getTableData()
                    ElMessage.success('添加成功')
                }
            })
        }
    })
}

const handelDelete = (row: any) => {
    ElMessageBox.confirm(
        '请再次确认删除 ' + row.userName + ' 管理员用户吗?',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(() => {
            detectAdmin({'idList': row.id}).then((res: any) => {
                if (res.code === 200) {
                    ElMessage.success("删除成功！")
                    getTableData()
                }
            })
        })
}

const deleteData = (rows: []) => {
    let idList: any[] = []
    let userNameList: any[] = []
    rows.forEach((row: any) => {
        idList.push(row.id)
        userNameList.push(row.userName)
    })
    ElMessageBox.confirm(
        '请再次确认删除 ' + userNameList.join('，') + ' 管理员用户吗?',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
        .then(() => {
            detectAdmin({'idList': idList.join(',')}).then((res: any) => {
                if (res.code === 200) {
                    ElMessage.success("删除成功！")
                    getTableData()
                }
            })
        })
}

const handelEdit = (row: any) => {
    title.value = '编辑管理员'
    dialogVisible.value = true
    formData.value = row
    formData.value.password_two = row.password
}

const handAdd = () => {
    title.value = '添加管理员'
    dialogVisible.value = true
}
</script>

<style scoped lang="scss"></style>
