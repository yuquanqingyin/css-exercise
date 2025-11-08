<template>
<div class="container content-field">
 <div class="card glass-card">
 <div class="card-body">
 <slot></slot>
 </div>
 </div>
</div>
</template>

<script>
export default{
 name: "ContentField",
}
</script>

<style scoped>
.content-field {
  margin-top: 20px;
  margin-bottom: 20px;
  padding-bottom: 20px;
}

.glass-card {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  border-radius: 15px;
  overflow: visible; /* 改为 visible，避免遮挡下拉菜单 */
}

.card-body {
  padding: 2rem;
}
</style>