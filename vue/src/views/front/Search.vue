<template>
  <div style="width: 60%; margin: 20px auto">
    <div style="font-size: 18px; line-height: 80px; border-bottom: 1px solid #cccccc">搜索"{{ data.title }}"的结果</div>
    <div style="margin-top: 50px">
      <el-row :gutter="15">
        <el-col :span="5" v-for="item in data.filmData" style="margin-bottom: 30px">
          <img :src="item.img" alt="" style="width: 100%; height: 230px; border-top-left-radius: 5px; border-top-right-radius: 5px; cursor: pointer" @click="$router.push('/front/filmDetail?id=' + item.id)">
          <div style="margin-top: 5px; font-size: 16px">{{ item.title }}</div>
          <div style="margin-top: 10px; display: flex; align-items: center">
            <div style="flex: 1">
              <el-tag v-if="item.status === '待上映'" type="warning">{{ item.status }}</el-tag>
              <el-tag v-if="item.status === '已上映'" type="success">{{ item.status }}</el-tag>
              <el-tag v-if="item.status === '停止上映'" type="danger">{{ item.status }}</el-tag>
            </div>
            <div style="color: orange; font-size: 20px; width: 50px">{{ item.score }}分</div>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const data = reactive({
  title: router.currentRoute.value.query.title,
  filmData: []
})

const loadByTitle = () => {
  data.title = router.currentRoute.value.query.title
  request.get('/film/selectAll', {
    params: {
      title: data.title
    }
  }).then(res => {
    if (res.code === '200') {
      data.filmData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadByTitle()
</script>

<style>
</style>