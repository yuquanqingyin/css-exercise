<template>
  <div class="person-page">
    <h1>{{ name }}</h1>
    <p>这里可以展示 {{ name }} 的详细信息。</p>
    <router-link to="/" class="btn">返回关系图</router-link>
  </div>
</template>

<script>
export default {
  name: "PersonView",
  computed: {
    name() {
      return this.$route.params.name;
    },
  },
};
</script>

<style scoped>
.person-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: linear-gradient(135deg, #dee2e6, #f8f9fa);
  color: #212529;
}
.btn {
  margin-top: 20px;
  background-color: #4dabf7;
  color: white;
  padding: 10px 20px;
  border-radius: 10px;
  text-decoration: none;
  transition: 0.3s;
}
.btn:hover {
  background-color: #339af0;
}
</style>
