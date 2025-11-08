import $ from 'jquery'

export default({
    state: {
        id: "",
        username: "",
        role: "",  // 新增角色字段
        is_login: false,
        token: "",
    },
    getters: {
        isTeacher: state => state.role === 'teacher',
        isStudent: state => state.role === 'student',
    },
    mutations: {
        updateUser(state, user){
            state.id = user.id;
            state.username = user.username;
            state.role = user.role || 'student';  // 新增角色
            state.is_login = true;
        },
        updateToken(state, token){
            state.token = token;
        },
        logout(state){
            state.id = "";
            state.username = "";
            state.role = "";
            state.token = "";
            state.is_login = false;
        },
    },
    actions: {
        login(context, data) {
            $.ajax({
                url: "http://127.0.0.1:8080/user/account/token/",
                type: "post",
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp) {
                    if(resp.error_message === "success"){
                        localStorage.setItem("jwt_token", resp.token);
                        context.commit("updateToken", resp.token);
                        data.success(resp);
                    }else{
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                },
            })
        },
        getinfo(context, data) {
            $.ajax({
                url: "http://127.0.0.1:8080/user/account/info/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + context.state.token,
                },
                success(resp) {
                    if(resp.error_message === "success"){
                        context.commit("updateUser", {
                            ...resp,
                            is_login: true,
                        });
                        data.success(resp);
                    }else{
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            })
        },
        logout(context){
            localStorage.removeItem("jwt_token");
            context.commit("logout");
        }
    },
    modules: {
    }
})