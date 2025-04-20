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
          } else {
            // 校验当前路由与用户角色，确保用户在刷新后进入正确的页面
            const currentPath = router.currentRoute.value.path;
            
            // 如果在根路径或前台首页但用户是管理员或影院角色，则重定向到相应页面
            if ((currentPath === '/' || currentPath === '/front/home') && 
                (user.role === 'ADMIN' || user.role === 'CINEMA')) {
              // 延迟执行以避免可能的路由冲突
              setTimeout(() => {
                if (user.role === 'ADMIN') {
                  router.push('/manager/adminHome');
                } else if (user.role === 'CINEMA') {
                  router.push('/manager/home');
                }
              }, 100);
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