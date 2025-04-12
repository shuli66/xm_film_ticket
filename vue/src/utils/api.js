/**
 * API配置文件
 * 集中管理所有API路径和URL配置
 */

// 基础URL配置
export const BASE_URL = import.meta.env.VITE_BASE_URL || 'http://localhost:9090';

// 支付相关
export const PAY_URL = `${BASE_URL}/alipay/pay`;

// API路径分类
export const API = {
  // 用户认证相关
  AUTH: {
    LOGIN: '/login',
    REGISTER: '/register',
    CHECK_USERNAME: '/user/checkUsername',
    CHECK_PHONE: '/user/checkPhone',
    RESET_PASSWORD: '/user/resetPassword',
    SEND_CODE: '/user/sendCode'
  },
  
  // 用户相关
  USER: {
    UPDATE: '/user/update',
    SELECT_BY_ID: (id) => `/user/selectById/${id}`,
    SELECT_PAGE: '/user/selectPage',
  },
  
  // 电影相关
  FILM: {
    SELECT_ALL: '/film/selectAll',
    SELECT_PAGE: '/film/selectPage',
    SELECT_BY_ID: (id) => `/film/selectById/${id}`,
    SELECT_TOTAL_TOP: '/film/selectTotalTop',
    SELECT_SCORE_TOP: '/film/selectScoreTop',
    SELECT_PRICE_RANKING: (id) => `/film/selectPriceRanking/${id}`,
    ADD: '/film/add',
    UPDATE: '/film/update',
    DELETE: (id) => `/film/delete/${id}`,
    DELETE_BATCH: '/film/delete/batch'
  },
  
  // 影院相关
  CINEMA: {
    SELECT_ALL: '/cinema/selectAll',
    SELECT_PAGE: '/cinema/selectPage',
    SELECT_BY_ID: (id) => `/cinema/selectById/${id}`,
    ADD: '/cinema/add',
    UPDATE: '/cinema/update',
    DELETE: (id) => `/cinema/delete/${id}`
  },
  
  // 场次相关
  SHOW: {
    SELECT_PAGE: '/show/selectPage',
    SELECT_BY_CINEMA_ID: (id) => `/show/selectByCinemaId/${id}`,
    SELECT_BY_FILM_ID: (id) => `/show/selectByFilmId/${id}`,
    ADD: '/show/add',
    UPDATE: '/show/update',
    DELETE: (id) => `/show/delete/${id}`
  },
  
  // 订单相关
  ORDERS: {
    SELECT_PAGE: '/orders/selectPage',
    TODAY_TOTAL: '/orders/todayTotal',
    CANCEL: (id) => `/orders/cancel/${id}`,
    PICKUP: (id) => `/orders/pickup/${id}`,
    UPDATE: '/orders/update',
    DELETE: (id) => `/orders/delete/${id}`,
    DELETE_BATCH: '/orders/delete/batch'
  },
  
  // 分类相关
  TYPE: {
    SELECT_ALL: '/type/selectAll',
    SELECT_PAGE: '/type/selectPage',
    ADD: '/type/add',
    UPDATE: '/type/update',
    DELETE: (id) => `/type/delete/${id}`
  },
  
  // 地区相关
  AREA: {
    SELECT_ALL: '/area/selectAll',
    SELECT_PAGE: '/area/selectPage',
    ADD: '/area/add',
    UPDATE: '/area/update',
    DELETE: (id) => `/area/delete/${id}`
  },
  
  // 收藏相关
  COLLECT: {
    SELECT_PAGE: '/collect/selectPage',
    ADD: '/collect/add',
    DELETE: (id) => `/collect/delete/${id}`
  },
  
  // 公告相关
  NOTICE: {
    SELECT_ALL: '/notice/selectAll',
    SELECT_PAGE: '/notice/selectPage',
    ADD: '/notice/add',
    UPDATE: '/notice/update',
    DELETE: (id) => `/notice/delete/${id}`
  },
  
  // 推荐系统相关
  RECOMMENDATIONS: {
    GET_BY_USER_ID: (id) => `/api/recommendations/${id}`
  },
  
  // 其他
  YEAR: '/getYear'
};

// 获取完整URL（用于window.open等场景）
export const getFullUrl = (path) => {
  return `${BASE_URL}${path}`;
};

export default API; 