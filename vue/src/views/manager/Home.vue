<template>
  <div class="container">
    <!-- 欢迎卡片 -->
    <div class="welcome-card">
      <div class="welcome-message">
        <h1>您好，{{ data.user?.name }}！</h1>
        <p>欢迎使用本系统！</p>
      </div>
      <div class="welcome-stats" v-if="data.user?.role === 'CINEMA' && isVerified">
        <div class="stat-item">
          <div class="stat-number">{{ formatPrice(data.baseStats.todayPrice || 0) }}</div>
          <div class="stat-label">今日票房(元)</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ formatPrice(data.baseStats.totalPrice || 0) }}</div>
          <div class="stat-label">累计票房(元)</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ data.orderStats.orderCount || 0 }}</div>
          <div class="stat-label">累计订单数</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">{{ data.orderStats.pendingCount || 0 }}</div>
          <div class="stat-label">待取票</div>
        </div>
      </div>
      
      <!-- 未审核状态提示 -->
      <div v-if="data.user?.role === 'CINEMA' && !isVerified" class="verification-alert">
        <el-alert
          title="您的影院账号尚未完成认证"
          type="warning"
          :closable="false"
          show-icon
        >
          <div class="verification-content">
            <p>请完善以下信息以便管理员进行审核，审核通过后将开放数据统计和全部功能。</p>
            <el-button type="primary" @click="navigateToVerification">前往完善认证资料</el-button>
          </div>
        </el-alert>
      </div>
    </div>

    <div class="content-wrapper">
      <!-- 左侧：系统公告 -->
      <div class="announcement-section">
        <div class="announcement-header">
          <h2>系统公告</h2>
          <span class="announcement-count">{{ data.noticeData.length }}条公告</span>
        </div>
        <el-timeline class="timeline">
          <el-timeline-item
              v-for="(item, index) in data.noticeData"
              :key="index"
              :timestamp="item.time"
              :type="getNoticeType(index)"
              :color="getNoticeColor(index)"
              class="announcement-item"
          >
            <div class="announcement-box">
              <div class="announcement-content">{{ item.content }}</div>
              <div class="announcement-footer">
                <span class="announcement-time">{{ formatTime(item.time) }}</span>
                <el-tag 
                  size="small" 
                  :type="index % 4 === 0 ? 'danger' : index % 3 === 0 ? 'warning' : index % 2 === 0 ? 'success' : 'info'"
                  effect="dark"
                >
                  {{ index % 4 === 0 ? '重要' : index % 3 === 0 ? '系统' : index % 2 === 0 ? '功能' : '常规' }}
                </el-tag>
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>
      </div>

      <!-- 右侧：票务数据可视化 (仅影院管理员可见且已审核通过) -->
      <div v-if="data.user?.role === 'CINEMA' && isVerified" class="visualization-section">
        <div class="dashboard-header">
          <h2>票务数据分析</h2>
          <el-select v-model="data.timeRange" placeholder="选择时间范围" size="small">
            <el-option label="最近7天" value="7" />
            <el-option label="最近30天" value="30" />
            <el-option label="最近90天" value="90" />
          </el-select>
        </div>

        <!-- 票房趋势图 -->
        <div class="chart-container">
          <div class="chart-title">
            <i class="el-icon-data-line"></i>
            票房趋势
          </div>
          <div class="chart-wrapper" ref="lineChartRef"></div>
        </div>

        <!-- 热门电影排行 -->
        <div class="chart-container">
          <div class="chart-title">
            <i class="el-icon-film"></i>
            热门电影排行
          </div>
          <div class="ranking-list">
            <div v-for="(item, index) in data.filmRanking" :key="index" class="ranking-item">
              <div class="ranking-index" :class="`rank-${index + 1}`">{{ index + 1 }}</div>
              <div class="ranking-info">
                <div class="ranking-title">{{ item.filmName }}</div>
                <div class="ranking-progress">
                  <div class="progress-bar" :style="{width: `${item.percentage}%`}"></div>
                </div>
              </div>
              <div class="ranking-value">{{ formatPrice(item.sales) }}</div>
            </div>
            <div v-if="!data.filmRanking.length" class="empty-data">
              暂无电影销售数据
            </div>
          </div>
        </div>

        <!-- 票务订单状态分布 -->
        <div class="chart-container">
          <div class="chart-title">
            <i class="el-icon-pie-chart"></i>
            订单状态分布
          </div>
          <div class="chart-wrapper" ref="pieChartRef"></div>
        </div>
      </div>
      
      <!-- 右侧：未审核时展示的引导内容 -->
      <div v-if="data.user?.role === 'CINEMA' && !isVerified" class="verification-guide">
        <div class="guide-header">
          <h2>认证指南</h2>
        </div>
        
        <div class="guide-container">
          <div class="guide-step">
            <div class="step-number">1</div>
            <div class="step-content">
              <h3>完善影院资料</h3>
              <p>提交负责人信息、营业执照、身份证等认证资料</p>
            </div>
          </div>
          
          <div class="guide-arrow"></div>
          
          <div class="guide-step">
            <div class="step-number">2</div>
            <div class="step-content">
              <h3>等待审核</h3>
              <p>管理员将在1-3个工作日内完成审核</p>
            </div>
          </div>
          
          <div class="guide-arrow"></div>
          
          <div class="guide-step">
            <div class="step-number">3</div>
            <div class="step-content">
              <h3>开始使用</h3>
              <p>审核通过后即可使用全部功能</p>
            </div>
          </div>
        </div>
        
        <div class="verification-action">
          <el-button type="primary" size="large" @click="navigateToVerification">
            <i class="el-icon-s-claim"></i> 立即前往完善认证资料
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, watch, computed } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import * as echarts from 'echarts';
import { useRouter } from 'vue-router';

