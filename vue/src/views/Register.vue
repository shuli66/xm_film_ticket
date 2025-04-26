<template>
  <div class="register-wrapper">
    <!-- 导入雪花效果组件 -->
    <SnowEffect />
    
    <!-- 顶部导航 -->
    <div class="top-nav" :class="{ 'scrolled': isScrolled }">
      <div class="logo">
        <div class="logo-cinema"></div>
        <span class="logo-text">电影购票系统</span>
      </div>
      <div class="nav-links">
        <div class="nav-item" @click="goToHome">
          <i class="el-icon-house"></i>
          首页
        </div>
        <div class="nav-item active">
          <i class="el-icon-user-solid"></i>
          注册
        </div>
        <div class="nav-item" @click="goToLogin">
          <i class="el-icon-key"></i>
          登录
        </div>
      </div>
    </div>
    
    <!-- 主要内容区域 -->
    <div class="content-wrapper">
      <!-- 注册表单区域 -->
    <div class="register-container" :class="{ 'appear': showContent }">
      <div class="register-header">
          <h2 class="fade-in-down">欢迎注册电影购票系统</h2>
        <div class="underline"></div>
          <p class="register-subtitle fade-in">一分钟注册，畅享电影世界</p>
      </div>

        <!-- 用户角色选择 -->
      <div class="role-switch fade-in-up">
          <div 
            class="role-option" 
            :class="{ 'active': selectedRole === 'USER' }"
            @click="selectedRole = 'USER'"
          >
            <el-icon><Avatar /></el-icon>
            <span>用户注册</span>
      </div>
          <div 
            class="role-option" 
            :class="{ 'active': selectedRole === 'CINEMA' }"
            @click="selectedRole = 'CINEMA'"
          >
            <el-icon><Location /></el-icon>
            <span>影院注册</span>
          </div>
        </div>

        <!-- 注册表单 -->
        <el-form ref="formRef" :rules="data.rules" :model="data.form" class="fade-in-up">
          <!-- 通用注册信息 -->
          <el-form-item prop="username">
            <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号" class="custom-input"></el-input>
        </el-form-item>
          
          <el-form-item prop="name">
            <el-input :prefix-icon="Avatar" size="large" v-model="data.form.name" placeholder="请输入昵称" class="custom-input"></el-input>
        </el-form-item>
          
        <el-form-item prop="password">
            <el-input :prefix-icon="Lock" size="large" v-model="data.form.password" show-password placeholder="请输入密码" class="custom-input"></el-input>
        </el-form-item>
          
        <el-form-item prop="confirmPassword">
            <el-input :prefix-icon="Lock" size="large" v-model="data.form.confirmPassword" show-password placeholder="请确认密码" class="custom-input"></el-input>
        </el-form-item>

          <el-form-item prop="phone">
            <el-input :prefix-icon="Phone" size="large" v-model="data.form.phone" placeholder="请输入手机号" class="custom-input"></el-input>
          </el-form-item>
          
          <el-form-item prop="email">
            <el-input :prefix-icon="Message" size="large" v-model="data.form.email" placeholder="请输入邮箱" class="custom-input"></el-input>
          </el-form-item>
          
          <!-- 验证方式切换 - 改进UI -->
          <div class="verification-switch">
            <span class="verification-label">验证方式：</span>
            <div class="verification-options">
              <div 
                class="verification-option" 
                :class="{ 'active': verificationType === 'captcha' }"
                @click="changeVerificationType('captcha')"
              >
                <el-icon><Picture /></el-icon>
                <span>图片验证码</span>
              </div>
              <div 
                class="verification-option" 
                :class="{ 'active': verificationType === 'email' }"
                @click="changeVerificationType('email')"
              >
                <el-icon><Message /></el-icon>
                <span>邮箱验证码</span>
              </div>
              <div 
                class="verification-option" 
                :class="{ 'active': verificationType === 'none' }"
                @click="changeVerificationType('none')"
              >
                <el-icon><Close /></el-icon>
                <span>跳过验证</span>
              </div>
            </div>
          </div>
          
          <!-- 验证码区域 - 根据验证方式显示不同内容 -->
          <div v-if="verificationType === 'captcha'" class="captcha-container">
            <el-form-item prop="captchaCode">
              <el-input
                v-model="data.form.captchaCode"
                placeholder="请输入图片验证码"
                clearable
              >
                <template #prepend>
                  <div class="input-icon-container">
                    <el-icon><Ticket /></el-icon>
                  </div>
                </template>
                <template #append>
                  <div class="captcha-image" @click="refreshCaptcha">
                    <img v-if="captchaImage" :src="captchaImage" alt="验证码" />
                    <el-icon v-else><Loading /></el-icon>
                  </div>
                </template>
              </el-input>
            </el-form-item>
          </div>

          <div v-else-if="verificationType === 'email'" class="email-code-container">
            <el-form-item prop="emailCode">
              <el-input
                v-model="data.form.emailCode"
                placeholder="请输入邮箱验证码"
                clearable
              >
                <template #prepend>
                  <div class="input-icon-container">
                    <el-icon><Ticket /></el-icon>
                  </div>
                </template>
                <template #append>
                  <el-button
                    :disabled="emailCodeTimer > 0"
                    @click="sendEmailCode"
                    class="send-code-btn"
                  >
                    {{ emailCodeTimer > 0 ? `${emailCodeTimer}秒后重发` : '获取验证码' }}
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </div>

          <div v-else class="skip-verification-notice">
            <el-alert
              title="您已选择跳过验证，请确保提供的信息准确无误"
              type="warning"
              :closable="false"
              show-icon
            />
          </div>
          
          <!-- 影院特有信息 -->
          <template v-if="selectedRole === 'CINEMA'">
          <el-form-item prop="address">
              <div class="location-input-group">
            <el-input 
              :prefix-icon="Location" 
              size="large" 
              v-model="data.form.address" 
                  placeholder="请选择影院地址" 
                  readonly 
              class="custom-input"
                ></el-input>
                <el-button type="primary" size="large" @click="showMapDialog = true" class="map-btn">选择位置</el-button>
              </div>
          </el-form-item>

            <!-- 影院认证资料提示 -->
            <div class="notice-box">
              <div class="notice-title">
                <el-icon><InfoFilled /></el-icon>
                <span>温馨提示</span>
              </div>
              <div class="notice-content">
                影院认证资料（负责人信息、证件照片等）可在注册后登录系统再提交，系统将在资料审核通过后开放完整功能。
              </div>
            </div>
        </template>

        <el-form-item>
          <el-button size="large" type="primary" class="register-btn" @click="register">注 册</el-button>
        </el-form-item>
        <div class="register-actions fade-in">
            <el-link type="primary" @click="goToLogin" class="custom-link">已有账号？立即登录</el-link>
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
  </div>
