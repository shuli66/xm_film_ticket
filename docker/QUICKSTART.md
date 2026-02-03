# 快速部署指南

## 🚀 一键部署

```bash
# 1. 配置环境变量
cp .env.example .env
nano .env  # 填入 MySQL 和 Redis 密码

# 2. 修复脚本行尾符（如果在 Windows 上开发）
sudo apt-get install -y dos2unix
dos2unix *.sh
# 或使用: sed -i 's/\r$//' *.sh

# 3. 给脚本添加执行权限
chmod +x *.sh

# 4. 部署
./deploy.sh
```

**⚠️ 重要**: 如果遇到 `cannot execute: required file not found` 错误，请查看 [FIX_LINE_ENDINGS.md](FIX_LINE_ENDINGS.md)

## 📌 重要信息

### 访问地址
- **前端**: http://your-server-ip:8081
- **后端**: http://your-server-ip:9090

### 端口说明
- 前端使用 **8081** 端口 (避免与其他项目的 80/8080 冲突)
- 后端使用 **9090** 端口

### 容器名称
- 前端: `xm_film_ticket_frontend`
- 后端: `xm_film_ticket_backend`

### Docker 资源
- 网络: `xm_film_network`
- 卷: `xm_film_upload_files`, `xm_film_backend_logs`

## 🔧 常用命令

```bash
# 查看容器状态
docker-compose ps

# 查看日志
docker-compose logs -f

# 重启服务
docker-compose restart

# 停止服务
./stop.sh

# 更新代码
git pull && ./deploy.sh
```

## ⚠️ 端口冲突解决

如果 8081 或 9090 端口仍被占用:

### 检查端口占用
```bash
sudo netstat -tlnp | grep -E '8081|9090'
```

### 修改端口
编辑 `docker-compose.yml`:
```yaml
services:
  frontend:
    ports:
      - "8082:80"  # 改为其他端口
  backend:
    ports:
      - "9091:9090"  # 改为其他端口
```

## 🔍 故障排查

### 后端无法连接数据库
```bash
# 检查 MySQL 配置
sudo nano /etc/mysql/mysql.conf.d/mysqld.cnf
# 确保: bind-address = 0.0.0.0

# 重启 MySQL
sudo systemctl restart mysql

# 查看后端日志
docker-compose logs backend | grep -i mysql
```

### 前端无法访问
```bash
# 检查容器状态
docker ps | grep xm_film_ticket

# 检查端口
sudo netstat -tlnp | grep 8081

# 查看前端日志
docker-compose logs frontend
```

## 📦 清理资源

```bash
# 停止并删除容器
docker-compose down

# 停止并删除容器、卷
docker-compose down -v

# 删除镜像
docker rmi xm-film-backend:latest xm-film-frontend:latest
```

## 📝 环境变量配置

`.env` 文件内容:
```env
MYSQL_PASSWORD=your_mysql_password
REDIS_PASSWORD=your_redis_password
```

## 🔐 MySQL/Redis 配置

### MySQL 配置
```bash
# 编辑配置
sudo nano /etc/mysql/mysql.conf.d/mysqld.cnf

# 添加/修改
bind-address = 0.0.0.0

# 重启
sudo systemctl restart mysql
```

### Redis 配置
```bash
# 编辑配置
sudo nano /etc/redis/redis.conf

# 添加/修改
bind 0.0.0.0
requirepass your_password

# 重启
sudo systemctl restart redis
```

## 📊 监控

```bash
# 查看资源使用
docker stats xm_film_ticket_backend xm_film_ticket_frontend

# 查看日志
docker-compose logs -f --tail=100

# 查看容器详情
docker inspect xm_film_ticket_backend
```

---

详细文档请查看: [DEPLOYMENT.md](DEPLOYMENT.md)
