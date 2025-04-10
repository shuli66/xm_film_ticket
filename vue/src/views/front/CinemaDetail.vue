<template>
  <div class="cinema-detail-container">
    <!-- 影院基本信息 -->
    <div class="cinema-info-card">
      <div class="cinema-header">
        <img :src="data.cinemaInfo.avatar" alt="影院logo" class="cinema-avatar" v-if="data.cinemaInfo.avatar">
        <div v-else class="cinema-avatar cinema-avatar-placeholder">
          <el-icon><Picture /></el-icon>
        </div>
        <div class="cinema-basic-info">
          <h1 class="cinema-name">{{ data.cinemaInfo.name }}</h1>
          <div class="cinema-address">
            <el-icon><Location /></el-icon>
            <span>{{ data.cinemaInfo.address }}</span>
          </div>
          <div class="cinema-contact">
            <span class="contact-item">
              <el-icon><Phone /></el-icon>
              <span>{{ data.cinemaInfo.phone || '暂无联系电话' }}</span>
            </span>
            <span class="contact-item">
              <el-icon><Message /></el-icon>
              <span>{{ data.cinemaInfo.email || '暂无电子邮箱' }}</span>
            </span>
          </div>
          <div class="cinema-services">
            <el-tag type="danger">退票无忧</el-tag>
            <el-tag type="success">儿童优惠</el-tag>
            <el-tag type="primary">WiFi覆盖</el-tag>
            <el-tag type="info">免费停车</el-tag>
          </div>
        </div>
      </div>
      
      <!-- 影院位置地图 -->
      <div class="cinema-map-section">
        <h2 class="section-title">影院位置</h2>
        <div class="cinema-map">
          <BaiduMap
            ref="mapRef"
            :height="400"
            :showSearch="false"
            :selectable="false"
            :defaultMarker="data.mapMarker"
          />
        </div>
        <div class="cinema-location-info">
          <p>
            <el-icon><Location /></el-icon>
            <span>{{ data.cinemaInfo.address }}</span>
          </p>
          <el-button type="primary" @click="openNavigation">
            <el-icon><Position /></el-icon>
            导航到这里
          </el-button>
        </div>
      </div>
      
      <!-- 正在上映的电影 -->
      <div class="cinema-films-section">
        <h2 class="section-title">该影院上线电影 <span class="subtitle">(点击选择电影)</span></h2>
        <div class="films-grid">
          <div 
            v-for="film in data.filmList" 
            :key="film.id" 
            class="film-card"
            :class="{active: film.id === data.currentFilm.id}"
            @click="changeFilm(film)"
          >
            <div class="film-poster">
              <img :src="film.img" :alt="film.title">
            </div>
            <div class="film-info">
              <div class="film-title">{{ film.title }}</div>
              <div class="film-score">{{ film.score }} <span>分</span></div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 电影放映场次 -->
      <div class="showtimes-section" v-if="data.currentFilm.title">
        <div class="film-header">
          <h2 class="section-title">{{ data.currentFilm.title }}</h2>
          <div class="film-meta">
            <span class="film-score">{{ data.currentFilm.score }} <span>分</span></span>
            <div class="film-details">
              <span>时长：{{ data.currentFilm.time }} 分钟</span>
              <span class="separator">|</span>
              <span>类型：<span v-for="type in data.currentFilm.types" :key="type" class="film-type">{{ type }}</span></span>
              <span class="separator">|</span>
              <span>演员：
                <span v-if="data.currentFilm.actors && data.currentFilm.actors.length">
                  <span v-for="actor in data.currentFilm.actors" :key="actor" class="film-actor">{{ actor }}</span>
                </span>
                <span v-else>管理员暂未录入演职人员</span>
              </span>
            </div>
          </div>
        </div>
        
        <div class="showtimes-table">
          <el-table stripe :data="data.showData">
            <el-table-column prop="time" label="放映时间" />
            <el-table-column prop="roomName" label="放映厅" />
            <el-table-column prop="price" label="售价">
              <template #default="scope">
                <span class="price">¥{{ scope.row.price.toFixed(2) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="scope">
                <el-button type="danger" @click="$router.push('/front/seat?id=' + scope.row.id)">
                  选座购票
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <div class="pagination-container" v-if="data.total > 0">
            <el-pagination
              @current-change="loadShow"
              background
              layout="total, prev, pager, next"
              :page-size="data.pageSize"
              v-model:current-page="data.pageNum"
              :total="data.total"
            />
          </div>
        </div>
      </div>
      
      <div class="empty-state" v-else>
        <el-empty description="请选择一部电影查看放映场次"></el-empty>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted, computed, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { Picture, Location, Phone, Message, Position } from '@element-plus/icons-vue';
import request from '@/utils/request.js';
import BaiduMap from '@/components/BaiduMap.vue';

const route = useRoute();
const router = useRouter();

const data = reactive({
  cinemaInfo: {},
  filmList: [],
  mapMarker: null,
  currentFilm: {},
  showData: [],
  pageNum: 1,
  pageSize: 5,
  total: 0,
  filmId: route.query.filmId // 获取URL中的filmId参数
});

const mapRef = ref(null);

// 计算属性：地图标记点
const mapMarker = computed(() => {
  if (data.cinemaInfo.longitude && data.cinemaInfo.latitude) {
    return {
      lng: data.cinemaInfo.longitude,
      lat: data.cinemaInfo.latitude,
      title: data.cinemaInfo.name
    };
  } else {
    // 如果没有经纬度数据，可以尝试地理编码获取
    return null;
  }
});

// 获取影院详情
const getCinemaDetail = () => {
  const cinemaId = route.query.id;
  if (!cinemaId) {
    ElMessage.error('影院ID不能为空');
    router.push('/front/films');
    return;
  }
  
  request.get(`/cinema/selectById/${cinemaId}`).then(res => {
    if (res.code === '200') {
      data.cinemaInfo = res.data;
      
      // 如果地址存在，使用默认标记点
      if (data.cinemaInfo.address) {
        data.mapMarker = {
          lng: 116.404, // 默认北京
          lat: 39.915,
          title: data.cinemaInfo.name
        };
        
        // 延迟一段时间后尝试地理编码
        setTimeout(() => {
          geocodeAddress(data.cinemaInfo.address, data.cinemaInfo.name);
        }, 1000);
      }
    } else {
      ElMessage.error(res.msg || '获取影院信息失败');
    }
  }).catch(() => {
    ElMessage.error('系统错误，请稍后重试');
  });
};

// 地理编码
const geocodeAddress = (address, name) => {
  if (!window.BMap) {
    console.error('百度地图API尚未加载');
    return;
  }
  
  const geoCoder = new BMap.Geocoder();
  geoCoder.getPoint(address, function(point) {
    if (point) {
      data.mapMarker = {
        lng: point.lng,
        lat: point.lat,
        title: name
      };
      
      // 如果地图组件已加载，设置中心点
      if (mapRef.value) {
        mapRef.value.setCenter(point.lng, point.lat);
      }
    }
  }, "全国");
};

// 获取影院上映的电影列表
const getCinemaFilms = () => {
  const cinemaId = route.query.id;
  if (!cinemaId) return;
  
  request.get(`/show/selectByCinemaId/${cinemaId}`).then(res => {
    if (res.code === '200') {
      data.filmList = res.data || [];
      
      // 如果URL中有filmId参数，则默认选中该电影
      if (data.filmId) {
        const selectedFilm = data.filmList.find(film => film.id == data.filmId);
        if (selectedFilm) {
          data.currentFilm = selectedFilm;
          loadShow();
        }
      } else if (data.filmList.length > 0) {
        // 否则默认选中第一部电影
        data.currentFilm = data.filmList[0];
        loadShow();
      }
    } else {
      ElMessage.error(res.msg || '获取电影列表失败');
    }
  }).catch(() => {
    ElMessage.error('系统错误，请稍后重试');
  });
};

// 点击电影切换当前选中的电影
const changeFilm = (film) => {
  data.currentFilm = film;
  loadShow();
};

// 加载放映场次数据
const loadShow = () => {
  const cinemaId = route.query.id;
  if (!cinemaId || !data.currentFilm.id) return;
  
  request.get('/show/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      filmId: data.currentFilm.id,
      cinemaId: cinemaId,
      status: '购票中'
    }
  }).then(res => {
    if (res.code === '200') {
      data.showData = res.data.list;
      data.total = res.data.total;
    } else {
      ElMessage.error(res.msg || '获取放映场次失败');
    }
  }).catch(() => {
    ElMessage.error('系统错误，请稍后重试');
  });
};

