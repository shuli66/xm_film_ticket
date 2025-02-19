<template>
  <div style="min-height: 1000px">
    <div style="background: linear-gradient(to right, #46187e, #32046b, #32115b); height: 320px">
      <div style="width: 60%; margin: 0 auto; height: 320px; display: flex; align-items: center">
        <img :src="data.cinemaData.avatar" alt="" style="width: 320px; height: 320px">
        <div style="flex: 1; color: white; padding: 20px 35px">
          <div style="font-size: 26px">{{ data.cinemaData.name }}</div>
          <div style="font-size: 16px; margin-top: 10px">{{ data.cinemaData.address }}</div>
          <div style="font-size: 16px; margin-top: 5px">电话：{{ data.cinemaData.phone }}</div>
          <div style="font-size: 16px; margin-top: 20px">影院服务：</div>
          <div style="margin-top: 10px"><el-tag type="primary" style="margin-right: 5px">退票无忧</el-tag>未取票用户在放映前60分钟可退票</div>
          <div style="margin-top: 10px"><el-tag type="primary" style="margin-right: 5px">儿童优惠</el-tag>1位成人可免费带1位不满1.3米儿童，儿童免票无座</div>
          <div style="margin-top: 10px"><el-tag type="primary" style="margin-right: 5px">WiFi覆盖</el-tag>1位成人可免费带1位不满1.3米儿童，儿童免票无座</div>
          <div style="margin-top: 10px"><el-tag type="primary" style="margin-right: 5px">免费停车</el-tag>停车场位于长江西路辅路乐客来地面停车场和乐客来生活馆地下负二层均免费停车</div>
        </div>
      </div>
    </div>
    <div style="width: 55%; margin: 40px auto">
      <div style="font-size: 20px">该影院上线电影 <span style="font-size: 14px; color: #666666">（点击选择电影）</span></div>
      <div style="margin-top: 20px">
        <el-row :gutter="20">
          <el-col :span="4" v-for="item in data.filmData" style="margin-bottom: 20px">
            <img :src="item.img" alt="" style="width: 100%; height: 180px; border-radius: 5px; cursor: pointer" @click="changeFilm(item)">
            <div style="font-size: 16px; margin: 5px 0">{{ item.title }}</div>
            <div style="font-size: 22px; color: orange">{{ item.score }} 分</div>
          </el-col>
        </el-row>
      </div>
      <div v-if="data.film.title" style="margin-top: 20px">
        <div style="font-size: 26px">
          <span>{{ data.film.title }}</span>
          <span style="color: orange; margin-left: 20px">{{ data.film.score }} <span style="font-size: 15px">分</span></span>
        </div>
        <div style="margin-top: 10px; display: flex">
          <div style="width: 200px"><span style="color: #666666">时长：</span>{{ data.film.time }} 分钟</div>
          <div style="width: 200px"><span style="color: #666666">类型：</span><span style="margin-right: 5px" v-for="item in data.film.types">{{ item }}</span></div>
          <div style="flex: 1"><span style="color: #666666">演职人员：</span>
            <span v-if="data.film.actors.length" style="margin-right: 5px" v-for="item in data.film.actors">{{ item }}</span>
            <span v-else>管理员暂未录入演职人员</span>
          </div>
        </div>
        <div style="margin-top: 20px">
          <el-table stripe :data="data.showData">
            <el-table-column prop="time" label="放映时间" />
            <el-table-column prop="roomName" label="放映厅" />
            <el-table-column prop="price" label="售价" />
            <el-table-column label="操作" width="100" fixed="right">
              <template v-slot="scope">
                <el-button type="danger"  @click="$router.push('/front/seat?id=' + scope.row.id)">选座购票</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div style="margin-top: 20px" v-if="data.total">
          <el-pagination @current-change="loadShow" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {reactive, onMounted} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  cinemaId: router.currentRoute.value.query.id,
  filmId: router.currentRoute.value.query.filmId,
  cinemaData: {},
  filmData: [],
  film: {},
  showData: [],
  pageNum: 1,
  pageSize: 2,
  total: 0
})
const loadCinema = () => {
  data.cinemaId = router.currentRoute.value.query.id
  request.get('/cinema/selectById/' + data.cinemaId).then(res => {
    if (res.code === '200') {
      data.cinemaData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const loadFilm = () => {
  request.get('/show/selectByCinemaId/' + data.cinemaId).then(res => {
    if (res.code === '200') {
      data.filmData = res.data
      if (data.filmId) {
        let list = data.filmData.filter(v => v.id + '' === data.filmId)
        data.film = list[0]
      } else {
        if (data.filmData.length > 0) {
          data.film = data.filmData[0]
          data.filmId = data.film.id
        }
      }
      loadShow()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
const changeFilm = (item) => {
  data.filmId = item.id
  data.film = item
  loadShow()
}
const loadShow = () => {
  request.get('/show/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      filmId: data.filmId,
      cinemaId: data.cinemaId,
      status: '购票中'
    }
  }).then(res => {
    if (res.code === '200') {
      data.showData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}

onMounted(() =>  {
  loadCinema()
  loadFilm()
})
</script>

<style scoped>

</style>