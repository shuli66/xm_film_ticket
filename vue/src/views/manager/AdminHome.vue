<template>
  <div>
    <!-- 顶部数据卡片，增加美观的阴影和过渡效果 -->
    <div class="data-cards">
      <div class="data-card" v-loading="data.loading">
        <div class="icon-wrapper">
          <img src="@/assets/imgs/电影数量.png" alt="">
        </div>
        <div class="card-content">
          <div class="card-title">电影数量</div>
          <div class="card-value">{{ data.baseData.filmNum || 0 }}</div>
        </div>
      </div>
      <div class="data-card" v-loading="data.loading">
        <div class="icon-wrapper">
          <img src="@/assets/imgs/入驻影院.png" alt="">
        </div>
        <div class="card-content">
          <div class="card-title">入驻影院</div>
          <div class="card-value">{{ data.baseData.cinemaNum || 0 }}</div>
        </div>
      </div>
      <div class="data-card" v-loading="data.loading">
        <div class="icon-wrapper">
          <img src="@/assets/imgs/今日票房.png" alt="">
        </div>
        <div class="card-content">
          <div class="card-title">今日票房</div>
          <div class="card-value">{{ (data.baseData.todayPrice * 1 || 0).toFixed(2) }}</div>
        </div>
      </div>
      <div class="data-card" v-loading="data.loading">
        <div class="icon-wrapper">
          <img src="@/assets/imgs/总计金额.png" alt="">
        </div>
        <div class="card-content">
          <div class="card-title">总计票房</div>
          <div class="card-value">{{ (data.baseData.totalPrice * 1 || 0).toFixed(2) }}</div>
        </div>
      </div>
    </div>
    
    <!-- 折线图 - 增加卡片标题和加载状态 -->
    <div class="chart-container" v-loading="data.chartLoading.line">
      <div class="chart-header">
        <h3>近一周每日票房变化</h3>
        <el-tooltip content="刷新数据">
          <el-button type="primary" size="small" circle @click="loadLine" :icon="Refresh"></el-button>
        </el-tooltip>
      </div>
      <div class="chart-content" id="line"></div>
    </div>
    
    <!-- 下方图表分布 -->
    <div class="charts-row">
      <!-- 饼图 - 类型统计 -->
      <div class="chart-container" v-loading="data.chartLoading.pie">
        <div class="chart-header">
          <h3>电影类型分布</h3>
          <el-tooltip content="刷新数据">
            <el-button type="primary" size="small" circle @click="loadPie" :icon="Refresh"></el-button>
          </el-tooltip>
        </div>
        <div class="chart-content" id="pie"></div>
      </div>
      
      <!-- 柱状图 - 票房统计 -->
      <div class="chart-container" v-loading="data.chartLoading.bar">
        <div class="chart-header">
          <h3>类型票房对比</h3>
          <el-tooltip content="刷新数据">
            <el-button type="primary" size="small" circle @click="loadBar" :icon="Refresh"></el-button>
          </el-tooltip>
        </div>
        <div class="chart-content" id="bar"></div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {reactive, onMounted, onBeforeUnmount} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import * as echarts from "echarts";
import { Refresh } from '@element-plus/icons-vue';

// 主题色配置
const THEME_COLORS = {
  primary: '#409EFF',
  success: '#67C23A',
  warning: '#E6A23C', 
  danger: '#F56C6C',
  info: '#909399',
  textPrimary: '#303133',
  textRegular: '#606266',
  borderColor: '#DCDFE6',
  chartColors: [
    '#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', 
    '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc', '#6173e3'
  ]
};

const data = reactive({
  baseData: {},
  charts: {
    line: null,
    bar: null,
    pie: null
  },
  loading: true,
  chartLoading: {
    line: true,
    bar: true,
    pie: true
  }
});

