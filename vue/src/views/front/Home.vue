<template>
  <div class="home-wrapper">
    <SnowEffect />
    <div class="home-container">
      <div class="main-content">
        <!-- 为你推荐 -->
        <div class="section-header">
          <div class="section-title">
            <span class="title-text">为你推荐</span>
            <span class="title-count">({{ data.recommended?.length }}部)</span>
          </div>
          <div class="view-all" @click="$router.push('/front/film')">全部 ></div>
        </div>
        <div class="movie-grid">
          <el-row :gutter="20">
            <el-col :span="6" v-for="item in data.recommended" :key="item.id" class="movie-card">
              <div class="movie-poster" @click="navTo('/front/filmDetail?id=' + item.id)">
                <img :src="item.img" :alt="item.title" class="poster-image">
                <div class="movie-overlay">
                  <el-button type="primary" class="buy-ticket-btn">购票</el-button>
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
          <div class="view-all" @click="$router.push('/front/film')">全部 ></div>
        </div>
        <div class="movie-grid">
          <el-row :gutter="20">
            <el-col :span="6" v-for="item in data.playingData" :key="item.id" class="movie-card">
              <div class="movie-poster" @click="navTo('/front/filmDetail?id=' + item.id)">
                <img :src="item.img" :alt="item.title" class="poster-image">
                <div class="movie-overlay">
                  <el-button type="primary" class="buy-ticket-btn">购票</el-button>
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
          <div class="view-all" @click="$router.push('/front/film')">全部 ></div>
        </div>
        <div class="movie-grid">
          <el-row :gutter="20">
            <el-col :span="6" v-for="item in data.noPlayData" :key="item.id" class="movie-card">
              <div class="movie-poster" @click="navTo('/front/filmDetail?id=' + item.id)">
                <img :src="item.img" :alt="item.title" class="poster-image">
                <div class="movie-info">
                  <div class="movie-title">{{ item.title }}</div>
                  <div class="release-date">{{ item.start }} 上映</div>
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
import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import SnowEffect from '@/components/SnowEffect.vue';

const data = reactive({
  data1: [], // 已上映的电影
  data2: [], // 待上映的电影
  playingData: [], // 播放中的电影（最多显示8部）
  noPlayData: [], // 待上映电影（最多显示8部）
  topTotal: [], // 总票房排行榜
  firstTotal: {}, // 排行榜第一的电影
  topScore: [], // 高评分排行榜
  firstScore: {}, // 排行榜第一的高评分电影
  todayTotal: 0, // 今日票房总计
  time: null, // 时间信息
  recommended: []  // 用于存储推荐的电影
});

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


// 初始加载数据
loadTodayTotal();
loadTotal();
loadScore();
loadRecommendations();  // 加载推荐电影数据


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
load()
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
}

.movie-poster {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  background: #fff;
  border: 1px solid rgba(0, 0, 0, 0.08);
}

.movie-poster:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.2);
}

.poster-image {
  width: 100%;
  height: 280px;
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
  background: linear-gradient(to top, rgba(0,0,0,0.9), transparent);
  padding: 25px;
  opacity: 0;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
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
  padding: 15px;
  background: #fff;
}

.movie-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 5px;
}

.release-date {
  color: #ff6b00;
  font-size: 14px;
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
</style>
