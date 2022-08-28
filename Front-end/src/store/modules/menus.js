import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default {
    state: {

        menuList: [],
        permissionList: [],
        hasRoute: false,
        editableTabsValue: 'Index',
        editableTabs: [{
                title: 'Home',
                name: 'Index',

            }

        ],


    },
    mutations: {
        setMenuList(state, menus) {
            state.menuList = menus

        },
        setPermissionList(state, perms) {
            state.permissionList = perms

        },

        reverseRouteStatus(state, hasRoute) {
            state.hasRoute = hasRoute

            //  sessionStorage.setItem("hasRoute",hasRoute)
        },
        addTab(state, tab) {
            let index = state.editableTabs.findIndex(e=>e.name === tab.name)

            if (index === -1){

            state.editableTabs.push({
                    title: tab.title,
                    name: tab.name,

                }

            );}
            state.editableTabsValue = tab.name;
        },
        resetState: (state)=>{
            state.menuList = []
            state.permissionList = []
            state.hasRoute = false
            state.editableTabsValue = 'Index'
            state.editableTabs=[{
                title: 'Home',
                name: 'Index',

            }
        ]
            

        }
        
        

    }
}