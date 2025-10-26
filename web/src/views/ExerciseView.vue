<template>
<div class="container">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
        <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">加载中...</span>
        </div>
        <p class="mt-3">正在加载练习题...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="alert alert-danger" role="alert">
        <h4 class="alert-heading">加载失败</h4>
        <p>{{ error }}</p>
        <button class="btn btn-primary" @click="loadExercise(exerciseId)">重试</button>
    </div>

    <!-- 练习内容 -->
    <div v-else>
        <div class="d-flex justify-content-between align-items-center mb-3">
            <button class="btn btn-outline-secondary" @click="goBack">
                ← 返回关系图
            </button>
            <h5 class="mb-0">练习 {{ exerciseId }}</h5>
        </div>

        <div class="card text-center h100">
            <div class="card-header">题目描述</div>
            <div class="card-body">
                <div class="row">
                    <div class="col-12">
                        <div v-html="renderedMarkdown" class="markdown-content"></div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-12">
                        <div class="card">
                            <div class="card-header">预期效果</div>
                            <div class="card-body">
                                <img :src="desiredOutcomeImage" class="img-fluid" alt="预期效果" @error="handleImageError">
                                <p v-if="imageLoadError" class="text-muted text-center mt-2">
                                    <small>预期效果图片加载失败</small>
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row" style="margin-top: 20px;">
            <div class="col-7">
                <div class="card text-center h-100">
                    <div class="card-header">
                        <ul class="nav nav-tabs card-header-tabs">
                            <li class="nav-item">
                                <a :class="activeTab === 'html' ? 'nav-link active': 'nav-link'" aria-current="true" href="#" @click.prevent="activeTab = 'html'">HTML</a>
                            </li>
                            <li class="nav-item">
                                <a :class="activeTab === 'css' ? 'nav-link active': 'nav-link'" href="#" @click.prevent="activeTab = 'css'">CSS</a>
                            </li>
                        </ul>
                    </div>
                    <div class="card-body">
                            <div v-if="activeTab === 'html'" v-html="renderedHTML" class="markdown-content"></div>

                            <VAceEditor v-else-if="activeTab === 'css'"
                                        v-model:value="cssCode"
                                        @init="editorInit" 
                                        lang="css"
                                        theme="textmate" 
                                        style="height: 300px" 
                                        :options="{
                                            enableBasicAutocompletion: true,
                                            enableSnippets: true,
                                            enableLiveAutocompletion: true,
                                            fontSize: 18,
                                            tabSize: 4,
                                            showPrintMargin: false,
                                            highlightActiveLine: true,
                                        }" />
                    </div>
                    <div class="card-footer text-end">   
                        <button class="btn btn-primary me-2" @click="runCode">运行</button>
                        <button class="btn btn-success" @click="submitCode" :disabled="submitting">
                            <span v-if="submitting">
                                <span class="spinner-border spinner-border-sm me-1" role="status" aria-hidden="true"></span>
                                评测中...
                            </span>
                            <span v-else>提交评测</span>
                        </button>
                    </div>
                </div>
            </div>
            <div class="col-5">
                <div class="card text-center h-100">
                    <div class="card-header">运行结果</div>
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

                    <!-- 评测结果显示区域 -->
                    <div v-if="evaluationResult" class="evaluation-result mt-3 p-3">
                        <div class="stars-container mb-2">
                            <span v-for="n in 3" :key="n" class="star">
                                {{ n <= evaluationResult.stars ? '★' : '☆' }}
                            </span>
                        </div>
                        <p class="evaluation-message mb-0">{{ evaluationResult.message }}</p>
                        <small class="text-muted">得分: {{ evaluationResult.stars }}/3 星</small>
                    </div>

                    <div class="card-footer text-center">
                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <!-- Button trigger modal -->
                            <button type="button" class="btn btn-outline-warning" data-bs-toggle="modal" data-bs-target="#self-check-modal">
                                💡提示
                            </button>
                            <!-- Modal -->
                            <div class="modal fade" id="self-check-modal" tabindex="-1" aria-labelledby="hintModalLabel" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered modal-lg">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h1 class="modal-title fs-5" id="hintModalLabel">💡 提示</h1>
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

                            <!-- Button trigger modal -->
                            <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#answer-modal">
                                查看答案
                            </button>
                            <!-- Modal -->
                            <div class="modal fade" id="answer-modal" tabindex="-1" aria-labelledby="answerModalLabel" aria-hidden="true">
                                <div class="modal-dialog modal-dialog-centered modal-lg">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h1 class="modal-title fs-5" id="answerModalLabel">✅ 参考答案</h1>
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
                        </div>
                    </div>
                </div>
            </div>
                
        </div>
    </div>
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

        // 运行代码
        const runCode = () => {
            currentCss.value = cssCode.value;
            hasRun.value = true;
        };

        const editorInit = () => {
            // 编辑器初始化回调
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
        // 从路由参数获取练习ID
        this.exerciseId = this.$route.params.id || '1';
        this.loadExercise(this.exerciseId);
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
            
            // 从后端获取练习题详情
            $.ajax({
                url: `http://localhost:8080/api/exercise/detail/${id}/`,
                type: "get",
                headers: {
                    Authorization: "Bearer " + this.$store.state.user.token,
                },
                success: (resp) => {
                    if (resp.error_message === "success") {
                        // 后端直接返回练习题的各个字段
                        this.currentExercise = resp;
                        this.exerciseContent = resp.description || '';
                        this.htmlContent = resp.htmlContent || '';
                        this.hint = resp.hint || '';
                        this.answer = resp.answer || '';
                        
                        // 重置状态
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

            // 提交代码到后端进行评测
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
                        
                        // 可选：显示提示信息
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
            // 动态生成图片路径，根据exerciseId
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
.markdown-content {
    text-align: left;
}

.markdown-content >>> h1 {
    font-size: 1.8rem;
    margin-bottom: 1rem;
    border-bottom: 2px solid #e9ecef;
    padding-bottom: 0.5rem;
}

.markdown-content >>> h2 {
    font-size: 1.5rem;
    margin-top: 1.5rem;
    margin-bottom: 0.8rem;
}

.markdown-content >>> h3 {
    font-size: 1.2rem;
    margin-top: 1rem;
    margin-bottom: 0.6rem;
}

.markdown-content >>> code {
    background-color: #f8f9fa;
    padding: 0.2rem 0.4rem;
    border-radius: 3px;
    font-family: 'Courier New', monospace;
}

.markdown-content >>> pre {
    background-color: #f8f9fa;
    padding: 1rem;
    border-radius: 5px;
    overflow-x: auto;
}

.markdown-content >>> pre code {
    background-color: transparent;
    padding: 0;
}

.markdown-content >>> blockquote {
    border-left: 4px solid #0d6efd;
    padding-left: 1rem;
    margin-left: 0;
    color: #6c757d;
}

.markdown-content >>> ul, .markdown-content >>> ol {
    padding-left: 2rem;
}

.markdown-content >>> li {
    margin-bottom: 0.5rem;
}

.preview-container {
    width: 100%;
    height: 100%;
    min-height: 300px;
    background-color: white;
    border: 1px solid #dee2e6;
    border-radius: 5px;
    overflow: hidden;
}

.preview-iframe {
    width: 100%;
    height: 300px;
    border: none;
}

.preview-placeholder {
    width: 100%;
    height: 100%;
    min-height: 300px;
    background-color: #f8f9fa;
    border: 1px solid #dee2e6;
    border-radius: 5px;
    display: flex;
    align-items: center;
    justify-content: center;
}

.card-footer {
    background-color: white;
    padding: 0.75rem 1.25rem;
}

.modal-lg {
    max-width: 800px;
}

.modal-body {
    max-height: 60vh;
    overflow-y: auto;
}

.loading-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 400px;
}

/* 评测结果样式 */
.evaluation-result {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 10px;
    color: white;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.stars-container {
    font-size: 2.5rem;
    letter-spacing: 5px;
}

.star {
    display: inline-block;
    color: #ffd700;
    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.evaluation-message {
    font-size: 1.1rem;
    font-weight: 500;
}
</style>