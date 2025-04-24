<template>
  <div class="manager-container">
    <div class="manager-header">
      <div class="manager-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">电影购票网站</div>
      </div>
      <div class="manager-header-center">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/manager/home' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item>{{ router.currentRoute.value.meta.name }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="manager-header-right">
        <el-dropdown style="cursor: pointer">
          <div style="padding-right: 20px; display: flex; align-items: center">
            <img style="width: 40px; height: 40px; border-radius: 50%;" :src="getFullImageUrl(data.user.avatar)" alt="">
            <span style="margin-left: 5px; color: white">{{ data.user.name }}</span><el-icon color="#fff"><arrow-down /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/manager/person')">个人资料</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/certificate')" v-if="data.user.role === 'CINEMA'">资质认证</el-dropdown-item>
              <el-dropdown-item @click="router.push('/manager/password')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <!-- 下面部分开始 -->
    <div style="display: flex">
      <div class="manager-main-left">
        <el-menu :default-active="router.currentRoute.value.path"
                 :default-openeds="['1', '2']"
                 router
        >
          <el-menu-item index="/manager/adminHome" v-if="data.user.role === 'ADMIN'"><el-icon><HomeFilled /></el-icon><span>系统首页</span></el-menu-item>
          <el-menu-item index="/manager/home" v-else><el-icon><HomeFilled /></el-icon><span>系统首页</span></el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/manager/type" v-if="data.user.role === 'ADMIN'">电影分类</el-menu-item>
            <el-menu-item index="/manager/area" v-if="data.user.role === 'ADMIN'">电影区域</el-menu-item>
            <el-menu-item index="/manager/film" v-if="data.user.role === 'ADMIN' || (data.user.role === 'CINEMA' && data.user.status === '审核通过')">电影信息</el-menu-item>
            <el-menu-item index="/manager/video" v-if="data.user.role === 'ADMIN'">预告视频</el-menu-item>
            <el-menu-item index="/manager/actor" v-if="data.user.role === 'ADMIN'">演职人员</el-menu-item>
            <el-menu-item index="/manager/room" v-if="data.user.role === 'ADMIN' || (data.user.role === 'CINEMA' && data.user.status === '审核通过')">影厅房间</el-menu-item>
            <el-menu-item index="/manager/show" v-if="data.user.role === 'ADMIN' || (data.user.role === 'CINEMA' && data.user.status === '审核通过')">放映记录</el-menu-item>
            <el-menu-item index="/manager/orders" v-if="data.user.role === 'ADMIN' || (data.user.role === 'CINEMA' && data.user.status === '审核通过')">购票订单</el-menu-item>
            <el-menu-item index="/manager/score" v-if="data.user.role === 'ADMIN'">电影评分</el-menu-item>
            <el-menu-item index="/manager/comment" v-if="data.user.role === 'ADMIN'">评论管理</el-menu-item>
            <el-menu-item index="/manager/notice" v-if="data.user.role === 'ADMIN'">系统公告</el-menu-item>

          </el-sub-menu>
          <el-sub-menu index="2" v-if="data.user.role === 'ADMIN'">
            <template #title>
              <el-icon><Menu /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/cinema">影院信息</el-menu-item>
            <el-menu-item index="/manager/user">用户信息</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <div class="manager-main-right">
        <RouterView @updateUser="updateUser" />
      </div>
    </div>
    <!-- 下面部分结束 -->


  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}')
})

// 获取完整的图片URL
const getFullImageUrl = (url) => {
  if (!url) return '';
  // 如果已经是完整URL则直接返回
  if (url.startsWith('http')) return url;
  // 否则拼接baseUrl
  return baseUrl + url;
}

const logout = () => {
  localStorage.removeItem('xm-user')
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(localStorage.getItem('xm-user') || '{}')
}

if (!data.user.id) {
  logout()
  ElMessage.error('请登录！')
}
</script>

<style scoped>
@import "@/assets/css/manager.css";
</style>