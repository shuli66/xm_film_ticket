#!/bin/bash

# 电影票务系统 - Docker 部署脚本
# 用途: 自动化部署应用到服务器

set -e  # 遇到错误立即退出

echo "========================================="
echo "  电影票务系统 - Docker 部署"
echo "========================================="
echo ""

# 检查 .env 文件是否存在
if [ ! -f .env ]; then
    echo "错误: .env 文件不存在!"
    echo "请先创建 .env 文件:"
    echo "  1. 复制模板: cp .env.example .env"
    echo "  2. 编辑 .env 文件,填入实际的数据库密码"
    exit 1
fi

echo "✓ 找到 .env 配置文件"
echo ""

# 停止并删除旧容器
echo "停止旧容器..."
docker-compose down 2>/dev/null || true
echo "✓ 旧容器已停止"
echo ""

# 构建镜像
echo "构建 Docker 镜像..."
./build.sh
if [ $? -ne 0 ]; then
    echo "✗ 镜像构建失败,部署终止"
    exit 1
fi
echo ""

# 启动容器
echo "启动容器..."
docker-compose up -d
if [ $? -eq 0 ]; then
    echo "✓ 容器启动成功"
else
    echo "✗ 容器启动失败"
    exit 1
fi
echo ""

# 等待服务启动
echo "等待服务启动..."
sleep 5
echo ""

# 检查容器状态
echo "========================================="
echo "  容器状态"
echo "========================================="
docker-compose ps
echo ""

# 显示日志
echo "========================================="
echo "  应用日志 (按 Ctrl+C 退出)"
echo "========================================="
echo ""
docker-compose logs -f
