<template>
  <div class="home-wrapper">
    <div class="snow-container">
      <SnowEffect />
    </div>
    
    <!-- 系统公告弹窗 -->
    <el-dialog
      v-model="data.noticeVisible"
      destroy-on-close
      :show-close="true"
      center
      width="750px"
      custom-class="notice-popup-dialog"
      append-to-body
    >
      <template #header>
        <div class="notice-popup-header">
          <div class="notice-popup-title">
            <el-icon class="notice-icon"><Bell /></el-icon>
            <span>系统公告</span>
          </div>
        </div>
      </template>
      
      <div class="notice-popup-content" v-if="data.currentNotice">
        <el-card 
          class="notice-popup-card" 
          :class="{'notice-card-important': data.currentNotice.important}"
        >
          <template #header>
            <div class="notice-card-header">
              <div class="notice-card-title">
                <el-tag 
                  v-if="data.currentNotice.important" 
                  type="danger" 
                  size="default" 
                  effect="dark"
                  class="notice-tag"
                >
                  重要
                </el-tag>
                <el-tag 
                  type="success" 
                  size="default" 
                  effect="light"
                  class="notice-tag"
                >
                  新
                </el-tag>
                <span class="title-text">{{ data.currentNotice.title || '系统公告' }}</span>
              </div>
              <div class="notice-card-meta">
                <span class="notice-card-time">
                  <el-icon><Clock /></el-icon>
                  {{ formatTime(data.currentNotice.time) }}
                </span>
              </div>
            </div>
          </template>
          <div class="notice-card-content">
            <pre class="content-text">{{ data.currentNotice.content }}</pre>
          </div>
        </el-card>
        
        <!-- 动画装饰元素 -->
        <div class="notice-decoration">
          <div class="notice-decoration-circle circle-1"></div>
          <div class="notice-decoration-circle circle-2"></div>
          <div class="notice-decoration-circle circle-3"></div>
        </div>
      </div>
      
      <div class="notice-empty" v-else>
        <el-empty description="暂无公告" />
      </div>
      
      <template #footer>
        <div class="notice-popup-footer">
          <el-checkbox v-model="data.noticeNoShow" label="不再提示" @change="watchNoticeNoShow" />
          <el-button type="primary" @click="closeNotice" class="close-btn">
            关闭
            <el-icon class="el-icon--right"><Close /></el-icon>
          </el-button>
        </div>
      </template>
    </el-dialog>
    
    <div class="home-container">
      <div class="main-content">
        <!-- 为你推荐 -->
        <div class="section-header">
          <div class="section-title">
            <span class="title-text">为你推荐</span>
            <span class="title-count">({{ data.recommended?.length }}部)</span>
          </div>
          <div class="view-all" @click="$router.push('/front/film?category=recommended')">全部 ></div>
        </div>
        <div class="movie-grid">
          <el-row :gutter="20">
            <el-col :span="6" v-for="item in data.recommended" :key="item.id" class="movie-card">
              <div class="movie-poster" @click="navTo('/front/filmDetail?id=' + item.id)">
                <div class="poster-container">
                <img :src="item.img" :alt="item.title" class="poster-image">
                <div class="movie-overlay">
                  <el-button type="primary" class="buy-ticket-btn">购票</el-button>
                  </div>
                </div>
                <div class="movie-info">
                  <div class="movie-title">{{ item.title }}</div>
                  <div class="movie-score" v-if="item.score && parseFloat(item.score) > 0">评分: {{ (item.score * 1).toFixed(1) }}分</div>
                  <div class="movie-score movie-no-score" v-else>暂无评分</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 正在热播 -->
        <div class="section-header">
          <div class="section-title">
            <span class="title-text">正在热播</span>
            <span class="title-count">({{ data.data1?.length }}部)</span>
          </div>
          <div class="view-all" @click="$router.push('/front/film?category=playing')">全部 ></div>
        </div>
        <div class="movie-grid">
          <el-row :gutter="20">
            <el-col :span="6" v-for="item in data.playingData" :key="item.id" class="movie-card">
              <div class="movie-poster" @click="navTo('/front/filmDetail?id=' + item.id)">
                <div class="poster-container">
                <img :src="item.img" :alt="item.title" class="poster-image">
                <div class="movie-overlay">
                  <el-button type="primary" class="buy-ticket-btn">购票</el-button>
                  </div>
                </div>
                <div class="movie-info">
                  <div class="movie-title">{{ item.title }}</div>
                  <div class="movie-score" v-if="item.score && parseFloat(item.score) > 0">评分: {{ (item.score * 1).toFixed(1) }}分</div>
                  <div class="movie-score movie-no-score" v-else>暂无评分</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 即将上映 -->
        <div class="section-header">
          <div class="section-title">
            <span class="title-text">即将上映</span>
            <span class="title-count">({{ data.data2?.length }}部)</span>
          </div>
          <div class="view-all" @click="$router.push('/front/film?category=upcoming')">全部 ></div>
        </div>
        <div class="movie-grid">
          <el-row :gutter="20">
            <el-col :span="6" v-for="item in data.noPlayData" :key="item.id" class="movie-card">
              <div class="movie-poster" @click="navTo('/front/filmDetail?id=' + item.id)">
                <div class="poster-container">
                <img :src="item.img" :alt="item.title" class="poster-image">
                </div>
                <div class="movie-info">
                  <div class="movie-title">{{ item.title }}</div>
                  <div class="release-date">{{ item.start }} 上映</div>
                  <div class="movie-score" v-if="item.score && parseFloat(item.score) > 0">评分: {{ (item.score * 1).toFixed(1) }}分</div>
                  <div class="movie-score movie-no-score" v-else>暂无评分</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>

      <div class="sidebar">
        <!-- 总票房Top 10 -->
        <div class="section-header">
          <div class="section-title">
            <span class="title-text">总票房Top 10</span>
          </div>
          <div class="view-all" @click="$router.push('/front/charts')">查看完整榜单 ></div>
        </div>
        <div class="rankings-box">
          <div class="top-movie">
            <img :src="data.firstTotal.img" :alt="data.firstTotal.title" class="top-movie-poster">
            <div class="top-movie-info">
              <div class="top-movie-title">{{ data.firstTotal.title }}</div>
              <div class="top-movie-total">{{ (data.firstTotal.total * 1).toFixed(2) }}元</div>
            </div>
          </div>
          <div class="rankings-list">
            <div v-for="(item, index) in data.topTotal" :key="item.id" class="ranking-item" @click="navTo('/front/filmDetail?id=' + item.id)">
              <div class="rank-number">{{ index + 2 }}</div>
              <div class="rank-title">{{ item.title }}</div>
              <div class="rank-total">{{ (item.total * 1).toFixed(2) }}元</div>
            </div>
          </div>
        </div>

        <!-- 今日票房 -->
        <div class="today-box">
          <div class="today-header">
            <div class="today-label">今日票房</div>
            <div class="today-content">
              <div class="today-total">{{ data.todayTotal.toFixed(2) }}元</div>
              <div class="today-time">北京时间：{{ data.time }}</div>
            </div>
          </div>
          <div class="update-btn" @click="loadTodayTotal">点击更新</div>
        </div>

        <!-- 总评分Top 10 -->
        <div class="section-header">
          <div class="section-title">
            <span class="title-text">总评分Top 10</span>
          </div>
          <div class="view-all" @click="$router.push('/front/charts')">查看完整榜单 ></div>
        </div>
        <div class="rankings-box">
          <div class="top-movie">
            <img :src="data.firstScore.img" :alt="data.firstScore.title" class="top-movie-poster">
            <div class="top-movie-info">
              <div class="top-movie-title">{{ data.firstScore.title }}</div>
              <div class="top-movie-score">{{ (data.firstScore.score * 1).toFixed(1) }}分</div>
            </div>
          </div>
          <div class="rankings-list">
            <div v-for="(item, index) in data.topScore" :key="item.id" class="ranking-item" @click="navTo('/front/filmDetail?id=' + item.id)">
              <div class="rank-number">{{ index + 2 }}</div>
              <div class="rank-title">{{ item.title }}</div>
              <div class="rank-score">{{ (item.score * 1).toFixed(1) }}分</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import router from "@/router/index.js";
