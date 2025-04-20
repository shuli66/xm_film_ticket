<template>
  <div class="register-wrapper">
    <!-- 添加动态背景元素 -->
    <div class="floating-circles">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>
    
    <div class="register-container" :class="{ 'appear': showContent }">
      <div class="register-header">
        <h2 class="fade-in-down">欢 迎 注 册</h2>
        <div class="underline"></div>
      </div>

      <!-- 角色选择开关 -->
      <div class="role-switch fade-in-up">
        <el-radio-group v-model="selectedRole" size="large">
          <el-radio-button label="USER">用户注册</el-radio-button>
          <el-radio-button label="CINEMA">影院注册</el-radio-button>
        </el-radio-group>
      </div>

      <!-- 通用注册表单 -->
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-position="top" class="fade-in-up">
        <el-form-item prop="username" label="账号">
          <el-input 
            :prefix-icon="User" 
            size="large" 
            v-model="data.form.username" 
            placeholder="请输入账号"
            maxlength="20"
            show-word-limit
            class="custom-input"
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
            class="custom-input"
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
            class="custom-input"
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
            class="custom-input"
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
              placeholder="请输入手机号(选填)"
              maxlength="11"
              class="custom-input"
            >
              <template #prepend>
                <div class="input-label">手机</div>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="email" :rules="data.rules.email">
            <el-input 
              :prefix-icon="Message" 
              size="large" 
              v-model="data.form.email" 
              placeholder="请输入邮箱"
              class="custom-input"
            >
              <template #prepend>
                <div class="input-label required">邮箱</div>
              </template>
              <template #append>
                <el-button type="primary" @click="sendEmailCode" :disabled="emailCodeTimer > 0 || sendingCode" :loading="sendingCode">
                  {{ emailCodeTimer > 0 ? `${emailCodeTimer}秒后重发` : (sendingCode ? '发送中...' : '发送验证码') }}
                </el-button>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="emailCode" :rules="data.rules.emailCode">
            <div class="verification-code-container">
              <el-input 
                :prefix-icon="Key" 
                size="large" 
                v-model="data.form.emailCode" 
                placeholder="请输入邮箱验证码"
                class="custom-input"
              >
                <template #prepend>
                  <div class="input-label required">验证码</div>
                </template>
              </el-input>
            </div>
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
              class="custom-input"
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
              class="custom-input"
            >
              <template #prepend>
                <div class="input-label required">邮箱</div>
              </template>
              <template #append>
                <el-button type="primary" @click="sendEmailCode" :disabled="emailCodeTimer > 0 || sendingCode" :loading="sendingCode">
                  {{ emailCodeTimer > 0 ? `${emailCodeTimer}秒后重发` : (sendingCode ? '发送中...' : '发送验证码') }}
                </el-button>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="emailCode" :rules="data.rules.emailCode">
            <div class="verification-code-container">
              <el-input 
                :prefix-icon="Key" 
                size="large" 
                v-model="data.form.emailCode" 
                placeholder="请输入邮箱验证码"
                class="custom-input"
              >
                <template #prepend>
                  <div class="input-label required">验证码</div>
                </template>
              </el-input>
            </div>
          </el-form-item>
          <el-form-item prop="address">
            <el-input 
              :prefix-icon="Location" 
              size="large" 
              v-model="data.form.address" 
              placeholder="请输入地址"
              class="custom-input"
            >
              <template #prepend>
                <div class="input-label required">地址</div>
              </template>
              <template #append>
                <el-button @click="showMapDialog = true">地图选择</el-button>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="leader">
            <el-input 
              :prefix-icon="User" 
              size="large" 
              v-model="data.form.leader" 
              placeholder="请输入负责人姓名"
              class="custom-input"
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
              class="custom-input"
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
        <div class="register-actions fade-in">
          <el-link type="primary" @click="goToLogin" class="custom-link">已有账号？请登录</el-link>
        </div>
      </el-form>
    </div>
  </div>

  <!-- 地图选择对话框 -->
  <el-dialog
    v-model="showMapDialog"
    title="选择影院位置"
    width="800px"
    destroy-on-close
    class="custom-dialog"
  >
    <div class="map-container">
      <p class="map-tip">请在地图上搜索或点击选择您影院的准确位置</p>
      <BaiduMap
        ref="mapRef"
        :height="500"
        :selectable="true"
        @select-address="handleSelectAddress"
      />
    </div>
    <div v-if="selectedLocation" class="selected-location">
      <p><strong>已选位置：</strong>{{ selectedLocation.title }}</p>
      <p><strong>详细地址：</strong>{{ selectedLocation.address }}</p>
    </div>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="showMapDialog = false" class="cancel-btn">取消</el-button>
        <el-button type="primary" @click="confirmLocation" class="confirm-btn">确认选择</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { reactive, ref, watch, onMounted, onBeforeUnmount } from "vue";
