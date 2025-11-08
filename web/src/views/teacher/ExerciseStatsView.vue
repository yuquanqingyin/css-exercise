<template>
<ContentField>
 <div v-if="loading" class="loading-container">
 <div class="spinner-border text-primary" role="status">
 <span class="visually-hidden">加载中...</span>
 </div>
 <p class="mt-3">正在加载统计数据...</p>
 </div>
 <div v-else-if="error" class="alert alert-danger" role="alert">
 <h4 class="alert-heading">加载失败</h4>
 <p>{{ error }}</p>
 <button class="btn btn-primary" @click="loadStats">重试</button>
 </div>
 <div v-else>
 <div class="d-flex justify-content-between align-items-center mb-4">
 <h2>练习题完成情况统计</h2>
 <div>
 <span class="badge bg-primary me-2">练习题总数: {{ totalExercises }}</span>
 <span class="badge bg-info me-2">学生总数: {{ totalStudents }}</span>
 <button class="btn btn-outline-primary" @click="loadStats">
 <i class="bi bi-arrow-clockwise"></i> 刷新
 </button>
 </div>
 </div>
 <div v-if="exercises.length === 0" class="empty-state text-center py-5">
 <h4 class="text-muted">暂无统计数据</h4>
 </div>
 <div v-else class="table-responsive">
 <table class="table table-hover align-middle">
 <thead class="table-light">
 <tr>
 <th scope="col">#</th>
 <th scope="col">练习题</th>
 <th scope="col">完成人数</th>
 <th scope="col">提交总次数</th>
 <th scope="col">满分人数</th>
 <th scope="col" class="text-center">平均分</th>
 </tr>
 </thead>
 <tbody>
 <tr v-for="(exercise, index) in exercises" :key="exercise.exerciseId">
 <th scope="row">{{ index + 1 }}</th>
 <td>
 <div>
 <strong>{{ exercise.exerciseName }}</strong>
 <br>
 <small class="text-muted">{{ exercise.exerciseTitle }}</small>
 </div>
 </td>
 <td>
 <span class="badge bg-primary">{{ exercise.completedStudents }} / {{ totalStudents }}</span>
 </td>
 <td>{{ exercise.totalSubmissions }}</td>
 <td>
 <span class="badge bg-success">{{ exercise.perfectStudents }}</span>
 </td>
 <td class="text-center">
 <div class="stars-display-small">
 <span v-for="n in 3" :key="n" class="star-small" :class="{'filled': n <= Math.round(exercise.avgScore)}">
 {{ n <= Math.round(exercise.avgScore) ? '★' : '☆' }}
 </span>
 </div>
 </td>
 </tr>
 </tbody>
 </table>
 </div>
 <!-- 整体统计卡片 -->
 <div class="row mt-4" v-if="exercises.length > 0">
 <div class="col-md-12">
 <div class="card">
 <div class="card-body">
 <h5 class="card-title">整体统计</h5>
 <div class="row text-center">
 <div class="col-md-4">
 <div class="stat-item">
 <h3 class="text-primary">{{ totalExercises }}</h3>
<p class="text-muted mb-0">练习题总数</p>
 </div>
 </div>
 <div class="col-md-4">
 <div class="stat-item">
 <h3 class="text-info">{{ totalStudents }}</h3>
<p class="text-muted mb-0">学生总数</p>
 </div>
 </div>
 <div class="col-md-4">
 <div class="stat-item">
 <h3 class="text-warning">{{ overallAvgScore }}</h3>
<p class="text-muted mb-0">整体平均分</p>
 </div>
 </div>
 </div>
 </div>
 </div>
 </div>
 </div>
 </div>
</ContentField>
</template>
<script>
import ContentField from '@/components/ContentField.vue';
import $ from 'jquery';
export default {
name: "ExerciseStatsView",
components: {
 ContentField,
},
data() {
 return {
 exercises: [],
 totalExercises: 0,
 totalStudents: 0,
 loading: false,
 error: null,
 };
},
computed: {
 overallAvgScore() {
 if (this.exercises.length === 0) return 0;
 const total = this.exercises.reduce((sum, e) => sum + e.avgScore, 0);
 return (total / this.exercises.length).toFixed(1);
 }
},
mounted() {
 this.loadStats();
},
methods: {
 loadStats() {
 this.loading = true;
 this.error = null;
 $.ajax({
 url: "http://localhost:8080/api/teacher/exercises/stats/",
 type: "get",
 headers: {
 Authorization: "Bearer " + this.$store.state.user.token,
 },
 success: (resp) => {
 if (resp.error_message === "success") {
 this.exercises = resp.exercises || [];
 this.totalExercises = resp.totalExercises || 0;
 this.totalStudents = resp.totalStudents || 0;
 this.loading = false;
 } else {
 this.error = resp.error_message || "获取统计数据失败";
 this.loading = false;
 }
 },
 error: (xhr, status, error) => {
 console.error("加载统计数据失败:", error);
 this.error = "无法连接到服务器";
 this.loading = false;
 }
 });
 }
}
}
</script>
<style scoped>
.loading-container {
display: flex;
flex-direction: column;
justify-content: center;
align-items: center;
min-height: 400px;
}
.empty-state {
background-color: #f8f9fa;
border-radius: 10px;
padding: 3rem;
}
.table-responsive {
box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
border-radius: 8px;
overflow: hidden;
}
.stars-display-small {
display: inline-flex;
align-items: center;
justify-content: center;
}
.star-small {
font-size: 1.4rem;
color: #ddd;
margin-right: 2px;
}
.star-small.filled {
color: #ffd700;
}
.stat-item {
padding: 1rem;
border-right: 1px solid #e9ecef;
}
.stat-item:last-child {
border-right: none;
}
.stat-item h3 {
margin-bottom: 0.5rem;
font-weight: bold;
}
.table tbody tr {
transition: background-color 0.2s;
}
.table tbody tr:hover {
background-color: #f8f9fa;
}
.card {
box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>