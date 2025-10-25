<template>
<nav class="navbar navbar-expand-lg bg-body-tertiary">
  <div class="container">
    <a class="navbar-brand" href="#">CSS修炼之路</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
            <li class="nav-item">
                <router-link class="nav-link" :to="{name: 'home'}">CSS练习题</router-link>
                
            </li>
        </ul>
        <ul class="navbar-nav " v-if="store.state.user.is_login">
            <li class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    {{ store.state.user.username }}
                </a>
                <ul class="dropdown-menu">
                    <li><router-link class="dropdown-item" >我成绩</router-link></li>
                    <li><hr class="dropdown-divider"></li>
                    <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
                </ul>
            </li>
        </ul>
        <ul class="navbar-nav " v-else>
            <li class="nav-item ">
                <router-link class="nav-link " :to="{name: 'user_account_login'}" role="button" >
                    登录
                </router-link>
            </li>
            <li class="nav-item ">
                <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button" >
                    注册
                </router-link>
            </li>
        </ul>
    </div>
  </div>
</nav>



</template>

<script>
import { useStore } from 'vuex';



export default{

    setup(){
        const store = useStore();

        const logout = () => {
            store.dispatch("logout");
        }
        return {
            store,
            logout
        }
    }
}


</script>



<style scoped>



</style>