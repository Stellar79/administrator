<template>
    <el-container>
        <el-aside width="200px">
            <SideMenu></SideMenu>
        </el-aside>
        <el-container>
            <el-header>

                <strong>Background Management</strong>
                <div class="header-avatar">
                    <el-avatar size="medium" :src="userInfo.avatar">
                    </el-avatar>
                    <el-dropdown>
                        <span class="el-dropdown-link">
                           {{userInfo.username}} <i class="el-icon-arrow-down el-icon--right"></i>
                        </span>
                        <el-dropdown-menu slot="dropdown">

                            <el-dropdown-item>
                                <router-link to="/userCenter">Individual</router-link>
                            </el-dropdown-item>
                            <el-dropdown-item @click.native="logout">Logout</el-dropdown-item>

                        </el-dropdown-menu>
                    </el-dropdown>
                    <el-link href="https//....com" target="_blank">Site</el-link>
                    <el-link href="https//....com" target="_blank">Bilibili</el-link>


                </div>

            </el-header>
            <el-main>
                <Tabs></Tabs><div style="margin: 15px;">
                <router-view></router-view></div>
            </el-main>
        </el-container>
    </el-container>

</template>

<script>
    import SideMenu from "./inc/SideMenu.vue";
    import Tabs from "./inc/Tabs.vue"
    export default {
        name: "Home",
        components: {
    SideMenu,
    Tabs
},
        data() {
            return {
                userInfo: {
                    id: "",
                    username: "",
                    avatar: ""

                },

            }
        },
        created() {
           
            this.getUserInfo()

        },
        methods: {
            getUserInfo() { 
        
                this.$axios.get("/sys/userInfo").then(res => {
                    this.userInfo = res.data.data
                })
            },
            logout(){
                this.$axios.post("/logout").then(res => {
                    localStorage.clear();
                    sessionStorage.clear();

                    this.$store.commit("resetState")
                    this.$router.push("/login")
                })

            }
        }

    }
</script>

<style scoped>
    .el-dropdown-link {
        cursor: pointer;
    }

    .el-icon-arrow-down {
        font-size: 12px;
    }

    .header-avatar {
        float: right;
        width: 210px;
        display: flex;
        justify-content: space-around;
        align-items: center;

    }

    .el-container {
        padding: 0;
        margin: 0;
        height: 100%;

    }

    .el-header {
        background-color: #17b3a3;
        color: #333;
        text-align: center;
        line-height: 60px;
    }

    .el-aside {
        background-color: #D3DCE6;
        color: #333;

        line-height: 200px;
    }

    .el-main {

        color: #333;

        padding: 0;
    }

    a {
        text-decoration: none;

    }
</style>>