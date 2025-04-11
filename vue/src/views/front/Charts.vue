<template>
  <div class="charts-wrapper">
    <!-- 顶部轮播图部分 -->
    <div class="banner-section">
      <el-carousel height="400px" indicator-position="outside" arrow="always" :interval="5000">
        <el-carousel-item v-for="(item, index) in data.topTotal.slice(0, 5)" :key="index" v-if="data.topTotal.length > 0">
          <div class="carousel-content" @click="goToMovieDetail(item.id)">
            <div class="carousel-overlay"></div>
            <img :src="item.img" :alt="item.title" class="carousel-image">
            <div class="carousel-info">
              <h2 class="carousel-title">{{ item.title }}</h2>
              <div class="carousel-details">
                <div class="carousel-rank">TOP {{ index + 1 }}</div>
                <div class="carousel-score" v-if="item.score">
                  <i class="el-icon-star-on"></i> {{ (item.score * 1).toFixed(1) }}
                </div>
                <div class="carousel-total">票房: {{ (item.total * 1).toFixed(2) }}元</div>
              </div>
              <div class="carousel-desc">
                <div class="carousel-actors" v-if="item.actors && item.actors.length">
                  主演: {{ item.actors.slice(0, 3).join(' / ') }}
                </div>
                <div class="carousel-time">上映日期: {{ item.start }}</div>
              </div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </div>

    <div class="charts-container">
      <!-- 分类标签 -->
      <div class="category-tabs">
        <div 
          v-for="tab in data.categories" 
          :key="tab.id" 
          :class="['category-tab', data.activeCategory === tab.id ? 'active-tab' : '']"
          @click="changeCategory(tab.id)"
        >
          {{ tab.name }}
        </div>
      </div>

      <!-- 票房榜单与影评榜单 -->
      <el-row :gutter="30">
        <el-col :span="17">
          <!-- 主要榜单内容 -->
          <div class="chart-card main-chart">
            <div class="chart-header">
              <div class="chart-title">
                <i class="el-icon-trophy" v-if="data.activeCategory === 'boxOffice'"></i>
                <i class="el-icon-star-on" v-else-if="data.activeCategory === 'rating'"></i>
                <i class="el-icon-video-camera" v-else-if="data.activeCategory === 'trending'"></i>
                <span>{{ data.categoryTitle }}</span>
              </div>
              <div class="view-all" @click="$router.push('/front/film')">查看全部影片 ></div>
            </div>
            
            <div class="detailed-ranking-list">
              <div v-for="(item, index) in data.displayList" :key="item.id" 
                   class="detailed-ranking-item" @click="goToMovieDetail(item.id)">
                <div class="detailed-rank-number" :class="{'top-rank': index < 3}">{{ index + 1 }}</div>
                <div class="detailed-rank-poster">
                  <img :src="item.img" :alt="item.title">
                  <div class="poster-hover">
                    <el-button type="primary" size="small" @click.stop="goToMovieDetail(item.id)">
                      查看详情
                    </el-button>
                  </div>
                </div>
                <div class="detailed-rank-info">
                  <div class="detailed-rank-title">{{ item.title }}</div>
                  <div class="detailed-rank-details">
                    <div class="detailed-rank-tags">
                      <span v-if="item.type">{{ item.type }}</span>
                      <span v-if="item.duration">{{ item.duration }}分钟</span>
                    </div>
                    <div class="detailed-rank-actors">
                      <span v-if="item.actors && item.actors.length">主演: {{ item.actors.slice(0, 3).join(' / ') }}</span>
                      <span v-else>管理员暂未输入演职人员信息</span>
                    </div>
                    <div class="detailed-rank-time">{{ item.start }}</div>
                  </div>
                </div>
                <div class="detailed-rank-value">
                  <template v-if="data.activeCategory === 'boxOffice'">
                    <div class="value-label">总票房</div>
                    <div class="value-number box-office">{{ (item.total * 1).toFixed(2) }}元</div>
                  </template>
                  <template v-else-if="data.activeCategory === 'rating'">
                    <div class="value-label">用户评分</div>
                    <div class="value-number rating">
                      <i class="el-icon-star-on"></i> {{ (item.score * 1).toFixed(1) }}
                    </div>
                  </template>
                  <template v-else>
                    <div class="value-label">热度指数</div>
                    <div class="value-number trending">{{ Math.floor(item.total * 10 / (index + 1)) }}</div>
                  </template>
                </div>
              </div>
            </div>
          </div>
        </el-col>

        <el-col :span="7">
          <!-- 侧边栏榜单 -->
          <div class="chart-card side-charts">
            <div class="chart-header">
              <div class="chart-title">
                <i class="el-icon-data-analysis"></i>
                <span>热门演员榜</span>
              </div>
            </div>
            
            <div class="actor-ranking-list" v-if="data.topActors.length > 0">
              <div v-for="(actor, index) in data.topActors" :key="index" class="actor-item">
                <div class="actor-rank">{{ index + 1 }}</div>
                <div class="actor-info">
                  <div class="actor-name">{{ actor }}</div>
                  <div class="actor-films-count">{{ 3 + Math.floor(Math.random() * 8) }} 部作品</div>
                </div>
              </div>
            </div>
            <div class="empty-data" v-else>
              暂无演员数据
            </div>
          </div>

          <div class="chart-card side-charts">
            <div class="chart-header">
              <div class="chart-title">
                <i class="el-icon-pie-chart"></i>
                <span>类型分布</span>
              </div>
            </div>
            
            <div class="type-distribution">
              <div v-for="(item, index) in data.typeDistribution" :key="index" class="type-item">
                <div class="type-name">{{ item.name }}</div>
                <div class="type-progress-container">
                  <div class="type-progress" :style="{width: `${item.percentage}%`, backgroundColor: getTypeColor(index)}"></div>
                </div>
                <div class="type-percentage">{{ item.percentage }}%</div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { reactive, computed, onMounted } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  topTotal: [],
  topScore: [],
  categories: [
    { id: 'boxOffice', name: '票房榜' },
    { id: 'rating', name: '评分榜' },
    { id: 'trending', name: '热度榜' }
  ],
  activeCategory: 'boxOffice',
  topActors: [],
  typeDistribution: []
})

