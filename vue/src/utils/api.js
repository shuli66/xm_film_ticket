/**
 * 统一API接口管理文件
 * 集中管理所有API路径，便于后端地址变更时统一修改
 */

import request from '@/utils/request';

// 基础路径 - 可直接使用环境变量中的地址
const baseApiUrl = import.meta.env.VITE_BASE_URL;

// 支付相关接口
const paymentApi = {
  // 支付宝支付页面
  alipayUrl: (orderNo) => `${baseApiUrl}/alipay/pay?orderNo=${orderNo}`,
};

// 用户相关接口
const userApi = {
  login: (data) => request.post('/login', data),
  register: (data) => request.post('/register', data),
  update: (data) => request.put('/user/update', data),
  resetPassword: (data) => request.post('/user/resetPassword', data),
  checkUsername: (params) => request.get('/user/checkUsername', { params }),
  checkPhone: (params) => request.get('/user/checkPhone', { params }),
  sendCode: (params) => request.get('/user/sendCode', { params }),
};

// 电影相关接口
const filmApi = {
  getPage: (params) => request.get('/film/selectPage', { params }),
  getAll: (params) => request.get('/film/selectAll', { params }),
  getById: (id) => request.get(`/film/selectById/${id}`),
  getTotalTop: () => request.get('/film/selectTotalTop'),
  getScoreTop: () => request.get('/film/selectScoreTop'),
  getPriceRanking: (id) => request.get(`/film/selectPriceRanking/${id}`),
  add: (data) => request.post('/film/add', data),
  update: (data) => request.put('/film/update', data),
  delete: (id) => request.delete(`/film/delete/${id}`),
  batchDelete: (ids) => request.delete('/film/delete/batch', { data: ids }),
};

// 订单相关接口
const orderApi = {
  getPage: (params) => request.get('/orders/selectPage', { params }),
  cancel: (id) => request.get(`/orders/cancel/${id}`),
  pickup: (id) => request.get(`/orders/pickup/${id}`),
  todayTotal: () => request.get('/orders/todayTotal'),
  update: (data) => request.put('/orders/update', data),
  delete: (id) => request.delete(`/orders/delete/${id}`),
  batchDelete: (ids) => request.delete('/orders/delete/batch', { data: ids }),
  getOrderStatus: (orderNo) => request.get(`/orders/status/${orderNo}`),
};

// 影院相关接口
const cinemaApi = {
  getPage: (params) => request.get('/cinema/selectPage', { params }),
  getAll: (params) => request.get('/cinema/selectAll', { params }),
  getById: (id) => request.get(`/cinema/selectById/${id}`),
  add: (data) => request.post('/cinema/add', data),
  update: (data) => request.put('/cinema/update', data),
  delete: (id) => request.delete(`/cinema/delete/${id}`),
  batchDelete: (ids) => request.delete('/cinema/delete/batch', { data: ids }),
};

// 场次相关接口
const showApi = {
  getPage: (params) => request.get('/show/selectPage', { params }),
  getByCinemaId: (id) => request.get(`/show/selectByCinemaId/${id}`),
  getById: (id) => request.get(`/show/selectById/${id}`),
  add: (data) => request.post('/show/add', data),
  update: (data) => request.put('/show/update', data),
  delete: (id) => request.delete(`/show/delete/${id}`),
  batchDelete: (ids) => request.delete('/show/delete/batch', { data: ids }),
};

// 评分相关接口
const scoreApi = {
  getAll: (params) => request.get('/score/selectAll', { params }),
  add: (data) => request.post('/score/add', data),
};

// 收藏相关接口
const collectApi = {
  getPage: (params) => request.get('/collect/selectPage', { params }),
  getAll: (params) => request.get('/collect/selectAll', { params }),
  add: (data) => request.post('/collect/add', data),
};

// 演员相关接口
const actorApi = {
  getPage: (params) => request.get('/actor/selectPage', { params }),
  getAll: (params) => request.get('/actor/selectAll', { params }),
  add: (data) => request.post('/actor/add', data),
  update: (data) => request.put('/actor/update', data),
  delete: (id) => request.delete(`/actor/delete/${id}`),
  batchDelete: (ids) => request.delete('/actor/delete/batch', { data: ids }),
};

// 类型相关接口
const typeApi = {
  getAll: () => request.get('/type/selectAll'),
  add: (data) => request.post('/type/add', data),
  update: (data) => request.put('/type/update', data),
  delete: (id) => request.delete(`/type/delete/${id}`),
};

// 区域相关接口
const areaApi = {
  getAll: () => request.get('/area/selectAll'),
  add: (data) => request.post('/area/add', data),
  update: (data) => request.put('/area/update', data),
  delete: (id) => request.delete(`/area/delete/${id}`),
};

// 视频相关接口
const videoApi = {
  getAll: (params) => request.get('/video/selectAll', { params }),
  add: (data) => request.post('/video/add', data),
  update: (data) => request.put('/video/update', data),
  delete: (id) => request.delete(`/video/delete/${id}`),
};

// 影厅相关接口
const roomApi = {
  getPage: (params) => request.get('/room/selectPage', { params }),
  getAll: (params) => request.get('/room/selectAll', { params }),
  add: (data) => request.post('/room/add', data),
  update: (data) => request.put('/room/update', data),
  delete: (id) => request.delete(`/room/delete/${id}`),
};

// 文件上传接口
const fileApi = {
  uploadUrl: `${baseApiUrl}/files/upload`,
};

// 推荐相关接口
const recommendationApi = {
  getRecommendations: (userId) => request.get(`/recommendations/${userId}`),
};

// 年份接口
const yearApi = {
  getAll: () => request.get('/getYear'),
};

// 公告相关接口
const noticeApi = {
  getAll: () => request.get('/notice/selectAll'),
  getPage: (params) => request.get('/notice/selectPage', { params }),
  add: (data) => request.post('/notice/add', data),
  update: (data) => request.put('/notice/update', data),
  delete: (id) => request.delete(`/notice/delete/${id}`),
};

// 邮箱验证相关接口
const emailApi = {
  sendVerificationCode: (data) => request.post('/email/sendVerificationCode', data),
  verifyCode: (data) => request.post('/email/verifyCode', data),
};

export {
  baseApiUrl,
  paymentApi,
  userApi,
  filmApi,
  orderApi,
  cinemaApi,
  showApi,
  scoreApi,
  collectApi,
  actorApi,
  typeApi,
  areaApi,
  videoApi,
  roomApi,
  fileApi,
  recommendationApi,
  yearApi,
  noticeApi,
  emailApi,
}; 