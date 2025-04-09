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
  background-color: #f0f4f8; /* 更柔和的背景色 */
  min-height: 100vh;
  padding: 60px 30px; /* 增加内边距 */
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: 'Arial', sans-serif;
}

/* 欢迎卡片样式 */
.welcome-card {
  background-color: #ffffff;
  padding: 40px; /* 增加内边距 */
  border-radius: 15px; /* 更圆的边角 */
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15); /* 更明显的阴影 */
  text-align: center;
  margin-bottom: 40px; /* 增加底部间距 */
  width: 100%;
  max-width: 900px; /* 增加最大宽度 */
}

.welcome-message h1 {
  font-size: 32px; /* 增加字体大小 */
  font-weight: 700; /* 更加醒目的字体 */
  color: #333;
  margin: 0;
}

.welcome-message p {
  font-size: 20px; /* 增加字体大小 */
  margin-top: 15px;
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
  max-width: 900px; /* 增加最大宽度 */
  background: #ffffff;
  border-radius: 15px; /* 更圆的边角 */
  padding: 40px; /* 增加内边距 */
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15); /* 更明显的阴影 */
}

.announcement-header h2 {
  font-size: 28px; /* 增加字体大小 */
  color: #007bff;
  font-weight: 700; /* 更加醒目的字体 */
  margin-bottom: 25px; /* 增加底部间距 */
  border-bottom: 2px solid #007bff;
  padding-bottom: 12px; /* 增加底部内边距 */
}

/* 每个公告的框样式 */
.announcement-item {
  display: flex;
  justify-content: flex-start; /* 设置左对齐 */
}

.announcement-box {
  background-color: #ffffff;
  border: 2px solid #e2e6ea;
  border-radius: 10px; /* 更圆的边角 */
  padding: 25px; /* 增加内边距 */
  margin-bottom: 20px; /* 增加底部间距 */
  width: 100%;
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1); /* 更加明显的阴影 */
  font-size: 18px; /* 增加字体大小 */
  color: #444;
  transition: transform 0.3s, box-shadow 0.3s; /* 增加动画效果 */
}

/* 鼠标悬停效果 */
.announcement-box:hover {
  transform: translateY(-5px); /* 鼠标悬停时稍微抬升 */
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.25); /* 增加阴影 */
}

/* 时间线样式 */
.timeline .el-timeline-item {
  position: relative;
  padding-left: 50px; /* 增加左侧内边距 */
}

.timeline .el-timeline-item .el-timeline-item-tail {
  border-left: 4px solid #007bff;
}

.timeline .el-timeline-item .el-timeline-item-content {
  font-size: 18px; /* 增加字体大小 */
  color: #444;
}

.timeline .el-timeline-item .el-timeline-item-time {
  font-size: 16px; /* 增加字体大小 */
  color: #aaa;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 30px; /* 减少内边距 */
  }

  .welcome-card {
    padding: 25px; /* 减少内边距 */
  }

  .announcement-section {
    padding: 25px; /* 减少内边距 */
  }

  .announcement-header h2 {
    font-size: 24px; /* 减少字体大小 */
  }
}
</style>
