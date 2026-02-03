# Docker 部署说明

## 📋 项目信息

- **项目名称**: 电影票务管理系统 (xm_film_ticket)
- **前端端口**: 8081 (避免与其他项目的 80/8080 端口冲突)
- **后端端口**: 9090
- **容器名称**:
  - 前端: `xm_film_ticket_frontend`
  - 后端: `xm_film_ticket_backend`
- **Docker 网络**: `xm_film_network` (独立网络,不影响其他项目)
- **Docker 卷**:
  - `xm_film_upload_files` (文件上传)
  - `xm_film_backend_logs` (后端日志)

## 📋 部署前准备

### 1. 服务器环境要求

- **操作系统**: Ubuntu 20.04+ 或 Debian 11+
- **Docker**: 20.10+
- **Docker Compose**: 2.0+
- **MySQL**: 8.0+ (已安装并运行)
- **Redis**: 6.0+ (已安装并运行)

### 2. 配置 MySQL

确保 MySQL 允许来自 Docker 容器的连接:

```bash
# 编辑 MySQL 配置文件
sudo nano /etc/mysql/mysql.conf.d/mysqld.cnf

# 修改绑定地址
bind-address = 0.0.0.0

# 重启 MySQL
sudo systemctl restart mysql

# 授权远程访问(如果需要)
mysql -u root -p
GRANT ALL PRIVILEGES ON xm_film_ticket.* TO 'root'@'%' IDENTIFIED BY 'your_password';
FLUSH PRIVILEGES;
```

### 3. 配置 Redis

确保 Redis 允许外部连接:

```bash
# 编辑 Redis 配置文件
sudo nano /etc/redis/redis.conf

# 修改绑定地址
bind 0.0.0.0

# 设置密码(推荐)
requirepass your_redis_password

# 重启 Redis
sudo systemctl restart redis
```

## 🚀 部署步骤

### 步骤 1: 上传项目代码

将项目代码上传到服务器,或使用 git 克隆:

```bash
git clone <your-repository-url>
cd xm_film_ticket
```

### 步骤 2: 配置环境变量

```bash
# 复制环境变量模板
cp .env.example .env

# 编辑 .env 文件,填入实际密码
nano .env
```

编辑 `.env` 文件内容:

```env
MYSQL_PASSWORD=your_actual_mysql_password
REDIS_PASSWORD=your_actual_redis_password
```

### 步骤 3: 给脚本添加执行权限

```bash
chmod +x build.sh deploy.sh stop.sh
```

### 步骤 4: 部署应用

```bash
# 执行部署脚本(会自动构建镜像并启动容器)
./deploy.sh
```

部署脚本会自动完成以下操作:
1. 检查 .env 文件
2. 停止旧容器
3. 构建 Docker 镜像
4. 启动新容器
5. 显示容器状态和日志

### 步骤 5: 验证部署

在浏览器中访问:

```
http://your-server-ip:8081
```

**注意**: 前端使用 **8081** 端口,而不是 80 端口,避免与服务器上其他项目冲突。

后端 API 地址:
```
http://your-server-ip:9090
```

## 📝 常用命令

### 查看容器状态

```bash
# 查看本项目的容器
docker-compose ps

# 或者查看所有容器(包括其他项目)
docker ps

# 查看本项目的容器详细信息
docker ps | grep xm_film_ticket
```

### 查看日志

```bash
# 查看所有日志
docker-compose logs -f

# 查看后端日志
docker-compose logs -f backend

# 查看前端日志
docker-compose logs -f frontend
```

### 重启服务

```bash
# 重启所有服务
docker-compose restart

# 重启后端
docker-compose restart backend

# 重启前端
docker-compose restart frontend
```

### 停止服务

```bash
# 使用停止脚本
./stop.sh

# 或直接使用 docker-compose
docker-compose down
```

### 更新代码

```bash
# 1. 拉取最新代码
git pull

# 2. 重新部署
./deploy.sh
```

### 查看容器内部

```bash
# 进入后端容器
docker exec -it xm_film_ticket_backend sh

# 进入前端容器
docker exec -it xm_film_ticket_frontend sh
```

## 🔍 与其他 Docker 项目共存

### 端口规划

本项目使用以下端口,已避免常见冲突:
- **前端**: 8081 (而不是 80/8080)
- **后端**: 9090

如果这些端口仍然被占用,可以修改 [docker-compose.yml](docker-compose.yml):

```yaml
services:
  frontend:
    ports:
      - "8082:80"  # 改为 8082 或其他未占用端口
  backend:
    ports:
      - "9091:9090"  # 改为 9091 或其他未占用端口
```

### 检查端口占用

```bash
# 检查端口是否被占用
sudo netstat -tlnp | grep -E '8081|9090'

# 或使用 lsof
sudo lsof -i :8081
sudo lsof -i :9090

# 查看所有 Docker 容器的端口映射
docker ps --format "table {{.Names}}\t{{.Ports}}"
```

### 独立的 Docker 资源

本项目使用独立命名的资源,不会与其他项目冲突:

