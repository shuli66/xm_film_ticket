<template>
  <div class="film-wrapper">
    <div class="film-container">
      <!-- 筛选区域 -->
      <div class="filter-section">
        <div class="filter-group">
          <div class="filter-label">类型：</div>
          <div class="filter-options">
            <div class="filter-item" :class="{'filter-active' : !data.typeFlag}" @click="changeTypeFlag(null)">全部</div>
            <div class="filter-item" v-for="item in data.typeData" :key="item.id" 
                 :class="{'filter-active' : data.typeFlag === item.id}" 
                 @click="changeTypeFlag(item.id)">{{ item.title }}</div>
          </div>
        </div>
        <div class="filter-group">
          <div class="filter-label">年代：</div>
          <div class="filter-options">
            <div class="filter-item" :class="{'filter-active' : !data.yearFlag}" @click="changeYearFlag(null)">全部</div>
            <div class="filter-item" v-for="item in data.yearData" :key="item" 
                 :class="{'filter-active' : data.yearFlag === item}" 
                 @click="changeYearFlag(item)">{{ item }}</div>
          </div>
        </div>
        <div class="filter-group">
          <div class="filter-label">区域：</div>
          <div class="filter-options">
            <div class="filter-item" :class="{'filter-active' : !data.areaFlag}" @click="changeAreaFlag(null)">全部</div>
            <div class="filter-item" v-for="item in data.areaData" :key="item.id" 
                 :class="{'filter-active' : data.areaFlag === item.id}" 
                 @click="changeAreaFlag(item.id)">{{ item.title }}</div>
          </div>
        </div>
      </div>

      <!-- 电影列表 -->
      <div class="movie-list" v-loading="data.loading">
        <el-row :gutter="30">
          <el-col :span="6" v-for="item in data.filmData" :key="item.id" class="movie-card">
            <div class="movie-item">
              <div class="movie-poster" @click="$router.push('/front/filmDetail?id=' + item.id)">
                <img :src="item.img" :alt="item.title">
                <div class="movie-overlay">
                  <div class="movie-rating">
                    <el-rate
                      v-model="item.score"
                      disabled
                      show-score
                      text-color="#ff9900"
                      score-template="{value}分"
                    />
                  </div>
                  <el-button type="primary" class="buy-ticket-btn" @click.stop="handleBuyTicket(item)">
                    立即购票
                    <el-icon class="el-icon--right"><ArrowRight /></el-icon>
                  </el-button>
                </div>
              </div>
              <div class="movie-info">
                <div class="movie-title">{{ item.title }}</div>
                <div class="movie-meta">
                  <div class="movie-status">
                    <el-tag v-if="item.status === '待上映'" type="warning" effect="light">{{ item.status }}</el-tag>
                    <el-tag v-if="item.status === '已上映'" type="success" effect="light">{{ item.status }}</el-tag>
                    <el-tag v-if="item.status === '停止上映'" type="danger" effect="light">{{ item.status }}</el-tag>
                  </div>
                  <div class="movie-score">
                    <span class="score-value">{{ item.score }}</span>
                    <span class="score-label">分</span>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>

        <!-- 空状态 -->
        <div v-if="data.filmData.length === 0 && !data.loading" class="empty-state">
          <el-empty description="暂无符合条件的电影">
            <template #image>
              <el-icon :size="60"><Film /></el-icon>
            </template>
            <template #description>
              <p class="empty-text">没有找到符合条件的电影，请尝试其他筛选条件</p>
            </template>
            <el-button type="primary" @click="resetFilters">重置筛选条件</el-button>
          </el-empty>
        </div>
      </div>

      <!-- 分页 -->
      <div class="pagination-container" v-if="data.total && data.filmData.length > 0">
        <el-pagination 
          @current-change="load" 
          background 
          layout="total, prev, pager, next, jumper" 
          :page-size="data.pageSize" 
          v-model:current-page="data.pageNum" 
          :total="data.total" />
      </div>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import { ArrowRight, Film } from '@element-plus/icons-vue';
import router from "@/router/index.js";

const data = reactive({
  typeFlag: null,
  areaFlag: null,
  yearFlag: null,
  typeData: [],
  areaData: [],
  yearData: [],
  pageNum: 1,
  pageSize: 8,
  total: 0,
  filmData: [],
  loading: false
})

const load = () => {
  data.loading = true;
  request.get('/film/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      typeId: data.typeFlag,
      areaId: data.areaFlag,
      year: data.yearFlag
    }
  }).then(res => {
    if (res.code === '200') {
      data.filmData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  }).finally(() => {
    data.loading = false;
  })
}