import { User, Lock, Phone, Message, Avatar, Location, Document, Plus, Key } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import BaiduMap from '@/components/BaiduMap.vue';
import axios from "axios";

const baseUrl = import.meta.env.VITE_BASE_URL;

// 文件上传相关
const frontFileList = ref([]);
const backFileList = ref([]);
const certificateFileList = ref([]);

// 角色选择
const selectedRole = ref('USER');

// 动画相关
const showContent = ref(false);

// 地图相关
const showMapDialog = ref(false);
const mapRef = ref(null);
const selectedLocation = ref(null);

const emailCodeTimer = ref(0);
const emailCodeInterval = ref(null);

// 添加加载状态变量
const sendingCode = ref(false);

onMounted(() => {
  // 添加延迟展示内容的效果
  setTimeout(() => {
    showContent.value = true;
  }, 300);
  
  // 检查是否有来自登录页面的转场状态
  const fromLogin = sessionStorage.getItem('from_login');
  if (fromLogin) {
    // 移除标记
    sessionStorage.removeItem('from_login');
  }
});

// 监听角色变化，重置表单
watch(selectedRole, (newVal) => {
  data.form = { role: newVal };
  formRef.value?.clearValidate();
  
  // 重置验证规则
  if (newVal === 'USER') {
    data.rules.email = [
      { required: true, message: '请输入邮箱', trigger: 'blur' }, 
      { validator: validateEmail, trigger: 'blur' }
    ];
    // 用户类型手机号为可选项，只验证格式
    data.rules.phone = [{ validator: validatePhone, trigger: 'blur' }];
    data.rules.emailCode = [{ required: true, message: '请输入邮箱验证码', trigger: 'blur' }];
  } else if (newVal === 'CINEMA') {
    data.rules.email = [
      { required: true, message: '请输入邮箱', trigger: 'blur' }, 
      { validator: validateEmail, trigger: 'blur' }
    ];
    data.rules.emailCode = [{ required: true, message: '请输入邮箱验证码', trigger: 'blur' }];
    // 影院类型手机号为必填项
    data.rules.phone = [
      { required: true, message: '请输入联系电话', trigger: 'blur' },
      { validator: validatePhone, trigger: 'blur' }
    ];
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
    callback() // 如果为空，直接通过验证
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
    // 初始状态下根据当前角色设置手机号验证规则
    phone: selectedRole.value === 'USER' 
      ? [{ validator: validatePhone, trigger: 'blur' }]  // 用户角色：可选
      : [
          { required: true, message: '请输入联系电话', trigger: 'blur' },  // 影院角色：必填
          { validator: validatePhone, trigger: 'blur' }
        ],
    email: [
      { required: true, message: '请输入邮箱', trigger: 'blur' },
      { validator: validateEmail, trigger: 'blur' }
    ],
    emailCode: [{ required: true, message: '请输入邮箱验证码', trigger: 'blur' }],
    verificationCode: [
      { required: true, message: '请输入验证码', trigger: 'blur' },
      { min: 6, max: 6, message: '验证码长度为6位', trigger: 'blur' }
    ]
  }
})

const formRef = ref()

