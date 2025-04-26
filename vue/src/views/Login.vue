<template>
  <div class="login-wrapper">
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
        <div class="nav-item" @click="goToRegister">
          <i class="el-icon-user-solid"></i>
          注册
        </div>
        <div class="nav-item active">
          <i class="el-icon-key"></i>
          登录
        </div>
      </div>
    </div>
    
    <!-- 全屏登录过渡加载动画 -->
    <div class="login-transition" v-if="showLoginTransition">
      <div class="login-transition-content">
        <div class="login-spinner"></div>
        <div class="welcome-message">
          <h3>欢迎回来，{{ welcomeMessage }}</h3>
          <p>正在加载中...</p>
        </div>
      </div>
    </div>
    
    <!-- 主要内容区域 -->
    <div class="content-wrapper">
      <!-- 登录表单区域 -->
      <div class="login-container" :class="{ 'appear': showContent }">
        <div class="login-header">
          <h2 class="fade-in-down">欢迎登录电影购票系统</h2>
          <div class="underline"></div>
          <p class="login-subtitle fade-in">享受精彩电影，从这里开始</p>
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
            <el-button size="large" type="primary" class="login-btn" @click="handleLogin" :class="{ 'button-loading': data.loading }">
              <span v-if="!data.loading">登 录</span>
              <span v-else class="loading-spinner"></span>
            </el-button>
          </el-form-item>
          <div class="login-actions fade-in">
            <el-link type="primary" @click="showForgetPassword" class="custom-link">忘记密码？</el-link>
            <el-link type="primary" @click="goToRegister" class="custom-link">注册账号</el-link>
          </div>
        </el-form>
        
        <!-- 下滑提示 -->
        <div class="scroll-hint fade-in" @click="scrollToExtendedContent">
          <div class="hint-text">下滑探索更多精彩内容</div>
          <div class="scroll-indicator">
            <div class="scroll-dot"></div>
            <div class="scroll-dot"></div>
            <div class="scroll-dot"></div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 下滑扩展内容区域 -->
    <div class="extended-content">
      <!-- 系统特色介绍 -->
      <div class="section features">
        <h3 class="section-title">我们的特色</h3>
        <div class="feature-cards">
          <div class="feature-card">
            <div class="feature-icon ticket-icon"></div>
            <h4>便捷购票</h4>
            <p>随时随地，轻松选座，一键购票</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon discount-icon"></div>
            <h4>优惠活动</h4>
            <p>会员专享折扣，节假日特惠</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon preview-icon"></div>
            <h4>新片速递</h4>
            <p>第一时间获取最新影片资讯</p>
          </div>
        </div>
      </div>
      
      <!-- 用户评价 -->
      <div class="section testimonials">
        <h3 class="section-title">用户评价</h3>
        <div class="testimonial-cards">
          <div class="testimonial-card" v-for="(testimonial, index) in testimonials" :key="index">
            <div class="user-avatar">
              <img :src="testimonial.avatar" :alt="testimonial.name">
            </div>
            <div class="user-name">{{ testimonial.name }}</div>
            <div class="testimonial-content">"{{ testimonial.content }}"</div>
            <div class="testimonial-rating">
              <i class="star-icon" v-for="n in 5" :key="n" :class="{ 'filled': n <= testimonial.rating }"></i>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 页脚 -->
      <footer class="page-footer">
        <div class="footer-content">
          <div class="footer-main">
            <div class="footer-section">
              <div class="footer-logo">
                <div class="logo-cinema"></div>
                <span class="logo-text">电影购票系统</span>
              </div>
              <div class="footer-desc">提供最新、最全的电影资讯和在线购票服务</div>
              <div class="footer-social-icons">
                <div class="social-icon weixin"></div>
                <div class="social-icon weibo"></div>
                <div class="social-icon douyin"></div>
              </div>
            </div>
            
            <div class="footer-section">
              <h4 class="footer-heading">电影资讯</h4>
              <ul class="footer-links">
                <li><a href="#" @click.prevent="navigateTo('/front/film?category=playing')">正在热映</a></li>
                <li><a href="#" @click.prevent="navigateTo('/front/film?category=upcoming')">即将上映</a></li>
                <li><a href="#" @click.prevent="navigateTo('/front/charts')">电影排行榜</a></li>
                <li><a href="#" @click.prevent="navigateTo('/front/home')">电影推荐</a></li>
              </ul>
            </div>
            
            <div class="footer-section">
              <h4 class="footer-heading">帮助中心</h4>
              <ul class="footer-links">
                <li><a href="#">购票指南</a></li>
                <li><a href="#">支付方式</a></li>
                <li><a href="#">退票说明</a></li>
                <li><a href="#">常见问题</a></li>
              </ul>
            </div>
            
            <div class="footer-section">
              <h4 class="footer-heading">关于我们</h4>
              <ul class="footer-links">
                <li><a href="#">公司介绍</a></li>
                <li><a href="#">联系我们</a></li>
                <li><a href="#">加入我们</a></li>
                <li><a href="#">合作伙伴</a></li>
              </ul>
            </div>
          </div>
          
          <div class="footer-bottom">
            <div class="footer-copyright">
              © 2025 电影购票系统 版权所有 | 隐私政策 | 用户协议
            </div>
            <div class="footer-cert">
              <span>证件信息: 沪ICP备xxxxxxxx号</span>
              <span>河南牧业经济学院许可证: 豫网文[xxxx]xxxx-xxx号</span>
            </div>
          </div>
        </div>
      </footer>
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

    <!-- 添加拼图滑块验证弹窗 -->
    <el-dialog
      v-model="data.showCaptcha"
      title="请完成安全验证"
      width="90%"
      max-width="400px"
      :close-on-click-modal="true"
      :close-on-press-escape="true"
      center
      class="captcha-dialog"
    >
      <div class="puzzle-captcha">
        <div class="puzzle-image">
          <div class="puzzle-bg" :style="{ backgroundColor: data.randomBackground }">
            <div 
              class="puzzle-piece" 
              :style="{ 
                left: data.puzzlePosition + 'px', 
                backgroundColor: data.randomBackground,
                top: '70px' 
              }"
            ></div>
            
            <div 
              class="puzzle-target" 
              :style="{ 
                left: data.puzzleTarget + 'px',
                top: '70px'
              }"
            ></div>
          </div>
        </div>
        
        <div class="puzzle-slider-container">
          <div class="puzzle-slider-tip">拖动滑块完成拼图</div>
          <div class="puzzle-slider-track"></div>
          <div 
            class="puzzle-slider-button"
            :class="{ 'verified': data.verified }"
            @mousedown="startDrag"
            @touchstart="startTouchDrag"
            :style="{ left: data.puzzlePosition + 'px' }"
          >
            <i :class="data.verified ? 'el-icon-check' : 'el-icon-d-arrow-right'"></i>
          </div>
        </div>
        
        <div class="puzzle-action">
          <el-button type="text" @click="refreshCaptcha" class="refresh-btn">
            <i class="el-icon-refresh"></i> 换一张
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted, onBeforeMount, onBeforeUnmount } from "vue";
import { User, Lock, Phone, ArrowDown } from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import SnowEffect from "@/components/SnowEffect.vue";

