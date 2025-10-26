// src/data/exercises.js
export const cssExercises = [
  {
    id: "1",
    name: "CSS添加方法",
    title: "Methods for Adding CSS",
    description: `# Methods for Adding CSS
在这个练习中，你将练习使用三种方法向HTML文件添加CSS：外部CSS、内部CSS和内联CSS。

需要为三个元素添加样式：
* \`div\`: 红色背景，白色文字，字体大小32px，居中对齐，加粗
* \`p\`: 绿色背景，白色文字，字体大小18px
* \`button\`: 橙色背景，字体大小18px`,
    
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
    
    hint: `- 你是否使用了所有三种CSS添加方法？
- 是否正确链接了外部CSS文件？
- div元素是否使用外部方法添加了CSS？
- p元素是否使用内部方法添加了CSS？
- button元素是否使用内联方法添加了CSS？`,
    
    answer: `### CSS 代码
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
\`\`\``
  },
  
  {
    id: "2",
    name: "类选择器和ID选择器",
    title: "Class and ID Selectors",
    description: `# 类选择器和ID选择器
练习使用类选择器和ID选择器来样式化元素。

要求：
* 为类名为 \`intro\` 的段落设置：蓝色背景，白色文字
* 为ID为 \`title\` 的标题设置：紫色文字，下划线
* 为类名为 \`highlight\` 的span设置：黄色背景`,
    
    htmlContent: `<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Class and ID Selectors</title>
  </head>
  <body>
    <h1 id="title">Welcome to CSS</h1>
    <p class="intro">This is an introduction paragraph.</p>
    <p>This is a normal paragraph with <span class="highlight">highlighted text</span>.</p>
  </body>
</html>`,
    
    hint: `- 类选择器使用 . 前缀
- ID选择器使用 # 前缀
- 确保选择器名称与HTML中的匹配`,
    
    answer: `### CSS 代码
\`\`\`css
.intro {
    background-color: blue;
    color: white;
}

#title {
    color: purple;
    text-decoration: underline;
}

.highlight {
    background-color: yellow;
}
\`\`\``
  },
  
  {
    id: "3",
    name: "盒模型",
    title: "Box Model",
    description: `# CSS盒模型
理解并应用CSS盒模型：内边距、边框、外边距。

要求：
* 为 \`box\` 类设置：20px内边距，3px实线黑色边框，15px外边距
* 为 \`content\` 类设置：浅蓝色背景，10px内边距`,
    
    htmlContent: `<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Box Model</title>
  </head>
  <body>
    <div class="box">
      <div class="content">This is content inside the box.</div>
    </div>
  </body>
</html>`,
    
    hint: `- padding 控制内边距
- border 控制边框
- margin 控制外边距
- 记住盒模型顺序：content → padding → border → margin`,
    
    answer: `### CSS 代码
\`\`\`css
.box {
    padding: 20px;
    border: 3px solid black;
    margin: 15px;
}

.content {
    background-color: lightblue;
    padding: 10px;
}
\`\`\``
  },
  
  {
    id: "4",
    name: "Flexbox布局",
    title: "Flexbox Layout",
    description: `# Flexbox布局
使用Flexbox创建灵活的布局。

要求：
* 容器使用flex布局，元素居中对齐
* 子元素之间有20px间距
* 每个item有蓝色背景，白色文字，20px内边距`,
    
    htmlContent: `<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Flexbox Layout</title>
  </head>
  <body>
    <div class="container">
      <div class="item">Item 1</div>
      <div class="item">Item 2</div>
      <div class="item">Item 3</div>
    </div>
  </body>
</html>`,
    
    hint: `- 使用 display: flex
- justify-content 控制主轴对齐
- align-items 控制交叉轴对齐
- gap 属性控制元素间距`,
    
    answer: `### CSS 代码
\`\`\`css
.container {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 20px;
    min-height: 200px;
}

.item {
    background-color: blue;
    color: white;
    padding: 20px;
}
\`\`\``
  },
  
  {
    id: "5",
    name: "定位",
    title: "CSS Positioning",
    description: `# CSS定位
练习使用不同的定位方式。

要求：
* \`relative-box\` 使用相对定位，向右移动50px
* \`absolute-box\` 使用绝对定位，位于右上角（top: 10px, right: 10px）
* 所有box有不同颜色背景和20px内边距`,
    
    htmlContent: `<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>CSS Positioning</title>
  </head>
  <body>
    <div class="container">
      <div class="relative-box">Relative Position</div>
      <div class="absolute-box">Absolute Position</div>
    </div>
  </body>
</html>`,
    
    hint: `- position: relative 相对于原位置定位
- position: absolute 相对于最近的定位祖先元素
- 容器需要设置 position: relative 作为参考`,
    
    answer: `### CSS 代码
\`\`\`css
.container {
    position: relative;
    height: 300px;
    background-color: lightgray;
}

.relative-box {
    position: relative;
    left: 50px;
    background-color: lightgreen;
    padding: 20px;
    width: fit-content;
}

.absolute-box {
    position: absolute;
    top: 10px;
    right: 10px;
    background-color: lightcoral;
    padding: 20px;
}
\`\`\``
  },
  
  {
    id: "6",
    name: "响应式设计",
    title: "Responsive Design",
    description: `# 响应式设计
使用媒体查询创建响应式布局。

要求：
* 默认：容器宽度100%，红色背景
* 屏幕宽度>768px：容器宽度80%，蓝色背景，居中
* 屏幕宽度>1024px：容器宽度60%，绿色背景`,
    
    htmlContent: `<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Responsive Design</title>
  </head>
  <body>
    <div class="responsive-container">
      <h2>Resize the window to see changes</h2>
      <p>This container changes based on screen size.</p>
    </div>
  </body>
</html>`,
    
    hint: `- 使用 @media 查询
- min-width 表示最小宽度
- 移动优先：从小屏幕开始设计`,
    
    answer: `### CSS 代码
\`\`\`css
.responsive-container {
    width: 100%;
    background-color: red;
    color: white;
    padding: 20px;
}

@media (min-width: 768px) {
    .responsive-container {
        width: 80%;
        margin: 0 auto;
        background-color: blue;
    }
}

@media (min-width: 1024px) {
    .responsive-container {
        width: 60%;
        background-color: green;
    }
}
\`\`\``
  },
  
  {
    id: "7",
    name: "文本样式",
    title: "Text Styling",
    description: `# 文本样式
练习各种文本样式属性。

要求：
* 标题：Arial字体，36px，加粗，字母间距2px
* 段落：行高1.6，首行缩进30px，两端对齐
* 链接：无下划线，hover时显示下划线和变色`,
    
    htmlContent: `<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Text Styling</title>
  </head>
  <body>
    <h1 class="title">Beautiful Typography</h1>
    <p class="paragraph">
      Lorem ipsum dolor sit amet, consectetur adipiscing elit. 
      Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
    </p>
    <a href="#" class="link">Click here to learn more</a>
  </body>
</html>`,
    
    hint: `- font-family 设置字体
- line-height 控制行高
- text-indent 控制首行缩进
- text-decoration 控制下划线`,
    
    answer: `### CSS 代码
\`\`\`css
.title {
    font-family: Arial, sans-serif;
    font-size: 36px;
    font-weight: bold;
    letter-spacing: 2px;
}

.paragraph {
    line-height: 1.6;
    text-indent: 30px;
    text-align: justify;
}

.link {
    text-decoration: none;
    color: blue;
}

.link:hover {
    text-decoration: underline;
    color: darkblue;
}
\`\`\``
  },
  
  {
    id: "8",
    name: "背景样式",
    title: "Background Styling",
    description: `# 背景样式
练习背景相关属性。

要求：
* box1：线性渐变背景（从左到右，红到黄）
* box2：径向渐变背景（从中心，蓝到白）
* 所有box高度200px，圆角10px，边距10px`,
    
    htmlContent: `<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Background Styling</title>
  </head>
  <body>
    <div class="box1">Linear Gradient</div>
    <div class="box2">Radial Gradient</div>
  </body>
</html>`,
    
    hint: `- linear-gradient() 创建线性渐变
- radial-gradient() 创建径向渐变
- border-radius 设置圆角`,
    
    answer: `### CSS 代码
\`\`\`css
.box1, .box2 {
    height: 200px;
    border-radius: 10px;
    margin: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    color: white;
}

.box1 {
    background: linear-gradient(to right, red, yellow);
}

.box2 {
    background: radial-gradient(circle, blue, white);
    color: black;
}
\`\`\``
  },
  
  {
    id: "9",
    name: "Grid布局",
    title: "CSS Grid Layout",
    description: `# Grid布局
使用CSS Grid创建网格布局。

要求：
* 容器使用3列网格，每列相等宽度
* 网格间距20px
* 每个item有浅蓝背景，居中文字，100px高度`,
    
    htmlContent: `<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Grid Layout</title>
  </head>
  <body>
    <div class="grid-container">
      <div class="grid-item">1</div>
      <div class="grid-item">2</div>
      <div class="grid-item">3</div>
      <div class="grid-item">4</div>
      <div class="grid-item">5</div>
      <div class="grid-item">6</div>
    </div>
  </body>
</html>`,
    
    hint: `- display: grid 启用网格布局
- grid-template-columns 定义列
- gap 设置网格间距
- 使用 fr 单位创建等宽列`,
    
    answer: `### CSS 代码
\`\`\`css
.grid-container {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    gap: 20px;
    padding: 20px;
}

.grid-item {
    background-color: lightblue;
    height: 100px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
}
\`\`\``
  },
  
  {
    id: "10",
    name: "动画和过渡",
    title: "Animations and Transitions",
    description: `# 动画和过渡
创建平滑的过渡效果和动画。

要求：
* 按钮hover时：背景色变化，放大1.1倍，0.3s过渡
* box执行无限旋转动画，4秒一圈
* 所有效果平滑过渡`,
    
    htmlContent: `<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8">
    <title>Animations</title>
  </head>
  <body>
    <button class="animated-btn">Hover Me!</button>
    <div class="rotating-box">Rotating</div>
  </body>
</html>`,
    
    hint: `- transition 属性创建过渡效果
- @keyframes 定义动画
- animation 属性应用动画
- transform 用于变换效果`,
    
    answer: `### CSS 代码
\`\`\`css
.animated-btn {
    padding: 15px 30px;
    font-size: 18px;
    background-color: blue;
    color: white;
    border: none;
    cursor: pointer;
    transition: all 0.3s ease;
}

.animated-btn:hover {
    background-color: darkblue;
    transform: scale(1.1);
}

.rotating-box {
    width: 100px;
    height: 100px;
    background-color: red;
    color: white;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 50px;
    animation: rotate 4s linear infinite;
}

@keyframes rotate {
    from {
        transform: rotate(0deg);
    }
    to {
        transform: rotate(360deg);
    }
}
\`\`\``
  }
];

// 定义练习题之间的关系（用于关系图）
export const exerciseLinks = [
  { source: "1", target: "2" },  // CSS添加方法 -> 选择器
  { source: "2", target: "3" },  // 选择器 -> 盒模型
  { source: "3", target: "4" },  // 盒模型 -> Flexbox
  { source: "3", target: "5" },  // 盒模型 -> 定位
  { source: "4", target: "6" },  // Flexbox -> 响应式
  { source: "2", target: "7" },  // 选择器 -> 文本样式
  { source: "3", target: "8" },  // 盒模型 -> 背景样式
  { source: "4", target: "9" },  // Flexbox -> Grid
  { source: "7", target: "10" }, // 文本样式 -> 动画
  { source: "8", target: "10" }, // 背景样式 -> 动画
];