<template>
  <div class="charts-wrapper">
    <div class="charts-container">
      <el-row :gutter="30">
        <el-col :span="12">
          <!-- 总票房Top榜 -->
          <div class="chart-card">
            <div class="chart-header">
              <div class="chart-title">
                <i class="el-icon-trophy"></i>
                <span>总票房 Top榜</span>
              </div>
              <div class="view-all" @click="$router.push('/front/film')">查看全部 ></div>
            </div>
            <div class="chart-content">
              <div class="top-movie">
                <div class="top-movie-poster">
                  <img :src="data.topTotal[0]?.img" :alt="data.topTotal[0]?.title">
                  <div class="top-movie-rank">1</div>
                </div>
                <div class="top-movie-info">
                  <div class="top-movie-title">{{ data.topTotal[0]?.title }}</div>
                  <div class="top-movie-actors">
                    <span v-if="data.topTotal[0]?.actors && data.topTotal[0]?.actors.length" v-for="it in data.topTotal[0]?.actors" :key="it" class="actor-item">{{ it }}</span>
                    <span v-else>管理员暂未输入演职人员信息</span>
                  </div>
                  <div class="top-movie-time">{{ data.topTotal[0]?.start }}</div>
                  <div class="top-movie-total">{{ (data.topTotal[0]?.total * 1).toFixed(2) }}元</div>
                </div>
              </div>
              <div class="rankings-list">
                <div v-for="(item, index) in data.topTotal.slice(1)" :key="item.id" class="ranking-item" @click="goToMovieDetail(item.id)">
                  <div class="rank-number">{{ index + 2 }}</div>
                  <div class="rank-poster">
                    <img :src="item.img" :alt="item.title">
                  </div>
                  <div class="rank-info">
                    <div class="rank-title">{{ item.title }}</div>
                    <div class="rank-actors">
                      <span v-if="item.actors && item.actors.length" v-for="it in item.actors" :key="it" class="actor-item">{{ it }}</span>
                      <span v-else>管理员暂未输入演职人员信息</span>
                    </div>
                    <div class="rank-time">{{ item.start }}</div>
                  </div>
                  <div class="rank-total">{{ (item.total * 1).toFixed(2) }}元</div>
                </div>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :span="12">
          <!-- 评分Top榜 -->
          <div class="chart-card">
            <div class="chart-header">
              <div class="chart-title">
                <i class="el-icon-star-on"></i>
                <span>评分 Top榜</span>
              </div>
              <div class="view-all" @click="$router.push('/front/film')">查看全部 ></div>
            </div>
            <div class="chart-content">
              <div class="top-movie">
                <div class="top-movie-poster">
                  <img :src="data.topScore[0]?.img" :alt="data.topScore[0]?.title">
                  <div class="top-movie-rank">1</div>
                </div>
                <div class="top-movie-info">
                  <div class="top-movie-title">{{ data.topScore[0]?.title }}</div>
                  <div class="top-movie-actors">
                    <span v-if="data.topScore[0]?.actors && data.topScore[0]?.actors.length" v-for="it in data.topScore[0]?.actors" :key="it" class="actor-item">{{ it }}</span>
                    <span v-else>管理员暂未输入演职人员信息</span>
                  </div>
                  <div class="top-movie-time">{{ data.topScore[0]?.start }}</div>
                  <div class="top-movie-score">{{ (data.topScore[0]?.score * 1).toFixed(1) }}分</div>
                </div>
              </div>
              <div class="rankings-list">
                <div v-for="(item, index) in data.topScore.slice(1)" :key="item.id" class="ranking-item" @click="goToMovieDetail(item.id)">
                  <div class="rank-number">{{ index + 2 }}</div>
                  <div class="rank-poster">
                    <img :src="item.img" :alt="item.title">
                  </div>
                  <div class="rank-info">
                    <div class="rank-title">{{ item.title }}</div>
                    <div class="rank-actors">
                      <span v-if="item.actors && item.actors.length" v-for="it in item.actors" :key="it" class="actor-item">{{ it }}</span>
                      <span v-else>管理员暂未输入演职人员信息</span>
                    </div>
                    <div class="rank-time">{{ item.start }}</div>
                  </div>
                  <div class="rank-score">{{ (item.score * 1).toFixed(1) }}分</div>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";  // 引入路由

const data = reactive({
  topTotal: [],
  topScore: [],
})

const loadTotal = () => {
  request.get('/film/selectTotalTop').then(res => {
    if (res.code === '200') {
      data.topTotal = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadScore = () => {
  request.get('/film/selectScoreTop').then(res => {
    if (res.code === '200') {
      data.topScore = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}


// 在你的电影列表部分，点击电影跳转到详情页
const goToMovieDetail = (id) => {
  // 使用路由跳转到电影详情页，传递电影的 id
  router.push({ path: '/front/filmDetail', query: { id } });
}



loadTotal()
loadScore()
</script>

<style scoped>
.charts-wrapper {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0f7ff 0%, #e6f3ff 100%);
  padding: 40px 0;
}

.charts-container {
  width: 85%;
  margin: 0 auto;
}

.chart-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 24px;
  padding: 30px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.9);
  margin-bottom: 30px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.chart-title {
  display: flex;
  align-items: center;
  font-size: 24px;
  font-weight: 600;
  color: #333;
}

.chart-title i {
  margin-right: 10px;
  color: #2d98f3;
  font-size: 28px;
}

.view-all {
  color: #2d98f3;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.view-all:hover {
  transform: translateX(5px);
}

.top-movie {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  padding-bottom: 30px;
  border-bottom: 1px solid #eee;
}

.top-movie-poster {
  position: relative;
  width: 180px;
  height: 240px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
}

.top-movie-poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.top-movie-rank {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 30px;
  height: 30px;
  background: #ff4d4f;
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 16px;
}

.top-movie-info {
  flex: 1;
}

.top-movie-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 15px;
}

.top-movie-actors {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 10px;
  color: #666;
}

.actor-item {
  background: rgba(0, 0, 0, 0.05);
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 14px;
}

.top-movie-time {
  color: #999;
  font-size: 14px;
  margin-bottom: 15px;
}

.top-movie-total {
  color: #ff4d4f;
  font-size: 28px;
  font-weight: 600;
}

.top-movie-score {
  color: #ffa940;
  font-size: 28px;
  font-weight: 600;
}

.rankings-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.ranking-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  border-radius: 12px;
  background: rgba(0, 0, 0, 0.02);
  cursor: pointer;
  transition: all 0.3s ease;
}

.ranking-item:hover {
  background: rgba(0, 0, 0, 0.05);
  transform: translateX(10px);
}

.rank-number {
  width: 30px;
  height: 30px;
  background: rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  color: #666;
}

.rank-poster {
  width: 60px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
}

.rank-poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.rank-info {
  flex: 1;
}

.rank-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 5px;
}

.rank-actors {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 5px;
  color: #666;
  font-size: 12px;
}

.rank-time {
  color: #999;
  font-size: 12px;
}

.rank-total {
  color: #ff4d4f;
  font-weight: 600;
  font-size: 18px;
}

.rank-score {
  color: #ffa940;
  font-weight: 600;
  font-size: 18px;
}
</style>