// 打开导航
const openNavigation = () => {
  if (!data.mapMarker) {
    ElMessage.warning('正在获取位置信息，请稍后再试');
    return;
  }
  
  // 使用百度地图导航
  const url = `https://api.map.baidu.com/direction?destination=latlng:${data.mapMarker.lat},${data.mapMarker.lng}|name:${data.cinemaInfo.name}&mode=driving&region=北京&output=html&src=webapp.baidu.openAPIdemo`;
  window.open(url, '_blank');
};

// 跳转到电影详情页
const goToFilmDetail = (filmId) => {
  router.push(`/front/filmDetail?id=${filmId}`);
};

onMounted(() => {
  getCinemaDetail();
  getCinemaFilms();
});
</script>

<style scoped>
.cinema-detail-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 15px;
}

.cinema-info-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
}

.cinema-header {
  display: flex;
  margin-bottom: 30px;
}

.cinema-avatar {
  width: 120px;
  height: 120px;
  border-radius: 10px;
  object-fit: cover;
  margin-right: 20px;
}

.cinema-avatar-placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
  color: #909399;
  font-size: 32px;
}

.cinema-basic-info {
  flex: 1;
}

.cinema-name {
  font-size: 28px;
  margin: 0 0 10px 0;
  color: #303133;
}

