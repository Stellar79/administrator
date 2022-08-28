import Vue from "vue"

Vue.mixin({
    methods: {
        hasAuth(permissionList) {
            var authorities = this.$store.state.menus.permissionList
            return authorities.indexOf(permissionList) > -1
        }
    }
})