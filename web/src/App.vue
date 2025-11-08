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
  background-color: rgb(242, 241, 217); /* 改为纯色背景 */
  display: flex;
  flex-direction: column;
}

.main-content {
flex: 1;
overflow-y: auto;
overflow-x: hidden;
}

/* 美化滚动条 */
.main-content::-webkit-scrollbar {
width: 8px;
}

.main-content::-webkit-scrollbar-track {
background: hsl(57, 50%, 15%);
border-radius: 4px;
}

.main-content::-webkit-scrollbar-thumb {
background: hsl(117, 80%, 20%);
border-radius: 4px;
}

.main-content::-webkit-scrollbar-thumb:hover {
background: hsl(117, 80%, 25%);
}
</style>