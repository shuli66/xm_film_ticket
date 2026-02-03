#!/bin/bash

# 电影票务系统 - Docker 镜像构建脚本
# 用途: 构建前后端 Docker 镜像

set -e  # 遇到错误立即退出

echo "========================================="
echo "  电影票务系统 - Docker 镜像构建"
echo "========================================="
echo ""

# 检查 Docker 是否安装
if ! command -v docker &> /dev/null; then
    echo "错误: Docker 未安装,请先安装 Docker"
    exit 1
fi

# 检查 Docker Compose 是否安装
if ! command -v docker-compose &> /dev/null; then
    echo "错误: Docker Compose 未安装,请先安装 Docker Compose"
    exit 1
fi

echo "开始构建 Docker 镜像..."
echo ""

# 构建后端镜像
echo "[1/2] 构建后端镜像 (Spring Boot)..."
docker build -t xm-film-backend:latest ./springboot
if [ $? -eq 0 ]; then
    echo "✓ 后端镜像构建成功"
else
    echo "✗ 后端镜像构建失败"
    exit 1
fi
echo ""

# 构建前端镜像
echo "[2/2] 构建前端镜像 (Vue + Nginx)..."
docker build -t xm-film-frontend:latest ./vue
if [ $? -eq 0 ]; then
    echo "✓ 前端镜像构建成功"
else
    echo "✗ 前端镜像构建失败"
    exit 1
fi
echo ""

echo "========================================="
echo "  镜像构建完成!"
echo "========================================="
echo ""
echo "已构建的镜像:"
docker images | grep xm-film
echo ""
echo "下一步: 运行 ./deploy.sh 部署应用"