const router = useRouter();
const lineChartRef = ref(null);
const pieChartRef = ref(null);
let lineChart = null;
let pieChart = null;

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  noticeData: [],
  timeRange: '7',
  baseStats: {
    todayPrice: 0,
    totalPrice: 0
  },
  orderStats: {
    orderCount: 0,
    pendingCount: 0
  },
  lineChartData: {
    xAxis: [],
    yAxis: []
  },
  pieChartData: [],
  filmRanking: []
})

// 计算影院是否已经通过审核
const isVerified = computed(() => {
  if (data.user?.role !== 'CINEMA') return true;
  
  // 先检查本地存储状态
  let localVerified = data.user.status === '审核通过' || data.user.status === 'NORMAL';
  
  // 如果本地状态是未审核，则从服务器获取最新状态
  if (!localVerified) {
    refreshUserStatus();
  }
  
  return localVerified;
});

// 从服务器获取最新的用户状态
const refreshUserStatus = () => {
  if (!data.user || !data.user.id) return;
  
  request.get(`/user/detail/${data.user.id}`).then(res => {
    if (res.code === '200' && res.data) {
      // 如果服务器上的状态与本地不一致，更新本地状态
      if (res.data.status !== data.user.status) {
        data.user = res.data;
        localStorage.setItem('xm-user', JSON.stringify(data.user));
        ElMessage.success('用户状态已更新，请刷新页面查看最新功能');
      }
    }
  }).catch(err => {
    console.error('获取用户状态失败', err);
  });
};

// 导航至认证页面
const navigateToVerification = () => {
  router.push('/manager/certificate');
};

