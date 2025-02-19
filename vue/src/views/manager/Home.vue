<template>
  <div class="container">
    <!-- 欢迎卡片 -->
    <div class="welcome-card">
      <div class="welcome-message">
        <h1>您好，{{ data.user?.name }}！</h1>
        <p>欢迎使用本系统！</p>
      </div>
    </div>

    <div class="content-wrapper">
      <div class="announcement-section">
        <div class="announcement-header">
          <h2>系统公告</h2>
        </div>
        <el-timeline class="timeline">
          <el-timeline-item
              v-for="(item, index) in data.noticeData"
              :key="index"
              :timestamp="item.time"
              class="announcement-item"
          >
            <div class="announcement-box">
              {{ item.content }}
            </div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  noticeData: []
})

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadNotice()
</script>

<style scoped>
/* 整体容器 */
.container {
  background-color: #f8f9fa;
  min-height: 100vh;
  padding: 50px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: 'Arial', sans-serif;
}

/* 欢迎卡片样式 */
.welcome-card {
  background-color: #ffffff;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  text-align: center;
  margin-bottom: 30px;
  width: 100%;
  max-width: 800px;
}

.welcome-message h1 {
  font-size: 28px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.welcome-message p {
  font-size: 18px;
  margin-top: 10px;
  color: #666;
}

/* 内容部分布局 */
.content-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.announcement-section {
  width: 100%;
  max-width: 800px;
  background: #ffffff;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.announcement-header h2 {
  font-size: 24px;
  color: #007bff;
  font-weight: 600;
  margin-bottom: 20px;
  border-bottom: 2px solid #007bff;
  padding-bottom: 10px;
}

/* 每个公告的框样式 */
.announcement-item {
  display: flex;
  justify-content: flex-start;  /* 设置左对齐 */
}

.announcement-box {
  background-color: #ffffff;
  border: 2px solid #e2e6ea;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 15px;
  width: 100%;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);  /* 更加明显的阴影 */
  font-size: 16px;
  color: #444;
  transition: transform 0.3s, box-shadow 0.3s;  /* 增加动画效果 */
}

/* 鼠标悬停效果 */
.announcement-box:hover {
  transform: translateY(-5px);  /* 鼠标悬停时稍微抬升 */
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);  /* 增加阴影 */
}

/* 时间线样式 */
.timeline .el-timeline-item {
  position: relative;
  padding-left: 40px;
}

.timeline .el-timeline-item .el-timeline-item-tail {
  border-left: 4px solid #007bff;
}

.timeline .el-timeline-item .el-timeline-item-content {
  font-size: 16px;
  color: #444;
}

.timeline .el-timeline-item .el-timeline-item-time {
  font-size: 14px;
  color: #aaa;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 20px;
  }

  .welcome-card {
    padding: 20px;
  }

  .announcement-section {
    padding: 20px;
  }

  .announcement-header h2 {
    font-size: 20px;
  }
}
</style>
