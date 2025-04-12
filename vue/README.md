# 电影票务系统 - 前端部分

## 项目介绍
基于Vue3和Element Plus的电影票务系统前端。

## 开发设置

### 安装依赖
```sh
npm install
```

### 开发环境运行
```sh
npm run dev
```

### 生产环境构建
```sh
npm run build
```

## API接口管理

### 接口配置
项目使用集中式API管理方案，所有API接口都在 `src/utils/api.js` 文件中配置，避免了代码中出现硬编码的URL路径，方便统一管理和修改后端接口地址。

### 后端服务器地址配置
后端服务器地址配置在环境变量文件中：
- 开发环境: `.env.development`
- 生产环境: `.env.production`

如需修改后端API服务地址，只需要修改对应环境的配置文件中的 `VITE_BASE_URL` 变量值即可。

### API使用示例
```javascript
// 引入API配置
import { API, getFullUrl } from '@/utils/api.js';

// 使用API常量发起请求
request.get(API.FILM.SELECT_ALL).then(res => {
  // 处理响应...
});

// 使用带参数的API路径
request.get(API.FILM.SELECT_BY_ID(filmId)).then(res => {
  // 处理响应...
});

// 获取完整URL（如用于window.open场景）
window.open(getFullUrl(`/alipay/pay?orderNo=${orderNo}`));
``` 