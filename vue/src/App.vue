<template>
  <RouterView />
</template>

<script>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const router = useRouter();
    
    onMounted(() => {
      // 在应用加载时检查token是否有效
      const userStr = localStorage.getItem('xm-user');
      if (userStr) {
        try {
          const user = JSON.parse(userStr);
          const tokenExpired = !user.token;
          
          // 如果token无效，清除localStorage并重定向到登录页
          if (tokenExpired) {
            console.warn('Token无效，清除本地存储');
            localStorage.removeItem('xm-user');
            if (router.currentRoute.value.meta.requiresAuth) {
              router.push('/login');
            }
          }
        } catch (e) {
          console.error('解析用户数据失败', e);
          localStorage.removeItem('xm-user');
        }
      }
    });
  }
}
</script>