</template>

<script setup>
import { reactive, ref, watch, onMounted, onBeforeUnmount } from "vue";
import { User, Lock, Phone, Message, Avatar, Location, Document, Plus, Key, InfoFilled, Loading, Picture, RefreshRight, CircleClose, Close } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import BaiduMap from '@/components/BaiduMap.vue';
import axios from "axios";
import { useRouter } from 'vue-router'
import { baseApiUrl } from '@/utils/api'
import SnowEffect from "@/components/SnowEffect.vue";

const baseUrl = import.meta.env.VITE_BASE_URL;

// 文件上传相关
const frontFileList = ref([]);
const backFileList = ref([]);
const certificateFileList = ref([]);

// 角色选择
const selectedRole = ref('USER');

// 动画相关
const showContent = ref(false);
const isScrolled = ref(false);

// 地图相关
const showMapDialog = ref(false);
const mapRef = ref(null);
const selectedLocation = ref(null);

const emailCodeTimer = ref(0);
const emailCodeInterval = ref(null);

// 添加加载状态变量
const sendingCode = ref(false);

// 验证方式切换（改为用字符串表示不同验证方式）
const verificationType = ref('captcha'); // 'captcha'=图片验证码, 'email'=邮箱验证码, 'none'=跳过验证
const captchaImage = ref('');
const captchaToken = ref('');

// 添加 goToHome 方法
const goToHome = () => {
  router.push('/front/home');
}

// 监听滚动事件，更新导航栏样式
const setupScrollAnimation = () => {
  const fadeInOnScroll = () => {
    isScrolled.value = window.scrollY > 20;
  };
  
  window.addEventListener('scroll', fadeInOnScroll);
  // 初始检查
  setTimeout(fadeInOnScroll, 500);
};