**网络**:
```bash
# 查看本项目的网络
docker network ls | grep xm_film

# 查看网络详情
docker network inspect xm_film_network
```

**卷**:
```bash
# 查看本项目的卷
docker volume ls | grep xm_film

# 查看卷详情
docker volume inspect xm_film_upload_files
docker volume inspect xm_film_backend_logs
```

**容器**:
```bash
# 查看本项目的容器
docker ps -a | grep xm_film_ticket

# 只操作本项目的容器
docker-compose ps
docker-compose logs
docker-compose restart
```

### 清理本项目资源

如果需要完全清理本项目(不影响其他项目):

```bash
# 停止并删除容器
docker-compose down

# 停止并删除容器、网络、卷
docker-compose down -v

# 删除镜像
docker rmi xm-film-backend:latest xm-film-frontend:latest
```

## 🔧 故障排查

### 问题 1: 后端无法连接 MySQL

**症状**: 后端日志显示数据库连接失败

**解决方案**:

```bash
# 1. 检查 MySQL 是否运行
sudo systemctl status mysql

# 2. 测试容器到宿主机的连接
docker exec -it xm_film_ticket_backend ping host.docker.internal

# 3. 检查 MySQL 绑定地址
sudo netstat -tlnp | grep 3306

# 4. 查看后端详细日志
docker-compose logs backend | grep -i mysql
```

### 问题 2: 后端无法连接 Redis

**症状**: 后端日志显示 Redis 连接失败

**解决方案**:

```bash
# 1. 检查 Redis 是否运行
sudo systemctl status redis

# 2. 测试 Redis 连接
redis-cli ping

# 3. 检查 Redis 绑定地址
sudo netstat -tlnp | grep 6379

# 4. 检查 Redis 密码配置
# 确保 .env 文件中的密码与 Redis 配置一致
```

### 问题 3: 前端无法访问后端 API

**症状**: 前端页面加载但 API 请求失败

**解决方案**:

```bash
# 1. 检查容器网络
docker network inspect xm_film_network

# 2. 测试前端到后端的连接
docker exec -it xm_film_ticket_frontend wget -O- http://backend:9090/actuator/health

# 3. 检查 Nginx 配置
docker exec -it xm_film_ticket_frontend nginx -t

# 4. 查看 Nginx 错误日志
docker exec -it xm_film_ticket_frontend cat /var/log/nginx/error.log
```

### 问题 4: 文件上传失败

**症状**: 无法上传图片或文件

**解决方案**:

```bash
# 1. 检查卷挂载
docker volume inspect xm_film_upload_files

# 2. 检查目录权限
docker exec -it xm_film_ticket_backend ls -la /usr/share/nginx/files

# 3. 测试写入权限
docker exec -it xm_film_ticket_backend touch /usr/share/nginx/files/test.txt

# 4. 如果权限不足,修改权限
docker exec -it xm_film_ticket_backend chmod 777 /usr/share/nginx/files
```

### 问题 5: 容器启动失败

**症状**: docker-compose up 失败

**解决方案**:

```bash
# 1. 查看详细错误信息
docker-compose up

# 2. 检查镜像是否构建成功
docker images | grep xm-film

# 3. 清理并重新构建
docker-compose down -v
./build.sh
docker-compose up -d

# 4. 检查端口占用
sudo netstat -tlnp | grep -E '80|9090'
```

## 📊 性能优化建议

### 1. 调整 JVM 参数

编辑 [springboot/Dockerfile](springboot/Dockerfile),修改 JAVA_OPTS:

```dockerfile
ENV JAVA_OPTS="-Xms1024m -Xmx2048m -XX:+UseG1GC"
```

### 2. 配置 Nginx 缓存

编辑 [vue/nginx.conf](vue/nginx.conf),添加缓存配置。

### 3. 数据库连接池优化

编辑 [application-docker.yml](springboot/src/main/resources/application-docker.yml),调整 Hikari 连接池参数。

## 🔒 安全建议

1. **修改默认密码**: 确保 MySQL 和 Redis 使用强密码
2. **配置防火墙**: 限制 MySQL(3306) 和 Redis(6379) 端口的访问
3. **启用 HTTPS**: 生产环境建议配置 SSL 证书
4. **定期备份**: 定期备份数据库和上传文件
5. **更新依赖**: 定期更新 Docker 镜像和系统包

## 📦 备份与恢复

### 备份上传文件

```bash
# 备份上传文件
docker run --rm -v xm_film_upload_files:/data -v $(pwd):/backup ubuntu tar czf /backup/files_$(date +%Y%m%d).tar.gz /data
```

### 恢复上传文件

```bash
# 恢复上传文件
docker run --rm -v xm_film_upload_files:/data -v $(pwd):/backup ubuntu tar xzf /backup/files_20240101.tar.gz -C /
```

## 📞 技术支持

如遇到问题,请检查:
1. Docker 和 Docker Compose 版本
2. 服务器资源(CPU、内存、磁盘)
3. 网络连接
4. 日志文件

---

**祝部署顺利! 🎉**
