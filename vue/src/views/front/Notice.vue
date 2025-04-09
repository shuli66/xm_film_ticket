<template>
  <div class="notice-container">
    <div class="notice-header">
      <div class="notice-title">
        <i class="el-icon-bell notice-icon"></i>
        <span>系统公告</span>
      </div>
      <div class="notice-subtitle">及时了解最新动态</div>
    </div>
    
    <div class="notice-content" v-loading="data.loading">
      <el-timeline>
        <el-timeline-item
          v-for="(notice, index) in data.noticeData"
          :key="index"
          :timestamp="formatTime(notice.time)"
          :type="getTimelineType(index)"
          :hollow="index % 2 === 0"
          placement="top"
          class="notice-item"
        >
          <el-card class="notice-card" :class="{'notice-card-important': notice.important}">
            <template #header>
              <div class="notice-card-header">
                <span class="notice-card-title">
                  <el-tag v-if="notice.important" type="danger" size="small" effect="dark">重要</el-tag>
                  {{ notice.title || '系统公告' }}
                </span>
                <span class="notice-card-time">{{ formatTime(notice.time) }}</span>
              </div>
            </template>
            <div class="notice-card-content">{{ notice.content }}</div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      
      <div v-if="data.noticeData.length === 0 && !data.loading" class="notice-empty">
        <el-empty description="暂无公告" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import dayjs from 'dayjs';

const data = reactive({
  noticeData: [],
  loading: false
})

const getTimelineType = (index) => {
  const types = ['primary', 'success', 'warning', 'danger'];
  return types[index % types.length];
}

const formatTime = (time) => {
  if (!time) return '';
  return dayjs(time).format('YYYY-MM-DD HH:mm');
}

const loadNotice = async () => {
  data.loading = true;
  try {
    const res = await request.get('/notice/selectAll');
    if (res.code === '200') {
      data.noticeData = res.data.map(notice => ({
        ...notice,
        important: Math.random() < 0.3 // 示例：随机标记一些公告为重要
      }));
    } else {
      ElMessage.error(res.msg);
    }
  } catch (error) {
    ElMessage.error('获取公告失败');
  } finally {
    data.loading = false;
  }
}

onMounted(() => {
  loadNotice();
})
</script>

<style scoped>
.notice-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
}

.notice-header {
  text-align: center;
  margin-bottom: 40px;
}

.notice-title {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.notice-icon {
  font-size: 24px;
  color: #409EFF;
}

.notice-subtitle {
  color: #909399;
  font-size: 14px;
}

.notice-content {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.notice-item {
  margin-bottom: 20px;
}

.notice-card {
  margin-left: 10px;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.notice-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.1);
}

.notice-card-important {
  border-left: 4px solid #F56C6C;
}

.notice-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.notice-card-title {
  font-weight: bold;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.notice-card-time {
  color: #909399;
  font-size: 13px;
}

.notice-card-content {
  color: #606266;
  line-height: 1.6;
  margin-top: 10px;
  white-space: pre-wrap;
}

.notice-empty {
  padding: 40px 0;
}

:deep(.el-timeline-item__node) {
  transition: all 0.3s ease;
}

:deep(.el-timeline-item:hover .el-timeline-item__node) {
  transform: scale(1.2);
}

:deep(.el-timeline-item__content) {
  width: calc(100% - 100px);
}

@media screen and (max-width: 768px) {
  .notice-container {
    padding: 10px;
  }
  
  .notice-title {
    font-size: 24px;
  }
  
  :deep(.el-timeline-item__content) {
    width: calc(100% - 80px);
  }
}
</style>