import { reactive, onMounted, onBeforeMount } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import { Calendar, Bell, Clock, Close } from '@element-plus/icons-vue'
import SnowEffect from '@/components/SnowEffect.vue';
import dayjs from 'dayjs'

const data = reactive({
  data1: [], // 所有已上映的电影
  data2: [], // 所有待上映的电影
  playingData: [], // 首页热映推荐
  noPlayData: [], // 首页即将上映
  firstTotal: {}, // 排行榜第一的总票房电影
  topTotal: [], // 总票房排行榜
  firstScore: {}, // 排行榜第一的高评分电影
  topScore: [], // 高评分排行榜
  todayTotal: 0, // 今日票房总计
  time: null, // 时间信息
  recommended: [],  // 用于存储推荐的电影
  noticeVisible: false, // 公告弹窗显示状态
  currentNotice: null, // 当前显示的公告
  noticeNoShow: false // 是否不再显示公告
});

// 格式化时间的函数
const formatTime = (time) => {
  if (!time) return '';
  return dayjs(time).format('YYYY-MM-DD HH:mm');
}

// 修复loadNotice函数，确保弹窗正确显示
const loadNotice = () => {
  // 1. 检查用户是否登录
  const user = JSON.parse(localStorage.getItem('xm-user') || '{}');
  const isLoggedIn = !!user.id;

  if (!isLoggedIn) {
    // 如果用户未登录，不显示公告
    return;
  }
  
  // 2. 检查用户是否设置了不再提示
  const noShowSetting = localStorage.getItem('xm-notice-noshow');
  if (noShowSetting === 'true') {
    data.noticeNoShow = true;
    // 用户设置了不再提示，不显示公告
    return;
  }
  
  // 3. 获取已读公告列表
  const readNotices = JSON.parse(localStorage.getItem('xm-read-notices') || '[]');

  // 4. 获取公告数据
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200' && res.data && res.data.length > 0) {
      // 处理数据
      const notices = res.data.map(notice => ({
        ...notice,
        important: notice.important || false // 保持原始重要性标志
      }));
      
      // 按时间排序，显示最新的
      const sortedNotices = [...notices].sort((a, b) => 
        new Date(b.time) - new Date(a.time)
      );
      
      // 过滤出未读的公告
      const unreadNotices = sortedNotices.filter(notice => !readNotices.includes(notice.id));
      
      if (unreadNotices.length > 0) {
        // 设置当前公告为最新的未读公告
        data.currentNotice = unreadNotices[0];
      
        // 显示公告弹窗
      setTimeout(() => {
        data.noticeVisible = true;
      }, 300);
      }
    }
  }).catch(err => {
    console.error("加载公告失败", err);
  });
}

