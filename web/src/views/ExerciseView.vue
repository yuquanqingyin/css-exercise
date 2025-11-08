<template>
<div class="exercise-container">
<!-- 加载状态 -->
<div v-if="loading" class="loading-container">
<div class="spinner-border text-primary" role="status">
<span class="visually-hidden">加载中...</span>
</div>
<p class="mt-3">正在加载练习题...</p>
</div>

<!-- 错误状态 -->
<div v-else-if="error" class="alert alert-danger glass-alert" role="alert">
<h4 class="alert-heading">加载失败</h4>
<p>{{ error }}</p>
<button class="btn btn-primary" @click="loadExercise(exerciseId)">重试</button>
</div>

<!-- 练习内容 -->
<div v-else class="container py-4">
<!-- 顶部导航 -->
<div class="d-flex justify-content-between align-items-center mb-4">
<button class="btn btn-glass" @click="goBack">
← 返回关系图
</button>
<h5 class="mb-0 exercise-badge">练习 {{ exerciseId }}</h5>
</div>

<!-- 题目描述卡片 -->
<div class="card glass-card mb-4">
<div class="card-header glass-header">
<h5 class="mb-0">
    <svg class="target-icon-inline" t="1762606371008" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" >
        <path d="M284.444444 0h455.111112c157.098667 0 284.444444 127.345778 284.444444 284.444444v455.111112c0 157.098667-127.345778 284.444444-284.444444 284.444444H284.444444C127.345778 1024 0 896.654222 0 739.555556V284.444444C0 127.345778 127.345778 0 284.444444 0z m451.214223 392.533333l-136.362667-131.157333c-3.214222-4.295111-7.509333-5.376-11.804444-5.376-4.295111 0-8.590222 2.161778-12.885334 5.376l-251.249778 257.991111c-2.133333 2.133333-3.214222 5.347556-4.266666 7.509333L284.728889 707.413333c-1.080889 6.456889 1.052444 10.752 5.347555 16.128a15.644444 15.644444 0 0 0 12.885334 5.376h3.242666l158.890667-36.551111c3.214222-1.080889 6.428444-2.133333 8.590222-5.376l260.892445-269.795555c6.456889-5.376 6.456889-17.180444 1.080889-24.718223z m-269.482667 267.605334a17.777778 17.777778 0 0 1-12.885333 5.404444 18.602667 18.602667 0 0 1-12.885334-5.404444l-99.84-93.496889c-7.537778-6.456889-7.537778-17.208889-1.080889-24.746667 6.428444-7.509333 17.180444-7.509333 24.689778-1.052444l99.84 93.525333c8.590222 7.509333 9.671111 18.261333 2.161778 25.770667z m45.624889 41.301333a17.92 17.92 0 0 0 0 35.84h196.750222a17.92 17.92 0 0 0 0-35.84h-196.750222z" fill="#5c0a0e" p-id="11073"></path></svg>
        题目描述
    </h5>
</div>
<div class="card-body">
<div v-html="renderedMarkdown" class="markdown-content"></div>
</div>
</div>

