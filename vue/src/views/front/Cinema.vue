<template>
  <div style="width: 50%; margin: 20px auto; min-height: 500px">
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
        <el-button style="padding: 20px 30px" type="primary" plain @click="$router.push('/front/cinemaDetail?id=' + item.id)">进入购票</el-button>
      </div>
    </div>
    <div style="margin-top: 40px" v-if="data.total">
      <el-pagination @current-change="loadCinema" background layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  pageNum: 1,
  pageSize: 4,
  total: 0,
  cinemaData: []
})

const loadCinema = () => {
  request.get('/cinema/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      status: '审核通过'
    }
  }).then(res => {
    if (res.code === '200') {
      data.cinemaData = res.data.list
      data.total = res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadCinema()
</script>
