<template>
  <div class="cinema-wrapper">
    <div class="cinema-container">
      <div class="cinema-list">
        <div class="cinema-card" v-for="item in data.cinemaData" :key="item.id">
          <div class="cinema-content">
            <div class="cinema-avatar">
              <img :src="item.avatar" :alt="item.name">
            </div>
            <div class="cinema-info">
              <div class="cinema-name">{{ item.name }}</div>
              <div class="cinema-tags">
                <el-tag type="danger" class="tag-item">退票无忧</el-tag>
                <el-tag type="success" class="tag-item">儿童优惠</el-tag>
                <el-tag type="primary" class="tag-item">WiFi覆盖</el-tag>
                <el-tag type="info" class="tag-item">免费停车</el-tag>
              </div>
              <div class="cinema-details">
                <div class="detail-item">
                  <i class="el-icon-location"></i>
                  <span>{{ item.address }}</span>
                </div>
                <div class="detail-item">
                  <i class="el-icon-phone"></i>
                  <span>{{ item.phone }}</span>
                </div>
                <div class="detail-item">
                  <i class="el-icon-message"></i>
                  <span>{{ item.email }}</span>
                </div>
              </div>
            </div>
            <div class="cinema-action">
              <el-button type="primary" class="buy-ticket-btn" @click="$router.push('/front/cinemaDetail?id=' + item.id)">
                进入购票
              </el-button>
            </div>
          </div>
        </div>
      </div>
      <div class="pagination-container" v-if="data.total">
        <el-pagination 
          @current-change="loadCinema" 
          background 
          layout="total, prev, pager, next" 
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
import router from "@/router/index.js";

const data = reactive({
  pageNum: 1,
  pageSize: 4,
  total: 0,
  cinemaData: []
})

const loadCinema = () => {
  request.get('/cinema/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      status: '审核通过'
    }
  }).then(res => {
    if (res.code === '200') {
      data.cinemaData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadCinema()
</script>

<style scoped>
.cinema-wrapper {
  min-height: 100vh;
  background: linear-gradient(135deg, #f0f7ff 0%, #e6f3ff 100%);
  padding: 40px 0;
}

.cinema-container {
  width: 85%;
  margin: 0 auto;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 24px;
  padding: 40px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.9);
}

.cinema-list {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.cinema-card {
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.cinema-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.15);
}

.cinema-content {
  display: flex;
  padding: 30px;
  gap: 30px;
}

.cinema-avatar {
  width: 150px;
  height: 150px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.cinema-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.cinema-info {
  flex: 1;
}

.cinema-name {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 15px;
}

.cinema-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}

.tag-item {
  border-radius: 20px;
  padding: 6px 12px;
  font-size: 14px;
}

.cinema-details {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.detail-item {
  display: flex;
  align-items: center;
  color: #666;
  font-size: 14px;
}

.detail-item i {
  margin-right: 8px;
  font-size: 16px;
}

.cinema-action {
  display: flex;
  align-items: center;
}

.buy-ticket-btn {
  padding: 12px 30px;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(45deg, #2d98f3, #1a73e8);
  border: none;
  box-shadow: 0 4px 12px rgba(45, 152, 243, 0.4);
  transition: all 0.3s ease;
}

.buy-ticket-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(45, 152, 243, 0.5);
}

.pagination-container {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}
</style>