// 格式化价格，添加千位分隔符
const formatPrice = (price) => {
  return price.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 格式化时间为更易读的格式
const formatTime = (time) => {
  if (!time) return '';
  const date = new Date(time);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
}

// 根据索引生成不同类型的时间线项目
const getNoticeType = (index) => {
  const types = ['primary', 'success', 'warning', 'danger', 'info'];
  return types[index % types.length];
}

// 根据索引生成不同颜色的时间线项目
const getNoticeColor = (index) => {
  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399'];
  return colors[index % colors.length];
}

// 加载基础统计数据
const loadBaseStats = () => {
  if (data.user?.role !== 'CINEMA' || !isVerified.value) return;
  
  const cinemaId = data.user.id;
  request.get('/statistics/cinemaBase', {
    params: { cinemaId }
  }).then(res => {
    if (res.code === '200') {
      data.baseStats = res.data;
    } else {
      ElMessage.error(res.msg);
    }
  });
}

// 加载订单统计数据
const loadOrderStats = () => {
  if (data.user?.role !== 'CINEMA' || !isVerified.value) return;
  
  const cinemaId = data.user.id;
  request.get('/orders/selectAll', {
    params: {
      cinemaId: cinemaId
    }
  }).then(res => {
    if (res.code === '200') {
      const orders = res.data || [];
      // 总订单数
      data.orderStats.orderCount = orders.length;
      // 待取票数量
      data.orderStats.pendingCount = orders.filter(o => o.status === '待取票').length;
      
      // 处理电影排行数据
      const filmSales = {};
      orders.forEach(order => {
        if (order.status !== '已退票' && (order.status === '已取票' || order.status === '待取票')) {
          if (!filmSales[order.filmId]) {
            filmSales[order.filmId] = {
              filmId: order.filmId,
              filmName: order.filmName,
              sales: 0
            };
          }
          filmSales[order.filmId].sales += order.price;
        }
      });
      
      // 转换为数组并排序
      const rankingList = Object.values(filmSales).sort((a, b) => b.sales - a.sales).slice(0, 5);
      
      // 计算百分比
      if (rankingList.length > 0) {
        const maxSales = rankingList[0].sales;
        rankingList.forEach(item => {
          item.percentage = (item.sales / maxSales) * 100;
        });
      }
      
      data.filmRanking = rankingList;
      
      // 处理订单状态分布数据
      const statusCount = {};
      orders.forEach(order => {
        if (!statusCount[order.status]) {
          statusCount[order.status] = 0;
        }
        statusCount[order.status]++;
      });
      
      // 转换为饼图数据
      data.pieChartData = Object.keys(statusCount).map(key => ({
        name: key,
        value: statusCount[key]
      }));
      
      // 更新图表
      initPieChart();
    }
  });
}

// 加载票房趋势图数据
const loadLineChartData = () => {
  if (data.user?.role !== 'CINEMA' || !isVerified.value) return;
  
  const cinemaId = data.user.id;
  request.get('/statistics/cinemaLine', {
    params: { cinemaId }
  }).then(res => {
    if (res.code === '200') {
      data.lineChartData = res.data;
      initLineChart();
    } else {
      ElMessage.error(res.msg);
    }
  });
}

// 初始化线图表
const initLineChart = () => {
  if (!lineChartRef.value) return;
  
  if (!lineChart) {
    lineChart = echarts.init(lineChartRef.value);
  }
  
  const option = {
    tooltip: {
      trigger: 'axis',
      formatter: '{b}<br/>{a}: {c} 元'
    },
    xAxis: {
      type: 'category',
      data: data.lineChartData.xAxis || [],
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      name: '金额 (元)'
    },
    series: [
      {
        name: '票房',
        type: 'line',
        data: data.lineChartData.yAxis || [],
        smooth: true,
        symbol: 'circle',
        symbolSize: 8,
        itemStyle: {
          color: '#3f87e8'
        },
        lineStyle: {
          width: 3
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(63, 135, 232, 0.5)' },
            { offset: 1, color: 'rgba(63, 135, 232, 0.05)' }
          ])
        }
      }
    ],
    grid: {
      left: '3%',
      right: '4%',
      bottom: '15%',
      top: '15%',
      containLabel: true
    }
  };
  
  lineChart.setOption(option);
}

