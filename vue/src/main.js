import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import '@/assets/css/global.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import { ElMessage } from 'element-plus'

const app = createApp(App)

// 全局错误处理
app.config.errorHandler = (err, vm, info) => {
  console.error('Vue应用错误:', err);
  // 避免将敏感错误信息显示给用户
  if (process.env.NODE_ENV !== 'production') {
    ElMessage.error(`应用错误: ${err.message}`);
  }
};

// 捕获Promise未处理的rejection
window.addEventListener('unhandledrejection', event => {
  console.warn('未处理的Promise拒绝:', event.reason);
  event.preventDefault();
});

app.use(router)
app.use(ElementPlus, { locale: zhCn })
app.mount('#app')

import * as ElementPlusIconsVue from '@element-plus/icons-vue'

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