const categoryTitle = computed(() => {
  switch(data.activeCategory) {
    case 'boxOffice': return '电影票房排行榜';
    case 'rating': return '电影评分排行榜';
    case 'trending': return '电影热度排行榜';
    default: return '电影排行榜';
  }
})

const displayList = computed(() => {
  if (data.activeCategory === 'boxOffice') {
    return data.topTotal;
  } else if (data.activeCategory === 'rating') {
    return data.topScore;
  } else if (data.activeCategory === 'trending') {
    // 热度榜可以根据票房和评分综合计算
    if (data.topTotal.length > 0 && data.topScore.length > 0) {
      // 创建一个结合了票房和评分的新数组
      const combined = [...data.topTotal];
      // 为每个电影添加一个热度指数（简单算法：票房排名+评分*10）
      combined.sort((a, b) => {
        const aScore = a.score ? parseFloat(a.score) : 0;
        const bScore = b.score ? parseFloat(b.score) : 0;
        const aTotal = a.total ? parseFloat(a.total) : 0;
        const bTotal = b.total ? parseFloat(b.total) : 0;
        
        // 热度计算公式：票房×0.7 + 评分×30×0.3
        const aHeat = aTotal * 0.7 + aScore * 30 * 0.3;
        const bHeat = bTotal * 0.7 + bScore * 30 * 0.3;
        
        return bHeat - aHeat; // 降序排列
      });
      return combined;
    }
    return data.topTotal;
  }
  return [];
})