onMounted(() => {
  // 添加延迟展示内容的效果
  setTimeout(() => {
    showContent.value = true;
  }, 300);
  
  // 设置滚动动画
  setupScrollAnimation();
  
  // 默认使用图片验证码，立即加载图片验证码
  refreshCaptcha();
  
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
    data.rules.emailCode = [{ required: verificationType.value === 'email', message: '请输入邮箱验证码', trigger: 'blur' }];
    data.rules.captchaCode = [{ required: verificationType.value === 'captcha', message: '请输入图片验证码', trigger: 'blur' }];
  } else if (newVal === 'CINEMA') {
    data.rules.email = [
      { required: true, message: '请输入邮箱', trigger: 'blur' }, 
      { validator: validateEmail, trigger: 'blur' }
    ];
    data.rules.emailCode = [{ required: verificationType.value === 'email', message: '请输入邮箱验证码', trigger: 'blur' }];
    data.rules.captchaCode = [{ required: verificationType.value === 'captcha', message: '请输入图片验证码', trigger: 'blur' }];
    // 影院类型手机号为必填项
    data.rules.phone = [
      { required: true, message: '请输入联系电话', trigger: 'blur' },
      { validator: validatePhone, trigger: 'blur' }
    ];
    data.rules.address = [{ required: true, message: '请输入地址', trigger: 'blur' }];
    // 以下认证资料不再是必填项
    data.rules.leader = [];
    data.rules.code = [];
    data.rules.front = [];
    data.rules.back = [];
    data.rules.certificate = [];
  }
});

// 监听验证方式变化，更新验证规则
watch(verificationType, (value) => {
  // 更新验证规则的required属性
  data.rules.emailCode = [{ required: value === 'email', message: '请输入邮箱验证码', trigger: 'blur' }];
  data.rules.captchaCode = [{ required: value === 'captcha', message: '请输入图片验证码', trigger: 'blur' }];
  
  // 清除验证状态
  formRef.value?.clearValidate(['emailCode', 'captchaCode']);
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
    emailCode: [{ required: verificationType.value === 'email', message: '请输入邮箱验证码', trigger: 'blur' }],
    captchaCode: [{ required: verificationType.value === 'captcha', message: '请输入图片验证码', trigger: 'blur' }]
  }
})

const formRef = ref()

// 发送邮箱验证码 - 使用直接的axios请求绕过拦截器
const sendEmailCode = () => {
  if (!data.form.email) {
    ElMessage.error('请先输入邮箱');
    return;
  }
  
  const emailPattern = /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/;
  if (!emailPattern.test(data.form.email)) {
    ElMessage.error('请输入正确的邮箱格式');
    return;
  }
  
  // 设置发送中状态
  sendingCode.value = true;
  
  // 显示加载提示
  const loadingMessage = ElMessage({
    type: 'info',
    message: '正在发送验证码，请稍候...',
    duration: 0
  });
  
  // 直接使用axios发送请求，绕过请求拦截器，避免token验证问题
  axios.post(`${baseUrl}/email/sendVerificationCode`, { email: data.form.email })
    .then(response => {
      // 关闭加载提示
      loadingMessage.close();
      sendingCode.value = false;
      
      const res = response.data;
      if (res.code === '200') {
        ElMessage({
          type: 'success',
          message: '验证码已发送到您的邮箱，请注意查收',
          duration: 3000
        });
        // 开始倒计时
        emailCodeTimer.value = 60;
        emailCodeInterval.value = setInterval(() => {
          emailCodeTimer.value--;
          if (emailCodeTimer.value <= 0) {
            clearInterval(emailCodeInterval.value);
          }
        }, 1000);
      } else {
        console.error('验证码发送失败:', res.msg);
        ElMessage({
          type: 'error',
          message: res.msg || '验证码发送失败',
          duration: 5000
        });
      }
    })
    .catch(err => {
      console.error('验证码发送请求异常:', err);
      
      // 关闭加载提示
      loadingMessage.close();
      sendingCode.value = false;
      
      ElMessage({
        type: 'error',
        message: '验证码发送失败，请稍后重试',
        duration: 5000
      });
    });
}