// 发送邮箱验证码 - 使用直接的axios请求绕过拦截器
const sendEmailCode = () => {
  if (!data.form.email) {
    ElMessage.error('请先输入邮箱')
    return
  }
  
  const emailPattern = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/
  if (!emailPattern.test(data.form.email)) {
    ElMessage.error('请输入正确的邮箱格式')
    return
  }
  
  // 设置发送中状态
  sendingCode.value = true
  
  // 显示加载提示
  const loadingMessage = ElMessage({
    type: 'info',
    message: '正在发送验证码，请稍候...',
    duration: 0
  })
  
  // 添加调试日志 - 记录请求开始
  console.log('发送验证码请求准备发送:', data.form.email, '时间:', new Date().toLocaleTimeString())
  
  // 使用axios而非request直接发送请求
  const apiUrl = `${baseUrl}/email/sendVerificationCode`;
  console.log('使用直接axios请求，绕过拦截器:', apiUrl);
  
  axios.post(apiUrl, { email: data.form.email })
    .then(res => {
      // 添加调试日志 - 记录成功响应
      console.log('验证码请求返回(直接请求):', res.status, res.data, '响应时间:', new Date().toLocaleTimeString())
      
      // 关闭加载提示
      loadingMessage.close()
      sendingCode.value = false
      
      const response = res.data;
      if (response.code === '200') {
        ElMessage({
          type: 'success',
          message: '验证码已发送到您的邮箱，请注意查收',
          duration: 3000
        })
        // 开始倒计时
        emailCodeTimer.value = 60
        emailCodeInterval.value = setInterval(() => {
          emailCodeTimer.value--
          if (emailCodeTimer.value <= 0) {
            clearInterval(emailCodeInterval.value)
          }
        }, 1000)
      } else {
        // 添加详细错误信息
        console.error('验证码发送失败 - 业务错误:', response.msg, response)
        ElMessage({
          type: 'error',
          message: response.msg || '验证码发送失败',
          duration: 5000
        })
      }
    })
    .catch(err => {
      // 添加详细错误日志
      console.error('验证码发送请求异常(直接请求):', err)
      if (err.response) {
        console.error('响应状态:', err.response.status, '响应数据:', err.response.data)
      }
      
      // 关闭加载提示
      loadingMessage.close()
      sendingCode.value = false
      
      ElMessage({
        type: 'error',
        message: err.response?.data?.msg || '验证码发送失败，请稍后重试',
        duration: 5000
      })
    })
}