const formRef = ref()
const forgetFormRef = ref()
const showContent = ref(false);
const isScrolled = ref(false);

// 用户评价示例数据
const testimonials = ref([
  {
    name: "李明",
    avatar: "https://randomuser.me/api/portraits/men/32.jpg",
    content: "这个电影购票系统太棒了，操作简单，选座方便，还有电影评分和推荐功能！",
    rating: 5
  },
  {
    name: "王芳",
    avatar: "https://randomuser.me/api/portraits/women/44.jpg",
    content: "界面设计很美观，使用体验很流畅，支持多种支付方式，非常方便。",
    rating: 4
  },
  {
    name: "张伟",
    avatar: "https://randomuser.me/api/portraits/men/22.jpg",
    content: "影院信息非常全面，能看到很多场次，优惠活动也很多，强烈推荐！",
    rating: 5
  }
]);

// 监听滚动事件，实现动画效果
const setupScrollAnimation = () => {
  const sections = document.querySelectorAll('.section');
  
  const fadeInOnScroll = () => {
    sections.forEach(section => {
      const sectionTop = section.getBoundingClientRect().top;
      const windowHeight = window.innerHeight;
      
      if (sectionTop < windowHeight * 0.85) {
        section.classList.add('visible');
      }
    });
    
    // 更新导航栏样式
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
  
  // 在登录页清除可能存在的不合法token，解决刷新问题
  // 检查URL中是否有错误参数
  const urlParams = new URLSearchParams(window.location.search);
  if (urlParams.get('auth_error') === 'true' || urlParams.get('expired') === 'true') {
    localStorage.removeItem('xm-user');
    ElMessage.error('您的登录已过期，请重新登录');
  }
  
  // 登录页面刷新时检查和清理token状态
  // 这可以避免登录页上有效token发起不必要的API请求导致系统异常
  const currentPath = window.location.pathname;
  if (currentPath.endsWith('/login')) {
    // 清除可能的请求状态和本地存储
    localStorage.removeItem('xm-user');
    
    // 清除URL中可能存在的异常状态参数
    if (window.location.search) {
      const cleanURL = window.location.pathname;
      window.history.replaceState({}, document.title, cleanURL);
    }
  }
  
  // 确保验证码弹窗初始状态为关闭
  data.showCaptcha = false;
  
  // 检查是否有来自注册页面的转场状态
  const fromRegister = sessionStorage.getItem('from_register');
  if (fromRegister) {
    // 移除标记
    sessionStorage.removeItem('from_register');
  }
});