// 初始化饼图表
const initPieChart = () => {
  if (!pieChartRef.value) return;
  
  if (!pieChart) {
    pieChart = echarts.init(pieChartRef.value);
  }
  
  const colors = ['#3f87e8', '#67C23A', '#E6A23C', '#F56C6C', '#909399'];
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'horizontal',
      bottom: '0',
      data: data.pieChartData.map(item => item.name)
    },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        center: ['50%', '45%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '16',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: data.pieChartData,
        color: colors
      }
    ]
  };
  
  pieChart.setOption(option);
}

watch(() => data.timeRange, () => {
  if (isVerified.value) {
  loadLineChartData();
  }
});

onMounted(() => {
  loadNotice();
  
  if (data.user?.role === 'CINEMA' && isVerified.value) {
    loadBaseStats();
    loadOrderStats();
    loadLineChartData();
    
    // 监听窗口大小变化，重绘图表
    window.addEventListener('resize', () => {
      if (lineChart) lineChart.resize();
      if (pieChart) pieChart.resize();
    });
  }
  
  refreshUserStatus();
});
</script>

<style scoped>
/* 整体容器 */
.container {
  background-color: #f5f7fa;
  min-height: 100vh;
  padding: 60px 30px;
  display: flex;
  flex-direction: column;
  align-items: center;
  font-family: 'Arial', sans-serif;
}

/* 欢迎卡片样式 */
.welcome-card {
  background: linear-gradient(135deg, #3f87e8, #2a6fc5);
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(47, 128, 237, 0.2);
  text-align: center;
  margin-bottom: 40px;
  width: 100%;
  max-width: 1800px;
  position: relative;
  overflow: hidden;
}

.welcome-card::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 70%);
  opacity: 0.7;
  pointer-events: none;
}

.welcome-message h1 {
  font-size: 32px;
  font-weight: 700;
  color: white;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.welcome-message p {
  font-size: 20px;
  margin-top: 15px;
  color: rgba(255, 255, 255, 0.85);
}

/* 欢迎卡片的统计数据样式 */
.welcome-stats {
  display: flex;
  justify-content: space-around;
  margin-top: 25px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 15px;
}

.stat-item {
  text-align: center;
  padding: 10px 20px;
}

.stat-number {
  font-size: 28px;
  font-weight: 700;
  color: white;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

/* 内容部分布局 */
.content-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  width: 100%;
  max-width: 1800px;
  gap: 40px;
}

.announcement-section {
  width: 100%;
  max-width: 900px;
  background: #ffffff;
  border-radius: 16px;
  padding: 30px 40px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  flex: 1;
}

.visualization-section {
  width: 100%;
  max-width: 900px;
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.dashboard-header h2 {
  font-size: 24px;
  color: #333;
  font-weight: 600;
  margin: 0;
  padding-left: 15px;
  position: relative;
}

.dashboard-header h2::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 22px;
  background: #3f87e8;
  border-radius: 2px;
}

.chart-container {
  background: #ffffff;
  border-radius: 16px;
  padding: 20px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
  margin-bottom: 20px;
}

.chart-title {
  font-size: 18px;
  color: #333;
  font-weight: 600;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.chart-wrapper {
  height: 300px;
  width: 100%;
}

/* 排行榜样式 */
.ranking-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.ranking-item {
  display: flex;
  align-items: center;
  gap: 15px;
}

.ranking-index {
  width: 26px;
  height: 26px;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: bold;
  color: white;
  background-color: #909399;
}

.rank-1 {
  background-color: #f56c6c;
}

.rank-2 {
  background-color: #e6a23c;
}

.rank-3 {
  background-color: #409eff;
}

.ranking-info {
  flex: 1;
}

.ranking-title {
  font-weight: 500;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 250px;
}

.ranking-progress {
  height: 6px;
  background-color: #ebeef5;
  border-radius: 3px;
  overflow: hidden;
}

.progress-bar {
  height: 100%;
  background: linear-gradient(to right, #3f87e8, #79bbff);
  border-radius: 3px;
}

.ranking-value {
  font-weight: 600;
  color: #f56c6c;
  min-width: 80px;
  text-align: right;
}

.empty-data {
  text-align: center;
  color: #909399;
  padding: 40px 0;
}

.announcement-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  position: relative;
}

.announcement-header h2 {
  font-size: 24px;
  color: #333;
  font-weight: 600;
  margin: 0;
  padding-left: 15px;
  position: relative;
}

.announcement-header h2::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 22px;
  background: #3f87e8;
  border-radius: 2px;
}

.announcement-count {
  font-size: 14px;
  color: #909399;
  background: #f5f7fa;
  padding: 4px 12px;
  border-radius: 12px;
}

/* 每个公告的框样式 */
.timeline {
  padding: 10px 0;
}

.announcement-item {
  margin-bottom: 20px;
}

.announcement-box {
  background-color: #f9fafc;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 10px;
  width: 100%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
  transition: all 0.3s ease;
  border-left: 4px solid #3f87e8;
}

.announcement-content {
  font-size: 16px;
  color: #333;
  line-height: 1.6;
  margin-bottom: 15px;
}

.announcement-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.announcement-time {
  color: #909399;
}

/* 鼠标悬停效果 */
.announcement-box:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  background-color: #fff;
}