.cinema-address, .cinema-contact {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  color: #606266;
  font-size: 16px;
}

.cinema-address .el-icon, .cinema-contact .el-icon {
  margin-right: 8px;
  color: #409EFF;
}

.contact-item {
  display: flex;
  align-items: center;
  margin-right: 20px;
}

.cinema-services {
  margin-top: 15px;
}

.cinema-services .el-tag {
  margin-right: 10px;
  margin-bottom: 5px;
}

.section-title {
  font-size: 20px;
  color: #303133;
  margin: 0 0 20px 0;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
  position: relative;
}

.subtitle {
  font-size: 14px;
  color: #909399;
  font-weight: normal;
}

.section-title::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -1px;
  width: 50px;
  height: 2px;
  background-color: #409EFF;
}

.cinema-map-section, .cinema-films-section, .showtimes-section {
  margin-top: 30px;
}

.cinema-map {
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 15px;
}

.cinema-location-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
}

.cinema-location-info p {
  display: flex;
  align-items: center;
  margin: 0;
  color: #606266;
}

.cinema-location-info .el-icon {
  margin-right: 8px;
  color: #409EFF;
}

.films-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 20px;
}

.film-card {
  width: 200px;
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  position: relative;
  border: 2px solid transparent;
}

.film-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

.film-card.active {
  border-color: #409EFF;
}

.film-card.active::before {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  border-style: solid;
  border-width: 0 24px 24px 0;
  border-color: transparent #409EFF transparent transparent;
  z-index: 2;
}

.film-poster {
  width: 100%;
  height: 280px;
  overflow: hidden;
}

.film-poster img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.film-card:hover .film-poster img {
  transform: scale(1.05);
}

.film-info {
  padding: 10px;
}

.film-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 5px;
}

.film-score {
  color: #f7ba2a;
  font-size: 22px;
  font-weight: bold;
}

.film-score span {
  font-size: 14px;
  margin-left: 2px;
}

.film-header {
  margin-bottom: 20px;
}

.film-meta {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.film-details {
  margin-left: 20px;
  font-size: 14px;
  color: #606266;
}

.film-type, .film-actor {
  margin-right: 8px;
}

.separator {
  margin: 0 8px;
  color: #dcdfe6;
}

.showtimes-table {
  margin-top: 20px;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.empty-state {
  padding: 40px 0;
  text-align: center;
}

@media (max-width: 768px) {
  .cinema-header {
    flex-direction: column;
  }
  
  .cinema-avatar {
    margin-bottom: 15px;
    margin-right: 0;
  }
  
  .films-grid {
    justify-content: center;
  }
  
  .film-card {
    width: 160px;
  }
  
  .film-poster {
    height: 220px;
  }
}
</style>