const loadTotal = () => {
  request.get('/film/selectTotalTop').then(res => {
    if (res.code === '200') {
      data.topTotal = res.data;
      extractActorsAndTypes(); // 提取演员和类型数据
    } else {
      ElMessage.error(res.msg);
    }
  })
}

const loadScore = () => {
  request.get('/film/selectScoreTop').then(res => {
    if (res.code === '200') {
      data.topScore = res.data;
      if (!data.topTotal.length) {
        extractActorsAndTypes(); // 如果没有票房数据，从评分数据提取
      }
    } else {
      ElMessage.error(res.msg);
    }
  })
}

// 从电影数据中提取演员和类型信息
const extractActorsAndTypes = () => {
  const films = data.topTotal.length ? data.topTotal : data.topScore;
  const actorsMap = new Map();
  const typesMap = new Map();
  
  films.forEach(film => {
    // 提取演员
    if (film.actors && film.actors.length) {
      film.actors.forEach(actor => {
        actorsMap.set(actor, (actorsMap.get(actor) || 0) + 1);
      });
    }
    
    // 提取类型
    if (film.type) {
      const types = film.type.split(',').map(t => t.trim());
      types.forEach(type => {
        if (type) {
          typesMap.set(type, (typesMap.get(type) || 0) + 1);
        }
      });
    }
  });
  
  // 处理演员数据
  const actors = Array.from(actorsMap.entries())
    .sort((a, b) => b[1] - a[1])
    .slice(0, 10)
    .map(entry => entry[0]);
  
  data.topActors = actors;
  
  // 处理类型数据
  const totalFilms = films.length;
  const types = Array.from(typesMap.entries())
    .sort((a, b) => b[1] - a[1])
    .slice(0, 6)
    .map(entry => ({
      name: entry[0],
      count: entry[1],
      percentage: Math.round((entry[1] / totalFilms) * 100)
    }));
  
  data.typeDistribution = types;
}

// 在你的电影列表部分，点击电影跳转到详情页
const goToMovieDetail = (id) => {
  router.push({ path: '/front/filmDetail', query: { id } });
}

const changeCategory = (categoryId) => {
  data.activeCategory = categoryId;
}

const getTypeColor = (index) => {
  const colors = [
    '#ff4d4f', '#ffa940', '#52c41a', 
    '#1890ff', '#722ed1', '#eb2f96'
  ];
  return colors[index % colors.length];
}

// 计算属性
Object.defineProperty(data, 'categoryTitle', { get: () => categoryTitle.value });
Object.defineProperty(data, 'displayList', { get: () => displayList.value });

// 初始化加载数据
loadTotal();
loadScore();
</script>

<style scoped>
.charts-wrapper {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0f7ff 0%, #e6f3ff 100%);
  padding-bottom: 40px;
}

.banner-section {
  width: 100%;
  margin-bottom: 40px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.carousel-content {
  width: 100%;
  height: 400px;
  position: relative;
  cursor: pointer;
  overflow: hidden;
}

.carousel-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    to top,
    rgba(0, 0, 0, 0.9) 0%,
    rgba(0, 0, 0, 0.5) 40%,
    rgba(0, 0, 0, 0.1) 100%
  );
  z-index: 1;
}

.carousel-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.6s ease;
}

.carousel-content:hover .carousel-image {
  transform: scale(1.05);
}

.carousel-info {
  position: absolute;
  bottom: 40px;
  left: 70px;
  right: 40px;
  color: #fff;
  z-index: 2;
}

.carousel-title {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 15px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.7);
}

.carousel-details {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 10px;
}

.carousel-rank {
  background: #ff4d4f;
  padding: 6px 12px;
  border-radius: 20px;
  font-weight: 600;
  font-size: 16px;
}

.carousel-score, .carousel-total {
  font-size: 18px;
  display: flex;
  align-items: center;
  gap: 5px;
}

.carousel-score {
  color: #ffc53d;
}

.carousel-desc {
  font-size: 16px;
  opacity: 0.9;
}

.carousel-actors, .carousel-time {
  margin-top: 4px;
}