const data = reactive({
  form: {},
  forgetForm: {
    phone: '',
    newPassword: '',
    confirmPassword: ''
  },
  forgetVisible: false,
  showCaptcha: false,
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
  },
  verified: false,
  puzzlePosition: 0,
  puzzleTarget: 180,
  isDragging: false,
  startX: 0,
  startPos: 0,
  randomBackground: '',
  backgroundImages: ['#e74c3c', '#3498db', '#2ecc71', '#f39c12', '#9b59b6'],
  captchaLoading: false
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

const showLoginTransition = ref(false);
const welcomeMessage = ref('');

// 处理登录按钮点击
const handleLogin = () => {
  formRef.value.validate(valid => {
    if (valid) {
      // 显示验证码弹窗
      showCaptcha();
    }
  });
}

// 显示验证码弹窗
const showCaptcha = () => {
  data.showCaptcha = true;
  data.verified = false;
  refreshCaptcha();
}

// 刷新验证码
const refreshCaptcha = () => {
  data.captchaLoading = true;
  data.verified = false;
  setTimeout(() => {
    data.captchaLoading = false;
    data.puzzlePosition = 0;
    data.puzzleTarget = 100 + Math.floor(Math.random() * 150);
    data.randomBackground = data.backgroundImages[Math.floor(Math.random() * data.backgroundImages.length)];
  }, 500);
}

// 开始拖动 - 鼠标事件
const startDrag = (e) => {
  if (data.verified) return;
  data.isDragging = true;
  data.startX = e.clientX;
  data.startPos = data.puzzlePosition;
  document.addEventListener('mousemove', onDrag);
  document.addEventListener('mouseup', stopDrag);
  e.preventDefault(); // 防止选中文本
}

// 开始拖动 - 触摸事件
const startTouchDrag = (e) => {
  if (data.verified) return;
  data.isDragging = true;
  data.startX = e.touches[0].clientX;
  data.startPos = data.puzzlePosition;
  document.addEventListener('touchmove', onTouchDrag, { passive: false });
  document.addEventListener('touchend', stopTouchDrag);
  document.addEventListener('touchcancel', stopTouchDrag);
  e.preventDefault(); // 防止页面滚动
}

// 拖动中 - 鼠标事件
const onDrag = (e) => {
  if (!data.isDragging) return;
  
  let deltaX = e.clientX - data.startX;
  let newPos = data.startPos + deltaX;
  
  // 限制滑块在轨道内
  if (newPos < 0) newPos = 0;
  if (newPos > 320) newPos = 320;
  
  data.puzzlePosition = newPos;
}

// 拖动中 - 触摸事件
const onTouchDrag = (e) => {
  if (!data.isDragging) return;
  
  let deltaX = e.touches[0].clientX - data.startX;
  let newPos = data.startPos + deltaX;
  
  // 限制滑块在轨道内
  if (newPos < 0) newPos = 0;
  if (newPos > 320) newPos = 320;
  
  data.puzzlePosition = newPos;
  
  e.preventDefault(); // 防止页面滚动
}

// 停止拖动 - 鼠标事件
const stopDrag = () => {
  if (!data.isDragging) return;
  data.isDragging = false;
  document.removeEventListener('mousemove', onDrag);
  document.removeEventListener('mouseup', stopDrag);
  
  verifyPuzzlePosition();
}

// 停止拖动 - 触摸事件
const stopTouchDrag = () => {
  if (!data.isDragging) return;
  data.isDragging = false;
  document.removeEventListener('touchmove', onTouchDrag);
  document.removeEventListener('touchend', stopTouchDrag);
  document.removeEventListener('touchcancel', stopTouchDrag);
  
  verifyPuzzlePosition();
}

// 验证滑块位置
const verifyPuzzlePosition = () => {
  // 判断是否验证成功 (允许10px误差)
  if (Math.abs(data.puzzlePosition - data.puzzleTarget) < 10) {
    data.verified = true;
    data.puzzlePosition = data.puzzleTarget; // 微调到正确位置
    ElMessage.success('验证成功');
    
    // 延迟关闭弹窗并登录
    setTimeout(() => {
      data.showCaptcha = false;
      login();
    }, 800);
  } else {
    // 验证失败，滑块回到起点
    ElMessage.error('验证失败，请重试');
    data.puzzlePosition = 0;
  }
}

// 修改登录方法
const login = () => {
  data.loading = true;
  request.post('/login', data.form).then(res => {
    if (res.code === '200') {
      // 显示成功消息
      ElMessage.success('登录成功');
      
      // 先保存用户信息
      localStorage.setItem('xm-user', JSON.stringify(res.data));
      sessionStorage.setItem('just_logged_in', 'true');
      
      // 用户角色和重定向路径
      const userRole = res.data.role;
      let redirectPath = '';
      
      if (userRole === 'USER') {
        redirectPath = '/front/home';
        welcomeMessage.value = '尊敬的用户';
      } else if (userRole === 'CINEMA') {
        redirectPath = '/manager/home';
        welcomeMessage.value = '尊敬的影院管理员';
      } else if (userRole === 'ADMIN') {
        redirectPath = '/manager/adminHome';
        welcomeMessage.value = '尊敬的系统管理员';
      }
      
      // 显示登录过渡动画
      showLoginTransition.value = true;
      
      // 确保登录状态保存后再跳转
      setTimeout(() => {
        // 取消加载状态
        data.loading = false;
        
        // 延迟跳转，给过渡动画留出足够时间
        setTimeout(() => {
          showLoginTransition.value = false;
          // 在状态保存好后跳转
          router.push(redirectPath);
          
          // 检查用户通知，但不影响登录流程
          if (userRole === 'CINEMA') {
            setTimeout(() => {
              checkNotifications(res.data.id);
            }, 1000);
          }
        }, 1000); // 过渡动画显示时间
      }, 800);
    } else {
      ElMessage.error(res.msg || '登录失败');
      data.loading = false;
    }
  }).catch((err) => {
    console.error('登录请求出错:', err);
    ElMessage.error('登录异常，请稍后重试');
    data.loading = false;
  });
}

// 分离通知检查逻辑为独立函数
const checkNotifications = (userId) => {
  if (!userId) return;
  
  request.get('/notice/unread', {
    params: { userId: userId }
  }).then(noticeRes => {
    if (noticeRes.code === '200' && noticeRes.data && noticeRes.data.length > 0) {
      // 找到审核通过或拒绝的通知
      const statusNotice = noticeRes.data.find(notice => 
        notice.title === '审核通过通知' || notice.title === '审核结果通知');
      
      if (statusNotice) {
        ElMessage({
          message: `您有一条关于审核结果的通知：${statusNotice.content}`,
          type: statusNotice.title === '审核通过通知' ? 'success' : 'warning',
          duration: 5000,
          showClose: true
        });
        
        // 标记该通知为已读
        request.put(`/notice/read/${statusNotice.id}`).catch(err => {
          console.error('标记通知已读失败:', err);
        });
      }
    }
  }).catch(err => {
    console.error('获取通知失败:', err);
  });
}

// 添加平滑过渡到注册页面的方法
const goToRegister = () => {
  // 设置动画离开效果
  showContent.value = false;
  
  // 设置注册页转场标记
  sessionStorage.setItem('from_login', 'true');
  
  // 延迟导航，等待动画完成
  setTimeout(() => {
    router.push('/register');
  }, 500);
}

// 添加返回首页的方法
const goToHome = () => {
  router.push('/front/home');
}

// 平滑滚动到指定位置
const scrollToSection = (sectionId) => {
  const section = document.getElementById(sectionId);
  if (section) {
    section.scrollIntoView({ behavior: 'smooth' });
  }
}

// 跳转到其他页面
const navigateTo = (path) => {
  router.push(path);
};

// 平滑滚动到扩展内容区域
const scrollToExtendedContent = () => {
  const extendedContent = document.querySelector('.extended-content');
  if (extendedContent) {
    extendedContent.scrollIntoView({ behavior: 'smooth' });
  }
};

// 显示忘记密码弹窗
const showForgetPassword = () => {
  data.forgetVisible = true
  data.forgetForm = {
    phone: '',
    newPassword: '',
    confirmPassword: ''
  }
}

// 重置密码
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

// 组件卸载时清除事件监听
onBeforeUnmount(() => {
  document.removeEventListener('mousemove', onDrag);
  document.removeEventListener('mouseup', stopDrag);
  document.removeEventListener('touchmove', onTouchDrag);
  document.removeEventListener('touchend', stopTouchDrag);
  document.removeEventListener('touchcancel', stopTouchDrag);
});
</script>

<style scoped>
.login-wrapper {
  min-height: 100vh;
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: cover;
  background-attachment: fixed;
  position: relative;
  overflow-x: hidden;
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

/* 内容包装器 */
.content-wrapper {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 100px 20px;
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

.login-container {
  width: 480px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 20px;
  padding: 40px 50px;
  box-shadow: 0 15px 50px rgba(0, 0, 0, 0.15);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.8);
  transform: translateY(30px);
  opacity: 0;
  transition: all 0.6s cubic-bezier(0.68, -0.55, 0.27, 1.55);
  margin-bottom: 60px;
}

.login-container.appear {
  transform: translateY(0);
  opacity: 1;
}

/* 离场动画效果 */
.login-container:not(.appear) {
  transform: translateY(-30px);
  opacity: 0;
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

.login-subtitle {
  color: #666;
  font-size: 16px;
  margin-top: 10px;
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
  height: 48px;
  font-size: 16px;
  margin-top: 10px;
  border-radius: 12px;
  background: linear-gradient(135deg, #ff5f6d 0%, #ffc371 100%);
  border: none;
  letter-spacing: 2px;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.login-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 7px 14px rgba(255, 95, 109, 0.3);
  background: linear-gradient(135deg, #ff5f6d 0%, #ffc371 100%);
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

/* 下滑提示样式 */
.scroll-hint {
  text-align: center;
  margin-top: 30px;
  color: #666;
  cursor: pointer;
  position: relative;
  transition: transform 0.3s ease;
  padding: 10px;
}

.scroll-hint:hover {
  transform: translateY(5px);
}

.hint-text {
  font-size: 15px;
  margin-bottom: 15px;
  font-weight: 500;
  letter-spacing: 0.5px;
  background: linear-gradient(to right, #ff5f6d, #ffc371);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.scroll-indicator {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6px;
  animation: pulseDown 2s infinite;
}

.scroll-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: linear-gradient(to right, #ff5f6d, #ffc371);
}

@keyframes pulseDown {
  0%, 100% {
    transform: translateY(0);
    opacity: 0.3;
  }
  50% {
    transform: translateY(10px);
    opacity: 1;
  }
}

/* 扩展内容区域 */
.extended-content {
  padding: 40px 0;
  background: #fff;
}

.section {
  max-width: 1200px;
  margin: 0 auto 80px;
  padding: 0 20px;
  opacity: 0;
  transform: translateY(30px);
  transition: all 0.8s ease-out;
}

.section.visible {
  opacity: 1;
  transform: translateY(0);
}

.section-title {
  text-align: center;
  font-size: 32px;
  margin-bottom: 40px;
  color: #333;
  position: relative;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: linear-gradient(to right, #409eff, #67c23a);
  border-radius: 3px;
}

/* 特色卡片 */
.feature-cards {
  display: flex;
  gap: 30px;
  justify-content: center;
  flex-wrap: wrap;
}

.feature-card {
  width: 280px;
  padding: 30px;
  border-radius: 15px;
  background: #f9f9f9;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  text-align: center;
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fff;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.ticket-icon {
  background: #fff url('data:image/svg+xml;utf8,<svg t="1608888888888" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5555" width="40" height="40"><path d="M128 896V277.248l185.6-185.6h528.128L1024 273.92V896h-896z m576-768H337.376L208 247.296V832h752V299.52L704 128z" fill="%23409EFF" p-id="5556"></path><path d="M640 576h128v64h-128zM640 448h128v64h-128zM640 320h128v64h-128z" fill="%23409EFF" p-id="5557"></path><path d="M256 384h320v320H256z" fill="%23409EFF" p-id="5558"></path></svg>') no-repeat center;
  background-size: 40px;
}

.discount-icon {
  background: #fff url('data:image/svg+xml;utf8,<svg t="1608888888888" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="6666" width="40" height="40"><path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64z m0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="%23FF5722" p-id="6667"></path><path d="M686.7 638.6L544.1 535.5V288c0-4.4-3.6-8-8-8H488c-4.4 0-8 3.6-8 8v275.4c0 2.6 1.2 5 3.3 6.5l165.4 120.6c3.6 2.6 8.6 1.8 11.2-1.7l28.6-39c2.6-3.7 1.8-8.7-1.8-11.2z" fill="%23FF5722" p-id="6668"></path></svg>') no-repeat center;
  background-size: 40px;
}

.preview-icon {
  background: #fff url('data:image/svg+xml;utf8,<svg t="1608888888888" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7777" width="40" height="40"><path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64z m0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z" fill="%2367C23A" p-id="7778"></path><path d="M719.4 499.1l-296.1-215c-4.8-3.5-11.4-0.3-11.4 5.6v430c0 5.9 6.6 9.1 11.4 5.6l296.1-215c4.1-3 4.1-9.2 0-12.2z" fill="%2367C23A" p-id="7779"></path></svg>') no-repeat center;
  background-size: 40px;
}

.feature-card h4 {
  font-size: 20px;
  margin: 0 0 15px;
  color: #333;
}

.feature-card p {
  color: #666;
  line-height: 1.6;
}

/* 用户评价卡片 */
.testimonial-cards {
  display: flex;
  gap: 30px;
  justify-content: center;
  flex-wrap: wrap;
}

.testimonial-card {
  width: 300px;
  padding: 30px;
  border-radius: 15px;
  background: #f9f9f9;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  text-align: center;
  transition: all 0.3s ease;
}

.testimonial-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
}

.user-avatar {
  width: 80px;
  height: 80px;
  margin: 0 auto 15px;
  border-radius: 50%;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #333;
}

.testimonial-content {
  color: #666;
  line-height: 1.6;
  margin-bottom: 15px;
  font-style: italic;
}

.testimonial-rating {
  display: flex;
  justify-content: center;
  gap: 5px;
}

.star-icon {
  display: inline-block;
  width: 20px;
  height: 20px;
  background: url('data:image/svg+xml;utf8,<svg t="1608888888888" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="8888" width="20" height="20"><path d="M908.1 353.1l-253.9-36.9L540.7 86.1c-3.1-6.3-8.2-11.4-14.5-14.5-15.8-7.8-35-1.3-42.9 14.5L369.8 316.2l-253.9 36.9c-7 1-13.4 4.3-18.3 9.3-12.3 12.7-12.1 32.9 0.6 45.3l183.7 179.1-43.4 252.9c-1.2 6.9-0.1 14.1 3.2 20.3 8.2 15.6 27.6 21.7 43.2 13.4L512 754l227.1 119.4c6.2 3.3 13.4 4.4 20.3 3.2 17.4-3 29.1-19.5 26.1-36.9l-43.4-252.9 183.7-179.1c5-4.9 8.3-11.3 9.3-18.3 2.7-17.5-9.5-33.7-27-36.3z" p-id="8889" fill="%23CCCCCC"></path></svg>') no-repeat center;
  background-size: contain;
}

.star-icon.filled {
  background: url('data:image/svg+xml;utf8,<svg t="1608888888888" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="9999" width="20" height="20"><path d="M908.1 353.1l-253.9-36.9L540.7 86.1c-3.1-6.3-8.2-11.4-14.5-14.5-15.8-7.8-35-1.3-42.9 14.5L369.8 316.2l-253.9 36.9c-7 1-13.4 4.3-18.3 9.3-12.3 12.7-12.1 32.9 0.6 45.3l183.7 179.1-43.4 252.9c-1.2 6.9-0.1 14.1 3.2 20.3 8.2 15.6 27.6 21.7 43.2 13.4L512 754l227.1 119.4c6.2 3.3 13.4 4.4 20.3 3.2 17.4-3 29.1-19.5 26.1-36.9l-43.4-252.9 183.7-179.1c5-4.9 8.3-11.3 9.3-18.3 2.7-17.5-9.5-33.7-27-36.3z" p-id="9999" fill="%23FFBA5A"></path></svg>') no-repeat center;
  background-size: contain;
}

/* 页脚 */
.page-footer {
  background: linear-gradient(to right, #1c2331, #263238);
  color: #fff;
  padding: 0;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
}

.footer-main {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  padding: 60px 20px 40px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.footer-section {
  flex: 1;
  min-width: 200px;
  margin-bottom: 30px;
  padding: 0 15px;
}

.footer-section:first-child {
  flex: 1.5;
}

.footer-logo {
  font-size: 24px;
  font-weight: bold;
  color: #fff;
  margin-bottom: 15px;
  position: relative;
  display: flex;
  align-items: center;
  gap: 10px;
}

.footer-logo::before {
  content: '';
  display: inline-block;
  width: 24px;
  height: 24px;
  background: url('/favicon.ico') no-repeat center;
  background-size: contain;
}

.footer-logo::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -8px;
  width: 50px;
  height: 3px;
  background: linear-gradient(to right, #ff5f6d, #ffc371);
  border-radius: 1.5px;
}

.footer-desc {
  color: rgba(255, 255, 255, 0.7);
  line-height: 1.6;
  margin-bottom: 20px;
  max-width: 300px;
}

.footer-social-icons {
  display: flex;
  gap: 15px;
}

.social-icon {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.social-icon:hover {
  transform: translateY(-3px);
  background-color: rgba(255, 255, 255, 0.2);
}

.weixin {
  background: rgba(255, 255, 255, 0.1) url('data:image/svg+xml;utf8,<svg t="1608888888888" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2222" width="22" height="22"><path d="M690.2 364.8c4.6 0 9.2 0.2 13.8 0.4-24.4-132.8-158.8-231.4-309.6-231.4-169 0-306.6 115.2-306.6 257.4 0 83.2 45.4 151.6 121.2 204.8l-30.2 91 106-53.2c38 7.6 68.4 15.4 106.4 15.4 9.4 0 18.8-0.2 28-1-5.4-20.2-9.2-41.2-9.2-63.4 0-128.8 111-233 280.2-233z m-177-89c22.8 0 38 15 38 37.8 0 22.8-15 38-38 38-22.8 0-45.4-15.2-45.4-38s22.6-37.8 45.4-37.8z m-233.6 75.8c-22.8 0-45.6-15.2-45.6-38s22.8-37.8 45.6-37.8c22.8 0 38 15 38 37.8-0.2 22.8-15.2 38-38 38z" fill="%23FFFFFF" p-id="2223"></path><path d="M686.7 638.6L544.1 535.5V288c0-4.4-3.6-8-8-8H488c-4.4 0-8 3.6-8 8v275.4c0 2.6 1.2 5 3.3 6.5l165.4 120.6c3.6 2.6 8.6 1.8 11.2-1.7l28.6-39c2.6-3.7 1.8-8.7-1.8-11.2z" fill="%23FF5722" p-id="6668"></path></svg>') no-repeat center;
  background-size: 22px;
}

.weibo {
  background: rgba(255, 255, 255, 0.1) url('data:image/svg+xml;utf8,<svg t="1608888888888" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4444" width="22" height="22"><path d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64z m-57.9 601.5c-108.5 0-196.7-84.7-196.7-189 0-104.5 88.2-189 196.7-189 115 0 189.9 89.3 189.9 189 0.1 104.3-86.4 189-189.9 189z m194.4-277.2c-23.3-24.8-57-34.2-88.5-25.7-6.8 1.8-11.2 8.2-9.9 15.2 1.2 7 7.9 11.4 14.9 10.3 21.6-5.7 45.2 1.6 61.2 18.7 16 17.2 20.4 40.9 13.4 61.6-2.6 7.6 1.3 15.8 8.9 18 7.6 2.2 15.7-2.2 18-9.7 10.4-31.7 3.9-66.7-28-88.4z m-5.9-138.3c-47.4-49.4-117.7-68.1-182.6-52.7-13.9 3.3-22.2 16.5-18.9 30 3.3 13.5 17.2 21.8 30.8 18.5 65.3-15.5 136 13.1 184 61.5 48.1 48.4 61.5 114.4 40.6 172.3-5.3 15 4.2 30.8 20.4 35.9 16.3 5.1 33.4-3.3 38.7-18.3 28.4-79.6 10.5-170.9-62.4-246.2h-0.2z" fill="%23FFFFFF" p-id="4445"></path><path d="M470.7 491.5c-57.8 5.3-98.8 53.9-90.9 108.3 7.9 54.4 60.6 94.1 118.3 88.8 57.7-5.3 99.8-53.7 91.9z m-14.2 161.7c-31.5 3.7-65.2-15.4-77.1-45.5-11.9-30.2 1.5-61.1 33-64.9 31.5-3.7 65.9 15.9 77.8 46 11.9 30.2-1.5 61-33.7 64.4z m246.4-301.1c-28.7 0.7-51.8 24.1-51.1 52 0.7 27.9 23.9 50.3 52.6 49.6 28.8-0.7 51.8-24.1 51.1-52-0.7-27.9-24-50.3-52.6-49.6z" fill="%23FFFFFF" p-id="4446"></path></svg>') no-repeat center;
  background-size: 22px;
}

.douyin {
  background: rgba(255, 255, 255, 0.1) url('data:image/svg+xml;utf8,<svg t="1647507566001" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="9999" width="22" height="22"><path d="M937.1 423.2c-82 0-150.1-66-150.1-148V270h-138v602c0 36.5-33.5 66-70 66-36.5 0-66.7-29.5-66.7-66 0-36.5 30.2-66 66.7-66 9.7 0 13.1 0 26.1 3.3V671c-9.7-3.3-13.1-3.3-26.1-3.3-95.1 0-171.6 76.4-171.6 171.6S384.9 1010.9 480 1010.9s174.9-76.4 174.9-171.6V554.8c26.4 16.4 79.1 33.2 151.9 33.2V423.2h-49.9c51.5-26.4 89.9-94.8 89.9-148V270h-59.8v5.2c0 82-56.5 148-138.5 148H118v164.8h49.9c82 0 150.1-21.3 150.1-21.3 0 95.1 36.9 171.6 131.9 171.6 0 0-13.1-36.5-13.1-72.9s13.1-69.7 36.5-89.6V137.4h263.4c0-52.7 36.2-124 89.9-124V137.4h263.4v285.8h-113z" fill="%23FFFFFF" p-id="10000"></path></svg>') no-repeat center;
  background-size: 22px;
}

.footer-heading {
  font-size: 18px;
  margin-bottom: 20px;
  font-weight: 600;
  color: #fff;
  position: relative;
  display: inline-block;
}

.footer-heading::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -8px;
  width: 30px;
  height: 2px;
  background: linear-gradient(to right, #409eff, #67c23a);
  border-radius: 1px;
}

.footer-links {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-links li {
  margin-bottom: 12px;
}

.footer-links a {
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  transition: all 0.3s ease;
  position: relative;
  display: inline-block;
}

.footer-links a::before {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 1px;
  background-color: #fff;
  transition: width 0.3s ease;
}

.footer-links a:hover {
  color: #fff;
  transform: translateX(5px);
}

.footer-links a:hover::before {
  width: 100%;
}

.footer-bottom {
  padding: 20px;
  text-align: center;
}

.footer-copyright {
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
  margin-bottom: 10px;
}

.footer-cert {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 20px;
  color: rgba(255, 255, 255, 0.4);
  font-size: 12px;
}

/* 响应式样式 */
@media (max-width: 768px) {
  .footer-main {
    flex-direction: column;
    padding: 40px 20px 20px;
  }
  
  .footer-section {
    margin-bottom: 30px;
    padding: 0;
  }
  
  .footer-bottom {
    padding: 15px;
  }
  
  .footer-cert {
    flex-direction: column;
    gap: 5px;
  }
}

/* 添加滑块验证样式 */
.slider-verify {
  margin-bottom: 25px;
  background: #f5f7fa;
  border-radius: 12px;
  padding: 20px;
  box-shadow: inset 0 2px 6px rgba(0, 0, 0, 0.05);
}

.verify-title {
  font-size: 15px;
  color: #606266;
  margin-bottom: 15px;
  text-align: center;
  transition: all 0.3s ease;
  font-weight: 500;
}

.verify-title.verified {
  color: #67c23a;
  font-weight: 500;
}

.verify-slider :deep(.el-slider__runway) {
  height: 44px;
  margin: 0;
  background-color: #e9ecef;
  border-radius: 22px;
  transition: all 0.3s ease;
  cursor: pointer;
}

.verify-slider :deep(.el-slider__bar) {
  height: 44px;
  background: linear-gradient(to right, #ff5f6d, #ffc371);
  border-radius: 22px;
  transition: all 0.3s ease;
}

.custom-slider-button {
  width: 44px;
  height: 44px;
  border: none;
  background: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: grab;
  font-size: 18px;
}

.custom-slider-button:active {
  cursor: grabbing;
  transform: scale(0.95);
}

.custom-slider-button:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.custom-slider-button.verified {
  background: #67c23a;
  color: #fff;
}

.verify-slider :deep(.el-slider__button-wrapper) {
  top: -2px;
  width: 44px;
  height: 44px;
  cursor: grab;
}

.verify-slider :deep(.el-slider__button-wrapper:active) {
  cursor: grabbing;
}

.verify-slider :deep(.el-slider__button) {
  display: none;
}

/* 拼图验证样式 */
.captcha-dialog :deep(.el-dialog__body) {
  padding: 15px;
}

.captcha-dialog :deep(.el-dialog) {
  border-radius: 16px;
  overflow: hidden;
}

.puzzle-captcha {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.puzzle-image {
  width: 100%;
  max-width: 360px;
  height: 180px;
  position: relative;
  overflow: hidden;
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.puzzle-bg {
  width: 100%;
  height: 100%;
  position: relative;
}

.puzzle-piece {
  position: absolute;
  width: 50px;
  height: 50px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
  border-radius: 6px;
  cursor: pointer;
  transition: box-shadow 0.3s;
  z-index: 10;
  border: 2px solid rgba(255, 255, 255, 0.8);
  touch-action: none; /* 防止触摸设备上的滚动和缩放 */
}

.puzzle-target {
  position: absolute;
  width: 50px;
  height: 50px;
  background: rgba(255, 255, 255, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.7);
  border-radius: 6px;
  z-index: 5;
}

.puzzle-slider-container {
  width: 100%;
  max-width: 360px;
  height: 50px;
  position: relative;
  margin-bottom: 15px;
}

.puzzle-slider-tip {
  text-align: center;
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
}

.puzzle-slider-track {
  width: 100%;
  height: 40px;
  background: #f5f7fa;
  border-radius: 20px;
  box-shadow: inset 0 2px 6px rgba(0, 0, 0, 0.05);
  position: relative;
}

.puzzle-slider-button {
  position: absolute;
  width: 44px;
  height: 44px;
  background: #fff;
  top: 20px;
  margin-top: -22px;  /* 垂直居中 */
  border-radius: 50%;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: grab;
  z-index: 20;
  font-size: 18px;
  color: #909399;
  transition: all 0.3s;
  touch-action: none; /* 防止触摸设备上的滚动和缩放 */
}

.puzzle-slider-button:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.puzzle-slider-button:active {
  cursor: grabbing;
}

.puzzle-slider-button.verified {
  background: #67c23a;
  color: #fff;
}

.puzzle-action {
  display: flex;
  justify-content: center;
  margin-top: 5px;
}

.refresh-btn {
  font-size: 14px;
  color: #909399;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 8px 12px;
  border-radius: 4px;
}

.refresh-btn:hover {
  background-color: #f5f7fa;
  color: #409eff;
}

/* 登录过渡动画 */
.login-transition {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.85);
  backdrop-filter: blur(8px);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
  animation: fadeIn 0.3s ease-out;
}

.login-transition-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: white;
}

.login-spinner {
  width: 60px;
  height: 60px;
  border: 4px solid rgba(255, 255, 255, 0.2);
  border-top-color: #409eff;
  border-radius: 50%;
  margin-bottom: 30px;
  animation: spin 1s linear infinite;
}

.welcome-message {
  text-align: center;
}

.welcome-message h3 {
  font-size: 24px;
  margin-bottom: 10px;
  background: linear-gradient(to right, #409eff, #67c23a);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.welcome-message p {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.7);
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@media (max-width: 768px) {
  .login-spinner {
    width: 50px;
    height: 50px;
    margin-bottom: 20px;
  }
  
  .welcome-message h3 {
    font-size: 20px;
  }
  
  .welcome-message p {
    font-size: 14px;
  }
}
</style>