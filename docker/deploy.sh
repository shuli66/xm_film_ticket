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

# 检测 Docker Compose 版本
DOCKER_COMPOSE_CMD=""
if docker compose version &> /dev/null; then
    DOCKER_COMPOSE_CMD="docker compose"
    echo "✓ 检测到 Docker Compose V2"
elif command -v docker-compose &> /dev/null; then
    DOCKER_COMPOSE_CMD="docker-compose"
    echo "✓ 检测到 Docker Compose V1"
else
    echo "错误: Docker Compose 未安装"
    echo "请查看 INSTALL_DOCKER.md 获取安装指南"
    exit 1
fi
echo ""

# 检查端口占用
echo "检查端口占用情况..."
if command -v netstat &> /dev/null; then
    if netstat -tlnp 2>/dev/null | grep -q ":8081 "; then
        echo "⚠️  警告: 端口 8081 已被占用"
        echo "   请修改 docker-compose.yml 中的前端端口映射"
        read -p "   是否继续部署? (y/n): " -n 1 -r
        echo ""
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    else
        echo "✓ 端口 8081 可用"
    fi

    if netstat -tlnp 2>/dev/null | grep -q ":9090 "; then
        echo "⚠️  警告: 端口 9090 已被占用"
        echo "   请修改 docker-compose.yml 中的后端端口映射"
        read -p "   是否继续部署? (y/n): " -n 1 -r
        echo ""
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    else
        echo "✓ 端口 9090 可用"
    fi
fi
echo ""

# 停止并删除旧容器
echo "停止旧容器..."
$DOCKER_COMPOSE_CMD down 2>/dev/null || true
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
$DOCKER_COMPOSE_CMD up -d
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
$DOCKER_COMPOSE_CMD ps
echo ""

# 显示访问信息
echo "========================================="
echo "  部署完成!"
echo "========================================="
echo ""
echo "访问地址:"
echo "  前端: http://localhost:8081"
echo "  后端: http://localhost:9090"
echo ""
echo "容器名称:"
echo "  前端: xm_film_ticket_frontend"
echo "  后端: xm_film_ticket_backend"
echo ""
echo "查看日志: $DOCKER_COMPOSE_CMD logs -f"
echo "停止服务: ./stop.sh"
echo ""

# 显示日志
echo "========================================="
echo "  应用日志 (按 Ctrl+C 退出)"
echo "========================================="
echo ""
$DOCKER_COMPOSE_CMD logs -f