// 优化图表大小自适应
const resizeCharts = () => {
  Object.values(data.charts).forEach(chart => {
    if (chart) {
      chart.resize();
      
      // 为饼图特别调整
      if (chart === data.charts.pie) {
        // 根据窗口宽度调整饼图的布局
        const width = window.innerWidth;
        if (width < 1200) {
          // 小屏幕时调整布局
          chart.setOption({
            legend: {
              orient: width < 768 ? 'horizontal' : 'vertical',
              top: width < 768 ? 'bottom' : 20,
              left: width < 768 ? 'center' : null,
              right: width < 768 ? null : 10
            },
            series: [{
              center: width < 768 ? ['50%', '40%'] : ['38%', '50%'],
              radius: width < 768 ? ['30%', '60%'] : ['40%', '70%']
            }]
          });
        } else {
          // 大屏幕保持原有布局
          chart.setOption({
            legend: {
              orient: 'vertical',
              top: 20,
              right: 10
            },
            series: [{
              center: ['38%', '50%'],
              radius: ['40%', '70%']
            }]
          });
        }
      }
    }
  });
};

// 封装通用图表初始化函数
const initChart = (id, options, chartKey) => {
  data.chartLoading[chartKey] = true;
  const chartDom = document.getElementById(id);
  if (!chartDom) return null;
  
  if (data.charts[chartKey]) {
    data.charts[chartKey].dispose();
  }
  
  const chart = echarts.init(chartDom);
  chart.setOption(options);
  
  // 添加图表交互事件
  chart.on('click', params => {
    ElMessage.info(`选择了: ${params.name}, 值: ${params.value}`);
  });
  
  data.chartLoading[chartKey] = false;
  return chart;
};

// 添加窗口大小变化监听
onMounted(() => {
  window.addEventListener('resize', resizeCharts);
  
  // 显示加载状态
  data.loading = true;
  
  // 首屏加载基础数据
  loadBaseData().then(() => {
    // 加载图表, 使用链式加载减轻服务器压力
    loadLine()
      .then(() => loadPie())
      .then(() => loadBar())
      .finally(() => {
        data.loading = false;
      });
  });
});

// 释放资源
onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeCharts);
  
  // 销毁图表实例，释放内存
  Object.keys(data.charts).forEach(key => {
    if (data.charts[key]) {
      data.charts[key].dispose();
      data.charts[key] = null;
    }
  });
});

// 将API请求封装为Promise
const loadBaseData = () => {
  return new Promise((resolve, reject) => {
    request.get('/statistics/base').then(res => {
      if (res.code === '200') {
        data.baseData = res.data;
        resolve(res.data);
      } else {
        ElMessage.error(res.msg);
        reject(res.msg);
      }
    }).catch(err => {
      reject(err);
    });
  });
};

const loadLine = () => {
  data.chartLoading.line = true;
  return new Promise((resolve, reject) => {
    request.get('/statistics/line').then(res => {
      if (res.code === '200') {
        // 更新折线图数据
        lineOptions.xAxis.data = res.data.xAxis || [];
        lineOptions.series[0].data = res.data.yAxis || [];
        
        // 计算最大值和最小值，调整Y轴范围
        if (res.data.yAxis && res.data.yAxis.length > 0) {
          const max = Math.max(...res.data.yAxis);
          const min = Math.min(...res.data.yAxis);
          lineOptions.yAxis.max = Math.ceil(max * 1.1); // 留出10%的顶部空间
          lineOptions.yAxis.min = Math.max(0, Math.floor(min * 0.9)); // 底部至少从0开始
        }
        
        // 初始化图表
        data.charts.line = initChart('line', lineOptions, 'line');
        resolve(res.data);
      } else {
        ElMessage.error(res.msg);
        reject(res.msg);
      }
    }).catch(err => {
      reject(err);
    }).finally(() => {
      data.chartLoading.line = false;
    });
  });
};

const loadBar = () => {
  data.chartLoading.bar = true;
  return new Promise((resolve, reject) => {
    request.get('/statistics/bar').then(res => {
      if (res.code === '200') {
        // 处理数据，确保排序和格式正确
        let dataSource = [];
        if (res.data.xAxis && res.data.yAxis) {
          for (let i = 0; i < res.data.xAxis.length; i++) {
            dataSource.push({
              name: res.data.xAxis[i],
              value: res.data.yAxis[i]
            });
          }
          
          // 对数据排序（从大到小）
          dataSource.sort((a, b) => b.value - a.value);
          
          // 限制最多显示8个类别，其他归为"其他"
          if (dataSource.length > 8) {
            const othersValue = dataSource.slice(8).reduce((sum, item) => sum + item.value, 0);
            dataSource = dataSource.slice(0, 8);
            if (othersValue > 0) {
              dataSource.push({ name: '其他类型', value: othersValue });
            }
          }
        }
        
        // 更新柱状图配置
        barOptions.xAxis.data = dataSource.map(item => item.name);
        barOptions.series[0].data = dataSource.map(item => ({
          value: item.value,
          name: item.name,
          itemStyle: {
            color: THEME_COLORS.chartColors[barOptions.xAxis.data.indexOf(item.name) % THEME_COLORS.chartColors.length]
          }
        }));
        
        // 初始化图表
        data.charts.bar = initChart('bar', barOptions, 'bar');
        resolve(dataSource);
      } else {
        ElMessage.error(res.msg);
        reject(res.msg);
      }
    }).catch(err => {
      reject(err);
    }).finally(() => {
      data.chartLoading.bar = false;
    });
  });
};