// 组件卸载时清除定时器
onBeforeUnmount(() => {
  if (emailCodeInterval.value) {
    clearInterval(emailCodeInterval.value)
  }
})

// 切换验证方式
const changeVerificationType = (type) => {
  verificationType.value = type;
  // 切换验证方式时重置验证码
  data.form.captchaCode = '';
  data.form.emailCode = '';
  
  // 如果切换到图片验证码，刷新验证码
  if (type === 'captcha') {
    refreshCaptcha();
  }
};

// 刷新图片验证码
const refreshCaptcha = () => {
  // 设置加载中状态
  captchaImage.value = '';
  
  // 显示加载提示
  const loadingMessage = ElMessage({
    type: 'info',
    message: '正在获取验证码...',
    duration: 1000
  });
  
  // 请求新的验证码
  axios.get(`${baseUrl}/email/captcha`)
    .then(response => {
      const res = response.data;
      if (res.code === '200') {
        captchaImage.value = res.data.captchaImage;
        captchaToken.value = res.data.captchaToken;
      } else {
        ElMessage.error('获取验证码失败，请重试');
      }
    })
    .catch(err => {
      console.error('获取图片验证码失败:', err);
      ElMessage.error('获取验证码失败，请重试');
    });
}

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
      if (selectedRole.value === 'CINEMA' && !data.form.phone) missingFields.push('联系电话');
      if (!data.form.email) missingFields.push('邮箱');
      
      // 根据验证方式检查验证码
      if (verificationType.value === 'email') {
        if (!data.form.emailCode) missingFields.push('邮箱验证码');
      } else if (verificationType.value === 'captcha') {
        if (!data.form.captchaCode) missingFields.push('图片验证码');
      }
      // 当选择"跳过验证"时，不检查验证码
      
      // 影院特有必填字段检查 - 仅地址
      if (selectedRole.value === 'CINEMA') {
        if (!data.form.address) missingFields.push('地址');
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
      
      // 创建注册请求数据
      const registerData = {
        ...data.form,
        status: selectedRole.value === 'CINEMA' ? 'PENDING_VERIFICATION' : 'NORMAL', // 影院默认为待认证状态
      };
      
      // 根据验证方式添加不同类型的验证码
      if (verificationType.value === 'email') {
        registerData.verificationCode = data.form.emailCode; // 添加邮箱验证码
        registerData.verificationType = 'EMAIL'; // 标识使用邮箱验证码
      } else if (verificationType.value === 'captcha') {
        registerData.verificationCode = data.form.captchaCode; // 添加图片验证码
        registerData.verificationType = 'CAPTCHA'; // 标识使用图片验证码
        registerData.captchaToken = captchaToken.value; // 使用token而不是id
      } else {
        // 跳过验证的情况
        registerData.verificationType = 'NONE'; // 标识跳过验证
      }
      
      // 显示加载提示
      const loadingMessage = ElMessage({
        type: 'info',
        message: '正在提交注册信息，请稍候...',
        duration: 0
      });
      
      // 发送注册请求
      request.post('/register', registerData).then(res => {
        // 关闭加载提示
        loadingMessage.close();
        
        if (res.code === '200') {
          ElMessage({
            type: 'success',
            message: selectedRole.value === 'USER' ? 
              '注册成功，即将跳转到登录页面' : 
              '注册成功，请登录系统完善影院认证资料后等待审核',
            duration: 2000
          });
          
          // 设置离场动画
          showContent.value = false;
          
          // 设置登录页转场标记
          sessionStorage.setItem('from_register', 'true');
          
          setTimeout(() => {
            router.push('/login');
          }, 1000);
        } else {
          ElMessage({
            type: 'error',
            message: res.msg || '注册失败',
            duration: 3000
          });
          
          // 如果是验证码错误且当前使用的是图片验证码，则刷新验证码
          if (verificationType.value === 'captcha') {
            refreshCaptcha();
          }
        }
      }).catch(err => {
        // 关闭加载提示
        loadingMessage.close();
        
        console.error('注册请求异常:', err);
        ElMessage({
          type: 'error',
          message: '系统错误，请稍后重试',
          duration: 3000
        });
      });
    } else {
      // 表单验证未通过，显示提示
      ElMessage({
        type: 'warning',
        message: '请正确填写所有必填项',
        duration: 3000
      });
    }
  });
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
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: cover;
  background-attachment: fixed;
  position: relative;
  overflow-x: hidden;
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

