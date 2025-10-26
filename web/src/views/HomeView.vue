<template>
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
      
      // 从后端获取练习题列表
      $.ajax({
        url: "http://localhost:8080/api/exercise/list/",
        type: "get",
        headers: {
          Authorization: "Bearer " + this.$store.state.user.token,
        },
        success: (resp) => {
          if (resp.error_message === "success") {
            // 将后端返回的数据转换为图表需要的格式
            this.graphData.nodes = resp.exercises.map(exercise => ({
              id: exercise.id,
              name: exercise.name,
              exerciseData: exercise
            }));
            
            // 设置关系数据（如果后端返回了links）
            if (resp.links) {
              this.graphData.links = resp.links;
            } else {
              // 如果后端没有返回links，使用默认的关系
              this.graphData.links = this.getDefaultLinks(resp.exercises);
            }
            
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
    
    // 如果后端没有返回关系数据，生成默认的关系图
    getDefaultLinks(exercises) {
      const links = [];
      // 创建一个简单的链式关系
      for (let i = 0; i < exercises.length - 1; i++) {
        links.push({
          source: exercises[i].id,
          target: exercises[i + 1].id
        });
      }
      return links;
    }
  },
};
</script>

<style scoped>
.loading-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to bottom right, #ffffff, #f1f3f5);
}

.error-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to bottom right, #ffffff, #f1f3f5);
  padding: 20px;
}

.error-container .alert {
  max-width: 500px;
}
</style>