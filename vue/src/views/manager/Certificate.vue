<template>
  <div style="width: 50%" class="card">
    <div style="color: red; padding: 10px">
      一旦认证信息修改，影院状态就会变为待审核状态，将无法使用影院功能
    </div>
    <el-form ref="formRef" :rules="data.rules" :model="data.user" label-width="95px" style="padding: 20px">
      <el-form-item prop="front" label="身份证正面">
        <el-upload
            :action="baseUrl + '/files/upload'"
            :on-success="handleFrontUpload"
            :filmShow-file-list="false"
            class="avatar-uploader"
        >
          <img v-if="data.user.front" :src="data.user.front" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item prop="back" label="身份证反面">
        <el-upload
            :action="baseUrl + '/files/upload'"
            :on-success="handleBackUpload"
            :filmShow-file-list="false"
            class="avatar-uploader"
        >
          <img v-if="data.user.back" :src="data.user.back" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item prop="certificate" label="营业执照">
        <el-upload
            :action="baseUrl + '/files/upload'"
            :on-success="handleCertificateUpload"
            :filmShow-file-list="false"
            class="avatar-uploader"
        >
          <img v-if="data.user.certificate" :src="data.user.certificate" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item prop="leader" label="负责人姓名">
        <el-input v-model="data.user.leader" placeholder="请输入负责人姓名"></el-input>
      </el-form-item>
      <el-form-item prop="code" label="身份证号">
        <el-input v-model="data.user.code" placeholder="请输入身份证号"></el-input>
      </el-form-item>
      <el-form-item prop="address" label="影院地址">
        <el-input type="textarea" :rows="4" v-model="data.user.address" placeholder="请输入影院地址"></el-input>
      </el-form-item>
      <el-form-item prop="status" label="审核状态">
        <el-tag type="warning" v-if="data.user.status === '待审核'">{{ data.user.status }}</el-tag>
        <el-tag type="success" v-if="data.user.status === '审核通过'">{{ data.user.status }}</el-tag>
        <el-tag type="danger" v-if="data.user.status === '审核拒绝'">{{ data.user.status }}</el-tag>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" @click="update">提 交</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const baseUrl = import.meta.env.VITE_BASE_URL
const formRef = ref()

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  rules: {
    front: [
      { required: true, message: '请上传身份证正面', trigger: 'blur' }
    ],
    back: [
      { required: true, message: '请上传身份证反面', trigger: 'blur' }
    ],
    certificate: [
      { required: true, message: '请上传营业执照', trigger: 'blur' }
    ],
    leader: [
      { required: true, message: '请填写负责人姓名', trigger: 'blur' }
    ],
    code: [
      { required: true, message: '请填写负责人身份证号', trigger: 'blur' }
    ],
    address: [
      { required: true, message: '请填写影院地址', trigger: 'blur' }
    ],
  }
})

const handleFrontUpload = (res) => {
  data.user.front = res.data
}
const handleBackUpload = (res) => {
  data.user.back = res.data
}
const handleCertificateUpload = (res) => {
  data.user.certificate = res.data
}

const emit = defineEmits(['updateUser'])
const update = () => {
  formRef.value.validate(valid => {
    if (valid) {
      data.user.status = '待审核'
      request.put('/cinema/update', data.user).then(res => {
        if (res.code === '200') {
          ElMessage.success('提交成功')
          localStorage.setItem('xm-user', JSON.stringify(data.user))
          emit('updateUser')
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style>
.avatar-uploader {
  height: 120px;
}
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}
</style>