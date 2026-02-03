# 多阶段构建 - 第一阶段: 构建前端
FROM node:18-alpine AS builder

# 设置工作目录
WORKDIR /app

# 复制 package.json 和 package-lock.json
COPY package*.json ./

# 安装依赖
RUN npm ci --only=production

# 复制源代码
COPY . .

# 构建生产版本
RUN npm run build

# 多阶段构建 - 第二阶段: Nginx 服务
FROM nginx:alpine

# 复制构建好的静态文件到 Nginx 目录
COPY --from=builder /app/dist /usr/share/nginx/html

# 复制 Nginx 配置文件
COPY nginx.conf /etc/nginx/nginx.conf

# 创建文件上传目录(用于访问后端上传的文件)
RUN mkdir -p /usr/share/nginx/files

# 暴露端口
EXPOSE 80

# 启动 Nginx
CMD ["nginx", "-g", "daemon off;"]
