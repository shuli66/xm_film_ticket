import axios from "axios";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

// 防止多次提示 token 失效
let isTokenExpired = false;
let isRedirecting = false;

// 创建需要排除token验证的URL列表
const excludeTokenUrls = [
    '/login', 
    '/register', 
    '/user/resetPassword',
    '/user/checkUsername',
    '/user/checkPhone',
    '/user/sendCode'
];

const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 30000 // 后台接口超时时间
});

// request 拦截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    
    // 检查是否为不需要token的请求
    const isExcluded = excludeTokenUrls.some(url => config.url.includes(url));
    if (isExcluded) {
        return config;
    }
    
    // 检查用户是否已登录
    const user = localStorage.getItem('xm-user') ? JSON.parse(localStorage.getItem('xm-user')) : null;
    
    // 如果用户未登录且请求需要token
    if (!user || !user.token) {
        // 如果已经在重定向到登录页面，则不再重复处理
        if (isRedirecting) {
            return Promise.reject('正在跳转到登录页，请求已取消');
        }
        
        // 对于任何需要token的API请求，如果未登录，直接跳转到登录页
        if (!isRedirecting && router.currentRoute.value.path !== '/login') {
            isRedirecting = true;
            router.push('/login');
            
            // 延时恢复标志位，避免多次跳转
            setTimeout(() => {
                isRedirecting = false;
            }, 2000);
        }
        
        // 取消无意义的API请求，减少后端压力
        return Promise.reject('未登录状态，请求已取消');
    } else {
        // 用户已登录，添加token
        config.headers['token'] = user.token;
    }
    
    return config;
}, error => {
    return Promise.reject(error);
});

// response 拦截器
request.interceptors.response.use(
    response => {
        let res = response.data;
        // 如果是返回的文件
        if (response.config.responseType === 'blob') {
            return res;
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res;
        }
        
        // 处理token验证失败
        if (res.code === '401') {
            // 防止重复提示和重复跳转
            if (!isTokenExpired && !isRedirecting) {
                isTokenExpired = true;  // 标记为 token 已失效
                isRedirecting = true;  // 标记为正在跳转到登录页
                
                // 清除本地缓存中的 token
                localStorage.removeItem('xm-user');
                
                // 只有当前不在登录页时才提示和跳转
                if (router.currentRoute.value.path !== '/login') {
                    ElMessage.error("登录已过期，请重新登录");
                    router.push('/login');  // 跳转到登录页面
                }
                
                // 延时恢复标志位
                setTimeout(() => {
                    isTokenExpired = false;  // 恢复 token 失效标志
                    isRedirecting = false;   // 恢复跳转标志
                }, 2000);  // 延时恢复，防止重复操作
            }
            // 对于401错误，返回一个已处理的结果，避免页面组件继续处理
            return Promise.reject('token已失效，请求已取消');
        }
        
        return res;
    },
    error => {
        // 判断是否是主动取消的请求（未登录状态取消的请求）
        if (axios.isCancel(error) || 
            error === '未登录状态，请求已取消' || 
            error === 'token已失效，请求已取消' ||
            error === '正在跳转到登录页，请求已取消') {
            console.log('请求被取消:', error);
            return Promise.reject(error);
        }
        
        if (error.response) {
            if (error.response.status === 404) {
                ElMessage.error('未找到请求接口');
            } else if (error.response.status === 500) {
                ElMessage.error('系统异常，请查看后端控制台报错');
            } else {
                console.error(error.message);
            }
        } else {
            console.error('网络请求错误:', error);
        }
        
        return Promise.reject(error);
    }
);

export default request;
