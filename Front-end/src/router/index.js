import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Index from '../views/Index.vue'
import Menu from '../views/sys/Menu.vue'
import Role from '../views/sys/Role.vue'
import User from '../views/sys/User.vue'
import axios from '../axios'
import store from '../store'
import el from "element-ui/src/locale/lang/el";



Vue.use(VueRouter)

const routes = [{
    path: '/',
    name: 'Home',
    component: Home,
    children: [{
        path: '/index',
        name: 'Index',
        meta: {
          title: "Home"
        },
        component: Index
      },
      {
        path: '/userCenter',
        name: 'UserCenter',
        meta: {
          title: "User Center"
        },
        component: () => import( /* webpackChunkName: "about" */ '../views/UserCenter.vue')
      },
      // {
      //   path: '/sys/role',
      //   name: 'SysRole',
      //   component: Role
      // },
      // {
      //   path: '/sys/user',
      //   name: 'SysUser',
      //   component: User
      // },
      // {
      //   path: '/sys/menu',
      //   name: 'SysMenu',
      //   component: Menu
      // },
    ]
  },

  {
    path: '/login',
    name: 'Login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import( /* webpackChunkName: "about" */ '../views/Login.vue')
  }
]

const router = new VueRouter({
  mode: 'hash',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {

  let hasRoute = store.state.menus.hasRoute;



  let token = localStorage.getItem("token")
  if (to.path == '/login'){
    next()
  }else if(!token){
    next({path: '/login'})
  }else if (token && !hasRoute) {
    // if (!hasRoute){
    axios.get("/sys/menu/nav", {
      headers: {
        Authorization: localStorage.getItem("token")
      }

    }).then(res => {
      // console.log(res.data.data)
      // Get menu list from storage
      store.commit("setMenuList", res.data.data.nav)

      // Get current person's permission
      store.commit("setPermissionList", res.data.data.authorities)

      console.log(store.state.menus.menuList)

      // 动态绑定路由
      // get nav
      let newRoutes = router.options.routes
      // Whether there is a children menu exist
      res.data.data.nav.forEach(menus => {
        if (menus.children) {
          menus.children.forEach(e => {
            // 接收到navigation菜单后转换为路由的输出格式： {path: , name: , component: }
            let route = menuToRoute(e)
            // 添加到parent menu中
            if (route)
              newRoutes[0].children.push(route)

          })
        }

      })
      router.addRoutes(newRoutes)

      hasRoute = true
      store.commit("reverseRouteStatus",hasRoute)

    })
  }


  next();

})

const menuToRoute = (menu => {
  if (!menu.component) {
    return null
  }

  let route = {
    name: menu.name,
    path: menu.path,
    meta: {
      icon: menu.icon,
      title: menu.title
    }
  }
  route.component = () => import('../views/' + menu.component + '.vue')
  return route

})

export default router