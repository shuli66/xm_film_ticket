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
          <span class="announcement-count">{{ data.noticeData.length }}条公告</span>
        </div>
        <el-timeline class="timeline">
          <el-timeline-item
              v-for="(item, index) in data.noticeData"
              :key="index"
              :timestamp="item.time"
              :type="getNoticeType(index)"
              :color="getNoticeColor(index)"
              class="announcement-item"
          >
            <div class="announcement-box">
              <div class="announcement-content">{{ item.content }}</div>
              <div class="announcement-footer">
                <span class="announcement-time">{{ formatTime(item.time) }}</span>
                <el-tag 
                  size="small" 
                  :type="index % 4 === 0 ? 'danger' : index % 3 === 0 ? 'warning' : index % 2 === 0 ? 'success' : 'info'"
                  effect="dark"
                >
                  {{ index % 4 === 0 ? '重要' : index % 3 === 0 ? '系统' : index % 2 === 0 ? '功能' : '常规' }}
                </el-tag>
              </div>
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

// 格式化时间为更易读的格式
const formatTime = (time) => {
  if (!time) return '';
  const date = new Date(time);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
}

// 根据索引生成不同类型的时间线项目
const getNoticeType = (index) => {
  const types = ['primary', 'success', 'warning', 'danger', 'info'];
  return types[index % types.length];
}

// 根据索引生成不同颜色的时间线项目
const getNoticeColor = (index) => {
  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399'];
  return colors[index % colors.length];
}

loadNotice()
</script>

<style scoped>
/* 整体容器 */
.container {
  background-color: #f5f7fa;
  min-height: 100vh;
  padding: 60px 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: 'Arial', sans-serif;
}

/* 欢迎卡片样式 */
.welcome-card {
  background: linear-gradient(135deg, #3f87e8, #2a6fc5);
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(47, 128, 237, 0.2);
  text-align: center;
  margin-bottom: 40px;
  width: 100%;
  max-width: 900px;
  position: relative;
  overflow: hidden;
}

.welcome-card::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 70%);
  opacity: 0.7;
  pointer-events: none;
}

.welcome-message h1 {
  font-size: 32px;
  font-weight: 700;
  color: white;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.welcome-message p {
  font-size: 20px;
  margin-top: 15px;
  color: rgba(255, 255, 255, 0.85);
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
  max-width: 900px;
  background: #ffffff;
  border-radius: 16px;
  padding: 30px 40px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

.announcement-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  position: relative;
}

.announcement-header h2 {
  font-size: 24px;
  color: #333;
  font-weight: 600;
  margin: 0;
  padding-left: 15px;
  position: relative;
}

.announcement-header h2::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 22px;
  background: #3f87e8;
  border-radius: 2px;
}

.announcement-count {
  font-size: 14px;
  color: #909399;
  background: #f5f7fa;
  padding: 4px 12px;
  border-radius: 12px;
}

/* 每个公告的框样式 */
.timeline {
  padding: 10px 0;
}

.announcement-item {
  margin-bottom: 20px;
}

.announcement-box {
  background-color: #f9fafc;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 10px;
  width: 100%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
  border-left: 4px solid #3f87e8;
}

.announcement-content {
  font-size: 16px;
  color: #333;
  line-height: 1.6;
  margin-bottom: 15px;
}

.announcement-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.announcement-time {
  color: #909399;
}

/* 鼠标悬停效果 */
.announcement-box:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  background-color: #fff;
}

/* 时间线样式定制 */
:deep(.el-timeline-item__node) {
  box-shadow: 0 0 0 4px rgba(63, 135, 232, 0.2);
}

:deep(.el-timeline-item__timestamp) {
  color: #909399;
  margin-top: 8px;
}

:deep(.el-timeline-item__tail) {
  border-left: 2px dashed #dcdfe6;
}

:deep(.el-tag--dark) {
  border: none;
  font-weight: 500;
  padding: 0 8px;
  height: 22px;
  line-height: 22px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 20px;
  }

  .welcome-card {
    padding: 25px;
    margin-bottom: 25px;
  }

  .announcement-section {
    padding: 20px;
  }

  .announcement-header h2 {
    font-size: 20px;
  }
  
  .announcement-content {
    font-size: 14px;
  }
  
  .announcement-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
}
</style>
