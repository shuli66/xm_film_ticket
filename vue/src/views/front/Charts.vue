<template>
  <div style="width: 80%; margin: 20px auto">
    <el-row :gutter="30">
      <el-col :span="12">
        <div class="card" style="font-size: 20px; line-height: 50px; background-color: #f1e3bf; font-weight: bold; color: red; text-align: center">总票房 Top榜</div>
        <div class="card">
          <div style="display: flex; align-items: center; padding: 10px; border-bottom: 1px solid #cccccc" v-for="(item, index) in data.topTotal" :key="item.id" @click="goToMovieDetail(item.id)">
            <div style="width: 50px">
              <div v-if="index === 0" style="height: 40px; width: 40px; background-color: red; color: white; border-radius: 5px; line-height: 40px; text-align: center; font-size: 20px; font-weight: bold">{{ index + 1 }}</div>
              <div v-else-if="index === 1" style="height: 40px; width: 40px; background-color: darkorange; color: white; border-radius: 5px; line-height: 40px; text-align: center; font-size: 20px; font-weight: bold">{{ index + 1 }}</div>
              <div v-else-if="index === 2" style="height: 40px; width: 40px; background-color: orange; color: white; border-radius: 5px; line-height: 40px; text-align: center; font-size: 20px; font-weight: bold">{{ index + 1 }}</div>
              <div v-else style="height: 40px; width: 40px; background-color: #d9d9d9; color: #666666; border-radius: 5px; line-height: 40px; text-align: center; font-size: 20px; font-weight: bold">{{ index + 1 }}</div>
            </div>
            <div style="width: 100px; text-align: center">
              <img :src="item.img" alt="" style="height: 100px; width: 80px; border-radius: 5px">
            </div>
            <div style="flex: 1; margin-left: 10px">
              <div style="font-weight: bold; font-size: 18px">{{ item.title }}</div>
              <div style="font-size: 15px; margin-top: 10px; color: #666666">主演：
                <span v-if="item.actors && item.actors.length" v-for="it in item.actors" style="margin-right: 5px">{{ it }}</span>
                <span v-else>管理员暂未输入演职人员信息</span>
              </div>
              <div style="font-size: 15px; margin-top: 5px; color: #999999">上映时间：{{ item.start }}</div>
            </div>
            <div style="width: 200px; font-size: 18px; color: red; text-align: right">
              总票房：<span style="font-size: 22px; font-weight: bold">{{ item.total }}</span>
            </div>
          </div>
        </div>
      </el-col>
      <!-- 评分 Top榜部分 -->
      <el-col :span="12">
        <div class="card" style="font-size: 20px; line-height: 50px; background-color: #f1e3bf; font-weight: bold; color: red; text-align: center">评分 Top榜</div>
        <div class="card">
          <div style="display: flex; align-items: center; padding: 10px; border-bottom: 1px solid #cccccc" v-for="(item, index) in data.topScore" :key="item.id" @click="goToMovieDetail(item.id)">
            <div style="width: 50px">
              <div v-if="index === 0" style="height: 40px; width: 40px; background-color: red; color: white; border-radius: 5px; line-height: 40px; text-align: center; font-size: 20px; font-weight: bold">{{ index + 1 }}</div>
              <div v-else-if="index === 1" style="height: 40px; width: 40px; background-color: darkorange; color: white; border-radius: 5px; line-height: 40px; text-align: center; font-size: 20px; font-weight: bold">{{ index + 1 }}</div>
              <div v-else-if="index === 2" style="height: 40px; width: 40px; background-color: orange; color: white; border-radius: 5px; line-height: 40px; text-align: center; font-size: 20px; font-weight: bold">{{ index + 1 }}</div>
              <div v-else style="height: 40px; width: 40px; background-color: #d9d9d9; color: #666666; border-radius: 5px; line-height: 40px; text-align: center; font-size: 20px; font-weight: bold">{{ index + 1 }}</div>
            </div>
            <div style="width: 100px; text-align: center">
              <img :src="item.img" alt="" style="height: 100px; width: 80px; border-radius: 5px">
            </div>
            <div style="flex: 1; margin-left: 10px">
              <div style="font-weight: bold; font-size: 18px">{{ item.title }}</div>
              <div style="font-size: 15px; margin-top: 10px; color: #666666">主演：
                <span v-if="item.actors && item.actors.length" v-for="it in item.actors" style="margin-right: 5px">{{ it }}</span>
                <span v-else>管理员暂未输入演职人员信息</span>
              </div>
              <div style="font-size: 15px; margin-top: 5px; color: #999999">上映时间：{{ item.start }}</div>
            </div>
            <div style="width: 200px; font-size: 20px; color: orange; text-align: right; font-weight: bold">
              <span style="font-size: 30px">{{ item.score.toFixed(1) }}</span> 分
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";  // 引入路由

const data = reactive({
  topTotal: [],
  topScore: [],
})

const loadTotal = () => {
  request.get('/film/selectTotalTop').then(res => {
    if (res.code === '200') {
      data.topTotal = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const loadScore = () => {
  request.get('/film/selectScoreTop').then(res => {
    if (res.code === '200') {
      data.topScore = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}


// 在你的电影列表部分，点击电影跳转到详情页
const goToMovieDetail = (id) => {
  // 使用路由跳转到电影详情页，传递电影的 id
  router.push({ path: '/front/filmDetail', query: { id } });
}



loadTotal()
loadScore()
</script>
