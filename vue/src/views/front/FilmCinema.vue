<template>
  <div>
    <div style="background: linear-gradient(to right, #46187e, #32046b, #32115b); height: 320px">
      <div style="width: 55%; margin: 0 auto; height: 320px; display: flex; align-items: center">
        <img :src="data.filmData.img" alt="" style="width: 280px; height: 320px">
        <div style="flex: 1; color: white; padding: 20px 35px">
          <div style="font-size: 26px">{{ data.filmData.title }}</div>
          <div style="font-size: 16px">{{ data.filmData.english }}</div>
          <div style="margin-top: 15px">
            <span style="margin-right: 5px" v-for="item in data.filmData.types">{{ item }}</span>
          </div>
          <div style="margin-top: 5px">{{ data.filmData.areaName }} / {{ data.filmData.time }}分钟</div>
          <div style="margin-top: 5px">{{ data.filmData.start }} 中国大陆上映</div>
          <el-button style="background-color: #ef4238; border: none; color: white; width: 100%; margin-top: 60px; height: 40px; font-size: 16px" @click="$router.push('/front/filmDetail?id=' + data.filmData.id)">查看更多电影详情</el-button>
        </div>
        <div style="width: 250px; color: white">
          <div style="font-size: 12px">影片口碑</div>
          <div style="display: flex; align-items: center">
            <div style="width: 100px; font-weight: bold; font-size: 16px"><span style="font-size: 30px">{{ data.filmData.score }}</span> 分</div>
            <div style="flex: 1">
              <el-rate v-model="data.filmData.score" disabled/>
              <div>2人评分</div>
            </div>
          </div>
          <div style="margin-top: 10px; font-size: 12px">累计票房</div>
          <div style="margin-top: 5px; font-weight: bold; font-size: 16px"><span style="font-size: 25px">500.23</span> 元</div>
        </div>
      </div>
    </div>
    <div style="width: 55%; margin: 50px auto">
      <div style="font-size: 22px; border-left: 4px solid red; padding-left: 5px; line-height: 30px; margin-top: 40px">放映影院</div>
      <div style="margin-top: 20px">
        <div style="display: flex; align-items: center; border-bottom: 1px solid #cccccc" v-for="item in data.cinemaData">
          <div style="flex: 1; display: flex; padding: 20px 0">
            <img :src="item.avatar" alt="" style="width: 150px; height: 150px; border-radius: 5px; cursor: pointer">
            <div style="flex: 1; margin-left: 20px">
              <div style="font-size: 18px; font-weight: bold">{{ item.name }}</div>
              <div style="margin-top: 10px">
                <el-tag type="danger">退票无忧</el-tag>
                <el-tag style="margin-left: 10px" type="success">儿童优惠</el-tag>
                <el-tag style="margin-left: 10px" type="primary">WiFi覆盖</el-tag>
                <el-tag style="margin-left: 10px" type="info">免费停车</el-tag>
              </div>
              <div style="margin-top: 10px; color: #666666">地址：{{ item.address }}</div>
              <div style="margin-top: 10px; color: #666666">电话：{{ item.phone }}</div>
              <div style="margin-top: 10px; color: #666666">邮箱：{{ item.email }}</div>
            </div>
          </div>
          <div style="width: 120px; margin-left: 10px">
            <el-button style="padding: 20px 30px" type="primary" plain @click="$router.push('/front/cinemaDetail?id=' + item.id + '&filmId=' + data.filmId)">进入购票</el-button>
          </div>
        </div>
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
  cinemaData: []
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
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadFilm()
loadCinema()
</script>

<style scoped>

</style>