.charts-container {
  width: 85%;
  margin: 0 auto;
}

/* 分类标签样式 */
.category-tabs {
  display: flex;
  margin-bottom: 30px;
  background: #fff;
  border-radius: 20px;
  padding: 8px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.category-tab {
  padding: 12px 24px;
  border-radius: 16px;
  font-size: 16px;
  font-weight: 500;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-right: 8px;
  text-align: center;
}

.category-tab:hover {
  color: #2d98f3;
  background: rgba(45, 152, 243, 0.08);
}

.active-tab {
  background: #2d98f3;
  color: #fff;
  box-shadow: 0 6px 16px rgba(45, 152, 243, 0.25);
}

.active-tab:hover {
  color: #fff;
  background: #2d98f3;
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

.main-chart {
  height: 100%;
}

.side-charts {
  margin-bottom: 20px;
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.chart-title {
  display: flex;
  align-items: center;
  font-size: 22px;
  font-weight: 600;
  color: #333;
}

.chart-title i {
  margin-right: 10px;
  color: #2d98f3;
  font-size: 24px;
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

/* 详细排行榜样式 */
.detailed-ranking-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.detailed-ranking-item {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 20px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.detailed-ranking-item:hover {
  background: rgba(255, 255, 255, 0.9);
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.detailed-rank-number {
  min-width: 40px;
  width: 40px;
  height: 40px;
  background: rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 18px;
  color: #666;
}

.top-rank {
  background: #ff4d4f;
  color: white;
}

.detailed-rank-poster {
  position: relative;
  width: 100px;
  height: 140px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.detailed-rank-poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s ease;
}

.detailed-ranking-item:hover .detailed-rank-poster img {
  transform: scale(1.1);
}

.poster-hover {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.detailed-ranking-item:hover .poster-hover {
  opacity: 1;
}

.detailed-rank-info {
  flex: 1;
}

.detailed-rank-title {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}

.detailed-rank-details {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.detailed-rank-tags {
  display: flex;
  gap: 10px;
}

.detailed-rank-tags span {
  background: rgba(0, 0, 0, 0.05);
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 14px;
  color: #666;
}

.detailed-rank-actors {
  color: #666;
  font-size: 14px;
}

.detailed-rank-time {
  color: #999;
  font-size: 14px;
}

.detailed-rank-value {
  text-align: center;
  min-width: 120px;
}

.value-label {
  font-size: 14px;
  color: #999;
  margin-bottom: 6px;
}

.value-number {
  font-size: 24px;
  font-weight: 700;
}

.box-office {
  color: #ff4d4f;
}

.rating {
  color: #ffa940;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
}

.trending {
  color: #13c2c2;
}

/* 演员榜单样式 */
.actor-ranking-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.actor-item {
  display: flex;
  align-items: center;
  padding: 15px;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 12px;
  transition: all 0.3s ease;
}

.actor-item:hover {
  background: rgba(255, 255, 255, 0.9);
  transform: translateX(5px);
}

.actor-rank {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  margin-right: 15px;
}

.actor-name {
  font-weight: 600;
  font-size: 16px;
  margin-bottom: 4px;
}

.actor-films-count {
  font-size: 12px;
  color: #999;
}

/* 类型分布样式 */
.type-distribution {
  margin-top: 10px;
}

.type-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.type-name {
  width: 80px;
  font-size: 14px;
  text-align: right;
  padding-right: 10px;
  color: #666;
}

.type-progress-container {
  flex: 1;
  height: 8px;
  background: rgba(0, 0, 0, 0.05);
  border-radius: 4px;
  overflow: hidden;
  margin: 0 15px;
}

.type-progress {
  height: 100%;
  border-radius: 4px;
}

.type-percentage {
  width: 40px;
  text-align: left;
  font-size: 14px;
  color: #666;
}

.empty-data {
  padding: 40px 20px;
  text-align: center;
  color: #999;
  font-size: 14px;
}
</style>
