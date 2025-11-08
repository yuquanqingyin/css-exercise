<template>
<div class="app-wrapper">
  <NavBar/>
  <div class="main-content">
    <router-view/>
  </div>
</div>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'
import NavBar from './components/NavBar.vue';
export default {
  name: 'App',
  components: {
    NavBar,
  }
}
</script>

<style>
body {
  margin: 0;
  padding: 0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Microsoft YaHei', sans-serif;
}

#app {
  margin: 0;
  padding: 0;
  height: 100vh;
  overflow: hidden;
}

.app-wrapper {
  width: 100vw;
  height: 100vh;
  background-image: url('@/assets/images/background.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  overflow-y: auto; /* 允许主内容区域滚动 */
  overflow-x: hidden;
}

/* 美化滚动条 */
.main-content::-webkit-scrollbar {
  width: 8px;
}

.main-content::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.1);
  border-radius: 4px;
}

.main-content::-webkit-scrollbar-thumb {
  background: rgba(102, 126, 234, 0.5);
  border-radius: 4px;
}

.main-content::-webkit-scrollbar-thumb:hover {
  background: rgba(102, 126, 234, 0.7);
}
</style>