<template>
    <el-row type="flex" class="row-bg" justify="center">
        <el-col :xl="6" :lg="7">
            <h2>
                Vue Admin Management 
            </h2>
            <el-image :src="require('@/assets/seal.jpg')" style="height: 180px; width: 180px;" ></el-image>
            <p>Stellar</p>
        </el-col>
        <el-col :span="1">
             <el-divider direction="vertical"></el-divider>
        </el-col>
        <el-col :xl="6" :lg="7">
            <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="100px" class="demo-loginForm">
  <el-form-item label="username" prop="username" style="width: 380px;">
    <el-input v-model="loginForm.username" ></el-input>
  </el-form-item>
  <el-form-item label="password" prop="password"  style="width: 380px;">
    <el-input v-model="loginForm.password" type="password"></el-input>
  </el-form-item>
  <el-form-item label="captcha" prop="captcha" style="width: 380px;">
    <el-input v-model="loginForm.captcha" style="width: 172px; float: left;" maxlength="5"></el-input>
    <el-image :src="captchaImg" class="captchaImg" @click="getCaptcha()"></el-image>
  </el-form-item>
  <el-form-item>
    <el-button type="primary" @click="submitForm('loginForm')">Login</el-button>
    <el-button @click="resetForm('loginForm')">Reset</el-button>
  </el-form-item>
  
</el-form>
        </el-col>
    </el-row>
</template>

<script>
import qs from 'qs'

    export default {
        name: 'Login',
        data() {
      return {
        loginForm: {
          username: '',
          password: '',
          captcha: '',
          token: ''
        },
        rules: {
          username: [
            { required: true, message: 'Please type in your name.', trigger: 'blur' }, 
          ],
          password: [
            { required: true, message: 'Please enter your password.', trigger: 'blur' },
          ],
          captcha: [
            { required: true, message: 'Please enter the captcha', trigger: 'blur' },
            { min: 5, max: 5, message: 'The number of character must be 5!', trigger: 'blur' }
          ],

        },
        captchaImg: ''
      }
    },
  
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            this.$axios.post('/login?'+ qs.stringify(this.loginForm)).then(res =>{

                console.log(res)
                
                const jwt = res.headers['authorization']
                console.log(jwt)

                this.$store.commit('SET_TOKEN',jwt)

                this.$router.push('/index')
            } )
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      getCaptcha(){
    
        this.$axios.get('/captcha').then(res => {

            console.log("/captcha")
            console.log(res)
            

            this.loginForm.token = res.data.data.token;
        
            this.captchaImg = res.data.data.captchaImg
            this.loginForm.captcha = ''
        })

      },
      // Result: msg, code, data
     
    },
       created() {
        this.getCaptcha()
      },
    

    }
</script>
<style scoped>

.el-divider {
    height: 200px;
}
.el-row {
    background-color: #fafafa;
    height: 100vh;
    display: flex;
    align-items: center;
    text-align: center;
}
.captchaImg {
    float: left;
    margin-left: 8px;
    border-radius: 4px;

}


</style>