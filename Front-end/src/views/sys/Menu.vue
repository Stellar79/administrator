<template>
    <div>
        <el-form :inline="true">
            <el-form-item>
                <el-button type="primary" @click="dialogVisible=true">Add</el-button>
            </el-form-item>
        </el-form>
        <el-table :data="tableData" style="width: 100%;margin-bottom: 20px;" row-key="id" border stripe
            default-expand-all :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
            <el-table-column prop="name" label="Name" sortable width="180"></el-table-column>
            <el-table-column prop="permissionCode" label="Permission Code" sortable width="180"></el-table-column>
            <el-table-column prop="icon" label="Icon"></el-table-column>
            <el-table-column prop="type" label="Type">
                <template slot-scope="scope">
                    <el-tag size="small" v-if="scope.row.type === 0">Column</el-tag>
                    <el-tag size="small" v-else-if="scope.row.type === 1" type="success">Menu</el-tag>
                    <el-tag size="small" v-else-if="scope.row.type === 2" type="info">Button</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="path" label="URL of Menu"></el-table-column>
            <el-table-column prop="component
             label="Component of Menu"></el-table-column>
            <el-table-column prop="sortNum" label="Sort Number"></el-table-column>
            <el-table-column prop="status" label="Status">
                <template slot-scope="scope">
                    <el-tag size="small" v-if="scope.row.status === 1" type="success">Normal</el-tag>
                    <el-tag size="small" v-else-if="scope.row.status === 0" type="danger">Frozen</el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="icon" label="Operation">
                <template slot-scope="scope">
                    <el-button type="text" @click="editHandle(scope.row.id)">Edit</el-button>
                    <el-divider direction="vertical"></el-divider>
                    <template>
                        <el-popconfirm title="Sure to delete?" @confirm="deleteHandle(scope.row.id)">
                            <el-button type="text" slot="reference" >Delete</el-button>
                        </el-popconfirm>

                    </template>
                </template>
            </el-table-column>

        </el-table>
        <el-dialog title="Tip" :visible.sync="dialogVisible" width="600px" :before-close="handleClose">
            <el-form :model="editForm" :rules="editFormRules" ref="editForm" label-width="130px" class="demo-editForm">
                <el-form-item label="Parent Menu" prop="parentId">
                    <el-select v-model="editForm.parentId" placeholder="Please select parent menu">
                        <template v-for="item in tableData">
                            <el-option :label="item.name" :value="item.id"></el-option>
                            <template v-for="child in item.children">
                                <el-option :label="child.name" :value="child.id">
                                    <span>
                                        {{"- " + child.name}}
                                    </span>
                                </el-option>
                            </template>
                        </template>
                    </el-select>
                </el-form-item>
                <el-form-item label="Name" prop="name">
                    <el-input v-model="editForm.name"></el-input>
                </el-form-item>
                <el-form-item label="Permission Code" prop="permCode" label-width="100px">
                    <el-input v-model="editForm.permCode" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="Icon" prop="icon" label-width="100px">
                    <el-input v-model="editForm.icon" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="URL of Menu" prop="path" label-width="100px">
                    <el-input v-model="editForm.path" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="Component" prop="component" label-width="100px">
                    <el-input v-model="editForm.component" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="Type" prop="type" label-width="100px">
                    <el-radio-group v-model="editForm.type">
                        <el-radio :label=0>Column</el-radio>
                        <el-radio :label=1>Menu</el-radio>
                        <el-radio :label=2>Button</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="Status" prop="status" label-width="100px">
                    <el-radio-group v-model="editForm.status">
                        <el-radio :label=0>Frozen</el-radio>
                        <el-radio :label=1>Normal</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="Sort Number" prop="sortNum" label-width="100px">
                    <el-input-number v-model="editForm.sortNum" :min="1" label="Sort Number">1</el-input-number>
                </el-form-item>





                <el-form-item>
                    <el-button type="primary" @click="submitForm('editForm')">Create</el-button>
                    <el-button @click="resetForm('editForm')">Cancel</el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>
<script>
    export default {
        name: 'AdminVue2Menu',

        data() {
            return {
                editForm: {

                },
                editFormRules: {
                    parentId: [{
                        required: true,
                        message: 'Please select parent menu',
                        trigger: 'blur'
                    }],
                    name: [{
                        required: true,
                        message: 'Please enter the name',
                        trigger: 'blur'
                    }],
                    permissionCode: [{
                        required: true,
                        message: 'Please enter the permission code',
                        trigger: 'blur'
                    }],
                    type: [{
                        required: true,
                        message: 'Please select type',
                        trigger: 'blur'
                    }],
                    sortNum: [{
                        required: true,
                        message: 'Please enter sort number',
                        trigger: 'blur'
                    }],
                    status: [{
                        required: true,
                        message: 'please select status',
                        trigger: 'blur'
                    }]
                },
                dialogVisible: false,
                tableData: [],


            }
        }

        ,
        created() {
            this.getMenuTree()
        },
        methods: {
            getMenuTree() {
                this.$axios.get("/sys/menu/list").then(res => {
                    this.tableData = res.data.data
                    console.log(this.tableData)
                })
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$axios.post('/sys/menu/' + (this.editForm.id ? 'update' : 'save'), this.editForm)
                            .then(res => {
                                this.$message({
                                    showClose: true,
                                    message: 'Successful operation!',
                                    type: 'success',
                                    onClose: () => {
                                        this.getMenuTree()
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
            editHandle(id) {
                this.$axios.get('/sys/menu/info/' + id).then(res => {
                    this.editForm = res.data.data
                    this.dialogVisible = true
                })
            },
            resetForm(formName) {
                this.$refs[formName].resetFields()
                this.dialogVisible = false
                this.editForm={}
            },
            handleClose(){
                thies.handleClose=this.resetForm('editForm')
            },
            deleteHandle(id){
                this.$axios.post(('/sys/menu/delete/' + id), this.editForm)
                .then(res => {
                                this.$message({
                                    showClose: true,
                                    message: 'Successful operation!',
                                    type: 'success',
                                    onClose: () => {
                                        this.getMenuTree()
                                    }
                                })

            });

},

        }
    }

    
        

        
</script>
<style lang="scss" scoped></style>