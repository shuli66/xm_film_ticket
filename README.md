# 电影购票管理系统

## 项目概述

电影购票管理系统是一个基于 **SpringBoot + Vue** 的全栈 Web 应用，提供完整的电影票购买和管理服务。

![img.png](img.png)

## 技术架构

### 后端技术栈
- 核心框架：SpringBoot 3.3.1
- 数据库：MySQL 8.0
- 持久层：MyBatis + PageHelper
- 接口设计：RESTful API
- 权限管理：JWT (JSON Web Token)
- 缓存：Redis
- 邮件服务：Spring Mail

### 前端技术栈
- 框架：Vue 3
- UI 组件库：Element Plus
- 路由管理：Vue Router 4
- 状态管理：Vue 内置响应式系统
- HTTP 客户端：Axios
- 图表：ECharts
- 构建工具：Vite 5

## 系统角色设计

系统设计了三种不同的角色：

1. **普通用户 (USER)**
   - 浏览电影信息
   - 查看影院和场次信息
   - 在线选座购票
   - 支付宝支付
   - 电影评分和评论
   - 订单管理和取票
   - 电影收藏

2. **影院管理员 (CINEMA)**
   - 影厅房间管理
   - 放映场次管理
   - 订单查询和管理
   - 票务数据统计

3. **系统管理员 (ADMIN)**
   - 用户管理
   - 电影信息管理
   - 区域和分类管理
   - 影院审核
   - 评论管理
   - 系统公告发布

## 本地运行指南

### 环境要求

| 环境 | 版本 |
|------|------|
| JDK  | 17+  |
| MySQL | 8.0 |
| Redis | 6.0+ |
| Node.js | 18+ |
| Maven | 3.6+ |

### 数据库初始化

```bash
# 创建数据库并导入 Schema
mysql -uroot -p -e "CREATE DATABASE IF NOT EXISTS xm_film_ticket CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"
mysql -uroot -p xm_film_ticket < init.sql
```

### 后端启动

1. 修改 `springboot/src/main/resources/application.yml` 中的数据库密码：
   ```yaml
   spring:
     datasource:
       password: 你的MySQL密码
   ```

2. 启动 Redis（本地默认无密码即可）

3. 在 IDEA 中运行 `SpringbootApplication.java`，或执行：
   ```bash
   cd springboot
   mvn spring-boot:run
   ```

后端默认运行在 `http://localhost:9090`

### 前端启动

```bash
cd vue
npm install
npm run dev
```

前端默认运行在 `http://localhost:5173`

### 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 系统管理员 | admin | admin |
| 普通用户 | zhangsan | 123456 |

## 核心功能模块

### 电影管理模块
- 电影信息维护（片名、简介、时长、语言、分辨率等）
- 电影分类和区域管理
- 演职人员信息管理
- 影片评分系统

### 影院管理模块
- 影院信息管理（含资质审核）
- 影厅和座位管理
- 放映场次安排

### 订票系统
- 电影场次查询
- 8×8 在线可视化选座
- 支付宝沙箱支付集成
- 订单状态管理（待支付 → 待取票 → 已取票 / 已退票）

### 推荐系统
- 协同过滤推荐（余弦相似度算法）
- 基于内容的推荐（用户收藏偏好）
- 冷启动处理（热门/多样化/随机推荐）

### 用户中心
- 用户注册（邮箱验证码 / 图片验证码）
- 个人信息管理
- 订单历史查询
- 收藏管理

### 评价系统
- 电影评分（自动更新影片均分）
- 用户评论

## 数据模型

| 实体 | 说明 |
|------|------|
| User | 普通用户 |
| Admin | 系统管理员 |
| Cinema | 影院 |
| Film | 电影 |
| FilmShow | 放映场次 |
| Room | 影厅 |
| Orders | 订单 |
| Score | 评分 |
| Comment | 评论 |
| Collect | 收藏 |
| Actor | 演职人员 |
| Area | 地区 |
| Type | 电影分类 |
| Notice | 公告 |
| Video | 预告视频 |

## 业务流程

### 用户购票流程
1. 浏览电影列表 → 查看电影详情
2. 选择影院和场次
3. 可视化选座（最多4个座位）
4. 提交订单
5. 支付宝支付
6. 系统更新订单状态和票房
7. 凭订单号取票

### 影院入驻流程
1. 影院注册并提交资质认证
2. 管理员审核通过
3. 创建影厅和座位
4. 发布放映场次

## 系统特色

1. **实时选座系统**：直观的座位选择界面，显示已售和可选座位
2. **智能推荐算法**：协同过滤 + 内容推荐的混合推荐策略
3. **多角色权限管理**：基于 JWT 的三角色权限体系
4. **影院审核机制**：新增影院需要管理员审核才能上线
5. **在线支付集成**：集成支付宝沙箱支付
6. **票房统计分析**：ECharts 可视化票房数据
7. **定时任务**：自动更新电影上映/下映状态