/* 时间线样式定制 */
:deep(.el-timeline-item__node) {
  box-shadow: 0 0 0 4px rgba(63, 135, 232, 0.2);
}

:deep(.el-timeline-item__timestamp) {
  color: #909399;
  margin-top: 8px;
}

:deep(.el-timeline-item__tail) {
  border-left: 2px dashed #dcdfe6;
}

:deep(.el-tag--dark) {
  border: none;
  font-weight: 500;
  padding: 0 8px;
  height: 22px;
  line-height: 22px;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .content-wrapper {
    flex-direction: column;
  }
  
  .announcement-section,
  .visualization-section {
    max-width: none;
  }
}

@media (max-width: 768px) {
  .container {
    padding: 20px;
  }

  .welcome-card {
    padding: 25px;
    margin-bottom: 25px;
  }
  
  .welcome-stats {
    flex-wrap: wrap;
  }
  
  .stat-item {
    width: 50%;
  }

  .announcement-section,
  .chart-container {
    padding: 20px;
  }

  .announcement-header h2,
  .dashboard-header h2 {
    font-size: 20px;
  }
  
  .announcement-content {
    font-size: 14px;
  }
  
  .announcement-footer {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .chart-wrapper {
    height: 250px;
  }
}

/* 认证提示样式 */
.verification-alert {
  margin-top: 20px;
  width: 100%;
}

.verification-content {
  margin-top: 10px;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 10px;
}

.verification-content p {
  margin: 0;
  font-size: 14px;
}

/* 认证指南样式 */
.verification-guide {
  flex: 1;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 20px;
  margin-left: 20px;
}

.guide-header {
  margin-bottom: 30px;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 15px;
}

.guide-header h2 {
  margin: 0;
  font-size: 20px;
  color: #333;
  display: flex;
  align-items: center;
}

.guide-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 0;
}

.guide-step {
  display: flex;
  align-items: center;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 8px;
  flex: 1;
  max-width: 28%;
}

.step-number {
  width: 40px;
  height: 40px;
  background: #409EFF;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 18px;
  margin-right: 15px;
}

.step-content h3 {
  margin: 0 0 5px 0;
  font-size: 16px;
  color: #333;
}

.step-content p {
  margin: 0;
  font-size: 14px;
  color: #666;
}

.guide-arrow {
  width: 20px;
  height: 20px;
  border-top: 2px solid #ddd;
  border-right: 2px solid #ddd;
  transform: rotate(45deg);
  margin: 0 10px;
}

.verification-action {
  margin-top: 40px;
  display: flex;
  justify-content: center;
}
</style>
