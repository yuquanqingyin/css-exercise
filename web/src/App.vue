<template>
  <NavBar/>
  <router-view/>
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
</style>
