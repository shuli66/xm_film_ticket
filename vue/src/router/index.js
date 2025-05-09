import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/front/home' },
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      meta: { requiresAuth: true }, // 需要登录
      children: [
        { path: 'home', meta: { name: '系统首页', requiresAuth: true }, component: () => import('@/views/manager/Home.vue') },
        { path: 'adminHome', meta: { name: '系统首页', requiresAuth: true }, component: () => import('@/views/manager/AdminHome.vue') },
        { path: 'admin', meta: { name: '管理员信息', requiresAuth: true }, component: () => import('@/views/manager/Admin.vue') },
        { path: 'notice', meta: { name: '系统公告', requiresAuth: true }, component: () => import('@/views/manager/Notice.vue') },
        { path: 'person', meta: { name: '个人资料', requiresAuth: true }, component: () => import('@/views/manager/Person.vue') },
        { path: 'password', meta: { name: '修改密码', requiresAuth: true }, component: () => import('@/views/manager/Password.vue') },
        { path: 'cinema', meta: { name: '影院信息', requiresAuth: true }, component: () => import('@/views/manager/Cinema.vue') },
        { path: 'user', meta: { name: '用户信息', requiresAuth: true }, component: () => import('@/views/manager/User.vue') },
        { path: 'type', meta: { name: '电影分类', requiresAuth: true }, component: () => import('@/views/manager/Type.vue') },
        { path: 'area', meta: { name: '电影区域', requiresAuth: true }, component: () => import('@/views/manager/Area.vue') },
        { path: 'film', meta: { name: '电影信息', requiresAuth: true }, component: () => import('@/views/manager/Film.vue') },
        { path: 'actor', meta: { name: '演职人员', requiresAuth: true }, component: () => import('@/views/manager/Actor.vue') },
        { path: 'video', meta: { name: '预告视频', requiresAuth: true }, component: () => import('@/views/manager/Video.vue') },
        { path: 'certificate', meta: { name: '资质认证', requiresAuth: true, roles: ['CINEMA'] }, component: () => import('@/views/manager/Certificate.vue') },
        { path: 'room', meta: { name: '影厅房间', requiresAuth: true }, component: () => import('@/views/manager/Room.vue') },
        { path: 'show', meta: { name: '放映记录', requiresAuth: true }, component: () => import('@/views/manager/Show.vue') },
        { path: 'orders', meta: { name: '购票订单', requiresAuth: true }, component: () => import('@/views/manager/Orders.vue') },
        { path: 'score', meta: { name: '电影评分', requiresAuth: true }, component: () => import('@/views/manager/Score.vue') },
        { path: 'comment', meta: { name: '评论管理', requiresAuth: true }, component: () => import('@/views/manager/Comment.vue') }
      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue') },
        { path: 'person', meta: { requiresAuth: true }, component: () => import('@/views/front/Person.vue') },
        { path: 'password', meta: { requiresAuth: true }, component: () => import('@/views/front/Password.vue') },
        { path: 'film', component: () => import('@/views/front/Film.vue') },
        { path: 'cinema', component: () => import('@/views/front/Cinema.vue') },
        { path: 'filmDetail', component: () => import('@/views/front/FilmDetail.vue') },
        { path: 'cinemaDetail', component: () => import('@/views/front/CinemaDetail.vue') },
        { path: 'filmCinema', component: () => import('@/views/front/FilmCinema.vue') },
        { path: 'collect', meta: { requiresAuth: true }, component: () => import('@/views/front/Collect.vue') },
        { path: 'seat', meta: { requiresAuth: true }, component: () => import('@/views/front/Seat.vue') },
        { path: 'orders', meta: { requiresAuth: true }, component: () => import('@/views/front/Orders.vue') },
        { path: 'notice', component: () => import('@/views/front/Notice.vue') },
        { path: 'charts', component: () => import('@/views/front/Charts.vue') },
        { path: 'search', component: () => import('@/views/front/Search.vue') }
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue') },
    { path: '/register', component: () => import('@/views/Register.vue') },
    { path: '/404', component: () => import('@/views/404.vue') },
    { path: '/:pathMatch(.*)', redirect: '/404' }
  ]
})

// 防止重复提示
let hasLoginTip = false;
let isRedirectingToLogin = false;

// 全局路由守卫，检查用户登录状态
router.beforeEach((to, from, next) => {
  // 无需登录的页面直接放行
  if (to.path === '/login' || to.path === '/register' || to.path === '/404') {
    // 重置提示标记
    hasLoginTip = false;
    isRedirectingToLogin = false;
    next();
    return;
  }
  
  // 检查用户登录状态
  const userStr = localStorage.getItem("xm-user");
  const user = userStr ? JSON.parse(userStr) : null;
  const isLoggedIn = user && user.token;
  
  // 刚登录检测 - 当从登录页跳转且携带了刚登录标记时，优先放行
  if (from.path === '/login' && sessionStorage.getItem('just_logged_in') === 'true') {
    console.log('检测到用户刚刚登录成功，优先放行', to.path);
    // 移除标记，避免重复使用
    sessionStorage.removeItem('just_logged_in');
    next();
    return;
  }
  
  // 需要登录但未登录
  if (to.meta.requiresAuth && !isLoggedIn) {
    if (!hasLoginTip && !isRedirectingToLogin) {
      console.warn("需要登录权限，跳转到登录页");
      hasLoginTip = true;
      isRedirectingToLogin = true;
      
      // 2秒后重置提示标记
      setTimeout(() => {
        hasLoginTip = false;
        isRedirectingToLogin = false;
      }, 2000);
      
      next('/login');
    } else {
      // 如果已经在处理跳转，不触发新的跳转
      next(false);
    }
  } else {
    // 已登录且需要检查角色权限
    if (isLoggedIn) {
      // 检查用户是否尝试访问前台页面，但实际是管理员或影院角色
      if (to.path === '/front/home' && (user.role === 'ADMIN' || user.role === 'CINEMA')) {
        // 根据角色重定向到正确的首页
        if (user.role === 'ADMIN') {
          next('/manager/adminHome');
          return;
        } else if (user.role === 'CINEMA') {
          next('/manager/home');
          return;
        }
      }
      
      // 检查用户角色是否满足访问要求
      if (to.meta.roles && !to.meta.roles.includes(user.role)) {
        console.warn("没有访问权限");
        next('/404');
        return;
      }
    }
    // 已登录或不需要登录权限
    next();
  }
});

export default router;
