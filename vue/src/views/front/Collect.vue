<template>
  <div style="width: 55%; margin: 20px auto">
    <div class="card" style="font-size: 20px">我想看的电影（{{ data.total }}）</div>
    <div style="margin-top: 20px">
      <el-row :gutter="15">
        <el-col :span="5" v-for="item in data.collectData" style="margin-bottom: 30px">
          <img :src="item.film.img" alt="" style="width: 100%; height: 230px; border-top-left-radius: 5px; border-top-right-radius: 5px; cursor: pointer" @click="$router.push('/front/filmDetail?id=' + item.film.id)">
          <div style="margin-top: 5px; font-size: 16px">{{ item.film.title }}</div>
          <div style="color: orange; font-size: 20px; width: 50px">{{ item.film.score }}分</div>
        </el-col>
      </el-row>
    </div>
    <div v-if="data.total">
      <el-pagination @current-change="load" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  pageNum: 1,
  pageSize: 10,
  total: 0,
  collectData: []
})

const load = () => {
  request.get('/collect/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      userId: data.user.id
    }
  }).then(res => {
    if (res.code === '200') {
      data.collectData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
</script>
<style>
.el-col-5 {
  width: 20%;
  max-width: 20%;
}
</style>