<!-- 预期效果卡片 -->
<div class="card glass-card mb-4">
<div class="card-header glass-header">
<h5 class="mb-0">
  <svg class="target-icon-inline" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
    <path d="M479.007774 757.823322a209.75265 209.75265 0 1 1 50.261484-413.455831l33.922261 8.367491-24.76325 24.706714-28.268552 28.268551-49.75265 49.752651-5.144876 1.35689a94.360424 94.360424 0 1 0 116.353357 109.003534l1.130742-5.992933 99.674911-99.787986 8.9894 32.791519a210.035336 210.035336 0 0 1-202.402827 265.102474" fill="#5c0a0e"></path>
    <path d="M479.007774 1023.943463A475.872792 475.872792 0 1 1 636.407067 98.883392l29.568905 10.402827-22.162544 22.162544-61.229682 61.116608-7.858658 7.858657-10.742049-2.600706a359.80212 359.80212 0 1 0 265.724381 266.798586l-2.600706-10.685512 7.80212-7.80212L896.477739 384.791519l22.219081-22.275618 10.289752 29.738516a476.212014 476.212014 0 0 1-449.978798 631.745583" fill="#5c0a0e"></path>
    <path d="M538.59788 542.077739a54.04947 54.04947 0 0 1-15.60424-2.431096 51.731449 51.731449 0 0 1-25.724382-81.696113 94.190813 94.190813 0 0 1 8.59364-9.272085l36.353356-36.353357c39.915194-39.971731 49.469965-49.469965 108.607774-108.438162a9.780919 9.780919 0 0 0 3.787986-10.007067q-3.335689-29.738516-5.65371-59.533569-1.187279-14.24735-2.487633-28.268551a24.19788 24.19788 0 0 1 7.406361-20.805654l130.035335-130.035336 42.120142-42.120141a16.056537 16.056537 0 0 1 3.90106-2.826855l1.865724-1.017668 15.378092-9.102474 3.392226 36.918728 3.90106 42.855124 1.922262 20.975265q3.166078 32.565371 5.65371 65.243816h2.14841c37.710247 3.109541 76.042403 6.558304 113.074205 9.95053l21.427562 1.978799h1.809187a21.65371 21.65371 0 0 1 15.038869 4.636042l5.031802 4.183746-1.187279 6.388692a21.144876 21.144876 0 0 1-8.084805 12.607774l-1.130742 0.961131-54.332156 54.388693q-55.236749 55.236749-110.416961 110.699646a29.681979 29.681979 0 0 1-25.667845 9.498233 2770.883392 2770.883392 0 0 0-39.575971-3.731448q-23.689046-2.035336-47.378092-4.466431a6.897527 6.897527 0 0 0-7.293286 2.600706c-42.685512 42.855124-60.325088 60.551237-81.752651 81.922262l-65.413427 65.639576a56.028269 56.028269 0 0 1-39.915194 18.544169" fill="#5c0a0e"></path>
  </svg>
  预期效果
</h5>
</div>
<div class="card-body text-center">
<img :src="desiredOutcomeImage" class="img-fluid rounded shadow-sm" alt="预期效果" @error="handleImageError">
<p v-if="imageLoadError" class="text-muted text-center mt-2">
<small>预期效果图片加载失败</small>
</p>
</div>
</div>

<!-- 代码编辑和预览区域 -->
<div class="row g-4 mb-4">
<!-- 左侧：代码编辑器 -->
<div class="col-lg-7">
<div class="card glass-card h-100">
<div class="card-header glass-header">
<ul class="nav nav-tabs card-header-tabs border-0">
<li class="nav-item">
<a :class="activeTab === 'html' ? 'nav-link active gradient-tab': 'nav-link glass-tab'" 
href="#" @click.prevent="activeTab = 'html'">
HTML
</a>
</li>
<li class="nav-item">
<a :class="activeTab === 'css' ? 'nav-link active gradient-tab': 'nav-link glass-tab'" 
href="#" @click.prevent="activeTab = 'css'">
CSS
</a>
</li>
</ul>
</div>
<div class="card-body p-0">
<div v-if="activeTab === 'html'" class="p-3">
<div v-html="renderedHTML" class="markdown-content"></div>
</div>
<VAceEditor v-else-if="activeTab === 'css'"
v-model:value="cssCode"
@init="editorInit" 
lang="css"
theme="textmate" 
style="height: 350px" 
:options="{
enableBasicAutocompletion: true,
enableSnippets: true,
enableLiveAutocompletion: true,
fontSize: 16,
tabSize: 4,
showPrintMargin: false,
highlightActiveLine: true,
}" />
</div>
<div class="card-footer glass-footer text-end">
<button class="btn btn-gradient-primary me-2" @click="runCode">
▶ 运行
</button>
<button class="btn btn-gradient-success" @click="submitCode" :disabled="submitting">
<span v-if="submitting">
<span class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
评测中...
</span>
<span v-else>✓ 提交评测</span>
</button>
</div>
</div>
</div>

