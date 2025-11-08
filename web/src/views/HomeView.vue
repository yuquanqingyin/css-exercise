<template>
<div class="home-view">
  <div v-if="loading" class="loading-container">
    <div class="spinner-border text-primary" role="status">
      <span class="visually-hidden">加载中...</span>
    </div>
    <p class="mt-3">正在加载练习题...</p>
  </div>
  <div v-else-if="error" class="error-container">
    <div class="alert alert-danger" role="alert">
      <h4 class="alert-heading">加载失败</h4>
      <p>{{ error }}</p>
      <button class="btn btn-primary" @click="loadData">重试</button>
    </div>
  </div>
  <RelationGraph v-else :data="graphData" />
</div>
</template>

<script>
import RelationGraph from "@/components/RelationGraph.vue";
import $ from 'jquery';
export default {
  name: "HomeView",
  components: { 
    RelationGraph,
  },
  data() {
    return {
      graphData: {
        nodes: [],
        links: []
      },
      loading: true,
      error: null,
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    loadData() {
      this.loading = true;
      this.error = null;
      
      $.ajax({
        url: "http://localhost:8080/api/exercise/list/",
        type: "get",
        headers: {
          Authorization: "Bearer " + this.$store.state.user.token,
        },
        success: (resp) => {
          if (resp.error_message === "success") {
            this.graphData.nodes = resp.nodes || [];
            this.graphData.links = resp.links || [];
            this.loading = false;
          } else {
            this.error = resp.error_message || "获取数据失败";
            this.loading = false;
          }
        },
        error: (xhr, status, error) => {
          console.error("加载练习题失败:", error);
          this.error = "无法连接到服务器，请检查后端是否正常运行";
          this.loading = false;
        }
      });
    },
  },
};
</script>

<style scoped>
.home-view {
  position: relative;
  width: 100%;
  height: 100%; /* 改为 100% 而不是 calc(100vh - 56px) */
  min-height: calc(100vh - 56px); /* 确保最小高度 */
  background: transparent;
  overflow: hidden;
}

.loading-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  border-radius: 20px;
  margin: 20px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.loading-container .spinner-border {
  width: 3rem;
  height: 3rem;
}

.error-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  padding: 20px;
}

.error-container .alert {
  max-width: 500px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
}
</style>