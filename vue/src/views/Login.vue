<template>
  <div class="login-wrapper">
    <!-- 导入雪花效果组件 -->
    <SnowEffect />
    
    <!-- 添加一些动态装饰元素 -->
    <div class="floating-circles">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>
    
    <div class="login-container" :class="{ 'appear': showContent }">
      <div class="login-header">
        <h2 class="fade-in-down">欢迎登录电影购票系统</h2>
        <div class="underline"></div>
      </div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" class="fade-in-up">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号" class="custom-input"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码" class="custom-input"></el-input>
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" v-model="data.form.role" placeholder="请选择角色" class="custom-select">
            <el-option value="ADMIN" label="管理员"></el-option>
            <el-option value="CINEMA" label="电影院"></el-option>
            <el-option value="USER" label="用户"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" class="login-btn" @click="login" :class="{ 'button-loading': data.loading }">
            <span v-if="!data.loading">登 录</span>
            <span v-else class="loading-spinner"></span>
          </el-button>
        </el-form-item>
        <div class="login-actions fade-in">
          <el-link type="primary" @click="showForgetPassword" class="custom-link">忘记密码？</el-link>
          <el-link type="primary" @click="$router.push('/register')" class="custom-link">注册账号</el-link>
        </div>
      </el-form>
    </div>

    <!-- 找回密码弹窗 -->
    <el-dialog
      v-model="data.forgetVisible"
      title="找回密码"
      width="400px"
      destroy-on-close
      class="forget-dialog custom-dialog"
    >
      <el-form :model="data.forgetForm" :rules="forgetRules" ref="forgetFormRef">
        <el-form-item prop="phone">
          <el-input v-model="data.forgetForm.phone" placeholder="请输入手机号" class="custom-input">
            <template #prefix>
              <el-icon><Phone /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="newPassword">
          <el-input 
            v-model="data.forgetForm.newPassword" 
            type="password" 
            placeholder="请输入新密码" 
            show-password
            class="custom-input"
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input 
            v-model="data.forgetForm.confirmPassword" 
            type="password" 
            placeholder="请确认新密码" 
            show-password
            class="custom-input"
          >
            <template #prefix>
              <el-icon><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.forgetVisible = false" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="resetPassword" class="confirm-btn">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from "vue";
import { User, Lock, Phone } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import SnowEffect from "@/components/SnowEffect.vue";

const formRef = ref()
const forgetFormRef = ref()
const showContent = ref(false);

onMounted(() => {
  // 添加延迟展示内容的效果
  setTimeout(() => {
    showContent.value = true;
  }, 300);
});

const data = reactive({
  form: {},
  forgetForm: {
    phone: '',
    newPassword: '',
    confirmPassword: ''
  },
  forgetVisible: false,
  loading: false,
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ],
    role: [
      { required: true, message: '请选择角色', trigger: 'blur' }
    ]
  }
})

const forgetRules = {
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (value !== data.forgetForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const login = () => {
  formRef.value.validate(valid => {
    if (valid) { // 表示表单校验通过
      data.loading = true; // 开始加载状态
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success('登录成功')
          // 存储用户信息到浏览器的缓存
          localStorage.setItem('xm-user', JSON.stringify(res.data))
          
          // 设置登录成功标记，用于触发系统公告显示
          sessionStorage.setItem('just_logged_in', 'true')
          
          setTimeout(() => {
            if (res.data.role === 'USER') {
              router.push('/front/home')
            }
            if (res.data.role === 'CINEMA') {
              router.push('/manager/home')
            }
            if (res.data.role === 'ADMIN') {
              router.push('/manager/adminHome')
            }
          }, 800)
        } else {
          ElMessage.error(res.msg)
          data.loading = false; // 结束加载状态
        }
      }).catch(() => {
        data.loading = false; // 错误时也结束加载状态
      })
    }
  })
}

const showForgetPassword = () => {
  data.forgetVisible = true
  data.forgetForm = {
    phone: '',
    newPassword: '',
    confirmPassword: ''
  }
}

const resetPassword = () => {
  forgetFormRef.value.validate((valid) => {
    if (valid) {
      request.post('/user/resetPassword', data.forgetForm).then(res => {
        if (res.code === '200') {
          ElMessage({
            type: 'success',
            message: '密码重置成功，请使用新密码登录',
            duration: 3000
          })
          data.forgetVisible = false
          // 清空表单
          data.forgetForm = {
            phone: '',
            newPassword: '',
            confirmPassword: ''
          }
        } else {
          ElMessage({
            type: 'error',
            message: res.msg || '密码重置失败',
            duration: 3000
          })
        }
      }).catch(err => {
        ElMessage({
          type: 'error',
          message: '系统错误，请稍后重试',
          duration: 3000
        })
      })
    }
  })
}
</script>

<style scoped>
.login-wrapper {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: cover;
  position: relative;
  overflow: hidden;
}

