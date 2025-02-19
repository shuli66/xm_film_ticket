<template>
  <div style="width: 70%; margin: 20px auto; display: flex">
    <div style="flex: 1">



      <!-- 为你推荐 -->
      <div style="flex: 1; margin-top: 20px">
        <div style="display: flex; align-items: center">
          <div style="flex: 1; font-size: 22px; color: #2d98f3">为你推荐（{{ data.recommended?.length }}部）</div>
          <div style="width: 80px; text-align: right; color: #2d98f3; cursor: pointer" @click="$router.push('/front/film')">全部 ></div>
        </div>
      </div>
      <div style="margin-top: 20px">
        <el-row :gutter="15">
          <el-col :span="6" v-for="item in data.recommended" style="margin-bottom: 20px; cursor: pointer" @click="navTo('/front/filmDetail?id=' + item.id)">
            <img :src="item.img" alt="" style="width: 100%; height: 260px; border-top-left-radius: 5px; border-top-right-radius: 5px">
            <el-button type="primary" plain style="width: 100%; height: 35px" @click="navTo('/front/filmDetail?id=' + item.id)">购票</el-button>
<!--            <div style="margin-top: 5px; font-size: 18px" class="line1">{{ item.title }}</div>-->
          </el-col>
        </el-row>
      </div>




      <div style="display: flex; align-items: center">
        <div style="flex: 1; font-size: 22px; color: #ef4238">正在热播（{{ data.data1?.length }}部）</div>
        <div style="width: 80px; text-align: right; color: #ef4238; cursor: pointer" @click="$router.push('/front/film')">全部 ></div>
      </div>

      <div style="margin-top: 20px">
        <el-row :gutter="15">
          <el-col :span="6" v-for="item in data.playingData" style="margin-bottom: 20px">
            <!-- 给 img 添加 cursor: pointer，显示鼠标指针效果 -->
            <img :src="item.img" alt="" style="width: 100%; height: 260px; border-top-left-radius: 5px; border-top-right-radius: 5px; cursor: pointer" @click="navTo('/front/filmDetail?id=' + item.id)">
            <!-- 这里保留按钮，按钮会显示在图片下方 -->
            <el-button type="primary" plain style="width: 100%; height: 35px" @click="navTo('/front/filmDetail?id=' + item.id)">购票</el-button>
          </el-col>
        </el-row>
      </div>






      <div style="flex: 1; margin-top: 20px">
        <div style="display: flex; align-items: center">
          <div style="flex: 1; font-size: 22px; color: #2d98f3">即将上映（{{ data.data2?.length }}部）</div>
          <div style="width: 80px; text-align: right; color: #2d98f3; cursor: pointer" @click="$router.push('/front/film')">全部 ></div>
        </div>
      </div>

      <div style="margin-top: 20px">
        <el-row :gutter="15">
          <el-col :span="6" v-for="item in data.noPlayData" style="margin-bottom: 20px; cursor: pointer" @click="navTo('/front/filmDetail?id=' + item.id)">
            <img :src="item.img" alt="" style="width: 100%; height: 260px; border-top-left-radius: 5px; border-top-right-radius: 5px">
            <div style="margin-top: 5px; font-size: 18px" class="line1">{{ item.title }}</div>
            <div style="margin-top: 5px; font-size: 16px; color: orange">{{ item.start }} 上映</div>
          </el-col>
        </el-row>
      </div>
    </div>
    <div style="width: 300px; margin-left: 50px">
      <div style="display: flex; align-items: center">
        <div style="flex: 1; font-size: 22px; color: #ef4238">总票房Top 10</div>
        <div style="width: 100px; text-align: right; color: #ef4238; cursor: pointer" @click="$router.push('/front/charts')">查看完整榜单 ></div>
      </div>
      <div style="margin-top: 20px; border: 1px solid #dddddd; padding: 20px; border-radius: 5px">
        <div style="display: flex; grid-gap: 20px; margin-bottom: 20px">
          <img :src="data.firstTotal.img" alt="" style="width: 80px; height: 80px">
          <div style="flex: 1; width: 0">
            <div style="font-size: 22px" class="line1">{{ data.firstTotal.title }}</div>
            <div style="margin-top: 10px; color: red; font-size: 18px; font-weight: bold">{{ (data.firstTotal.total * 1).toFixed(2) }}元</div>
          </div>
        </div>
        <div style="display: flex; grid-gap: 10px; margin-bottom: 20px" v-for="(item, index) in data.topTotal">
          <div style="width: 15px; font-size: 16px">{{ index + 2 }}</div>
          <div style="flex: 1; cursor: pointer; font-size: 16px" class="line1" @click="navTo('/front/filmDetail?id=' + item.id)">{{ item.title }}</div>
          <div style="width: 70px; text-align: right; color: red">{{ (item.total * 1).toFixed(2) }}元</div>
        </div>
      </div>

      <div style="margin-top: 50px; display: flex; grid-gap: 20px; align-items: center; background-color: #efefef">
        <div style="background-color: red; font-size: 18px; text-align: center; color: white; width: 40px; padding: 10px">今日票房</div>
        <div style="flex: 1">
          <div style="display: flex; color: red; align-items: center">
            <div style="flex: 1; font-size: 26px">{{ data.todayTotal.toFixed(2) }}元</div>
            <div style="width: 100px; cursor: pointer" @click="loadTodayTotal">点击更新</div>
          </div>
          <div style="margin-top: 10px; color: #8c939d">北京时间：{{ data.time }}</div>
        </div>
      </div>

      <div style="display: flex; align-items: center; margin-top: 50px">
        <div style="flex: 1; font-size: 22px; color: #ef4238">总评分Top 10</div>
        <div style="width: 100px; text-align: right; color: #ef4238; cursor: pointer" @click="$router.push('/front/charts')">查看完整榜单 ></div>
      </div>
      <div style="margin-top: 20px; border: 1px solid #dddddd; padding: 20px; border-radius: 5px">
        <div style="display: flex; grid-gap: 20px; margin-bottom: 20px">
          <img :src="data.firstScore.img" alt="" style="width: 80px; height: 80px">
          <div style="flex: 1; width: 0">
            <div style="font-size: 22px" class="line1">{{ data.firstScore.title }}</div>
            <div style="margin-top: 10px; color: orange; font-size: 18px; font-weight: bold">{{ (data.firstScore.score * 1).toFixed(1) }}分</div>
          </div>
        </div>
        <div style="display: flex; grid-gap: 10px; margin-bottom: 20px" v-for="(item, index) in data.topScore">
          <div style="width: 15px; font-size: 16px">{{ index + 2 }}</div>
          <div style="flex: 1; cursor: pointer; font-size: 16px" class="line1" @click="navTo('/front/filmDetail?id=' + item.id)">{{ item.title }}</div>
          <div style="width: 70px; text-align: right; color: darkorange">{{ (item.score * 1).toFixed(1) }}分</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import router from "@/router/index.js";