// 组件卸载时清除定时器
onBeforeUnmount(() => {
  if (emailCodeInterval.value) {
    clearInterval(emailCodeInterval.value)
  }
})

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
      // 移除对手机号的必填检查，仅用户类型影院需要必填手机号
      if (selectedRole.value === 'CINEMA' && !data.form.phone) missingFields.push('联系电话');
      if (!data.form.email) missingFields.push('邮箱');
      if (!data.form.emailCode) missingFields.push('邮箱验证码');
      
      // 影院特有字段检查
      if (selectedRole.value === 'CINEMA') {
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
      
      // 创建注册请求数据，包含邮箱验证码
      const registerData = {
        ...data.form,
        verificationCode: data.form.emailCode // 添加邮箱验证码
      };
      
      request.post('/register', registerData).then(res => {
        if (res.code === '200') {
          ElMessage({
            type: 'success',
            message: selectedRole.value === 'USER' ? 
              '注册成功，即将跳转到登录页面' : 
              '注册成功，请等待管理员审核后登录',
            duration: 2000
          })
          
          // 设置离场动画
          showContent.value = false;
          
          // 设置登录页转场标记
          sessionStorage.setItem('from_register', 'true');
          
          setTimeout(() => {
            router.push('/login')
          }, 1000)
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

// 处理地图选择的地址
const handleSelectAddress = (address) => {
  selectedLocation.value = address;
};

// 确认选择的地址
const confirmLocation = () => {
  if (selectedLocation.value) {
    data.form.address = selectedLocation.value.address;
    // 不再存储经纬度信息
    showMapDialog.value = false;
  } else {
    ElMessage.warning('请先在地图上选择位置');
  }
};

// 在打开地图对话框时，如果已有地址，则自动搜索
watch(showMapDialog, (val) => {
  if (val && data.form.address && mapRef.value) {
    // 延迟一点时间确保地图已加载
    setTimeout(() => {
      mapRef.value.search(data.form.address);
    }, 500);
  }
});

// 添加平滑过渡到登录页面的方法
const goToLogin = () => {
  // 设置动画离开效果
  showContent.value = false;
  
  // 设置登录页转场标记
  sessionStorage.setItem('from_register', 'true');
  
  // 延迟导航，等待动画完成
  setTimeout(() => {
    router.push('/login');
  }, 500);
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
  background: rgba(103, 194, 58, 0.4);
  top: 10%;
  left: 20%;
  animation: float1 15s ease-in-out infinite;
}

.circle-2 {
  width: 120px;
  height: 120px;
  background: rgba(64, 158, 255, 0.3);
  bottom: 20%;
  right: 15%;
  animation: float2 18s ease-in-out infinite;
}

.circle-3 {
  width: 180px;
  height: 180px;
  background: rgba(255, 102, 0, 0.2);
  bottom: 40%;
  left: 30%;
  animation: float3 20s ease-in-out infinite;
}

@keyframes float1 {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(20px, 30px) rotate(5deg); }
  50% { transform: translate(-10px, -15px) rotate(-5deg); }
  75% { transform: translate(-20px, 10px) rotate(3deg); }
}

@keyframes float2 {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(-25px, 10px) rotate(-8deg); }
  50% { transform: translate(20px, -10px) rotate(8deg); }
  75% { transform: translate(10px, 20px) rotate(-4deg); }
}

@keyframes float3 {
  0%, 100% { transform: translate(0, 0) rotate(0deg); }
  25% { transform: translate(15px, -20px) rotate(3deg); }
  50% { transform: translate(-20px, 10px) rotate(-3deg); }
  75% { transform: translate(-10px, -15px) rotate(5deg); }
}

.register-container {
  width: 500px;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  padding: 40px;
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  margin: 20px 0;
  transform: translateY(30px);
  opacity: 0;
  transition: all 0.6s cubic-bezier(0.68, -0.55, 0.27, 1.55);
  max-height: 80vh;
  overflow-y: auto;
}

.register-container.appear {
  transform: translateY(0);
  opacity: 1;
}

/* 离场动画效果 */
.register-container:not(.appear) {
  transform: translateY(-30px);
  opacity: 0;
}

.register-header {
  text-align: center;
  margin-bottom: 25px;
  position: relative;
}

.register-header h2 {
  color: #333;
  font-size: 28px;
  margin: 0;
  font-weight: bold;
  letter-spacing: 1px;
}

.underline {
  width: 50px;
  height: 3px;
  background: linear-gradient(to right, #67c23a, #409eff);
  margin: 12px auto 0;
  border-radius: 3px;
  transition: width 0.4s ease;
}

.register-header:hover .underline {
  width: 100px;
}

.role-switch {
  text-align: center;
  margin-bottom: 25px;
}

.register-btn {
  width: 100%;
  height: 45px;
  font-size: 16px;
  border-radius: 25px;
  background: linear-gradient(135deg, #67c23a 0%, #409eff 100%);
  border: none;
  letter-spacing: 1px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.register-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.7s ease;
}

.register-btn:hover::before {
  left: 100%;
}

.register-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 7px 14px rgba(103, 194, 58, 0.3);
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
}

.register-actions {
  display: flex;
  justify-content: center;
  margin-top: 18px;
}

.custom-link {
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s ease;
  position: relative;
}

.custom-link::after {
  content: '';
  position: absolute;
  width: 0;
  height: 1px;
  bottom: -2px;
  left: 50%;
  background: currentColor;
  transition: all 0.3s ease;
}

.custom-link:hover {
  transform: translateY(-2px);
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.custom-link:hover::after {
  width: 100%;
  left: 0;
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

.map-container {
  width: 100%;
}

.map-tip {
  margin-bottom: 10px;
  color: #606266;
  font-size: 14px;
}

.selected-location {
  margin-top: 15px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.selected-location p {
  margin: 5px 0;
  font-size: 14px;
}

.custom-input {
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

/* 增强对话框样式 */
.custom-dialog :deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
  transform: translateY(20px);
  transition: transform 0.3s ease-out;
}

.custom-dialog :deep(.el-dialog--center) {
  transform: translateY(0);
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
  transition: all 0.3s ease;
}

.cancel-btn:hover {
  transform: translateY(-2px);
}

.confirm-btn {
  background: linear-gradient(135deg, #67c23a 0%, #409eff 100%);
  border: none;
}

.confirm-btn:hover {
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

/* 滚动条样式优化 */
.register-container::-webkit-scrollbar {
  width: 6px;
}

.register-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.register-container::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

.register-container::-webkit-scrollbar-thumb:hover {
  background: #aaa;
}

/* 验证码按钮样式 */
:deep(.el-input-group__append) {
  padding: 0;
}

:deep(.el-input-group__append button) {
  height: 100%;
  margin: 0;
  border-radius: 0 12px 12px 0;
  padding: 0 15px;
  background: linear-gradient(135deg, #67c23a 0%, #409eff 100%);
  border: none;
  transition: all 0.3s ease;
}

:deep(.el-input-group__append button:hover) {
  background: linear-gradient(135deg, #409eff 0%, #67c23a 100%);
}

:deep(.el-input-group__append button:disabled) {
  background: #c0c4cc;
  color: #ffffff;
  cursor: not-allowed;
}

.verification-code-container {
  display: flex;
  align-items: center;
}

.verification-code-container .el-input {
  flex: 1;
  margin-right: 10px;
}
</style>