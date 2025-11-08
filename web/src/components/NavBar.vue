<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container">
      <a class="navbar-brand" href="#">CSS修炼之路</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" 
        aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <!-- 学生菜单 -->
          <li class="nav-item" v-if="store.getters.isStudent">
            <router-link class="nav-link" :to="{name: 'home'}">CSS练习题</router-link>
          </li>
          
          <!-- 教师菜单 -->
          <li class="nav-item" v-if="store.getters.isTeacher">
            <router-link class="nav-link" :to="{name: 'teacher_dashboard'}">学生管理</router-link>
          </li>
          <li class="nav-item" v-if="store.getters.isTeacher">
            <router-link class="nav-link" :to="{name: 'exercise_stats'}">练习题统计</router-link>
          </li>
        </ul>
        
        <ul class="navbar-nav" v-if="store.state.user.is_login">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle"
              href="#"
              role="button"
              @click.prevent="toggleDropdown"
              aria-expanded="false">
              {{ store.state.user.username }}
              <span class="badge bg-secondary ms-1" v-if="store.getters.isTeacher">教师</span>
            </a>
            <ul class="dropdown-menu" :class="{'show': showDropdown}">
              <!-- 学生才显示我的成绩 -->
              <li v-if="store.getters.isStudent">
                <router-link class="dropdown-item"
                  :to="{name: 'my_submissions'}"
                  @click="closeDropdown">
                  我的成绩
                </router-link>
              </li>
              <li v-if="store.getters.isStudent"><hr class="dropdown-divider"></li>
              <li>
                <a class="dropdown-item"
                  href="#"
                  @click.prevent="handleLogout">
                  退出
                </a>
              </li>
            </ul>
          </li>
        </ul>
        
        <ul class="navbar-nav" v-else>
          <li class="nav-item">
            <router-link class="nav-link" :to="{name: 'user_account_login'}" role="button">
              登录
            </router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button">
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
import { ref, onMounted, onUnmounted } from 'vue';

export default {
  name: 'NavBar',
  setup() {
    const store = useStore();
    const showDropdown = ref(false);

    const toggleDropdown = () => {
      showDropdown.value = !showDropdown.value;
    };

    const closeDropdown = () => {
      showDropdown.value = false;
    };

    const handleLogout = () => {
      closeDropdown();
      store.dispatch("logout");
    };

    const handleClickOutside = (event) => {
      const dropdown = event.target.closest('.dropdown');
      if (!dropdown && showDropdown.value) {
        closeDropdown();
      }
    };

    onMounted(() => {
      document.addEventListener('click', handleClickOutside);
    });

    onUnmounted(() => {
      document.removeEventListener('click', handleClickOutside);
    });

    return {
      store,
      showDropdown,
      toggleDropdown,
      closeDropdown,
      handleLogout
    };
  }
}
</script>

<style scoped>
.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  z-index: 1000;
  display: none;
  min-width: 10rem;
  padding: 0.5rem 0;
  margin: 0;
  font-size: 1rem;
  color: #212529;
  text-align: left;
  list-style: none;
  background-color: #fff;
  background-clip: padding-box;
  border: 1px solid rgba(0,0,0,.15);
  border-radius: 0.375rem;
}

.dropdown-menu.show {
  display: block;
}

.dropdown-toggle::after {
  display: inline-block;
  margin-left: 0.255em;
  vertical-align: 0.255em;
  content: "";
  border-top: 0.3em solid;
  border-right: 0.3em solid transparent;
  border-bottom: 0;
  border-left: 0.3em solid transparent;
}

.dropdown-item {
  display: block;
  width: 100%;
  padding: 0.25rem 1rem;
  clear: both;
  font-weight: 400;
  color: #212529;
  text-align: inherit;
  text-decoration: none;
  white-space: nowrap;
  background-color: transparent;
  border: 0;
}

.dropdown-item:hover,
.dropdown-item:focus {
  color: #1e2125;
  background-color: #e9ecef;
}

.dropdown-divider {
  height: 0;
  margin: 0.5rem 0;
  overflow: hidden;
  border-top: 1px solid rgba(0,0,0,.15);
}

.badge {
  font-size: 0.75rem;
}
</style>