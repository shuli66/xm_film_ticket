<template>
  <div class="person-container">
    <div class="profile-card">
      <div class="card-header">
        <h2>个人资料</h2>
        <p>您可以在这里更新您的个人信息</p>
      </div>

      <el-form ref="user" :model="data.user" label-width="80px" class="profile-form">
        <div class="avatar-section">
          <el-form-item prop="avatar" label="头像">
            <el-upload
                :action="uploadUrl"
                :on-success="handleFileUpload"
                :filmShow-file-list="false"
                class="avatar-uploader"
            >
              <div class="avatar-wrapper">
                <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                <div class="avatar-hover">
                  <div>更换头像</div>
                </div>
              </div>
            </el-upload>
          </el-form-item>
        </div>

        <div class="form-content">
          <el-form-item prop="username" label="用户名">
            <el-input disabled v-model="data.user.username" placeholder="请输入用户名"></el-input>
          </el-form-item>
          
          <el-form-item prop="name" label="名称">
            <el-input v-model="data.user.name" placeholder="请输入名称"></el-input>
          </el-form-item>
          
          <el-form-item prop="phone" label="电话">
            <el-input v-model="data.user.phone" placeholder="请输入电话"></el-input>
          </el-form-item>
          
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="data.user.email" placeholder="请输入邮箱"></el-input>
          </el-form-item>

          <div class="form-actions">
            <el-button type="primary" @click="update">保存修改</el-button>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import { fileApi } from "@/utils/api.js";  // 导入API模块

const baseUrl = import.meta.env.VITE_BASE_URL

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}')
})

// 使用API模块中的上传URL
const uploadUrl = fileApi.uploadUrl;

const handleFileUpload = (res) => {
  data.user.avatar = res.data
  ElMessage.success('头像上传成功')
}

const emit = defineEmits(['updateUser'])
const update = () => {
  let url = (data.user.role === 'ADMIN' ? '/admin/update' : '/cinema/update')
  request.put(url, data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('保存成功')
      localStorage.setItem('xm-user', JSON.stringify(data.user))
      emit('updateUser')
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>

<style scoped>
.person-container {
  display: flex;
  justify-content: center;
  padding: 20px;
}

.profile-card {
  width: 100%;
  max-width: 800px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.card-header {
  padding: 30px 40px;
  background: linear-gradient(135deg, #f96332, #ff8a65);
  color: white;
}

.card-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.card-header p {
  margin: 8px 0 0;
  font-size: 14px;
  opacity: 0.8;
}

.profile-form {
  display: flex;
  padding: 30px;
}

.avatar-section {
  flex: 0 0 180px;
  margin-right: 30px;
}

.form-content {
  flex: 1;
}

.avatar-wrapper {
  position: relative;
  width: 150px;
  height: 150px;
  overflow: hidden;
  border-radius: 50%;
  border: 4px solid #f5f7fa;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  background-color: #f5f7fa;
  transition: all 0.3s ease;
}

.avatar-wrapper:hover .avatar-hover {
  opacity: 1;
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-hover {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  opacity: 0;
  transition: opacity 0.3s ease;
  cursor: pointer;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f7fa;
}

.form-actions {
  margin-top: 30px;
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .profile-form {
    flex-direction: column;
  }
  
  .avatar-section {
    margin-right: 0;
    margin-bottom: 30px;
    display: flex;
    justify-content: center;
  }
}
</style>