import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
const data = reactive({
  data1: [], // 已上映的电影
  data2: [], // 待上映的电影
  playingData: [], // 播放中的电影（最多显示8部）
  noPlayData: [], // 待上映电影（最多显示8部）
  topTotal: [], // 总票房排行榜
  firstTotal: {}, // 排行榜第一的电影
  topScore: [], // 高评分排行榜
  firstScore: {}, // 排行榜第一的高评分电影
  todayTotal: 0, // 今日票房总计
  time: null, // 时间信息
  recommended: []  // 用于存储推荐的电影
});

// 加载今日票房总计
const loadTodayTotal = () => {
  request.get('/orders/todayTotal').then(res => {
    if (res.code === '200') {
      data.todayTotal = res.data.total
      data.time = res.data.time
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 加载总票房排行榜
const loadTotal = () => {
  request.get('/film/selectTotalTop').then(res => {
    if (res.code === '200') {
      data.firstTotal = res.data[0]
      data.topTotal = res.data.slice(1, res.data.length)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 加载评分排行榜
const loadScore = () => {
  request.get('/film/selectScoreTop').then(res => {
    if (res.code === '200') {
      data.firstScore = res.data[0]
      data.topScore = res.data.slice(1, res.data.length)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 加载推荐电影
const loadRecommendations = () => {
  console.log("加载推荐电影...");
  const user = JSON.parse(localStorage.getItem('xm-user'));  // 获取并解析存储的用户数据
  const userId = user ? user.id : null;  // 提取 id 或返回 null 如果没有数据

  console.log(userId);  // 输出用户的 id

  if (!userId) {
    ElMessage.error("用户未登录");
    return;
  }

  request.get(`/api/recommendations/${userId}`).then(res => {
    console.log("API 请求结果:", res);  // 输出请求返回的结果
    if (res.code === '200') {
      console.log(data.recommended);  // 输出推荐电影的数据，检查每个 item 是否有 img 和 title 字段
      data.recommended = res.data;  // 存储推荐数据
    } else {
      ElMessage.error(res.msg);
    }
  }).catch(error => {
    ElMessage.error('加载推荐电影失败');
    console.error(error);
  });
}


// 初始加载数据
loadTodayTotal();
loadTotal();
loadScore();
loadRecommendations();  // 加载推荐电影数据


const navTo = (url) => {
  location.href = url
}

// 加载所有电影
const load = () => {
  request.get('/film/selectAll').then(res => {
    if (res.code === '200') {
      data.data1 = res.data.filter(v => v.status === '已上映')
      data.data2 = res.data.filter(v => v.status === '待上映')
      if (data.data1 && data.data1.length > 8) {
        data.playingData = data.data1.slice(0, 8)
      } else {
        data.playingData = data.data1
      }
      if (data.data2 && data.data2.length > 8) {
        data.noPlayData = data.data2.slice(0, 8)
      } else {
        data.noPlayData = data.data2
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
</script>
