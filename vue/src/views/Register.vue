<template>
  <div class="register-wrapper">
    <div class="register-container">
      <div class="register-header">
        <h2>欢 迎 注 册</h2>
      </div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <el-form-item prop="username">
          <el-input 
            :prefix-icon="User" 
            size="large" 
            v-model="data.form.username" 
            placeholder="请输入用户名"
            maxlength="20"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            show-password 
            :prefix-icon="Lock" 
            size="large" 
            v-model="data.form.password" 
            placeholder="请输入密码"
            maxlength="20"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input 
            show-password 
            :prefix-icon="Lock" 
            size="large" 
            v-model="data.form.confirmPassword" 
            placeholder="请确认密码"
            maxlength="20"
            show-word-limit
          ></el-input>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input 
            :prefix-icon="Phone" 
            size="large" 
            v-model="data.form.phone" 
            placeholder="请输入手机号"
            maxlength="11"
          ></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input 
            :prefix-icon="Message" 
            size="large" 
            v-model="data.form.email" 
            placeholder="请输入邮箱（选填）"
          ></el-input>
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" v-model="data.form.role" placeholder="请选择角色">
            <el-option value="CINEMA" label="电影院"></el-option>
            <el-option value="USER" label="用户"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" class="register-btn" @click="register">注 册</el-button>
        </el-form-item>
        <div class="register-actions">
          <el-link type="primary" @click="$router.push('/login')">已有账号？请登录</el-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { User, Lock, Phone, Message } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请确认密码'))
  } else if (value !== data.form.password) {
    callback(new Error("确认密码跟原密码不一致!"))
  } else {
    callback()
  }
}

const validateUsername = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入用户名'))
  } else if (value.length < 2) {
    callback(new Error('用户名长度不能小于2位'))
  } else if (!/^[\u4e00-\u9fa5a-zA-Z0-9_]+$/.test(value)) {
    callback(new Error('用户名只能包含中文、字母、数字和下划线'))
  } else {
    // 检查用户名是否已存在
    request.get('/user/checkUsername', {
      params: { username: value }
    }).then(res => {
      if (res.code === '200') {
        if (res.data) {
          callback(new Error('用户名已存在'))
        } else {
          callback()
        }
      } else {
        callback(new Error('验证失败'))
      }
    }).catch(() => {
      callback(new Error('验证失败'))
    })
  }
}

const validatePassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入密码'))
  } else if (value.length < 6) {
    callback(new Error('密码长度不能小于6位'))
  } else {
    callback()
  }
}

const validatePhone = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入手机号'))
  } else if (!/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error('请输入正确的手机号'))
  } else {
    // 检查手机号是否已存在
    request.get('/user/checkPhone', {
      params: { phone: value }
    }).then(res => {
      if (res.code === '200') {
        if (res.data) {
          callback(new Error('手机号已被注册'))
        } else {
          callback()
        }
      } else {
        callback(new Error('验证失败'))
      }
    }).catch(() => {
      callback(new Error('验证失败'))
    })
  }
}

const validateEmail = (rule, value, callback) => {
  if (value && !/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value)) {
    callback(new Error('请输入正确的邮箱格式'))
  } else {
    callback()
  }
}

const data = reactive({
  form: { },
  rules: {
    username: [
      { validator: validateUsername, trigger: 'blur' }
    ],
    password: [
      { validator: validatePassword, trigger: 'blur' }
    ],
    confirmPassword: [
      { validator: validatePass, trigger: 'blur' }
    ],
    phone: [
      { validator: validatePhone, trigger: 'blur' }
    ],
    email: [
      { validator: validateEmail, trigger: 'blur' }
    ],
    role: [
      { required: true, message: '请选择角色', trigger: 'blur' }
    ]
  }
})

const formRef = ref()

const register = () => {
  formRef.value.validate(valid => {
    if (valid) {
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          ElMessage({
            type: 'success',
            message: '注册成功，即将跳转到登录页面',
            duration: 3000
          })
          setTimeout(() => {
            router.push('/login')
          }, 1500)
        } else {
          ElMessage({
            type: 'error',
            message: res.msg || '注册失败',
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
.register-wrapper {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: cover;
}

.register-container {
  width: 400px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.9);
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.register-header h2 {
  color: #333;
  font-size: 24px;
  margin: 0;
  font-weight: bold;
}

.register-btn {
  width: 100%;
  height: 40px;
  font-size: 16px;
  margin-top: 10px;
}

.register-actions {
  display: flex;
  justify-content: center;
  margin-top: 15px;
}

:deep(.el-input__wrapper) {
  box-shadow: 0 0 0 1px #dcdfe6;
  transition: all 0.3s;
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #409eff;
}

:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409eff;
}

:deep(.el-select) {
  width: 100%;
}
</style>