#!/bin/bash

# 电影票务系统 - Docker 停止脚本
# 用途: 停止所有运行中的容器

echo "========================================="
echo "  停止电影票务系统容器"
echo "========================================="
echo ""

# 检测 Docker Compose 版本
DOCKER_COMPOSE_CMD=""
if docker compose version &> /dev/null; then
    DOCKER_COMPOSE_CMD="docker compose"
elif command -v docker-compose &> /dev/null; then
    DOCKER_COMPOSE_CMD="docker-compose"
else
    echo "错误: Docker Compose 未安装"
    echo "请查看 INSTALL_DOCKER.md 获取安装指南"
    exit 1
fi

# 停止容器
echo "正在停止容器..."
$DOCKER_COMPOSE_CMD down

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ 所有容器已成功停止"
    echo ""
    echo "如需重新启动,请运行: ./deploy.sh"
else
    echo ""
    echo "✗ 停止容器时出现错误"
    exit 1
fi