const loadPie = () => {
  data.chartLoading.pie = true;
  return new Promise((resolve, reject) => {
    request.get('/statistics/pie').then(res => {
      if (res.code === '200') {
        let pieData = res.data || [];
        
        // 移除数量限制，显示所有类型
        // 为每个项目添加自定义样式
        pieData = pieData.map((item, index) => ({
          ...item,
          itemStyle: {
            color: THEME_COLORS.chartColors[index % THEME_COLORS.chartColors.length]
          }
        }));
        
        pieOptions.series[0].data = pieData;
        
        // 初始化图表
        data.charts.pie = initChart('pie', pieOptions, 'pie');
        resolve(pieData);
      } else {
        ElMessage.error(res.msg);
        reject(res.msg);
      }
    }).catch(err => {
      reject(err);
    }).finally(() => {
      data.chartLoading.pie = false;
    });
  });
};

// 平滑折线图配置
let lineOptions = {
  color: THEME_COLORS.chartColors,
  title: {
    show: false
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross',
      label: {
        backgroundColor: THEME_COLORS.primary
      }
    },
    formatter: function(params) {
      const data = params[0];
      return `<div style="font-weight:bold;margin-bottom:5px;">${data.name}</div>
              <div style="display:flex;align-items:center;">
                <span style="display:inline-block;width:10px;height:10px;border-radius:50%;background:${data.color};margin-right:5px;"></span>
                <span>票房: ￥${data.value}</span>
              </div>`;
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    top: '8%',
    containLabel: true
  },
  toolbox: {
    feature: {
      saveAsImage: { title: '保存为图片' },
      dataZoom: { title: '数据缩放' },
      restore: { title: '重置' }
    },
    right: 20
  },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: [],
    axisLabel: {
      formatter: '{value}',
      color: THEME_COLORS.textRegular
    },
    axisLine: {
      lineStyle: {
        color: THEME_COLORS.borderColor
      }
    }
  },
  yAxis: {
    type: 'value',
    name: '票房金额 (元)',
    nameTextStyle: {
      color: THEME_COLORS.textRegular
    },
    axisLabel: {
      formatter: '{value} 元',
      color: THEME_COLORS.textRegular
    },
    splitLine: {
      lineStyle: {
        type: 'dashed',
        color: THEME_COLORS.borderColor
      }
    }
  },
  series: [
    {
      name: '日票房',
      type: 'line',
      smooth: true,
      lineStyle: {
        width: 3,
        shadowColor: 'rgba(0,0,0,0.3)',
        shadowBlur: 10,
        shadowOffsetY: 8
      },
      symbol: 'circle',
      symbolSize: 8,
      showSymbol: true,
      areaStyle: {
        opacity: 0.3,
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: THEME_COLORS.chartColors[0] },
          { offset: 1, color: 'rgba(84,112,198,0.1)' }
        ])
      },
      emphasis: {
        focus: 'series',
        itemStyle: {
          borderWidth: 2,
          shadowBlur: 20
        }
      },
      data: [],
      markLine: {
        silent: true,
        symbol: ['none', 'none'],
        label: { show: true },
        data: [{ type: 'average', name: '平均值' }]
      },
      markPoint: {
        data: [
          { type: 'max', name: '最高值' },
          { type: 'min', name: '最低值' }
        ],
        symbolSize: 50
      }
    }
  ]
};

