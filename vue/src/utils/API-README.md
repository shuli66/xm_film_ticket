# API接口管理使用指南

## 简介

本项目使用了集中式API管理模式，所有与后端的API交互都通过`src/utils/api.js`文件进行统一管理。这种方式有以下优点：

1. **便于维护**：后端接口路径变更时，只需修改一个地方
2. **统一管理**：所有API调用遵循相同的格式和错误处理方式
3. **代码提示**：编辑器可以提供更好的代码补全支持
4. **易于切换环境**：开发、测试、生产环境的API地址统一配置

## API模块结构

API模块按功能领域进行划分：

```javascript
import {
  baseApiUrl,      // 基础URL，可用于拼接完整地址
  userApi,         // 用户相关API
  filmApi,         // 电影相关API
  orderApi,        // 订单相关API
  cinemaApi,       // 影院相关API
  showApi,         // 场次相关API
  scoreApi,        // 评分相关API
  collectApi,      // 收藏相关API
  actorApi,        // 演员相关API
  typeApi,         // 类型相关API
  areaApi,         // 区域相关API
  videoApi,        // 视频相关API
  roomApi,         // 影厅相关API
  fileApi,         // 文件上传相关API
  recommendationApi,// 推荐相关API
  yearApi,         // 年份相关API
  paymentApi       // 支付相关API
} from '@/utils/api';
```

## 使用方法

### 1. 导入需要的API模块

只导入组件中需要使用的API模块，避免全部导入：

```javascript
import { filmApi, scoreApi } from '@/utils/api';
```

### 2. 调用API方法

所有API方法都返回Promise，可以使用async/await或then方式处理：

```javascript
// 使用async/await
async function getFilms() {
  try {
    const response = await filmApi.getPage({
      pageNum: 1,
      pageSize: 10,
      status: '已上映'
    });
    
    if (response.code === '200') {
      return response.data.list;
    } else {
      console.error(response.msg);
      return [];
    }
  } catch (error) {
    console.error('获取电影失败:', error);
    return [];
  }
}

// 使用Promise链式调用
filmApi.getPage({
  pageNum: 1,
  pageSize: 10,
  status: '已上映'
})
.then(res => {
  if (res.code === '200') {
    this.films = res.data.list;
  } else {
    console.error(res.msg);
  }
})
.catch(error => {
  console.error('获取电影失败:', error);
});
```

### 3. 文件上传

文件上传URL通过`fileApi.uploadUrl`获取：

```vue
<template>
  <el-upload
    :action="uploadUrl"
    :on-success="handleSuccess"
    list-type="picture">
    <el-button type="primary">点击上传</el-button>
  </el-upload>
</template>

<script>
import { fileApi } from '@/utils/api';

export default {
  data() {
    return {
      uploadUrl: fileApi.uploadUrl
    }
  },
  methods: {
    handleSuccess(response) {
      if (response.code === '200') {
        // 处理上传成功的逻辑
      }
    }
  }
}
</script>
```

### 4. 支付相关

使用`paymentApi.alipayUrl`获取完整的支付URL：

```javascript
import { paymentApi } from '@/utils/api';

// 打开支付页面
function payOrder(orderNo) {
  window.open(paymentApi.alipayUrl(orderNo));
}
```

## 环境配置

后端API地址在`.env.development`和`.env.production`文件中配置：

```
# 开发环境 (.env.development)
VITE_BASE_URL='http://localhost:9090'

# 生产环境 (.env.production)
VITE_BASE_URL='http://your-production-server:9090'
```

修改生产环境API地址时，只需更新`.env.production`文件中的`VITE_BASE_URL`值。

## 添加新的API

如需添加新的API接口，请按照以下步骤操作：

1. 在`src/utils/api.js`文件中找到对应的功能模块
2. 添加新的API方法
3. 确保导出新添加的API

示例：

```javascript
// 添加新的电影搜索API
const filmApi = {
  // 已有API...
  getPage: (params) => request.get('/film/selectPage', { params }),
  
  // 添加新API
  search: (keyword) => request.get('/film/search', { 
    params: { keyword } 
  }),
};
```

## 最佳实践

1. **不要直接使用字符串URL**：始终使用API模块中的方法
2. **统一错误处理**：遵循统一的错误处理模式
3. **按需导入**：只导入需要的API模块
4. **并行请求**：使用Promise.all处理多个并行API请求

```javascript
// 并行请求示例
async function loadDashboardData() {
  try {
    const [filmResponse, orderResponse] = await Promise.all([
      filmApi.getPage({ pageNum: 1, pageSize: 5 }),
      orderApi.getPage({ pageNum: 1, pageSize: 5 })
    ]);
    
    // 处理响应...
  } catch (error) {
    console.error('加载数据失败:', error);
  }
}
```

## 注意事项

1. API模块方法已经包含了与后端的完整交互逻辑，不需要再次导入`request`
2. 修改后端地址时，只需更新环境变量文件，无需修改业务代码
3. 请遵循API命名规范，保持代码一致性 