<!-- 右侧：运行结果 -->
<div class="col-lg-5">
<div class="card glass-card h-100">
<div class="card-header glass-header">
<h5 class="mb-0">
    <svg class="target-icon-inline" t="1762606677765" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" >
        <path d="M0 146.285714h48.761905v780.190476H0V146.285714z m975.238095 0h48.761905v780.190476h-48.761905V146.285714z m0 731.428572v48.761904H48.761905v-48.761904h926.47619z m0-585.142857v48.761904H48.761905V292.571429h926.47619z m-243.809524 438.857142v48.761905H438.857143v-48.761905h292.571428z m243.809524-585.142857v48.761905H48.761905V146.285714h926.47619zM195.047619 438.857143h48.761905v48.761905H195.047619V438.857143z m48.761905 48.761905h48.761905v48.761904H243.809524v-48.761904z m48.761905 48.761904h48.761904v48.761905H292.571429v-48.761905z m48.761904 48.761905h48.761905v48.761905H341.333333v-48.761905z m-48.761904 48.761905h48.761904v48.761905H292.571429v-48.761905z m-48.761905 48.761905h48.761905v48.761904H243.809524v-48.761904z m-48.761905 48.761904h48.761905v48.761905H195.047619v-48.761905z" fill="#5c0a0e" p-id="12109"></path></svg>
    运行结果</h5>
</div>
<div class="card-body p-0" style="min-height: 350px;">
<div class="preview-container" v-if="hasRun">
<iframe 
ref="previewFrame"
:srcdoc="previewContent"
sandbox="allow-scripts"
frameborder="0"
class="preview-iframe"
></iframe>
</div>
<div v-else class="preview-placeholder">
<p class="text-muted">点击"运行"按钮查看结果</p>
</div>
</div>
<!-- 工具按钮 -->
<div class="card-footer glass-footer text-center">
<div class="d-grid gap-2 d-md-flex justify-content-md-end">
<button type="button" class="btn btn-glass-warning" data-bs-toggle="modal" data-bs-target="#hint-modal">
💡 提示
</button>
<button type="button" class="btn btn-glass-success" data-bs-toggle="modal" data-bs-target="#answer-modal">
✅ 查看答案
</button>
</div>
</div>
</div>
</div>
</div>
</div>

<!-- 提示模态框 -->
<teleport to="body">
<div class="modal fade" id="hint-modal" tabindex="-1" aria-labelledby="hintModalLabel" aria-hidden="true">
<div class="modal-dialog modal-dialog-centered modal-lg">
<div class="modal-content modal-glass">
<div class="modal-header">
<h5 class="modal-title" id="hintModalLabel">💡 提示</h5>
<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
</div>
<div class="modal-body">
<div v-html="renderedHint" class="markdown-content"></div>
</div>
<div class="modal-footer">
<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
</div>
</div>
</div>
</div>

<!-- 答案模态框 -->
<div class="modal fade" id="answer-modal" tabindex="-1" aria-labelledby="answerModalLabel" aria-hidden="true">
<div class="modal-dialog modal-dialog-centered modal-lg">
<div class="modal-content modal-glass">
<div class="modal-header">
<h5 class="modal-title" id="answerModalLabel">✅ 参考答案</h5>
<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
</div>
<div class="modal-body">
<div v-html="renderedAnswer" class="markdown-content"></div>
</div>
<div class="modal-footer">
<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
</div>
</div>
</div>
</div>

<!-- 评测结果模态框 -->
<div class="modal fade" id="evaluation-modal" tabindex="-1" aria-labelledby="evaluationModalLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
<div class="modal-dialog modal-dialog-centered">
<div class="modal-content modal-glass evaluation-modal-content">
<div class="modal-header border-0">
<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
</div>
<div class="modal-body text-center py-5">
<div v-if="evaluationResult">
<!-- 星星显示 -->
<div class="evaluation-stars-large mb-4">
<span v-for="n in 3" :key="n" class="star-large" :class="{'filled': n <= evaluationResult.stars}">
{{ n <= evaluationResult.stars ? '★' : '☆' }}
</span>
</div>
          
<!-- 得分文字 -->
<h2 class="evaluation-score-text mb-3">
{{ evaluationResult.stars }} / 3 星
</h2>
          
<!-- 评价信息 -->
<p class="evaluation-message-text mb-4">
{{ evaluationResult.message }}
</p>
          
<!-- 操作按钮 -->
<div class="d-grid gap-2 d-md-flex justify-content-md-center">
<button type="button" class="btn btn-gradient-primary" data-bs-dismiss="modal">
知道了
</button>
<button type="button" class="btn btn-gradient-success" @click="continueEdit" data-bs-dismiss="modal">
继续优化
</button>
</div>
</div>
</div>
</div>
</div>
</div>
</teleport>
</div>
</template>

