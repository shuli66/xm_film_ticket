<template>
  <div class="collect-container">
    <div class="collect-header">
      <h2>我想看的电影</h2>
      <span class="collect-count">({{ data.total }})</span>
    </div>
    
    <div class="collect-list">
      <el-row :gutter="20">
        <el-col :span="4" v-for="item in data.collectData" :key="item.id">
          <div class="film-card" @click="$router.push('/front/filmDetail?id=' + item.film.id)">
            <div class="film-img-wrapper">
              <img :src="item.film.img" :alt="item.film.title" class="film-img">
              <div class="film-score">
                <i class="el-icon-star-on"></i>
                {{ item.film.score }}分
              </div>
            </div>
            <div class="film-info">
              <div class="film-title">{{ item.film.title }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="pagination-container" v-if="data.total">
      <el-pagination
        @current-change="load"
        background
        layout="total, prev, pager, next"
        :page-size="data.pageSize"
        v-model:current-page="data.pageNum"
        :total="data.total"
      />
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  pageNum: 1,
  pageSize: 10,
  total: 0,
  collectData: []
})

const load = () => {
  request.get('/collect/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      userId: data.user.id
    }
  }).then(res => {
    if (res.code === '200') {
      data.collectData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
</script>

<style scoped>
.collect-container {
  width: 80%;
  margin: 30px auto;
  padding: 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.collect-header {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
  padding: 15px 0;
  border-bottom: 2px solid #f0f0f0;
  position: relative;
}

.collect-header h2 {
  margin: 0;
  font-size: 28px;
  color: #333;
  font-weight: 600;
  background: linear-gradient(45deg, #409EFF, #36cfc9);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  position: relative;
  padding-left: 20px;
}

.collect-header h2::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 24px;
  background: linear-gradient(to bottom, #409EFF, #36cfc9);
  border-radius: 2px;
}

.collect-count {
  margin-left: 12px;
  font-size: 20px;
  color: #666;
  background: #f5f7fa;
  padding: 4px 12px;
  border-radius: 20px;
  display: flex;
  align-items: center;
}

.collect-count::before {
  content: '';
  display: inline-block;
  width: 8px;
  height: 8px;
  background: #409EFF;
  border-radius: 50%;
  margin-right: 8px;
}

.collect-list {
  min-height: 400px;
}

.film-card {
  margin-bottom: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.film-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

.film-img-wrapper {
  position: relative;
  width: 100%;
  padding-top: 140%;
  overflow: hidden;
}

.film-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.film-card:hover .film-img {
  transform: scale(1.05);
}

.film-score {
  position: absolute;
  bottom: 10px;
  right: 10px;
  background: rgba(0, 0, 0, 0.7);
  color: #ffd700;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 14px;
  display: flex;
  align-items: center;
}

.film-score i {
  margin-right: 4px;
}

.film-info {
  padding: 12px;
}

.film-title {
  font-size: 16px;
  color: #333;
  font-weight: 500;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.pagination-container {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

:deep(.el-col-4) {
  width: 20%;
  max-width: 20%;
}

@media screen and (max-width: 1200px) {
  .collect-container {
    width: 90%;
  }
  
  :deep(.el-col-4) {
    width: 25%;
    max-width: 25%;
  }
}

@media screen and (max-width: 768px) {
  .collect-container {
    width: 95%;
  }
  
  :deep(.el-col-4) {
    width: 33.33%;
    max-width: 33.33%;
  }
}

@media screen and (max-width: 480px) {
  :deep(.el-col-4) {
    width: 50%;
    max-width: 50%;
  }
}
</style>