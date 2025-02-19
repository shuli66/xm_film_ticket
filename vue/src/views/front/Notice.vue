<template>
  <div style="width: 40%; margin: 20px auto" class="card">
    <div style="font-size: 20px; font-weight: bold; margin-bottom: 20px; padding: 10px">公告列表</div>
    <el-timeline style="max-width: 600px">
      <el-timeline-item
          v-for="(activity, index) in data.noticeData"
          :key="index"
          :timestamp="activity.time"
      >
        {{ activity.content }}
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data = reactive({
  noticeData: []
})

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}
loadNotice()
</script>

<style>
</style>