/* 顶部导航 */
.top-nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 50px;
  z-index: 1000;
  transition: all 0.3s ease;
  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.5), transparent);
}

.top-nav.scrolled {
  background: rgba(0, 0, 0, 0.85);
  padding: 10px 50px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px);
}

.logo {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.logo-cinema {
  width: 36px;
  height: 36px;
  background-image: url("@/assets/imgs/logo.png");
  background-size: contain;
  background-repeat: no-repeat;
  margin-right: 10px;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
  color: #fff;
  letter-spacing: 1px;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 30px;
}

.nav-item {
  color: rgba(255, 255, 255, 0.8);
  font-size: 16px;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.nav-item:hover, .nav-item.active {
  color: #fff;
  transform: translateY(-2px);
}

.nav-item.active {
  color: #fff;
  font-weight: 500;
  position: relative;
}

.nav-item.active::after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 50%;
  transform: translateX(-50%);
  width: 20px;
  height: 2px;
  background-color: #fff;
  border-radius: 1px;
}

/* 内容区域 */
.content-wrapper {
  display: flex;
  min-height: 100vh;
  justify-content: center;
  align-items: center;
  padding: 100px 20px 50px;
}

.register-container {
  background: rgba(255, 255, 255, 0.95);
  width: 100%;
  max-width: 550px;
  padding: 45px 50px;
  border-radius: 15px;
  box-shadow: 0 15px 25px rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  opacity: 0;
  transform: translateY(30px);
  transition: all 0.5s ease-out;
}

.register-container.appear {
  opacity: 1;
  transform: translateY(0);
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.register-header h2 {
  font-size: 28px;
  font-weight: 600;
  color: #333;
  margin-bottom: 15px;
}

.underline {
  width: 70px;
  height: 3px;
  background: linear-gradient(to right, #409eff, #67c23a);
  margin: 10px auto 15px;
  border-radius: 3px;
}

.register-subtitle {
  font-size: 16px;
  color: #666;
  margin-bottom: 20px;
}

/* 角色选择器 */
.role-switch {
  display: flex;
  gap: 20px;
  margin-bottom: 35px;
  padding: 0 15px;
}

.role-option {
  flex: 1;
  padding: 18px;
  text-align: center;
  background: rgba(64, 158, 255, 0.1);
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.role-option:hover {
  background: rgba(64, 158, 255, 0.2);
}

.role-option.active {
  background: rgba(64, 158, 255, 0.15);
  border-color: #409eff;
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(64, 158, 255, 0.2);
}

.role-option i {
  font-size: 24px;
  color: #409eff;
}

.role-option span {
  font-size: 16px;
  font-weight: 500;
  color: #333;
}

/* 表单样式 */
.custom-input {
  --el-input-height: 52px;
  margin-bottom: 8px;
}

.custom-input :deep(.el-input__wrapper) {
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.custom-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 12px rgba(64, 158, 255, 0.2);
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409eff inset, 0 2px 15px rgba(64, 158, 255, 0.2);
}

.custom-input :deep(.el-input__inner) {
  height: 52px;
  font-size: 15px;
}

.custom-input :deep(.el-input__prefix) {
  font-size: 18px;
  color: #999;
}

.register-btn {
  width: 100%;
  margin-top: 20px;
  height: 54px;
  font-size: 18px;
  font-weight: 500;
  letter-spacing: 2px;
  border-radius: 10px;
  background: linear-gradient(135deg, #409eff, #67c23a);
  border: none;
  transition: all 0.3s ease;
}

.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 15px rgba(64, 158, 255, 0.3);
  background: linear-gradient(135deg, #4caaff, #78d348);
}

.register-actions {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.custom-link {
  font-size: 16px;
  position: relative;
  transition: all 0.3s ease;
  margin: 0 15px;
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

/* 验证码输入组 */
.code-input-group {
  display: flex;
  gap: 12px;
}

.code-input-group .custom-input {
  flex: 1;
}

.send-code-btn {
  width: 150px;
  height: 52px;
  border-radius: 10px;
}

/* 地址输入组 */
.location-input-group {
  display: flex;
  gap: 12px;
}

.location-input-group .custom-input {
  flex: 1;
}

.map-btn {
  width: 110px;
  height: 52px;
  border-radius: 10px;
}

/* 提示框样式 */
.notice-box {
  background: rgba(255, 236, 220, 0.8);
  border-left: 4px solid #ff9900;
  padding: 15px;
  border-radius: 8px;
  margin: 20px 0;
}

.notice-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  color: #ff9900;
  margin-bottom: 8px;
}

.notice-content {
  color: #666;
  font-size: 14px;
  line-height: 1.5;
}

/* 地图对话框 */
.custom-dialog :deep(.el-dialog__body) {
  padding: 20px;
}

.map-tip {
  margin-bottom: 15px;
  color: #666;
}

.selected-location {
  margin-top: 15px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 8px;
}

.selected-location p {
  margin: 5px 0;
  color: #333;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 15px;
}

.cancel-btn, .confirm-btn {
  padding: 10px 20px;
  border-radius: 6px;
}

.confirm-btn {
  background: #409eff;
  color: white;
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
  0%, 100% { transform: translate(0, 0); }
  25% { transform: translate(-20px, 15px); }
  50% { transform: translate(15px, 25px); }
  75% { transform: translate(20px, -15px); }
}

@keyframes float2 {
  0%, 100% { transform: translate(0, 0); }
  25% { transform: translate(15px, -20px); }
  50% { transform: translate(-20px, -15px); }
  75% { transform: translate(-10px, 15px); }
}

@keyframes float3 {
  0%, 100% { transform: translate(0, 0); }
  25% { transform: translate(15px, 15px); }
  50% { transform: translate(-15px, 20px); }
  75% { transform: translate(-20px, -10px); }
}

/* 响应式适配 */
@media (max-width: 768px) {
  .top-nav {
    padding: 15px 20px;
  }

  .top-nav.scrolled {
    padding: 10px 20px;
  }
  
  .logo-text {
    font-size: 18px;
  }
  
  .nav-item {
    font-size: 14px;
    padding: 5px;
  }
  
  .register-container {
    padding: 30px 25px;
    max-width: 95%;
  }

  .register-header h2 {
    font-size: 24px;
  }

  .code-input-group {
    flex-direction: column;
  }

  .send-code-btn {
    width: 100%;
    margin-top: 5px;
  }

  .location-input-group {
    flex-direction: column;
  }
  
  .map-btn {
    width: 100%;
    margin-top: 5px;
  }
}

/* 验证方式切换改进样式 */
.verification-switch {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
}

.verification-label {
  margin-bottom: 12px;
  color: #606266;
  font-size: 14px;
}

.verification-options {
  display: flex;
  gap: 15px;
}

.verification-option {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 12px;
  border-radius: 8px;
  background-color: #f5f7fa;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.verification-option .el-icon {
  font-size: 20px;
  color: #909399;
}

.verification-option span {
  font-size: 14px;
  color: #606266;
}

.verification-option:hover {
  background-color: #edf2fc;
}

.verification-option.active {
  background-color: #ecf5ff;
  border-color: #409eff;
}

.verification-option.active .el-icon,
.verification-option.active span {
  color: #409eff;
}

/* 改进图片验证码样式 */
.captcha-image-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.captcha-image {
  width: 120px;
  height: 40px;
  border-radius: 4px;
  overflow: hidden;
  border: 1px solid #dcdfe6;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
  position: relative;
}

.captcha-image:hover {
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
  transform: translateY(-1px);
}

.captcha-image:hover .refresh-overlay {
  opacity: 1;
}

.refresh-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.refresh-overlay .el-icon {
  font-size: 24px;
  color: white;
  filter: drop-shadow(0 1px 2px rgba(0, 0, 0, 0.3));
}

.captcha-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.refresh-tip {
  margin-top: 6px;
  font-size: 12px;
  color: #909399;
  cursor: pointer;
}

.refresh-tip:hover {
  color: #409eff;
}

.captcha-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  width: 100%;
  color: #409eff;
}

/* 跳过验证提示 */
.skip-verification-notice {
  margin-top: 10px;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 8px;
}
</style>