<template>
  <div class="register-wrapper">
    <div class="register-container">
      <div class="register-header">
        <h2>欢 迎 注 册</h2>
      </div>

      <!-- 角色选择开关 -->
      <div class="role-switch">
        <el-radio-group v-model="selectedRole" size="large">
          <el-radio-button label="USER">用户注册</el-radio-button>
          <el-radio-button label="CINEMA">影院注册</el-radio-button>
        </el-radio-group>
      </div>

      <!-- 通用注册表单 -->
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-position="top">
        <el-form-item prop="username" label="账号">
          <el-input 
            :prefix-icon="User" 
            size="large" 
            v-model="data.form.username" 
            placeholder="请输入账号"
            maxlength="20"
            show-word-limit
          >
            <template #prepend>
              <div class="input-label required">账号</div>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="name" label="昵称">
          <el-input 
            :prefix-icon="Avatar" 
            size="large" 
            v-model="data.form.name" 
            placeholder="请输入昵称"
            maxlength="10"
            show-word-limit
          >
            <template #prepend>
              <div class="input-label required">昵称</div>
            </template>
          </el-input>
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
          >
            <template #prepend>
              <div class="input-label">密码</div>
            </template>
          </el-input>
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
          >
            <template #prepend>
              <div class="input-label">确认</div>
            </template>
          </el-input>
        </el-form-item>

        <!-- 用户注册特有表单 -->
        <template v-if="selectedRole === 'USER'">
          <el-form-item prop="phone">
            <el-input 
              :prefix-icon="Phone" 
              size="large" 
              v-model="data.form.phone" 
              placeholder="请输入手机号"
              maxlength="11"
            >
              <template #prepend>
                <div class="input-label required">手机</div>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="email">
            <el-input 
              :prefix-icon="Message" 
              size="large" 
              v-model="data.form.email" 
              placeholder="请输入邮箱（选填）"
            >
              <template #prepend>
                <div class="input-label">邮箱</div>
              </template>
            </el-input>
          </el-form-item>
        </template>

        <!-- 电影院注册特有表单 -->
        <template v-if="selectedRole === 'CINEMA'">
          <el-form-item prop="phone">
            <el-input 
              :prefix-icon="Phone" 
              size="large" 
              v-model="data.form.phone" 
              placeholder="请输入联系电话"
              maxlength="11"
            >
              <template #prepend>
                <div class="input-label required">电话</div>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="email">
            <el-input 
              :prefix-icon="Message" 
              size="large" 
              v-model="data.form.email" 
              placeholder="请输入邮箱"
            >
              <template #prepend>
                <div class="input-label required">邮箱</div>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="address">
            <el-input 
              :prefix-icon="Location" 
              size="large" 
              v-model="data.form.address" 
              placeholder="请输入地址"
            >
              <template #prepend>
                <div class="input-label required">地址</div>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="leader">
            <el-input 
              :prefix-icon="User" 
              size="large" 
              v-model="data.form.leader" 
              placeholder="请输入负责人姓名"
            >
              <template #prepend>
                <div class="input-label required">负责人</div>
              </template>
            </el-input>
          </el-form-item>
          <div class="sub-title">影院认证资料（提交后审核）</div>
          <el-form-item prop="code">
            <el-input 
              :prefix-icon="Document" 
              size="large" 
              v-model="data.form.code" 
              placeholder="请输入负责人身份证号"
            >
              <template #prepend>
                <div class="input-label required">证件号</div>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="front">
            <p class="field-label required">上传身份证正面照片</p>
            <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="(res) => handleUploadSuccess(res, 'front')"
              :before-upload="beforeUpload"
              list-type="picture-card"
              :limit="1"
              :file-list="frontFileList"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
          </el-form-item>
          <el-form-item prop="back">
            <p class="field-label required">上传身份证反面照片</p>
            <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="(res) => handleUploadSuccess(res, 'back')"
              :before-upload="beforeUpload"
              list-type="picture-card"
              :limit="1"
              :file-list="backFileList"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
          </el-form-item>
          <el-form-item prop="certificate">
            <p class="field-label required">上传营业执照</p>
            <el-upload
              :action="baseUrl + '/files/upload'"
              :on-success="(res) => handleUploadSuccess(res, 'certificate')"
              :before-upload="beforeUpload"
              list-type="picture-card"
              :limit="1"
              :file-list="certificateFileList"
            >
              <el-icon><Plus /></el-icon>
            </el-upload>
          </el-form-item>
        </template>

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
import { reactive, ref, watch } from "vue";
import { User, Lock, Phone, Message, Avatar, Location, Document, Plus } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const baseUrl = import.meta.env.VITE_BASE_URL;

// 文件上传相关
const frontFileList = ref([]);
const backFileList = ref([]);
const certificateFileList = ref([]);

// 角色选择
const selectedRole = ref('USER');

