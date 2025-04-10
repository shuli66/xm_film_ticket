<template>
  <div class="map-container">
    <div id="map-container" ref="mapContainer" :style="{ height: height + 'px' }"></div>
    <div v-if="showSearch" class="search-box">
      <el-input
        v-model="searchKeyword"
        placeholder="请输入地址"
        clearable
        @keyup.enter="searchAddress"
      >
        <template #append>
          <el-button @click="searchAddress">搜索</el-button>
        </template>
      </el-input>
      <div v-if="searchResults.length > 0" class="search-results">
        <div 
          v-for="(result, index) in searchResults" 
          :key="index" 
          class="search-result-item"
          @click="selectAddress(result)"
        >
          <span>{{ result.title }}</span>
          <span class="address">{{ result.address }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue';
import { ElMessage } from 'element-plus';

const props = defineProps({
  // 地图高度
  height: {
    type: Number,
    default: 400
  },
  // 是否显示搜索框
  showSearch: {
    type: Boolean,
    default: true
  },
  // 默认中心点
  center: {
    type: Object,
    default: () => ({ lng: 116.404, lat: 39.915 })
  },
  // 默认缩放级别
  zoom: {
    type: Number,
    default: 15
  },
  // 默认标记点
  defaultMarker: {
    type: Object,
    default: null
  },
  // 是否允许点击地图选择位置
  selectable: {
    type: Boolean,
    default: true
  }
});

const emit = defineEmits(['select-address']);

const mapContainer = ref(null);
const map = ref(null);
const searchKeyword = ref('');
const searchResults = ref([]);
let localSearch = null;
let currentMarker = null;

// 动态加载百度地图脚本
const loadBaiduMapScript = () => {
  return new Promise((resolve, reject) => {
    if (window.BMap) {
      resolve(window.BMap);
      return;
    }
    
    const script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = `https://api.map.baidu.com/api?v=3.0&ak=rmDZ0gPaWjBfUdkoewMXxHlbWyaEAW7E&callback=initBaiduMap`;
    
    // 定义全局回调函数
    window.initBaiduMap = () => {
      resolve(window.BMap);
    };
    
    script.onerror = reject;
    document.head.appendChild(script);
  });
};

// 初始化地图
const initMap = async () => {
  try {
    await loadBaiduMapScript();
    
    // 创建地图实例
    map.value = new BMap.Map(mapContainer.value);
    
    // 设置中心点和缩放级别
    const centerPoint = new BMap.Point(props.center.lng, props.center.lat);
    map.value.centerAndZoom(centerPoint, props.zoom);
    
    // 添加控件
    map.value.addControl(new BMap.NavigationControl());
    map.value.addControl(new BMap.ScaleControl());
    map.value.enableScrollWheelZoom();
    
    // 如果有默认标记，则添加标记
    if (props.defaultMarker) {
      addMarker(new BMap.Point(props.defaultMarker.lng, props.defaultMarker.lat), props.defaultMarker.title);
    }
    
    // 初始化本地搜索
    localSearch = new BMap.LocalSearch(map.value, {
      renderOptions: { map: map.value, panel: null },
      onSearchComplete: (results) => {
        handleSearchComplete(results);
      }
    });
    
    // 如果允许点击选择，添加点击事件
    if (props.selectable) {
      map.value.addEventListener('click', (e) => {
        const point = e.point;
        addMarker(point);
        
        // 获取点击位置的地址信息
        const geoCoder = new BMap.Geocoder();
        geoCoder.getLocation(point, (result) => {
          if (result) {
            const address = {
              title: result.address,
              address: result.address,
              point: {
                lng: point.lng,
                lat: point.lat
              }
            };
            emit('select-address', address);
          }
        });
      });
    }
  } catch (error) {
    console.error('加载百度地图失败', error);
    ElMessage.error('加载地图失败，请刷新页面重试');
  }
};

// 搜索地址
const searchAddress = () => {
  if (!searchKeyword.value) {
    ElMessage.warning('请输入搜索关键词');
    return;
  }
  
  searchResults.value = [];
  localSearch.search(searchKeyword.value);
};

// 处理搜索结果
const handleSearchComplete = (results) => {
  if (!results) {
    searchResults.value = [];
    ElMessage.info('未找到相关地点');
    return;
  }
  
  // 获取所有搜索结果
  searchResults.value = [];
  
  // 检查返回的结果类型并适当处理
  // 处理多种可能的结果格式
  try {
    // 尝试直接从结果中获取POI信息
    if (results.getCurrentNumPois && results.getCurrentNumPois() > 0) {
      for (let i = 0; i < results.getCurrentNumPois(); i++) {
        const poi = results.getPoi(i);
        if (poi) {
          searchResults.value.push({
            title: poi.title,
            address: poi.address,
            point: {
              lng: poi.point.lng,
              lat: poi.point.lat
            }
          });
        }
      }
    } 
    // 尝试遍历结果集
    else if (results.getNumPois && results.getNumPois() > 0) {
      const pois = results.getPois();
      pois.forEach(poi => {
        searchResults.value.push({
          title: poi.title,
          address: poi.address,
          point: {
            lng: poi.point.lng,
            lat: poi.point.lat
          }
        });
      });
    }
    // 尝试从多种结果集中获取
    else {
      for (let i = 0; i < results.getNumResults(); i++) {
        if (results.getPoi(i)) {
          const poi = results.getPoi(i);
          searchResults.value.push({
            title: poi.title || poi.name,
            address: poi.address,
            point: {
              lng: poi.point.lng,
              lat: poi.point.lat
            }
          });
        }
      }
    }
  } catch (e) {
    console.error('解析搜索结果时出错:', e);
  }
  
  // 如果没有解析到结果，尝试直接遍历
  if (searchResults.value.length === 0) {
    try {
      // 百度地图可能返回多个结果数组
      for (let i = 0; i < results.getNumResults(); i++) {
        const result = results.getResults()[i];
        if (result && result.getPoi) {
          const numPois = result.getNumPois();
          for (let j = 0; j < numPois; j++) {
            const poi = result.getPoi(j);
            searchResults.value.push({
              title: poi.title || poi.name,
              address: poi.address,
              point: {
                lng: poi.point.lng,
                lat: poi.point.lat
              }
            });
          }
        }
      }
    } catch (e) {
      console.error('尝试备用方法解析搜索结果时出错:', e);
    }
  }
  
  if (searchResults.value.length === 0) {
    ElMessage.info('未找到相关地点或结果解析失败');
  }
};

// 选择地址
const selectAddress = (result) => {
  const point = new BMap.Point(result.point.lng, result.point.lat);
  map.value.centerAndZoom(point, 18);
  addMarker(point, result.title);
  searchResults.value = [];
  emit('select-address', result);
};

// 添加标记
const addMarker = (point, title) => {
  // 移除现有标记
  if (currentMarker) {
    map.value.removeOverlay(currentMarker);
  }
  
  // 添加新标记
  currentMarker = new BMap.Marker(point);
  map.value.addOverlay(currentMarker);
  
  // 如果有标题，添加信息窗口
  if (title) {
    const infoWindow = new BMap.InfoWindow(title);
    currentMarker.addEventListener('click', () => {
      currentMarker.openInfoWindow(infoWindow);
    });
  }
  
  // 居中显示
  map.value.panTo(point);
};

// 暴露方法供父组件调用
defineExpose({
  setCenter: (lng, lat, zoom = 18) => {
    if (map.value) {
      const point = new BMap.Point(lng, lat);
      map.value.centerAndZoom(point, zoom);
      addMarker(point);
    }
  },
  search: (keyword) => {
    searchKeyword.value = keyword;
    searchAddress();
  }
});

// 监听属性变化
watch(() => props.defaultMarker, (newVal) => {
  if (newVal && map.value) {
    const point = new BMap.Point(newVal.lng, newVal.lat);
    addMarker(point, newVal.title);
  }
}, { deep: true });

onMounted(() => {
  nextTick(() => {
    initMap();
  });
});
</script>

<style scoped>
.map-container {
  position: relative;
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.search-box {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 300px;
  z-index: 1000;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.search-results {
  max-height: 300px;
  overflow-y: auto;
  background-color: #fff;
  border-radius: 0 0 4px 4px;
  margin-top: 2px;
}

.search-result-item {
  padding: 10px 15px;
  cursor: pointer;
  border-bottom: 1px solid #ebeef5;
}

.search-result-item:hover {
  background-color: #f5f7fa;
}

.search-result-item .address {
  font-size: 12px;
  color: #909399;
  display: block;
  margin-top: 5px;
}
</style> 