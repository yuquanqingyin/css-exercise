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
/* 毛玻璃导航栏 */
.custom-navbar {
  background: rgba(255, 255, 255, 0.75) !important;
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  position: relative;
  z-index: 1000; /* 确保导航栏在最上层 */
}

/* 品牌样式 */
.navbar-brand {
  font-weight: 700;
  font-size: 1.4rem;
  background: rgb(92, 10, 14);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.brand-icon {
  font-size: 1.5rem;
  margin-right: 5px;
}

/* 导航链接 */
.nav-link {
  color: rgb(92, 10, 14) !important;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
  padding: 0.5rem 1rem !important;
}

.nav-link:hover {
  color: rgb(92, 10, 14) !important;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: rgb(92, 10, 14);
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.nav-link:hover::after {
  width: 80%;
}

.nav-link.router-link-active {
  color: rgb(92, 10, 14) !important;
  font-weight: 600;
}

.nav-link.router-link-active::after {
  width: 80%;
  background: rgb(92, 10, 14);
}

/* 渐变徽章 */
.bg-gradient-custom {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
}

/* 下拉菜单容器 */
.dropdown {
  position: relative;
}

/* 下拉菜单 */
.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  z-index: 1050; /* 提高 z-index */
  display: none;
  min-width: 12rem;
  padding: 0.5rem 0;
  margin: 0.125rem 0 0;
  font-size: 1rem;
  color: #212529;
  text-align: left;
  list-style: none;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  background-clip: padding-box;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.dropdown-menu.show {
  display: block;
}

.dropdown-toggle {
  cursor: pointer;
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

/* 下拉菜单项 */
.dropdown-item {
  display: block;
  width: 100%;
  padding: 0.5rem 1.5rem;
  clear: both;
  font-weight: 400;
  color: #4a5568;
  text-align: inherit;
  text-decoration: none;
  white-space: nowrap;
  background-color: transparent;
  border: 0;
  transition: all 0.3s ease;
  cursor: pointer; /* 添加手型光标 */
}

.dropdown-item:hover,
.dropdown-item:focus {
  color: #667eea;
  background: linear-gradient(90deg, rgba(102, 126, 234, 0.1) 0%, transparent 100%);
}

.dropdown-divider {
  height: 0;
  margin: 0.5rem 0;
  overflow: hidden;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

.badge {
  font-size: 0.75rem;
  padding: 0.25rem 0.6rem;
}
</style>