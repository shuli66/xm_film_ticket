#!/bin/bash

# 电影票务系统 - Docker 停止脚本
# 用途: 停止所有运行中的容器

echo "========================================="
echo "  停止电影票务系统容器"
echo "========================================="
echo ""

# 停止容器
echo "正在停止容器..."
docker-compose down

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
