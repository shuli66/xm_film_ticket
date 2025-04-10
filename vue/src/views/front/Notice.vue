<template>
  <div class="notice-container">
    <div class="notice-header">
      <div class="notice-title">
        <el-icon class="notice-icon"><Bell /></el-icon>
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
          <el-card 
            class="notice-card" 
            :class="{
              'notice-card-important': notice.important,
              'notice-card-new': isNewNotice(notice.time)
            }"
            @click="showNoticeDetail(notice)"
          >
            <template #header>
              <div class="notice-card-header">
                <div class="notice-card-title">
                  <el-tag 
                    v-if="notice.important" 
                    type="danger" 
                    size="small" 
                    effect="dark"
                    class="notice-tag"
                  >
                    重要
                  </el-tag>
                  <el-tag 
                    v-if="isNewNotice(notice.time)" 
                    type="success" 
                    size="small" 
                    effect="light"
                    class="notice-tag"
                  >
                    新
                  </el-tag>
                  <span class="title-text">{{ notice.title || '系统公告' }}</span>
                </div>
                <div class="notice-card-meta">
                  <span class="notice-card-time">
                    <el-icon><Clock /></el-icon>
                    {{ formatTime(notice.time) }}
                  </span>
                </div>
              </div>
            </template>
            <div class="notice-card-content">
              <div class="content-text">{{ notice.content }}</div>
              <div class="notice-card-footer">
                <el-button 
                  type="primary" 
                  size="small" 
                  @click.stop="showNoticeDetail(notice)"
                >
                  查看详情
                  <el-icon class="el-icon--right"><ArrowRight /></el-icon>
                </el-button>
              </div>
            </div>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      
      <div v-if="data.noticeData.length === 0 && !data.loading" class="notice-empty">
        <el-empty description="暂无公告" />
      </div>
    </div>

    <!-- 公告详情弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="currentNotice?.title || '公告详情'"
      width="50%"
      :close-on-click-modal="false"
    >
      <div class="notice-detail">
        <div class="detail-header">
          <div class="detail-meta">
            <span class="detail-time">
              <el-icon><Clock /></el-icon>
              {{ formatTime(currentNotice?.time) }}
            </span>
            <el-tag 
              v-if="currentNotice?.important" 
              type="danger" 
              size="small" 
              effect="dark"
            >
              重要
            </el-tag>
          </div>
        </div>
        <div class="detail-content">
          {{ currentNotice?.content }}
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import { Bell, Clock, ArrowRight } from '@element-plus/icons-vue';
import dayjs from 'dayjs';

const data = reactive({
  noticeData: [],
  loading: false
})

const dialogVisible = ref(false);
const currentNotice = ref(null);

const getTimelineType = (index) => {
  const types = ['primary', 'success', 'warning', 'danger'];
  return types[index % types.length];
}

const formatTime = (time) => {
  if (!time) return '';
  return dayjs(time).format('YYYY-MM-DD HH:mm');
}

const isNewNotice = (time) => {
  const noticeDate = dayjs(time);
  const now = dayjs();
  return now.diff(noticeDate, 'day') <= 3; // 3天内的公告标记为新
}

const showNoticeDetail = (notice) => {
  currentNotice.value = notice;
  dialogVisible.value = true;
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
  max-width: 1000px;
  margin: 30px auto;
  padding: 20px;
}

.notice-header {
  text-align: center;
  margin-bottom: 40px;
  animation: fadeInDown 0.6s ease-out;
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.notice-title {
  font-size: 32px;
  font-weight: bold;
  color: #2c3e50;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.notice-icon {
  font-size: 28px;
  color: #409EFF;
  animation: bellRing 2s infinite;
}

@keyframes bellRing {
  0%, 100% { transform: rotate(0); }
  5%, 15% { transform: rotate(15deg); }
  10%, 20% { transform: rotate(-15deg); }
}

.notice-subtitle {
  color: #909399;
  font-size: 16px;
  letter-spacing: 1px;
}

.notice-content {
  background: #fff;
  border-radius: 12px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.notice-item {
  margin-bottom: 25px;
  transition: all 0.3s ease;
}

.notice-card {
  margin-left: 10px;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  cursor: pointer;
  border-radius: 8px;
}

.notice-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.notice-card-important {
  border-left: 4px solid #F56C6C;
}

.notice-card-new {
  border-left: 4px solid #67C23A;
}

.notice-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
}

.notice-card-title {
  display: flex;
  align-items: center;
  gap: 10px;
}

.notice-tag {
  margin-right: 8px;
}

.title-text {
  font-weight: 600;
  font-size: 16px;
  color: #2c3e50;
}

.notice-card-meta {
  display: flex;
  align-items: center;
  gap: 8px;
}

.notice-card-time {
  color: #909399;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.notice-card-content {
  padding: 20px;
  color: #606266;
  line-height: 1.6;
}

.content-text {
  margin-bottom: 15px;
  white-space: pre-wrap;
}

.notice-card-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.notice-empty {
  padding: 60px 0;
  text-align: center;
}

/* 详情弹窗样式 */
.notice-detail {
  padding: 20px;
}

.detail-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.detail-meta {
  display: flex;
  align-items: center;
  gap: 15px;
}

.detail-time {
  color: #909399;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.detail-content {
  color: #606266;
  line-height: 1.8;
  font-size: 15px;
  white-space: pre-wrap;
}

/* 时间线样式 */
:deep(.el-timeline-item__node) {
  transition: all 0.3s ease;
  width: 16px;
  height: 16px;
}

:deep(.el-timeline-item:hover .el-timeline-item__node) {
  transform: scale(1.2);
}

:deep(.el-timeline-item__content) {
  width: calc(100% - 100px);
}

/* 响应式设计 */
@media screen and (max-width: 768px) {
  .notice-container {
    padding: 15px;
    margin: 15px auto;
  }
  
  .notice-title {
    font-size: 24px;
  }
  
  .notice-content {
    padding: 15px;
  }
  
  .notice-card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  :deep(.el-timeline-item__content) {
    width: calc(100% - 80px);
  }
  
  :deep(.el-dialog) {
    width: 90% !important;
  }
}
</style>