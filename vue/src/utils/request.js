import axios from "axios";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

// 防止多次提示 token 失效
let isTokenExpired = false;
let isRedirecting = false;

// 创建需要排除token验证的URL列表
// 这些URL不需要携带Token就能访问，特别适用于登录、注册和验证码相关操作
const excludeTokenUrls = [
    // 核心认证操作
    '/login',                      // 用户登录接口
    '/register',                   // 用户注册接口
    
    // 用户信息相关不需要认证的接口
    '/user/resetPassword',         // 重置密码
    '/user/checkUsername',         // 检查用户名是否存在
    '/user/checkPhone',            // 检查手机号是否存在
    '/user/sendCode',              // 发送手机验证码
    
    // 邮箱验证相关接口 - 邮箱验证必须无需Token
    '/email/sendVerificationCode', // 发送邮箱验证码
    '/email/verifyCode',           // 验证邮箱验证码
    
    // 其他公共接口
    '/files/upload',               // 文件上传
    '/files/download'              // 文件下载
];

// 针对生产环境添加控制台调试日志
// 设置为true开启详细日志，生产环境问题排查后应设置回false
const ENABLE_DEBUG = true;

const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 30000 // 后台接口超时时间
});

// 全局图片URL处理函数
export const getFullImageUrl = (url) => {
    // 如果已经是完整URL，直接返回
    if (url && (url.startsWith('http://') || url.startsWith('https://'))) {
        return url;
    }
    
    // 如果是相对路径，拼接基础URL
    if (url && url.startsWith('/')) {
        return import.meta.env.VITE_BASE_URL + url;
    }
    
    return url;
};

// request 拦截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    
    // 登录页面的特殊处理
    if (window.location.pathname.endsWith('/login')) {
        // 判断是否是登录请求
        if (config.url.includes('/login') && config.method === 'post') {
            // 登录请求一定放行，并记录日志
            console.log('正在处理登录请求，确保无阻拦');
            return config;
        }
        
        // 其他登录页请求也放行
        if (ENABLE_DEBUG) console.log('登录页面请求，直接放行:', config.url);
        return config;
    }
    
    // 检查是否为不需要token的请求
    const isExcluded = excludeTokenUrls.some(url => config.url.includes(url));
    if (isExcluded) {
        if (ENABLE_DEBUG) console.log('白名单请求，无需Token:', config.url);
        return config;
    }
    
    // 优先使用内存中最新的用户状态
    let user = null;
    try {
        // 检查用户是否已登录
        const userStr = localStorage.getItem('xm-user');
        if (userStr) {
            user = JSON.parse(userStr);
        }
    } catch (e) {
        console.error('解析用户信息出错:', e);
    }
    
    // 如果用户未登录且请求需要token
    if (!user || !user.token) {
        // 详细记录无Token的请求
        if (ENABLE_DEBUG) console.error('请求需要Token但未找到:', config.url, '用户状态:', !!user);
        
        // 如果已经在重定向到登录页面，则不再重复处理
        if (isRedirecting) {
            return Promise.reject('正在跳转到登录页，请求已取消');
        }
        
        // 对于任何需要token的API请求，如果未登录，直接跳转到登录页
        if (!isRedirecting && router.currentRoute.value.path !== '/login') {
            isRedirecting = true;
            console.log('需要登录权限，跳转到登录页');
            ElMessage.warning('请先登录系统');
            router.push('/login?auth_error=true');
            
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
        if (ENABLE_DEBUG) console.log('Token已添加到请求:', config.url);
    }
    
    return config;
}, error => {
    if (ENABLE_DEBUG) console.error('请求拦截器错误:', error);
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
        
        // 处理返回数据中的图片URL
        if (res.data && typeof res.data === 'object') {
            // 如果是数组，遍历处理
            if (Array.isArray(res.data)) {
                res.data.forEach(item => {
                    processImageUrls(item);
                });
            } 
            // 如果是分页数据
            else if (res.data.list && Array.isArray(res.data.list)) {
                res.data.list.forEach(item => {
                    processImageUrls(item);
                });
            }
            // 普通对象
            else {
                processImageUrls(res.data);
            }
        }
        
        // 处理token验证失败（401或403都表示权限问题）
        if (res.code === '401' || res.code === '403' || res.code === 401 || res.code === 403) {
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
            // 对于401/403错误，返回一个已处理的结果，避免页面组件继续处理
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

// 处理对象中的图片URL字段
function processImageUrls(obj) {
    if (!obj || typeof obj !== 'object') return;
    
    // 常见的图片URL字段名
    const imageFields = ['img', 'image', 'avatar', 'poster', 'cover', 'photo', 'url', 'src', 'picture', 'thumbnail', 'icon'];
    
    Object.keys(obj).forEach(key => {
        // 如果字段名包含常见图片字段，且值是字符串且不为空
        if (imageFields.some(field => key.toLowerCase().includes(field)) && 
            typeof obj[key] === 'string' && 
            obj[key] && 
            !obj[key].startsWith('data:')) { // 排除base64图片
            
            // 处理完整URL情况 - 首先检查是否包含localhost
            if (obj[key].includes('localhost')) {
                // 使用正则表达式匹配所有localhost格式（包括不同端口）
                obj[key] = obj[key].replace(/https?:\/\/localhost(:[0-9]+)?/g, import.meta.env.VITE_BASE_URL);
                return;
            }
            
            // 完整URLs不处理（除了localhost）
            if (obj[key].startsWith('http://') || obj[key].startsWith('https://')) {
                return;
            }
            
            // 处理以/files/开头的路径
            if (obj[key].startsWith('/files/')) {
                obj[key] = import.meta.env.VITE_BASE_URL + obj[key];
                return;
            }
            
            // 处理其他相对路径
            if (obj[key].startsWith('/')) {
                obj[key] = import.meta.env.VITE_BASE_URL + obj[key];
                return;
            }
            
            // 处理不带/的文件名 (补充完整路径)
            if (!obj[key].includes('/') && (
                obj[key].endsWith('.jpg') || 
                obj[key].endsWith('.jpeg') || 
                obj[key].endsWith('.png') || 
                obj[key].endsWith('.gif') || 
                obj[key].endsWith('.webp')
            )) {
                obj[key] = `${import.meta.env.VITE_BASE_URL}/files/download/${obj[key]}`;
                return;
            }
        }
    });
}

export default request;
