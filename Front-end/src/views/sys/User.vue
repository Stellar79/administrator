
<template>
    <div>
        <el-form :inline="true">
            <el-form-item>
                <el-input v-model="searchForm.username" placeholder="username" clearable>
                </el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="getUserList">Search</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="dialogVisible=true" v-if="hasAuth('sys:user:save')">Add</el-button>
            </el-form-item>
            <el-form-item>
                <el-popconfirm title="Sure to batch delete?" @confirm="deleteHandle(null)">
                    <el-button type="danger" slot="reference" :disabled="deleteBtnStatus"
                        v-if="hasAuth('sys:user:delete')">Batch Delete</el-button>
                </el-popconfirm>
            </el-form-item>
        </el-form>

        <el-table ref="multipleTable" :data="tableData" tooltip-effect="dark" style="width: 100%" border stripe
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column label="Avatar" width="80">
                <template slot-scope="scope">
                    <el-avatar size="small" :src="scope.row.avatar"></el-avatar>
                </template>
            </el-table-column>
            <el-table-column prop="username" label="User" width="120">
            </el-table-column>


            <el-table-column width="180" label="Roles">
                <template slot-scope="scope">
                    <el-tag style="margin-right: 5px;" size="small" type="info" v-for="item in scope.row.roles">
                        {{item.name}}</el-tag>

                </template>
            </el-table-column>
            <el-table-column prop="email" label="Email">

            </el-table-column>
            <el-table-column prop="phoneNum" label="Phone Number">
            </el-table-column>
            <el-table-column prop="status" label="Status">
                <template slot-scope="scope">
                    <el-tag size="small" v-if="scope.row.status === 1" type="success">Normal</el-tag>
                    <el-tag size="small" v-else-if="scope.row.status === 0" type="danger">Frozen</el-tag>
                </template>
            </el-table-column>

            <el-table-column prop="created" label="Created Time" width="200px">

            </el-table-column>
            <el-table-column prop="icon" label="Operation" width="260">
                <template slot-scope="scope">
                    <el-button type="text" @click="roleHandle(scope.row.id)">Assign Role</el-button>
                    <el-divider direction="vertical"></el-divider>
                    <el-button type="text" @click="resetPwdHandle(scope.row.id, scope.row.username)">Reset Password
                    </el-button>
                    <el-divider direction="vertical"></el-divider>
                    <el-button type="text" @click="editHandle(scope.row.id)">Edit</el-button>
                    <el-divider direction="vertical"></el-divider>
                    <template>
                        <el-popconfirm title="Sure to delete?" @confirm="deleteHandle(scope.row.id)">
                            <el-button type="text" slot="reference">Delete</el-button>
                        </el-popconfirm>
                    </template>
                </template>
            </el-table-column>
        </el-table>

        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="current"
            :page-sizes="[10, 20, 50, 100]" :page-size="size" layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>

        <el-dialog title="Hint" :visible.sync="dialogVisible" width="600px" :before-close="handleClose">
            <el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="100px">

                <el-form-item label="username" prop="username" label-width="100px">
                    <el-input v-model="editForm.username" autocomplete="off"></el-input>
                    <el-alert title="初始密码为888888" :closable="false" type="info" style="line-height: 12px;">

                    </el-alert>
                </el-form-item>
                <el-form-item label="Email" prop="email" label-width="100px">
                    <el-input v-model="editForm.email" autocomplete="off"></el-input>
                </el-form-item>

                <el-form-item label="Phone Number" prop="phoneNum" label-width="100px">
                    <el-input v-model="editForm.phoneNum" autocomplete="off"></el-input>
                </el-form-item>


                <el-form-item label="Status" prop="status" label-width="100px">
                    <el-radio-group v-model="editForm.status">
                        <el-radio :label=0>Frozen</el-radio>
                        <el-radio :label=1>Normal</el-radio>
                    </el-radio-group>
                </el-form-item>




                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button @click="submitForm('editForm')">Submit</el-button>
                    <el-button @click="resetForm('editForm')">Cancel</el-button>
                </div>
           

        </el-dialog>
        <el-dialog title="Role Assignment" :visible.sync="roleAssignDialogVisible" width="600px">
            <el-form :model="roleForm">
                <el-tree :data="roleTreeData" show-checkbox node-key="id" ref="roleTree" :props="defaultProps"
                    :default-expand-all=true :check-strictly=true>
                </el-tree>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="roleAssignDialogVisible = false">Cancel</el-button>
                <el-button type="primary" @click="submitRoleHandle('roleForm')">Submit</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    export default {

        data() {
            return {
                searchForm: {

                },
                editForm: {

                },


                deleteBtnStatus: true,
                total: 0,
                size: 10,
                current: 1,

                editFormRules: {

                    username: [{
                        required: true,
                        message: 'Please enter the name',
                        trigger: 'blur'
                    }],
                    email: [{
                        required: true,
                        message: 'Please enter the email',
                        trigger: 'blur'
                    }],


                    status: [{
                        required: true,
                        message: 'please select status',
                        trigger: 'blur'
                    }]
                },


                dialogVisible: false,
                tableData: [

                ],

                multipleSelection: [],
                roleAssignDialogVisible: false,
                roleForm: {

                },
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                roleTreeData: [],
                treeCheckedKeys: [],
                checkStrictly: true

            }
        },
        created() {
            this.getUserList()
            this.$axios.get('/sys/role/list').then(res => {
                this.roleTreeData = res.data.data.records
                console.log(this.roleTreeData)
            })
        },

        mounted() {

        },

        methods: {

            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post('/sys/user/' + (this.editForm.id ? 'update' : 'save'), this
                                .editForm)
                            .then(res => {
                                this.$message({
                                    showClose: true,
                                    message: 'Successful operation!',
                                    type: 'success',
                                    onClose: () => {
                                        this.getUserList()
                                    }
                                });

                                this.dialogVisible = false

                            })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            getUserList() {
                this.$axios.get("/sys/user/list", {
                    params: {
                        username: this.searchForm.username,
                        size: this.size,
                        current: this.current
                    }
                }).then(res => {
                    this.tableData = res.data.data.records
                    this.size = res.data.data.size
                    this.current = res.data.data.current
                    this.total = res.data.data.total
                    console.log(this.tableData)
                })

            },
            editHandle(id) {
                this.$axios.get('/sys/user/info/' + id).then(res => {
                    this.editForm = res.data.data
                    this.dialogVisible = true
                })
            },
            deleteHandle(id) {
                var ids = []

                if (id) {
                    ids.push(id)
                } else {
                    this.multipleSelection.forEach(row =>
                        ids.push(row.id))
                }

                this.$axios.post('/sys/user/delete', ids)
                    .then(res => {
                        this.$message({
                            showClose: true,
                            message: 'Successful operation!',
                            type: 'success',
                            onClose: () => {
                                this.getUserList()
                            }
                        })

                    });

            },
            resetForm(formName) {
                this.$refs[formName].resetFields()
                this.dialogVisible = false
                this.editForm = {}
            },
            handleClose() {
                this.handleClose = this.resetForm('editForm')
            },
            toggleSelection(rows) {
                if (rows) {
                    rows.forEach(row => {
                        this.$refs.multipleTable.toggleRowSelection(row);
                    });
                } else {
                    this.$refs.multipleTable.clearSelection();
                }
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
                this.deleteBtnStatus = val.length == 0
            },
            handleSizeChange(val) {
                console.log(`每页 ${val} 条`);
                this.size = val
                this.getUserList()
            },
            handleCurrentChange(val) {
                console.log(`当前页: ${val}`);
                this.current = val
                this.getUserList()
            },
            roleHandle(id) {
                this.roleAssignDialogVisible = true

                this.$axios.get("/sys/user/info/" + id).then(res => {
                        this.roleForm = res.data.data
                        console.log(this.roleForm)

                        let roleIds = []
                        // console.log(roleIds)
                        res.data.data.roles.forEach(row => {
                            roleIds.push(row.id)
                        })
                        console.log(roleIds)
                        // this.$axios.get("/sys/role/list").then(res => {
                        //     this.roleTreeData = res.data.data.records
                            this.$refs.roleTree.setCheckedKeys(roleIds);
                        // })

                    }

                )
            },
            submitRoleHandle(formName) {

                var roleIds = this.$refs.roleTree.getCheckedKeys()

                console.log(roleIds)

                this.$axios.post('/sys/user/role_assign/' + this.roleForm.id, roleIds)
                    .then(res => {
                        this.$message({
                            showClose: true,
                            message: 'Successful operation!',
                            type: 'success',
                            onClose: () => {
                                this.getUserList()
                            }
                        });
                        this.roleAssignDialogVisible = false

                    })

            },
            resetPwdHandle(id, username) {
                this.$confirm('Confirm to reset the password of user' + username, 'Hint', {
                    confirmButtonText: 'Confirm',
                    cancelButtonText: 'Cancel',
                    type: "warning"
                }).then(() => {
                    this.$axios.post("/sys/user/rest_pwd", id).then(res => {
                        this.$message({
                            showClose: true,
                            message: 'Successful operation!',
                            type: 'success',
                            onClose: () => {

                            }
                        })
                    })
                })
            }
        }
    }
</script>

<style scoped>
    .el-pagination {
        float: right;
        margin-top: 22px;
    }
</style>