<template>
<div class="container">
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
                            <img src="@/assets/images/desired-outcome.png" class="img-fluid" alt="">
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
                    <button class="btn btn-primary" @click="runCode">运行</button>
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
</template>

<script>
import { ref } from 'vue';
import { marked } from 'marked'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/mode-css';
import 'ace-builds/src-noconflict/theme-textmate';
import 'ace-builds/src-noconflict/ext-language_tools';

export default {
    name: "ExerciseView",
    components: {
        VAceEditor,
    },
    data() {
        return {
            exerciseContent: `# Methods for Adding CSS
In this exercise, you're going to practice adding CSS to an HTML file using all three methods: external CSS, internal CSS, and inline CSS. You should only be using type selectors for this exercise when adding styles via the external and internal methods. You should also use keywords for colors (e.g. "blue") instead of using RGB or HEX values.

There are three elements for you to add styles to, each of which uses a different method of adding CSS to it, as noted in the outcome image below. All other exercises in this section will have a CSS file provided and linked for you, but for this exercise you will have to create the file and link it in the HTML file yourself. This is all about practicing using these different methods and getting the syntax right.

The properties you need to add to each element are: 
* \`div\`: a red background, white text, a font size of 32px, center aligned, and bold
* \`p\`: a green background, white text, and a font size of 18px
* \`button\`: an orange background and a font size of 18px`,
            
            htmlContent: `<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Methods for Adding CSS</title>
  </head>
  <body>
    <div>Style me via the external method!</div>
    <p>I would like to be styled with the internal method, please.</p>
    <button>Inline Method</button>
  </body>
</html>`,
            
            hint: `
- Did you use all three methods of adding CSS to an HTML file?
- Did you properly link the external CSS file in the HTML file?
- Does the \`div\` element have CSS added via the external method?
- Does the \`p\` element have CSS added via the internal method?
- Does the \`button\` element have CSS added via the inline method?`,
            
            answer: `
### CSS 代码

\`\`\`css
div {
    background-color: red;
    color: white;
    font-size: 32px;
    text-align: center;
    font-weight: bold;
}

p {
    background-color: green;
    color: white;
    font-size: 18px;
}

button {
    background-color: orange;
    font-size: 18px;
}
\`\`\`

`
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

        // 运行代码
        const runCode = () => {
            currentCss.value = cssCode.value;
            hasRun.value = true;
        };

        const editorInit = () => {
            // 编辑器初始化回调
        };

        return {
            activeTab,
            cssCode,
            previewFrame,
            hasRun,
            currentCss,
            runCode,
            editorInit,
        };
    },
    computed: {
        renderedMarkdown() {
            return marked(this.exerciseContent)
        },
        renderedHTML() {
            // 将 HTML 包裹在代码块中显示
            return marked('```html\n' + this.htmlContent + '\n```')
        },
        renderedHint() {
            return marked(this.hint)
        },
        renderedAnswer() {
            return marked(this.answer)
        },
        previewContent() {
            // 生成预览内容
            const parser = new DOMParser();
            const doc = parser.parseFromString(this.htmlContent, 'text/html');
            
            // 创建 style 标签并插入 CSS
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

/* Markdown 样式优化 */
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

/* 预览容器样式 */
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

/* Modal 样式优化 */
.modal-lg {
    max-width: 800px;
}

.modal-body {
    max-height: 60vh;
    overflow-y: auto;
}
</style>