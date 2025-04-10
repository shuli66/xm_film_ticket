<template>
  <div class="container">
    <!-- 欢迎卡片 -->
    <div class="welcome-card" :class="{ 'animate-in': showWelcome }">
      <div class="welcome-message">
        <div class="welcome-title">
          <h1>您好，<span class="highlight">{{ data.user?.name }}</span>！</h1>
          <div class="welcome-subtitle">
            <p>欢迎使用本系统</p>
            <div class="welcome-time">
              <el-icon><Clock /></el-icon>
              <span>{{ currentTime }}</span>
            </div>
          </div>
        </div>
        <div class="welcome-stats">
          <div class="stat-item">
            <el-icon><Document /></el-icon>
            <div class="stat-info">
              <span class="stat-value">{{ data.noticeData.length }}</span>
              <span class="stat-label">系统公告</span>
            </div>
          </div>
          <div class="stat-item">
            <el-icon><User /></el-icon>
            <div class="stat-info">
              <span class="stat-value">{{ data.user?.role || '管理员' }}</span>
              <span class="stat-label">用户角色</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="content-wrapper">
      <div class="announcement-section">
        <div class="announcement-header">
          <h2>系统公告</h2>
          <div class="announcement-stats">
            <el-tag type="info">共 {{ data.noticeData.length }} 条公告</el-tag>
          </div>
        </div>
        <el-timeline class="timeline">
          <el-timeline-item
              v-for="(item, index) in data.noticeData"
              :key="index"
              :timestamp="item.time"
              :type="getTimelineItemType(index)"
              class="announcement-item"
              placement="top"
          >
            <el-card class="announcement-box" :body-style="{ padding: '20px' }">
              <div class="announcement-content">
                <div class="announcement-title">
                  <el-icon><Bell /></el-icon>
                  <span>公告 #{{ data.noticeData.length - index }}</span>
                </div>
                <p class="announcement-text">{{ item.content }}</p>
                <div class="announcement-footer">
                  <span class="time">发布时间：{{ formatDate(item.time) }}</span>
                  <el-button type="primary" size="small" @click="viewDetails(item)">
                    查看详情
                    <el-icon class="el-icon--right"><ArrowRight /></el-icon>
                  </el-button>
                </div>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import { Bell, ArrowRight, Clock, Document, User } from '@element-plus/icons-vue'

const showWelcome = ref(false)
const currentTime = ref('')

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  noticeData: []
})

const getTimelineItemType = (index) => {
  const types = ['primary', 'success', 'warning', 'danger']
  return types[index % types.length]
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const viewDetails = (item) => {
  ElMessage({
    message: `查看公告详情: ${item.content}`,
    type: 'info',
    duration: 2000
  })
}

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 更新时间
const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
    hour12: false
  })
}

onMounted(() => {
  // 显示欢迎动画
  setTimeout(() => {
    showWelcome.value = true
  }, 100)
  
  // 更新时间
  updateTime()
  setInterval(updateTime, 1000)
})

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
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  padding: 40px;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  margin-bottom: 40px;
  width: 100%;
  max-width: 1000px;
  transform: translateY(20px);
  opacity: 0;
  transition: all 0.6s cubic-bezier(0.4, 0, 0.2, 1);
}

.welcome-card.animate-in {
  transform: translateY(0);
  opacity: 1;
}

.welcome-message {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 40px;
}

.welcome-title {
  flex: 1;
}

.welcome-title h1 {
  font-size: 36px;
  font-weight: 700;
  color: #2c3e50;
  margin: 0;
  line-height: 1.2;
}

.highlight {
  color: #409EFF;
  position: relative;
  display: inline-block;
}

.highlight::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, #409EFF, #36cfc9);
  border-radius: 2px;
  animation: highlight-pulse 2s infinite;
}

@keyframes highlight-pulse {
  0% { opacity: 0.5; }
  50% { opacity: 1; }
  100% { opacity: 0.5; }
}

.welcome-subtitle {
  margin-top: 15px;
  display: flex;
  align-items: center;
  gap: 20px;
}

.welcome-subtitle p {
  font-size: 18px;
  color: #606266;
  margin: 0;
}

.welcome-time {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #909399;
  font-size: 14px;
}

.welcome-stats {
  display: flex;
  gap: 30px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 15px 20px;
  background: #f5f7fa;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-item .el-icon {
  font-size: 24px;
  color: #409EFF;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .welcome-card {
    padding: 30px;
  }

  .welcome-message {
    flex-direction: column;
    gap: 20px;
  }

  .welcome-title h1 {
    font-size: 28px;
  }

  .welcome-subtitle {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .welcome-stats {
    width: 100%;
    justify-content: space-between;
  }

  .stat-item {
    padding: 12px 15px;
  }
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
  max-width: 1000px;
  background: #ffffff;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
}

.announcement-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 15px;
  border-bottom: 2px solid #eaeaea;
}

.announcement-header h2 {
  font-size: 24px;
  color: #2c3e50;
  font-weight: 600;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.announcement-stats {
  display: flex;
  gap: 10px;
}

.announcement-box {
  margin-bottom: 20px;
  border-radius: 8px;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.announcement-box:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
}

.announcement-content {
  position: relative;
}

.announcement-title {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
}

.announcement-text {
  font-size: 15px;
  line-height: 1.6;
  color: #5c6c7c;
  margin: 12px 0;
}

.announcement-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #f0f0f0;
}

.time {
  font-size: 14px;
  color: #909399;
}

.timeline {
  padding: 20px 0;
}

.timeline :deep(.el-timeline-item__node) {
  width: 16px;
  height: 16px;
}

.timeline :deep(.el-timeline-item__tail) {
  border-left: 2px solid #e4e7ed;
}

.timeline :deep(.el-card__body) {
  padding: 20px;
}

/* 响应式设计优化 */
@media (max-width: 768px) {
  .announcement-section {
    padding: 20px;
  }

  .announcement-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .announcement-footer {
    flex-direction: column;
    gap: 10px;
    align-items: flex-start;
  }

  .announcement-text {
    font-size: 14px;
  }
}
</style>
