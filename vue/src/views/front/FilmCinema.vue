<template>
  <div>
    <div class="film-header" style="background: linear-gradient(to right, #46187e, #32046b, #32115b);">
      <div class="film-content">
        <img :src="data.filmData.img" alt="" class="film-poster">
        <div class="film-info">
          <div class="film-title">{{ data.filmData.title }}</div>
          <div class="film-english">{{ data.filmData.english }}</div>
          <div class="film-tags">
            <span v-for="(item, index) in data.filmData.types" :key="index" class="film-tag">{{ item }}</span>
          </div>
          <div class="film-meta">{{ data.filmData.areaName }} / {{ data.filmData.time }}分钟</div>
          <div class="film-release">{{ data.filmData.start }} 中国大陆上映</div>
          <el-button class="details-btn" @click="$router.push('/front/filmDetail?id=' + data.filmData.id)">查看更多电影详情</el-button>
        </div>
        <div class="film-stats">
          <div class="stats-label">影片口碑</div>
          <div class="stats-score">
            <div class="score-number"><span class="big-score">{{ data.filmData.score }}</span> 分</div>
            <div class="score-stars">
              <el-rate v-model="data.halfScore" disabled/>
              <div class="rate-count">{{ data.scoreTime }}人评分</div>
            </div>
          </div>
          <div class="stats-label stats-boxoffice">累计票房</div>
          <div class="boxoffice-number"><span class="big-number">{{ (data.filmData.total * 1).toFixed(2) }}</span> 元</div>
        </div>
      </div>
    </div>
    <div class="page-content">
      <div class="section-title">放映影院</div>
      
      <!-- 有放映影院时显示影院列表 -->
      <template v-if="data.cinemaData && data.cinemaData.length > 0">
        <div class="cinema-list">
          <div class="cinema-item" v-for="(item, index) in data.cinemaData" :key="index">
            <div class="cinema-info">
              <img :src="item.avatar" alt="" class="cinema-avatar">
              <div class="cinema-details">
                <div class="cinema-name">{{ item.name }}</div>
                <div class="cinema-tags">
                  <el-tag type="danger" effect="dark" size="small">退票无忧</el-tag>
                  <el-tag type="success" effect="dark" size="small">儿童优惠</el-tag>
                  <el-tag type="primary" effect="dark" size="small">WiFi覆盖</el-tag>
                  <el-tag type="info" effect="dark" size="small">免费停车</el-tag>
                </div>
                <div class="cinema-address">地址：{{ item.address }}</div>
                <div class="cinema-contact">电话：{{ item.phone }}</div>
                <div class="cinema-contact">邮箱：{{ item.email }}</div>
              </div>
            </div>
            <div class="cinema-actions">
              <el-button type="primary" @click="$router.push('/front/cinemaDetail?id=' + item.id + '&filmId=' + data.filmId)">
                进入购票
              </el-button>
            </div>
          </div>
        </div>
      </template>
      
      <!-- 没有放映影院时显示提示 -->
      <div v-else class="no-cinema-container">
        <el-empty 
          description="暂无放映影院" 
          :image-size="200"
        >
          <template #image>
            <div class="custom-empty-image">
              <svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="160" height="160">
                <path d="M720 288H304c-78.4 0-80 32-80 32v384s1.6 32 80 32h416c78.4 0 80-32 80-32V320s-1.6-32-80-32z" fill="#FFF" />
                <path d="M784 320v384c0 16-21.6 32-64 32H304c-42.4 0-64-16-64-32V320c0-16 21.6-32 64-32h416c42.4 0 64 16 64 32z" fill="#E8F0FC" />
                <path d="M288 336h448v352H288z" fill="#E8E8E8" />
                <path d="M751.8 416a31.8 31.8 0 1 0 0 63.6 31.8 31.8 0 0 0 0-63.6z" fill="#e6a23c" />
                <path d="M306.9 800h410.2v32H306.9zM513.8 96a31.8 31.8 0 1 0 0 63.6 31.8 31.8 0 0 0 0-63.6zM272.2 192a31.8 31.8 0 1 0 0 63.6 31.8 31.8 0 0 0 0-63.6zM751.8 192a31.8 31.8 0 1 0 0 63.6 31.8 31.8 0 0 0 0-63.6zM640 512l-256 160V352l256 160z" fill="#409EFF" />
              </svg>
            </div>
          </template>
          <template #description>
            <div class="empty-description">
              <p>该电影暂无放映影院安排</p>
              <p class="empty-subtitle">我们将尽快安排更多影院放映该电影</p>
            </div>
          </template>
          <div class="empty-actions">
            <el-button type="primary" @click="$router.push('/front/filmList')">
              查看其他热映电影
            </el-button>
            <el-button @click="$router.push('/front/filmDetail?id=' + data.filmId)">
              返回电影详情
            </el-button>
          </div>
        </el-empty>
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
  filmId: router.currentRoute.value.query.id,
  filmData: {},
  cinemaData: [],
  scoreTime: 0,
  halfScore: 0
})

