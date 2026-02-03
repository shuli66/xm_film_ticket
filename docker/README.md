# Docker 部署文件

这个目录包含了电影票务系统的所有 Docker 部署相关文件。

## 📁 文件说明

| 文件 | 说明 |
|------|------|
| [docker-compose.yml](docker-compose.yml) | Docker Compose 配置文件 |
| [.env.example](.env.example) | 环境变量模板 |
| [build.sh](build.sh) | 构建 Docker 镜像脚本 |
| [deploy.sh](deploy.sh) | 一键部署脚本 |
| [stop.sh](stop.sh) | 停止容器脚本 |
| [README_DOCKER.md](README_DOCKER.md) | Docker 部署总览 |
| [QUICKSTART.md](QUICKSTART.md) | 快速开始指南 |
| [DEPLOYMENT.md](DEPLOYMENT.md) | 详细部署文档 |
| [INSTALL_DOCKER.md](INSTALL_DOCKER.md) | Docker 安装指南 |
| [FIX_LINE_ENDINGS.md](FIX_LINE_ENDINGS.md) | 行尾符问题修复指南 |

## 🚀 快速开始

```bash
# 1. 进入 docker 目录
cd docker

# 2. 配置环境变量
cp .env.example .env
nano .env  # 填入 MySQL 和 Redis 密码

# 3. 修复脚本行尾符（如果在 Windows 上开发）
dos2unix *.sh
# 或使用: sed -i 's/\r$//' *.sh

# 4. 添加执行权限
chmod +x *.sh

# 5. 部署
./deploy.sh
```

## 📖 详细文档

- **新手入门**: 查看 [QUICKSTART.md](QUICKSTART.md)
- **完整部署**: 查看 [DEPLOYMENT.md](DEPLOYMENT.md)
- **Docker 安装**: 查看 [INSTALL_DOCKER.md](INSTALL_DOCKER.md)
- **问题修复**: 查看 [FIX_LINE_ENDINGS.md](FIX_LINE_ENDINGS.md)

## 🎯 访问地址

部署成功后:
- **前端**: http://your-server-ip:8081
- **后端**: http://your-server-ip:9090

## 📝 常用命令

```bash
# 查看容器状态
docker compose ps

# 查看日志
docker compose logs -f

# 重启服务
docker compose restart

# 停止服务
./stop.sh
```

## ⚠️ 注意事项

1. 所有脚本都需要在 `docker` 目录下执行
2. 确保 MySQL 和 Redis 已在服务器上运行
3. 端口 8081 和 9090 不能被占用
4. 需要先创建 `.env` 文件并配置密码

---

**返回项目根目录**: `cd ..`
