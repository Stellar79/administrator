<template>
    <div>
        <el-form :inline="true">
            <el-form-item>
                <el-input v-model="searchForm.name" placeholder="name" clearable></el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="getRoleList">Search</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="dialogVisible=true">Add</el-button>
            </el-form-item>
            <el-form-item>
                <el-popconfirm title="Sure to batch delete?" @confirm="deleteHandle(null)">
                    <el-button type="danger" slot="reference" :disabled="deleteBtnStatus">Batch Delete</el-button>
                </el-popconfirm>
            </el-form-item>
        </el-form>

        <el-table ref="multipleTable" :data="tableData" tooltip-effect="dark" style="width: 100%" border stripe
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column prop="name" label="Name" width="120">

            </el-table-column>
            <el-table-column prop="uniqueCode" label="Unique Code" width="120" show-overflow-tooltip>
            </el-table-column>
            <el-table-column prop="remark" label="Remark" show-overflow-tooltip>
            </el-table-column>
            <el-table-column prop="status" label="Status" width="120">
                <template slot-scope="scope">
                    <el-tag size="small" v-if="scope.row.status === 1" type="success">Normal</el-tag>
                    <el-tag size="small" v-else-if="scope.row.status === 0" type="danger">Frozen</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="icon" label="Operation">
                <template slot-scope="scope">
                    <el-button type="text" @click="permissionHandle(scope.row.id)">Assign Permission</el-button>
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
            <el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="130px" class="demo-editForm">

                <el-form-item label="Role Name" prop="name" label-width="100px">
                    <el-input v-model="editForm.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="Unique Code" prop="uniqueCode" label-width="100px">
                    <el-input v-model="editForm.uniqueCode" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="Remark" prop="remark" label-width="100px">
                    <el-input v-model="editForm.remark" autocomplete="off"></el-input>
                </el-form-item>

                <el-form-item label="Status" prop="status" label-width="100px">
                    <el-radio-group v-model="editForm.status">
                        <el-radio :label=0>Frozen</el-radio>
                        <el-radio :label=1>Normal</el-radio>
                    </el-radio-group>
                </el-form-item>





                <el-form-item>
                    <el-button type="primary" @click="submitForm('editForm')">Create</el-button>
                    <el-button @click="resetForm('editForm')">Cancel</el-button>
                </el-form-item>
            </el-form>

        </el-dialog>
        <el-dialog title="Permission Assignment" :visible.sync="permissionDialogVisible" width="600px">
            <el-form :model="permissionForm">
                <el-tree :data="permissionTreeData" show-checkbox node-key="id" ref="permissionTree"
                    :props="defaultProps" :default-expand-all=true :check-strictly=true>
                </el-tree>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="permissionDialogVisible = false">Cancel</el-button>
                <el-button type="primary" @click="submitPermissionForm('permissionForm')">OK</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    export default {
        name: 'Role',

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

                    name: [{
                        required: true,
                        message: 'Please enter the name',
                        trigger: 'blur'
                    }],
                    uniqueCode: [{
                        required: true,
                        message: 'Please enter the unique code',
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
                permissionDialogVisible: false,
                permissionForm: {

                },
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                permissionTreeData: []
            }
        },
        created() {
            this.getRoleList()
            this.$axios.get('/sys/menu/list').then(res => {
                this.permissionTreeData = res.data.data
            })
        },

        mounted() {

        },

        methods: {

            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post('/sys/role/' + (this.editForm.id ? 'update' : 'save'), this
                                .editForm)
                            .then(res => {
                                this.$message({
                                    showClose: true,
                                    message: 'Successful operation!',
                                    type: 'success',
                                    onClose: () => {
                                        this.getRoleList()
                                    }
                                });

                                this.dialogVisible = false
                                this.resetForm(formName)

                            })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            getRoleList() {
                this.$axios.get("/sys/role/list", {
                    params: {
                        name: this.searchForm.name,
                        size: this.size,
                        current: this.current
                    }
                }).then(res => {
                    this.tableData = res.data.data.records
                    this.size = res.data.data.size
                    this.current = res.data.data.current
                    this.total = res.data.data.total
                })

            },
            editHandle(id) {
                this.$axios.get('/sys/role/info/' + id).then(res => {
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

                console.log(ids)

                this.$axios.post('/sys/role/delete', ids)
                    .then(res => {
                        this.$message({
                            showClose: true,
                            message: 'Successful operation!',
                            type: 'success',
                            onClose: () => {
                                this.getRoleList()
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
                this.getRoleList()
            },
            handleCurrentChange(val) {
                console.log(`当前页: ${val}`);
                this.current = val
                this.getRoleList()
            },
            permissionHandle(id) {
                this.permissionDialogVisible = true

                this.$axios.get("/sys/role/info/" + id).then(res => {
                        // console.log()

                        let Ids = res.data.data.menuIds
                        // console.log(Ids)
                        this.$refs.permissionTree.setCheckedKeys(Ids)



                        this.permissionForm = res.data.data
                    }

                )
            },
            submitPermissionForm() {

                var menuIds = this.$refs.permissionTree.getCheckedKeys()

                console.log(menuIds)

                this.$axios.post('/sys/role/permission/' + this.permissionForm.id, menuIds)
                    .then(res => {
                        this.$message({
                            showClose: true,
                            message: 'Successful operation!',
                            type: 'success',
                            onClose: () => {
                                this.getRoleList()
                            }
                        });
                        this.permissionDialogVisible = false
                        this.resetForm(formName)

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