const loadCinema = () => {
  request.get('/show/selectByFilmId/' + data.filmId).then(res => {
    if (res.code === '200') {
      data.cinemaData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadFilm = () => {
  data.filmId = router.currentRoute.value.query.id
  request.get('/film/selectById/' + data.filmId).then(res => {
    if (res.code === '200') {
      data.filmData = res.data
      data.halfScore = (data.filmData.score / 2).toFixed(1)
      // 如果没有票房数据，设置默认值为0
      if (!data.filmData.total) {
        data.filmData.total = 0
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadScoreTime = () => {
  request.get('/score/selectAll', {
    params: {
      filmId: data.filmId,
    }
  }).then(res => {
    if (res.code === '200') {
      data.scoreTime = res.data.length
    }
  })
}

loadFilm()
loadCinema()
loadScoreTime()
</script>

<style scoped>
.film-header {
  height: 320px;
}

.film-content {
  width: 60%;
  max-width: 1200px;
  margin: 0 auto;
  height: 320px;
  display: flex;
  align-items: center;
  padding: 0 20px;
}

.film-poster {
  width: 220px;
  height: 300px;
  object-fit: cover;
  border-radius: 8px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s;
}

.film-poster:hover {
  transform: translateY(-5px);
}

.film-info {
  flex: 1;
  color: white;
  padding: 20px 35px;
}

.film-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 5px;
}

.film-english {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.8);
  margin-bottom: 15px;
}

.film-tags {
  margin-top: 15px;
}

.film-tag {
  display: inline-block;
  background-color: rgba(255, 255, 255, 0.15);
  padding: 2px 10px;
  border-radius: 12px;
  margin-right: 8px;
  font-size: 13px;
}

.film-meta, .film-release {
  margin-top: 10px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.details-btn {
  background-color: #ef4238;
  border: none;
  color: white;
  width: 100%;
  margin-top: 40px;
  height: 40px;
  font-size: 16px;
  border-radius: 20px;
  transition: all 0.3s;
}

.details-btn:hover {
  background-color: #ff5649;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(239, 66, 56, 0.4);
}

.film-stats {
  width: 220px;
  color: white;
  padding-left: 20px;
}

.stats-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.7);
}

.stats-score {
  display: flex;
  align-items: center;
  margin: 10px 0 20px 0;
}

.score-number {
  width: 100px;
  font-weight: bold;
  font-size: 16px;
}

.big-score {
  font-size: 36px;
  color: #ffb400;
}

.score-stars {
  flex: 1;
}

.rate-count {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.7);
  margin-top: 5px;
}

.stats-boxoffice {
  margin-top: 10px;
}

.boxoffice-number {
  margin-top: 8px;
  font-weight: bold;
  font-size: 16px;
}

.big-number {
  font-size: 30px;
  color: #ffb400;
}

.page-content {
  width: 80%;
  max-width: 1200px;
  margin: 50px auto;
  padding: 0 20px;
}

.section-title {
  font-size: 22px;
  border-left: 4px solid #ef4238;
  padding-left: 15px;
  line-height: 30px;
  margin: 40px 0 30px 0;
  font-weight: bold;
}

.cinema-list {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.cinema-item {
  display: flex;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
  transition: all 0.3s;
}

.cinema-item:hover {
  background-color: #f9f9f9;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.cinema-item:last-child {
  border-bottom: none;
}

.cinema-info {
  flex: 1;
  display: flex;
}

.cinema-avatar {
  width: 120px;
  height: 120px;
  border-radius: 8px;
  object-fit: cover;
  transition: all 0.3s;
}

.cinema-avatar:hover {
  transform: scale(1.05);
}

.cinema-details {
  flex: 1;
  margin-left: 20px;
}

.cinema-name {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.cinema-tags {
  margin-bottom: 15px;
}

.cinema-tags .el-tag {
  margin-right: 8px;
}

.cinema-address, .cinema-contact {
  margin-top: 8px;
  color: #666;
  font-size: 14px;
}

.cinema-actions {
  width: 120px;
  text-align: center;
}

.cinema-actions .el-button {
  padding: 12px 25px;
  border-radius: 20px;
}

.no-cinema-container {
  padding: 60px 20px;
  text-align: center;
  background-color: #f9fafc;
  border-radius: 10px;
  margin-top: 20px;
}

.custom-empty-image {
  margin-bottom: 20px;
}

.empty-description {
  font-size: 16px;
  color: #606266;
  margin-bottom: 20px;
}

.empty-subtitle {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}

.empty-actions {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  gap: 15px;
}

.empty-actions .el-button {
  padding: 10px 20px;
  border-radius: 20px;
}

@media screen and (max-width: 768px) {
  .film-content {
    width: 100%;
    flex-direction: column;
    height: auto;
    padding: 20px;
  }
  
  .film-header {
    height: auto;
    padding: 20px 0;
  }
  
  .film-poster {
    width: 180px;
    height: 250px;
    margin-bottom: 20px;
  }
  
  .film-info {
    padding: 0;
    text-align: center;
  }
  
  .film-stats {
    width: 100%;
    padding: 20px 0 0 0;
    display: flex;
    flex-direction: column;
    align-items: center;
  }
  
  .page-content {
    width: 100%;
  }
  
  .cinema-info {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .cinema-item {
    flex-direction: column;
  }
  
  .cinema-details {
    margin-left: 0;
    margin-top: 20px;
  }
  
  .cinema-actions {
    width: 100%;
    margin-top: 20px;
  }
  
  .empty-actions {
    flex-direction: column;
    align-items: center;
  }
  
  .empty-actions .el-button {
    margin-bottom: 10px;
  }
}
</style>