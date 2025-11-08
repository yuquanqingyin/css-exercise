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
 <button class="btn btn-primary" @click="loadStudentData">重试</button>
 </div>
 <div v-else>
 <div class="d-flex justify-content-between align-items-center mb-4">
 <div>
 <button class="btn btn-outline-secondary me-3" @click="goBack">
 ← 返回
 </button>
 <h2 class="d-inline">{{ studentName }} 的做题情况</h2>
 </div>
 <button class="btn btn-outline-primary" @click="loadStudentData">
 <i class="bi bi-arrow-clockwise"></i> 刷新
 </button>
 </div>
 <div v-if="submissions.length === 0" class="empty-state text-center py-5">
 <h4 class="text-muted">该学生暂无提交记录</h4>
 </div>
 <div v-else>
 <!-- 统计卡片 -->
 <div class="row mb-4">
 <div class="col-md-3">
 <div class="stat-card text-center">
 <h3 class="text-primary">{{ totalSubmissions }}</h3>
 <p class="text-muted mb-0">总提交次数</p>
 </div>
 </div>
 <div class="col-md-3">
 <div class="stat-card text-center">
 <h3 class="text-success">{{ completedExercises }}</h3>
 <p class="text-muted mb-0">已完成题目</p>
 </div>
 </div>
 <div class="col-md-3">
 <div class="stat-card text-center">
 <h3 class="text-warning">{{ averageScore }}</h3>
 <p class="text-muted mb-0">平均分</p>
 </div>
 </div>
 <div class="col-md-3">
 <div class="stat-card text-center">
 <h3 class="text-info">{{ perfectCount }}</h3>
 <p class="text-muted mb-0">满分次数</p>
 </div>
 </div>
 </div>
 <!-- 提交记录表格 -->
 <div class="table-responsive">
 <table class="table table-hover align-middle">
 <thead class="table-light">
 <tr>
 <th scope="col">#</th>
 <th scope="col">练习题</th>
 <th scope="col">得分</th>
 <th scope="col">提交时间</th>
 <th scope="col">操作</th>
 </tr>
 </thead>
 <tbody>
 <tr v-for="(submission, index) in submissions" :key="submission.id">
 <th scope="row">{{ index + 1 }}</th>
 <td>
 <div>
 <strong>{{ submission.exerciseName }}</strong>
<br>
<small class="text-muted">{{ submission.exerciseTitle }}</small>
 </div>
 </td>
 <td>
 <div class="stars-display">
 <span v-for="n in 3" :key="n" class="star" :class="{'filled': n <= submission.score}">
 {{ n <= submission.score ? '★' : '☆' }}
 </span>
<span class="ms-2 badge" :class="getScoreBadgeClass(submission.score)">
 {{ submission.score }}/3 星
 </span>
 </div>
 </td>
 <td>
 <small>{{ formatDateTime(submission.submitTime) }}</small>
 </td>
 <td>
 <button class="btn btn-sm btn-outline-primary" @click="viewCode(submission)">
 查看代码
 </button>
 </td>
 </tr>
 </tbody>
 </table>
 </div>
 </div>
 </div>
</ContentField>

<!-- 将模态框移到 ContentField 外面，放在根元素下 -->
<teleport to="body">
  <div class="modal fade" id="codeModal" tabindex="-1" aria-labelledby="codeModalLabel" aria-hidden="true" ref="modalElement">
    <div class="modal-dialog modal-lg modal-dialog-centered">
      <div class="modal-content modal-glass">
        <div class="modal-header">
          <h5 class="modal-title" id="codeModalLabel">提交的代码</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div v-if="selectedSubmission">
            <div class="mb-3">
              <strong>学生：</strong> {{ studentName }}
            </div>
            <div class="mb-3">
              <strong>练习题：</strong> {{ selectedSubmission.exerciseName }}
            </div>
            <div class="mb-3">
              <strong>得分：</strong>
              <span v-for="n in 3" :key="n" class="star" :class="{'filled': n <= selectedSubmission.score}">
                {{ n <= selectedSubmission.score ? '★' : '☆' }}
              </span>
            </div>
            <div class="mb-3">
              <strong>提交时间：</strong> {{ formatDateTime(selectedSubmission.submitTime) }}
            </div>
            <div>
              <strong>CSS 代码：</strong>
              <pre class="code-display"><code>{{ selectedSubmission.cssCode }}</code></pre>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
          <button type="button" class="btn btn-primary" @click="copyCode">复制代码</button>
        </div>
      </div>
    </div>
  </div>
