import axios from "axios";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

// 防止多次提示 token 失效
let isTokenExpired = false;
let isRedirecting = false;

const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL,
    timeout: 30000 // 后台接口超时时间
});
// request 拦截器
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';
    let user = JSON.parse(localStorage.getItem("xm-user") || '{}')
    config.headers['token'] = user.token || '';
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
        if (res.code === '401') {
            // 防止重复提示
            if (!isTokenExpired && !isRedirecting) {
                isTokenExpired = true;  // 标记为 token 已失效
                isRedirecting = true;  // 标记为正在跳转到登录页
                ElMessage.error("Token验证失败，请重新登录");
                // 清除本地缓存中的 token
                localStorage.removeItem('xm-user');
                router.push('/login');  // 跳转到登录页面
                setTimeout(() => {
                    isTokenExpired = false;  // 恢复 token 失效标志
                    isRedirecting = false;   // 恢复跳转标志
                }, 2000);  // 延时恢复，防止重复操作
            }
        }
        return res;
    },
    error => {
        if (error.response.status === 404) {
            ElMessage.error('未找到请求接口');
        } else if (error.response.status === 500) {
            ElMessage.error('系统异常，请查看后端控制台报错');
        } else {
            console.error(error.message);
        }
        return Promise.reject(error);
    }
);

export default request;
