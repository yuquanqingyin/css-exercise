<template>
<ContentField>
 <!-- 加载状态 -->
 <div v-if="loading" class="loading-container">
 <div class="spinner-border text-primary" role="status">
 <span class="visually-hidden">加载中...</span>
 </div>
 <p class="mt-3">正在加载提交记录...</p>
 </div>
 <!-- 错误状态 -->
 <div v-else-if="error" class="alert alert-danger glass-alert" role="alert">
 <h4 class="alert-heading">加载失败</h4>
 <p>{{ error }}</p>
 <button class="btn btn-primary" @click="loadSubmissions">重试</button>
 </div>
 <!-- 提交记录列表 -->
 <div v-else>
 <div class="d-flex justify-content-between align-items-center mb-4">
 <h2>我的成绩</h2>
 <button class="btn btn-glass" @click="loadSubmissions">
 <i class="bi bi-arrow-clockwise"></i> 刷新
 </button>
 </div>
 <!-- 空状态 -->
 <div v-if="submissions.length === 0" class="empty-state text-center py-5">
 <div class="empty-icon mb-3">
 <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" fill="currentColor" class="bi bi-clipboard-x text-muted" viewBox="0 0 16 16">
 <path fill-rule="evenodd" d="M6.146 7.146a.5.5 0 0 1 .708 0L8 8.293l1.146-1.147a.5.5 0 1 1 .708.708L8.707 9l1.147 1.146a.5.5 0 0 1-.708.708L8 9.707l-1.146 1.147a.5.5 0 0 1-.708-.708L7.293 9 6.146 7.854a.5.5 0 0 1 0-.708z"/>
 <path d="M4 1.5H3a2 2 0 0 0-2 2V14a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V3.5a2 2 0 0 0-2-2h-1v1h1a1 1 0 0 1 1 1V14a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3.5a1 1 0 0 1 1-1h1v-1z"/>
 <path d="M9.5 1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-3a.5.5 0 0 1-.5-.5v-1a.5.5 0 0 1 .5-.5h3zm-3-1A1.5 1.5 0 0 0 5 1.5v1A1.5 1.5 0 0 0 6.5 4h3A1.5 1.5 0 0 0 11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3z"/>
 </svg>
 </div>
 <h4 class="text-muted">暂无提交记录</h4>
 <p class="text-muted">开始练习并提交代码后，您的成绩将显示在这里</p>
 <router-link :to="{name: 'home'}" class="btn btn-gradient-primary mt-3">
 开始练习
 </router-link>
 </div>
 <!-- 提交记录表格 -->
 <div v-else class="table-responsive">
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
 <button class="btn btn-sm btn-glass me-2" @click="viewCode(submission)">
 查看代码
 </button>
 <router-link 
 :to="{name: 'exercise', params: {id: submission.exerciseId}}" 
 class="btn btn-sm btn-gradient-success">
 重新练习
 </router-link>
 </td>
 </tr>
 </tbody>
 </table>
 </div>
 <!-- 统计信息 -->
 <div v-if="submissions.length > 0" class="card glass-card mt-4">
 <div class="card-body">
 <h5 class="card-title mb-4">📊 统计信息</h5>
 <div class="row text-center">
 <div class="col-md-3">
 <div class="stat-item">
 <h3 class="text-primary">{{ submissions.length }}</h3>
 <p class="text-muted mb-0">总提交次数</p>
 </div>
 </div>
 <div class="col-md-3">
 <div class="stat-item">
 <h3 class="text-success">{{ getPerfectCount }}</h3>
 <p class="text-muted mb-0">满分次数</p>
 </div>
 </div>
 <div class="col-md-3">
 <div class="stat-item">
 <h3 class="text-info">{{ getAverageScore.toFixed(1) }}</h3>
 <p class="text-muted mb-0">平均得分</p>
 </div>
 </div>
 <div class="col-md-3">
 <div class="stat-item">
 <h3 class="text-warning">{{ getUniqueExerciseCount }}</h3>
 <p class="text-muted mb-0">练习题数</p>
 </div>
 </div>
 </div>
 </div>
 </div>
 </div>
</ContentField>

