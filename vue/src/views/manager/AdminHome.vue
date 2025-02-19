<template>
  <div>
    <div style="display: flex; grid-gap: 10px">
      <div style="flex: 1; display: flex; align-items: center; height: 120px" class="card">
        <div style="flex: 1; text-align: center">
          <img src="@/assets/imgs/电影数量.png" alt="" style="width: 80px; height: 80px">
        </div>
        <div style="flex: 1">
          <div style="margin-bottom: 10px; font-size: 20px">电影数量</div>
          <div style="font-size: 20px; font-weight: bold">{{ data.baseData.filmNum }}</div>
        </div>
      </div>
      <div style="flex: 1; display: flex; align-items: center; height: 120px" class="card">
        <div style="flex: 1; text-align: center">
          <img src="@/assets/imgs/入驻影院.png" alt="" style="width: 80px; height: 80px">
        </div>
        <div style="flex: 1">
          <div style="margin-bottom: 10px; font-size: 20px">入驻影院</div>
          <div style="font-size: 20px; font-weight: bold">{{ data.baseData.cinemaNum }}</div>
        </div>
      </div>
      <div style="flex: 1; display: flex; align-items: center; height: 120px" class="card">
        <div style="flex: 1; text-align: center">
          <img src="@/assets/imgs/今日票房.png" alt="" style="width: 80px; height: 80px">
        </div>
        <div style="flex: 1">
          <div style="margin-bottom: 10px; font-size: 20px">今日票房</div>
          <div style="font-size: 20px; font-weight: bold">{{ (data.baseData.todayPrice * 1).toFixed(2) }}</div>
        </div>
      </div>
      <div style="flex: 1; display: flex; align-items: center; height: 120px" class="card">
        <div style="flex: 1; text-align: center">
          <img src="@/assets/imgs/总计金额.png" alt="" style="width: 80px; height: 80px">
        </div>
        <div style="flex: 1">
          <div style="margin-bottom: 10px; font-size: 20px">总计票房</div>
          <div style="font-size: 20px; font-weight: bold">{{ (data.baseData.totalPrice * 1).toFixed(2) }}</div>
        </div>
      </div>
    </div>
    <div style="margin-top: 10px; height: 400px" class="card" id="line"></div>
    <div style="margin-top: 10px; display: flex; grid-gap: 10px">
      <div style="flex: 2; height: 400px" class="card" id="pie"></div>
      <div style="flex: 3; height: 400px; padding-top: 50px" class="card" id="bar"></div>
    </div>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import * as echarts from "echarts";

const data = reactive({
  baseData: {}
})

const loadBaseData = () => {
  request.get('/statistics/base').then(res => {
    if (res.code === '200') {
      data.baseData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadBaseData()

const loadLine = () => {
  request.get('/statistics/line').then(res => {
    if (res.code === '200') {
      let chartDom = document.getElementById('line')
      let myChart = echarts.init(chartDom)
      lineOptions.xAxis.data = res.data.xAxis
      lineOptions.series[0].data = res.data.yAxis
      myChart.setOption(lineOptions)
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadBar = () => {
  request.get('/statistics/bar').then(res => {
    if (res.code === '200') {
      let chartDom = document.getElementById('bar')
      let myChart = echarts.init(chartDom)
      barOptions.xAxis.data = res.data.xAxis
      barOptions.series[0].data = res.data.yAxis
      myChart.setOption(barOptions)
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadPie = () => {
  request.get('/statistics/pie').then(res => {
    if (res.code === '200') {
      let chartDom = document.getElementById('pie')
      let myChart = echarts.init(chartDom)
      pieOptions.series[0].data = res.data
      myChart.setOption(pieOptions)
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadLine()
loadBar()
loadPie()
// 平滑折线图
let lineOptions = {
  title: {
    text: '近一周每日票房变化折线图',
    subtext: '统计维度：最近一周',
    left: 'center'
  },
  legend: {
    data: [],
    template:""
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  tooltip: {
    trigger: 'item'
  },
  xAxis: {
    name: '日期',
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    name: '票房金额',
    type: 'value'
  },
  series: [
    {
      name: '电影票房金额',
      data: [820, 932, 901, 934, 1290, 1330, 1320],
      type: 'line',
      smooth: true,
      markLine: {
        data: [{ type: 'average', name: '最近7天电影票房金额平均值' }]
      },
      markPoint: {
        data: [
          { type: 'max', name: '最大值' },
          { type: 'min', name: '最小值' }
        ]
      },
    },
  ]
};
// 柱状图
let barOptions = {
  title: {
    text: '不同类型电影总票房柱状图', // 主标题
    subtext: '统计维度：电影类型', // 副标题
    left: 'center'
  },
  grid : {   // ---------------------------增加这个属性，bottom就是距离底部的距离
    bottom : '30%'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'], // 示例数据：统计的维度（横坐标）
    name: '电影类型',
    axisLabel: {
      show: true, // 是否显示刻度标签，默认显示
      interval: 0, // 坐标轴刻度标签的显示间隔，在类目轴中有效；默认会采用标签不重叠的策略间隔显示标签；可以设置成0强制显示所有标签；如果设置为1，表示『隔一个标签显示一个标签』，如果值为2，表示隔两个标签显示一个标签，以此类推。
      rotate: -60, // 刻度标签旋转的角度，在类目轴的类目标签显示不下的时候可以通过旋转防止标签之间重叠；旋转的角度从-90度到90度
      inside: false, // 刻度标签是否朝内，默认朝外
      margin: 6, // 刻度标签与轴线之间的距离
    },
  },
  yAxis: {
    type: 'value',
    name: '票房金额',
  },
  tooltip: {
    trigger: 'item',
  },
  series: [
    {
      data: [120, 200, 150, 80, 70, 110, 130], // 示例数据：横坐标维度对应的值（纵坐标）
      type: 'bar',
      itemStyle: {
        normal: {
          color: function () {
            return "#" + Math.floor(Math.random() * (256 * 256 * 256 - 1)).toString(16);
          }
        },
      },
    }
  ]
};
let pieOptions = {
  title: {
    text: '不同类型电影数量统计图', // 主标题
    subtext: '统计维度：电影类型', // 副标题
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b} : {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '数量占比', // 鼠标移上去显示内容
      type: 'pie',
      radius: '50%',
      center: ['50%', '60%'],
      data: [
        {value: 1048, name: '瑞幸咖啡'}, // 示例数据：name表示维度，value表示对应的值
        {value: 735, name: '雀巢咖啡'},
        {value: 580, name: '星巴克咖啡'},
        {value: 484, name: '栖巢咖啡'},
        {value: 300, name: '小武哥咖啡'}
      ]
    }
  ]
}
</script>