// 柱状图配置
let barOptions = {
  color: THEME_COLORS.chartColors,
  title: {
    show: false
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'shadow'
    },
    formatter: function(params) {
      const data = params[0];
      return `<div style="font-weight:bold;margin-bottom:5px;">${data.name}</div>
              <div style="display:flex;align-items:center;">
                <span style="display:inline-block;width:10px;height:10px;border-radius:50%;background:${data.color};margin-right:5px;"></span>
                <span>票房: ￥${data.value}</span>
              </div>`;
    }
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '15%',
    top: '8%',
    containLabel: true
  },
  toolbox: {
    feature: {
      saveAsImage: { title: '保存为图片' },
      magicType: {
        type: ['bar', 'line'],
        title: {
          bar: '切换为柱状图',
          line: '切换为折线图'
        }
      },
      restore: { title: '重置' }
    },
    right: 20
  },
  xAxis: {
    type: 'category',
    data: [],
    axisLabel: {
      interval: 0,
      rotate: -30,
      margin: 18,
      color: THEME_COLORS.textRegular,
      fontSize: 12,
      hideOverlap: true
    },
    axisLine: {
      lineStyle: {
        color: THEME_COLORS.borderColor
      }
    }
  },
  yAxis: {
    type: 'value',
    name: '票房金额 (元)',
    nameTextStyle: {
      color: THEME_COLORS.textRegular
    },
    axisLabel: {
      formatter: '{value} 元',
      color: THEME_COLORS.textRegular
    },
    splitLine: {
      lineStyle: {
        type: 'dashed',
        color: THEME_COLORS.borderColor
      }
    }
  },
  series: [
    {
      type: 'bar',
      barMaxWidth: '40%',
      data: [],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0,0,0,0.5)'
        }
      },
      label: {
        show: true,
        position: 'top',
        formatter: '{c} 元'
      }
    }
  ]
};

// 饼图配置
let pieOptions = {
  color: THEME_COLORS.chartColors,
  title: {
    show: false
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} ({d}%)',
    borderWidth: 1,
    padding: 10
  },
  legend: {
    type: 'scroll',
    orient: 'vertical',
    right: 10,
    top: 20,
    bottom: 20,
    textStyle: {
      color: THEME_COLORS.textRegular
    },
    pageButtonItemGap: 5,
    pageButtonPosition: 'end',
    pageFormatter: '{current}/{total}',
    pageIconColor: THEME_COLORS.primary,
    pageIconInactiveColor: THEME_COLORS.borderColor,
    pageIconSize: 15,
    pageTextStyle: {
      color: THEME_COLORS.textRegular
    }
  },
  toolbox: {
    feature: {
      saveAsImage: { title: '保存为图片' },
      restore: { title: '重置' }
    },
    right: 20,
    top: 0
  },
  series: [
    {
      name: '类型分布',
      type: 'pie',
      radius: ['40%', '70%'],
      center: ['38%', '50%'],
      avoidLabelOverlap: true,
      itemStyle: {
        borderColor: '#fff',
        borderWidth: 2
      },
      label: {
        show: true,
        formatter: '{b}: {c} ({d}%)',
        fontSize: 12
      },
      emphasis: {
        label: {
          show: true,
          fontSize: 14,
          fontWeight: 'bold'
        },
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0,0,0,0.5)'
        }
      },
      labelLine: {
        show: true,
        length: 10,
        length2: 10
      },
      data: []
    }
  ]
};
</script>

<style scoped>
.data-cards {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.data-card {
  flex: 1;
  display: flex;
  align-items: center;
  padding: 20px;
  height: 120px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.data-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 25px rgba(0, 0, 0, 0.1);
}

.icon-wrapper {
  width: 80px;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-right: 20px;
}

.icon-wrapper img {
  width: 80px;
  height: 80px;
  object-fit: contain;
}

.card-content {
  flex: 1;
}

.card-title {
  font-size: 16px;
  color: #606266;
  margin-bottom: 15px;
}

.card-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.chart-container {
  background: #fff;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.chart-header h3 {
  font-size: 18px;
  margin: 0;
  color: #303133;
  font-weight: 600;
}

.chart-content {
  width: 100%;
  height: 350px;
}

.charts-row {
  display: flex;
  gap: 20px;
}

.charts-row .chart-container {
  flex: 1;
}

@media (max-width: 1400px) {
  .data-cards, .charts-row {
    flex-direction: column;
  }
  
  .data-card, .charts-row .chart-container {
    width: 100%;
  }
  
  .chart-content {
    height: 300px;
  }
}
</style>