// 监听角色变化，重置表单
watch(selectedRole, (newVal) => {
  data.form = { role: newVal };
  formRef.value?.clearValidate();
  
  // 重置验证规则
  if (newVal === 'USER') {
    data.rules.email = [{ validator: validateEmail, trigger: 'blur' }];
    data.rules.phone = [{ validator: validatePhone, trigger: 'blur' }];
  } else if (newVal === 'CINEMA') {
    data.rules.email = [{ required: true, message: '请输入邮箱', trigger: 'blur' }, 
                     { validator: validateEmail, trigger: 'blur' }];
    data.rules.phone = [{ required: true, message: '请输入联系电话', trigger: 'blur' }];
    data.rules.address = [{ required: true, message: '请输入地址', trigger: 'blur' }];
    data.rules.leader = [{ required: true, message: '请输入负责人姓名', trigger: 'blur' }];
    data.rules.code = [{ required: true, message: '请输入身份证号', trigger: 'blur' }];
    data.rules.front = [{ required: true, message: '请上传身份证正面照片', trigger: 'change' }];
    data.rules.back = [{ required: true, message: '请上传身份证反面照片', trigger: 'change' }];
    data.rules.certificate = [{ required: true, message: '请上传营业执照', trigger: 'change' }];
  }
});

// 文件上传处理
const handleUploadSuccess = (res, field) => {
  if (res.code === '200') {
    data.form[field] = res.data;
    // 手动触发验证
    formRef.value?.validateField(field);
  } else {
    ElMessage.error('上传失败');
  }
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/');
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isImage) {
    ElMessage.error('请上传图片格式文件!');
    return false;
  }
  if (!isLt2M) {
    ElMessage.error('文件大小不能超过 2MB!');
    return false;
  }
  return true;
}

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
    callback(new Error('请输入账号'))
  } else if (value.length < 2) {
    callback(new Error('账号长度不能小于2位'))
  } else if (!/^[a-zA-Z0-9_]+$/.test(value)) {
    callback(new Error('账号只能包含字母、数字和下划线'))
  } else {
    // 检查用户名是否已存在
    request.get('/user/checkUsername', {
      params: { username: value }
    }).then(res => {
      if (res.code === '200') {
        if (res.data) {
          callback(new Error('该账号已被注册'))
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

const validateName = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入昵称'))
  } else if (value.length < 2) {
    callback(new Error('昵称长度不能小于2位'))
  } else {
    callback()
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
    callback()
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
  form: { role: selectedRole.value },
  rules: {
    username: [
      { validator: validateUsername, trigger: 'blur' }
    ],
    name: [
      { validator: validateName, trigger: 'blur' }
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
    ]
  }
})

const formRef = ref()

const register = () => {
  formRef.value.validate(valid => {
    if (valid) {
      // 设置角色
      data.form.role = selectedRole.value;
      
      // 检查所有必填字段
      const missingFields = [];
      
      // 通用字段检查
      if (!data.form.username) missingFields.push('账号');
      if (!data.form.name) missingFields.push('昵称');
      if (!data.form.password) missingFields.push('密码');
      if (!data.form.confirmPassword) missingFields.push('确认密码');
      if (!data.form.phone) missingFields.push('手机号/联系电话');
      
      // 影院特有字段检查
      if (selectedRole.value === 'CINEMA') {
        if (!data.form.email) missingFields.push('邮箱');
        if (!data.form.address) missingFields.push('地址');
        if (!data.form.leader) missingFields.push('负责人');
        if (!data.form.code) missingFields.push('身份证号');
        if (!data.form.front) missingFields.push('身份证正面照片');
        if (!data.form.back) missingFields.push('身份证反面照片');
        if (!data.form.certificate) missingFields.push('营业执照');
      }
      
      // 如果有缺失字段，显示统一提示
      if (missingFields.length > 0) {
        ElMessage({
          type: 'warning',
          message: `请填写以下必填项: ${missingFields.join('、')}`,
          duration: 3000
        });
        return;
      }
      
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          ElMessage({
            type: 'success',
            message: selectedRole.value === 'USER' ? 
              '注册成功，即将跳转到登录页面' : 
              '注册成功，请等待管理员审核后登录',
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
    } else {
      // 表单验证未通过，显示提示
      ElMessage({
        type: 'warning',
        message: '请正确填写所有必填项',
        duration: 3000
      });
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
  width: 500px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 16px;
  padding: 40px;
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.08);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.9);
  margin: 20px 0;
}

.register-header {
  text-align: center;
  margin-bottom: 20px;
}

.register-header h2 {
  color: #333;
  font-size: 24px;
  margin: 0;
  font-weight: bold;
}

.role-switch {
  text-align: center;
  margin-bottom: 20px;
}

.register-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
  border-radius: 25px;
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
  border: none;
  letter-spacing: 1px;
  transition: all 0.3s ease;
}

.register-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 7px 14px rgba(0, 123, 255, 0.3);
  background: linear-gradient(135deg, #67c23a 0%, #409eff 100%);
}

.register-actions {
  display: flex;
  justify-content: center;
  margin-top: 18px;
}

.input-label {
  width: 38px;
  text-align: justify;
  text-align-last: justify;
  color: #606266;
}

.required:after {
  content: '*';
  color: #F56C6C;
  margin-left: 4px;
}

.sub-title {
  font-size: 16px;
  font-weight: bold;
  color: #409eff;
  margin: 15px 0;
  padding-bottom: 5px;
  border-bottom: 1px solid #ebeef5;
}

.field-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}
</style>