</teleport>
</template>

<script>
import ContentField from '@/components/ContentField.vue';
import $ from 'jquery';
import { Modal } from 'bootstrap';

export default {
  name: "StudentDetailView",
  components: {
    ContentField,
  },
  data() {
    return {
      studentId: null,
      studentName: '',
      submissions: [],
      totalSubmissions: 0,
      loading: false,
      error: null,
      selectedSubmission: null,
      codeModal: null,
    };
  },
  computed: {
    completedExercises() {
      const uniqueExercises = new Set(this.submissions.map(s => s.exerciseId));
      return uniqueExercises.size;
    },
    averageScore() {
      if (this.submissions.length === 0) return 0;
      const total = this.submissions.reduce((sum, s) => sum + s.score, 0);
      return (total / this.submissions.length).toFixed(1);
    },
    perfectCount() {
      return this.submissions.filter(s => s.score === 3).length;
    }
  },
  mounted() {
    this.studentId = this.$route.params.id;
    this.loadStudentData();
    
    // 等待 DOM 更新后初始化模态框
    this.$nextTick(() => {
      const modalElement = document.getElementById('codeModal');
      if (modalElement) {
        this.codeModal = new Modal(modalElement);
      }
    });
  },
  beforeUnmount() {
    // 组件销毁前清理模态框
    if (this.codeModal) {
      this.codeModal.dispose();
    }
  },
  methods: {
    loadStudentData() {
      this.loading = true;
      this.error = null;
      
      $.ajax({
        url: `http://localhost:8080/api/teacher/student/${this.studentId}/submissions/`,
        type: "get",
        headers: {
          Authorization: "Bearer " + this.$store.state.user.token,
        },
        success: (resp) => {
          if (resp.error_message === "success") {
            this.studentName = resp.studentName;
            this.submissions = resp.submissions || [];
            this.totalSubmissions = resp.totalSubmissions || 0;
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
    viewCode(submission) {
      this.selectedSubmission = submission;
      if (this.codeModal) {
        this.codeModal.show();
      }
    },
    copyCode() {
      if (this.selectedSubmission && this.selectedSubmission.cssCode) {
        navigator.clipboard.writeText(this.selectedSubmission.cssCode)
          .then(() => {
            alert('代码已复制到剪贴板');
          })
          .catch(err => {
            console.error('复制失败:', err);
            alert('复制失败，请手动复制');
          });
      }
    },
    formatDateTime(dateTimeStr) {
      if (!dateTimeStr) return '';
      const date = new Date(dateTimeStr);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      const seconds = String(date.getSeconds()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    getScoreBadgeClass(score) {
      if (score === 3) return 'bg-success';
      if (score === 2) return 'bg-info';
      if (score === 1) return 'bg-warning';
      return 'bg-secondary';
    },
    goBack() {
      this.$router.push({ name: 'teacher_dashboard' });
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

.stat-card {
  background-color: #fff;
  border-radius: 10px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stat-card h3 {
  margin-bottom: 0.5rem;
  font-weight: bold;
}

.stars-display {
  display: flex;
  align-items: center;
}

.star {
  font-size: 1.5rem;
  color: #ddd;
  margin-right: 2px;
}

.star.filled {
  color: #ffd700;
}

.code-display {
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 5px;
  padding: 1rem;
  max-height: 400px;
  overflow-y: auto;
  font-family: 'Courier New', monospace;
  font-size: 0.9rem;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.table-responsive {
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  overflow: hidden;
}

.table tbody tr {
  transition: background-color 0.2s;
}

.table tbody tr:hover {
  background-color: #f8f9fa;
}

/* 模态框样式 */
.modal-glass {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
  border-radius: 15px;
}

/* 确保模态框在最上层 */
:deep(.modal) {
  z-index: 2000 !important;
}

:deep(.modal-backdrop) {
  z-index: 1999 !important;
}
</style>