/* 自定义动画效果 */
@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translate3d(0, -20px, 0);
  }
  to {
    opacity: 1;
    transform: translate3d(0, 0, 0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translate3d(0, 20px, 0);
  }
  to {
    opacity: 1;
    transform: translate3d(0, 0, 0);
  }
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

.fade-in-down {
  animation: fadeInDown 0.6s ease-out;
}

.fade-in-up {
  animation: fadeInUp 0.6s ease-out;
}

.fade-in {
  animation: fadeIn 0.8s ease-out 0.5s forwards;
  opacity: 0;
}

/* 浮动圆圈装饰 */
.floating-circles {
  position: absolute;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.6;
  filter: blur(20px);
}

.circle-1 {
  width: 150px;
  height: 150px;
  background: rgba(64, 158, 255, 0.4);
  bottom: 10%;
  left: 10%;
  animation: float1 15s ease-in-out infinite;
}

.circle-2 {
  width: 100px;
  height: 100px;
  background: rgba(255, 102, 0, 0.3);
  top: 20%;
  right: 20%;
  animation: float2 18s ease-in-out infinite;
}

.circle-3 {
  width: 200px;
  height: 200px;
  background: rgba(103, 194, 58, 0.2);
  bottom: 30%;
  right: 10%;
  animation: float3 20s ease-in-out infinite;
}

@keyframes float1 {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(-20px, 30px) rotate(5deg); }
  50% { transform: translate(10px, -20px) rotate(-5deg); }
  75% { transform: translate(30px, 10px) rotate(3deg); }
}

@keyframes float2 {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(30px, 10px) rotate(-8deg); }
  50% { transform: translate(-20px, -20px) rotate(8deg); }
  75% { transform: translate(-10px, 30px) rotate(-4deg); }
}

@keyframes float3 {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(-15px, -25px) rotate(3deg); }
  50% { transform: translate(25px, 10px) rotate(-3deg); }
  75% { transform: translate(10px, -15px) rotate(5deg); }
}

.login-container {
  width: 420px;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  transform: translateY(30px);
  opacity: 0;
  transition: all 0.6s cubic-bezier(0.68, -0.55, 0.27, 1.55);
}

.login-container.appear {
  transform: translateY(0);
  opacity: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 35px;
  position: relative;
}

.login-header h2 {
  color: #333;
  font-size: 28px;
  margin: 0;
  font-weight: bold;
  letter-spacing: 1px;
}

.underline {
  width: 50px;
  height: 3px;
  background: linear-gradient(to right, #409eff, #67c23a);
  margin: 12px auto 0;
  border-radius: 3px;
  transition: width 0.4s ease;
}

.login-header:hover .underline {
  width: 100px;
}

.login-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
  margin-top: 10px;
  border-radius: 25px;
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
  border: none;
  letter-spacing: 1px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.login-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 7px 14px rgba(0, 123, 255, 0.3);
  background: linear-gradient(135deg, #67c23a 0%, #409eff 100%);
}

.login-btn:active {
  transform: translateY(1px);
}

.button-loading {
  pointer-events: none;
  opacity: 0.8;
}

.loading-spinner {
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 3px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: #fff;
  animation: spin 1s ease-in-out infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.login-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 18px;
}

.custom-link {
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s ease;
}

.custom-link:hover {
  transform: translateY(-2px);
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.code-input {
  display: flex;
  gap: 10px;
}

.code-input .el-input {
  flex: 1;
}

.forget-dialog {
  :deep(.el-dialog__body) {
    padding: 20px 30px;
  }
  
  :deep(.el-dialog__header) {
    text-align: center;
    padding: 20px 0 0;
    
    .el-dialog__title {
      font-size: 20px;
      font-weight: bold;
      color: #333;
    }
  }
  
  :deep(.el-dialog__headerbtn) {
    top: 15px;
    right: 15px;
  }
}

.custom-dialog :deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

.dialog-footer {
  display: flex;
  justify-content: center;
  gap: 15px;
  padding-top: 10px;
}

.cancel-btn, .confirm-btn {
  min-width: 100px;
  border-radius: 20px;
}

.confirm-btn {
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
  border: none;
}

.confirm-btn:hover {
  background: linear-gradient(135deg, #67c23a 0%, #409eff 100%);
  transform: translateY(-2px);
}

.custom-input {
  :deep(.el-input__wrapper) {
    box-shadow: 0 0 0 1px rgba(220, 223, 230, 0.5);
    border-radius: 12px;
    padding: 2px 15px;
    transition: all 0.3s;
  }

  :deep(.el-input__wrapper:hover) {
    box-shadow: 0 0 0 1px #409eff;
  }

  :deep(.el-input__wrapper.is-focus) {
    box-shadow: 0 0 8px rgba(64, 158, 255, 0.4);
  }
}

.custom-select {
  width: 100%;
  
  :deep(.el-input__wrapper) {
    box-shadow: 0 0 0 1px rgba(220, 223, 230, 0.5);
    border-radius: 12px;
    padding: 2px 15px;
    transition: all 0.3s;
  }

  :deep(.el-input__wrapper:hover) {
    box-shadow: 0 0 0 1px #409eff;
  }

  :deep(.el-input__wrapper.is-focus) {
    box-shadow: 0 0 8px rgba(64, 158, 255, 0.4);
  }
}

:deep(.el-input__prefix-inner) {
  display: flex;
  align-items: center;
}

:deep(.el-select) {
  width: 100%;
}
</style>