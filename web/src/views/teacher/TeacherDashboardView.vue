<template>
  <ContentField>
    <div v-if="loading" class="loading-container">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">加载中...</span>
      </div>
      <p class="mt-3">正在加载学生数据...</p>
    </div>

    <div v-else-if="error" class="alert alert-danger" role="alert">
      <h4 class="alert-heading">加载失败</h4>
      <p>{{ error }}</p>
      <button class="btn btn-primary" @click="loadStudents">重试</button>
    </div>

    <div v-else>
      <div class="d-flex justify-content-between align-items-center mb-4">
        <h2>学生做题情况</h2>
        <div>
          <span class="badge bg-primary me-2">学生总数: {{ totalStudents }}</span>
          <button class="btn btn-outline-primary" @click="loadStudents">
            <i class="bi bi-arrow-clockwise"></i> 刷新
          </button>
        </div>
      </div>

      <div v-if="students.length === 0" class="empty-state text-center py-5">
        <h4 class="text-muted">暂无学生数据</h4>
      </div>

      <div v-else class="table-responsive">
        <table class="table table-hover align-middle">
          <thead class="table-light">
            <tr>
              <th scope="col">#</th>
              <th scope="col">学生姓名</th>
              <th scope="col">已完成题目</th>
              <th scope="col">总提交次数</th>
              <th scope="col">满分次数</th>
              <th scope="col">平均分</th>
              <th scope="col">最后提交时间</th>
              <th scope="col">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(student, index) in students" :key="student.id">
              <th scope="row">{{ index + 1 }}</th>
              <td><strong>{{ student.username }}</strong></td>
              <td>
                <span class="badge bg-info">{{ student.completedExercises }} 题</span>
              </td>
              <td>{{ student.totalSubmissions }}</td>
              <td>
                <span class="badge bg-success">{{ student.perfectCount }}</span>
              </td>
              <td>
                <div class="progress" style="width: 60px; height: 20px;">
                  <div class="progress-bar" 
                    :class="getScoreColor(student.avgScore)"
                    role="progressbar" 
                    :style="{width: (student.avgScore / 3 * 100) + '%'}"
                    :aria-valuenow="student.avgScore" 
                    aria-valuemin="0" 
                    aria-valuemax="3">
                    {{ student.avgScore }}
                  </div>
                </div>
              </td>
              <td>
                <small>{{ student.lastSubmitTime ? formatDateTime(student.lastSubmitTime) : '未提交' }}</small>
              </td>
              <td>
                <router-link 
                  :to="{name: 'student_detail', params: {id: student.id}}"
                  class="btn btn-sm btn-outline-primary">
                  查看详情
                </router-link>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue';
import $ from 'jquery';

export default {
  name: "TeacherDashboardView",
  components: {
    ContentField,
  },
  data() {
    return {
      students: [],
      totalStudents: 0,
      loading: false,
      error: null,
    };
  },
  mounted() {
    this.loadStudents();
  },
  methods: {
    loadStudents() {
      this.loading = true;
      this.error = null;

      $.ajax({
        url: "http://localhost:8080/api/teacher/students/",
        type: "get",
        headers: {
          Authorization: "Bearer " + this.$store.state.user.token,
        },
        success: (resp) => {
          if (resp.error_message === "success") {
            this.students = resp.students || [];
            this.totalStudents = resp.totalStudents || 0;
            this.loading = false;
          } else {
            this.error = resp.error_message || "获取学生数据失败";
            this.loading = false;
          }
        },
        error: (xhr, status, error) => {
          console.error("加载学生数据失败:", error);
          this.error = "无法连接到服务器";
          this.loading = false;
        }
      });
    },
    formatDateTime(dateTimeStr) {
      if (!dateTimeStr) return '';
      const date = new Date(dateTimeStr);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    },
    getScoreColor(score) {
      if (score >= 2.5) return 'bg-success';
      if (score >= 1.5) return 'bg-info';
      if (score >= 0.5) return 'bg-warning';
      return 'bg-danger';
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

.progress {
  display: inline-block;
  vertical-align: middle;
}

.table tbody tr {
  transition: background-color 0.2s;
}

.table tbody tr:hover {
  background-color: #f8f9fa;
}
</style>