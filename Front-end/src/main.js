import global from './globalFuc'
import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css';
import axios from './axios'

Vue.prototype.$axios = axios //global
Vue.config.productionTip = false

// require("./mock.js")

Vue.use(ElementUI)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