// 修复watchNoticeNoShow函数，防止阻止公告显示
const watchNoticeNoShow = () => {
  // 将选择保存到localStorage，确保持久化存储用户选择
  if (data.noticeNoShow) {
    // 用户选择不再提示
    localStorage.setItem('xm-notice-noshow', 'true');
    
    // 如果有当前公告，将其ID添加到已读公告列表中
    if (data.currentNotice) {
      const readNotices = JSON.parse(localStorage.getItem('xm-read-notices') || '[]');
      if (!readNotices.includes(data.currentNotice.id)) {
        readNotices.push(data.currentNotice.id);
        localStorage.setItem('xm-read-notices', JSON.stringify(readNotices));
      }
    }
  } else {
    // 用户取消了不再提示，移除该标记
    localStorage.removeItem('xm-notice-noshow');
  }
}

// 加载今日票房总计
const loadTodayTotal = () => {
  request.get('/orders/todayTotal').then(res => {
    if (res.code === '200') {
      data.todayTotal = res.data.total
      data.time = res.data.time
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 加载总票房排行榜
const loadTotal = () => {
  request.get('/film/selectTotalTop').then(res => {
    if (res.code === '200') {
      data.firstTotal = res.data[0]
      data.topTotal = res.data.slice(1, res.data.length)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 加载评分排行榜
const loadScore = () => {
  request.get('/film/selectScoreTop').then(res => {
    if (res.code === '200') {
      data.firstScore = res.data[0]
      data.topScore = res.data.slice(1, res.data.length)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 加载推荐电影
const loadRecommendations = () => {
  console.log("加载推荐电影...");
  const user = JSON.parse(localStorage.getItem('xm-user'));  // 获取并解析存储的用户数据
  const userId = user ? user.id : null;  // 提取 id 或返回 null 如果没有数据

  console.log(userId);  // 输出用户的 id

  if (!userId) {
    ElMessage.error("用户未登录");
    return;
  }

  request.get(`/api/recommendations/${userId}`).then(res => {
    console.log("API 请求结果:", res);  // 输出请求返回的结果
    if (res.code === '200') {
      console.log(data.recommended);  // 输出推荐电影的数据，检查每个 item 是否有 img 和 title 字段
      data.recommended = res.data;  // 存储推荐数据
    } else {
      ElMessage.error(res.msg);
    }
  }).catch(error => {
    ElMessage.error('加载推荐电影失败');
    console.error(error);
  });
}

// 修改初始化逻辑
const navTo = (url) => {
  location.href = url
}

// 加载所有电影
const load = () => {
  request.get('/film/selectAll').then(res => {
    if (res.code === '200') {
      data.data1 = res.data.filter(v => v.status === '已上映')
      data.data2 = res.data.filter(v => v.status === '待上映')
      if (data.data1 && data.data1.length > 8) {
        data.playingData = data.data1.slice(0, 8)
      } else {
        data.playingData = data.data1
      }
      if (data.data2 && data.data2.length > 8) {
        data.noPlayData = data.data2.slice(0, 8)
      } else {
        data.noPlayData = data.data2
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 修复closeNotice方法，关闭弹窗后记录已读标记
const closeNotice = () => {
  data.noticeVisible = false;
  
  // 如果用户选择了不再提示，则不需要再单独记录已读
  if (data.noticeNoShow) {
    return;
  }
  
  // 将当前公告添加到已读列表
  if (data.currentNotice) {
    const readNotices = JSON.parse(localStorage.getItem('xm-read-notices') || '[]');
    if (!readNotices.includes(data.currentNotice.id)) {
      readNotices.push(data.currentNotice.id);
      localStorage.setItem('xm-read-notices', JSON.stringify(readNotices));
    }
  }
}

// 修改onMounted钩子，确保在组件挂载后初始化公告弹窗
onMounted(() => {
  // 加载其他数据
  load();
  loadTodayTotal();
  loadTotal();
  loadScore();
  
  // 检查用户是否登录
  const user = JSON.parse(localStorage.getItem('xm-user') || '{}');
  const isLoggedIn = !!user.id;
  
  if (isLoggedIn) {
    // 加载推荐信息
    loadRecommendations();
    
    // 加载公告
    setTimeout(() => {
  loadNotice();
    }, 1000); // 延迟1秒加载公告，确保其他数据已加载完成
  }
});
</script>

<style scoped>
.home-wrapper {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0f7ff 0%, #e6f3ff 100%);
  position: relative;
  overflow: hidden;
  z-index: 0;
}

.home-wrapper::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: 
    radial-gradient(circle at 20% 20%, rgba(45, 152, 243, 0.15) 0%, transparent 50%),
    radial-gradient(circle at 80% 80%, rgba(255, 107, 107, 0.15) 0%, transparent 50%);
  pointer-events: none;
  z-index: 1;
}

.snow-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 2;
  overflow: hidden;
}

.home-container {
  width: 85%;
  margin: 30px auto;
  display: flex;
  gap: 40px;
  position: relative;
  background: rgba(255, 255, 255, 0.95);
  padding: 40px;
  border-radius: 24px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.9);
  z-index: 3;
}

.main-content {
  flex: 1;
  position: relative;
  z-index: 1;
}

.sidebar {
  width: 320px;
  position: relative;
  z-index: 1;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 25px;
  position: relative;
}

.section-header::after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 0;
  width: 60px;
  height: 4px;
  background: linear-gradient(90deg, #2d98f3, transparent);
  border-radius: 2px;
}

.section-title {
  flex: 1;
  font-size: 26px;
  font-weight: 600;
}

.title-text {
  color: #2d98f3;
  text-shadow: 0 2px 4px rgba(45, 152, 243, 0.15);
  position: relative;
  display: inline-block;
}

.title-text::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 100%;
  height: 2px;
  background: linear-gradient(90deg, #2d98f3, transparent);
  border-radius: 1px;
}

.title-count {
  color: #666;
  font-size: 16px;
  margin-left: 10px;
  background: rgba(45, 152, 243, 0.15);
  padding: 4px 12px;
  border-radius: 16px;
}

.view-all {
  color: #2d98f3;
  cursor: pointer;
  font-size: 14px;
}

.movie-grid {
  margin-bottom: 40px;
  position: relative;
}

.movie-card {
  margin-bottom: 30px;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  height: 380px; /* 固定卡片高度 */
}

.movie-poster {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  background: #fff;
  border: 1px solid rgba(0, 0, 0, 0.08);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.movie-poster:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.2);
}

.poster-container {
  position: relative;
  width: 100%;
  height: 280px;
  overflow: hidden;
  flex: 1;
}

.poster-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
  transition: transform 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.movie-poster:hover .poster-image {
  transform: scale(1.05);
}

.movie-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0,0,0,0.7), transparent);
  padding: 25px;
  opacity: 0;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  z-index: 2;
}

.movie-poster:hover .movie-overlay {
  opacity: 1;
}

.buy-ticket-btn {
  width: 100%;
  height: 44px;
  border-radius: 22px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(45deg, #2d98f3, #1a73e8);
  border: none;
  box-shadow: 0 6px 16px rgba(45, 152, 243, 0.4);
  transition: all 0.3s ease;
}

.buy-ticket-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(45, 152, 243, 0.5);
}

.movie-info {
  padding: 12px 15px;
  background: #fff;
  text-align: center;
  height: auto;
  min-height: 70px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  border-top: 1px solid #f0f0f0;
}

.movie-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  line-height: 1.4;
}

.release-date {
  color: #ff6b00;
  font-size: 14px;
  line-height: 1.4;
}

.movie-score {
  color: #ffa940;
  font-size: 14px;
  font-weight: 500;
  line-height: 1.4;
}

.movie-no-score {
  color: #909399;
  font-style: italic;
}

.rankings-box {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
  padding: 25px;
  margin-bottom: 35px;
  border: 1px solid rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.rankings-box:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.12);
}

.top-movie {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.top-movie-poster {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  object-fit: cover;
}

.top-movie-info {
  flex: 1;
}

.top-movie-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 10px;
}

.top-movie-total {
  color: #ff4d4f;
  font-size: 20px;
  font-weight: 600;
}

.top-movie-score {
  color: #ffa940;
  font-size: 20px;
  font-weight: 600;
}

.rankings-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.ranking-item {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 15px;
  border-radius: 12px;
  transition: all 0.3s ease;
  background: rgba(45, 152, 243, 0.08);
  border: 1px solid rgba(45, 152, 243, 0.15);
}

.ranking-item:hover {
  background: rgba(45, 152, 243, 0.15);
  transform: translateX(8px);
}

.rank-number {
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(45, 152, 243, 0.1);
  border-radius: 50%;
  font-size: 14px;
  font-weight: 600;
  color: #2d98f3;
}

.rank-title {
  flex: 1;
  font-size: 14px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.rank-total {
  color: #ff6b6b;
  font-weight: 600;
}

.rank-score {
  color: #ffa940;
  font-weight: 600;
}

.today-box {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
  padding: 25px;
  margin-bottom: 35px;
  border: 1px solid rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.today-box:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.12);
}

.today-header {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
}

.today-label {
  background: linear-gradient(45deg, #ff6b6b, #ff9a8b);
  color: #fff;
  padding: 10px 20px;
  border-radius: 12px;
  font-weight: 600;
  box-shadow: 0 6px 16px rgba(255, 107, 107, 0.4);
}

.today-content {
  flex: 1;
}

.today-total {
  color: #ff6b6b;
  font-size: 28px;
  font-weight: 600;
  text-shadow: 0 2px 4px rgba(255, 107, 107, 0.15);
}

.today-time {
  color: #999;
  font-size: 12px;
  margin-top: 5px;
}

.update-btn {
  text-align: right;
  color: #2d98f3;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
  padding: 8px 0;
}

.update-btn:hover {
  text-decoration: underline;
  transform: translateX(8px);
  color: #1a73e8;
}

/* 公告弹窗样式 */
.notice-popup-dialog {
  background: #fff;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15) !important;
  border-radius: 16px !important;
  overflow: hidden;
}

.notice-popup-header {
  text-align: center;
  margin-bottom: 10px;
  padding: 20px 0 0;
  position: relative;
}

.notice-popup-title {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

.notice-icon {
  font-size: 24px;
  color: #409EFF;
  animation: bellRing 2s infinite;
}

@keyframes bellRing {
  0%, 100% { transform: rotate(0); }
  5%, 15% { transform: rotate(15deg); }
  10%, 20% { transform: rotate(-15deg); }
}

.notice-popup-content {
  position: relative;
  z-index: 1;
  padding: 10px 15px;
}

.notice-popup-card {
  margin-bottom: 15px;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  overflow: visible;
}

.notice-card-important {
  border-left: 4px solid #F56C6C;
}

.notice-card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: #f7f8fa;
  border-bottom: 1px solid #ebeef5;
}

.notice-card-title {
  display: flex;
  align-items: center;
  gap: 10px;
  flex: 1;
}

.notice-tag {
  margin-right: 5px;
}

.title-text {
  font-weight: 600;
  font-size: 16px;
  color: #2c3e50;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.notice-card-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 120px;
  justify-content: flex-end;
}

.notice-card-time {
  color: #909399;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.notice-card-content {
  padding: 25px;
  color: #606266;
  line-height: 1.8;
  font-size: 15px;
  max-height: 400px;
  min-height: 200px;
  overflow-y: auto;
}

.content-text {
  white-space: pre-wrap;
  word-break: break-all;
  font-family: inherit;
  margin: 0;
}

.notice-decoration {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: -1;
}

.notice-decoration-circle {
  position: absolute;
  border-radius: 50%;
  filter: blur(50px);
  opacity: 0.1;
}

.circle-1 {
  width: 300px;
  height: 300px;
  background: #409EFF;
  top: 10%;
  right: 10%;
  animation: float 10s ease-in-out infinite;
}

.circle-2 {
  width: 200px;
  height: 200px;
  background: #F56C6C;
  bottom: 20%;
  left: 15%;
  animation: float 12s ease-in-out infinite reverse;
}

.circle-3 {
  width: 150px;
  height: 150px;
  background: #67C23A;
  bottom: 10%;
  right: 20%;
  animation: float 8s ease-in-out infinite 2s;
}

@keyframes float {
  0%, 100% { transform: translateY(0) scale(1); }
  50% { transform: translateY(-20px) scale(1.05); }
}

.notice-empty {
  padding: 30px 0;
  text-align: center;
}

.notice-popup-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0 0;
}

.close-btn {
  padding: 9px 20px;
  border-radius: 4px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.close-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

:deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
  margin-top: 8vh !important;
}

:deep(.el-dialog__header) {
  padding: 0;
  margin: 0;
}

:deep(.el-dialog__headerbtn) {
  top: 20px;
  right: 20px;
  font-size: 20px;
  z-index: 10;
}

:deep(.el-dialog__headerbtn .el-dialog__close) {
  color: #909399;
}

:deep(.el-dialog__body) {
  padding: 10px 20px;
}

:deep(.el-dialog__footer) {
  padding: 10px 20px 20px;
}

/* 自定义滚动条 */
.notice-card-content::-webkit-scrollbar {
  width: 6px;
}

.notice-card-content::-webkit-scrollbar-thumb {
  background-color: #dcdfe6;
  border-radius: 3px;
}

.notice-card-content::-webkit-scrollbar-track {
  background-color: #f5f7fa;
}

.debug-panel {
  position: fixed;
  top: 10px;
  right: 10px;
  z-index: 9999;
  background: rgba(0, 0, 0, 0.5);
  padding: 5px;
  border-radius: 4px;
}
</style>