<script>
import { ref } from 'vue';
import { marked } from 'marked';
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/mode-css';
import 'ace-builds/src-noconflict/theme-textmate';
import 'ace-builds/src-noconflict/ext-language_tools';
import { useRoute, useRouter } from 'vue-router';
import $ from 'jquery';
import { Modal } from 'bootstrap';

export default {
name: "ExerciseView",
components: {
VAceEditor,
},
data() {
return {
exerciseContent: '',
htmlContent: '',
hint: '',
answer: '',
exerciseId: null,
currentExercise: null,
loading: false,
error: null,
imageLoadError: false,
submitting: false,
evaluationResult: null,
evaluationModal: null,
}
},
setup() {
ace.config.set(
"basePath", 
"https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")

let activeTab = ref('html');
let cssCode = ref('/* 在这里编写你的 CSS 代码 */\n');
let previewFrame = ref(null);
let hasRun = ref(false);
let currentCss = ref('');

const router = useRouter();
const route = useRoute();

const runCode = () => {
currentCss.value = cssCode.value;
hasRun.value = true;
};

const editorInit = () => {
};

const goBack = () => {
router.push({ name: 'home' });
};

return {
activeTab,
cssCode,
previewFrame,
hasRun,
currentCss,
runCode,
editorInit,
goBack,
route
};
},
created() {
this.exerciseId = this.$route.params.id || '1';
this.loadExercise(this.exerciseId);
},
mounted() {
// 初始化评测结果模态框
this.$nextTick(() => {
const evaluationModalElement = document.getElementById('evaluation-modal');
if (evaluationModalElement) {
this.evaluationModal = new Modal(evaluationModalElement);
}
});
},
beforeUnmount() {
// 清理模态框
if (this.evaluationModal) {
this.evaluationModal.dispose();
}
},
watch: {
'$route.params.id'(newId) {
if (newId) {
this.exerciseId = newId;
this.loadExercise(newId);
}
}
},
methods: {
loadExercise(id) {
this.loading = true;
this.error = null;
this.evaluationResult = null;

$.ajax({
url: `http://localhost:8080/api/exercise/detail/${id}/`,
type: "get",
headers: {
Authorization: "Bearer " + this.$store.state.user.token,
},
success: (resp) => {
if (resp.error_message === "success") {
this.currentExercise = resp;
this.exerciseContent = resp.description || '';
this.htmlContent = resp.htmlContent || '';
this.hint = resp.hint || '';
this.answer = resp.answer || '';
this.cssCode = '/* 在这里编写你的 CSS 代码 */\n';
this.hasRun = false;
this.currentCss = '';
this.imageLoadError = false;
this.loading = false;
} else {
this.error = resp.error_message || "获取题目详情失败";
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

submitCode() {
if (!this.cssCode || this.cssCode.trim() === '/* 在这里编写你的 CSS 代码 */') {
alert('请先编写CSS代码再提交！');
return;
}

this.submitting = true;
this.evaluationResult = null;

$.ajax({
url: 'http://localhost:8080/api/submit/evaluate/',
type: 'post',
contentType: 'application/json',
headers: {
Authorization: "Bearer " + this.$store.state.user.token,
},
data: JSON.stringify({
exerciseId: this.exerciseId,
cssCode: this.cssCode
}),
success: (resp) => {
this.submitting = false;
if (resp.error_message === "success") {
this.evaluationResult = {
stars: resp.stars,
message: resp.message,
exerciseId: resp.exerciseId
};

// 显示评测结果模态框
if (this.evaluationModal) {
this.evaluationModal.show();
}

console.log('评测成功！得分：' + resp.stars + ' 星');
} else {
alert('评测失败：' + resp.error_message);
}
},
error: (xhr, status, error) => {
this.submitting = false;
console.error("提交评测失败:", error);
alert('提交失败，请检查网络连接或重试！');
}
});
},

continueEdit() {
// 继续编辑优化代码
console.log('继续优化代码');
},

handleImageError() {
this.imageLoadError = true;
console.warn(`图片加载失败: desired-outcome${this.exerciseId}.png`);
}
},
computed: {
renderedMarkdown() {
return marked(this.exerciseContent)
},
desiredOutcomeImage() {
try {
return require(`@/assets/images/desired-outcome${this.exerciseId}.png`);
} catch (e) {
console.warn(`无法加载图片: desired-outcome${this.exerciseId}.png`);
return '';
}
},
renderedHTML() {
return marked('```html\n' + this.htmlContent + '\n```')
},
renderedHint() {
return marked(this.hint)
},
renderedAnswer() {
return marked(this.answer)
},
previewContent() {
const parser = new DOMParser();
const doc = parser.parseFromString(this.htmlContent, 'text/html');
const styleTag = doc.createElement('style');
styleTag.textContent = this.currentCss;
doc.head.appendChild(styleTag);
return `<!DOCTYPE html>${doc.documentElement.outerHTML}`;
}
}
}
</script>

<style scoped>
.exercise-container {
min-height: 100%;
padding-bottom: 40px;
}

/* 玻璃卡片效果 */
.glass-card {
background: rgba(255, 255, 255, 0.85);
backdrop-filter: blur(10px);
-webkit-backdrop-filter: blur(10px);
border: 1px solid rgba(255, 255, 255, 0.3);
box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
border-radius: 15px;
overflow: hidden;
}

.glass-header {
background: rgba(255, 255, 255, 0.5);
backdrop-filter: blur(5px);
border-bottom: 1px solid rgba(255, 255, 255, 0.3);
padding: 1rem 1.5rem;
}

.glass-footer {
background: rgba(255, 255, 255, 0.5);
backdrop-filter: blur(5px);
border-top: 1px solid rgba(255, 255, 255, 0.3);
padding: 1rem 1.5rem;
}

.glass-alert {
background: rgba(255, 255, 255, 0.9);
backdrop-filter: blur(10px);
border: 1px solid rgba(255, 255, 255, 0.3);
box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
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

.btn-glass-warning {
background: rgba(255, 193, 7, 0.2);
backdrop-filter: blur(5px);
border: 1px solid rgba(255, 193, 7, 0.3);
color: #f57c00;
font-weight: 500;
transition: all 0.3s ease;
}

.btn-glass-warning:hover {
background: rgba(255, 193, 7, 0.3);
color: #e65100;
transform: translateY(-2px);
}

.btn-glass-success {
background: rgba(76, 175, 80, 0.2);
backdrop-filter: blur(5px);
border: 1px solid rgba(76, 175, 80, 0.3);
color: #2e7d32;
font-weight: 500;
transition: all 0.3s ease;
}

.btn-glass-success:hover {
background: rgba(76, 175, 80, 0.3);
color: #1b5e20;
transform: translateY(-2px);
}

.btn-gradient-primary {
background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
border: none;
color: white;
font-weight: 500;
padding: 0.5rem 1.5rem;
border-radius: 8px;
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
padding: 0.5rem 1.5rem;
border-radius: 8px;
transition: all 0.3s ease;
box-shadow: 0 4px 12px rgba(17, 153, 142, 0.3);
}

.btn-gradient-success:hover {
transform: translateY(-2px);
box-shadow: 0 6px 20px rgba(17, 153, 142, 0.4);
color: white;
}

.btn-gradient-success:disabled {
opacity: 0.6;
transform: none;
cursor: not-allowed;
}

/* 练习徽章 */
.exercise-badge {
background: rgb(92, 10, 14);
-webkit-background-clip: text;
-webkit-text-fill-color: transparent;
background-clip: text;
font-weight: 700;
font-size: 1.2rem;
}

/* 标签页样式 */
.glass-tab {
background: transparent;
border: none;
color: #6c757d;
font-weight: 500;
transition: all 0.3s ease;
border-radius: 8px 8px 0 0;
margin-right: 0.25rem;
}

.glass-tab:hover {
background: rgba(255, 255, 255, 0.3);
color: #495057;
}

.gradient-tab {
background: rgb(92, 10, 14) ;
border: none;
color: white !important;
font-weight: 600;
border-radius: 8px 8px 0 0;
}

/* Markdown 内容 */
.markdown-content {
text-align: left;
}

.markdown-content >>> h1 {
font-size: 1.8rem;
margin-bottom: 1rem;
border-bottom: 2px solid #e9ecef;
padding-bottom: 0.5rem;
color: #2d3748;
}

.markdown-content >>> h2 {
font-size: 1.5rem;
margin-top: 1.5rem;
margin-bottom: 0.8rem;
color: #2d3748;
}

.markdown-content >>> h3 {
font-size: 1.2rem;
margin-top: 1rem;
margin-bottom: 0.6rem;
color: #4a5568;
}

.markdown-content >>> code {
background-color: rgba(102, 126, 234, 0.1);
padding: 0.2rem 0.4rem;
border-radius: 4px;
font-family: 'Courier New', monospace;
color: #667eea;
font-size: 0.9em;
}

.markdown-content >>> pre {
background-color: #f8f9fa;
padding: 1rem;
border-radius: 8px;
overflow-x: auto;
border: 1px solid #e9ecef;
}

.markdown-content >>> pre code {
background-color: transparent;
padding: 0;
color: #2d3748;
}

.markdown-content >>> blockquote {
border-left: 4px solid #667eea;
padding-left: 1rem;
margin-left: 0;
color: #6c757d;
font-style: italic;
}

.markdown-content >>> ul, .markdown-content >>> ol {
padding-left: 2rem;
}

.markdown-content >>> li {
margin-bottom: 0.5rem;
}

/* 预览区域 */
.preview-container {
width: 100%;
height: 100%;
min-height: 350px;
background-color: white;
border-radius: 8px;
overflow: hidden;
}

.preview-iframe {
width: 100%;
height: 350px;
border: none;
}

.preview-placeholder {
width: 100%;
height: 100%;
min-height: 350px;
background: linear-gradient(135deg, rgba(102, 126, 234, 0.05) 0%, rgba(118, 75, 162, 0.05) 100%);
display: flex;
align-items: center;
justify-content: center;
border-radius: 8px;
}

.preview-placeholder p {
font-size: 1.1rem;
}

/* 加载状态 */
.loading-container {
display: flex;
flex-direction: column;
justify-content: center;
align-items: center;
min-height: 60vh;
background: rgba(255, 255, 255, 0.8);
backdrop-filter: blur(10px);
border-radius: 20px;
margin: 20px;
box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

/* 模态框 */
.modal-glass {
background: rgba(255, 255, 255, 0.95);
backdrop-filter: blur(10px);
-webkit-backdrop-filter: blur(10px);
border: 1px solid rgba(255, 255, 255, 0.3);
box-shadow: 0 8px 32px rgba(0, 0, 0, 0.2);
border-radius: 15px;
}

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

/* 美化滚动条 */
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

/* 评测结果模态框样式 */
.evaluation-modal-content {
background: rgb(249, 248, 236) !important;
border: 2px solid rgb(200, 199, 180);
box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.evaluation-stars-large {
font-size: 4rem;
letter-spacing: 10px;
margin-bottom: 1.5rem;
}

.star-large {
display: inline-block;
color: #ddd;
transition: all 0.3s ease;
text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.star-large.filled {
color: #ffd700;
text-shadow: 0 4px 8px rgba(255, 215, 0, 0.4);
animation: starPulse 0.5s ease-in-out;
}

@keyframes starPulse {
0% {
transform: scale(0.8);
opacity: 0;
}
50% {
transform: scale(1.1);
}
100% {
transform: scale(1);
opacity: 1;
}
}

.evaluation-score-text {
color: rgb(92, 10, 14);
font-weight: 700;
font-size: 2rem;
}

.evaluation-message-text {
color: rgb(38, 37, 13);
font-size: 1.3rem;
font-weight: 500;
}

/* 确保评测模态框在最上层 */
#evaluation-modal {
z-index: 2100 !important;
}

#evaluation-modal .modal-backdrop {
z-index: 2099 !important;
}

/* 响应式调整 */
@media (max-width: 992px) {
.col-lg-7, .col-lg-5 {
margin-bottom: 1rem;
}
}

/* 标题中的内联SVG图标 */
.target-icon-inline {
  width: 24px;
  height: 24px;
  vertical-align: middle;
  margin-right: 8px;
  display: inline-block;
}
</style>