const loadType = () => {
  request.get('/type/selectAll').then(res => {
    if (res.code === '200') {
      data.typeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadArea = () => {
  request.get('/area/selectAll').then(res => {
    if (res.code === '200') {
      data.areaData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadYear = () => {
  request.get('/getYear').then(res => {
    if (res.code === '200') {
      data.yearData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const changeTypeFlag = (id) => {
  data.typeFlag = id
  load()
}
const changeYearFlag = (year) => {
  data.yearFlag = year
  load()
}
const changeAreaFlag = (id) => {
  data.areaFlag = id
  load()
}

const handleBuyTicket = (item) => {
  if (item.status !== '已上映') {
    ElMessage.warning('该电影暂未上映，敬请期待！');
    return;
  }
  router.push('/front/filmDetail?id=' + item.id);
}

const resetFilters = () => {
  data.typeFlag = null;
  data.areaFlag = null;
  data.yearFlag = null;
  data.pageNum = 1;
  load();
}

loadType()
loadArea()
loadYear()
load()
</script>

<style scoped>
.film-wrapper {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0f7ff 0%, #e6f3ff 100%);
  padding: 40px 0;
}

.film-container {
  width: 90%;
  max-width: 1400px;
  margin: 0 auto;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 24px;
  padding: 40px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.9);
}

.filter-section {
  background: #fff;
  border-radius: 16px;
  padding: 30px;
  margin-bottom: 40px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.05);
}

.filter-group {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.filter-group:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.filter-label {
  width: 60px;
  font-size: 16px;
  color: #333;
  font-weight: 500;
  padding-top: 8px;
}

.filter-options {
  flex: 1;
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.filter-item {
  padding: 8px 16px;
  border: 1px solid #ddd;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  color: #666;
}

.filter-item:hover {
  background: rgba(45, 152, 243, 0.1);
  border-color: #2d98f3;
  color: #2d98f3;
  transform: translateY(-2px);
}

.filter-active {
  background: #2d98f3;
  color: #fff;
  border-color: #2d98f3;
  box-shadow: 0 4px 12px rgba(45, 152, 243, 0.2);
}

.movie-list {
  margin-top: 30px;
  min-height: 400px;
}

.movie-card {
  margin-bottom: 30px;
}

.movie-item {
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.movie-item:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.15);
}

.movie-poster {
  position: relative;
  cursor: pointer;
  flex: 1;
  overflow: hidden;
}

.movie-poster img {
  width: 100%;
  height: 320px;
  object-fit: cover;
  display: block;
  transition: transform 0.5s ease;
}

.movie-item:hover .movie-poster img {
  transform: scale(1.05);
}

.movie-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to top, rgba(0,0,0,0.8), rgba(0,0,0,0.2));
  padding: 20px;
  opacity: 0;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
}

.movie-poster:hover .movie-overlay {
  opacity: 1;
}

.movie-rating {
  margin-bottom: 15px;
}

.buy-ticket-btn {
  width: 100%;
  height: 40px;
  border-radius: 20px;
  font-weight: 600;
  background: linear-gradient(45deg, #2d98f3, #1a73e8);
  border: none;
  box-shadow: 0 4px 12px rgba(45, 152, 243, 0.4);
  transition: all 0.3s ease;
}

.buy-ticket-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(45, 152, 243, 0.6);
}

.movie-info {
  padding: 20px;
  background: #fff;
}

.movie-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #333;
  line-height: 1.4;
}

.movie-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.movie-status {
  margin-bottom: 0;
}

.movie-score {
  display: flex;
  align-items: baseline;
  color: #ff6b6b;
}

.score-value {
  font-size: 24px;
  font-weight: 600;
}

.score-label {
  font-size: 14px;
  margin-left: 4px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.empty-text {
  color: #909399;
  margin: 10px 0 20px;
}

.pagination-container {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}

/* 响应式设计 */
@media screen and (max-width: 1200px) {
  .film-container {
    width: 95%;
    padding: 30px;
  }
  
  .movie-poster img {
    height: 280px;
  }
}

@media screen and (max-width: 768px) {
  .film-container {
    width: 100%;
    padding: 20px;
    border-radius: 0;
  }
  
  .filter-section {
    padding: 20px;
  }
  
  .movie-poster img {
    height: 240px;
  }
  
  .movie-title {
    font-size: 16px;
  }
}
</style>