<!-- 代码查看模态框 - 使用 teleport 移到 body 下 -->
<teleport to="body">
  <div class="modal fade" id="codeModal" tabindex="-1" aria-labelledby="codeModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
      <div class="modal-content modal-glass">
        <div class="modal-header">
          <h5 class="modal-title" id="codeModalLabel">📝 提交的代码</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div v-if="selectedSubmission">
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
          <button type="button" class="btn btn-gradient-primary" @click="copyCode">复制代码</button>
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
  name: "MySubmissionsView",
  components: {
    ContentField,
  },
  data() {
    return {
      submissions: [],
      loading: false,
      error: null,
      selectedSubmission: null,
      codeModal: null,
    };
  },
  mounted() {
    this.loadSubmissions();
    
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
  computed: {
    getPerfectCount() {
      return this.submissions.filter(s => s.score === 3).length;
    },
    getAverageScore() {
      if (this.submissions.length === 0) return 0;
      const total = this.submissions.reduce((sum, s) => sum + s.score, 0);
      return total / this.submissions.length;
    },
    getUniqueExerciseCount() {
      const uniqueExercises = new Set(this.submissions.map(s => s.exerciseId));
      return uniqueExercises.size;
    }
  },
  methods: {
    loadSubmissions() {
      this.loading = true;
      this.error = null;
      
      $.ajax({
        url: "http://localhost:8080/api/record/my-submissions/",
        type: "get",
        headers: {
          Authorization: "Bearer " + this.$store.state.user.token,
        },
        success: (resp) => {
          if (resp.error_message === "success") {
            this.submissions = resp.submissions || [];
            this.loading = false;
          } else {
            this.error = resp.error_message || "获取提交记录失败";
            this.loading = false;
          }
        },
        error: (xhr, status, error) => {
          console.error("加载提交记录失败:", error);
          this.error = "无法连接到服务器，请检查后端是否正常运行";
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

.glass-alert {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
}

.empty-state {
  background: rgba(248, 249, 250, 0.8);
  backdrop-filter: blur(5px);
  border-radius: 15px;
  padding: 3rem;
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
  border-radius: 8px;
  padding: 1rem;
  max-height: 400px;
  overflow-y: auto;
  font-family: 'Courier New', monospace;
  font-size: 0.9rem;
  white-space: pre-wrap;
  word-wrap: break-word;
}

.code-display::-webkit-scrollbar {
  width: 8px;
}

.code-display::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.05);
  border-radius: 4px;
}

.code-display::-webkit-scrollbar-thumb {
  background: rgba(102, 126, 234, 0.5);
  border-radius: 4px;
}

.code-display::-webkit-scrollbar-thumb:hover {
  background: rgba(102, 126, 234, 0.7);
}

.glass-card {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border-radius: 15px;
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

.table-responsive {
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  overflow: hidden;
}

.table {
  margin-bottom: 0;
}

.table thead {
  background-color: #f8f9fa;
}

.table tbody tr {
  transition: background-color 0.2s;
}

.table tbody tr:hover {
  background-color: #f8f9fa;
}

/* 按钮样式 */
.btn-glass {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(5px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: #4a5568;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-glass:hover {
  background: rgba(255, 255, 255, 0.9);
  color: #667eea;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.btn-gradient-primary {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  font-weight: 500;
  padding: 0.375rem 1rem;
  border-radius: 6px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3);
}

.btn-gradient-primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
  color: white;
}

.btn-gradient-success {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  border: none;
  color: white;
  font-weight: 500;
  padding: 0.25rem 0.75rem;
  border-radius: 6px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(17, 153, 142, 0.3);
  font-size: 0.875rem;
}

.btn-gradient-success:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(17, 153, 142, 0.4);
  color: white;
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

.modal-body {
  max-height: 60vh;
  overflow-y: auto;
}

.modal-body::-webkit-scrollbar {
  width: 8px;
}

.modal-body::-webkit-scrollbar-track {
  background: rgba(0, 0, 0, 0.05);
  border-radius: 4px;
}

.modal-body::-webkit-scrollbar-thumb {
  background: rgba(102, 126, 234, 0.5);
  border-radius: 4px;
}

.modal-body::-webkit-scrollbar-thumb:hover {
  background: rgba(102, 126, 234, 0.7);
}
</style>